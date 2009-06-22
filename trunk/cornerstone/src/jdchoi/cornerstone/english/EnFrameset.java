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

/**
 * <b>Last update:</b> 06/16/2009
 * @author Jinho D. Choi
 */
import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

public class EnFrameset extends EnElement
{
	private EnNote              en_note;
	private ArrayList<EnPredicate> ar_predicate;
	private JTabbedPane         tb_predicate;
	
	/**
	 * Initializes frameset panel.
	 * This panel is called by {@link EnEditor}
	 * @param eFrameset frameset XML element
	 */
	public EnFrameset(Element eFrameset)
	{
		super(eFrameset);
		setLayout(new BorderLayout());
		
		initNotes();
		initPredicates();
	}
	
	private void initNotes()
	{
		en_note = getNote("Frameset note");
		add(en_note, BorderLayout.NORTH);
	}
	
	private void initPredicates()
	{
		ar_predicate = new ArrayList<EnPredicate>();
		tb_predicate = new JTabbedPane();
		
		// initialize predicates
		NodeList list = getElementsByTagName(EnLib.PREDICATE);
		for (int i=0; i<list.getLength(); i++)
			addPredicate((Element)list.item(i));
		
		// select the first predicate
		if (list.getLength() > 0)
			tb_predicate.setSelectedIndex(0);
		
		add(tb_predicate, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a predicate.
	 * @param ePredicate predicate element to be added
	 */
	public void addPredicate(Element ePredicate)
	{
		EnPredicate enPredicate = new EnPredicate(ePredicate);
		
		ar_predicate.add(enPredicate);
		tb_predicate.addTab(enPredicate.lemma, enPredicate);
		tb_predicate.setSelectedComponent(enPredicate);
	}
	
	/**
	 * Edits the current predicate lemma.
	 * @param lemma new lemma
	 */
	public void editPredicate(String lemma)
	{
		int currIndex = tb_predicate.getSelectedIndex();
		
		ar_predicate.get(currIndex).lemma = lemma;
		tb_predicate.setTitleAt(currIndex, lemma);
	}
	
	/** Removes the current predicate. */
	public void removePredicate()
	{
		EnPredicate enPredicate = getCurrPredicate();
		
		removeChild(enPredicate.getElement());
		ar_predicate.remove(enPredicate);
		tb_predicate.remove(enPredicate);
	}
	
	/**
	 * Returns the currently selected predicate.
	 * @return currently selected predicate
	 */
	public EnPredicate getCurrPredicate()
	{
		return (EnPredicate)tb_predicate.getSelectedComponent();
	}
	
	/**
	 * Returns true if predicate {@link lemma} exists
	 * @param lemma predicate lemma
	 * @return true if predicate {@link lemma} exists
	 */
	public boolean existPredicate(String lemma)
	{
		for (EnPredicate enPredicate : ar_predicate)
			if (enPredicate.lemma.equals(lemma))	return true;
		
		return false;
	}
	
	/**
	 * Returns true if roleset 'id' exists.
	 * @see EnPredicate#existRoleset(String)
	 * @param id roleset ID
	 * @return true if roleset 'id' exists
	 */
	public boolean existRoleset(String id)
	{
		for (EnPredicate enPredicate : ar_predicate)
			if (enPredicate.existRoleset(id))	return true;
		
		return false;
	}
	
	/** 
	 * Selects the previous predicate.
	 * If there is none, select the last predicate.
	 */
	public void selectPrevPredicate()
	{
		int count = tb_predicate.getTabCount();
		tb_predicate.setSelectedIndex((tb_predicate.getSelectedIndex()+count-1)%count);
	}
	
	/** 
	 * Selects the next predicate.
	 * If there is none, select the first predicate.
	 */
	public void selectNextPredicate()
	{
		int count = tb_predicate.getTabCount();
		tb_predicate.setSelectedIndex((tb_predicate.getSelectedIndex()+1)%count);
	}
	
	/**
	 * Returns true if predicate exists.
	 * @return true if predicate exists
	 */
	public boolean existPredicate()
	{
		return (tb_predicate.getTabCount() > 0);
	}
	
	/** Saves all elements. */
	public void save()
	{
		en_note.save();
		for (EnPredicate enPredicate : ar_predicate)
			enPredicate.save();
	}
}
