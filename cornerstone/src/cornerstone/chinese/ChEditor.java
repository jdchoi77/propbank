/**
* Copyright (c) 2009, Regents of the University of Colorado
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
package cornerstone.chinese;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import cornerstone.english.EnLib;
import cornerstone.toolkit.*;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class ChEditor extends EditorTemplate implements ActionListener
{
	private ChEditorMenuBar mn_bar;
	private ChVerb          ch_verb = null;
	private int             i_lastFramesetID = 0;
	
	/**
	 * Initializes the editor.
	 * @param title title of the frame
	 * @param userId user ID (e.g. choijd)
	 * @param language language ("ch", "ar")
	 */
	public ChEditor(String title, String userId, String language)
	{
		super(750, 750, title, userId, language);
		init();
		
		setLayout(new BorderLayout());
		setBounds(0, 0, WIDTH+30, HEIGHT);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void init()
	{
		mn_bar = new ChEditorMenuBar(this);
		setJMenuBar(mn_bar);
		
		ChLib.ARR_N   = getArray(SYS_PATH+LANGUAGE+"."+ChLib.N);
		ChLib.ARR_F   = getArray(SYS_PATH+LANGUAGE+"."+ChLib.F);
		ChLib.ARR_SRC = getArray(SYS_PATH+LANGUAGE+"."+ChLib.SRC);
		ChLib.ARR_TRG = getArray(SYS_PATH+LANGUAGE+"."+ChLib.TRG);
	}
	
	// ---------------------------------- menu*() ----------------------------------
	
	/**
	 * Initializes menu actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if      (e.getSource() == mn_bar.fileNew)		menuNew();
		else if (e.getSource() == mn_bar.fileOpen)		menuOpen();
		else if (e.getSource() == mn_bar.fileQuit)		menuQuit();
		else if (CURR_FILE    == null)					;
		else if (e.getSource() == mn_bar.fileSave)		menuSave();
		else if (e.getSource() == mn_bar.fileSaveAs)	menuSaveAs();
		
		else if (e.getSource() == mn_bar.editAddFrameset)		menuAddFrameset();
		else if (e.getSource() == mn_bar.editEditFrameset)		menuEditFrameset();
		else if (e.getSource() == mn_bar.editRemoveFrameset)	menuRemoveFrameset();
		
		else if (e.getSource() == mn_bar.editAddFrame)	menuAddFrame();
		else if (e.getSource() == mn_bar.editAddRole)	menuAddRole();
		
		else if (e.getSource() == mn_bar.movePrevRoleset)	menuPrevFrameset();
		else if (e.getSource() == mn_bar.moveNextRoleset)	menuNextFrameset();
	}
	
	// ---------------------------------- menu.file*() ----------------------------------
	
	/** Creates a new frameset file. */
	public void menuNew()
	{
		String filename = new JDCFileDialog(this, CURR_DIR).save("New file");
		
		if (filename != null)
		{
			open(SYS_PATH+LANGUAGE+".xml");
			updateTitle(filename);			
			addFrameset();
		}
	}
	
	/** Prompts an input dialog and opens a frameset file. */
	public void menuOpen()
	{
		String filename = new JDCFileDialog(this, CURR_DIR).loadFile("Open file");
		
		if (filename != null)
		{
			open(filename);
			updateTitle(filename);
		}
	}
	
	/** Saves the frameset file. */
	public void menuSave()
	{
		ch_verb.save();
		saveDir();
		
		try
		{
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer        trans     = transFact.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			Source      src  = new DOMSource(ch_verb.getElement());
			PrintStream fout = new PrintStream(new FileOutputStream(CURR_FILE));
			
			fout.println("<!DOCTYPE verb SYSTEM \"verb.dtd\">");
			StreamResult dest = new StreamResult(fout);
	        trans.transform(src, dest);
		}
		catch (Exception e) {System.err.println(e);}
	}
	
	/** Prompts an iput dialog and saves the frameset file. */
	public void menuSaveAs()
	{
		String filename = new JDCFileDialog(this, CURR_DIR).save("Save As");
		
		if (filename != null)
		{
			updateTitle(filename);
			menuSave();
		}
	}
	
	/** Quits the editor. */
	public void menuQuit()
	{
		System.exit(0);
	}
	
	/**
	 * Opens {@link fileaname}.
	 * @param filename name of the file to open
	 */
	private void open(String filename)
	{
		try
		{
			// remove previous verb
			if (ch_verb != null)	remove(ch_verb);
			// <verb> is the root of the document
			doc = d_builder.parse(new File(filename));
			Element eVerb = doc.getDocumentElement();
			// add new frameset
			ch_verb = new ChVerb(eVerb);
			add(ch_verb, BorderLayout.CENTER);
			// validate new frameset
			validate();
			CURR_DIR = filename.substring(0, filename.lastIndexOf('/')+1);
			saveDir();
			setLastFramesetID(eVerb);
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	private void setLastFramesetID(Element eVerb)
	{
		NodeList list = eVerb.getElementsByTagName(ChLib.FRAMESET);
		int max = 0;
		
		for (int i=0; i<list.getLength(); i++)
		{
			Element eFrameset = (Element)list.item(i);
			try
			{
				int num = Integer.parseInt(eFrameset.getAttribute(ChLib.ID).substring(1));
				if (num > max)	max = num;
			}
			catch (Exception e) {}
		}
		
		i_lastFramesetID = max;
	}
	
	// ---------------------------------- menu*Frameset()  ----------------------------------

	private void menuAddFrameset()
	{
	/*	String id = showInputDialog("Enter a frameset id", "Add Frameset", "f");
		if (id == null)	return;

		if (ch_verb.existFrameset(id))
			showErrorDialog("'"+id+"' already exists");
		else
			addFrameset(id);*/
		
		addFrameset();
	}
	
	private void addFrameset()
	{
		Element eFrameset = createEmptyFrameset("f"+(++i_lastFramesetID));
		
		ch_verb.appendChild(eFrameset);
		ch_verb.addFrameset(eFrameset);
	}
	
	private void menuEditFrameset()
	{
		if (ch_verb.existFrameset())
		{
			String id = showInputDialog("Enter a frameset id", "Edit Frameset ID", ch_verb.getCurrFrameset().id);
			if (id == null)	return;
			
			if (ch_verb.existFrameset(id))
				showErrorDialog("'"+id+"' already exists");
			else
				ch_verb.editFramesetId(id);
		}
	}
	
	private void menuRemoveFrameset()
	{
		if (ch_verb.existFrameset())
			ch_verb.removeFrameset();
	}
	
	// ---------------------------------- menu*Role()  ----------------------------------
	
	private void menuAddFrame()
	{
		if (ch_verb.existFrameset())
		{
			ChFrameset chFrameset = ch_verb.getCurrFrameset();
			Element eFrame = createEmptyFrame();
			
			chFrameset.appendChild(eFrame);
			chFrameset.addFrame(eFrame);
			chFrameset.validate();
		}
	}
	
	private void menuAddRole()
	{
		if (ch_verb.existFrameset())
		{
			ChFrameset chFrameset = ch_verb.getCurrFrameset();
			Element eRole = createEmptyRole();
			
			chFrameset.appendChild(eRole);
			chFrameset.addRole(eRole);
			chFrameset.validate();
		}
	}
	
	// ---------------------------------- menuPrev|Next*()  ----------------------------------
	
	private void menuPrevFrameset()
	{
		if (ch_verb.existFrameset())
			ch_verb.selectPrevFrameset();
	}
	
	private void menuNextFrameset()
	{
		if (ch_verb.existFrameset())
			ch_verb.selectNextFrameset();
	}

	// ---------------------------------- create*() ----------------------------------
	
	/**
	 * Creates an empty id XML element.
	 * @return empty id XML element
	 */
	static public Element createEmptyId()
	{
		return createElement(ChLib.ID);
	}
	
	/**
	 * Creates an empty frameset XML element with {@link id}.
	 * @param id frameset ID
	 * @return empty frameset XML element
	 */
	static public Element createEmptyFrameset(String id)
	{
		Element eFrameset = createElement(ChLib.FRAMESET);
		
		eFrameset.setAttribute(ChLib.ID, id);
		eFrameset.appendChild(createEmptyRole());
		eFrameset.appendChild(createEmptyFrame());
		
		return eFrameset;
	}
	
	/**
	 * Creates an empty role XML element.
	 * @return empty role XML element
	 */
	static public Element createEmptyRole()
	{
		return createElement(ChLib.ROLE);
	}
	
	/**
	 * Creates an empty frame XML element.
	 * @return empty frame XML element
	 */
	static public Element createEmptyFrame()
	{
		Element eFrame = createElement(ChLib.FRAME);
		eFrame.appendChild(createEmptyMapping());
		eFrame.appendChild(createEmptyExample());
		
		return eFrame; 
	}
	
	/**
	 * Creates an empty mapping XML element.
	 * @return empty mapping XML element
	 */
	static public Element createEmptyMapping()
	{
		return createElement(ChLib.MAPPING);
	}
	
	/**
	 * Creates an empty example XML element.
	 * @return empty example XML element
	 */
	static public Element createEmptyExample()
	{
		Element eExample = createElement(ChLib.EXAMPLE);
		eExample.appendChild(createEmptyParse());
		
		return eExample;
	}
	
	/**
	 * Creates an empty parse XML element.
	 * @return empty parse XML element
	 */
	static public Element createEmptyParse()
	{
		return createElement(ChLib.PARSE);
	}
	
	/**
	 * Creates an empty mapitem XML element.
	 * @return empty mapitem XML element
	 */
	static public Element createEmptyMapitem()
	{
		return createElement(ChLib.MAPITEM);
	}
	
	/**
	 * Creates an empty argument XML element.
	 * @return empty argument XML element
	 */
	static public Element createEmptyArg()
	{
		return createElement(ChLib.ARG);
	}
	
	/**
	 * Creates an empty v XML element.
	 * @return empty v XML element
	 */
	static public Element createEmptyV()
	{
		return createElement(ChLib.V);
	}
	
	/**
	 * Creates a comment XML element.
	 * @param content content of the comment
	 * @return comment XML element
	 */
	static public Element createComment(String content)
	{
		Element eNote = createElement(ChLib.COMMENT);
		eNote.setTextContent(content.trim());
		
		return eNote; 
	}
}
