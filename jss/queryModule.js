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
function goToAddLimitsPage()
{

}

function addToView()
{
	var selectTag = document.getElementById('selectCategoryList');
	var len = selectTag.length;
	var items = "";
	for(i=0;i<len;i++)
	{
		if(selectTag[i].selected)
		{
			items += selectTag[i].value + "~";
		}
	}
	document.applets[0].addNodeToView(items);
}


function expand()
{			
	switchObj = document.getElementById('image');
	dataObj = document.getElementById('collapsableTable');
	resultSetDivObj = document.getElementById('resultSetDiv');

	if(dataObj.style.display != 'none') //Clicked on - image
	{
		dataObj.style.display = 'none';				
		switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0"/>';
		if(navigator.appName == "Microsoft Internet Explorer")
		{					
				resultSetDivObj.height = "400";
		}
		else
		{
				resultSetDivObj.height = "400";
		}
	}
	else  							   //Clicked on + image
	{
		if(navigator.appName == "Microsoft Internet Explorer")
		{					
			dataObj.style.display = 'block';
			resultSetDivObj.height = "320";
		}
		else
		{
			dataObj.style.display = 'table-row';
			dataObj.style.display = 'block';
			resultSetDivObj.height = "320";
		}
		switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0"/>';

	}
}

function operatorChanged(rowId,dataType)
{

var textBoxId = rowId+"_textBox1";
var calendarId1 = rowId+"_calendar1";
var textBoxId0 = rowId+"_textBox";
var calendarId0 = "calendarImg";
var opId =  rowId+"_combobox";
var dateFormatLabel = rowId+"_dateFormatLabel2";
if(document.all) {
		// IE.
		var op = document.getElementById(opId).value;
	} else if(document.layers) {
		// Netspace 4
	var op = document.getElementById(opId).value;
	} else {
		// Mozilla
		op = document.forms['categorySearchForm'].elements[opId].value;
	}	
	if(op == "Is Null" || op== "Is Not Null")
	{
		document.getElementById(textBoxId0).value = "";
			document.getElementById(textBoxId0).disabled = true;
			if(dataType == "true")
			{
				document.getElementById(calendarId0).disabled = true;
			}	
	} 	
	if(op == "In" || op== "Not In")
	{
		
	}
	else
	{
			document.getElementById(textBoxId0).disabled = false;
			if(dataType == "true")
			{
				document.getElementById(calendarId0).disabled = false;
			}	
	}
if(op == "Between")
{
	if(document.all) {
		// IE.
		document.getElementById(textBoxId).style.display="block";		
		if(dataType == "true")
		{
			document.getElementById(calendarId1).style.display="block";		
			document.getElementById(dateFormatLabel).style.display="block";		
		}
	} else if(document.layers) {
		// Netspace 4
		document.elements[textBoxId].visibility="visible";
	} else {
		// Mozilla
		var textBoxId1 = document.getElementById(textBoxId);
		textBoxId1.style.display="block";
		if(dataType == "true")
		{
			var calId = document.getElementById(calendarId1);
			var dateFormatLabelId = document.getElementById(dateFormatLabel);
			calId.style.display="block";
			dateFormatLabelId.style.display="block";
		}
	}	
}
else if(op == "In" || op== "Not In")
	{
		
	}	
else 
{
	if(document.all) {
		// IE.
		document.getElementById(textBoxId).style.display="none";		
		if(dataType == "true")
		{
		document.getElementById(calendarId1).style.display="none";	
		var dateFormatLabelId = document.getElementById(dateFormatLabel);
			dateFormatLabelId.style.display="none";
		}
	} else if(document.layers) {
		// Netspace 4
		document.elements[textBoxId].visibility="none";
	} else {
		// Mozilla
		var textBoxId1 = document.getElementById(textBoxId);
		textBoxId1.style.display="none";

		if(dataType == "true")
		{
			var calId = document.getElementById(calendarId1);
			calId.style.display="none";
			var dateFormatLabelId = document.getElementById(dateFormatLabel);
			dateFormatLabelId.style.display="none";
		}
	}	
}
}
function expandCollapseDag()
{
}


