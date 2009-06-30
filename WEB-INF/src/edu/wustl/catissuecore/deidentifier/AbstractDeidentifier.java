
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;

public abstract class AbstractDeidentifier extends Thread
{

	abstract public void initialize() throws Exception;

	abstract public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception;

	abstract public void shutdown() throws Exception;
}
