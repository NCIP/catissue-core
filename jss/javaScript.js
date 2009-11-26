
/**Confirm dialog box for disable functionality*/
function confirmDialogForDisable()
{
	var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
	return go;
}

/* for deleting or disabling the objects*/
function deleteObject(action,onSubmitAction)
{
	var go = confirmDialogForDisable();
	 if (go==true)
	 {
		document.forms[0].onSubmit.value = onSubmitAction;
		document.forms[0].activityStatus.value = "Disabled";
		document.forms[0].action = action;
		document.forms[0].submit();
	 }
}
/* for deleting or disabling the SpecimenArray objects*/
function deleteSpecimenArray(action,onSubmitAction)
{	var go = confirmDialogForDisable();
	if (go==true)
	{
		document.forms[0].onSubmit.value = onSubmitAction;
		document.forms[0].action = action;
		document.forms[0].submit();
	}	
}
/* for deleting or disabling the Storage Container objects*/
function deleteStorageContainer(action,onSubmitAction)
{	
	var go = confirmDialogForDisable();
	 if (go==true)
	 {
		document.forms[0].onSubmit.value = onSubmitAction;
		document.forms[0].activityStatus.value = "Disabled";
		document.forms[0].action = action;
		document.forms[0].submit();
	 }
}


/* section for outer block start */
function replaceSpeChar(div,d1,searchChar)
{
	var y = d1.innerHTML;
	insno=insno+1;
	var starpos = y.indexOf(searchChar); // pos of 1st `.  ie: No.
	var adstr="";
	var c=0;
	for(c=0;c<starpos;c++)
	{
		adstr = y.replace(searchChar,insno);
		starpos = y.indexOf(searchChar); 
		y = adstr;
	}
	addDiv(div,adstr);
}

/* section for outer block end */

function addDiv(div,adstr)
{
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
	
}


