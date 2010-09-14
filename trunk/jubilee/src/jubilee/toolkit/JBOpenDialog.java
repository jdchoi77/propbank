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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jubilee.util.DataManager;

/**
 * @author Jinho D. Choi
 * <b>Last update:</b> 5/6/2010
 */
@SuppressWarnings("serial")
public class JBOpenDialog extends JDialog implements ActionListener, ItemListener, ListSelectionListener
{
	private JBToolkit              jbtk;
	private JComboBox              cb_projects;
	private JList                  ls_newTask = null;
	private JList                  ls_myTask  = null;
	private DefaultListModel       lm_newTask;
	private DefaultListModel       lm_myTask;
	private JButton                bt_cancel;
	private JButton                bt_enter;
	private HashMap<String,String> m_dataset;
	private int                    i_maxAnn;
	
	/**
	 * Initializes the open-dialog.
	 * @param jbtk Jubilee's main window (JBToolKit).
	 * @param isCancel if (isCancel == true/false) enable/disable the cancel button.
	 */
	public JBOpenDialog(JBToolkit jbtk, boolean isCancel, int maxAnn)
	{
		super(jbtk, "Open a task", true);
		this.jbtk = jbtk;
		i_maxAnn  = maxAnn;
		
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
		cb_projects = new JComboBox(DataManager.getProjects());
		cb_projects.setBorder(new TitledBorder("Choose a project"));
		cb_projects.addItemListener(this);
		
		ArrayList<String[]> list = DataManager.getContents((String)cb_projects.getItemAt(jbtk.i_currSetting) + DataManager.PATH_FILE_EXT);
		m_dataset = getMap(list);
		
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
		
		cb_projects.setSelectedIndex(jbtk.i_currSetting);
	}
	
	private HashMap<String, String> getMap(ArrayList<String[]> list)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (String[] arr : list)	map.put(arr[0], arr[1]);
		
		return map;
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
		File taskDir = new File(m_dataset.get(DataManager.TASK));
		File annDir  = new File(m_dataset.get(DataManager.ANNOTATION));
		String[] tasklist = taskDir.list();	Arrays.sort(tasklist);
	//	String[] annlist  = annDir .list();	Arrays.sort(annlist);
		
		List     <String> tmp1 = Arrays.asList(annDir.list());
		ArrayList<String> tmp2 = new ArrayList<String>();
		for (String str : tmp1)
			if (jbtk.isGold() || !str.endsWith(".gold"))	tmp2.add(str);
		
		String[] annlist = new String[tmp2.size()];
		tmp2.toArray(annlist);	Arrays.sort(annlist);
		
		// remove previous lists
		lm_newTask.removeAllElements();
		lm_myTask .removeAllElements();
		
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
			int    index = annlist[i].lastIndexOf(".");
			String task  = annlist[i].substring(0, index);
			String id    = annlist[i].substring(index+1);
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
		
		cp.add(cb_projects, BorderLayout.NORTH);
		
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
		if      (e.getSource() == ls_newTask)
			ls_myTask.clearSelection();
		else if (e.getSource() == ls_myTask)
			ls_newTask.clearSelection();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == cb_projects)
		{
			ArrayList<String[]> list = DataManager.getContents((String)cb_projects.getSelectedItem() + DataManager.PATH_FILE_EXT);
			m_dataset = getMap(list);
			initJList();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if      (e.getSource() == bt_cancel)	dispose();
		else if (e.getSource() == bt_enter)		actionBtEnter();
	}

	private void actionBtEnter()
	{
		if (ls_newTask.isSelectionEmpty() && ls_myTask.isSelectionEmpty())	return;
		StringTokenizer tok = new StringTokenizer((String)cb_projects.getSelectedItem(), ".");
		jbtk.i_currSetting = cb_projects.getSelectedIndex();
		String language = tok.nextToken();
		jbtk.initProperties(language, m_dataset);	// tok.nextToken() = language
		
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
	
	@SuppressWarnings("static-access")
	private String[] getFileList(String fstFile, boolean isNew)
	{
		Vector<String> vec = new Vector<String>();
		String task = (isNew) ? fstFile : fstFile.substring(0, fstFile.lastIndexOf("."));

		if (jbtk.isGold())
		{
			File annDir = new File(m_dataset.get(DataManager.ANNOTATION));
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
