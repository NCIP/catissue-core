//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
//
//public class MolecularSpecimenReviewParametersFactory extends AbstractReviewEventParametersFactory<MolecularSpecimenReviewParameters>
//{
//	private static MolecularSpecimenReviewParametersFactory molecularSpecimenReviewParametersFactory;
//
//	private MolecularSpecimenReviewParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized MolecularSpecimenReviewParametersFactory getInstance()
//	{
//		if(molecularSpecimenReviewParametersFactory==null){
//			molecularSpecimenReviewParametersFactory = new MolecularSpecimenReviewParametersFactory();
//		}
//		return molecularSpecimenReviewParametersFactory;
//	}
//
//	public MolecularSpecimenReviewParameters createClone(MolecularSpecimenReviewParameters obj)
//	{
//		MolecularSpecimenReviewParameters molecularSpecimenReviewParameters = createObject();
//		return molecularSpecimenReviewParameters;
//	}
//
//
//	public MolecularSpecimenReviewParameters createObject()
//	{
//		MolecularSpecimenReviewParameters molecularSpecimenReviewParameters =  new MolecularSpecimenReviewParameters();
//		initDefaultValues(molecularSpecimenReviewParameters);
//		return molecularSpecimenReviewParameters;
//	}
//	public void initDefaultValues(MolecularSpecimenReviewParameters obj)
//	{
//	}
//
//	public void doRoundOff()
//	{
//
//	}
//
//}
