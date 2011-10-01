//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.ReceivedEventParameters;
//
//public class ReceivedEventParametersFactory extends AbstractSpecimenEventParametersFactory<ReceivedEventParameters>
//{
//	private static ReceivedEventParametersFactory receivedEventParametersFactory;
//
//	private ReceivedEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized ReceivedEventParametersFactory getInstance()
//	{
//		if(receivedEventParametersFactory==null){
//			receivedEventParametersFactory = new ReceivedEventParametersFactory();
//		}
//		return receivedEventParametersFactory;
//	}
//	public ReceivedEventParameters createClone(ReceivedEventParameters obj)
//	{
//		ReceivedEventParameters receivedEventParameters = createObject();
//		receivedEventParameters.setReceivedQuality(obj.getReceivedQuality());
//		receivedEventParameters.setComment(obj.getComment());
//		return receivedEventParameters;
//	}
//
//	public ReceivedEventParameters createObject()
//	{
//		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
//		initDefaultValues(receivedEventParameters);
//		return receivedEventParameters;
//	}
//
//	public void initDefaultValues(ReceivedEventParameters obj)
//	{
//	}
//}

