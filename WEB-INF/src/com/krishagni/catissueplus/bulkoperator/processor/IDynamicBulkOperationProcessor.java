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


import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.csv.impl.CsvFileReader;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.beans.SessionDataBean;

public interface IDynamicBulkOperationProcessor
{

	Object process(CsvReader csvReader, int csvRowCounter,
			SessionDataBean sessionDataBean) throws BulkOperationException,
			Exception;
}
