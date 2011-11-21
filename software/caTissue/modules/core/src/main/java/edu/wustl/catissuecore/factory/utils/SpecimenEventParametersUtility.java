package edu.wustl.catissuecore.factory.utils;

import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;

public class SpecimenEventParametersUtility
{
	public static SpecimenEventParameters createClone(SpecimenEventParameters eventParameter)
	{
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
