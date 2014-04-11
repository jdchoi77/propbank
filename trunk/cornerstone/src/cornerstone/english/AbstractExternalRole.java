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
abstract public class AbstractExternalRole extends EnElement implements ActionListener
{
	private static final long serialVersionUID = -5482412588189337833L;
	private String F_CLASS;		// to be a constant value in the future
	private String F_THETA;		// to be a constant value in the future
	
	private EnRole				e_parent;
	private JTextField			tf_class;
	private JComboBox<String>	cb_theta;
	private JButton				bt_remove;
	private String				s_source;	// vn|fn
	private String[]			s_thetas;
	
	/**
	 * Initializes vnrole panel.
	 * This panel is called from {@link EnRole}.
	 * @param parent role panel
	 * @param eRole vnrole XML element
	 */
	public AbstractExternalRole(EnRole parent, Element eRole, String source, String[] thetas, String vclass, String theta)
	{
		super(eRole);
		
		e_parent = parent;
		s_source = source;
		s_thetas = thetas;
		F_CLASS  = vclass;
		F_THETA  = theta;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(EnRole.WIDTH, FD_HEIGHT+V_GAP));
		
		add(Box.createVerticalStrut(V_GAP));
		add(getComponentPanel());
	}
	
	private JPanel getComponentPanel()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel(s_source+"-class: "));
		tf_class = new JTextField(getAttribute(F_CLASS));
		tf_class.setPreferredSize(new Dimension(200, FD_HEIGHT));
		pn.add(tf_class);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		pn.add(new JLabel(s_source+"-theta: "));
		cb_theta = new JComboBox<String>(s_thetas);
		String vntheta = getAttribute(F_THETA).toLowerCase();
		if (!contains(cb_theta, vntheta))	cb_theta.addItem(vntheta);
		cb_theta.setSelectedItem(vntheta);
		cb_theta.setPreferredSize(new Dimension(270, FD_HEIGHT));
		pn.add(cb_theta);
		pn.add(Box.createHorizontalStrut(H_GAP));
		
		bt_remove = new JButton("Remove");
		bt_remove.setPreferredSize(new Dimension(84, FD_HEIGHT));
		bt_remove.addActionListener(this);
		pn.add(bt_remove);
						
		return pn;
	}
	
	/** Saves all attributes */
	public void save()
	{
		setAttribute(F_CLASS, tf_class.getText());
		setAttribute(F_THETA, (String)cb_theta.getSelectedItem());
	}
	
	/**
	 * Removes this vnrole.
	 * @see {@link EnRole#removeExternalRole(EnFnrole)}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			e_parent.removeExternalRole(this);
	}
}
