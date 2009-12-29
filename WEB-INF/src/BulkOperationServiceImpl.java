import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.bulkOperations.JobMessage;
import edu.wustl.common.exception.ErrorKey;


/**
 * Bulk operation service.
 * @author kalpana_thakur
 *
 */
public class BulkOperationServiceImpl implements BulkOperationService
{

	/**
	 * URL.
	 */
	private String url;
	/**
	 * Application User name.
	 */
	private String applicationUserName;
	/**
	 * Application User password.
	 */
	private String applicationUserPassword;
	/**
	 * Key store location.
	 */
	private String keyStoreLocation;
	/**
	 * template file.
	 */
	public static final String TEMPLATE_FILE= "templateFile";

	/**
	 * CSV file.
	 */
	public static final String CSV_FILE = "csvFile";
	/**
	 * Login name.
	 */
	public static final String USER_NAME= "loginName";
	/**
	 * Password.
	 */
	public static final String PASSWORD = "password";

	/**
	 * Operation.
	 */
	public static final String OPERATION = "operation";

	/**
	 * Set to true if logged in.
	 */
	private boolean isLoggedIn = false;

	/**
	 * @return the isLoggedIn
	 */
	public boolean isLoggedIn()
	{
		return isLoggedIn;
	}

	/**
	 * @param isLoggedIn the isLoggedIn to set
	 */
	public void setLoggedIn(boolean isLoggedIn)
	{
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * Client.
	 */
	private HttpClient client = new HttpClient();

	/**
	 * @return the URL
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return the applicationUserName
	 */
	public String getApplicationUserName()
	{
		return applicationUserName;
	}

	/**
	 * @return the applicationUserPassword
	 */
	public String getApplicationUserPassword()
	{
		return applicationUserPassword;
	}

	/**
	 * @return the keyStoreLocation
	 */
	public String getKeyStoreLocation()
	{
		return keyStoreLocation;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @param applicationUserName the applicationUserName to set
	 */
	public void setApplicationUserName(String applicationUserName)
	{
		this.applicationUserName = applicationUserName;
	}

	/**
	 * @param applicationUserPassword the applicationUserPassword to set
	 */
	public void setApplicationUserPassword(String applicationUserPassword)
	{
		this.applicationUserPassword = applicationUserPassword;
	}

	/**
	 * @param keyStoreLocation the keyStoreLocation to set
	 */
	public void setKeyStoreLocation(String keyStoreLocation)
	{
		this.keyStoreLocation = keyStoreLocation;
	}


	/**
	 * This will be called to set application details.
	 * @param url URL.
	 * @param applicationUserName application user name.
	 * @param applicationUserPassword application user password.
	 * @param keyStoreLocation key store location.
	 */
	private void setApplicationDetails(String url,String applicationUserName,
			String applicationUserPassword,String keyStoreLocation)
	{
		this.url = url;
		this.applicationUserName = applicationUserName;
		this.applicationUserPassword = applicationUserPassword;
		this.keyStoreLocation = keyStoreLocation;
	}
	/**
	 * This method will be called to get Job details.
	 * @param jobId jobId
	 * @return Job Message
	 * @throws BulkOperationException BulkOperationException
	 */
	public JobMessage getJobDetails(Long jobId) throws BulkOperationException
	{
		if(!isLoggedIn)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.client.login.error");
			throw new BulkOperationException(errorkey,null,"");
		}
		JobMessage jobMessage = null;
		PostMethod postMethod = new PostMethod(url+"/BulkHandler.do");
		try
		{
			postMethod.addParameter("jobId", jobId.toString());
			postMethod.addParameter("fromAction", "jobDetails");
			client.executeMethod(postMethod);
			InputStream inputStream =  (InputStream)postMethod.getResponseBodyAsStream();
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Object object = ois.readObject();
		//	Object object1 = ois.readObject();
			jobMessage = (JobMessage)object;
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.client.error");
			throw new BulkOperationException(errorkey,exp,
					url+":"+applicationUserName+":"+applicationUserPassword);
		}
		finally
		{
			postMethod.releaseConnection();
		}
		return jobMessage;
	}

	/**
	 * This method will be called to logged in to the application.
	 * @param url URL
	 * @param applicationUserName application user name.
	 * @param applicationUserPassword application user password.
	 * @param keyStoreLocation key store location.
	 * @return Job Message.
	 * @throws BulkOperationException BulkOperationException
	 */
	public JobMessage login(String url,String applicationUserName,
			String applicationUserPassword,String keyStoreLocation) throws BulkOperationException
	{
		setApplicationDetails(url,applicationUserName,
				applicationUserPassword,keyStoreLocation);
		JobMessage jobMessage = null;
		PostMethod postMethod = new PostMethod(url+"/BulkLogin.do");
		try
		{

			if(keyStoreLocation!=null)
			{
				System.setProperty("javax.net.ssl.trustStore", keyStoreLocation);
			}
			postMethod.addParameter(USER_NAME, applicationUserName);
			postMethod.addParameter(PASSWORD,applicationUserPassword);
			client.executeMethod(postMethod);
			InputStream inputStream =  (InputStream)postMethod.getResponseBodyAsStream();
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Object object = ois.readObject();
			jobMessage = (JobMessage)object;
			isLoggedIn = true;
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.client.error");
			throw new BulkOperationException(errorkey,exp,
					url+":"+applicationUserName+":"+applicationUserPassword);
		}
		finally
		{
			postMethod.releaseConnection();
		}
		return jobMessage;
	}

	/**
	 * This method will be called to start the bulk operation.
	 * @param operationName operation name.
	 * @param csvFile CSV file.
	 * @param xmlTemplateFile XML template file.
	 * @return Job Message.
	 * @throws BulkOperationException BulkOperationException
	 */
	public JobMessage startbulkOperation(String operationName, File csvFile,
			File xmlTemplateFile) throws BulkOperationException
	{
		if(!isLoggedIn)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.client.login.error");
			throw new BulkOperationException(errorkey,null,"");
		}
		JobMessage jobMessage = null;
		PostMethod postMethod = new PostMethod(url+"/BulkHandler.do");
		try
		{
			Part[] parts = {
					new StringPart(OPERATION,operationName),
					new FilePart(TEMPLATE_FILE, xmlTemplateFile),
					new FilePart(CSV_FILE, csvFile)
			};
			postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
			client.executeMethod(postMethod);

			InputStream in =  (InputStream)postMethod.getResponseBodyAsStream();
			ObjectInputStream oist = new ObjectInputStream(in);
			Object object = oist.readObject();
			jobMessage = (JobMessage)object;

		}
		catch (FileNotFoundException exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.file.not.found");
			throw new BulkOperationException(errorkey,exp,xmlTemplateFile+":"+csvFile);
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.client.error");
			throw new BulkOperationException(errorkey,exp,
					url+":"+applicationUserName+":"+applicationUserPassword);
		}
		finally
		{
			postMethod.releaseConnection();
		}
		return jobMessage;
	}

}
