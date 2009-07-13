
package edu.wustl.catissuecore.reportloader;

import java.util.HashMap;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 *
 * interface for Parse.
 *
 */
public interface Parser
{

	/**
	 * @param fileName name of the file
	 * @throws Exception while parsing the report
	 */
	void parse(String fileName) throws Exception;

	/**
	 * Parse string.
	 * @param participant Participant object
	 * @param reportText report Text
	 * @param specimenCollectionGroup specimen Collection Group
	 * @param abbrToHeader abbrToHeader
	 * @throws Exception Exception while parsing the string.
	 */
	void parseString(Participant participant, String reportText,
			SpecimenCollectionGroup specimenCollectionGroup, HashMap<String, String> abbrToHeader)
			throws Exception;

}
