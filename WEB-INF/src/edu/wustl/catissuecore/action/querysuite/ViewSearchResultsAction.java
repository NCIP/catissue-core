
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This action is an applet action called from DiagrammaticViewApplet class when user clicks on seach button of AddLimits.jsp 
 * and defineresultsView.jsp.This class gets IQuery Object from the applet and generates sql out of it with the help of sqlGenerator.
 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
 * These results are stored in session so that they can be retrived at the time of loading of result's screen.
 * An empty map is sent back to the applet. 
 * @author deepti_shelar
 */
public class ViewSearchResultsAction extends BaseAppletAction
{
	/**
	 * This method gets IQuery Object from the applet and also generates sql out of it with the help of sqlGenerator.
	 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
	 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
	 * These results are stored in session so that they can be retirved at the time of loading of result's screen.
	 * An empty map is sent back to the applet. 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			IQuery query = (IQuery) inputDataMap.get(AppletConstants.QUERY_OBJECT);
			Map<String,String> validationMessagesMap = new HashMap<String,String>();
			try
			{
				boolean isZeroRecordsFound = setResultData(request, query);
				if(isZeroRecordsFound)
				{
					String errorMessage = ApplicationProperties.getValue("query.zero.records.present");
					validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
				}
			}
			catch (MultipleRootsException e)
			{
				Logger.out.error(e);
				String errorMessage = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
				validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}
			catch (SqlException e)
			{
				Logger.out.error(e);
				String errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
				validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			} 
			catch (ClassNotFoundException e)
			{
				Logger.out.error(e);
				String errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
				validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}
			catch (DAOException e)
			{
				Logger.out.error(e);
				String errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
				validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}
			finally
			{
				writeMapToResponse(response, validationMessagesMap);
			}
		}
		return null;
	}

	/**
	 * This method extracts query object and forms results for tree and grid.
	 * @param request HttpRequest
	 * @param query IQuery
	 * @throws MultipleRootsException will be thrown when more than one root are present in query
	 * @throws SqlException error in sql 
	 * @throws DAOException error in DAO 
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private boolean setResultData(HttpServletRequest request, IQuery query) throws MultipleRootsException, SqlException, DAOException, ClassNotFoundException
	{ 
		HttpSession session = request.getSession();
		int recordsPerPage; 
		String recordsPerPageSessionValue = (String)session.getAttribute(Constants.RESULTS_PER_PAGE);
		if (recordsPerPageSessionValue==null)
		{
				recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
				session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage+"");
		}
		else
			recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
		
		session.setAttribute(AppletConstants.QUERY_OBJECT, query);
		Map<Long, Map<AttributeInterface, String>> columnMap = null;
		String selectSql = "";
		SessionDataBean sessionData = getSessionData(request);
		SqlGenerator sqlGenerator = (SqlGenerator)SqlGeneratorFactory.getInstance();
		QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
		selectSql = sqlGenerator.generateSQL(query);
		outputTreeBizLogic.createOutputTreeTable(selectSql, sessionData);
		//Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>> outputTreeMap = sqlGenerator.getOutputTreeMap();
		List<OutputTreeDataNode> rootOutputTreeNodeList = sqlGenerator.getRootOutputTreeNodeList();
		OutputTreeDataNode rootNode = rootOutputTreeNodeList.get(0);
		session.setAttribute(Constants.CURRENT_SELECTED_OBJECT, rootNode);
		session.setAttribute(Constants.TREE_ROOTS,rootOutputTreeNodeList);
		//Set<OutputTreeDataNode> keys = outputTreeMap.keySet();
		Long noOfTrees = new Long(rootOutputTreeNodeList.size());
		session.setAttribute(Constants.NO_OF_TREES, noOfTrees);
		Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryObjectProcessor.getAllChildrenNodes(rootOutputTreeNodeList);
		session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
		int i =0;
		boolean isZeroRecordsFound = false;
		for(OutputTreeDataNode node :rootOutputTreeNodeList)
		{
			Vector treeData = outputTreeBizLogic.createDefaultOutputTreeData(i,node, sessionData);
			int resultsSize = treeData.size();
			if(resultsSize == 0)
			{
				isZeroRecordsFound = true;
			}
			session.setAttribute(Constants.TREE_DATA+"_"+i, treeData);
			i += 1;
		}
		OutputTreeDataNode node = rootOutputTreeNodeList.get(0);
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String parentNodeId = null;
		String treeNo = "0";
		Map spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(treeNo,node, sessionData,parentNodeId,recordsPerPage);
		QuerySessionData querySessionData = (QuerySessionData) spreadSheetDatamap.get(Constants.QUERY_SESSION_DATA);
		int totalNumberOfRecords = querySessionData.getTotalNumberOfRecords();
		session.setAttribute(Constants.QUERY_SESSION_DATA,querySessionData);
		session.setAttribute(Constants.TOTAL_RESULTS,new Integer(totalNumberOfRecords));	
		List list= (List)spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST);
		session.setAttribute(Constants.PAGINATION_DATA_LIST, list);
		session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST));
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA,null);
		
		return isZeroRecordsFound;
	}

	/**
	 * This is a overloaded method to call the actions method set by applet class.
	 * @param methodName String
	 * @param mapping ActionMapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
			{
		if (methodName.trim().length() > 0)
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object args[] = {mapping, form, request, response};
				return (ActionForward) method.invoke(this, args);
			}
			else
				return null;
		}
		return null;
			}
}
