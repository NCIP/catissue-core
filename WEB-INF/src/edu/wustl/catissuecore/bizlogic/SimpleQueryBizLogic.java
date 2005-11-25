/**
 * <p>Title: SimpleQueryBizLogic Class>
 * <p>Description:	SimpleQueryBizLogic contains the bizlogic required for simple query interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * SimpleQueryBizLogic contains the bizlogic required for simple query interface.
 * @author gautam_shetty
 */
public class SimpleQueryBizLogic extends DefaultBizLogic
{
    /**
	 * Adds single quotes (') for string and date type attributes in the condition collecion 
	 * and the returns the Set of objects to which the condition attributes belong.
     * @param simpleConditionNodeCollection The condition collection.
     * @return the Set of objects to which the condition attributes belong.
     * @throws DAOException
     */
    public Set handleStringAndDateConditions(Collection simpleConditionNodeCollection, Set fromTables) throws DAOException
    {
        //Adding single quotes to strings and date values.
        Iterator iterator = simpleConditionNodeCollection.iterator();
        while (iterator.hasNext())
        {
            SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) iterator.next();
        	
        	// Add all the objects selected in UI to the fromtables Set. 
        	fromTables.add(simpleConditionsNode.getCondition().getDataElement().getTable());
        	
        	// Adds single quotes to the value of attributes whose type is string or date.
            String tableInPath = addSingleQuotes(simpleConditionsNode);
        	
        	//Get the tables in path for this field and add it in the fromTables Set. 
        	if (tableInPath != null)
        	{
        	    addTablesInPathToFromSet(fromTables, tableInPath);
        	}
        	
        	fromTables.add(simpleConditionsNode.getCondition().getDataElement().getTable());
        }
        return fromTables;
    }

    /**
     * Gets the alias names of the tables in path and adds them in the fromTables Set passed.
     * @param fromTables The Set to which the alias names are to be added.
     * @param tableInPath The ids of tables in path separated by :
     * @throws DAOException
     */
    private void addTablesInPathToFromSet(Set fromTables, String tableInPath) throws DAOException
    {
        StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
        while (tableInPathTokenizer.hasMoreTokens())
        {
            Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
            QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
            							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
            String aliasName = bizLogic.getAliasNameFromTableId(tableId);
            
            if (aliasName != null)
        	{
        	    fromTables.add(aliasName);
        	}
        }
    }

    /**
     * Adds quotes to the value of the attribute whose type is string or date 
     * and returns the tables in path for that object.
     * @param simpleConditionsNode The conditio node to be checked. 
     * @return The tables in path for that object.
     */
    private String addSingleQuotes(SimpleConditionsNode simpleConditionsNode)
    {
        String columnName = simpleConditionsNode.getCondition().getDataElement().getField();
        StringTokenizer stringToken = new StringTokenizer(columnName, ".");
        simpleConditionsNode.getCondition().getDataElement().setTable(stringToken.nextToken());
        simpleConditionsNode.getCondition().getDataElement().setField(stringToken.nextToken());
        String fieldType = stringToken.nextToken();
        String value = simpleConditionsNode.getCondition().getValue();
        String tableInPath = null;
        
        if (stringToken.hasMoreTokens())
        {
            tableInPath = stringToken.nextToken();
        }
        
        // For operators STARTS_WITH, ENDS_WITH, CONTAINS. 
        String operator = simpleConditionsNode.getCondition().getOperator().getOperator();
        if(operator.equals(Operator.STARTS_WITH)){
            simpleConditionsNode.getCondition().setValue(value+"%");
            simpleConditionsNode.getCondition().getOperator().setOperator(Operator.LIKE);
        }
        else if(operator.equals(Operator.ENDS_WITH)){
            simpleConditionsNode.getCondition().setValue("%"+value);
            simpleConditionsNode.getCondition().getOperator().setOperator(Operator.LIKE);
        }
        else if(operator.equals(Operator.CONTAINS)){
            simpleConditionsNode.getCondition().setValue("%"+value+"%");
            simpleConditionsNode.getCondition().getOperator().setOperator(Operator.LIKE);
        }
        
        if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR)
        		|| fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_DATE) 
        		|| fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_TEXT))
        {
        	if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR))
        	{
        		value = "'" + value + "'";
        	}
        	else
        	{
        		value = "STR_TO_DATE('" + value + "','" + Constants.MYSQL_DATE_PATTERN + "')";
        	}
        	
        	simpleConditionsNode.getCondition().setValue(value);
        }
        
        return tableInPath;
    }

    /**
	 * Adds the activity status conditions for all the objects in the from clause.
     * @param simpleConditionNodeCollection The SimpleConditionsNode Collection.
     * @param fromTables Set of tables in the from clause of the query.
     * @param simpleConditionsNode The last condition in the simpleConditionNode's Collection.
     */
    public void addActivityStatusConditions(Collection simpleConditionNodeCollection, Set fromTables)
    {
        // Creating aliasName set with full package names.
        // Required for checking the activityStatus.
        Set fromTablesWithPackageNames = new HashSet();
        Iterator fromTableSetIterator = fromTables.iterator();
        while (fromTableSetIterator.hasNext())
        {
            String tableName = "edu.wustl.catissuecore.domain."+fromTableSetIterator.next();
            fromTablesWithPackageNames.add(tableName);
        }
        
        // Check and get the activity status conditions for all the objects in the conditions.
        List activityStatusConditionList = new ArrayList();
        Iterator aliasNameIterator = fromTablesWithPackageNames.iterator();
        while (aliasNameIterator.hasNext())
        {
            String fullyQualifiedClassName = (String) aliasNameIterator.next();
            SimpleConditionsNode activityStatusCondition = getActivityStatusCondition(fullyQualifiedClassName);
            
            if (activityStatusCondition != null)
            {
                activityStatusCondition.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
                activityStatusConditionList.add(activityStatusCondition);
            }
        }
        
        if (activityStatusConditionList.isEmpty() == false)
        {
            // Set the next operator of the last simple condition nodes as AND.
            Iterator iterator = simpleConditionNodeCollection.iterator();
            SimpleConditionsNode simpleConditionsNode = null;
            while (iterator.hasNext())
            {
                simpleConditionsNode = (SimpleConditionsNode) iterator.next();    
            }
            simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
            
            // Add the activity status conditions in the simple conditions node collection.
            simpleConditionNodeCollection.addAll(activityStatusConditionList);
        }
    }

    /**
	 * Returns SimpleConditionsNode if the object named aliasName contains the activityStatus 
	 * data member, else returns null.
	 * @param fullyQualifiedClassName The fully qualified name of the class in which 
	 * activity status field is to be searched.
	 * @return SimpleConditionsNode if the object named aliasName contains the activityStatus 
	 * data member, else returns null.
	 */
	private SimpleConditionsNode getActivityStatusCondition(String fullyQualifiedClassName)
	{
		SimpleConditionsNode activityStatusCondition = null;

		//Returns the Class object if it is a valid class else returns null.
		Class className = edu.wustl.common.util.Utility.getClassObject(fullyQualifiedClassName);
		if (className != null)
		{
		    Field[] objectFields = className.getDeclaredFields();

			for (int i = 0; i < objectFields.length; i++)
			{
				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
				{
					activityStatusCondition = new SimpleConditionsNode();
					activityStatusCondition.getCondition().getDataElement().setTable(
							Utility.parseClassName(fullyQualifiedClassName));
					activityStatusCondition.getCondition().getDataElement().setField(
							Constants.ACTIVITY_STATUS_COLUMN);
					activityStatusCondition.getCondition().getOperator().setOperator("!=");
					activityStatusCondition.getCondition().setValue(
							"'" + Constants.ACTIVITY_STATUS_DISABLED + "'");
				}
			}
			
			if ((activityStatusCondition == null) &&
					(className.getSuperclass().getName().equals(
							"edu.wustl.catissuecore.domain.AbstractDomainObject") == false))
			{
				activityStatusCondition = getActivityStatusCondition(className.getSuperclass()
						.getName());
			}
		}
	    
		return activityStatusCondition;
	}
	
	 private Vector getViewElements(String []selectedColumnsList) 
		{
	    	/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames 
	    	 * and set the dataelement object.
	    	 */
		    Vector vector = new Vector();
		    for(int i=0;i<selectedColumnsList.length;i++)
		    {
		    	StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
		    	DataElement dataElement = new DataElement();
		    	while (st.hasMoreTokens())
				{
		    		dataElement.setTable(st.nextToken());
		    		String field = st.nextToken();
		    		Logger.out.debug(st.nextToken());
		    		String tableInPath = null;
					if (st.hasMoreTokens())
					{
					    tableInPath = st.nextToken();
					    field = field+"."+tableInPath;
					    Logger.out.debug("Field with the table id......."+field);
					}
					dataElement.setField(field);
		    	}
		        vector.add(dataElement);
		    }
		    
			return vector;
		}
	 
	 private List getColumnDisplayNames(String []selectedColumnsList) 
		{
	    	/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames 
	    	 * and set the dataelement object.
	    	 */
		    List columnDisplayNames = new ArrayList();
		    for(int i=0;i<selectedColumnsList.length;i++)
		    {
		    	StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
		    	DataElement dataElement = new DataElement();
		    	while(st.hasMoreTokens())
				{
		    		st.nextToken();
		    		st.nextToken();
		    		String displayName = st.nextToken();
		    		columnDisplayNames.add(displayName);
		    		Logger.out.debug("columnDisplayNames"+displayName);
		    		if(st.hasMoreTokens())
		    			st.nextToken();
				}
		    }
			return columnDisplayNames;
		}
	 
	 //set the result view for the query. 
	 public Vector getSelectDataElements(String[] selectedColumns, Set tableSet, List columnNames) throws DAOException
	 {
	    Vector selectDataElements = null;
	     
		//If columns not conigured, set to default.
	 	if(selectedColumns==null)
	 	{
	 	    selectDataElements = getViewElements(tableSet, columnNames);
	 	}
	 	//else set to the configured columns.
		else
		{
			selectDataElements = getViewElements(selectedColumns);
			List columnNamesList = getColumnDisplayNames(selectedColumns);
			columnNames.addAll(columnNamesList);
		}
	 	
	 	// Getting the aliasNames of the table ids in the tables in path.
		Set forFromSet = configureSelectDataElements(selectDataElements);
		tableSet.addAll(forFromSet);
		
		return selectDataElements;
	 }
	 
	 /**
	  * Gets the fields from select clause of the query and returns 
	  * Set of objects of that attributes to be added in the from clause.  
	  * @param query The query object whose select fields are to be get.
	  * @return Set of objects of that attributes to be added in the from clause.
	  * @throws DAOException
	  */
	 private Set configureSelectDataElements(Vector selectDataElements) throws DAOException
	 {
	     Set forFromSet = new HashSet();
	     
	     Iterator iterator = selectDataElements.iterator();
	     QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
		 							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
	     while (iterator.hasNext())
	     {
	         DataElement dataElement = (DataElement) iterator.next();
	         String fieldName = dataElement.getField();
	         StringTokenizer stringToken = new StringTokenizer(dataElement.getField(), ".");
	         dataElement.setField(stringToken.nextToken());
	         forFromSet.add(dataElement.getTable());
	         
	         if (stringToken.hasMoreElements())
	         {
	             String tableInPath = stringToken.nextToken(); 
	             addTablesInPathToFromSet(forFromSet, tableInPath);
	         }
	     }
	     
	     return forFromSet;
	 }
	 
	 /**
	     * Returns the Vector of DataElement objects for the select clause of the query.
	     * And also list the column names in the columnList list.
	     * @param aliasNameSet The Set of the alias names for which the DataElements are to be created.
	     * @param columnList List of column names to be shown in the spreadsheet view.
	     * @return the Vector of DataElement objects for the select clause of the query.
	     * @throws DAOException
	     */
	    public Vector getViewElements(Set aliasNameSet, List columnList) throws DAOException
		{
		    Vector vector = new Vector();
		    
		    try
		    {
			    JDBCDAO jdbcDao = new JDBCDAO();
		        jdbcDao.openSession(null);
		        
		        Iterator aliasNameIterator = aliasNameSet.iterator();
		        while (aliasNameIterator.hasNext())
		        {
		            String aliasName = (String) aliasNameIterator.next();
		            
		            String sql =" SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.TABLES_IN_PATH, temp.DISPLAY_NAME " +
						        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 join " +
						        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, displayData.DISPLAY_NAME, relationData.TABLES_IN_PATH " +
						        " FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
						        " CATISSUE_TABLE_RELATION relationData, " +
						        " CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData, " +
						        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
						        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
						        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
						        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
						        " columnData.IDENTIFIER = displayData.COL_ID and " +
						        " tableData.ALIAS_NAME = '"+aliasName+"') as temp " +
						        " on temp.TABLE_ID = tableData2.TABLE_ID";
		            
				    Logger.out.debug("DATA ELEMENT SQL : "+sql);
				    
				    List list = jdbcDao.executeQuery(sql, null, false, null);
				    
				    Logger.out.debug("list.size()************************"+list.size());
				    String [] columnNames = new String[list.size()];
				    Iterator iterator = list.iterator();
				    int i = 0;
				    while(iterator.hasNext())
				    {
				        List rowList = (List) iterator.next();
				        DataElement dataElement = new DataElement();
				        dataElement.setTable((String)rowList.get(0));
				        dataElement.setField((String)rowList.get(1)+"."+(String)rowList.get(2));
				        vector.add(dataElement);
				        columnList.add((String)rowList.get(3));
				    }
		        }
			    
			    jdbcDao.closeSession();
		    }
		    catch(ClassNotFoundException classExp)
		    {
		        throw new DAOException(classExp.getMessage(),classExp);
		    }
		    
		    return vector;
		}
	    
	    

		/**
		 * @param fromAliasNameValue
		 * @return
		 * @throws DAOException
		 */
		public QueryResultObjectData createQueryResultObjectData(String fromAliasNameValue) throws DAOException {
			QueryResultObjectData queryResultObjectData;
			queryResultObjectData = new QueryResultObjectData();
			queryResultObjectData.setAliasName(fromAliasNameValue);
			//Aarti: getting related tables that should be dependent 
			//on main object for authorization
			Vector relatedTables = new Vector();
			relatedTables = QueryBizLogic
					.getRelatedTableAliases(fromAliasNameValue);
//					Aarti: Get main query objects which should have individual checks
			//for authorization and should not be dependent on others
			Vector mainQueryObjects = QueryBizLogic.getMainObjectsOfQuery();
			
			String queryObject;
			//Aarti: remove independent query objects from related objects
			//vector and add them to tableSet so that their authorization
			//is checked individually
			for (int i = 0; i < mainQueryObjects.size(); i++) {
				queryObject = (String) mainQueryObjects.get(i);
				if (relatedTables.contains(queryObject)) {
					relatedTables.remove(queryObject);
//							tableSet.add(queryObject);
					if(!queryObject.equals(fromAliasNameValue))
					{
						queryResultObjectData.addRelatedQueryResultObject(new QueryResultObjectData(queryObject));
					}
				}
			}
			
			//Aarti: Map all related tables to the main table
//					relatedTablesMap.put(fromAliasNameValue, relatedTables);
			queryResultObjectData.setDependentObjectAliases(relatedTables);
			return queryResultObjectData;
		}

		/**
		 * @param queryResultObjectDataMap
		 * @param query
		 */
		public List addObjectIdentifierColumnsToQuery(Map queryResultObjectDataMap, Query query) {
			DataElement identifierDataElement;
			
			List columnNames = new ArrayList();
			Set keySet = queryResultObjectDataMap.keySet();
			Iterator keyIterator = keySet.iterator();
			QueryResultObjectData queryResultObjectData2;
			QueryResultObjectData queryResultObjectData3;
			Vector queryObjects;
			Vector queryObjectNames;
			int initialColumnNumbers = query.getResultView().size();
			Map columnIdsMap;
			
			int columnId =0;
			
			for(int i=0;keyIterator.hasNext();i++)
			{
				queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap.get(keyIterator.next());
				queryObjects = queryResultObjectData2.getIndependentQueryObjects();
				queryObjectNames = queryResultObjectData2.getIndependentObjectAliases();
				for(int j = 0 ; j<queryObjects.size();j++)
				{
					columnIdsMap = query.getIdentifierColumnIds(queryObjectNames);
					queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
					identifierDataElement = new DataElement(queryResultObjectData3.getAliasName(),
						Constants.IDENTIFIER);
//					query.addElementToView(columnId, identifierDataElement);
//					queryResultObjectData3.setIdentifierColumnId(columnId++);
					queryResultObjectData3.setIdentifierColumnId(((Integer)columnIdsMap.get(queryResultObjectData3.getAliasName())).intValue()-1);
//					columnNames.add(queryResultObjectData3.getAliasName() + " ID");
				}
				
			}
			int columnsAdded = query.getResultView().size() - initialColumnNumbers;
			for(int i=0;i<columnsAdded;i++)
			{
				columnNames.add(" ID");
			}
			return columnNames;
		}

		/**
		 * @param queryResultObjectDataMap
		 * @param query
		 */
		public void setDependentIdentifiedColumnIds(Map queryResultObjectDataMap, Query query) {
			Iterator keyIterator;
			QueryResultObjectData queryResultObjectData2;
			QueryResultObjectData queryResultObjectData3;
			Vector queryObjects;
			Set keySet2 = queryResultObjectDataMap.keySet();
			keyIterator = keySet2.iterator();
			for(int i=0;keyIterator.hasNext();i++)
			{
				queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap.get(keyIterator.next());
				queryObjects = queryResultObjectData2.getIndependentQueryObjects();
				for(int j = 0 ; j<queryObjects.size();j++)
				{
					queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
					queryResultObjectData3.setDependentColumnIds(query.getColumnIds(queryResultObjectData3.getAliasName(),queryResultObjectData3.getDependentObjectAliases()));
					queryResultObjectData3.setIdentifiedDataColumnIds(query.getIdentifiedColumnIds(queryResultObjectData3.getAliasName(),queryResultObjectData3.getDependentObjectAliases()));
					Logger.out.debug(" table:"+queryResultObjectData3.getAliasName()+" columnIds:"+queryResultObjectData3.getDependentColumnIds());
				}
				
			}
		}

		/**
		 * @param fromTables
		 * @param queryResultObjectDataMap
		 * @param query
		 */
		public void createQueryResultObjectData(Set fromTables, Map queryResultObjectDataMap, Query query) throws DAOException{
			Iterator iterator = fromTables.iterator();
			String tableAlias;
			QueryResultObjectData queryResultObjectData;
	        while (iterator.hasNext())
	        {
	        	tableAlias = (String) iterator.next();
	        	queryResultObjectData = createQueryResultObjectData(tableAlias);
	        	if(query.getColumnIds(tableAlias,queryResultObjectData.getDependentObjectAliases()).size()!=0)
	        	{
	        		queryResultObjectDataMap.put(tableAlias,queryResultObjectData);
	        	}
	        	
	        	
	        }
		}
	    

}
