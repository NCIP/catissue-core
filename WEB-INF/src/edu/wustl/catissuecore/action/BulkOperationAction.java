package edu.wustl.catissuecore.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.actionForm.BulkOperationForm;
import edu.wustl.catissuecore.bizlogic.BulkOperationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;

/**
 * This action class receives and forwards all the bulk operations
 * request done from UI.
 * @author sagar_baldwa
 *
 */
public class BulkOperationAction extends SecureAction
{
	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String mappingForward = null;
		try
		{
			if(request.getParameter("report") != null)
			{
				HttpSession session = request.getSession();
				File resultFile = (File)session.getAttribute("resultFile");
				createResponse(response, resultFile);
			}
			BulkOperationForm bulkOperationForm = (BulkOperationForm)form;
			BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
			List<NameValueBean> bulkOperationList =
						bulkOperationBizLogic.getTemplateNameDropDownList();
			if(bulkOperationList != null && !bulkOperationList.isEmpty())
			{
				NameValueBean nameValueBean = bulkOperationList.get(0);
				String initialDropdownValue = nameValueBean.getValue();
				
				request.setAttribute(Constants.BULK_OPEARTION_LIST, bulkOperationList);
				request.setAttribute("dropdownName", initialDropdownValue);
				File file = (File)request.getAttribute("resultFile");
				String dropdownNameFromUI = (String)request.getParameter("dropdownName");
				String dropdownName = bulkOperationForm.getOperationName();
				
				if((dropdownName != null && !"".equals(dropdownName)) ||
						(dropdownNameFromUI != null && !"".equals(dropdownNameFromUI)))
				{
					if(file != null)
					{
						request.setAttribute("resultFile", file);
						ActionMessages messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("bulk.success.message"));
						saveMessages(request, messages);
						
						HttpSession session = request.getSession();
						session.setAttribute("resultFile", file);
						request.setAttribute("dropdownName", bulkOperationForm.getOperationName());
						request.setAttribute(Constants.SUCCESS, Constants.SUCCESS);
						request.setAttribute("report", "report");
						mappingForward = Constants.PAGE_OF_BULK_OPERATION;
						//createResponse(response, file);
					}
					else if(request.getParameter(Constants.PAGE_OF) == null && file == null
							&& (dropdownNameFromUI == null && !"".equals(dropdownNameFromUI)))
					{
						request.setAttribute("dropdownName", bulkOperationForm.getOperationName());
						mappingForward = Constants.PAGE_OF_BULK_OPERATION;
					}
					else
					{
						File csvFile = bulkOperationBizLogic.getCSVFile(dropdownNameFromUI);
						createResponse(response, csvFile);
					}
				}
				else if(request.getParameter(Constants.PAGE_OF) != null)
				{
					mappingForward = request.getParameter(Constants.PAGE_OF); 
				}
			}
			else
			{
				request.setAttribute("noTemplates", "noTemplates");
				mappingForward = Constants.PAGE_OF_BULK_OPERATION;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapping.findForward(mappingForward);
	}

	/**
	 * Ajax method to load a file pop-up on UI.
	 * @param response HttpServletResponse.
	 * @param file File.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private void createResponse(HttpServletResponse response, File file)
		throws BulkOperationException
	{
		try
		{
			response.setContentType("Content-Type");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + file.getName()
					+ "\";");
			final OutputStream outStream = response.getOutputStream();
			InputStream inputStream = new FileInputStream(file);
			int count;
			long length = 0L;
			final byte[] buf = new byte[4096];
			count = inputStream.read(buf);
			while (count > -1)
			{
				length += count;
				count = inputStream.read(buf);
			}
			response.setContentLength((int) length);

			inputStream = new FileInputStream(file);
			count = inputStream.read(buf);
			while (count > -1)
			{
				outStream.write(buf, 0, count);
				count = inputStream.read(buf);
			}
			outStream.flush();
			inputStream.close();
		}
		catch (Exception exception)
		{
			throw new BulkOperationException(exception);
		}
	}	
}