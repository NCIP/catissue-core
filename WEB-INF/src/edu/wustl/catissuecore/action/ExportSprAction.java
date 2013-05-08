package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.util.SprPrintPdfUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

public class ExportSprAction extends SecureAction {

	private transient final Logger logger = Logger
			.getCommonLogger(ExportCollectionProtocolAction.class);

	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String mappingForward = null;
		try {
			SessionDataBean sessionDataBean = (SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA);
			String reportId = request.getParameter("reportId");
			String reportType = request.getParameter("reportType");
			SprPrintPdfUtil pdfUtil = new SprPrintPdfUtil();
			String sprNumber = request.getParameter("sprNumber");
			String deIdentifiedreportIdStr = request.getParameter("deIdentifiedId");
			Long deIdentifiedreportId = deIdentifiedreportIdStr!=null && ! "".equals(deIdentifiedreportIdStr.trim())?Long.parseLong(deIdentifiedreportIdStr):0L;
			Map<String,Object> returnMap;
			byte[] byteArr={};
			if (reportType.equals(Constants.IDENTIFIED)) {
					returnMap = pdfUtil.generateIdentifiedPdf(Long.parseLong(reportId),deIdentifiedreportId,
							sessionDataBean);
					byteArr = (byte[]) returnMap.get("fileData");
					response.setContentLength(byteArr.length);
					response.setHeader(Constants.CONTENT_DISPOSITION, "attachment;"
							+ "filename="+sprNumber+"_"+""+returnMap.get("fileName")+".pdf");
					response.setContentType(Constants.CONTENT_TYPE_PDF);
					
			}else if(reportType.equals(Constants.DEIDENTIFIED)){
				    returnMap = pdfUtil.generateDIdentifiedPdf(deIdentifiedreportId,
							sessionDataBean);
				    byteArr = (byte[]) returnMap.get("fileData");
					response.setContentLength(byteArr.length);
					response.setHeader(Constants.CONTENT_DISPOSITION, "attachment;"
							+ "filename="+returnMap.get("fileName")+".pdf");
					response.setContentType(Constants.CONTENT_TYPE_PDF);
					
			} else if (reportType.equals("uploadedFile")) {

				IdentifiedSurgicalPathologyReportBizLogic bizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				String scgId = request.getParameter("reportId");
				List<Object> retList = bizLogic.getFileName(Long.parseLong(scgId),
						sessionDataBean);
				response.setContentType("application/download");
				// Get the directory and iterate them to get file by file...
				//Added " when there was problem when file name is having empty space in it. 
				response.setHeader("Content-Disposition", "attachment;"
						+ "filename=\"" + retList.get(0)+"\"");
				byteArr = (byte[])retList.get(1);
				
			}
			response.getOutputStream().write(byteArr);
			
		} catch (IOException e) {
			response.getWriter().write("<script>alert('"+e.getMessage()+"')</script>");
		} catch (ApplicationException e) {
			response.getWriter().write("<script>alert('"+e.getMessage()+"')</script>");
		} catch (Exception e) {
			response.getWriter().write("<script>alert('"+Constants.DWD_ERROR_MESSAGE+"')</script>");
			
		} finally {
			response.flushBuffer();
		}

		return mapping.findForward(mappingForward);
	}

}