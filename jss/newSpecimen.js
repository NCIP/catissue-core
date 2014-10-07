//this method is called when aliquot or derivative is selected
function onCheckboxButtonClick(radioButton)
{
		var childSpecimenCount  = document.getElementById("noOfAliquots");
		var deriveSpecimenCount  = document.getElementById("numberOfSpecimens");
		
		var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
		var countForDerive=document.getElementById("derivedDiv");
		var countForAliquot=document.getElementById("aliquotDiv");

		if(radioButton.value==1)
		{
			childSpecimenCount.disabled = true;
			qtyPerAliquotTextBox.disabled = true;
			countForDerive.style.display="none";
			countForAliquot.style.display="none";
		}
		else if(radioButton.value==2)
		{
			childSpecimenCount.disabled = false;
			
			qtyPerAliquotTextBox.disabled = false;
			countForDerive.style.display="none";
			countForAliquot.style.display="block";
			childSpecimenCount.focus();
		}
		else if(radioButton.value==3)
		{
			childSpecimenCount.disabled = true;
			qtyPerAliquotTextBox.disabled = true;
			countForDerive.style.display="block";
			countForAliquot.style.display="none";
			deriveSpecimenCount.focus();
		}
		else
		{
			childSpecimenCount.disabled = true;
			qtyPerAliquotTextBox.disabled = true;
			countForDerive.style.display="none";
			countForAliquot.style.display="none";
		}
}
// Consent Tracking Module Virender mehta
function switchToTab(selectedTab)
{
	var switchImg1=document.getElementById("specimenDetailsTab");
	var switchImg2=document.getElementById("consentViewTab");
	if(selectedTab=="specimenDetailsTab")
	{
		document.getElementById("consentTable").style.display='none';
		document.getElementById("mainTable").style.display='block';
		switchImg1.innerHTML="<img src='images/uIEnhancementImages/tab_specimen_details1.gif' alt='Specimen Details' width='126' height='22' border='0'>";
		switchImg2.innerHTML="<img src='images/uIEnhancementImages/tab_consents2.gif' alt='Consents' width='76' height='22' border='0'>";
	}
	else
	{
		document.getElementById("consentTable").style.display='block';
		document.getElementById("mainTable").style.display='none';
		switchImg2.innerHTML="<img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'>";
		switchImg1.innerHTML="<img src='images/uIEnhancementImages/tab_specimen_details2.gif' alt='Specimen Details' width='126' height='22' border='0'>";
	}
}

function switchToNewTab(selectedTab)
{
	specimenImage=document.getElementById("newSpecimenTab");
	consentImage=document.getElementById("newConsentTab");
	if(selectedTab == "newSpecimenTab")
	{
		document.getElementById("consentTable").style.display='none';
		document.getElementById("mainTable").style.display='block';
		specimenImage.innerHTML="<img src='images/uIEnhancementImages/new_specimen_selected.gif' alt='Specimen Details'  width='115' height='22' border='0'>";
		consentImage.innerHTML="<img src='images/uIEnhancementImages/tab_consents2.gif' alt='Consents' width='76' height='22' border='0'>";
	}
	else
	{
		document.getElementById("consentTable").style.display='block';
		document.getElementById("mainTable").style.display='none';
		specimenImage.innerHTML="<img src='images/uIEnhancementImages/new_specimen_unselected.gif' alt='Specimen Details'  width='115' height='22' border='0'>";
		consentImage.innerHTML="<img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'>";
	}
}

//This function will Switch tab to newSpecimen page
function newspecimenPage()
{
	switchToTab("specimenDetailsTab");
}

//This function will switch page to consentPage
function consentPage(ConsentTierCounter)
{
	checkForConsents(ConsentTierCounter);
}

function newSpecimenTab()
{
	switchToNewTab("newSpecimenTab");
}

function showConsents(tab,ConsentTierCounter)
{
	var showConsents = tab;
	if(showConsents=="null" || showConsents=="specimen" || showConsents=="newSpecimenForm")
	{
		newspecimenPage();
	}
	else
	{
		consentPage(ConsentTierCounter);
	}
}

function setContainerValues(containerName,pos1,pos2)
{
	document.getElementById(containerDropDownInfo['dropDownId']).value=containerName;
	document.getElementById("selectedContainerName").value=containerName;
	document.getElementById("pos1").value=pos1;
	document.getElementById("pos2").value=pos2;
}

function showConsent(tabValue,ConsentTierCounter)
{
	var tab=tabValue;
	if(tab == null)
		 switchToTab("specimenDetailsTab");
	else
	{
	  if(tab=="consent")
		 consentTab(ConsentTierCounter);
	  else
		switchToTab("specimenDetailsTab");
	}
}
 
function eventClicked(pageOf)
{
	// Clear the value of onSubmit 
	document.forms[0].onSubmit.value="";
	var consentTier=document.forms[0].consentTierCounter.value;
	//var answer = confirm("Do you want to submit any changes?");
	var formName;
	var formNameAction = null;
	
//	<% String formNameAction = null;%>
		var id = document.forms[0].id.value;
		var label = document.getElementById("label").value;
		formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
		if(pageOf== "pageOfNewSpecimenCPQuery")
		{
			formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
		}
		formName = formNameAction+"&specimenId="+id+"&menuSelected=15&consentTierCounter="+consentTier;
	//}
	confirmDisable(formName,document.forms[0].activityStatus);
}

function setSize()
{
	var tempWidth =document.body.clientWidth;
}

