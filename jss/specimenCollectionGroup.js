var applyToSpecimen;
//Set last refresh time
if(window.parent!=null)
{
	if(window.parent.lastRefreshTime!=null)
	{
		window.parent.lastRefreshTime = new Date().getTime();
	}
}

function initializeSCGCombo()
{
		var clinicalStatusCombo = dhtmlXComboFromSelect("clinicalStatus");  
		clinicalStatusCombo.setOptionWidth(165);
		clinicalStatusCombo.setSize(165);
	
		var collectionEventCollectionProcedureCombo = dhtmlXComboFromSelect("collectionEventCollectionProcedure");  
		collectionEventCollectionProcedureCombo.setOptionWidth(165);
		collectionEventCollectionProcedureCombo.setSize(165);
		
		var collectionEventContainerCombo = dhtmlXComboFromSelect("collectionEventContainer");  
		collectionEventContainerCombo.setOptionWidth(165);
		collectionEventContainerCombo.setSize(165);
		
		
		var receivedEventReceivedQualityCombo = dhtmlXComboFromSelect("receivedEventReceivedQuality");  
		receivedEventReceivedQualityCombo.setOptionWidth(165);
		receivedEventReceivedQualityCombo.setSize(165);
		

		var activityStatusCombo = dhtmlXComboFromSelect("activityStatus");  
		activityStatusCombo.setOptionWidth(165);
		activityStatusCombo.setSize(165);
		
		activityStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=activityStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
			});

		var collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");  
		collectionStatusCombo.setOptionWidth(165);
		collectionStatusCombo.setSize(165);
		
		collectionStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=collectionStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
			});
		
			var collectionEventTimeInHoursCombo = dhtmlXComboFromSelect("collectionEventTimeInHours");  
			collectionEventTimeInHoursCombo.setOptionWidth(50);
			collectionEventTimeInHoursCombo.setSize(50);
			
			var collectionEventTimeInMinutesCombo = dhtmlXComboFromSelect("collectionEventTimeInMinutes");  
			collectionEventTimeInMinutesCombo.setOptionWidth(50);
			collectionEventTimeInMinutesCombo.setSize(50);
			
			var receivedEventTimeInHoursCombo = dhtmlXComboFromSelect("receivedEventTimeInHours");  
			receivedEventTimeInHoursCombo.setOptionWidth(50);
			receivedEventTimeInHoursCombo.setSize(50);
			
			var receivedEventTimeInMinutesCombo = dhtmlXComboFromSelect("receivedEventTimeInMinutes");  
			receivedEventTimeInMinutesCombo.setOptionWidth(50);
			receivedEventTimeInMinutesCombo.setSize(50);
}

function onRadioButtonClick(element)
{ 
	if(element.value == 1)
	{
		document.forms[0].participantId.disabled = false;
		document.forms[0].protocolParticipantIdentifier.disabled = true;
		document.forms[0].participantsMedicalIdentifierId.disabled = false;
	}
	else
	{
		document.forms[0].participantId.disabled = true;
		document.forms[0].protocolParticipantIdentifier.disabled = false;
		//disable Medical Record number field.
		document.forms[0].participantsMedicalIdentifierId.disabled = true;
	}
}

function changeAction(action)
{
	document.forms[0].action = action;
	document.forms[0].submit();
}

