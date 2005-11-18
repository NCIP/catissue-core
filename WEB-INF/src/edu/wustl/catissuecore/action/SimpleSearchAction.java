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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.bizlogic.SimpleQueryBizLogic;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
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
		
		Map map= (Map)session.getAttribute(Constants.SIMPLE_QUERY_MAP);
	    
		//If map from session is null get the map values from form.
		if(map==null)
		{
			map = simpleQueryInterfaceForm.getValuesMap();
			session.setAttribute(Constants.SIMPLE_QUERY_MAP,map);
		}
		
		MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
		Collection simpleConditionNodeCollection = parser.generateData(map, true);
		
		SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();  
		
		// Get the alias name of the first object in the condition.  
		String viewAliasName = (String)map.get("SimpleConditionsNode:1_Condition_DataElement_table");
		
		// Instantiating the query object. 
		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
		
		// Puts the single quotes for attributes of type string and date and 
		// returns the Set of objects to which the attributes belong.
		Set fromTables = new HashSet();
		simpleQueryBizLogic.handleStringAndDateConditions(simpleConditionNodeCollection, fromTables);
		
		// Get the configured result view columns else is null.
		String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
		
		// Set the result view for the query.
		List columnNames = new ArrayList();
		Vector selectDataElements = simpleQueryBizLogic
											.getSelectDataElements(selectedColumns, fromTables, columnNames);
		query.setResultView(selectDataElements);
		
		// Set the from tables in the query.
		query.setTableSet(fromTables);
		
		// Checks and gets the activity status conditions for all the objects in the from clause
		// and adds it in the simple conditions node collection.
		simpleQueryBizLogic.addActivityStatusConditions(simpleConditionNodeCollection, fromTables);
		
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
				identifierDataElement = new DataElement(tableName,Constants.SYSTEM_IDENTIFIER_COLUMN_NAME);
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
		
		// If the result contains no data, show error message.
		if (list.isEmpty())
		{
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"simpleQuery.noRecordsFound"));
			saveErrors(request, errors);
			
			String alias = (String)session.getAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME);
			if(alias==null)
				alias = simpleQueryInterfaceForm.getAliasName();
			simpleQueryInterfaceForm.setValues(map);
			
			return getActionForward(Constants.SIMPLE_QUERY_NO_RESULTS, "/SimpleQueryInterface.do?pageOf="
					+ simpleQueryInterfaceForm.getPageOf() + "&aliasName=" + alias);
		}
		else
		{
		    // If the result contains only one row and the page is of edit  
		    // then show the result in the edit page.
			if ((list.size() == 1) 
			        && (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE.equals(simpleQueryInterfaceForm.getPageOf()) == false))
			{
				List rowList = (List) list.get(0);
				
				return getActionForward(Constants.SIMPLE_QUERY_SINGLE_RESULT, "/SearchObject.do?pageOf="
						+ simpleQueryInterfaceForm.getPageOf()
						+ "&operation=search&systemIdentifier=" + rowList.get(0));
			}
			else
			{
			    Logger.out.debug("columnNames..........................."+columnNames.size());
			    Logger.out.debug("list..........................."+list.size());
			    // If results contain more than one result, show the spreadsheet view.  
				request.setAttribute(Constants.PAGEOF, simpleQueryInterfaceForm.getPageOf());
				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			}
		}

		return mapping.findForward(target);
	}
	
	private ActionForward getActionForward(String name, String path)
	{
	    ActionForward actionForward = new ActionForward();
	    actionForward.setName(name);
		actionForward.setPath(path);
		
		return actionForward; 
	}
}