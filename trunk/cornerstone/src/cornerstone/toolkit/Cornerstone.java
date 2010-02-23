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
package cornerstone.toolkit;

import javax.swing.*;

import cornerstone.chinese.ChEditor;
import cornerstone.english.EnEditor;

/**
 * <b>Last update:</b> 06/15/2009
 * @author Jinho D. Choi
 */
public class Cornerstone
{
	/**
	 * Launches Cornerston.
	 * @param args args[0] = "language", args[1] = "user ID"
	 */
	static public void main(String[] args)
	{
		try
		{
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {System.err.println(e);}
		catch (ClassNotFoundException e) {System.err.println(e);}
		catch (InstantiationException e) {System.err.println(e);}
		catch (IllegalAccessException e) {System.err.println(e);}
		
		String title = "Conerstone 1.3b";
		if (args[0].equals("en") || args[0].equals("hi"))
			new EnEditor(title, args[0], args[1]);
		else
			new ChEditor(title, args[0], args[1]);
	}
}
