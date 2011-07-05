package jubilee.propbank;

import jdsl.core.ref.NodeTree;
import jubilee.treebank.TBTree;

public class PBInstance
{
	String   treePath = null;
	int      treeId;
	int      predId;
	String   annotator;
	String   type;
	String   roleset;
	String   aspect;
	TBTree   pTree;
	String   predPos = null;	// for Hindi
	NodeTree dTree   = null;	// for Hindi
	
	public boolean isTreePath(String treePath)
	{
		return treePath != null && treePath.equals(treePath);
	}
	
	public int getTreeId()
	{
		return treeId;
	}
	
	public void copy(PBInstance instance)
	{
		treePath  = instance.treePath;
		treeId    = instance.treeId;
		predId    = instance.predId;
	//	annotator = instance.annotator;
		type      = instance.type;
		roleset   = instance.roleset;
		aspect    = instance.aspect;
		pTree     = instance.pTree.clone();
	}
	
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		
		build.append(treePath);		build.append(" ");
		build.append(treeId);		build.append(" ");
		build.append(predId);		build.append(" ");
		build.append(annotator);	build.append(" ");
		build.append(type);			build.append(" ");
		build.append(roleset);		build.append(" ");
		build.append(aspect);		build.append(" ");
		build.append(pTree.toPropbank());
		
		return build.toString();
	}
}
