/**
* Copyright (c) 2007-2009, Regents of the University of Colorado
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
package jubilee.treebank;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jubilee.propbank.PBReader;
import jubilee.toolkit.JBToolkit;

/**
 * @author Jinho D. Choi
 * <b>Last update:</b> 5/6/2010
 */
@SuppressWarnings("serial")
public class TBNodePanel extends JPanel
{
	private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	JLabel lb_tag, lb_word, lb_arg;
	
	public TBNodePanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(Color.lightGray);
		lb_tag = new JLabel();
		lb_tag.setFont(DEFAULT_FONT);
		add(lb_tag);
		
		lb_word = new JLabel();
		lb_word.setFont(DEFAULT_FONT);
		add(lb_word);
		
		lb_arg = new JLabel();
		lb_arg.setFont(DEFAULT_FONT);
		add(lb_arg);
	}
	
	public void copyTBNode(TBNode node)
	{
		lb_tag.setText(node.getPos());
		if (node.getWord() != null)	lb_word.setText(node.getWord());
		else						lb_word.setText("");
		if (node.getArg() != null)	lb_arg.setText(node.getLoc() + PBReader.ARG_JOINER + node.getArg());
		else						lb_arg.setText("");

		if (node.depParent != null && node.depParent.equals(JBToolkit.s_predPos))
			lb_tag.setForeground(Color.red);
		else						
			lb_tag.setForeground(Color.gray);
		
		lb_word.setForeground(Color.black);
		if (lb_arg.getText().contains(PBReader.REL))	lb_arg.setForeground(Color.red);
		else											lb_arg.setForeground(Color.blue);
	}
}