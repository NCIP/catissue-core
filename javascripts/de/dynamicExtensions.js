var contextParam = "";
function setContextParameter(contextParameter)
{
	contextParam = contextParameter;
}
function formSelectedAction()
{
}

function tagHandlerFunction(selectedTool)
{
    document.getElementById('userSelectedTool').value=selectedTool;
}

function addSubForm()
{
    document.getElementById('userSelectedTool').value = "AddSubFormControl";
    controlSelectedAction();
}

function showBuildFormJSP()
{
    document.getElementById('operation').value='buildForm';
    var formDefinitionForm = document.getElementById('formDefinitionForm');
    formDefinitionForm.selectedParentForm.value=formDefinitionForm.parentForm.value;
    formDefinitionForm.submit();
}

function saveFormDetails()
{
    document.getElementById('operation').value='saveForm';
    var formDefinitionForm = document.getElementById('formDefinitionForm');
    setWaitCursorforAllObjectHierarchy(formDefinitionForm);
    formDefinitionForm.submit();
}
function saveFormDetailsOnKeyDown(evt)
{
	var evt = evt || window.event;
	if(evt && evt.keyCode == 13)
	{
		saveFormDetails();
	}
}
function controlSelectedAction()
{
    clearControlAttributes();
    var controlsForm = document.getElementById('controlsForm');
    controlsForm.action= contextParam + "/SelectControlAction.do";
    controlsForm.submit();
}

function formCreateAsChanged()
{
}

function showHomePageFromCreateForm()
{
    var formDefinitionForm = document.getElementById('formDefinitionForm');
    formDefinitionForm.action= contextParam +"/RedirectAction.do";
    formDefinitionForm.submit();
}

function showHomePageFromBuildForm()
{
    var controlsForm = document.getElementById('controlsForm');
    controlsForm.action= contextParam + "/RedirectAction.do";
    controlsForm.submit();
}

function showHomePageFromCreateGroup()
{
    var groupForm = document.getElementById('groupForm');
    groupForm.action= contextParam + "/RedirectAction.do";
    groupForm.submit();
}

function addControlToFormTree()
{
    var optionListTable = document.getElementById('optionListTable');
    var optiongridDiv = document.getElementById('optiongrid');
    if(optionListTable!=null && optiongridDiv!=null)
    {
        optionGrid.setCSVDelimiter("\t");
        var csvString = optionGrid.serializeToCSV();
        document.getElementById('csvString').value = csvString;
    }

    document.getElementById('operation').value='controlAdded';
    var controlsForm=document.getElementById("controlsForm");
    if(document.getElementById("selectedAttributeIds")!=null)
    {
        selectAllListAttributes(document.getElementById("selectedAttributeIds"));
    }
    controlsForm.action= contextParam + "/AddControlsAction.do";
    controlsForm.target="_self";
    controlsForm.submit();
}

function addControlToForm() {

    if (window.dialogArguments)
    {
        window.opener = window.dialogArguments;
    }
    if(window.opener!=null)
    {
        window.opener.document.getElementById('operation').value='controlAdded';
        var controlsForm=window.opener.document.getElementById("controlsForm");
        if(controlsForm!=null)
        {
            controlsForm.action= contextParam + "/AddControlsAction.do";
            controlsForm.submit();
        }
    }
    window.close();
}

function cancelControlOpern(addBtnCaption,formTitle)
{
    clearCommonAttributes();
    clearControlAttributes();
    changeOperationMode(addBtnCaption,formTitle);
}

function changeOperationMode(addBtnCaption,formTitle)
{
    if(document.getElementById('controlOperation')!=null)
    {
        document.getElementById('controlOperation').value="Add";
    }
    if(document.getElementById('addControlToFormButton')!=null)
    {
        document.getElementById('addControlToFormButton').value=addBtnCaption;
    }
    if(document.getElementById('formTitle')!=null)
    {
        document.getElementById('formTitle').innerHTML=formTitle;
    }
}

function closeWindow()
{
    window.close();
}

function showNextActionConfirmDialog()
{
    var  url= contextParam + "/pages/de/confirmNextActionDialog.jsp";
	//for bug 5933
	if(navigator.userAgent.indexOf("Firefox")!= -1 )
	{
		var windowProperties = "height=200,width=350,top=300,left=350,chrome,centerscreen,dependent=YES, dialog=YES,modal=YES,resizable=NO,scrollbars=NO, location=0,status=0,menubar=0,toolbar=0";
		window.open(url, window, windowProperties);
	}
	else
    {
        var modalDialogProperties = "dialogHeight: 200px; dialogWidth: 350px; dialogTop: 300px; dialogLeft: 350px;  center: Yes; resizable: 0;  minimize: NO; status: 0; toolbar:0;";
        window.showModalDialog(url,window,modalDialogProperties);
    }
}

function showCreateFormJSP()
{
    if (window.dialogArguments)
    {
        window.opener = window.dialogArguments;
    }
    var operationMode = window.opener.document.getElementById('operationMode');
    if(operationMode!=null)
    {
        operationMode.value = "EditForm";
    }
    var controlsForm=window.opener.document.getElementById("controlsForm");
    if(controlsForm!=null)
    {
        controlsForm.action= contextParam + "/LoadFormDefinitionAction.do";
        controlsForm.submit();
    }
    window.close();
}

//Added by Preeti
function dataFldDataTypeChanged(datatypeControl)
{
	if(datatypeControl!=null)
	{
		var selectedDatatype = datatypeControl.value;
		var divForDataTypeId = selectedDatatype + "DataType";
		var divForDataType = document.getElementById(divForDataTypeId);
		if(datatypeControl.value!="Text")
		{
			var linesTypeElt = document.getElementById('linesTypeSingleLine');
			if(linesTypeElt!=null)
			{
				linesTypeElt.checked= 'true';
				document.getElementById("rowForNumberOfLines").style.display ="none";
			}
		}

		if(divForDataType!=null)
		{
			//alert(document.getElementById('controlOperation').value);
			if(document.getElementById('controlOperation') != null && document.getElementById('controlOperation').value=='Add'){
				clearFields(selectedDatatype);
			}
			if(selectedDatatype == 'Text')
			{
				var textArr=document.getElementsByName("TextDataType");
				for(var i=0;i<textArr.length;i++)
				{
					 textArr[i].style.display="";
				}
				document.getElementById("NumberDataType").style.display="none";
			}else{
				var textArr=document.getElementsByName("TextDataType");
				for(var i=0;i<textArr.length;i++)
				{
					 textArr[i].style.display="none";
				}
			    document.getElementById("NumberDataType").style.display="";
			}
		}
		insertRules(datatypeControl);
	}
}
// Added Method for bug 5371
function clearFields(datatypeControl)
{
	if(datatypeControl=="Text")
	{
		clearTextValues();
	}
	else
	{
		clearNumberValues();
	}
}
function clearTextValues()
{
	if(document.getElementById('attributeNoOfRows').value != "")
	{
		document.getElementById('attributeNoOfRows').value = "";
	}
	if(document.getElementById('attributeSize').value != "")
	{
		document.getElementById('attributeSize').value = "";
	}
	if(document.getElementById('attributeDefaultValue').value != "")
	{
		document.getElementById('attributeDefaultValue').value = "";
	}
	if(document.forms[0].attributeDisplayAsURL.checked == true)
	{
		document.forms[0].attributeDisplayAsURL.checked = false;
	}
	if(document.forms[0].attributeIsPassword.checked == true)
	{
		document.forms[0].attributeIsPassword.checked = false;
	}

}

function clearNumberValues()
{
	if(document.getElementById('attributeDecimalPlaces').value != "")
	{
		document.getElementById('attributeDecimalPlaces').value = "";
	}
	if(document.getElementById('attributeDefaultValue').value != "")
	{
		document.getElementById('attributeDefaultValue').value = "";
	}
	if(document.forms[0].attributeIsPassword.checked == true)
	{
		document.forms[0].attributeIsPassword.checked = false;
	}

}

function insertRules(datatypeControl)
{
    var selectedDatatype = datatypeControl.value;
    var divForDataTypeId = selectedDatatype + "Div";

    var tempInnerHTML = "<table width=\"100%\">";
    var divForCommonRule = document.getElementById("commonsDiv");
    if(divForCommonRule != null)
    {
        tempInnerHTML = tempInnerHTML + "<tr><td width=\"100%\">" + divForCommonRule.innerHTML + "</tr></td>";

        var divForDataType = document.getElementById(divForDataTypeId);
        if(divForDataType!=null)
        {
            tempInnerHTML = tempInnerHTML + "<tr><td width=\"100%\">" + divForDataType.innerHTML + "</tr></td>";
        }

        while (tempInnerHTML.indexOf("tempValidationRules") != -1)
        {
            tempInnerHTML = tempInnerHTML.replace("tempValidationRules","validationRules");
        }
        while (tempInnerHTML.indexOf("minTemp") != -1)
        {
            tempInnerHTML = tempInnerHTML.replace("minTemp","min");
        }
        while (tempInnerHTML.indexOf("maxTemp") != -1)
        {
            tempInnerHTML = tempInnerHTML.replace("maxTemp","max");
        }
        while (tempInnerHTML.indexOf("temp_") != -1)
        {
            tempInnerHTML = tempInnerHTML.replace("temp_","");
        }

        var substitutionDivRules = document.getElementById('substitutionDivRules');
        substitutionDivRules.innerHTML = tempInnerHTML;
    }
    tempInnerHTML = tempInnerHTML + "</table>"
}

function initBuildForm(contextParameter)
{
	contextParam = contextParameter;
    //If single line textbox, dont show row for noOfLines
    if(document.getElementById("linesTypeHidden")!=null)
    {
        textBoxTypeChange(document.getElementById("linesTypeHidden"));
    }
    var dataTypeElt = document.getElementById("initialDataType");
    if(dataTypeElt!=null)
    {
        //Load datatype details for selected datatype
        dataFldDataTypeChanged(dataTypeElt);
    }
    else
    {
        insertRules("");
    }

    var sourceElt=document.getElementById("hiddenDisplayChoice");
    if(sourceElt!=null)
    {
        //Load source details for selected sourcetype
        changeSourceForValues(sourceElt);
    }

    //Initilialize default value for list of options
    initializeOptionsDefaultValue();

    //If other option is selected in measurement units, enable the text box next to it
  //  var cboMeasurementUnits = document.getElementById('attributeMeasurementUnits');
 //   measurementUnitsChanged(cboMeasurementUnits);

    //List box type : Combo-box or List box
    var attributeMultiSelect = document.getElementById('hiddenIsMultiSelect');
    if(attributeMultiSelect!=null)
    {
        listTypeChanged(attributeMultiSelect);
    }

    //Date page initializations
    var dateValueType = document.getElementById('initialDateValueType');
    if(dateValueType!=null)
    {
        changeDateType(dateValueType);
    }

    //List of form names for selected group
    groupChanged(false);

    //List of attributes for selected form
    formChanged(false);

    //Create as option for CreateForm
    createFormAsChanged();
}

