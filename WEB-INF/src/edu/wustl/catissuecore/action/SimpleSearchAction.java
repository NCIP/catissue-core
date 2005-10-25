/**
 * Created on Aug 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * @author gautam_shetty
 */
public class SimpleSearchAction extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Logger.out.debug("SimpleSearchAction");
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
		String counter = simpleQueryInterfaceForm.getCounter();
		HttpSession session = request.getSession();
		if(counter==null)
			counter = (String)session.getAttribute(Constants.SIMPLE_QUERY_COUNTER);
		//Set form attributes in session for configuration after Search
		else
			session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,simpleQueryInterfaceForm.getCounter());
		Map map=null;
		
		//	Get the aliasName.
		String viewAliasName = (String) simpleQueryInterfaceForm.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
		
		if(viewAliasName==null)
		{
			map = (Map)session.getAttribute(Constants.SIMPLE_QUERY_MAP);
			viewAliasName = (String)map.get("SimpleConditionsNode:1_Condition_DataElement_table");
		}
		else
			session.setAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME,viewAliasName);

		Logger.out.debug("viewAliasName"+viewAliasName);
		String target = Constants.SUCCESS;
		
		try
		{
			//If map from session is null get the map values from form
			if(map==null)
			{
				map = simpleQueryInterfaceForm.getValuesMap();
				session.setAttribute(Constants.SIMPLE_QUERY_MAP,map);
			}
			Logger.out.debug("map size"+map.size());
			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
			
			Collection simpleConditionNodeCollection = parser.generateData(map, true);
			Iterator iterator = simpleConditionNodeCollection.iterator();
			
			Set fromTables = new HashSet();
			Set aliasNameSet = new HashSet(); 
			
			//Aarti: identifiers of the objects that need to be checked for object level privileges.
			String[][] objectIdentifiers;
			
			//Adding single quotes to strings and date values.
			SimpleConditionsNode simpleConditionsNode = null;
			while (iterator.hasNext())
			{
				simpleConditionsNode = (SimpleConditionsNode) iterator.next();
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
				
				//Get the tables in path for this field and add it in the fromTables Set. 
				if (tableInPath != null)
				{
				    StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
				    String aliasName = null;
					while (tableInPathTokenizer.hasMoreTokens())
					{
					    Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
					    QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
					    							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
					    aliasName = bizLogic.getAliasNameFromTableId(tableId);
					    Logger.out.debug("aliasName for from Set**************************"+aliasName);
					}
					
					if (aliasName != null)
					{
					    fromTables.add(aliasName);
					}
				}
				
				//Creating aliasName set with full package names.
				//Required for checking the activityStatus.
				aliasNameSet.add("edu.wustl.catissuecore.domain."
				        		+ simpleConditionsNode.getCondition().getDataElement().getTable());
			}
			
			//Add all the objects selected in UI to the fromtables Set. 
			for (int i=1;i<=Integer.parseInt(counter);i++)
			{
			    String fromAliasNameKey = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
			    String fromAliasNameValue = (String)map.get(fromAliasNameKey);
			    
			    //Prepare a Set of table names.
				fromTables.add(fromAliasNameValue);
			}
			
			//Activity Status condition nodes.
			List activityStatusConditionList = new ArrayList();
			Iterator aliasNameIterator = aliasNameSet.iterator();
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
		    
            if(activityStatusConditionList.isEmpty() == false)
            {
                simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
                simpleConditionNodeCollection.addAll(activityStatusConditionList);
            }
            
            // Forming the query.
			Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
			
			// Adding the condition objects from user.
			((SimpleQuery) query).addConditions(simpleConditionNodeCollection);
			List list = null;
			
			// Get the view columns.
			List columnNames = null;
			
						Iterator iterator1 = fromTables.iterator();
			            while (iterator1.hasNext())
			            {
			                Logger.out.debug("From tABLES before............................."+iterator1.next());
			            }
			
			if (simpleQueryInterfaceForm.getPageOf()
					.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
			{
				String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
				
				//Set the result view for the query
				List tempColumnNames = getColumnNames(selectedColumns,query,fromTables);

				//Getting the DataElement for select columns of the query.
				Vector selectDataElements = query.getResultView();
				
				//Getting the aliasNames of the table ids in the tables in path.
				Set forFromSet = configureSelectDataElements(selectDataElements, query);
				fromTables.addAll(forFromSet);
				
//				iterator1 = fromTables.iterator();
//	            while (iterator1.hasNext())
//	            {
//	                Logger.out.debug("From tABLES. after............................"+iterator1.next());
//	            }
				
				// Set the from tables in the query.
				query.setTableSet(fromTables);
				
				Logger.out.debug("Show tables Set..............");
				query.showTableSet();
				
				Set tableSet;
				if(Constants.switchSecurity)
				{
					tableSet = query.getTableSet();
				}
				else
				{
					tableSet = new HashSet();
				}
				
				columnNames = new ArrayList();
				objectIdentifiers = new String[tableSet.size()][2];
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
				
				for(int i=0, j= tableSet.size(); i<tempColumnNames.size();i++,j++)
				{
					columnNames.add((String)tempColumnNames.get(i));
				}
				
				//Setting column ids for the corresponding table Aliases.
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
				columnNames = query.setViewElements(fromTables);
				
				//Getting the DataElement for select columns of the query.
				Vector selectDataElements = query.getResultView();
				
				//Getting the aliasNames of the table ids in the tables in path.
				Set forFromSet = configureSelectDataElements(selectDataElements, query);
				fromTables.addAll(forFromSet);
				
				//Set the from tables in the query.
				query.setTableSet(fromTables);
				
//				Logger.out.debug("Show tables Set..............");
//				query.showTableSet();
//				
//				iterator1 = fromTables.iterator();
//	            while (iterator1.hasNext())
//	            {
//	                Logger.out.debug("From tABLES. after............................"+iterator1.next());
//	            }
				
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
						+ simpleQueryInterfaceForm.getPageOf() + "&aliasName="
						+ alias;
				simpleQueryInterfaceForm.setValues(map);
				
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

//	/**
//	 * Returns true if the object named aliasName contains the activityStatus 
//	 * data member, else returns false.
//	 * @param aliasName
//	 * @return
//	 */
//	private boolean hasActivityStatus(String aliasName)
//	{
//		try
//		{
//			Class className = Class.forName("edu.wustl.catissuecore.domain." + aliasName);
//
//			if (className.equals(CollectionProtocol.class)
//					|| className.equals(DistributionProtocol.class)
//					|| className.equals(Specimen.class))
//				return true;
//
//			Logger.out.debug("Class.................." + className.getName());
//			Field[] objectFields = className.getDeclaredFields();
//			Logger.out.debug("Field Size..........................." + objectFields.length);
//			for (int i = 0; i < objectFields.length; i++)
//			{
//				Logger.out.debug("objectFields[i].getName().............................."
//						+ objectFields[i].getName());
//				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
//				{
//					return true;
//				}
//			}
//		}
//		catch (ClassNotFoundException classNotExcp)
//		{
//			Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
//		}
//
//		return false;
//	}

//	/**
//	 * Returns SimpleConditionsNode if the object named aliasName contains the activityStatus 
//	 * data member, else returns false.
//	 * @param aliasName
//	 * @return
//	 */
//	private List getActivityStatusCondition(String fullyQualifiedClassName)
//	{
//		SimpleConditionsNode activityStatusCondition = null;
//		List activityStatusList = new ArrayList();
//		
//		try
//		{
//			Class className = Class.forName(fullyQualifiedClassName);
//			Field[] objectFields = className.getDeclaredFields();
//
//			for (int i = 0; i < objectFields.length; i++)
//			{
//				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
//				{
//
//					activityStatusCondition = new SimpleConditionsNode();
//					activityStatusCondition.getCondition().getDataElement().setTable(Utility.parseClassName(fullyQualifiedClassName));
//					activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
//					activityStatusCondition.getCondition().getOperator().setOperator("=");
//					activityStatusCondition.getCondition().setValue("'" + Constants.ACTIVITY_STATUS_ACTIVE + "'");
//					activityStatusCondition.getOperator().setOperator(Constants.OR_JOIN_CONDITION);
//					
//					activityStatusList.add(activityStatusCondition);
//					
//					activityStatusCondition = new SimpleConditionsNode();
//					activityStatusCondition.getCondition().getDataElement().setTable(Utility.parseClassName(fullyQualifiedClassName));
//					activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
//					activityStatusCondition.getCondition().getOperator().setOperator("=");
//					activityStatusCondition.getCondition().setValue("'" + Constants.ACTIVITY_STATUS_CLOSED + "'");
//
//					activityStatusList.add(activityStatusCondition);
//				}
//			}
//
//			if ((activityStatusCondition == null)&&
//					(className.getSuperclass().getName().equals(
//							"edu.wustl.catissuecore.domain.AbstractDomainObject") == false))
//			{
//			    activityStatusList = getActivityStatusCondition(className.getSuperclass()
//						.getName());
//			}
//		}
//
//		catch (ClassNotFoundException classNotExcp)
//		{
//			Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
//
//		}
//
//		return activityStatusList;
//
//	}
	
	/**
	 * Returns SimpleConditionsNode if the object named aliasName contains the activityStatus 
	 * data member, else returns false.
	 * @param aliasName
	 * @return
	 */
	private SimpleConditionsNode getActivityStatusCondition(String fullyQualifiedClassName)
	{
		SimpleConditionsNode activityStatusCondition = null;

		//Returns the Class objet if it is a valid class else returns null.
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
							"ACTIVITY_STATUS");
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
	 	//set the result view for the query 
		List tempColumnNames;
		
		//If columns not conigured, set to default
	 	if(selectedColumns==null)
			tempColumnNames	= query.setViewElements(tableSet);
	 	//else set to the configured columns
		else
		{
			Vector resultViewVector = setViewElements(selectedColumns); 
			query.setResultView(resultViewVector);
			tempColumnNames = getColumnDisplayNames(selectedColumns); 
		}
	 	return tempColumnNames;
	 }
	 
	 
	 private Set configureSelectDataElements(Vector selectDataElements, Query query) throws DAOException
	 {
	     Set forFromSet = new HashSet();
	     
	     Iterator iterator = selectDataElements.iterator();
	     QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
		 							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
	     while (iterator.hasNext())
	     {
	         DataElement dataElement = (DataElement) iterator.next();
	         String fieldName = dataElement.getField();
	         Logger.out.debug("Field with the table id in configureSelectDataElements......."+fieldName);
	         StringTokenizer stringToken = new StringTokenizer(dataElement.getField(), ".");
	         dataElement.setField(stringToken.nextToken());
//	         Logger.out.debug("..........................."+stringToken.nextToken());
	         
	         if (stringToken.hasMoreElements())
	         {
	             StringTokenizer pathIdToken = new StringTokenizer(stringToken.nextToken(), ":");
		         while (pathIdToken.hasMoreTokens())
		         {
		             Long tableId = Long.valueOf(pathIdToken.nextToken());
		             String aliasName = bizLogic.getAliasNameFromTableId(tableId);
		             Logger.out.debug("configureSelectDataElements..............aliasName.."+aliasName);
		             forFromSet.add(aliasName);
		         }
	         }
	     }
	     query.setResultView(selectDataElements);
	     
	     return forFromSet;
	 }
}