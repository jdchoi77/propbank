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

public class StringManager
{
	static public String addIndent(String str, int length)
	{
		String tmp = str;
		
		for (int i=str.length(); i<length; i++)
			tmp += " ";
		
		return tmp;
	}
	
	static public String[] valueOf(Vector<String> vec)
	{
		String str[] = new String[vec.size()];
		vec.toArray(str);
		
		return str;
	}
	
	static public String[] valueOf(ArrayList<String> arr)
	{
		String str[] = new String[arr.size()];
		arr.toArray(str);
		
		return str;
	}
	
	static public Vector<String> toVector(String[] str)
	{
		Vector<String> vec = new Vector<String>();
		
		for (int i=0; i<str.length; i++)
			vec.add(str[i]);
		
		return vec;
	}
	
	static public boolean isInteger(String str)
	{
		try
		{
			Integer.parseInt(str);
		}
		catch (NumberFormatException e){return false;}
		
		return true;
	}

	static public String getUTF8(String str)
	{
		String utf = "";
		
		try
		{
			utf = new String(str.getBytes(), "UTF-8");
		}
		catch (UnsupportedEncodingException e) {System.err.println(e);}
		
		return utf; 
	}
}
