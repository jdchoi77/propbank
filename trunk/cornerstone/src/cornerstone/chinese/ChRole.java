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
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

import javax.swing.border.*;

/**
 * <b>Last update:</b> 06/18/2009
 * @author Jinho D. Choi
 */
public class ChRole extends ChElement implements ActionListener
{
	private ChFrameset parent;
	private JComboBox  cb_argnum;
	private JTextField tf_argrole;
	private JTextField tf_vncls;
	private JComboBox  cb_vntheta;
	private JButton    bt_remove;
	
	public ChRole(ChFrameset parent, Element element)
	{
		super(element);
		this.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalStrut(V_GAP));
		initAttributes();
	}

	private void initAttributes()
	{
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setPreferredSize(new Dimension(ChEditor.WIDTH, FD_HEIGHT));
		
		pn.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pn.add(new JLabel(ChLib.ARGNUM+": "));
		cb_argnum = new JComboBox(ChLib.ARR_N);
		String argnum = getAttribute(ChLib.ARGNUM).toLowerCase();
		if (!contains(cb_argnum, argnum))	cb_argnum.addItem(argnum);
		cb_argnum.setSelectedItem(argnum);
		pn.add(cb_argnum);
		pn.add(Box.createHorizontalStrut(5));
		
		pn.add(new JLabel(ChLib.ARGROLE+": "));
		tf_argrole = new JTextField(getAttribute(ChLib.ARGROLE));
		tf_argrole.setPreferredSize(new Dimension(100, FD_HEIGHT));
		pn.add(tf_argrole);
		pn.add(Box.createHorizontalStrut(5));
		
		if (ChEditor.LANGUAGE.equals(ChLib.LANG_AR))
		{
			pn.add(new JLabel(ChLib.VNCLS+": "));
			tf_vncls = new JTextField(getAttribute(ChLib.VNCLS));
			pn.add(tf_vncls);
			pn.add(Box.createHorizontalStrut(5));
			
			pn.add(new JLabel(ChLib.VNTHETA+": "));
			cb_vntheta = new JComboBox(ChLib.ARR_VNTHETA);
			String vntheta = getAttribute(ChLib.VNTHETA).toLowerCase();
			if (!contains(cb_vntheta, vntheta))	cb_vntheta.addItem(vntheta);
			cb_vntheta.setSelectedItem(vntheta);
			pn.add(cb_vntheta);
			pn.add(Box.createHorizontalStrut(5));			
		}

		bt_remove = new JButton("Remove");
		bt_remove.addActionListener(this);
		pn.add(bt_remove);
	
		add(pn);
	}
	
	/** Saves all attributes and vnroles */
	public void save()
	{
		setAttribute(ChLib.ARGNUM , (String)cb_argnum.getSelectedItem());
		setAttribute(ChLib.ARGROLE, tf_argrole.getText());
		
		if (ChEditor.LANGUAGE.equals(ChLib.LANG_AR))
		{
			setAttribute(ChLib.VNCLS  , tf_vncls.getText());
			setAttribute(ChLib.VNTHETA, (String)cb_vntheta.getSelectedItem());			
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bt_remove)
			parent.removeRole(this);
	}
}

