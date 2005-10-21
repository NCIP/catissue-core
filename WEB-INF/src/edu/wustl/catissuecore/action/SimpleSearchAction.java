/**
 * Created on Aug 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
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
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;

		//Get the aliasName.
		String viewAliasName = (String) simpleQueryInterfaceForm
				.getValue("SimpleConditionsNode:1_Condition_DataElement_table");

		String target = Constants.SUCCESS;
		
		try
		{
			Map map = simpleQueryInterfaceForm.getValuesMap();

			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");

			Collection simpleConditionNodeCollection = parser.generateData(map, true);
			Iterator iterator = simpleConditionNodeCollection.iterator();

			Set fromTables = new HashSet();
			
			//Aarti: identifiers of the objects that need to be checked for object level privileges
			String[][] objectIdentifiers;

			SimpleConditionsNode simpleConditionsNode = null;
			while (iterator.hasNext())
			{
				simpleConditionsNode = (SimpleConditionsNode) iterator.next();
				String columnName = simpleConditionsNode.getCondition().getDataElement().getField();
				StringTokenizer stringToken = new StringTokenizer(columnName, ".");
				simpleConditionsNode.getCondition().getDataElement().setTable(
						stringToken.nextToken());
				simpleConditionsNode.getCondition().getDataElement().setField(
						stringToken.nextToken());
				String fieldType = stringToken.nextToken();
				String value = simpleConditionsNode.getCondition().getValue();

				if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR)
						|| fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
				{
					if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR))
					{
						value = "'" + value + "'";
					}
					else
					{
						value = "STR_TO_DATE('" + value + "','" + Constants.MYSQL_DATE_PATTERN
								+ "')";
					}
					simpleConditionsNode.getCondition().setValue(value);
				}

				if (simpleQueryInterfaceForm.getPageOf().equals(
						Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
				{
					//Prepare a Set of table names.
					fromTables.add(simpleConditionsNode.getCondition().getDataElement().getTable());
				}
			}

			String fullyQualifiedClassName = "edu.wustl.catissuecore.domain."+viewAliasName;
//            List activityStatusConditionList = getActivityStatusCondition(fullyQualifiedClassName);
			SimpleConditionsNode activityStatusCondition = getActivityStatusCondition(fullyQualifiedClassName); 
//            if(activityStatusConditionList.isEmpty() == false)
			if(activityStatusCondition != null)
            {
                simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
//                simpleConditionNodeCollection.addAll(activityStatusConditionList);
                simpleConditionNodeCollection.add(activityStatusCondition);
            }
            
            //            Iterator iterator1 = fromTables.iterator();
			//            while (iterator1.hasNext())
			//            {
			//                Logger.out.debug("From tABLES............................."+iterator1.next());
			//            }
			Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
			((SimpleQuery) query).addConditions(simpleConditionNodeCollection);
			List list = null;
			
			//Get the view columns.
			String[] columnNames = null;
			
			if (simpleQueryInterfaceForm.getPageOf()
					.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
			{
				query.setTableSet(fromTables);
				Logger.out.debug("setTableSet.......................................");
				
				String[] tempColumnNames= query.setViewElements(viewAliasName);
				Set tableSet = query.getTableSet();
				columnNames = new String[tempColumnNames.length+tableSet.size()];
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
					columnNames[i] = tableName+" ID";
					Logger.out.debug("Identifier field added: ********* "+identifierDataElement.getTable()+" "+identifierDataElement.getField());
				}
				for(int i=0, j= tableSet.size(); i<tempColumnNames.length;i++,j++)
				{
					columnNames[j] = tempColumnNames[i];
				}
				
				//Setting column ids for the corresponding table Aliases
				fromTablesIterator = tableSet.iterator();
				Map columnIdsMap = new HashMap();
				for(int i =0; i<tableSet.size(); i++)
				{
					tableName = (String) fromTablesIterator.next();
					columnIdsMap.put(tableName,query.getColumnIds(tableName));
				}
				
				list = query.execute(getSessionData(request),Constants.OBJECT_LEVEL_SECURE_RETRIEVE,objectIdentifiers,columnIdsMap);
			}
			else
			{
				columnNames = query.setViewElements(viewAliasName);
				 list = query.execute(getSessionData(request),Constants.INSECURE_RETRIEVE,null,null);
			}
			
			if (list.isEmpty())
			{
				ActionErrors errors = new ActionErrors();
				errors
						.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"simpleQuery.noRecordsFound"));
				saveErrors(request, errors);

				String path = "SimpleQueryInterface.do?pageOf="
						+ simpleQueryInterfaceForm.getPageOf() + "&aliasName="
						+ simpleQueryInterfaceForm.getAliasName();
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
	 * Returns true if the object named aliasName contains the activityStatus 
	 * data member, else returns false.
	 * @param aliasName
	 * @return
	 */
	//TODO To be fix
	private boolean hasActivityStatus(String aliasName)
	{
		try
		{
			Class className = Class.forName("edu.wustl.catissuecore.domain." + aliasName);

			if (className.equals(CollectionProtocol.class)
					|| className.equals(DistributionProtocol.class)
					|| className.equals(Specimen.class))
				return true;

			Logger.out.debug("Class.................." + className.getName());
			Field[] objectFields = className.getDeclaredFields();
			Logger.out.debug("Field Size..........................." + objectFields.length);
			for (int i = 0; i < objectFields.length; i++)
			{
				Logger.out.debug("objectFields[i].getName().............................."
						+ objectFields[i].getName());
				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
				{
					return true;
				}
			}
		}
		catch (ClassNotFoundException classNotExcp)
		{
			Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
		}

		return false;
	}

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

		try
		{
			Class className = Class.forName(fullyQualifiedClassName);

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
		catch (ClassNotFoundException classNotExcp)
		{
			Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
		}
		return activityStatusCondition;
	}

}