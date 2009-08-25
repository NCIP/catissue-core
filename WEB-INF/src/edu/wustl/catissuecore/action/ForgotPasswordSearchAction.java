/**
 * <p>
 * Title: ForgotPasswordSearchAction Class>
 * <p>
 * Description: This Class is used to retrieve the password of the user and mail
 * it to his email address.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00 Created on Apr 19, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ForgotPasswordForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to retrieve the password of the user and mail it to his
 * email address.
 *
 * @author gautam_shetty
 */
public class ForgotPasswordSearchAction extends Action
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ForgotPasswordSearchAction.class);

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
	 * @throws IOException
	 *             IOException
	 * @throws ServletException
	 *             ServletException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		String target = null;

		try
		{
			final ForgotPasswordForm forgotPasswordForm = (ForgotPasswordForm) form;
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(forgotPasswordForm
					.getFormId());

			// Retrieves and sends the password to the user whose email address
			// is passed
			// else returns the error key in case of an error.
			final SessionDataBean sessionData = new SessionDataBean();
			sessionData.setUserName(forgotPasswordForm.getEmailAddress());
			final String message = userBizLogic.sendForgotPassword(forgotPasswordForm
					.getEmailAddress(), sessionData);

			request.setAttribute(Constants.STATUS_MESSAGE_KEY, message);

			target = new String(Constants.SUCCESS);
		}
		catch (final BizLogicException excp)
		{
			target = new String(Constants.FAILURE);
			this.logger.error(excp.getMessage());
		}

		return (mapping.findForward(target));
	}
}