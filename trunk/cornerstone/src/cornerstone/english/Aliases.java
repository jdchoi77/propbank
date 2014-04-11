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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class Aliases extends EnElement implements ActionListener
{
	private static final long serialVersionUID = -33842078023169544L;
	static public final int WIDTH  = EnEditor.WIDTH - 35;
	static public final int HEIGHT = 150;
	
	private EnElement   e_parent;
	private JPanel      pn_aliases;
	private List<Alias> l_aliases;
	private JButton     bt_add;
	
	/**
	 * Initializes roles pane.
	 * This panel is called by {@link EnRoleset}. 
	 * @param parent roleset panel
	 * @param eAliases roles XML element
	 */
	public Aliases(EnElement parent, Element eAliases, String title)
	{
		super(eAliases);
		e_parent = parent;
		setLayout(new BorderLayout());
		init(title);
	}
	
	private void init(String title)
	{
		// initialize panel that contains roles
		pn_aliases = new JPanel();
		pn_aliases.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pn_aliases.setPreferredSize(new Dimension(EnEditor.WIDTH, HEIGHT));
		
		// initialize buttons
		bt_add = new JButton(title);
		bt_add.addActionListener(this);
		bt_add.setPreferredSize(new Dimension(new Dimension(EnEditor.WIDTH, FD_HEIGHT)));
		add(bt_add, BorderLayout.NORTH);
		
		// initialize roles
		l_aliases = new ArrayList<Alias>();
		
		NodeList list = getElementsByTagName(EnLib.ALIAS);
		int i, size = list.getLength();
		
		for (i=0; i<size; i++)
			addAlias((Element)list.item(i));
		
		// add scroll-panel that contains roles
		JScrollPane sp = new JScrollPane(pn_aliases);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(sp, BorderLayout.CENTER);
	}
	
	public void addAlias(Element eAlias)
	{
		Alias alias = new Alias(this, eAlias);
		l_aliases.add(alias);
		pn_aliases.add(alias);
	}
	
	public void removeAlias(Alias alias)
	{
		removeChild(alias.getElement());
		l_aliases.remove(alias);
		pn_aliases.remove(alias);
		if      (e_parent instanceof EnRoleset)		((EnRoleset)e_parent).validateRoleset();
		else if (e_parent instanceof EnPredicate)	((EnPredicate)e_parent).validatePredicate();
	}
	
	/** Saves all elements. */
	public void save()
	{
		for (Alias alias : l_aliases)
			alias.save();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_add)
		{
			addAliasGUI(EnEditor.createElement(EnLib.ALIAS));
		}
	}
	
	private void addAliasGUI(Element eAlias)
	{
		appendChild(eAlias);
		addAlias(eAlias);
		pn_aliases.validate();
	}
}
