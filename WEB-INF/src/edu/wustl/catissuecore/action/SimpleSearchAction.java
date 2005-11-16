/**
 * <p>Title: SimpleSearchAction Class>
 * <p>Description:	SimpleSearchAction takes the conditions from the user, prepares, 
 * executes the query and shows the result in a spreadsheet view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * SimpleSearchAction takes the conditions from the user, prepares, 
 * executes the query and shows the result in a spreadsheet view.      
 * @author gautam_shetty
 */
public class SimpleSearchAction extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
		// -------- set the selected menu ------- start 
			String strMenu = simpleQueryInterfaceForm.getMenuSelected();
			request.setAttribute(Constants.MENU_SELECTED,strMenu  );
			Logger.out.debug(Constants.MENU_SELECTED+" set in SimpleSearch Action : -- "+ strMenu  ); 
		// -------- set the selected menu ------- end
		HttpSession session = request.getSession();

		String target = Constants.SUCCESS;
		try
		{
		    Map map= (Map)session.getAttribute(Constants.SIMPLE_QUERY_MAP);
		    
			//If map from session is null get the map values from form.
			if(map==null)
			{
				map = simpleQueryInterfaceForm.getValuesMap();
				session.setAttribute(Constants.SIMPLE_QUERY_MAP,map);
			}
			
			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
			Collection simpleConditionNodeCollection = parser.generateData(map, true);
			
			// Get the alias name of the first object in the condition.  
			String viewAliasName = (String)map.get("SimpleConditionsNode:1_Condition_DataElement_table");
			
			// Instantiating the query object. 
			Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
			
			// Puts the single quotes for attributes of type string and date and 
			// returns the Set of objects to which the attributes belong.
			Set fromTables = new HashSet();
			handleStringAndDateConditions(simpleConditionNodeCollection, fromTables);
			
			// Get the configured result view columns else is null. 
			String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
			
			// Set the result view for the query.
			List columnNames = getColumnNames(selectedColumns, query, fromTables);
			
			// Getting the aliasNames of the table ids in the tables in path.
			Set forFromSet = configureSelectDataElements(query);
			fromTables.addAll(forFromSet);
			
			// Set the from tables in the query.
			query.setTableSet(fromTables);
			
			// Checks and gets the activity status conditions for all the objects in the from clause
			// and adds it in the simple conditions node collection.
			addActivityStatusConditions(simpleConditionNodeCollection, fromTables);
			
            // Sets the condition objects from user in the query object.
			((SimpleQuery) query).addConditions(simpleConditionNodeCollection);
			
			// List of results the query will return on execution.
			List list = null;
			if (simpleQueryInterfaceForm.getPageOf()
					.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
			{
				Set tableSet;
				if(Constants.switchSecurity)
				{
					tableSet = query.getTableSet();
				}
				else
				{
					tableSet = new HashSet();
				}
				
				// Aarti: identifiers of the objects that need to be checked for object level privileges.
				String[][] objectIdentifiers = new String[tableSet.size()][2];
				Iterator fromTablesIterator = tableSet.iterator();
				DataElement identifierDataElement;
				String tableName;
				for(int i =0; i<tableSet.size(); i++)
				{
					tableName = (String) fromTablesIterator.next();
					objectIdentifiers[i][0] = tableName;
					objectIdentifiers[i][1] = String.valueOf(i);
					identifierDataElement = new DataElement(tableName,"IDENTIFIER");
					query.addElementToView(i,identifierDataElement);
					columnNames.add(tableName+" ID");
				}
				
				//Setting column ids for the corresponding table aliases.
				fromTablesIterator = tableSet.iterator();
				Map columnIdsMap = new HashMap();
				for(int i =0; i<tableSet.size(); i++)
				{
					tableName = (String) fromTablesIterator.next();
					columnIdsMap.put(tableName,query.getColumnIds(tableName));
				}
				
				list = query.execute(getSessionData(request),
				        Constants.OBJECT_LEVEL_SECURE_RETRIEVE,objectIdentifiers,columnIdsMap);
			}
			else
			{
				list = query.execute(getSessionData(request),Constants.INSECURE_RETRIEVE,null,null);
			}
			
			if (list.isEmpty())
			{
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"simpleQuery.noRecordsFound"));
				saveErrors(request, errors);
				String alias = (String)session.getAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME);
				if(alias==null)
					alias = simpleQueryInterfaceForm.getAliasName();
				String path = "SimpleQueryInterface.do?pageOf="
						+ simpleQueryInterfaceForm.getPageOf() + "&aliasName=" + alias;
				simpleQueryInterfaceForm.setValues(map);
				Logger.out.debug("path*************************"+path); 
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
				requestDispatcher.forward(request, response);
			}
			else
			{
				if ((list.size() == 1) 
				        && (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE.equals(simpleQueryInterfaceForm.getPageOf()) == false))
				{
					List rowList = (List) list.get(0);
					String action = "SearchObject.do?pageOf="
							+ simpleQueryInterfaceForm.getPageOf()
							+ "&operation=search&systemIdentifier=" + rowList.get(0);
					
					RequestDispatcher requestDispatcher = request.getRequestDispatcher(action);
					requestDispatcher.forward(request, response);
				}
				else
				{
					request.setAttribute(Constants.PAGEOF, simpleQueryInterfaceForm.getPageOf());
					request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
					request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
				}
			}
		}
		catch (DAOException daoExp)
		{
			Logger.out.debug(daoExp.getMessage(), daoExp);
			target = Constants.FAILURE;
		}

		return mapping.findForward(target);
	}

	/**
	 * Adds single quotes (') for string and date type attributes in the condition collecion 
	 * and the returns the Set of objects to which the condition attributes belong.
     * @param simpleConditionNodeCollection The condition collection.
     * @return the Set of objects to which the condition attributes belong.
     * @throws DAOException
     */
    private Set handleStringAndDateConditions(Collection simpleConditionNodeCollection, Set fromTables) throws DAOException
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
    private void addActivityStatusConditions(Collection simpleConditionNodeCollection, Set fromTables)
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
	
	 private Vector setViewElements(String []selectedColumnsList) 
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
	 
	 private List getColumnNames(String[] selectedColumns,Query query,Set tableSet) throws DAOException
	 {
	 	//set the result view for the query. 
		List tempColumnNames;
		
		//If columns not conigured, set to default.
	 	if(selectedColumns==null)
			tempColumnNames	= query.setViewElements(tableSet);
	 	//else set to the configured columns.
		else
		{
			Vector resultViewVector = setViewElements(selectedColumns); 
			query.setResultView(resultViewVector);
			tempColumnNames = getColumnDisplayNames(selectedColumns); 
		}
	 	return tempColumnNames;
	 }
	 
	 /**
	  * Gets the fields from select clause of the query and returns 
	  * Set of objects of that attributes to be added in the from clause.  
	  * @param query The query object whose select fields are to be get.
	  * @return Set of objects of that attributes to be added in the from clause.
	  * @throws DAOException
	  */
	 private Set configureSelectDataElements(Query query) throws DAOException
	 {
	     // Getting the DataElement for select columns of the query.
		 Vector selectDataElements = query.getResultView();
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
	     
	     query.setResultView(selectDataElements);
	     
	     return forFromSet;
	 }
	 
//	 private void displayConditions(Collection coll)
//	 {
//	     Iterator iterator = coll.iterator();
//	     
//	     while (iterator.hasNext())
//	     {
//	         SimpleConditionsNode s = (SimpleConditionsNode)iterator.next();
//	         Logger.out.debug("Next Operator*********************"+s.getOperator().getOperator());
//	     }
//	 }
}