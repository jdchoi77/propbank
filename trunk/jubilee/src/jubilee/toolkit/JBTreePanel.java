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
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import jubilee.awt.JDCTextAreaFrame;
import jubilee.propbank.*;
import jubilee.treebank.*;

/**
 * 'TBTreePanel' takes 'TBTree' and shows the tree in 'JTree' format.
 * Last update: 09/22/07
 */
@SuppressWarnings("serial")
public class JBTreePanel extends JPanel
{
	private TBTree tb_tree;
	private JTree j_tree;
	private String prevLoc, prevArg;
	JDCTextAreaFrame fr_argView = null;
	
	/** Puts a JTree in a container.  TBTree has not be initialized yet. */
	public JBTreePanel()
	{
		TreeCellRenderer renderer = new TBTreeRenderer();
		
		tb_tree = null;
		j_tree = new JTree();
		j_tree.setModel(null);
		j_tree.setCellRenderer(renderer);
		prevArg = prevLoc = null;

		setLayout(new BorderLayout());
		add(new JScrollPane(j_tree), BorderLayout.CENTER);
	}
	
	public TBTree getTree()
	{
		return tb_tree;
	}
	
	public void viewArgument()
	{
		if (fr_argView != null)
			fr_argView.dispose();
		fr_argView = new JDCTextAreaFrame("View Arguments", tb_tree.getArgView());
	}
	
	/**
	 * Takes 'tree' as its TBTree, and views the tree in JTree.
	 * @param tree the tree to be viewed.
	 */
	public void setTree(TBTree tree)
	{
		tb_tree = tree;
		j_tree.setModel(tb_tree.toJTree().getModel());
		expandJTree(j_tree);
				
		j_tree.revalidate();
		j_tree.repaint();
	}
	
	private void expandJTree(JTree tree)
	{
		for (int i=0; i<tree.getRowCount(); i++)
			tree.expandRow(i);
	}
	
	/**
	 * Indicates whether or not TBTree is null.
	 * @return isNull(TBTree).
	 */
	public boolean isNull()
	{
		return (tb_tree == null);
	}
	
	/**
	 * Updates the location and argument of the currently selected node.
	 * If (func != null), it automatically finds the most recent node with 'arg' and join the locations.
	 * @param func * | , | ;
	 * @param arg the Propbank argument-tag.
	 */
	public void updateArg(String func, String arg)
	{
		// current node
		int[] selectionRow = j_tree.getSelectionRows();
		String currLoc = getLoc();
		if (currLoc == null)	return;

//		if (tb_tree.getArg() != null && tb_tree.getArg().equals(PBReader.REL))
//			return;
		
		if (arg.equals(JBArgPanel.ERASE))
			tb_tree.setArg(null, null);
		else if (func == null)
			tb_tree.setArg(currLoc, arg);
		else if (func.equals("/"))
		{
			String relLoc = tb_tree.getRelLoc();
			if (!relLoc.equals(""))	tb_tree.setArg(relLoc+","+currLoc, PBReader.REL);
			else					tb_tree.setArg(currLoc, PBReader.REL);
		}
		else if (prevArg != null)
			tb_tree.setArg(prevLoc+func+currLoc, prevArg);
		
		if (tb_tree.getArg() == null)
			prevLoc = prevArg = null;
		else
		{
			prevLoc = tb_tree.getLoc();
			prevArg = tb_tree.getArg();
		}
		
		setTree(tb_tree);		
		j_tree.setSelectionInterval(selectionRow[0], selectionRow[selectionRow.length-1]);
	}
	
	public void updateNull(String arg)
	{
		int[] selectionRow = j_tree.getSelectionRows();
		
		tb_tree.addNull(getLoc(), arg);
		
		setTree(tb_tree);		
		j_tree.setSelectionInterval(selectionRow[0], selectionRow[selectionRow.length-1]);
	}
	
	/**
	 * Gets the relative location of TBTree for the currently selected node in JTree.
	 * The current pointer of TBTree will be set according to the currently selected node in JTree.
	 * @return the relative location.
	 */
	@SuppressWarnings("unchecked")
	private String getLoc()
	{
		// current node
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)j_tree.getLastSelectedPathComponent();
		if (node == null)	return null;
		
		DefaultMutableTreeNode leaf = node.getFirstLeaf();	// first leaf from the current node
		int leafIndex = 0, height = -1;
		
		// get the height of the current node for the first leaf
		Enumeration ef = leaf.pathFromAncestorEnumeration(node);
		while (ef.hasMoreElements())
		{
			ef.nextElement();
			height++;
		}
		
		// get the index of the first leaf
		while (leaf.getPreviousLeaf() != null)
		{
			leaf = leaf.getPreviousLeaf();
			leafIndex++;
		}
		
		tb_tree.moveTo(leafIndex, height);
		return leafIndex + ":" + height;
	}
}