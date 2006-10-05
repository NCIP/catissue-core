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
		var go = confirm("Are you sure, you want to disable?");
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
		function showStatus(sMsg) 
		{
		    window.status = sMsg ;
		}
		function showTip(objId)
		{

			var obj = document.getElementById(objId);
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
		function hideTip(objId)
		{
			var obj = document.getElementById(objId);
			obj.title = "";
			
			var browser=navigator.appName;
			if(browser=="Microsoft Internet Explorer")
			{
				showStatus(' ');
			}
			
		}	
		
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

	  var distributionForm =  document.forms[0];
	   if (  parseInt(document.forms[0].counter.value) == 0)
	   {
		         
	   			  distributionForm.distributionBasedOn[0].disabled=false
				  distributionForm.distributionBasedOn[1].disabled=false;
				  distributionForm.distributionType[0].disabled = false;
				  distributionForm.distributionType[1].disabled = false;
				  return;
	   } 

	 	if (distributionForm.distributionBasedOn[0].checked == true)
		 {
        	    distributionForm.distributionBasedOn[1].disabled=true
		 }

	 	if (distributionForm.distributionBasedOn[1].checked == true)
		 {
        	    distributionForm.distributionBasedOn[0].disabled=true
		 }

	 	if (distributionForm.distributionType[0].checked == true)
		 {
        	    distributionForm.distributionType[1].disabled=true
		 }

	 	if (distributionForm.distributionType[1].checked == true)
		 {
        	    distributionForm.distributionType[0].disabled=true
		 }
}

function checkDistributionBasedOn() {

	 	if (distributionForm.distributionBasedOn[0].checked == false && distributionForm.distributionBasedOn[1].checked == false) {
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
        var url ='NewMultipleSpecimenAction.do?deriveButtonClicked=true&method=showDerivedSpecimenDialog&specimenAttributeKey=' + key + '&operation=' + operation + '&derivedSpecimenCollectionGroup=' + derivedSpecimenCollectionGroup + '&derivedSpecimenClass=' + derivedSpecimenClass + '&derivedParentSpecimenLabel=' + derivedParentSpecimenLabel + '&derivedParentSpecimenBarcode=' + derivedParentSpecimenBarcode + '&derivedSpecimenType=' + derivedSpecimenType;
	    NewWindow(url,'name','600','600','yes');
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

function setStoragePosition(specimenMapKey,storageId,storageType,xPos,yPos) {
   parent.window.opener.document.applets[0].setStorageDetails(specimenMapKey,storageId,storageType,xPos,yPos);
}

function showStoragePositionMap(specimenAttributeKey,collectionGroup,specimenClass) {
	var  url = "ShowFramedPage.do?pageOf=pageOfMultipleSpecimen";
	url = url + "&amp;specimenAttributeKey=" + specimenAttributeKey;
	url = url + "&amp;SpecimenCollectionGroup=" + collectionGroup;
	url = url + "&amp;specimenClass=" + specimenClass;
	url = url + "&amp;specimenCallBackFunction=" + "setStoragePosition";

    NewWindow(url,'name','810','320','yes');
}

function getSpecimenSubmitResult(target) {
	  document.forms[0].action =    document.forms[0].action + "?method=getResult&multipleSpecimenResult=" + target ;  
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

function submitBioHazards() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitBioHazards";
  form.submit();
}
function deleteExternalIdentifiers()
{
	deleteChecked('addExternalIdentifier','<%=Constants.NEW_MULTIPLE_SPECIMEN_ACTION%>?method=deleteExternalIdentifiers&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false)
}

function submitEvents() 
{
  var form =  document.forms[0];
  form.action =  form.action + "?method=submitEvents";
  form.submit();
}
function deleteBioHazards()
{
	deleteChecked('addBiohazardRow','<%=Constants.NEW_MULTIPLE_SPECIMEN_ACTION%>?method=deleteBioHazards&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
}



/* --- End Multiple Specimen  javascript functions ---*/


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
		//alert(applet);
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

		var confirmChange = confirm("Your entered Array Contents will be lost !! do you really want to continue?");
		if (confirmChange == true)
		{
			
			form.subOperation.value="ChangeEnterSpecimenBy";
			form.action = "SpecimenArray.do?menuSelected=20";
			form.submit();
		}
}
/* --- End Specimen Array javascript functions ---*/
