
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;

public class DoNothingDeidentifier extends AbstractDeidentifier
{

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

	public void initialize()
	{

	}

	public void shutdown()
	{

	}

}