function changeChoiceListTableDisplay()
{
    var optionListTable = document.getElementById('optionListTable');
    if(optionListTable!=null)
    {
        var noOfRows = optionListTable.rows.length;
        if(noOfRows > 0)
        {
            document.getElementById('optionsListRow').style.display = "";
        }
        else
        {
            document.getElementById('optionsListRow').style.display = "none";
        }
    }
}

function changeSourceForValues(sourceControl)
{
    if(sourceControl!=null)
    {
        if(canChangeSource(sourceControl)) //If the source of values can be changed
        {
            var sourceForValues = sourceControl.value;
            if(sourceForValues!=null)
            {
                var divForSourceId = sourceForValues + "Values";
                var divForSource = document.getElementById(divForSourceId);
                if(divForSource!=null)
                {
                    var valueSpecnDiv = document.getElementById('optionValuesSpecificationDiv');
                    if(valueSpecnDiv!=null)
                    {
                        var source = divForSource.innerHTML;
                        while (source.indexOf("tempoptiongrid") != -1)
                        {
                            source = source.replace("tempoptiongrid","optiongrid");
                        }
                        while (source.indexOf("tempcsvFile") != -1)
                        {
                            source = source.replace("tempcsvFile","csvFile");
                        }
                        valueSpecnDiv.innerHTML = source;
                        if(sourceControl.value == "UserDefined")
                        {
                            initOptionGrid();
                        }
                    }
                }
            }
        }
    }
}

//Check if source of values can be changed
function canChangeSource(sourceControl)
{
    var operation = null;
    if(document.getElementById("controlOperation")!=null)
    {
        operation = document.getElementById("controlOperation").value;
        if(operation=="Edit")
        {
            if(document.getElementById("hiddenDisplayChoice")!=null)
            {
                var originalDisplayChoice = document.getElementById("hiddenDisplayChoice").value;
                var selectedDisplayChoice = sourceControl.value;
                if(originalDisplayChoice!=selectedDisplayChoice)
                {
                    alert("Cannot change source for values");
                    var originalSelectedDisplayChoice = document.getElementById(originalDisplayChoice);
                    if(originalSelectedDisplayChoice!=null)
                    {
                        originalSelectedDisplayChoice.checked=true;
                    }

                    return false;
                }
            }
        }
    }
    return true;
}

function initializeOptionsDefaultValue()
{
    var valuestable = document.getElementById('optionListTable');
    var defaultValue = document.getElementById('attributeDefaultValue');
    if((defaultValue!=null)&&(valuestable!=null))
    {
        var rowForDefaultValue = document.getElementById(defaultValue.value+"");
        if(rowForDefaultValue!=null)
        {
            rowForDefaultValue.style.fontWeight='bold';
        }
    }
}

function setDefaultValue()
{
    var itemIds = optionGrid.getAllItemIds(",");
    if(itemIds != null)
    {
        var rowIds = new Array();
        rowIds = itemIds.split(',');
        var item=0;
        for(item=0; item<rowIds.length; item++)
        {
            optionGrid.setRowTextNormal(rowIds[item]);
        }
    }
    var selectedRows = optionGrid.getCheckedRows(0);
    var selectedRowIndices = selectedRows.split(',');
    optionGrid.setRowTextBold(selectedRowIndices[0]);
    document.getElementById('attributeDefaultValue').value = optionGrid.cells(selectedRowIndices[0],1).getValue();
}

//Added by sujay
function showFormPreview()
{
    var entitySaved = document.getElementById('entitySaved');
    if(entitySaved!=null)
    {
        entitySaved.value="";
    }
    var controlsForm = document.getElementById('controlsForm');
    controlsForm.action= contextParam + "/ShowPreviewAction.do";
    controlsForm.submit();
}

function addFormAction()
{
    document.getElementById('operationMode').value = 'AddNewForm';
}

function textBoxTypeChange(obj)
{
    if(obj.value == 'SingleLine')
    {
        document.getElementById('rowForNumberOfLines').style.display="none";
    }
    if(obj.value == 'MultiLine')
    {
        document.getElementById('rowForNumberOfLines').style.display="";
    }
}

// Added by Chetan
function backToControlForm()
{
    var dataEntryForm = document.getElementById('dataEntryForm');
    if(dataEntryForm != null)
    {
        setWaitCursorforAllObjectHierarchy(dataEntryForm);
        dataEntryForm.action= contextParam + "/LoadFormControlsAction.do";
        dataEntryForm.submit();
    }
}

function clearCommonAttributes()
{
    if(document.getElementById('caption') != null)
    {
        document.getElementById('caption').value = "";
    }
    if(document.getElementById('attributeConceptCode') != null)
    {
        document.getElementById('attributeConceptCode').value = "";
    }
    if(document.getElementById('description') != null)
    {
        document.getElementById('description').value = "";
    }
}

function clearControlAttributes()
{
    var controlsForm = document.getElementById('controlsForm');

    if(document.getElementById('attributeSize') != null)
    {
        document.getElementById('attributeSize').value = "";
    }
    if(document.getElementById('attributeDefaultValue') != null)
    {
        document.getElementById('attributeDefaultValue').value = "";
    }
    if(document.getElementById('attributeDigits') != null)
    {
        document.getElementById('attributeDigits').value = "";
    }
    if(document.getElementById('attributeDecimalPlaces') != null)
    {
        document.getElementById('attributeDecimalPlaces').value = "";
    }

/*    if(document.getElementById('attributeMeasurementUnits') != null)
    {
        document.getElementById('attributeMeasurementUnits').value = "";
    }*/
    if(document.getElementById('measurementUnitOther') != null)
    {
        document.getElementById('measurementUnitOther').value = "";
    }
    if(document.getElementById('format') != null)
    {
        document.getElementById('format').value = "";
    }
    if(controlsForm.attributeNoOfRows != null)
    {
        controlsForm.attributeNoOfRows.value = "";
    }
    if(controlsForm.attributenoOfCols != null)
    {
        controlsForm.attributenoOfCols.value = "";
    }
    if(document.getElementById('dataType') != null)
    {
        document.getElementById('dataType').value = "";
    }

    if(document.getElementById('attributeIsPassword') != null)
    {
        document.getElementById('attributeIsPassword').value = "";
    }

    clearSelectedAttributesList();
}
function setWaitCursorforAllObjectHierarchy(form)
{
	form.style.cursor = "wait";
}

function resetWaitCursor()
{
	var dv = document.getElementById('waitcursorDiv');
	 if (dv!= null)
	 {
		dv.style.display = "none";
	 }
}
function saveEntity()
{
	var entitySaved = document.getElementById('entitySaved');
	if(entitySaved!=null)
	{
		entitySaved.value='true';
	}
	var controlsForm = document.getElementById('controlsForm');
	if(controlsForm!=null)
	{
	    setWaitCursorforAllObjectHierarchy(controlsForm);
		controlsForm.action= contextParam + "/SaveEntityAction.do";
		controlsForm.submit();
	}
}
function saveEntityOnKeyDown(evt)
{
	var evt = evt || window.event;
	if(evt && evt.keyCode == 13)
	{
		saveEntity();
	}
}

function loadPreviewForm(contextParameter)
{
	contextParam = contextParameter;
    var entitySaved = document.getElementById('entitySaved');
    if(entitySaved!=null)
    {
        if(entitySaved.value=='true')
        {
            var backBtn = document.getElementById('backToPrevious');
            if(backBtn!=null)
            {
                backBtn.disabled='true';
            }
        }
    }
}

function listTypeChanged(obj)
{
    if(obj!=null)
    {
        var rowForDisplayHeight = document.getElementById('rowForDisplayHeight');
        if(rowForDisplayHeight!=null)
        {
            if(obj.value == 'SingleSelect')
            {
                rowForDisplayHeight.style.display="none";
                document.getElementById("userSelectedTool").value = "ComboboxControl";
            }
            if(obj.value == 'MultiSelect')
            {
                rowForDisplayHeight.style.display="";
                document.getElementById("userSelectedTool").value = "ListBoxControl";
                //alert(document.getElementById("userSelectedTool").value);
            }
        }
    }
}

//When Date type is changed :  Disable default value txt box for None and Todays date option
function changeDateType(dateType)
{
    if(dateType!=null)
    {
        dateTypeValue =dateType.value;
    }
    //  var defValueTxtBox = document.getElementById('attributeDefaultValue');
    var rowForDefaultValue = document.getElementById('rowForDateDefaultValue');
    if((dateTypeValue == "None")||(dateTypeValue == "Today"))
    {
        rowForDefaultValue.style.display="none";
    }
    else
    {
        rowForDefaultValue.style.display="";
    }
}

function addDynamicData(recordIdentifier)
{
    var dataEntryForm = document.getElementById('dataEntryForm');

    if(dataEntryForm != null)
    {
        if(recordIdentifier != null || recordIdentifier != "")
        {
            document.getElementById('recordIdentifier').value = recordIdentifier;
            document.getElementById('isEdit').value = "true";
        }
        dataEntryForm.action= contextParam + "/ApplyDataEntryFormAction.do";
        setWaitCursorforAllObjectHierarchy(dataEntryForm);
    }
}

function showFormDefinitionPage()
{
    var previewForm = document.getElementById('previewForm');
    previewForm.action= contextParam + "/LoadFormDefinitionAction.do";
    previewForm.submit();
}

function showTooltip(text,obj,message)
{
    var tooltip = "";
    var w1 = obj.scrollWidth;
    var w2 = obj.offsetWidth;
    var difference = w1-w2;
    if(difference > 0)
    {
        tooltip = text;
        obj.title = tooltip;
    }
    else
    {
        if(message != null)
        {
            tooltip = message;
            obj.title = tooltip;
        }
        else
        {
            if(obj.tagName != "IMG")
            {
                obj.title = "";
            }
        }
    }
}

function hideTooltip()
{
  el = document.getElementById("akvtooltip");
  if( el != null)
  {
     el.style.visibility="hidden";
  }
}

function controlSelected(rowId,colId)
{
    //Added by Preeti
    document.getElementById('controlOperation').value='Edit';
    document.getElementById('selectedControlId').value=rowId;

    //Control type is displayed in 2nd column
    controlType = mygrid.cells(mygrid.getSelectedId(),2).getValue();

    if(controlType=="Sub Form") //"Sub form"  tightly coupled with ProcessorConstants.ADD_SUBFORM_TYPE
    {
        var opernMode = document.getElementById('operationMode');
        if(opernMode!=null)
        {
            document.getElementById('operationMode').value = "EditSubForm";
        }
    }
    var controlsForm=document.getElementById('controlsForm');
    controlsForm.action= contextParam + '/LoadFormControlsAction.do';
    controlsForm.submit();
}

