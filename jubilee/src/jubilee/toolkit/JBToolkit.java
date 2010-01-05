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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import jubilee.awt.*;
import jubilee.propbank.*;
import jubilee.util.*;

public class JBToolkit extends JFrame implements ActionListener, ItemListener, ListSelectionListener
{
	static final String FRAMESET = "Frameset";
	static final String TREEBANK = "Treebank";
	static final String TASK = "Task";
	static final String ANN = "Annotation";
	static final String GOLD = "gold";
	
	private String str_frameTitle;
	private String str_userID;
	private String[][] str_dataset;		// dataset[0] = resource, dataset[1] = directory-paths
	private String str_annFile;			// path of annotation file
	
	// Treeview: top-pane
	private JButton bt_prev, bt_next;
	private JComboBox cb_jump;
	private JTextField tf_annotator;
	private JTextArea ta_sentence;
	private JList ls_gold;
	private DefaultListModel lm_gold;
	private JBTreePanel tv_tree;
	
	private JBFramesetPanel framesetPanel;
	private JBArgPanel argPanel;
	private JBMenuBar mbar;
	private PBReader pb_origin;
	private PBReader[] pb_more;
	
	int i_currSetting = 0;		// current project setting (i.e. english.sample.path)
	int i_maxAnn;
	boolean b_skip;
	
	static public String s_language = "english";
	static public String s_sysDir   = null;
	
	public JBToolkit(String title, String sysDir, String userID, int maxAnn, byte skip)
	{
		super(title);
		str_frameTitle = title;
		s_sysDir = sysDir;
		str_userID = userID;
		b_skip = (skip == 0) ? false : true;
		i_maxAnn = maxAnn;
		
		Container cp = getContentPane();
		initComponents(cp);
		initBounds(cp);
		
		new JBOpenDialog(this, false, maxAnn);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	boolean isGold()
	{
		return str_userID.equalsIgnoreCase(GOLD);
	}
	
	String getUserID()
	{
		return str_userID;
	}
	
	// -------------------- initialize components --------------------
	
	private void initComponents(Container cp)
	{
		bt_prev = new JButton("Prev");
		bt_prev.addActionListener(this);
		
		bt_next = new JButton("Next");
		bt_next.addActionListener(this);
		
		cb_jump = new JComboBox();
		cb_jump.setMaximumRowCount(20);
		cb_jump.addItemListener(this);
		cb_jump.setFocusable(false);
		
		tf_annotator = new JTextField(str_userID);
		tf_annotator.setEditable(false);
		tf_annotator.setFocusable(false);
		
		ta_sentence = new JTextArea();
		ta_sentence.setEditable(false);
	//	ta_sentence.setFocusable(false);
		ta_sentence.setBackground(Color.white);
		ta_sentence.setRows(4);
		ta_sentence.setLineWrap(true);
		
		tv_tree = new JBTreePanel();
		
		framesetPanel = new JBFramesetPanel();
		framesetPanel.setBorder(new TitledBorder("Frameset View"));
		
		argPanel = new JBArgPanel(tv_tree, this);
		argPanel.setBorder(new TitledBorder("Argument View"));
		
		mbar = new JBMenuBar(this);
		setJMenuBar(mbar);
	}

	private JPanel getTreePanel()
	{
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());
		
		// top of the treeview
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		
		JPanel top1 = new JPanel();
		top1.setLayout(new GridLayout(0,4));
		top1.add(bt_prev);
		top1.add(bt_next);
		top1.add(cb_jump);
		top1.add(tf_annotator);
		top.add(top1, BorderLayout.NORTH);
		
		if (isGold())
		{
			lm_gold = new DefaultListModel();
			ls_gold = new JList(lm_gold);
			
			ls_gold.setFont(new Font("Courier", Font.PLAIN, 12));
			ls_gold.addListSelectionListener(this);
			ls_gold.setVisibleRowCount(3);
			ls_gold.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			top.add(new JScrollPane(ls_gold), BorderLayout.SOUTH);
		}
		
		// bottom of the treeview
		JScrollPane bottom = new JScrollPane(ta_sentence);
		bottom.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		treePanel.add(top, BorderLayout.NORTH);
		treePanel.add(tv_tree, BorderLayout.CENTER);
		treePanel.add(bottom, BorderLayout.SOUTH);
		treePanel.setBorder(new TitledBorder("Treebank View"));
		
		return treePanel;
	}
	
