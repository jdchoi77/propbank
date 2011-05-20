/**
* Copyright (c) 2007-2009, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package jubilee.propbank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import jdsl.core.ref.NodeTree;
import jubilee.hindi.HDUtil;
import jubilee.toolkit.JBToolkit;
import jubilee.treebank.TBReader;
import jubilee.treebank.TBTree;
/**
 * 'PBReader' reads a Propbank annotation file and stores all information from both treebank 
 * and annotation into vectors.
 * It also provides operations to access and manage the information.
 * The following show the annotation format.
 * <pre>
 * TERMS:
 * s# = sentence number of the tree to be extracted
 * t# = terminal number (start from 0)
 * a# = ancestor number (start from 0)
 * i.e. t#:a# = a#'th ancestor from the t#'th terminal
 *      3:0 = 0th ancestor from the 4th terminal = 4th terminal
 *      0:1 = 1st ancestor from the 1st terminal
 * tr = * (trace)
 * jo = , (join)
 * sc = ; (?)
 * ARG = argument tag
 * 
 * FORMAT:
 * treebank-path s# t#_of_predicate annotator predicate_lemma -----  [t#:a#[tr|jo|sct#:a#]*-ARG]+
 * i.e.
 * ebn/ebn_0001.mrg 0 10 gold keep.XX -----  5:1*9:0-ARG0 10:0-rel 11:1,13:2-ARG1
 * </pre>
 * @since 09/13/07
 */
public class PBReader
{
	/** argument-joiner used in annotation files */
	static public final String ARG_JOINER = "-";
	/** annotation functions */
	static public final String ANT_FUNC = "*,;&";
	/** argument tag for relation */
	static public String REL = "rel";
	
	private ArrayList<PBInstance>	ls_instance;
	private TBReader				tb_reader;
	private TBTree                  p_tree;
	private String					s_treeDir;
	private int						i_currIdx;		// index of the current tree
	private HashMap<String,String>	m_context;
	private int						i_prevTreeId;
	private String					s_prevTreePath;
		
	/**
	 * Opens 'annotationFile', finds trees from 'treebankPath', and collects information.
	 * @param annotationFile the path of the annotation file.
	 * @param treebankDir the path of the treebank.
	 */
	public PBReader(String annotationFile, String treebankDir)
	{
		ls_instance  = new ArrayList<PBInstance>();
		s_treeDir    = treebankDir + File.separator;
		i_currIdx    = 0;
		m_context    = new HashMap<String, String>();
		i_prevTreeId = -1;
		s_prevTreePath = "";
		
		try
		{
			Scanner scan = new Scanner(new File(annotationFile));
			
			while (scan.hasNextLine())
				tokenizeLine(scan.nextLine());
			
			scan.close();
		}
		catch (IOException e) {System.err.println(e);}
	}
		
	private void tokenizeLine(String line)
	{
		StringTokenizer tok = new StringTokenizer(line);
		if (!tok.hasMoreTokens())	return;		// ignore empty line
		
		PBInstance instance = new PBInstance();
		
		instance.treePath = tok.nextToken();
		instance.treeId   = Integer.parseInt(tok.nextToken());
		instance.predId   = Integer.parseInt(tok.nextToken());
		
		// get the tree
		if (instance.treeId < i_prevTreeId || !instance.treePath.equals(s_prevTreePath))
		{
			tb_reader = new TBReader(s_treeDir + instance.treePath);
			i_prevTreeId   = -1;
			s_prevTreePath = instance.treePath;
		}
		
		if (!m_context.containsKey(s_prevTreePath))
		{
			TBReader reader = new TBReader(s_treeDir + s_prevTreePath);
			TBTree   tree;
			StringBuilder build = new StringBuilder();
			
			for (int i=0; (tree = reader.nextTree()) != null; i++)
			{
				build.append(tree.toSentence(false));
				build.append("\n");
			}
			
			m_context.put(s_prevTreePath, build.toString().trim());
		}
		
		for (int i=i_prevTreeId; i<instance.treeId; i++)
			p_tree = tb_reader.nextTree();
		
		TBTree pTree = p_tree.clone();
		i_prevTreeId = instance.treeId;
		
		// add 'rel' as an argument to the predicate
		pTree.moveToTerminal(instance.predId);
		pTree.setArg(instance.predId+":0", REL);
		
		instance.annotator = tok.nextToken();
		instance.type      = tok.nextToken();
		instance.roleset   = tok.nextToken();
		instance.aspect    = tok.nextToken();
		instance.pTree     = pTree;
		
		while (tok.hasMoreTokens())
		{
			StringTokenizer tok_termInfo = new StringTokenizer(tok.nextToken(), ARG_JOINER);
			StringTokenizer tok_description = new StringTokenizer(tok_termInfo.nextToken(), ":"+ANT_FUNC, true);
			
			String arg = tok_termInfo.nextToken();	// get argument type
			while (tok_termInfo.hasMoreTokens())
				arg += "-" + tok_termInfo.nextToken();
			
			String simbol = "", loc = "";
			while (tok_description.hasMoreTokens())
			{
				int terminalIdx = Integer.parseInt(tok_description.nextToken());
				tok_description.nextToken();	// :
				int height = Integer.parseInt(tok_description.nextToken());
				
				pTree.moveTo(terminalIdx, height);
				loc += simbol + terminalIdx + ":" + height;
				pTree.setArg(loc, arg);
				
				if (tok_description.hasMoreTokens())		// get symbol
					simbol = tok_description.nextToken();
			}
		}
		
		if (JBToolkit.s_language.equals("hindi"))
		{
			instance.dTree = HDUtil.getTree(pTree.getRootNode());
			HDUtil.cleanTree(pTree.getRootNode());
			
			pTree.moveTo(instance.predId, 1);
			instance.predPos = pTree.getPos();
		}
		
		ls_instance.add(instance);
	}
	
