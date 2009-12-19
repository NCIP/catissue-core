
package edu.wustl.catissuecore.action;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.xml.sax.InputSource;

import edu.wustl.bulkoperator.BulkOperator;
import edu.wustl.bulkoperator.DataList;
import edu.wustl.bulkoperator.DataReader;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.bulkoperator.validator.TemplateValidator;
import edu.wustl.catissuecore.actionForm.BulkOperationForm;
import edu.wustl.catissuecore.bizlogic.BulkOperationBizLogic;
import edu.wustl.catissuecore.client.BulkOperatorJob;
import edu.wustl.catissuecore.client.CaTissueAppServiceImpl;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.jobmanager.DefaultJobStatusListner;
import edu.wustl.common.jobmanager.JobManager;
import edu.wustl.common.jobmanager.JobStatusListener;
import edu.wustl.common.util.global.CommonServiceLocator;
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
		String operationName = bulkOperationForm.getOperationName();
		try
		{
			BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
			List<String> list = null;
			try
			{
				list = bulkOperationBizLogic.getOperationNameAndXml(operationName);
			}
			catch (Exception exp)
			{
				throw new BulkOperationException("bulk.error.database.operation."
						+ "reading.operation.name.xml.template");
			}
			if (!list.isEmpty())
			{
				Properties properties = new Properties();
				FormFile file = bulkOperationForm.getFile();
				properties.put("inputStream", file.getInputStream());
				DataList dataList = DataReader.getNewDataReaderInstance(properties).readData();
				if (dataList != null)
				{
					readCsvAndStartBulkOperation(request, list.get(0), list, dataList);
				}
				else
				{
					throw new BulkOperationException("bulk.error.reading.csv.file");
				}
			}
			else
			{
				throw new BulkOperationException("bulk.error.incorrect.operation.name");
			}
		}
		catch (Exception exp)
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(exp.getMessage()));
			}
			this.saveErrors(request, errors);
			logger.error(exp.getMessage(), exp);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param request
	 * @param operationName
	 * @param list
	 * @param dataList
	 * @throws BulkOperationException
	 * @throws Exception
	 */
	private void readCsvAndStartBulkOperation(HttpServletRequest request, String operationName,
			List<String> list, DataList dataList) throws BulkOperationException, Exception
	{
		InputSource templateInputSource = new InputSource(new StringReader(list.get(1)));

		String mappingFilePath = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "mapping.xml";
		InputSource mappingFileInputSource = new InputSource(mappingFilePath);

		String loginName = this.getSessionData(request).getUserName();
		long userId = this.getSessionData(request).getUserId();

		BulkOperator bulkOperator = new BulkOperator(templateInputSource, mappingFileInputSource);
		BulkOperationMetaData metaData = bulkOperator.getMetadata();
		if (metaData != null && !metaData.getBulkOperationClass().isEmpty())
		{
			validateAndStartBulkOperation(operationName, list, dataList, loginName, userId,
					bulkOperator, metaData);
		}
		else
		{
			throw new BulkOperationException("bulk.error.bulk.metadata.xml.file");
		}
	}

	/**
	 * @param operationName
	 * @param list
	 * @param dataList
	 * @param loginName
	 * @param userId
	 * @param bulkOperator
	 * @param metaData
	 * @throws Exception
	 * @throws BulkOperationException
	 */
	private void validateAndStartBulkOperation(String operationName, List<String> list,
			DataList dataList, String loginName, long userId, BulkOperator bulkOperator,
			BulkOperationMetaData metaData) throws Exception, BulkOperationException
	{
		BulkOperationClass bulkOperationClass = metaData.getBulkOperationClass().iterator().next();
		TemplateValidator templateValidator = new TemplateValidator();
		Set<String> errorList = templateValidator.validateXmlAndCsv(bulkOperationClass,
				operationName, dataList);
		if (errorList.isEmpty())
		{
			JobStatusListener jobStatusListner = new DefaultJobStatusListner();
			BulkOperatorJob bulkOperatorJob = new BulkOperatorJob(list.get(0), loginName, null,
					String.valueOf(userId), bulkOperator, dataList, CaTissueAppServiceImpl.class
							.getName(), jobStatusListner);
			JobManager.getInstance().addJob(bulkOperatorJob);
		}
		else
		{
			throw new BulkOperationException("bulk.error.csv.column.name.change");
		}
	}
}