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
import jubilee.util.*;
/**
 * 'PBRole' contains 'n', 'f', and 'descr'.
 * For PBRoleset, 'n' represents < role n > in English and < role argnum > in other languages.
 * For PBExample, 'n' represents < arg n > in all languages.
 * 'f' represents < arg f > in all languages.
 * For PBRoleset, 'descr' represents < role descr > in English and < role argrole > in others.
 * For PBExample, 'descr' represents < text > in English and < parse > in others.
 * <br><br>Last update: 10/04/07
 */
public class PBRole
{
	private String n, f;
	private String descr;
	
	/**
	 * Initailizes the role.
	 */
	public PBRole()
	{
		n = f = descr = null;
	}
	
	/**
	 * Sets the argument number.
	 * @param n the argument number.
	 */
	public void setN(String n)
	{
		this.n = n;
	}
	
	/**
	 * Sets the function tag.
	 * @param f the funciton tag.
	 */
	public void setF(String f) 
	{
		this.f = StringManager.getUTF8(f);
	}
	
	/**
	 * Sets the argument description.
	 * @param descr the argument description.
	 */
	public void setDescr(String descr)
	{
		this.descr = StringManager.getUTF8(descr);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		if (f == null || f.equals(""))
			return "Arg" + n + " : " + descr;
		else
			return "Arg" + n + "-" + f + " : " + descr;
	}
}
