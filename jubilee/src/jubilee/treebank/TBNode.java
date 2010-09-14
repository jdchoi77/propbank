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
import java.util.ArrayList;

import jubilee.util.StringManager;

/**
 * 'TBNode' represents a node in Treebank.  It contains the following fields.
 * <pre>
 * Member fields:
 * pos - part-of-speech tag showing both bracket-labels and functional-tags (i.e. NP-SBJ)
 * word - lexical word (non-lemmatized)
 * arg - Propbank argument-tag
 * loc - relative location (terminalIndex:height[*|,|;terminalIndex:height])
 * parent - the parent node
 * children vector - the children nodes 
 * </pre>
 * @author Jinho D. Choi
 * <b>Last update:</b> 9/22/2007
 */
public class TBNode
{
	String pos, word, arg, loc;
	TBNode parent;
	ArrayList<TBNode> children;
	
	/**
	 * Creates a node and assigns its parent and tag.
	 * Every other fields are assigned to null.
	 * @param parent the parent node.
	 * @param pos the pos-tag.
	 */
	public TBNode(TBNode parent, String pos)
	{
		this.parent = parent;
		this.pos = pos;
		word = arg = loc = null;
		children = null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TBNode clone()
	{
		return copySelf(this, null);
	}
	
	private TBNode copySelf(TBNode origin, TBNode parent) 
	{
		TBNode curr = new TBNode(parent, origin.getPos());
		
		curr.word = origin.getWord();
		curr.arg = origin.getArg();
		curr.loc = origin.getLoc();
		
		if (origin.children != null)
		{
			curr.children = new ArrayList<TBNode>();
			
			for (int i=0; i<origin.getChildren().size(); i++)
				curr.children.add(copySelf(origin.children.get(i), curr));
		}
		
		return curr;
	}
	
	// ---------------------------- get*() ----------------------------
	
	/**
	 * Returns its part-of-speech tag.
	 * @return the part-of-speech tag.
	 */
	public String getPos()
	{
		return pos;
	}
	
	/**
	 * Returns its word.
	 * @return (isExist(word)) ? the word : null.
	 */
	public String getWord()
	{
		return word;
	}
	
	/**
	 * Returns its Propbank argument-tag.
	 * @return (isExist(arg)) ? the Propbank argument-tag : null.
	 */
	public String getArg()
	{
		return arg;
	}
	
	/**
	 * Returns its relative location.
	 * @return (isExist(loc)) ? the relative location : null.
	 */
	public String getLoc()
	{
		return loc;
	}
	
	/**
	 * Returns its parent.
	 * @return (isExist(parent)) ? the parent : null.
	 */
	public TBNode getParent()
	{
		return parent;
	}
	
	/**
	 * Returns its children in vector.
	 * @return (isExist(children)) ? the children : null.
	 */
	public ArrayList<TBNode> getChildren()
	{
		return children;
	}
	
	// ---------------------------- set*() ----------------------------
	
	/**
	 * Assigns its word.
	 * @param word the word.
	 */
	public void setWord(String word)
	{
		this.word = StringManager.getUTF8(word);
	}
	
	public void setPos(String pos)
	{
		this.pos = pos;
	}
		
	/**
	 * Assigns its relative location and Propbank argument-tag.
	 * @param loc the relative location.
	 * @param arg the Propbank argument-tag.
	 */
	public void setLocArg(String loc, String arg)
	{
		this.loc = loc;
		this.arg = arg;
	}
	
	/**
	 * Assigns its parent-node.
	 * @param parent the parent-node.
	 */
	public void setParent(TBNode parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Adds a child-node to the node.
	 * @param child the child-node.
	 */
	public void addChild(TBNode child)
	{
		if (children == null)
			children = new ArrayList<TBNode>();
		
		children.add(child);
	}
	
	public String toWords()
	{
		return toWordsAux(this);
	}
	
	private String toWordsAux(TBNode curr)
	{
		ArrayList<TBNode> children = curr.getChildren();
		
		if (children != null)
		{
			StringBuilder build = new StringBuilder();
			
			for (TBNode child : children)
			{
				build.append(toWordsAux(child));
				build.append(" ");
			}

			return build.toString().trim();
		}
		else
			return curr.word;
	}
}
