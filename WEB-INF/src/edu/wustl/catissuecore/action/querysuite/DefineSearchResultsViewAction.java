package edu.wustl.catissuecore.action.querysuite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.TreeBizLogic;
import edu.wustl.common.action.BaseAction;
//import edu.wustl.common.bizlogic.querySuite.DefineAdvancedResultsView;

public class DefineSearchResultsViewAction extends BaseAction
{
	/**
	 * This method loads the data required for Add limits section in categorySearch.jsp , This data can be validation messages or 
	 * can be list of attributes and its associated operators depending upon the selection of category/entity made by user.  
	 * 
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
		CategorySearchForm actionForm = (CategorySearchForm)form;
		Map<String, Vector<?>> treesMap = new HashMap<String, Vector<?>>();
//		DefineAdvancedResultsView defineResults = new DefineAdvancedResultsView();
		int categoryID = 14;
		
		TreeBizLogic treeBizLogic = new TreeBizLogic();
//		Vector treeData = defineResults.getTreeForThisCategory(categoryID);
		Vector treeData1 = treeBizLogic.getQueryTreeNode();
		Vector treeData2 = treeBizLogic.getQueryTreeNodeForCategory2();
		Vector treeData3 = treeBizLogic.getQueryTreeNodeForCategory3();
//		treesMap.put("Category1",treeData);
		treesMap.put("Category2",treeData2);
		treesMap.put("Category3",treeData3);
		request.setAttribute("treesMap",treesMap);
		if(actionForm.getNextOperation() != null && actionForm.getNextOperation().equalsIgnoreCase("BuildNewTree"))
		{
			return mapping.findForward("BuildNewTree");
		} 
		return mapping.findForward("success");
	}

}
