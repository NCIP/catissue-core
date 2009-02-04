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
		}
		else
		{
			var keys = dataTable.keys();

			for(var i=0;i<keys.length;i++)
			{
			  	var temp =  parseInt(keys[i]);  // Added by Santosh to cut down non-numeric values
				if(temp.toString() != "NaN")
				handleToListBox.options[i+1] = new Option(keys[i],keys[i]);
			}
		}
	}
}

function onCustomListBoxChangeInAliquot(element,action)
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
	//alert("customListBoxName-->" + customListBoxName);	
	if(serialNo == 0)
	{
	
	   	document.forms[0].submittedFor.value = "ForwardTo";
		document.forms[0].action = action+"&rowNo="+rowNo;
	    document.forms[0].submit();

	}
	else
	{
	  onCustomListBoxChange(element);
    }
}