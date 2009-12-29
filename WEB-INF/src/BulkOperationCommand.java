import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import edu.wustl.bulkoperator.jobmanager.JobDetails;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.bulkOperations.JobMessage;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
/**
 * @author kalpana_thakur
 * Bulk client API
 */
public class BulkOperationCommand
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir")+ "/BulkOperations/conf");
	}
	/**
	 * Operation Name.
	 */
	private String operationName;
	/**
	 * CSV File.
	 */
	private File csvFile;
	/**
	 * Template File Name.
	 */
	private File templateFile;
	/**
	 * URL.
	 */
	private String url;
	/**
	 * Application User Name.
	 */
	private String applicationUserName;
	/**
	 * Application User Password.
	 */
	private String applicationUserPassword;
	/**
	 * Key store location.
	 */
	private String keyStoreLocation;
	/**
	 * Usage log.
	 */
	private StringBuffer USAGE_LOG = new StringBuffer(
			"\nUsage:\nant runBulkOperation -DoperationName=<operationName>" +
			" -DcsvFile=<csvFile> -DtemplateFile=<templateFile> -Durl=<url> " +
			"\n-DapplicationUserName=<applicationUserName> " +
			"-DapplicationUserPassword=<applicationUserPassword> " +
			"-DkeyStoreLocation=<keyStoreLocation>\n\n Where,\n operationName =" +
			"\t template name or operation name\n csvFile =\t Absolute path" +
			" of CSV data file. This CSV file should be in format as per template\n" +
			" templateFile[optional] =\t Absolute path of the XML template." +
			" Template file is optional to give.\n url=\t Application URL " +
			"e.g. http://localhost:8080/catissuecore\n" +
			" applicationUserName[optional] =\t Application user Login name." +
			" Application user name file is optional to give.\n" +
			" applicationUserPassword =\t Application user password \n" +
			" keyStoreLocation[optional]=\t Absolute path of Keystore location," +
			" if application is deployed as https(SSL).\n" +
			" Keystore loaction is optional to give \n");
	/**
	 * logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(BulkOperationCommand.class);

	/**
	 * Bulk Client.
	 * @param args args.
	 */
	public static void main(String[] args)
	{
		BulkOperationCommand bulkOperationCommand = new BulkOperationCommand();
		try
		{
			if(bulkOperationCommand.keyStoreLocation!=null)
			{
				System.setProperty("javax.net.ssl.trustStore",
						bulkOperationCommand.keyStoreLocation);
			}
			bulkOperationCommand.csvFile = new File("BulkOperations/editSite.csv");
			bulkOperationCommand.templateFile = new File("BulkOperations/editSite.xml");
			bulkOperationCommand.url = "http://localhost:8380/catissuecore";
			bulkOperationCommand.applicationUserName = "admin@admin.com";
			bulkOperationCommand.applicationUserPassword = "Test123";
			bulkOperationCommand.operationName = "editSite";
			//validateAndCommandLineArguments(args);

			BulkOperationService bulkOperationService =
				new BulkOperationServiceImpl();
			JobMessage jobMessage = bulkOperationService.login(
					bulkOperationCommand.url,
					bulkOperationCommand.applicationUserName,
					bulkOperationCommand.applicationUserPassword,
					bulkOperationCommand.keyStoreLocation);
			logger.info(jobMessage.getMessages());
		    if(jobMessage.isOperationSuccessfull())
		    {
		    	jobMessage = bulkOperationService.startbulkOperation(bulkOperationCommand.operationName,
		    			bulkOperationCommand.csvFile,
		    			bulkOperationCommand.templateFile);
		       	logger.info(jobMessage.getMessages());
		       	JobMessage jobIdMessage = bulkOperationService.getJobDetails(jobMessage.getJobId());
		       	logger.info(jobIdMessage.getMessages());
		       	if(jobIdMessage.getJobData()!=null)
		       	{
		       		JobDetails jobDetails = (JobDetails)jobIdMessage.getJobData();
		       		
		       		final byte[] buf = jobDetails.getLogFileBytes();
		       		String zipFileName = "output.zip";
		       		FileOutputStream fos = new FileOutputStream(new File(zipFileName));
			        ObjectOutputStream outStream = new ObjectOutputStream(fos);
			        outStream.write(buf);
			        outStream.flush();
			        outStream.close();
		       	}
		    }

		}
		catch (BulkOperationException exp)
		{
			logger.info(bulkOperationCommand.USAGE_LOG.toString());
		}
		catch (FileNotFoundException fileExp)
		{
			logger.error(fileExp.getMessage(),fileExp);
		}
		catch (IOException fileExp)
		{
			logger.error(fileExp.getMessage(),fileExp);
		}
	}

	/**
	 * Bulk parameters.
	 */
	public static final String[] bulkParams = {"Operation","CSV file",
		"Template file",
		"Application URL",
		"KeyStore location"
	};


	/**
	 * This method will be called to validate the arguments.
	 * @param args
	 * @throws BulkOperationException BulkOperationException
	 */
	private static void validateAndCommandLineArguments(String args[])
	throws BulkOperationException
	{
		if(args.length != 7)
		{
			logger.error("Error: Bulk parameters are either missing  or incorrect," +
					" please check the below usage command.");
			throw new BulkOperationException(null,null,"");
		}
		for (int index=0; index < args.length ; index++)
		{
			if(Validator.isEmpty(args[index]))
			{
				logger.error("Error: Bulk parameters are either missing  or incorrect" +
				bulkParams[index]+", please check the below usage command.");
				throw new BulkOperationException(null,null, "");

			}
		}
	}
}
