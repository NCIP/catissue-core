import java.io.File;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;



public class BulkOperationCommand
{
	private String operationName;
	private File csvFile;
	private File templateFile;
	private String url;
	private String applicationUserName;
	private String applicationUserPassword;
	private String keyStoreLocation;
	public static final String TEMPLATE_FILE= "templateFile";
	public static final String CSV_FILE = "csvFile";
	public static final String USER_NAME= "userName";
	public static final String PASSWORD = "password";
	
	public static void main(String[] args)
	{
		try
		{
			
			BulkOperationCommand bulkOperationCommand = new BulkOperationCommand();
			if(bulkOperationCommand.keyStoreLocation!=null)
			{
				System.setProperty("javax.net.ssl.trustStore", bulkOperationCommand.keyStoreLocation);
			}	
			
			bulkOperationCommand.csvFile = new File("BulkOperations/editSite.csv");
			bulkOperationCommand.templateFile = new File("BulkOperations/editSite.xml");
			bulkOperationCommand.url = "http://localhost:8080/catissuecore/BulkOperationCommand.do";
			bulkOperationCommand.applicationUserName = "admin@admin.com";
			bulkOperationCommand.applicationUserPassword = "Test123";

			PostMethod postMethod = new PostMethod(bulkOperationCommand.url);
			HttpClient client = new HttpClient();
		    Part[] parts = {
		        new StringPart(USER_NAME, "admin@admin.com"),
		        new StringPart(PASSWORD,"Test123"),
		        new FilePart(TEMPLATE_FILE, bulkOperationCommand.templateFile),
		        new FilePart(CSV_FILE, bulkOperationCommand.csvFile)
		    };
		    postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
		    int statusCode = client.executeMethod(postMethod);
		    System.out.println("statusLine>>>" + postMethod.getStatusLine());
		    
		    InputStream i =  (InputStream)postMethod.getResponseBodyAsStream();
//		    ObjectInputStream ois = new ObjectInputStream(i);  
//		    Object o = ois.readObject();
//		    System.out.println("Rec:"+o);
		    postMethod.releaseConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private boolean validateAndCommandLineArguments(String args[])
	{
		return false;
	}
	private void printCommandUsage()
	{
		
	}
}
