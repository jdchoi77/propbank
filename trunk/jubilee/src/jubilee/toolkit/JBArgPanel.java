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
package jubilee.toolkit;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JBArgPanel extends JPanel implements ActionListener
{
	static public       int    NUM_ARG = 6;
	static public final String ERASE   = "ERASE";
	
	private final String ARG_PREFIX = "ARG";
	private final Font   BT_FONT    = new Font("Arial", Font.PLAIN, 12);
	
	private JBTreePanel tree;
	private JButton     bt_erase = null;
	private JButton[]   bt_arg;
	private JPanel      pn_button;
	private JBToolkit   pbtk;

	/**
	 * Creates the argument panel.
	 * @param filename the name of the file containing argument info.
	 */
	public JBArgPanel(JBTreePanel tree, JBToolkit pbtk)
	{
		this.tree = tree;
		this.pbtk = pbtk;
		pn_button = new JPanel();
		
		setLayout(new BorderLayout());
		add(getButtonPanel(), BorderLayout.CENTER);
	}
	
	// ----------------------------- Set components -----------------------------
	
	private JPanel getButtonPanel()
	{
		pn_button.setLayout(new GridLayout(0, 4));
		return pn_button;
	}
	
	public void updateArgButtons(ArrayList<String[]> args)
	{
		if (JBToolkit.s_language.equals("hindi"))
			NUM_ARG = 4;
		
		if (bt_erase != null)
		{
			for (int i=0; i<bt_arg.length; i++)	pn_button.remove(bt_arg[i]);
			pn_button.remove(bt_erase);
		}
		
		bt_arg = new JButton[NUM_ARG+args.size()-1];
		int k = 0;
		for (int i=0; i<NUM_ARG; i++)
			bt_arg[k++] = getJButton(String.valueOf(i));
		
		for (int i=1; i<args.size(); i++)
		{
			String[] arg = args.get(i);
			bt_arg[k++] = getJButton(arg[0] + " (" + arg[1] + ")");
		}

		bt_erase = getJButton(ERASE+" (-)");
		pn_button.revalidate();
	}
	
	private JButton getJButton(String title)
	{
		JButton bt = new JButton(title);
		bt.setFont(BT_FONT);
		bt.addActionListener(this);
		pn_button.add(bt);
		
		return bt;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		StringTokenizer tok = new StringTokenizer(e.getActionCommand());
		updateArg(tok.nextToken());
	}
	
	public void updateArg(String arg)
	{
		if (arg.equals(ERASE))
			tree.updateArg(null, ERASE);
		else if (arg.equals("M-SLC"))
			tree.updateArg(null, "LINK-SLC");
		else
			tree.updateArg(null, ARG_PREFIX+arg);

		pbtk.updateGoldTopList();
	}
	
	public void setFunction(String func)
	{
		tree.updateArg(func, "");
		pbtk.updateGoldTopList();
	}
}
