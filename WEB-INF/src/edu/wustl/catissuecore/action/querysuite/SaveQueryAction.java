/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.hibernate.HibernateCleanser;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.ObjectCloner;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This class saves the Query in Dag into database.
 * 
 * @author chetan_patil
 * @created Sep 11, 2007, 3:50:16 PM
 */
public class SaveQueryAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{ 
		HttpSession session = request.getSession();
		IQuery query = (IQuery) session.getAttribute(AppletConstants.QUERY_OBJECT);
		String target = Constants.FAILURE;
		if (query != null)
		{
			/**
			 * Name: Abhishek Mehta
			 * Reviewer Name : Deepti 
			 * Bug ID: 5661
			 * Patch ID: 5661_3
			 * See also: 1-4 
			 * Description : Calling bizlogic insert and update
			 */
			
			IParameterizedQuery parameterizedQuery = populateParameterizedQueryData(query,
					actionForm, request);
			if(parameterizedQuery != null)
			{
			try
			{
				IBizLogic bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
						.getValue("app.bizLogicFactory"), "getBizLogic",
						Constants.CATISSUECORE_QUERY_INTERFACE_ID);
//				if (query.getId() != null && query instanceof ParameterizedQuery)
//				{
//					bizLogic.update(parameterizedQuery, Constants.HIBERNATE_DAO);
//					Logger.out.info(ApplicationProperties.getValue("query.update.info"));
//				}
//				else
//				{
//					bizLogic.insert(parameterizedQuery, Constants.HIBERNATE_DAO);
//					Logger.out.info(ApplicationProperties.getValue("query.saved.info"));
//				}
                IParameterizedQuery queryClone = ObjectCloner.clone(parameterizedQuery);
                new HibernateCleanser(queryClone).clean();
                bizLogic.insert(queryClone, Constants.HIBERNATE_DAO);
				target = Constants.SUCCESS;
				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("query.saved.success");
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);
				
				request.setAttribute(Constants.QUERY_SAVED, Constants.TRUE);
			}
			catch (BizLogicException bizLogicException)
			{
				setActionError(request, bizLogicException.getMessage());
				Logger.out.error(bizLogicException.getMessage(), bizLogicException);
			}
			catch (UserNotAuthorizedException userNotAuthorizedException)
			{
				SessionDataBean sessionDataBean = getSessionData(request);
				String userName = "";
				if (sessionDataBean != null)
				{
					userName = sessionDataBean.getUserName();
				}

				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("access.addedit.object.denied", userName,
						parameterizedQuery.getClass().getName());
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);

				Logger.out.error(userNotAuthorizedException.getMessage(),
						userNotAuthorizedException);
			}
		}
	}
		else
		{
			// Handle null query 
			String errorMsg = ApplicationProperties.getValue("query.noLimit.error");
			setActionError(request,errorMsg);
		}
		return actionMapping.findForward(target);
	}

	/**
	 * This method sets the error action 
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

	/**
	 * This method populates the Parameterized Query related data form the
	 * ActionForm and returns the new ParameterizedQuery object
	 * 
	 * @param query
	 * @param actionForm
	 * @return
	 */
	private IParameterizedQuery populateParameterizedQueryData(IQuery query, ActionForm actionForm,
			HttpServletRequest request)
	{
		SaveQueryForm saveActionForm = (SaveQueryForm) actionForm;
		String error = "";
		
		/**
		 * Name: Abhishek Mehta
		 * Reviewer Name : Deepti 
		 * Bug ID: 5661
		 * Patch ID: 5661_4
		 * See also: 1-4 
		 * Description : Creating IParameterizedQuery's new instance only if it new query else type casting IQuery to IParameterizedQuery. 
		 */
		
		IParameterizedQuery parameterizedQuery = (IParameterizedQuery)query;
		
		if (query.getId() == null)
		{
			parameterizedQuery = new ParameterizedQuery(query);
		}
		
		HttpSession session = request.getSession();
		String queryTitle = saveActionForm.getTitle();
		if (queryTitle != null)
		{
			parameterizedQuery.setName(queryTitle);
		}

		String queryDescription = saveActionForm.getDescription();
		if (queryDescription != null)
		{
			parameterizedQuery.setDescription(queryDescription);
		}
		else
		{
			parameterizedQuery.setDescription("");
		}
		
		CreateQueryObjectBizLogic bizLogic = new CreateQueryObjectBizLogic();
		String conditionList = request.getParameter(Constants.CONDITIONLIST);
		Map<String, String> displayNameMap = getDisplayNamesForConditions(saveActionForm, request);
		error = bizLogic.setInputDataToQuery(conditionList, parameterizedQuery.getConstraints(),
				displayNameMap);
		if (error != null && error.trim().length() > 0)
		{
			setActionError(request, error);
			return null;
		}
		// Saving view 
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
		List<IOutputAttribute> selectedOutputAttributeList = new ArrayList<IOutputAttribute>();
		if(selectedColumnsMetadata != null)
		{
			selectedOutputAttributeList = selectedColumnsMetadata.getSelectedOutputAttributeList();
		}
		if(query.getId()!= null && selectedColumnsMetadata.isDefinedView())
		{
			selectedOutputAttributeList = new ArrayList<IOutputAttribute>();
			
			String errorMessage = ApplicationProperties.getValue("query.edit.message");
			request.getSession().setAttribute("errorMessageForEditQuery", errorMessage);
			
		}
     	parameterizedQuery.setOutputTerms(query.getOutputTerms()); 
		parameterizedQuery.setOutputAttributeList(selectedOutputAttributeList);
		return parameterizedQuery;
	}
	
/**
 * This method returns the map<expressionid+attributeId,displayname> containing the displaynames entered by user for 
 * parameterized conditions 
 * @param saveActionForm
 * @param request
 * @return
 */
	
	private Map<String, String> getDisplayNamesForConditions(SaveQueryForm saveActionForm,
			HttpServletRequest request)
	{
		Map<String, String> displayNameMap = new HashMap<String, String>();
	
		String queryString = saveActionForm.getQueryString();
		if (queryString != null)
		{
			StringTokenizer strtokenizer = new StringTokenizer(queryString, ";");
			while (strtokenizer.hasMoreTokens())
			{
				String token = strtokenizer.nextToken();
				String displayName = request.getParameter(token + Constants.DISPLAY_NAME_FOR_CONDITION);
				displayNameMap.put(token, displayName);
			}
		}
		return displayNameMap;
	}

}
