package edu.wustl.catissuecore.action.querysuite;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
/**
 * Action is called when user clicks on QueryWizard link on search tab.
 * @author deepti_shelar
 */
public class QueryWizardAction extends BaseAction
{
	/**
	 * This method loads the data required for categorySearch.jsp
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
		CategorySearchForm searchForm = (CategorySearchForm) form;
		session.setAttribute(AppletConstants.QUERY_OBJECT, null);
		searchForm = QueryModuleUtil.setDefaultSelections(searchForm);
		Map<String, EntityInterface> searchedEntitiesMap = (Map<String, EntityInterface>) session.getAttribute(Constants.SEARCHED_ENTITIES_MAP);
		if (searchedEntitiesMap == null)
		{
			searchedEntitiesMap = new HashMap<String, EntityInterface>();
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
}
