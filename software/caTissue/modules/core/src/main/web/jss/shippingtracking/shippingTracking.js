function viewDashboard()
{
		document.forms[0].action="ShowDashboardAction.do";
		document.forms[0].submit();
}
// Global variables.
var ACCEPT = "Accept";
var REJECT_AND_DESTROY = "Reject & Destroy";
var REJECT_AND_RESEND = "Reject & Return";

// Set activityStatus to "Reject & Resend" for selected items.
//changes added to work with Mozilla - bug 12265
function setRejectAndResend(choiceSpecimenContainer) {
	var chkBoxArr;
	if  (choiceSpecimenContainer == 'specimen') {
		chkBoxArr = document.getElementsByName('chkSpecimenId');
	} else {
		chkBoxArr = document.getElementsByName('chkStorgaeContainerId');
	}
	
	//check whether the CheckBox exists or Not
	if(chkBoxArr) {
		var chkLen = chkBoxArr.length;
		if(chkLen) {
			for(i =0; i<chkLen; i++) {
				if(chkBoxArr[i].checked) {
					// change item's activity status = "Reject & Return"
					if  (choiceSpecimenContainer == 'specimen') {
						document.getElementsByName('specimenItem[' + i + '].activityStatus')[0].value = REJECT_AND_RESEND
						
						// Modify storage location to 'Auto', as content should be stored in receiving site.
						modifyStorageLocationAsPerStatus(document.getElementsByName('specimenItem[' + i + '].activityStatus')[0], 
							document.getElementsByName('specimenItem[' + i + '].id')[0].value, 'specimen')
						
					} else {
						document.getElementsByName('containerItem[' + i + '].activityStatus')[0].value = REJECT_AND_RESEND
						
						// Modify storage location to 'Auto', as content should be stored in receiving site.
						modifyStorageLocationAsPerStatus(document.getElementsByName('containerItem[' + i + '].activityStatus')[0], 
							document.getElementsByName('containerItem[' + i + '].id')[0].value, 'container')
					}
				}
			}
		}
	} 
}
//Set activityStatus to "Reject & Destroy" for selected items.
//changes added to work with Mozilla - bug 12265
function setRejectAndDestroy(choiceSpecimenContainer) {
	var chkBoxArr;
	if  (choiceSpecimenContainer == 'specimen') {
		chkBoxArr = document.getElementsByName('chkSpecimenId');
	} else {
		chkBoxArr = document.getElementsByName('chkStorgaeContainerId');
	}

	//check whether the CheckBox exists or Not
	if(chkBoxArr) {
		var chkLen = chkBoxArr.length;
		if(chkLen) {
			for(i =0; i<chkLen; i++) {
				if(chkBoxArr[i].checked) {
					// change item's activity status = "Reject & Destroy"
					if  (choiceSpecimenContainer == 'specimen') {
						document.getElementsByName('specimenItem[' + i + '].activityStatus')[0].value = REJECT_AND_DESTROY
						
						// Modify storage location to 'Virtual'.
						modifyStorageLocationAsPerStatus(document.getElementsByName('specimenItem[' + i + '].activityStatus')[0], 
							document.getElementsByName('specimenItem[' + i + '].id')[0].value, 'specimen')
						
					} else {
						document.getElementsByName('containerItem[' + i + '].activityStatus')[0].value = REJECT_AND_DESTROY
						
						// Modify storage location to 'Site'.
						modifyStorageLocationAsPerStatus(document.getElementsByName('containerItem[' + i + '].activityStatus')[0], 
							document.getElementsByName('containerItem[' + i + '].id')[0].value, 'container')
					}
				}
			}
		}
	} 
}

// Select/Deselect All checkboxes.
function selectAllCheckBox(selectAllChkBoxName,chkBoxNameInEachRow)
{
	var chkBoxObj = document.getElementById(selectAllChkBoxName);
	var chkBoxInEachRow = document.getElementsByName(chkBoxNameInEachRow);
	var i =0;
	var len = chkBoxInEachRow.length;
	if(chkBoxObj)
	{
		//If checkAll checkbox is checked then checked all the checkbox in the List
		if(chkBoxObj.checked == true)
		{
			if(chkBoxInEachRow)
			{
				for(i =0;i<len;i++)
				{
					chkBoxInEachRow[i].checked = true;
				}
			}
		}
		//If checkAll checkbox is Not checked then Unchecked all the checkbox in the List
		else if(chkBoxObj.checked == false)
		{
			if(chkBoxInEachRow)
			{
				for(i =0;i<len;i++)
				{
					chkBoxInEachRow[i].checked = false;
				}
			}
		}
	}
}//End of Function


//To be used on Shipment Receiving page
var winNew = null;
function StorageMapWindowShipReceive(mypage,myname,w,h,scroll,containerNameControlId,selectedContainerNameContValue,contPosition1Value,contPosition2Value)
{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;
			// Sri: Added position one and two as parameters
            // with format positionOne:positionTwo
			mypage=mypage+"1" + 
					"&storageToBeSelected="+ selectedContainerNameContValue +  //parentContainerId.value +
					"&position=" + contPosition1Value +   //positionDimensionOne.value + 
					":" + contPosition2Value;
					
		   var storageContainer = document.getElementById(containerNameControlId).value;
		   mypage+="&storageContainerName="+storageContainer;		
					// alert(mypage);
			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'

			winNew = open(mypage,myname,settings)
			if (winNew.opener == null)
				winNew.opener = self;
}

// While shipment receiving, modify the storage location dropdown as per status.
function modifyStorageLocationAsPerStatus(statusElement, specimenOrContainerId, choiceSpecimenContainer) {
	var storageLocationElement;
	if (choiceSpecimenContainer == 'specimen') {
		storageLocationElement = document.getElementById('specimenStorageLocation_' + specimenOrContainerId);
	} else {
		storageLocationElement = document.getElementById('containerStorageLocation_' + specimenOrContainerId);
	}
	
	if (storageLocationElement != null && statusElement != null) {
		if (statusElement.value == REJECT_AND_RESEND) {
			// If status = reject and return, storage location set to Auto.
			if (choiceSpecimenContainer == 'specimen') {
					storageLocationElement.value = 3;
				} else {
					storageLocationElement.value = 'Manual';
			}
			
			storageLocationElement.onchange();
			storageLocationElement.disabled = true;

		} else if(statusElement.value == REJECT_AND_DESTROY) {
			// If status = reject and destroy, storage location set to Virtual in specimen case or Site in container case.
			if (choiceSpecimenContainer == 'specimen') {
				storageLocationElement.value = 1;
			} else {
				storageLocationElement.value = 'Site';
				
				// Modify site dropdown to save the content on site.
				var siteElement = document.getElementById('siteId_' + specimenOrContainerId);
				if (siteElement != null && siteElement.length > 1) {
					siteElement.value = siteElement.options[1].value
				}
			}
			
			storageLocationElement.onchange();
			storageLocationElement.disabled = true;
		} else {
			storageLocationElement.disabled = false;
		}
	}
}