	private void initBounds(Container cp)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int fr_wt = (int)screenSize.getWidth() - 50;
		int fr_ht = (int)screenSize.getHeight() - 50;
		
		JSplitPane sp_right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, framesetPanel, argPanel);
		JSplitPane sp_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTreePanel(), sp_right);
		
		sp_main.setDividerLocation((int)(fr_wt*0.65));
		sp_right.setDividerLocation((int)(fr_ht*0.5));

		cp.setLayout(new BorderLayout());
		cp.add(sp_main, BorderLayout.CENTER);
		setBounds(0, 0, fr_wt, fr_ht);
	}
	
	// called from JBOpenDialog
	void initProperties(String language, String[][] dataset)
	{
		String[][] argTag = DataManager.getContents(language, DataManager.ARGS);
		
		str_dataset = dataset;
		mbar.setMenuArgTag(argTag);
		argPanel.updateArgButtons(argTag);
		framesetPanel.setProperties(language, DataManager.getPath(str_dataset, FRAMESET));
		s_language = language;
	}
	
	// called from JBOpenDialog
	void openFile(String[] filename, boolean isNewTask)
	{
		str_annFile = DataManager.getPath(str_dataset, ANN) + "/" + filename[0] + "." + str_userID;
		setTitle(str_frameTitle + " - " + filename[0]);
		
		if (isNewTask)
		{
			if (isGold())
			{
				String taskFile = DataManager.getPath(str_dataset, ANN) + "/" + filename[1];
				pb_origin = new PBReader(taskFile, DataManager.getPath(str_dataset, TREEBANK));
			}
			else
			{
				String taskFile = DataManager.getPath(str_dataset, TASK) + "/" + filename[0];
				pb_origin = new PBReader(taskFile, DataManager.getPath(str_dataset, TREEBANK));
			}
		}
		else
			pb_origin = new PBReader(str_annFile, DataManager.getPath(str_dataset, TREEBANK));
		
		if (isGold())
		{
			pb_more = new PBReader[filename.length-1];
			for (int i=1; i<filename.length; i++)
			{
				filename[i] = DataManager.getPath(str_dataset, ANN) + "/" + filename[i];
				pb_more[i-1] = new PBReader(filename[i], DataManager.getPath(str_dataset, TREEBANK));
			}
		}
			
		framesetPanel.initPBReader(pb_origin);
		
		cb_jump.removeAllItems();
		for (int i=0; i<pb_origin.getSize(); i++)
			cb_jump.insertItemAt(i, i);
		cb_jump.setSelectedIndex(0);
	}
	
	// ---------------------- Listener/Adpater Start ----------------------
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == mbar.fileOpen)				menuFileOpen();
		else if (e.getSource() == mbar.fileSave)		menuFileSave();
		else if (e.getSource() == mbar.fileSaveAs)		menuFileSaveAs();
		else if (e.getSource() == mbar.fileQuit)		System.exit(0);
		else if (e.getSource() == mbar.tbPrev || e.getSource() == bt_prev)	actionBtPrev();
		else if (e.getSource() == mbar.tbNext || e.getSource() == bt_next)	actionBtNext();
		else if (e.getSource() == mbar.tbJump)			menuTbJump();
		else if (e.getSource() == mbar.tbView)			menuTbView();
		else if (e.getSource() == mbar.fsPrev)			framesetPanel.prevRoleset();
		else if (e.getSource() == mbar.fsNext)			framesetPanel.nextRoleset();
		else if (e.getSource() == mbar.fsExample)		framesetPanel.showExample();
		else if (e.getSource() == mbar.fsViewArg)		tv_tree.viewArgument();
		else if (menuArgumentArg(e))	;
		else if (menuArgumentFunc(e))	;
		else if (e.getSource() == mbar.argNoArg)		argPanel.updateArg(e.getActionCommand());
		else if (e.getSource() == mbar.helpAbout)		menuHelpAbout();
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == cb_jump && cb_jump.getSelectedIndex() >= 0)
		{
			menuFileSave();
			pb_origin.setIndex(cb_jump.getSelectedIndex());
			updateAll();
			updateGoldList();
		}
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
        if (e.getSource() == ls_gold && ls_gold.getSelectedIndex() > 0)
        {
        	pb_origin.copyCurrent(pb_more[ls_gold.getSelectedIndex()-1]);
			updateAll();
			updateGoldTopList();
        }
    }
		
	private void updateAll()
	{
		tv_tree.setTree(pb_origin.getTBTree());
		ta_sentence.setText(pb_origin.getTBTree().getSentence());
		framesetPanel.updateFrameset(pb_origin.getLemma());
	}
	
	private void updateGoldList()
	{
		if (isGold())
		{
			lm_gold.removeAllElements();
			lm_gold.addElement(StringManager.addIndent(pb_origin.getAnnotator(), 12) + pb_origin.getTBTree().toPropbank());
			for (int i=0; i<pb_more.length; i++)
			{
				pb_more[i].setIndex(cb_jump.getSelectedIndex());
				lm_gold.addElement(StringManager.addIndent(pb_more[i].getAnnotator(), 12) + pb_more[i].getTBTree().toPropbank());
			}
			ls_gold.invalidate();
		}
	}
	
	void updateGoldTopList()
	{
		if (isGold())
		{
			lm_gold.setElementAt(StringManager.addIndent(pb_origin.getAnnotator(), 12) + pb_origin.getTBTree().toPropbank(), 0);
			ls_gold.invalidate();
		}
	}
	
	// ---------------------- Menu-File Action ----------------------
	
	private void menuFileOpen()
	{
		new JBOpenDialog(this, true, i_maxAnn);
	}
	
	private void menuFileSave()
	{
		menuFileSave(str_annFile);
	}
	
	private void menuFileSave(String filename)
	{
		pb_origin.setAnnotator(tf_annotator.getText());
		pb_origin.toFile(filename);
	}
	
	// str_annFile stays
	private void menuFileSaveAs()
	{
		JDCFileDialog fd_load = new JDCFileDialog(this);
		String filename = fd_load.save();
		
		if (filename != null && pb_origin != null)
			menuFileSave(filename);
	}

