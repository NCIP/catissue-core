var typeCombo;
var classNameCombo;
var collectionStatusCombo;
function initCombo()
{
	    classNameCombo = dhtmlXComboFromSelect("className");
		//classNameCombo.setOptionWidth(203);
		classNameCombo.setSize(203);
		classNameCombo.attachEvent("onChange", function(){onSpecimenTypeChange(this);validateAndProcessDeriveComboData(this);});
		classNameCombo.attachEvent("onOpen",onComboClick);
		classNameCombo.attachEvent("onKeyPressed",onComboKeyPress);
	//	classNameCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});

		
		typeCombo = dhtmlXComboFromSelect("type");
		//typeCombo.setOptionWidth(203);
		typeCombo.setSize(203);
		typeCombo.attachEvent("onChange", function(){validateAndProcessDeriveComboData(this);});
		typeCombo.attachEvent("onOpen",onComboClick);
		typeCombo.attachEvent("onKeyPressed",onComboKeyPress);
		setDefaultText("extIdName",defaultTextForExtIdName);
	    setDefaultText("extIdValue",defaultTextForExtIdValue);

	//	typeCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
}
var pathologicalStatusCombo;
var tissueSiteCombo;
var tissueSideCombo;
function initSpecimenCombo()
{
		tissueSiteCombo = dhtmlXComboFromSelect("tissueSite");
		tissueSiteCombo.setOptionWidth(202);
		tissueSiteCombo.setSize(202);
		tissueSiteCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
//		tissueSiteCombo.enableFilteringMode(true);
		tissueSiteCombo.attachEvent("onOpen",onComboClick);
		tissueSiteCombo.attachEvent("onKeyPressed",onComboKeyPress);

		tissueSideCombo = dhtmlXComboFromSelect("tissueSide");
		tissueSideCombo.setOptionWidth(203);
		tissueSideCombo.setSize(203);
		tissueSideCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
		tissueSideCombo.attachEvent("onOpen",onComboClick);
		tissueSideCombo.attachEvent("onKeyPressed",onComboKeyPress);

		pathologicalStatusCombo = dhtmlXComboFromSelect("pathologicalStatus");
		pathologicalStatusCombo.setOptionWidth(203);
		pathologicalStatusCombo.setSize(203);
		pathologicalStatusCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
		pathologicalStatusCombo.attachEvent("onOpen",onComboClick);
		pathologicalStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);

		collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");
		collectionStatusCombo.setOptionWidth(203);
		collectionStatusCombo.setSize(203);
		collectionStatusCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
		collectionStatusCombo.attachEvent("onOpen",onComboClick);
		collectionStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);
		
		classNameCombo = dhtmlXComboFromSelect("className");
		classNameCombo.setOptionWidth(203);
		classNameCombo.setSize(203);
		classNameCombo.attachEvent("onChange", function(){onSpecimenTypeChange(this); validateAndProcessComboData(this);});
		classNameCombo.attachEvent("onOpen",onComboClick);
		classNameCombo.attachEvent("onKeyPressed",onComboKeyPress);

		typeCombo = dhtmlXComboFromSelect("type");
		typeCombo.setOptionWidth(203);
		typeCombo.setSize(203);
		typeCombo.attachEvent("onChange", function(){onSpecimenSubTypeChange(); validateAndProcessComboData(this);});
		typeCombo.attachEvent("onOpen",onComboClick);
		typeCombo.attachEvent("onKeyPressed",onComboKeyPress);

		if(document.getElementById('activityStatus')!=null)
		{
			var activityStatusCombo = dhtmlXComboFromSelect("activityStatus");
			activityStatusCombo.setOptionWidth(203);
			activityStatusCombo.setSize(203);
			activityStatusCombo.attachEvent("onBlur", function(){processComboData(this.name,this.getSelectedText());});
			activityStatusCombo.attachEvent("onOpen",onComboClick);
			activityStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);
		}

}

function showAddExternalIdDiv()
{
	document.getElementById('addExternalIdDiv').style.display="block";
}

function showAddBioHazardDiv()
{
	document.getElementById('addBioHazardDiv').style.display="block";
}

var editElement;
var cnt="100";
function addEditExtIdTag(buttonElement)
{
	var nameObj = document.getElementById('extIdName');
	var valueObj = document.getElementById('extIdValue');
	
	if(nameObj.value!="" && nameObj.value!=defaultTextForExtIdName && valueObj.value!="" && valueObj.value!=defaultTextForExtIdValue)
	{	
		if(document.getElementById('addEditExtIdButton').value==="Add")
		{	
				var ul = document.getElementById('externalIDList');
				var li = document.createElement("li");
				li.title="Edit";
				var span = document.createElement("span");
				span.onclick=function(){editTag(this)};
				span.id="Ext_"+ cnt++;
				span.setAttribute("name","ExtIds");
				span.appendChild(document.createTextNode(nameObj.value+" - "+valueObj.value));
				li.appendChild(span);
				
				var a = document.createElement("a");
				a.title="Delete";
				a.onclick=function(){deleteTag(this)};
				a.appendChild(document.createTextNode("X"));
				li.appendChild(a);
				
				var hidden = document.createElement("input");
				hidden.type="hidden";
				hidden.name=span.id+"Status";
				hidden.id=span.id+"Status";
				hidden.value="ADD";
				li.appendChild(hidden);
				
				ul.appendChild(li);
				
				setDefaultText("extIdName",defaultTextForExtIdName);
				setDefaultText("extIdValue",defaultTextForExtIdValue);
			
		}else{
				editElement.firstChild.nodeValue=nameObj.value+" - "+valueObj.value;
				document.getElementById('addEditExtIdButton').value="Add";
				setDefaultText("extIdName",defaultTextForExtIdName);
				setDefaultText("extIdValue",defaultTextForExtIdValue);
				
				var hidden = document.getElementById(editElement.id+"Status");
				if(hidden.value!="ADD")
				{
					hidden.value="EDIT";
				}
		}
	}
	
	document.getElementById('addExternalIdDiv').style.display="none";
}

function deleteTag(e)
{
	var agree=confirm("Are you sure you want to delete it?");
	if(agree)
	{
		e.parentNode.parentNode.removeChild(e.parentNode);
	}
}

function editTag(e)
{
	var n = e.firstChild.nodeValue.split(" - "); 
	
	document.getElementById('extIdName').value=n[0];
	document.getElementById('extIdName').style.color = "#000";
	document.getElementById('extIdValue').value=n[1];
	document.getElementById('extIdValue').style.color = "#000";
	
	document.getElementById('addEditExtIdButton').value="Edit";
	
	document.getElementById('extIdName').focus();
	showAddExternalIdDiv();
	
	editElement = e;
}

function populateBiohazardTypeOptions()
{
	var myData= new Array();
	myData = JSON.parse(biohazardNameListJSON);
	if(myData.length==0)
		return;
	
	var typeList = new Array();
	for(var i=0;i<myData.length;i++) {
		typeList.push(myData[i].type);
	}
	typeList= getSortedUniqueArrayElements(typeList);
	for(var i=0;i<typeList.length;i++) {
		biohazardTypeCombo.addOption(typeList[i],typeList[i]);
	}
}

