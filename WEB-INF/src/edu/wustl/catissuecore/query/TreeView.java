/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
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
	private boolean andOrBool = false;
	
	//Recursive function to create the tree
	public void arrangeTree(DefaultMutableTreeNode node,int parentId,Vector tree,Map advancedConditionNodesMap,int checkedNode,String operation) throws Exception
	{
			nodeId++;
			
			//Loop for all the children for the current node.
			for(int i = 0; i < node.getChildCount();i++){
				//nodeCount++;
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)child.getParent();
				
				if(!parent.isRoot())
				{
					AdvancedConditionsNode parentAdvConditionNode = (AdvancedConditionsNode)parent.getUserObject();
					String temp = parentAdvConditionNode.getOperationWithChildCondition().getOperator();
					if(temp.equals(Operator.AND))
					{
						andOrBool = true;
					}
					else
					{
						andOrBool = false;
					}
					
				}
				AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)child.getUserObject();
				advancedConditionNodesMap.put(new Integer(nodeId),child);
								
				if(nodeId == checkedNode)
				{
//					if(operation !)
					if(operation.equals(Operator.AND))
					{
						advConditionNode.setOperationWithChildCondition(new Operator(Operator.AND));
					}
					else
					{
						advConditionNode.setOperationWithChildCondition(new Operator(Operator.OR));
					}
				}
				
				Vector vectorOfCondtions = advConditionNode.getObjectConditions();
				String tableName = advConditionNode.getObjectName();
				Logger.out.debug("object name for advance node"+tableName);
				Logger.out.debug("size-->"+vectorOfCondtions.size());
				String str = "";
				Condition con = null;
				DataElement data = null;
				
				for(int k = 0; k < vectorOfCondtions.size(); k++)
				{
					con = (Condition)vectorOfCondtions.get(k);
					data = con.getDataElement();
			        Operator op = con.getOperator();
			        String value = con.getValue();
			        String columnName = data.getField();
			        String table = data.getTable();
			        //split column name in case of Specimen event parameters to remove aliasName
			        //StringTokenizer columnNameTokenizer = new StringTokenizer(columnName,".");
			        QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
														.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
			        String columnDisplayName = bizLogic.getColumnDisplayNames(table,columnName);
			        //append table name to the column name in case of event parameters conditions.
			        if(table.indexOf("Parameter")>0)
			        	columnDisplayName = table+"."+columnName;
			        if(columnDisplayName.equals(""))
			        {
			        	columnDisplayName=columnName;
			        }
			        Logger.out.debug("Column Display name in tree view:"+columnDisplayName);
			        String column = data.getField();
			        if(k == 0)
			        {
			        	
			        	//str = temp + "|" + parentId + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
			        	str = nodeId + "|" + parentId + "|" +columnDisplayName+ " "+ op.getOperator() + " " + value+ "";
			        }
			        else
			        	//str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
			        	str = str +" "+"<font color='red'>AND</font>"+" "+columnDisplayName+" "+op.getOperator() + " "+value+ "";
			        // entered by Mandar for validation of single quotes around the values.
			        Logger.out.debug( "STR :---------- : "+ str);
			    }
				if(data != null)
					if(andOrBool)
		        	{
						str = str +  "|" + tableName + "|true";
		        	}
					else
					{
						str = str +  "|" + tableName + "|false";
					}
				
				if(con == null)
				{
					if(andOrBool)
		        	{
						str = nodeId + "|" + parentId + "|" + "ANY" + "|"+advConditionNode.getObjectName() + "|true";
		        	}
					else
					{
						str = nodeId + "|" + parentId + "|" + "ANY" + "|"+advConditionNode.getObjectName() + "|false";
					}
				}
				Logger.out.debug("STR in TREVIEW--"+str);
				tree.add(str);
				if(child.isLeaf())
				{
					nodeId++;
				}
				else
					arrangeTree(child,nodeId,tree,advancedConditionNodesMap,checkedNode,operation);
			}
		} 
	
	
}
