function ajaxCall(dataToSend, collectionProtocolId, identifier,anchorTagKey,index,consentCheckStatus)
{
	if(flag==true)
	{
		return;
	}
	flag=true;
	var request = newXMLHTTPReq();
	request.onreadystatechange=function(){checkForConsents(request, collectionProtocolId, identifier,anchorTagKey,index,consentCheckStatus)};
	//send data to ActionServlet
	//Open connection to servlet
	request.open("POST","CheckConsents.do",true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	request.send(dataToSend);
}

function textLimit(field) 
{
	if(field.value.length>0) 
		field.value = field.value.replace(/[^\d]+/g, '');
}
function intOnly(field) 
{
	if(field.value.length>0) 
	{
		field.value = field.value.replace(/[^\d]+/g, ''); 
	}
}

function deselectParticipant()
{
	var rowCount = mygrid.getRowsNum();
	for(i=1;i<=rowCount;i++)
	{
		var cl = mygrid.cells(i,0);
		cl.setChecked(false);
	}
}

function LookupAgain()
{
	document.forms[0].radioValue.value="Lookup";
	document.forms[0].forwardTo.value="ForwardTo";
	document.forms[0].participantId.value="";
	document.forms[0].id.value="";
	deselectParticipant();
}

function onVitalStatusRadioButtonClick(element)
{
	if(element.value == "Dead")
	{
		document.forms[0].deathDate.disabled = false;				
	}
	else
	{
		document.forms[0].deathDate.disabled = true;
	}
}		 

//This function is called when user clicks on 'Use Selected Participant' Button
function UseSelectedParticipant()
{
	if(document.forms[0].participantId.value=="" || document.forms[0].participantId.value=="0")
	{
		alert("Please select the Participant from the list");
	}
	else
	{
		document.forms[0].action="ParticipantSelect.do?operation=add&id="+document.forms[0].participantId.value;
		alert(document.forms[0].action);
		document.forms[0].submit();
	}
}

function checkActivityStatusForCPR()
{
	var collectionProtocolRegistrationVal = parseInt(document.forms[0].collectionProtocolRegistrationValueCounter.value);
	var isAllActive = true;
	for(i = 1 ; i <= collectionProtocolRegistrationVal ; i++)
	{
		var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_activityStatus)";
		if((document.getElementById(name) != undefined) && document.getElementById(name).value=="Disabled")
		{
			isAllActive = false;
			var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
			if (go==true)
			{	document.forms[0].submit();	}
			else
			{	break;	}
		}
	}
	if (isAllActive==true)
	{	document.forms[0].submit();	}
}
function showMessage(titleMessage)
{
	Tip(titleMessage,BGCOLOR,'#FFFFFF',BORDERCOLOR,'#000000',FONTCOLOR,'#000000',WIDTH,'30',FOLLOWMOUSE,'FALSE');
}
		