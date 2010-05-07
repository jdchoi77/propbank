/**
* Copyright (c) 2007, Regents of the University of Colorado
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
package jubilee.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import jubilee.toolkit.JBToolkit;

/**
 * @author Jinho D. Choi
 * <b>Last update:</b> 5/6/2010
 */
public class DataManager
{
	static final public String PATH_FILE_EXT = ".path";
	static final public String ARGS_FILE_EXT = ".args";
	static final public String OPERATOR_FILE = "function.args";	// operators (*, &, etc.)
	
	static final public String FRAMESET   = "Frameset";
	static final public String TREEBANK   = "Treebank";
	static final public String SOURCE     = "Source";
	static final public String TASK       = "Task";
	static final public String ANNOTATION = "Annotation";
	static final public String GOLD_ID    = "gold";
	
	/** @return the list of project filenames. */
	static public Vector<String> getProjects()
	{
		Vector<String> arr  = new Vector<String>();
		String[]          list = new File(JBToolkit.s_sysDir).list();
		
		for (String filename : list)
		{
			if (filename.endsWith(PATH_FILE_EXT))
			{
				int dotIndex = filename.lastIndexOf(".");
				arr.add(filename.substring(0, dotIndex));
			}
		}
				
		Collections.sort(arr);
		return arr;
	}
	
	/**
	 * Returns contents of <code>filename</code>.
	 * @param filename name of the file containing the contents
	 */
	static public ArrayList<String[]> getContents(String filename)
	{
		ArrayList<String[]> list = new ArrayList<String[]>();

		try
		{
			Scanner scan = new Scanner(new File(JBToolkit.s_sysDir + filename));

			while (scan.hasNextLine())
			{
				String line = scan.nextLine().trim();
				// skip comments and blank-lines
				if (line.length() > 0 && line.charAt(0) != '#')
					list.add(line.split(" "));
			}
			
			scan.close();
		}
		catch (IOException e){e.printStackTrace();}
		
		return list;
	}
}
