
package edu.wustl.catissuecore.domain.pathology;

/**
 * Represents xml content of the pathology report.
 * @hibernate.joined-subclass table="CATISSUE_REPORT_XMLCONTENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

public class XMLContent extends ReportContent
{

	/**
	 * Surgical Pathology report of the current text data.
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;

	/**
	 * constructor
	 */
	public XMLContent()
	{

	}

	/**
	 * @return surgical pathology report of current binary data.
	 * @hibernate.many-to-one name="surgicalPathologyReport"
	 *                        class="edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport"
	 *                        column="REPORT_ID" not-null="false"
	 */
	public SurgicalPathologyReport getSurgicalPathologyReport()
	{
		return surgicalPathologyReport;
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