/*
	This function automatically handles all the client side events on custom
	list boxes. If onChange event occurs on a list then this function empties
	all the succeeding list boxes & automatically populates the immediate next
	list with appropriate data.
*/
function onCustomListBoxChange(element)
{
	//Get the element identifier
	var elementId = element.id;

	//Get the position of first occurrence of underscore
	var firstIndex = elementId.indexOf("_");

	//Get the position of last occurrence of underscore
	var lastIndex = elementId.lastIndexOf("_");

	//Retrieve the row number on which the list box is placed
	var rowNo = elementId.substring(firstIndex+1,lastIndex);

	//Retrieve the serial number of the list box
	var serialNo = elementId.substring(lastIndex+1);

	//Retrieve the list box name till the last underscore
	var customListBoxName = elementId.substring(0,lastIndex+1);

	//This loop automatically empties the succeeding list boxes
	for(var i=parseInt(serialNo)+1;;i++)
	{
		//Generating handle for each list
		var handleToListBox = document.getElementById(customListBoxName + i);

		if(handleToListBox == null)
		{
			break;
		}
		else
		{
			handleToListBox.options.length = 0;
			handleToListBox.options[0] = new Option('---','-1');
		}
	}

	//Temporary variable to hold array or hashtable instance
	var dataTable = outerMostDataTable.get(rowNo);

	if(dataTable != null)
	{
		//This loop retrieves the data from hashtable for immediate next list to be populated
		for(var i=0;i<=parseInt(serialNo);i++)
		{
			//Handle to preceeding list box
			var handleToListBox = document.getElementById(customListBoxName + i);

			//Value of preceeding list box
			var listBoxValue = handleToListBox.options[handleToListBox.selectedIndex].value;

			dataTable = dataTable.get(listBoxValue);
		}
	}

	//Populate the immediate next list box with appropriate data
	if(dataTable != null)
	{
		var handleToListBox = document.getElementById(customListBoxName + (parseInt(serialNo)+1));

		if(dataTable instanceof Array)
		{
			for(var i=0;i<dataTable.length;i++)
			{
				handleToListBox.options[i+1] = new Option(dataTable[i],dataTable[i]);
			}
			handleToListBox.value = handleToListBox[1].value; // Added by Santosh to set first available position
		}
		else
		{
			var keys = dataTable.keys();
            var  j=0;
			for(var i=0;i<keys.length;i++)
			{
			  	var temp =  parseInt(keys[i]);  // Added by Santosh to cut down non-numeric values

				if(temp.toString() != "NaN")
				{

				  handleToListBox.options[j+1] = new Option(keys[i],keys[i]);
				  j++;
				}

			}

			if(handleToListBox.value == "-1") // Added by Santosh to set first available position
			{
			  handleToListBox.value = handleToListBox[1].value;
			  onCustomListBoxChange(handleToListBox);
		    }

		}

	}
}

function onCustomListBoxChangeInAliquot(element,action)
{
	//alert(element);
   //Get the element identifier
	var elementId = element.id;

	//Get the position of first occurrence of underscore
	var firstIndex = elementId.indexOf("_");

	//Get the position of last occurrence of underscore
	var lastIndex = elementId.lastIndexOf("_");

	//Retrieve the row number on which the list box is placed
	var rowNo = elementId.substring(firstIndex+1,lastIndex);

	//Retrieve the serial number of the list box
	var serialNo = elementId.substring(lastIndex+1);
	//alert("customListBoxName-->" + customListBoxName);

	var customListBoxName = elementId.substring(0,lastIndex+2);
	var dataTable = outerMostDataTable.get(rowNo);

	//alert(customListBoxName);
//alert(document.getElementById(customListBoxName).value);
//alert(document.getElementById(customListBoxName).diaplayValue);
//alert(dataTable.get(document.getElementById(customListBoxName).name));
	var containerName=document.getElementById(customListBoxName).value                                                                    ;
	if(serialNo == 0)
	{

			//alert(containerName);
	   	document.forms[0].submittedFor.value = "ForwardTo";
		var noOfAliquots=document.getElementById('noOfAliquots').value;
		//alert(rowNo);
		document.forms[0].action = action+"&rowNo="+rowNo+"&requestType=ajax&containerName="+containerName+"&noOfAliquots="+noOfAliquots;
		onContainerChange(document.forms[0].action);
	   // document.forms[0].submit();

	}
	else
	{
	  onCustomListBoxChange(element);
    }
}

function onContainerChange(action)
	{

		var request=newXMLHTTPReq();
		if(request == null)
		{
			alert ("Your browser does not support AJAX!");
			return;
		}
		var handlerFunction = getReadyStateHandler(request,responseHandler,true);
		request.onreadystatechange = handlerFunction;
		request.open("POST",action,true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.send("");
	}

	function responseHandler(response)
	{
		var jsonResponse = eval('('+ response+')');
		var populatedMap= new String(jsonResponse.resultObject.containerMap);
		var rowNo=jsonResponse.resultObject.rowNo;

//		alert("populatedMap    "+populatedMap);

		var lastIndex=	populatedMap.indexOf("}");



		var subString=populatedMap.substring(1,lastIndex);
		subString=subString.replace(/ /g,"");

		var rowList=subString.split(",");

		var aliquotCount=document.getElementById('noOfAliquots').value;

			for ( var index = rowNo; index <= aliquotCount; index++)
			{
				updateContainerName(rowList,index);

			}

	}

	function updateContainerName(rowList,index)
	{

		var containerControl=document.getElementById("customListBox_"+index+"_0");

		updateControlValue(rowList,index,containerControl);
		onCustomListBoxChange(containerControl);

		updateDimensionOne(rowList,index);

	}
	function updateDimensionOne(rowList,index)
	{

		var containerControl=document.getElementById("customListBox_"+index+"_1");
		updateControlValue(rowList,index,containerControl);

		updateDimensionTwo(rowList,index);

	}
	function updateDimensionTwo(rowList,index)
	{

		var containerControl=document.getElementById("customListBox_"+index+"_2");
		updateControlValue(rowList,index,containerControl)
		/*var containerControlName=containerControl.name
//alert("rowList "+rowList);
		for(var i=0;i<rowList.length;i++)
		{
			var childList=rowList[i].split("=");
					//alert(childList+ " childList " );

			if(containerControlName.indexOf(childList[0]) != -1)
			{
				containerControl.value=childList[1];
			}
		}*/
	}

	function updateControlValue(rowList,index,containerControl)
	{
		var containerControlName=containerControl.name

		for(var i=0;i<rowList.length;i++)
		{
			var childList=rowList[i].split("=");

			if(containerControlName.indexOf(childList[0]) != -1)
			{
				containerControl.value=childList[1];
			}
		}
	}