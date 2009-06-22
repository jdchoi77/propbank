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
import javax.swing.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class ChId extends ChElement
{
	private JTextField tf_id;
	
	public ChId(Element eId)
	{
		super(eId);
	//	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		setPreferredSize(new Dimension(ChEditor.WIDTH, FD_HEIGHT));
		initId();
	}

	private void initId()
	{
		add(Box.createHorizontalStrut(8));
		add(new JLabel("ID: "));
		tf_id = new JTextField(getTextContent());
		tf_id.setPreferredSize(new Dimension(250, FD_HEIGHT));
		add(tf_id);
	}
	
	/**
	 * Saves id.
	 */
	public void save()
	{
		setTextContent(tf_id.getText());
	}
}
