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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnVnrole extends EnElement implements ActionListener
{
	private EnRole     parent;
	private JTextField tf_vncls;
	private JComboBox  cb_vntheta;
	private JButton    bt_remove;
	
	/**
	 * Initializes vnrole panel.
	 * This panel is called from {@link EnRole}.
	 * @param parent role panel
	 * @param eVnrole vnrole XML element
	 */
	public EnVnrole(EnRole parent, Element eVnrole)
	{
		super(eVnrole);
		this.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(parent.WIDTH, FD_HEIGHT+V_GAP));
		
		add(Box.createVerticalStrut(V_GAP));
		add(getComponentPanel());
	}
	
	private JPanel getComponentPanel()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel("- "+EnLib.VNCLS+": "));
		tf_vncls = new JTextField(getAttribute(EnLib.VNCLS));
		tf_vncls.setPreferredSize(new Dimension(108, FD_HEIGHT));
		pn.add(tf_vncls);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		pn.add(new JLabel(EnLib.VNTHETA+": "));
		cb_vntheta = new JComboBox(EnLib.ARR_VNTHETA);
		String vntheta = getAttribute(EnLib.VNTHETA).toLowerCase();
		if (!contains(cb_vntheta, vntheta))	cb_vntheta.addItem(vntheta);
		cb_vntheta.setSelectedItem(vntheta);
		cb_vntheta.setPreferredSize(new Dimension(225, FD_HEIGHT));
		pn.add(cb_vntheta);
		pn.add(Box.createHorizontalStrut(280));
		
		bt_remove = new JButton("Remove");
		bt_remove.setPreferredSize(new Dimension(84, FD_HEIGHT));
		bt_remove.addActionListener(this);
		pn.add(bt_remove);
						
		return pn;
	}
	
	/** Saves all attributes */
	public void save()
	{
		setAttribute(EnLib.VNCLS  , tf_vncls.getText());
		setAttribute(EnLib.VNTHETA, (String)cb_vntheta.getSelectedItem());
	}
	
	/**
	 * Removes this vnrole.
	 * @see {@link EnRole#removeVnrole(EnVnrole)}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			parent.removeVnrole(this);
	}
}
