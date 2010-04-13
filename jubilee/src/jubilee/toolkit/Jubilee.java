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
package jubilee.toolkit;

import javax.swing.*;

public class Jubilee
{
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
		
		String usage  = "Usage: java -jar jubilee.jar -u <userId> [-m max-annotations=2 -p skip=0 -s system-folder=system/]";
		String title  = "Jubilee 2.12";
		String userId = null;
		int    maxAnn = 2;
		byte   skip   = 0;
		String sysDir = "system/";
		
		if (args.length == 0 || args.length % 2 != 0)	{System.err.println(usage);	return;}
		
		for (int i=0; i<args.length; i+=2)
		{
			String option = args[i];
			String value  = args[i+1];
			
			if      (option.equals("-u"))	userId = value;
			else if (option.equals("-m"))	maxAnn = Integer.parseInt(value);
			else if (option.equals("-p"))	skip   = Byte.parseByte(value);
			else if (option.equals("-s"))	sysDir = value+"/";
			else							{System.err.println(usage);	return;}
		}
		
		if (userId == null)	{System.err.println(usage);	return;}
		
		new JBToolkit(title, sysDir, userId, maxAnn, skip);
	}
}
