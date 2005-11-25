/*
 * Created on Nov 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashSet;
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
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbManager.DAOException;
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
		
		//Get the advance query root object from session 
		HttpSession session = request.getSession();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_ROOT);
		
		//Create Advance Query object
		Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);
		
		//Set the root object as the where conditions
		((AdvancedConditionsImpl)((AdvancedQuery)query).getWhereConditions()).setWhereCondition(root);
		
		//Set the table set for join Condition 
		Set tableSet = new HashSet();
		setTables(root,tableSet);
		
		Logger.out.debug("no. of tables in tableSet "+tableSet.size());
		query.setTableSet(tableSet);
		List columnNames = new ArrayList();
		SimpleQueryBizLogic bizLogic = new SimpleQueryBizLogic(); 
		
		//Set the result view for Advance Search
	 	Vector selectDataElements = bizLogic.getSelectDataElements(null,tableSet, columnNames);
	 	query.setResultView(selectDataElements);
	 	
	 	SessionDataBean sessionData = getSessionData(request);
		//String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
	 	String tempTableName = Constants.QUERY_RESULTS_TABLE+"_null";
		
		//Set Identifier of participant,Collection Protocol, Specimen Collection Group and Specimen if not existing in the resultView
		Vector tablesVector = new Vector();
		tablesVector.add(Constants.PARTICIPANT);
		tablesVector.add(Constants.COLLECTION_PROTOCOL);
		tablesVector.add(Constants.SPECIMEN_COLLECTION_GROUP);
		tablesVector.add(Constants.SPECIMEN);
		Map columnIdsMap = query.getIdentifierColumnIds(tablesVector);
		
		//Create temporary table with the data from the Advance Query Search 
		AdvanceQueryBizlogic advBizLogic = (AdvanceQueryBizlogic)BizLogicFactory
												.getBizLogic(Constants.ADVANCE_QUERY_INTERFACE_ID);
		String sql = query.getString();
	 	Logger.out.debug("Advance Query Sql"+sql);
		//advBizLogic.createTempTable(sql,tempTableName,sessionData);
		session.setAttribute(Constants.COLUMN_ID_MAP,columnIdsMap);
		
		request.setAttribute(Constants.PAGEOF, pageOf);
		return mapping.findForward("success");
	}

	//Recursive function to Traverse root and set tables in path
	private void setTables(DefaultMutableTreeNode tree,Set tableSet)throws DAOException, ClassNotFoundException
	{
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		int childCount = tree.getChildCount();
		//Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			parent = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode parentAdvNode = (AdvancedConditionsNode)parent.getUserObject();
			String parentTable = parentAdvNode.getObjectName();
			tableSet.add(parentTable);

			if(!parent.isLeaf())
			{
				child = (DefaultMutableTreeNode)parent.getFirstChild();
				AdvancedConditionsNode childAdvNode = (AdvancedConditionsNode)child.getUserObject();
				String childTable = childAdvNode.getObjectName();
				Logger.out.debug("parent Table Name in the root object: "+parentTable);
				Logger.out.debug("child Table Name in the root object: "+childTable);
				//tableSet.add("CollectionProtocolRegistration");
				tableSet.add(childTable);
				QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
												.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
				String parentTableId = bizLogic.getTableIdFromAliasName(parentTable);
				Logger.out.debug("parent Table id in the root object: "+parentTableId);
				String childTableId = bizLogic.getTableIdFromAliasName(childTable);
				Logger.out.debug("child Table id in the root object: "+childTableId);
				Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(childTableId));
				Logger.out.debug("tablesInPath after method call:"+tablesInPath);
				tableSet.addAll(tablesInPath);
			}
			setTables(parent,tableSet);
		}
	}
}