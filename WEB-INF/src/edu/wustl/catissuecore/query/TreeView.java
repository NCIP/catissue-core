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
 *This class is for diplaying the tree in query view of Advanced Search 
 * 
 */
public class TreeView {
	
	//Variable which holds node ID in the tree. 
	private int nodeId=0;
	
	//Recursive function to create the tree
	public void arrangeTree(DefaultMutableTreeNode node,int parentId,Vector tree,Map advancedConditionNodesMap){
		
		nodeId++;
		
		//Loop for all the children for the current node.
		for(int i = 0; i < node.getChildCount();i++){
			//nodeCount++;
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
			AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)child.getUserObject();
			advancedConditionNodesMap.put(new Integer(nodeId),child);
			Vector vectorOfCondtions = advConditionNode.getObjectConditions();
			String tableName = advConditionNode.getObjectName();
			Logger.out.debug("object name for advance node"+tableName);
			Logger.out.debug("size-->"+vectorOfCondtions.size());
			String str = "";
			Condition con = null;
			DataElement data = null;
			Logger.out.debug("before str--"+nodeId);
			
			for(int k = 0; k < vectorOfCondtions.size(); k++){
				con = (Condition)vectorOfCondtions.get(k);
				data = con.getDataElement();
		        Operator op = con.getOperator();
		        String column = data.getField();
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
			if(child.isLeaf())
			{
				nodeId++;
			}
			else
				arrangeTree(child,nodeId,tree,advancedConditionNodesMap);
		}
	
	}
}