function onBiohazardTypeSelected()
{
	biohazardCombo.clearAll();
	biohazardCombo.setComboText(defaultTextForBioName);
	var myData=JSON.parse(biohazardNameListJSON);
	for(var i=0;i<myData.length;i++) {
		if(biohazardTypeCombo.getSelectedValue()==myData[i].type || biohazardTypeCombo.getComboText()==myData[i].type)
			biohazardCombo.addOption(myData[i].id+"_"+myData[i].type,myData[i].name);
	}
}

function addEditBioHazTag(buttonElement)
{
	if(biohazardCombo.getSelectedValue()!=null)
	{	var idTypeForBiohazard = biohazardCombo.getSelectedValue().split("_");
		var biohazard = idTypeForBiohazard[1]+" - "+biohazardCombo.getSelectedText();
				
		var ul = document.getElementById('bioHazardList');
		var li = document.createElement("li");
		li.title="Edit";
		var span = document.createElement("span");
		span.onclick=function(){editBiohazardTag(this)};
		span.id="Bio_"+idTypeForBiohazard[0];
		span.setAttribute("name","Biohazards");
		span.appendChild(document.createTextNode(biohazard));
		li.appendChild(span);

		var hidden = document.createElement("input");
		hidden.type="hidden";
		hidden.name=span.id+"Status";
		hidden.id=span.id+"Status";
		hidden.value="ADD";
		li.appendChild(hidden);

		var a = document.createElement("a");
		a.title="Delete";
		a.onclick=function(){deleteTag(this)};
		a.appendChild(document.createTextNode("X"));
		li.appendChild(a);

		ul.appendChild(li);

		if(buttonElement.value=="Edit")
		{
			buttonElement.value="Add";
			editElement.parentNode.parentNode.removeChild(editElement.parentNode);
		}
	}
	document.getElementById('addBioHazardDiv').style.display="none";
	biohazardCombo.setComboText(defaultTextForBioName);
	biohazardCombo.unSelectOption();
	biohazardTypeCombo.setComboText(defaultTextForBioType);
	biohazardTypeCombo.unSelectOption();
	}

function editBiohazardTag(e)
{
	var n = e.firstChild.nodeValue.split(" - ");
	
	document.getElementById('addEditBioHazButton').value="Edit";
	biohazardTypeCombo.unSelectOption();
	biohazardTypeCombo.setComboText(n[0]);
	onBiohazardTypeSelected();
	biohazardCombo.setComboText(n[1]);
	showAddBioHazardDiv();
	
	editElement = e;
}

function setDefaultText(id,defaultText){
	// Reference our element
	var txtContent  = document.getElementById(id);
	// Set our default text
	//var defaultText = "Please enter a value.";

	// Set default state of input
	txtContent.value = defaultText;
	txtContent.style.color = "#CCC";

	// Apply onfocus logic
	txtContent.onfocus = function() {
	  // If the current value is our default value
	  if (this.value == defaultText) {
		// clear it and set the text color to black
		this.value = "";
		this.style.color = "#000";
	  }
	}

	// Apply onblur logic
	txtContent.onblur = function() {
	  // If the current value is empty
	  if (this.value == "") {
		// set it to our default value and lighten the color
		this.value = defaultText;
		this.style.color = "#CCC";
	  }
	}
}

function createExtIdJSON()
{
	var externalIdJSON = new Array();
	var extIds = externalIDList.getElementsByTagName("span");
	for(var i=0;i<extIds.length;i++)
	{
		var e = extIds[i];
		var n = e.firstChild.nodeValue.split(" - "); 
		var id = e.id.split("_")[1];
		var status = document.getElementById(e.id+'Status').value;
		if(status=="ADD")
		{
			id=null;
		}
		externalIdJSON.push({ 
			"id"	:id,
			"name"	:n[0],
			"value"	:n[1],
			"status":status
		});
	}
	return externalIdJSON;
}

function createBioHazardJSON()
{
	var biohazardJSON = new Array();
	var biohazards = bioHazardList.getElementsByTagName("span");
	for(var i=0;i<biohazards.length;i++)
	{
		var e = biohazards[i];
		var n = e.firstChild.nodeValue.split(" - "); 
		var id = e.id.split("_")[1];
		var status = document.getElementById(e.id+'Status').value;
		biohazardJSON.push({
			"id"	:id,
			"type"	:n[0],
			"name"	:n[1],
			"status":status
		});
	}
	return biohazardJSON;
}

var defaultTextForExtIdName = "Enter Identifier Name";
var defaultTextForExtIdValue = "Enter Identifier Value";
var defaultTextForBioName = "Select Biohazard Name";
var defaultTextForBioType = "Select Biohazard Type";
var biohazardCombo;
var biohazardTypeCombo;
var biohazardNameListJSON;

function initializeSpecimenPage(biohazardNameJSON)
{
	onSpecimenSubTypeChange();
	biohazardNameListJSON=biohazardNameJSON;
	setDefaultText("extIdName",defaultTextForExtIdName);
	setDefaultText("extIdValue",defaultTextForExtIdValue);
	biohazardCombo=new dhtmlXCombo("biohazardSelect","biohazardSelectBox",165);
	biohazardCombo.setComboText(defaultTextForBioName);
	biohazardTypeCombo=new dhtmlXCombo({
	parent	: "biohazardTypeSelect",
	name	: "biohazardTypeSelectBox",
	width	: 165,
	onChange:function(){
			onBiohazardTypeSelected();
		}
	});
	biohazardTypeCombo.setComboText(defaultTextForBioType);
	populateBiohazardTypeOptions();
}

function getSortedUniqueArrayElements(arr) {
    arr = arr.sort(function (a, b) { return a*1 - b*1; });
    var ret = [arr[0]];
    for (var i = 1; i < arr.length; i++) 
	{ 
        if (arr[i-1] !== arr[i]) {
            ret.push(arr[i]);
        }
    }
    return ret;
}

var cellTypeOptions = new Array();
var molecularTypeOptions = new Array(); 
var tissueTypeOptions = new Array();
var fluidTypeOptions = new Array();

function prepareSpecimenTypeOptions(cellTypeJSON,molecularTypeJSON,tissueTypeJSON,fluidTypeJSON)
{
	var typeLists= new Array();
	typeLists.push(
		[JSON.parse(cellTypeJSON),cellTypeOptions],
		[JSON.parse(molecularTypeJSON),molecularTypeOptions],
		[JSON.parse(tissueTypeJSON),tissueTypeOptions],
		[JSON.parse(fluidTypeJSON),fluidTypeOptions]
	)
	for(var i=0;i<typeLists.length;i++) {
		createTypeOptionsFromJSON(typeLists[i][0],typeLists[i][1]);
	}
}

function createTypeOptionsFromJSON(jsonObject,array)
{
	for(var i=0;i<jsonObject.length;i++) {
		array.push([jsonObject[i].value,jsonObject[i].name]);
	}
}


