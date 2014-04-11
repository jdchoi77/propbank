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

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <b>Last update:</b> 06/17/2009
 * @author Jinho D. Choi
 */
abstract public class EnElement extends JPanel
{
	protected final int FD_HEIGHT   = 20;	// field height
	protected final int H_GAP       = 10;	// horizontal gap
	protected final int H_GAP_FRONT = 3;	// horizontal gap in the front
	protected final int V_GAP       = 5;	// vertical gap
	
	private Element element;
	
	/**
	 * Initializes element panel.
	 * @param element XML element
	 */
	public EnElement(Element element)
	{
		this.element = element;
	}
	
	protected EnNote getNote(String label)
	{
		NodeList list = element.getChildNodes();
		String   note = "";
		
		for (int i=0; i<list.getLength(); i++)
		{
			Node node = list.item(i);
			if (!node.getNodeName().equals(EnLib.NOTE))	continue;
			
			note += node.getTextContent().trim()+"\n\n";
			element.removeChild(node);
		}
		
		Element eNote = EnEditor.createNote(note.trim());
		appendChild(eNote);
		
		return new EnNote(eNote, label);
	}
	
	protected void appendChild(Element child)
	{
		element.appendChild(child);
	}
	
	protected void removeChild(Element child)
	{
		element.removeChild(child);
	}
	
	protected NodeList getChildNodes()
	{
		return element.getChildNodes();
	}
	
	protected String getAttribute(String name)
	{
		return element.getAttribute(name).trim();
	}
	
	protected void setAttribute(String name, String value)
	{
		element.setAttribute(name, value.trim());
	}
	
	protected boolean hasAttribute(String name)
	{
		return element.hasAttribute(name);
	}
	
	protected String getTextContent()
	{
		return element.getTextContent().trim();
	}
	
	protected void setTextContent(String textContent)
	{
		element.setTextContent(textContent.trim());
	}
	
	protected Element getFirstChildByTagName(String name)
	{
		NodeList list = getChildNodes();
		int i, size = list.getLength();
		Node node;
		
		for (i=0; i<size; i++)
		{
			node = list.item(i);
			
			if (node.getNodeName().equals(name))
				return (Element)node;
		}
		
		return null; 
	}
	
	protected NodeList getElementsByTagName(String name)
	{
		return element.getElementsByTagName(name);
	}
	
	protected Element getElement()
	{
		return element;
	}
	
	abstract public void save();
	
	/**
	 * Returns true if <code>cb</code> contains <code>item</code>.
	 * @param cb combo-box
	 * @param item item to search
	 * @return true if <code>cb</code> contains <code>item</code>.
	 */
	public boolean contains(JComboBox cb, String item)
	{
		for (int i=0; i<cb.getComponentCount(); i++)
			if (cb.getItemAt(i).equals(item))	return true;
		
		return false;
	}
	
	protected Aliases initAliases(String label)
	{
		Element eAliases = getFirstChildByTagName(EnLib.ALIASES);
		
		if (eAliases == null)
		{
			eAliases = EnEditor.createElement(EnLib.ALIASES);
			appendChild(eAliases);
		}
		
		Aliases aliases = new Aliases(this, eAliases, label);
		aliases.setPreferredSize(new Dimension(EnEditor.WIDTH, 77));
		return aliases;
	}
}
