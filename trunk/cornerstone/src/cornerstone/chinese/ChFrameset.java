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
 * <b>Last update:</b> 06/20/2009
 * @author Jinho D. Choi
 */
public class ChFrameset extends ChElement
{
	private final int FRAME_HEIGHT = 1500;
	
	public  String             id;
	private ChVerb             parent;
	private JTextField         tf_cdef;
	private JTextField         tf_edef;
	private JTextField         tf_vncls;
	private ChComment          ch_comment;
	private ArrayList<ChRole>  ar_role;
	private JPanel             pn_role;
	private ArrayList<ChFrame> ar_frame;
	private JPanel             pn_frame;
	
	/**
	 * Initializes frameset panel.
	 * This panel is called from {@link ChVerb}.
	 * @param parent verb panel
	 * @param eFrameset frameset XML element
	 */
	public ChFrameset(ChVerb parent, Element eFrameset)
	{
		super(eFrameset);
		this.parent = parent;
		setLayout(new BorderLayout());
		
		initNorth();
		initRoles();
		initFrames();
	}
	
	private void initNorth()
	{
		ch_comment = getComment("Frameset comment");
		id         = getAttribute(ChLib.ID);
		
		// attributes
		JPanel pnAttr = new JPanel();
		pnAttr.setLayout(new BoxLayout(pnAttr, BoxLayout.X_AXIS));
		pnAttr.add(Box.createHorizontalStrut(8));
		
		pnAttr.add(new JLabel(ChLib.EDEF+": "));
		tf_edef = new JTextField(getAttribute(ChLib.EDEF));
		tf_edef.setPreferredSize(new Dimension(150, FD_HEIGHT));
		pnAttr.add(tf_edef);
		pnAttr.add(Box.createHorizontalStrut(H_GAP));	
		
		pnAttr.add(new JLabel(ChLib.CDEF+": "));
		tf_cdef = new JTextField(getAttribute(ChLib.CDEF));
	//	tf_cdef.setPreferredSize(new Dimension(100, FD_HEIGHT));
		pnAttr.add(tf_cdef);
		
		if (ChEditor.LANGUAGE.equals(ChLib.LANG_AR))
		{
			pnAttr.add(Box.createHorizontalStrut(H_GAP));
			pnAttr.add(new JLabel(ChLib.VNCLS+": "));
			tf_vncls = new JTextField(getAttribute(ChLib.VNCLS));
		//	tf_vncls.setPreferredSize(new Dimension(100, FD_HEIGHT));
			pnAttr.add(tf_vncls);	
		}
		
		// north pane
		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.Y_AXIS));
		
		pnNorth.add(ch_comment);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		pnNorth.add(pnAttr);
		pnNorth.add(Box.createVerticalStrut(V_GAP));
		
		add(pnNorth, BorderLayout.NORTH);
	}
	
	private void initRoles()
	{
		ar_role = new ArrayList<ChRole>();
		pn_role = new JPanel();
		pn_role.setBorder(new TitledBorder("Roles"));
		pn_role.setLayout(new BoxLayout(pn_role, BoxLayout.Y_AXIS));
		
		NodeList list = getElementsByTagName(ChLib.ROLE);
		for (int i=0; i<list.getLength(); i++)
			addRole((Element)list.item(i));
		
		add(pn_role, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds a role.
	 * @param eRole role XML element to add
	 */
	public void addRole(Element eRole)
	{
		ChRole chRole = new ChRole(this, eRole);
		
		ar_role.add(chRole);
		pn_role.add(chRole);
	}
	
	/**
	 * Removes a role.
	 * @param chRole role to remove
	 */
	public void removeRole(ChRole chRole)
	{
		removeChild(chRole.getElement());
		ar_role.remove(chRole);
		pn_role.remove(chRole);
		validate();
	}

	private void initFrames()
	{
		ar_frame = new ArrayList<ChFrame>();
		pn_frame = new JPanel();
		pn_frame.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		pn_frame.setPreferredSize(new Dimension(ChFrame.WIDTH, FRAME_HEIGHT));
		
		NodeList list = getElementsByTagName(ChLib.FRAME);
		for (int i=0; i<list.getLength(); i++)
			addFrame((Element)list.item(i));
		
		add(new JScrollPane(pn_frame), BorderLayout.CENTER);
	}
	
	/**
	 * Adds a frame.
	 * @param eFrame frame XML element to add
	 */
	public void addFrame(Element eFrame)
	{
		ChFrame chFrame = new ChFrame(this, eFrame);
		
		ar_frame.add(chFrame);
		pn_frame.add(chFrame);
	}
	
	/**
	 * Removes a frame.
	 * @param chFrame frame to remove
	 */
	public void removeFrame(ChFrame chFrame)
	{
		removeChild(chFrame.getElement());
		ar_frame.remove(chFrame);
		pn_frame.remove(chFrame);
		parent.validateFrameset();
	}
	
	/** Saves attributes and elements. */
	public void save()
	{
		setAttribute(ChLib.ID   , id);
		setAttribute(ChLib.CDEF , tf_cdef .getText());
		setAttribute(ChLib.EDEF , tf_edef .getText());
		
		if (ChEditor.LANGUAGE.equals(ChLib.LANG_AR))
			setAttribute(ChLib.VNCLS, tf_vncls.getText());
		
		ch_comment.save();
		for (ChRole  chRole  : ar_role)		chRole.save();
		for (ChFrame chFrame : ar_frame)	chFrame.save();
	}
}