/*function measurementUnitsChanged(cboMeasuremtUnits)
{
    if(cboMeasuremtUnits!=null)
    {
        var txtMeasurementUnitOther = document.getElementById('measurementUnitOther');
        if(txtMeasurementUnitOther!=null)
        {
            if(cboMeasuremtUnits.value =="other")
            {
                txtMeasurementUnitOther.disabled=false;
                txtMeasurementUnitOther.style.display="inline";
                txtMeasurementUnitOther.focus();

            }
            else
            {
                txtMeasurementUnitOther.value="";
                txtMeasurementUnitOther.disabled=true;
                txtMeasurementUnitOther.style.display="none";
            }
        }
    }
}*/

function ruleSelected(ruleObject)
{
    if(ruleObject.value == 'range')
    {
        if(ruleObject.checked ==false)
        {
            document.getElementById('min').value='';
            document.getElementById('max').value='';
        }
    }
}

function deleteControl()
{
    deleteControlFromUI();
    updateControlsSequence();   //ajax code to delete control from form
}

function deleteControlFromUI()
{
    var selectedRows = mygrid.getCheckedRows(0);
    var selectedRowIndices = selectedRows.split(',');
    for(i=0;i<selectedRowIndices.length;i++)
    {
        mygrid.deleteRow(selectedRowIndices[i]);
    }
}


//ajax function to delete controls from the form
function updateControlsSequence()
{
    var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,ignoreResponseHandler,false);

    //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
    request.onreadystatechange = handlerFunction;
    //send data to ActionServlet
    var gridItemIds = mygrid.getAllItemIds(",");

    if(gridItemIds!=null)
    {
        var controlSeqNos = document.getElementById('controlsSequenceNumbers');
        if(controlSeqNos!=null)
        {
            controlSeqNos.value = gridItemIds;
        }
        //Open connection to servlet
        request.open("POST","AjaxcodeHandlerAction.do",true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send("&ajaxOperation=updateControlsSequence&gridControlIds="+gridItemIds);
    }
}

function resetStartPointArray(startPointArray,value)
{
    for(counter = 0 ; counter  < startPointArray.length;counter++)
    {
        if(startPointArray[counter] > value )
        {
            startPointArray[counter] = startPointArray[counter] -1 ;
        }
    }
}


function resetRowNum(checkAttribute)
{
    for(i = 0; i < checkAttribute.length-1; i++)
    {
        (document.getElementById(checkAttribute[i].value + "rowNum")).value = i + 1 ;
    }
}

function deleteRow(tableId, startPoint)
{
    var tab = document.getElementById(tableId);
    tab.deleteRow(startPoint);
}

function decreaseSequencenumber()
{
    checkAttribute = document.controlsForm.checkAttribute;
    for(i = 0; i < checkAttribute.length-1; i++)
    {
        if(checkAttribute[i].checked)
        {
            var startPoint = (document.getElementById(checkAttribute[i].value + "rowNum")).value;
            moveRowsUp('controlList',startPoint,1);
        }
    }
    resetRowNum(checkAttribute);
}

function moveRowsUp (tableId, startPoint, counter)
{
    var tab = document.getElementById(tableId);
    for (var i = 0 ; i < counter; i++)
    {
        if (startPoint == 1)
        {
            break;
        }
        tab.moveRow(startPoint,startPoint-1);
        startPoint +=1;
    }
}


function increaseSequencenumber()
{
    checkAttribute = document.controlsForm.checkAttribute;

    for(i = 0; i < checkAttribute.length-1; i++)
    {
        if(checkAttribute[i].checked)
        {
            var startPoint = (document.getElementById(checkAttribute[i].value + "rowNum")).value;
            if(startPoint != checkAttribute.length-1)
            {
                moveRowsDown('controlList',startPoint,1);
            }
        }
    }
    resetRowNum(checkAttribute);
}

function moveRowsDown(tableId, startPoint, counter)
{
    var tab = document.getElementById(tableId);
    for (var i = 0 ; i < counter; i++)
    {
        tab.moveRow(startPoint,parseInt(startPoint)+1);
        startPoint -=1;
    }
}

function showDefineGroupPage(formName)
{
    var form = document.getElementById(formName+'');
    if(form!=null)
    {
        form.action= contextParam +"/LoadGroupDefinitionAction.do";
        form.submit();
    }
}

function changeGroupSource(groupSrc)
{
    if(groupSrc!=null)
    {
        var divForGrpDetails = document.getElementById('groupDetailsDiv');

        var groupSourceName = groupSrc.value+"Div";

        var divForGrpSrc = document.getElementById(groupSourceName);

        var divForGrpSrcInnerHTML = divForGrpSrc.innerHTML;
        divForGrpSrc.innerHTML = '';

        var groupForm = document.getElementById('groupForm');
        groupForm.removeChild(divForGrpSrc);

        if((divForGrpSrc!=null)&&(divForGrpDetails!=null))
        {
            var source = divForGrpSrcInnerHTML;
            while (source.indexOf("temp") != -1)
            {
                source = source.replace("temp","");
            }
            divForGrpDetails.innerHTML = source;

        }
        var groupNameTextFld = document.getElementById('groupNameText');
        if(groupNameTextFld!=null)
        {
            groupNameTextFld.value="";
        }

        if(groupSourceName=="ExistingGroupDiv")
        {
            var selectedGroupName = document.getElementById('groupName');
            groupSelected(selectedGroupName);
        }
        groupForm.appendChild(divForGrpSrc);
        divForGrpSrc.innerHTML = divForGrpSrcInnerHTML;
    }
}

function initDefineGroupForm(contextParameter)
{
	contextParam = contextParameter;
    changeGroupSource(document.getElementById('createGroupAsHidden'));
}

function showDefineFormJSP()
{
    var groupForm = document.getElementById('groupForm');
    var groupOperation = document.getElementById('groupOperation');
    if(groupOperation!=null)
    {
        groupOperation.value = "showNextPage";
    }
    if(groupForm!=null)
    {
        groupForm.action =  contextParam +"/ApplyGroupDefinitionAction.do";
        groupForm.submit();
    }
}

function setDataEntryOperation(target)
{
    var formsIndexForm = document.getElementById('formsIndexForm');
    formsIndexForm.action = target;
    formsIndexForm.submit();
}

function setEditOperationMode(target)
{
    document.getElementById('operationMode').value='EditForm';
    var formsIndexForm = document.getElementById('formsIndexForm');
    formsIndexForm.action = target;
    formsIndexForm.submit();
}

function setRecordListTarget(target)
{
    var formsIndexForm = document.getElementById('recordListForm');
    formsIndexForm.action = target;
    formsIndexForm.submit();
}

function loadRecordList(target)
{
    document.getElementById('operationMode').value = 'EditForm';
    var formsIndexForm = document.getElementById('formsIndexForm');
    formsIndexForm.action = target;
    formsIndexForm.submit();
}

function saveGroup()
{
	var groupForm = document.getElementById('groupForm');
	var groupOperation = document.getElementById('groupOperation');
	if(groupOperation!=null)
	{
		groupOperation.value = "savegroup";
	}
	if(groupForm!=null)
	{
		setWaitCursorforAllObjectHierarchy(groupForm);
		groupForm.action = contextParam + "/ApplyGroupDefinitionAction.do";
		groupForm.submit();
	}
}
function saveGroupOnKeyDown(evt)
{
	var evt = evt || window.event;
	if(evt && evt.keyCode == 13)
	{
		saveGroup();
	}
}

function toggle(fldForSelectedObject,id,p)
{
    prevSelectedId =document.getElementById(fldForSelectedObject).value;
    if(prevSelectedId!='')
    {
        document.getElementById(prevSelectedId).style.fontWeight='normal';
    }
    document.getElementById(fldForSelectedObject).value='';
    var myChild = document.getElementById(id);
    if((myChild!=null)&&(myChild!=undefined))
    {
        if(myChild.style.display!='block')
        {
            myChild.style.display='block';
            document.getElementById(p).className='folderOpen';
        }
        else
        {
            myChild.style.display='none';
            document.getElementById(p).className='folder';
        }
    }

    formName = getFormNameFromParent(p);
    setSelectedObjectName(fldForSelectedObject,formName);
}

function setSelectedObjectName(fldForSelectedObject,name)
{
    var selectedObjectName = document.getElementById(fldForSelectedObject+'Name');
    if(selectedObjectName!=null)
    {
        selectedObjectName.value = name;
    }
}

function changeSelection(fldForSelectedObject,str1,seqno)
{
    prevSelectedId =document.getElementById(fldForSelectedObject).value;
    document.getElementById(fldForSelectedObject).value=str1;
    document.getElementById(str1).style.fontWeight='bold';
    if(prevSelectedId!='')
    {
        document.getElementById(prevSelectedId).style.fontWeight='normal';
    }

    var formName = document.getElementById(str1);

    if(formName!=null)
    {
        setSelectedObjectName(fldForSelectedObject,formName.innerText);
    }
    else
    {
        setSelectedObjectName(fldForSelectedObject,"");
    }
}


function  getFormNameFromParent(p)
{
    var parent = document.getElementById(p);
    if(parent!=null)
    {
        var elts = parent.getElementsByTagName("a");
        if(elts!=null)
        {
            var noOfElts = elts.length;
            if((noOfElts!=null)&&(noOfElts!=undefined)&&(noOfElts>0))
            {
                var parentName = elts[0].innerText;
                return parentName;
            }
        }
    }
}

function getDocumentElementForXML(xmlString)
{
    // code for IE
    if (window.ActiveXObject)
    {
        var doc=new ActiveXObject("Microsoft.XMLDOM");
        doc.async="false";
        doc.loadXML(xmlString);
    }
    // code for Mozilla, Firefox, Opera, etc.
    else
    {
        var parser=new DOMParser();
        var doc=parser.parseFromString(xmlString,"text/xml");
    }

    return doc;
}

/***  code using ajax :gets the list of form names for selected group without refreshing the whole page  ***/
function groupChanged(flagClearAttributeList)
{
    if(flagClearAttributeList)
    {
        clearSelectedAttributesList();
    }
    var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,groupChangedResponse,false);

    /* no brackets after the function name and no parameters are passed because we are
       assigning a reference to the function and not actually calling it */
    request.onreadystatechange = handlerFunction;
    //send data to ActionServlet
    if(document.getElementById('groupName')!=null)
    {
        var grpName  = document.getElementById('groupName').value;

        request.open("POST","AjaxcodeHandlerAction.do",true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send("&ajaxOperation=changeGroup&grpName="+grpName);
    }
}

