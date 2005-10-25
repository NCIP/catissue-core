package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class ConditionMapParser
{
	private Map createMap(String []keys,String []values)
	{
		Map map = new HashMap();
		for(int i=0;i<keys.length;i++)
			map.put(keys[i],values[i]);
		return map;
	}
	public List parseCondition(Map conditionMap)
	{
		List conditionList=new ArrayList();
		Iterator keyItr = conditionMap.keySet().iterator();
		while(keyItr.hasNext())
		{
			DataElement dataElement = new DataElement();
			String key = (String)keyItr.next();
			if(key.startsWith("Operator"))
			{
				StringTokenizer st = new StringTokenizer(key, ":");
				String operator=(String)conditionMap.get(key);
				if(!operator.equals(Constants.ANY))
				{
					String value=new String();
					while(st.hasMoreTokens())
					{
						st.nextToken();
						String aliasName = st.nextToken();
						Logger.out.debug("table name in condition obj"+aliasName);
						dataElement.setTable(aliasName);
						String columnName = st.nextToken();
						dataElement.setField(columnName);
						value = (String)conditionMap.get(aliasName+":"+columnName);
					}
					String operatorValue = Operator.getOperator(operator);
					Condition condition = new Condition(dataElement,new Operator(operator),value);
					conditionList.add(condition);
				}
			}
		}
		return conditionList;
	}
	public DefaultMutableTreeNode createAdvancedQueryObj(List list,DefaultMutableTreeNode root) 
	{
		//Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
		 
		Iterator itr = list.iterator();
		Condition condition = (Condition)itr.next();
		String prevTableObj;
		String tableObject = condition.getDataElement().getTable();
		AdvancedConditionsNode advancedConditionsNode = new AdvancedConditionsNode(tableObject);
		advancedConditionsNode.addConditionToNode(condition);
		while(itr.hasNext())
		{
			condition = (Condition)itr.next();
			advancedConditionsNode.addConditionToNode(condition);
		}
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(advancedConditionsNode);
		System.out.println("Child: "+child);
		if(root.getChildCount()==0)
		{
			root.add(child);
			//System.out.println("Root: "+root.getLevel());
		}
		else
		{
			String presentTable = new String();
			Enumeration children = root.children();
			while(children.hasMoreElements())
			{
				DefaultMutableTreeNode child1 = (DefaultMutableTreeNode)children.nextElement();
				AdvancedConditionsNode advNode = (AdvancedConditionsNode)child1.getUserObject();
				Vector conditions = advNode.getObjectConditions();
				Iterator itr1 = conditions.iterator();
				while(itr1.hasNext())
				{
					Condition cond = (Condition)itr1.next();
					presentTable = cond.getDataElement().getTable();
				}
			}
			if(tableObject.equals(presentTable))
			{
				root.add(child);
			}
			else
			{
				AdvancedConditionsNode advNode = (AdvancedConditionsNode)root.getLastLeaf().getUserObject();
				List conditions = (List)advNode.getObjectConditions();
				Condition parentCondition = (Condition)conditions.get(0);
				presentTable = parentCondition.getDataElement().getTable();
				if(tableObject.equals(presentTable))
				{
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode)root.getLastLeaf().getParent();
					parent.add(child);
				}
				else
					root.getLastLeaf().add(child);
			}
		}
		//((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(root);
		//advQueryObj.setTableSet(fromTables);
		//List dataList = query.execute();
		//System.out.println("Data: "+list);
		//String fileName = Variables.catissueHome+System.getProperty("file.separator")+"AdvancedQueryResult.csv";
		//ExportReport exp = new ExportReport(fileName);
		return root;
	}
	public static void main(String[] args) throws Exception
	{
		ConditionMapParser conditionParser = new ConditionMapParser();
		String keys1[] = {"Participant:LAST_NAME:LIKE","Participant:GENDER:Equal"};
		String values1[] = {"Part","Male"};
		String keys2[] = {"CollectionProtocolRegistration:INDENTIFIER:GREATER_THAN"};
		String values2[] = 	{"1"};		 
		String keys3[] = {"CollectionProtocolRegistration:INDENTIFIER:LESS_THAN"};
		String values3[] = 	{"10"};	
		String keys4[] = {"SpecimenCollectionGroup:CLINICAL_STATUS:Equal"};
		String values4[] = 	{"Relapse"};	
		String keys5[] = {"Participant:LAST_NAME:LIKE"};
		String values5[] = 	{"A"};	
		String keys6[] = {"Specimen:TYPE:EQUAL"};
		String values6[] = 	{"cDNA"};	

		Map map1 = conditionParser.createMap( keys1, values1);
		System.out.println("Map: "+map1);
		Map map2 = conditionParser.createMap( keys2, values2);
		Map map3 = conditionParser.createMap( keys3, values3);
		Map map4 = conditionParser.createMap( keys4, values4);
		Map map5 = conditionParser.createMap( keys5, values5);
		Map map6 = conditionParser.createMap( keys6, values6);
		//System.out.println(map);
		
		List dataCollection1 = conditionParser.parseCondition(map1);
		System.out.println("List: "+dataCollection1);
		List dataCollection2 = conditionParser.parseCondition(map2);
		List dataCollection3 = conditionParser.parseCondition(map3);
		List dataCollection4 = conditionParser.parseCondition(map4);
		List dataCollection5 = conditionParser.parseCondition(map5);
		List dataCollection6 = conditionParser.parseCondition(map6);

		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root = conditionParser.createAdvancedQueryObj(dataCollection1,root);
		root = conditionParser.createAdvancedQueryObj(dataCollection2,root);
		root = conditionParser.createAdvancedQueryObj(dataCollection3,root);
		root = conditionParser.createAdvancedQueryObj(dataCollection4,root);
		root = conditionParser.createAdvancedQueryObj(dataCollection5,root);
		root = conditionParser.createAdvancedQueryObj(dataCollection6,root);
		conditionParser.traverseTree(root);
	}
	private void traverseTree(DefaultMutableTreeNode tree)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		int childCount = tree.getChildCount();
		System.out.println("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			child = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode advNode1 = (AdvancedConditionsNode)child.getUserObject();
			Vector conditions1 = advNode1.getObjectConditions();
			Iterator itr1 = conditions1.iterator();
			while(itr1.hasNext())
			{
				Condition condition1 = (Condition)itr1.next();
				System.out.println("Column Name: "+condition1.getDataElement().getField());
			}
			traverseTree(child);
		}
	}
}