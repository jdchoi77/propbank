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
package jubilee.treebank;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import jubilee.propbank.*;

/**
 * 'TBTree' represents a tree in Treebank.
 * The tree consists of TBNode: one head-node followed by its children-nodes.
 * <pre>
 * Opertions: move*(), get*(), set*(), to*()
 * </pre>
 * Last update: 09/22/07
 */
public class TBTree
{
	private TBNode mb_head, mb_curr;
	
	/**
	 * Creates a tree with a head node, and sets its current pointer to the head.
	 * <pre>
	 * The head-node's parent: null
	 * The head-node's children: pre-assigned
	 * </pre>
	 * @param head the head-node.
	 */
	public TBTree(TBNode head)
	{
		mb_head = mb_curr = head;
	}
	
	public String getPredicate(String[] tags)
	{
		String predicate[] = {""};
		int[] index = {-1};
		getPredicateAux(predicate, tags, mb_head, index);
		return predicate[0];
	}
	
	private void getPredicateAux(String[] predicate, String[] tags, TBNode node, int[] index)
	{
		if (node.getChildren() != null)
		{
			for (int i=0; i<node.getChildren().size(); i++)
				getPredicateAux(predicate, tags, node.getChildren().get(i), index);
		}
		else
		{
			index[0]++;
			for (int i=0; i<tags.length; i++)
			{
				if (node.getPos().equalsIgnoreCase(tags[i]))
					predicate[0] += node.getWord() + "/" + index[0] + "/";
			}
		}
	}
	
	public String getArgView()
	{
		String str = "";
		StringTokenizer tokArg = new StringTokenizer(toPropbank());
		while (tokArg.hasMoreTokens())
		{
			StringTokenizer tokTag = new StringTokenizer(tokArg.nextToken(), PBReader.ARG_JOINER);
			StringTokenizer tokLoc = new StringTokenizer(tokTag.nextToken(), PBReader.ANT_FUNC, true);
			
			
			String lexicon = "";
			do
			{
				StringTokenizer tokNum = new StringTokenizer(tokLoc.nextToken(), ":");
				moveTo(Integer.parseInt(tokNum.nextToken()), Integer.parseInt(tokNum.nextToken()));
				lexicon += getWords();
				
				if (tokLoc.hasMoreTokens())	lexicon += " " + tokLoc.nextToken() + " ";
			}
			while (tokLoc.hasMoreTokens());

			String arg = tokTag.nextToken();
			while (tokTag.hasMoreTokens())
				arg += "-" + tokTag.nextToken();
			
			str += arg + ": " + lexicon + "\n";
		}
		
		return str;
	}
	
	public String getRelLoc()
	{
		String[] str = {""};
		getRelLocAux(mb_head, str);
		return str[0];
	}
	
