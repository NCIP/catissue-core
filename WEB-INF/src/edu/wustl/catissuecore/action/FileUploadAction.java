
package edu.wustl.catissuecore.action;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
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
import org.xml.sax.InputSource;

import edu.wustl.bulkoperator.BulkOperator;
import edu.wustl.bulkoperator.DataList;
import edu.wustl.bulkoperator.DataReader;
import edu.wustl.bulkoperator.jobmanager.DefaultJobStatusListner;
import edu.wustl.bulkoperator.jobmanager.JobManager;
import edu.wustl.bulkoperator.jobmanager.JobStatusListener;
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
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
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
		String dropDownName = bulkOperationForm.getOperationName();
		String operationName = null;
		String forward = Constants.SUCCESS;
		try
		{
			BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
			InputStream csvFileInputStream = bulkOperationForm.getCsvFile().getInputStream();
			InputSource xmlTemplateInputSource= null;
			
			if(bulkOperationForm.getXmlTemplateFile()==null)
			{
				try
				{
					List<String> list = bulkOperationBizLogic.getOperationNameAndXml(dropDownName);
					if (list.isEmpty())
					{
						throw new BulkOperationException("bulk.error.incorrect.operation.name");
					}
					operationName = list.get(0);
					xmlTemplateInputSource = new InputSource(new StringReader(list.get(1)));
				}
				catch (Exception exp)
				{
					throw new BulkOperationException("bulk.error.database.operation."
							+ "reading.operation.name.xml.template");
				}
			}
			else
			{
				forward = request.getParameter(Constants.PAGE_OF);
				operationName = bulkOperationForm.getOperationName();
			
				String s = new String(bulkOperationForm.getXmlTemplateFile().getFileData());
				
				xmlTemplateInputSource= new InputSource(new StringReader(s));
				
			}
			Properties properties = new Properties();
			properties.put("inputStream", csvFileInputStream);
			DataList dataList = DataReader.getNewDataReaderInstance(properties).readData();
			if (dataList != null)
			{
				BulkOperator bulkOperator = readCsvAndgetBulkOperator(operationName, xmlTemplateInputSource, dataList);
				validateBulkOperation(operationName,dataList,bulkOperator);
				SessionDataBean sessionDataBean = this.getSessionData(request);
				Long jobId = startBulkOperation(operationName, dataList, sessionDataBean.getUserName(), sessionDataBean.getUserId(), bulkOperator);
				request.setAttribute("jobId", jobId.toString());
			}
			else
			{
				throw new BulkOperationException("bulk.error.reading.csv.file");
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
		return mapping.findForward(forward);
	}

	/**
	 * @param request
	 * @param operationName
	 * @param list
	 * @param dataList
	 * @throws BulkOperationException
	 * @throws Exception
	 */
	private BulkOperator readCsvAndgetBulkOperator(String operationName,
			InputSource templateInputSource, DataList dataList) throws BulkOperationException, Exception
	{
		String mappingFilePath = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "mapping.xml";
		InputSource mappingFileInputSource = new InputSource(mappingFilePath);
		BulkOperator bulkOperator = new BulkOperator(templateInputSource, mappingFileInputSource);
		return bulkOperator;
	}
	
	private void validateBulkOperation(String operationName,DataList dataList, BulkOperator bulkOperator) 
	throws Exception, BulkOperationException
	{
		BulkOperationMetaData metaData = bulkOperator.getMetadata();
		if (metaData == null && metaData.getBulkOperationClass().isEmpty())
		{
			throw new BulkOperationException("bulk.error.bulk.metadata.xml.file");
		}
		BulkOperationClass bulkOperationClass = metaData.getBulkOperationClass().iterator().next();
		TemplateValidator templateValidator = new TemplateValidator();
		Set<String> errorList = templateValidator.validateXmlAndCsv(bulkOperationClass,
				operationName, dataList);
		if (!errorList.isEmpty())
		{
			StringBuffer strBuffer = new StringBuffer();
			Iterator<String> errorIterator = errorList.iterator();
			while(errorIterator.hasNext())
			{
				strBuffer.append(errorIterator.next());
			}
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.errors");
			throw new BulkOperationException(errorkey,null,strBuffer.toString());
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
	private Long startBulkOperation(String operationName, DataList dataList, 
			String loginName, long userId, BulkOperator bulkOperator) throws Exception, BulkOperationException
	{
		JobStatusListener jobStatusListner = new DefaultJobStatusListner();
		BulkOperatorJob bulkOperatorJob = new BulkOperatorJob(operationName, loginName, null,
				String.valueOf(userId), bulkOperator, dataList, CaTissueAppServiceImpl.class
						.getName(), jobStatusListner);
		JobManager.getInstance().addJob(bulkOperatorJob);
		while(bulkOperatorJob.getJobData() == null)
		{
			logger.debug("Job not started yet !!!");
		}
		return bulkOperatorJob.getJobData().getJobID();
	}
}