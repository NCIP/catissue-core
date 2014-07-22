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
			collectionProtocolEventCombo.setOptionWidth(177);
			collectionProtocolEventCombo.setSize(177);
			collectionProtocolEventCombo.attachEvent("onOpen",onComboClick);
			collectionProtocolEventCombo.attachEvent("onKeyPressed",onComboKeyPress);
			collectionProtocolEventCombo.attachEvent("onChange", function(){collectionProtocolEventCombo.DOMelem_input.focus();});
		collectionProtocolEventCombo.readonly(true,false);
		}
		scgCombo.clinicalStatusCombo = dhtmlXComboFromSelect("clinicalStatus");  
		scgCombo.clinicalStatusCombo.setOptionWidth(177);
		scgCombo.clinicalStatusCombo.setSize(177);
		scgCombo.clinicalStatusCombo.attachEvent("onOpen",onComboClick);
		scgCombo.clinicalStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);
		scgCombo.clinicalStatusCombo.attachEvent("onChange", function(){scgCombo.clinicalStatusCombo.DOMelem_input.focus();});
	
		scgCombo.collectionEventCollectionProcedureCombo = dhtmlXComboFromSelect("collectionEventCollectionProcedure");  
		scgCombo.collectionEventCollectionProcedureCombo.setOptionWidth(177);
		scgCombo.collectionEventCollectionProcedureCombo.setSize(177);
		scgCombo.collectionEventCollectionProcedureCombo.attachEvent("onOpen",onComboClick);
		scgCombo.collectionEventCollectionProcedureCombo.attachEvent("onKeyPressed",onComboKeyPress);
		scgCombo.collectionEventCollectionProcedureCombo.attachEvent("onChange", function(){scgCombo.collectionEventCollectionProcedureCombo.DOMelem_input.focus();});
		if(scgCombo.collectionEventCollectionProcedureCombo.getSelectedValue()=="-1"){
			scgCombo.collectionEventCollectionProcedureCombo.setComboValue("Use CP Defaults");
			scgCombo.collectionEventCollectionProcedureCombo.setComboText("Use CP Defaults");
		}
		
		scgCombo.collectionEventContainerCombo = dhtmlXComboFromSelect("collectionEventContainer");  
		scgCombo.collectionEventContainerCombo.setOptionWidth(177);
		scgCombo.collectionEventContainerCombo.setSize(177);
		scgCombo.collectionEventContainerCombo.attachEvent("onOpen",onComboClick);
		scgCombo.collectionEventContainerCombo.attachEvent("onKeyPressed",onComboKeyPress);
		scgCombo.collectionEventContainerCombo.attachEvent("onChange", function(){scgCombo.collectionEventContainerCombo.DOMelem_input.focus();});
		if(scgCombo.collectionEventContainerCombo.getSelectedValue()=="-1"){
			scgCombo.collectionEventContainerCombo.setComboValue("Use CP Defaults");
			scgCombo.collectionEventContainerCombo.setComboText("Use CP Defaults");
		}
		
		scgCombo.receivedEventReceivedQualityCombo = dhtmlXComboFromSelect("receivedEventReceivedQuality");  
		scgCombo.receivedEventReceivedQualityCombo.setOptionWidth(177);
		scgCombo.receivedEventReceivedQualityCombo.setSize(177);
		scgCombo.receivedEventReceivedQualityCombo.attachEvent("onOpen",onComboClick);
		scgCombo.receivedEventReceivedQualityCombo.attachEvent("onKeyPressed",onComboKeyPress);
		scgCombo.receivedEventReceivedQualityCombo.attachEvent("onChange", function(){scgCombo.receivedEventReceivedQualityCombo.DOMelem_input.focus();});
		if(scgCombo.receivedEventReceivedQualityCombo.getSelectedValue()=="-1"){
			scgCombo.receivedEventReceivedQualityCombo.setComboValue("Use CP Defaults");
			scgCombo.receivedEventReceivedQualityCombo.setComboText("Use CP Defaults");
		}

		scgCombo.activityStatusCombo = dhtmlXComboFromSelect("activityStatus");  
		scgCombo.activityStatusCombo.setOptionWidth(177);
		scgCombo.activityStatusCombo.setSize(177);
		scgCombo.activityStatusCombo.deleteOption('-- Select --');
		//scgCombo.activityStatusCombo.readonly(true,false);
		scgCombo.activityStatusCombo.attachEvent("onOpen",onComboClick);
		scgCombo.activityStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);
		
		scgCombo.activityStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=scgCombo.activityStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
				scgCombo.activityStatusCombo.attachEvent("onChange", function(){scgCombo.activityStatusCombo.DOMelem_input.focus();});
			});

		scgCombo.collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");  
		scgCombo.collectionStatusCombo.setOptionWidth(177);
		scgCombo.collectionStatusCombo.setSize(177);
		scgCombo.collectionStatusCombo.attachEvent("onOpen",onComboClick);
		scgCombo.collectionStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);
		
		
		var clinicalDiagnosisCombo = new dhtmlXCombo("clinicalDiagnosis","clinicalDiagnosis","100px");;
		clinicalDiagnosisCombo.setOptionWidth(177);
		clinicalDiagnosisCombo.setSize(177);
		var collectionProtocolId = document.getElementsByName('collectionProtocolId')[0].value;
		clinicalDiagnosisCombo.loadXML('ClinicalDiagnosisList.do?collectionProtocolId='+collectionProtocolId,function(){
			clinicalDiagnosisCombo.setComboText(clinicalDiagnosisValue);
			clinicalDiagnosisCombo.setComboValue(clinicalDiagnosisValue);
			clinicalDiagnosisCombo.DOMelem_input.title=clinicalDiagnosisValue;
		
		});
		
		clinicalDiagnosisCombo.attachEvent("onKeyPressed",function(){
			clinicalDiagnosisCombo.enableFilteringMode(true,'ClinicalDiagnosisList.do?collectionProtocolId='+collectionProtocolId,false);
			clinicalDiagnosisCombo.attachEvent("onChange", function(){clinicalDiagnosisCombo.DOMelem_input.focus();});
			});
			clinicalDiagnosisCombo.attachEvent("onOpen",onComboClick);
			
			clinicalDiagnosisCombo.attachEvent("onSelectionChange",function(){
 var diagnosisVal = clinicalDiagnosisCombo.getSelectedText();
			if(diagnosisVal)
				clinicalDiagnosisCombo.DOMelem_input.title=clinicalDiagnosisCombo.getSelectedText();
			else
				clinicalDiagnosisCombo.DOMelem_input.title='Start typing to see values';
 });
 clinicalDiagnosisCombo.attachEvent("onXLE",function (){clinicalDiagnosisCombo.addOption(clinicalDiagnosisValue,clinicalDiagnosisValue);});
 /*clinicalDiagnosisCombo.attachEvent("onBlur",function(){var diagnosisVal = clinicalDiagnosisCombo.getSelectedText();
 if(!diagnosisVal)
 {
	clinicalDiagnosisCombo.DOMelem_input.value="";
 }
 });
 clinicalDiagnosisCombo.DOMelem_input.onfocus=function(event)
 {
		var diagnosisVal = clinicalDiagnosisCombo.getSelectedText();
			if(!diagnosisVal)
				
				clinicalDiagnosisCombo.DOMelem_input.value='Start typing to see values';
 }*/
 dhtmlxEvent(clinicalDiagnosisCombo.DOMelem_input,"mouseover",function(){
     var diagnosisVal = clinicalDiagnosisCombo.getSelectedText();
			if(diagnosisVal){
				clinicalDiagnosisCombo.DOMelem_input.title=clinicalDiagnosisCombo.getSelectedText();}
			else
				clinicalDiagnosisCombo.DOMelem_input.title='Start typing to see values';
});
 /*clinicalDiagnosisCombo.attachEvent("onBlur", onBlurFunc);
function onBlurFunc() {alert(clinicalDiagnosisCombo.getLastSelectedValue());
	if(!clinicalDiagnosisCombo.getSelectedValue())
	{
		clinicalDiagnosisCombo.setComboValue("Not Specified");
	}
    return true;
}*/
		
		
		
		
		/*var collectionProtocolEventCombo = new dhtmlXCombo("collectionProtocolEventId","collectionProtocolEventId","100px");;
		collectionProtocolEventCombo.setOptionWidth(165);
		collectionProtocolEventCombo.setSize(165);
		*/
		
		
		scgCombo.collectionStatusCombo.attachEvent("onChange", 
			function()
			{
				var activityValue=scgCombo.collectionStatusCombo.getSelectedValue();
				checkNewActivityStatus(activityValue,'/QueryManageBioSpecimen.do');
				scgCombo.collectionStatusCombo.attachEvent("onChange", function(){scgCombo.collectionStatusCombo.DOMelem_input.focus();});
			});
			scgCombo.collectionEventTimeInHoursCombo = dhtmlXComboFromSelect("collectionEventTimeInHours");  
			scgCombo.collectionEventTimeInHoursCombo.setOptionWidth(40);
			scgCombo.collectionEventTimeInHoursCombo.setSize(40);
			scgCombo.collectionEventTimeInHoursCombo.attachEvent("onOpen",onComboClick);
			scgCombo.collectionEventTimeInHoursCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.collectionEventTimeInHoursCombo.attachEvent("onChange", function(){scgCombo.collectionEventTimeInHoursCombo.DOMelem_input.focus();});
		
			scgCombo.collectionEventTimeInMinutesCombo = dhtmlXComboFromSelect("collectionEventTimeInMinutes");  
			scgCombo.collectionEventTimeInMinutesCombo.setOptionWidth(40);
			scgCombo.collectionEventTimeInMinutesCombo.setSize(40);
			scgCombo.collectionEventTimeInMinutesCombo.attachEvent("onOpen",onComboClick);
			scgCombo.collectionEventTimeInMinutesCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.collectionEventTimeInMinutesCombo.attachEvent("onChange", function(){scgCombo.collectionEventTimeInMinutesCombo.DOMelem_input.focus();});
			
			scgCombo.receivedEventTimeInHoursCombo = dhtmlXComboFromSelect("receivedEventTimeInHours");  
			scgCombo.receivedEventTimeInHoursCombo.setOptionWidth(40);
			scgCombo.receivedEventTimeInHoursCombo.setSize(40);
			scgCombo.receivedEventTimeInHoursCombo.attachEvent("onOpen",onComboClick);
			scgCombo.receivedEventTimeInHoursCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.receivedEventTimeInHoursCombo.attachEvent("onChange", function(){scgCombo.receivedEventTimeInHoursCombo.DOMelem_input.focus();});
			
			scgCombo.receivedEventTimeInMinutesCombo = dhtmlXComboFromSelect("receivedEventTimeInMinutes");  
			scgCombo.receivedEventTimeInMinutesCombo.setOptionWidth(40);
			scgCombo.receivedEventTimeInMinutesCombo.setSize(40);
			scgCombo.receivedEventTimeInMinutesCombo.attachEvent("onOpen",onComboClick);
			scgCombo.receivedEventTimeInMinutesCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.receivedEventTimeInMinutesCombo.attachEvent("onChange", function(){scgCombo.receivedEventTimeInMinutesCombo.DOMelem_input.focus();});
			
			scgCombo.collectionEventUserIdCombo = dhtmlXComboFromSelect("collectionEventUserId");  
			scgCombo.collectionEventUserIdCombo.setOptionWidth(177);
			scgCombo.collectionEventUserIdCombo.setSize(177);
			scgCombo.collectionEventUserIdCombo.attachEvent("onOpen",onComboClick);
			scgCombo.collectionEventUserIdCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.collectionEventUserIdCombo.attachEvent("onChange", function(){scgCombo.collectionEventUserIdCombo.DOMelem_input.focus();});
			
			scgCombo.receivedEventUserIdCombo = dhtmlXComboFromSelect("receivedEventUserId");  
			scgCombo.receivedEventUserIdCombo.setOptionWidth(177);
			scgCombo.receivedEventUserIdCombo.setSize(177);
			scgCombo.receivedEventUserIdCombo.attachEvent("onOpen",onComboClick);
			scgCombo.receivedEventUserIdCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.receivedEventUserIdCombo.attachEvent("onChange", function(){scgCombo.receivedEventUserIdCombo.DOMelem_input.focus();});
			
			scgCombo.siteIdCombo = dhtmlXComboFromSelect("siteId");  
			scgCombo.siteIdCombo.setOptionWidth(177);
			scgCombo.siteIdCombo.setSize(177);
			scgCombo.siteIdCombo.attachEvent("onOpen",onComboClick);
			scgCombo.siteIdCombo.attachEvent("onKeyPressed",onComboKeyPress);
			scgCombo.siteIdCombo.attachEvent("onChange", function(){scgCombo.siteIdCombo.DOMelem_input.focus();});
			//scgCombo.siteIdCombo.enableFilteringMode('between');
			
			
			
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

