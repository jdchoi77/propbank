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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jdsl.core.ref.NodeTree;
import jubilee.hindi.BoundingBoxCalculator;
import jubilee.hindi.TreeDrawer;

/** 
 * Frame containing a Hindi dependency tree.
 * @author Jinho D. Choi
 * <b>Last update:</b> 9/13/2010
*/
@SuppressWarnings("serial")
public class JDCDepTreeFrame extends JFrame implements ActionListener
{
	static final Color bg = new Color(238, 238, 238);
	private NodeTree               g_tree;
	private JDCDepTreeFrameMenuBar mbar;
	
	public JDCDepTreeFrame(String title, NodeTree tree)
	{
		super(title);
		g_tree = tree;
		
		mbar = new JDCDepTreeFrameMenuBar(this);
		setJMenuBar(mbar);
		setBounds(20, 20, 800, 400);
		setBackground(bg);
		
		addWindowListener(new WindowAdapt());
		setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(getBackground());
		new BoundingBoxCalculator(getGraphics()).execute(g_tree);

		TreeDrawer draw = new TreeDrawer(g);
		draw.execute(g_tree);
		
	//	setSize(draw.i_width, draw.i_height);
		invalidate();
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
    
    class MyAdjustmentListener implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          repaint();
        }
      }
}