function checkForChanges()
{
	var collectionEventdateOfEvent = document.getElementById("collectionEventdateOfEvent").value;
	var collectionEventUserId = document.getElementById("collectionEventUserId").value;
	var collectionEventTimeInHours = document.getElementById("displaycollectionEventTimeInHours").value;
	var collectionEventTimeInMinutes = document.getElementById("displaycollectionEventTimeInMinutes").value;
	var collectionEventCollectionProcedure = document.getElementById("collectionEventCollectionProcedure").value;
	var collectionEventContainer = document.getElementById("collectionEventContainer").value;
	var collectionEventComments = document.getElementById("collectionEventComments").value;

	var receivedEventdateOfEvent;
	var currentReceivedDateForm;
	var recDate = document.getElementById("receivedEventdateOfEvent");
	if(recDate != null)
	{
		receivedEventdateOfEvent = recDate.value;
		 currentReceivedDateForm = document.getElementById("currentReceivedDateForm").value;
	}
	var receivedEventUserId = document.getElementById("receivedEventUserId").value;
	var receivedEventTimeInHours = document.getElementById("displayreceivedEventTimeInHours").value;
	var receivedEventTimeInMinutes = document.getElementById("displayreceivedEventTimeInMinutes").value;
	var receivedEventReceivedQuality = document.getElementById("receivedEventReceivedQuality").value;
	var receivedEventComments = document.getElementById("receivedEventComments").value;

	//Values from form
	var collectionEventdateOfEventForm = document.getElementById("collectionEventdateOfEventForm").value;
	var collectionEventUserIdForm = document.getElementById("collectionEventUserIdForm").value;
	var collectionEventTimeInHoursForm = document.getElementById("collectionEventTimeInHoursForm").value;
	var collectionEventTimeInMinutesForm = document.getElementById("collectionEventTimeInMinutesForm").value;
	var collectionEventCollectionProcedureForm = document.getElementById("collectionEventCollectionProcedureForm").value;
	var collectionEventContainerForm = document.getElementById("collectionEventContainerForm").value;
	var collectionEventCommentsForm = document.getElementById("collectionEventCommentsForm").value;

	var receivedEventUserIdForm = document.getElementById("receivedEventUserIdForm").value;

	var receivedEventTimeInHoursForm = document.getElementById("receivedEventTimeInHoursForm").value;
	var receivedEventTimeInMinutesForm = document.getElementById("receivedEventTimeInMinutesForm").value;
	var receivedEventReceivedQualityForm = document.getElementById("receivedEventReceivedQualityForm").value;
	var receivedEventCommentsForm = document.getElementById("receivedEventCommentsForm").value;

	if(collectionEventUserId == "")
	  collectionEventUserId = "0";

	if(receivedEventUserId == "")
	   receivedEventUserId = "0";

	if((collectionEventdateOfEvent != collectionEventdateOfEventForm)
		|| (collectionEventUserId != collectionEventUserIdForm)
		|| (collectionEventTimeInHours != collectionEventTimeInHoursForm)
		|| (collectionEventTimeInMinutes != collectionEventTimeInMinutesForm)
		|| (collectionEventCollectionProcedure != collectionEventCollectionProcedureForm)
		|| (collectionEventContainer != collectionEventContainerForm)
		|| (receivedEventUserId != receivedEventUserIdForm)
		|| (receivedEventdateOfEvent != currentReceivedDateForm)
		|| (receivedEventTimeInHours != receivedEventTimeInHoursForm)
		|| (receivedEventTimeInMinutes != receivedEventTimeInMinutesForm)
		|| (receivedEventReceivedQuality != receivedEventReceivedQualityForm)
		|| (collectionEventComments != collectionEventCommentsForm)
		|| (receivedEventComments != receivedEventCommentsForm))
	  {
		var appResources = "The collected and received events data that you have entered will be propagated to all specimens under this Specimen Collection Group and override any existing data. Do you want to continue?";
		var answer = confirm(appResources);
		if(answer)
		{
			applyToSpecimen = 'true';
		}
		else
		{
			applyToSpecimen = 'false';
		}
	 }
}

function confirmDisableForSCG(action,formField)
{
	var temp = action+"&applyToSpecimenValue="+applyToSpecimen;
	if((formField != undefined) && (formField.value == "Disabled"))
	{
		var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
		if (go==true)
		{
			if(document.forms[0].nextForwardTo.value!=null)
			{
			 temp = temp + "&domainObject=SCG&nextForwardTo="+document.forms[0].nextForwardTo.value;
			}
			document.forms[0].action = temp;
			document.forms[0].submit();
		}
	}
	else
	{
		document.forms[0].action = temp;
		document.forms[0].submit();
	}
}

