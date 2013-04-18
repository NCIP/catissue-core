var applyToSpecimen;
//Set last refresh time
if(window.parent!=null)
{
	if(window.parent.lastRefreshTime!=null)
	{
		window.parent.lastRefreshTime = new Date().getTime();
	}
}
var scgCombo={};

function initializeSCGCombo(operation)
{
		if(operation =='add'){
			var collectionProtocolEventCombo = dhtmlXComboFromSelect("collectionProtocolEventId");
			collectionProtocolEventCombo.setOptionWidth(165);
			collectionProtocolEventCombo.setSize(165);
		
		}
		scgCombo.clinicalStatusCombo = dhtmlXComboFromSelect("clinicalStatus");  
		scgCombo.clinicalStatusCombo.setOptionWidth(165);
		scgCombo.clinicalStatusCombo.setSize(165);
	
		scgCombo.collectionEventCollectionProcedureCombo = dhtmlXComboFromSelect("collectionEventCollectionProcedure");  
		scgCombo.collectionEventCollectionProcedureCombo.setOptionWidth(165);
		scgCombo.collectionEventCollectionProcedureCombo.setSize(165);
		if(scgCombo.collectionEventCollectionProcedureCombo.getSelectedValue()=="-1"){
			scgCombo.collectionEventCollectionProcedureCombo.setComboValue("Use CP Defaults");
			scgCombo.collectionEventCollectionProcedureCombo.setComboText("Use CP Defaults");
		}
		
		scgCombo.collectionEventContainerCombo = dhtmlXComboFromSelect("collectionEventContainer");  
		scgCombo.collectionEventContainerCombo.setOptionWidth(165);
		scgCombo.collectionEventContainerCombo.setSize(165);
		if(scgCombo.collectionEventContainerCombo.getSelectedValue()=="-1"){
			scgCombo.collectionEventContainerCombo.setComboValue("Use CP Defaults");
			scgCombo.collectionEventContainerCombo.setComboText("Use CP Defaults");
		}
		
		scgCombo.receivedEventReceivedQualityCombo = dhtmlXComboFromSelect("receivedEventReceivedQuality");  
		scgCombo.receivedEventReceivedQualityCombo.setOptionWidth(165);
		scgCombo.receivedEventReceivedQualityCombo.setSize(165);
		if(scgCombo.receivedEventReceivedQualityCombo.getSelectedValue()=="-1"){
			scgCombo.receivedEventReceivedQualityCombo.setComboValue("Use CP Defaults");
			scgCombo.receivedEventReceivedQualityCombo.setComboText("Use CP Defaults");
		}

		scgCombo.activityStatusCombo = dhtmlXComboFromSelect("activityStatus");  
		scgCombo.activityStatusCombo.setOptionWidth(165);
		scgCombo.activityStatusCombo.setSize(165);
		
		scgCombo.activityStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=scgCombo.activityStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
			});

		scgCombo.collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");  
		scgCombo.collectionStatusCombo.setOptionWidth(165);
		scgCombo.collectionStatusCombo.setSize(165);
		
		
		var clinicalDiagnosisCombo = new dhtmlXCombo("clinicalDiagnosis","clinicalDiagnosis","100px");;
		clinicalDiagnosisCombo.setOptionWidth(165);
		clinicalDiagnosisCombo.setSize(165);
		var collectionProtocolId = document.getElementsByName('collectionProtocolId')[0].value;
		clinicalDiagnosisCombo.loadXML('ClinicalDiagnosisList.do?collectionProtocolId='+collectionProtocolId );
		clinicalDiagnosisCombo.setComboText(clinicalDiagnosisValue);
		clinicalDiagnosisCombo.setComboValue(clinicalDiagnosisValue);
		clinicalDiagnosisCombo.enableFilteringMode(true,'ClinicalDiagnosisList.do?collectionProtocolId='+collectionProtocolId,false);
		
		/*var collectionProtocolEventCombo = new dhtmlXCombo("collectionProtocolEventId","collectionProtocolEventId","100px");;
		collectionProtocolEventCombo.setOptionWidth(165);
		collectionProtocolEventCombo.setSize(165);
		*/
		
		
		scgCombo.collectionStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=scgCombo.collectionStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
			});
			scgCombo.collectionEventTimeInHoursCombo = dhtmlXComboFromSelect("collectionEventTimeInHours");  
			scgCombo.collectionEventTimeInHoursCombo.setOptionWidth(50);
			scgCombo.collectionEventTimeInHoursCombo.setSize(50);
		
			scgCombo.collectionEventTimeInMinutesCombo = dhtmlXComboFromSelect("collectionEventTimeInMinutes");  
			scgCombo.collectionEventTimeInMinutesCombo.setOptionWidth(50);
			scgCombo.collectionEventTimeInMinutesCombo.setSize(50);
			
			scgCombo.receivedEventTimeInHoursCombo = dhtmlXComboFromSelect("receivedEventTimeInHours");  
			scgCombo.receivedEventTimeInHoursCombo.setOptionWidth(50);
			scgCombo.receivedEventTimeInHoursCombo.setSize(50);
			
			scgCombo.receivedEventTimeInMinutesCombo = dhtmlXComboFromSelect("receivedEventTimeInMinutes");  
			scgCombo.receivedEventTimeInMinutesCombo.setOptionWidth(50);
			scgCombo.receivedEventTimeInMinutesCombo.setSize(50);
			
			scgCombo.collectionEventUserIdCombo = dhtmlXComboFromSelect("collectionEventUserId");  
			scgCombo.collectionEventUserIdCombo.setOptionWidth(165);
			scgCombo.collectionEventUserIdCombo.setSize(165);
			
			scgCombo.receivedEventUserIdCombo = dhtmlXComboFromSelect("receivedEventUserId");  
			scgCombo.receivedEventUserIdCombo.setOptionWidth(165);
			scgCombo.receivedEventUserIdCombo.setSize(165);
			
			scgCombo.siteIdCombo = dhtmlXComboFromSelect("siteId");  
			scgCombo.siteIdCombo.setOptionWidth(165);
			scgCombo.siteIdCombo.setSize(165);
			
			
			
			/*var clinicalDignosisCombo = dhtmlXComboFromSelect("clinicalDiagnosis");  
			clinicalDignosisCombo.setOptionWidth(50);
			clinicalDignosisCombo.setSize(50);
			clinicalDignosisCombo.setSize(50);*/
			
			
			
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
	var collectionEventUserId=scgCombo.collectionEventUserIdCombo.getSelectedText();
	var collectionEventTimeInHours=scgCombo.collectionEventTimeInHoursCombo.getSelectedText();
	var collectionEventTimeInMinutes=scgCombo.collectionEventTimeInMinutesCombo.getSelectedText();
	var collectionEventCollectionProcedure =scgCombo.collectionEventCollectionProcedureCombo.getSelectedText();
	var collectionEventContainer=scgCombo.collectionEventContainerCombo.getSelectedText();
	var collectionEventComments = document.getElementById("collectionEventComments").value;
	var receivedEventdateOfEvent;
	var currentReceivedDateForm;
	var recDate = document.getElementById("receivedEventdateOfEvent");
	if(recDate != null)
	{
		receivedEventdateOfEvent = recDate.value;
		 currentReceivedDateForm = document.getElementById("currentReceivedDateForm").value;
	}
	var receivedEventUserId =scgCombo.receivedEventUserIdCombo.getSelectedText();
	var receivedEventTimeInHours=scgCombo.receivedEventTimeInHoursCombo.getSelectedText();
	var receivedEventTimeInMinutes=scgCombo.receivedEventTimeInMinutesCombo.getSelectedText();
	var receivedEventReceivedQuality=scgCombo.receivedEventReceivedQualityCombo.getSelectedText();
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

function viewSCGSPR(reportIdValue,pageOfValue,scgid)
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
		var action="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf="+pageOfValue+"&reportId="+reportId+"&id="+scgid;
		document.location=action;
	//	document.forms[0].submit();
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