function confirmDisable(action,formField)
{	
	if((formField != undefined) && (formField.value == "Disabled"))
	{
		var go = confirmDialogForDisable();
		if (go==true)
		{	
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	}
	else
	{
		document.forms[0].action = action;
		document.forms[0].submit();
	}			
}
	

function enableButton(formButton,countElement,checkName)
{
	/** number of rows present    **/
	var counts = countElement.value;
	if(counts == undefined){
		var cnt = document.getElementById(countElement);
		
	/** number of rows present(counts) when countElement is again element    **/
	counts = cnt.value;
	}
	/** checking whether checkbox is checked or not **/
	var status = false;
	
	for(var i=1;i <= counts;i++)
	{
		
		/** creating checkbox name**/
		var itemCheck = checkName+i;
		var chk = document.getElementById(itemCheck);
	        if (chk.checked == true)
	        {
				
	        	status = true;
	        	break;
	        }
	}
	if(status)
		formButton.disabled = false;
	else
		formButton.disabled = true;
}
// function to delete biHazards and external identifiers from UI and to call respective action
// Patch-Id: Improve_Space_Usability_On_Specimen_Page_1
// Description: pageOf variable is added as an argument to function. If pageOf=pageOfNewSpecimenPage then do not call action.
function  deleteChecked(subdivtag,action,countElement,checkName,isOuterTable,pageOf)
{
	var r = new Array(); 
	
	/** element of tbody    **/
	var element = document.getElementById(subdivtag);
	
	/** number of rows present    **/
	var counts = countElement.value;
	if(counts == undefined){
		var cnt = document.getElementById(countElement);
		
		/** number of rows present(counts) when countElement is again element    **/
		counts = cnt.value;
	}
	/** number if rows deleted**/
	var delCounts = 0;
	
	/** checking whether checkbox is checked or not **/
	var status = false;
	for(i=1;i <= counts;i++)
	{
		/** creating checkbox name**/
		itemCheck = checkName+i;
		var chk = document.getElementById(itemCheck);
		
		
		if(chk.checked==true){
			var pNode = null;
			var k = 0;
			
			/** condition for checking whether outerTable's delete is clicked or not **/
			if(isOuterTable) {
				tableId = "table_" + i;
				var table = document.getElementById(tableId);
				// md 21 mar start
				var currentRow = table.parentNode.parentNode;
				k = currentRow.rowIndex;
				pNode = element.parentNode;
				pNode.deleteRow(k);
				// md 21 mar end
				
				/** removing table from tbody tag(div)   **/
				// 21 mar commented by md: element.removeChild(table);
	
			}
			else {
				/** getting table ref from tbody    **/
				pNode = element.parentNode;
				
				/** curent row of table ref **/
				var currentRow = chk.parentNode.parentNode;
				k = currentRow.rowIndex;
				
				/** deleting row from table **/
				pNode.deleteRow(k);
			}
			
			delCounts++;
			status = true;
			
		}
	}
	
	
	if(countElement.value == undefined){
		/** setting number of rows present in form   **/
		cnt.value = counts - delCounts;
	}
	else
		/** setting number of rows present in form   **/
		countElement.value = (countElement.value - delCounts);
	
	// Patch-Id: Improve_Space_Usability_On_Specimen_Page_1 
	// Description: if page of pageOfNewSpecimen then no need to call action
	if(pageOf=='pageOfNewSpecimen')
	{
		status=false;
	}
	if(status){
		/** set action when checkbox is clicked **/
		document.forms[0].action = action;
		document.forms[0].submit();
	}
		
		
}
		
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
	
		function moveToNext(element,value,nextID )
		{
		if (value.length==element.maxLength)
			{
				document.getElementById(nextID).focus();
			}
		}
		
function rearrangeIdsForDistribution() {
       		var addMore =	document.getElementById("addMore");
			var tableRows = addMore.rows

			for(var i=0;i<tableRows.length;i++) {
			    var row  = tableRows[i];

			   // var childNodeCollection = row.all;
			    var childNodeCollection = row.cells;

				for(var j=0;j<childNodeCollection.length;j++) {
					   
					   if( childNodeCollection[j].firstChild.nodeType == "3" )
						    continue;

						var idName = childNodeCollection[j].firstChild.id;
 
						if(idName == null || idName == "" ) {
                              idName =  childNodeCollection[j].firstChild.name;
						} 

                        var newId = getNewId(idName,i+1);
						childNodeCollection[j].firstChild.name =  newId;
						childNodeCollection[j].firstChild.id =  newId;
				}
			}
		}

function getNewId(oldId,newRowNo) {
	
	if( oldId == "undefined" || oldId == null || oldId == ""  ) {
	 return oldId;
	}


if( oldId.indexOf(":") == -1) {
    newId =  oldId.substr(0,oldId.indexOf("_")+1) + newRowNo ;

} else {
	var firstPart =  oldId.substr(0,oldId.indexOf(":")) ;
	var thirdPart = oldId.substr(oldId.indexOf("_")) ;
	newId = firstPart + ":"+ newRowNo  + thirdPart ;
}

//alert(oldId + "====>" + newId );

/*	var firstPart = oldId.split(":")[0];
	var thirdPart = oldId.split("_")[1];

   var newId =  firstPart + ":"+ newRowNo + "_" + thirdPart ; */

   return  newId;
}

function disableDistributeOptions() {

	  var distributionForm1 =  document.forms[0];
	   if (  parseInt(document.forms[0].counter.value) == 0)
	   {
		         
	   			  distributionForm1.distributionBasedOn[0].disabled=false
				  distributionForm1.distributionBasedOn[1].disabled=false;
				  distributionForm1.distributionType[0].disabled = false;
				  distributionForm1.distributionType[1].disabled = false;
				  return;
	   } 

	 	if (distributionForm1.distributionBasedOn[0].checked == true)
		 {
        	    distributionForm1.distributionBasedOn[1].disabled=true
		 }

	 	if (distributionForm1.distributionBasedOn[1].checked == true)
		 {
        	    distributionForm1.distributionBasedOn[0].disabled=true
		 }

	 	if (distributionForm1.distributionType[0].checked == true)
		 {
        	    distributionForm1.distributionType[1].disabled=true
		 }

	 	if (distributionForm1.distributionType[1].checked == true)
		 {
        	    distributionForm1.distributionType[0].disabled=true
		 }
}

function checkDistributionBasedOn() {

	 	var distributionForm1 =  document.forms[0];
	 	if (distributionForm1.distributionBasedOn[0].checked == false && distributionForm1.distributionBasedOn[1].checked == false) {
		 	 alert("Please select 'Distribution Based On'");
		 	 return false;
        }
        
        return true;
}

function  deleteCheckedNoSubmit(subdivtag,action,countElement,checkName,isOuterTable) {
	var r = new Array(); 
	
	/** element of tbody    **/
	var element = document.getElementById(subdivtag);
	
	/** number of rows present    **/
	var counts = countElement.value;
	if(counts == undefined){
		var cnt = document.getElementById(countElement);
		
		/** number of rows present(counts) when countElement is again element    **/
		counts = cnt.value;
	}
	/** number if rows deleted**/
	var delCounts = 0;


	/** checking whether checkbox is checked or not **/
	var status = false;
	for(i=1;i <= counts;i++)
	{
		itemCheck = checkName+i;
		var chk = document.getElementById(itemCheck);
		
		
		if(chk.checked==true){
			var pNode = null;
			var k = 0;
			
			/** condition for checking whether outerTable's delete is clicked or not **/
			if(isOuterTable) {
				tableId = "table_" + i;
				var table = document.getElementById(tableId);
				// md 21 mar start
				var currentRow = table.parentNode.parentNode;
				k = currentRow.rowIndex;
				pNode = element.parentNode;
				pNode.deleteRow(k);
				// md 21 mar end
				
				/** removing table from tbody tag(div)   **/
				// 21 mar commented by md: element.removeChild(table);
	
			}
			else {
				/** getting table ref from tbody    **/
				pNode = element.parentNode;
				
				/** curent row of table ref **/
				var currentRow = chk.parentNode.parentNode;
				k = currentRow.rowIndex;
				
				/** deleting row from table **/
				pNode.deleteRow(k);
			}
			
			delCounts++;
			status = true;
			
		}
	}
	
	
	if(countElement.value == undefined){
		/** setting number of rows present in form   **/
		cnt.value = counts - delCounts;
	}
	else
		/** setting number of rows present in form   **/
		countElement.value = (countElement.value - delCounts);
	
	return status;
}

/* --- Start Multiple Specimen  javascript functions ---*/
function showCommentsDialog(operation,key) {
 		 var url ='NewMultipleSpecimenAction.do?method=showCommentsDialog&operation=' + operation+ '&specimenAttributeKey=' + key;
		 var properties = "height = 120; width:100px; Top:300; Left:350; center: Yes; resizable: no;status:no;help:no;toolbar :no";
   		 window.open(url,"caTissuecore", properties);
}

function showExtenalIdentifierDialog(operation,key) {
	   var url ='NewMultipleSpecimenAction.do?method=showExtenalIdentifierDialog&operation=' + operation+ '&specimenAttributeKey=' + key;
        NewWindow(url,'name','810','320','yes');
}

function showBioHazardDialog(operation,key) {
	   var url ='NewMultipleSpecimenAction.do?method=showBioHazardDialog&operation=' + operation+ '&specimenAttributeKey=' + key;
        NewWindow(url,'name','810','320','yes');
}
function showDerivedSpecimenDialog(operation,key,derivedSpecimenCollectionGroup,derivedSpecimenClass,derivedParentSpecimenLabel,derivedParentSpecimenBarcode,derivedSpecimenType) {
        
	 if(derivedSpecimenCollectionGroup==null || derivedSpecimenCollectionGroup=="null" || derivedSpecimenCollectionGroup==""||derivedSpecimenCollectionGroup=="-- Select --")
       {
            alert("You have to give Specimen Group Name or valid Parent Specimen before creating derived Specimen");
       }
  else {
        var url ='NewMultipleSpecimenAction.do?deriveButtonClicked=true&method=showDerivedSpecimenDialog&specimenAttributeKey=' + key + '&operation=' + operation + '&derivedSpecimenCollectionGroup=' + derivedSpecimenCollectionGroup + '&derivedSpecimenClass=' + derivedSpecimenClass + '&derivedParentSpecimenLabel=' + derivedParentSpecimenLabel + '&derivedParentSpecimenBarcode=' + derivedParentSpecimenBarcode + '&derivedSpecimenType=' + derivedSpecimenType + '&isMultipleSpecimenCall=true';
	    NewWindow(url,'name','600','600','yes');
		}
}
function showEventsDialog(operation,key) {
	   var url ='NewMultipleSpecimenAction.do?method=showEventsDialog&operation=' + operation+ '&specimenAttributeKey=' + key;
        NewWindow(url,'name','810','320','yes');
}
function submitComments() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitComments";
  form.submit();
}	   

