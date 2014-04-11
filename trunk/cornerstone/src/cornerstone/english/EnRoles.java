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
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnRoles extends EnElement
{
	private static final long serialVersionUID = 3648946561855835122L;
	private final int         HEIGHT = 1500;
	
	private EnRoleset         parent;
	private EnNote            en_note;
	private JPanel            pn_role;
	private ArrayList<EnRole> ar_role;
	
	/**
	 * Initializes roles pane.
	 * This panel is called by {@link EnRoleset}. 
	 * @param parent roleset panel
	 * @param eRoles roles XML element
	 */
	public EnRoles(EnRoleset parent, Element eRoles)
	{
		super(eRoles);
		this.parent = parent;
		setLayout(new BorderLayout());

		initNotes();
		initRoles();
	}
	
	private void initNotes()
	{
		en_note = getNote("Roles note");
		add(en_note, BorderLayout.NORTH);
	}
	
	private void initRoles()
	{
		// initialize panel that contains roles
		pn_role = new JPanel();
		pn_role.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pn_role.setPreferredSize(new Dimension(EnEditor.WIDTH, HEIGHT));
		
		// initialize roles
		ar_role = new ArrayList<EnRole>();
		NodeList list = getElementsByTagName(EnLib.ROLE);
		for (int i=0; i<list.getLength(); i++)
			addRole((Element)list.item(i));
		
		// add scroll-panel that contains roles
		JScrollPane sp = new JScrollPane(pn_role);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(sp, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a role.
	 * @param eRole role XML element to add
	 */
	public void addRole(Element eRole)
	{
		EnRole enRole = new EnRole(this, eRole);
		
		ar_role.add(enRole);
		pn_role.add(enRole);
	}
	
	/**
	 * Removes the role.
	 * @param enRole role XML element to remove
	 */
	public void removeRole(EnRole enRole)
	{
		removeChild(enRole.getElement());
		ar_role.remove(enRole);
		pn_role.remove(enRole);
		parent.validateRoleset();
	}
	
	/** Saves all elements. */
	public void save()
	{
		en_note.save();
		for (EnRole enRole : ar_role)	enRole.save();
	}
}
