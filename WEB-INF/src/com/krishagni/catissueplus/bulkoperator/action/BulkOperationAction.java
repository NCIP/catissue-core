/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.krishagni.catissueplus.bulkoperator.actionForm.BulkOperationForm;
import com.krishagni.catissueplus.bulkoperator.bizlogic.BulkOperationBizLogic;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This action class receives and forwards all the bulk operations
 * request done from UI.
 * @author sagar_baldwa
 *
 */
public class BulkOperationAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(BulkOperationAction.class);

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
			if (request.getParameter("report") != null)
			{
				HttpSession session = request.getSession();
				File resultFile = (File) session.getAttribute("resultFile");
				createResponse(response, resultFile);
			}
			//for reading the jobGrid.refresh.time from the bulkOperation.properties file
			String filePath=CommonServiceLocator.getInstance().getPropDirPath()+ File.separator + "bulkOperation.properties";
			Properties properties = BulkOperationUtility.getPropertiesFile(filePath);
			String gridRefreshTime=properties.getProperty("jobGrid.refresh.timeInterval");

			request.setAttribute("gridRefreshTime", gridRefreshTime);

			BulkOperationForm bulkOperationForm = (BulkOperationForm) form;
			BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
			List<NameValueBean> bulkOperationList = bulkOperationBizLogic
					.getTemplateNameDropDownList();
			if (bulkOperationList != null && !bulkOperationList.isEmpty())
			{
				NameValueBean nameValueBean = bulkOperationList.get(0);
				String initialDropdownValue = nameValueBean.getValue();

				request.setAttribute(BulkOperationConstants.BULK_OPERATION_LIST, bulkOperationList);
				request.setAttribute("dropdownName", initialDropdownValue);
				File file = (File) request.getAttribute("resultFile");
				String dropdownNameFromUI = (String) request.getParameter("dropdownName");
				String dropdownName = bulkOperationForm.getDropdownName();

				if ((dropdownName != null && !"".equals(dropdownName))
						|| (dropdownNameFromUI != null && !"".equals(dropdownNameFromUI)))
				{
					if (file != null)
					{
						request.setAttribute("resultFile", file);
						ActionMessages messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"bulk.success.message"));
						saveMessages(request, messages);

						HttpSession session = request.getSession();
						session.setAttribute("resultFile", file);
						request.setAttribute("dropdownName", bulkOperationForm.getDropdownName());
						request.setAttribute(Constants.SUCCESS, Constants.SUCCESS);
						request.setAttribute("report", "report");
						mappingForward = BulkOperationConstants.PAGE_OF_BULK_OPERATION;
					}
					else if (request.getParameter(BulkOperationConstants.PAGE_OF) == null && file == null
							&& (dropdownNameFromUI == null && !"".equals(dropdownNameFromUI)))
					{
						request.setAttribute("dropdownName", bulkOperationForm.getDropdownName());
						mappingForward = BulkOperationConstants.PAGE_OF_BULK_OPERATION;
					}
					else
					{
						File downloadFile=null;
						if("downloadXSD".equals(request.getAttribute("operation")))
						{
							downloadFile = new File(CommonServiceLocator.getInstance().getPropDirPath()
							+ File.separator + "BulkOperations.xsd");
						}
						else
						{
							downloadFile = bulkOperationBizLogic.getCSVFile(dropdownNameFromUI);
						}
						createResponse(response, downloadFile);
					}
				}
				else if (request.getParameter(BulkOperationConstants.PAGE_OF) != null)
				{
					mappingForward = request.getParameter(BulkOperationConstants.PAGE_OF);
				}
			}
			else
			{
				request.setAttribute("noTemplates", "noTemplates");
				mappingForward = BulkOperationConstants.PAGE_OF_BULK_OPERATION;
			}
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
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
			response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName()
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
		catch (Exception exp)
		{
			logger.error(exp);
			ErrorKey errorKey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorKey, exp, exp.getMessage());
		}
	}
}