/* Function to show TissueSiteTreeApplet */
function showTreeMap(column)
{
//	alert("TissueSite TreeMap : column"+column);
	var pName = "MultipleSpecimen:"+column+"_tissueSite";
//	alert(pName);
	// Patch ID: Bug#3090_6
	NewWindow('ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName='+pName+'&cdeName=Tissue Site','name','360','525','no');
}
//to delete since locations will be auto allocated.
function setStoragePosition(specimenMapKey,storageId,storageType,xPos,yPos) {
   parent.window.opener.document.applets[0].setStorageDetails(specimenMapKey,storageId,storageType,xPos,yPos);
}

/**
* Patch ID: Entered_Events_Need_To_Be_Visible_18
* See also: 1-5
* Description: Since the signature of the method is changed empty string is passed to finction setButtonCaption
*/ 
function setCaptionInMapFromJS(specimenMapKey) {
  parent.window.opener.document.applets[0].setButtonCaption(specimenMapKey,"");
}

function showStoragePositionMap(specimenAttributeKey,collectionGroup,specimenClass) {
	var  url = "ShowFramedPage.do?pageOf=pageOfMultipleSpecimen";
	url = url + "&specimenAttributeKey=" + specimenAttributeKey;
	url = url + "&SpecimenCollectionGroup=" + collectionGroup;
	url = url + "&specimenClass=" + specimenClass;
	url = url + "&specimenCallBackFunction=" + "setStoragePosition";

    NewWindow(url,'name','810','320','yes');
}

function getSpecimenSubmitResult(target) {
//	  document.forms[0].action =    document.forms[0].action + "?method=getResult&multipleSpecimenResult=" + target ;  
	  document.forms[0].action = "MultipleSpecimenStorageLocation.do";
	  if(window.parent.frames.length > 0)
      {
		document.forms[0].action = "CPQueryMultipleSpecimenStorageLocation.do?pageOf=pageOfMultipleSpecimenCPQuery";
	  }
	  document.forms[0].submit(); 
}

