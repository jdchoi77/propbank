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
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnRoleset extends EnElement
{
	public  String         id;
	
	private EnPredicate    parent;
	private JTextField     tf_name;
	private JTextField     tf_vncls;
	private JTextField     tf_framnet;
	private EnNote         en_note;
	private EnRoles        en_roles;
	private EnExampleFrame en_example;
	
	/**
	 * Initializes roleset panel.
	 * This panel is called by {@link EnPredicate}.
	 * @param parent predicate panel
	 * @param eRoleset roleset XML element
	 */
	public EnRoleset(EnPredicate parent, Element eRoleset)
	{
		super(eRoleset);
		this.parent = parent;
		
		setLayout(new BorderLayout());
		initAttributesAndNotes();
		initRoles();
		initExample();
	}
	
	private void initAttributesAndNotes()
	{
		en_note = getNote("Roleset note");
		id      = getAttribute(EnLib.ID);
		
		// attributes
		JPanel pnAttr = new JPanel();
		pnAttr.setLayout(new BoxLayout(pnAttr, BoxLayout.X_AXIS));
		pnAttr.add(Box.createHorizontalStrut(8));
		
		pnAttr.add(new JLabel(EnLib.NAME+": "));
		tf_name = new JTextField(getAttribute(EnLib.NAME));
		tf_name.setPreferredSize(new Dimension(300, FD_HEIGHT));
		pnAttr.add(tf_name);
		pnAttr.add(Box.createHorizontalStrut(H_GAP));	
		
		pnAttr.add(new JLabel(EnLib.VNCLS+": "));
		tf_vncls = new JTextField(getAttribute(EnLib.VNCLS));
		tf_vncls.setPreferredSize(new Dimension(90, FD_HEIGHT));
		pnAttr.add(tf_vncls);
		pnAttr.add(Box.createHorizontalStrut(H_GAP));
		
		pnAttr.add(new JLabel(EnLib.FRAMNET+": "));
		tf_framnet = new JTextField(getAttribute(EnLib.FRAMNET));
		tf_framnet.setPreferredSize(new Dimension(90, FD_HEIGHT));
		pnAttr.add(tf_framnet);
		
		// north pane
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.Y_AXIS));
		
		pnNorth.add(en_note);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		pnNorth.add(pnAttr);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		
		add(pnNorth, BorderLayout.NORTH);
	}
	
	private void initRoles()
	{
		NodeList list   = getElementsByTagName(EnLib.ROLES);
		Element  eRoles = (list.getLength() > 0) ? (Element)list.item(0) : EnEditor.createEmptyRoles();
		
		en_roles = new EnRoles(this, eRoles);
		add(en_roles, BorderLayout.CENTER);
	}
	
	private void initExample()
	{
		en_example = new EnExampleFrame(getElement());
	}
	
	/**
	 * Returns roles panel.
	 * @return roles panel
	 */
	public EnRoles getRoles()
	{
		return en_roles;
	}
	
	/** Shows example frame. */
	public void showExamples()
	{
		en_example.setVisible(true);
	}
	
	/**
	 * Validates the selected roleset.
	 * @see EnPredicate#validateRoleset()
	 */
	public void validateRoleset()
	{
		parent.validateRoleset();
	}
	
	/** Saves attributes and elements. */
	public void save()
	{
		setAttribute(EnLib.ID     , id);
		setAttribute(EnLib.NAME   , tf_name.getText());
		setAttribute(EnLib.VNCLS  , tf_vncls.getText());
		setAttribute(EnLib.FRAMNET, tf_framnet.getText());
		
		en_note.save();
		en_roles.save();
		en_example.save();
	}
}
