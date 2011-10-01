//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.FrozenEventParameters;
//
//public class FrozenEventParametersFactory extends AbstractSpecimenEventParametersFactory<FrozenEventParameters>
//{
//	private static FrozenEventParametersFactory frozenEventParametersFactory;
//
//	private FrozenEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized FrozenEventParametersFactory getInstance()
//	{
//		if(frozenEventParametersFactory==null){
//			frozenEventParametersFactory = new FrozenEventParametersFactory();
//		}
//		return frozenEventParametersFactory;
//	}
//	public FrozenEventParameters createClone(FrozenEventParameters obj)
//	{
//		FrozenEventParameters frozenEventParameters = createObject();
//		return frozenEventParameters;
//	}
//
//	public FrozenEventParameters createObject()
//	{
//		FrozenEventParameters frozenEventParameters = new FrozenEventParameters();
//		initDefaultValues(frozenEventParameters);
//		return frozenEventParameters;
//	}
//
//	public void initDefaultValues(FrozenEventParameters obj)
//	{
//	}
//}
