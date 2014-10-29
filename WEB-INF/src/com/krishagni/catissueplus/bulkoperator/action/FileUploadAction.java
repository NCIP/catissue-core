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

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.xml.sax.InputSource;

import com.krishagni.catissueplus.bulkoperator.actionForm.BulkOperationForm;
import com.krishagni.catissueplus.bulkoperator.bizlogic.BulkOperationBizLogic;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This class uploads the CSV file of bulk operations and returns a new CSV file
 * with the results in it.
 * @author sagar_baldwa
 *
 */
public class FileUploadAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(FileUploadAction.class);

	/**
	 * Execute Secure Action.
	 * @return ActionForward ActionForward.
	 * @param mapping ActionMapping.
	 * @param form ActionForm.
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	 * @throws Exception Exception.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		BulkOperationForm bulkOperationForm = (BulkOperationForm) form;
		String dropDownName = bulkOperationForm.getDropdownName();
		String operationName = bulkOperationForm.getOperationName();
		logger.info("Operation Name: " + operationName);
		logger.info("DropDown Name: " + dropDownName);
		String retrievedOperationName = null;
		String forward = BulkOperationConstants.SUCCESS;
		try
		{
			if(request.getParameter(BulkOperationConstants.PAGE_OF) != null &&
					!"".equals(request.getParameter(BulkOperationConstants.PAGE_OF)))
			{
				forward = request.getParameter(BulkOperationConstants.PAGE_OF);
			}
			BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
			if(bulkOperationForm.getCsvFile() == null)
			{
				ErrorKey errorKey = ErrorKey.getErrorKey("bulk.operation.request.param.error");
				throw new BulkOperationException(errorKey, null, "Error in loading the CSV file on server");
			}
			InputStream csvFileInputStream = bulkOperationForm.getCsvFile().getInputStream();
			InputStream csvFileInputStreamForValidation = bulkOperationForm.getCsvFile().getInputStream();
			InputSource xmlTemplateInputSource = null;
			logger.info("bulkOperationForm.getXmlTemplateFile() : " + bulkOperationForm.getXmlTemplateFile());
			if(bulkOperationForm.getXmlTemplateFile()==null ||
					bulkOperationForm.getXmlTemplateFile().toString().equalsIgnoreCase("noname"))
			{
				try
				{
					List<String> list = bulkOperationBizLogic.getOperationNameAndXml
								(dropDownName, operationName);
					if (list.isEmpty())
					{
						ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.incorrect.operation.name");
						throw new BulkOperationException(errorkey, null, "");
					}
					retrievedOperationName = list.get(0);
					xmlTemplateInputSource = new InputSource(new StringReader(list.get(1)));
					logger.info("xmlTemplateInputSource : "+xmlTemplateInputSource);
				}
				catch (BulkOperationException exp)
				{
					logger.error(exp.getMessage(), exp);
					throw new BulkOperationException(exp.getErrorKey(), exp, exp.getMsgValues());
				}
			}
			else
			{
				retrievedOperationName = bulkOperationForm.getOperationName();
				String xmlTemplateString = new String(bulkOperationForm.getXmlTemplateFile().getFileData());
				xmlTemplateInputSource= new InputSource(new StringReader(xmlTemplateString));
			}
			Long jobId = bulkOperationBizLogic.initBulkOperation(csvFileInputStream, 
					csvFileInputStreamForValidation, xmlTemplateInputSource, 
					retrievedOperationName, this.getSessionData(request));
			final ActionMessages msg = new ActionMessages();
			final ActionMessage msgs = new ActionMessage("job.submitted");
			msg.add(ActionErrors.GLOBAL_MESSAGE, msgs);
			if (!msg.isEmpty())
			{
				saveMessages(request, msg);
			}
			request.setAttribute("jobId", jobId.toString());
		}
		catch (BulkOperationException exp)
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				errors = new ActionErrors();
				if(dropDownName == null || "".equals(dropDownName))
				{
					errors.add(ActionErrors.GLOBAL_ERROR,
					 new ActionError("errors.item",exp.getMessage()));

				}
				else
				{
					if(exp.getErrorKeyName().equals("bulk.operation.issues")
						||exp.getErrorKeyName().equals(
								"bulk.error.csv.column.name.change.validation"))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item",
								ApplicationProperties.getValue("bulk.error.csv.column.name.change")));
					}
					else
					{
						errors.add(ActionErrors.GLOBAL_ERROR,
								new ActionError("errors.item",exp.getMessage()));
					}					
				}
			}
			this.saveErrors(request, errors);
			logger.error(exp.getMessage(), exp);
		}
		return mapping.findForward(forward);
	}	
}