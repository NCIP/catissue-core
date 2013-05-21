var typeCombo;
var classNameCombo;
var collectionStatusCombo;
function initSpecimenCombo()
{
		var tissueSiteCombo = dhtmlXComboFromSelect("tissueSite");
		tissueSiteCombo.setOptionWidth(202);
		tissueSiteCombo.setSize(202);
		tissueSiteCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
		tissueSiteCombo.enableFilteringMode(true);

		var tissueSideCombo = dhtmlXComboFromSelect("tissueSide");
		tissueSideCombo.setOptionWidth(203);
		tissueSideCombo.setSize(203);
		tissueSideCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});

		var pathologicalStatusCombo = dhtmlXComboFromSelect("pathologicalStatus");
		pathologicalStatusCombo.setOptionWidth(203);
		pathologicalStatusCombo.setSize(203);
		pathologicalStatusCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});

		collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");
		collectionStatusCombo.setOptionWidth(203);
		collectionStatusCombo.setSize(203);
		collectionStatusCombo.attachEvent("onChange", function(){validateAndProcessComboData(this);});
		
		classNameCombo = dhtmlXComboFromSelect("className");
		classNameCombo.setOptionWidth(203);
		classNameCombo.setSize(203);
		classNameCombo.attachEvent("onChange", function(){onSpecimenTypeChange(this); validateAndProcessComboData(this);});

		typeCombo = dhtmlXComboFromSelect("type");
		typeCombo.setOptionWidth(203);
		typeCombo.setSize(203);
		typeCombo.attachEvent("onChange", function(){onSpecimenSubTypeChange(); validateAndProcessComboData(this);});

		if(document.getElementById('activityStatus')!=null)
		{
			var activityStatusCombo = dhtmlXComboFromSelect("activityStatus");
			activityStatusCombo.setOptionWidth(203);
			activityStatusCombo.setSize(203);
			activityStatusCombo.attachEvent("onBlur", function(){processComboData(this.name,this.getSelectedText());});
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


function forwardToChildSpecimen() {

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
		case '2' : 	action = 'CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&buttonClicked=submit&parentSpecimenId=-1&CPQuery=CPQuery&nextForwardTo=""&specimenLabel='+specimenLabel; break;
		
		case '3' :	action = 'CPQueryCreateSpecimen.do?operation=add&pageOf=pageOfCreateSpecimenCPQuery&menuSelected=15&virtualLocated=true&forwardFromPage=editSpecimenPage&parentLabel='+specimenLabel+'&parentSpecimenId='+specimenId;
					
					if(document.getElementById("numberOfSpecimens").value>1)
					{
						action = 'MultipleSpecimenFlexInitAction.do?operation=add&pageOf=pageOfMultipleSpWithoutMenu&parentType=Derived_Specimen&parentLabel='+specimenLabel;
					}
					break;	
		
		case '4' :	action = 'AnticipatorySpecimenView.do?scgId='+scgId+'&specimenId='+specimenId; break;
		
		default :	action = "none";
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
	document.body.scrollTop = document.documentElement.scrollTop = 0;
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
	if(obj.name=='concentration' && !isNumeric(obj.value.trim()))
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
		tabDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
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
function submitTabData()
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
		}
		tabDataJSON["isVirtual"] = isVirtual; 
		var printFlag = false;
		if(document.getElementById('printCheckbox').checked == true)
		{
			printFlag=true;
		}
		
		var loader = dhtmlxAjax.postSync("CatissueCommonAjaxAction.do","type=updateSpecimen&dataJSON="+JSON.stringify(tabDataJSON)+"&extidJSON="+JSON.stringify(extidJSON)+"&biohazardJSON="+JSON.stringify(biohazardJSON)+"&printLabel="+printFlag);
		
		if(loader.xmlDoc.responseText != null)
		{
			var response = eval('('+loader.xmlDoc.responseText+')')
			if(response.success == "success")
			{
				var updatedSpecimenDTO = JSON.parse(response.updatedSpecimenDTO);
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
				
				var nodeId= "Specimen_"+document.getElementById("id").value;
				refreshTree(null,null,null,null,nodeId);
				document.getElementById('print-error').style.display='none';
				document.getElementById('print-success').style.display='none';
				document.getElementById('error').style.display='none';
				document.getElementById('success').style.display='block';
				forwardToChildSpecimen();
			}
			else
			{
				showErrorMessage(response.msg);
			}
			if(printFlag)
			{
				if(response.printLabel == "success")
				{
					document.getElementById('print-error').style.display='none';
					document.getElementById('print-success').innerHTML = response.printLabelSuccess;
					document.getElementById('print-success').style.display='block';
					
				}
				else
				{
					document.getElementById('print-success').style.display='none';
					document.getElementById('print-error').innerHTML = response.printLabelError;
					document.getElementById('print-error').style.display='block';
				}
			}
			scrollToTop();
		}
	}
	else
	{
		showErrorMessage("Unable to submit. Please resolve higlighted issue(s).");
		scrollToTop();
	}
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
return true;
}

function validateDate(dt){
	if (isDate(dt.value)==false){
		dt.focus();
		return false;
	}
    return true;
 }

function setLabelBarcodeVisibility(isSpecimenLabelGeneratorAvl,isSpecimenBarcodeGeneratorAvl,collectionStatus)
{
	if(isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='true' && collectionStatus!='Collected')
	{
		document.getElementById('label').setAttribute('disabled',true);
		document.getElementById('barcode').setAttribute('disabled',true);
	}
	else if(isSpecimenLabelGeneratorAvl=='false' && isSpecimenBarcodeGeneratorAvl=='true' && collectionStatus!='Collected')
	{
		document.getElementById('barcode').setAttribute('disabled',true);
	}
	else if(isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='false' && collectionStatus!='Collected')
	{
		document.getElementById('label').setAttribute('disabled',true);
	}
}