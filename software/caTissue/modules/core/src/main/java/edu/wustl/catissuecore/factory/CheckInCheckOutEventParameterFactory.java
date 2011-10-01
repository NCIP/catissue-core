//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
//
//public class CheckInCheckOutEventParameterFactory  extends AbstractSpecimenEventParametersFactory<CheckInCheckOutEventParameter>{
//
//	private static CheckInCheckOutEventParameterFactory checkInCheckOutEventParameterFactory;
//
//	private CheckInCheckOutEventParameterFactory()
//	{
//		super();
//	}
//
//	public static  synchronized CheckInCheckOutEventParameterFactory getInstance()
//	{
//		if(checkInCheckOutEventParameterFactory==null){
//			checkInCheckOutEventParameterFactory = new CheckInCheckOutEventParameterFactory();
//		}
//		return checkInCheckOutEventParameterFactory;
//	}
//
//	public CheckInCheckOutEventParameter createClone(CheckInCheckOutEventParameter obj)
//	{
//		CheckInCheckOutEventParameter checkInCheckOutEventParameter = createObject();
//		return checkInCheckOutEventParameter;
//	}
//
//	public CheckInCheckOutEventParameter createObject()
//	{
//		CheckInCheckOutEventParameter checkInCheckOutEventParameter = new CheckInCheckOutEventParameter();
//		initDefaultValues(checkInCheckOutEventParameter);
//		return checkInCheckOutEventParameter;
//	}
//
//	public void initDefaultValues(CheckInCheckOutEventParameter obj)
//	{
//	}
//
//}