function retriveSearchedEntities(url,nameOfFormToPost,currentPage) 
{
	var request = newXMLHTTPReq();		
	var textFieldValue = document.forms[0].textField.value;
	var classCheckStatus = document.forms[0].classChecked.checked;
	var attributeCheckStatus = document.forms[0].attributeChecked.checked;
	var permissibleValuesCheckStatus = document.forms[0].permissibleValuesChecked.checked;
	var radioCheckStatus;
	var actionURL;
	if(currentPage == 'null')
	{
		var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
		actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&selected=" + radioCheckStatus+"&currentPage=AddLimits";
	}
	else
	{
		//document.forms['categorySearchForm'].currentPage.value = "DefineResultsView";
	//	url = url+"?currentPage=DefineResultsView";
		actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&selected=" + radioCheckStatus +"&currentPage=DefineResultsView";
		var handlerFunction = getReadyStateHandler(request,showEntityList,true);
	}
	


	request.onreadystatechange = handlerFunction;
			
	if(document.forms[0].selected[0].checked)
		radioCheckStatus = "rb1";
	if(document.forms[0].selected[1].checked)
		radioCheckStatus = "rb2";



	if(!(classCheckStatus || attributeCheckStatus || permissibleValuesCheckStatus) ) 
	{
		alert("Please select any of the checkbox ");
		onResponseUpdate(" ");
	}
	else if(textFieldValue == "")
	{
		alert("Please Enter the String to search.");
		onResponseUpdate(" ");
	}
	else if(radioCheckStatus == null)
	{
		alert("Please select any of the radio button : 'based on' criteria");
		onResponseUpdate(" ");
	}
		
	else
	{
		//<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);
	}
}
function showEntityList(text)
{
	if(text == "")
	{
		alert("No results found.");
	}
	var element = document.getElementById('resultSet');
	var row ='<table border="1" width="20%" height="60%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">';
	row = row + '<tr>' + text + '</tr>';
	row = row+'</table>';		
	element.innerHTML =row;
}
/*function showtip(a,event,text)
{
		alert("sda  "+text);
}
function hidetip()
{
}*/
function onResponseUpdate(text)
{

	if(text == "")
	{
		alert("Zero Entitites found.");
	}
	if(text.indexOf("##") != -1)
	{
		var currentPageText = text.split("##");
		var currentPage = currentPageText[0];
		text = currentPageText[1];
	}

	var element = document.getElementById('resultSet');
	var listOfEntities = text.split(";");
	var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';

		for(i=1; i<listOfEntities.length; i++)
		{
			var e = listOfEntities[i];			
			var nameDescription = e.split("|");		
			var name = nameDescription[0];				
			var description = nameDescription[1];
			
			var lastIndex = name.lastIndexOf(".");
			var entityName = name.substring(lastIndex + 1);
			if(currentPage != "DefineResultsView")
			{
			var functionCall = "retriveEntityInformation('loadDefineSearchRules.do','categorySearchForm','"+name+"')";					
			row = row+'<tr><td><a class="entityLink" title="'+description+'"  href="javascript:'+functionCall+'">' +entityName+ '</a></td></tr>';
			}
			else
			{
				row = row + '<tr><td class="entityLink" title="'+description+'">' + entityName + ' </td></tr>';
			}
			//row =row+ '<tr><td><a href=href="javascript:'+functionCall+'" onMouseover="showtip(this,event,'description  wds')" onMouseout="hidetip()">' +entityName+ '</a></td></tr>';
			
		}			
	row = row+'</table>';		
	element.innerHTML =row;
}
function retriveEntityInformation(url,nameOfFormToPost,entityName) 
{	
	var request = newXMLHTTPReq();			
	var actionURL;
	var handlerFunction = getReadyStateHandler(request,showEntityInformation,true);	
	request.onreadystatechange = handlerFunction;				
	actionURL = "entityName=" + entityName;				
	
	<!-- Open connection to servlet -->
	request.open("POST",url,true);	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	request.send(actionURL);		
} 
function showEntityInformation(text)
{					
	var element = document.getElementById('addLimits');
	element.innerHTML =text;
}

