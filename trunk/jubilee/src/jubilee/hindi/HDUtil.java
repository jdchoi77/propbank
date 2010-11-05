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
package jubilee.hindi;

import jdsl.core.api.Position;
import jdsl.core.ref.NodeTree;
import jubilee.treebank.TBNode;

public class HDUtil
{
	static public NodeTree getTree(TBNode pSSF)
	{
		NodeTree dTree = new NodeTree();
		Position dRoot = dTree.root();
		
		setDepTree(pSSF, dTree, dRoot, "ROOT");
		dTree.replaceElement(dRoot, "ROOT");
		
		return dTree;
	}
	
	static public void cleanTree(TBNode pSSF)
	{
		for (TBNode child : pSSF.getChildren())
		{
			String[] cpos = child.getPos().split(":");	// NP:k1:VGF
			child.setPos(cpos[0]);
		}
	}
	
	static private void setDepTree(TBNode pSSF, NodeTree dTree, Position dParent, String parentName)
	{
		for (TBNode pChild : pSSF.getChildren())
		{
			String[] cpos = pChild.getPos().split(":");	// NP:k1:VGF
			
			if (cpos[2].equals(parentName))
			{
				Position dChild = dTree.insertLastChild(dParent, cpos[0]+":"+cpos[1]+":"+pChild.toWords());
				setDepTree(pSSF, dTree, dChild, cpos[0]);
			}
		}
	}
}
