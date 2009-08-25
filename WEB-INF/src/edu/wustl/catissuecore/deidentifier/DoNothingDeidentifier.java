
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
	@Override
	public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport)
	{
		final DeidentifiedSurgicalPathologyReport deidentifiedReport = new DeidentifiedSurgicalPathologyReport();
		final TextContent textContent = new TextContent();
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
	@Override
	public void initialize()
	{

	}

	/**
	 * stutdown.
	 */
	@Override
	public void shutdown()
	{

	}

}
