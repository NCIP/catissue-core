/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author chetan_patil
 * @created Sep 14, 2007, 9:53:15 AM
 */
public class FetchAndExecuteQueryAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = Constants.FAILURE;

		try
		{
			SaveQueryForm saveQueryForm = (SaveQueryForm) actionForm;
			Long queryId = saveQueryForm.getQueryId();

			List<IParameterizedQuery> queryList = null;
			IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
					.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.CATISSUECORE_QUERY_INTERFACE_ID);
			Object object = bizLogic.retrieve(ParameterizedQuery.class.getName(), queryId);

			if (object != null)
			{
				IParameterizedQuery parameterizedQuery = (ParameterizedQuery)object;
				HttpSession session = request.getSession();
				session.setAttribute(AppletConstants.QUERY_OBJECT, parameterizedQuery);

				String errorMessage = QueryModuleUtil.executeQuery(request, parameterizedQuery);
				if (errorMessage == null)
				{
					target = Constants.SUCCESS;
				}
				else if(errorMessage.equalsIgnoreCase(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS))
				{
					target = Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS;
					return actionMapping.findForward(target);
				}
				else
				{
					setActionError(request, errorMessage);
				}
			}
			else
			{
				setActionError(request, "No result found.");
			}
		}
		catch (NumberFormatException numberFormatException)
		{
			setActionError(request, "Query identifier is not valid.");
		}
		catch (DAOException daoException)
		{
			setActionError(request, daoException.getMessage());
		}

		return actionMapping.findForward(target);
	}

	private void setActionError(HttpServletRequest request, String errorMessage)
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
	}

}
