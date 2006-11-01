package edu.wustl.catissuecore.applet;

import java.io.Serializable;
import java.util.HashMap;
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
	private List selectedCopiedRows;
	/**
	 * Specify the selectedCopiedCols field - selected cols for copy operation 
	 */
	private List selectedCopiedCols;

	/**
	 * Specify the selectedPastedRows field - selected rows for paste operation 
	 */
	private List selectedPastedRows;
	
	/**
	 * Specify the selectedPastedCols field - selected cols for paste operation
	 */
	private List selectedPastedCols;

	/**
	 * To hold copied data.
	 */
	private HashMap copiedData;	

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
	 * Specify page index
	 */
	private int pageIndexWhenCopied;


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
	 * @return Returns the copiedData.
	 */
	public HashMap getCopiedData()
	{
		return copiedData;
	}
	/**
	 * @param copiedData The copiedData to set.
	 */
	public void setCopiedData(HashMap copiedData)
	{
		this.copiedData = copiedData;
	}
	/**
	 * @return Returns the selectedCopiedCols.
	 */
	public List getSelectedCopiedCols()
	{
		return selectedCopiedCols;
	}
	/**
	 * @param selectedCopiedCols The selectedCopiedCols to set.
	 */
	public void setSelectedCopiedCols(List selectedCopiedCols)
	{
		this.selectedCopiedCols = selectedCopiedCols;
	}
	/**
	 * @return Returns the selectedCopiedRows.
	 */
	public List getSelectedCopiedRows()
	{
		return selectedCopiedRows;
	}
	/**
	 * @param selectedCopiedRows The selectedCopiedRows to set.
	 */
	public void setSelectedCopiedRows(List selectedCopiedRows)
	{
		this.selectedCopiedRows = selectedCopiedRows;
	}
	/**
	 * @return Returns the selectedPastedCols.
	 */
	public List getSelectedPastedCols()
	{
		return selectedPastedCols;
	}
	/**
	 * @param selectedPastedCols The selectedPastedCols to set.
	 */
	public void setSelectedPastedCols(List selectedPastedCols)
	{
		this.selectedPastedCols = selectedPastedCols;
	}
	/**
	 * @return Returns the selectedPastedRows.
	 */
	public List getSelectedPastedRows()
	{
		return selectedPastedRows;
	}
	/**
	 * @param selectedPastedRows The selectedPastedRows to set.
	 */
	public void setSelectedPastedRows(List selectedPastedRows)
	{
		this.selectedPastedRows = selectedPastedRows;
	}
		
	
	/**
	 * @return Returns the pageIndexWhenCopied.
	 */
	public int getPageIndexWhenCopied()
	{
		return pageIndexWhenCopied;
	}
	/**
	 * @param pageIndexWhenCopied The pageIndexWhenCopied to set.
	 */
	public void setPageIndexWhenCopied(int pageIndexWhenCopied)
	{
		this.pageIndexWhenCopied = pageIndexWhenCopied;
	}
}
