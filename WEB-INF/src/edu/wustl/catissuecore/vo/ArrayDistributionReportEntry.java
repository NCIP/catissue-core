package edu.wustl.catissuecore.vo;

import java.util.List;


/**
 * @author rahul_ner
 *
 */
public class ArrayDistributionReportEntry {
	
	List arrayInfo;
	
	List specimenEntries;
	
	List gridInfo;

	/**
	 * @return Returns the arrayInfo.
	 */
	public List getArrayInfo() {
		return arrayInfo;
	}

	/**
	 * @param arrayInfo The arrayInfo to set.
	 */
	public void setArrayInfo(List arrayInfo) {
		this.arrayInfo = arrayInfo;
	}

	/**
	 * @return Returns the gridInfo.
	 */
	public List getGridInfo() {
		return gridInfo;
	}

	/**
	 * @param gridInfo The gridInfo to set.
	 */
	public void setGridInfo(List gridInfo) {
		this.gridInfo = gridInfo;
	}

	/**
	 * @return Returns the specimenEntries.
	 */
	public List getSpecimenEntries() {
		return specimenEntries;
	}

	/**
	 * @param specimenEntries The specimenEntries to set.
	 */
	public void setSpecimenEntries(List specimenEntries) {
		this.specimenEntries = specimenEntries;
	}
}
