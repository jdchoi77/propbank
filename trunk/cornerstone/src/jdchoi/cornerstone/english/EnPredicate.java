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
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/16/2009
 * @author Jinho D. Choi
 */
public class EnPredicate extends EnElement
{
	public  String            lemma;
	private EnNote            en_note;
	private ArrayList<EnRoleset> ar_roleset;
	private JTabbedPane       tb_roleset;
	
	/**
	 * Initializes predicate panel.
	 * This panel is called by {@link EnFrameset}.
	 * @param ePredicate predicate XML element
	 */
	public EnPredicate(Element ePredicate)
	{
		super(ePredicate);
		setLayout(new BorderLayout());
		
		initAttributes();
		initNotes();
		initRolesets();
	}
	
	private void initAttributes()
	{
		lemma = getAttribute(EnLib.LEMMA);
	}
	
	private void initNotes()
	{
		en_note = getNote("Predicate note");
		add(en_note, BorderLayout.NORTH);
	}
	
	private void initRolesets()
	{
		ar_roleset = new ArrayList<EnRoleset>();
		tb_roleset = new JTabbedPane();
		
		// initialize rolesets
		NodeList list = getElementsByTagName(EnLib.ROLESET);
		for (int i=0; i<list.getLength(); i++)
			addRoleset((Element)list.item(i));
		
		// select the first roleset
		if (list.getLength() > 0)
			tb_roleset.setSelectedIndex(0);
		
		add(tb_roleset, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a roleset.
	 * @param eRoleset roleset XML element to add
	 */
	public void addRoleset(Element eRoleset)
	{
		EnRoleset enRoleset = new EnRoleset(this, eRoleset);
		
		ar_roleset.add(enRoleset);
		tb_roleset.addTab(enRoleset.id, enRoleset);
		tb_roleset.setSelectedComponent(enRoleset);
	}
	
	/**
	 * Edits the roleset ID.
	 * @param id new ID
	 */
	public void editRolesetId(String id)
	{
		int currIndex = tb_roleset.getSelectedIndex();
		
		ar_roleset.get(currIndex).id = id;
		tb_roleset.setTitleAt(currIndex, id);
	}
	
	/** Removes the current roleset. */
	public void removeRoleset()
	{
		EnRoleset enRoleset = getCurrRoleset();
		
		removeChild(enRoleset.getElement());
		ar_roleset.remove(enRoleset);
		tb_roleset.remove(enRoleset);
	}
	
	/**
	 * Returns the currently selected roleset.
	 * @return currently selected roleset
	 */
	public EnRoleset getCurrRoleset()
	{
		return (EnRoleset)tb_roleset.getSelectedComponent();
	}
	
	/**
	 * Returns true if roleset 'id' exists.
	 * @param id roleset ID
	 * @return true if roleset 'id' exists
	 */
	public boolean existRoleset(String id)
	{
		for (EnRoleset enRoleset : ar_roleset)
			if (enRoleset.id.equals(id))	return true;
		
		return false;
	}
	
	/** 
	 * Selects the previous roleset.
	 * If there is none, select the last roleset.
	 */
	public void selectPrevRoleset()
	{
		int count = tb_roleset.getTabCount();
		tb_roleset.setSelectedIndex((tb_roleset.getSelectedIndex()+count-1)%count);
	}
	
	/** 
	 * Selects the next roleset.
	 * If there is none, select the first roleset.
	 */
	public void selectNextRoleset()
	{
		int count = tb_roleset.getTabCount();
		tb_roleset.setSelectedIndex((tb_roleset.getSelectedIndex()+1)%count);
	}
	
	/**
	 * Returns true if roleset exists.
	 * @return true if roleset exists
	 */
	public boolean existRoleset()
	{
		return (tb_roleset.getTabCount() > 0);
	}
	
	/** Validates the selected roleset. */
	public void validateRoleset()
	{
		int currIndex = tb_roleset.getSelectedIndex();
		tb_roleset.setSelectedIndex(-1);
		tb_roleset.setSelectedIndex(currIndex);
	}

	/** Saves all elements. */
	public void save()
	{
		setAttribute(EnLib.LEMMA, lemma);
		en_note.save();
		for (EnRoleset enRoleset : ar_roleset)
			enRoleset.save();
	}
}
