
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
/**
 * @author
 *
 */
public class DoNothingDeidentifier extends AbstractDeidentifier
{
	/**
	 * @param identifiedReport : identifiedReport
	 * @return DeidentifiedSurgicalPathologyReport : DeidentifiedSurgicalPathologyReport
	 */
	public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport)
	{
		DeidentifiedSurgicalPathologyReport deidentifiedReport = new DeidentifiedSurgicalPathologyReport();
		TextContent textContent = new TextContent();
		if (identifiedReport.getTextContent() != null)
		{
			textContent.setData(identifiedReport.getTextContent().getData());
		}
		deidentifiedReport.setTextContent(textContent);
		textContent.setSurgicalPathologyReport(deidentifiedReport);
		return deidentifiedReport;
	}
	/**
	 * init.
	 */
	public void initialize()
	{

	}
	/**
	 * stutdown.
	 */
	public void shutdown()
	{

	}

}
