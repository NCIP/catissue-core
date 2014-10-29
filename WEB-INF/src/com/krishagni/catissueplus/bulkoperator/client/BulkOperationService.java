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
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.krishagni.catissueplus.bulkoperator.action.JobMessage;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;


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
	 * Logout.
	 *
	 * @param url the url
	 * @throws BulkOperationException the bulk operation exception
	 * @throws IOException
	 * @throws HttpException
	 */
	void logout(String url)
				throws BulkOperationException, HttpException, IOException;

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
