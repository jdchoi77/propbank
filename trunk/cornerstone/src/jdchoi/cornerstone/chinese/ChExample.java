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
package jdchoi.cornerstone.chinese;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/18/2009
 * @author Jinho D. Choi
 */
public class ChExample extends ChElement
{
	private ChComment         ch_comment;
	private ChParse           ch_parse;
	private Vector<ChElement> vt_argV;
	private JPanel            pn_argV;
	
	/**
	 * Initializes 'example' panel.
	 * This panel is called from {@link ChExampleFrame}.
	 * @param eExample 'example' XML element
	 */
	public ChExample(Element eExample)
	{
		super(eExample);
		setLayout(new BorderLayout());
		
		initComment();
		initParse();
		initArguments();
	}
	
	private void initComment()
	{
		ch_comment = getComment("Comment");
		add(ch_comment, BorderLayout.NORTH);
	}
	
	private void initParse()
	{
		NodeList  list = getElementsByTagName(ChLib.PARSE);
		Element eParse = (list.getLength() > 0) ? (Element)list.item(0) 
				                                : ChEditor.createEmptyParse();
		ch_parse = new ChParse(eParse);
		add(ch_parse, BorderLayout.CENTER);
	}
	
	private void initArguments()
	{
		vt_argV = new Vector<ChElement>();
		pn_argV = new JPanel();
		pn_argV.setLayout(new BoxLayout(pn_argV, BoxLayout.Y_AXIS));
		pn_argV.setBorder(new TitledBorder("Arguments"));
		
		NodeList list = getChildNodes();
		for (int i=0; i<list.getLength(); i++)
		{
			Node node = list.item(i);
			
			if (node.getNodeName().equals(ChLib.ARG))
				addArg((Element)node);
			else if (node.getNodeName().equals(ChLib.V))
				addV((Element)node);
		}
		
		add(pn_argV, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds an argument.
	 * @param eArg argument XML element to add
	 */
	public void addArg(Element eArg)
	{
		ChArg chArg = new ChArg(this, eArg);
		
		vt_argV.add(chArg);
		pn_argV.add(chArg);
	}
	
	/**
	 * Removes an argument.
	 * @param chArg arg panel to remove
	 */
	public void removeArg(ChArg chArg)
	{
		removeChild(chArg.getElement());
		vt_argV.remove(chArg);
		pn_argV.remove(chArg);
		validate();
	}
	
	/**
	 * Adds a verb.
	 * @param eV v XML element to add
	 */
	public void addV(Element eV)
	{
		ChV chV = new ChV(this, eV, false);
		
		vt_argV.add(chV);
		pn_argV.add(chV);
	}
	
	/**
	 * Removes a verb.
	 * @param chV v panel to remove
	 */
	public void removeV(ChV chV)
	{
		removeChild(chV.getElement());
		vt_argV.remove(chV);
		pn_argV.remove(chV);
		validate();
	}
	
	/** Saves elements */
	public void save()
	{
		ch_comment.save();
		ch_parse.save();
		for (ChElement chArgV : vt_argV)	chArgV.save();
	}
}