function groupChangedResponse(formNameListXML)
{
    if(formNameListXML!=null)
    {
        var htmlFormNameList = document.getElementById("formName");
        if(htmlFormNameList!=null)
        {
            htmlFormNameList.options.length = 0;
            var documentElt  = getDocumentElementForXML(formNameListXML);
            var formnames  =  documentElt.getElementsByTagName('forms');
            if(formnames !=null)
            {
                var optionName =null;
                var optionValue = null;
                for (i=0;i<formnames.length;i++)
                {
                    var formnamenode = formnames[i];
                    optionValue = "";
                    optionName = "";
                    for (var j=0; j<formnamenode.childNodes.length; j++)
                    {
                        if(formnamenode.childNodes[j].nodeName=="form-id")
                        {
                            optionValue = formnamenode.childNodes[j].firstChild.nodeValue;
                         }
                        if(formnamenode.childNodes[j].nodeName=="form-name")
                        {
                            optionName = formnamenode.childNodes[j].firstChild.nodeValue;
                        }
                    }
                    if((optionName != null)&&(optionValue != null))
                    {
                          var oOption = document.createElement("OPTION");
                           htmlFormNameList.options.add(oOption,htmlFormNameList.options.length+1);
                 //       htmlFormNameList.options[htmlFormNameList.options.length] = new Option (optionName,optionValue);

                       if(window.ActiveXObject)
                        {
                            oOption.text=optionName;
                        }
                        else
                        {
                            oOption.textContent=optionName;
                        }
                        oOption.value = optionValue;
                    }
                }
            }
        }
        formChanged();
    }
}

//When form changed load attributes for form
function formChanged(flagClearAttributeList)
{
    if(flagClearAttributeList)
    {
        clearSelectedAttributesList();
    }
    var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,formChangedResponse,false);
    //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
    request.onreadystatechange = handlerFunction;
    if(document.getElementById('formName')!=null)
    {
        //send data to ActionServlet
        var frmName  = document.getElementById('formName').value;
        //Open connection to servlet
        /*
        request.open("POST","LoadFormControlsAction.do",true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send("&operation=changeForm&frmName="+frmName);
        */

        request.open("POST","AjaxcodeHandlerAction.do",true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send("&ajaxOperation=changeForm&frmName="+frmName);
    }
}

function formChangedResponse(formAttributesListXML)
{
    if(formAttributesListXML!=null)
    {
        var htmlFormAttributeList = document.getElementById("formAttributeList");
        if(htmlFormAttributeList!=null)
        {
            htmlFormAttributeList.options.length = 0;
            //var formAttributes  =  formAttributesListXML.getElementsByTagName('formAttributes');
            var documentElt  = getDocumentElementForXML(formAttributesListXML);
            var formAttributes  =  documentElt.getElementsByTagName('formAttributes');
            if(formAttributes !=null)
            {
                var optionName =null;
                var optionValue = null;
                for (i=0;i<formAttributes.length;i++)
                {
                    var formAttribute = formAttributes[i];
                    optionValue = "";
                    optionName = "";
                    for (var j=0; j<formAttribute.childNodes.length; j++)
                    {
                        if(formAttribute.childNodes[j].nodeName=="form-attribute-id")
                        {
                            optionValue = formAttribute.childNodes[j].firstChild.nodeValue;
                        }
                        if(formAttribute.childNodes[j].nodeName=="form-attribute-name")
                        {
                            optionName = formAttribute.childNodes[j].firstChild.nodeValue;
                        }
                    }
                    if((optionName!=null)&&(optionValue!=null))
                    {
                        var oOption = document.createElement("OPTION");
                        htmlFormAttributeList.options.add(oOption,htmlFormAttributeList.options.length+1);
                     //  htmlFormAttributeList.options[htmlFormAttributeList.options.length] = new Option (optionName,optionValue);
                        if(window.ActiveXObject)
                        {
                            oOption.text = optionName;
                        }
                        else
                        {
                            oOption.textContent = optionName;
                        }
                        oOption.value = optionValue;
                    }
                }
            }
        }
    }
}

/*** code using ajax  ***/
function clearSelectedAttributesList()
{
    var selectedAttributeList = document.getElementById('selectedAttributeIds');
    if(selectedAttributeList !=null)
    {
        var noOfElements = selectedAttributeList.options.length;
        for (i=noOfElements-1;i>=0;i--)
        {
            selectedAttributeList.options[i] = null;
        }

    }
}
function selectFormAttribute()
{
    var fromListBox = document.getElementById('formAttributeList');
    var toListBox = document.getElementById('selectedAttributeIds');
    transferElementsFromList(fromListBox,toListBox);
}

function unSelectFormAttribute()
{
    var fromListBox = document.getElementById('selectedAttributeIds');
    if(fromListBox!=null)
    {
        var noOfElements = fromListBox.options.length;
        for (i=noOfElements-1;i>=0;i--)
        {
            var current = fromListBox.options[i];
            if((current!=null)&&(current.selected))
            {
                fromListBox.options[i] = null;
            }
        }
    }
}

function transferElementsFromList(fromListBox,toListBox)
{
    if((fromListBox!=null)&&(toListBox!=null))
    {
        for (i=0;i<fromListBox.options.length;i++)
        {
            var current = fromListBox.options[i];
            if (current.selected)
            {
                if(!isDuplicateOption(current.value,toListBox))
                {
                    var newOption = new Option(current.value);
                    toListBox.options[toListBox.length] = newOption;
                    toListBox.options[toListBox.length - 1].value = current.value;
                    toListBox.options[toListBox.length - 1].innerHTML = current.innerHTML;
                }
            }
        }
    }
}

function isDuplicateOption(optionValue,optionsList)
{
    if((optionsList!=null)&&(optionValue!=null))
    {
        for(var i=0;i<optionsList.length;i++)
        {
            if(optionsList.options[i].value == optionValue)
            {
                return true;
            }
        }
    }
    return false;
}

function selectAllListAttributes(list)
{
    if(list!=null)
    {
        for(var i=0;i<list.length;i++)
        {
            list.options[i].selected=true;
        }
    }
}

//Create form as New/Existing option changed
function createFormAsChanged()
{
    var existingFormDiv = document.getElementById('rowForExistingFormDetails');
    if(existingFormDiv!=null)
    {
        var createAsExistingElement = document.getElementById('createAsExisting');
        if((createAsExistingElement!=null)&&(createAsExistingElement.checked==true))
        {
            existingFormDiv.style.display="";
        }
        else
        {
            existingFormDiv.style.display="none";
        }
    }
}

//added by vishvesh
function addRow(containerId)
{
    var divName = "";
    divName = divName + containerId + "_substitutionDiv";
    var div = document.getElementById(divName);

    var tab = div.childNodes[0];
    tableId = containerId + "_table";
    var table = document.getElementById (tableId);
    var rows = table.rows;
    var rowTobeCopied = tab.rows[0];
    var counter = table.rows.length;

    var newRow = table.insertRow(-1);
    if(counter%2==0)
  	{
  		newRow.className="td_color_f0f2f6";
  	}
	else
	{
		newRow.className="formField_withoutBorder";
	}
    var cells = rowTobeCopied.cells;
    for(i = 0 ; i < cells.length ; i++)
    {
        var newCell = newRow.insertCell(i);
        newCell.className = cells[i].className;
        newCell.style.whiteSpace = "nowrap";
        newCell.innerHTML = cells[i].innerHTML;
        newCell = setDefaultValues(tableId, newCell, containerId);
    }

    var hiddenVar = "";
    hiddenVar += containerId + "_rowCount";

    var currentRowCounter = document.getElementById(hiddenVar);

    currentRowCounter1 = currentRowCounter.value;
    document.getElementById(hiddenVar).value = parseInt(currentRowCounter1) + 1;

	var x = document.getElementsByTagName("script");
	var RegularExpression  =  new RegExp("\^print");
	var RegularExpressionForCombo  =  new RegExp("Ext.form.ComboBox");

    for(var i=0;i<x.length;i++)
    {
	  if(x[i].text!='' && x[i].text.search(RegularExpression) == -1 && x[i].text.search(RegularExpressionForCombo) == -1)
	  {
       		eval(x[i].text);
	  }
    }
    document.getElementById('isDirty').value = true;
}

function removeCheckedRow(containerId)
{
    var table = document.getElementById(containerId + "_table");
    var children = table.rows;
    var deletedRowIds = "";

    if(children.length > 0)
    {
        var rowsDeleted = 0;

        var hiddenVar = "";
        hiddenVar += containerId + "_rowCount";

        for (var rowIndex = 0; rowIndex < children.length; rowIndex++)
        {
            var inputArray = table.rows[rowIndex].getElementsByTagName('input');
            var len = inputArray.length;
            for(var inputIndex = 0; inputIndex < len; inputIndex++)
            {
                if ((inputArray[inputIndex] != null) && (inputArray[inputIndex].name == "deleteRow") && (inputArray[inputIndex].checked))
                {
                    deletedRowIds = deletedRowIds + rowIndex + ",";
                    table.deleteRow(rowIndex);

                    rowsDeleted += 1;
                    children = table.rows;
                    rowIndex = 0;
                    break;
                }
            }
        }

        for (var rowIndex = 0; rowIndex < children.length; rowIndex++)
        {
            var childObject = children[rowIndex];
            var cells = childObject.cells;
            for (cellIndex = 0; cellIndex < cells.length; cellIndex++)
            {
                var cell = cells[cellIndex ];
                var childNodes = cell.childNodes;
                for (childNodeIndex = 0; childNodeIndex < childNodes.length; childNodeIndex++)
                {
                    var childNode= childNodes[childNodeIndex];

                    var childObjectName = childNode.name;

					if (childObjectName != null && "deleteRow" == childObjectName)
                    {
						childNode.id = "checkBox_" + containerId + "_" + rowIndex;
					}

                    if (childObjectName != null && childObjectName.indexOf('_') != -1)
                    {
            			if (childObjectName.indexOf('_div') != -1)
            			{
            				if (childObject.hasChildNodes)
            				{
            					childObject = childObject.childNodes[0];
            					childObjectName = childObject.name;
            				}
            			}
    					if (childObjectName != null
    							&& childObjectName.indexOf('_') != -1)
    					{
	                        var arr = childObjectName.split('_');

	                        arr[arr.length - 1] = rowIndex;
	                        var str = "";
	                        for (arrIndex = 0; arrIndex < arr.length; arrIndex++)
	                        {
	                            str += arr[arrIndex];
	                            if (arrIndex != arr.length - 1)
	                            {
	                                str += "_";
	                            }
	                        }
	                        if (childObjectName.indexOf(')') != -1)
	                        {
	                            str = str + ")";
	                        }

							if (document.getElementById(childObjectName) == null)
							{
								var controlValue = childNode.value;
							}
							else
							{
								var controlValue = document.getElementById(childObjectName).value;
							}

							cell.innerHTML = replaceAll(cell.innerHTML,childObjectName,str);
							if (document.getElementById(childObjectName) == null)
							{
								str = controlValue;
							}
							else
							{
								document.getElementById(str).value  = controlValue;
							}

							break;
    					}
                    }
                }
            }
        }

        var currentRowCounter = document.getElementById(hiddenVar);
        var numberOfRows = currentRowCounter.value;
        document.getElementById(hiddenVar).value = parseInt(numberOfRows) - rowsDeleted;

        document.getElementById(containerId + "_table").value = table;
		document.getElementById('isDirty').value = true;
    }

    var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,ignoreResponseHandler,false);

    //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
    request.onreadystatechange = handlerFunction;
    //send data to ActionServlet
    //Open connection to servlet
    request.open("POST","AjaxcodeHandlerAction.do",true);
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    request.send("&ajaxOperation=deleteRowsForContainment&containerId=" + containerId+"&deletedRowIds="+deletedRowIds);
}

