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
 * Bulk client API.
 * @author kalpana_thakur
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
	 * Bulk output ZIP file.
	 */
	public static final String BULK_OUTPUT = "bulkOutput.zip";
	/**
	 * Usage log.
	 */
	private static StringBuilder USAGE_LOG = new StringBuilder(
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
	private static final Logger logger =
		Logger.getCommonLogger(BulkOperationCommand.class);

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
			//if(validateAndCommandLineArguments(args))
			{

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
					jobMessage = bulkOperationService.startbulkOperation(
							bulkOperationCommand.operationName,
							bulkOperationCommand.csvFile,
							bulkOperationCommand.templateFile);
					logger.info(jobMessage.getMessages());
					JobDetails jobDetails = null;
					do
					{
						JobMessage jobIdMessage = bulkOperationService.getJobDetails(
								jobMessage.getJobId());
						logger.info(jobIdMessage.getMessages());
						if(jobIdMessage.getJobData()!=null)
						{
							jobDetails = (JobDetails)jobIdMessage.getJobData();
							logger.info(getJobDetails(jobDetails));
							final byte[] buf = jobDetails.getLogFileBytes();
							if(buf != null)
							{
								String zipFileName = BULK_OUTPUT;
								FileOutputStream fileOutputStream =
								 new FileOutputStream(new File(zipFileName));
								fileOutputStream.write(buf);
								fileOutputStream.flush();
								fileOutputStream.close();

							}
						}
					}while(checkStatus(jobDetails.getStatus()));
				}
			}

		}
		catch (BulkOperationException exp)
		{
			logger.error(exp.getMessage(),exp);
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
	 * This method will be called to check the status.
	 * @param status Job status
	 * @return return false if status completed or failed.
	 */
	private static boolean  checkStatus(String status)
	{
		boolean isSuccessOrFailure = true;
		if("Completed".equals(status) || "Failed".equals(status))
		{
			isSuccessOrFailure = false;
		}
		else
		{
			long waitingTime = 100000l;
			logger.info("Current time start :"+System.currentTimeMillis());
			while(waitingTime > 0)
			{
				logger.debug("Waiting for 10 secs");
				waitingTime = waitingTime - 1;
			}
			logger.info("Current time end:"+System.currentTimeMillis());
		}
		return isSuccessOrFailure;
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
	 * Bulk Operation Name.
	 */
	public static final String BULK_OPERATION_NAME = "Bulk Operation Name :";
	/**
	 * Start Time.
	 */
	public static final String START_TIME = "Start Time :";
	/**
	 * Status.
	 */
	public static final String STATUS = "Status :";
	/**
	 * Total Records.
	 */
	public static final String TOTAL_RECORDS = "Total Records :";
	/**
	 * Processed Records.
	 */
	public static final String PROCESSED_RECORDS = "Processed Records :";
	/**
	 * Failed Records.
	 */
	public static final String FAILED_RECORDS = "Failed Records :";
	/**
	 * Time taken(sec).
	 */
	public static final String TIME_TAKEN = "Time taken(sec) :";
	/**
	 * Report.
	 */
	public static final String REPORT = "Report :";
	/**
	 * New line.
	 */
	public static final String NEW_LINE = "\n";

	/**
	 * This method will return the job details.
	 * @param jobDetails Job details.
	 * @return Job details.
	 */
	private static String getJobDetails(JobDetails jobDetails)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(NEW_LINE).append(BULK_OPERATION_NAME).append(jobDetails.getJobName())
		.append(NEW_LINE).append(START_TIME).append(jobDetails.getStartTime())
		.append(NEW_LINE).append(STATUS).append(jobDetails.getStatus())
		.append(NEW_LINE).append(TOTAL_RECORDS).append(jobDetails.getTotalRecordsCount())
		.append(NEW_LINE).append(PROCESSED_RECORDS).append(jobDetails.getCurrentRecordsProcessed())
		.append(NEW_LINE).append(FAILED_RECORDS).append(jobDetails.getFailedRecordsCount())
		.append(NEW_LINE).append(TIME_TAKEN).append(jobDetails.getTimeTaken())
		.append(NEW_LINE).append(REPORT).append(System.getProperty("user.dir"))
		.append("\\").append(BULK_OUTPUT);
		return stringBuilder.toString();
	}

	/**
	 * This method will be called to validate the arguments.
	 * @param args[] arguments.
	 */
	private static boolean validateAndCommandLineArguments(String args[])
	{
		boolean isValid = true;
		if(args.length != 7)
		{
			logger.error("Error: Bulk parameters are either missing  or incorrect," +
					" please check the below usage command.");
			logger.info(USAGE_LOG.toString());
			isValid = false;
		}
		for (int index=0; index < args.length ; index++)
		{
			if(Validator.isEmpty(args[index]))
			{
				logger.error("Error: Bulk parameters are either missing  or incorrect" +
				bulkParams[index]+", please check the below usage command.");
				logger.info(USAGE_LOG.toString());
				isValid = false;
			}
		}
		return isValid;
	}
}
