
package edu.wustl.catissuecore.action;

// import edu.wustl.catissuecore.actionForm.DepartmentForm;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.bizlogic.InstitutionBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class AddInstitutionAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(AddInstitutionAction.class);

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
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final String institutionName = request.getParameter(Constants.INSTITUTION_NAME);
		final InstitutionBizLogic bizlogic = new InstitutionBizLogic();
		String institutionId = null;
		String responseString = "";
		final InstitutionForm institutionForm = (InstitutionForm) form;

		institutionForm.setOperation(Constants.ADD);
		institutionForm.setName(institutionName);

		final ActionForward forward = super.executeXSS(mapping, institutionForm, request, response);

		if ((forward != null) && (forward.getName().equals(Constants.FAILURE)))
		{
			responseString = AppUtility.getResponseString(request, responseString);
		}
		else
		{
			try
			{
				institutionId = bizlogic.getLatestInstitution(institutionName);
				responseString = institutionId + Constants.RESPONSE_SEPARATOR + institutionName;
			}
			catch (final BizLogicException bizEx)
			{
				this.logger.error("Exception occurred in retrieving Institution",bizEx);
				bizEx.printStackTrace();
			}
		}
		final PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		/**
		 * Sending the response
		 */
		out.write(responseString);

		return null;
	}

}
