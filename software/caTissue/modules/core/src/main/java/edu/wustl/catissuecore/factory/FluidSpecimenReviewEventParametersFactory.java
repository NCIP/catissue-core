//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
//
//public class FluidSpecimenReviewEventParametersFactory extends AbstractReviewEventParametersFactory<FluidSpecimenReviewEventParameters>
//{
//
//	private static FluidSpecimenReviewEventParametersFactory fluidSpecimenReviewEventParametersFactory;
//
//	private FluidSpecimenReviewEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized FluidSpecimenReviewEventParametersFactory getInstance()
//	{
//		if(fluidSpecimenReviewEventParametersFactory==null){
//			fluidSpecimenReviewEventParametersFactory = new FluidSpecimenReviewEventParametersFactory();
//		}
//		return fluidSpecimenReviewEventParametersFactory;
//	}
//
//	public FluidSpecimenReviewEventParameters createClone(FluidSpecimenReviewEventParameters obj)
//	{
//		FluidSpecimenReviewEventParameters fluidSpecimenReviewEventParameters = createObject();
//		return fluidSpecimenReviewEventParameters;
//	}
//
//
//	public FluidSpecimenReviewEventParameters createObject()
//	{
//		FluidSpecimenReviewEventParameters fluidSpecimenReviewEventParameters =  new FluidSpecimenReviewEventParameters();
//		initDefaultValues(fluidSpecimenReviewEventParameters);
//		return fluidSpecimenReviewEventParameters;
//	}
//	public void initDefaultValues(FluidSpecimenReviewEventParameters obj)
//	{
//	}
//
//	public void doRoundOff()
//	{
//
//	}
//
//}
