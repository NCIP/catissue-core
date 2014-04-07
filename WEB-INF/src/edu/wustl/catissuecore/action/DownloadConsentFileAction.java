
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ConsentTrackingBizLogic;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.dto.ParticipantConsentFileDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;

public class DownloadConsentFileAction extends SecureAction
{

	private transient final Logger logger = Logger
			.getCommonLogger(DownloadConsentFileAction.class);

	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String mappingForward = null;
		HibernateDAO dao=null;
		try
		{
			SessionDataBean sessionDataBean = (SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA);
			dao = (HibernateDAO)AppUtility.openDAOSession(sessionDataBean);

			ConsentTrackingBizLogic bizLogic = new ConsentTrackingBizLogic();
			String cprId = request.getParameter("cprId");
			ParticipantConsentFileDTO participantConsentFileDTO = bizLogic
					.getParticipantConsentFileDetails(Long.parseLong(cprId), dao);
			response.setContentType("application/download");
			String fileName = participantConsentFileDTO.getFileName();
			fileName = fileName.replace(fileName.split("_")[0]+"_","");
			response.setHeader("Content-Disposition", "attachment;"
					+ "filename=\"" +fileName + "\"");
			byte[] byteArr = participantConsentFileDTO.getByteArr();

			response.getOutputStream().write(byteArr);

		}
		catch (IOException e)
		{
			response.getWriter().write(
					"<script>alert('" + e.getMessage() + "')</script>");
		}
		catch (ApplicationException e)
		{
			response.getWriter().write(
					"<script>alert('" + e.getMessage() + "')</script>");
		}
		catch (Exception e)
		{
			response.getWriter().write(
					"<script>alert('" + Constants.DWD_ERROR_MESSAGE
							+ "')</script>");

		}
		finally
		{
		    AppUtility.closeDAOSession(dao);
			response.flushBuffer();
		}

		return mapping.findForward(mappingForward);
	}

}