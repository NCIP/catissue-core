//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
//
//public class EmbeddedEventParametersFactory extends AbstractSpecimenEventParametersFactory<EmbeddedEventParameters>
//{
//	private static EmbeddedEventParametersFactory embeddedEventParametersFactory;
//
//	private EmbeddedEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized EmbeddedEventParametersFactory getInstance()
//	{
//		if(embeddedEventParametersFactory==null){
//			embeddedEventParametersFactory = new EmbeddedEventParametersFactory();
//		}
//		return embeddedEventParametersFactory;
//	}
//
//	public EmbeddedEventParameters createClone(EmbeddedEventParameters obj) {
//
//		EmbeddedEventParameters embeddedEventParameters = createObject();
//		return embeddedEventParameters;
//	}
//
//	public EmbeddedEventParameters createObject()
//	{
//		EmbeddedEventParameters embeddedEventParameters = new EmbeddedEventParameters();
//		initDefaultValues(embeddedEventParameters);
//		return embeddedEventParameters;
//	}
//
//	public void initDefaultValues(EmbeddedEventParameters obj)
//	{
//	}
//
//}
