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
package jdchoi.cornerstone.chinese;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class ChVerb extends ChElement
{
	private ChComment             ch_comment;
	private ChId                  ch_id;
	private ArrayList<ChFrameset> ar_frameset;
	private JTabbedPane           tb_frameset;
	
	/**
	 * Initializes verb panel.
	 * This panel is called from {@link ChEditor}.
	 * @param eVerb verb XML element
	 */
	public ChVerb(Element eVerb)
	{
		super(eVerb);
		setLayout(new BorderLayout());
		
		initNorth();
		initFramesets();
	}
	
	private void initNorth()
	{
		NodeList list = getElementsByTagName(ChLib.ID);
		Element   eId = (list.getLength() > 0) ? (Element)list.item(0) : ChEditor.createEmptyId();
		
		ch_comment = getComment("Verb comment");
		ch_id      = new ChId(eId);
		
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));
		pn.add(ch_comment);
		pn.add(Box.createVerticalStrut(V_GAP));
		pn.add(ch_id);

		add(pn, BorderLayout.NORTH);
	}
	
	private void initFramesets()
	{
		ar_frameset = new ArrayList<ChFrameset>();
		tb_frameset = new JTabbedPane();
		
		// initialize framesets
		NodeList list = getElementsByTagName(ChLib.FRAMESET);
		for (int i=0; i<list.getLength(); i++)
			addFrameset((Element)list.item(i));
		
		// select the first frameset
		if (list.getLength() > 0)
			tb_frameset.setSelectedIndex(0);
		
		add(tb_frameset, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a frameset.
	 * @param eFrameset frameset XML element to add
	 */
	public void addFrameset(Element eFrameset)
	{
		ChFrameset chFrameset = new ChFrameset(this, eFrameset);
		
		ar_frameset.add(chFrameset);
		tb_frameset.addTab(chFrameset.id, chFrameset);
		tb_frameset.setSelectedComponent(chFrameset);
	}
	
	/**
	 * Edits the selected frameset ID.
	 * @param id new frameset ID
	 */
	public void editFramesetId(String id)
	{
		int currIndex = tb_frameset.getSelectedIndex();
		
		ar_frameset.get(currIndex).id = id;
		tb_frameset.setTitleAt(currIndex, id);
	}
	
	/** Removes the selected frameset. */
	public void removeFrameset()
	{
		ChFrameset chFrameset = getCurrFrameset();
		
		removeChild(chFrameset.getElement());
		ar_frameset.remove(chFrameset);
		tb_frameset.remove(chFrameset);
	}
	
	/**
	 * Returns the selected frameset.
	 * @return selected frameset
	 */
	public ChFrameset getCurrFrameset()
	{
		return (ChFrameset)tb_frameset.getSelectedComponent();
	}
	
	/**
	 * Returns true if frameset exists.
	 * @return true if frameset exists
	 */
	public boolean existFrameset()
	{
		return !ar_frameset.isEmpty();
	}
	
	/**
	 * Returns true if frameset.{@link id} exists.
	 * @param id frameset ID
	 * @return true if frameset.{@link id} exists
	 */
	public boolean existFrameset(String id)
	{
		for (ChFrameset chFrameset : ar_frameset)
			if (chFrameset.id.equals(id))	return true;
		
		return false;
	}
	
	/** 
	 * Selects the previous frameset.
	 * If there is none, select the last frameset.
	 */
	public void selectPrevFrameset()
	{
		int count = tb_frameset.getTabCount();
		tb_frameset.setSelectedIndex((tb_frameset.getSelectedIndex()+count-1)%count);
	}
	
	/** 
	 * Selects the next frameset.
	 * If there is none, select the first frameset.
	 */
	public void selectNextFrameset()
	{
		int count = tb_frameset.getTabCount();
		tb_frameset.setSelectedIndex((tb_frameset.getSelectedIndex()+1)%count);
	}
	
	/** Validates the selected frameset. */
	public void validateFrameset()
	{
		int currIndex = tb_frameset.getSelectedIndex();
		tb_frameset.setSelectedIndex(-1);
		tb_frameset.setSelectedIndex(currIndex);
	}

	/** Saves all elements. */
	public void save()
	{
		ch_comment.save();
		ch_id.save();
		
		for (ChFrameset chFrameset : ar_frameset)
			chFrameset.save();
	}
}
