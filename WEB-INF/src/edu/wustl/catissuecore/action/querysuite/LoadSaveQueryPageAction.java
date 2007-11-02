
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

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class LoadSaveQueryPageAction extends BaseAction
{

	@Override
	/**
	 * This action loads all the conditions from the query.   
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		IQuery queryObject = (IQuery) request.getSession().getAttribute(
				AppletConstants.QUERY_OBJECT);
		String target = Constants.FAILURE;
		String isAjax = request.getParameter("isAjax");
		String error = "";
		HttpSession session = request.getSession();
		String msg = "";
		if (queryObject != null && queryObject.getId() != null && queryObject instanceof ParameterizedQuery)
		{
			msg = "This query is already saved, Re-saving query feature is not yet implemented.";
			setActionError(request, msg);
			System.out.println(msg);
			target = Constants.SUCCESS;
			request.setAttribute("isQuerySaved","isQuerySaved");
		}
		else
		{
			if (queryObject != null)
			{
//				 Saving view 
				SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
				List<IOutputAttribute> selectedOutputAttributeList = null;
				if(selectedColumnsMetadata != null)
				{
					selectedOutputAttributeList = selectedColumnsMetadata.getSelectedOutputAttributeList();
				}
				else
				{
					try
					{
						selectedColumnsMetadata = new SelectedColumnsMetadata();
						selectedOutputAttributeList = QueryModuleUtil.getOutAttributeListForDirectSaveQuery(queryObject);
						if(selectedOutputAttributeList == null)
						{
							error = ApplicationProperties.getValue("query.empty.dag");
							setActionError(request, error);
							target = Constants.SUCCESS;
							request.setAttribute("isQuerySaved","isQuerySaved");
						} 
						selectedColumnsMetadata.setSelectedOutputAttributeList(selectedOutputAttributeList);
						session.setAttribute(Constants.SELECTED_COLUMN_META_DATA,selectedColumnsMetadata);
					}catch (MultipleRootsException e)
					{
						Logger.out.error(e);
						error = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
						setActionError(request, error);
						target = Constants.SUCCESS;
						request.setAttribute("isQuerySaved","isQuerySaved");
					}
					catch (SqlException e)
					{
						Logger.out.error(e);
						error = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
						setActionError(request, error);
						target = Constants.SUCCESS;
						request.setAttribute("isQuerySaved","isQuerySaved");
					}
				}
				
				boolean isShowAll = request.getParameter(Constants.SHOW_ALL) == null ? false : true;
				GenerateHtmlForAddLimitsBizLogic htmlGenerator = new GenerateHtmlForAddLimitsBizLogic();
				String htmlContents = htmlGenerator.getHTMLForSavedQuery(queryObject, isShowAll,
						Constants.SAVE_QUERY_PAGE);
				request.setAttribute(Constants.HTML_CONTENTS, htmlContents);
				String showAllLink = isShowAll
						? Constants.SHOW_SELECTED_ATTRIBUTE
						: Constants.SHOW_ALL_ATTRIBUTE;
				request.setAttribute("showAllLink", showAllLink);
				if (!isShowAll)
					request.setAttribute(Constants.SHOW_ALL, "true");
				target = Constants.SUCCESS;
			}
			else
			{
				// Handle null query 
				target = Constants.SUCCESS;
				String errorMsg = ApplicationProperties.getValue("query.noLimit.error");
				setActionError(request, errorMsg);
				request.setAttribute("isQuerySaved","isQuerySaved");
	
			}
		}
		return mapping.findForward(target);

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

}
