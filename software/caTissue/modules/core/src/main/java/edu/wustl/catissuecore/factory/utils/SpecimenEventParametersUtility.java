package edu.wustl.catissuecore.factory.utils;

import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;

public class SpecimenEventParametersUtility
{
	public static void doRoundOff(SpecimenEventParameters specimenEventParam)
	{
//		if(specimenEventParam instanceof CellSpecimenReviewParameters)
//		{
//			CellSpecimenReviewParameters cellSpecReviewParam = (CellSpecimenReviewParameters) specimenEventParam;
//
//			if (cellSpecReviewParam.getNeoplasticCellularityPercentage() != null)
//			{
//				cellSpecReviewParam.setNeoplasticCellularityPercentage(AppUtility.roundOff(cellSpecReviewParam.getNeoplasticCellularityPercentage() , Constants.QUANTITY_PRECISION));
//			}
//			if (cellSpecReviewParam.getViableCellPercentage() != null)
//			{
//				cellSpecReviewParam.setViableCellPercentage(AppUtility.roundOff(cellSpecReviewParam.getViableCellPercentage(), Constants.QUANTITY_PRECISION));
//			}
//		}
//	   else if(specimenEventParam instanceof FluidSpecimenReviewEventParameters)
//		{
//			FluidSpecimenReviewEventParameters fluidSpecReviewParam = (FluidSpecimenReviewEventParameters)specimenEventParam;
//			if (fluidSpecReviewParam.getCellCount() != null)
//			{
//				fluidSpecReviewParam.setCellCount(AppUtility.roundOff(fluidSpecReviewParam.getCellCount(), Constants.QUANTITY_PRECISION));
//			}
//		}
//		else if(specimenEventParam instanceof MolecularSpecimenReviewParameters)
//		{
//			MolecularSpecimenReviewParameters molecularSpecReviewParam = (MolecularSpecimenReviewParameters)specimenEventParam;
//
//			if (molecularSpecReviewParam.getAbsorbanceAt260()!= null)
//			{
//				molecularSpecReviewParam.setAbsorbanceAt260(AppUtility.roundOff(molecularSpecReviewParam.getAbsorbanceAt260(), Constants.QUANTITY_PRECISION));
//			}
//			if (molecularSpecReviewParam.getAbsorbanceAt280()!= null)
//			{
//				molecularSpecReviewParam.setAbsorbanceAt280(AppUtility.roundOff(molecularSpecReviewParam.getAbsorbanceAt280(), Constants.QUANTITY_PRECISION));
//			}
//			if (molecularSpecReviewParam.getRatio28STo18S()!= null)
//			{
//				molecularSpecReviewParam.setRatio28STo18S(AppUtility.roundOff(molecularSpecReviewParam.getRatio28STo18S(), Constants.QUANTITY_PRECISION));
//			}
//		}
//		else if(specimenEventParam instanceof SpunEventParameters)
//		{
//			SpunEventParameters spunEventParam = (SpunEventParameters)specimenEventParam;
//			if (spunEventParam.getGravityForce() != null)
//			{
//				spunEventParam.setGravityForce(AppUtility.roundOff(spunEventParam.getGravityForce(), Constants.QUANTITY_PRECISION));
//			}
//
//		}
//		else if(specimenEventParam instanceof TissueSpecimenReviewEventParameters)
//		{
//			TissueSpecimenReviewEventParameters tissueSpecReviewParam = (TissueSpecimenReviewEventParameters)specimenEventParam;
//
//			if (tissueSpecReviewParam.getNeoplasticCellularityPercentage()!= null)
//			{
//				tissueSpecReviewParam.setNeoplasticCellularityPercentage(AppUtility.roundOff(tissueSpecReviewParam.getNeoplasticCellularityPercentage(), Constants.QUANTITY_PRECISION));
//			}
//			if (tissueSpecReviewParam.getNecrosisPercentage()!= null)
//			{
//				tissueSpecReviewParam.setNecrosisPercentage(AppUtility.roundOff(tissueSpecReviewParam.getNecrosisPercentage(), Constants.QUANTITY_PRECISION));
//			}
//			if (tissueSpecReviewParam.getLymphocyticPercentage()!= null)
//			{
//				tissueSpecReviewParam.setLymphocyticPercentage(AppUtility.roundOff(tissueSpecReviewParam.getLymphocyticPercentage(), Constants.QUANTITY_PRECISION));
//			}
//			if (tissueSpecReviewParam.getTotalCellularityPercentage()!= null)
//			{
//				tissueSpecReviewParam.setTotalCellularityPercentage(AppUtility.roundOff(tissueSpecReviewParam.getTotalCellularityPercentage(), Constants.QUANTITY_PRECISION));
//			}
//
//		}
	}

