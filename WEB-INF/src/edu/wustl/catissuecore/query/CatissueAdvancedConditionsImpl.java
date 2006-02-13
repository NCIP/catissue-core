package edu.wustl.catissuecore.query;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CatissueAdvancedConditionsImpl extends AdvancedConditionsImpl {
	
	public void formatTree()
	{
		DefaultMutableTreeNode root = this.getWhereCondition();
		Enumeration enum = root.breadthFirstEnumeration();
		DefaultMutableTreeNode child;
		DefaultMutableTreeNode parent;
		
		Operator operator;
		
		//For all the nodes that are either specimen nodes or
		//Specimen Collection Group nodes and have operationWithChildCondition as "OR"
		//make the operation as 'EXIST' with parameter 'OR' to it.
		//This would solve the problem of Specimen Event parameters as mentioned in bug# 960
		while(enum.hasMoreElements())
		{
			child = (DefaultMutableTreeNode) enum.nextElement();
			parent = (DefaultMutableTreeNode) child.getParent();
			AdvancedConditionsNode advConditionNode = null;
			advConditionNode = (AdvancedConditionsNode)child.getUserObject();
			if(advConditionNode != null)
			{
				if((advConditionNode.getObjectName().equals(Query.SPECIMEN) || 
						advConditionNode.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP)) 
						&& advConditionNode.getOperationWithChildCondition().getOperator().equals(Operator.OR))
				{
					operator = new Operator(Operator.EXIST);
					operator.setOperatorParams(new String[] {Operator.OR});
					advConditionNode.setOperationWithChildCondition(operator);
				}
			}
		}
		
		enum = root.breadthFirstEnumeration();
		while(enum.hasMoreElements())
		{
			child = (DefaultMutableTreeNode) enum.nextElement();
			parent = (DefaultMutableTreeNode) child.getParent();
			
			
			if(parent != null)
			{
				AdvancedConditionsNode advConditionNode = null;
				advConditionNode = (AdvancedConditionsNode)child.getUserObject();
				Vector vectorOfCondtions = advConditionNode.getObjectConditions();
				//If there is no condition add a condition Identifier > 0 
				if(vectorOfCondtions.size() == 0)
				{
					vectorOfCondtions.add(getIdentifierCondition(advConditionNode.getObjectName()));
				}
				if( !parent.isRoot())
				{
					AdvancedConditionsNode parentAdvConditionNode = (AdvancedConditionsNode)parent.getUserObject();
					String operationWithChildren = parentAdvConditionNode.getOperationWithChildCondition().getOperator();
					if(parentAdvConditionNode.getObjectName().equals(Query.SPECIMEN) || parentAdvConditionNode.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP))
					{
						setTableAliasOfChild(parent,advConditionNode,operationWithChildren,child.getLevel(),parent.getIndex(child));
					}
				}
			}
		}
	}
	
	/**
	 * @param child
	 * @param temp
	 * @param depth
	 */
	private void setTableAliasOfChild(final TreeNode parentNode,final AdvancedConditionsNode advConditionNode,
			String temp, int level, int childIndex) {
		
		Vector vectorOfCondtions = advConditionNode.getObjectConditions();
		Vector additionalConditions = new Vector();
		String tableName = advConditionNode.getObjectName();
		String str = "";
		Condition con = null;
		DataElement data = null;
		
		//first level parent
		AdvancedConditionsNode parent1 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)parentNode).getUserObject();
		//second level parent
		AdvancedConditionsNode parent2 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)((DefaultMutableTreeNode)parentNode).getParent()).getUserObject();
		Logger.out.debug( "tablename:"+tableName+" parents operation on children:"+temp);
		for (int k = 0; k < vectorOfCondtions.size(); k++) {
			
			con = (Condition) vectorOfCondtions.get(k);
			data = con.getDataElement();
			Operator op = con.getOperator();
			String value = con.getValue();
			String columnName = data.getField();
			String table = data.getTableAliasName();
			
			//If children of specimen node then change aliasname of children
			
			Table table2 = data.getTable();
			
			if (temp.equals(Operator.OR)) {
				
				if(table2.getTableName().equals(Query.SPECIMEN))
				{
					//If parents parent is also Specimen
					if(parent2.getObjectName().equals(Query.SPECIMEN) && !parent2.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						//						//third level parent
						//						AdvancedConditionsNode parent1 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)parentNode).getUserObject();
						//parent3 conditions vector
						Table parent2LinkingTable = getParentLinkingTable(parent2);
						table2.setLinkingTable(new Table(Query.SPECIMEN,
								Query.SPECIMEN+(level-1)+"L",parent2LinkingTable));
						table2.setTableAliasAppend(table2.getTableName() + level+"L");
					}
					else if(parent1.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP) && !parent1.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN_COLLECTION_GROUP));
					}
					else
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN,Query.SPECIMEN));
						table2.setTableAliasAppend(table2.getTableName() + level+"L");
					}
				}
				//IF the condition belongs to specimen characteristics
				else if(table2.getTableName().equals(Query.SPECIMEN_CHARACTERISTICS))
				{
					
					//					table2.setLinkingTable(new Table(Query.SPECIMEN,
					//							Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
					//									Query.SPECIMEN)));
					//					If parents parent is also Specimen
					if(parent2.getObjectName().equals(Query.SPECIMEN) && !parent2.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						//						//third level parent
						//						AdvancedConditionsNode parent1 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)parentNode).getUserObject();
						//parent3 conditions vector
						Table parent2LinkingTable = getParentLinkingTable(parent2);
						table2.setLinkingTable(new Table(Query.SPECIMEN,
								Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
										Query.SPECIMEN+(level-1)+"L",parent2LinkingTable)));
						table2.setTableAliasAppend(table2.getTableName() + level+"L");
					}
					else if(parent1.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP) && !parent1.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN_COLLECTION_GROUP));
					}
					else
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN,
								Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
										Query.SPECIMEN)));
						table2.setTableAliasAppend(table2.getTableName() + level+"L");
					}
					
					
				}
				//else SpecimenEventParameters condition
				else
				{
					if(parent1.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP) && !parent1.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						if(table2.getTableName().equals(Query.SPECIMEN_EVENT_PARAMETERS))
						{
							table2.setLinkingTable(new Table(Query.SPECIMEN,
									Query.SPECIMEN));
						}
						else if(table2.getTableName().indexOf(Query.PARAM)!=-1) 
						{
							table2.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
									Query.SPECIMEN_EVENT_PARAMETERS_APPEND+table2.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
											Query.SPECIMEN)));
						}
					}
					
					else if(parent2.getObjectName().equals(Query.SPECIMEN) && !parent2.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
					{
						Table parent2LinkingTable = getParentLinkingTable(parent2);
						table2.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
								Query.SPECIMEN_EVENT_PARAMETERS_APPEND+table2.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
										Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
												Query.SPECIMEN+(level-1)+"L",parent2LinkingTable))));
					}
					else
					{
						
						table2.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
								Query.SPECIMEN_EVENT_PARAMETERS_APPEND+table2.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
										Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
												Query.SPECIMEN))));
					}
					table2.setTableAliasAppend(table2.getTableName() + level+"L"+childIndex+"C");
					
				}
			} 
			
			//if operation is EXIST
			else 
			{
				//if event parameter tables
				//if attribute is of the base SpecimenEventParameter class
				if(table2.getTableName().equals(Query.SPECIMEN_EVENT_PARAMETERS))
				{
					//Aarti: A condition corresponding to actual event is added 
					//so that a link is obtained between actual event parameter class and specimenEventParameter
					Condition actualEventIdentifierCondition = getIdentifierCondition(table2.getLinkingTable().getTableName());
					Table eventTable = actualEventIdentifierCondition.getDataElement().getTable();
					eventTable.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
							Query.SPECIMEN_EVENT_PARAMETERS_APPEND+eventTable.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
									Query.SPECIMEN,new Table(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN_COLLECTION_GROUP))));
					eventTable.setTableAliasAppend(eventTable.getTableName() + level+"L"+childIndex+"C");
					Logger.out.debug("table:"+table2+ " Linking table:"+table2.getLinkingTable().getTableName()+" event table:"+eventTable);
					table2.setTableAliasAppend(table2.getTableName() + level+"L"+childIndex+"C");
					additionalConditions.add(actualEventIdentifierCondition);
