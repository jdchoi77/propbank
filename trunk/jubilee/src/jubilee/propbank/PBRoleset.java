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

/**
 * 'PBRoleset' contains 'id', 'name', 'role's, and 'example's.
 * 'id' represents < roleset id > in English, and < frameset id > in other langauges.
 * 'name' represents < roleset name > in English, and < frameset edef > in others.
 * 'role' represents < role > in all languages.
 * 'example' represents < example > in all languages.
 * <br><br>Last update: 10/04/07
 * @see PBRole, PBExample
 */
public class PBRoleset
{
	private String id, name;
	private String comment;
	private Vector<PBRole> roles;
	private Vector<PBExample> examples;
	
	public PBRoleset()
	{
		roles = new Vector<PBRole>();
		examples = new Vector<PBExample>();
	}
	
	/**
	 * Gets the ID of the roleset.
	 * @return the ID of the roleset.
	 */
	public String getId()
	{
		return id;
	}
	
	/** @return the comment of the roleset. */
	public String getComment()
	{
		return comment;
	}
	
	/**
	 * Gets all examples.
	 * @return all examples.
	 */
	public String getExamples()
	{
		String str = "";
		
		for (int i=0; i<examples.size(); i++)
			str += examples.get(i) + "\n\n";
		
		return str.trim();
	}
	
	/**
	 * Set ID of the roleset.
	 * @param id ID of the roleset. 
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Set name of the roleset.
	 * @param name the meaning of the ID.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets comment of the roleset.
	 * @param comment comment of the roleset.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	/**
	 * Add a role to the roleset.
	 * @param role the role to be added.
	 */
	public void addRole(PBRole role)
	{
		roles.add(role);
	}
	
	/**
	 * Adds an example to the roleset.
	 * @param example the example to be added.½
	 */
	public void addExample(PBExample example)
	{
		examples.add(example);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "ID : " + id + "\nName : " + name;
		for (int i=0; i<roles.size(); i++)
			str += "\n" + roles.get(i);
		
		return str;
	}
}