function onSpecimenTypeChange(selectedElement)
{
	typeCombo.clearAll();
	switch(selectedElement.getSelectedValue())
	{
		case 'Cell' : typeCombo.addOption(cellTypeOptions); break;
		case 'Molecular' : typeCombo.addOption(molecularTypeOptions); break;
		case 'Tissue' : typeCombo.addOption(tissueTypeOptions); break;
		case 'Fluid' : typeCombo.addOption(fluidTypeOptions); break;
		default : typeCombo.addOption(-1,"-- Select --");
	}
	typeCombo.selectOption(0);
	setQuantityUnitOnClassChange(selectedElement.getSelectedValue());
}


function forwardToChildSpecimen(operation) {

    var radios = document.getElementsByName("specimenChild");
	var checkedRadio;
    for (var i = 0; i < radios.length; i++) {       
        if (radios[i].checked) {
            checkedRadio=radios[i].value;
            break;
        }
    }
	
	if(checkedRadio!="1" && !document.getElementById('available').checked)
	{
		showErrorMessage("Cannot create child specimen(s) since specimen is not available.");
		return;
	}
	
	var action;
	var specimenLabel = document.getElementById('label').value;
	var specimenId = document.getElementById("id").value;
	var scgId = document.getElementById("scgId").value;
	
	switch(checkedRadio)
	{
		case '2' : 	
		
		 var aliquotCount = document.getElementById("noOfAliquots").value;
		 var quantityPerAliquot = document.getElementById("quantityPerAliquot").value;
			//action = 'CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&buttonClicked=submit&parentSpecimenId=-1&CPQuery=CPQuery&nextForwardTo=""&specimenLabel='+specimenLabel; break;
			action = 'GetAliquotDetails.do?pageOf=fromSpecimen&parentSpecimentLabel='+specimenLabel+"&aliquotCount="+aliquotCount+"&quantityPerAliquot="+quantityPerAliquot+"&searchBasedOn=label";
			break;
			
		
		case '3' :	action = 'CPQueryCreateSpecimen.do?operation=add&pageOf=pageOfCreateSpecimenCPQuery&menuSelected=15&virtualLocated=true&forwardFromPage=editSpecimenPage&parentLabel='+specimenLabel+'&parentSpecimenId='+specimenId+'&specClassName='+classNameCombo.getSelectedText()+'&specType='+typeCombo.getSelectedText();
					
					if(document.getElementById("numberOfSpecimens").value>1)
					{
						action = 'MultipleSpecimenFlexInitAction.do?operation=add&pageOf=pageOfMultipleSpWithoutMenu&parentType=Derived_Specimen&parentLabel='+specimenLabel;
					}
					break;	
		
		case '4' :	action = 'AnticipatorySpecimenView.do?scgId='+scgId+'&specimenId='+specimenId; break;
		
		default :	if(operation=="add"){
						/*action = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+specimenId+"&refresh=true";*/
			getTabDetails(specimenId);
						action = "none";
					}else{
						action = "none";
					}
	}
	
	
	if(action!="none")
	{
		document.forms[0].action = action;
		document.forms[0].submit();
	}
}

function giveCall(url,msg,msg1,id)
{
	document.getElementsByName('objCheckbox').value=id;
	document.getElementsByName('objCheckbox').checked = true;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}

function setQuantityUnitOnClassChange(className)
{
	var unit = document.getElementById("unitSpan");
	var unit1 = document.getElementById("unitSpan1");
	var unitSpecimen = "";
	document.forms[0].concentration.disabled = true;
	
	if(className == "Tissue")
	{
		unitSpecimen = "gm";
	}
	else if(className == 'Fluid')
	{
		unitSpecimen = "ml";
	}
	else if(className == "Cell")
	{
		unitSpecimen = "cell count";
	}
	else if(className == "Molecular")
	{
		unitSpecimen = "\u00B5" + "g";
		document.forms[0].concentration.disabled = false;
	}
	if(unit!=null)
	{
		unit.innerHTML = unitSpecimen;
	}
	if(unit1!=null)
	{
		unit1.innerHTML = unitSpecimen;
	}
}

function onSpecimenSubTypeChange()
{
	var subTypeData1 = "Frozen Tissue Slide";
	var subTypeData2 = "Fixed Tissue Block";
	var subTypeData3 = "Frozen Tissue Block";
	var subTypeData4 = "Not Specified";
	var subTypeData5 = "Microdissected";
	var subTypeData6 = "Fixed Tissue Slide";
	
	var className = classNameCombo.getComboText();
	var selectedOption = typeCombo.getComboText();
	
	
		if(className != "Tissue")
		{
			setQuantityUnitOnClassChange(className);
		}
		else if(className == "Tissue" && (selectedOption == subTypeData1 || selectedOption == subTypeData2 || selectedOption == subTypeData3 || selectedOption == subTypeData4 || selectedOption == subTypeData6))
		{
			document.getElementById("unitSpan").innerHTML = "count";
			// added for Available quantity
			var unit1= document.getElementById("unitSpan1");
			if(unit1!=null)
			{
				unit1.innerHTML = "count";
			}
		}
		else
		{
			if(className == "Tissue")
			{
				// added for Available quantity
				var unit1= document.getElementById("unitSpan1");
				if(selectedOption == subTypeData5)
				{
					document.getElementById("unitSpan").innerHTML = "cells";

					if(unit1!=null)
					{
						unit1.innerHTML = "cells";
					}
				}
				else
				{
					document.getElementById("unitSpan").innerHTML = "gm";
					if(unit1!=null)
					{
						unit1.innerHTML = "gm";
					}
				}
			}
		}
}

function organizeTarget()
{
	ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem');
}

function scrollToTop()
{
	//document.body.scrollTop = document.documentElement.scrollTop = 0;
	document.getElementById("specimenDetailsDiv").scrollTop = 0;
}

