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
		while(enum.hasMoreElements())
		{
			child = (DefaultMutableTreeNode) enum.nextElement();
			parent = (DefaultMutableTreeNode) child.getParent();
			if(parent != null && !parent.isRoot())
			{
				AdvancedConditionsNode parentAdvConditionNode = (AdvancedConditionsNode)parent.getUserObject();
				String operationWithChildren = parentAdvConditionNode.getOperationWithChildCondition().getOperator();
				AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)child.getUserObject();
				if(parentAdvConditionNode.getObjectName().equals(Query.SPECIMEN))
				{
					setTableAliasOfChild(parent,advConditionNode,operationWithChildren,child.getLevel(),parent.getIndex(child));
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
		String tableName = advConditionNode.getObjectName();
		String str = "";
		Condition con = null;
		DataElement data = null;
		
		//first level parent
		AdvancedConditionsNode parent1 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)parentNode).getUserObject();
		//second level parent
		AdvancedConditionsNode parent2 = (AdvancedConditionsNode) ((DefaultMutableTreeNode)((DefaultMutableTreeNode)parentNode).getParent()).getUserObject();
		Logger.out.debug( "parents operation on children:"+temp);
		
		//If there is no condition add a condition Identifier > 0 so that specimen gets included in query
		if(vectorOfCondtions.size() == 0)
		{
			vectorOfCondtions.add(getIdentifierCondition(Query.SPECIMEN));
		}
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
				
				if(table2.getTableName().equals(Query.SPECIMEN) )
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
					}
					else
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN,
							Query.SPECIMEN));
					}
					
					table2.setTableAliasAppend(table2.getTableName() + level+"L");
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
					}
					else
					{
						table2.setLinkingTable(new Table(Query.SPECIMEN,
								Query.SPECIMEN+ level+"L",new Table(Query.SPECIMEN,
							Query.SPECIMEN)));
					}
					
					table2.setTableAliasAppend(table2.getTableName() + level+"L");
				}
				//else SpecimenEventParameters condition
				else
				{
					
					if(parent2.getObjectName().equals(Query.SPECIMEN) && !parent2.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
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
				
				
//				table2.setTableName(table2.getTableName());
				
			} else {
				
				//if event parameter tables
				if(table2.getTableName().indexOf(Query.PARAM)!=-1)
				{
					table2.setLinkingTable(new Table(Query.SPECIMEN_EVENT_PARAMETERS,
							Query.SPECIMEN_EVENT_PARAMETERS_APPEND+table2.getTableName()+ level+"L"+childIndex+"C",new Table(Query.SPECIMEN,
							Query.SPECIMEN)));
					table2.setTableAliasAppend(table2.getTableName() + level+"L"+childIndex+"C");
				}
				else
				{
					table2
							.setLinkingTable(new Table(Query.SPECIMEN,
									Query.SPECIMEN));
					table2.setTableAliasAppend(table2.getTableName());
					
				}
				
			}
			
			table2.setTableName(table2.getTableName());
			Logger.out.debug("table name:" + table2.getTableName()
					+ " Alias:" + table2.getTableAliasAppend());

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
			parent2ConVector.add(getIdentifierCondition(Query.SPECIMEN));
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
