 var specimenCombo;
 var eventCombo;
 var scgCombo;

 function initComboForSpecimenLabels()
	{
	    
            specimenCombo = new dhtmlXCombo("specimenLabels", "specimenLabels1", 240);
			
			var scgValue = scgCombo.getSelectedValue();
			if(scgValue)
			{
				getSpecimenLabelsforSCG(scgCombo.getSelectedValue());
			}
			else
			{
				
				specimenCombo.addOption(specLabelString);
				specimenCombo.enableFilteringMode(true);
	var count = specimenCombo.optionsArr.length;
				if(count==1) //select if only one item pesent
               {
                 specimenCombo.selectOption(0);
               }
			    specimenCombo.attachEvent("onSelectionChange",function(){specimenCombo.DOMelem_input.title=specimenCombo.getSelectedText();});
        specimenCombo.attachEvent("onOpen",onComboClick);
        specimenCombo.attachEvent("onKeyPressed",onComboKeyPress); 
			}
	}
	
 /*function initComboForAddSpecimenEvents()
 {
		
		var comboObject = new dhtmlXCombo("addSpecimenEvents", "addSpecimenEvents", 240);
		comboObject.addOption([[1, 'Fixed'], [2, 'Check in Check Out'], [3, 'Fluid Specimen Review'], [4, 'Cell Specimen Review']]);
		comboObject.enableFilteringMode(true);
 }*/

  function addNewScg()
 {
   var participantId=document.getElementById("pId").value;
   var cpId=document.getElementById("cpId").value;
   var eventId = eventCombo.getSelectedValue();
   if(eventId == null)
	{
	  alert("Select Event Point Label to add SCG.");
	  return;
	}
	else
	{
		var action="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroupCPQuery&cpId="+cpId+"&pId="+participantId+"&requestFrom=participantView&cpeId="+eventId;
		self.location=action;
		
	}
   
  }


function editScg()
{
 var participantId=document.getElementById("pId").value;
 var cpId=document.getElementById("cpId").value;
  var scgId = scgCombo.getSelectedValue();
  var action="";
 
 if(scgId == null && !scgId)
 {
  alert("Please select a SCG to edit.");
  return;
 }
 else
 {
  action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=true&operation=edit&id="+scgId  
  +"&cpSearchParticipantId="+participantId+"&cpSearchCpId="+cpId;
  self.location=action;
  
 }

 
 }


function editSpecimen()
{
 var specimenId = specimenCombo.getSelectedValue();
 var participantId=document.getElementById("pId").value;
 var cpId=document.getElementById("cpId").value;
  if(specimenId == null || specimenId == '')
{
  alert("Select specimen to edit.");
}
 
 else
 { 
    action = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+specimenId+"&refresh=true&cpSearchParticipantId="+participantId+"&cpSearchCpId="+cpId;
	self.location=action;
 }
 
}

function createAliquote()
{
 var specimenId = specimenCombo.getSelectedValue();
 var label = specimenCombo.getSelectedText();
 var noOfAliquotes = document.getElementById("noOfAliquots").value;
 var quantity = document.getElementById("quantityPerAliquot").value;
 if(specimenId == null || specimenId == '')
{
  alert("Select specimen to create Aliquots.");
  return false;
}
 if(isNaN(quantity))
 {
  quantity = "";
 }
 var isSubmit = true;
 var action = "";
if((noOfAliquotes==null ||noOfAliquotes=="" || noOfAliquotes == 'Count'))
{ 
 alert("Count is required");
 return false;
 isSubmit = false;
}
if(isSubmit)
{
	action = 'GetAliquotDetails.do?pageOf=fromSpecimen&parentSpecimentLabel='+label+"&aliquotCount="+noOfAliquotes+"&quantityPerAliquot="+quantity+"&searchBasedOn=label";
	self.location=action;
}

 
}

function createDerivative()
{
 var count = document.getElementById("derivative_count").value;
 var specimenId = specimenCombo.getSelectedValue();
 var label = specimenCombo.getSelectedText();
 var nodeId = "Specimen_"+specimenId;
 var scgId = eventCombo.getSelectedValue();
 var action = "";
 if(specimenId == null || specimenId == '')
{
  alert("Select specimen to create Derivative.");
  return false;
}
if((count==null ||count=="" || count == 'Count'))
{ 
 alert("Count is required");
 return false;
}
 else if(count=="1")
 {
  action = 'CPQueryCreateSpecimen.do?operation=add&pageOf=pageOfCreateSpecimenCPQuery&menuSelected=15&virtualLocated=true&requestFrom=participantView&parentLabel='+label+'&parentSpecimenId='+specimenId;
  self.location=action;
 }
 else if(count >= 1)//check if greater than 1
 {
  //refreshTree(null,null,null,null,nodeId);
  action = "MultipleSpecimenFlexInitAction.do?operation=add&pageOf=pageOfMultipleSpWithoutMenu&parentType=Derived_Specimen&numberOfSpecimens="+count 
    +"&parentLabel="+label;
	self.location=action;
 }
 
 
 
}