var labelSubmit = true;
var quantitySubmit = true;
var availableQuantitySubmit = true;
var concentrationSubmit = true;
var createdDateSubmit = true;
//creates json string for the changed fields and will validate the fields too
function processData(obj)
{
	if(obj.name=='label' && obj.value.trim()=="" && collectionStatusCombo.getComboText()=="Collected")
	{
		labelSubmit=false;
		obj.className += " errorStyleOn";
	}
	else if(obj.name=='quantity' && (obj.value.trim()=="" || !isNumeric(obj.value.trim()) || Number(obj.value) < Number(document.getElementById('availableQuantity').value)))
	{
		quantitySubmit=false;
		
		var errorDiv = document.getElementById('quantityErrorMsg');
		errorDiv.style.display ="none";
		
		if(availableQuantitySubmit)
		{
			var availableQuantityElement = document.getElementById('availableQuantity');
			availableQuantityElement.className=availableQuantityElement.className.replace(/errorStyleOn/g,"");
			
			var avlErrorDiv = document.getElementById('avlQuantityErrorMsg');
			avlErrorDiv.style.display ="none";
		}
		
		if(obj.value.trim()=="")
		{
			obj.className += " errorStyleOn";
		}
		if(!isNumeric(obj.value.trim()))
		{
			obj.className += " errorStyleOn";
			errorDiv.style.display ="block";
			errorDiv.innerHTML = "(Enter a valid number)";
		}
		else if(obj.value.trim()!="" && Number(obj.value) < Number(availableQuantityElement.value))
		{
			availableQuantityElement.className += " errorStyleOn";
			avlErrorDiv.style.display ="block";
			avlErrorDiv.innerHTML = "(Cannot be greater than Initial Quantity)";
			obj.className = obj.className.replace(/errorStyleOn/g,"");
		}
		
	}
	else if(obj.name=='availableQuantity' && (!isNumeric(obj.value.trim()) || Number(obj.value) > Number(document.getElementById('quantity').value)))
	{
		availableQuantitySubmit=false;
		obj.className += " errorStyleOn";
		var errorDiv = document.getElementById('avlQuantityErrorMsg');
		errorDiv.style.display ="block";
		if(!isNumeric(obj.value))
			errorDiv.innerHTML = "(Enter a valid number)";
		else
			errorDiv.innerHTML = "(Cannot be greater than Initial Quantity)";
		
			
	}
	else if(obj.name=='concentration' && !isNumeric(obj.value.trim()))
	{
		concentrationSubmit=false;
		obj.className += " errorStyleOn";
		var errorDiv = document.getElementById('concentrationErrorMsg');
		errorDiv.style.display ="block";
		if(!isNumeric(obj.value))
			errorDiv.innerHTML = "(Enter a valid number)";
	}
	else if(obj.name=='createdDate' && !validateDate(obj))
	{
		createdDateSubmit=false;
		obj.className += " errorStyleOn";
	}
	else
	{	
		if(obj.name=='availableQuantity')
		{
			if(obj.value==""){obj.value=0;}
			document.getElementById('avlQuantityErrorMsg').style.display="none";
		}
		if(obj.name=='available' && obj.checked == true)
		{
			tabDataJSON[obj.name] = 'true';
		}
		else
		{
			tabDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
		}
		obj.className = obj.className.replace(/errorStyleOn/g,"");
		if(obj.name=='quantity')
		{
			if(availableQuantitySubmit)
			{
				var availableQuantityElement = document.getElementById('availableQuantity');
				availableQuantityElement.className=availableQuantityElement.className.replace(/errorStyleOn/g,"");
				document.getElementById('avlQuantityErrorMsg').style.display="none";
			}
			quantitySubmit = true;
			document.getElementById('quantityErrorMsg').style.display="none";
		}
		else if(obj.name=='label'){labelSubmit = true;}
		else if(obj.name=='availableQuantity'){availableQuantitySubmit = true;}
		else if(obj.name=='createdDate'){createdDateSubmit = true;}
		else if(obj.name=='concentration'){concentrationSubmit = true;document.getElementById('concentrationErrorMsg').style.display="none";}
	}
}

function isNumeric(value) {
   return (value==Number(value))?true:false;
}

function processComboData(objName,objValue)
{
	tabDataJSON[objName] = objValue;
}

var submitCombo = true;
function validateAndProcessComboData(obj)
{

	if(obj.getSelectedValue()=='-1' || obj.getSelectedText()=='-- Select --' || obj.getSelectedText().trim()=="")
	{
		obj.DOMelem.className += " errorStyleOn";
		submitCombo=false;
	}
	else
	{
		tabDataJSON[obj.name] = obj.getSelectedText();
		
		if(obj.name=='className' && typeCombo.getComboText()=='-- Select --')
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
			typeCombo.DOMelem.className = typeCombo.DOMelem.className.replace(/errorStyleOn/g,"");
			typeCombo.DOMelem.className += " errorStyleOn";
			submitCombo=false;
			return;
		}
		var index = obj.DOMelem.className.indexOf("errorStyleOn");
		if(index != -1)
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
		}
		
		if(obj.name=="collectionStatus" && obj.getSelectedText()!="Collected" && !labelSubmit)
		{
			var lblElement = document.getElementById('label');
			lblElement.className = lblElement.className.replace(/errorStyleOn/g,"");			
			labelSubmit=true;
		}
		submitCombo=true;
	}	
}

//submits changed data
function submitTabData(operation)
{
	var obj = document.getElementById('label');
	
	if(obj!=null && obj.value.trim()=="" && collectionStatusCombo.getComboText()=="Collected" && obj.disabled==false)
	{
		labelSubmit=false;
		obj.className += " errorStyleOn";
	}
	
	if(labelSubmit && submitCombo && quantitySubmit && availableQuantitySubmit && createdDateSubmit && concentrationSubmit)
	{
		var extidJSON = createExtIdJSON();
		var biohazardJSON = createBioHazardJSON();
		
		
		var isVirtual = document.getElementById("isVirtual").value;
		
		if(isVirtual == 'false')
		{
			tabDataJSON["containerName"]= document.getElementById("containerName").value;
			tabDataJSON["pos1"]= document.getElementById("pos1").value;
			tabDataJSON["pos2"]= document.getElementById("pos2").value;
			if(document.getElementById("containerId").value)
			{
				tabDataJSON["containerId"]= document.getElementById("containerId").value;
			}
		}
		tabDataJSON["isVirtual"] = isVirtual; 
		var printFlag = false;
		if(document.getElementById('printCheckbox').checked == true)
		{
			printFlag=true;
		}
		if(obj!=null && obj.value.trim()!="" && obj.disabled==false)
		{
			tabDataJSON['label']=obj.value;
		}
		if(operation = "add")
		{
			tabDataJSON['className']=classNameCombo.getSelectedText();
			tabDataJSON['type']=typeCombo.getComboText();
		}
		//var loader = dhtmlxAjax.postSync("SpecimenAjaxAction.do","type=updateSpecimen&dataJSON="+JSON.stringify(tabDataJSON)+"&extidJSON="+JSON.stringify(extidJSON)+"&biohazardJSON="+JSON.stringify(biohazardJSON)+"&printLabel="+printFlag);
		tabDataJSON["externalIdentifiers"]=extidJSON;
		tabDataJSON["bioHazards"]=biohazardJSON;
		tabDataJSON["isToPrintLabel"]=printFlag;
		tabDataJSON["lineage"]=document.getElementById("lineage").value;
		tabDataJSON["collectionStatus"]=collectionStatusCombo.getSelectedText();
		var spId = document.getElementById("id").value;
		if(spId != null && spId != "")
		{
			operation="edit";
			tabDataJSON["id"] = document.getElementById("id").value; 
		}
		var scgId = document.getElementById("scgId").value;
		if(scgId != null && scgId != "")
		{
			tabDataJSON["specimenCollectionGroupId"] = document.getElementById("scgId").value; 
		}
		var psId = document.getElementById("parentSpecimenId").value;
		if(psId != null && psId != "")
		{
			tabDataJSON["parentSpecimenId"] = document.getElementById("parentSpecimenId").value; 
		}
		var requirementId = document.getElementById("requirementId").value;
		if(requirementId != null && requirementId != "")
		{
			tabDataJSON["requirementId"] = document.getElementById("requirementId").value; 
		}
		var initQty = document.getElementById("quantity").value;
		if(initQty != null && initQty != "")
		{
			tabDataJSON["quantity"] = document.getElementById("quantity").value; 
		}
		var cDate = document.getElementById("createdDate").value;
		if(cDate != null && cDate != "")
		{
			tabDataJSON["createdDate"] = document.getElementById("createdDate").value; 
		}
		var concen = document.getElementById("concentration").value;
		if(concen != null && concen != "")
		{
			tabDataJSON["concentration"] = document.getElementById("concentration").value; 
		}
	
		createRESTSpec(tabDataJSON,printFlag,operation);
		
	}
	else
	{
		showErrorMessage("Unable to submit. Please resolve higlighted issue(s).");
		scrollToTop();
	}
}

