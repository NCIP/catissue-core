package edu.wustl.catissuecore.action.bulkOperations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import edu.wustl.bulkoperator.jobmanager.JobDetails;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.actionForm.BulkOperationForm;
import edu.wustl.catissuecore.bizlogic.BulkOperationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Bulk Handler.
 * @author kalpana_thakur
 */
public class BulkHandler extends Action
{
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(BulkHandler.class);
	/**
	 * Status message.
	 */
	private  String STATUS_MESSAGE = " Message: ";
	/**
	 * Bulk log.
	 */
	private  StringBuffer BULK_LOG;

	/**
	 * Login action.
	 */
	public static final String LOGIN_ACTION = "login";
	/**
	 * FileUpload action.
	 */
	public static final String FILEUPLOAD_ACTION = "fileUpload";
	/**
	 * Jobdetails action to return Jobdetails object.
	 */
	public static final String JOBDETAILS_ACTION = "jobDetails";
	/**
	 * Action from where request is coming.
	 */
	public static final String FROM_ACTION = "fromAction";
	/**
	 * Failed status message.
	 */
	public static final String FAILED = " Failed ";
	/**
	 * Success status message.
	 */
	public static final String SUCCESS = " Success ";

	/**
	 * Action enum.
	 * @author kalpana_thakur
	 *
	 */
	private enum fromAction
	{
		LOGIN
		{
		public String getActionName()
		{
			return LOGIN_ACTION;
		}}
		, FILEUPLOAD
		{
		 public String getActionName()
		{
			return FILEUPLOAD_ACTION;
		}}
		, JOBDETAILS
		{
		 public String getActionName()
		{
			return JOBDETAILS_ACTION;
		}};
		public String getActionName(){return "Not a valid Action";};
	}


	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		BULK_LOG = new StringBuffer(" ");

