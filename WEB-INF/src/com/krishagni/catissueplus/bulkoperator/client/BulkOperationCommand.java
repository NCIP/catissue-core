/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.client;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.krishagni.catissueplus.bulkoperator.action.JobMessage;
import com.krishagni.catissueplus.bulkoperator.jobmanager.JobDetails;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.util.global.ApplicationProperties;
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
	 * Key store location.
	 */
	private String bulkArtifactsLocation;

	/**
	 * Bulk output ZIP file.
	 */
	public static final StringBuilder BULK_OUTPUT = new StringBuilder();
	/**
	 * Usage log.
	 */
	private static StringBuilder USAGE_LOG = new StringBuilder(
			"\nUsage:\nant -f deploy.xml runBulkOperation -DoperationName=<operationName>" +
			" -DcsvFile=<csvFile> -DtemplateFile=<templateFile> -Durl=<url> " +
			"\n-DapplicationUserName=<applicationUserName> " +
			"-DapplicationUserPassword=<applicationUserPassword> " +
			"-DkeyStoreLocation=<keyStoreLocation>" +
			"-DbulkArtifactsLocation=<bulkArtifactsLocation>\n\n Where,\n operationName =" +
			"\t template name or operation name\n csvFile =\t Absolute path" +
			" of CSV data file. This CSV file should be in format as per template\n" +
			" templateFile[optional] =\t Absolute path of the XML template." +
			" Template file is optional to give.\n url=\t Application URL " +
			"e.g. http://localhost:8080/catissuecore\n" +
			" applicationUserName =\t Application user Login name." +
			" Application user name file is optional to give.\n" +
			" applicationUserPassword =\t Application user password \n" +
			" keyStoreLocation[optional]=\t Absolute path of Keystore location," +
			" if application is deployed as https(SSL).\n" +
			" Keystore loaction is optional to give \n"+
			"bulkArtifactsLocation[optional]=\t Specify the path where you want " +
			"to store bulk artifacts.\n");
	/**
	 * logger.
	 */
	private static final Logger logger =
		Logger.getCommonLogger(BulkOperationCommand.class);

	/**
	 * Bulk Client.
	 * @param args args.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		try
		{
			ApplicationProperties
			.initBundle("ApplicationResources");
			startCommandLine(args);
		}
		catch (BulkOperationException exp)
		{
			logger.info(exp.getMessage());
			logger.debug(exp.getMessage(),exp);
		}
		catch (FileNotFoundException fileExp)
		{
			logger.debug(fileExp.getMessage(),fileExp);
		}
		catch (IOException fileExp)
		{
			logger.debug(fileExp.getMessage(),fileExp);
		}
	}

	/**
	 * Method to start the bulk operation through command line.
	 * @param args Array of String.
	 * @throws BulkOperationException BulkOperationException.
	 * @throws FileNotFoundException FileNotFoundException.
	 * @throws IOException IOException.
	 */
	public static void startCommandLine(String[] args)
	throws BulkOperationException, FileNotFoundException, IOException
	{
		BulkOperationCommand bulkOperationCommand  = new BulkOperationCommand();
		logger.info("No of arguments ::"+args.length);
		if(validateAndCommandLineArguments(args))
		{
			bulkOperationCommand.operationName = args[0];
			bulkOperationCommand.csvFile = new File(args[1]);
			bulkOperationCommand.url = args[2];
			bulkOperationCommand.applicationUserName = args[3];
			bulkOperationCommand.applicationUserPassword = args[4];

			for(int index=5; index<args.length ;index++)
			{
				if(args[index].contains(".keystore"))
				{
					bulkOperationCommand.keyStoreLocation = args[index];
					System.setProperty("javax.net.ssl.trustStore",
						bulkOperationCommand.keyStoreLocation);
				}
				else if(new File(args[index]).isDirectory())
				{
					bulkOperationCommand.bulkArtifactsLocation = args[index];
				}
				else if(new File(args[index]).isFile())
				{
					bulkOperationCommand.templateFile = new File(args[index]);
				}

			}
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
				if(jobMessage.isOperationSuccessfull())
				{
					if(bulkOperationCommand.bulkArtifactsLocation != null &&
							!"".equals(bulkOperationCommand.bulkArtifactsLocation))
					{
						bulkOperationCommand.bulkArtifactsLocation=bulkOperationCommand.bulkArtifactsLocation.replace("\\", "/");
						BULK_OUTPUT.append(bulkOperationCommand.bulkArtifactsLocation ).append("/");
					}
					BULK_OUTPUT.append(bulkOperationCommand.operationName).append(jobMessage.getJobId()).append(".zip");

					JobDetails jobDetails = null;

					do
					{
						JobMessage jobIdMessage = bulkOperationService.getJobDetails(
								jobMessage.getJobId());
						logger.info(jobIdMessage.getMessages());
						if(jobIdMessage.getJobData()!=null)
						{
							jobDetails = (JobDetails)jobIdMessage.getJobData();
							logger.info(getJobDetails(jobDetails,bulkOperationCommand));
							final byte[] buf = jobDetails.getLogFileBytes();
							if(buf != null)
							{
								String zipFileName = BULK_OUTPUT.toString();
								FileOutputStream fileOutputStream =
									new FileOutputStream(new File(zipFileName));
								fileOutputStream.write(buf);
								fileOutputStream.flush();
								fileOutputStream.close();

							}
						}
					}
					while(checkStatus(jobDetails.getStatus()));
				}
			}

			bulkOperationService.logout(bulkOperationCommand.url);

		}
	}

	/**
	 * This method will be called to remove extra path separator.
	 * @param url : application URL
	 * @return URL having no path separator in the end. 
	 */
	
	
	
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
			logger.debug("Current time start :"+System.currentTimeMillis());
			while(waitingTime > 0)
			{
				logger.debug("Waiting for 10 secs");
				waitingTime = waitingTime - 1;
			}
			logger.debug("Current time end:"+System.currentTimeMillis());
		}
		return isSuccessOrFailure;
	}
	/**
	 * Bulk parameters.
	 */

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
	private static String getJobDetails(JobDetails jobDetails,BulkOperationCommand bulkOperationCommand)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(NEW_LINE).append(BULK_OPERATION_NAME).append(jobDetails.getJobName())
		.append(NEW_LINE).append(START_TIME).append(jobDetails.getStartTime())
		.append(NEW_LINE).append(STATUS).append(jobDetails.getStatus())
		.append(NEW_LINE).append(TOTAL_RECORDS).append(jobDetails.getTotalRecordsCount())
		.append(NEW_LINE).append(PROCESSED_RECORDS).append(jobDetails.getCurrentRecordsProcessed())
		.append(NEW_LINE).append(FAILED_RECORDS).append(jobDetails.getFailedRecordsCount())
		.append(NEW_LINE).append(TIME_TAKEN).append(jobDetails.getTimeTaken());

		if(bulkOperationCommand.bulkArtifactsLocation != null &&
						!"".equals(bulkOperationCommand.bulkArtifactsLocation))
		{
			stringBuilder.append(NEW_LINE).append(REPORT).append(BULK_OUTPUT);
		}
		else
		{
			stringBuilder.append(NEW_LINE).append(REPORT).append(System.getProperty("user.dir"))
			.append("/").append(BULK_OUTPUT);
		}


		return stringBuilder.toString();
	}

	/**
	 * This method will be called to validate the arguments.
	 * @param args[] arguments.
	 */
	private static boolean validateAndCommandLineArguments(String args[])
	{

		boolean isValid = true;
		for (int index=0; index < 5 ; index++) // To validate first 5 fields.
		{
			if(Validator.isEmpty(args[index]))
			{
				logger.info("Error: Bulk parameters are either missing  or incorrect " +
						args[index]+", please check the below usage command.");
				logger.info(USAGE_LOG.toString());
				isValid = false;
				break;
			}
			if (!Validator.isEmpty(args[index]) && args[index].contains(".csv"))
			{
				
				File file = new File(args[index]);
				if(!file.exists())
				{
					logger.info("Error: File is missing " +
						args[index]+", please check the path.");
						logger.info(USAGE_LOG.toString());
						isValid = false;
						break;
				}
			}
		}

		if(args.length > 5) // for optional fields.
		{
			for(int index=5; index<args.length ;index++)
			{
				if(!Validator.isEmpty(args[index]) && (args[index].contains(".keystore") ||args[index].contains(".xml")))
				{
					File file = new File(args[index]);
					if(!file.exists())
					{
						logger.info("Error: File is missing " +
								args[index]+", please check the path.");
						logger.info(USAGE_LOG.toString());
						isValid = false;
						break;
					}
				}
				else if(!Validator.isEmpty(args[index]) && !new File(args[index]).isDirectory())
				{
					logger.info("Error: Path specified to store bulk artifacts should be a directory   " +
							args[index]+", please check the path.");
					logger.info(USAGE_LOG.toString());
					isValid = false;
					break;
					
				}
			}
		}
		return isValid;
	}
}
