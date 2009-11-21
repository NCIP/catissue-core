package edu.wustl.catissuecore.actionForm;

import org.apache.struts.upload.FormFile;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Bulk operation form from UI.
 * @author sagar_baldwa
 *
 */
public class BulkOperationForm extends AbstractActionForm
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
	 * file.
	 */
	private FormFile file;
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
	 * Get File.
	 * @return FormFile file.
	 */
	public FormFile getFile()
	{
		return file;
	}
	/**
	 * Set File.
	 * @param file FormFile.
	 */
	public void setFile(FormFile file)
	{
		this.file = file;
	}
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub		
	}

	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub		
	}
}