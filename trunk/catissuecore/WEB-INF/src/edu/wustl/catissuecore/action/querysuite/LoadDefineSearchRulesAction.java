
package edu.wustl.catissuecore.action.querysuite;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.Entity;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
/**
 * When the Link representing the searched entity is clicked, the UI for Add Limits section is generated with help of
 * GenerateHtmlForAddLimitsBizLogic. The entity is taken from a map of user searched entities is already strored in session. 
 * @author deepti_shelar
 *
 */
public class LoadDefineSearchRulesAction extends BaseAction
{

	/**
	 * This method loads the html for addlimits section.This html is the replaced with the div data with the help of Ajax script.
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
		GenerateHtmlForAddLimitsBizLogic addLimitsBizLogic = new GenerateHtmlForAddLimitsBizLogic();

		String html = "";
		Map searchedEntitiesMap = (Map) request.getSession().getAttribute(Constants.SEARCHED_ENTITIES_MAP);
		if (searchedEntitiesMap != null)
		{
			Entity entity = (Entity) searchedEntitiesMap.get(entityName);
			if (entity != null)
			{
				html = addLimitsBizLogic.generateHTML(entity,null);
			}
		}
		response.setContentType("text/html");
		response.getWriter().write(html);
		return null;
	}

}