function showSpecimenErrorMessages(errorMsg) {
     var errorDiv =  document.getElementById("errorMessages");
     errorDiv.innerHTML = "<LI><font color=red>" +  errorMsg + "</font></LI>";
}

function submitExternalIdentifiers() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitExternalIdentifiers";
  form.submit();
}	
//used to show error messages while copy paste.   
function showErrorMessage(errorMessage)
{
	alert(errorMessage);
}

function submitBioHazards() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitBioHazards";
  form.submit();
}

// Bug 11446 S

// function to delete External identifiers
// Patch-Id: Improve_Space_Usability_On_Specimen_Page_1
// Description: pageOf variable is added as an argument to function. If pageOf=pageOfNewSpecimenPage then do not call action.
/*function deleteExternalIdentifiers(pageOf)
{
	deleteChecked('addExternalIdentifier','NewMultipleSpecimenAction.do?method=deleteExternalIdentifiers&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false,pageOf)
}*/

//Bug 11446 E

function submitEvents() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitEvents&operation=addMultipleSpecimen";
  form.submit();
}

//Bug 11446 S
// function to delete bioHazards
// Patch-Id: Improve_Space_Usability_On_Specimen_Page_1
// Description: pageOf variable is added as an argument to function. If pageOf=pageOfNewSpecimenPage then do not call action.
/*function deleteBioHazards(pageOf)
{
	deleteChecked('addBiohazardRow','NewMultipleSpecimenAction.do?method=deleteBioHazards&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false,pageOf);
}*/

//Bug 11446 E



/* --- End Multiple Specimen  javascript functions ---*/

/*-Common method to select between edit box or drop down to select storage container-*/
    	function onStorageRadioButtonClick(element)
    	{
    		//alert("inside method of radio button click");
			if(element.value == 1)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;
				document.forms[0].containerMap.disabled = true;
				document.forms[0].customListBox_1_0.disabled = false;
				document.forms[0].customListBox_1_1.disabled = false;
				document.forms[0].customListBox_1_2.disabled = false;
			}
			else
			{
				document.forms[0].selectedContainerName.disabled = false;
				document.forms[0].pos1.disabled = false;
				document.forms[0].pos2.disabled = false;
				document.forms[0].containerMap.disabled = false;
				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
			}
    	}
/* --- Start Specimen Array javascript functions ---*/
function changeArrayType() 
{

		var form = document.forms[0];
		var specimenArrayTypeVal = form.specimenArrayTypeId.value;
		if (specimenArrayTypeVal == -1)
		{
			alert("Please select Valid Specimen Array Type !!");
			return;
		}

//		var confirmArrayChange = confirm("Your entered contents will be lost !! do you really want to continue?");
		
//		if (confirmArrayChange == true) 
//		{
			var form = document.forms[0];
			//form.operation.value="CreateSpecimenArray";
			form.subOperation.value="ChangeArraytype";
			form.action = "SpecimenArray.do?menuSelected=20";
			form.submit();
//		}	
}

function doStoreTableData() 
{
		var applet = document.applets[0];
		if (applet != null) 
		{
			applet.updateSessionData();
		}
}

function doUploadSpecimenArray() 
{
		doStoreTableData();
		var form = document.forms[0];
		form.submit();
}

function createSpecimenArrayClicked()
{
		var form = document.forms[0];
		var specimenArrayTypeVal = form.specimenArrayTypeId.value;
		if (specimenArrayTypeVal == -1)
		{
			alert("Please select Valid Specimen Array Type !!");
			return;
		}
		
		//form.operation.value="CreateSpecimenArray";
		form.subOperation.value="CreateSpecimenArray";
		form.action = "SpecimenArray.do?menuSelected=20";
		form.submit();
}

function doClickEnterSpecimenBy()
{
		var form = document.forms[0];
		var createSpecimenArrayVal = form.createSpecimenArray.value;
		if (createSpecimenArrayVal == "no")
		{
			return;
		}
		
		var applet = document.applets[0];
		if (applet != null) 
		{
			var form = document.forms[0];
			var enterSpecimenBy = form.enterSpecimenBy[0].value;
			if (form.enterSpecimenBy[1].checked)
			{
				enterSpecimenBy = form.enterSpecimenBy[1].value;
			}
			applet.changeEnterSpecimenBy(enterSpecimenBy);
		}
//		var confirmChange = confirm("Your entered Array Contents will be lost !! do you really want to continue?");
//		if (confirmChange == true)
//		{
//			doStoreTableData();
//			form.subOperation.value="ChangeEnterSpecimenBy";
//			form.action = "SpecimenArray.do?menuSelected=20";
//			form.submit();
//		}
}
/* --- End Specimen Array javascript functions ---*/




