
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;

/**
 * @author
 *
 */
public abstract class AbstractDeidentifier extends Thread
{

	/**
	 * @throws Exception : Exception
	 */
	abstract public void initialize() throws Exception;

	/**
	 * @param identifiedReport : identifiedReport
	 * @return DeidentifiedSurgicalPathologyReport
	 * @throws Exception : Exception
	 */
	abstract public DeidentifiedSurgicalPathologyReport deidentify(
			IdentifiedSurgicalPathologyReport identifiedReport) throws Exception;

	/**
	 * @throws Exception : Exception
	 */
	abstract public void shutdown() throws Exception;
}
