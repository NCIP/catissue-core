package edu.wustl.catissuecore.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.global.CommonServiceLocator;

public class SPPTemplateDownloadAction extends SecureAction
{
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String requestFor = request.getParameter("requestFor");
		if("xsd".equals(requestFor))
		{
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.SPP_XSD_FILENAME);
			sendFileToClient(in, response, Constants.SPP_XSD_FILENAME);
		}
		else if("template".equals(requestFor))
		{
			Long id = Long.valueOf(request.getParameter("id"));
			SPPBizLogic bizLogic = new SPPBizLogic();
			sendFileToClient(bizLogic.getTemplateAsStream(id), response, Constants.SPP_TEMPLATE_FILENAME);
		}
		
		return null;
	}
	
	private void sendFileToClient(InputStream in, HttpServletResponse response, String zipFileName) throws Exception
	{
		String tempDirFilePath = CommonServiceLocator.getInstance().getAppHome();
		File file = new File(tempDirFilePath + "/" + zipFileName); 
		OutputStream out = new FileOutputStream(file);
		byte buff[] = new byte[1024];
		int len;
		while ((len = in.read(buff)) > 0)
		{
			out.write(buff, 0, len);
		}
		out.close();
		in.close();

		if (file.exists())
		{
			response.setContentType("application/download");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ zipFileName + "\";");
			response.setContentLength((int) file.length());

			OutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int count;
			byte buf[] = new byte[4096];
			while ((count = bis.read(buf)) > -1)
			{
				outputStream.write(buf, 0, count);
			}
			outputStream.flush();
			bis.close();
		}
		else
		{
			throw new Exception("An Exception has occured while sending file to client....");
		}
		file.delete();
		

		}
}
