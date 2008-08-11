package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.DefineGridViewBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.tree.QueryTreeNodeData;

/**
 * This action is invoked when user clicks on Define View button from the results screen. This will open a page where user can select the attributes 
 * for which he / she wants to see records.
 *  
 * @author deepti_shelar
 */
public class DefineQueryResultsViewAction extends BaseAction
{
	/**
	 * This method loads DefineGridResultsView jsp.This code forms a tree which contains all class names present in query 
	 * and all its attributes , from which user can select desired attributes to be shown in grid. 
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
		CategorySearchForm categorySearchForm = (CategorySearchForm)form;
		HttpSession session  = request.getSession();
		QueryDetails queryDetailsObj = new QueryDetails(session);
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
		List<NameValueBean> prevSelectedColumnNameValueBeanList= selectedColumnsMetadata.getSelectedColumnNameValueBeanList();
		if(!selectedColumnsMetadata.isDefinedView())
			prevSelectedColumnNameValueBeanList = null;
		OutputTreeDataNode currentSelectedObject = selectedColumnsMetadata.getCurrentSelectedObject();
		request.setAttribute(Constants.categorySearchForm,categorySearchForm);
		
		Map<Long,OutputTreeDataNode> uniqueIdNodesMap = (Map<Long,OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		Vector<QueryTreeNodeData> treeDataVector = new Vector<QueryTreeNodeData>();
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		defineGridViewBizLogic.createTree(categorySearchForm, queryDetailsObj,
				treeDataVector, currentSelectedObject,prevSelectedColumnNameValueBeanList);
		List<NameValueBean> selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		selectedColumnsMetadata.setSelectedColumnNameValueBeanList(selectedColumnNameValueBeanList);
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA,selectedColumnsMetadata);
		session.setAttribute(Constants.SELECTED_COLUMN_NAME_VALUE_BEAN_LIST,selectedColumnNameValueBeanList);
		session.setAttribute(Constants.TREE_DATA, treeDataVector);
		return mapping.findForward(Constants.SUCCESS);
	}
}

