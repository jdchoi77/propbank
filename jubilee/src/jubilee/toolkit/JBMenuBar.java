/**
* Copyright (c) 2007, Regents of the University of Colorado
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
package jubilee.toolkit;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jubilee.propbank.PBReader;
import jubilee.util.DataManager;

/**
 * @author Jinho D. Choi
 * <b>Last update:</b> 5/6/2010
 */
@SuppressWarnings("serial")
public class JBMenuBar extends JMenuBar
{
	private JBToolkit jbtk;
	private JMenu     mn_args;
	
	JMenuItem   fileOpen, fileSave, fileSaveAs, fileQuit;
	JMenuItem   tbPrev, tbNext, tbJump, tbView, tbDepTree;
	JMenuItem   fsPrev, fsNext, fsViewExample, fsViewArgument, fsViewRolesetComment;
	JMenuItem[] argArgs, argFunc;
	JMenuItem   argErase;
	JMenuItem   helpAbout;
	
	/**
	 * Creates a menubar.
	 * @param jbtk parent class.
	 */
	public JBMenuBar(JBToolkit jbtk)
	{
		this.jbtk = jbtk;
		
		initMenuFile();
		initMenuTreebank();
		initMenuFrameset();
		initMenuArgument();
		initMenuHelp();
	}
	
	/** Called from {@link JBToolkit#initProperties(String, java.util.HashMap)} */
	void setMenuArgTag(ArrayList<String[]> args)
	{
		mn_args.removeAll();
		argArgs = new JMenuItem[JBArgPanel.NUM_ARG+args.size()-1];
		PBReader.REL = args.get(0)[0];

		// number argument-tags
		int k = 0;
		for (int i=0; i<JBArgPanel.NUM_ARG; i++)
		{
			argArgs[k] = getJMenuItem(String.valueOf(i), 48+i, 48+i, 0);
			mn_args.add(argArgs[k++]);
		}
		mn_args.addSeparator();

		// add ArgM tags
		for (int i=1; i<args.size(); i++)
		{
			String[] arg = args.get(i);
			argArgs[k] = getJMenuItem(arg[0], 0, arg[1].charAt(0), 0);
			mn_args.add(argArgs[k++]);
		}
		mn_args.addSeparator();
		
		// no arguement-tag
		argErase = getJMenuItem(JBArgPanel.ERASE, 0, KeyEvent.VK_MINUS, 0);
		mn_args.add(argErase);
		mn_args.revalidate();
	}
	
	private void initMenuFile()
	{
		JMenu mFile = new JMenu("File");
		mFile.setMnemonic(KeyEvent.VK_F);
		
		fileOpen = getJMenuItem("Open", KeyEvent.VK_O, KeyEvent.VK_O, KeyEvent.CTRL_MASK);
		mFile.add(fileOpen);
		mFile.addSeparator();
		
		fileSave = getJMenuItem("Save", KeyEvent.VK_S, KeyEvent.VK_S, KeyEvent.CTRL_MASK);
		mFile.add(fileSave);
		fileSaveAs = getJMenuItem("Save As", KeyEvent.VK_A);
		mFile.add(fileSaveAs);
		mFile.addSeparator();
	
		fileQuit = getJMenuItem("Quit", KeyEvent.VK_Q, KeyEvent.VK_Q, KeyEvent.CTRL_MASK);
		mFile.add(fileQuit);
		
		add(mFile);
	}
	
	private void initMenuTreebank()
	{
		JMenu mTreebank = new JMenu("Treebank");
		mTreebank.setMnemonic(KeyEvent.VK_T);
		
		tbPrev = getJMenuItem("Previous Tree", KeyEvent.VK_P, KeyEvent.VK_COMMA, 0);
		mTreebank.add(tbPrev);
		tbNext = getJMenuItem("Next Tree", KeyEvent.VK_N, KeyEvent.VK_PERIOD, 0);
		mTreebank.add(tbNext);
		tbJump = getJMenuItem("Jump To", KeyEvent.VK_J, KeyEvent.VK_J, KeyEvent.CTRL_MASK);
		mTreebank.add(tbJump);
		mTreebank.addSeparator();
		
		tbView = getJMenuItem("View Tree in Text", KeyEvent.VK_T, KeyEvent.VK_T, KeyEvent.CTRL_MASK);
		mTreebank.add(tbView);
		
		tbDepTree = getJMenuItem("View Dependency Tree", KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.CTRL_MASK);
		mTreebank.add(tbDepTree);
		
		add(mTreebank);
	}
	
	private void initMenuFrameset()
	{
		JMenu mFrameset = new JMenu("Frameset");
		mFrameset.setMnemonic(KeyEvent.VK_R);

		fsPrev = getJMenuItem("Previous Roleset", KeyEvent.VK_P, KeyEvent.VK_OPEN_BRACKET, 0);
		mFrameset.add(fsPrev);
		fsNext = getJMenuItem("Next Roleset", KeyEvent.VK_N, KeyEvent.VK_CLOSE_BRACKET, 0);
		mFrameset.add(fsNext);
		
		fsViewExample = getJMenuItem("View Example", KeyEvent.VK_E, KeyEvent.VK_E, KeyEvent.CTRL_MASK);
		mFrameset.add(fsViewExample);
		fsViewArgument = getJMenuItem("View Arguments", KeyEvent.VK_W, KeyEvent.VK_W, KeyEvent.CTRL_MASK);
		mFrameset.add(fsViewArgument);
		
		fsViewRolesetComment = getJMenuItem("View Roleset Comments", KeyEvent.VK_C, KeyEvent.VK_C, KeyEvent.CTRL_MASK);
		mFrameset.add(fsViewRolesetComment);
		
		add(mFrameset);
	}
	
	private void initMenuArgument()
	{
		JMenu mArg = new JMenu("Argument");
		mArg.setMnemonic(KeyEvent.VK_A);
		
		// --------------- argument-tags ---------------
		mn_args = new JMenu("Arguments");
		mArg.add(mn_args);
		
		// --------------- function-tags ---------------
		JMenu mArgFunc = new JMenu("Functions");
		
		ArrayList<String[]> operators = DataManager.getContents(DataManager.OPERATOR_FILE);
		argFunc = new JMenuItem[operators.size()];
		
		for (int i=0; i<operators.size(); i++)
		{
			String[] operator = operators.get(i);
			argFunc[i] = getJMenuItem(operator[0], 0, operator[1].charAt(0), KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK);
			mArgFunc.add(argFunc[i]);
		}
		
		mArg.add(mArgFunc);		
		add(mArg);
	}
	
	private void initMenuHelp()
	{
		JMenu mHelp = new JMenu("Help");
		mHelp.setMnemonic(KeyEvent.VK_H);

		helpAbout = getJMenuItem("About", KeyEvent.VK_A, KeyEvent.VK_F1, 0);
 		mHelp.add(helpAbout);
		
		add(mHelp);
	}
	
	// set menu-item with name, short-key
	private JMenuItem getJMenuItem(String text, int mnemonic)
	{
		JMenuItem mi = new JMenuItem(text, mnemonic);		
		mi.addActionListener(jbtk);
		
		return mi;
    }
	
	// set menu-item with name, short-key, accelerator
	private JMenuItem getJMenuItem(String text, int mnemonic, int keyCode, int modifiers)
	{
		JMenuItem mi = new JMenuItem(text, mnemonic);
		
		mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		mi.addActionListener(jbtk);
		
		return mi;
    }
}
