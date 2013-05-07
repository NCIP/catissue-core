//Set last refresh time
if(window.parent!=null)
{
	if(window.parent.lastRefreshTime!=null)
	{
		window.parent.lastRefreshTime = new Date().getTime();
	}
}
		
function showHideStorageContainerGrid(e,gridDivId, dropDownId,scGridVisible,containerDropDownInfo,scGrid)
{		
		setValue(e,containerDropDownInfo['gridDiv'], containerDropDownInfo['dropDownId']);
		if(containerDropDownInfo['visibilityStatusVariable'])
		{
			hideGrid(containerDropDownInfo['gridDiv']);
			containerDropDownInfo['visibilityStatusVariable'] = false;
		}
		else 
		 {	
			showGrid(containerDropDownInfo['gridDiv'],containerDropDownInfo['dropDownId']);
			containerDropDownInfo['visibilityStatusVariable'] = true;
			var containerName=document.getElementById(containerDropDownInfo['dropDownId']);
			if(null== containerName)
			{
				containerName="";
			}
			scGrid.load(containerDropDownInfo['actionToDo'],"");
		 }
}

function onRadioButtonClick(element)
{
	if(element.value == 1)
	{
		document.forms[0].specimenLabel.disabled = false;
		document.forms[0].barcode.disabled = true;
	}
	else
	{
		document.forms[0].barcode.disabled = false;
		document.forms[0].specimenLabel.disabled = true;
	}
}