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

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This class which contains the default implementation for Protocol
 * Participant label generation.
 * @author sagar_baldwa
 */

public class ProtocolParticipantIdentifierLabelGenerator implements LabelGenerator
{

	/**
	 *@param object object
	 *@return null
	 */
	public String getLabel(Object object)
	{
		return null;
	}

	/**
	 * This method generates a label and sets it to CollectionProtocolRegistration
	 * domain object. Label is in the format "COLLECTIONPROTOCOLID_PARTICIPANTID"
	 * @param object Abstract domain object
	 */
	public void setLabel(Object object)
	{
		final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) object;
		if (cpr.getProtocolParticipantIdentifier() == null
				|| Constants.DOUBLE_QUOTES.equals(cpr.getProtocolParticipantIdentifier()))
		{
			cpr.setProtocolParticipantIdentifier(cpr.getCollectionProtocol().getId() + "_"
					+ cpr.getParticipant().getId());
		}
	}

	/**
	 *Set Label.
	 *@param object Abstract domain object
	 */
	public void setLabel(List object)
	{
		// TODO Auto-generated method stub
	}

	public void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		// TODO Auto-generated method stub

	}

	public void setLabel(Object object, boolean ignoreCollectedStatus) throws LabelGenException
	{
		// TODO Auto-generated method stub
		
	}
}