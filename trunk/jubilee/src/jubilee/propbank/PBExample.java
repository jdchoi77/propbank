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
package jubilee.propbank;

import java.util.*;
import jubilee.util.*;
/**
 * 'PBExample' contains 'text', 'rel', and 'arg's.
 * 'text' represents < text > in English, and < parse > in other languages.
 * 'rel' represents < rel > in English, and doesn't exist in other languages.
 * 'arg' represents < arg > in all languages.
 */
public class PBExample
{
	private String text, rel;
	private Vector<PBRole> args;
	
	/**
	 * Initailizes the example.
	 */
	public PBExample()
	{
		text = rel = null;
		args = new Vector<PBRole>();
	}

	/**
	 * Sets the text of the example.
	 * @param text the text to be set.
	 */
	public void setText(String text)
	{
		this.text = StringManager.getUTF8(text);
	}
	
	/**
	 * Sets the relation of the example.
	 * @param rel the relation to be set.
	 */
	public void setRel(String rel)
	{
		this.rel = rel;
	}
	
	/**
	 * Adds an argument of the example.
	 * @param arg the argument to be added.
	 */
	public void addArg(PBRole arg)
	{
		args.add(arg);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = text + "\n";
		str += (rel != null) ? "\nRel : " + rel : "";
		
		for (int i=0; i<args.size(); i++)
			str += "\n" + args.get(i);
		
		return str;
	}
}
