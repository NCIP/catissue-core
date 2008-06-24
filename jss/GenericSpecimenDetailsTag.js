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
	//this if loop decides that the checkboxes need to be checked or unchecked
	if(Obj.checked == true)
		{
			type= 'aliquot';
			state='checkUnableState';
			uncheckDisable(type,state)
			type= 'derived';
			uncheckDisable(type,state);
		}
	else
		{
			 type= 'aliquot';
			 state='uncheckDisableState';
			 uncheckDisable(type,state);
			 type= 'derived';
			 uncheckDisable(type,state);
		
		}

}
function uncheckDisable(type,state)
{
	// the identifier decides the hidden fields to be considered
	var identifier='';
	if(type == "aliquot")
		{
			identifier="disabledAliquots";
		}
	else if(type == "derived")
		{
			identifier="disabledDerivatives";
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
						element.checked = false;
						element.disabled = true;
						}
						else if(state=='checkUnableState')
						{
						element.checked = true;
						element.disabled = false;
						}
					}
					ctr++;
				}
			}
			while(chkCount>0);
}
