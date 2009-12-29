import java.io.File;

import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.bulkOperations.JobMessage;


/**
 * Bulk client API
 * @author kalpana_thakur
 *
 */
public interface BulkOperationService
{
	/**
	 * This method will be called to get Job details.
	 * @param jobId jobId
	 * @return Job Message
	 * @throws BulkOperationException BulkOperationException
	 */
	JobMessage getJobDetails(Long jobId) throws BulkOperationException;

	/**
	 * This method will be called to logged in to the application.
	 * @param url URL
	 * @param applicationUserName application user name.
	 * @param applicationUserPassword application user password.
	 * @param keyStoreLocation key store location.
	 * @return Job Message.
	 * @throws BulkOperationException BulkOperationException
	 */
	JobMessage login(String url,String applicationUserName,
			String applicationUserPassword,String keyStoreLocation)
				throws BulkOperationException;

	/**
	 * This method will be called to start the bulk operation.
	 * @param operationName operation name.
	 * @param csvFile CSV file.
	 * @param xmlTemplateFile XML template file.
	 * @return Job Message.
	 * @throws BulkOperationException BulkOperationException
	 */
	JobMessage startbulkOperation(String operationName, File csvFile,
			File xmlTemplateFile)throws BulkOperationException;

}
