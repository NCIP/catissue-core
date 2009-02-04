	
	/*Declaration of global variables.*/

	//The variables request and url are used in ajax functions
	var request,url;
	//Value=1 indicates 'SpecimenRequests' tab and value=2 indicates 'ArrayRequests' tab
	var pageDisplayed=1;
	
	/**
	 * This funciton is called on click of 'Create Array' button in the ArrayRequests.jsp.It redirects to SpecimenArray.jsp
	 */		
	function gotoCreateArrayPage(arrayRowCounter)
	{
		var id  = "defineArrayName_" + arrayRowCounter;
		var arrayName = document.getElementById(id).value;
		var queryString = "?array=" + arrayName + "&operation=add&id=";
		var action = "CreateDefinedArray.do" + queryString;
		document.forms[0].action = action ;		
	    document.forms[0].submit(); 
	}
	
	/**
	 * This function submits the form.(That is,data from both the RequestDetails.jsp and ArrayRequests.jsp)
	 */
	function submitPage()
	{
		document.forms[0].submit();
	}
	   
	/** 
	 * Function updateAllStatus() constructs the request url alongwith the required query string and calls the sendRequestForUpdateStatus() function.
	 * This is called to update statuses of the order items.
	 */
	function updateAllStatus()
	{
		var selectNext = document.getElementById('nextStatusId');
		selectedNextValue = selectNext.value;
		//Send request only if dropdown value is changed.
		if(selectNext.selectedIndex != 0 )
		{		
			//Construst URL to send the request
			var queryString = "updateValue=" + selectedNextValue;
			url = "UpdateStatus.do?"+queryString;
			sendRequestForUpdateStatus();
		}
	}	
		
	/**
	 * Function sendRequest() sends the GET request to the server.
	 */
	function sendRequestForUpdateStatus()
	{	
		//Obtain XMLHttpRequest object
		request = newXMLHTTPReq();
		if(request)
		{  		
			//To update status of orderitems in Specimen Requests tab page	
			if(pageDisplayed == 1)
			{		
				request.onreadystatechange = handleResponse; 	
			}
			//To update status of defined array and existing array in Array Requests tab page
			else
			{
				request.onreadystatechange = handleResponseForArray;
			}
			try
			{		
				request.open("GET", url, true);
				request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				request.send("");
			}
			catch(e)
			{}			
		}
	}
		
	/**
	 * This function assigns the response to the select element in the ArrayRequests page
	 * (for defined arrays and existing arrays) when the response is ready.
	 */
	function handleResponseForArray()
	{
		if(request.readyState == 4)
		{  
		   //Response is ready
			if(request.status == 200)
			{				
				/* ',' seperates statuses of all defined arrays from existing arrays.
				 * The length of responseStringArray would be always 2.
				 * The first element of the array contains response string of the defined array statuses.
				 * The second element of the array contains response string of the existing array statuses.
				 */					
				responseStringArray = parseResponseText(',');
				
				var selectId="";
			
				//Change status for Defined Arrays
				if(document.getElementById("definedArrayRows") != null)
					var num_rows_definedArray = document.getElementById("definedArrayRows").value;
			
				//Change status for Existing Arrays
				if(document.getElementById("numOfExistingArrays") != null)
					var num_rows_existingArray = document.getElementById("numOfExistingArrays").value;
			
				//';' seperates status of individual orderitems in the definedarrays
				definedArrayResponseStringArray = responseStringArray[0].split(';');
			
				//Set new values in the options
				setNewStatusValuesInOptionsArray("array_",num_rows_definedArray,definedArrayResponseStringArray);
				
				//Change status for Existing Arrays
				if(responseStringArray.length == 2)
				{	
					//';' seperates status of indivudual existing arrays
					existingArrayResponseStringArray = responseStringArray[1].split(';');		
					
					//Set new values in the options
					setNewStatusValuesInOptionsArray("existingArray_",num_rows_existingArray,existingArrayResponseStringArray);
				}	
			}//End if(request.status == 200)
		}//End if(request.readyState == 4)	
	}
	
	/**
	 * handleResponse() assigns the response to the select element when the response is ready and there is no error in the response.
	 */
	function handleResponse()
	{		
		if(request.readyState == 4)
		{  
			//Response is ready
			if(request.status == 200)
			{
				responseStringArray = parseResponseText(';');
				
				var tbodyId = document.getElementById('tbody');
				var num_rows = tbodyId.rows.length;				//responseStringArray.length==rows.length
				var selectId="";
			
				//Set new values in the options
				setNewStatusValuesInOptionsArray("select_",num_rows,responseStringArray);
			}//End if(request.status == 200)
		}//End if(request.readyState == 4)	
	}
	
	/**
	 * Added by Ashish to update quantity for selected derived biospecimens.
	 */
	var id;
	function handleResponseForQty()
	{
		if(request.readyState == 4)
		{  
			//Response is ready
			if(request.status == 200)
			{
				/* Response contains required output.
				 * Get the response from server.
				 */
				var responseString = request.responseText;
				var divId = "avaiQty" +id; 
				document.getElementById(divId).innerHTML = responseString;
			}//End if(request.status == 200)
		}//End if(request.readyState == 4)	
	}
	
	/**
	 *  This function updates the quantity for selected derived biospecimens (Added by Ashish)
	 */
	function updateQuantity(requestForId)
	{
		var sendRequestFor = 'updateQty';
		id = requestForId.substring(10);
		var selectNext = document.getElementById(requestForId);
		selectedNextValue = selectNext.value;		
		//Construst URL to send the request
		var queryString = "selectedSpecimen=" + selectedNextValue+"&finalSpecimenListId=" + id;
		url = "UpdateQuantity.do?"+queryString;
		sendRequestForQuantity();	
	}
	
	/**
	 * This function sends 'GET' request to the server for updating quantity (Added by Ashish)
	 */
	function sendRequestForQuantity()
	{
		request = newXMLHTTPReq();
		
		if(request)
		{  					
			request.onreadystatechange = handleResponseForQty; 	
			try
			{		
				request.open("GET", url, true);
				request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				request.send("");
			}
			catch(e)
			{}			
		}
	}
	
	/**
	 * Thisfunction expand/collapses block for rows in the Existing Array(ArrayRequests.jsp)
	 */
	function expandArrayBlock()
	{			
		switchObj = document.getElementById('switchExistingArray');
		tbodyObj = document.getElementById('existingArrayId');	
		switchBlock(switchObj,tbodyObj);		
	}
	
	/**
	 * This function expand/collapses block for individual orderitems defined in an array(ArrayRequests.jsp)
	 */
	function expandOrderItemsInArray(rowid,arrayId)
	{
		var switchObj = document.getElementById('switchdefineArray' + rowid + '_array' + arrayId);
		var dataObj = document.getElementById('dataDefinedArray' + rowid + '_array' + arrayId);
		switchBlock(switchObj,dataObj);
	}
	
	/**
	 * This function expand/collapses block for individual orderitems defined in orderlist in RequestDetails.jsp
	 */
	function switchOrderList(rowid)
	{			
		switchObj = document.getElementById('switch'+rowid);
		dataObj = document.getElementById('data'+rowid);
		switchBlock(switchObj,dataObj);		
	}
	
	/**
	 * This function switches the block in the deifned arrays row
	 */	
	function switchDefinedArrayBlock(rowid)
	{
		switchObj = document.getElementById('switchArray' + rowid);
		dataObj = document.getElementById('dataArray' + rowid);
		arrayHeaderObj = document.getElementById('headerArray' + rowid);
		btnCreateArrayObj = document.getElementById('btnCreateArray' + rowid); 
	
		if(dataObj.style.display != 'none') //Clicked on - image
		{
			dataObj.style.display = 'none';	
			arrayHeaderObj.style.display = 'none';
			btnCreateArrayObj.style.display = 'none';
			switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0"/>';
		}
		else
		{
			if(navigator.appName == "Microsoft Internet Explorer")	
			{
				dataObj.style.display = 'block';	
				arrayHeaderObj.style.display = 'block';
				btnCreateArrayObj.style.display = 'block';
			}
			else
			{
				dataObj.style.display = 'table-row';	
				arrayHeaderObj.style.display = 'table-row';
				btnCreateArrayObj.style.display = 'table-row';
			}
			switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0"/>';
		}
	}

	/**
	 * This function switches the tab to SpecimenRequest Page (i.e,ArrayRequests.jsp)
	 */
	function gotoArrayRequestTab()
	{
		document.getElementById("tabIndexId").value=2;
		pageDisplayed=2;
		//Hide the table containing biospecimen  order items
		document.getElementById('table3_specimenDataTab').style.display="none";
	
		if(navigator.appName == "Microsoft Internet Explorer")
		{
			//Display the table containing ArrayRequests.jsp
			document.getElementById('table5_arrayDataTable').style.display="block";
			expandTableForIE();			
		}
		else
		{
			//Display the table containing ArrayRequests.jsp
			document.getElementById('table5_arrayDataTable').style.display="table-row";
			expandTableForOtherBrowsers();			
		}
		changeCssForTab('arrayRequestTab','specimenRequestTab');		
	}
	
	/**
	 * This function switches the tab to SpecimenRequest Page (i.e, RequestDetails.jsp)
	 */
	function gotoSpecimenRequestTab()
	{
		document.getElementById("tabIndexId").value=1;
		pageDisplayed=1;
		//Hide the table containing ArrayRequests.jsp
		document.getElementById('table5_arrayDataTable').style.display="none";
		if(navigator.appName == "Microsoft Internet Explorer")
		{
			//Display the table containing the data of RequestDetails.jsp
			document.getElementById('table3_specimenDataTab').style.display="block";
			expandTableForIE();			
		}
		else
		{
			//Display the table containing the data of RequestDetails.jsp
			document.getElementById('table3_specimenDataTab').style.display="table-row";
			expandTableForOtherBrowsers();			
		}
		changeCssForTab('specimenRequestTab','arrayRequestTab');
	}
		
	
	/**
	 * This function expands tables on both tabbed pages(i.e,Specimen Requests tab and Array Requests tab)
	 * when displayed on Microsoft IE browser
	 */
	function expandTableForIE()
	{
		//Display other tables required for both the tabbed pages
		document.getElementById('table4_pageFooter').style.display="block";
		document.getElementById('table2_TabPage').style.display="block";
		document.getElementById('table1_OrderRequestHeader').style.display="block";
	}
	
	/**
	 * This funciton expands tables on both tabbed pages(i.e,Specimen Requests tab and Array Requests tab)
	 * when displayed on Mozilla/NetScape or other browsers
	 */
	function expandTableForOtherBrowsers()
	{
		//Display other tables required for both the tabbed pages
		document.getElementById('table4_pageFooter').style.display="table-row";
		document.getElementById('table2_TabPage').style.display="table-row";
		document.getElementById('table1_OrderRequestHeader').style.display="table-row";				
	}
	
	/**
	 * This function changes the css of the tab depending upon whether 'Specimen Requests' or 'Array Requests' tab
	 * was selected. 
	 */
	function changeCssForTab(strSelectedTab,strUnSelectedTab)
	{
		var selectedTab = document.getElementById(strSelectedTab);
		var unSelectedTab = document.getElementById(strUnSelectedTab);
		
		selectedTab.onmouseover=null;
		selectedTab.onmouseout=null;
		selectedTab.className="tabMenuItemSelected";
	
		unSelectedTab.onmouseover=function() { changeMenuStyle(this,'tabMenuItemOver'),showCursor();};
		unSelectedTab.onmouseout=function() {changeMenuStyle(this,'tabMenuItem'),hideCursor();};
		unSelectedTab.className="tabMenuItem";
	}
	
	/**
	 * This function obtains responsetext from the request object and parses it with the given delimiter and returns a string array
	 */
	function parseResponseText(delimiter)
	{
		//Response contains required output.								    
		var responseString = request.responseText;
		var responseStringArray = new Array();
		
		//Get array values for each row.	
		responseStringArray = responseString.split(delimiter);
		return responseStringArray;
	}
	
	/**
	 * This function sets the new status values in the options array
	 */
	function setNewStatusValuesInOptionsArray(strElement,rowCount,responseStringArray)
	{
		for(var counter=0;counter<rowCount;counter++)
		{
			strSelectElementId = strElement + counter;
			selectElementId = document.getElementById(strSelectElementId);
			optionStringArray = responseStringArray[counter].split('||');
				
			for(var x=0;x<optionStringArray.length-1;x++)
			{										
				optionStrings = optionStringArray[x].split('|');
				//Set the text/value pair in options.
				selectElementId.options[x] = new Option(optionStrings[0],optionStrings[1]);
			}
		}
	}
	
	/**
	 * This function switches the block when clicked on +/- image
	 */
	function switchBlock(switchObj,dataObj)
	{
		if(dataObj.style.display != 'none') //Clicked on - image
		{
			dataObj.style.display = 'none';	
			switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0"/>';
		}
		else  							   //Clicked on + image
		{
			if(navigator.appName == "Microsoft Internet Explorer")					
				dataObj.style.display = 'block';					
			else
				dataObj.style.display = 'table-row';					
			
			switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0"/>';
		}
	}
