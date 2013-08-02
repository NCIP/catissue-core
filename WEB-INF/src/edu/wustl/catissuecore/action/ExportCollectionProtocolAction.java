
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ExportCollectionProtocolBizLogic;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.logger.Logger;

/**
 * This action class receives and forwards all the bulk operations
 * request done from UI.
 * @author sagar_baldwa
 *
 */
public class ExportCollectionProtocolAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ExportCollectionProtocolAction.class);

	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String mappingForward = null;
		try
		{

			ExportCollectionProtocolBizLogic exportCP = new ExportCollectionProtocolBizLogic();
			StringBuffer downloadFile = exportCP.getCPXMLFile(request
					.getParameter("title"));
			response.setContentType("application/download");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ request.getParameter("shortTitle") + ".csv\"");
			response.setBufferSize(downloadFile.length());
			response.getWriter().append(downloadFile);

		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
		}
		return mapping.findForward(mappingForward);
	}
}