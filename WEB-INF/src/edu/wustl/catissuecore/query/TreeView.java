/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.query;

import java.util.Map;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.common.util.logger.Logger;

/**
 * @author poornima_govindrao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeView {
	private int nodeId=0;
	public void arrangeTree(DefaultMutableTreeNode node,int parentId,Vector tree,Map advancedConditionNodesMap){
		
		nodeId++;
		Logger.out.debug("after incre--"+nodeId);
		
		
		for(int i = 0; i < node.getChildCount();i++){
			//nodeCount++;
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)node.getChildAt(i);
			AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)n.getUserObject();
			
			advancedConditionNodesMap.put(new Integer(nodeId),n);
			
			Vector v = advConditionNode.getObjectConditions();
			String tableName = advConditionNode.getObjectName();
			Logger.out.debug("object name for advance node"+tableName);
			Logger.out.debug("size-->"+v.size());
			String str = "";
			Condition con = null;
			DataElement data = null;
			Logger.out.debug("before str--"+nodeId);
			
			for(int k = 0; k < v.size(); k++){
				con = (Condition)v.get(k);
				data = con.getDataElement();
		        Operator op = con.getOperator();
		        if(k == 0)
		        	//str = temp + "|" + parentId + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
		        	str = nodeId + "|" + parentId + "|" +data.getField()+ " "+op.getOperator() + " '" + con.getValue()+ "'";
		        else
		        	//str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
		        	str = str +" "+"<font color='red'>AND</font>"+" "+data.getField()+" "+op.getOperator() + " '"+con.getValue()+ "'";
		        // entered by Mandar for validation of single quotes around the values.
		        Logger.out.debug( "STR :---------- : "+ str);
		    }
			if(data != null)
				str = str +  "|" + tableName;
			
			if(con == null){
				str = nodeId + "|" + parentId + "|" + "ANY" + "|"+advConditionNode.getObjectName();
			}
				
			tree.add(str);
			Logger.out.debug("before arrangetree--"+nodeId);
			if(n.isLeaf())
			{
				nodeId++;
				
				Logger.out.debug("inside leaf loop"+nodeId);
			}
			else
				arrangeTree(n,nodeId,tree,advancedConditionNodesMap);
			Logger.out.debug("temp after rec"+nodeId);
		}
	
	}
}
