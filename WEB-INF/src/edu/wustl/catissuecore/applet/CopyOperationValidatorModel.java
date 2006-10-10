package edu.wustl.catissuecore.applet;

import java.io.Serializable;
import java.util.List;


/**
 * <p>This class will contain data which is required for Copy operation validator.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class CopyOperationValidatorModel implements Serializable {
	
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
	 * @return Returns the selectedCopiedCols.
	 */
	public List getSelectedCopiedCols() {
		return selectedCopiedCols;
	}

	/**
	 * @param selectedCopiedCols The selectedCopiedCols to set.
	 */
	public void setSelectedCopiedCols(List selectedCopiedCols) {
		this.selectedCopiedCols = selectedCopiedCols;
	}

	/**
	 * @return Returns the selectedCopiedRows.
	 */
	public List getSelectedCopiedRows() {
		return selectedCopiedRows;
	}

	/**
	 * @param selectedCopiedRows The selectedCopiedRows to set.
	 */
	public void setSelectedCopiedRows(List selectedCopiedRows) {
		this.selectedCopiedRows = selectedCopiedRows;
	}

	/**
	 * @return Returns the selectedPastedCols.
	 */
	public List getSelectedPastedCols() {
		return selectedPastedCols;
	}

	/**
	 * @param selectedPastedCols The selectedPastedCols to set.
	 */
	public void setSelectedPastedCols(List selectedPastedCols) {
		this.selectedPastedCols = selectedPastedCols;
	}

	/**
	 * @return Returns the selectedPastedRows.
	 */
	public List getSelectedPastedRows() {
		return selectedPastedRows;
	}

	/**
	 * @param selectedPastedRows The selectedPastedRows to set.
	 */
	public void setSelectedPastedRows(List selectedPastedRows) {
		this.selectedPastedRows = selectedPastedRows;
	}
}
