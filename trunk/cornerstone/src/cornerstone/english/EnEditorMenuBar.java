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

import java.awt.event.*;
import javax.swing.*;

/**
 * <b>Last update:</b> 06/15/2009
 * @author Jinho D. Choi
 */
public class EnEditorMenuBar extends JMenuBar
{
	EnEditor  parent;
	JMenuItem fileNew, fileOpen, fileSave, fileSaveAs, fileQuit;
	JMenuItem editAddPredicate, editEditPredicate, editRemovePredicate;
	JMenuItem editAddRoleset, editEditRoleset, editRemoveRoleset;
	JMenuItem editAddRole, editEditExample;
	JMenuItem movePrevPredicate, moveNextPredicate, movePrevRoleset, moveNextRoleset;
	
	/**
	 * Initializes a menubar for {@link parent}.
	 * @param parent editor frame
	 */
	public EnEditorMenuBar(EnEditor parent)
	{
		this.parent = parent;
		
		initMenuFile();
		initMenuEdit();
		initMenuMove();
	}
	
	private void initMenuFile()
	{
		JMenu mFile = new JMenu("File");
		mFile.setMnemonic(KeyEvent.VK_F);
		
		fileNew = getJMenuItem("New", KeyEvent.VK_N, KeyEvent.VK_N, KeyEvent.CTRL_MASK);
		mFile.add(fileNew);
		fileOpen = getJMenuItem("Open", KeyEvent.VK_O, KeyEvent.VK_O, KeyEvent.CTRL_MASK);
		mFile.add(fileOpen);
		mFile.addSeparator();
		
		fileSave = getJMenuItem("Save", KeyEvent.VK_S, KeyEvent.VK_S, KeyEvent.CTRL_MASK);
		mFile.add(fileSave);
		fileSaveAs = getJMenuItem("Save As", KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK);
		mFile.add(fileSaveAs);
		mFile.addSeparator();
		
		fileQuit = getJMenuItem("Quit", KeyEvent.VK_Q, KeyEvent.VK_Q, KeyEvent.CTRL_MASK);
		mFile.add(fileQuit);
		
		add(mFile);
	}
	
	private void initMenuEdit()
	{
		JMenu mEdit = new JMenu("Edit");
		mEdit.setMnemonic(KeyEvent.VK_E);
		
		editAddPredicate = getJMenuItem("Add Predicate", KeyEvent.VK_A, KeyEvent.VK_P, KeyEvent.CTRL_MASK);
		mEdit.add(editAddPredicate);
		editEditPredicate = getJMenuItem("Edit Predicate Lemma", KeyEvent.VK_E, KeyEvent.VK_P, KeyEvent.CTRL_MASK+KeyEvent.ALT_MASK);
		mEdit.add(editEditPredicate);
		editRemovePredicate = getJMenuItem("Remove Predicate", KeyEvent.VK_R, KeyEvent.VK_P, KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK);
		mEdit.add(editRemovePredicate);
		mEdit.addSeparator();
		
		editAddRoleset = getJMenuItem("Add Roleset", KeyEvent.VK_A, KeyEvent.VK_R, KeyEvent.CTRL_MASK);
		mEdit.add(editAddRoleset);
	//	editEditRoleset = getJMenuItem("Edit Roleset ID", KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.CTRL_MASK+KeyEvent.ALT_MASK);
	//	mEdit.add(editEditRoleset);
		editRemoveRoleset = getJMenuItem("Remove Roleset", KeyEvent.VK_R, KeyEvent.VK_R, KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK);
		mEdit.add(editRemoveRoleset);
		mEdit.addSeparator();		
		
		editAddRole = getJMenuItem("Add Role", KeyEvent.VK_A, KeyEvent.VK_L, KeyEvent.CTRL_MASK);
		mEdit.add(editAddRole);
		editEditExample = getJMenuItem("Edit Examples", KeyEvent.VK_E, KeyEvent.VK_E, KeyEvent.CTRL_MASK);
		mEdit.add(editEditExample);
		
		add(mEdit);
	}
	
	private void initMenuMove()
	{
		JMenu mMove = new JMenu("Move");
		mMove.setMnemonic(KeyEvent.VK_M);
		
		movePrevPredicate = getJMenuItem("Move to previous predicate", KeyEvent.VK_P, KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_MASK);
		mMove.add(movePrevPredicate);
		moveNextPredicate = getJMenuItem("Move to next predicate", KeyEvent.VK_N, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.CTRL_MASK);
		mMove.add(moveNextPredicate);
		mMove.addSeparator();
		
		movePrevRoleset = getJMenuItem("Move to previous roleset", KeyEvent.VK_P, KeyEvent.VK_COMMA, KeyEvent.CTRL_MASK);
		mMove.add(movePrevRoleset);
		moveNextRoleset = getJMenuItem("Move to next roleset", KeyEvent.VK_N, KeyEvent.VK_PERIOD, KeyEvent.CTRL_MASK);
		mMove.add(moveNextRoleset);
		
		add(mMove);
	}
	
	// set menu-item with name, short-key, accelerator
	private JMenuItem getJMenuItem(String text, int mnemonic, int keyCode, int modifiers)
	{
		JMenuItem mi = new JMenuItem(text, mnemonic);
		
		mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		mi.addActionListener(parent);
		
		return mi;
    }
}
