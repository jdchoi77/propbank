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
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/20/2009
 * @author Jinho D. Choi
 */
public class ChComment extends ChElement
{
	private JTextArea ta_comment;
	
	/**
	 * Initializes comment panel.
	 * @param eComment comment XML element
	 * @param title title of the comment
	 */
	public ChComment(Element eComment, String title)
	{
		super(eComment);
		
		setLayout(new BorderLayout());
		if (title != null)	setBorder(new TitledBorder(title));
		
		ta_comment = new JTextArea(getTextContent());
		ta_comment.setLineWrap(true);
		add(ta_comment, BorderLayout.CENTER);
	}

	/** Saves comment. */
	public void save()
	{
		setTextContent(ta_comment.getText());
	}
}
