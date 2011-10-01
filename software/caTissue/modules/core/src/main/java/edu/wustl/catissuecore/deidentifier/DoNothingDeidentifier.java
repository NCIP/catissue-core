
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;

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
		InstanceFactory<DeidentifiedSurgicalPathologyReport> instFact = DomainInstanceFactory.getInstanceFactory(DeidentifiedSurgicalPathologyReport.class);
		final DeidentifiedSurgicalPathologyReport
			deidentifiedReport = instFact.createObject();//new DeidentifiedSurgicalPathologyReport();
		InstanceFactory<TextContent> textContentInstFact = DomainInstanceFactory.getInstanceFactory(TextContent.class);
		final TextContent textContent = textContentInstFact.createObject();//new TextContent();
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
		//
	}

	/**
	 * stutdown.
	 */
	@Override
	public void shutdown()
	{
		//
	}

}