function onScgSelect()
{
 var value = this.getSelectedValue();
 scgCombo.DOMelem_input.title=scgCombo.getSelectedText();
 getSpecimenLabelsforSCG(value);
 getCPEForSCG(value);
}

function onEventSelect()
{
	var value = this.getSelectedValue();
	eventCombo.DOMelem_input.title=eventCombo.getSelectedText();
	getSCGLabelsForCPE(value);
	getSpecimenLabelsForCPE(value);
}

function getSpecimenLabelsForCPE(cpeId)
{
	var cprId = document.getElementById('cprId').value;
	var scgValue = scgCombo.getSelectedValue();
			if(!scgValue)
			{
				var request = newXMLHTTPReq();
	     var url = "ParticipantViewAjax.do";//AJAX action class
	     request.onreadystatechange = getReadyStateHandler(request,populateSpecimenLabelsCombo,true);//AJAX handler
	     request.open("POST", url, true);
	     request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     var dataToSend = "cpeId=" + cpeId+"&cprId="+cprId+"&method=getAllSpecimenLabels";
	     request.send(dataToSend);
			}
}
function onScgClick()
{
this.setComboText("");

}
var droupDownCounter = 0;

function getSCGLabelsForCPE(cpeId)
{
	var request = newXMLHTTPReq();
	var cprId = document.getElementById('cprId').value;
	     var url = "ParticipantViewAjax.do";//AJAX action class
		 droupDownCounter = 0;
	     request.onreadystatechange = getReadyStateHandler(request,populateSCGLabelsCombo,true);//AJAX handler
	     request.open("POST", url, true);
	     request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     var dataToSend = "cpeId=" + cpeId+"&cprId="+cprId+"&method=getSCGLabel";
	     request.send(dataToSend);
		 
}
function getCPEForSCG(scgId)
{
	var request = newXMLHTTPReq();
	var cprId = document.getElementById('cprId').value;
	     var url = "ParticipantViewAjax.do";//AJAX action class
		 droupDownCounter = 0;
	     request.onreadystatechange = getReadyStateHandler(request,populateCPE,true);//AJAX handler
	     request.open("POST", url, true);
	     request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     var dataToSend = "scgId=" + scgId+"&cprId="+cprId+"&method=getCPELabelsForSCG";
	     request.send(dataToSend);
		 
}
function populateCPE(response)
{
	if(droupDownCounter==0){
	var cpeId = eval(response);
	var count;
	for(i=0;i<eventCombo.optionsArr.length;i++)
	{
		eventCombo.optionsArr[i].value == cpeId;
		count = i;
		break;
	}
	
	/*
	var scgVal = scgCombo.getSelectedText();
	var scgTxt = scgCombo.getSelectedValue();
	eventCombo.setComboText(eventCombo.optionsArr[count].text);*/
	eventCombo.setComboValue(cpeId);
	
		
		droupDownCounter = 1;
	}
	
	//alert(response);
	
}
// Populate specimen label on scg select.
function getSpecimenLabelsforSCG(id)
{
         var request = newXMLHTTPReq();
	     var url = "ParticipantViewAjax.do";//AJAX action class
	     request.onreadystatechange = getReadyStateHandler(request,populateSpecimenLabelsCombo,true);//AJAX handler
	     request.open("POST", url, true);
	     request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     var dataToSend = "scgId=" + id+"&method=getSpecimenLabel";
	     request.send(dataToSend);
 }
function populateSCGLabelsCombo(response)
{
	if(droupDownCounter==0){
		var scgId = eval(response)[0].value;
		var count = scgCombo.optionsArr.length;
		if(count==1) //select if only one item pesent
	   {
		 scgCombo.selectOption(0);
	   }
		scgCombo.setComboValue(scgId);
		

		
		
	
	
	/*
	
	scgCombo.setComboText("");
        scgCombo.clearAll(); 
	scgCombo.addOption(eval(response)); 
	scgCombo.enableFilteringMode(true);
        scgCombo.attachEvent("onOpen",onComboClick);
        scgCombo.attachEvent("onKeyPressed",onComboKeyPress); 
	*/
		droupDownCounter = 1;
	}
}
function populateSpecimenLabelsCombo(response)
 {
        specimenCombo.setComboText("");
        specimenCombo.clearAll(); 
	specimenCombo.addOption(eval(response)); 
	specimenCombo.enableFilteringMode(true);
	var count = specimenCombo.optionsArr.length;
	if(count==1) //select if only one item pesent
               {
                 specimenCombo.selectOption(0);
               }
        specimenCombo.attachEvent("onOpen",onComboClick);
        specimenCombo.attachEvent("onKeyPressed",onComboKeyPress); 
           
  }

