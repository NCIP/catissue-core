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
