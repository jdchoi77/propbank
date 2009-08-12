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
package cornerstone.english;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnExampleFrame extends JFrame implements ActionListener
{ 
	static  final int WIDTH  = 700;
	private final int HEIGHT = 670;
	
	private Element              e_frame;
	private ArrayList<EnExample> ar_example;
	private JTabbedPane          tb_example;
	private EnExampleMenuBar     mn_bar;
	private int                  i_numExamples;
	
	/**
	 * Initializes example frame.
	 * @param eFrame frame XML element
	 */
	public EnExampleFrame(Element eFrame)
	{
		super("Examples");
		
		setLayout(new BorderLayout());
		setBounds(10, 10, WIDTH, HEIGHT);
		setVisible(false);
		addComponentListener(new ResizeAdapter());
		
		i_numExamples = 0;
		e_frame       = eFrame;
		initMenuBar();
		initExamples();
	}
	
	private void initMenuBar()
	{
		mn_bar = new EnExampleMenuBar(this);
		setJMenuBar(mn_bar);
	}

	private void initExamples()
	{
		ar_example = new ArrayList<EnExample>();
		tb_example = new JTabbedPane();
		
		// initialize examples
		NodeList list = e_frame.getElementsByTagName(EnLib.EXAMPLE);
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
		EnExample enExample = new EnExample(eExample);
		
		ar_example.add(enExample);
		tb_example.addTab(Integer.toString(i_numExamples++), enExample);
		tb_example.setSelectedComponent(enExample);
	}
	
	/** Saves all examples. */
	public void save()
	{
		for (EnExample enExample : ar_example)
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
		else if (e.getSource() == mn_bar.editAddRel)		menuAddRel();
		
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
		Element eExample = EnEditor.createEmptyExample();
		
		e_frame.appendChild(eExample);
		addExample(eExample);
	}
	
	/** Removes the selected example. */
	public void menuRemoveExample()
	{
		EnExample enExample = (EnExample)tb_example.getSelectedComponent();
		
		if (enExample != null)
		{
			e_frame.removeChild(enExample.getElement());
			ar_example.remove(enExample);
			tb_example.remove(enExample);
		}
	}
	
	/**
	 * Adds an argument.
	 * @see EnExample#addArg(Element)
	 */
	public void menuAddArg()
	{
		if (tb_example.getTabCount() < 1)	return;
		
		EnExample enExample = (EnExample)tb_example.getSelectedComponent();
		Element   eArg      = EnEditor.createEmptyArg();
		
		enExample.appendChild(eArg);
		enExample.addArg(eArg);
		enExample.validate();
	}
	
	/**
	 * Adds an argument.
	 * @see EnExample#addRel(Element)
	 */
	public void menuAddRel()
	{
		if (tb_example.getTabCount() < 1)	return;
		
		EnExample enExample = (EnExample)tb_example.getSelectedComponent();
		Element   eRel      = EnEditor.createEmptyRel();
		
		enExample.appendChild(eRel);
		enExample.addRel(eRel);
		enExample.validate();
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
	
	private class ResizeAdapter extends ComponentAdapter
	{
		public void componentResized(ComponentEvent event)
		{
			setSize(WIDTH, getHeight());
		}
	}
}
