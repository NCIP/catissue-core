package edu.wustl.catissuecore.action.bulkOperations;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.xml.sax.InputSource;


import edu.wustl.bulkoperator.BulkOperator;
import edu.wustl.bulkoperator.DataList;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.LoginAction;
import edu.wustl.catissuecore.actionForm.BulkOperationForm;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;


public class BulkOperationCommandAction extends Action
{
	private static Logger logger = Logger.getCommonLogger(BulkOperationCommandAction.class);
	@Override
	public ActionForward execute(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		System.out.println("Sachin in server");
		try
		{
			Hashtable<String,Object> requestParam = getAndValidateRequestParameters(request);
			String userName = (String)requestParam.get("userName");
			String password = (String)requestParam.get("password");
			Boolean successFullLogin  = new CaCoreAppServicesDelegator().delegateLogin(userName, password);
			if(successFullLogin)
			{
				HttpSession session = request.getSession();
				final User validUser = AppUtility.getUser(userName);
				final SessionDataBean sessionData = setSessionDataBean(validUser, "", true);
				logger.debug("CSM USer ID ....................... : " + validUser.getCsmUserId());
				session.setAttribute(Constants.SESSION_DATA, sessionData);
				session.setAttribute(Constants.USER_ROLE, validUser.getRoleId());
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * This method will createSessionDataBeanObject.
	 * @param validUser existing user
	 * @param ipAddress IP address of the system
	 * @param adminUser true/false
	 * @return sessionData
	 */
	private SessionDataBean setSessionDataBean(final User validUser, final String ipAddress,
			boolean adminUser)
	{
		final SessionDataBean sessionData = new SessionDataBean();
		sessionData.setAdmin(adminUser);
		sessionData.setUserName(validUser.getLoginName());
		sessionData.setIpAddress(ipAddress);
		sessionData.setUserId(validUser.getId());
		sessionData.setFirstName(validUser.getFirstName());
		sessionData.setLastName(validUser.getLastName());
		sessionData.setCsmUserId(validUser.getCsmUserId().toString());
		return sessionData;
	}
	/**
	 * @param request
	 * @param operationName
	 * @param list
	 * @param dataList
	 * @throws BulkOperationException
	 * @throws Exception
	 */
	private void readCsvAndStartBulkOperation(String userName,String operationName,
			InputSource templateInputSource, InputSource csvInputSource,SessionDataBean sessionDataBean) throws BulkOperationException, Exception
	{
		String mappingFilePath = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "mapping.xml";
		InputSource mappingFileInputSource = new InputSource(mappingFilePath);

		BulkOperator bulkOperator = new BulkOperator(templateInputSource, mappingFileInputSource);
		BulkOperationMetaData metaData = bulkOperator.getMetadata();
		if (metaData != null && !metaData.getBulkOperationClass().isEmpty())
		{
//			validateAndStartBulkOperation(operationName, list, dataList, sessionDataBean.getUserName(), sessionDataBean.getUserId(),
//					bulkOperator, metaData);
		}
		else
		{
			throw new BulkOperationException("bulk.error.bulk.metadata.xml.file");
		}
	}
	private Hashtable<String,Object> getAndValidateRequestParameters(HttpServletRequest request) throws Exception
	{
		Hashtable<String,Object> requestParam = new Hashtable<String, Object>();
		try
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(CommonServiceLocator.getInstance().getAppHome()));
			ServletFileUpload fu = new ServletFileUpload(factory);
		    // If file size exceeds, a FileUploadException will be thrown
		    fu.setSizeMax(1000000);
		    List<FileItem> fileItems = fu.parseRequest(request);
		    Iterator<FileItem> itr = fileItems.iterator();
		    while(itr.hasNext()) 
		    {
			      FileItem fi = itr.next();
			      new BulkOperationForm().setFile((FormFile)fi);
			      //Check if not form field so as to only handle the file inputs
			      //else condition handles the submit button input
			      if(!fi.isFormField()) 
			      {
			    	 requestParam.put(fi.getFieldName(), fi.getInputStream());
			    	 System.out.println("Field ="+fi.getFieldName());
			      }
			      else 
			      {
			    	  requestParam.put(fi.getFieldName(), fi.getString());
			    	  System.out.println("Field ="+fi.getFieldName());
			          System.out.println("Value ="+fi.getString());
			      }
			 }
		    boolean isValidationFail=false;
		    if(requestParam.keySet().size()<4)
		    {
		    	
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(),e);
		}
		
		return requestParam;
	}
}
