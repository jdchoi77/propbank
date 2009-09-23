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
		
		String version = "Jubilee 2.1";
		
		if (args.length == 1)
			new JBToolkit(version, args[0], 2, 0);
		else if (args.length == 2)
			new JBToolkit(version, args[0], Integer.parseInt(args[1]), 0);
		else if (args.length == 3)
			new JBToolkit(version, args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		else
			System.err.println("Usage: java -jar jubilee.jar <userId> [max-annotations] [skip: gold mode only]");
	}
}
