package edu.wustl.catissuecore.applet;

import java.io.Serializable;
import java.util.List;


/**
 * <p>This class initializes the fields of BaseCopyPasteValidator.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class BaseCopyPasteValidator implements Serializable 
{
	
	/**
	 * Specify the serialVersionUID field 
	 */
	private static final long serialVersionUID = 366636337395262535L;

	/**
	 * Specify the validatorModel field 
	 */
	protected CopyPasteOperationValidatorModel validatorModel;
	
	/**
	 * Perform the base level validations required for copy operation.  
	 */
	public String validate()
	{
		String message = "";
		message = preValidate();
		message = doValidate();
		message = postValidate();
		return message;
	}
	
	/**
	 * Perform the pre validations required for copy operation.  
	 */
	protected String preValidate()
	{
		String message = "";
		
		List selectedRows = validatorModel.getSelectedRows();
		int rowValue = 0;
		int numberOfRowsCopied = 0; // TODO get number of rows copied 
		if(validatorModel.getOperation().equals("paste") && selectedRows.size()==1 )
		{
			Integer row = (Integer) selectedRows.get(0);
			rowValue = row.intValue();
			if(validatorModel.getRowCount() - rowValue < numberOfRowsCopied)
			{
				return "There are not enough rows to paste the copied data";
			}
		}
	
		for (int i = 0; i < selectedRows.size(); i++)
		{
			Integer row = (Integer) selectedRows.get(i);
			if (i == 0)
			{
				rowValue = row.intValue();
				continue;
			}
			if ((row.intValue()) != rowValue + i)
			{
				return "Please select contiguous rows";
			}
		}
		
		return message;
	}
	
	/**
	 * Perform the actual validations required for copy operation.
	 * That method must be overridden in subclasses.  
	 */
	protected abstract String doValidate();
	
	/**
	 * Perform the post validations required for copy operation.
	 */
	protected String postValidate()
	{
		String message = "";
		return message;
	}
}