function onRadioButtonGroupClick(element)
    	{		
		
    		//alert("inside method of radio button click");
			if(element.value == 1)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;

				document.forms[0].containerMap.disabled = true;
				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
				
				//document.forms[0].virtuallyLocated.value = true;
				
			}
			else if(element.value == 2)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;

				document.forms[0].containerMap.disabled = true;
				document.forms[0].customListBox_1_0.disabled = false;
				document.forms[0].customListBox_1_1.disabled = false;
				document.forms[0].customListBox_1_2.disabled = false;
				
				//document.forms[0].virtuallyLocated.value = false;
				
				onCollOrClassChange();

			}
			else
			{
				document.forms[0].selectedContainerName.disabled = false;
				document.forms[0].pos1.disabled = false;
				document.forms[0].pos2.disabled = false;
				document.forms[0].containerMap.disabled = false;

				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
				
				//document.forms[0].virtuallyLocated.value = ;
				onCollOrClassChange();
			}
    	}
		
		
		function onRadioButtonGroupClickForTransfer(element)
    	{		
    		//alert("inside method of radio button click");
			
			if(element.value == 1)
			{			
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;
				document.forms[0].containerMap.disabled = true;
				
				document.forms[0].customListBox_1_0.disabled = false;
				document.forms[0].customListBox_1_1.disabled = false;
				document.forms[0].customListBox_1_2.disabled = false;				
				//document.forms[0].virtuallyLocated.value = false;
				
			}
			else if (element.value == 2)
			{				
				document.forms[0].selectedContainerName.disabled = false;
				document.forms[0].pos1.disabled = false;
				document.forms[0].pos2.disabled = false;
				document.forms[0].containerMap.disabled = false;

				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;				
				//document.forms[0].virtuallyLocated.value = ;				
			}
    	}
		
		
		function onRadioButtonGroupClickForDerived(element)
    	{		
    		//alert("inside method of radio button click");
			if(element.value == 1)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;

				document.forms[0].containerMap.disabled = true;
				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
				
				//document.forms[0].virtuallyLocated.value = true;
				
			}
			else if(element.value == 2)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;

				document.forms[0].containerMap.disabled = true;
				document.forms[0].customListBox_1_0.disabled = false;
				document.forms[0].customListBox_1_1.disabled = false;
				document.forms[0].customListBox_1_2.disabled = false;
				
				isLabelBarcodeOrClassChange();
				//document.forms[0].virtuallyLocated.value = false;
		
			}
			else
			{
				document.forms[0].selectedContainerName.disabled = false;
				document.forms[0].pos1.disabled = false;
				document.forms[0].pos2.disabled = false;
				document.forms[0].containerMap.disabled = false;

				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
				
				isLabelBarcodeOrClassChange();				
				//document.forms[0].virtuallyLocated.value = ;
				
			}
    	}
		
		
		function onRadioButtonGroupClickForArray(element)
    	{
    		//alert("inside method of radio button click");
			if(element.value == 1)
			{
				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;				
				document.forms[0].containerMap.disabled = true;
				
				document.forms[0].customListBox_1_0.disabled = false;
				document.forms[0].customListBox_1_1.disabled = false;
				document.forms[0].customListBox_1_2.disabled = false;
			}
			else
			{
				document.forms[0].selectedContainerName.disabled = false;
				document.forms[0].pos1.disabled = false;
				document.forms[0].pos2.disabled = false;
				document.forms[0].containerMap.disabled = false;
				
				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;
			}
    	}
    	
    	//This function gets called whenever a specimen/ scg gets added in system.
    	function refreshTree(cpAndParticipantsFrame, treeFrame,cpSearchCpId,cpSearchParticipantId,nodeId)
    	{
    		//alert("In refresh tree method & node id is:"+nodeId);
    		//var cpId = window.parent.frames[cpAndParticipantsFrame].document.getElementById("cpId").value;
			//var participantId = window.parent.frames[cpAndParticipantsFrame].document.getElementById("participantId").value;
			//window.parent.frames[treeFrame].location="showTree.do?"+cpSearchCpId+"="+cpId+"&"+cpSearchParticipantId+"="+participantId+"&nodeId="+nodeId;
   		
   		     //Changes made related to flex ....By Baljeet 
   		    
   		     top.frames["cpAndParticipantView"].pageInit(nodeId); 
    	}
		
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

// This function is required to trim the spaces introduced by user	
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

