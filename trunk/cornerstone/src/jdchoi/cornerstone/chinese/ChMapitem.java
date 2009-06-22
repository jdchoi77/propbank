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
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/20/2009
 * @author Jinho D. Choi
 */
public class ChMapitem extends ChElement implements ActionListener
{
	private ChMapping  parent;
	private JComboBox  cb_src;
	private JComboBox  cb_trg;
	private JButton    bt_remove;
	
	/**
	 * Initializes mapitem panel.
	 * This panel is called from {@link ChMapping}.
	 * @param parent mapping panel
	 * @param eMapitem mapitem XML element
	 */
	public ChMapitem(ChMapping parent, Element eMapitem)
	{
		super(eMapitem);
		this.parent = parent;
		setPreferredSize(new Dimension(ChMapping.WIDTH, FD_HEIGHT+V_GAP));
		setLayout(null);
		
		initComponents();
	}

	private void initComponents()
	{
		JLabel src = new JLabel(ChLib.SRC+":");
		src.setBounds(H_GAP_FRONT, V_GAP, 30, FD_HEIGHT);
		add(src);
		
		cb_src = new JComboBox(ChLib.ARR_SRC); 
		cb_src.setSelectedItem(getAttribute(ChLib.SRC).toLowerCase());
		cb_src.setBounds(30, V_GAP, 120, FD_HEIGHT);
		add(cb_src);
		
		JLabel trg = new JLabel(ChLib.TRG+":");
		trg.setBounds(160, V_GAP, 30, FD_HEIGHT);
		add(trg);
		
		cb_trg = new JComboBox(ChLib.ARR_TRG);
		cb_trg.setSelectedItem(getAttribute(ChLib.TRG).toLowerCase());
		cb_trg.setBounds(185, V_GAP, 80, FD_HEIGHT);
		add(cb_trg);
		
		bt_remove = new JButton("Remove");
		bt_remove.setBounds(275, V_GAP, 100, FD_HEIGHT);
		bt_remove.addActionListener(this);
		add(bt_remove);
	}
	
	/** Saves attributes */
	public void save()
	{
		setAttribute(ChLib.SRC, (String)cb_src.getSelectedItem());
		setAttribute(ChLib.TRG, (String)cb_trg.getSelectedItem());
	}
	
	/**
	 * Removes this panel from its {@link ChMapitem#parent}.
	 * @see {@link ChMapping#removeMapitem(ChMapitem)}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			parent.removeMapitem(this);
	}
}
