/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.actionForm;

import java.io.Serializable;

import org.apache.struts.upload.FormFile;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Bulk operation form from UI.
 * @author sagar_baldwa
 *
 */
public class BulkOperationForm extends AbstractActionForm implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -7455581325611509186L;
	/**
	 * operationName.
	 */
	private String operationName = "";
	/**
	 * dropdownName.
	 */
	private String dropdownName = "";	
	/**
	 * file.
	 */
	private FormFile csvFile;
	/**
	 * xmlTemplateFile.
	 */
	private FormFile xmlTemplateFile;
	/**
	 * @return the xmlTemplateFile
	 */
	public FormFile getXmlTemplateFile() 
	{
		return xmlTemplateFile;
	}
	/**
	 * @param xmlTemplateFile the xmlTemplateFile to set
	 */
	public void setXmlTemplateFile(FormFile xmlTemplateFile) 
	{
		this.xmlTemplateFile = xmlTemplateFile;
	}
	/**
	 * Get Operation Name.
	 * @return String operationName.
	 */
	public String getOperationName()
	{
		return operationName;
	}
	/**
	 * Set operation Name.
	 * @param operationName String.
	 */
	public void setOperationName(String operationName)
	{
		this.operationName = operationName;
	}
	/**
	 * Get DropdownName.
	 * @return String.
	 */
	public String getDropdownName()
	{
		return dropdownName;
	}
	/**
	 * Set DropdownName.
	 * @param dropdownName String.
	 */
	public void setDropdownName(String dropdownName)
	{
		this.dropdownName = dropdownName;
	}
	/**
	 * Get File.
	 * @return FormFile file.
	 */
	public FormFile getCsvFile()
	{
		return csvFile;
	}
	/**
	 * Set File.
	 * @param file FormFile.
	 */
	public void setCsvFile(FormFile file)
	{
		this.csvFile = file;
	}
	@Override
	public int getFormId()
	{
		return 0;
	}

	@Override
	protected void reset()
	{}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{}

	public void setAllValues(AbstractDomainObject arg0)
	{}
}