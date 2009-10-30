
package edu.wustl.catissuecore.domain.pathology;

import java.util.Set;

/**
 * Represents the text data of surgical pathology report.
 * @hibernate.joined-subclass table="CATISSUE_REPORT_TEXTCONTENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

public class TextContent extends ReportContent
{

	/**
	 *
	 */
	private static final long serialVersionUID = -8526356845606240757L;

	/**
	 * Collection of surgical pathology report sections.
	 */
	protected Set reportSectionCollection;

	/**
	 * Surgical Pathology report of the current text data.
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;

	/**
	 * Constructor.
	 */
	public TextContent()
	{
		super();
	}

	/**
	 * @return collection of report sections associated with the report.
	 * @hibernate.set inverse="false" table="REPORT_SECTION" lazy="false"
	 *                cascade="all"
	 * @hibernate.collection-key column="TEXT_CONTENT_ID"
	 * @hibernate.collection-one-to-many
	 *                                   class="edu.wustl.catissuecore.domain.pathology.ReportSection"
	 */
	public Set getReportSectionCollection()
	{
		return this.reportSectionCollection;
	}

	/**
	 * @param reportSectionCollection
	 *            sets the collection of report section.
	 */
	public void setReportSectionCollection(Set reportSectionCollection)
	{
		this.reportSectionCollection = reportSectionCollection;
	}

	/**
	 * @return surgical pathology report of current text data.
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
	 *            sets the surgical pathology report of current text content.
	 */
	public void setSurgicalPathologyReport(SurgicalPathologyReport surgicalPathologyReport)
	{
		this.surgicalPathologyReport = surgicalPathologyReport;
	}

}