//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
//
//public class TissueSpecimenReviewEventParametersFactory extends AbstractReviewEventParametersFactory<TissueSpecimenReviewEventParameters>
//{
//	private static TissueSpecimenReviewEventParametersFactory tissueSpecimenReviewEventParametersFactory;
//
//	private TissueSpecimenReviewEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized TissueSpecimenReviewEventParametersFactory getInstance()
//	{
//		if(tissueSpecimenReviewEventParametersFactory==null){
//			tissueSpecimenReviewEventParametersFactory = new TissueSpecimenReviewEventParametersFactory();
//		}
//		return tissueSpecimenReviewEventParametersFactory;
//	}
//
//	public TissueSpecimenReviewEventParameters createClone(TissueSpecimenReviewEventParameters obj)
//	{
//		TissueSpecimenReviewEventParameters tissueSpecimenReviewEventParameters = createObject();
//		return tissueSpecimenReviewEventParameters;
//	}
//
//
//	public TissueSpecimenReviewEventParameters createObject()
//	{
//		TissueSpecimenReviewEventParameters tissueSpecimenReviewEventParameters =  new TissueSpecimenReviewEventParameters();
//		initDefaultValues(tissueSpecimenReviewEventParameters);
//		return tissueSpecimenReviewEventParameters;
//	}
//	public void initDefaultValues(TissueSpecimenReviewEventParameters obj)
//	{
//	}
//
//	public void doRoundOff()
//	{
//
//	}
//
//}
