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

import java.io.*;
import java.util.*;

import jubilee.treebank.*;

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
	/**
	 * argument-joiner used in annotation files.
	 */
	static public final String ARG_JOINER = "-";
	/**
	 * annotation functions.
	 */
	static public final String ANT_FUNC = "*,;&";
	/**
	 * argument tag for relation.
	 */
	static public String REL = "rel";
	
	private Vector<String> vec_prefix;		// treebank-path s# t#_of_predicate
	private Vector<String> vec_annotator;
	private Vector<String> vec_lemma;
	private Vector<String> vec_extra;
	private Vector<TBTree> vec_tree;
	private int i_currIndex;				// index of the current tree
	
	/**
	 * Opens 'annotationFile', finds trees from 'treebankPath', and collects information.
	 * @param annotationFile the path of the annotation file.
	 * @param treebankPath the path of the treebank.
	 */
	public PBReader(String annotationFile, String treebankPath)
	{
		vec_prefix = new Vector<String>();
		vec_annotator = new Vector<String>();
		vec_lemma = new Vector<String>();
		vec_extra = new Vector<String>();
		vec_tree = new Vector<TBTree>();
		i_currIndex = 0;
		
		try
		{
			Scanner scan = new Scanner(new File(annotationFile));
			
			while (scan.hasNextLine())
				tokenizeLine(scan.nextLine(), treebankPath);
			
			scan.close();
		}
		catch (IOException e) {System.err.println(e);}
	}
		
	private void tokenizeLine(String line, String treebankPath)
	{
		StringTokenizer tok = new StringTokenizer(line);
		if (!tok.hasMoreTokens())	return;		// ignore empty line
		
		String filename = tok.nextToken();						// treebank-path
		int sentenceIdx = Integer.parseInt(tok.nextToken());	// s#
		int predicateIdx = Integer.parseInt(tok.nextToken());	// t#_of_predicate
		vec_prefix.add(filename+" "+sentenceIdx+" "+predicateIdx);
		
		// get the tree
		TBReader tbank = new TBReader(treebankPath+"/"+filename);
		TBTree tree = null;
		
		for (int i=0; i<=sentenceIdx; i++)			// reach the tree
			tree = tbank.nextTree();
		
		// add 'rel' as an argument to the predicate
		tree.moveToTerminal(predicateIdx);
		tree.setArg(predicateIdx+":0", REL);
		
		String annotator = tok.nextToken();			// annotator
		vec_annotator.add(annotator);
		String predicateLemma = tok.nextToken();	// predicate_lemma
		vec_lemma.add(predicateLemma);
		String extra = tok.nextToken();				// -----
		vec_extra.add(extra);
		
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
				
				tree.moveTo(terminalIdx, height);
				loc += simbol + terminalIdx + ":" + height;
				tree.setArg(loc, arg);
				
				if (tok_description.hasMoreTokens())		// get symbol
					simbol = tok_description.nextToken();
			}
		}
		
		vec_tree.add(tree);
	}
	
	/**
	 * Saves the propbank to 'filename' as an annotation file.
	 * @param filename the name of the file to be saved.
	 */
	public void toFile(String filename)
	{
		PrintStream fout = null;	String str;
		
		try
		{
			fout = new PrintStream(new FileOutputStream(filename));
		}
		catch (IOException ee) {System.err.println(ee);}
		
		for (int i=0; i<getSize(); i++)
		{
			str = vec_prefix.get(i);
			str += " " + vec_annotator.get(i);
			str += " " + vec_lemma.get(i);
			str += " " + vec_extra.get(i);
			str += " " + vec_tree.get(i).toPropbank();
			
			fout.println(str);
		}
	}

	/**
	 * Gets the size of the propbank.
	 * @return the size of the propbank.
	 */
	public int getSize()
	{
		return vec_tree.size();
	}
	
	/**
	 * Gets the current prefix.
	 * @return the current prefix (treebank-path s# t#_of_predicate). 
	 */
	public String getPrefix()
	{
		return vec_prefix.get(i_currIndex);
	}
	
	/**
	 * Gets the current annotator.
	 * @return the current annotator.
	 */
	public String getAnnotator()
	{
		return vec_annotator.get(i_currIndex);
	}
	
	/**
	 * Gets the current predicate lemma.
	 * @return the current predicate lemma
	 */
	public String getLemma()
	{
		return vec_lemma.get(i_currIndex);
	}
	
	/**
	 * Gets the current extra (-----).
	 * @return the current extra.
	 */
	public String getExtra()
	{
		return vec_extra.get(i_currIndex);
	}
	
	/**
	 * Gets the current tree in treebank format.
	 * @return the current tree.
	 */
	public TBTree getTBTree()
	{
		return vec_tree.get(i_currIndex);
	}
	
	/**
	 * Gets the current index.
	 * @return the current index.
	 */
	public int getIndex()
	{
		return i_currIndex;
	}
	
	/**
	 * Sets the current index.
	 * @param index the current index to be set.
	 */
	public void setIndex(int index)
	{
		i_currIndex = index;
	}
	
	/**
	 * Sets the current predicate lemma.
	 * @param lemma the current lemma to be set.
	 */
	public void setLemma(String lemma)
	{
		vec_lemma.set(i_currIndex, lemma);
	}
	
	/**
	 * Sets the current annotator.
	 * @param annotator the current annotator.
	 */
	public void setAnnotator(String annotator)
	{
		vec_annotator.set(i_currIndex, annotator);
	}
	
	public void copyCurrent(PBReader pb)
	{
		vec_prefix.set(pb.getIndex(), pb.getPrefix());
		vec_lemma.set(pb.getIndex(), pb.getLemma());
		vec_extra.set(pb.getIndex(), pb.getExtra());
		vec_tree.set(pb.getIndex(), pb.getTBTree().clone());
	}
}
