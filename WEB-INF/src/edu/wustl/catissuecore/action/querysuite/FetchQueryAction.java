package edu.wustl.catissuecore.action.querysuite;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateUIFromQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;

public class FetchQueryAction extends BaseAction {

	protected ActionForward executeAction(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String target = Constants.FAILURE;
		String htmlContents = null;
		String queryIdParameter = request.getParameter("queryId");

		try {

			Long queryId = Long.parseLong(queryIdParameter);

			List<IParameterizedQuery> queryList = null;
			IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(
					ApplicationProperties.getValue("app.bizLogicFactory"),
					"getBizLogic", Constants.CATISSUECORE_QUERY_INTERFACE_ID);
			queryList = bizLogic.retrieve(ParameterizedQuery.class.getName(),
					"id", queryId);
			if (queryList != null && !queryList.isEmpty()) {
				IParameterizedQuery parameterizedQuery = queryList.get(0);
				request.getSession().setAttribute(AppletConstants.QUERY_OBJECT,
						parameterizedQuery);
				List<IParameterizedCondition> conditions = parameterizedQuery
						.getParameterizedConditions();
				if (conditions != null && !conditions.isEmpty()) {
					htmlContents = new CreateUIFromQueryObjectBizLogic()
							.getHTMLForSavedQuery(parameterizedQuery, false,
									Constants.EXECUTE_QUERY_PAGE);
					request.setAttribute("HTMLContents", htmlContents);
					target = "success";

				} else {
					target = Constants.EXECUTE_QUERY;

				}
			}
		} catch (NumberFormatException numberFormatException) {
			setActionError(request, "Query identifier is not valid.");
		} catch (DAOException daoException) {
			setActionError(request, daoException.getMessage());
		}

		return actionMapping.findForward(target);
	}

	private void setActionError(HttpServletRequest request, String errorMessage) {
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
	}
}