function disableButtonsOnCheck(selectedRadio)
{
	var adhocSpCnt = document.getElementById('numberOfSpecimens');
	var restrictCheckbox = document.getElementById("restrictSCGCheckbox");

	if(selectedRadio.value==1)
		{
			adhocSpCnt.readOnly = true;
			restrictCheckbox.checked == true;
			adhocSelect = false;
			cpSelect = true;
			adhocSpCnt.value="";
			adhocSpCnt.style.border="0px";
		}
		else if(selectedRadio.value==2)
		{
			adhocSelect = true;
			adhocSpCnt.readOnly = false;
			
			adhocSpCnt.style.border="1px solid grey";
			
			restrictCheckbox.checked == false;
			cpSelect = false;
			adhocSpCnt.value="1";
			adhocSpCnt.focus();
		}
		else if(selectedRadio.value==3)
		{
			adhocSelect = false;
			cpSelect = false;
			adhocSpCnt.readOnly = true;
			restrictCheckbox.checked == false;
			adhocSpCnt.value="";
			adhocSpCnt.style.border="0px";
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

function loadSCGTabbar()
{
	scgTabbar = new dhtmlXTabBar("SCG_tabbar", "top",25);
	scgTabbar.setSkin('default');
	scgTabbar.setImagePath("dhtmlx_suite/imgs/");
	scgTabbar.setSkinColors("#FFFFFF", "#FFFFFF");
	if("edit" == operation)
	{
		scgTabbar.addTab("editSCGtab",'<span style="font-size:13px"> Edit SCG </span>', "150px");
		scgTabbar.addTab("reportsTab",'<span style="font-size:13px"> Report </span>', "150px");
		scgTabbar.addTab("annotationTab",'<span style="font-size:13px"> Annotations </span>',"150px");
		

		scgTabbar.setHrefMode("iframes-on-demand");
		scgTabbar.setContent("editSCGtab", "SCGDiv");
		scgTabbar.setContentHref("reportsTab",showViewSPRTab);
		scgTabbar.setContentHref("annotationTab",showAnnotationTab);
		
		scgTabbar.setTabActive("editSCGtab");
	}
	if("add" == operation)
	{
		scgTabbar.addTab("editSCGtab",'<span style="font-size:13px"> Add SCG </span>', "150px");
		
		scgTabbar.setHrefMode("iframes-on-demand");
		scgTabbar.setContent("editSCGtab", "SCGDiv");
		scgTabbar.setTabActive("editSCGtab");
	}
}
