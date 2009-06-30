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
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

import javax.swing.border.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnRole extends EnElement implements ActionListener
{
	public final int WIDTH = EnEditor.WIDTH - 40;
	
	private EnRoles              parent;
	private JComboBox            cb_n;
	private JComboBox            cb_f;
	private JTextField           tf_descr;
	private ArrayList<EnVnrole>  vt_vnrole;
	private JButton              bt_removeRole;
	private JButton              bt_addVnrole;
	
	/**
	 * Initializes role panel.
	 * This panel is called by {@link EnRoles}.
	 * @param parent roles panel
	 * @param element role XML element
	 */
	public EnRole(EnRoles parent, Element element)
	{
		super(element);
		this.parent = parent;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder("Role"));
		
		add(Box.createVerticalStrut(V_GAP));
		initAttributes();
		initVnrole();
	}

	private void initAttributes()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setPreferredSize(new Dimension(WIDTH, FD_HEIGHT));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel(EnLib.N+": "));
		cb_n = new JComboBox(EnLib.ARR_N);
		String n = getAttribute(EnLib.N).toLowerCase();
		if (!contains(cb_n, n))	cb_n.addItem(n);
		cb_n.setSelectedItem(n);
		pn.add(cb_n);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		pn.add(new JLabel(EnLib.F+": "));
		cb_f = new JComboBox(EnLib.ARR_F);
		String f = getAttribute(EnLib.F).toLowerCase();
		if (!contains(cb_f, f))	cb_f.addItem(f);
		cb_f.setSelectedItem(f);
		pn.add(cb_f);
		pn.add(Box.createHorizontalStrut(H_GAP+15));
		
		pn.add(new JLabel(EnLib.DESCR+": "));
		tf_descr = new JTextField(getAttribute(EnLib.DESCR));
		pn.add(tf_descr);
		pn.add(Box.createHorizontalStrut(5));
		
		bt_addVnrole = new JButton("Add Vnrole");
		bt_addVnrole.addActionListener(this);
		pn.add(bt_addVnrole);
	
		bt_removeRole = new JButton("Remove");
		bt_removeRole.addActionListener(this);
		pn.add(bt_removeRole);
		
		add(pn);
	}
	
	private void initVnrole()
	{
		vt_vnrole     = new ArrayList<EnVnrole>();
		NodeList list = getElementsByTagName(EnLib.VNROLE);
		
		for (int i=0; i<list.getLength(); i++)
			addVnrole((Element)list.item(i));
	}
	
	/**
	 * Adds a vnrole.
	 * @param eVnrole vnrole XML element to add
	 */
	public void addVnrole(Element eVnrole)
	{
		EnVnrole enVnrole = new EnVnrole(this, eVnrole);
		
		vt_vnrole.add(enVnrole);
		add(enVnrole);
	}
	
	/**
	 * Removes a vnrole.
	 * @param enVnrole vnrole element to remove
	 */
	public void removeVnrole(EnVnrole enVnrole) 
	{
		removeChild(enVnrole.getElement());
		vt_vnrole.remove(enVnrole);
		remove(enVnrole);
		parent.validate();
	}

	/** Saves all attributes and vnroles. */
	public void save()
	{
		setAttribute(EnLib.N    , (String)cb_n.getSelectedItem());
		setAttribute(EnLib.F    , (String)cb_f.getSelectedItem());
		setAttribute(EnLib.DESCR, tf_descr.getText());
		
		for (EnVnrole enVnrole : vt_vnrole)		enVnrole.save();
	}
	
	/**
	 * Initializes menu actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_addVnrole)
		{
			Element eVnrole = EnEditor.createEmptyVnrole();
			
			appendChild(eVnrole);
			addVnrole(eVnrole);
			parent.validate();
		}
		else if (e.getSource() == bt_removeRole)
		{
			parent.removeRole(this);
		}
	}
}

