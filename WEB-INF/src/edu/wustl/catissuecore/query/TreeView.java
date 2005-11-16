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

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
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
	public void arrangeTree(DefaultMutableTreeNode node,int parentId,Vector tree,Map advancedConditionNodesMap)
	{
		try {
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
				
				for(int k = 0; k < vectorOfCondtions.size(); k++)
				{
					con = (Condition)vectorOfCondtions.get(k);
					data = con.getDataElement();
			        Operator op = con.getOperator();
			        String value = con.getValue();
			        String columnName = data.getField();
			        QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
														.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
			        String columnDisplayName = bizLogic.getColumnDisplayNames(tableName,columnName);
			        Logger.out.debug("Column Display name in tree view:"+columnDisplayName);
			        //Change the operator to starts with or ends with or contains if it is 'Like' operator
			        if((op.getOperator()).equals(Operator.LIKE))
					{
			        	if((value.startsWith("'%")) && (value.endsWith("%'")))
			        	{
			        		op = new Operator(Operator.CONTAINS);
			        		value = value.replaceAll("%","");
			        	}
			        	else if(value.endsWith("%'"))
			        	{
			        		op = new Operator(Operator.STARTS_WITH);
			        		value = value.replaceAll("%","");
			        	}
			        	else if(value.startsWith("'%"))
			        	{
			        		op = new Operator(Operator.ENDS_WITH);
			        		value = value.replaceAll("%","");
			        	}
					}
			        if(value.startsWith("STR_TO_DATE"))
			        {
			        	value=value.substring(12,24);
			        }
			        if((op.getOperator()).equals(Operator.EQUAL))
					{
			        	op = new Operator(Operator.EQUALS_CONDITION);
					}
			        if((op.getOperator()).equals(Operator.NOT_EQUALS))
					{
			        	op = new Operator(Operator.NOT_EQUALS_CONDITION);
					}
			        String column = data.getField();
			        if(k == 0)
			        	//str = temp + "|" + parentId + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
			        	str = nodeId + "|" + parentId + "|" +columnDisplayName+ " "+ op.getOperator() + " " + value+ "";
			        else
			        	//str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
			        	str = str +" "+"<font color='red'>AND</font>"+" "+columnDisplayName+" "+op.getOperator() + " "+value+ "";
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
		catch (DAOException e) 
		{
			Logger.out.error(e.getMessage(),e);
		} 
		catch (ClassNotFoundException e) 
		{
			Logger.out.error(e.getMessage(),e);
		}
	
	}

}