function ignoreResponseHandler(str)
{
}

// Re-factored the entire method as it was very difficult to understand
// Changes by Gaurav Mehta
//Modified By Suhas Khot,    Date:-13 May 2010
function setDefaultValues(tableId, obj, containerId)
{
	var children = obj.childNodes;
	var rowIndex = document.getElementById(tableId).rows.length;
	rowIndex = parseInt(rowIndex) - 1;

	var i = 0;
	for (j = 0; j < children.length; j++)
	{
		var childObject = children[j];
		childObjectName = childObject.name;

		//For calender and other controls
		if (childObjectName == null && childObject.id != null
				&& childObject.id != "auto_complete_dropdown"
					&& childObject.id.indexOf('slcalcodControl') == -1)
		{
			childObjectName = childObject.id;
		}

		if(childObjectName != null && childObjectName.indexOf('_') != -1)
		{
			initializeDefaultValue(childObjectName,childObject,obj,i,rowIndex,true);
			break;
		}
		//For Combobox
		if ("auto_complete_dropdown" == childObject.id)
		{
			var childNodes2 = childObject.childNodes;
			for(i=0;i<childNodes2.length;i++)
			{
//				alert(childNodes2[i].id);
				if(childNodes2[i].id == 'comboHtml')
				{
					var oldName = childNodes2[i].childNodes[0].childNodes[0].name;
					var newName = oldName + "_" + rowIndex;
					var newScript = replaceAll(childNodes2[i-1].innerHTML,
							oldName, newName);
					obj.innerHTML = replaceAll(childNodes2[i].innerHTML,
										oldName, newName);
					eval(newScript);
					break;
				}
			}
		}
		//Only in case of delete checkbox in addrow
		if("deleteRow" == childObjectName)
		{
			childObject.id = "checkBox_" + containerId + "_" + rowIndex;
		}
	}
	return obj;
}

function initializeDefaultValue(childObjectName,childObject,obj,i,rowIndex, isFromMain)
{
	if (childObjectName.indexOf(')') != -1)
	{
		childObjectName = childObjectName.substring(0, childObjectName
				.indexOf(')'));
		i++;
		if (i == 1)
		{
			str = childObjectName + "_" + rowIndex;
		}
		str = str + ")";
	}
	else if (isFromMain == true && (childObjectName.indexOf('_div') != -1  || childObjectName.indexOf('_button') != -1))
	{
		if (childObject.hasChildNodes)
		{
			childObject = childObject.childNodes[0];
			childObjectName = childObject.name;
			if (childObjectName == null && childObject.id != null
				&& childObject.id != "auto_complete_dropdown"
					&& childObject.id.indexOf('slcalcodControl') == -1)
			{
				childObjectName = childObject.id;
			}
			if (childObjectName != null
							&& childObjectName.indexOf('_') != -1)
			{
				initializeDefaultValue(childObjectName,childObject,obj,i,rowIndex, false)
			}
			checkForAutoComplete(childObject,obj,rowIndex);
			//continue;
		}
	}
	else
	{
		i++;
		if (i == 1) {
			str = childObjectName + "_" + rowIndex;
		}
	}
	if(isFromMain == true)
	{
		obj.innerHTML = replaceAll(obj.innerHTML,childObjectName, str);
	}
}

function checkForAutoComplete(childObject,obj,rowIndex)
{
	if ("auto_complete_dropdown" == childObject.id)
	{
		var childNodes2 = childObject.childNodes;

		var oldName = childNodes2[2].childNodes[0].childNodes[0].name;
		var newName = oldName + "_" + rowIndex;
		var newScript = replaceAll(childNodes2[1].innerHTML,
				oldName, newName);

		var div = document.createElement("DIV");
		div.id = oldName + "_Outer_div";
		div.name= oldName + "_Outer_div";

		var divObject = document.createElement("DIV");
		divObject.id = oldName + "_div";
		divObject.name= oldName + "_div";
		divObject.appendChild(childNodes2[2].childNodes[0]);
		div.appendChild(divObject);

		var inputSkipLogicControl = document.createElement("INPUT");
		inputSkipLogicControl.type = "hidden";
		inputSkipLogicControl.id = "skipLogicControl";
		inputSkipLogicControl.name= "skipLogicControl";
		inputSkipLogicControl.value= divObject.name;
		divObject.appendChild(inputSkipLogicControl);

		var inputComboScript = document.createElement("INPUT");
		inputComboScript.type = "hidden";
		inputComboScript.id = "skipLogicControlScript";
		inputComboScript.name= "skipLogicControlScript";
		inputComboScript.value= "comboScript_" + oldName;
		divObject.appendChild(inputComboScript);

		obj.innerHTML = replaceAll(div.innerHTML,
				oldName, newName);
		eval(newScript);
	}
}


function replaceAll(inputString, regExpr, newString)
{
    var outputStr = "";
    var pivot;
    while(inputString.indexOf(regExpr) != - 1)
    {
        inputString = inputString.replace(regExpr,newString);
        pivot = inputString.indexOf(newString) + newString.length;
        outputStr = outputStr + inputString.substring(0, pivot) ;
        inputString = inputString.substring(pivot,inputString.length);
    }
    outputStr = outputStr + inputString;
    return outputStr;
}

//Ajax code for form name selection from tree
function treeNodeSelected(fldName)
{
    var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,treeNodeSelectedResponse,false);

    //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
    request.onreadystatechange = handlerFunction;

    //Open connection to servlet
    request.open("POST","AjaxcodeHandlerAction.do",true);
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    //var selectedFormName  = document.getElementById(fldName).value;
    request.send("&ajaxOperation=selectFormNameFromTree&selectedFormName="+fldName);
}

