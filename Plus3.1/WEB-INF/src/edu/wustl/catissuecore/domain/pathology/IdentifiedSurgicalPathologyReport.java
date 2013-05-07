
package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 * Represents the identified surgical pathology report.
 * @hibernate.joined-subclass table="CATISSUE_IDENTIFIED_REPORT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

public class IdentifiedSurgicalPathologyReport extends SurgicalPathologyReport
{

	/**
	 *
	 */
	private static final long serialVersionUID = -3942632327778301489L;

	/**
	 * Deidentified surgical pathology report.
	 */
	protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;

	/**
	 * Specimen collection group of the report.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * Constructor.
	 */
	public IdentifiedSurgicalPathologyReport()
	{
		super();
	}

	/**
	 * @return deidentified report.
	 * @hibernate.many-to-one name="deidentifiedSurgicalPathologyReport" class=
	 *                        "edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 *                        column="DEID_REPORT" not-null="false"
	 */

	public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport()
	{
		return this.deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * @param deIdentifiedSurgicalPathologyReport : sets deidentified report.
	 */
	public void setDeIdentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport)
	{
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * @return specimen collection group of the report.
	 * @hibernate.many-to-one name="specimenCollectionGroup"
	 *                        class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 *                        column="SCG_ID" not-null="false"
	 */
	@Override
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return this.specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup
	 *            sets specimen collection group of the identified report.
	 */
	@Override
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

}