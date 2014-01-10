//Mandar: 24-Apr-06 for tooltip
		// -------
	var timeInterval=100;
	var interval;
	var objID="";

		function showStatus(sMsg) 
		{
		    window.status = sMsg ;
		}
		function showTip(objId)
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

	
	// -------
	
	function trim(inputString) {
		   // Removes leading and trailing spaces from the passed string. Also removes
		   // consecutive spaces and replaces it with one space. If something besides
		   // a string is passed in (null, custom object, etc.) then return the input.
		   if (typeof inputString != "string") { return inputString; }
		   var retValue = inputString;
		   var ch = retValue.substring(0, 1);
		   while (ch == " ") { // Check for spaces at the beginning of the string
		      retValue = retValue.substring(1, retValue.length);
		      ch = retValue.substring(0, 1);
		   }
		   ch = retValue.substring(retValue.length-1, retValue.length);
		   while (ch == " ") { // Check for spaces at the end of the string
		      retValue = retValue.substring(0, retValue.length-1);
		      ch = retValue.substring(retValue.length-1, retValue.length);
		   }
		   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
		      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
		   }
		   return retValue; // Return the trimmed string back to the user
		} // Ends the "trim" function
		
// This function is required to trim the spaces introduced by user	
function trimByAutoTag(object) {
   	object.value = trim(object.value);
}

function trimByAutoTagAndSetIdInForm(object) {
 
	object.value = trim(object.value);
	var valuesList = window["valuesInListOf" + object.id];
	var idsList = window["idsInListOf" + object.id];
	var index = valuesList.indexOf(object.value);
    var idObject = document.getElementById(object.id.substr(7,object.id.length));  // 7 for "display"
	
	//  Type is changing on client side, hence will not be in the list
	if(idObject.id == "type")
	{
	   idObject.value = object.value;
	}
	else
	{
	  idObject.value = idsList[index];
	}
	
	// alert(idObject.id + idObject.value);

}
