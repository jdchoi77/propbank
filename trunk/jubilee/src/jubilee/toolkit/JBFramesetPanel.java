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
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import jubilee.awt.JDCTextAreaFrame;
import jubilee.propbank.*;

public class JBFramesetPanel extends JPanel implements ActionListener, ItemListener
{
	PBFrameset frameset;
	JComboBox cb_roleset;
	JTextField tf_predicate;
	JTextArea ta_role;
	JButton bt_example;
	PBFramesetReader fr_reader;
	PBReader pb_reader = null;
	JDCTextAreaFrame fr_example = null;
	JDCTextAreaFrame fr_rolesetComment = null;
	
	private Vector<Integer> vec_pNum, vec_cNum;
	
	private String[] S_TAGS = {"ER", "LV", "NN", "IE"};
	
	/**
	 * @param framesetPath the directiory path of frameset-files
	 */
	public JBFramesetPanel()
	{
		setLayout(new BorderLayout());
		addTopPanel();
		addTextField();
		fr_reader = new PBFramesetReader();
		vec_pNum = new Vector<Integer>();
		vec_cNum = new Vector<Integer>();
	}
	
	public void setProperties(String language, String framesetPath)
	{
		fr_reader.setProperties(language, framesetPath);
	}
	
	// ------------------------- initialize -------------------------
	
	private void addTopPanel()
	{
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(0,2));
		
		tf_predicate = new JTextField("Predicate");
		tf_predicate.setEditable(false);
		top.add(tf_predicate);
		
		String str[] = {"Roleset"};
		cb_roleset = new JComboBox(str);
		cb_roleset.addItemListener(this);
		top.add(cb_roleset);
		
		bt_example = new JButton("Example");
		bt_example.addActionListener(this);
		bt_example.setEnabled(false);
		
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.add(top, BorderLayout.CENTER);
		main.add(bt_example, BorderLayout.EAST);
		add(main, BorderLayout.NORTH);
		
		
		
		
	/*	JPanel top = new JPanel();
		top.setLayout(new GridLayout(0,3));
		
		tf_predicate = new JTextField("Predicate");
		tf_predicate.setEditable(false);
		top.add(tf_predicate);
		
		String str[] = {"Roleset"};
		cb_roleset = new JComboBox(str);
		cb_roleset.addItemListener(this);
		top.add(cb_roleset);
		
		bt_example = new JButton("Example");
		bt_example.addActionListener(this);
		bt_example.setEnabled(false);
		top.add(bt_example);
		
		add(top, BorderLayout.NORTH);*/
	}
	
	private void addTextField()
	{
		ta_role = new JTextArea();
		ta_role.setEditable(false);
		JScrollPane sp_role = new JScrollPane(ta_role);
		sp_role.setBorder(new TitledBorder("Roleset Information"));
		sp_role.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(sp_role, BorderLayout.CENTER);
	}
	
	// ---------------------- utilties ----------------------
	
	// called from PBToolKit
	public void initPBReader(PBReader reader)
	{
		pb_reader = reader;
	}
	
	public String getRoleset()
	{
		return (String)cb_roleset.getSelectedItem();
	}
	
	public void prevRoleset()
	{
		int index = cb_roleset.getSelectedIndex() - 1;
		
		if (index >= 0)
		cb_roleset.setSelectedIndex(index);
	}
	
	public void nextRoleset()
	{
		int index = cb_roleset.getSelectedIndex() + 1;
		
		if (index != 0 && index < cb_roleset.getItemCount())
			cb_roleset.setSelectedIndex(index);
	}
	
	// ---------------------- Events ---------------------- 
	
	public void itemStateChanged(ItemEvent e)
	{
		if (cb_roleset.getSelectedIndex() < 0)	return;
		if (cb_roleset.getSelectedIndex() != 0 && cb_roleset.getSelectedIndex() < cb_roleset.getItemCount()-S_TAGS.length)
		{
			PBPredicate predicate = frameset.getPredicate(getPredicateNum());
			PBRoleset roleset = predicate.getRoleset(getRolesetNum());
			
			tf_predicate.setText(predicate.getLemma());
			ta_role.setText(roleset.toString());
			bt_example.setEnabled(true);
		}
		else
		{
			tf_predicate.setText("");
			ta_role.setText("");
			bt_example.setEnabled(false);
		}
		
		pb_reader.setLemma((String)cb_roleset.getSelectedItem());
	}
	
	public void actionPerformed(ActionEvent e)
	{
		showExample();
	}
	
	public void showExample()
	{
		PBPredicate predicate = frameset.getPredicate(getPredicateNum());
		PBRoleset roleset = predicate.getRoleset(getRolesetNum());
		
		if (fr_example != null)
			fr_example.dispose();
		fr_example = new JDCTextAreaFrame("Examples for "+roleset.getId(), roleset.getExamples());
	}
	
	public void viewRolesetComment()
	{
		PBPredicate predicate = frameset.getPredicate(getPredicateNum());
		PBRoleset roleset = predicate.getRoleset(getRolesetNum());
		
		fr_rolesetComment = new JDCTextAreaFrame("Comments for "+roleset.getId(), roleset.getComment());
	}

	public void updateFrameset(String lemma)
	{
		StringTokenizer tok = new StringTokenizer(lemma, ".");
		String lemmaTok = tok.nextToken();
		
		// remove previous settings
		cb_roleset.removeAllItems();
		vec_pNum.removeAllElements();
		vec_cNum.removeAllElements();
		
		int k = 0;
		cb_roleset.insertItemAt(lemmaTok+".XX", k++);
		if ((frameset = fr_reader.getFrameset(lemmaTok)) != null)
		{
			for (int i=0; i<frameset.getSize(); i++)
			{
				PBPredicate predicate = frameset.getPredicate(i);
				String[] roleset = predicate.getRolesetID();
				for (int j=0; j<roleset.length; j++)
				{
					cb_roleset.insertItemAt(roleset[j], k++);
					vec_pNum.add(i);	vec_cNum.add(j);
				}
			}
			
			for (int i=0; i<S_TAGS.length; i++)
				cb_roleset.insertItemAt(lemmaTok+"."+S_TAGS[i], k+i);
			
			int index = getItemIndex(lemma);
			if (index != -1)	cb_roleset.setSelectedIndex(index);
			else				cb_roleset.setSelectedIndex(0);
		}
		else
		{
			for (int i=0; i<S_TAGS.length; i++)
				cb_roleset.insertItemAt(lemmaTok+"."+S_TAGS[i], k+i);
			
			cb_roleset.setSelectedIndex(0);
			tf_predicate.setText("No frameset-file");
		}
	}
	
	private int getItemIndex(String lemma)
	{
		for (int i=0; i<cb_roleset.getItemCount(); i++)
			if (lemma.equalsIgnoreCase((String)cb_roleset.getItemAt(i)))	return i;
		
		return -1;
	}
	
	private int getPredicateNum()
	{
		return vec_pNum.get(cb_roleset.getSelectedIndex()-1);
	}
	
	private int getRolesetNum()
	{
		return vec_cNum.get(cb_roleset.getSelectedIndex()-1);
	}
}
