//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
//
//public class CellSpecimenReviewParametersFactory extends AbstractReviewEventParametersFactory<CellSpecimenReviewParameters>
//{
//	private static CellSpecimenReviewParametersFactory cellSpecimenReviewParametersFactory;
//
//	private CellSpecimenReviewParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized CellSpecimenReviewParametersFactory getInstance()
//	{
//		if(cellSpecimenReviewParametersFactory==null){
//			cellSpecimenReviewParametersFactory = new CellSpecimenReviewParametersFactory();
//		}
//		return cellSpecimenReviewParametersFactory;
//
//	}
//
//	public CellSpecimenReviewParameters createClone(CellSpecimenReviewParameters obj)
//	{
//		CellSpecimenReviewParameters cellSpecimenReviewParameters = createObject();
//		return cellSpecimenReviewParameters;
//	}
//
//	public CellSpecimenReviewParameters createObject()
//	{
//		CellSpecimenReviewParameters cellSpecimenReviewParameters = new CellSpecimenReviewParameters();
//		return cellSpecimenReviewParameters;
//	}
//
//	public void initDefaultValues(CellSpecimenReviewParameters obj)
//	{
//	}
//
//	public void doRoundOff()
//	{
//	}
//}
