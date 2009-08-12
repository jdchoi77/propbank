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

import java.awt.event.*;
import javax.swing.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class ChExampleMenuBar extends JMenuBar
{
	ChExampleFrame parent;
	JMenuItem fileQuit;
	JMenuItem editAddExample, editRemoveExample;
	JMenuItem editAddArg, editAddRel;
	JMenuItem movePrevExample, moveNextExample;
	
	/**
	 * Initializes a menubar for {@link parent}.
	 * @param parent example frame
	 */
	public ChExampleMenuBar(ChExampleFrame parent)
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
		
		fileQuit = getJMenuItem("Quit", KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.CTRL_MASK);
		mFile.add(fileQuit);
		
		add(mFile);
	}
	
	private void initMenuEdit()
	{
		JMenu mEdit = new JMenu("Edit");
		mEdit.setMnemonic(KeyEvent.VK_E);
		
		editAddExample = getJMenuItem("Add Example", KeyEvent.VK_A, KeyEvent.VK_EQUALS, KeyEvent.CTRL_MASK);
		mEdit.add(editAddExample);
		editRemoveExample = getJMenuItem("Remove Example", KeyEvent.VK_R, KeyEvent.VK_MINUS, KeyEvent.CTRL_MASK);
		mEdit.add(editRemoveExample);
		mEdit.addSeparator();
		
		editAddArg = getJMenuItem("Add Argument", KeyEvent.VK_A, KeyEvent.VK_9, KeyEvent.CTRL_MASK);
		mEdit.add(editAddArg);
		editAddRel = getJMenuItem("Add Verb", KeyEvent.VK_A, KeyEvent.VK_0, KeyEvent.CTRL_MASK);
		mEdit.add(editAddRel);
		
		add(mEdit);
	}
	
	private void initMenuMove()
	{
		JMenu mMove = new JMenu("Move");
		mMove.setMnemonic(KeyEvent.VK_M);
		
		movePrevExample = getJMenuItem("Move to previous example", KeyEvent.VK_P, KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_MASK);
		mMove.add(movePrevExample);
		moveNextExample = getJMenuItem("Move to next example", KeyEvent.VK_N, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.CTRL_MASK);
		mMove.add(moveNextExample);
		
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
