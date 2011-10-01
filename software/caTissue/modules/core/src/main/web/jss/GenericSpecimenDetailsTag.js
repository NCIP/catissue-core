function identifyDisabledCheckBox()
{
	var radioButtons = document.getElementsByTagName("input");
	var parentNode = document.getElementById("divForHiddenChild");
				for(j=0 ; j < radioButtons.length ; j++)
				{
					var radioName=radioButtons[j].name;
					if(radioButtons[j].type == 'checkbox')
					{
						if(radioButtons[j].disabled == true && radioName.indexOf("aliquot")>=0)
						{
							var hiddenfield=document.createElement('input'); 
							hiddenfield.id=radioButtons[j].name;
							hiddenfield.type='hidden'; 
							hiddenfield.value='disabledAliquots';
						}
						if(radioButtons[j].disabled == true && radioName.indexOf("derived")>=0)
						{
							var hiddenfield=document.createElement('input'); 
							hiddenfield.id=radioButtons[j].name;
							hiddenfield.type='hidden'; 
							hiddenfield.value='disabledDerivatives';
						}
						if(hiddenfield != null && hiddenfield != "undefined")
						{
							try
							{
								parentNode.appendChild(hiddenfield);
							}
							catch(e)
							{
								alert("hidden field not created");
							}
						}
					}
				}
}
function onClickCollected(Obj)
{
	var type='';
	var state='';
	var flagOfParentCall=true;
	//this if loop decides that the checkboxes need to be checked or unchecked
	if(Obj.checked == true)
		{
		   	type= 'aliquot';
			state='checkUnableState';
			uncheckDisable(type,state,flagOfParentCall)
			type= 'derived';
			
			uncheckDisable(type,state,flagOfParentCall);
		}
	else
		{
			 type= 'aliquot';
			 state='uncheckDisableState';
			 uncheckDisable(type,state,flagOfParentCall);
			 type= 'derived';
			
			 uncheckDisable(type,state,flagOfParentCall);
		
		}

}
function uncheckDisable(type,state,flagOfParentCall)
{
	
	// the identifier decides the hidden fields to be considered
	var identifier='';
	
	if(type == "aliquot")
		{
			identifier="disabledAliquots";
			var CollectedCheckBox = document.getElementById("aliquotCheckBox");
		}
	else if(type == "derived")
		{
			identifier="disabledDerivatives";
			var CollectedCheckBox = document.getElementById("derivedCheckBox");
		}
		var hiddenFields= document.getElementsByTagName("input");
		var index = new Array(); 
		var ctr=0;
		// this for loop stores all the hidden fields according to the identiifier(aliquots/derived)
		for(var s=0 ; s < hiddenFields.length ; s++)
				{
					var tempInput = hiddenFields[s];
					
					if(tempInput.value == identifier)
					{
					   index[ctr]= tempInput;
					   ctr++;
					}
					
				}
	var indexSize = index.length;
	var checkedSpecimen ='].checkedSpecimen';
			var elementType = type +'[';
			var ctr=0;
			do
			{
				var flag = true;
				var elementName = elementType + ctr + checkedSpecimen;
				var chkCount= document.getElementsByName(elementName).length;
				//sets the flag for the checkboxes which need to be changed
				for(var j=0 ; j< indexSize;j++)
					{
						if(elementName == index[j].id)
						{
						flag = false;
						
						}
					}
				if (chkCount >0)
				{
					if(flag == true)
					{
					var element = document.getElementsByName(elementName)[0];
						if(state=='uncheckDisableState')
						{
							if(CollectedCheckBox != null && flagOfParentCall == true)
							{
								CollectedCheckBox.checked = false;
								//commented to solve bug 11570
								//CollectedCheckBox.disabled = true;
							}
						element.checked = false;
						//commented to solve bug 11570
						//element.disabled = true;
						}
						else if(state=='checkUnableState')
						{
							if(CollectedCheckBox != null && flagOfParentCall == true)
							{
								CollectedCheckBox.checked = true;
								CollectedCheckBox.disabled = false;
							}
						element.checked = true;
						element.disabled = false;
						}
					}
					ctr++;
				}
			}
			while(chkCount>0);
}
function applyToAlquots()
{
	
var v = document.getElementById("aliquotCheckBox");
var type="aliquot";
var flagOfParentCall = false;

if(v.checked == true)
	{
	var state = "checkUnableState";
	}
	else
	{
var state= "uncheckDisableState";
	}
uncheckDisable(type,state,flagOfParentCall);
}
function applyToDerived()
{
	
var v = document.getElementById("derivedCheckBox");
var type="derived";
var flagOfParentCall = false;
if(v.checked == true)
	{
	var state = "checkUnableState";
	}
	else
	{
var state= "uncheckDisableState";
	}
uncheckDisable(type,state,flagOfParentCall);
}
