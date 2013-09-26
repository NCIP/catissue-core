/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.vo;

import java.util.List;

/**
 * @author rahul_ner
 *
 */
public class ArrayDistributionReportEntry
{

	List arrayInfo;

	List specimenEntries;

	List gridInfo;

	/**
	 * @return Returns the arrayInfo.
	 */
	public List getArrayInfo()
	{
		return this.arrayInfo;
	}

	/**
	 * @param arrayInfoParam The arrayInfo to set.
	 */
	public void setArrayInfo(List arrayInfoParam)
	{
		this.arrayInfo = arrayInfoParam;
	}

	/**
	 * @return Returns the gridInfo.
	 */
	public List getGridInfo()
	{
		return this.gridInfo;
	}

	/**
	 * @param gridInfoParam The gridInfo to set.
	 */
	public void setGridInfo(List gridInfoParam)
	{
		this.gridInfo = gridInfoParam;
	}

	/**
	 * @return Returns the specimenEntries.
	 */
	public List getSpecimenEntries()
	{
		return this.specimenEntries;
	}

	/**
	 * @param specimenEntriesParam The specimenEntries to set.
	 */
	public void setSpecimenEntries(List specimenEntriesParam)
	{
		this.specimenEntries = specimenEntriesParam;
	}
}