function produceQuery(isTopButton, url,nameOfFormToPost, entityName , attributesList) 
{
	var strToCreateQueyObject ="";
	var attribute = attributesList.split(";");
	for(i=1; i<attribute.length; i++)
	{
		var opId =  attribute[i]+"_combobox";
		var textBoxId = attribute[i]+"_textBox";
		var textBoxId1 = attribute[i]+"_textBox1";
		var enumBox = attribute[i]+"_enumeratedvaluescombobox";

		if(navigator.appName == "Microsoft Internet Explorer")
		{					
			var op = document.getElementById(opId).value;
			if(document.forms[nameOfFormToPost].elements[enumBox])
			{
				enumValue = document.getElementById(enumBox).value;
			}
		}
		else
		{
			var op = document.forms[nameOfFormToPost].elements[opId].value;
			if(document.forms[nameOfFormToPost].elements[enumBox])
			{
				enumValue = document.forms[nameOfFormToPost].elements[enumBox].value;	
			}
		}		
		if(op != "Between")
		{
			if (document.getElementById(textBoxId))
			{
				textId = document.getElementById(textBoxId).value;		
				if(textId != "")
				{
					strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +";";
				}
			}
			if(navigator.appName == "Microsoft Internet Explorer")
				{
					if(document.getElementById(enumBox))
						var ob =  document.getElementById(enumBox);
				}
				else
				{
					if(document.forms[nameOfFormToPost].elements[enumBox])
						var ob = document.forms[nameOfFormToPost].elements[enumBox];
				}	

				if(ob)
				{
					if(ob.value != "")
					{
						var arSelected = new Array();			
						while(ob.selectedIndex != -1)
						{
							var selectedValue = ob.options[ob.selectedIndex].value;
							arSelected.push(selectedValue);
							ob.options[ob.selectedIndex].selected = false;
						}
						var values = arSelected.toString();
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + values +";";
					}
				}
				else
				{
					//var element = document.getElementById('validationMessages');
					var row = document.getElementById('validationMessagesRow');
					row.innerHTML = "";
				}
		}
		if(op == "Between")
		{
			if(document.getElementById(textBoxId1))
			{
				textId1 = document.getElementById(textBoxId1).value;
			}
			if (document.getElementById(textBoxId))
			{
				textId = document.getElementById(textBoxId).value;		
			}
			if(textId != "")
			{
				strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+"missingTwoValues"+";";
			}
			if(textId1 != "")
			{
				strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + "missingTwoValues" +"!*=*!"+"textId1"+";";
			}
			if(textId != "" && textId1!= "")
			{
				strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+textId1+";";
			}
		/*	else if ( textId1 == "" || textId == "" )
			{
				alert("Please enter two values to add limit for operator between");
				strToCreateQueyObject = "";
				i = attribute.length;
			}*/
		}
		if(op == "Is Null" || op == "Is Not Null")
		{
			strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op +";";
		}
	}
	if(navigator.appName == "Microsoft Internet Explorer")
	{
		if(isTopButton)
		{
			var isEditLimit = document.getElementById('TopAddLimitButton').value;
		}else 
		{
			var isEditLimit = document.getElementById('BottomAddLimitButton').value;
		}

	}else
	{
	if(isTopButton)
		{
			var isEditLimit = document.forms[nameOfFormToPost].elements["TopAddLimitButton"].value;
		}else 
		{
			var isEditLimit = document.forms[nameOfFormToPost].elements["BottomAddLimitButton"].value;
		}
	}
	if(isEditLimit == 'Add Limit')
	{	
		document.applets[0].addExpression(strToCreateQueyObject,entityName);
	} else if(isEditLimit == 'Edit Limit')
	{
		document.applets[0].editExpression(strToCreateQueyObject,entityName);
	}
}
function viewSearchResults()
{
	var errorMessage = document.applets[0].getSearchResults();
	if(errorMessage == null)
	{
		 showViewSearchResultsJsp();
	}
	else if (errorMessage == "<li><font color=\"red\">showErrorPage</font></li>")
	{
		showErrorPage();
	}
	else {
		showValidationMessages(errorMessage);
	}
}
function showValidationMessages(text)
{
	var rowId= 'validationMessagesRow';
	var textBoxId1 = document.getElementById("rowMsg");
		
	var element = document.getElementById('validationMessages');
	var row = document.getElementById(rowId);
	row.innerHTML = "";
	if(text == "")
	{

		if(document.all)
		{
			//	textBoxId1.style.display= "none";
			document.getElementById("validationMessagesRow").style.display="none";		

		} 
		else if(document.layers) 
		{
			document.elements['validationMessagesRow'].visibility="none";
		}
		else 
		{
			//	textBoxId1.style.display= "none";
			row.style.display = 'none';		

		}	
	}
	else
	{
	//	textBoxId1.style.display= "block";
		row.style.display = 'block';
		row.innerHTML = text;
	}	
}
function showErrorPage()
{
	document.forms['categorySearchForm'].action='ViewSearchResultsJSPAction.do';
	document.forms['categorySearchForm'].nextOperation.value = "showErrorPage";
	document.forms['categorySearchForm'].submit();	
}
function showViewSearchResultsJsp()
{
	document.forms['categorySearchForm'].action='ViewSearchResultsJSPAction.do';
	document.forms['categorySearchForm'].submit();			
}
function saveClientQueryToServer()
{
	document.applets[0].defineResultsView();
	defineSearchResultsView();
}
function defineSearchResultsView()
{
	document.forms['categorySearchForm'].action='DefineSearchResultsView.do';
	document.forms['categorySearchForm'].submit();
}
function setFocusOnSearchButton()
{
	alert(document.forms[0].searchButton11.onclick);
	document.getElementById(searchButton11).onclick();
}
function showAddLimitsPage()
{
	document.forms['categorySearchForm'].action='SearchCategory.do';
	document.forms['categorySearchForm'].currentPage.value = "AddLimits222";
	document.forms['categorySearchForm'].submit();
}
function previousFromDefineResults()
{
	document.forms['categorySearchForm'].action='SearchCategory.do';
	document.forms['categorySearchForm'].currentPage.value = "prevToAddLimits";
	document.forms['categorySearchForm'].submit();
}

		/*function viewAddLimitsPage()
		{
			document.forms['categorySearchForm'].action = "SearchCategory.do";
			document.forms['categorySearchForm'].submit();
		}
		function onProduceQueryUpdate(expressionId)
		{
			document.applets[0].addExpression(expressionId);
		}
		function clearForm1(attributesList)
		{
			var a = attributesList.split(";");
			for(i=1; i<a.length; i++)
			{
				var opId =  a[i]+"_combobox";
				var textBoxId = a[i]+"_textBox";
				var textId = document.getElementById(textBoxId).value="";
				var textBoxId1 = a[i]+"_textBox1";
				var textId1 = document.getElementById(textBoxId1).value="";
				var op = document.forms['categorySearchForm'].elements[opId].value="";
			}
		}
		function produceQuery(url,nameOfFormToPost, entityName , attributesList) 
		{
			//var element = document.getElementById('query');
			var strToCreateQueyObject ="";
			var attribute = attributesList.split(";");
			var textThis = "";
			queryString ="";
			var stringQuery = "";
			for(i=1; i<attribute.length; i++)
			{
				var opId =  attribute[i]+"_combobox";
				var textBoxId = attribute[i]+"_textBox";
				var textId = document.getElementById(textBoxId).value;
				var textBoxId1 = attribute[i]+"_textBox1";
				var textId1 = document.getElementById(textBoxId1).value;
				if(navigator.appName == "Microsoft Internet Explorer")
					{					
						var op = document.getElementById(opId).value;
					}
					else
					{
						var op = document.forms[nameOfFormToPost].elements[opId].value;
					}					
				if(op != "Between")
				{
					if(textId != "")
					{
						if(i!=attribute.length-1)
						{
							textThis = textThis + " "+ attribute[i] + " " + op + " " + textId + ";  ";								
						}
						if(i==attribute.length-1)
						{
							textThis = textThis + " "+ attribute[i] + " " + op + " " + textId+ ";  ";			
						}
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +";";
					}
				}
				if(op == "Between")
				{
					if(textId != "" && textId1!= "")
					{
						if(i!=attribute.length-1)
						{
							textThis = textThis + " "+ attribute[i] + " " + op + " (" + textId +", "+textId1 +") ;  ";					
						}
						if(i==attribute.length-1)
						{
							textThis = textThis + " "+ attribute[i] + " " + op + " (" + textId +", "+textId1 +") ;  ";	
						}
							strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+textId1+";";
					}
				}
				if(op == "Is Null" || op == "Is Not Null")
				{
					
				}				
			}
		
			document.applets[0].addExpression(strToCreateQueyObject,entityName);
		}
		function expandSearchHeader()
			{			
				switchObj = document.getElementById('imageCategorySearch');
			//	collapsableHeaderObj = document.getElementById('collapsableHeader');
				collapsableTableObj = document.getElementById('collapsableTable');
				resultSetDivObj = document.getElementById('resultSetDiv');
			
				if(collapsableTableObj.style.display != 'none') //Clicked on - image
				{
				//	collapsableHeaderObj.style.display = 'none';	
					collapsableTableObj.style.display= 'none';
					switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0"/>';
					resultSetDivObj.height = 450;
				}
				else  							   //Clicked on + image
				{
					if(navigator.appName == "Microsoft Internet Explorer")
					{					
					//	collapsableHeaderObj.style.display = 'block';
						collapsableTableObj.style.display= 'block';
					}
					else
					{
					//	collapsableHeaderObj.style.display = 'table-row';
						collapsableTableObj.style.display = 'table-row';

					}
					switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0"/>';
					resultSetDivObj.height = 350;
				}
			}*/
					