	private void getRelLocAux(TBNode node, String[] str)
	{
		if (node.getChildren() != null)
			for (int i=0; i<node.getChildren().size(); i++)
				getRelLocAux(node.getChildren().get(i), str);
		
		if (node.getArg() != null && node.getArg().equalsIgnoreCase(PBReader.REL) && node.getLoc().length() > str[0].length())
			str[0] = node.getLoc();				
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TBTree clone()
	{
		return new TBTree(mb_head.clone());
	}
	
	// ------------------------ move*() ------------------------
	
	/**
	 * Moves the current pointer to the head.
	 */
	public void moveToHead()
	{
		mb_curr = mb_head;
	}
	
	/**
	 * Moves the current pointer to its height'th ancestor.
	 * If the height is less than 0, the current pointer stays.
	 * If the height is greater than its range, the current pointer moves to its head.
	 * @param height the vertical-height of the node.
	 *        0 indicates self, 1 indicates its parent, 2 indicates its grandparent, etc.
	 */
	public void moveToAncestor(int height)
	{
		for (int i=0; i<height && mb_curr.getParent() != null; i++)
			mb_curr = mb_curr.getParent();
	}
	
	/**
	 * Moves the current pointer to index'th terminal.
	 * If the index is less than 0, it is replaces with 0.
	 * If the index is greater than its range, the current pointer stays.
	 * @param index the horizontal-index of the terminal, starting from 0. 
	 */
	public void moveToTerminal(int index)
	{
		int[] idx = {index};
		moveToTerminalAux(mb_head, idx);
	}
	
	private void moveToTerminalAux(TBNode node, int[] index)
	{
		if (node.getChildren() != null)		// node != leaf
		{
			Iterator<TBNode> it = node.getChildren().iterator();
			
			while (it.hasNext())		// traverse all children
			{
				moveToTerminalAux(it.next(), index);
				if (index[0] < 0)	break;
			}
		}
		else							// node = leaf
		{
			mb_curr = node;		index[0]--;
		}
	}
	
	/**
	 * Moves the current pointer to the height's ancestor of the terminalIndex'th terminal.
	 * This is a combined operation of 'moveToTerminal(int)' and moveToAncestor(int)'.
	 * @param index the index of the terminal.
	 * @param height the height from the terminal.
	 */
	public void moveTo(int index, int height)
	{
		moveToTerminal(index);
		moveToAncestor(height);
	}
	
	// ---------------------------- get*() ----------------------------
	
	/**
	 * Gets the raw sentence reprenting the tree.
	 * @return the raw sentence reprenting the tree.
	 */
	public String getSentence()
	{
		StringBuilder build = new StringBuilder();
		getWordsAux(build, mb_head, " ");
		
		return build.toString();
	}
	
	/**
	 * Gets all words in order including and below the current pointing node.
	 * Words are joined with a space (ex. a boy)
	 * @return all words including and below the current pointing node.
	 */
	public String getWords()
	{
		return getWordsAux(mb_curr, " ").trim();
	}
	
	/**
	 * Gets all words in order including and below the current pointing node.
	 * Words are joined with a joiner (ex. a_boy; when _ is a joiner)).
	 * @param joiner joins each words.
	 * @return all words including and below the current pointing node.
	 */
	public String getWords(String joiner)
	{
		return getWordsAux(mb_curr, joiner).trim();
	}
	
	private String getWordsAux(TBNode node, String joiner)
	{
		String lemma = "";
		
		if (node.getChildren() != null)
		{
			Iterator<TBNode> it = node.getChildren().iterator();
			
			while (it.hasNext())
				lemma += getWordsAux(it.next(), joiner);
		}
		else
			return node.getWord() + joiner;	
		
		return lemma;
	}
	
	private void getWordsAux(StringBuilder build, TBNode node, String joiner)
	{
		if (node.getChildren() != null)
		{
			Iterator<TBNode> it = node.getChildren().iterator();
			
			while (it.hasNext())
				getWordsAux(build, it.next(), joiner);
		}
		else if (!node.pos.equals("-NONE-"))
		{
			build.append(node.getWord());
			build.append(joiner);
		}
	}
	
	/**
	 * Gets the Propbank argument-tag of the current node.
	 * @return the Propbank argument-tag.
	 */
	public String getArg()
	{
		return mb_curr.getArg();
	}
	
	/**
	 * Gets the relative location of the current node.
	 * @return the relative location.
	 */
	public String getLoc()
	{
		return mb_curr.getLoc();
	}
	
	/**
	 * Returns the most recent location of 'arg' in the tree. 
	 * @param arg the Propbank argument-tag for the location.
	 * @return (isExist(arg)) ? the most recent location : "".
	 */
	public String getRecentLoc(String arg)
	{
		String[] locArg = {"", arg};
		return getRecentLocAux(mb_head, locArg);
	}
	
	private String getRecentLocAux(TBNode node, String[] locArg)
	{
		if (node.getArg() != null)
		{
			String loc = node.getLoc();
			String arg = node.getArg();
			
			if (arg.equals(locArg[1]))
			{
				StringTokenizer tok1 = new StringTokenizer(locArg[0], PBReader.ANT_FUNC);
				StringTokenizer tok2 = new StringTokenizer(loc, PBReader.ANT_FUNC);
				if (tok1.countTokens() < tok2.countTokens())
					locArg[0] = loc;
			}
		}
		
		if (node.getChildren() != null)
		{
			Iterator<TBNode> it = node.getChildren().iterator();
			while (it.hasNext())	getRecentLocAux(it.next(), locArg);
		}
		
		return locArg[0];
	}
	
	// ---------------------------- set*() ----------------------------
	
	/**
	 * Assigns a relative location and Propbank argument-tag to the current node.
	 * @param loc the relative location.
	 * @param arg the Propbank argument-tag.
	 */
	public void setArg(String loc, String arg)
	{
		mb_curr.setLocArg(loc, arg);
	}
	
	// ---------------------------- to*() ----------------------------
	
	public String toSentence(boolean isArg)
	{
		return toSentenceAux(mb_head, isArg);
	}
	
	private String toSentenceAux(TBNode curr, boolean isArg)
	{
		String str = "";
		
		if (curr.children == null)
		{
			if (curr.arg != null && isArg)
				return "[_"+curr.arg+" "+curr.word+"] ";
			else
				return curr.word + " ";
		}
		else
		{
			for (int i=0; i<curr.children.size(); i++)
				str += toSentenceAux(curr.children.get(i), isArg);
		}
		
		return str;
	}
	
	/**
	 * Returns the tree as a Propbank annotation format.
	 * @return the Propbank annotation.
	 */
	public String toPropbank()
	{
		Vector<String> vec_arg = new Vector<String>();
		Vector<String> vec_loc = new Vector<String>();
		toPropbankAux(mb_head, vec_arg, vec_loc);

		String str = "";
		for (int i=0; i<vec_arg.size(); i++)
			str += vec_loc.get(i) + PBReader.ARG_JOINER + vec_arg.get(i) + " ";
	
		return str.trim();
	}
	
	// traverse the tree
	private void toPropbankAux(TBNode node, Vector<String> vec_arg, Vector<String> vec_loc)
	{
		addLocToHash(node, vec_arg, vec_loc);
			
		if (node.getChildren() != null)
		{
			Iterator<TBNode> it = node.getChildren().iterator();
			while (it.hasNext())	toPropbankAux(it.next(), vec_arg, vec_loc);
		}
	}
	
	// adds an argument-tag with more tokens to the hashtable
	private void addLocToHash(TBNode node, Vector<String> vec_arg, Vector<String> vec_loc)
	{
		if (node.getArg() != null)
		{
			String loc = node.getLoc();
			String arg = node.getArg();
			
			int idx = 0;
			while ((idx = vec_arg.indexOf(arg, idx)) != -1)
			{
				StringTokenizer tok1 = new StringTokenizer(vec_loc.get(idx), PBReader.ANT_FUNC);
				StringTokenizer tok2 = new StringTokenizer(loc, PBReader.ANT_FUNC);
				
				HashSet<String> set1 = new HashSet<String>();
				HashSet<String> set2 = new HashSet<String>();
				
				while (tok1.hasMoreTokens())	set1.add(tok1.nextToken());
				while (tok2.hasMoreTokens())	set2.add(tok2.nextToken());
				
				if      (set1.containsAll(set2))	return;
				else if (set2.containsAll(set1))	{vec_loc.set(idx, loc);	return;}

			/*	if (tok1.countTokens() < tok2.countTokens())
				{
					String tmp = tok1.nextToken();
					while (tok2.hasMoreTokens())
						if (tok2.nextToken().equals(tmp))
						{
							vec_loc.set(idx, loc);
							return;
						}
				}
				else if (tok1.countTokens() > tok2.countTokens())
				{
					String tmp = tok2.nextToken();
					while (tok1.hasMoreTokens())
						if (tok1.nextToken().equals(tmp))
							return;
				}*/
				
				idx++;
			}
			
			vec_arg.add(arg);	vec_loc.add(loc);
		}
	}
	
	/**
	 * Returns an Object array representing the tree.
	 * For example, for the tree below
	 * <pre>
	 * ( (S
     *     (NP
     *       (AT The)
     *       (NNS children))
     *     (VP
     *       (VBD ate))))
     * </pre>    
     * the representation will be
     * <pre>    
     * {S, {NP, AT, NNS}, {VP, VBD}}
	 * </pre> 
	 * @return the Object array representing the tree.
	 */
	public Object[] toObjectArray()
	{
		return (Object[])toObjectArrayAux(mb_head);
	}
	
	private Object toObjectArrayAux(TBNode node)
	{	
		if (node.getChildren() == null)
			return (Object)node;
		else
		{
			int objSize = node.getChildren().size() + 1;
			Object[] objArr = new Object[objSize];
			objArr[0] = (Object)node;
			
			for (int i=1; i<objArr.length; i++)
				objArr[i] = toObjectArrayAux(node.getChildren().get(i-1));
			
			return objArr;
		}
	}
	
	/**
	 * Returns JTree representing the tree.
	 * This method uses 'toObjectArray' method as an input and produces JTree as an output.
	 * @see JTree
	 * @return the JTree representing the tree.
	 */
	public JTree toJTree()
	{
		DefaultMutableTreeNode root = processHierarchy(toObjectArray());
		JTree tree = new JTree(root);
		
  	    return tree;
	}
	
	private DefaultMutableTreeNode processHierarchy(Object[] hierarchy)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
		DefaultMutableTreeNode child;
		
		for(int i=1; i<hierarchy.length; i++)
		{
			Object nodeSpecifier = hierarchy[i];
			
			if (nodeSpecifier instanceof Object[])  // is node with children
				child = processHierarchy((Object[])nodeSpecifier);
			else
				child = new DefaultMutableTreeNode(nodeSpecifier); // is Leaf
			
			node.add(child);
		}
		
		return(node);
	}
	
	public String toTextTree()
	{
		return toTextTreeAux(mb_head, "");
	}
	
	private String toTextTreeAux(TBNode node, String indent)
	{
		String str = indent + "(" + node.pos;
		if (node.word != null)	return str += " " + node.word + ")";
		
		for (TBNode child : node.getChildren())
			str += "\n" + toTextTreeAux(child, indent+"  ");
		
		return str+")";	
	}
}
