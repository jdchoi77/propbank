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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Element;

/**
 * <b>Last update:</b> 04/10/2014
 * @author Jinho D. Choi
 */
public class Alias extends EnElement implements ActionListener
{
	private static final long serialVersionUID = -2567870510024554551L;
	
	private Aliases				e_parent;
	private JTextField			tf_alias;
	private JTextField			tf_verbnet;
	private JTextField			tf_framenet;
	private JComboBox<String>	cb_pos;
	private JButton				bt_remove;
	
	public Alias(Aliases parent, Element eAlias)
	{
		super(eAlias);
		e_parent = parent;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(Aliases.WIDTH, FD_HEIGHT+V_GAP));
		
		add(Box.createVerticalStrut(V_GAP));
		add(getComponentPanel());
	}
	
	private JPanel getComponentPanel()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel("pos: "));
		cb_pos = new JComboBox<String>(EnLib.POS_TAGS);
		String pos = getAttribute(EnLib.A_POS).toLowerCase();
		if (!contains(cb_pos, pos))	cb_pos.addItem(pos);
		cb_pos.setSelectedItem(pos);
		cb_pos.setPreferredSize(new Dimension(60, FD_HEIGHT));
		pn.add(cb_pos);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		tf_alias    = initTextField(pn, "alias: ", getTextContent(), 100);
		tf_verbnet  = initTextField(pn, "vn: ", getAttribute(EnLib.VERBNET) , 100);
		tf_framenet = initTextField(pn, "fn: ", getAttribute(EnLib.FRAMENET), 200);
		
		bt_remove = new JButton("Remove");
		bt_remove.setPreferredSize(new Dimension(84, FD_HEIGHT));
		bt_remove.addActionListener(this);
		pn.add(bt_remove);

		return pn;
	}
	
	private JTextField initTextField(JPanel pn, String label, String value,int width)
	{
		JTextField tf = new JTextField(value);
		tf.setPreferredSize(new Dimension(width, FD_HEIGHT));
		
		pn.add(new JLabel(label));
		pn.add(tf);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		return tf;
	}
	
	/** Saves all attributes */
	public void save()
	{
		setTextContent(tf_alias.getText());
		setAttribute(EnLib.A_POS, (String)cb_pos.getSelectedItem());
		setAttribute(EnLib.VERBNET , tf_verbnet .getText());
		setAttribute(EnLib.FRAMENET, tf_framenet.getText());
	}
	
	/**
	 * Removes this vnrole.
	 * @see {@link EnRole#removeExternalRole(EnFnrole)}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			e_parent.removeAlias(this);
	}
}
