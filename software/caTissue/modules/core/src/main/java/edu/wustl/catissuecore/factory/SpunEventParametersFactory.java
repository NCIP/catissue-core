//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.SpunEventParameters;
//
//public class SpunEventParametersFactory extends AbstractSpecimenEventParametersFactory<SpunEventParameters> {
//
//
//	private static SpunEventParametersFactory spunEventParametersFactory;
//
//	private SpunEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized SpunEventParametersFactory getInstance()
//	{
//		if(spunEventParametersFactory==null){
//			spunEventParametersFactory = new SpunEventParametersFactory();
//		}
//		return spunEventParametersFactory;
//	}
//	public SpunEventParameters createClone(SpunEventParameters obj)
//	{
//		SpunEventParameters spunEventParameters = createObject();
//		return spunEventParameters;
//	}
//
//	public SpunEventParameters createObject()
//	{
//		SpunEventParameters spunEventParameters = new SpunEventParameters();
//		initDefaultValues(spunEventParameters);
//		return spunEventParameters;
//	}
//
//	public void initDefaultValues(SpunEventParameters obj)
//	{
//	}
//}
