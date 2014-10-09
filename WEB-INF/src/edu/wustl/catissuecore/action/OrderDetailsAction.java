/**
 * <p>
 * Title: RequestDetailsAction Class>
 * <p>
 * Description: This class initializes the fields of RequestDetails.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ashish Gupta
 * @version 1.00 Created on Oct 05,2006
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.velocity.VelocityManager;
import edu.wustl.dao.HibernateDAO;

/**
 * @author renuka_bajpai
 */
public class OrderDetailsAction extends BaseAction
{

	/**
	 * logger.
	 */

	private static final Logger LOGGER = Logger.getCommonLogger(RequestDetailsAction.class);
	private StringBuilder speciemnIdValue; 

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{
			
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
			.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			HibernateDAO dao=null;
			try
			{
				dao=(HibernateDAO)AppUtility.openDAOSession((SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA));
				String xmlString = VelocityManager.getInstance().evaluate(orderBizLogic.getOrderItemsDetailForGrid(Long.parseLong(request.getParameter("id")),dao),"orderGridTemplate.vm");
				response.setContentType("text/xml");
		        response.getWriter().write(xmlString);
			}
			finally
			{
				if(dao!=null)
				{
					dao.closeSession();
				}
			}
			return null;
		
		}

}