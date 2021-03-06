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
package jubilee.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * @author Jinho D. Choi
 * <b>Last update:</b> 5/6/2010
 */
@SuppressWarnings("serial")
public class JDCTextAreaFrame extends JFrame implements ActionListener
{
	private JDCTextAreaFrameMenuBar mbar;
	
	public JDCTextAreaFrame(String title, String example)
	{
		super(title);
		
		mbar = new JDCTextAreaFrameMenuBar(this);
		setJMenuBar(mbar);
		
		add(new JScrollPane(new JTextArea(example)));
		addWindowListener(new WindowAdapt());
		
		setBounds(20, 20, 700, 700);
		setVisible(true);
	}
	
	public JDCTextAreaFrame(String title, String example, int lineNum)
	{
		super(title);
		
		mbar = new JDCTextAreaFrameMenuBar(this);
		setJMenuBar(mbar);
		
		JTextArea ta = new JTextArea(example);
		int bId = 0, eId = -1;
		
		for (int i=0; i<=lineNum; i++)
		{
			bId = eId + 1;
			eId = example.indexOf("\n", bId);
		}
		
		Highlighter h = ta.getHighlighter();
		try
		{
			h.addHighlight(bId, eId, new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
		}
		catch (BadLocationException e) {e.printStackTrace();}
		
		ta.setCaretPosition(bId);
		
		add(new JScrollPane(ta), BorderLayout.CENTER);
		addWindowListener(new WindowAdapt());
		
		setBounds(20, 20, 700, 700);
		setVisible(true);
	}
	
	class WindowAdapt extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{		dispose();		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == mbar.fileQuit)		dispose();
	}
}
