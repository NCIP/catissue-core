
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/* This action is an applet action called from DiagrammaticViewApplet class when user clicks on seach button of AddLimits.jsp.
 * This class gets IQuery Object from the applet and also generates sql out of it with the help of sqlGenerator.
 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
 * These results are stored in session so that they can be retrived at the time of loading of result's screen.
 * An emplty map is sent back to the applet. 
 * @author deepti_shelar
 */
public class ViewSearchResultsAction extends BaseAppletAction
{
	/**
	 * This method gets IQuery Object from the applet and also generates sql out of it with the help of sqlGenerator.
	 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
	 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
	 * These results are stored in session so that they can be retirved at the time of loading of result's screen.
	 * An emplty map is sent back to the applet. 
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
			HttpSession session = request.getSession();
			session.setAttribute(AppletConstants.QUERY_OBJECT, query);
			QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
			SessionDataBean sessionData = getSessionData(request);
			ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
			String selectSql = "";
			String tableName = "";
			Map<Long, Map<AttributeInterface, String>> columnMap = null;
			Map ruleDetailsMap = new HashMap();
			try
			{
				selectSql = sqlGenerator.generateSQL(query);
				tableName = outputTreeBizLogic.createOutputTreeTable(selectSql, sessionData);
				columnMap = sqlGenerator.getColumnMap();
				session.setAttribute(Constants.ID_COLUMNS_MAP, columnMap);
				IOutputTreeNode root = query.getRootOutputClass();
				Map<Long, IOutputTreeNode> idNodesMap = QueryObjectProcessor.getAllChildrenNodes(root);
				session.setAttribute(Constants.ID_NODES_MAP, idNodesMap);
				Vector treeData = outputTreeBizLogic.createDefaultOutputTreeData(tableName, query, sessionData, columnMap);
				session.setAttribute(Constants.TREE_DATA, treeData);
				QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
				String parentNodeId = null;
				boolean isFirstLevel = true;
				Map spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(tableName, root, columnMap, isFirstLevel, parentNodeId,sessionData);
				session.setAttribute(Constants.SPREADSHEET_DATA_LIST, spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST));
				session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST));;
				
			}
			catch (MultipleRootsException e)
			{
				Logger.out.error(e);
				ruleDetailsMap.put(AppletConstants.ERROR_MESSAGE, e.getMessage());
				throw e;
			}
			catch (SqlException e)
			{
				Logger.out.error(e);
				ruleDetailsMap.put(AppletConstants.ERROR_MESSAGE, e.getMessage());
				throw e;
			} 
			catch (DAOException e)
			{
				Logger.out.error(e);
				throw e;
			}
			finally
			{
				writeMapToResponse(response, ruleDetailsMap);
			}
		}
		return null;
	}

	/**
	 * This is a overloaded method to call the actions method set bt applet class.
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
