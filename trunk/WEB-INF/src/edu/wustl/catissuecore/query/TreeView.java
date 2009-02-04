/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.query;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.query.AdvancedConditionsNode;
import edu.wustl.common.query.Condition;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Operator;
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
	private boolean noOrBool = false;
	
	//Recursive function to create the tree
	public void arrangeTree(DefaultMutableTreeNode node,int parentId,Vector tree,Map advancedConditionNodesMap,int checkedNode,String operation,HttpSession session) throws Exception
	{
			nodeId++;
			//Checking whether parent rule has more than 1 child rule or not(OR condition)
			if(node.getChildCount() > 1)
			{
				Logger.out.debug("childcount::"+node.getChildCount());
				AdvancedConditionsNode advConditionDefaultOrNode = (AdvancedConditionsNode)node.getUserObject();
				Logger.out.debug("advConditionDefaultOrNode::"+advConditionDefaultOrNode);
				
				//Setting to true to display DOT image,symbol of OR condition
				if(advConditionDefaultOrNode != null)
					advConditionDefaultOrNode.setDefaultAndOr(true);
			}
			
			//Loop for all the children for the current node.
			for(int i = 0; i < node.getChildCount();i++)
			{
				//nodeCount++;
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)child.getParent();
				
				//Condition that allow to start from Participant as a parent node
				if(!parent.isRoot())
				{
					AdvancedConditionsNode parentAdvConditionNode = (AdvancedConditionsNode)parent.getUserObject();
					Operator operator = parentAdvConditionNode.getOperationWithChildCondition();
					String temp = operator.getOperator();
					Logger.out.debug("operator "+temp);
					//Condition to provide Psudo And 
					if(temp.equals(Operator.EXIST) && operator.getOperatorParams()[0].equals(Operator.AND))
					{
						andOrBool = true;
					}
					else
					{
						andOrBool = false;
					}
					
					//Boolean for setting value false in (String array)rule which will display DOT image 
					if(parentAdvConditionNode.isDefaultAndOr())
					{
						noOrBool = true;
					}
					else
						noOrBool = false;
				}
				else
				{
					//Condition only when Parent is Root
					if(parent.getChildCount() > 1)	
						noOrBool = true;
					else
						noOrBool = false;
				}
				AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)child.getUserObject();
				advancedConditionNodesMap.put(new Integer(nodeId),child);
				
				AdvancedConditionsNode temp = (AdvancedConditionsNode)session.getAttribute("tempAdvConditionNode");
				if(advConditionNode.getObjectName().equals(temp.getObjectName()))
				{
					session.setAttribute("lastNodeId",(""+nodeId));
				}
				
				if(nodeId == checkedNode)
				{
					Logger.out.debug("operation inside if nodeid clicked"+operation);
					if(operation.equals(Operator.EXIST))
					{
						Logger.out.debug("Setting the child condition");
						//Condition to set value only when selected node has child
						if(child.getChildCount() > 0)
							advConditionNode.setOperationWithChildCondition(new Operator(Operator.EXIST));
					}
					else
					{
						advConditionNode.setOperationWithChildCondition(new Operator(Operator.OR));
					}
				}
				
				Vector vectorOfCondtions = advConditionNode.getObjectConditions();
				String tableName = advConditionNode.getObjectName();
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
			        String table = data.getTableAliasName();
			        //split column name in case of Specimen event parameters to remove aliasName
			        //StringTokenizer columnNameTokenizer = new StringTokenizer(columnName,".");
			        QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
			        //String columnDisplayName = bizLogic.getColumnDisplayNames(table,columnName);
			        
			        int formId = SearchUtil.getFormId(tableName);
			        String columnDisplayName = SearchUtil.getColumnDisplayName(formId,table,columnName);
			        
			        //append table name to the column name in case of event parameters conditions.
					StringTokenizer tableTokens = new StringTokenizer(table,".");
					String superTable = new String();
					if(tableTokens.countTokens()==2)
					{
						Logger.out.debug("table before tokenizing:"+table);
						table = tableTokens.nextToken();
						superTable = tableTokens.nextToken();
						
						Map eventParameterDisplayNames = SearchUtil.getEventParametersDisplayNames(bizLogic,SearchUtil.getEventParametersTables(bizLogic));
			        	columnDisplayName = (String)eventParameterDisplayNames.get(table+"."+columnName);
					}
		        	Logger.out.debug("column display name for event parameters"+columnDisplayName);
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
				{
					str = str +  "|" + tableName;
					
					if(andOrBool)
					{
						str = str + "|true";
					}
					else
					{
						if(noOrBool)
							str = str + "|false";
						else
							//Appending default which will not display any image
							str = str + "|default";
					}
				}
				
				if(con == null)
				{
					str = nodeId + "|" + parentId + "|" + "ANY" + "|"+advConditionNode.getObjectName();
					
					if(andOrBool)
		        	{
						str = str + "|true";
		        	}
					else
					{
						if(noOrBool)
							str = str + "|false";
						else
							str = str + "|default";
					}
				}
				Logger.out.debug("STR in TREVIEW--"+str);
				tree.add(str);
				andOrBool = false;
				if(child.isLeaf())
				{
					nodeId++;
				}
				else
					arrangeTree(child,nodeId,tree,advancedConditionNodesMap,checkedNode,operation,session);
			}
		} 
}
