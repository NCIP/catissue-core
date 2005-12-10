/*
 * Created on Nov 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
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
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AdvanceQueryBizlogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.bizlogic.SimpleQueryBizLogic;
import edu.wustl.catissuecore.query.AdvancedConditionsImpl;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.AdvancedQuery;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

/**
 * @author poornima_govindrao
 *
 * Action which forms the Advance query object and 
 * creates a Temporary table with the search results
 */
public class AdvanceSearchResultsAction extends BaseAction
{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		//Query Start object
		String aliasName = Constants.PARTICIPANT;
		String pageOf=Constants.PAGEOF_QUERY_RESULTS;
		AdvanceQueryBizlogic advBizLogic = (AdvanceQueryBizlogic)BizLogicFactory.getBizLogic(Constants.ADVANCE_QUERY_INTERFACE_ID);

		//Get the advance query root object from session 
		HttpSession session = request.getSession();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_ROOT);
		
		//Create Advance Query object
		Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);

		//Set the root object as the where conditions
		((AdvancedConditionsImpl)((AdvancedQuery)query).getWhereConditions()).setWhereCondition(root);
		
		//Set the table set for join Condition 
		Set tableSet = new HashSet();
		advBizLogic.setTables(root,tableSet);
		setTablesDownTheHeirarchy(tableSet);
		query.setTableSet(tableSet);
		
		List columnNames = new ArrayList();
		SimpleQueryBizLogic bizLogic = new SimpleQueryBizLogic(); 
		
		//Set attribute type in the DataElement	
		setAttributeType(root);

		//Set Identifier of Participant,Collection Protocol, Specimen Collection Group and Specimen if not existing in the resultView
		Vector tablesVector = new Vector();
		tablesVector.add(Constants.PARTICIPANT);
		tablesVector.add(Constants.COLLECTION_PROTOCOL);
		tablesVector.add(Constants.SPECIMEN_COLLECTION_GROUP);
		tablesVector.add(Constants.SPECIMEN);
		tablesVector.add(Constants.COLLECTION_PROTOCOL_REGISTRATION);
		query.getIdentifierColumnIds(tablesVector);
		
		
		//Set tables for Configuration.
		Object selectedTables[] = query.getTableSet().toArray();
		session.setAttribute(Constants.TABLE_ALIAS_NAME,selectedTables);
		Logger.out.debug("tableSet from query before setting resultview :"+query.getTableSet());
		//Set the result view for Advance Search
	 	Vector selectDataElements = bizLogic.getSelectDataElements(null,query.getTableSet(), columnNames);
		Logger.out.debug("column display names "+columnNames+":"+columnNames.size());

		query.setResultView(selectDataElements);
	 	
		

	 	SessionDataBean sessionData = getSessionData(request);
	 	//Temporary table name with userID attached
		String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
		
		
		Map queryResultObjectDataMap = new HashMap();

        SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();

        simpleQueryBizLogic.createQueryResultObjectData(query.getTableSet(),

                                queryResultObjectDataMap,query);
        simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(
				queryResultObjectDataMap, query);

        simpleQueryBizLogic.setDependentIdentifiedColumnIds(

                                queryResultObjectDataMap, query);
        
        Map columnIdsMap = query.getColumnIdsMap();
		session.setAttribute(Constants.COLUMN_ID_MAP,columnIdsMap);
		Logger.out.debug("map of column ids:"+columnIdsMap+":"+columnIdsMap.size());

		//Create temporary table with the data from the Advance Query Search 
		String sql = query.getString();
		Logger.out.debug("no. of tables in tableSet after table created"+query.getTableSet().size()+":"+query.getTableSet());
	 	Logger.out.debug("Advance Query Sql"+sql);
		advBizLogic.createTempTable(sql,tempTableName,sessionData,queryResultObjectDataMap);

		//Set the columnDisplayNames in session
		session.setAttribute(Constants.COLUMN_DISPLAY_NAMES,columnNames);
		
		//Remove shopping cart attribute from session
		session.setAttribute(Constants.SHOPPING_CART,null);
		
		//Remove configured columns from session for previous query in same session
		session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST,null);
		session.setAttribute(Constants.CONFIGURED_COLUMN_DISPLAY_NAMES,null);
		
		//Remove the spreadsheet column display names from session
		session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,null);
		
		//Remove selected node from session
		session.setAttribute(Constants.SELECTED_NODE,null);
		
		//Remove select columnList from Session
		session.setAttribute(Constants.SELECT_COLUMN_LIST,null);
		
		request.setAttribute(Constants.PAGEOF, pageOf);
		return mapping.findForward(Constants.SUCCESS);
	}

	private void setTablesDownTheHeirarchy(Set tableSet)
	{
		if(!tableSet.contains(Constants.COLLECTION_PROTOCOL))
		{
			tableSet.add(Constants.COLLECTION_PROTOCOL);
			tableSet.add(Constants.SPECIMEN_COLLECTION_GROUP);
			tableSet.add(Constants.SPECIMEN);
		}
		else if(!tableSet.contains(Constants.SPECIMEN_COLLECTION_GROUP))
		{
			tableSet.add(Constants.SPECIMEN_COLLECTION_GROUP);
			tableSet.add(Constants.SPECIMEN);
		}
		else if(!tableSet.contains(Constants.SPECIMEN))
		{
			tableSet.add(Constants.SPECIMEN);
		}
		
	}
	//Parse the root and set the attribute type in the DataElement
	private void setAttributeType(DefaultMutableTreeNode tree) throws Exception
	{
		DefaultMutableTreeNode child;
		int childCount = tree.getChildCount();
		Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			child = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode advNode = (AdvancedConditionsNode)child.getUserObject();
			Vector conditions = advNode.getObjectConditions();
			Iterator conditionsItr = conditions.iterator();
			while(conditionsItr.hasNext())
			{
				Condition condition = (Condition)conditionsItr.next();
				String columnName = condition.getDataElement().getField();
				String aliasName = condition.getDataElement().getTable();
				QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
				String attributeType = bizLogic.getAttributeType(columnName,aliasName);
				condition.getDataElement().setFieldType(attributeType);
			}
			setAttributeType(child);
		}
	}
}