	/**
	 * Saves the propbank to 'filename' as an annotation file.
	 * @param filename the name of the file to be saved.
	 */
	public void toFile(String filename)
	{
		PrintStream fout = null;
		
		try
		{
			fout = new PrintStream(new FileOutputStream(filename));
		}
		catch (IOException ee) {System.err.println(ee);}

		StringBuilder build = new StringBuilder();
		
		for (PBInstance instance : ls_instance)
		{
			build.append(instance.toString());
			build.append("\n");
		}
		
		fout.println(build.toString().trim());
	}

	/**
	 * Gets the size of the propbank.
	 * @return the size of the propbank.
	 */
	public int getSize()
	{
		return ls_instance.size();
	}
	
	public PBInstance getInstance()
	{
		return ls_instance.get(i_currIdx);
	}
	
	/**
	 * Gets the current annotator.
	 * @return the current annotator.
	 */
	public String getAnnotator()
	{
		return ls_instance.get(i_currIdx).annotator;
	}
	
	public String getType()
	{
		return ls_instance.get(i_currIdx).type;
	}
	
	/**
	 * Gets the current predicate lemma.
	 * @return the current predicate lemma
	 */
	public String getRoleset()
	{
		return ls_instance.get(i_currIdx).roleset;
	}
	
	/**
	 * Gets the current extra (-----).
	 * @return the current extra.
	 */
	public String getAspect()
	{
		return ls_instance.get(i_currIdx).aspect;
	}
	
	/**
	 * Gets the current tree in treebank format.
	 * @return the current tree.
	 */
	public TBTree getTBTree()
	{
		return ls_instance.get(i_currIdx).pTree;
	}
	
	public NodeTree getDepTree()
	{
		return ls_instance.get(i_currIdx).dTree;
	}
	
	public String getPredPos()
	{
		return ls_instance.get(i_currIdx).predPos;
	}
	
	/**
	 * Gets the current index.
	 * @return the current index.
	 */
	public int getIndex()
	{
		return i_currIdx;
	}
	
	public String getContexts()
	{
		return m_context.get(getInstance().treePath);
	}
	
	/**
	 * Sets the current index.
	 * @param index the current index to be set.
	 */
	public void setIndex(int index)
	{
		i_currIdx = index;
	}
	
	/**
	 * Sets the current predicate roleset.
	 * @param roleset the current roleset to be set.
	 */
	public void setRoleset(String roleset)
	{
		ls_instance.get(i_currIdx).roleset = roleset;
	}
	
	/**
	 * Sets the current annotator.
	 * @param annotator the current annotator.
	 */
	public void setAnnotator(String annotator)
	{
		ls_instance.get(i_currIdx).annotator = annotator;
	}
	
	public void copyCurrent(PBReader pb)
	{
		ls_instance.set(pb.getIndex(), pb.getInstance());
	}
}
