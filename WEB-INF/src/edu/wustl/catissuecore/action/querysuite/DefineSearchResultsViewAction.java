package edu.wustl.catissuecore.action.querysuite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.querysuite.DefineAdvancedResultsView;
import edu.wustl.common.tree.QueryTreeNodeData;
/**
 * This is a action class to Define Search Results View.
 * @author deepti_shelar
 *
 */
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
		List<EntityInterface> ListOfEntitiesInQuery = (List)request.getSession().getAttribute(Constants.LIST_OF_ENTITIES_IN_QUERY);
		Map<String, Vector<QueryTreeNodeData>> treesMap = new HashMap<String, Vector<QueryTreeNodeData>>();
		DefineAdvancedResultsView defineResults = new DefineAdvancedResultsView();
		treesMap = defineResults.getTreeForThisCategory(ListOfEntitiesInQuery);
		request.setAttribute("treesMap",treesMap);
		if(actionForm.getNextOperation() != null && actionForm.getNextOperation().equalsIgnoreCase("BuildNewTree"))
		{
			request.getSession().setAttribute("nextOperation",actionForm.getNextOperation());
			return mapping.findForward("BuildNewTree");
		} 
		return mapping.findForward(Constants.SUCCESS);
	}
}

