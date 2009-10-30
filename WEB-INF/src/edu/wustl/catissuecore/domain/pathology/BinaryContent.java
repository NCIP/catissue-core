
package edu.wustl.catissuecore.domain.pathology;

/**
 * Represents binary content of the pathology report.
 * @hibernate.joined-subclass table="CATISSUE_REPORT_BICONTENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class BinaryContent extends ReportContent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 2927694650319915989L;
	/**
	 * Surgical Pathology report of the current binary data.
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;

	/**
	 * Constructor.
	 */
	public BinaryContent()
	{
		super();
	}

	/**
	 * @return surgical pathology report of current binary data.
	 * @hibernate.many-to-one name="surgicalPathologyReport"
	 *                        class="edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport"
	 *                        column="REPORT_ID" not-null="false"
	 */
	public SurgicalPathologyReport getSurgicalPathologyReport()
	{
		return this.surgicalPathologyReport;
	}

	/**
	 * @param surgicalPathologyReport
	 *            sets the surgical pathology report of current binary content.
	 */
	public void setSurgicalPathologyReport(SurgicalPathologyReport surgicalPathologyReport)
	{
		this.surgicalPathologyReport = surgicalPathologyReport;
	}

}