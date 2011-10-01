//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.ThawEventParameters;
//
//public class ThawEventParametersFactory extends AbstractSpecimenEventParametersFactory<ThawEventParameters>
//{
//	private static ThawEventParametersFactory thawEventParametersFactory;
//
//	private ThawEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized ThawEventParametersFactory getInstance()
//	{
//		if(thawEventParametersFactory==null){
//			thawEventParametersFactory = new ThawEventParametersFactory();
//		}
//		return thawEventParametersFactory;
//	}
//	public ThawEventParameters createClone(ThawEventParameters obj)
//	{
//		ThawEventParameters thawEventParameters = createObject();
//		return thawEventParameters;
//	}
//
//	public ThawEventParameters createObject()
//	{
//		ThawEventParameters thawEventParameters = new ThawEventParameters();
//		initDefaultValues(thawEventParameters);
//		return thawEventParameters;
//	}
//
//	public void initDefaultValues(ThawEventParameters obj)
//	{
//	}
//}