		try
		{
			JobMessage jobMessage = null;
			if(request.getParameter(FROM_ACTION) != null &&
				request.getParameter(FROM_ACTION).equals(fromAction.LOGIN.getActionName()))
			{
				jobMessage = getJobMessage(request, response,fromAction.LOGIN);

			}
			else if(request.getParameter(FROM_ACTION) != null
				&& request.getParameter(FROM_ACTION).equals(fromAction.FILEUPLOAD.getActionName()))
			{
				jobMessage = getJobMessage(request, response,fromAction.FILEUPLOAD);
			}
			else if(request.getParameter(FROM_ACTION) != null
				&& request.getParameter(FROM_ACTION).equals(fromAction.JOBDETAILS.getActionName()))
			{
				String jobId = request.getParameter("jobId");
				if(!Validator.isEmpty(jobId))
				{
						JobDetails details = new BulkOperationBizLogic().getJobDetails(jobId);
						jobMessage = getJobMessage(request, response,fromAction.JOBDETAILS);
						jobMessage.setJobData(details);
				}
			}
			else
			{
				BulkOperationForm bulkOperationForm = new BulkOperationForm();
				request.setAttribute("bulkOperationForm", bulkOperationForm);
				getRequestParameters(request,bulkOperationForm);
				form = bulkOperationForm;
				return mapping.findForward(Constants.SUCCESS);
			}
			writeJobMessage(jobMessage, response);
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
			getJobMessage(request, response,null);
		}
		return null;
	}

	/**
	 * This method will be called to get appropriate Job message.
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	 * @param fromAction Action from where request is coming.
	 * @throws IOException Exception.
	 * @return JobMessage.
	 */
	private JobMessage getJobMessage(HttpServletRequest request,
			HttpServletResponse response,fromAction fromAction)
	throws IOException
	{
		JobMessage jobMessage = new JobMessage();
		if(fromAction != null)
		{
			jobMessage.setOperationCalled(fromAction.getActionName());
		}

		if(request.getAttribute("org.apache.struts.action.ERROR")!=null)
		{
			jobMessage.setOperationSuccessfull(false);
			ActionErrors errors = (ActionErrors)request.getAttribute("org.apache.struts.action.ERROR");
			Iterator<ActionError> it = errors.get();
			while(it.hasNext())
			{
				ActionError message = (ActionError)it.next();
				jobMessage.addMessage(message.getKey());
			}
			BULK_LOG.append(jobMessage.getOperationCalled()).append(FAILED);
			jobMessage.addMessage(BULK_LOG.toString());
		}
		else
		{
			BULK_LOG.append("\n").append(STATUS_MESSAGE).
			append(":").append(jobMessage.getOperationCalled()).append(SUCCESS);
			jobMessage.setOperationSuccessfull(true);
			if(fromAction != null && fromAction.equals(fromAction.FILEUPLOAD))
			{
				BULK_LOG.append("\n").append(STATUS_MESSAGE)
				.append(" Job Id :"+request.getAttribute("jobId"));
				jobMessage.setJobId(Long.valueOf(request.getAttribute("jobId").toString()));

			}
			jobMessage.addMessage(BULK_LOG.toString());
		}
		return jobMessage;
	}

	/**
	 * This method call to write Job message.
	 * @param jobMessage jobMessage
	 * @param response HttpServletResponse
	 * @throws IOException IOException
	 * @throws SQLException SQLException
	 */
	private void writeJobMessage(JobMessage jobMessage,HttpServletResponse response)
	throws IOException, SQLException
	{
		OutputStream ost = response.getOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(ost);

		if(jobMessage.getJobData() != null)
		{
			Blob blob = (jobMessage.getJobData()).getLogFile();
			InputStream inputStream = blob.getBinaryStream();
			int count = 0;

			final byte[] buf = new byte[Long.valueOf(blob.length()).intValue()];
			while (count > -1)
			{
				count = inputStream.read(buf);
			}
			(jobMessage.getJobData()).setLogFilesBytes(buf);
		}
		os.writeObject(jobMessage);

	}
	/**
	 * This method will be called to get request parameters.
	 * @param request HttpServletRequest.
	 * @param bulkOperationForm form.
	 * @throws BulkOperationException Exception.
	 */
	private void getRequestParameters(HttpServletRequest request
			,BulkOperationForm bulkOperationForm) throws BulkOperationException
	{
		try
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(CommonServiceLocator.getInstance().getAppHome()));
			ServletFileUpload fu = new ServletFileUpload(factory);

		    // If file size exceeds, a FileUploadException will be thrown
		    fu.setSizeMax(1000000);
		    List<FileItem> fileItems;
				fileItems = fu.parseRequest(request);
		    Iterator<FileItem> itr = fileItems.iterator();
		    while(itr.hasNext())
		    {
			      FileItem fi = itr.next();
			      //Check if not form field so as to only handle the file inputs
			      //else condition handles the submit button input
			      if(!fi.isFormField())
			      {
			    	 if("csvFile".equals(fi.getFieldName()))
			    	 {
			    		 bulkOperationForm.setCsvFile(getFormFile(fi));
			    	 }
			    	 else
			    	 {
			    		 bulkOperationForm.setXmlTemplateFile(getFormFile(fi));
			    	 }
			    	 logger.info("Field ="+fi.getFieldName());
			      }
			      else
			      {
			    	  if("operation".equals(fi.getFieldName()))
			    	  {
			    		  bulkOperationForm.setOperationName(fi.getString());
			    	  }

			      }
			 }
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.request.param.error");
			throw new BulkOperationException(errorkey,exp,"");
		}
	}
	/**
	 * This method will be called to get fileItem.
	 * @param fileItem fileItem
	 * @return FormFile FormFile
	 * @throws BulkOperationException BulkOperationException
	 */
	public FormFile getFormFile(FileItem fileItem)
	throws BulkOperationException
	{
		try
		{
			Class parentClass = Class.forName
			("org.apache.struts.upload.CommonsMultipartRequestHandler");

			Class childClass = parentClass .getDeclaredClasses()[0];
			Constructor c = childClass .getConstructors()[0];
			c.setAccessible(true);
			return (FormFile)c.newInstance(new Object[] {fileItem});
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.request.param.error");
			throw new BulkOperationException(errorkey,exp,"");
		}
	}
}
