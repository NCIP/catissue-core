package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ConsentTier;

public class ConsentTierFactory implements InstanceFactory<ConsentTier>
{
	private static ConsentTierFactory consentTierFactory;

	private ConsentTierFactory()
	{
		super();
	}

	public static synchronized ConsentTierFactory getInstance()
	{
		if(consentTierFactory==null){
			consentTierFactory = new ConsentTierFactory();
		}
		return consentTierFactory;
	}

	public ConsentTier createClone(ConsentTier obj)
	{
		ConsentTier consentTier = createObject();
		if (obj.getId() != null && obj.getId().toString().trim().length() > 0)
		{
			consentTier.setId(obj.getId());
		}
		consentTier.setStatement(obj.getStatement());

		return consentTier;
	}

	public ConsentTier createObject()
	{
		ConsentTier consentTier = new ConsentTier();
		initDefaultValues(consentTier);
		return consentTier;
	}

	public void initDefaultValues(ConsentTier obj)
	{
	}
}
