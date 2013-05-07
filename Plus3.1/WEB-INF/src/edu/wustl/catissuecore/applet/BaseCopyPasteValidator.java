
package edu.wustl.catissuecore.applet;

import java.io.Serializable;
import java.util.List;

/**
 * <p>This class initializes the fields of BaseCopyPasteValidator.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class BaseCopyPasteValidator implements Serializable
{

	/**
	 * Specify the serialVersionUID field.
	 */
	private static final long serialVersionUID = 366636337395262535L;

	/**
	 * Specify the validatorModel field.
	 */
	protected CopyPasteOperationValidatorModel validatorModel;

	/**
	 * Default Constructor.
	 */
	public BaseCopyPasteValidator()
	{
		//Empty Constructor.
	}

	/**
	 * Constructor with model.
	 * @param validatorModel model
	 */
	public BaseCopyPasteValidator(CopyPasteOperationValidatorModel validatorModel)
	{
		this.validatorModel = validatorModel;
	}

	/**
	 * Perform the base level validations required for copy operation.
	 * @return String
	 */
	public String validate()
	{
		String message = "";
		message = this.preValidate();
		if ("".equals(message))
		{
			message = this.doValidate();
		}
		if ("".equals(message))
		{
			message = this.postValidate();
		}
		return message;
	}

	/**
	 * Perform the pre validations required for copy operation.
	 * The different validations performed here are
	 * 1. Selection of number of rows in a column or number of columns should be contiguous
	 * 2. Number of rows/columns selected for copy and paste should match
	 * 3. If a single row/column is selected while paste operation,
	 *  there should be enough rows/columns as per copied data
	 *  @return String
	 */
	protected String preValidate()
	{
		String message = "";
		List selectedRows = null;
		List selectedCols = null;

		// get selected rows and columns as per copy or paste operation
		if (this.validatorModel.getOperation().equals("copy"))
		{
			selectedRows = this.validatorModel.getSelectedCopiedRows();
			selectedCols = this.validatorModel.getSelectedCopiedCols();
		}
		else
		{
			selectedRows = this.validatorModel.getSelectedPastedRows();
			selectedCols = this.validatorModel.getSelectedPastedCols();
		}
		/**
		 *  -- Number of rows/columns selected for copy and paste should match
		 *  -- If a single row/column is selected while paste operation,
		 *   there should be enough rows/columns as per copied data
		 */
		if (this.validatorModel.getOperation().equals("paste"))
		{
			message = this.validateRowsForPaste(selectedRows, this.validatorModel);
			if ("".equals(message))
			{
				message = getMessage(selectedRows, selectedCols) ;
			}
		}
		return message;
	}
	/**
	 * This function returns the message.
	 * @param selectedRows List of Selected Rows.
	 * @param selectedCols List of Selected Columns.
	 * @return String message.
	 */
	private String getMessage(List selectedRows, List selectedCols)
	{
		String message = this.validateColsForPaste(selectedCols, this.validatorModel);
		if ("".equals(message))
		{
			/**
			 * -- Selection of number of rows in a column should be contiguous
			 */
			message = this.validateRows(selectedRows);
			if ("".equals(message))
			{
				/**
				 * -- Selection of number of columns should be contiguous
				 */
				message = this.validateCols(selectedCols);
			}
		}
		return message ;
	}
	/**
	 * This method checks Selection of number of rows in a column should be contiguous.
	 * @param selectedRows - list
	 * @return - message
	 */
	private String validateRows(List selectedRows)
	{
		int rowValue = 0;
		String message = "" ;
		for (int i = 0; i < selectedRows.size(); i++)
		{
			final Integer row = (Integer) selectedRows.get(i);
			if (i == 0)
			{
				rowValue = row.intValue();
				continue;
			}
			if ((row.intValue()) != rowValue + i)
			{
				// return "Please select contiguous rows";
				message = "Please select contiguous rows";
				break ;
			}
		}
		//return "";
		return message ;
	}

	/**
	 * This method checks Selection of number of columns should be contiguous.
	 * @param selectedCols - list
	 * @return - message
	 */
	private String validateCols(List selectedCols)
	{
		int colValue = 0;
		String message = "" ;
		for (int i = 0; i < selectedCols.size(); i++)
		{
			final Integer col = (Integer) selectedCols.get(i);
			if (i == 0)
			{
				colValue = col.intValue();
				continue;
			}
			if ((col.intValue()) != colValue + i)
			{
				//return "Please select contiguous columns";
				message = "Please select contiguous columns";
				break ;
			}
		}
		//return "";
		return message ;
	}

	/**
	 * This method checks.
	 *  -- Number of rows selected for copy and paste should match
	 *  -- If a single row is selected while paste operation, there should be enough rows as per copied data
	 *
	 * @param selectedRows - list
	 * @param validatorModel - CopyPasteOperationValidatorModel
	 * @return - message
	 */
	private String validateRowsForPaste(List selectedRows,
			CopyPasteOperationValidatorModel validatorModel)
	{
		final int numberOfRowsCopied = validatorModel.getSelectedCopiedRows().size();
		String message = "" ;
		if (selectedRows.size() == 1)
		{
			final Integer row = (Integer) selectedRows.get(0);
			final int rowValue = row.intValue();
			if (validatorModel.getRowCount() - rowValue < numberOfRowsCopied)
			{
				//return "There are not enough rows to paste the copied data";
				message = "There are not enough rows to paste the copied data";
			}
		}
		else
		{
			if (selectedRows.size() != numberOfRowsCopied)
			{
				//return "Number of rows selected for copy and paste operation do not match";
				message = "Number of rows selected for copy and paste operation do not match";
			}
		}
		//return "";
		return message ;
	}

	/**
	 * This method checks.
	 *  -- Number of columns selected for copy and paste should match
	 *  -- If a single column is selected while paste operation,
	 *   there should be enough columns as per copied data
	 * @param selectedCols - list
	 * @param validatorModel - CopyPasteOperationValidatorModel
	 * @return - message
	 */
	private String validateColsForPaste(List selectedCols,
			CopyPasteOperationValidatorModel validatorModel)
	{
		final int numberOfColsCopied = validatorModel.getSelectedCopiedCols().size();
		String message = "" ;
		if (selectedCols.size() == 1)
		{
			final Integer col = (Integer) selectedCols.get(0);
			final int colValue = col.intValue();
			if (validatorModel.getColumnCount() - colValue < numberOfColsCopied)
			{
				//return "There are not enough columns to paste the copied data";
				message = "There are not enough columns to paste the copied data";
			}
		}
		else
		{
			if (selectedCols.size() != numberOfColsCopied) // && numberOfColsCopied!=1)
			{
				//return "Number of columns selected for copy and paste operation do not match";
				message = "Number of columns selected for copy and paste operation do not match";
			}
		}
		//return "";
		return message ;
	}

	/**
	 * Perform the actual validations required for copy operation.
	 * That method must be overridden in subclasses.
	 * @return String
	 */
	protected abstract String doValidate();

	/**
	 * Perform the post validations required for copy operation.
	 * @return String
	 */
	protected String postValidate()
	{
		final String message = "";
		return message;
 	}
}