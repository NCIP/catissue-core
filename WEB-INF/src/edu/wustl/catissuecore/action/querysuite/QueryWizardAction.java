package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.flex.dag.DAGConstant;
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
		session.removeAttribute(AppletConstants.QUERY_OBJECT);
		session.removeAttribute(Constants.SELECTED_COLUMN_META_DATA);
		session.removeAttribute(Constants.IS_SAVED_QUERY);
		session.removeAttribute(edu.wustl.common.util.global.Constants.IS_SIMPLE_SEARCH);
		session.removeAttribute(DAGConstant.ISREPAINT);
		session.removeAttribute(DAGConstant.TQUIMap);
		session.removeAttribute(Constants.EXPORT_DATA_LIST);
		session.removeAttribute(Constants.ENTITY_IDS_MAP);
		searchForm = QueryModuleUtil.setDefaultSelections(searchForm);
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
}