//Previously defined entity selected
function definedEntitySelected(fldname)
{
    if(fldname.indexOf("Group_")==-1)   //Selection does not contain string "Group_" implies its not a group but a form
    {
        if(document.getElementById('selectedObjectId')!=null)
        {
            document.getElementById('selectedObjectId').value = fldname;
        }
        var request = newXMLHTTPReq();
        var handlerFunction = getReadyStateHandler(request,treeNodeSelectedResponse,false);

        //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
        request.onreadystatechange = handlerFunction;

        //Open connection to servlet
        request.open("POST","AjaxcodeHandlerAction.do",true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send("&ajaxOperation=selectFormNameFromAssociationTree&selectedFormId="+fldname);
    }
    else
    {
        if(document.getElementById('selectedObjectId')!=null)
        {
            document.getElementById('selectedObjectId').value = "";
        }
        //clear all other form elts
        if(document.getElementById("formName")!=null)
        {
            document.getElementById("formName").value = "";
        }

        if(document.getElementById("conceptCode")!=null)
        {
            document.getElementById("conceptCode").value = "";
        }

        if(document.getElementById("formDescription")!=null)
        {
            document.getElementById("formDescription").value = "";
        }
    }
}

function treeNodeSelectedResponse(formNameListXML)
{
    if(formNameListXML!=null)
    {
        var htmlFormName = document.getElementById("formName");
        var htmlFormCC = document.getElementById("conceptCode");
        var htmlFormDesc = document.getElementById("formDescription");
        var htmlOperationMode = document.getElementById('operationMode');
        var htmlOperation = document.getElementById('operation');
        var isAbstract = document.getElementById('isAbstract');
        var htmlParentEntity = document.getElementById('parentForm');

        var documentElt = getDocumentElementForXML(formNameListXML);
        var formname = documentElt.getElementsByTagName('form-name');
        var formDesc = documentElt.getElementsByTagName('form-description');
        var formConceptCode = documentElt.getElementsByTagName('form-conceptcode');
        var operationmode = documentElt.getElementsByTagName('operationMode');
        var checkAbstract = documentElt.getElementsByTagName('isAbstract');
        var parentEntityName = documentElt.getElementsByTagName('parentEntity');

        if((htmlFormName!=null)&&(formname!=null))
        {
            if(formname[0]!=null)
            {
                htmlFormName.value = getElementText(formname[0]);
            }
        }
        if((htmlFormCC!=null)&&(formConceptCode!=null))
        {
            if(formConceptCode[0]!=null)
            {
                htmlFormCC.value = getElementText(formConceptCode[0]);
            }
        }
        if((htmlFormDesc!=null)&&(formDesc!=null))
        {
            if(formDesc[0]!=null)
            {
                htmlFormDesc.value = getElementText(formDesc[0]);
            }
        }
        if((htmlOperationMode!=null)&&(operationmode!=null))
        {
            if(operationmode[0]!=null)
            {
                htmlOperationMode.value = getElementText(operationmode[0]);
            }
        }
        if((htmlOperation!=null)&&(operationmode!=null))
        {
            if(operationmode[0]!=null)
            {
                htmlOperation.value =  getElementText(operationmode[0]);
            }
        }
        if((isAbstract!=null)&&(checkAbstract!=null))
		{
			if(checkAbstract[0]!=null)
			{
				var checked = false;
				if(getElementText(checkAbstract[0]) == "true")
				{
					isAbstract.checked = true;
				}
				else
				{
					isAbstract.checked =  false;
				}
			}
		}

		if((htmlParentEntity!=null)&&(parentEntityName!=null))
		{
			htmlParentEntity.value= getElementText(parentEntityName[0]);
		}

	}
}

function getElementText(element)
{
	var elementText = "";
	if(window.ActiveXObject)
	{
		elementText = element.text;
	}
	else
	{
		if(element.firstChild != null){
			elementText = element.firstChild.nodeValue;
		}else{
			elementText = "";
		}
	}
	return elementText;
}


function insertDataForContainer(containerId)
{

    alert("page to insert date for contianerId" + containerId);
}

function groupSelected(groupList)
{
    if(groupList!=null)
    {
        var groupName = groupList.value;
        if((groupName!=null)&&(groupName!=undefined))
        {
            var request = newXMLHTTPReq();
            var handlerFunction = getReadyStateHandler(request,groupSelectedResponse,false);

            //no brackets after the function name and no parameters are passed because we are
            //assigning a reference to the function and not actually calling it
            request.onreadystatechange = handlerFunction;
            request.open("POST","AjaxcodeHandlerAction.do",true);
            request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            request.send("&ajaxOperation=selectGroup&selectedGroupName="+groupName);
        }
    }
}

function evaluateFormulaForAttribute(controlName)
{
    if(controlName != null)
    {
        var request = newXMLHTTPReq();
		var handlerFunction = function()
		{
	         if(request.readyState  == 4)
	         {
	              if(request.status  == 200)
	              {
	            	  setCalculatedValue(controlName,request.responseText);
	              }
	         }
		};
		// no brackets after the function name and no parameters are passed
		// because we are
		// assigning a reference to the function and not actually calling it
		request.onreadystatechange = handlerFunction;
		request.open("POST", "ApplyDataEntryFormAction.do", true);
		request.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		request.send("&dataEntryOperation=evaluateFormula&selectedControlId="
				+ controlName);
    }
}

function setCalculatedValue(controlName,calculatedValue)
{
	alert("setCalculatedValue");
	document.getElementById(controlName).value = calculatedValue;
}

function groupSelectedResponse(groupXML)
{
    if(groupXML!=null)
    {
        var htmlGroupDescription = document.getElementById("groupDescription");
        var documentElt = getDocumentElementForXML(groupXML);
        var grpDesc = documentElt.getElementsByTagName('group-description');

        if((htmlGroupDescription!=null)&&(grpDesc!=null))
        {
            htmlGroupDescription.value = getElementText(grpDesc[0]);
        }
        else
        {
            htmlGroupDescription.value = "";
        }
    }
}

function showChildContainerInsertDataPage(containerId,ths)
{
    getValues();
    document.getElementById('childContainerId').value = containerId;
    document.getElementById('dataEntryOperation').value  = "insertChildData";
    document.getElementById('childRowId').value = ths.parentNode.parentNode.rowIndex;
    var dataEntryForm = document.getElementById('dataEntryForm');

    var showFormPreview = document.getElementById('showFormPreview').value;
    var mode = document.getElementById('mode').value;
    if(showFormPreview == "true")
    {
        dataEntryForm.action= contextParam + "/ApplyFormPreviewAction.do";
    }
    else if(mode == "view" || mode == "edit")
    {
        dataEntryForm.action= contextParam + "/ApplyDataEntryFormAction.do?scrollTop="+document.getElementById('dataEntryFormDiv').scrollTop;
        document.getElementById('scrollTop').value = document.getElementById('dataEntryFormDiv').scrollTop;
    }
    setWaitCursorforAllObjectHierarchy(dataEntryForm);
    dataEntryForm.submit();
}

function showEditRecordPage(target)
{
    var editRecordsForm = document.getElementById('editRecordsForm');
    editRecordsForm.action=target;
    editRecordsForm.submit();
}

function showParentContainerInsertDataPage()
{
	getValues();
    //document.getElementById('mode').value = "edit";
    document.getElementById('dataEntryOperation').value = "insertParentData";
    var dataEntryForm = document.getElementById('dataEntryForm');
    setWaitCursorforAllObjectHierarchy(dataEntryForm);
}

function getValues()
{
	var frm = document.forms[0];
	for (f = 0; f < document.forms[0].elements.length; ++f )
	{
		var fld = frm.elements[f];

		// select all values in list box before form is submitted.
		if (fld.id.startsWith('protocolCoordId_Control_'))
		{
			var lbOptions = document.getElementById(fld.id);
			for (i = lbOptions.options.length-1; i >= 0; i--)
			{
				lbOptions.options[i].selected=true;
			}
		}
	}
}

function calculateAttributes()
{
    document.getElementById('dataEntryOperation').value = "calculateAttributes";
	var str = $("dataEntryForm").serialize();
	jQuery.ajax(
	{
				type :"POST",
				url :"ApplyDataEntryFormAction.do",
				dataType: "html",
				data :str,
				success : function(htmlresult)
				{
					var iframe = document.getElementById("skipLogicIframe");
					if (iframe != null)
					{
						var iframeDocument = getIframeDocument(iframe);
						if (iframeDocument != null)
						{
							iframeDocument.body.innerHTML = htmlresult;
							var calculatedControlsArray =  iframeDocument.getElementsByName("calculatedControl");
							if (calculatedControlsArray != null)
							{
					            var len = calculatedControlsArray.length;
					            for(var inputIndex = 0; inputIndex < len; inputIndex++)
					            {
					            	var calculatedControl = calculatedControlsArray[inputIndex];
					            	if (calculatedControl != null)
					            	{
					            		var calculatedControlDiv = calculatedControl.value;
					            		var originalDiv = document.getElementById(calculatedControlDiv);
					            		var calculatedDiv = iframeDocument.getElementById(calculatedControlDiv);
					            		if (calculatedDiv!= null && originalDiv != null)
					            		{
					            			originalDiv.innerHTML = calculatedDiv.innerHTML;
					            		}
					            	}
					            }
							}
							//On error: error_div on the dataEntry.jsp is populated with error list
							if(iframeDocument.getElementById("error_div")!= null)
							{
								var errorString = iframeDocument.getElementById("error_div").innerHTML;
								printErrors(errorString)
							}
						}
					}
				}
	});
}

function setInsertDataOperation()
{
	getValues();
    document.getElementById('dataEntryOperation').value = "";
    var dataEntryForm = document.getElementById('dataEntryForm');
    setWaitCursorforAllObjectHierarchy(dataEntryForm);
}

function changeValueForCheckBox(checkbox)
{
    if (checkbox != null)
    {
        if (checkbox.checked == true)
        {
            checkbox.value = true;
        }
        else
        {
            checkbox.value = false;
        }
    }
}

function changeValueForMultiSelectCheckBox(checkbox)
{
    if (checkbox != null)
    {
        if (checkbox.checked == true)
        {
            checkbox.value = checkbox.id;
        }
        else
        {
            checkbox.value = false;
        }
    }
}
function changeValueForAllCheckBoxes(checkbox)
{
	var elements  = document.getElementsByName(checkbox.name);
	if(elements!=null && elements!='undefined')
	{
		var isAllSelected=0;
		for(var i=0;i<elements.length;i++)
		{
			var chckbox = elements[i];
			if (chckbox != null && chckbox.checked == true)
			{
				isAllSelected = isAllSelected+1;
			}
		}
		if(isAllSelected == (elements.length-1))
		{
			selectAllCheckBoxes(false,elements);
		}
		else
		{
			selectAllCheckBoxes(true,elements);
		}

	}
}
function selectAllCheckBoxes(selectAllchkboxes, elements)
{

	for(var i=0;i<elements.length;i++)
	{
		var chkbox = elements[i];
		if (chkbox != null)
		{
			if (selectAllchkboxes == true)
			{
				chkbox.value = chkbox.id;
				chkbox.checked="yes";
			}
			else
			{
				chkbox.value = false;
				chkbox.checked=null;
			}
		}

	}
}
function cancelInsertData()
{
    document.getElementById('dataEntryOperation').value = document.getElementById('operation_mode').value;
    document.getElementById('mode').value = "cancel";
    var dataEntryForm = document.getElementById('dataEntryForm');
    setWaitCursorforAllObjectHierarchy(dataEntryForm);
    dataEntryForm.submit();
}

function setDeleteDataOperation()
{
    document.getElementById('mode').value = "delete";
    var dataEntryForm = document.getElementById('dataEntryForm');
    setWaitCursorforAllObjectHierarchy(dataEntryForm);
    dataEntryForm.submit();
}

function dropFn(srcId,targetId,sourceGridObj,targetGridObj)
{
    updateControlsSequence();
}

//Move controls up in sequence
function moveControlsUp()
{
    var selectedRows = mygrid.getCheckedRows(0);
    if(selectedRows!=null)
    {
        var selectedRowIndices = selectedRows.split(',');
        for(i=0;i<selectedRowIndices.length;i++)
        {
            mygrid.moveRowUp(selectedRowIndices[i]);
        }
    }
    updateControlsSequence();
}

//move controls down in sequence
function moveControlsDown()
{
    var selectedRows = mygrid.getCheckedRows(0);
    if(selectedRows!=null)
    {
        var selectedRowIndices = selectedRows.split(',');
        for(i=selectedRowIndices.length-1;i>=0;i--)
        {
            mygrid.moveRowDown(selectedRowIndices[i]);
        }
    }
    updateControlsSequence();
}

//Added by Preeti : move elements in list
function listEltMoveUp(element)
{
    for(i = 0; i < element.options.length; i++)
    {
        if(element.options[i].selected == true)
        {
            if(i != 0)
            {
                var temp, temp2;
                temp = new Option(getElementText(element.options[i-1]),element.options[i-1].value);
                temp2 = new Option(getElementText(element.options[i]),element.options[i].value);
                element.options[i-1] = temp2;
                element.options[i-1].selected = true;
                element.options[i] = temp;
            }
        }
    }
}

function listEltMoveDown(element)
{
    for(i = (element.options.length - 1); i >= 0; i--)
    {
        if(element.options[i].selected == true)
        {
            if(i != (element.options.length - 1))
            {
                var temp, temp2;
                temp = new Option(getElementText(element.options[i+1]),element.options[i+1].value);
                element.options[i+1] = temp2;
                element.options[i+1].selected = true;
                element.options[i] = temp;
            }
        }
    }
}

function setDateTimeControl(showTime, value)
{
	// Bugzilla Bug 8682 Date format mismatch in forms created from UI and forms are not getting saved.
	// 'shouldUseTime' is variable in 'calendarComponent.js' file. It should be same as showTime when
	// calendar changes from date only calendar to date&time calendar.
	shouldUseTime = showTime;

    showDateTimeControl(showTime, '', 'attributeDefaultValue', value);
    showDateTimeControl(showTime, 'Min', 'min', value);
    showDateTimeControl(showTime, 'Max', 'max', value);
}

function showDateTimeControl(showTime, divType, id, value)
{
    if(showTime=='false')
    {
        if(value=='MonthAndYear')
        {
            document.getElementById('slcalcod'+id).innerHTML = document.getElementById('MonthAndYear'+divType+'Div').innerHTML;
        }
        else if(value=='YearOnly')
        {
            document.getElementById('slcalcod'+id).innerHTML = document.getElementById('YearOnly'+divType+'Div').innerHTML;
        }
        else
        {
            document.getElementById('slcalcod'+id).innerHTML = document.getElementById('DateOnly'+divType+'Div').innerHTML;
        }
    }
    else
    {
        document.getElementById('slcalcod'+id).innerHTML = document.getElementById('dateTime'+divType+'Div').innerHTML;
    }
}

/* Added by Chetan */
function loadOptionGrid()
{
    var csvStr = document.getElementById('csvStr').value;
    optionGrid.loadCSVString(csvStr);
}

function addOptionRow()
{
    var rows = optionGrid.getRowsNum();
    var lastRowId = "";
    if(rows==0)
    {
        lastRowId = "-1";
    }
    else
    {
        lastRowId = optionGrid.getRowId(rows-1);
    }
    var newRowId = parseInt(lastRowId) + 1;
    var newRowIdStr = newRowId + "";
    optionGrid.addRow(newRowIdStr,",,,");
    optionGrid.setSizes();
}

function deleteSelectedOptions()
{
    var selectedRows = optionGrid.getCheckedRows(0);
    var selectedRowIndices = selectedRows.split(',');
    for(i=0;i<selectedRowIndices.length;i++)
    {
        optionGrid.deleteRow(selectedRowIndices[i]);
    }
}

function appendRecordId(ths)
{
    var str = ths.href;
    var recordIdentifier = document.getElementById('recordIdentifier');
    var myRegExp = /dynamicExtensions/;
    var match = str.search(myRegExp);
    if(match != -1)
    {
    	str = str.replace(/dynamicExtensions/, contextParam);
    }
    ths.href = str;
}

function deleteRecord(cotainerId,recordId,mode) {
    var formsIndexForm = document.getElementById('recordListForm');
    formsIndexForm.action = contextParam + "/DeleteRecordAction.do?containerIdentifier=" + cotainerId + "&recordIdentifier=" + recordId + "&mode=" + mode;
    formsIndexForm.submit();
}

// Removes leading whitespaces
function LTrim( value )
{
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
}

// Removes ending whitespaces
function RTrim( value )
{
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
}

// Removes leading and ending whitespaces
function trim( value )
{
    return LTrim(RTrim(value));
}


//for textArea Max length
function textCounter( field,  maxlimit )
{
  //bug id :7778
  //Fixed by : prashant
  //reviewed by : kunal
  var length=field.value.length;
  var n=0;
  var i=0;
  while(i<length)
  {
  	if(field.value.charAt(i)=="\n")
  	{
		n++;
  	}
    i++;
  }
   if ( length-n > maxlimit )
  {
     alert( 'Input value can be '+  maxlimit + ' characters maximum in length.' );
  }
 }

//for resetting the parent timeout counter

function resetTimeoutCounter()
{
	//Set last refresh time
	if(window.parent!=null)
	{
		if(window.parent.lastRefreshTime!=null)
		{
			window.parent.lastRefreshTime = new Date().getTime();
		}
	}
}

//==========================tool tip code started=========
var timeInterval=100;
var interval;
var objID="";

function showStatus(sMsg)
{
	 window.status = sMsg ;
}

function showToolTip(objId)
{
	objID = objId;
	interval = self.setInterval("setTip()",timeInterval);
}

function showGivenTip(objId, toolTipTxt)
{
	objID = objId;
	setGivenTip(toolTipTxt);
}

function hideTip(objId)
{
	var obj = document.getElementById(objId);
	var browser=navigator.appName;
	if(browser=="Microsoft Internet Explorer")
	{
		showStatus(' ');
	}
	else
	{
		obj.title = "";
	}
	interval = window.clearInterval(interval);
}

function setTip()
{
	var obj = document.getElementById(objID);
	if(obj != null)
	{
		if(obj.type == 'text')
		{
			var tip="";
			tip = obj.value;
			obj.title = ""+tip;
		}
		else
		{
			var tip="";
			if(obj.selectedIndex == -1)
				tip="";
			else
				tip = obj.options[obj.selectedIndex].text;

			var browser=navigator.appName;
			if(browser=="Microsoft Internet Explorer")
			{
				showStatus(tip);
			}
			else
			{
				obj.title = tip;
			}
		}
	}
}


function setGivenTip(tooltipValue)
{
	var obj = document.getElementById(objID);
    obj.title = ""+tooltipValue;
	var browser=navigator.appName;
	if(browser=="Microsoft Internet Explorer")
	{
		showStatus(""+tooltipValue);
	}
	else
	{
		obj.title = ""+tooltipValue;
	}
}
//==================tool tip code ends===============================

function clearDate(id, pattern)
{
	var id = document.getElementById(id);
	if((pattern == 'MM-DD-YYYY' && id.value == 'MM-DD-YYYY')
			|| (pattern == 'MM-DD-YYYY HH:MM' && id.value == 'MM-DD-YYYY HH:MM')
			|| (pattern == 'MM-YYYY' && id.value == 'MM-YYYY')
			|| (pattern == 'YYYY' && id.value == 'YYYY'))
	{
		id.value="";
		id.style.color="black";
	}
}
function getIframeDocument(iframe)
{
	var iframeDocument = null;
	if (iframe.contentDocument)
	{
		iframeDocument = iframe.contentDocument;
	}
	else if (iframe.contentWindow)
	{
		iframeDocument = iframe.contentWindow.document;
	}
	else if (iframe.document)
	{
		iframeDocument = iframe.document;
	}
	return iframeDocument;
}
function getSkipLogicControl(controlName, controlId, containerId)
{
    document.getElementById('dataEntryOperation').value = "skipLogicAttributes";
	var str = $("dataEntryForm").serialize();
	var control = document.getElementById(controlName);
	var controlValue = "";
	if (control != null && control.value != null)
	{
		controlValue = control.value;
	}
	str =  str + "&containerId=" + containerId + "&controlId=" + controlId
			+ "&controlValue=" + controlValue + "&controlName=" + controlName;
	jQuery.ajax(
	{
				type :"POST",
				url :"ApplyDataEntryFormAction.do",
				dataType: "html",
				data :str,
				success : function(htmlresult)
				{
					var iframe = document.getElementById("skipLogicIframe");
					if (iframe != null)
					{
						var iframeDocument = getIframeDocument(iframe);
						if (iframeDocument != null)
						{
							iframeDocument.body.innerHTML = htmlresult;
							var skipLogicControlsArray =  iframeDocument.getElementsByName("skipLogicControl");
							if (skipLogicControlsArray != null)
							{
					            var len = skipLogicControlsArray.length;
					            for(var inputIndex = 0; inputIndex < len; inputIndex++)
					            {
					            	var skipLogicControl = skipLogicControlsArray[inputIndex];
					            	if (skipLogicControl != null)
					            	{
					            		var skipLogicControlDiv = skipLogicControl.value;
					            		var originalDiv = document.getElementById(skipLogicControlDiv);
					            		var skipLogicDiv = iframeDocument.getElementById(skipLogicControlDiv);
					            		if (skipLogicDiv!= null && originalDiv != null)
					            		{
					            			originalDiv.innerHTML = skipLogicDiv.innerHTML;
					            			var items = originalDiv.getElementsByTagName('script');
											for(var i=0;i<items.length;i++)
											{
												eval(items[i].innerHTML);
											}
					            		}
					            	}
					            }
					            executeComboScriptsForSkipLogic();
							}
							var skipLogicHideControlsArray =  iframeDocument.getElementsByName("skipLogicHideControls");
							if (skipLogicHideControlsArray != null)
							{
								var len = skipLogicHideControlsArray.length;
					            for(var inputIndex = 0; inputIndex < len; inputIndex++)
					            {
					            	var skipLogicHideControl = skipLogicHideControlsArray[inputIndex];
					            	if (skipLogicHideControl != null)
					            	{
					            		var skipLogicHideControlValue = skipLogicHideControl.value;
					            		var skipLogicHideControlObject = document.getElementById(skipLogicHideControlValue);
					            		var skipLogicHideControlIframeObject = iframeDocument.getElementById(skipLogicHideControlValue);
					            		if (skipLogicHideControlObject != null && skipLogicHideControlIframeObject != null)
					            		{
					            			skipLogicHideControlObject.style.display = skipLogicHideControlIframeObject.style.display;
					            		}
					            	}
					            }
							}
						}
					}
				}
	});
}
function executeComboScriptsForSkipLogic()
{
	var comboScriptDiv= document.getElementsByName("skipLogicControlScript");
	if (comboScriptDiv != null)
	{
		var divCount = comboScriptDiv.length;
		for(var i = 0;i<divCount;i++)
		{
			if (comboScriptDiv[i].value != null)
			{
				var comboScript = document.getElementById(comboScriptDiv[i].value);
				if (comboScript != null)
				{
					eval(comboScript.innerHTML);
				}
			}
		}
	}
}

function insertBreadCrumbForSubForm(containerId,applicationName)
{
	if(applicationName=="clinportal")
	{
		var request = newXMLHTTPReq()
		var handlerFunction = getReadyStateHandler(request,insertBreadCrumbForSubFormResponse,false);
		request.onreadystatechange = handlerFunction;
		request.open("POST","AjaxcodeHandlerAction.do",true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.send("&ajaxOperation=breadCrumbOperation&breadCrumbOperation=prepareBreadCrumbLink&containerId="+containerId);
	}
}

function insertBreadCrumbForSubFormResponse(responseXML)
{
	window.parent.document.getElementById('breadCrumbDiv').innerHTML = responseXML;
	window.parent.document.getElementById ("breadCrumbDiv").style.height="5%";
	window.parent.document.getElementById ("breadCrumbDiv").style.fontSize="12px";
}

function isDataChanged()
{
	if(document.getElementById('isDirty')!=null)
	{
		document.getElementById('isDirty').value = true;
		if(document.getElementById('isShowInDiv').value == 'false')
		{
			document.getElementById('isDirty').value = false;
		}
	}
}

//==================copy paste=======================================

var chkTable,noOfRecordsCopied,cardinality;
var batch = 10;
var slice,start,end;
var startTime= new Date(), endTime;
var rowIndex=0;
var rowCount = 0;
var conatinerId;
var numCombosInPastedData;


function intVariables()
{
chkTable ="";
noOfRecordsCopied = 0;
cardinality="";
batch = 1000;
slice=start=end=0;
startTime= new Date();
endTime="";
rowIndex=0;
rowCount = 0;
conatinerId=0;
numCombosInPastedData=0;
}

function pasteDataPart(clipboardData,index)
{
	var request = newXMLHTTPReq();
    var handlerFunction = getReadyStateHandler(request,paster,false);

    //no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
    request.onreadystatechange = handlerFunction;
    request.open("POST","AjaxcodeHandlerAction.do",true);
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    request.send("&ajaxOperation=pasteData&containerId="+conatinerId+"&cardinality="+cardinality+"&index="+index+"&clipboradData="+clipboardData);

}
function pasteData(conatinerId_temp,cardinality_temp)
{
	intVariables();
	if(window.clipboardData!=null && window.clipboardData!='undefined')
	{
		chkTable = window.clipboardData.getData("Text");
	}
	else if(window.Components)
	{
		try
		{
			this.netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
		} catch(e) {
			alert("Clipboard access not permitted, sorry. You will have to set signed.applet.codebase_principal_support to true.\nVisit 'about:config' the address bar.\nSearch for 'signed.applets.codebase_principal_support' and set its value to true");
		}
		var clip = Components.classes["@mozilla.org/widget/clipboard;1"].getService(Components.interfaces.nsIClipboard);
		if (!clip) return false;
		netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		var trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);
		if (!trans) return false;
		trans.addDataFlavor("text/unicode");
		clip.getData(trans, clip.kGlobalClipboard);
		var str       = new Object();
		var strLength = new Object();
		trans.getTransferData("text/unicode", str, strLength);
		if (str) str       = str.value.QueryInterface(Components.interfaces.nsISupportsString);
		if (str) pastetext = str.data.substring(0, strLength.value / 2);
		chkTable = str.data;
	}
	else
	{
		alert("Your browser doesn't support clipboard access");
	}

	if(chkTable == null)
	{
		return;
	}
	noOfRecordsCopied=chkTable.split("\n").length - 1;
	cardinality = cardinality_temp;
	conatinerId = conatinerId_temp;
	start=rowIndex*batch;
	end = noOfRecordsCopied;
	slice= chkTable.split("\n").slice(start,end);

	if(cardinality == "many")
	{
		rowCount = document.getElementById(conatinerId+"_table").rows.length -1 ;
	}else
	{
		slice= chkTable.split("\n").slice(start,start+1);
	}
	pasteDataPart(slice,rowCount+start+1)
}

function paster(response)
{
	var reponseList  = response.split("~ErrorList");
	var generatedHTML  = reponseList[0];
	var matches = generatedHTML.match(/skipLogicControlScript/g);
	if(matches!=null && matches!='undefined')
	{
		numCombosInPastedData = (matches.length)/2;
	}
	var errorCountList = reponseList[1].split("~RowsCopied");;
	var errorString = errorCountList[0];
	noOfRecordsCopied = errorCountList[1];
	//PRINT ERRORS IF ANY
	printErrors(errorString);
	//ERROR PRINTING FINISHED

	//GET EXTING HTML AND REMOVE THE TABLE
	var existingTableHTML = document.getElementById(conatinerId+"_table").innerHTML;
	var existingTable = document.getElementById(conatinerId+"_table");

	if(!window.Components && document.all)
	{
		var reg = /<SCRIPT defer>/ig
		existingTableHTML=	existingTableHTML.replace(reg,"<SCRIPT>");
		var replaceDiv = document.getElementById("wrapper_div_"+conatinerId);
		var rows = existingTable.rows;
		var divName = "";
		divName = divName + conatinerId + "_substitutionDiv";
		var div = document.getElementById(divName);
		var tab = div.childNodes[0];
		var rowTobeCopied = tab.rows[0];
		var counter = existingTable.rows.length;
		var cells = rowTobeCopied.cells;
		replaceDiv.innerHTML = "<table id='"+conatinerId+"_tbl'>"+generatedHTML+"</table>";
		var wrapperDivName = "wrapper_div_"+conatinerId;
		var wrapperDiv = document.getElementById(wrapperDivName);
		var newtab = wrapperDiv.childNodes[0];
		for(j=0; j<newtab.rows.length; j++)
		{
			var newRow = existingTable.insertRow(-1);
			if(counter%2==0)
			{
				newRow.className="td_color_f0f2f6";
			}
			else
			{
				newRow.className="formField_withoutBorder";
			}
			var newRowTobeCopied = newtab.rows[j];
			var newcells = newRowTobeCopied.cells;
			for(i = 0 ; i < newcells.length ; i++)
			{
				var newCell = newRow.insertCell(i);
				newCell.className = newcells[i].className;
				newCell.innerHTML = newcells[i].innerHTML;
			}
			counter = existingTable.rows.length;
		}
		replaceDiv.innerHTML ="";
	}
	else
	{
		if(parseInt(noOfRecordsCopied)>0)
		{
			var tbody = document.createElement('tbody');
			existingTable.appendChild(tbody);
			tbody.innerHTML = generatedHTML;
		}
	}
	var rowCount_t = document.getElementById(conatinerId+"_rowCount").value * 1;
	document.getElementById(conatinerId+"_rowCount").value = parseInt(rowCount_t)+parseInt(noOfRecordsCopied);
	executeCombos();

}

function printErrors(errorString)
{
	//PRINT ERRORS IF ANY
	var errorHTML = "<table width=\"100%\" height=\"30\"  border=\"0\" cellpadding=\"4\" cellspacing=\"4\" class=\"td_color_FFFFCC\">";
	var errors = errorString.split(',');

	for(var i = 0 ;i<errors.length;i++)
	{
		if(errors[i].length>0)
		{
			errorHTML +=  "<tr><th align=\"center\" class=\"font_bl_nor\"><img src=\"images/de/ic_error.gif\" alt=\"Error\" width=\"28\" height=\"25\" hspace=\"3\" align=\"absmiddle\">"
			+errors[i]+"<br />"
		}

	}
	errorHTML += "</table>";

	document.getElementById("error_div").innerHTML =  errorHTML;
	document.getElementById('error_div').style.display="";
	//ERROR PRINTING FINISHED
}
function executeComboScripts()
{
	var temp = document.getElementsByName("auto_complete_dropdown");
	for(var i = rowCount+1;i<temp.length;i++)
	{
		eval(temp[i].childNodes[1].innerHTML);
	}
}

function executeCombos()
{
	var comboScriptDiv= document.getElementsByName("skipLogicControlScript");
	if (comboScriptDiv != null)
	{
		var totalRowCount =  document.getElementById(conatinerId+"_table").rows.length -1;
		var newRowsAdded = totalRowCount - rowCount;
		var combosPerRow = numCombosInPastedData/newRowsAdded;
		var divCount = comboScriptDiv.length;
		if(divCount!=0)
		{
			var totalCombosToBeExecuted = newRowsAdded * combosPerRow;
			for(var i = 0;i<totalCombosToBeExecuted;i++)
			{
				if (comboScriptDiv[(divCount-1)].value != null)
				{
					var comboScript = document.getElementById(comboScriptDiv[(divCount-1)].value)
					if (comboScript != null)
					{
						eval(comboScript.innerHTML);
					}
				}
				divCount = divCount-1;
			}
		}
	}
}

function removeElement(id)
{
	var c = document.getElementById(id);
	var p = c.parentElement;
	if(c.parentElement==null || c.parentElement=='undefined')
	{
		p = c.parentNode;
	}

	p.removeChild(c);

}

function updateOffsets()
{
	start=rowIndex*batch;
	end = start + batch;
	if( end >noOfRecordsCopied)
	{
		end = noOfRecordsCopied;
	}
	rowIndex++;
}

function updateFileControl(controlId)
{
 if(controlId.id == null)
 {
    ctrlID = controlId;
 }
 else
 {
    ctrlID = controlId.id;
 }
 var spanName = ctrlID + "_button";
 var controlID = ctrlID;
 var spanElement = document.getElementById(spanName);
 var innerHTMLString = "<input type='file' id='" +controlID+ "' name='" +controlID+ "' onchange='isDataChanged();' />";

 spanElement.innerHTML = innerHTMLString;
 setJQueryParameters(controlID);
}

function setJQueryParameters(controlId)
{
	new AjaxUpload(controlId,
		{
		   action: 'UploadFile.do',
			 name: 'upload1',
			 responseType: 'json',
			 onSubmit : function(file,extension)
					{
          var submitButton = document.getElementById('btnDESubmit');
						var imageSrc = "./images/de/waiting.gif";
						var buttonName = controlId + "_button";
						var spanElement = document.getElementById(buttonName);
						var htmlComponent = spanElement.innerHTML;
						htmlComponent = htmlComponent + "&nbsp;&nbsp;<img src='" +imageSrc+ "'/>";
						spanElement.innerHTML = htmlComponent;
					},
			onComplete : function(file, response)
					{
						var jsonResponse = response;
						var fileId = "1";
						var contentType = "";
						var htmlComponent = "";
						var buttonName = controlId + "_button";
						var spanElement = document.getElementById(buttonName);

						if(jsonResponse.uploadedFile!=null)
						{
							fileId = jsonResponse.uploadedFile[0].uploadedFileId;
							contentType = jsonResponse.uploadedFile[0].contentType;
							var imageSrc = "./images/uIEnhancementImages/error-green.gif";
							var deleteImageSrc = "./images/de/deleteIcon.jpg";

							htmlComponent = "<input type='text' disabled name='" +controlId+ "'_1 id='" +controlId+ "_1' value='" +file+ "'/>&nbsp;&nbsp;";
							htmlComponent = htmlComponent + "<img src='" +imageSrc+ "' />&nbsp;&nbsp;";
							htmlComponent = htmlComponent + "<img src='" +deleteImageSrc+ "' style='cursor:pointer' onClick='updateFileControl(\"" +controlId+ "\");' />";
							htmlComponent = htmlComponent + "<input type='hidden' name='" +controlId+ "' id='" +controlId+ "' value='" +fileId+ "'/>";
							htmlComponent = htmlComponent + "<input type='hidden' name='" +controlId+ "_hidden' id='" +controlId+ "_hidden' value='" +file+ "'/>";
							htmlComponent = htmlComponent + "<input type='hidden' name='" +controlId+ "_contentType' id='" +controlId+ "_contentType' value='" +contentType+ "'/>";
							spanElement.innerHTML = htmlComponent;
						}
						else
						{
							htmlComponent = "<input type='file' name='" +controlId+ "' id='" +controlId+ "'/>&nbsp;&nbsp;";
							htmlComponent = htmlComponent + "<span class='font_red'>Error occured .Please try again.</span>";
							spanElement.innerHTML = htmlComponent;
							updateFileControl(controlId);
						}
					}
		});
}
function setDateTimeControlFormatCaption(value)
{
	if(value=="DateOnly")
	{
		setDateFormat("[MM-DD-YYYY]&nbsp;");

	}
	else if(value=="DateAndTime")
	{
		setDateFormat("[MM-DD-YYYY HH:mm]&nbsp;");

	}
	else if(value=="MonthAndYear")
	{
		setDateFormat("[MM-YYYY]&nbsp;");

	}
	else
	{
		setDateFormat("[YYYY]&nbsp;");

	}

}

function setDateFormat(format)
{
	for(var i=0;i<3;i++)
		{

			document.getElementById("dateFormat_"+i).innerHTML=format;
		}


}