package edu.wustl.catissuecore.action.querysuite;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import edu.wustl.common.util.dbManager.DAOException;
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
		Long queryId = Long.parseLong(queryIdStr);
		if (queryId != null)
		{
			IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
					.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.CATISSUECORE_QUERY_INTERFACE_ID);
			try
			{
				List<IParameterizedQuery> queryList = bizLogic.retrieve(ParameterizedQuery.class.getName(), Constants.ID, queryId);
				if (!queryList.isEmpty())
				{
					IParameterizedQuery parameterizedQuery = queryList.get(0);
					{
						bizLogic.delete(parameterizedQuery,Constants.HIBERNATE_DAO);
						target = Constants.SUCCESS;
					}
				}
			}
			catch (DAOException daoException)
			{
				setActionError(request, daoException.getMessage());
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
