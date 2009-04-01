/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleConstants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.metadata.util.DyExtnObjectCloner;

/**
 * @author chetan_patil
 * @created Sep 14, 2007, 9:53:15 AM
 */
public class ExecuteQueryAction extends BaseAction
{

	/**
	 * This action process the input from the UI and executes parameterized query with the input values.
	 */
	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{ 
		String target = Constants.FAILURE;        
		//request.setAttribute(Constants.IS_SAVED_QUERY, Constants.TRUE);
		HttpSession session = request.getSession();
		session.removeAttribute(Constants.SELECTED_COLUMN_META_DATA);
		session.removeAttribute(Constants.EXPORT_DATA_LIST);
		session.removeAttribute(Constants.ENTITY_IDS_MAP);
		session.removeAttribute(Constants.ATTRIBUTE_COLUMN_NAME_MAP);
		IParameterizedQuery parameterizedQuery = (IParameterizedQuery) session.getAttribute(AppletConstants.QUERY_OBJECT);
		IParameterizedQuery parameterizedQuery1 = new DyExtnObjectCloner().clone(parameterizedQuery);
		
		String conditionstr = request.getParameter("conditionList");
		String rhsList = request.getParameter(QueryModuleConstants.STR_TO_FORM_TQ);
		session.setAttribute(Constants.IS_SAVED_QUERY, Constants.TRUE);
		Map<Integer,ICustomFormula> customFormulaIndexMap = (Map<Integer,ICustomFormula>)session.getAttribute(QueryModuleConstants.CUSTOM_FORMULA_INDEX_MAP);
		session.removeAttribute(QueryModuleConstants.CUSTOM_FORMULA_INDEX_MAP);
		if (conditionstr != null) 
		{
			CreateQueryObjectBizLogic bizLogic = new CreateQueryObjectBizLogic();
			String errorMessage = bizLogic.setInputDataToQuery(conditionstr, parameterizedQuery1.getConstraints(), null,parameterizedQuery);
			errorMessage = bizLogic.setInputDataToTQ(parameterizedQuery1, Constants.EXECUTE_QUERY_PAGE, rhsList,customFormulaIndexMap);
			if (errorMessage.trim().length()>0)
			{
				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("errors.item", errorMessage);
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);
				target = Constants.INVALID_CONDITION_VALUES;
				request.setAttribute("queryId", parameterizedQuery.getId());
				return actionMapping.findForward(target);
			}
		}
		
		String errorMessage = QueryModuleUtil.executeQuery(request, parameterizedQuery1);
		session.setAttribute(AppletConstants.PARAMETERIZED_QUERY, parameterizedQuery1);
		session.setAttribute(AppletConstants.QUERY_OBJECT, parameterizedQuery);
		
		if (errorMessage == null)
		{
			session.setAttribute(AppletConstants.PARAMETERIZED_QUERY, null);
			target = Constants.SUCCESS;
		}
		else
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("errors.item", errorMessage);
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
		}
		
		return actionMapping.findForward(target);
	}
}