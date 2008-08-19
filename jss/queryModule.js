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
	var isFirtHit = true;
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
		isFirtHit = false;
		if(!isFirtHit)
		{
			//alert(isFirtHit);
		  buildSpreadsheet(id);
		  
		}
		isFirtHit = false;
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
				var parentChildNode = nodeId.split('::');
				var childNode= parentChildNode[1];
				var treeNums = childNode.split('_');
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
					 img = "ic_folder.gif";
				}
				trees[i1].insertNewChild(parentIdToSet,nodeId,displayName,0,img,img,img,"");
				var start = displayName.indexOf("<i>");
				if (start == -1)
				{
					trees[i1].setItemText(nodeId,displayName,displayName);
					
				}
				else
				{
					start = start + 3;
					var end = displayName.indexOf("</i>");
					end = end + 0;
					var name = displayName.substring(start, end);
					trees[i1].setItemText(nodeId,displayName,name);
					
				}
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

	function expand()
	{			
		switchObj = document.getElementById('image');
		dataObj = document.getElementById('collapsableTable');
        var td1 = document.getElementById('td1');
		var td2 = document.getElementById('td3');
		resultSetDivObj = document.getElementById('resultSetTd');
		var resultSetDiv = document.getElementById('resultSet');
	    //var advancedSearchHeaderTd = document.forms[0].elements['advancedSearchHeaderTd'];
		var advancedSearchHeaderTd = document.getElementById('advancedSearchHeaderTd');
		var imageContainer = document.getElementById('imageContainer');
        
		 	   
	   if(dataObj.style.display != 'none') //Clicked on - image
		{
			advancedSearchHeaderTd.style.borderBottom = "1px solid #000000";
            imageContainer.style.borderBottom = "1px solid #000000";
			dataObj.style.display = 'none';				
			switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" hspace="3" vspace="3"/>';
			if(navigator.appName == "Microsoft Internet Explorer")
			{					
				resultSetDivObj.height = "530";
			}
			else
			{
				resultSetDivObj.height = "530";
			}
			resultSetDiv.style.height = "530"+'px';
		}
		else  							   //Clicked on + image
		{
            advancedSearchHeaderTd.style.borderBottom = "0";
			imageContainer.style.borderBottom = "0";
			if(navigator.appName == "Microsoft Internet Explorer")
			{					
				dataObj.style.display = 'block';
				td1.style.display = 'block';
				td2.style.display = 'block';
				resultSetDivObj.height = "420";
			}
			else
			{
				dataObj.style.display = 'table-row';
				dataObj.style.display = 'block';
				td1.style.display = 'block';
				td2.style.display = 'block';
				resultSetDivObj.height = "420";
			}
			resultSetDiv.style.height = "420"+'px'
			switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" hspace="3" vspace="3"/>';
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
		else if(document.forms[0].name=='saveQueryForm')
		{
            var op = document.forms['saveQueryForm'].elements[opId].value;    
		}
		else if(document.forms[0].name=='fetchQueryForm')
		{
			op = document.forms['fetchQueryForm'].elements[opId].value;
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
			var platform = navigator.platform.toLowerCase();
			if (platform.indexOf("mac") == -1)
			{						
				document.getElementById('searchButton').focus();
			}	
		} 
		else return true;
		
	}
	function retriveSearchedEntities(url,nameOfFormToPost,currentPage, key) 
	{
		waitCursor();		
		
		var request = newXMLHTTPReq();		
		var textFieldValue = document.forms[0].textField.value;
		var classCheckStatus = document.forms[0].classChecked.checked;
		var attributeCheckStatus = document.forms[0].attributeChecked.checked;
		var permissibleValuesCheckStatus = document.forms[0].permissibleValuesChecked.checked;
		var includeDescriptionCheckedStatus = document.forms[0].includeDescriptionChecked.checked;
		
		var radioCheckStatus;
		var actionURL;
		
		// Bug #5131: Setting 'text' mode as default
		radioCheckStatus="text_radioButton";
		/*
		if(document.forms[0].selected[0].checked)
			radioCheckStatus = "text_radioButton";
		else if(document.forms[0].selected[1].checked)
			radioCheckStatus = "conceptCode_radioButton";
		*/

		if(currentPage == 'null')
		{
			var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&includeDescriptionChecked="+includeDescriptionCheckedStatus+ "&selected=" + radioCheckStatus+"&currentPage=AddLimits&key="+key;
		}
		else
		{
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&includeDescriptionChecked="+includeDescriptionCheckedStatus+ "&selected=" + radioCheckStatus +"&currentPage=DefineResultsView";
			var handlerFunction = getReadyStateHandler(request,showEntityListOnDefineViewPage,true);
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
	function showEntityListOnDefineViewPage(text)
	{
		var element = document.getElementById('resultSet');
		if(text.indexOf("No result found") != -1)
		{
			element.innerHTML =text;
		} 
		else
		{
			var listOfEntities = text.split(";");
			var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';
			for(i=1; i<listOfEntities.length; i++)
			{
				var e = listOfEntities[i];			
				var nameIdDescription = e.split("|");		
				var name = nameIdDescription[0];
				var id = nameIdDescription[1];				
				var description = nameIdDescription[2];
				var functionCall = "addNodeToView('"+id+"')";		
				var entityName = "<font color=#0000CC>"+name +"</font>";
				var tooltipFunction = "Tip('"+description+"', WIDTH, 200)";				
				row = row+'<tr><td><a  class="entityLink" onmouseover="'+tooltipFunction+'"  href="javascript:'+functionCall+'">' +entityName+ '</a></td></tr>';
				
			}
			row = row+'</table>';		
			element.innerHTML =row;
		}
		hideCursor();
	}
	
	function onResponseUpdate(text)
	{
		
		var element = document.getElementById('resultSet');
		if(text.indexOf("No result found") != -1)
		{
			element.innerHTML =text;
		} 
		else
		{
		
			var listOfEntities = text.split(";");
			var length = listOfEntities.length;
			var temp = listOfEntities[length-1].split("*&*");
			var key = temp[1];
			var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';
			for(i=1; i<listOfEntities.length; i++)
			{
				var e = listOfEntities[i];			
				var nameIdDescription = e.split("|");		
				var name = nameIdDescription[0];
				var id = nameIdDescription[1];				
				var description = nameIdDescription[2];
				var functionCall = "retriveEntityInformation('loadDefineSearchRules.do','categorySearchForm','"+id+"')";		
				var entityName = "<font color=#0000CC>"+name +"</font>";
				var tooltipFunction = "Tip('"+description+"', WIDTH, 200)";				
				row = row+'<tr><td ><a  class="entityLink"  onmouseover="'+tooltipFunction+'"  href="javascript:'+functionCall+'">' +entityName+ '</a></td></tr>';
			}			
			row = row+'</table>';		
			element.innerHTML =row;
			
			if (key != null && key == 13 && listOfEntities.length == 2)
			{
				var e = listOfEntities[1];	
				var nameIdDescription = e.split("|");	
				var id = nameIdDescription[1];		
				retriveEntityInformation('loadDefineSearchRules.do','categorySearchForm', id);	
			}
		}
		hideCursor();
	}
	function retriveEntityInformation(url,nameOfFormToPost,entityName) 
	{	
		waitCursor();
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
		var addLimitsButtonElement = document.getElementById('AddLimitsButtonRow');
		if(text.indexOf("####") != -1)
		{
			var htmlArray = text.split('####');
			addLimitsButtonElement.style.display = 'block';
			//addLimitsButtonElement.height = "30";
			addLimitsButtonElement.innerHTML = htmlArray[0];
			element.innerHTML =htmlArray[1];
		} else 
		{
			element.innerHTML = "";
			addLimitsButtonElement.innerHTML = text;
		}
		hideCursor();
	}
	
	function createQueryStringForSavedQuery(nameOfFormToPost, entityName , attributesList,callingFrom)
	{
		waitCursor();
		
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
				if(document.getElementById(enumBox))
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
					/*if(textId != "")
					{*/
						if(op == "In" || op =="Not In")
						{
							var valString = "";
							var inVals = textId.split(",");
							for(g=0; g<inVals.length; g++)
							{
								if(inVals[g] != "")
								{
									valString = valString  + "&" + inVals[g];
								}
							}
							strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + valString +";";
						} else 
						{
							if(textId == "")
								strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + " " +";";
							else
								strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +";";
						}
					//}
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
						var values = "";
						while(ob.selectedIndex != -1)
						{
							var selectedValue = ob.options[ob.selectedIndex].value;
							values = values + "&" +  selectedValue;
							ob.options[ob.selectedIndex].selected = false;
						}
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + values +";";
					}
					else if(ob.value == "")
					{
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + " " +";";
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
					else if(!objTrue.checked && !objFalse.checked)
					{
						strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + " " +";";
					}
				}
				else
				{
				   if(callingFrom=='addLimit')
					{
				 	 var row = document.getElementById('validationMessagesRow');
				 	 row.innerHTML = "";
					}
					
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
				if(textId != "" && textId1 == "")
				{
					strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+"missingTwoValues"+";";
				}
				if(textId1 != "" && textId == "")
				{
					strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + "missingTwoValues" +"!*=*!"+"textId1"+";";
				}
				if(textId != "" && textId1!= "")
				{
					strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+textId1+";";
				}
				if(textId == "" && textId1== "")
				{
					strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + " " +"!*=*!"+" "+";";
				}
			}
			if(op == "Is Null" || op == "Is Not Null")
			{
				strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op +";";
			}
		}
           return strToCreateQueyObject;
	}
	
	 function createQueryString(nameOfFormToPost, entityName , attributesList,callingFrom)
        {
         waitCursor();
	
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
				if(document.getElementById(enumBox))
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
						if(op == "In" || op =="Not In")
						{
							var valString = "";
							var inVals = textId.split(",");
							for(g=0; g<inVals.length; g++)
							{
								if(inVals[g] != "")
								{
									valString = valString  + "&" + inVals[g];
								}
							}
							strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + valString +";";
						} else 
						{
							strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +";";
						}
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
						var values = "";
						while(ob.selectedIndex != -1)
						{
							var selectedValue = ob.options[ob.selectedIndex].value;
							values = values + "&" +  selectedValue;
							ob.options[ob.selectedIndex].selected = false;
						}
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
				   if(callingFrom=='addLimit')
					{
				 	 var row = document.getElementById('validationMessagesRow');
				 	 row.innerHTML = "";
					}
					
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
				if(textId != "" && textId1 == "")
				{
					strToCreateQueyObject =  strToCreateQueyObject + "@#condition#@"+ attribute[i] + "!*=*!" + op + "!*=*!" + textId +"!*=*!"+"missingTwoValues"+";";
				}
				if(textId1 != "" && textId == "")
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
  
           return strToCreateQueyObject;
          
         } 

	function produceQuery(isTopButton, url,nameOfFormToPost, entityName , attributesList) 
	{
        var strToCreateQueyObject = createQueryString(nameOfFormToPost, entityName , attributesList,'addLimit');
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
			//document.applets[0].addExpression(strToCreateQueyObject,entityName);
			addLimit(strToCreateQueyObject,entityName);
		}
		else if(isEditLimit == 'Edit Limit')
		{
			//document.applets[0].editExpression(strToCreateQueyObject,entityName);
			editLimits(strToCreateQueyObject,entityName);
			
		}
			hideCursor();
	}
	
	function viewSearchResults()
	{
        waitCursor();
		callFlexMethod();
     	if(interfaceObj.isDAGEmpty())
		{
			var message = 	"<li> <font color='red'>Graph must have atleast one node.</font> </li>";
			showValidationMessages(message)
		}
		else
		{
		 search();
		}
		 hideCursor();
	//	var errorMessage = document.applets[0].getSearchResults();
	/*	if(errorMessage == null)
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
      */  
	}
	function showValidationMessages(text)
	{
		var rowId= 'validationMessagesRow';
		var textBoxId1 = document.getElementById("rowMsg");
		var element = document.getElementById('validationMessages');
		var row = document.getElementById(rowId);
		closeWaitPage();
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
	
	function produceSavedQuery()
	{
		var totalentities = document.getElementById("totalentities").value;
		var numberOfEntities = totalentities.split(";");
        var strquery='';
        var count = numberOfEntities.length;
        
    	for(i=0;i<count-1 ;i++)
		{
			var entityName = numberOfEntities[i];
			var attributesListComponent = numberOfEntities[i]+"_attributeList";
			var attributesList = document.getElementById(attributesListComponent).value;
			var checkboxes = attributesList.split(";"); 
			
			for(j=1;j<checkboxes.length;j++)
			{
        		var comp = checkboxes[j]+'_checkbox';
				var val = document.getElementById(comp).checked;
				if(val==true)
					strquery = strquery + checkboxes[j] +";" ;                             
            }
		} 
		var strvalu = document.getElementById('queryString');
        strvalu.value =  strquery;
        var entityName="";
        var frmName = document.forms[0].name;
        var list = document.getElementById('attributesList').value;
    	var buildquerystr =  createQueryStringForSavedQuery(frmName, entityName , list,frmName);
        document.getElementById('conditionList').value = buildquerystr;
        // Save query
        document.getElementById('saveQueryForm').submit();
	}
	
	function ExecuteSavedQuery()
	{
		  showWaitPage();
	   	  var entityName="";
		  var frmName = document.forms[0].name;
          var list = document.getElementById('attributesList').value;
    	  var buildquerystr =  createQueryString(frmName, entityName , list,frmName);
          document.getElementById('conditionList').value = buildquerystr;
		  document.forms[0].submit();
    }
	
	function enableDisplayField(frm, textfield)
	{
	   var fieldName = textfield+'_displayName';
       var sts =  document.getElementById(fieldName).disabled;
          if(sts==true)
           document.getElementById(fieldName).disabled=false;
          else
            document.getElementById(fieldName).disabled=true;
	}
	
 	function saveQuerySubmitForm(frm,action)
 	{
 	  if(action=='preview')
 	  {
 	    frm.action="/previewExecuteQuery.do?action=preview";
 	    frm.submit();
 	  }
 	  else
 	  { 
 	    frm.action="/saveQuery.do";
 	  }
 	}
	function saveClientQueryToServer(action)
	{
		if(action=='next')
		{
			callFlexMethod();
						
			if(interfaceObj.isDAGEmpty())
			{
				//showValidationMessages("<li><font color='red'>Graph must have atleast one node.</font></li>")
				var message = 	"<li> <font color='red'>Graph must have atleast one node.</font></li>";
				showValidationMessages(message);
			}
			else
			{
				defineSearchResultsView();
			}
		}
		else if(action=='save')
		{
			waitCursor();
			var url = "LoadSaveQueryPage.do";
			platform = navigator.platform.toLowerCase();
		    if (platform.indexOf("mac") != -1)
			{
		    	NewWindow(url,'name',screen.width,screen.height,'yes');
		    }
		    else
		    {
		    	NewWindow(url,'name','870','600','yes');
		    }
			hideCursor();
		/*	var request = newXMLHTTPReq();			
			var actionURL;
			var handlerFunction = getReadyStateHandler(request,showAlertBox,true);	
			request.onreadystatechange = handlerFunction;				
			var url = "LoadSaveQueryPage.do?isAjax=true";
			request.open("POST",url,true);	
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
			request.send(actionURL);	*/	
		}
	}
	function showAlertBox(text)
	{
		if(text != "")
		{
			alert(text);
		}
		else
		{
			var url = "LoadSaveQueryPage.do";
			platform = navigator.platform.toLowerCase();
		    if (platform.indexOf("mac") != -1)
			{
		    	NewWindow(url,'name',screen.width,screen.height,'yes');
		    }
		    else
		    {
		    	NewWindow(url,'name','800','600','yes');
		    }
		}
	}

	
	function defineSearchResultsView()
	{
		waitCursor();
		document.forms['categorySearchForm'].action='DefineSearchResultsView.do';
		document.forms['categorySearchForm'].submit();
		hideCursor();
						
	}
	function showAddLimitsPage()
	{
		document.forms['categorySearchForm'].action='SearchCategory.do';
		document.forms['categorySearchForm'].currentPage.value = "AddLimits222";
		document.forms['categorySearchForm'].submit();
	}
	function previousFromDefineResults()
	{
		waitCursor();
		var action ="SearchCategory.do";
		document.forms['categorySearchForm'].action=action;
		document.forms['categorySearchForm'].isQuery.value = "true";  // change for flex
		document.forms['categorySearchForm'].currentPage.value = "prevToAddLimits";
		document.forms['categorySearchForm'].submit();
		hideCursor();
	}
	function setIncludeDescriptionValue()
	{
      var isClassChecked = document.forms[0].classChecked.checked;
	  var isArrtibuteChecked = document.forms[0].attributeChecked.checked;
	  var permissibleValuesChecked = document.forms[0].permissibleValuesChecked.checked;
	  var conceptCodeSelected =  document.forms[0].selected[1].checked;
	  if(isClassChecked==true || isArrtibuteChecked==true)
		{
		  if(permissibleValuesChecked == true || conceptCodeSelected == true )
			{
			   document.forms[0].includeDescriptionChecked.checked = false;
		      document.forms[0].includeDescriptionChecked.disabled = true;
			} else
			{
		          document.forms[0].includeDescriptionChecked.disabled = false;
			}
		}
	   else
		{
	       document.forms[0].includeDescriptionChecked.checked = false;
           document.forms[0].includeDescriptionChecked.disabled = true;
		}

	}
	var radio="";
	var toggleRadio ="";
	function resetOptionButton(id,currentObj)
	{ 
		
		//variable radio keeps track of which radio button is selected (True/False) and 
		// variable toggleRadio maintains the status of the radiobutton clicked (selected/not selected)
		if (id != radio) // if previous object and current object are not same
		{
			radio = id;						// set the current object id;
			toggleRadio = "false";			// default value of radio button
		}
		if (toggleRadio == "false")			//if radiobutton is deselected
		{
			currentObj.checked = true;		// select it
			toggleRadio = "true";			//set toggleRadio to radio button status
		}
		else							//if toggleRadio == "true" i.e. if radiobutton is selected
		{			
			currentObj.checked = false;		// deselect it
			toggleRadio = "false";			//set toggleRadio to radio button status
		}		
		
	}

	function radioButtonSelected(element)
	{
		if(element.value == 'conceptCode_radioButton')
		{
		  document.forms[0].includeDescriptionChecked.checked = false;
		  document.forms[0].includeDescriptionChecked.disabled = true;
		} else
		{
			 var permissibleValuesChecked = document.forms[0].permissibleValuesChecked.checked;
			 if(permissibleValuesChecked == true)
				{
			  	    document.forms[0].includeDescriptionChecked.checked = false;
					document.forms[0].includeDescriptionChecked.disabled = true;
				}
				else		
					  document.forms[0].includeDescriptionChecked.disabled = false;
		}
	}
function permissibleValuesSelected(element)
{
	var conceptCodeSelected =  document.forms[0].selected[1].checked;
	if(element.checked == true)
		{
		   document.forms[0].includeDescriptionChecked.checked = false;
		   document.forms[0].includeDescriptionChecked.disabled = true;
		} else
		{
			if(conceptCodeSelected == true)
			{
		  document.forms[0].includeDescriptionChecked.disabled = true;
			}
			else
			{
		  document.forms[0].includeDescriptionChecked.disabled = false;
			}
		}
  }
  
//---Flex Call
var interfaceObj;

	function callFlexMethod()
	{
		if(navigator.appName.indexOf("Microsoft") != -1)
		{
			interfaceObj = window["DAG"];				
		}
		else
		{
			interfaceObj = document["DAG"];
		}
	}

var jsReady = false;

// ——- functions called by ActionScript ——-
// called to check if the page has initialized and JavaScript is available
	function isReady()
	{
		return jsReady;
	}
// called by the onload event of the <body> tag
	function pageInit()
	 {
	// Record that JavaScript is ready to go.
		jsReady = true;
	}

	function addLimit(strToCreateQueyObject,entityName)
	{	
		callFlexMethod();
		interfaceObj.createNode(strToCreateQueyObject,entityName);
	}

	function editLimits(strToCreateQueyObject,entityName)
	{	
		callFlexMethod();
		interfaceObj.editLimit(strToCreateQueyObject,entityName);
	}
	
	window.onload=function(){
		pageInit();
	}
	
	 function search()
	 {
		 callFlexMethod();
		 interfaceObj.searchResult();
	 }
	
	function addNodeToView(nodesStr)
	{	
		callFlexMethod();
		interfaceObj.addNodeToView(nodesStr);
	}
	
	/*This function is called form QueryListView.jsp. It invokes the FetchAndExecuteQueryAction*/
	function executeQuery(queryId)
	{
		showWaitPage();
		document.getElementById('queryId').value=queryId;
		document.forms[0].submit();
	} 
	
	/*This function is called form QueryListView.jsp. Pops up for confirmation while deleting the query*/
	function deleteQueryPopup(queryId, popupMessage)
	{
		var r=confirm(popupMessage);
		if (r==true)
		{
			deleteQuery(queryId);
		}

	} 
	
	function deleteQuery(queryId)
	{
		action="DeleteQuery.do?queryId="+queryId;
		document.forms[0].action = action;
		document.forms[0].submit();
	}  
	
	function openDecisionMakingPage()
	{
		action="OpenDecisionMakingPage.do";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
	function proceedClicked()
	{		
		var radioObj = document.forms[0].options;
		var option = "";
		var radioLength = radioObj.length;
		
		for(var i = 0; i < radioLength; i++) 
		{
			if(radioObj[i].checked)
			{
				option =  radioObj[i].value;
			}
		}		
		if(option == "")
		{
			alert("Please select the option to proceed");
		}
		else if(option == 'redefineQuery')
		{
			onRedefineQueryOption();			
		}
		else
		{
			showWaitPage();
			document.forms[0].submit();			
		}
	}	
	
	function onRedefineQueryOption()
	{
		waitCursor();
		document.forms[0].action='SearchCategory.do?currentPage=resultsView';
		document.forms[0].submit();
		hideCursor();
	}
	function checkItDefault()
	{
		document.forms[0].options[1].checked = "true";
	}
	function showMainObjectNotPresentMessage()
	{
		var request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,showValidationMessages,true);	
		request.onreadystatechange = handlerFunction;				
		var url = "QueryMessageAction.do";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);	
	}
	
	function validateQuery(text)
	{	
		if (text != "save")
		{
			showWaitPage();
		}
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,displayValidationMessage,true);	
		request.onreadystatechange = handlerFunction;			
		var actionURL = "buttonClicked=" + text;		
		var url = "ValidateQuery.do";
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	}

	function displayValidationMessage(text)
	{		
		if (text == "save")
		{
			saveClientQueryToServer('save');
		}
		else
		{
			if (text == "search")
			{
				//showValidationMessages("We are searching for your query. Please wait...");
				viewSearchResults();
			}			
			else 
			{
				if(text != "")
				{		
					showValidationMessages(text);
				}		
			}
		}
	}

	var myWindow;
	function showWaitPage()
	{			
		var popupContent = "<table width='400' height='200' border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr><td height='85' align='center' valign='top'><img src='images/loading_bg.jpg' alt='Query Search' width='400' height='85' /></td></tr><tr><td align='center' valign='top'><img src='images/loading.gif' alt='Loading...' width='50' height='50' vspace='5' /></td></tr></table>";
		myWindow=window.open('','','left=400,top=400,width=420,height=200,modal=yes');
		myWindow.document.write(popupContent);
		myWindow.focus();
	}

	function closeWaitPage()
	{
		if ((myWindow != null) && (!myWindow.closed))
		{
			myWindow.close();
		}
	}