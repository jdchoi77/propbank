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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnRole extends EnElement implements ActionListener
{
	private static final long serialVersionUID = 3610160758945242137L;
	static public final int WIDTH = EnEditor.WIDTH - 50;
	
	private EnRoles				e_parent;
	private JComboBox<String>	cb_n;
	private JComboBox<String>	cb_f;
	private JComboBox<String>	cb_drel;
	private JTextField			tf_descr;
	
	private JButton bt_removeRole;
	private JButton bt_addVerbNetRole;
	private JButton bt_addFrameNetRole;
	private List<AbstractExternalRole> l_externalRoles;
	
	/**
	 * Initializes role panel.
	 * This panel is called by {@link EnRoles}.
	 * @param parent roles panel
	 * @param element role XML element
	 */
	public EnRole(EnRoles parent, Element element)
	{
		super(element);
		e_parent = parent;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder("Role"));
		
		add(Box.createVerticalStrut(V_GAP));
		initAttributes();
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_EN)) initExternalRoles();
	}

	private void initAttributes()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setPreferredSize(new Dimension(WIDTH, FD_HEIGHT));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel(EnLib.N+": "));
		cb_n = new JComboBox<String>(EnLib.ARR_N);
		String n = getAttribute(EnLib.N).toLowerCase();
		if (!contains(cb_n, n))	cb_n.addItem(n);
		cb_n.setSelectedItem(n);
		pn.add(cb_n);
		
		pn.add(Box.createHorizontalStrut(H_GAP));
		pn.add(new JLabel(EnLib.F+": "));
		cb_f = new JComboBox<String>(EnLib.ARR_F);
		String f = getAttribute(EnLib.F).toLowerCase();
		if (!contains(cb_f, f))	cb_f.addItem(f);
		cb_f.setSelectedItem(f);
		pn.add(cb_f);
		
		pn.add(Box.createHorizontalStrut(H_GAP));
		pn.add(new JLabel(EnLib.DESCR+": "));
		tf_descr = new JTextField(getAttribute(EnLib.DESCR));
		pn.add(tf_descr);
		
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_HI))
		{
			pn.add(Box.createHorizontalStrut(H_GAP));
			pn.add(new JLabel(EnLib.DREL+": "));
			cb_drel = new JComboBox<String>(EnLib.ARR_DREL);
			String drel = getAttribute(EnLib.DREL).toLowerCase();
			if (!contains(cb_drel, drel))	cb_drel.addItem(drel);
			cb_drel.setSelectedItem(drel);
			pn.add(cb_drel);
		}
		
		pn.add(Box.createHorizontalStrut(5));
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_EN))
		{
			bt_addVerbNetRole = new JButton("VN+");
			bt_addVerbNetRole.addActionListener(this);
			pn.add(bt_addVerbNetRole);
			
			bt_addFrameNetRole = new JButton("FN+");
			bt_addFrameNetRole.addActionListener(this);
			pn.add(bt_addFrameNetRole);
		}
		
		bt_removeRole = new JButton("Remove");
		bt_removeRole.addActionListener(this);
		pn.add(bt_removeRole);
		
		add(pn);
	}
	
	private void initExternalRoles()
	{
		l_externalRoles = new ArrayList<AbstractExternalRole>();
		initExternalRolesAux(EnLib.VNROLE);
		initExternalRolesAux(EnLib.FNROLE);
	}
	
	private void initExternalRolesAux(String tagName)
	{
		NodeList list = getElementsByTagName(tagName);
		int i, size = list.getLength();
		
		for (i=0; i<size; i++)
			addExternalRole((Element)list.item(i));
	}
	
	public void addExternalRole(Element eRole)
	{
		AbstractExternalRole role = eRole.getTagName().equals(EnLib.VNROLE) ? new VerbNetRole(this, eRole) : new FrameNetRole(this, eRole);
		l_externalRoles.add(role);
		add(role);
	}
	
	public void removeExternalRole(AbstractExternalRole externalRole) 
	{
		removeChild(externalRole.getElement());
		l_externalRoles.remove(externalRole);			
		remove(externalRole);
		e_parent.validate();
	}

	/** Saves all attributes and vnroles. */
	public void save()
	{
		setAttribute(EnLib.N    , (String)cb_n.getSelectedItem());
		setAttribute(EnLib.F    , (String)cb_f.getSelectedItem());
		setAttribute(EnLib.DESCR, tf_descr.getText());
		
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_HI))
		{
			setAttribute(EnLib.DREL, (String)cb_drel.getSelectedItem());
		}
		
		if (EnEditor.LANGUAGE.equals(EnLib.LANG_EN))
		{
			for (AbstractExternalRole role : l_externalRoles)
				role.save();
		}
	}
	
	/**
	 * Initializes menu actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_addVerbNetRole)
		{
			addExternalRoleGUI(EnEditor.createElement(EnLib.VNROLE));
		}
		else if (e.getSource() == bt_addFrameNetRole)
		{
			addExternalRoleGUI(EnEditor.createElement(EnLib.FNROLE));
		}
		else if (e.getSource() == bt_removeRole)
		{
			e_parent.removeRole(this);
		}
	}
	
	private void addExternalRoleGUI(Element eRole)
	{
		appendChild(eRole);
		addExternalRole(eRole);
		e_parent.validate();
	}
}