function updateStorageContainerValue()
{
	var containerElement=document.getElementById(containerDropDownInfo['dropDownId']);
	if(containerElement!=null)
	{
		var containerName=containerElement.value;
		document.getElementById("selectedContainerName").value=containerName;
		if("Virtual"==containerName)
		{
			document.getElementById("virtuallyLocated").value="true";
		}
		else
		{
			document.getElementById("virtuallyLocated").value="false";
		}
	}
}

function setSubmitted(forwardTo,printaction,nextforwardTo)
{
	var printFlag = document.getElementById("printCheckbox");
	if(printFlag.checked)
	{
	  setSubmittedForPrint(forwardTo,printaction,nextforwardTo);
	}
	else
	{
	  setSubmittedFor(forwardTo,nextforwardTo);
	}
}

function consentTab(ConsentTierCounter)
{
	   	if(ConsentTierCounter>0)
		{
			switchToTab("consentTab");
		}
		else
		{
				alert("No consents available for selected Specimen");
		}
}

function checkForConsents(ConsentTierCounter)
{
		if(ConsentTierCounter>0)
		{
				switchToTab("consentTab");
		}
		else
		{
			alert("No consents available for selected Specimen Collection Group");
		}
}

function getDocumentElementForXML(xmlString)
{
	var document = null;
	if (window.ActiveXObject) // code for IE
	{
				document = new ActiveXObject("Microsoft.XMLDOM");
				document.async="false";
				document.loadXML(xmlString);
	}
	else // code for Mozilla, Firefox, Opera, etc.
	{
				var parser = new DOMParser();
				document = parser.parseFromString(xmlString,"text/xml");
	}
   return document;
}

function loadContainerValues(isVirtuallyLocatedValue)
{
	if(!isVirtuallyLocatedValue)
		  doOnLoad();
}

// funtion from NewSpecimenPageButtonJSP page
function doInitGrid()
{
	grid = new dhtmlXGridObject('mygrid_container');
	grid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
 	grid.setHeader("My Specimen Lists");
 	grid.setInitWidths("175");
 	grid.setColAlign("left");
 	grid.setSkin("dhx_skyblue");
 	grid.setEditable(false);
   	grid.attachEvent("onRowSelect", doOnRowSelected);
 	grid.init();
    grid .load ("TagGridInItAction.do");
}

function doOnRowSelected(rId)
{
	submitTagName(rId);	 
}

function doInitParseTree()
{
	popupmygrid.loadXML("TreeTagAction.do?entityTag=SpecimenListTag");
}

function giveCall(url,msg,msg1,id)
{
	document.getElementById('objCheckbox').checked=true;
	document.getElementById('objCheckbox').value=id;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}


function LoadSCGTabBar(oprationValue)
{
	if(oprationValue== "edit")
		loadSpecimenTabbarForEdit();
	else
		loadSpecimenTabbarForAdd();
}

function loadSpecimenTabbarForEdit()
{
	
	specimenTabbar = new dhtmlXTabBar("specimen_tabbar", "top",25);
	specimenTabbar.setSkin('default');
	specimenTabbar.setImagePath("dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
	specimenTabbar.setSkinColors("#FFFFFF", "#FFFFFF");
	
	specimenTabbar.addTab("specimenDetailsTab",'<span style="font-size:13px"> Specimen Details </span>', "150px");
        specimenTabbar.addTab("deSpecimenEventsTab",'<span style="font-size:13px"> Specimen Events </span>',"150px");

	if(reportId != null && reportId != -1  && reportId != -2)
	{
		specimenTabbar.addTab("reportsTab",'<span style="font-size:13px"> View Report(s)</span>',"150px");
	}
	specimenTabbar.addTab("annotationTab",'<span style="font-size:13px">View Annotation </span>',"150px");
	/*if(hasConsents){
	specimenTabbar.addTab("consentsTab",'<span style="font-size:13px">Consents </span>',"150px");
	}*/
	if(isImageEnabled)
	{
		specimenTabbar.addTab("imagesTab",'<span style="font-size:13px">Images </span>',"150px");
	}
	
	specimenTabbar.setHrefMode("iframes-on-demand");
	specimenTabbar.setContent("specimenDetailsTab", "specimenDetailsDiv");
	//specimenTabbar.setContentHref("eventsTab", showEventsTab);
	if(reportId != null && reportId != -1  && reportId != -2)
	{
		specimenTabbar.setContentHref("reportsTab", showViewSPRTab);  
	}

	specimenTabbar.setContentHref("annotationTab", showAnnotationTab);
	specimenTabbar.setContentHref("deSpecimenEventsTab", showDESpecimenEventsTab);

/*if(hasConsents){	
	specimenTabbar.setContentHref("consentsTab", showConsentsTab);  
	}*/
	if(isImageEnabled)
	{
		specimenTabbar.setContentHref("imagesTab", showImagesTab);
	}
	specimenTabbar.setTabActive("specimenDetailsTab");
}

function loadSpecimenTabbarForAdd()
{
	specimenTabbar = new dhtmlXTabBar("specimen_tabbar", "top",25);
	specimenTabbar.setSkin('default');
	specimenTabbar.setImagePath("dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
	specimenTabbar.setSkinColors("#FFFFFF", "#FFFFFF");
	specimenTabbar.addTab("specimenDetailsTab",'<span style="font-size:13px"> New Specimen </span>', "150px");
	specimenTabbar.setHrefMode("iframes-on-demand");
	specimenTabbar.setContent("specimenDetailsTab", "specimenDetailsDiv");
	specimenTabbar.setTabActive("specimenDetailsTab");
}

