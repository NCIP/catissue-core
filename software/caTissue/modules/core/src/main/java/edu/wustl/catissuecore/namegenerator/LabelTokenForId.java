/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.namegenerator;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.KeySequenceGeneratorUtil;
import edu.wustl.catissuecore.util.global.*;

// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForId.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForId  extends DefaultSpecimenLabelGenerator implements LabelTokens
{

	public LabelTokenForId() throws ApplicationException
	{
		
	}

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
	 * java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object) throws ApplicationException
	{
		KeySequenceGeneratorUtil kseq=KeySequenceGeneratorUtil.getInstance();
		Long newLabel = kseq.getNextUniqeId(Constants.SYS_UID_KEY, Constants.SYS_UID_TYPE);
		return String.valueOf(newLabel);
	}
}
