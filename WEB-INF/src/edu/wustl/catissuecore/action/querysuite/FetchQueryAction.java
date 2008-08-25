
package edu.wustl.catissuecore.action.querysuite;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.utils.QueryUtility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
/**
 * 
 * @author Chetan Patil
 *
 */
public class FetchQueryAction extends BaseAction
{

	/**
	 * This action fetch saved query and loads the parameterized conditions
	 */
	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = Constants.FAILURE;
		Long queryId = null;
		SaveQueryForm saveQueryForm = (SaveQueryForm) actionForm;
		if (request.getAttribute("queryId") == null)
		{
			queryId = saveQueryForm.getQueryId();
		}
		else
		{
			queryId = (Long) request.getAttribute("queryId");
			String htmlContent = saveQueryForm.getQueryString();
			request.setAttribute(Constants.HTML_CONTENTS, htmlContent);
			target = Constants.SUCCESS;
			return actionMapping.findForward(target);
		}

		Logger.out.debug("Fetching query having identifier as " + queryId);
		if (queryId != null)
		{
			IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
					.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.CATISSUECORE_QUERY_INTERFACE_ID);
			try
			{
				List<IParameterizedQuery> queryList = bizLogic.retrieve(ParameterizedQuery.class
						.getName(), "id", queryId);
				if (!queryList.isEmpty())
				{
					IParameterizedQuery parameterizedQuery = queryList.get(0);
					request.getSession().setAttribute(AppletConstants.QUERY_OBJECT,
							parameterizedQuery);
//					Map<IExpression, Collection<IParameterizedCondition>> expressionIdConditionCollectionMap = QueryUtility
//							.getAllParameterizedConditions(parameterizedQuery);

					if (parameterizedQuery.getParameters().isEmpty())
					{
						target = Constants.EXECUTE_QUERY;
					}
					else
					{
						Map<Integer,ICustomFormula> customFormulaIndexMap = new HashMap<Integer, ICustomFormula>();
						String htmlContents = new GenerateHtmlForAddLimitsBizLogic()
								.getHTMLForSavedQuery(parameterizedQuery, false,
										Constants.EXECUTE_QUERY_PAGE,customFormulaIndexMap);
						request.setAttribute(Constants.HTML_CONTENTS, htmlContents);
						request.getSession().setAttribute("customFormulaIndexMap", customFormulaIndexMap);
						saveQueryForm.setQueryString(htmlContents);
						target = Constants.SUCCESS;
					}
				}
			}
			catch (DAOException daoException)
			{
				setActionError(request, daoException.getMessage());
			}
		}
		else
		{
			target = Constants.SUCCESS;
			setActionError(request, "Query identifier is not valid.");
		}
		return actionMapping.findForward(target);
	}

	/**
	 * 
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
