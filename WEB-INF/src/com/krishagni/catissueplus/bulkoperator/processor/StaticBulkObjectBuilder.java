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

import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;

import edu.wustl.common.util.logger.Logger;

public class StaticBulkObjectBuilder extends AbstractBulkObjectBuilder
		implements
		BulkObjectBuilder
{
	private static final Logger logger = Logger.getCommonLogger(StaticBulkObjectBuilder.class);

	public StaticBulkObjectBuilder(BulkOperationClass bulkOperationClass)
	{
		super(bulkOperationClass);
	}

	@Override
	Object processObject(Map<String, String> csvData) throws BulkOperationException
	{
		return null;
	}

	public Object process(CsvReader csvReader, int csvRowNumber) throws Exception
	{
		Object staticObject = null;

		try
		{
			staticObject = getEntityObject(csvReader);
			processObject(staticObject, bulkOperationClass, csvReader, "", false, csvRowNumber);
		}
		catch (Exception exp){
			logger.error(exp.getMessage(), exp);
			throw exp;
		}
		return staticObject;
	}
}