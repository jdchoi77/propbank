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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnRel extends EnElement implements ActionListener
{
	private EnExample  parent;
	private JTextField tf_content;
	private JComboBox  cb_f;
	private JButton    bt_remove;
	
	/**
	 * Initializes relation panel.
	 * This panel is called from {@link EnExample}.
	 * @param parent example panel
	 * @param eRel rel XML element
	 */
	public EnRel(EnExample parent, Element eRel)
	{
		super(eRel);
		this.parent = parent;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(EnExampleFrame.WIDTH, FD_HEIGHT+V_GAP));
		
		initComponents();
		initBounds();
	}

	private void initComponents()
	{
		tf_content = new JTextField(getTextContent());
		
		cb_f = new JComboBox(EnLib.ARR_F);
		String f = getAttribute(EnLib.F).toLowerCase();
		if (!contains(cb_f, f))	cb_f.addItem(f);
		cb_f.setSelectedItem(f);
		
		bt_remove = new JButton("Remove");
		bt_remove.addActionListener(this);
	}
	
	private void initBounds()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setPreferredSize(new Dimension(EnExampleFrame.WIDTH, FD_HEIGHT));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel(EnLib.REL+":"));
		pn.add(Box.createHorizontalStrut(45));
		
		pn.add(new JLabel(EnLib.F+": "));
		pn.add(cb_f);
		pn.add(Box.createHorizontalStrut(H_GAP));

		pn.add(new JLabel("text: "));
		pn.add(tf_content);
		pn.add(Box.createHorizontalStrut(5));
		
		pn.add(bt_remove);
		
		add(Box.createVerticalStrut(V_GAP));
		add(pn);
	}
	
	/** Saves content and attributes. */
	public void save()
	{
		setTextContent(tf_content.getText()); 
		setAttribute(EnLib.F, (String)cb_f.getSelectedItem());
	}
		
	/**
	 * Removes this relation.
	 * @see EnExample#removeRel(EnRel)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			parent.removeRel(this);
	}
}
