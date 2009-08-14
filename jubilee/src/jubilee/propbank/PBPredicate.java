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
 * 'PBPrediate' contains 'lemma' and 'roleset's.
 * 'lemma' represents < predicate lemma > in English and < id > in others.
 * 'roleset' represents < roleset > in English, < frameset > in others.
 * <br><br>Last update: 10/04/07
 * @see PBRoleset
 */
public class PBPredicate
{
	private String lemma;
	private Vector<PBRoleset> rolesets;
	
	/**
	 * Initializes the predicate.
	 */
	public PBPredicate()
	{
		rolesets = new Vector<PBRoleset>();
	}
	
	/**
	 * Assigns the lemma.
	 * @param lemma the lemma to be assigned.
	 */
	public void setLemma(String lemma)
	{
		this.lemma = StringManager.getUTF8(lemma);
	}
	
	/**
	 * Adds a roleset.
	 * @param roleset the roleset to be added.
	 */
	public void addRoleset(PBRoleset roleset)
	{
		rolesets.add(roleset);
	}
	
	/**
	 * Gets the lemma of the predicate.
	 * @return the lemma.
	 */
	public String getLemma()
	{
		return lemma;
	}
	
	/**
	 * Gets the index'th roleset.
	 * @param index the index of the roleset.
	 * @return the index'th roleset.
	 */
	public PBRoleset getRoleset(int index)
	{
		return rolesets.get(index);
	}
	
	/**
	 * Gets all roleset IDs.
	 * @return the roleset IDs.
	 */
	public String[] getRolesetID()
	{
		String[] ids = new String[rolesets.size()];
		
		for (int i=0; i<ids.length; i++)
			ids[i] = rolesets.get(i).getId();
		
		return ids;
	}
}