// ------------------ Menu-Treebank Action ------------------
	
	private void actionBtPrev()
	{
		for (int i=pb_origin.getIndex(); i>0; i--)
		{
			pb_origin.setIndex(i-1);
			if (!isGold() || !b_skip || !isGoldSame())	break;
		}
		
		cb_jump.setSelectedIndex(pb_origin.getIndex());
	}
	
	private void actionBtNext()
	{
		for (int i=pb_origin.getIndex(); i<pb_origin.getSize()-1; i++)
		{
			pb_origin.setIndex(i+1);
			if (!isGold() || !b_skip || !isGoldSame())	break;
		}
		
		cb_jump.setSelectedIndex(pb_origin.getIndex());
	}
	
	private boolean isGoldSame()
	{
		String tmp = pb_origin.getTBTree().toPropbank();
		for (int i=0; i<pb_more.length; i++)
		{
			pb_more[i].setIndex(pb_origin.getIndex());
			if (!tmp.equals(pb_more[i].getTBTree().toPropbank()))
				return false;
		}
		
		pb_origin.setAnnotator(GOLD);
		return true;
	}
	
	private void menuTbJump()
	{
		String str = new JOptionPane().showInputDialog(this, "Jump to", "Jump to");
		if (StringManager.isInteger(str))
		{
			int index = Integer.parseInt(str);
			
			if (0 <= index && index < cb_jump.getItemCount())
				cb_jump.setSelectedIndex(index);
		}
	}
	
	private void menuTbView()
	{
		new JDCTextAreaFrame("Text View", tv_tree.getTree().toTextTree());
	}
	
	// ------------------ Menu-Argument Action ------------------
	
	private boolean menuArgumentArg(ActionEvent e)
	{
		for (int i=0; i<mbar.argArg.length; i++)
		{
			if (e.getSource() == mbar.argArg[i])
			{
				argPanel.updateArg(e.getActionCommand());
				return true;
			}
		}
		
		return false;
	}
	
	private boolean menuArgumentFunc(ActionEvent e)
	{
		for (int i=0; i<mbar.argFunc.length; i++)
		{
			if (e.getSource() == mbar.argFunc[i])
			{
				argPanel.setFunction(e.getActionCommand());
				return true;
			}
		}
		
		return false;
	}
	
	private void menuHelpAbout()
	{
		String msg = str_frameTitle + "\n";
		msg += "Jinho D. Choi\n";
		msg += "University of Colorado\n\n";
		msg += "http://verbs.colorado.edu/~choijd/jubilee";
		
		new JOptionPane().showMessageDialog(this, msg, "About", JOptionPane.INFORMATION_MESSAGE);
	}
}
