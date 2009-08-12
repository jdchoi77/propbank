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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/20/2009
 * @author Jinho D. Choi
 */
public class ChFrame extends ChElement implements ActionListener
{
	static final int WIDTH = ChEditor.WIDTH - 35;
	
	private ChFrameset     parent;
	private ChComment      ch_comment;
	private ChMapping      ch_mapping;
	private ChExampleFrame ch_example;
	private JButton        bt_editExample;
	private JButton        bt_addV, bt_addMapitem, bt_removeFrame;
	
	/**
	 * Initializes frame panel.
	 * This panel is called from {@link ChFrameset}.
	 * @param parent frameset panel
	 * @param eFrame frame XML element
	 */
	public ChFrame(ChFrameset parent, Element eFrame)
	{
		super(eFrame);
		this.parent = parent;
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("Frame"));
	
		initNorth();
		initMapping();
	}
	
	private void initNorth()
	{
		// buttons
		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));

		bt_addV = getButton("Add V");
		pnButton.add(bt_addV);
		
		bt_addMapitem = getButton("Add Mapitem");
		pnButton.add(bt_addMapitem);
		
		bt_removeFrame = getButton("Remove Frame");
		pnButton.add(bt_removeFrame);
		
		ch_example     = new ChExampleFrame(getElement());
		bt_editExample = getButton("Edit Examples");
		pnButton.add(bt_editExample);
		
		// comment
		JPanel pnComment = new JPanel();
		pnComment.setLayout(new BoxLayout(pnComment, BoxLayout.X_AXIS));
		pnComment.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pnComment.add(new JLabel("Frame comment    : "));
		ch_comment = getComment(null);
		ch_comment.setBorder(new LineBorder(Color.lightGray));
		pnComment.add(ch_comment);
		
		// north pane
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.Y_AXIS));
		pnNorth.setPreferredSize(new Dimension(WIDTH, (FD_HEIGHT+V_GAP)*2));
		
		pnNorth.add(pnButton);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		pnNorth.add(pnComment);
		pnNorth.add(Box.createVerticalStrut(3));
		
		add(pnNorth, BorderLayout.NORTH);
	}
	
	private JButton getButton(String label)
	{
		JButton bt = new JButton(label);
		
		bt.setPreferredSize(new Dimension(130, FD_HEIGHT));
		bt.addActionListener(this);
		
		return bt;
	}
	
	private void initMapping()
	{
		NodeList list = getElementsByTagName(ChLib.MAPPING);
		Element eMapping = (list.getLength() > 0) ? (Element)list.item(0) : ChEditor.createEmptyMapping();
		
		ch_mapping = new ChMapping(this, eMapping);
		add(ch_mapping, BorderLayout.CENTER);
	}
	
	/** Saves all elements. */
	public void save()
	{
		ch_comment.save();
		ch_mapping.save();
		ch_example.save();
	}
	
	/** Validates frameset containing this panel. */
	public void validateFrameset()
	{
		parent.validate();
	}
	
	/**
	 * Initializes menu actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if      (e.getSource() == bt_addV)			menuAddV();
		else if (e.getSource() == bt_addMapitem)	menuAddMapitem();
		else if (e.getSource() == bt_removeFrame)	menuRemoveFrame();
		else if (e.getSource() == bt_editExample)	menuEditExample();
	}
	
	/**
	 * Adds a verb.
	 * @see ChMapping#addV(Element)
	 */
	public void menuAddV()
	{
		Element eV = ChEditor.createEmptyV();
		
		ch_mapping.appendChild(eV);
		ch_mapping.addV(eV);
		validateFrameset();
	}
	
	/**
	 * Adds a mapitem.
	 * @see ChMapping#addMapitem(Element)
	 */
	public void menuAddMapitem()
	{
		Element eMapitem = ChEditor.createEmptyMapitem();
		
		ch_mapping.appendChild(eMapitem);
		ch_mapping.addMapitem(eMapitem);
		validateFrameset();
	}
	
	/**
	 * Removes this frame.
	 * @see ChFrameset#removeFrame(ChFrame)
	 */
	public void menuRemoveFrame()
	{
		parent.removeFrame(this);
	}
	
	/** Makes the example frame visible. */
	public void menuEditExample()
	{
		ch_example.setVisible(true);
	}
}
