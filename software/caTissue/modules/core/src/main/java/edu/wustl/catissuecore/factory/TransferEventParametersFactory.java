package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.TransferEventParameters;

public class TransferEventParametersFactory extends AbstractSpecimenEventParametersFactory<TransferEventParameters>
{
	private static TransferEventParametersFactory transferEventParametersFactory;

	private TransferEventParametersFactory()
	{
		super();
	}

	public static synchronized TransferEventParametersFactory getInstance()
	{
		if(transferEventParametersFactory==null){
			transferEventParametersFactory = new TransferEventParametersFactory();
		}
		return transferEventParametersFactory;
	}
	public TransferEventParameters createClone(TransferEventParameters obj)
	{
		TransferEventParameters TransferEventParameters = createObject();
		return TransferEventParameters;
	}

	public TransferEventParameters createObject()
	{
		TransferEventParameters transferEventParameters = new TransferEventParameters();
		initDefaultValues(transferEventParameters);
		return transferEventParameters;
	}

	public void initDefaultValues(TransferEventParameters obj)
	{
	}

}
