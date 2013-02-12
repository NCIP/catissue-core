package edu.wustl.catissuecore.domain.pathology;



public class FileContent  extends ReportContent
{
	
	/**
	 *
	 */
	private static final long serialVersionUID = -8526356845606240757L;


	/**
	 * Surgical Pathology report of the current text data.
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;

	/**
	 * Constructor.
	 */
	public FileContent()
	{
		super();
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
