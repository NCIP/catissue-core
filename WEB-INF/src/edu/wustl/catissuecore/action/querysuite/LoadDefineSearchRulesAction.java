
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
import edu.wustl.common.action.BaseAction;
/**
 * When the Link representing the searched entity is clicked, the UI for Add Limits section is generated with help of GenerateHtmlForAddLimitsBizLogic.
 * @author deepti_shelar
 *
 */
public class LoadDefineSearchRulesAction extends BaseAction
{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		CategorySearchForm searchForm = (CategorySearchForm) form;
		String entityName = searchForm.getEntityName();
		GenerateHtmlForAddLimitsBizLogic searchProcessor = new GenerateHtmlForAddLimitsBizLogic();

		String html = "";
		Map searchedEntitiesMap = (Map) request.getSession().getAttribute("searchedEntitiesMap");
		if (searchedEntitiesMap != null)
		{
			Entity entity = (Entity) searchedEntitiesMap.get(entityName);
			if (entity != null)
			{
				html = searchProcessor.generateHTML(entity);
			}
		}
		response.setContentType("text/html");
		response.getWriter().write(html);
		return null;
	}

}
