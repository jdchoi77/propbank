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
package jdchoi.cornerstone.english;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnExample extends EnElement
{
	private JTextField           tf_name;
	private JTextField           tf_type;
	private JTextField           tf_src;
	private EnNote               en_note;
	private EnInflection         en_inflection;
	private EnText               en_text;
	private ArrayList<EnElement> ar_argRel;
	private JPanel               pn_argRel;
	
	public EnExample(Element eExample)
	{
		super(eExample);
		setLayout(new BorderLayout());
		
		initNorth();
		initText();
		initSouth();
	}
	
	private void initNorth()
	{
		// initialize values
		JPanel pnAttr = getAttributePanel();
		en_note       = getNote("Note");
		en_inflection = getInflection();

		// initialize north-pane
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.Y_AXIS));
		
		pnNorth.add(en_note);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		pnNorth.add(pnAttr);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		pnNorth.add(en_inflection);
		
		add(pnNorth, BorderLayout.NORTH);
	}
	
	private JPanel getAttributePanel()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setBorder(new TitledBorder("Attributes"));
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		tf_name = new JTextField(getAttribute(EnLib.NAME));
		tf_name.setPreferredSize(new Dimension(300, FD_HEIGHT));
		pn.add(new JLabel(EnLib.NAME+": "));
		pn.add(tf_name);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		tf_type = new JTextField(getAttribute(EnLib.TYPE));
		tf_type.setPreferredSize(new Dimension(80, FD_HEIGHT));
		pn.add(new JLabel(EnLib.TYPE+": "));
		pn.add(tf_type);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		tf_src = new JTextField(getAttribute(EnLib.SRC));
		tf_src.setPreferredSize(new Dimension(80, FD_HEIGHT));
		pn.add(new JLabel(EnLib.SRC+": "));
		pn.add(tf_src);
		
		return pn;
	}

	private EnInflection getInflection()
	{
		NodeList list = getElementsByTagName(EnLib.INFLECTION);
		
		if ((list.getLength() > 0))	return new EnInflection((Element)list.item(0));
		else						return new EnInflection(EnEditor.createEmptyInflection());
	}
	
	private void initText()
	{
		NodeList list = getElementsByTagName(EnLib.TEXT);
		Element eText = (list.getLength() > 0) ? (Element)list.item(0) 
				                               : EnEditor.createEmptyText();
		en_text = new EnText(eText);
		add(en_text, BorderLayout.CENTER);
	}
	
	private void initSouth()
	{
		ar_argRel = new ArrayList<EnElement>();
		pn_argRel = new JPanel();
		pn_argRel.setLayout(new BoxLayout(pn_argRel, BoxLayout.Y_AXIS));
		pn_argRel.setBorder(new TitledBorder("Arguments"));
		
		NodeList list = getChildNodes();
		for (int i=0; i<list.getLength(); i++)
		{
			Node node = list.item(i);
			
			if (node.getNodeName().equals(EnLib.ARG))
				addArg((Element)node);
			else if (node.getNodeName().equals(EnLib.REL))
				addRel((Element)node);
		}
		
		add(pn_argRel, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds an argument.
	 * @param eArg arg XML element to add
	 */
	public void addArg(Element eArg)
	{
		EnArg enArg = new EnArg(this, eArg);
		
		ar_argRel.add(enArg);
		pn_argRel.add(enArg);
	}
	
	/**
	 * Adds an relation.
	 * @param eRel rel XML element to add
	 */
	public void addRel(Element eRel)
	{
		EnRel enRel = new EnRel(this, eRel);
		
		ar_argRel.add(enRel);
		pn_argRel.add(enRel);
	}
	
	/**
	 * Removes an argument.
	 * @param enArg arg XML to remove
	 */
	public void removeArg(EnArg enArg)
	{
		removeChild(enArg.getElement());
		ar_argRel.remove(enArg);
		pn_argRel.remove(enArg);
		validate();
	}
	
	/**
	 * Removes a relation.
	 * @param enRel rel XML to remove
	 */
	public void removeRel(EnRel enRel)
	{
		removeChild(enRel.getElement());
		ar_argRel.remove(enRel);
		pn_argRel.remove(enRel);
		validate();
	}
	
	/** Saves attributes and elements */
	public void save()
	{
		// attributes
		setAttribute(EnLib.NAME, tf_name.getText());
		setAttribute(EnLib.TYPE, tf_name.getText());
		setAttribute(EnLib.SRC , tf_name.getText());
		
		// elements
		en_note.save();
		en_inflection.save();
		en_text.save();
		for (EnElement enArgRel : ar_argRel)	enArgRel.save();
	}
}
