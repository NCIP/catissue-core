
package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 * Represents the identified surgical pathology report. 
 * @hibernate.joined-subclass table="CATISSUE_IDENTIFIED_REPORT" 
 * @hibernate.joined-subclass-key
 * column="IDENTIFIER"
 */

public class IdentifiedSurgicalPathologyReport extends SurgicalPathologyReport
{

	/**
	 * Deidentified surgical pathology report.
	 */
	protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;

	/**
	 * Specimen collection group of the report 
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * Constructor
	 */
	public IdentifiedSurgicalPathologyReport()
	{

	}

	/**
	 * @return deidentified report.	
	 * @hibernate.many-to-one  name="deidentifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 * column="DEID_REPORT"  not-null="false"
	 */

	public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport()
	{
		return deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * @param deidentifiedSurgicalPathologyReport sets deidentified report.
	 */
	public void setDeIdentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport)
	{
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}

	/**	
	 * @return specimen collection group of the report.
	 * @hibernate.many-to-one name="specimenCollectionGroup"
	 * class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 * column="SCG_ID" not-null="false"
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup sets specimen collection group of the identified report.
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

}