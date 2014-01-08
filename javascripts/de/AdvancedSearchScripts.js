/*This is the wrapper function over show_calendar() that allows to select the date only if the operator is Not 'ANY'
  opratorListId : Id of the date-operators list box
  formNameAndDateFieldId : A string that contains the form name & date field's id
  isSecondDateField : A boolean variable that tells whether the date field is first or second
*/
function onDate(operatorListId,formNameAndDateFieldId,isSecondDateField)
{
	var dateCombo = document.getElementById(operatorListId);
	
	if(dateCombo.options[dateCombo.selectedIndex].value != "Any")
	{
		if(!isSecondDateField)
		{
			show_calendar(formNameAndDateFieldId,null,null,'MM-DD-YYYY')
		}
		else
		{
			if(dateCombo.options[dateCombo.selectedIndex].value == "Between" || dateCombo.options[dateCombo.selectedIndex].value == "Not Between")
			{
				show_calendar(formNameAndDateFieldId,null,null,'MM-DD-YYYY');
			}
		}
	}
}

/*Generic function to enable/disable value fields as per the operator selected
  opratorListId : Id of the operators list box
  valueFieldId  : Id of the value field (Textbox/List) which is to be enabled/disabled
*/
function onOperatorChange(operatorListId,valueFieldId)
{
	var opCombo  = document.getElementById(operatorListId);
	var valField = document.getElementById(valueFieldId);
	
	if(opCombo.options[opCombo.selectedIndex].value == "Any")
	{
		if(valField.type == "text")
		{
			valField.value = "";
			valField.disabled = true;
		}
		else
		{
			valField.options[0].selected = true;
			valField.disabled = true;
		}
	}
	else
	{
		valField.disabled = false;
	}
}

/*This function enables the second date field only if the operator is 'Between' or 'Not Between'
  & disables both the date fields if operator is 'ANY'
  element : An object of type list
  dateFiled1 : An id of date field one
  dateFiled2 : An id of date field two
*/
function onDateOperatorChange(element,dateFiled1,dateFiled2)
{
	var dateTxt1  = document.getElementById(dateFiled1);
	var dateTxt2  = document.getElementById(dateFiled2);
	
	if(element.value == "Between" || element.value == "Not Between")
	{
		dateTxt1.disabled = false;
		dateTxt2.disabled = false;
	}
	else if(element.value == "Any")
	{
		dateTxt1.value = "";
		dateTxt1.disabled = true;

		dateTxt2.value = "";
		dateTxt2.disabled = true;
	}
	else
	{
		dateTxt1.disabled = false;

		dateTxt2.value = "";
		dateTxt2.disabled = true;
	}
}

/*This function forwards the request to the given action
  action : An action to which request is to be forwarded
*/
function onAddRule(action)
{
	document.forms[0].action = action;
	document.forms[0].submit();
}