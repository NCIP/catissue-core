/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.metadata;

import java.util.ArrayList;
import java.util.Collection;

public class BulkOperationMetaData
{

	private transient final Collection<BulkOperationClass> bulkOperationMetaDataClassCollection = new ArrayList<BulkOperationClass>();
	private Integer batchSize;
	private String templateName;
	
	public Collection<BulkOperationClass> getBulkOperationClass()
	{
		return bulkOperationMetaDataClassCollection;
	}
	public void addBulkOperationClass(BulkOperationClass bulkOperationClass)
	{
		bulkOperationMetaDataClassCollection.add(bulkOperationClass);	
	}
	
	public Integer getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public BulkOperationClass getBulkOperationClassInstance() {
		BulkOperationClass bulkOperationClass = bulkOperationMetaDataClassCollection.iterator().next();
		if (bulkOperationClass.getTemplateName()==null)
		{
			bulkOperationClass.setTemplateName(templateName);
		}
		
		if (bulkOperationClass.getBatchSize()==null || bulkOperationClass.getBatchSize()==0)
		{
			bulkOperationClass.setBatchSize(batchSize);
		}
		
		return bulkOperationClass;
	}
}
