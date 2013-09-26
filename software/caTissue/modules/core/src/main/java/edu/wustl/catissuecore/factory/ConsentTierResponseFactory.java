/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ConsentTierResponse;

public class ConsentTierResponseFactory implements InstanceFactory<ConsentTierResponse>
{
	private static ConsentTierResponseFactory consentTierResponseFactory;

	private ConsentTierResponseFactory()
	{
		super();
	}

	public static synchronized ConsentTierResponseFactory getInstance()
	{
		if(consentTierResponseFactory==null){
			consentTierResponseFactory = new ConsentTierResponseFactory();
		}
		return consentTierResponseFactory;
	}

	public ConsentTierResponse createClone(ConsentTierResponse obj)
	{
		ConsentTierResponse consentTierResponse = createObject();
		consentTierResponse.setResponse(obj.getResponse());
		consentTierResponse.setConsentTier(obj.getConsentTier());
		return consentTierResponse;
	}

	public ConsentTierResponse createObject()
	{
		ConsentTierResponse consentTierResponse = new ConsentTierResponse();
		initDefaultValues(consentTierResponse);
		return consentTierResponse;
	}

	public void initDefaultValues(ConsentTierResponse obj)
	{
	}
}
