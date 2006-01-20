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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
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
    public List handleStringAndDateConditions(Collection simpleConditionNodeCollection, List fromTables) throws DAOException
    {
        //Adding single quotes to strings and date values.
        Iterator iterator = simpleConditionNodeCollection.iterator();
        while (iterator.hasNext())
        {
            SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) iterator.next();
            
        	// Add all the objects selected in UI to the fromtables Set. 
        	addInListIfNotPresent(fromTables, simpleConditionsNode.getCondition().getDataElement().getTableAliasName());
        	
        	// Adds single quotes to the value of attributes whose type is string or date.
            String tableInPath = addSingleQuotes(simpleConditionsNode);
        	
        	//Get the tables in path for this field and add it in the fromTables Set. 
        	if (tableInPath != null)
        	{
        	    addTablesInPathToFromSet(fromTables, tableInPath);
        	}
        	
        	addInListIfNotPresent(fromTables, simpleConditionsNode.getCondition().getDataElement().getTableAliasName());
        }
        return fromTables;
    }

    /**
     * Gets the alias names of the tables in path and adds them in the fromTables Set passed.
     * @param fromTables The Set to which the alias names are to be added.
     * @param tableInPath The ids of tables in path separated by :
     * @throws DAOException
     */
    private void addTablesInPathToFromSet(List fromTables, String tableInPath) throws DAOException
    {
        StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
        while (tableInPathTokenizer.hasMoreTokens())
        {
            Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
            QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
            							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
            String aliasName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableId);
            
            if (aliasName != null)
        	{
                addInListIfNotPresent(fromTables, aliasName);
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
        Logger.out.debug(" columnName:"+columnName);
        StringTokenizer stringToken = new StringTokenizer(columnName, ".");
        simpleConditionsNode.getCondition().getDataElement().setTable(stringToken.nextToken());
        Logger.out.debug("tablename:"+simpleConditionsNode.getCondition().getDataElement().getTableAliasName());
        simpleConditionsNode.getCondition().getDataElement().setField(stringToken.nextToken());
        simpleConditionsNode.getCondition().getDataElement().setFieldType(stringToken.nextToken());
        String tableInPath = null;
        
        if (stringToken.hasMoreTokens())
        {
            tableInPath = stringToken.nextToken();
        }
        
        Logger.out.debug("^^^^^^^^^^^Condition:"+simpleConditionsNode.getCondition());
        
        return tableInPath;
    }

    /**
	 * Adds the activity status conditions for all the objects in the from clause.
     * @param simpleConditionNodeCollection The SimpleConditionsNode Collection.
     * @param fromTables Set of tables in the from clause of the query.
     * @param simpleConditionsNode The last condition in the simpleConditionNode's Collection.
     */
    public void addActivityStatusConditions(Collection simpleConditionNodeCollection, Set fromTables) throws DAOException,ClassNotFoundException
    {
        // Creating aliasName set with full package names.
        // Required for checking the activityStatus.
        Set fromTablesWithPackageNames = new HashSet();
        Iterator fromTableSetIterator = fromTables.iterator();
        while (fromTableSetIterator.hasNext())
        {
            String tableName = getClassName((String)fromTableSetIterator.next());
            fromTablesWithPackageNames.add(tableName);
        }
        // Check and get the activity status conditions for all the objects in the conditions.
        List activityStatusConditionList = new ArrayList();
        Iterator aliasNameIterator = fromTablesWithPackageNames.iterator();
        while (aliasNameIterator.hasNext())
        {
            String fullyQualifiedClassName = (String) aliasNameIterator.next();
            if (fullyQualifiedClassName.equals("edu.wustl.catissuecore.domain.ReportedProblem"))
                continue;
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
	private SimpleConditionsNode getActivityStatusCondition(String fullyQualifiedClassName) throws DAOException,ClassNotFoundException
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
					activityStatusCondition.getCondition().getDataElement().setTableName(
							getAliasName(HibernateMetaData.getTableName(className)));
					activityStatusCondition.getCondition().getDataElement().setField(
							Constants.ACTIVITY_STATUS_COLUMN);
					activityStatusCondition.getCondition().getOperator().setOperator("!=");
					activityStatusCondition.getCondition().setValue(Constants.ACTIVITY_STATUS_DISABLED);
					activityStatusCondition.getCondition().getDataElement().setFieldType(Constants.FIELD_TYPE_VARCHAR);
				}
			}
			
			if ((activityStatusCondition == null) &&
					(className.getSuperclass().getName().equals(
							"edu.wustl.common.domain.AbstractDomainObject") == false))
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
		    		dataElement.setTableName(st.nextToken());
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
	 public Vector getSelectDataElements(String[] selectedColumns, List tableSet, List columnNames) throws DAOException
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
		List forFromSet = configureSelectDataElements(selectDataElements);
		
		forFromSet.removeAll(tableSet);
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
	 private List configureSelectDataElements(Vector selectDataElements) throws DAOException
	 {
	     List forFromSet = new ArrayList();
	     
	     Iterator iterator = selectDataElements.iterator();
	     QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
		 							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
	     while (iterator.hasNext())
	     {
	         DataElement dataElement = (DataElement) iterator.next();
	         String fieldName = dataElement.getField();
	         StringTokenizer stringToken = new StringTokenizer(dataElement.getField(), ".");
	         dataElement.setField(stringToken.nextToken());
	         
	         addInListIfNotPresent(forFromSet, dataElement.getTableAliasName());
	         
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
	    public Vector getViewElements(List aliasNameSet, List columnList) throws DAOException
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
		            Logger.out.debug("Alias Name : ............."+aliasName);

		            String sql= " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.TABLES_IN_PATH, " +
			        			" temp.DISPLAY_NAME, temp.ATTRIBUTE_TYPE " +
						        " from CATISSUE_QUERY_TABLE_DATA tableData2 join " +
						        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, displayData.DISPLAY_NAME, " +
						        " relationData.TABLES_IN_PATH, columnData.ATTRIBUTE_TYPE " +
						        " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, " +
						        " CATISSUE_TABLE_RELATION relationData, " +
						        " CATISSUE_QUERY_TABLE_DATA tableData, " +
						        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
						        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
						        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
						        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
						        " columnData.IDENTIFIER = displayData.COL_ID and " +
						        " tableData.ALIAS_NAME = '"+aliasName+"') temp " +
						        " on temp.TABLE_ID = tableData2.TABLE_ID";
				    Logger.out.debug("DATA ELEMENT SQL : "+sql);
				    
				    List list = jdbcDao.executeQuery(sql, null, false, null);
			        String nameSql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"+aliasName+"'";
			        List nameList = jdbcDao.executeQuery(nameSql,null,false, null);
			        String tableDisplayName=new String();
			        if (!nameList.isEmpty())
			        {
			            List rowNameList = (List)nameList.get(0);
			            tableDisplayName = (String)rowNameList.get(0);
			        }
			        Logger.out.debug("tableDisplayName in getviewelements:"+tableDisplayName);
				    Logger.out.debug("list.size()************************"+list.size());
				    String [] columnNames = new String[list.size()];
				    Iterator iterator = list.iterator();
				    int i = 0;
				    while(iterator.hasNext())
				    {
				        List rowList = (List) iterator.next();
				        DataElement dataElement = new DataElement();
				        dataElement.setTableName((String)rowList.get(0));
				        String fieldName = (String)rowList.get(1);
				        
				        Logger.out.debug("fieldName*********************"+fieldName);    
				        dataElement.setField(fieldName+"."+(String)rowList.get(2));
				        dataElement.setFieldType((String)rowList.get(4));
				        vector.add(dataElement);
				        columnList.add((String)rowList.get(3)+" : "+tableDisplayName);
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
		 * @param fromTables
		 * @return
		 * @throws DAOException
		 */
		public QueryResultObjectData createQueryResultObjectData(String fromAliasNameValue, Set fromTables) throws DAOException 
		{
			QueryResultObjectData queryResultObjectData;
			queryResultObjectData = new QueryResultObjectData();
			queryResultObjectData.setAliasName(fromAliasNameValue);
			//Aarti: getting related tables that should be dependent 
			//on main object for authorization
			Vector relatedTables = new Vector();
			relatedTables = QueryBizLogic
					.getRelatedTableAliases(fromAliasNameValue);
			//remove all the related objects that are not part of the current query
			for(int i=0; i<relatedTables.size();i++)
			{
				if(!fromTables.contains(relatedTables.get(i)))
				{
					relatedTables.remove(i--);
				}
			}
			
			Logger.out.debug("After removing tables not in query relatedTable:"+relatedTables);
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
			
			for(int i=0;keyIterator.hasNext();i++)
			{
				queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap.get(keyIterator.next());
				queryObjects = queryResultObjectData2.getIndependentQueryObjects();
				queryObjectNames = queryResultObjectData2.getIndependentObjectAliases();
				for(int j = 0 ; j<queryObjects.size();j++)
				{
					columnIdsMap = query.getIdentifierColumnIds(queryObjectNames);
					queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
					queryResultObjectData3.setIdentifierColumnId(((Integer)columnIdsMap.get(queryResultObjectData3.getAliasName())).intValue()-1);
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
			Vector mainQueryObjects = QueryBizLogic.getMainObjectsOfQuery();
			Logger.out.debug(" tables in query:"+fromTables);
	        while (iterator.hasNext())
	        {
	        	tableAlias = (String) iterator.next();
	        	Logger.out.debug("*********** table obtained from fromTables set:"+tableAlias);
	        	if(mainQueryObjects.contains(tableAlias))
	        	{
		        	queryResultObjectData = createQueryResultObjectData(tableAlias,fromTables);
		        	if(query.getColumnIds(tableAlias,queryResultObjectData.getDependentObjectAliases()).size()!=0)
		        	{
		        		queryResultObjectDataMap.put(tableAlias,queryResultObjectData);
		        	}
	        	}
	        }
		}
		
		private void addInListIfNotPresent(List list, String string)
		{
		    if (list.contains(string) == false)
		    {
		        list.add(string);
		    }
		}
		private String getClassName(String aliasName) throws DAOException,ClassNotFoundException
		{
			String tableName = getTableName(aliasName);
			String className = HibernateMetaData.getClassName(tableName);
			return className;
		}
		public String getTableName(String aliasName) throws DAOException,ClassNotFoundException
		{
			String tableName = new String();
	        JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	        jdbcDAO.openSession(null);
	        String sql = "select TABLE_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"+aliasName+"'";
	        List list = jdbcDAO.executeQuery(sql,null,false, null);
	        jdbcDAO.closeSession();
	        
	        if (!list.isEmpty())
	        {
	            List rowList = (List)list.get(0);
	            tableName = (String)rowList.get(0);
	        }
			return tableName;
		}
		public String getAliasName(String tableName) throws DAOException,ClassNotFoundException
		{
			String aliasName = new String();
	        JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	        jdbcDAO.openSession(null);
	        String sql = "select ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"+tableName+"'";
	        List list = jdbcDAO.executeQuery(sql,null,false, null);
	        jdbcDAO.closeSession();
	        
	        if (!list.isEmpty())
	        {
	            List rowList = (List)list.get(0);
	            aliasName = (String)rowList.get(0);
	        }
			return aliasName;
		}
}
