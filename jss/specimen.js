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
				span.id="Ext"+ cnt++;
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
	var agree=confirm("Are you sure you want to delete this tag?");
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


function onBiohazardTypeSelected(selectedTypeElement)
{
	biohazardCombo.clearAll();
	var myData=biohazardNameListJSON;
	for(var i=0;i<myData.length;i++) {
		if(selectedTypeElement.getSelectedValue()==myData[i].value)
			biohazardCombo.addOption(myData[i].value,myData[i].name);
	}
	biohazardCombo.setComboText("Select BioHazard Name");
}

function addEditBioHazTag(buttonElement)
{
	var biohazard = biohazardCombo.getSelectedValue()+" - "+biohazardCombo.getSelectedText();
	if(biohazardCombo.getSelectedValue()!=null)
	{	
		if(document.getElementById('addEditBioHazButton').value==="Add")
		{
				var ul = document.getElementById('bioHazardList');
				var li = document.createElement("li");
				li.title="Edit";
				var span = document.createElement("span");
				span.onclick=function(){editBiohazardTag(this)};
				span.id="Bio"+ cnt++;
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
		
		}else{
				editElement.firstChild.nodeValue=biohazard;
				var hidden = document.getElementById(editElement.id+"Status");
				if(hidden.value!="ADD")
				{
					hidden.value="EDIT";
				}
			
				document.getElementById('addEditBioHazButton').value="Add";
			}
	}
	document.getElementById('addBioHazardDiv').style.display="none";
	biohazardCombo.setComboText(defaultTextForBioName);
	biohazardCombo.unSelectOption();
	typeCombo.setComboText(defaultTextForBioType);
	typeCombo.unSelectOption();
	}

function editBiohazardTag(e)
{
	var n = e.firstChild.nodeValue.split(" - ");
	
	document.getElementById('addEditBioHazButton').value="Edit";
	biohazardCombo.setComboText(n[1]);
	typeCombo.setComboText(n[0]);
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
	
	var extIds = document.getElementsByName("ExtIds");	
	for(var i=0;i<extIds.length;i++)
	{
		var e = extIds[i];
		var n = e.firstChild.nodeValue.split(" - "); 
		var id = e.id;
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
	
	var biohazards = document.getElementsByName("Biohazards");	
	for(var i=0;i<biohazards.length;i++)
	{
		var e = biohazards[i];
		var n = e.firstChild.nodeValue.split(" - "); 
		var id = e.id;
		var status = document.getElementById(e.id+'Status').value;
		if(status=="ADD")
		{
			id=null;
		}
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
var defaultTextForBioName = "Select BioHazard Name";
var defaultTextForBioType = "Select BioHazard Type";
var biohazardCombo;
var typeCombo;
var biohazardNameListJSON;

function initializeSpecimenPage(biohazardNameJSON)
{
	biohazardNameListJSON=biohazardNameJSON;
	setDefaultText("extIdName",defaultTextForExtIdName);
	setDefaultText("extIdValue",defaultTextForExtIdValue);
	
	biohazardCombo=new dhtmlXCombo("biohazardSelect","biohazardSelectBox",165);
	biohazardCombo.setComboText(defaultTextForBioName);
	typeCombo = dhtmlXComboFromSelect("biohazardType");
	typeCombo.setComboText(defaultTextForBioType);
	typeCombo.setSize(165);
}