// /** Name : Vijay_Pande
// * Reviewer : Santosh_Chandak
// * Bug ID: Improve_Space_Usability_On_Specimen_Page
// * Patch ID: Improve_Space_Usability_On_Specimen_Page_1 
// * See also: 1-5
// * Description: To improve space usability on specimen page, the table which are rarely used are kept invisible by default. 
// * Following script is used to toggle between the show and hide the table. (Collected/recieved Events, External identifiers, And Biohazard table)
// */
// This function collapse or expand the table  
function switchStyle(image,dispVar, object, buttonId)
{
	imageObj = document.getElementById(image);	
	if(document.getElementById(dispVar).value== 'hide') //Clicked on + image
	{
		show(object,dispVar);
		imageObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" /> ';
//		/** Name : Vijay_Pande
//		 * Reviewer : Sachin_Lale
//		 * Bug ID: 4169
//		 * Patch ID: 4169_1 
//		 * See also: -
//		 * Description: There was a java sript error while clicking on image to expand/colapse collected/recieved event table. 
//		 * Error was because of the collection/recieved event table do not contain 'AddMore' button.
//		 * Following check is added to check whether buttonId is provided or not.
//		 */
		if(document.getElementById(buttonId))
		{		document.getElementById(buttonId).disabled=false; }
	}
	else  							   //Clicked on - image
	{
		hide(object,dispVar);				
		imageObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" /> ';
		if(document.getElementById(buttonId))
		{	document.getElementById(buttonId).disabled=true;  }
//		/**  -- patch ends here -- */
	}
}
// This function is to show object on UI 
function show(obj,dispVar)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display = 'block';
	document.getElementById(dispVar).value='show';
}
// This function is to hide object from UI 
function hide(obj,dispVar)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display='none';
	document.getElementById(dispVar).value='hide';
}
// function that will run after the new specimen page is loaded to set tables in collapsed form
function newSpecimenInit()
{
	switchStyle('imageEI','eiDispType','externalIdentifiers','addExId');
	switchStyle('imageBH','bhDispType','biohazards','addBiohazard');
	if(document.getElementById('crDispType')!=null)
	{	
		if(document.getElementById('crDispType').value=="hide")
		{
			hide('collRecTable','crDispType');
			imageObj = document.getElementById('imageCR');	
			imageObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" /> ';
		}
		else
		{		
			show('collRecTable','crDispType');
			imageObj = document.getElementById('imageCR');	
			imageObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" /> ';
		}
	}
}

/**
 * Name: Chetan Patil
 * Reviewer: Sachin Lale
 * Bug ID: Bug#4116
 * Patch ID: Bug#4116_1
 * Description: The following two functions are commonly used across many JSPs;
 * hence made them reusable by moving them to this shared file.
 */
function openPopupWindow(frameUrl,name)
{
	platform = navigator.platform.toLowerCase();
    if (platform.indexOf("mac") != -1)
	{
    	NewWindow(frameUrl,name,screen.width,screen.height,'no');
    }
    else
    {
    	NewWindow(frameUrl,name,'800','600','no');
    }
}

function mapButtonClickedOnNewSpecimen(frameUrl,name)
{
   	var storageContainer = document.getElementById('selectedContainerName').value;
	frameUrl+="&storageContainerName="+storageContainer;
	openPopupWindow(frameUrl,name);
}

function mapButtonClickedOnSpecimen(frameUrl,name,storageContainerName)
{
	var contName=storageContainerName+"";
   	var storageContainer = document.getElementById(contName).value;
	frameUrl+="&storageContainerName="+storageContainer;
	alert(frameUrl+"");
	openPopupWindow(frameUrl,name);
}

// Patch ID: Bug#4129_4
// Description: Function to verify the validity of number of specimen value.
//  check for valid numeric strings	
function isNumeric(strString)
{
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;

	if (strString.length == 0) return false;
		
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++)
	{
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1)
		{
			blnResult = false;
		}
	}
	return blnResult;
}

   	//Added by Preeti for DE Integration


function initializeGridForSelectedEntities(groupsXML)
{
	gridForEntities= new dhtmlXGridObject('gridForEnities');
	gridForEntities.setImagePath("dhtml_comp/imgs/");
	gridForEntities.enableAutoHeigth(false);
	gridForEntities.setHeader("#,Form Title,Entity,Date,Created By,Conditions");
	gridForEntities.setInitWidthsP("5,23,17,12,15,15,13");
	gridForEntities.setColAlign("left,left,left,left,left,left,left")
	gridForEntities.setColTypes("ch,link,ro,ro,ro,ro,link");
	gridForEntities.setColSorting("str,str,str,date,str,str,str");

   
	gridForEntities.init();
	
	gridForEntities.loadXMLString(groupsXML);
	if(gridForEntities.getRowsNum()>0)
	{
		gridForEntities.selectRow(0,true,false);	
	}
}


function initializeGridForEntities()
{
	gridForEntities= new dhtmlXGridObject('gridForEnities');
	gridForEntities.setImagePath("dhtml_comp/imgs/");
	gridForEntities.enableAutoHeigth(false);
	gridForEntities.setHeader("Form Title,Entity,Date,Created By,Conditions");
	gridForEntities.setInitWidthsP("27,18,15,18,22");
	gridForEntities.setColAlign("left,left,left,left,left")
	gridForEntities.setColTypes("link,ro,ro,ro,link");
	gridForEntities.setColSorting("str,str,date,str,str");
	gridForEntities.init();
}

