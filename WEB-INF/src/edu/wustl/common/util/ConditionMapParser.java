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

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author poornima_govindrao
 *
 * ConditionMapParser is the parser class to parse the Condition object and create advancedConditionNode for Advance Search
 */
public class ConditionMapParser
{
	private Map createMap(String []keys,String []values)
	{
		Map map = new HashMap();
		for(int i=0;i<keys.length;i++)
			map.put(keys[i],values[i]);
		return map;
	}
	//Given a Map, parseCondition function creates list of conditions
	public List parseCondition(Map conditionMap)
	{
		List conditionList=new ArrayList();
		Iterator keyItr = conditionMap.keySet().iterator();
		while(keyItr.hasNext())
		{
			DataElement dataElement = new DataElement();
			String key = (String)keyItr.next();
			//Check for the keys of operators which is in the form Operator:TableAliasName:ColumnName
			if(key.startsWith("Operator"))
			{
				StringTokenizer st = new StringTokenizer(key, ":");
				String operator=(String)conditionMap.get(key);
				if(!operator.equals(Constants.ANY))
				{
					String value=new String();
					String value2=new String();
					String operator1=new String();
					String operator2=new String();
					while(st.hasMoreTokens())
					{
						st.nextToken();
						String aliasName = st.nextToken();
						//Logger.out.debug("table name in condition obj"+aliasName);
						dataElement.setTable(aliasName);
						String columnName = st.nextToken();
						dataElement.setField(columnName);
						value = (String)conditionMap.get(aliasName+":"+columnName);
						//Create two different conditions in case of Between and Not Between operators.
						if(operator.equals(Operator.NOT_BETWEEN))
						{
							operator1 = Operator.LESS_THAN_OR_EQUALS;
							operator2 = Operator.GREATER_THAN_OR_EQUALS;
							value2 = (String)conditionMap.get(aliasName+":"+columnName+":"+"HLIMIT");
						}
						if(operator.equals(Operator.BETWEEN))
						{
							operator1 = Operator.GREATER_THAN_OR_EQUALS;
							operator2 = Operator.LESS_THAN_OR_EQUALS;
							value2 = (String)conditionMap.get(aliasName+":"+columnName+":"+"HLIMIT");
						}
					}
					//String operatorValue = Operator.getOperator(operator);
					Condition condition = new Condition(dataElement,new Operator(operator),value);
					if(operator.equals(Operator.NOT_BETWEEN))
					{
						condition = new Condition(dataElement,new Operator(operator2),value2);
						Condition condition1 = new Condition(dataElement,new Operator(operator1),value);
						conditionList.add(condition1);
					}
					if(operator.equals(Operator.BETWEEN))
					{
						condition = new Condition(dataElement,new Operator(operator2),value2);
						Condition condition1 = new Condition(dataElement,new Operator(operator1),value);
						conditionList.add(condition1);
					}
					conditionList.add(condition);
				}
			}
		}
		return conditionList;
	}
	//Given a list of conditions, creates an advancedConditionNode and adds it to the root.
	
	public DefaultMutableTreeNode createAdvancedQueryObj(List list,DefaultMutableTreeNode root,String objectName,String selectedNode,Map advancedConditionNodesMap) 
	{
		//Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
//		String tableObject = condition.getDataElement().getTable();
		Logger.out.debug("selectedNode"+selectedNode);
		String prevTableObj;
		AdvancedConditionsNode advancedConditionsNode = new AdvancedConditionsNode(objectName);
		
		Iterator itr = list.iterator();
		while(itr.hasNext())
		{
			Condition condition = (Condition)itr.next();
			advancedConditionsNode.addConditionToNode(condition);
		}
		
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(advancedConditionsNode);
		

		if(root.getChildCount()==0)
		{
			root.add(child);
		}
		else
		{
			int nodeCount=0;
			addNode(root,Integer.parseInt(selectedNode),nodeCount,child,objectName,advancedConditionNodesMap);
			Logger.out.debug("root size"+root.getDepth());
			
		}
		traverseTree(root);
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
		String keys1[] = {"Participant:LAST_NAME","Participant:GENDER","Operator:Participant:LAST_NAME","Operator:Participant:GENDER"};
		String values1[] = {"Part","Male","LIKE","EQUALS"};
		String keys2[] = {"CollectionProtocolRegistration:INDENTIFIER","Operator:CollectionProtocolRegistration:INDENTIFIER"};
		String values2[] = 	{"1","GREATER_THAN"};		 
		String keys3[] = {"CollectionProtocolRegistration:INDENTIFIER","Operator:CollectionProtocolRegistration:INDENTIFIER"};
		String values3[] = 	{"10","LESS_THAN"};	
		String keys4[] = {"SpecimenCollectionGroup:CLINICAL_STATUS","Operator:SpecimenCollectionGroup:CLINICAL_STATUS"};
		String values4[] = 	{"Relapse","Equal"};	
		String keys5[] = {"Participant:LAST_NAME","Operator:Participant:LAST_NAME"};
		String values5[] = 	{"A","LIKE"};	
		String keys6[] = {"Specimen:TYPE","Operator:Specimen:TYPE"};
		String values6[] = 	{"cDNA","EQUAL"};	

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
//		root = conditionParser.createAdvancedQueryObj(dataCollection1,root,"Participant","");
//		root = conditionParser.createAdvancedQueryObj(dataCollection2,root,"CollectionProtocolRegistration","");
//		root = conditionParser.createAdvancedQueryObj(dataCollection3,root,"CollectionProtocolRegistration","");
//		root = conditionParser.createAdvancedQueryObj(dataCollection4,root,"SpecimenCollectionGroup","");
//		root = conditionParser.createAdvancedQueryObj(dataCollection5,root,"Participant","");
//		root = conditionParser.createAdvancedQueryObj(dataCollection6,root,"Specimen","");
		conditionParser.traverseTree(root);
	}
	//Traverse root and display map contents.
	private void traverseTree(DefaultMutableTreeNode tree)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		int childCount = tree.getChildCount();
		Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			child = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode advNode1 = (AdvancedConditionsNode)child.getUserObject();
			Vector conditions1 = advNode1.getObjectConditions();
			Iterator itr1 = conditions1.iterator();
			while(itr1.hasNext())
			{
				Condition condition1 = (Condition)itr1.next();
				Logger.out.debug("Column Name: "+condition1.getDataElement().getField());
			}
			traverseTree(child);
		}
	}
	//Add advancedConditionNode
	private void addNode(DefaultMutableTreeNode tree,int selectedNode,int nodeCount,DefaultMutableTreeNode presentNode,String objectName,Map advancedConditionNodesMap)
	{
		//DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
		DefaultMutableTreeNode selectedAdvNode = (DefaultMutableTreeNode) advancedConditionNodesMap.get(new Integer(selectedNode));
		String presentObjectName =((AdvancedConditionsNode)selectedAdvNode.getUserObject()).getObjectName();
		Logger.out.debug("selectedAdvNode's object name"+((AdvancedConditionsNode)selectedAdvNode.getUserObject()).getObjectName());

		if(objectName.equals(Constants.PARTICIPANT))
		{
			selectedAdvNode = (DefaultMutableTreeNode) advancedConditionNodesMap.get(new Integer(1));
			parent =(DefaultMutableTreeNode) selectedAdvNode.getParent();
			parent.add(presentNode);
		}
		else if(objectName.equals(presentObjectName))
		{
			parent =(DefaultMutableTreeNode) selectedAdvNode.getParent();
			parent.add(presentNode);
		
		}
		else		
		selectedAdvNode.add(presentNode);
	}
}