function createRESTSpec(specimenData,printFlag,operation)
{
var req = createRequest(); // defined above
// Create the callback:
document.getElementById("specimenSubmitButton").disabled = true;
req.onreadystatechange = function() {
  if (req.readyState != 4) return; // Not there yet
  if (req.status != 201) {
    // Handle request failure here...
	var errorMsg=req.getResponseHeader("errorMsg");
	showErrorMessage(errorMsg);
	document.getElementById("specimenSubmitButton").disabled = false;
    return;
  }
  document.getElementById("specimenSubmitButton").disabled = false;
  // Request successful, read the response
  var resp = req.responseText;
  var updatedSpecimenDTO = eval('('+resp+')')
	
  parent.handleCpView(null, updatedSpecimenDTO.specimenCollectionGroupId, updatedSpecimenDTO.id);

				document.getElementById('available').disabled = false;
				document.getElementById('available').checked = updatedSpecimenDTO.available;
				document.getElementById('availableQuantity').value = updatedSpecimenDTO.availableQuantity;
				
				var labelElement = document.getElementById('label');
				if(labelElement!=null && updatedSpecimenDTO.label!=null && updatedSpecimenDTO.label!='undefined')
				{
					labelElement.value = updatedSpecimenDTO.label;
					document.getElementById('label').disabled = false;
				}
				
				var barcodeElement = document.getElementById('barcode');
				if(barcodeElement!=null && updatedSpecimenDTO.barcode!=null && updatedSpecimenDTO.barcode!='undefined')
				{
					barcodeElement.value = updatedSpecimenDTO.barcode;
					document.getElementById('barcode').disabled = false;
				}
				var specimenIdElem = document.getElementById('id');
				
					specimenIdElem.value = updatedSpecimenDTO.id;
					
				
				var nodeId= "Specimen_"+document.getElementById("id").value;
//				refreshTree(null,null,null,null,nodeId);
				document.getElementById('print-error').style.display='none';
				document.getElementById('print-success').style.display='none';
				document.getElementById('error').style.display='none';
				document.getElementById('success').style.display='block';
				forwardToChildSpecimen(operation);
			
			if(printFlag)
			{//alert('print flag');
				if(updatedSpecimenDTO.toPrintLabel)
				{//alert("print true");
					document.getElementById('print-error').style.display='none';
					document.getElementById('print-success').innerHTML = 'Label Printed Successfully.';
					document.getElementById('print-success').style.display='block';
					
				}
				else
				{
					document.getElementById('print-success').style.display='none';
					document.getElementById('print-error').innerHTML = 'Printer configuration not found.';
					document.getElementById('print-error').style.display='block';
				}
			}
			var divStyle = document.getElementById('specListDiv').style.display;
			
			if(divStyle == 'none')
			{
				divStyle='block';
				document.getElementById('specListDiv').style.display='block';
			}
			tabDataJSON={};
		/*	if(operation == 'add')
			{
				LoadSCGTabBar('edit');
			}*/
			scrollToTop();
		}
		if(operation == 'edit')
		{
			req.open("PUT", "rest/specimens/", false);
		}
		else if(operation == 'add')
		{
			specimenData['tissueSite']=tissueSiteCombo.getSelectedText();
			specimenData['tissueSide']=tissueSideCombo.getSelectedText();
			specimenData['pathologicalStatus']=pathologicalStatusCombo.getSelectedText();
			req.open("POST", "rest/specimens/", false);
		}
req.setRequestHeader("Content-Type",
                     "application/json");
req.send(JSON.stringify(specimenData));
}

function getTabDetails(specimenId){
	var req = createRequest(); // defined above
	// Create the callback:
	req.onreadystatechange = function() {
		  if (req.readyState != 4) return; // Not there yet
		  if (req.status != 201) {
			// Handle request failure here...
			var errorMsg=req.getResponseHeader("errorMsg");
			showErrorMessage(errorMsg);
			return;
		  }
		  // Request successful, read the response
		  var resp = req.responseText;
		  var tabDetail = eval('('+resp+')');
		  
		  var reportId=tabDetail.identifiedReportId;
			var entityId=tabDetail.specimenRecordEntryEntityId;
			var staticEntityName=tabDetail.entityName;
			var hasConsents = tabDetail.hasConsents;
		 var isImageEnabled = tabDetail.isImageEnabled;


		var showEventsTab = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery&specimenId="+specimenId+"&menuSelected=15";


		var showViewSPRTab="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf=pageOfNewSpecimenCPQuery&reportId="+reportId+"&id="+specimenId;

		var showAnnotationTab="DisplayAnnotationDataEntryPage.do?entityId="+entityId+"&entityRecordId="+specimenId+"&pageOf=pageOfNewSpecimenCPQuery&operation=viewAnnotations&staticEntityName="+staticEntityName+"&id="+specimenId;

		/*var showConsentsTab="FetchConsents.do?consentLevelId="+specimenId+"&consentLevel=specimen&reportId="+reportId+"&pageof=pageOfNewSpecimenCPQuery&entityId="+entityId+"&staticEntityName="+staticEntityName;
*/
		var showImagesTab="EditSpecimenImage.do?id="+specimenId;

		specimenTabbar.setLabel("specimenDetailsTab",'<span style="font-size:13px"> Specimen Details </span>', "150px");
		specimenTabbar.addTab("eventsTab",'<span style="font-size:13px"> Events </span>', "150px");
	if(reportId != null && reportId != -1  && reportId != -2)
	{
		specimenTabbar.addTab("reportsTab",'<span style="font-size:13px"> View Report(s)</span>',"150px");
	}
	specimenTabbar.addTab("annotationTab",'<span style="font-size:13px">View Annotation </span>',"150px");
	/*if(hasConsents){
	specimenTabbar.addTab("consentsTab",'<span style="font-size:13px">Consents </span>',"150px");
	}*/
	if(isImageEnabled)
	{
		specimenTabbar.addTab("imagesTab",'<span style="font-size:13px">Images </span>',"150px");
	}
	
	specimenTabbar.setHrefMode("iframes-on-demand");
	specimenTabbar.setContent("specimenDetailsTab", "specimenDetailsDiv");
	specimenTabbar.setContentHref("eventsTab", showEventsTab);
	if(reportId != null && reportId != -1  && reportId != -2)
	{
		specimenTabbar.setContentHref("reportsTab", showViewSPRTab);  
	}
	specimenTabbar.setContentHref("annotationTab", showAnnotationTab);
/*if(hasConsents){	
	specimenTabbar.setContentHref("consentsTab", showConsentsTab);  
	}*/
	if(isImageEnabled)
	{
		specimenTabbar.setContentHref("imagesTab", showImagesTab);
	}
	specimenTabbar.setTabActive("specimenDetailsTab");
	  
	}
	req.open("GET", "rest/specimens/"+specimenId+"/TabDetails", false);
	req.setRequestHeader("Content-Type",
                     "application/json");
	req.send();
}



