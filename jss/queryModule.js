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
	
	var addedNodes = "";
	function treeNodeClicked(id)
	{
		if(id.indexOf('_NULL') == -1)
		{
			var aa = id.split("::");		
			var nodes = addedNodes.split(",");
			var isNodeAdded = false;
			if(nodes != "")
			{
			for(i=0; i<nodes.length; i++)
				{
					if(nodes[i] == id)
					{
						isNodeAdded = true;
						break;
					}
				}
			}
			if(!isNodeAdded)
			{
				
				var request = newXMLHTTPReq();			
				var actionURL;
				var handlerFunction = getReadyStateHandler(request,showChildNodes,true);	
				request.onreadystatechange = handlerFunction;				
				actionURL = "nodeId=" + id;				
				var url = "BuildQueryOutputTree.do";
				<!-- Open connection to servlet -->
				request.open("POST",url,true);	
				request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
				request.send(actionURL);	
				addedNodes = addedNodes + ","+id;
			}
		}
		buildSpreadsheet(id);
	}
	function buildSpreadsheet(id)
	{
		window.parent.frames[1].location = "ShowGrid.do?pageOf=pageOfQueryModule&nodeId="+id;
	}
	function showChildNodes(outputTreeStr)
	{
		var nodes = outputTreeStr.split("|");
		var flag = new Boolean(false);
		for(i=0; i<nodes.length; i++)
		{
			var node = nodes[i];
			if(node != "")
			{
				var treeValues = node.split(",");
				var nodeId = treeValues[0];
				var treeNums = nodeId.split('_');
				var i1= treeNums[0];
				var displayName = treeValues[1];
				var objectname = treeValues[2];
				var parentIdToSet = treeValues[3];
				/*if(parentIdToSet.indexOf('NULL')!=-1)
				{
				  if(flag == false)
					{
						clearTree(parentIdToSet,i1);
						flag = true;
					}
				}*/
				var parentObjectName = treeValues[4];
				var img = "results.gif";
				var totalLen= nodeId.length;
				var labelLen = 'Label'.length;
				var diff= totalLen - labelLen;
				var lab = nodeId.substring(diff);
				if(lab == 'Label')
				{
					 img = "folder.gif";
				}
				trees[i1].insertNewChild(parentIdToSet,nodeId,displayName,0,img,img,img,"");
			}
		}
	}
	function clearTree(id,treeNum)
	{
		tree[treeNum].deleteChildItems(id);
		addedNodes = "";
	}	
	function showSpreadsheetData(columnDataStr)
	{
		var columnData = columnDataStr.split("&");
		var columns = columnData[0];	
		var data = columnData[1];	
		var columnNames = columns.split(",");
		var width ="";
		var colDataTypes1 = ""
		var colTypes1 = "";
		if(columns != 'Entity Name, Count')
		{
			var width =180 +",";
			var colDataTypes1 = "ch,"
			var colTypes1 = "ch,";
		}
		for(i=0; i<columnNames.length; i++)
		{
			var name = columnNames[i];
			if(!name == "")
			{
				width = width + "180,"
				colDataTypes1 = colDataTypes1 + "ro,";
				colTypes1 = colTypes1 +"str,";
			}		
		}		
		mygrid.clearAll();
		mygrid.setHeader(columns);
		mygrid.setInitWidths(width);
		mygrid.setColTypes(colDataTypes1);
		mygrid.setColSorting(colTypes1);
	//	mygrid.enableAutoHeigth(true);
		mygrid.init();
		var myData = data.split("|");
		for(var row=0;row<myData.length;row++)
		{
			if(row != "")
			{
				if(columns == 'Entity Name, Count')
				{
					data = myData[row];
				}
				else
				{
					data = "0,"+myData[row];
				}
				mygrid.addRow(row+1,data,row+1);
			}
		}	
	}
	function addToView()
	{
		var row = document.getElementById('validationMessagesRow');
		row.innerHTML = "";	
		row.style.display = 'none';		
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
		if(document.getElementById(textBoxId0))
		{
			document.getElementById(textBoxId0).value = "";
		}
		if(document.all)
		{
			var op = document.getElementById(opId).value;
		}
		else if(document.layers)
		{
			var op = document.getElementById(opId).value;
		} 
		else
		{
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
		if(op == "Is Null" || op== "Is Not Null")
		{
			document.getElementById(textBoxId0).disabled= true;
		} 
		else
		{
			if(document.getElementById(textBoxId0))
			{
				document.getElementById(textBoxId0).disabled= false;
			}
		}
		if(op == "Between")
		{
			if(document.all) 
			{
				document.getElementById(textBoxId0).value = "";
				document.getElementById(textBoxId).style.display="block";		
				if(dataType == "true")
				{
					document.getElementById(calendarId1).style.display="block";		
				}
			} 
			else if(document.layers) 
			{
				document.elements[textBoxId0].value = "";
				document.elements[textBoxId].visibility="visible";
			}
			else
			{
				document.getElementById(textBoxId0).value = "";
				var textBoxId1 = document.getElementById(textBoxId);
				textBoxId1.style.display="block";
				if(dataType == "true")
				{
					var calId = document.getElementById(calendarId1);
					calId.style.display="block";
				}
			}	
		}
		else if(op == "In" || op== "Not In")
		{
			if(document.all)
			{
				document.getElementById(textBoxId).style.display="none";		
				if(dataType == "true")
				{
					document.getElementById(calendarId1).style.display="none";	
				}
			}
			else if(document.layers)
			{
				document.elements[textBoxId].visibility="none";
			}
			else
			{
				var textBoxId1 = document.getElementById(textBoxId);
				if(textBoxId1)
				{
					textBoxId1.style.display="none";
				}
				if(dataType == "true")
				{
					var calId = document.getElementById(calendarId1);
					calId.style.display="none";
				}
			}	
		}	
		else 
		{
			if(document.all) 
			{
				document.getElementById(textBoxId).style.display="none";		
				if(dataType == "true")
				{
					document.getElementById(calendarId1).style.display="none";	
				}
			}
			else if(document.layers)
			{
				document.elements[textBoxId].visibility="none";
			} 
			else
			{
				var textBoxId1 = document.getElementById(textBoxId);
				textBoxId1.style.display="none";
				if(dataType == "true")
				{
					var calId = document.getElementById(calendarId1);
					calId.style.display="none";
				}
			}	
		}
	}
	function expandCollapseDag()
	{
	}
	function setFocusOnSearchButton(e)
	{
		if (!e) var e = window.event
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		if(code == 13)
		{
			document.getElementById('searchButton').focus();
		//	document.getElementById('searchButton').onclick();
		}
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
		if(document.forms[0].selected[0].checked)
			radioCheckStatus = "rb1";
		else if(document.forms[0].selected[1].checked)
			radioCheckStatus = "rb2";
		if(currentPage == 'null')
		{
			var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&selected=" + radioCheckStatus+"&currentPage=AddLimits";
		}
		else
		{
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&selected=" + radioCheckStatus +"&currentPage=DefineResultsView";
			var handlerFunction = getReadyStateHandler(request,showEntityList,true);
		}
		request.onreadystatechange = handlerFunction;
				
		
		if(!(classCheckStatus || attributeCheckStatus || permissibleValuesCheckStatus) ) 
		{
			alert("Please choose at least one option for metadata search from advanced options ");
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
			request.open("POST",url,true);	
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
			request.send(actionURL);
		}
	}
	function showEntityList(text)
	{
		if(text == "")
		{
			alert("Zero Entitites found.");
		}
		var element = document.getElementById('resultSet');
		var row ='<table width="100%" height="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">';
		row = row + '<tr>' + text + '</tr>';
		row = row+'</table>';		
		element.innerHTML =row;
	}
	
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
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	} 
	function showEntityInformation(text)
	{				
		var row = document.getElementById('validationMessagesRow');
		row.innerHTML = "";	
		row.style.display = 'none';		
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
			
			//var radioButtonFalse = attribute[i]+"_radioButton_false";
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
				var radioButtonTrue = attribute[i]+"_radioButton_true";
			    var radioButtonFalse = attribute[i]+"_radioButton_false";
				if(document.getElementById(radioButtonTrue) != null  || document.getElementById(radioButtonFalse)!= null)
				{
					var objTrue = document.getElementById(radioButtonTrue);
					var objFalse = document.getElementById(radioButtonFalse);
					if(objTrue.checked)
					{
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + 'true' +";";
					}
					else if(objFalse.checked)
					{
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + 'false' +";";
					}
				}
				else
				{
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
			}
			else 
			{
				var isEditLimit = document.getElementById('BottomAddLimitButton').value;
			}
	
		}else
		{
		if(isTopButton)
			{
				var isEditLimit = document.forms[nameOfFormToPost].elements["TopAddLimitButton"].value;
			}
			else 
			{
				var isEditLimit = document.forms[nameOfFormToPost].elements["BottomAddLimitButton"].value;
			}
		}
		if(isEditLimit == 'Add Limit')
		{	
			document.applets[0].addExpression(strToCreateQueyObject,entityName);
		}
		else if(isEditLimit == 'Edit Limit')
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
		else
		{
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
				document.getElementById("validationMessagesRow").style.display="none";		
			} 
			else if(document.layers) 
			{
				document.elements['validationMessagesRow'].visibility="none";
			}
			else 
			{
				row.style.display = 'none';		
			}	
		}
		else
		{
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
		var message = document.applets[0].defineResultsView();
		if(message != "")
		{
			showValidationMessages(message);
		}
		else
		{
			defineSearchResultsView();
		}
	}
	function defineSearchResultsView()
	{
		document.forms['categorySearchForm'].action='DefineSearchResultsView.do';
		document.forms['categorySearchForm'].submit();
	}
	function showAddLimitsPage()
	{
		document.forms['categorySearchForm'].action='SearchCategory.do';
		document.forms['categorySearchForm'].currentPage.value = "AddLimits222";
		document.forms['categorySearchForm'].submit();
	}
	function previousFromDefineResults()
	{
		document.applets[0].defineResultsView();
		document.forms['categorySearchForm'].action='SearchCategory.do';
		document.forms['categorySearchForm'].currentPage.value = "prevToAddLimits";
		document.forms['categorySearchForm'].submit();
	}
