package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
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
						Logger.out.debug("value1 "+value);
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
						Logger.out.debug("value2 "+value2);
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
	
	public DefaultMutableTreeNode createAdvancedQueryObj(List list,DefaultMutableTreeNode root,String objectName,String selectedNode,Map advancedConditionNodesMap,Integer nodeId) 
	{
		//Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,Query.PARTICIPANT);
//		String tableObject = condition.getDataElement().getTable();
		Logger.out.debug("selectedNode"+selectedNode);
		Vector objectConditions = new Vector(list);
//		String prevTableObj;
		Logger.out.debug("nodeId--"+nodeId);
		
		//Condition for Add operation
		if(nodeId == null)
		{
			AdvancedConditionsNode advancedConditionsNode = new AdvancedConditionsNode(objectName);
			
//			Iterator itr = list.iterator();
//			while(itr.hasNext())
//			{
//				Condition condition = (Condition)itr.next();
//				advancedConditionsNode.addConditionToNode(condition);
//			}
			advancedConditionsNode.setObjectConditions(objectConditions);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(advancedConditionsNode);
			
	
			if(root.getChildCount()==0)
			{
				root.add(child);
			
			}
			else
			{
				int nodeCount=0;
				if(selectedNode.equals(""))
					addNode(root,0,nodeCount,child,objectName,advancedConditionNodesMap);
				else
					addNode(root,Integer.parseInt(selectedNode),nodeCount,child,objectName,advancedConditionNodesMap);
				Logger.out.debug("root size"+root.getDepth());
			}
		}
		//Else edit operation
		else
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)advancedConditionNodesMap.get(nodeId);
			AdvancedConditionsNode advancedConditionsNode =  (AdvancedConditionsNode)node.getUserObject();
			advancedConditionsNode.setObjectConditions(objectConditions);
		}
		
		/**** Delete Function starts *****/
		/*if(delete)
		{
			//Map deleteNodeMap = new HashMap();
			TraverseTree traverseTree = new TraverseTree();
			DefaultMutableTreeNode node = traverseTree.getSelectedNode(root,nodeId);
			
			AdvancedConditionsNode advNode1 = (AdvancedConditionsNode)node.getUserObject();
			Vector conditions1 = advNode1.getObjectConditions();
			Iterator itr1 = conditions1.iterator();
			while(itr1.hasNext())
			{
				Condition condition1 = (Condition)itr1.next();
				Logger.out.debug("Column Name: "+condition1.getDataElement().getField());
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			int position = parent.getIndex(node);
			Logger.out.debug("position--"+position);
			//parent.remove(position);
			//Logger.out.debug("position using tree--"+root.getIndex(node));
		}*/
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
		Map map = new HashMap();
		map.put("EventName_1","CellSpecimenReviewEventParameters");
		map.put("EventColumnName_1","CellSpecimenReviewEventParameters.IDENTIFIER.bigint");
		map.put("EventColumnOperator_1","=");
		map.put("EventColumnValue_1","1");
		
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
		//conditionParser.traverseTree(root,null,null,false,0,null);
	}
	//Traverse root and display map contents.
	
	//Add advancedConditionNode
	private void addNode(DefaultMutableTreeNode tree,int selectedNode,int nodeCount,DefaultMutableTreeNode presentNode,String objectName,Map advancedConditionNodesMap)
	{
		//DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
		DefaultMutableTreeNode selectedAdvNode = (DefaultMutableTreeNode) advancedConditionNodesMap.get(new Integer(selectedNode));

		if(selectedNode==0)
		{
			selectedAdvNode = (DefaultMutableTreeNode) advancedConditionNodesMap.get(new Integer(0));
			selectedAdvNode.add(presentNode);
		}
		else
		{		
			parent = new DefaultMutableTreeNode();
			selectedAdvNode = (DefaultMutableTreeNode) advancedConditionNodesMap.get(new Integer(selectedNode));
			String presentObjectName =((AdvancedConditionsNode)selectedAdvNode.getUserObject()).getObjectName();
			Logger.out.debug("selectedAdvNode's object name"+((AdvancedConditionsNode)selectedAdvNode.getUserObject()).getObjectName());
			
			
			if(objectName.equals(presentObjectName))
			{
				parent =(DefaultMutableTreeNode) selectedAdvNode.getParent();
				parent.add(presentNode);
				
			}
			else		
				selectedAdvNode.add(presentNode);
		}
	}
	
	/**
     * This function parses the event parameter map & returns it in a format
     * suitable to parseCondition() function.
     * @param eventMap A map of specimen event parameters that is to be parsed.
     * @return Map the parsed map suitable for parseCondition().
     */
	public static Map parseEventParameterMap(Map eventMap)
	{
		Map newMap = new HashMap();
		
		if(eventMap != null)
		{
			int rows = eventMap.size() / 4;
			
			//Constants for eventMap keys
			String columnKeyConstant = "EventColumnName_";
			String columnValConstant = "EventColumnValue_";
			String operatorConstant = "EventColumnOperator_";
				
			for(int i=1;i<=rows;i++)
			{
				//Preparing the eventMap keys
				String columnKey = columnKeyConstant + i;
				String columnValKey = columnValConstant + i;
				String operatorKey = operatorConstant + i;

				String columnKeyValue = (String)eventMap.get(columnKey);				
				StringTokenizer tokenizer = new StringTokenizer(columnKeyValue,".");
				
				//Extracting alias name & column name
				String aliasName = tokenizer.nextToken();
				String columnName = tokenizer.nextToken();
				
				//Extracting actual column value & operator value
				String columnValue = (String)eventMap.get(columnValKey);
				String operatorValue = (String)eventMap.get(operatorKey);
				
				//Preparing keys for new map
				String newValKey = aliasName + ":" + columnName;
				String newOpKey = "Operator:" + aliasName + ":" + columnName;
				
				//Setting values in new map
				newMap.put(newValKey,columnValue);
				newMap.put(newOpKey,operatorValue);
			}
		}
		
		return newMap;
	}
}