function showErrorMessage(msg)
{
	document.getElementById('print-error').style.display='none';
	document.getElementById('print-success').style.display='none';
	document.getElementById('success').style.display='none';
	document.getElementById('errorMsg').innerHTML = msg;
	document.getElementById('error').style.display='block';
}

function updateHelpURL()
{
	var URL="";
	return URL;
}


// Declaring valid date character, minimum year and maximum year
var dtCh= "-";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30;}
		if (i==2) {this[i] = 29;}
   } 
   return this;
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12);
	var pos1=dtStr.indexOf(dtCh);
	var pos2=dtStr.indexOf(dtCh,pos1+1);
	var strMonth=dtStr.substring(0,pos1);
	var strDay=dtStr.substring(pos1+1,pos2);
	var strYear=dtStr.substring(pos2+1);
	strYr=strYear;
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
	}
	month=parseInt(strMonth);
	day=parseInt(strDay);
	year=parseInt(strYr);
	if (pos1==-1 || pos2==-1){
		//alert("The date format should be : mm-dd-yyyy")
		return false;
	}
	if (strMonth.length<1 || month<1 || month>12){
		//alert("Please enter a valid month")
		return false;
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		//alert("Please enter a valid day")
		return false;
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		//alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false;
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		//alert("Please enter a valid date")
		return false;
	}
}
function validateAliqDate(dt)
{
var datefmt = document.getElementById('dateId').innerHTML;
if(datefmt.trim().toUpperCase() == '[dd-MM-yyyy]'.toUpperCase() || datefmt.trim().toUpperCase() == '[dd/MM/yyyy]'.toUpperCase())
{
	if (validateddmmyyyydate(dt)==false){
		dt.focus();
		dt.className += " errorStyleOn";
		aliquotDateErr = true;
		return false;
	}
}
if(datefmt.trim().toUpperCase() == '[MM-DD-YYYY]'.toUpperCase() || datefmt.trim().toUpperCase() == '[MM/DD/YYYY]'.toUpperCase())
{
	if (validatemmddyyyydate(dt)==false){
		dt.focus();
		dt.className += " errorStyleOn";
		aliquotDateErr = true;
		return false;
	}
}
aliquotDateErr = false;
dt.className = dt.className.replace(/errorStyleOn/g,"");
 //   return true;
}
function validateDate(dt){
var datefmt = document.getElementById('dateId').innerHTML;
if(datefmt.trim().toUpperCase() == '[dd-MM-yyyy]'.toUpperCase() || datefmt.trim().toUpperCase() == '[dd/MM/yyyy]'.toUpperCase())
{
	if (validateddmmyyyydate(dt)==false){
		dt.focus();
		return false;
	}
}
if(datefmt.trim().toUpperCase() == '[MM-DD-YYYY]'.toUpperCase() || datefmt.trim().toUpperCase() == '[MM/DD/YYYY]'.toUpperCase())
{
	if (validatemmddyyyydate(dt)==false){
		dt.focus();
		return false;
	}
}

    return true;
 }
 
function validateDateDerive(dt){
	var datefmt = document.getElementById('dateId').innerHTML;

if(datefmt.trim().toUpperCase() == '[dd-MM-yyyy]'.toUpperCase() || datefmt.trim().toUpperCase() == '[dd/MM/yyyy]'.toUpperCase())
{
	if (validateddmmyyyydate(dt)==false){
		dt.focus();
		return false;
	}
}
if(datefmt.trim().toUpperCase() == '[MM-DD-YYYY]'.toUpperCase() || datefmt.trim().toUpperCase() == '[MM/DD/YYYY]'.toUpperCase())
{
	if (validatemmddyyyydate(dt)==false){
		dt.focus();
		return false;
	}
}
    return true;
 }

function setLabelBarcodeVisibility(isSpecimenLabelGeneratorAvl,isSpecimenBarcodeGeneratorAvl,collectionStatus,operation)
{
	if(isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='true' && (collectionStatus!='Collected'||operation=='add'))
	{
		document.getElementById('label').setAttribute('disabled',true);
		document.getElementById('barcode').setAttribute('disabled',true);
	}
	else if(isSpecimenLabelGeneratorAvl=='false' && isSpecimenBarcodeGeneratorAvl=='true' && (collectionStatus!='Collected'||operation=='add'))
	{
		document.getElementById('barcode').setAttribute('disabled',true);
	}
	else if(isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='false' && (collectionStatus!='Collected'||operation=='add'))
	{
		document.getElementById('label').setAttribute('disabled',true);
	}
}

