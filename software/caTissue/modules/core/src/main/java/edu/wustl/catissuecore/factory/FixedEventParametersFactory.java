//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.FixedEventParameters;
//
//public class FixedEventParametersFactory extends AbstractSpecimenEventParametersFactory<FixedEventParameters>
//{
//	private static FixedEventParametersFactory fixedEventParametersFactory;
//
//	private FixedEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized FixedEventParametersFactory getInstance()
//	{
//		if(fixedEventParametersFactory==null){
//			fixedEventParametersFactory = new FixedEventParametersFactory();
//		}
//		return fixedEventParametersFactory;
//	}
//	public FixedEventParameters createClone(FixedEventParameters obj)
//	{
//		FixedEventParameters fixedEventParameters = createObject();
//		return fixedEventParameters;
//	}
//
//	public FixedEventParameters createObject()
//	{
//		FixedEventParameters fixedEventParameters = new FixedEventParameters();
//		initDefaultValues(fixedEventParameters);
//		return fixedEventParameters;
//	}
//
//	public void initDefaultValues(FixedEventParameters obj)
//	{
//	}
//}
