
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
	 * Default Constructor
	 */
	public BaseCopyPasteValidator()
	{

	}

	/**
	 * Constructor with model
	 * @param validatorModel model
	 */
	public BaseCopyPasteValidator(CopyPasteOperationValidatorModel validatorModel)
	{
		this.validatorModel = validatorModel;
	}

	/**
	 * Perform the base level validations required for copy operation.  
	 */
	public String validate()
	{
		String message = "";
		message = preValidate();
		if (message.equals(""))
		{
			message = doValidate();
		}
		if (message.equals(""))
		{
			message = postValidate();
		}
		return message;
	}

	/**
	 * Perform the pre validations required for copy operation.  
	 * The different validations performed here are 
	 * 1. Selection of number of rows in a column or number of columns should be contiguous
	 * 2. Number of rows/columns selected for copy and paste should match
	 * 3. If a single row/column is selected while paste operation, there should be enough rows/columns as per copied data  
	 */
	protected String preValidate()
	{

		// TODO null or empty list
		String message = "";

		List selectedRows = null;
		List selectedCols = null;

		// get selected rows and columns as per copy or paste operation
		if (validatorModel.getOperation().equals("copy"))
		{
			selectedRows = validatorModel.getSelectedCopiedRows();
			selectedCols = validatorModel.getSelectedCopiedCols();
		}
		else
		{
			selectedRows = validatorModel.getSelectedPastedRows();
			selectedCols = validatorModel.getSelectedPastedCols();
		}

		/**
		 *  -- Number of rows/columns selected for copy and paste should match
		 *  -- If a single row/column is selected while paste operation, there should be enough rows/columns as per copied data
		 */
		if (validatorModel.getOperation().equals("paste"))
		{
			message = validateRowsForPaste(selectedRows, validatorModel);
			if (!message.equals(""))
			{
				return message;
			}
			message = validateColsForPaste(selectedCols, validatorModel);
			if (!message.equals(""))
			{
				return message;
			}
		}

		/**
		 * -- Selection of number of rows in a column should be contiguous
		 */

		message = validateRows(selectedRows);
		if (!message.equals(""))
		{
			return message;
		}

		/**
		 * -- Selection of number of columns should be contiguous
		 */
		message = validateCols(selectedCols);

		return message;
	}

	/**
	 * This method checks Selection of number of rows in a column should be contiguous
	 * @param selectedRows - list
	 * @return - message
	 */
	private String validateRows(List selectedRows)
	{
		int rowValue = 0;
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
		return "";
	}

	/**
	 * This method checks Selection of number of columns should be contiguous
	 * @param selectedRows - list
	 * @return - message
	 */
	private String validateCols(List selectedCols)
	{
		int colValue = 0;
		for (int i = 0; i < selectedCols.size(); i++)
		{
			Integer col = (Integer) selectedCols.get(i);
			if (i == 0)
			{
				colValue = col.intValue();
				continue;
			}
			if ((col.intValue()) != colValue + i)
			{
				return "Please select contiguous columns";
			}
		}

		return "";
	}

	/**
	 * This method checks
	 *  -- Number of rows selected for copy and paste should match
	 *  -- If a single row is selected while paste operation, there should be enough rows as per copied data
	 *
	 * @param selectedRows - list
	 * @param validatorModel - CopyPasteOperationValidatorModel
	 * @return - message
	 */
	private String validateRowsForPaste(List selectedRows, CopyPasteOperationValidatorModel validatorModel)
	{
		int numberOfRowsCopied = validatorModel.getSelectedCopiedRows().size();
		if (selectedRows.size() == 1)
		{
			Integer row = (Integer) selectedRows.get(0);
			int rowValue = row.intValue();
			if (validatorModel.getRowCount() - rowValue < numberOfRowsCopied)
			{
				return "There are not enough rows to paste the copied data";
			}
		}
		else
		{
			if (selectedRows.size() != numberOfRowsCopied)
			{
				return "Number of rows selected for copy and paste operation do not match";
			}
		}
		return "";
	}

	/**
	 * This method checks
	 *  -- Number of columns selected for copy and paste should match
	 *  -- If a single column is selected while paste operation, there should be enough columns as per copied data
	 *
	 * @param selectedRows - list
	 * @param validatorModel - CopyPasteOperationValidatorModel
	 * @return - message
	 */
	private String validateColsForPaste(List selectedCols, CopyPasteOperationValidatorModel validatorModel)
	{
		int numberOfColsCopied = validatorModel.getSelectedCopiedCols().size();
		if (selectedCols.size() == 1)
		{
			Integer col = (Integer) selectedCols.get(0);
			int colValue = col.intValue();
			if (validatorModel.getColumnCount() - colValue < numberOfColsCopied)
			{
				return "There are not enough columns to paste the copied data";
			}
		}
		else
		{
			if (selectedCols.size() != numberOfColsCopied) // && numberOfColsCopied!=1)
			{
				return "Number of columns selected for copy and paste operation do not match";
			}
		}
		return "";
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