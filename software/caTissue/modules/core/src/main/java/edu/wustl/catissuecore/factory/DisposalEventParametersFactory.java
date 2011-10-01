package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.DisposalEventParameters;

public class DisposalEventParametersFactory extends
		AbstractSpecimenEventParametersFactory<DisposalEventParameters>
{

	private static DisposalEventParametersFactory disposalEventParametersFactory;

	private DisposalEventParametersFactory()
	{
		super();
	}

	public static synchronized DisposalEventParametersFactory getInstance()
	{
		if(disposalEventParametersFactory==null){
			disposalEventParametersFactory = new DisposalEventParametersFactory();
		}
		return disposalEventParametersFactory;
	}

	public DisposalEventParameters createClone(DisposalEventParameters obj)
	{
		DisposalEventParameters disposalEventParameters = createObject();
		return disposalEventParameters;
	}

	public DisposalEventParameters createObject()
	{
		DisposalEventParameters disposalEventParameters = new DisposalEventParameters();
		initDefaultValues(disposalEventParameters);
		return disposalEventParameters;
	}

	public void initDefaultValues(DisposalEventParameters obj)
	{
	}

}
