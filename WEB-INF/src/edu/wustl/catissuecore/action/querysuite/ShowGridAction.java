package edu.wustl.catissuecore.action.querysuite;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.querysuite.security.utility.Utility;

/**
 * This class is invoked when user clicks on a node from the tree. It loads the data required for grid formation.
 * @author deepti_shelar
 */
public class ShowGridAction extends BaseAction
{
	/**
	 * This method loads the data required for Query Output tree. 
	 * With the help of QueryOutputTreeBizLogic it generates a string which will be then passed to client side and tree is formed accordingly. 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{         
	 	HttpSession session = request.getSession();
	 	QueryDetails queryDetailsObj = new QueryDetails(session);
	 	Map<String, IOutputTerm> outputTermsColumns = (Map<String, IOutputTerm>)session.getAttribute(Constants.OUTPUT_TERMS_COLUMNS);
		IQuery query = (IQuery)session.getAttribute(AppletConstants.QUERY_OBJECT);
		boolean hasConditionOnIdentifiedField = AppUtility.isConditionOnIdentifiedField(query);
		session.setAttribute(Constants.HAS_CONDITION_ON_IDENTIFIED_FIELD, hasConditionOnIdentifiedField);
		Map<EntityInterface ,List<EntityInterface>> mainEntityMap =(Map<EntityInterface ,List<EntityInterface>>)session.getAttribute(Constants.MAIN_ENTITY_MAP);
		Map<Long, QueryResultObjectDataBean> queryResultObjectDataMap = (Map<Long, QueryResultObjectDataBean>)session.getAttribute(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP);
		Map<String, OutputTreeDataNode> uniqueIdNodesMap = (Map<String, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		Map<Long, Map<AttributeInterface, String>> columnMap = (Map<Long, Map<AttributeInterface, String>>) session.getAttribute(
				Constants.ID_COLUMNS_MAP);
		List<OutputTreeDataNode> rootOutputTreeNodeList = (List<OutputTreeDataNode>)session.getAttribute(Constants.TREE_ROOTS);
		SessionDataBean sessionData = getSessionData(request);
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
		String idOfClickedNode = request.getParameter(Constants.TREE_NODE_ID);
		QueryOutputTreeBizLogic treeBizLogic = new QueryOutputTreeBizLogic();
		idOfClickedNode = treeBizLogic.decryptId(idOfClickedNode);
 		Map spreadSheetDatamap = null;
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		int recordsPerPage = new Integer(recordsPerPageStr);
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String actualParentNodeId = idOfClickedNode.substring(idOfClickedNode.lastIndexOf(Constants.NODE_SEPARATOR) + 2, idOfClickedNode.length());
		String actualId = actualParentNodeId.substring(actualParentNodeId.lastIndexOf("_")+1, actualParentNodeId.length());
		if(actualId !=null && actualId.equals(Constants.HASHED_NODE_ID))
		{
		  getErrorMessageUserNotAuthorized(request);
		  return mapping.findForward(Constants.FAILURE);
		}
		
		else
		{
			String randomNumber = (String) session.getAttribute(Constants.RANDOM_NUMBER);
			if (idOfClickedNode.endsWith(Constants.LABEL_TREE_NODE))
			{
				spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForLabelNode(
						queryDetailsObj, columnMap, idOfClickedNode, recordsPerPage,
						selectedColumnsMetadata, hasConditionOnIdentifiedField, 
						queryResultObjectDataMap, query.getConstraints(), outputTermsColumns);
			}
			else
			{
				spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForDataNode(
						queryDetailsObj, actualParentNodeId, recordsPerPage, 
						selectedColumnsMetadata, hasConditionOnIdentifiedField, 
						queryResultObjectDataMap, query.getConstraints(), outputTermsColumns);
			}
			spreadSheetDatamap.put(Constants.MAIN_ENTITY_MAP, mainEntityMap);
			request.getSession().setAttribute(Constants.ENTITY_IDS_MAP,spreadSheetDatamap.get(Constants.ENTITY_IDS_MAP));
			request.getSession().setAttribute(Constants.EXPORT_DATA_LIST,spreadSheetDatamap.get(Constants.EXPORT_DATA_LIST));
			
			QueryModuleUtil.setGridData(request, spreadSheetDatamap);
		}
		
		return mapping.findForward(Constants.SUCCESS);
	}

	/**Method that will add an error message in action errors when id perticular data node is -1 i.e. user is not authorized to see 
	 * this perticular record. 
	 * @param request
	 */
	private void getErrorMessageUserNotAuthorized(HttpServletRequest request)
	{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("query.userNotAuthorizedError");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
	}
}