/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.processor;

import java.util.Map;

import com.krishagni.catissueplus.core.bo.factory.impl.BulkOperationProcessor;
import com.krishagni.catissueplus.bulkoperator.appservice.AppServiceInformationObject;
import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkProcessor;
import com.krishagni.catissueplus.bulkoperator.util.ServiceAction;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

public class StaticBulkOperationProcessor extends AbstractBulkOperationProcessor
		implements
		IBulkOperationProcessor
{
	private static final Logger logger = Logger.getCommonLogger(StaticBulkOperationProcessor.class);

	public StaticBulkOperationProcessor(BulkOperationClass bulkOperationClass,
			AppServiceInformationObject serviceInformationObject)
	{
		super(bulkOperationClass, serviceInformationObject);
	}

	@Override
	Object processObject(Map<String, String> csvData) throws BulkOperationException
	{
		return null;
	}

	public Object process(CsvReader csvReader, int csvRowNumber,SessionDataBean sessionDataBean)
			throws BulkOperationException, Exception
	{
		Object staticObject = null;

		try
		{
			staticObject = getEntityObject(csvReader);
			processObject(staticObject, bulkOperationClass, csvReader, "", false, csvRowNumber);
			BulkProcessor bulkProcessor = new BulkOperationProcessor();
			
			if (bulkOperationClass.isUpdateOperation())
			{
				staticObject = bulkProcessor.processObject(staticObject, ServiceAction.UPDATE, sessionDataBean);
			}
			else
			{
				staticObject = bulkProcessor.processObject(staticObject, ServiceAction.ADD, sessionDataBean);
			}
		}
		catch (Exception exp){
			logger.error(exp.getMessage(), exp);
			throw exp;
		}
		return staticObject;
	}
}