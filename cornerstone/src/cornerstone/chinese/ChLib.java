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
package cornerstone.chinese;

/**
 * <b>Last update:</b> 06/18/2009
 * @author Jinho D. Choi
 */
public class ChLib
{
	static public String   LANG_CH = "ch";	// chinese
	static public String   LANG_AR = "ar";	// arabic
	
	static public String[] ARR_N;
	static public String[] ARR_F;
	static public String[] ARR_SRC;
	static public String[] ARR_TRG;
	static public String[] ARR_VNTHETA;
	
	// element tags
	static public final String VERB     = "verb";
	static public final String ID       = "id";		// verb#id, frameset.id
	static public final String FRAMESET = "frameset";
	static public final String ROLE     = "role";
	static public final String FRAME    = "frame";
	static public final String MAPPING  = "mapping";
	static public final String EXAMPLE  = "example";
	static public final String V        = "V";
	static public final String MAPITEM  = "mapitem";
	static public final String PARSE    = "parse";
	static public final String ARG      = "arg";
	static public final String COMMENT  = "comment";
	
	// attribute tags
	static public final String CDEF    = "cdef";	// frameset.cdef
	static public final String EDEF    = "edef";	// frameset.edef
	static public final String ARGNUM  = "argnum";	// role.argnum
	static public final String FTAG    = "ftag";	// role.ftag
	static public final String ARGROLE = "argrole";	// role.argrole
	static public final String VNCLS   = "vncls";	// role.vncls
	static public final String VNTHETA = "vntheta";	// role.vntheta
	static public final String SRC     = "src";		// mapitem.src
	static public final String TRG     = "trg";		// mapitem.trg
	static public final String N       = "n";		// arg.n
	static public final String F       = "f";		// arg.f
	static public final String G       = "g";		// arg.g
}
