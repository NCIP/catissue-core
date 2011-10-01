//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.CollectionEventParameters;
//
//public class CollectionEventParametersFactory extends AbstractSpecimenEventParametersFactory<CollectionEventParameters>
//{
//	private static CollectionEventParametersFactory collectionEventParametersFactory;
//
//	private CollectionEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized CollectionEventParametersFactory getInstance()
//	{
//		if(collectionEventParametersFactory==null){
//			collectionEventParametersFactory = new CollectionEventParametersFactory();
//		}
//		return collectionEventParametersFactory;
//	}
//
//	public CollectionEventParameters createClone(CollectionEventParameters obj)
//	{
//		CollectionEventParameters collectionEventParameters = createObject();
//
//		collectionEventParameters.setCollectionProcedure(obj.getCollectionProcedure());
//		collectionEventParameters.setContainer(obj.getContainer());
//		collectionEventParameters.setComment(obj.getComment());
//
//		return collectionEventParameters;
//	}
//
//	public CollectionEventParameters createObject()
//	{
//		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
//		initDefaultValues(collectionEventParameters);
//		return collectionEventParameters;
//	}
//
//	public void initDefaultValues(CollectionEventParameters obj)
//	{
//	}
//}
