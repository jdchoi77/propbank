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
package jubilee.util;

import java.io.*;
import java.util.*;

/**
 * @author jdchoi
 * @since 09/13/07
 */
public class DataManager
{
	static final public String PATH = ".path";
	static final public String ARGS = ".args";
	static final public String FUNC = "function";	// for propbank functions (trace, etc.)
	static final public String SYS_DIR = "system/";
	
	static final private String ARGS_TAG = "args.tag";
	static final private String ARGS_FUNCTION = "args.func";
	
	/**
	 * Gets the list of *.path files.
	 * @return the list of *.path files.
	 */
	static public String[] getSettings()
	{
		ArrayList<String> arr = new ArrayList<String>();
		String[] list = new File(SYS_DIR).list();
		
		for (int i=0; i<list.length; i++)
		{
			int dotIndex = list[i].lastIndexOf(".");
			if (dotIndex != -1 && list[i].substring(dotIndex).equals(PATH))
				arr.add(list[i].substring(0, dotIndex));
		}
				
		Collections.sort(arr);
		return StringManager.valueOf(arr);
	}
	
	/**
	 * Reads 'system/language.ext' and returns contents of the file.
	 * @param language the language for the contents.
	 * @param ext the extension of the file (DataManager.PATH | ARGS).
	 * @return the contents.
	 */
	// used
	static public String[][] getContents(String language, String ext)
	{
		Vector<String> vec = new Vector<String>();

		try
		{
			Scanner scan = new Scanner(new File(SYS_DIR + language + ext));

			while (scan.hasNextLine())
			{
				String line = scan.nextLine().trim();
				// skip comments and blank-lines
				if (line.length() > 0 && line.charAt(0) != '#')
					vec.add(line);
			}
			
			scan.close();
		}
		catch (IOException e){System.err.println(e);}
		
		StringTokenizer tok = new StringTokenizer(vec.get(0));
		String[][] str = new String[vec.size()][tok.countTokens()];
		for (int i=0; i<vec.size(); i++)
		{
			tok = new StringTokenizer(vec.get(i));
			for (int j=0; tok.hasMoreTokens(); j++)
				str[i][j] = tok.nextToken();
		}

		return str;
	}
	
	/**
	 * Gets 'dataset' and return the directory-path of 'resource'. 
	 * @param dataset 1st row = resource, 2nd row = directory-path.
	 * @param resource resource of the directory-path. ex) Treebank, Frameset, etc.
	 * @return (resource exists) ? the directory-path : ".".
	 */
	// used
	static public String getPath(String[][] dataset, String resource)
	{
		for (int i=0; i<dataset.length; i++)
			if (dataset[i][0].equalsIgnoreCase(resource))
				return dataset[i][1];
		
		return ".";
	}
	
	
	
	
	
	
	

	
	// used
	static public Vector<String> getArgTags()
	{
		return getArg(ARGS_TAG);
	}
	
	static public Vector<String> getArgFunctions()
	{
		return getArg(ARGS_FUNCTION);
	}
	
	// used
	static private Vector<String> getArg(String filename)
	{
		Vector<String> vec_arg = new Vector<String>();
		
		try
		{
			Scanner scan = new Scanner(new File(SYS_DIR+filename));
			String str;
			
			while (scan.hasNextLine())
			{
				str = scan.nextLine().trim();
				
				if (str.charAt(0) != '!')	// skip comments
					vec_arg.add(str);
			}
		}
		catch (IOException e){System.err.println(e);}
		
		return vec_arg;
	}
	
	
	
	static public Vector<String> getPBArg()
	{
		Vector<String> vec_arg = new Vector<String>();
		
		try
		{
			Scanner scan = new Scanner(new File(SYS_DIR+"args.tag"));
			scan.nextLine();	scan.nextLine();	// skip comments
			
			// initialize Arg#
			int n = Integer.parseInt(scan.nextLine());	// last # of arguments
			for (int i=0; i<=n; i++)	vec_arg.add("ARG"+i);
			
			scan.nextLine();	// skip a comment
			
			// initialize ArgM
			while (scan.hasNextLine())	vec_arg.add("ARGM-"+scan.nextLine().trim());
		}
		catch (IOException e){System.err.println(e);}
		
		return vec_arg;
	}
	
	static public Vector<String> getPBArg(String prefix)
	{
		Vector<String> vec_arg = new Vector<String>();
		
		try
		{
			Scanner scan = new Scanner(new File("system/args.tag"));
			scan.nextLine();	scan.nextLine();	// skip comments
			
			// initialize Arg#
			int n = Integer.parseInt(scan.nextLine());	// last # of arguments
			for (int i=0; i<=n; i++)	vec_arg.add(prefix+"ARG"+i);
			
			scan.nextLine();	// skip a comment
			
			// initialize ArgM
			while (scan.hasNextLine())	vec_arg.add(prefix+"ARGM-"+scan.nextLine().trim());
		}
		catch (IOException e){System.err.println(e);}
		
		return vec_arg;
	}
}
