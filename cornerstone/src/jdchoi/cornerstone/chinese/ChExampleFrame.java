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
import java.awt.event.*;

import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/18/2009
 * @author Jinho D. Choi
 */
public class ChExampleFrame extends JFrame implements ActionListener
{ 
	static  final int WIDTH  = 800;
	private final int HEIGHT = 700;
	
	private Element           e_frame;
	private Vector<ChExample> vt_example;
	private JTabbedPane       tb_example;
	private ChExampleMenuBar  mn_bar;
	private int               i_numExamples;
	
	/**
	 * Initializes example frame.
	 * @param eFrame frame XML element
	 */
	public ChExampleFrame(Element eFrame)
	{
		super("Examples");
		
		setLayout(new BorderLayout());
		setBounds(10, 10, WIDTH, HEIGHT);
		setVisible(false);
		
		e_frame       = eFrame;
		i_numExamples = 0;
		initMenuBar();
		initExamples();
	}
	
	private void initMenuBar()
	{
		mn_bar = new ChExampleMenuBar(this);
		setJMenuBar(mn_bar);
	}

	private void initExamples()
	{
		vt_example = new Vector<ChExample>();
		tb_example = new JTabbedPane();
		
		// initialize examples
		NodeList list = e_frame.getElementsByTagName(ChLib.EXAMPLE);
		for (int i=0; i<list.getLength(); i++)
			addExample((Element)list.item(i));
		
		// select the first example
		if (list.getLength() > 0)
			tb_example.setSelectedIndex(0);
		
		add(tb_example, BorderLayout.CENTER);
	}
	
	/**
	 * Adds an example.
	 * @param eExample example XML element to add
	 */
	public void addExample(Element eExample)
	{
		ChExample enExample = new ChExample(eExample);
		
		vt_example.add(enExample);
		tb_example.addTab(Integer.toString(i_numExamples++), enExample);
		tb_example.setSelectedComponent(enExample);
	}
	
	/** Saves all examples. */
	public void save()
	{
		for (ChExample enExample : vt_example)
			enExample.save();
	}
	
	/**
	 * Initializes menu actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if      (e.getSource() == mn_bar.fileQuit)			menuQuit();
		
		else if (e.getSource() == mn_bar.editAddExample)	menuAddExample();
		else if (e.getSource() == mn_bar.editRemoveExample)	menuRemoveExample();
		
		else if (e.getSource() == mn_bar.editAddArg)		menuAddArg();
		else if (e.getSource() == mn_bar.editAddRel)		menuAddV();
		
		else if (e.getSource() == mn_bar.movePrevExample)	menuPrevExample();
		else if (e.getSource() == mn_bar.moveNextExample)	menuNextExample();
	}
	
	/** Makes this frame not visible. */
	public void menuQuit()
	{
		setVisible(false);
	}
	
	/** Adds an empty example. */
	public void menuAddExample()
	{
		Element eExample = ChEditor.createEmptyExample();
		
		e_frame.appendChild(eExample);
		addExample(eExample);
	}
	
	/** Removes the selected example. */
	public void menuRemoveExample()
	{
		ChExample enExample = getCurrExample();
		
		if (enExample != null)
		{
			e_frame.removeChild(enExample.getElement());
			vt_example.remove(enExample);
			tb_example.remove(enExample);
		}
	}
	
	/**
	 * Adds an argument.
	 * @see ChExample#addArg(Element)
	 */
	public void menuAddArg()
	{
		ChExample enExample = getCurrExample();
		
		if (enExample != null)
		{
			Element eArg = ChEditor.createEmptyArg();
			
			enExample.appendChild(eArg);
			enExample.addArg(eArg);
			enExample.validate();
		}
	}
	
	/**
	 * Adds a verb.
	 * @see ChExample#addV(Element)
	 */
	private void menuAddV()
	{
		ChExample enExample = getCurrExample();
		
		if (enExample != null)
		{
			Element eV = ChEditor.createEmptyV();
			
			enExample.appendChild(eV);
			enExample.addV(eV);
			enExample.validate();
		}
	}
	
	/**
	 * Returns the selected example.
	 * If there is no example, return null.
	 * @return (isExist) ? selected example : null
	 */
	public ChExample getCurrExample()
	{
		return (ChExample)tb_example.getSelectedComponent();
	}
	
	/**
	 * Selects the previous example.
	 * If the previous example does not exist, selects the last example.
	 */
	public void menuPrevExample()
	{
		int count = tb_example.getTabCount();
		if (count > 0)	tb_example.setSelectedIndex((tb_example.getSelectedIndex()+count-1)%count);
	}
	
	/**
	 * Selects the next example.
	 * If the next example does not exist, selects the first example.
	 */
	public void menuNextExample()
	{
		int count = tb_example.getTabCount();
		if (count > 0)	tb_example.setSelectedIndex((tb_example.getSelectedIndex()+1)%count);
	}
}