function disablebuttons()
{
	var enteredValue = document.getElementById("numberOfSpecimen").value;
	var submitButton = document.getElementById("submitOnly");
	var submitAndAddButton = document.getElementById("submitAndAdd");
	// Patch ID: Bug#4245_4
	// Description: User is allowed to click the Add Multiple Specimen irrespective of state of restric checkbox.
	// Patch ID: Bug#3184_34
	var submitAndAddMultipleButton =  document.getElementById("submitAndAddMultiple");
	var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
	if(enteredValue > 1)
	{
		submitButton.disabled = true;
		submitAndAddButton.disabled = true;
		submitAndAddMultipleButton.disabled = false;
	}
	else if(restrictCheckbox.checked)
	{
		submitButton.disabled = true;
		submitAndAddButton.disabled = false;
		submitAndAddMultipleButton.disabled = false;
	}
	else
	{
		submitButton.disabled = true;
		submitAndAddButton.disabled = false;
		submitAndAddMultipleButton.disabled = false;
	}
}

function disableButtonsOnCheck(restrictCheckbox)
{
	var submitButton = document.getElementById("submitOnly");
	var addSpecimenButton = document.getElementById("submitAndAdd");
	// Patch ID: Bug#3184_35
	var submitAndAddMultipleButton = document.getElementById("submitAndAddMultiple");
	if(restrictCheckbox.checked)
	{
		submitButton.disabled = false;
		addSpecimenButton.disabled = true;
		submitAndAddMultipleButton.disabled = true;
	}
	else
	{
		disablebuttons();
		submitButton.disabled = true;
	}
}

function initializeSCGForm(formValue,getRestrictSCGCheckboxValue)
{
	if(formValue!=null)
	{
		var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
		//bug id: 4333
		var valueForCheckbox = getRestrictSCGCheckboxValue;
		if(valueForCheckbox!=null && valueForCheckbox == 'true')
		{
			disableButtonsOnCheck(restrictCheckbox);
		}
	}
}

function setButtonType(addButton)
{	
	document.getElementById("buttonType").value = addButton.id;
}

function specimencollgroup()
{
	switchToTab("specimenCollectionGroupTab");
}

//This function will switch page to consentPage
function consentPage(formValue,getConsentTierCounterValue)
{
	checkForConsents(formValue,getConsentTierCounterValue);
}

function checkForConsents(formValue,getConsentTierCounterValue)
{
	if(formValue!=null && getConsentTierCounterValue >0)
	{
		switchToTab("consentTab");
	}
	else
	{
		alert("No consents available for selected Specimen Collection Group");
	}
}

function showConsents(tabValue,formValue,getConsentTierCounterValue)
{
	var showConsents = tabValue;
	if(showConsents=="null" || showConsents=="scgPage")
	{
		specimencollgroup();
	}
	else
	{
		consentPage(formValue,getConsentTierCounterValue);	
	}
}

function viewSPR(reportIdValue,pageOfValue)
{
	var reportId=reportIdValue;
/*	if(reportId==null || reportId==-1)
	{		
		alert("There is no associate report in the system!");
	}
	else if(reportId==null || reportId==-2)
	{
		alert("Associated report is under quarantined request! Please contact administrator for further details.");
	}
	else */
		var action="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf="+pageOfValue+"&reportId="+reportId;
		document.forms[0].action=action;
		document.forms[0].submit();
}

function setSubmitted(forwardTo,printaction,nextforwardTo)
{
	var printFlag = document.getElementById("printCheckbox");
	if(printFlag.checked)
	{

	  setSubmittedForPrint(forwardTo,printaction,nextforwardTo);
	}
	else
	{
	  setSubmittedFor(forwardTo,nextforwardTo);
	}
}

function showAnnotations(scgEntityIdValue,idValue,staticEntityNameValue,pageOfValue)
{
	var action="DisplayAnnotationDataEntryPage.do?entityId="+scgEntityIdValue+"&entityRecordId="+idValue+"&staticEntityName="+staticEntityNameValue+"&pageOf="+pageOfValue+"&operation=viewAnnotations";
	document.forms[0].action=action;
	document.forms[0].submit();
}

