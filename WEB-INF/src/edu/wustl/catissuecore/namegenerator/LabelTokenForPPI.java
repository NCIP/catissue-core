package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;


// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForPPI.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForPPI implements LabelTokens
{


	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
	 * java.lang.String, java.lang.Long)
	 */

	public String getTokenValue(Object object, String token, Long currVal)
	{
		Specimen objSpecimen = (Specimen) object;
		return objSpecimen.getSpecimenCollectionGroup().
		getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
	}
}
