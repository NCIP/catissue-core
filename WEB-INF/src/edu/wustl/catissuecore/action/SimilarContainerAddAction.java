
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class SimilarContainerAddAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SimilarContainerAddAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param form
	 *            object of ActionForm
	 * @param mapping
	 *            object of ActionMapping
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		final StorageContainerForm storageContainerForm = (StorageContainerForm) form;
		this.logger.info("Map in similarContainerAction:"
				+ storageContainerForm.getSimilarContainersMap());
		final ActionForward forward = super.execute(mapping, form, request, response);
		this.logger.info("forward in similar container add action:" + forward.getName());

		final List list = new ArrayList();
		final ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);

		this.logger.info("Errors:" + errors);
		if (errors == null || errors.size() == 0)
		{
			ActionMessages messages = null;
			messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"similarContaienrs.add.success", new Integer(storageContainerForm
							.getNoOfContainers()).toString()));
			if (messages != null)
			{
				this.saveMessages(request, messages);
			}
			this.logger.info("Map in similarContainerAction after insert:"
					+ storageContainerForm.getSimilarContainersMap());

			final int noOfContainers = storageContainerForm.getNoOfContainers();
			final Map simMap = storageContainerForm.getSimilarContainersMap();

			for (int i = 1; i <= noOfContainers; i++)
			{
				final String simContPrefix = "simCont:" + i + "_";
				final String contName = (String) simMap.get(simContPrefix + "name");
				final String Id = (String) simMap.get(simContPrefix + "Id");
				this.logger.info("contName:" + contName);
				/*
				 * String Id = new Long(storageContainerForm.getId() -
				 * (noOfContainers - i)).toString();
				 */
				this.logger.info("Id:" + Id);
				list.add(new NameValueBean(contName, Id));
			}
			request.setAttribute("similarContainerList", list);

		}
		this.logger.info("Forward:" + forward.getName());
		return forward;

	}
}
