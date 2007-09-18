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
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.util.dbManager.DAOException;
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
			queryList = bizLogic.retrieve(ParameterizedQuery.class.getName(), "id", queryId);

			if (queryList != null && !queryList.isEmpty())
			{
				IParameterizedQuery parameterizedQuery = queryList.get(0);
				HttpSession session = request.getSession();
				session.setAttribute(AppletConstants.QUERY_OBJECT, parameterizedQuery);

				String errorMessage = executeQuery(request, parameterizedQuery);
				if (errorMessage == null)
				{
					target = Constants.SUCCESS;
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

	private String executeQuery(HttpServletRequest request, IParameterizedQuery parameterizedQuery)
	{
		String errorMessage = null;
		try
		{
			boolean isZeroRecordsFound = QueryModuleUtil.setResultData(request, parameterizedQuery);
			if (isZeroRecordsFound)
			{
				errorMessage = ApplicationProperties.getValue("query.zero.records.present");
			}
		}
		catch (MultipleRootsException e)
		{
			errorMessage = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
		}
		catch (SqlException e)
		{
			errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
		}
		catch (ClassNotFoundException e)
		{
			errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
		}
		catch (DAOException e)
		{
			errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
		}

		return errorMessage;
	}

}
