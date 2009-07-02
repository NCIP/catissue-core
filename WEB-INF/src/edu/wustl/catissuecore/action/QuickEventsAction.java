/**
 * <p>Title: QuickEventsAction Class</p>
 * <p>Description:  This class initializes the atributes required for the QuickEvents webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * Created : 03-July-2006.
 * @author mandar_deshmukh.
 *
 * This class initializes the atributes required for the QuickEvents webpage.
 */
public class QuickEventsAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in QuickEvents webpage.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//		DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		//IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		//IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		//String[] fields = {Constants.SYSTEM_IDENTIFIER};
		/*   List specimenList = bizLogic.getList(Specimen.class.getName(), fields,
		 *  Constants.SYSTEM_IDENTIFIER, true);
		 	request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenList); */

		request.setAttribute(Constants.EVENT_PARAMETERS_LIST, Constants.EVENT_PARAMETERS);

		//add messages from session to request
		HttpSession session = request.getSession(true);
		if (session != null)
		{
			ActionMessages messages = (ActionMessages) session.getAttribute("messages");
			if (messages != null)
			{
				saveMessages(request, messages);
				session.removeAttribute("messages");
			}
		}

		String pageOf = Constants.SUCCESS;

		return mapping.findForward(pageOf);
	}
}