//Shoe participant Edit page
function showEditPage()
{
	var participantId=document.getElementById("pId").value;
	var cpId=document.getElementById("cpId").value;
	var action="QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=search&id="+participantId+"&cpSearchCpId="+cpId;
//alert(action);	
//$('#cpFrameNew').attr('src',action);
//document.getElementById('cpFrameNew').contentWindow.location=action;
self.location = action;
	//window.parent.frames["cpFrameNew"].location=action;
	
}

// Handles Collect Specimen button click in scg details block.
function collectSpecimen()
{
 var scgId = eventCombo.getSelectedValue();
if(scgId==null || scgId=="")
{
 alert("Select Scg to Collect Specimen.");
}
else
{
 var nodeId = "SpecimenCollectionGroup_"+scgId;
 var action = "AnticipatorySpecimenView.do?scgId="+scgId;
 self.location=action;
 //refreshTree(null,null,null,null,nodeId);
}
}

function inputFocus(i){
	if(i.value==i.defaultValue)
		{ 
			i.value="";
			i.style.color="#000";
		}
}

function inputBlur(i){
	if(i.value=="")
	{ 
		i.value=i.defaultValue;
		if(i.value == i.defaultValue)
			i.style.color="Silver";
	}
}
function initComboForSCGEvents()
{
 		        
                eventCombo = new dhtmlXCombo("eventsList", "addSCGEven1", 240);
                
		       eventCombo.attachEvent("onOpen",onComboClick);
eventCombo.attachEvent("onKeyPressed",onComboKeyPress);

				
               //initComboForSpecimenLabels();
			   
			   scgCombo = new dhtmlXCombo("scgList", "addSCGEven2", 240);
                scgCombo.addOption(scgLabels);    
		       scgCombo.attachEvent("onOpen",onComboClick);
scgCombo.attachEvent("onKeyPressed",onComboKeyPress);
                var count = scgCombo.optionsArr.length;
                if(count==1) //select if only one item pesent
               {
                 scgCombo.selectOption(0);
               }
			   eventCombo.addOption(eventPointLabels);    
                var count = eventCombo.optionsArr.length;
                if(count==1) //select if only one item pesent
               {
                 eventCombo.selectOption(0);
               }
               initComboForSpecimenLabels();
			    eventCombo.attachEvent("onChange",onEventSelect);
				 scgCombo.attachEvent("onChange",onScgSelect);
				/*  scgCombo.attachEvent("onKeyPressed", function(keyCode){alert(keyCode);});  
				   scgCombo.attachEvent("onOpen", function(){alert(keyCode);});  */
				   	       eventCombo.attachEvent("onOpen",onComboClick);
eventCombo.attachEvent("onKeyPressed",onComboKeyPress);
	       scgCombo.attachEvent("onOpen",onComboClick);
scgCombo.attachEvent("onKeyPressed",onComboKeyPress);
				
				populateParticipantName();
}
function populateParticipantName(){
	var participantName ="";
	if(summaryLastName!=""){
		participantName += summaryLastName+",";
	}
	participantName += summaryFirstName;
	document.getElementById("summaryParticipantName").innerHTML = participantName;
	
}
function createNewSpecimens()
{
var eventId = eventCombo.getSelectedValue();
var scgId = scgCombo.getSelectedValue();
var participantId=document.getElementById("pId").value;
 var cpId=document.getElementById("cpId").value;
 var radios = document.getElementsByName("specimenChild");
 var isPlanned = '';
 var specimenCount = '';
	var checkedRadio;
    for (var i = 0; i < radios.length; i++) {       
        if (radios[i].checked) {
            checkedRadio=radios[i].value;
            break;
        }
    }
switch(checkedRadio)
	{
		case '1' :
		isPlanned = true;
		break;
		
		case '2' :
		isPlanned = false;
		specimenCount = document.getElementById('numberOfSpecimens').value;
		break;
	}		
var action="";
if(!scgId && !eventId)
{
	alert('Please select Event Label Point or SCG from the dropdown.');
	return;
}
if(scgId && scgId != '')
{
	action="QueryCollectSpecimenFromDashBoard.do?operation=editSCG&scgId="+scgId+"&cpSearchParticipantId="+participantId+"&cpSearchCpId="+cpId+"&isPlanned="+isPlanned+"&numberOfSpecimens="+specimenCount+"&pageOf=newParticipantViewPage";
}
else if(eventId)
{
	action="QueryCollectSpecimenFromDashBoard.do?operation=addSCG&cpeId="+eventId+"&cpSearchParticipantId="+participantId+"&cpSearchCpId="+cpId+"&isPlanned="+isPlanned+"&numberOfSpecimens="+specimenCount+"&pageOf=newParticipantViewPage";
}
self.location=action;

}
