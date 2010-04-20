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
package cornerstone.english;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import cornerstone.toolkit.*;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class EnEditor extends EditorTemplate implements ActionListener
{
	private EnEditorMenuBar mn_bar;
	private EnFrameset      en_frameset = null;
	private int             i_lastRolesetID = 0;
	
	/**
	 * Initializes the editor.
	 * @param title title of the frame
	 * @param userId user ID (e.g. choijd)
	 * @param language language ("en")
	 */
	public EnEditor(String title, String userId, String language)
	{
		super(800, 750, title, userId, language);
		init();
		
		setLayout(new BorderLayout());
		setBounds(0, 0, WIDTH, HEIGHT);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void init()
	{
		mn_bar = new EnEditorMenuBar(this);
		setJMenuBar(mn_bar);
		
		EnLib.ARR_N       = getArray(SYS_PATH+LANGUAGE+"."+EnLib.N);
		EnLib.ARR_F       = getArray(SYS_PATH+LANGUAGE+"."+EnLib.F);
		EnLib.ARR_VNTHETA = getArray(SYS_PATH+LANGUAGE+"."+EnLib.VNTHETA);
		EnLib.ARR_PERSON  = getArray(SYS_PATH+LANGUAGE+"."+EnLib.PERSON);
		EnLib.ARR_TENSE   = getArray(SYS_PATH+LANGUAGE+"."+EnLib.TENSE);
		EnLib.ARR_ASPECT  = getArray(SYS_PATH+LANGUAGE+"."+EnLib.ASPECT);
		EnLib.ARR_VOICE   = getArray(SYS_PATH+LANGUAGE+"."+EnLib.VOICE);
		EnLib.ARR_FORM    = getArray(SYS_PATH+LANGUAGE+"."+EnLib.FORM);
		
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_HI))
		{			
			EnLib.ARR_DREL  = getArray(SYS_PATH+LANGUAGE+"."+EnLib.DREL);
			EnLib.ARR_VTYPE = getArray(SYS_PATH+LANGUAGE+"."+EnLib.VTYPE);
		}
	}

	// ---------------------------------- menu*() ----------------------------------
	
	public void actionPerformed(ActionEvent e)
	{
		if      (e.getSource() == mn_bar.fileNew)		menuNew();
		else if (e.getSource() == mn_bar.fileOpen)		menuOpen();
		else if (e.getSource() == mn_bar.fileQuit)		menuQuit();
		else if (CURR_FILE     == null)					;
		else if (e.getSource() == mn_bar.fileSave)		menuSave();
		else if (e.getSource() == mn_bar.fileSaveAs)	menuSaveAs();
		
		else if (e.getSource() == mn_bar.editAddPredicate)		menuAddPredicate();
		else if (e.getSource() == mn_bar.editEditPredicate)		menuEditPredicate();
		else if (e.getSource() == mn_bar.editRemovePredicate)	menuRemovePredicate();
		
		else if (e.getSource() == mn_bar.editAddRoleset)		menuAddRoleset();
		else if (e.getSource() == mn_bar.editEditRoleset)		menuEditRoleset();
		else if (e.getSource() == mn_bar.editRemoveRoleset)		menuRemoveRoleset();
		
		else if (e.getSource() == mn_bar.editAddRole)			menuAddRole();
		else if (e.getSource() == mn_bar.editEditExample)		menuEditExample();
		
		else if (e.getSource() == mn_bar.movePrevPredicate)		menuPrevPredicate();
		else if (e.getSource() == mn_bar.moveNextPredicate)		menuNextPredicate();
		else if (e.getSource() == mn_bar.movePrevRoleset)		menuPrevRoleset();
		else if (e.getSource() == mn_bar.moveNextRoleset)		menuNextRoleset();
	}
	
	// ---------------------------------- menu.file*() ----------------------------------
	
	/** Creates a new frameset file. */
	public void menuNew()
	{
		if (CURR_FILE != null)	menuSave();
		String filename = new JDCFileDialog(this, CURR_DIR).save("New file");
		
		if (filename != null)
		{
			String dir = filename.substring(0, filename.lastIndexOf(File.separator)+1);
			open(SYS_PATH+LANGUAGE+".xml", dir);
			updateTitle(filename);
			
			String lemma = getLemma();
			addPredicate(lemma);
			addRoleset();
		}
	}
	
	/** Prompts an input dialog and opens a frameset file. */
	public void menuOpen()
	{
		if (CURR_FILE != null)	menuSave();
		String filename = new JDCFileDialog(this, CURR_DIR).loadFile("Open file");
		
		if (filename != null)
		{
			String dir = filename.substring(0, filename.lastIndexOf(File.separator)+1);
			open(filename, dir);
			updateTitle(filename);
		}
	}
	
	/** Saves the frameset file. */
	private void menuSave()
	{
		en_frameset.save();
		saveDir();
		
		try
		{
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer        trans     = transFact.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			Source      src  = new DOMSource(en_frameset.getElement());
			PrintStream fout = new PrintStream(new FileOutputStream(CURR_FILE));
			
			fout.println("<!DOCTYPE frameset SYSTEM \"frameset.dtd\">");
			StreamResult dest = new StreamResult(fout);
	        trans.transform(src, dest);
		}
		catch (Exception e) {System.err.println(e);}
	}
	
	/** Saves the frameset file. */
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
	private void menuQuit()
	{
		System.exit(0);
	}
	
	/**
	 * Opens {@link fileaname}.
	 * @param filename name of the file to open
	 */
	private void open(String filename, String dir)
	{
		try
		{
			// remove previous frameset
			if (en_frameset != null)	remove(en_frameset);
			// <frameset> is the root of the document
			doc = d_builder.parse(new File(filename));
			Element eFrameset = doc.getDocumentElement();
			// add new frameset
			en_frameset = new EnFrameset(eFrameset);
			add(en_frameset, BorderLayout.CENTER);
			// validate new frameset
			validate();
			CURR_DIR = dir;
			saveDir();
			setLastRolesetID(eFrameset);
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	private void setLastRolesetID(Element eFrameset)
	{
		NodeList list = eFrameset.getElementsByTagName(EnLib.ROLESET);
		int max = 0;
		
		for (int i=0; i<list.getLength(); i++)
		{
			Element eRoleset = (Element)list.item(i);
			try
			{
				String id = eRoleset.getAttribute(EnLib.ID);
				int num = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
				if (num > max)	max = num;
			}
			catch (Exception e) {}
		}
		
		i_lastRolesetID = max;
	}
	
	// ---------------------------------- menu*Predicate() ----------------------------------
	
	private void menuAddPredicate()
	{
		String lemma = showInputDialog("Enter a predicate lemma", "Add Predicate", getLemma());
		if (lemma == null)	return;

		lemma = lemma.replace(' ', '_');
		if (en_frameset.existPredicate(lemma))
			showErrorDialog("'"+lemma+"' already exists");
		else
		{
			addPredicate(lemma);
			addRoleset();
		}
	}
	
	private void addPredicate(String lemma)
	{
		Element ePredicate = createEmptyPredicate(lemma);
		
		en_frameset.appendChild(ePredicate);
		en_frameset.addPredicate(ePredicate);
	}
	
	private void menuEditPredicate()
	{
		if (en_frameset.existPredicate())
		{
			String lemma = showInputDialog("Enter a predicate lemma", "Edit Predicate", en_frameset.getCurrPredicate().lemma);
			if (lemma == null)	return;

			lemma = lemma.replace(' ', '_');
			if (en_frameset.existPredicate(lemma))
				showErrorDialog("'"+lemma+"' already exists");
			else
			{
				en_frameset.editPredicate(lemma);
				if (en_frameset.getCurrPredicateIndex() == 0)
				{
					en_frameset.editRolesetIds(lemma);
					updateTitle(CURR_FILE.substring(0, CURR_FILE.lastIndexOf(File.separator)+1) + lemma + ".xml");
				}
			}
		}
	}
	
	private void menuRemovePredicate()
	{
		if (en_frameset.existPredicate())
			en_frameset.removePredicate();
	}
	
	// ---------------------------------- menu*Roleset()  ----------------------------------
	
	private void menuAddRoleset()
	{
		if (en_frameset.existPredicate())
		{
		/*	String id = showInputDialog("Enter a roleset id", "Add Roleset", getLemma()+".");
			if (id == null)	return;
			
			if (en_frameset.existRoleset(id))
				showErrorDialog("'"+id+"' already exists");
			else
				addRoleset(id);*/
			
			addRoleset();
		}
	}
	
	private void addRoleset()
	{
		i_lastRolesetID++;
		String id = (i_lastRolesetID < 10) ? "0"+i_lastRolesetID : ""+i_lastRolesetID;
		
		EnPredicate enPredicate = en_frameset.getCurrPredicate();
		Element     eRoleset    = createEmptyRoleset(getLemma()+"."+id);
		
		enPredicate.appendChild(eRoleset);
		enPredicate.addRoleset(eRoleset);
	}
	
	private void menuEditRoleset()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
		{
			String id = showInputDialog("Enter a roleset id", "Edit Roleset ID", en_frameset.getCurrPredicate().getCurrRoleset().id);
			if (id == null)	return;
			
			if (en_frameset.existRoleset(id))
				showErrorDialog("'"+id+"' already exists");
			else
				en_frameset.getCurrPredicate().editRolesetId(id);
		}
	}
	
	private void menuRemoveRoleset()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
			en_frameset.getCurrPredicate().removeRoleset();
	}
	
	// ---------------------------------- menu*Role()  ----------------------------------
	
	private void menuAddRole()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
		{
			EnRoles enRoles = en_frameset.getCurrPredicate().getCurrRoleset().getRoles();
			Element eRole   = createEmptyRole();
			
			enRoles.appendChild(eRole);
			enRoles.addRole(eRole);
			enRoles.validate();
		}
	}

	/**
	 * Shows example frame.
	 * @see EnRoleset#showExamples()
	 */
	private void menuEditExample()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
			en_frameset.getCurrPredicate().getCurrRoleset().showExamples();
	}
	
	// ---------------------------------- menuPrev|Next*()  ----------------------------------
	
	private void menuPrevPredicate()
	{
		if (en_frameset.existPredicate())
			en_frameset.selectPrevPredicate();
	}
	
	private void menuNextPredicate()
	{
		if (en_frameset.existPredicate())
			en_frameset.selectNextPredicate();
	}
	
	private void menuPrevRoleset()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
			en_frameset.getCurrPredicate().selectPrevRoleset();
	}
	
	private void menuNextRoleset()
	{
		if (en_frameset.existPredicate() && en_frameset.getCurrPredicate().existRoleset())
			en_frameset.getCurrPredicate().selectNextRoleset();
	}

	// ---------------------------------- create*() ----------------------------------
	
	/**
	 * Creates an empty predicate element with {@link lemma}.
	 * @param lemma predicate lemma
	 * @return empty predicate element
	 */
	static public Element createEmptyPredicate(String lemma)
	{
		Element ePredicate = createElement(EnLib.PREDICATE);
		ePredicate.setAttribute(EnLib.LEMMA, lemma);
		
		return ePredicate;
	}
	
	/**
	 * Creates an empty roleset XML element with {@link id}.
	 * @param id roleset ID
	 * @return empty roleset XML element
	 */
	static public Element createEmptyRoleset(String id)
	{
		Element eRoleset = createElement(EnLib.ROLESET);
		eRoleset.setAttribute(EnLib.ID, id);
		
		Element eRoles = createEmptyRoles();
		eRoleset.appendChild(eRoles);
		
		return eRoleset;
	}
	
	/**
	 * Creates an empty roles XML element.
	 * @return empty roles XML element
	 */
	static public Element createEmptyRoles()
	{
		Element eRoles = createElement(EnLib.ROLES);
		eRoles.appendChild(createEmptyRole());
		
		return eRoles;
	}
	
	/**
	 * Creates an empty role XML element.
	 * @return empty role XML element
	 */
	static public Element createEmptyRole()
	{
		return createElement(EnLib.ROLE);
	}
	
	/**
	 * Creates an empty vnrole XML element.
	 * @return empty vnrole XML element
	 */
	static public Element createEmptyVnrole()
	{
		return createElement(EnLib.VNROLE);
	}
	
	/**
	 * Creates an empty example XML element.
	 * @return empty example XML element
	 */
	static public Element createEmptyExample()
	{
		Element eExample = createElement(EnLib.EXAMPLE);
		eExample.appendChild(createEmptyInflection());
		eExample.appendChild(createEmptyText());
		
		return eExample;
	}
	
	/**
	 * Creates an empty inflection XML element.
	 * @return empty inflection XML element
	 */
	static public Element createEmptyInflection()
	{
		return createElement(EnLib.INFLECTION);
	}
	
	/**
	 * Creates an empty text XML element.
	 * @return empty text XML element
	 */
	static public Element createEmptyText()
	{
		return createElement(EnLib.TEXT);
	}
	
	/**
	 * Creates an empty argument XML element.
	 * @return empty argument XML element
	 */
	static public Element createEmptyArg()
	{
		return createElement(EnLib.ARG);
	}
	
	/**
	 * Creates an empty relation XML element.
	 * @return empty relation XML element
	 */
	static public Element createEmptyRel()
	{
		return createElement(EnLib.REL);
	}
	
	/**
	 * Creates a note XML element.
	 * @param content content of the note
	 * @return note XML element
	 */
	static public Element createNote(String content)
	{
		Element eNote = createElement(EnLib.NOTE);
		eNote.setTextContent(content.trim());
		
		return eNote; 
	}
}
