package edu.wustl.catissuecore.action.querysuite;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;

public class DeleteQueryAction extends BaseAction{
	private static final String QUERY_ID="queryId";
	/**
	 * Action Handler for Deletes Save query from database 
	 */
	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String target = Constants.FAILURE;

		String queryIdStr = request.getParameter(QUERY_ID);
		HttpSession session = request.getSession();
		String queryDeletedInLastRequest = (String)session.getAttribute(Constants.QUERY_ALREADY_DELETED);
		Long queryId = Long.parseLong(queryIdStr);
		if (queryId != null)
		{
			if(!queryIdStr.equalsIgnoreCase(queryDeletedInLastRequest))
			{
				session.setAttribute(Constants.QUERY_ALREADY_DELETED,queryIdStr);
				IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
						.getValue("app.bizLogicFactory"), "getBizLogic",
						Constants.CATISSUECORE_QUERY_INTERFACE_ID);
				try
				{
					Object object = bizLogic.retrieve(ParameterizedQuery.class.getName(), queryId);
					if (object != null)
					{
						IParameterizedQuery parameterizedQuery = (ParameterizedQuery)object;
						bizLogic.delete(parameterizedQuery,Constants.HIBERNATE_DAO);
						target = Constants.SUCCESS; 
						setActionError(request, ApplicationProperties.getValue("query.deletedSuccessfully.message"));
						session.setAttribute(Constants.QUERY_ALREADY_DELETED,queryIdStr );
					}
					else
					{
						session.removeAttribute(Constants.QUERY_ALREADY_DELETED);
					}
				}
				catch (DAOException daoException)
				{
					setActionError(request, daoException.getMessage());
				}
			}
		}
		return actionMapping.findForward(target);
	}

	/**
	 * Set Error Action
	 * @param request
	 * @param errorMessage
	 */
	private void setActionError(HttpServletRequest request, String errorMessage)
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
	}

}
