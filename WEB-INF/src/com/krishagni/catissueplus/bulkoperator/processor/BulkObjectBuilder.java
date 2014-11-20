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

public interface BulkObjectBuilder
{
	Object process(CsvReader csvReader, int csvRowNumber) throws Exception;
}
