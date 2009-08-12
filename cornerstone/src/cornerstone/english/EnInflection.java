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
package cornerstone.english;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
public class EnInflection extends EnElement
{
	private JComboBox cb_person;
	private JComboBox cb_tense;
	private JComboBox cb_aspect;
	private JComboBox cb_voice;
	private JComboBox cb_form;
	
	/**
	 * Initializes inflection panel.
	 * This panel is called by {@link EnExample}.
	 * @param eInflection inflection XML element
	 */
	public EnInflection(Element eInflection)
	{
		super(eInflection);
		
		setBorder(new TitledBorder("Inflections"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		initAttributes();
		initBounds();
	}
	
	private void initAttributes()
	{
		cb_person = getComboBox(EnLib.PERSON, EnLib.ARR_PERSON);
		cb_tense  = getComboBox(EnLib.TENSE , EnLib.ARR_TENSE);
		cb_aspect = getComboBox(EnLib.ASPECT, EnLib.ARR_ASPECT);
		cb_voice  = getComboBox(EnLib.VOICE , EnLib.ARR_VOICE);
		cb_form   = getComboBox(EnLib.FORM  , EnLib.ARR_FORM);
	}
	
	private JComboBox getComboBox(String name, String[] values)
	{
		JComboBox cb = new JComboBox(values);
		
		cb.setPreferredSize(new Dimension(100, FD_HEIGHT));
		if (hasAttribute(name))	cb.setSelectedItem(getAttribute(name).toLowerCase());
		
		return cb;
	}
	
	private void initBounds()
	{
		// top panel: person, tense, aspect
		JPanel pnTop = new JPanel();
		pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.X_AXIS));
		pnTop.setPreferredSize(new Dimension(EnExampleFrame.WIDTH, FD_HEIGHT));
		
		pnTop.add(Box.createHorizontalStrut(H_GAP_FRONT));
		
		pnTop.add(new JLabel(EnLib.PERSON+": "));
		pnTop.add(cb_person);
		pnTop.add(Box.createHorizontalStrut(H_GAP));
		
		pnTop.add(new JLabel(EnLib.TENSE+": "));
		pnTop.add(cb_tense);
		pnTop.add(Box.createHorizontalStrut(H_GAP));
		
		pnTop.add(new JLabel(EnLib.ASPECT+": "));
		pnTop.add(cb_aspect);
		
		// bottom panel: voice, form
		JPanel pnBottom = new JPanel();
		pnBottom.setLayout(new BoxLayout(pnBottom, BoxLayout.X_AXIS));
		pnBottom.setPreferredSize(new Dimension(EnExampleFrame.WIDTH, FD_HEIGHT));
		
		pnBottom.add(Box.createHorizontalStrut(H_GAP_FRONT+11));
		pnBottom.add(new JLabel(EnLib.VOICE+": "));
		pnBottom.add(cb_voice);
		pnBottom.add(Box.createHorizontalStrut(H_GAP+4));
		
		pnBottom.add(new JLabel(EnLib.FORM+": "));
		pnBottom.add(cb_form);
		pnBottom.add(Box.createHorizontalStrut(233));
		
		add(pnTop);
		add(Box.createVerticalStrut(V_GAP));
		add(pnBottom);
	}
	
	/** Saves attributes */
	public void save()
	{
		setAttribute(EnLib.PERSON, (String)cb_person.getSelectedItem());
		setAttribute(EnLib.TENSE , (String)cb_tense.getSelectedItem());
		setAttribute(EnLib.ASPECT, (String)cb_aspect.getSelectedItem());
		setAttribute(EnLib.VOICE , (String)cb_voice.getSelectedItem());
		setAttribute(EnLib.FORM  , (String)cb_form.getSelectedItem());
	}
}
