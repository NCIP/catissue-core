var formTitleStyle = "font-family:arial,helvetica,verdana,sans-serif;  font-size:0.6em;  font-weight:bold;  padding-left:0.8em;  background-color:#5C5C5C;  color:#FFFFFF;   border-top:1px solid #5C5C5C;   border-left:1px solid #5C5C5C;  border-right:1px solid #5C5C5C; "
var formSubTableTitleStyle = "font-family:arial,helvetica,verdana,sans-serif; font-size:0.7em;  font-weight:bold;  background-color:#CCCCCC;  color:#000000;   border-bottom:1px solid #5C5C5C;   border-left:1px solid #5C5C5C;   border-right:1px solid #5C5C5C;   text-align:left;";

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
		var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
		if (go==true)
		{	document.forms[0].action = action;
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

function  deleteChecked(subdivtag,action,countElement,checkName,isOuterTable)
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
	
	if(status){
		/** set action when checkbox is clicked **/
		document.forms[0].action = action;
		document.forms[0].submit();
	}
		
		
}
		
	//Mandar: 24-Apr-06 for tooltip
		// -------
		var timeInterval=100;
	var interval = self.setInterval("setTip()",timeInterval);
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
		function hideTip(objId)
		{
			var obj = document.getElementById(objId);
			obj.title = "";
			
			var browser=navigator.appName;
			if(browser=="Microsoft Internet Explorer")
			{
				showStatus(' ');
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
						
					obj.title = tip;

					var browser=navigator.appName;
					if(browser=="Microsoft Internet Explorer")
					{
						showStatus(tip);
					}
				}
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
		 	 alert("Please select 'distribution based on'");
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
	NewWindow('ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName='+pName+'&cdeName=Tissue Site','name','375','330','yes');
}
//to delete since locations will be auto allocated.
function setStoragePosition(specimenMapKey,storageId,storageType,xPos,yPos) {
   parent.window.opener.document.applets[0].setStorageDetails(specimenMapKey,storageId,storageType,xPos,yPos);
}