function processDeriveData(obj)
{
	if(obj.name=='initialQuantity' && (obj.value.trim()=="" || !isNumeric(obj.value.trim())))
	{
		deriveQtySubmit=false;
		
		var errorDiv = document.getElementById('quantityErrorMsg');
		errorDiv.style.display ="none";
		
		if(obj.value.trim()=="")
		{
			obj.className += " errorStyleOn";
		}
		if(!isNumeric(obj.value.trim()))
		{
			obj.className += " errorStyleOn";
			errorDiv.style.display ="block";
			errorDiv.innerHTML = "(Enter a valid number)";
		}
	}
	
	else if(obj.name=='concentration' && !isNumeric(obj.value.trim()))
	{
		deriveConcentration=false;
		obj.className += " errorStyleOn";
		var errorDiv = document.getElementById('concentrationErrorMsg');
		errorDiv.style.display ="block";
		if(!isNumeric(obj.value))
			errorDiv.innerHTML = "(Enter a valid number)";
	}
	else if(obj.name=='createdOn' && !validateDateDerive(obj))
	{
		deriveCreatedOnSubmit=false;
		obj.className += " errorStyleOn";
	}
	else if(obj.name=='label' && isLabelGenAvl && obj.value.trim()=="")
	{
		derLabelSubmit=false;
		obj.className += " errorStyleOn";
	}
	else if(obj.name=='disposeParentSpecimen' || obj.name =='isToPrintLabel')
	{
		deriveDataJSON[obj.name] = obj.checked;
	}
	else
	{	
		if(obj.name=='initialQuantity')
		{
			if(obj.value==""){obj.value=0;}
			document.getElementById('quantityErrorMsg').style.display="none";
		}
		deriveDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
		obj.className = obj.className.replace(/errorStyleOn/g,"");
		if(obj.name=='initialQuantity')
		{
			if(deriveQtySubmit)
			{
				var availableQuantityElement = document.getElementById('initialQuantity');
				availableQuantityElement.className=availableQuantityElement.className.replace(/errorStyleOn/g,"");
				document.getElementById('quantityErrorMsg').style.display="none";
			}
			deriveQtySubmit = true;
			document.getElementById('quantityErrorMsg').style.display="none";
		}
		else if(obj.name=='label'){derLabelSubmit = true;}
		else if(obj.name=='initialQuantity'){deriveQtySubmit = true;}
		else if(obj.name=='createdOn'){deriveCreatedOnSubmit = true;}
		else if(obj.name=='concentration'){deriveConcentration = true;document.getElementById('concentrationErrorMsg').style.display="none";}
	}
}
function onRadioButtonClick(element)
{
	if(element.value == 1)
	{
		
		document.forms[0].parentSpecimenLabel.disabled = false;
		document.forms[0].parentSpecimenBarcode.className = document.forms[0].parentSpecimenBarcode.className.replace(/errorStyleOn/g,"");
		document.forms[0].parentSpecimenBarcode.value='';
		document.forms[0].parentSpecimenBarcode.disabled = true;
	}
	else
	{
		
		document.forms[0].parentSpecimenBarcode.disabled = false;
		document.forms[0].parentSpecimenLabel.className = document.forms[0].parentSpecimenLabel.className.replace(/errorStyleOn/g,"");
		document.forms[0].parentSpecimenLabel.value='';
		document.forms[0].parentSpecimenLabel.disabled = true;
	}
}
function submitDeriveData()
{
	var obj = document.getElementById('parentSpecimenLabel');
	
	if(obj!=null && obj.value.trim()=="" && obj.disabled==false)
	{
		deriveLabelSubmit=false;
		deriveBarcodeSubmit=false;
		obj.className += " errorStyleOn";
	}
	var derLabel = document.getElementById('derLabel');
	
	if(derLabel.value != null && derLabel.value.trim()=="" && derLabel.disabled==false)
	{
		derLabelSubmit=false;
		derLabel.className += " errorStyleOn";
	}
	
	if(submitDeriveCombo && deriveLabelSubmit && deriveBarcodeSubmit && deriveCreatedOnSubmit && deriveQtySubmit && derLabelSubmit)
	{
		var deriveExtidJSON = createExtIdJSON();
		//alert(deriveExtidJSON);
		document.getElementById('errorMsg').style.display='none';
		document.getElementById('errorMsg').innerHTML = '';
		var contName = document.getElementById("storageContainerPosition").value;
		//alert(contName);
		if(contName != 'Virtually Located')
		{
			deriveDataJSON["containerName"]= document.getElementById("containerName").value;
			deriveDataJSON["pos1"]= document.getElementById("pos1").value;
			deriveDataJSON["pos2"]= document.getElementById("pos2").value;
			deriveDataJSON["containerId"]= document.getElementById("containerId").value;
		}
		deriveDataJSON["className"]= classNameCombo.getSelectedText();
		deriveDataJSON["type"]= typeCombo.getSelectedText();
		deriveDataJSON["createdOn"]= document.getElementById('createdOn').value;
		deriveDataJSON["initialQuantity"]= document.getElementById('initialQuantity').value;
		deriveDataJSON["externalIdentifiers"] = deriveExtidJSON;
		
//		var loader1 =""; dhtmlxAjax.postSync("rest/specimen/createDerive/",JSON.stringify(deriveDataJSON));
		var loader = testREST(deriveDataJSON);
		
		
	}
	else
	{
		var msg="Unable to submit. Please resolve higlighted issue(s).";
		document.getElementById('errorMsg').style.display='block';
	document.getElementById('errorMsg').innerHTML = msg;
	document.getElementById('errorMsg').className = 'alert alert-error';
		scrollToTop();
	}
}
function testREST(dataderive)
{
	var req = createRequest(); // defined above
// Create the callback:
req.onreadystatechange = function() {
  if (req.readyState != 4) return; // Not there yet
  if (req.status != 201) {
    // Handle request failure here...
	var errorMsg=req.getResponseHeader("errorMsg");
	
	var errMsgDiv = document.getElementById('errorMsg');
	errMsgDiv.style.display='block';
				errMsgDiv.className='alert alert-error';
				errMsgDiv.innerHTML=errorMsg;
    return;
  }
  // Request successful, read the response
  var resp = req.responseText;
  if(resp != null)
		{
			//alert(loader.xmlDoc.responseText);
			//alert(loader.xmlDoc.responseText);
			var specimenDto = eval('('+resp+')');
			
			var errMsgDiv = document.getElementById('errorMsg');
			//if(response.success == "success")
			//{//
				errMsgDiv.style.display='block';
				errMsgDiv.className='alert alert-success';
				errMsgDiv.innerHTML='Specimen created successfully.';
				
				document.getElementById('derLabel').value=specimenDto.label;
				document.getElementById('derBarcode').value=specimenDto.barcode;
				document.getElementById('derSubmitButton').disabled=true;
				var nodeId= "Specimen_"+specimenDto.id;
				if(pageOf != "pageOfDeriveSpecimen")
				{
//					refreshTree(null,null,null,null,nodeId);
				}

				var addTospecListURL = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+specimenDto.id+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+specimenDto.id+"')";
				
				document.getElementById('assignTargetCall').value=addTospecListURL;
				document.getElementById('eventdiv').style.display="block";
				deriveId = specimenDto.label;
				document.getElementById('createDiv').style.display="none";
				var aliqChkBox = document.getElementById('aliquotChk');
				if(aliqChkBox.checked)
				{
					var aliquotCount = document.getElementById("noOfAliquots").value;
					var quantityPerAliquot = document.getElementById("quantityPerAliquot").value;
					if(pageOf == "pageOfDeriveSpecimen")
					{
						action = 'GetAliquotDetails.do?pageOf=fromMenu&parentSpecimentLabel='+specimenDto.label+"&aliquotCount="+aliquotCount+"&quantityPerAliquot="+quantityPerAliquot+"&searchBasedOn=label";
					}
					else
					{
						action = 'GetAliquotDetails.do?pageOf=fromSpecimen&parentSpecimentLabel='+specimenDto.label+"&aliquotCount="+aliquotCount+"&quantityPerAliquot="+quantityPerAliquot+"&searchBasedOn=label";
					}
					document.forms[0].action = action;
					document.forms[0].submit();
				}
				parent.handleCpView(null, specimenDto.specimenCollectionGroupId, specimenDto.id);

			/*}
			else
			{
				errMsgDiv.style.display='block';
				errMsgDiv.className='alert alert-error';
				errMsgDiv.innerHTML=response.msg;
			}*/
		}
  // ... and use it as needed by your app.
}//alert(dataderive[parentSpecimenLabel]);

req.open("POST", "rest/specimens/"+dataderive.parentSpecimenLabel+"/derivatives/", false);
req.setRequestHeader("Content-Type",
                     "application/json");
req.send(JSON.stringify(dataderive));
}

function validateLabelRequest(label,caption)
{
var req = createRequest(); // defined above
// Create the callback:
req.onreadystatechange = function() {


  if (req.readyState != 4) return; // Not there yet
  if (req.status != 200) {
	var errorMsg=req.getResponseHeader("errorMsg")
			deriveLabelSubmit = false;
			deriveBarcodeSubmit = false;
			label.className += " errorStyleOn";
    return;
  }
  // Request successful, read the response
  var resp = req.responseText;
  var specimenDto = eval('('+resp+')');
  
			deriveLabelSubmit=true;
			deriveBarcodeSubmit=true;
			enableMapButton();
			deriveDataJSON[label.name] = label.value;
			deriveDataJSON['parentSpecimenId'] = specimenDto.id;
			label.className = label.className.replace(/errorStyleOn/g,"");
			//alert(response);
			//alert(response.label);
			//alert(response.barcode);
			
	
  // ... and use it as needed by your app.
}
req.open("GET", "rest/specimens/"+caption+"="+label.value, false);
req.setRequestHeader("Content-Type",
                     "application/json");
req.send();
}
function createRequest() {
  var result = null;
  if (window.XMLHttpRequest) {
    // FireFox, Safari, etc.
    result = new XMLHttpRequest();
   
  }
  else if (window.ActiveXObject) {
    // MSIE
    result = new ActiveXObject("Microsoft.XMLHTTP");
  } 
  else {
    // No known mechanism -- consider aborting the application
  }
  return result;
}

