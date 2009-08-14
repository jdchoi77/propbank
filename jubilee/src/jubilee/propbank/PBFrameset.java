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
 * 'PBFrameset' contains 'predicate's.
 * 'predicate' represents < predicate > in English, and < id > in others.
 * <br><br>Last update: 10/04/07
 * @see PBPredicate
 */
public class PBFrameset
{
	private Vector<PBPredicate> predicates;
	
	/**
	 * Initializes the frameset.
	 */
	public PBFrameset()
	{
		predicates = new Vector<PBPredicate>();
	}
	
	/**
	 * Adds a predicate to the frameset.
	 * @param predicate the predicate to be added.
	 */
	public void addPredicate(PBPredicate predicate)
	{
		predicates.add(predicate);
	}
		
	/**
	 * Gets the index'th predicate.
	 * @param index the index of predicate.
	 * @return the index'th predicate.
	 */
	public PBPredicate getPredicate(int index)
	{
		return predicates.get(index);
	}

	/**
	 * Gets all all predicate lemmas.
	 * @return predicate lemmas.
	 */
	public String[] getPredicateLemma()
	{
		String[] lemmas = new String[predicates.size()];
		
		for (int i=0; i<predicates.size(); i++)
			lemmas[i] = predicates.get(i).getLemma();
		
		return lemmas;
	}
	
	/**
	 * Returns the number of predicates in this frameset.
	 * @return the number of predicates.
	 */
	public int getSize()
	{
		return predicates.size();
	}
}