	public static SpecimenEventParameters createClone(SpecimenEventParameters eventParameter)
	{
//		if (eventParameter instanceof CheckInCheckOutEventParameter)
//		{
//			CheckInCheckOutEventParameter obj= new CheckInCheckOutEventParameter();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setStorageStatus(((CheckInCheckOutEventParameter) eventParameter).getStorageStatus());
//			return obj;
//		}
//		if (eventParameter instanceof CollectionEventParameters)
//		{
//			CollectionEventParameters obj= new CollectionEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setCollectionProcedure(((CollectionEventParameters) eventParameter).getCollectionProcedure());
//			obj.setContainer(((CollectionEventParameters) eventParameter).getContainer());
//			return obj;
//		}
//		else if (eventParameter instanceof EmbeddedEventParameters)
//		{
//			EmbeddedEventParameters obj= new EmbeddedEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setEmbeddingMedium(((EmbeddedEventParameters) eventParameter).getEmbeddingMedium());
//			return obj;
//		}
//		else if (eventParameter instanceof FixedEventParameters)
//		{
//			FixedEventParameters obj= new FixedEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setDurationInMinutes(((FixedEventParameters) eventParameter).getDurationInMinutes());
//			obj.setFixationType(((FixedEventParameters) eventParameter).getFixationType());
//			return obj;
//		}
//		else if (eventParameter instanceof FrozenEventParameters)
//		{
//			FrozenEventParameters obj= new FrozenEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setMethod(((FrozenEventParameters) eventParameter).getMethod());
//			return obj;
//		}
//		else if (eventParameter instanceof ReceivedEventParameters)
//		{
//			ReceivedEventParameters obj= new ReceivedEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setReceivedQuality(((ReceivedEventParameters) eventParameter).getReceivedQuality());
//			return obj;
//		}
//		else if (eventParameter instanceof TissueSpecimenReviewEventParameters)
//		{
//			TissueSpecimenReviewEventParameters obj= new TissueSpecimenReviewEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setHistologicalQuality(((TissueSpecimenReviewEventParameters) eventParameter).getHistologicalQuality());
//			obj.setLymphocyticPercentage(((TissueSpecimenReviewEventParameters) eventParameter).getLymphocyticPercentage());
//			obj.setNecrosisPercentage(((TissueSpecimenReviewEventParameters) eventParameter).getNecrosisPercentage());
//			obj.setNeoplasticCellularityPercentage(((TissueSpecimenReviewEventParameters) eventParameter).getNeoplasticCellularityPercentage());
//			obj.setTotalCellularityPercentage(((TissueSpecimenReviewEventParameters) eventParameter).getTotalCellularityPercentage());
//			return obj;
//		}
		 if (eventParameter instanceof TransferEventParameters)
		{
			TransferEventParameters obj= new TransferEventParameters();
			setSpecimenEventParamValues(eventParameter,obj);
			obj.setFromPositionDimensionOne(((TransferEventParameters) eventParameter).getFromPositionDimensionOne());
			obj.setFromPositionDimensionTwo(((TransferEventParameters) eventParameter).getFromPositionDimensionTwo());
			obj.setFromStorageContainer(((TransferEventParameters) eventParameter).getFromStorageContainer());
			obj.setToPositionDimensionOne(((TransferEventParameters) eventParameter).getToPositionDimensionOne());
			obj.setToPositionDimensionTwo(((TransferEventParameters) eventParameter).getToPositionDimensionTwo());
			obj.setToStorageContainer(((TransferEventParameters) eventParameter).getToStorageContainer());
			return obj;
		}
		else if (eventParameter instanceof DisposalEventParameters)
		{
			DisposalEventParameters obj= new DisposalEventParameters();
			setSpecimenEventParamValues(eventParameter,obj);
			obj.setReason(((DisposalEventParameters) eventParameter).getReason());
			return obj;
		}
//		else if (eventParameter instanceof SpunEventParameters)
//		{
//			SpunEventParameters obj= new SpunEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setGravityForce(((SpunEventParameters) eventParameter).getGravityForce());
//			obj.setDurationInMinutes(((SpunEventParameters) eventParameter).getDurationInMinutes());
//			return obj;
//		}
//		else if (eventParameter instanceof ProcedureEventParameters)
//		{
//			ProcedureEventParameters obj= new ProcedureEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			obj.setName(((ProcedureEventParameters) eventParameter).getName());
//			obj.setUrl(((ProcedureEventParameters) eventParameter).getUrl());
//			return obj;
//		}
//		else if (eventParameter instanceof ThawEventParameters)
//		{
//			ThawEventParameters obj= new ThawEventParameters();
//			setSpecimenEventParamValues(eventParameter,obj);
//			return obj;
//		}
//		else if (eventParameter instanceof FluidSpecimenReviewEventParameters)
//		{
//			FluidSpecimenReviewEventParameters obj= new FluidSpecimenReviewEventParameters();
//			obj.setCellCount(((FluidSpecimenReviewEventParameters) eventParameter).getCellCount());
//			setSpecimenEventParamValues(eventParameter,obj);
//			return obj;
//		}
//		else if (eventParameter instanceof CellSpecimenReviewParameters)
//		{
//			CellSpecimenReviewParameters obj= new CellSpecimenReviewParameters();
//			obj.setNeoplasticCellularityPercentage(
//				((CellSpecimenReviewParameters) eventParameter).getNeoplasticCellularityPercentage());
//			obj.setViableCellPercentage(
//				((CellSpecimenReviewParameters) eventParameter).getViableCellPercentage());
//			setSpecimenEventParamValues(eventParameter,obj);
//			return obj;
//		}
//		else if (eventParameter instanceof MolecularSpecimenReviewParameters)
//		{
//			MolecularSpecimenReviewParameters obj= new MolecularSpecimenReviewParameters();
//			obj.setGelImageURL(
//				((MolecularSpecimenReviewParameters) eventParameter).getGelImageURL());
//			obj.setQualityIndex(
//				((MolecularSpecimenReviewParameters) eventParameter).getQualityIndex());
//			obj.setLaneNumber(
//					((MolecularSpecimenReviewParameters) eventParameter).getLaneNumber());
//			obj.setGelNumber(
//					((MolecularSpecimenReviewParameters) eventParameter).getGelNumber());
//			obj.setAbsorbanceAt260(
//					((MolecularSpecimenReviewParameters) eventParameter).getAbsorbanceAt260());
//			obj.setAbsorbanceAt280(
//					((MolecularSpecimenReviewParameters) eventParameter).getAbsorbanceAt280());
//			obj.setRatio28STo18S(
//					((MolecularSpecimenReviewParameters) eventParameter).getRatio28STo18S());
//			setSpecimenEventParamValues(eventParameter,obj);
//			return obj;
//		}
		return null;
	}

	private static void setSpecimenEventParamValues(SpecimenEventParameters eventParameter,SpecimenEventParameters childObject)
	{
		childObject.setComment(eventParameter.getComment());
		childObject.setId(eventParameter.getId());
		childObject.setSpecimen(eventParameter.getSpecimen());
		childObject.setSpecimenCollectionGroup(eventParameter.getSpecimenCollectionGroup());
		childObject.setUser(eventParameter.getUser());
		childObject.setTimestamp(eventParameter.getTimestamp());
	}

}