function validateAndProcessDeriveComboData(obj)
{
obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
	if(obj.getSelectedValue()=='-1' || obj.getSelectedText()=='-- Select --' || obj.getSelectedText().trim()=="")
	{
		obj.DOMelem.className += " errorStyleOn";
		submitDeriveCombo=false;
	}
	else
	{
		deriveDataJSON[obj.name] = obj.getSelectedText();
		if(obj.name=='className' && typeCombo.getComboText()=='-- Select --')
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
			typeCombo.DOMelem.className += " errorStyleOn";
			submitDeriveCombo=false;
			return;
		}
		var index = obj.DOMelem.className.indexOf("errorStyleOn");
		if(index != -1)
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
		}
		submitDeriveCombo=true;
	}
	enableMapButton();
}
function enableMapButton()
{
	if(submitDeriveCombo && deriveLabelSubmit && deriveBarcodeSubmit)
	{
//		document.getElementById('mapButton').disabled=false;
	}
}
function validateLabelBarcode(label,caption)
{
	var barcode = document.getElementById('parentSpecimenBarcode').value;
	//var loader = dhtmlxAjax.getSync("rest/specimens/getParentDetails/"+label.value);
	validateLabelRequest(label,caption);
	deriveDataJSON[label.name] = label.value;
	/*if(loader.xmlDoc.responseText != null && loader.xmlDoc.responseText != '')
	{
		var response = eval('('+loader.xmlDoc.responseText+')')
		if(response.msg == 'success')
		{
			deriveLabelSubmit=true;
			deriveBarcodeSubmit=true;
			deriveDataJSON[label.name] = label.value;
			deriveDataJSON['parentSpecimenId'] = response.parentId;
			deriveDataJSON['specimenCollGroupId'] = response.scgId;
			deriveDataJSON['cpId'] = response.cpId;
			selectedCPID = response.cpId;
			enableMapButton();
			label.className = label.className.replace(/errorStyleOn/g,"");
		}
		else
		{
			deriveLabelSubmit = false;
			deriveBarcodeSubmit = false;
			label.className += " errorStyleOn";
		}
	}
	else
	{
		deriveLabelSubmit = false;
		deriveBarcodeSubmit = false;
		label.className += " errorStyleOn";
	}*/
}
function openEventPage()
{
var action = 'QuickEvents.do?specimenLabel='+deriveId+'&pageOf=';
	if(pageOf != "pageOfDeriveSpecimen")
	{
		action = 'QuickEvents.do?specimenLabel='+deriveId+'&pageOf=CPQuery';
	}
	document.forms[0].action = action;
	document.forms[0].submit();
}
function giveCall(url,msg,msg1,id)
{
	if(id != null && id.trim()=="")
	{
		id=document.getElementById('id').value;
		url=url+id;
	}
    document.getElementsByName('objCheckbox').value=id;
    document.getElementsByName('objCheckbox').checked = true;
	
    ajaxAssignTagFunctionCall(url,msg,msg1);
}
function loadDHTMLXWindowForDeriveSpecimen()
{
var w =700;
var h =450;
var x = (screen.width / 3) - (w / 2);
var y = 0;
dhxWins = new dhtmlXWindows(); 
dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
var className = classNameCombo.getSelectedText();
var type = typeCombo.getSelectedText();
var url = "ShowStoragePositionGridView.do?pageOf=pageOfderivative&forwardTo=gridView&holdSpecimenClass="+className+"&holdSpecimenType="+type+"&collectionProtocolId="+selectedCPID+"&pos1=&pos2=";
dhxWins.window("containerPositionPopUp").attachURL(url);                     
dhxWins.window("containerPositionPopUp").button("park").hide();
dhxWins.window("containerPositionPopUp").allowResize();
dhxWins.window("containerPositionPopUp").setModal(true);
dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}

function validateddmmyyyydate(inputText)
  {
  var dateformat = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
  // Match the date format through regular expression
  if(inputText.value.match(dateformat))
  {
  //document.form1.text1.focus();
  //Test which seperator is used '/' or '-'
  var opera1 = inputText.value.split('/');
  var opera2 = inputText.value.split('-');
  lopera1 = opera1.length;
  lopera2 = opera2.length;
  // Extract the string into month, date and year
  if (lopera1>1)
  {
  var pdate = inputText.value.split('/');
  }
  else if (lopera2>1)
  {
  var pdate = inputText.value.split('-');
  }
  var dd = parseInt(pdate[0]);
  var mm  = parseInt(pdate[1]);
  var yy = parseInt(pdate[2]);
  // Create list of days of a month [assume there is no leap year by default]
  var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31];
  if (mm==1 || mm>2)
  {
  if (dd>ListofDays[mm-1])
  {
  //alert('Invalid date format!');
  return false;
  }
  }
  if (mm==2)
  {
  var lyear = false;
  if ( (!(yy % 4) && yy % 100) || !(yy % 400)) 
  {
  lyear = true;
  }
  if ((lyear==false) && (dd>=29))
  {
  //alert('Invalid date format!');
  return false;
  }
  if ((lyear==true) && (dd>29))
  {
  //alert('Invalid date format!');
  return false;
  }
  }
  }
  else
  {
  //alert("Invalid date format!");
 // document.form1.text1.focus();
  return false;
  }
  }
  
  function validatemmddyyyydate(inputText)
  {
  var dateformat = /^(0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])[\/\-]\d{4}$/;
  // Match the date format through regular expression
  if(inputText.value.match(dateformat))
  {
  //document.form1.text1.focus();
  //Test which seperator is used '/' or '-'
  var opera1 = inputText.value.split('/');
  var opera2 = inputText.value.split('-');
  lopera1 = opera1.length;
  lopera2 = opera2.length;
  // Extract the string into month, date and year
  if (lopera1>1)
  {
  var pdate = inputText.value.split('/');
  }
  else if (lopera2>1)
  {
  var pdate = inputText.value.split('-');
  }
  var mm  = parseInt(pdate[0]);
  var dd = parseInt(pdate[1]);
  var yy = parseInt(pdate[2]);
  // Create list of days of a month [assume there is no leap year by default]
  var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31];
  if (mm==1 || mm>2)
  {
  if (dd>ListofDays[mm-1])
  {
  //alert('Invalid date format!');
  return false;
  }
  }
  if (mm==2)
  {
  var lyear = false;
  if ( (!(yy % 4) && yy % 100) || !(yy % 400)) 
  {
  lyear = true;
  }
  if ((lyear==false) && (dd>=29))
  {
 // alert('Invalid date format!');
  return false;
  }
  if ((lyear==true) && (dd>29))
  {
  //alert('Invalid date format!');
  return false;
  }
  }
  }
  else
  {
  //alert("Invalid date format!");
 // document.form1.text1.focus();
  return false;
  }
  }
  