function setCaptionInMapFromJS(specimenMapKey) {
  parent.window.opener.document.applets[0].setButtonCaption(specimenMapKey);
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
function deleteExternalIdentifiers()
{
    deleteChecked('addExternalIdentifier','NewMultipleSpecimenAction.do?method=deleteExternalIdentifiers&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false)
}

function submitEvents() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitEvents&operation=addMultipleSpecimen";
  form.submit();
}
function deleteBioHazards()
{
	deleteChecked('addBiohazardRow','NewMultipleSpecimenAction.do?method=deleteBioHazards&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
}



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
    		var cpId = window.parent.frames[cpAndParticipantsFrame].document.getElementById("cpId").value;
			var participantId = window.parent.frames[cpAndParticipantsFrame].document.getElementById("participantId").value;
			window.parent.frames[treeFrame].location="showTree.do?"+cpSearchCpId+"="+cpId+"&"+cpSearchParticipantId+"="+participantId+"&nodeId="+nodeId;
   		
    	}

//Added by Preeti for DE Integration

function initializeGridForGroups(groupsXML)
{
	//alert('ho2:'+groupsXML);
	gridForGroups= new dhtmlXGridObject('divForGroups');
	gridForGroups.setImagePath("dhtml_comp/imgs/");
	gridForGroups.enableAutoHeigth(true);
	gridForGroups.setHeader("Group");
	gridForGroups.setInitWidthsP("100");
	gridForGroups.setColAlign("left");
	gridForGroups.setColTypes("ro");
	//gridForGroups.enableMultiselect(true);
	gridForGroups.setOnRowSelectHandler(groupSelected);
	gridForGroups.init();
	//gridForGroups.setStyle(formTitleStyle);
	gridForGroups.loadXMLString(groupsXML);
	if(gridForGroups.getRowsNum()>0)
	{
		gridForGroups.selectRow(0,true,false);	
	}
}


function initializeGridForSelectedEntities(groupsXML)
{
//	alert('ho1:'+groupsXML);
	gridForEntities= new dhtmlXGridObject('gridForEnities');
	gridForEntities.setImagePath("dhtml_comp/imgs/");
	gridForEntities.enableAutoHeigth(true);
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
	gridForEntities.enableAutoHeigth(true);
	gridForEntities.setHeader("Form Title,Entity,Date,Created By,Conditions");
	gridForEntities.setInitWidthsP("27,18,15,18,22");
	gridForEntities.setColAlign("left,left,left,left,left")
	gridForEntities.setColTypes("link,ro,ro,ro,link");
	gridForEntities.setColSorting("str,str,date,str,str");
	gridForEntities.init();
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
	request.send("&operation=selectGroup&groupId="+groupid);
}
function groupChangedResponse(entitiesXML)
{
	gridForEntities.clearAll(false);
	if(entitiesXML!=null)
	{
		gridForEntities.loadXMLString(entitiesXML);
	}	
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

function initAnnotationGrid()
{
	annotationsGrid = new dhtmlXGridObject('definedAnnotationsGrid');
	annotationsGrid.setImagePath("dhtml_comp/imgs/");
	annotationsGrid.setHeader("#,Annotation,Last Updated,Updated By");
	annotationsGrid.setInitWidthsP("5,32,31,32")
	annotationsGrid.setColAlign("center,left,left,left,left")
	annotationsGrid.setColTypes("ch,link,ro,ro");
	annotationsGrid.init();
	var annotationXMLFld = document.getElementById('definedAnnotationsDataXML');
	annotationsGrid.loadXMLString(annotationXMLFld.value);
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
//function initializeTabsForOrderingSystem(tabIds, tabNames, tabPageRefs)
//{
//	if((tabIds!=null)&&(tabNames!=null)&&(tabPageRefs!=null))
//	{
//		var noOfTabs = tabIds.length;
//		for(var i=0;i<noOfTabs;i++)
//		{
//			tabbar.addTab(tabIds[i],tabNames[i],"");		
			//tabbar.setContentHref(tabIds[i],tabPageRefs[i]);	
//			tabbar.setOnSelectHandler(addToOrderList);
//		}
//	}
//	tabbar.setTabActive(tabIds[0]);
			
//}
//function submitForm(tabId,action)
//{
//tabId="shoppingCartForm";
//for(i=0;i<document.elements.length;i++)
//	alert(window.frames[0].document.forms[0].action);
	//document.getElementsByTagName("form").length

//	window.frames[1].document.forms[0].action="/ShoppingCart.do?tabIndex=3";		
//	window.frames[1].document.forms[0].submit(); 
//}

//<!-- JavaScript for Participant.jsp  start -->
function textLimit(field) 
{
	if(field.value.length>0) 
		field.value = field.value.replace(/[^\d]+/g, '');
		
	/*if (element.value.length > maxlen + 1)
		alert('your input has been truncated!');*/
	/*if (field.value.length > maxlen)
	{
		//field.value = field.value.substring(0, maxlen);
		field.value = field.value.replace(/[^\d]+/g, '');
	}*/
}
function intOnly(field) 
{
	if(field.value.length>0) 
	{
		field.value = field.value.replace(/[^\d]+/g, ''); 
	}
}
//this function is called when participant clicks on radiao button 
function onParticipantClick(participant_id)
{
	//mandar for grid
	var cl = mygrid.cells(participant_id,mygrid.getColumnCount()-1);
	var pid = cl.getValue();
	//alert(pid);
	//participant_id = pid;
	//------------
	//document.forms[0].participantId.value=participant_id;
	document.forms[0].participantId.value=pid;
	document.forms[0].id.value=pid;
	document.forms[0].submitPage.disabled=true;
	document.forms[0].registratioPage.disabled=false;


}
//This function is called when user clicks on 'Use Selected Participant' Button
function UseSelectedParticipant()
{

	if(document.forms[0].participantId.value=="" || document.forms[0].participantId.value=="0")
	{
		alert("Please select the Participant from the list");
	}
	else
	{
	
		document.forms[0].action="ParticipantSelect.do?operation=add&id="+document.forms[0].participantId.value;
		alert(document.forms[0].action);
		document.forms[0].submit();
		//window.location.href="ParticipantSelect.do?operation=add&participantId="+document.forms[0].participantId.value+"&submittedFor="+document.forms[0].submittedFor.value+"&forwardTo="+document.forms[0].forwardTo.value;
	}
	
}

function onVitalStatusRadioButtonClick(element)
{
	if(element.value == "Dead")
	{
		document.forms[0].deathDate.disabled = false;				
	}
	else
	{
		document.forms[0].deathDate.disabled = true;
	}
}
function LookupAgain(submittedFor)
{
	
	document.forms[0].submitPage.disabled=false;
	document.forms[0].registratioPage.disabled=true;
	if(submittedFor!=null && submittedFor=="AddNew")
	{
		document.forms[0].submitPage.disabled=true;
		document.forms[0].registratioPage.disabled=false;
	}
	document.forms[0].radioValue.value="Lookup";
}

//<!-- JavaScript for Participant.jsp  end -->

//<!-- JavaScript for ViewSurigicalPathologyReport.jsp  start -->

	
//<!-- function collapse or expand the participant information table   -->
function switchStyle(action)
{
	imageObj = document.getElementById('image');	
	if(action== 'hide') //Clicked on - image
	{
		hide('paricipantInformation');				
		imageObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" /> ';
		imageObj.href="javascript:switchStyle('show');";
	}
	else  							   //Clicked on + image
	{
		show('paricipantInformation');
		imageObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" /> ';
		imageObj.href="javascript:switchStyle('hide');";
	}
}
//<!--function to show object on UI -->
function show(obj)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display = '';
}
//<!--function to hide object from UI -->
function hide(obj)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display='none'
}
//<!--function to display deidentified report table on UI -->
function clickOnLinkShowDeidReport()
{

	if(document.getElementById('showDeReportChkbox').checked==true)
	{
		hide('identifiedReportInfo');
		switchStyle('hide');
		show('deidReportInfo');

	}
	else
	{
		hide('deidReportInfo');
		show('identifiedReportInfo');
		switchStyle('show');
	}
}
//<!--function to display default view  -->
function clickOnLinkReport()
{
	hide('deidReportInfo');
	show('reportTable');
	show('categoryHighlighter');
	show('identifiedReportInfo');
	switchStyle('show');
}
//<!--function to display identified and de-identified report compare view  -->
function clickOnLinkCompareReport()
{
	switchStyle('hide')
	show('reportTable');
	show('identifiedReportInfo');
	show('deidReportInfo');
	show('categoryHighlighter');
}
//<!--function to display user requests table  -->
function clickOnLinkMyRequests()
{
	
}
//<!--function to display dialog box for confirmation of submitting comments-->
function confirmSubmit()
{
	if (confirm('Are you sure you want to Submit Comments?'))
  {
    return true;
  }
  else
  {
    return false;
  }
}
//<!--function to submit rview comments-->
function submitReviewComments(consentTierCounter)
{
	
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='review';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=add&consentTierCounter="+consentTierCounter;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
//<!--function to submit quarantine comments-->
function submitQuarantineComments(consentTierCounter)
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=add&consentTierCounter="+consentTierCounter;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
//<!-- JavaScript for ViewSurigicalPathologyReport.jsp  end -->

//Java Script for ParticipantTabPage.jsp
function initiallizeAddParticipantTabs(contextPath,queryString)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath(contextPath + "/dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["addTab"];
		var tabNames = ["Add Participant"];
		var tabHREFs = [contextPath + "/Participant.do?"+queryString];
		initializeTabs(tabIds,tabNames,tabHREFs);
}

function initiallizeEditParticipantTabs(contextPath,queryString,annotationQueryString)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath("dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["editTab","viewSPRTab","annotationsTab"];
		var tabNames = ["Edit Participant" , "View Surgical Pathology Report", "Annotations"];
		var tabHREFs = [contextPath + "/ParticipantSearch.do?" + queryString , contextPath + "/ViewSurgicalPathologyReport.do?operation=viewSPR&" + queryString ,contextPath + "/LoadAnnotationDataEntryPage.do"+annotationQueryString];
		initializeTabs(tabIds,tabNames,tabHREFs);
}

//Java Script for SpecimenCollGroupTabPage.jsp
function initiallizeAddSCGTabs(contextPath, str)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath(contextPath + "/dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["addTab"];
		var tabNames = ["Add Specimen Collection Group"];
		var tabHREFs = [contextPath + "/SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup&menuSelected=14"+str];
		initializeTabs(tabIds,tabNames,tabHREFs);
}

function initiallizeEditSCGTabs(contextPath,queryString,annotationQueryString)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath("dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["editTab","viewSPRTab","annotationsTab"];
		var tabNames = ["Edit Specimen Collection Group" , "View Surgical Pathology Report", "Annotations"];
		var tabHREFs = [contextPath + "/SpecimenCollectionGroupSearch.do?" + queryString , contextPath + "/ViewSurgicalPathologyReport.do?operation=viewSPR&" + queryString ,contextPath + "/LoadAnnotationDataEntryPage.do"+annotationQueryString];
		initializeTabs(tabIds,tabNames,tabHREFs);
}



//Javav Script for Collection Protocol Registration 
function initiallizeAddCpRegTabs(contextPath)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath(contextPath + "/dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["addTab"];
		var tabNames = ["Collection Protocol Registration"];
		var tabHREFs = [contextPath + "/CollectionProtocolRegistration.do?operation=add&pageOf=pageOfCollectionProtocolRegistration&menuSelected=13"];
		initializeTabs(tabIds,tabNames,tabHREFs);
}

function initiallizeEditCpRegTabs(contextPath,queryString,annotationQueryString)
{
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath("dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["editTab","annotationsTab"];
		var tabNames = ["Edit Partitipant Registration" , "Annotations"];
		var tabHREFs = [contextPath + "/CollectionProtocolRegistrationSearch.do?" + queryString , contextPath + "/LoadAnnotationDataEntryPage.do"+annotationQueryString];
		initializeTabs(tabIds,tabNames,tabHREFs);
}


function submitForm()
{

		var form =  document.getElementById('annotationForm');	
	    var selectBox = document.getElementById('optionSelect');	
	    var destination = selectBox.options[selectBox.selectedIndex].value;			
		if(destination != "-1")
		{
			form.action="/catissuecore/BuildDynamicEntity.do";
			form.submit();
		}
}

function viewAnnotations(specimenEntityId,ID,consentTierCounter,staticEntityName)
	{
		
		var action="DisplayAnnotationDataEntryPage.do?entityId="+specimenEntityId+"&entityRecordId="+ID+"&pageOf=pageOfNewSpecimen&operation=viewAnnotations&consentTierCounter="+consentTierCounter+"&staticEntityName="+staticEntityName;
		document.forms[0].action=action;
		document.forms[0].submit();
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
		
