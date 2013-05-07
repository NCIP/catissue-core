// Functions to implement multiselect list box using autcomplete combobox start here.
function addRemoveButtonEvent(theSelFromId, theSelToId, operation) 
{
	addDataFromList(theSelFromId, theSelToId, operation);

	var theSelFrom = document.getElementById(theSelFromId);
	var theSelTo = document.getElementById(theSelToId);    
	var selLength = theSelFrom.length;
	var selectedText = new Array();
	var selectedValues = new Array();
	var selectedCount = 0;
	var i;

	// Find the selected Options in reverse order
	// and delete them from the 'from' Select.

	for (i = selLength - 1; i >= 0; i--)
	{
		if (theSelFrom.options[i].selected)
		{
			selectedText[selectedCount] = theSelFrom.options[i].text;
			selectedValues[selectedCount] = theSelFrom.options[i].value;
			selectedCount++;

			deleteEvent(theSelFrom, i);
		}
	}

	// Add the selected text/values in reverse order.
	// This will add the Options to the 'to' Select
	// in the same order as they were in the 'from' Select.

	if (operation != 'edit')
	{
		for (i = selectedCount - 1; i >= 0; i--)
		{
			addEvent(theSelTo, selectedText[i], selectedValues[i]);
		}
	}
}

function addDataFromList(theSelFromId, theSelToId, operation)
{
	var getListText = document.getElementById(theSelFromId).value;
	var getListValue = null;

	if (document.getElementById(("CB_"+theSelFromId)) != null)
	{
		getListValue = document.getElementById(("CB_"+theSelFromId)).value;
	}
	
	var theSel = document.getElementById(theSelToId);
	var selLength = theSel.length;
	
	var exists = false;
	for ( var i = 0; i < selLength; i++) 
	{
		if (theSel.options[i].value == getListValue) 
		{
			exists = true;
			break;
		}
	}

	if (!exists)
	{
		if (operation != 'edit' && getListText != null && (getListText == "--Select--" || getListText == "-- Select --"))
		{
			alert("--Select-- is not a valid coordinator name. Please choose another.");
			return;
		}
		if (getListText != null && getListText != "" && getListValue!= null && getListValue!= "")
		{
			if (theSelFromId != 'protocolCoordinatorIds' || theSelFromId != 'principalInvestitorColl')
			{
				addEvent(theSel,getListText,getListValue);
			}
		}
	}
	
	return;
}

function deleteEvent(theSel, theIndex) 
{
	var selLength = theSel.length;

	if (selLength > 0) 
	{
		//theSel.options[theIndex].title="";
		theSel.options[theIndex] = null;
	}
}


function addEvent(theSel, theText, theValue) 
{
	var newOpt = new Option(theText, theValue);
	var selLength = theSel.length;
	var exists = "false";
	for ( var i = 0; i < selLength; i++) 
	{
		if (theSel.options[i].value == theValue) 
		{
			exists = "true";
			break;
		}
	}
	
	if (exists == "false")
	{
		theSel.options[selLength] = newOpt;
		theSel.options[selLength].title=theText;
		theSel.options[selLength].selected="selected";
	}
}

function selectCoordinators(formname)
{
	var coords = document.getElementById('protocolCoordinatorIds');
	if (coords != null)
	{
		for (i = coords.options.length-1; i >= 0; i--)
		{
			coords.options[i].selected=true;
		}
	}

	checkForDuplicates(formname);
}

function selectPC(formname)
{
	
	var coordsPC = document.getElementById('principalInvestitorColl');
	if (coordsPC != null)
	{
		
		for (i = coordsPC.options.length-1; i >= 0; i--)
		{
			coordsPC.options[i].selected=true;
		}
	}
}

function selectPresentCoordinators()
{
	var coords = document.getElementById('protocolCoordinatorIds');
	if (coords != null)
	{
		for (i = coords.options.length-1; i >= 0; i--)
		{
			coords.options[i].selected=true;
		}
	}
}

//Functions to implement multiselect list box using autcomplete combobox start here.
function moveOptions(theSelFromId, theSelToId, operation) 
{
	addFromList(theSelFromId, theSelToId, operation);

	var theSelFrom = document.getElementById(theSelFromId);
	var theSelTo = document.getElementById(theSelToId);    
	var selLength = theSelFrom.length;
	var selectedText = new Array();
	var selectedValues = new Array();
	var selectedCount = 0;
	var i;

	// Find the selected Options in reverse order
	// and delete them from the 'from' Select.

	for (i = selLength - 1; i >= 0; i--)
	{
		if (theSelFrom.options[i].selected)
		{
			selectedText[selectedCount] = theSelFrom.options[i].text;
			selectedValues[selectedCount] = theSelFrom.options[i].value;
			selectedCount++;

			deleteOption(theSelFrom, i);
		}
	}

	// Add the selected text/values in reverse order.
	// This will add the Options to the 'to' Select
	// in the same order as they were in the 'from' Select.

	if (operation != 'edit')
	{
		for (i = selectedCount - 1; i >= 0; i--)
		{
			addOption(theSelTo, selectedText[i], selectedValues[i]);
		}
	}
}
