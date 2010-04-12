package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.Validator;


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


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String)
	 */
	public String getTokenValue(Object object, String token)
	{
		Specimen objSpecimen = (Specimen) object;
		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
	}
}
