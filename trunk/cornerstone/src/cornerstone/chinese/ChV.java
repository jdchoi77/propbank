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
package cornerstone.chinese;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/20/2009
 * @author Jinho D. Choi
 */
public class ChV extends ChElement implements ActionListener
{
	private ChElement parent;
	private JButton   bt_remove;
	
	/**
	 * Initializes v panel.
	 * This panel is called from {@link parent}.
	 * @param parent {@link ChElement} = ({@link isFrame}) ? {@link ChFrame} : {@link ChExample}
	 * @param eV v XML element 
	 * @param isFrame true if this is for Frame
	 */
	public ChV(ChElement parent, Element eV, boolean isFrame)
	{
		super(eV);
		this.parent = parent;
		
		bt_remove = new JButton("Remove");
		bt_remove.addActionListener(this);
		
		if (isFrame)	initFrame();
		else			initExample();
	}
	
	private void initFrame()
	{
		setLayout(null);
		setPreferredSize(new Dimension(ChFrame.WIDTH, FD_HEIGHT+V_GAP));
		
		JLabel label = new JLabel("V:");
		label.setBounds(H_GAP_FRONT, V_GAP, 30, FD_HEIGHT);
		add(label);
		
		bt_remove.setBounds(275, V_GAP, 100, FD_HEIGHT);
		add(bt_remove);
	}
	
	private void initExample()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());
		pn.setPreferredSize(new Dimension(ChExampleFrame.WIDTH, FD_HEIGHT));
		
		pn.add(new JLabel(" V:"), BorderLayout.WEST);
		bt_remove.setPreferredSize(new Dimension(84, FD_HEIGHT));
		pn.add(bt_remove, BorderLayout.EAST);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(ChExampleFrame.WIDTH, FD_HEIGHT+V_GAP));
		add(Box.createVerticalStrut(V_GAP));
		add(pn);
	}
	
	/** Saves nothing. */
	public void save() {}
	
	/**
	 * Removes this panel from its {@link ChV#parent}.
	 * @see {@link ChFrame#removeV(ChV)}
	 * @see {@link ChExample#removeV(ChV)}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			parent.removeV(this);
	}
}
