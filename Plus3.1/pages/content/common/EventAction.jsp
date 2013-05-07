<%!
// returns the url corresponding to the event selected
private String getEventAction(String event, String specimenId)
{
	String action = "";
	
	if(event.equalsIgnoreCase("Cell Specimen Review"))
		action = "CellSpecimenReviewParameters.do?operation=add&pageOf=pageOfCellSpecimenReviewParameters";
	else if(event.equalsIgnoreCase("Check In Check Out"))
		action = "CheckInCheckOutEventParameters.do?operation=add&pageOf=pageOfCheckInCheckOutEventParameters";
	else if(event.equalsIgnoreCase("Collection"))
		action = "CollectionEventParameters.do?operation=add&pageOf=pageOfCollectionEventParameters";
	else if(event.equalsIgnoreCase("Disposal"))
		action = "DisposalEventParameters.do?operation=add&pageOf=pageOfDisposalEventParameters";
	else if(event.equalsIgnoreCase("Embedded"))
		action = "EmbeddedEventParameters.do?operation=add&pageOf=pageOfEmbeddedEventParameters";
	else if(event.equalsIgnoreCase("Fixed"))
		action = "FixedEventParameters.do?operation=add&pageOf=pageOfFixedEventParameters";
	else if(event.equalsIgnoreCase("Fluid Specimen Review"))
		action = "FluidSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfFluidSpecimenReviewParameters";
	else if(event.equalsIgnoreCase("Frozen"))
		action = "FrozenEventParameters.do?operation=add&pageOf=pageOfFrozenEventParameters";
	else if(event.equalsIgnoreCase("Molecular Specimen Review"))
		action = "MolecularSpecimenReviewParameters.do?operation=add&pageOf=pageOfMolecularSpecimenReviewParameters";
	else if(event.equalsIgnoreCase("Procedure"))
		action = "ProcedureEventParameters.do?operation=add&pageOf=pageOfProcedureEventParameters";
	else if(event.equalsIgnoreCase("Received"))
		action = "ReceivedEventParameters.do?operation=add&pageOf=pageOfReceivedEventParameters";
	else if(event.equalsIgnoreCase("Spun"))
		action = "SpunEventParameters.do?operation=add&pageOf=pageOfSpunEventParameters";
	else if(event.equalsIgnoreCase("Thaw"))
		action = "ThawEventParameters.do?operation=add&pageOf=pageOfThawEventParameters";
	else if(event.equalsIgnoreCase("Tissue Specimen Review"))
		action = "TissueSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfTissueSpecimenReviewParameters";
	else if(event.equalsIgnoreCase("Transfer"))
	{
		action = "TransferEventParameters.do?operation=add&pageOf=pageOfTransferEventParameters";			
	}	
	action = action + "&specimenId=" + specimenId;

	return action;
}



%>