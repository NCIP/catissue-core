
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.bizlogic.CancerResearchBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class AddCRGAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(AddCRGAction.class);

	/**
	 * @param mapping : mapping
	 * @param form : form
	 * @param request : request
	 * @param response : response
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		final String crgName = request.getParameter(Constants.CRG_NAME);
		final CancerResearchBizLogic bizlogic = new CancerResearchBizLogic();
		String crgId = null;
		String responseString = null;

		final CancerResearchGroupForm crgForm = (CancerResearchGroupForm) form;
		crgForm.setOperation(Constants.ADD);
		crgForm.setName(crgName);

		final ActionForward forward = super.execute(mapping, crgForm, request, response);

		if ((forward != null) && (forward.getName().equals(Constants.FAILURE)))
		{
			responseString = AppUtility.getResponseString(request, responseString);
		}
		else
		{
			try
			{
				crgId = bizlogic.getLatestCRG(crgName);
				responseString = crgId + Constants.RESPONSE_SEPARATOR + crgName;
			}
			catch (final BizLogicException e)
			{
				this.logger.error("Exception occurred in retrieving Cancer Research Group");
				e.printStackTrace();
			}
		}

		final PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		/**
		 * Sending the response as crgId @ crgName
		 */
		out.write(responseString);
		return null;
	}

}
