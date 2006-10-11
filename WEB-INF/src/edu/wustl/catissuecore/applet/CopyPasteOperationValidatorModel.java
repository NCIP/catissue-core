package edu.wustl.catissuecore.applet;

import java.io.Serializable;
import java.util.List;


/**
 * <p>This class will contain data which is required for Copy operation validator.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class CopyPasteOperationValidatorModel implements Serializable {
	
	/**
	 * Specify the serialVersionUID field 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Specify the selectedCopiedRows field - selected rows for copy operation 
	 */
	private List selectedRows;
	/**
	 * Specify the selectedCopiedCols field - selected cols for copy operation 
	 */
	private List selectedCols;

	/**
	 * Specify the ignoredRows field - ignored Rows for paste operation 
	 */
	private List ignoredRows;
	
	/**
	 * Specify the rowCount field - total rows
	 */
	private int rowCount;
	
	/**
	 * Specify the columnCount field - total columns
	 */
	private int columnCount;
	
	/**
	 * Specify the operation - whether copy or paste
	 */
	private String operation;


	/**
	 * @return Returns the columnCount.
	 */
	public int getColumnCount()
	{
		return columnCount;
	}
	/**
	 * @param columnCount The columnCount to set.
	 */
	public void setColumnCount(int columnCount)
	{
		this.columnCount = columnCount;
	}
	/**
	 * @return Returns the ignoredRows.
	 */
	public List getIgnoredRows()
	{
		return ignoredRows;
	}
	/**
	 * @param ignoredRows The ignoredRows to set.
	 */
	public void setIgnoredRows(List ignoredRows)
	{
		this.ignoredRows = ignoredRows;
	}
	/**
	 * @return Returns the operation.
	 */
	public String getOperation()
	{
		return operation;
	}
	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	/**
	 * @return Returns the rowCount.
	 */
	public int getRowCount()
	{
		return rowCount;
	}
	/**
	 * @param rowCount The rowCount to set.
	 */
	public void setRowCount(int rowCount)
	{
		this.rowCount = rowCount;
	}
	/**
	 * @return Returns the selectedCols.
	 */
	public List getSelectedCols()
	{
		return selectedCols;
	}
	/**
	 * @param selectedCols The selectedCols to set.
	 */
	public void setSelectedCols(List selectedCols)
	{
		this.selectedCols = selectedCols;
	}
	/**
	 * @return Returns the selectedRows.
	 */
	public List getSelectedRows()
	{
		return selectedRows;
	}
	/**
	 * @param selectedRows The selectedRows to set.
	 */
	public void setSelectedRows(List selectedRows)
	{
		this.selectedRows = selectedRows;
	}
}
