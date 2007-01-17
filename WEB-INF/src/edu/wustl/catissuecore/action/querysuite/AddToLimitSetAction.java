
package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.common.action.BaseAction;

/**
 * This action is called when user clicks on Add Limits button of AddLimits.jsp.
 * This class creates Query Object and also generates validation messages with the help of CreateQueryObjectBizLogic. 
 * @author deepti_shelar
 */
public class AddToLimitSetAction extends BaseAction
{
	/**
	 * This method loads the data required for Add limits section in categorySearch.jsp , This data can be validation messages or 
	 * can be list of attributes and its associated operators depending upon the selection of category/entity made by user.  
	 * 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		CategorySearchForm searchForm = (CategorySearchForm) form;
		String entityName = searchForm.getEntityName();
		String queryStrTemp = searchForm.getTempStr();
		String queryStr = searchForm.getStringToCreateQueryObject();
		//TODO uncomment it after getting new release form cab2b 
		/*if(queryObject == null)
		{
			queryObject = new QueryObject();
		}*/
		/*if(queryObject != null)
		{
			Map searchedEntitiesMap = (Map) request.getSession().getAttribute("searchedEntitiesMap");
			Entity entity = (Entity) searchedEntitiesMap.get(entityName);
			CreateQueryObjectBizLogic queryProcessor = new CreateQueryObjectBizLogic();
			if (!queryStr.equalsIgnoreCase(""))
			{
				queryObject = queryProcessor.addRulesToQueryObject(queryObject,queryStr, entity, searchForm);
			}
			response.setContentType("text/html");
			if (searchForm.getErrors() == null || searchForm.getErrors().size() == 0)
			{
				queryStrTemp = "QueryString : " + queryStrTemp;
				response.getWriter().write(queryStrTemp);
			}
			else
			{
				List errorMessages = searchForm.getErrors();
				String errorMessagesStr = "";
				if (errorMessages != null && !errorMessages.isEmpty())
				{
					for (int i = 0; i < errorMessages.size(); i++)
					{
						errorMessagesStr = errorMessagesStr + errorMessages.get(i);
					}
				}
				response.getWriter().write(errorMessagesStr);
			}
			
		}
		response.getWriter().write(queryStrTemp);
		request.getSession().setAttribute("QueryObject", queryObject);*/
		return null;
	}
}

