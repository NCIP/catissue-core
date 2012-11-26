
package edu.wustl.catissuecore.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.com.bytecode.opencsv.CSVWriter;

import edu.wustl.catissuecore.bizlogic.ExportCollectionProtocolBizLogic;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.global.CommonServiceLocator;
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
	private transient final Logger logger = Logger.getCommonLogger(ExportCollectionProtocolAction.class);

	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String mappingForward = null;
		try 
		{
			
			ExportCollectionProtocolBizLogic exportCP=new ExportCollectionProtocolBizLogic();
			StringBuffer downloadFile = exportCP.getCPXMLFile(request.getParameter("title"));
			
			String[] headerNValue=downloadFile.toString().split("\n");
			 
			File file = new File(CommonServiceLocator.getInstance().getAppHome() + "/" +request.getParameter("title")+".csv");
			
			CSVWriter csvWriter=new CSVWriter(new FileWriter(CommonServiceLocator.getInstance().getAppHome() + "/" +request.getParameter("title")+".csv"));
			csvWriter.writeNext(headerNValue[0].split(","));
			csvWriter.writeNext(headerNValue[1].split(","));
			csvWriter.flush();
			response.setContentType("application/download");
            response.setHeader("Content-Disposition", "attachment;filename=\""+request.getParameter("title")+".csv\"");
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
			file.delete();
            
		} catch (Exception exp) {
			logger.error(exp.getMessage(), exp);
		}
		return mapping.findForward(mappingForward);
	}
}