function groupChangedResponse(entitiesXML)
{	
	gridForEntities.clearAll(false);
	var entities	= entitiesXML.split("@");
	for(var row=0;row<entities.length-1;row=row+1)
	{
   	  gridForEntities.addRow(row+1,entities[row],row+1);
	}
}

function groupSelected(groupid)
{
	var request = newXMLHTTPReq();
	var handlerFunction = getReadyStateHandler(request,groupChangedResponse,true);

	//no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
	request.onreadystatechange = handlerFunction;
	//send data to ActionServlet
	
	//Open connection to servlet
	request.open("POST","DefineAnnotations.do",true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	//alert('groupid:'+groupid.trim()+':');
	request.send("&operation=selectGroup&groupId="+groupid);
}


function onButtonClick(itemId,itemValue)
{
	var form =  document.getElementById('annotationForm');
	if(form!=null)
	{
		var selectedStaticEntityId = document.getElementById("selectedStaticEntityId");
		if(selectedStaticEntityId!=null)
		{
			selectedStaticEntityId.value = itemId;
		}
		form.action="/catissuecore/BuildDynamicEntity.do";
		form.submit();
	}
}


function displayAnnotationsPage()
{
	var form = document.forms[0];
	if(form!=null)
	{
		var entityRecordID = document.getElementById('id');
		form.action = "LoadAnnotationDataEntryPage.do?entityId=223&entityRecordId=" + entityRecordID.value;
		form.submit();
	}
}

function loadDynamicExtDataEntryPage()
{	
	    var selectBox = document.getElementById('selectedAnnotation');	
		if(selectBox.selectedIndex != "-1")
		{	 
			document.forms[0].action  = "/catissuecore/LoadDynamicExtentionsDataEntryPage.do";
			document.forms[0].submit();
		}
}

function loadDynExtDataEntryPage(event)
{	
	// for IE 
	if(document.all) {
		document.forms[0].selectedAnnotation.value=event.srcElement.name;
	}
	else { //mozilla
		document.forms[0].selectedAnnotation.value=event.target.name;
	}
	
	document.forms[0].action  = "/catissuecore/LoadDynamicExtentionsDataEntryPage.do";
	document.forms[0].submit();
}

function doOnRowSelected(rowID){
 	document.forms[0].selectedAnnotation.value=rowID;	
}

function editAnnotation(rowID){
	var entityId=null;
	var staticEntityId=null;
	var staticEntityRecordId=null;
	var recordId=null;
	var string =rowID.split(":");
	for(var i=0;i<string.length;i++)
	{
		entityId=string[0];
		staticEntityId=string[1];
		staticEntityRecordId=string[2];
		recordId=string[3];
	}
	document.forms[0].action  = "/catissuecore/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="+entityId+"&selectedStaticEntityId="+staticEntityId+"&selectedStaticEntityRecordId="+staticEntityRecordId+"&recordId="+recordId+"&operation=editSelectedAnnotation";
	document.forms[0].submit();
}

function editSelectedAnnotation(parameter)
{	
	var formId=null;
	var staticEntityId=null;
	var staticEntityRecordId=null;
	var string =parameter.split(":");
	for(var i=0;i<string.length;i++)
	{
		var str=string[i].split("=");
		if(str[0]=="formId")
		{
			formId=str[1];
		}
		if(str[0]=="staticEntityId")
		{
			staticEntityId=str[1];
		}
		if(str[0]=="staticEntityRecordId")
		{
			staticEntityRecordId=str[1];
		}
	}
		
	var action="LoadAnnotationDataEntryPage.do?operation=editSelectedAnnotation&selectedFormId="+formId+"&staticEntityId="+staticEntityId+"&staticEntityRecordId="+staticEntityRecordId;
	document.forms[0].action=action;
	document.forms[0].submit();
	
}

function initializeTabs(tabIds, tabNames, tabPageRefs)
{
	if((tabIds!=null)&&(tabNames!=null)&&(tabPageRefs!=null))
	{
		var noOfTabs = tabIds.length;
		for(var i=0;i<noOfTabs;i++)
		{
			tabbar.addTab(tabIds[i],tabNames[i],"");		
			tabbar.setContentHref(tabIds[i],tabPageRefs[i]);			
		}
	}
	tabbar.setTabActive(tabIds[0]);
}

function submitForm()
{

		var form =  document.getElementById('annotationForm');	
	    var selectBox = document.getElementById('optionSelect');	
	    var destination = selectBox.options[selectBox.selectedIndex].value;			
		form.action="/catissuecore/BuildDynamicEntity.do";
		form.submit();
	
}

function viewAnnotations(specimenEntityId,ID,consentTierCounter,staticEntityName,pageOf)
	{
		
		var action="DisplayAnnotationDataEntryPage.do?entityId="+specimenEntityId+"&entityRecordId="+ID+"&pageOf="+pageOf+"&operation=viewAnnotations&consentTierCounter="+consentTierCounter+"&staticEntityName="+staticEntityName;
		document.forms[0].action=action;
		document.forms[0].submit();
	}	   


// DE script end

//DateChange by Offset
function dateChange(newoffset,originaloffset,originalRegistrationDate)
		 {
			var newValueofOffset=newoffset.value;
			var originalOffsetvalue=originaloffset;
			var differenceInoffset=newValueofOffset-originalOffsetvalue;
			//alert(differenceInoffset);
			var newdate=new Date(originalRegistrationDate);
			//alert(newdate);
			newdate.setDate(newdate.getDate()+differenceInoffset);
			//alert(newdate);
			var curr_date = newdate.getDate();
			if(curr_date < 10)
			 {
				var appendzero="0";
				curr_date=appendzero.concat(curr_date);//it returns only 1,2,..and so on.We need to append 0 for proper mm-dd-yyyy format
				//alert(curr_date);
			 }
			var curr_month = newdate.getMonth();
			var curr_year = newdate.getFullYear();
			var month=curr_month + 1;//since january is 0(month starts from 0 and ends at 11)
			if(month < 10)
			 {
				var appendzero="0";
				month=appendzero.concat(month);//it returns only 1,2,..and so on.We need to append 0 for proper mm-dd-yyyy format
				//alert(month);
			 }
			//alert(curr_date + "-" + month + "-" + curr_year);
			var returndate=month + "-" + curr_date + "-" + curr_year;//date in format of mm-dd-yyyy
			//alert(returndate);
			return returndate;
		}

////////Method for setting the Iframe Height dynamically
/////////////////////////
/////////////////////////
var window_Viewport = {
    getWinWidth: function () {
        this.width = 0;
        if (window.innerWidth) 
            this.width = window.innerWidth - 18;
        else if (document.documentElement && document.documentElement.clientWidth) 
            this.width = document.documentElement.clientWidth;
        else if (document.body && document.body.clientWidth) 
            this.width = document.body.clientWidth;
        return this.width;
    },
  
    getWinHeight: function () {
        this.height = 0;
        if (window.innerHeight) 
            this.height = window.innerHeight - 18;
        else if (document.documentElement && document.documentElement.clientHeight) 
            this.height = document.documentElement.clientHeight;
        else if (document.body && document.body.clientHeight) 
            this.height = document.body.clientHeight;
        return this.height;
    },
  
    getScrollX: function () {
        this.scrollX = 0;
        if (typeof window.pageXOffset == "number") 
            this.scrollX = window.pageXOffset;
        else if (document.documentElement && document.documentElement.scrollLeft)
            this.scrollX = document.documentElement.scrollLeft;
        else if (document.body && document.body.scrollLeft) 
            this.scrollX = document.body.scrollLeft; 
        else if (window.scrollX) 
            this.scrollX = window.scrollX;
        return this.scrollX;
    },
    
    getScrollY: function () {
        this.scrollY = 0;    
        if (typeof window.pageYOffset == "number") 
            this.scrollY = window.pageYOffset;
        else if (document.documentElement && document.documentElement.scrollTop)
            this.scrollY = document.documentElement.scrollTop;
        else if (document.body && document.body.scrollTop) 
            this.scrollY = document.body.scrollTop; 
        else if (window.scrollY) 
            this.scrollY = window.scrollY;
        return this.scrollY;
    },
    
    getAll: function () {
        this.getWinWidth(); this.getWinHeight();
        this.getScrollX();  this.getScrollY();
    }
  
}
/////////////////////////
/////////////////////////

function adjFrmHt(frameId, h ,slope) 
{
	if ( document.getElementById && !(document.all) ) {
	var frameObj = document.getElementById(frameId);
	if (frameObj) {
		window_Viewport.getWinHeight();
		frameObj.style.height = "100%";
	}
	}
	else{
	var frameObj = document.getElementById(frameId);
	if (frameObj) {
		window_Viewport.getWinHeight();
		frameObj.style.height = "100%";
	}
	}

}

function setFrameHeight(frameId, h ,slope) {
    if ( document.getElementById && !(document.all) ) {
        var frameObj = document.getElementById(frameId);
        if (frameObj) {
            window_Viewport.getWinHeight();
            frameObj.style.height = Math.round( h * window_Viewport.height )+slope + "px";
			//alert(Math.round( h * dw_Viewport.height ));
        }
    }
	else{
		 var frameObj = document.getElementById(frameId);
        if (frameObj) {
            window_Viewport.getWinHeight();
            frameObj.style.height = Math.round( h * window_Viewport.height )+slope + "px";
        }
	}
}

function setDivWidth()
{
	window_Viewport.getWinWidth();
	//alert(window_Viewport.width);
	  if ( document.getElementById && !(document.all) )
		  {
		document.getElementById('treeboxbox_tree').style.width=window_Viewport.width+15;
		  }
		  else
	{
		document.getElementById('treeboxbox_tree').style.width=window_Viewport.width;
	}
}