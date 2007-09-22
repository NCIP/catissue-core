/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

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
import edu.wustl.catissuecore.bizlogic.querysuite.CreateParameterizedQueryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This class saves the Query in Dag into database.
 * 
 * @author chetan_patil
 * @created Sep 11, 2007, 3:50:16 PM
 */
public class SaveQueryAction extends BaseAction {

	protected ActionForward executeAction(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		IQuery query = (IQuery) session
				.getAttribute(AppletConstants.QUERY_OBJECT);
		String target = Constants.FAILURE;
		if (query != null) {
			IParameterizedQuery parameterizedQuery = populateParameterizedQueryData(
					query, actionForm, request);

			IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(
					ApplicationProperties.getValue("app.bizLogicFactory"),
					"getBizLogic", Constants.CATISSUECORE_QUERY_INTERFACE_ID);
			try {
				bizLogic.insert(parameterizedQuery, Constants.HIBERNATE_DAO);
				target = Constants.SUCCESS;
				session.removeAttribute(AppletConstants.QUERY_OBJECT);
			}
			catch (BizLogicException bizLogicException)
			{
				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("errors.item",
						bizLogicException.getMessage());
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);

				Logger.out.error(bizLogicException.getMessage(),
						bizLogicException);
			} catch (UserNotAuthorizedException userNotAuthorizedException) {
				SessionDataBean sessionDataBean = getSessionData(request);
				String userName = "";
				if (sessionDataBean != null) {
					userName = sessionDataBean.getUserName();
				}

				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError(
						"access.addedit.object.denied", userName,
						parameterizedQuery.getClass().getName());
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);

				Logger.out.error(userNotAuthorizedException.getMessage(),
						userNotAuthorizedException);
			} catch (Exception e) {
				Logger.out.error(e.getMessage(), e);
			}
		}
		return actionMapping.findForward(target);
	}

	/**
	 * This method populates the Parameterized Query related data form the
	 * ActionForm and returns the new ParameterizedQuery object
	 * 
	 * @param query
	 * @param actionForm
	 * @return
	 */
	private IParameterizedQuery populateParameterizedQueryData(IQuery query,
			ActionForm actionForm, HttpServletRequest request) {
		SaveQueryForm saveActionForm = (SaveQueryForm) actionForm;
		IParameterizedQuery parameterizedQuery = new ParameterizedQuery(query);

		String queryTitle = saveActionForm.getTitle();
		if (queryTitle != null) {
			parameterizedQuery.setName(queryTitle);
		}

		String queryDescription = saveActionForm.getDescription();
		if (queryDescription != null) {
			parameterizedQuery.setDescription(queryDescription);
		} else {
			parameterizedQuery.setDescription("");
		}
		CreateParameterizedQueryBizLogic paraQueryBizObject = new CreateParameterizedQueryBizLogic();
		paraQueryBizObject.getParameterizedConditions(parameterizedQuery,
				request, actionForm);
		return parameterizedQuery;
	}

}
