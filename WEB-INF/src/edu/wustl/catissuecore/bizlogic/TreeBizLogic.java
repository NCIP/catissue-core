package edu.wustl.catissuecore.bizlogic;

import java.util.Vector;

import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.tree.QueryTreeNodeData;

/**
 * 
 * @author deepti_shelar
 *
 */
public class TreeBizLogic 
{
	public Vector getQueryTreeNode()
	{
		Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		
		String id = "ParentIdentifier1";
		String displayName = "ParentNode1";
		QueryTreeNodeData treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("0");
		treeNode1.setParentObjectName("");
		treeData.add(treeNode1);
		
		id = "ChildIdentifier1";
		displayName = "ChildNode1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1");
		treeNode1.setParentObjectName("ParentNode1");		
		treeData.add(treeNode1);
		
		id = "Child1";
		displayName = "Child1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ChildIdentifier1");
		treeNode1.setParentObjectName("ChildNode1");		
		treeData.add(treeNode1);
		
		id = "ChildIdentifier2";
		displayName = "ChildNode2";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1");
		treeNode1.setParentObjectName("ParentNode1");
		treeData.add(treeNode1);
		
		id = "ParentIdentifier2";
		displayName = "ParentNode2";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("0");
		treeNode1.setParentObjectName("");
		treeData.add(treeNode1);
		
		id = "ChildIdentifier1";
		displayName = "ChildNode1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier2");
		treeNode1.setParentObjectName("ParentNode2");		
		treeData.add(treeNode1);
		
		id = "ChildIdentifier2";
		displayName = "ChildNode2";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier2");
		treeNode1.setParentObjectName("ParentNode2");
		treeData.add(treeNode1);
		
		return treeData;
	}
	public Vector getQueryTreeNodeForCategory2()
	{
		Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		
		String id = "ParentIdentifier1ForCategory2";
		String displayName = "ParentNode1ForCategory2";
		QueryTreeNodeData treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("0");
		treeNode1.setParentObjectName("");
		treeData.add(treeNode1);
		
		id = "ChildIdentifier1";
		displayName = "ChildNode1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory2");
		treeNode1.setParentObjectName("ParentNode1ForCategory2");		
		treeData.add(treeNode1);
		
		id = "Child1";
		displayName = "Child1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ChildIdentifier1");
		treeNode1.setParentObjectName("ChildNode1");		
		treeData.add(treeNode1);
		
		id = "ChildIdentifier2";
		displayName = "ChildNode2";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory2");
		treeNode1.setParentObjectName("ParentNode1ForCategory2");
		treeData.add(treeNode1);
		
		id = "ParentIdentifier2";
		displayName = "ParentNode2";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("0");
		treeNode1.setParentObjectName("");
		treeData.add(treeNode1);
		
		id = "ChildIdentifier1";
		displayName = "ChildNode1";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory2");
		treeNode1.setParentObjectName("ParentNode1ForCategory2");
		treeData.add(treeNode1);
		
		return treeData;
	}
	public Vector getQueryTreeNodeForCategory3()
	{
		Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		
		String id = "ParentIdentifier1ForCategory3";
		String displayName = "ParentNode1ForCategory3";
		QueryTreeNodeData treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("0");
		treeNode1.setParentObjectName("");
		treeData.add(treeNode1);
		
		id = "ChildIdentifier1";
		displayName = "ChildNode1ForCategory3";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory3");
		treeNode1.setParentObjectName("ParentNode1ForCategory3");		
		treeData.add(treeNode1);
		
		id = "Child1";
		displayName = "Child1ForCategory3";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory3");
		treeNode1.setParentObjectName("ParentNode1ForCategory3");	
		treeData.add(treeNode1);
		
		id = "ChildIdentifier2";
		displayName = "ChildNode2ForCategory3";
		treeNode1 = new QueryTreeNodeData();
		treeNode1.setIdentifier(id);
		treeNode1.setObjectName(displayName);
		treeNode1.setDisplayName(displayName);
		treeNode1.setParentIdentifier("ParentIdentifier1ForCategory3");
		treeNode1.setParentObjectName("ParentNode1ForCategory3");	
		treeData.add(treeNode1);
					
		return treeData;
	}
}
