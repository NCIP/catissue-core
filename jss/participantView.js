 var specimenCombo;
 var scgCombo;


 function initComboForSpecimenLabels()
	{
	    dhtmlx.skin ='dhx_skyblue';
            specimenCombo = new dhtmlXCombo("specimenLabels", "specimenLabels1", 240);
	}
	
 /*function initComboForAddSpecimenEvents()
 {
		dhtmlx.skin ='dhx_skyblue';
		var comboObject = new dhtmlXCombo("addSpecimenEvents", "addSpecimenEvents", 240);
		comboObject.addOption([[1, 'Fixed'], [2, 'Check in Check Out'], [3, 'Fluid Specimen Review'], [4, 'Cell Specimen Review']]);
		comboObject.enableFilteringMode(true);
 }*/

  function addNewScg()
 {
   var participantId=document.getElementById("pId").value;
   var cpId=document.getElementById("cpId").value;
   var action="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroupCPQuery&cpId="+cpId+"&pId="+participantId+"&requestFrom=participantView";
   document.forms[0].action=action;
   document.forms[0].submit();
 }


function editScg()
{
 var participantId=document.getElementById("pId").value;
 var cpId=document.getElementById("cpId").value;
  var scgId = scgCombo.getSelectedValue();
  var action="";
 
 if(scgId == null)
 {
  alert("Please select a SCG to edit.");
 }
 else
 {
  action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=true&operation=edit&id="+scgId  
  +"&cpSearchParticipantId="+participantId+"&cpSearchCpId="+cpId;
 }

 document.forms[0].action=action;
 document.forms[0].submit();
}


function editSpecimen()
{
 var specimenId = specimenCombo.getSelectedValue();
 var participantId=document.getElementById("pId").value;
 var cpId=document.getElementById("cpId").value;
 if(specimenId == null)
{
  alert("Select specimen to edit.");
}
 
 else
 { 
    action = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+specimenId+"&refresh=true&cpSearchParticipantId="+participantId
         +"&cpSearchCpId="+cpId;
 }
 document.forms[0].action=action;
 document.forms[0].submit();

}

function createAliquote()
{
 var specimenId = specimenCombo.getSelectedValue();
 var label = specimenCombo.getSelectedText();
 var isSubmit = true;
 var action = "";
/*if((count==null ||count=="") || (quantity==null || quantity==""))
{ 
 alert("Count is required");
 isSubmit = false;
}*/
if(isSubmit)
{
 action = "CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&specimenLabel="+label+"&requestFrom=participantView&parentSpecimenId="+specimenId;
}

 document.forms[0].action=action;
 document.forms[0].submit();
}

function createDerivative()
{
 var count = document.getElementById("derivative_count").value;
 var specimenId = specimenCombo.getSelectedValue();
 var label = specimenCombo.getSelectedText();
 var nodeId = "Specimen_"+specimenId;
 var scgId = scgCombo.getSelectedValue();
 var action = "";
 if(count=="1")
 {
   action = "CPQueryCreateSpecimen.do?pageOf=pageOfCreateSpecimenCPQuery&parentSpecimenId="+specimenId+"&parentLabel="+label+"&scgId="+scgId+"&operation=add";
 }
 else //check if greater than 1
 {
  refreshTree(null,null,null,null,nodeId);
  action = "MultipleSpecimenFlexInitAction.do?operation=add&pageOf=pageOfMultipleSpWithoutMenu&parentType=Derived_Specimen&numberOfSpecimens="+count 
    +"&parentLabel="+label;
 }
 
 document.forms[0].action=action;
 document.forms[0].submit();
}


function onScgSelect()
{
 var value = this.getSelectedValue();
// alert("value"+value);
 var sel_text_id = "scg_"+this.getSelectedValue();
 var text;


if(document.getElementById(sel_text_id)!=null)
{ 
  text = document.getElementById(sel_text_id).innerHTML;
}
else
{
  text = this.getSelectedText();
}
 
 this.setComboText(text);
 getSpecimenLabelsforSCG(value);
}

function onScgClick()
{
this.setComboText("");

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

function populateSpecimenLabelsCombo(response)
 {
    dhtmlx.skin ='dhx_skyblue';
    
    specimenCombo.clearAll(); 
	specimenCombo.addOption(eval(response)); 
	specimenCombo.enableFilteringMode(true);
  }

//Shoe participant Edit page
function showEditPage()
{
	var participantId=document.getElementById("pId").value;
	var cpId=document.getElementById("cpId").value;
	action="QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=search&id="+participantId+"&cpSearchCpId="+cpId;	
	document.forms[0].action=action;
	document.forms[0].submit();
}

// Handles Collect Specimen button click in scg details block.
function collectSpecimen()
{
 var scgId = scgCombo.getSelectedValue();
if(scgId==null || scgId=="")
{
 alert("Select Scg to Collect Specimen.");
}
else
{
 var nodeId = "SpecimenCollectionGroup_"+scgId;
 var action = "AnticipatorySpecimenView.do?scgId="+scgId;
 document.forms[0].action=action;
 document.forms[0].submit();
 refreshTree(null,null,null,null,nodeId);
}
}