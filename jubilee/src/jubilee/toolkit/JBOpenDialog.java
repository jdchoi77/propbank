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

import java.io.File;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import jubilee.util.*;

public class JBOpenDialog extends JDialog implements ActionListener, ItemListener, ListSelectionListener
{
	private JBToolkit jbtk;
	private JComboBox cb_setting;
	private JList ls_newTask = null, ls_myTask = null;
	private DefaultListModel lm_newTask, lm_myTask;
	private JButton bt_cancel, bt_enter;
	private String str_dataset[][];
	int i_maxAnn;
	
	/**
	 * Initializes the open-dialog.
	 * @param jbtk Jubilee's main window (JBToolKit).
	 * @param isCancel if (isCancel == true/false) enable/disable the cancel button.
	 */
	public JBOpenDialog(JBToolkit jbtk, boolean isCancel, int maxAnn)
	{
		super(jbtk, "Open a task", true);
		this.jbtk = jbtk;
		i_maxAnn = maxAnn;
		
		Container cp = getContentPane();
		initComponents(cp, isCancel);
		initBounds(cp);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(10, 10, 300, 600);
		setVisible(true);
	}
	
	// --------------------------- init*() --------------------------- 
	
	private void initComponents(Container cp, boolean isCancel)
	{
		// combobox: dataset
		cb_setting = new JComboBox(DataManager.getSettings());
		cb_setting.setBorder(new TitledBorder("Choose a setting"));
		cb_setting.addItemListener(this);
		str_dataset = DataManager.getContents((String)cb_setting.getItemAt(jbtk.i_currSetting), DataManager.PATH);
		
		// lists new and my tasks
		lm_newTask = new DefaultListModel();
		ls_newTask = new JList(lm_newTask);
		ls_newTask.setBorder(new TitledBorder("New Tasks"));
		ls_newTask.addListSelectionListener(this);
		
		lm_myTask = new DefaultListModel();
		ls_myTask = new JList(lm_myTask);
		ls_myTask.setBorder(new TitledBorder("My Tasks"));
		ls_myTask.addListSelectionListener(this);
		initJList();
		
		// button: cancel, enter
		bt_cancel = getJButton(cp, "Cancel");
		bt_cancel.setEnabled(isCancel);
		bt_enter = getJButton(cp, "Enter");
		
		cb_setting.setSelectedIndex(jbtk.i_currSetting);
	}
	
	private JButton getJButton(Container cp, String title)
	{
		JButton bt = new JButton(title);
		
		bt.addActionListener(this);
		cp.add(bt);
		return bt;
	}
	
	private void initJList()
	{
		// get file lists
		File taskDir = new File(DataManager.getPath(str_dataset, jbtk.TASK));
		File annDir = new File(DataManager.getPath(str_dataset, jbtk.ANN));
		String[] tasklist = taskDir.list();	Arrays.sort(tasklist);
		String[] annlist = annDir.list();	Arrays.sort(annlist);
		
		// remove previous lists
		lm_newTask.removeAllElements();
		lm_myTask.removeAllElements();
		
		// add new task lists
		for (int i=0; i<tasklist.length; i++)
		{
			if (tasklist[i].substring(tasklist[i].lastIndexOf(".")+1).equalsIgnoreCase("task"))
				lm_newTask.addElement(tasklist[i]);
		}
		
		Vector<String> vec = new Vector<String>();
		// add ann file list and remove the corresponding task list
		for (int i=0; i<annlist.length; i++)
		{
			String task = annlist[i].substring(0, annlist[i].lastIndexOf("."));
			String id = annlist[i].substring(annlist[i].lastIndexOf(".")+1);
			vec.add(task);
			
			if (id.equalsIgnoreCase(jbtk.getUserID()))
			{
				lm_newTask.removeElement(task);
				lm_myTask.addElement(annlist[i]);
			}
			else if (!jbtk.isGold())
			{
				int count = 1, j = vec.indexOf(task, 0);
				while ((j = vec.indexOf(task, ++j)) != -1)	count++;
					
				if (count >= i_maxAnn)	lm_newTask.removeElement(task);
			}
		}
	}
	
	private void initBounds(Container cp)
	{
		cp.setLayout(new BorderLayout());
		
		cp.add(cb_setting, BorderLayout.NORTH);
		
		JPanel pnC = new JPanel();
		pnC.setLayout(new GridLayout(0,2));
		pnC.add(new JScrollPane(ls_newTask));
		pnC.add(new JScrollPane(ls_myTask));
		cp.add(pnC, BorderLayout.CENTER);
		
		JPanel pnS = new JPanel();
		pnS.setLayout(new GridLayout(0,2));
		pnS.add(bt_cancel);
		pnS.add(bt_enter);
		cp.add(pnS, BorderLayout.SOUTH);
	}
	
	// --------------------------- Events ---------------------------
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getSource() == ls_newTask)
			ls_myTask.clearSelection();
		else if (e.getSource() == ls_myTask)
			ls_newTask.clearSelection();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == cb_setting)
		{
			str_dataset = DataManager.getContents((String)cb_setting.getSelectedItem(), DataManager.PATH);
			initJList();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_cancel)			dispose();
		else if (e.getSource() == bt_enter)		actionBtEnter();
	}

	private void actionBtEnter()
	{
		if (ls_newTask.isSelectionEmpty() && ls_myTask.isSelectionEmpty())	return;
		
		StringTokenizer tok = new StringTokenizer((String)cb_setting.getSelectedItem(), ".");
		jbtk.i_currSetting = cb_setting.getSelectedIndex();
		String language = tok.nextToken();
		jbtk.initProperties(language, str_dataset);	// tok.nextToken() = language
		
		if (!ls_newTask.isSelectionEmpty())
		{
			String[] tmp = getFileList((String)ls_newTask.getSelectedValue(), true);
			if (tmp != null)	jbtk.openFile(tmp, true);
			else				return;
		}
		else
		{
			String[] tmp = getFileList((String)ls_myTask.getSelectedValue(), false);
			jbtk.openFile(tmp, false);
		}
		
		dispose();
	}
	
	private String[] getFileList(String fstFile, boolean isNew)
	{
		Vector<String> vec = new Vector<String>();
		String task = (isNew) ? fstFile : fstFile.substring(0, fstFile.lastIndexOf("."));

		if (jbtk.isGold())
		{
			File annDir = new File(DataManager.getPath(str_dataset, jbtk.ANN));
			String[] annlist = annDir.list();	Arrays.sort(annlist);
			
			for (int i=0; i<annlist.length; i++)
			{
				String myTask = annlist[i].substring(0, annlist[i].lastIndexOf("."));
				if (myTask.equals(task) && !annlist[i].equals(fstFile))
					vec.add(annlist[i]);
			}
			
			if (isNew && vec.size() == 0)
			{
				new JOptionPane().showMessageDialog(this, "No annotations for the task");
				return null;
			}
		}
		
		String[] tmp = new String[1+vec.size()];
		tmp[0] = task;
		for (int i=0; i<vec.size(); i++)	tmp[i+1] = vec.get(i);
		
		return tmp;
	}
}
