package edu.wustl.catissuecore.vo;


/**
 * @author rahul_ner
 *
 */
public class ArrayDistributionReportEntry {
	
	String[] arrayInfo;
	
	String[][] specimenEntries;
	
	String[][] gridInfo;

	/**
	 * @return Returns the arrayInfo.
	 */
	public String[] getArrayInfo() {
		return arrayInfo;
	}

	/**
	 * @param arrayInfo The arrayInfo to set.
	 */
	public void setArrayInfo(String[] arrayInfo) {
		this.arrayInfo = arrayInfo;
	}

	/**
	 * @return Returns the gridInfo.
	 */
	public String[][] getGridInfo() {
		return gridInfo;
	}

	/**
	 * @param gridInfo The gridInfo to set.
	 */
	public void setGridInfo(String[][] gridInfo) {
		this.gridInfo = gridInfo;
	}

	/**
	 * @return Returns the specimenEntries.
	 */
	public String[][] getSpecimenEntries() {
		return specimenEntries;
	}

	/**
	 * @param specimenEntries The specimenEntries to set.
	 */
	public void setSpecimenEntries(String[][] specimenEntries) {
		this.specimenEntries = specimenEntries;
	}
}
