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
package cornerstone.chinese;

import java.util.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/18/2009
 * @author Jinho D. Choi
 */
public class ChMapping extends ChElement
{
	static final int WIDTH = 350;
	
	private ChFrame           parent;
	private ChComment         ch_comment;
	private Vector<ChElement> vt_mapitemV;
	private JPanel            pn_mapitemV;
	
	/**
	 * Initializes mapping panel.
	 * This panel is called from {@link ChFrame}.
	 * @param parent frame panel
	 * @param eMapping mapping XML element
	 */
	public ChMapping(ChFrame parent, Element eMapping)
	{
		super(eMapping);
		this.parent = parent;
		setLayout(new BorderLayout());
		
		initComment();
		initMapitems();
	}
	
	private void initComment()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setPreferredSize(new Dimension(ChFrame.WIDTH, FD_HEIGHT));
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel("Mapping comment: "));
		ch_comment = getComment(null);
		ch_comment.setBorder(new LineBorder(Color.lightGray));
		pn.add(ch_comment);
				
		add(pn, BorderLayout.NORTH);
	}
	
	private void initMapitems()
	{
		vt_mapitemV = new Vector<ChElement>();
		pn_mapitemV = new JPanel();
		pn_mapitemV.setLayout(new BoxLayout(pn_mapitemV, BoxLayout.Y_AXIS));
		
		NodeList list = getChildNodes();
		for (int i=0; i<list.getLength(); i++)
		{
			Node node = list.item(i);
			
			if (node.getNodeName().equals(ChLib.MAPITEM))
				addMapitem((Element)node);
			else if (node.getNodeName().equals(ChLib.V))
				addV((Element)node);
		}
		
		add(pn_mapitemV, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a mapitem.
	 * @param eMapitem mapitem XML element to add
	 */
	public void addMapitem(Element eMapitem)
	{
		ChMapitem chMapitem = new ChMapitem(this, eMapitem);
		
		vt_mapitemV.add(chMapitem);
		pn_mapitemV.add(chMapitem);
	}

	/**
	 * Removes a mapitem.
	 * @param chMapitem mapitem panel to remove
	 */
	public void removeMapitem(ChMapitem chMapitem)
	{
		removeChild(chMapitem.getElement());
		vt_mapitemV.remove(chMapitem);
		pn_mapitemV.remove(chMapitem);
		parent.validateFrameset();
	}
	
	/**
	 * Adds a verb.
	 * @param eV v XML element to add
	 */
	public void addV(Element eV)
	{
		ChV chV = new ChV(this, eV, true);
		
		vt_mapitemV.add(chV);
		pn_mapitemV.add(chV);
	}
		
	/**
	 * Removes a verb.
	 * @param chV v panel to remove
	 */
	public void removeV(ChV chV)
	{
		removeChild(chV.getElement());
		vt_mapitemV.remove(chV);
		pn_mapitemV.remove(chV);
		parent.validateFrameset();
	}
	
	/** Saves elements */
	public void save()
	{
		ch_comment.save();
		for (ChElement chMapitemV : vt_mapitemV)	chMapitemV.save();
	}
}