//					advConditionNode.addConditionToNode(actualEventIdentifierCondition);
					table2.setLinkingTable(new Table(Query.SPECIMEN,
							Query.SPECIMEN));
					table2.setTableAliasAppend(Query.SPECIMEN_EVENT_PARAMETERS_APPEND+eventTable.getTableName()+ level+"L"+childIndex+"C");
					
				}
				//if attribute chosen belongs to the derived event class itself
				else if(table2.getTableName().indexOf(Query.PARAM)!=-1)
				{
					table2.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
							Query.SPECIMEN_EVENT_PARAMETERS_APPEND+table2.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
									Query.SPECIMEN,new Table(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN_COLLECTION_GROUP))));
					table2.setTableAliasAppend(table2.getTableName() + level+"L"+childIndex+"C");
				}
				//else when its not any event condition
				else 
				{
					table2.setLinkingTable(new Table(Query.SPECIMEN,Query.SPECIMEN));
					table2.setTableAliasAppend(table2.getTableName());
				}
			}
			
			table2.setTableName(table2.getTableName());
			Logger.out.debug("table name: " + table2.getTableName()
					+ " Alias: " + table2.getTableAliasAppend()+" Linking table:"+table2.getLinkingTable());
		}
		
		for(int i=0; i< additionalConditions.size(); i++)
		{
			Logger.out.debug(" additional condition added:"+(Condition) additionalConditions.get(i));
			advConditionNode.addConditionToNode((Condition) additionalConditions.get(i));
		}
	}
	
	/**
	 * @param parent2
	 * @return
	 */
	private Table getParentLinkingTable(AdvancedConditionsNode parent2) {
		Vector parent2ConVector = parent2.getObjectConditions();
		if(parent2ConVector.size() == 0)
		{
			if(parent2.getObjectName().equals(Query.SPECIMEN))
				parent2ConVector.add(getIdentifierCondition(Query.SPECIMEN));
			else if(parent2.getObjectName().equals(Query.SPECIMEN_COLLECTION_GROUP))
				parent2ConVector.add(getIdentifierCondition(Query.SPECIMEN_COLLECTION_GROUP));
		}
		//Get table from any one of the conditions and set it as linking table
		Condition parent2Con = (Condition) parent2ConVector.get(0);
		Table parent2LinkingTable = parent2Con.getDataElement().getTable();
		return parent2LinkingTable;
	}
	
	/**
	 * @return
	 */
	private Condition getIdentifierCondition(String ObjClass) {
		Condition con;
		DataElement data;
		Logger.out.debug(" Zero conditions present in the node");
		con = new Condition();
		data = new DataElement(new Table(ObjClass,ObjClass),Constants.IDENTIFIER,Constants.FIELD_TYPE_BIGINT);
		con.setDataElement(data);
		con.setOperator(new Operator(Operator.GREATER_THAN));
		con.setValue("0");
		return con;
	} 
	
}
