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
package cornerstone.english;

/**
 * <b>Last update:</b> 06/16/2009
 * @author Jinho D. Choi
 */
public class EnLib
{
	static public String   LANG_EN = "en";	// english
	static public String   LANG_HI = "hi";	// hindi
	
	static public String[] ARR_N;
	static public String[] ARR_F;
	static public String[] ARR_DREL;
	static public String[] ARR_VTYPE;
	static public String[] ARR_VNTHETA;
	static public String[] ARR_PERSON;
	static public String[] ARR_TENSE;
	static public String[] ARR_ASPECT;
	static public String[] ARR_VOICE;
	static public String[] ARR_FORM;
	
	// element tags
	static public final String FRAMESET   = "frameset";
	static public final String PREDICATE  = "predicate";
	static public final String ROLESET    = "roleset";
	static public final String ROLES      = "roles";
	static public final String ROLE       = "role";
	static public final String VNROLE     = "vnrole";
	static public final String EXAMPLE    = "example";
	static public final String TEXT       = "text";
	static public final String INFLECTION = "inflection";
	static public final String ARG        = "arg";
	static public final String REL        = "rel";
	static public final String NOTE       = "note";
	
	// attribute tags
	static public final String LEMMA   = "lemma";	// predicate.lemma
	static public final String ID      = "id";		// roleset.id
	static public final String NAME    = "name";	// roleset.name, example.name
	static public final String TYPE    = "type";	// example.type
	static public final String SRC     = "src";		// example.src
	static public final String VTYPE   = "vtype";	// roleset.vtype
	static public final String VNCLS   = "vncls";	// roleset.vncls, vnrole.vncls
	static public final String VNTHETA = "vntheta";	// vnrole.vntheta
	static public final String FRAMNET = "framnet";	// roleset.framnet
	static public final String N       = "n";		// role.n, arg.n
	static public final String F       = "f";		// role.f, arg.f, rel.f
	static public final String DREL    = "drel";	// role.drel, arg.drel
	static public final String DESCR   = "descr";	// role.desrc
	static public final String PERSON  = "person";	// inflection.person
	static public final String TENSE   = "tense";	// inflection.tense
	static public final String ASPECT  = "aspect";	// inflection.aspect
	static public final String VOICE   = "voice";	// inflection.voice
	static public final String FORM    = "form";	// inflection.form
}
