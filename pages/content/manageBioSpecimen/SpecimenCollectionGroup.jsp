<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.CDMSIntegrationConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/calendarComponent.js" type="text/javascript"></script>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<%
 	String operation = (String)request.getAttribute(Constants.OPERATION);
	String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String signedConsentDate = "";
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	String warning_unsaved_data = ApplicationProperties.getValue("unsaved.data.warning");
	String clinportalUrl =(String) request.getAttribute(CDMSIntegrationConstants.CALLBACK_URL);
	boolean isAddNew = false;
	Long scgEntityId = null;
	scgEntityId = (Long)request.getAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
	String participantId=null;
	Object obj = request.getAttribute("specimenCollectionGroupForm");
	SpecimenCollectionGroupForm form =null;
	if(obj != null && obj instanceof SpecimenCollectionGroupForm)
	{
 		form=(SpecimenCollectionGroupForm)obj;
 		participantId=""+form.getParticipantId();
	}
	String id = request.getParameter("id");
	String appendingPath = "/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
	if (reqPath != null)
 	appendingPath = reqPath + "|/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
 	if(form  != null)
 	{
 		if(id==null)
 		{
 			id=String.valueOf(form.getId());
 		}
 	}
	String nodeId="";
	String formName, pageView = operation ,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=false;
	if(!operation.equals("add") )
	{
		if(form != null)
		{
			appendingPath = "/SpecimenCollectionGroupSearch.do?operation=search&pageOf="+pageOf+"&id="+form.getId() ;
					int radioButtonForParticipant1 = form.getRadioButtonForParticipant();
			nodeId= "SpecimenCollectionGroup_"+form.getId();

		}

 	   	}
 		if(operation.equals(Constants.EDIT)|| operation.equals("viewAnnotations"))
 		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION +"?";
			readOnlyValue=true;
			if(pageOf.equals(Constants.QUERY))
				formName = Constants.QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
			if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
			{
				formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
			}
 		}
 		else
 		{
			formName = Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION;
			if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
			{
				formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION + "?pageOf="+pageOf;
			}
 			readOnlyValue=false;
 		}
 		long idToTree =0;
 		if(form!=null)
 		{
 			idToTree = form.getId();
 		}
 		String currentReceivedDate = "";
 		String currentCollectionDate = "";
 		if (form != null)
 		{
		currentReceivedDate = form.getReceivedEventDateOfEvent();
		if(currentReceivedDate == null)
				currentReceivedDate = "";
		currentCollectionDate = form.getCollectionEventdateOfEvent();
		if(currentCollectionDate == null)
 			currentCollectionDate = "";
 		}

 		String formNameForCal = "specimenCollectionGroupForm";
 		String numberOfSpecimenCollection = (String)request.getAttribute(Constants.NUMBER_OF_SPECIMEN_REQUIREMENTS);
 		if(numberOfSpecimenCollection == null)
 		{
 	numberOfSpecimenCollection = "0";
 		}
 %>
<head>

	<%
		String refreshTree = (String)request.getAttribute("refresh");
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
		if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY) && (refreshTree==null || !(refreshTree.equalsIgnoreCase("false"))))
		{
	%>
		<script language="javascript">
		//Added by Falguni to refresh participant tree
		var nodeid =  "<%=nodeId%>";
	//	top.frames["cpAndParticipantView"].editParticipant(<%=participantId%>,nodeid);
		if(nodeid!=""&&nodeid!="1")
		{
	//	alert("nodeid: "+nodeid);
		refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');
		}
		</script>
	<%
		}
	%>

	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}
	</script>
	<script language="JavaScript">
	function makeClinPortalUrl()
	{
		var submitButton = document.getElementById("submitOnly");
		submitButton.disabled = false;
		document.getElementById('isClinicalDataEntry').value="true";
		var scgId=document.getElementById("id").value;
		var url='<%=clinportalUrl%>'+"&scgId="+scgId;
		if (confirm("<%=warning_unsaved_data%>"))
		{
			document.getElementById('clinicalDataEntryURL').value=url;
			if (window.ActiveXObject)
			{
				submitButton.click();
			}
			else
			{
				submitButton.enable();
				if(submitButton.enable())
				{
					submitButton.click();
				}
				else
				{
					makeClinPortalUrl();
				}
			}
		}
		else
		{
			var clinportalPath = url.split("?");
			var clinportalPath1 = clinportalPath[0];
			var clinportalPath2 = clinportalPath[1];
			var request = newXMLHTTPReq();
			request.onreadystatechange = getReadyStateHandler(request,openClinportalPage,true);
			request.open("POST","AjaxAction.do?method=encryptData",true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			var dataToSend = clinportalPath2;
			request.send(dataToSend);
		}
	}
	function logout()
	{
		var request = newXMLHTTPReq();
		request.onreadystatechange = getReadyStateHandler(request,"",true);
		request.open("POST","AjaxAction.do?method=logout",true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var dataToSend = "";
		request.send(dataToSend);
	}

	function openClinportalPage(dataString)
	{
		var clinportalUrl='<%=clinportalUrl%>';
		var clinportalUrlPath = clinportalUrl.split("?");
		var clinportalUrlPath1 = clinportalUrlPath[0];
		logout();
		window.top.location=clinportalUrlPath1 + "?method=login&path=" + dataString;
	}
	function showAnnotations()
	{
		var action="DisplayAnnotationDataEntryPage.do?entityId=<%=scgEntityId%>&entityRecordId=<%=id%>&staticEntityName=<%=staticEntityName%>&pageOf=<%=pageOf%>&operation=viewAnnotations";
		document.forms[0].action=action;
		document.forms[0].submit();
	}
	function onRadioButtonClick(element)
	{
		if(element.value == 1)
		{
			document.forms[0].participantId.disabled = false;
			document.forms[0].protocolParticipantIdentifier.disabled = true;
			document.forms[0].participantsMedicalIdentifierId.disabled = false;
		}
		else
		{
			document.forms[0].participantId.disabled = true;
			document.forms[0].protocolParticipantIdentifier.disabled = false;
			//disable Medical Record number field.
			document.forms[0].participantsMedicalIdentifierId.disabled = true;
		}
	}
	function onChangeEvent(element)
	{
		var getCPID=document.getElementById('collectionProtocolId');
		var cpID=getCPID.value;
		var getID=document.getElementById(element);
		var index=getID.selectedIndex;
		if(index<0)
		{
			alert("Please Select Valid Value");
		}
		else
		{
			if(element=='collectionProtocolEventId')
			{
				var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&protocolEventId=true&showConsents=yes&pageOf=pageOfSpecimenCollectionGroup&" +
					"isOnChange=true&cpID="+cpID;
			}
			else
			{
				var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&protocolEventId=false&showConsents=yes&pageOf=pageOfSpecimenCollectionGroup&" +
					"isOnChange=true&cpID="+cpID;

			}
			changeAction(action);
		}
	}
	function onChange(element)
	{
		var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&pageOf=pageOfSpecimenCollectionGroup&" +
				"isOnChange=true";
		changeAction(action);
	}
	function changeAction(action)
	{
		document.forms[0].action = action;
		document.forms[0].submit();
	}
		var applyToSpecimen;
		function checkForChanges()
		{
			var collectionEventdateOfEvent = document.getElementById("collectionEventdateOfEvent").value;
			var collectionEventUserId = document.getElementById("collectionEventUserId").value;
			var collectionEventTimeInHours = document.getElementById("displaycollectionEventTimeInHours").value;
			var collectionEventTimeInMinutes = document.getElementById("displaycollectionEventTimeInMinutes").value;
			var collectionEventCollectionProcedure = document.getElementById("collectionEventCollectionProcedure").value;
		    var collectionEventContainer = document.getElementById("collectionEventContainer").value;
		    var collectionEventComments = document.getElementById("collectionEventComments").value;

			var receivedEventdateOfEvent;
			var currentReceivedDateForm;
			var recDate = document.getElementById("receivedEventdateOfEvent");
			if(recDate != null)
			{
				receivedEventdateOfEvent = recDate.value;
				 currentReceivedDateForm = document.getElementById("currentReceivedDateForm").value;
			}
			var receivedEventUserId = document.getElementById("receivedEventUserId").value;
			var receivedEventTimeInHours = document.getElementById("displayreceivedEventTimeInHours").value;
			var receivedEventTimeInMinutes = document.getElementById("displayreceivedEventTimeInMinutes").value;
			var receivedEventReceivedQuality = document.getElementById("receivedEventReceivedQuality").value;
			var receivedEventComments = document.getElementById("receivedEventComments").value;

			//Values from form
			var collectionEventdateOfEventForm = document.getElementById("collectionEventdateOfEventForm").value;
			var collectionEventUserIdForm = document.getElementById("collectionEventUserIdForm").value;
			var collectionEventTimeInHoursForm = document.getElementById("collectionEventTimeInHoursForm").value;
			var collectionEventTimeInMinutesForm = document.getElementById("collectionEventTimeInMinutesForm").value;
			var collectionEventCollectionProcedureForm = document.getElementById("collectionEventCollectionProcedureForm").value;
			var collectionEventContainerForm = document.getElementById("collectionEventContainerForm").value;
			var collectionEventCommentsForm = document.getElementById("collectionEventCommentsForm").value;

			var receivedEventUserIdForm = document.getElementById("receivedEventUserIdForm").value;

			var receivedEventTimeInHoursForm = document.getElementById("receivedEventTimeInHoursForm").value;
			var receivedEventTimeInMinutesForm = document.getElementById("receivedEventTimeInMinutesForm").value;
			var receivedEventReceivedQualityForm = document.getElementById("receivedEventReceivedQualityForm").value;
			var receivedEventCommentsForm = document.getElementById("receivedEventCommentsForm").value;

			if(collectionEventUserId == "")
			  collectionEventUserId = "0";

			if(receivedEventUserId == "")
			   receivedEventUserId = "0";

			if((collectionEventdateOfEvent != collectionEventdateOfEventForm)
				|| (collectionEventUserId != collectionEventUserIdForm)
				|| (collectionEventTimeInHours != collectionEventTimeInHoursForm)
				|| (collectionEventTimeInMinutes != collectionEventTimeInMinutesForm)
				|| (collectionEventCollectionProcedure != collectionEventCollectionProcedureForm)
				|| (collectionEventContainer != collectionEventContainerForm)
				|| (receivedEventUserId != receivedEventUserIdForm)
				|| (receivedEventdateOfEvent != currentReceivedDateForm)
				|| (receivedEventTimeInHours != receivedEventTimeInHoursForm)
				|| (receivedEventTimeInMinutes != receivedEventTimeInMinutesForm)
				|| (receivedEventReceivedQuality != receivedEventReceivedQualityForm)
				|| (collectionEventComments != collectionEventCommentsForm)
				|| (receivedEventComments != receivedEventCommentsForm))
			{
				var appResources = "The collected and received events data that you have entered will be propagated to all specimens under this Specimen Collection Group and override any existing data. Do you want to continue?";
				var answer = confirm(appResources);
				if(answer)
				{
					applyToSpecimen = 'true';
				}
				else
				{
					applyToSpecimen = 'false';
				}
			}
		}
		function confirmDisableForSCG(action,formField)
		{
			var temp = action+"&applyToSpecimenValue="+applyToSpecimen;
			if((formField != undefined) && (formField.value == "Disabled"))
			{
				var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
				if (go==true)
				{
					if(document.forms[0].nextForwardTo.value!=null)
					{
					 temp = temp + "&domainObject=SCG&nextForwardTo="+document.forms[0].nextForwardTo.value;
					}
				    document.forms[0].action = temp;
					document.forms[0].submit();
				}
			}
			else
			{
				document.forms[0].action = temp;
				document.forms[0].submit();
			}
		}

		function disablebuttons()
		{
			var enteredValue = document.getElementById("numberOfSpecimen").value;
			var submitButton = document.getElementById("submitOnly");
			var submitAndAddButton = document.getElementById("submitAndAdd");

			// Patch ID: Bug#4245_4
			// Description: User is allowed to click the Add Multiple Specimen irrespective of state of restric checkbox.
			// Patch ID: Bug#3184_34
			var submitAndAddMultipleButton =  document.getElementById("submitAndAddMultiple");
			var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
			if(enteredValue > 1)
			{
				submitButton.disabled = true;
				submitAndAddButton.disabled = true;
				submitAndAddMultipleButton.disabled = false;
			}
			else if(restrictCheckbox.checked)
			{
				submitButton.disabled = true;
				submitAndAddButton.disabled = false;
				submitAndAddMultipleButton.disabled = false;
			}
			else
			{
				submitButton.disabled = true;
				submitAndAddButton.disabled = false;
				submitAndAddMultipleButton.disabled = false;
			}
		}
		function disableButtonsOnCheck(restrictCheckbox)
		{
			var submitButton = document.getElementById("submitOnly");
			var addSpecimenButton = document.getElementById("submitAndAdd");
			// Patch ID: Bug#3184_35
			var submitAndAddMultipleButton = document.getElementById("submitAndAddMultiple");
			if(restrictCheckbox.checked)
			{
				submitButton.disabled = false;
				addSpecimenButton.disabled = true;
				submitAndAddMultipleButton.disabled = true;
			}
			else
			{
				disablebuttons();
				submitButton.disabled = true;
			}
		}

		function initializeSCGForm()
		{
			<%if(form!=null)
			{%>
			var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
			//bug id: 4333
			var valueForCheckbox = '<%=form.getRestrictSCGCheckbox()%>';
			if(valueForCheckbox!=null && valueForCheckbox == 'true')
			{
				disableButtonsOnCheck(restrictCheckbox);
			}
			<%}%>
		}

		function setButtonType(addButton)
		{
			document.getElementById("buttonType").value = addButton.id;
		}

	function switchToTab(selectedTab)
	{
		var displayKey="block";
		var showAlways="block";
		if(!document.all)
		{
			displayKey="table";
			showAlways="table";
		}

		var displayTable=displayKey;
		var tabSelected="none";
		if(selectedTab=="specimenCollectionGroupTab")
		{
			tabSelected=displayKey;
			displayTable="none";
		}

		var display=document.getElementById('collectionEvent');
		display.style.display=tabSelected;

		var display=document.getElementById('scgTable');
		display.style.display=tabSelected;

		var display=document.getElementById('multiplespecimenTable');
		display.style.display=tabSelected;

		var display=document.getElementById('scgPageButtons');
		display.style.display=tabSelected;

		var displayConsentTable=document.getElementById('consentTabForSCG');
		if(displayConsentTable!=null)
		{
			displayConsentTable.style.display=displayTable;
		}
		//var collectionTab=document.getElementById('specimenCollectionGroupTab');
		var consentTab=document.getElementById('consentTab');
		var SCGImg = document.getElementById('SCGImage');
		var consetsImg = document.getElementById('consentsImage');

		if(selectedTab=="specimenCollectionGroupTab")
		{
			if('${requestScope.operation}' == "edit")
			{
				SCGImg.innerHTML ="<img src=images/uIEnhancementImages/tab_edit_collection.gif  alt=Add Specimen  Collection group  width=216 height=22 border=0>"
				consetsImg.innerHTML ="<img src=images/uIEnhancementImages/tab_consents2.gif alt=Consents width=76 height=22 border=0 onClick=consentPage()>"
			}
			else if('${requestScope.operation}' == "add")
			{
				SCGImg.innerHTML ="<img src=images/uIEnhancementImages/tab_add_scg.gif  alt=Add Specimen  Collection group  width=222 height=22 border=0>"
				consetsImg.innerHTML ="<img src=images/uIEnhancementImages/tab_consents2.gif alt=Consents width=76 height=22 border=0 onClick=consentPage()>"
			}
		}
		else
		{
			if('${requestScope.operation}' == "edit")
			{
				SCGImg.innerHTML ="<img src=images/uIEnhancementImages/tab_edit_collection2.gif  alt=Edit Specimen  Collection group  width=216 height=22 border=0 onclick=specimencollgroup()>"
				consetsImg.innerHTML ="<img src=images/uIEnhancementImages/tab_consents1.gif alt=Consents width=76 height=22 border=0>"
			}
			else if('${requestScope.operation}' == "add")
			{
				SCGImg.innerHTML ="<img src=images/uIEnhancementImages/tab_add_scg2.gif  alt=Add Specimen  Collection group  width=222 height=22 border=0 onclick=specimencollgroup()>"
				consetsImg.innerHTML ="<img src=images/uIEnhancementImages/tab_consents1.gif alt=Consents width=76 height=22 border=0>"
			}
		}
	}
	function updateTab(tab1, tab2)
	{
		tab1.onmouseover=null;
		tab1.onmouseout=null;
		tab1.className="tabMenuItemSelected";

		tab2.className="tabMenuItem";
		tab2.onmouseover=function() { changeMenuStyle(this,'tabMenuItemOver'),showCursor();};
		tab2.onmouseout=function() {changeMenuStyle(this,'tabMenuItem'),hideCursor();};
	}
		function specimencollgroup()
		{
			switchToTab("specimenCollectionGroupTab");
		}

		//This function will switch page to consentPage
		function consentPage()
		{
			checkForConsents();
		}

		function checkForConsents()
		{
			<%if(form!=null && form.getConsentTierCounter()>0)
				{%>
					switchToTab("consentTab");
				<%}
				else
				{%>
					alert("No consents available for selected Specimen Collection Group");
				<%}%>
		}

	  function showConsents()
	  {
		var showConsents = "<%=tab%>";
		if(showConsents=="<%=Constants.NULL%>" || showConsents=="scgPage")
		{
			specimencollgroup();
		}
		else
		{
			consentPage();
		}
	  }
		function viewSPR()
		{
			<%Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID);%>
			var reportId='<%=reportId%>';
			if(reportId==null || reportId==-1)
			{
				alert("There is no associate report in the system!");
			}
			else if(reportId==null || reportId==-2)
			{
				alert("Associated report is under quarantined request! Please contact administrator for further details.");
			}
			else
			{
		    	var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&reportId="+reportId;
				document.forms[0].action=action;
				document.forms[0].submit();
			}
		}


	function editSCG()
		{
			var tempId='<%=request.getParameter("id")%>';
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId;
			if('<%=pageOf%>'=='<%=Constants.PAGE_OF_SCG_CP_QUERY%>')
			{
				action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=search&id="+tempId;
			}
			document.forms[0].action=action;
			document.forms[0].submit();
		}

		function setTarget()
		{
			var fwdPage="<%=pageOf%>";
			if(!fwdPage=="pageOfSpecimenCollectionGroupCPQuery")
				document.forms[0].target = '_top';
		}

		function goToConsentPage()
		{
			var tempId=document.forms[0].id.value;
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId+"&tab=consent";
			document.forms[0].action=action;
			document.forms[0].submit();
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

 </script>
</head>
<!-- As it was giving javascript error on disableButtons() as the scg form is not loaded for DE -->
<%
	if(pageView != null && !pageView.equals("viewAnnotations") && !pageView.equals(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
	{
%>
	<body onload="disablebuttons();initializeSCGForm();showConsents();">
<%
	}else{
%>
	<body>
 <%
 	}
 %>
<html:form action="<%=formName%>">
	<%
		if(pageView.equals("add"))
		{
	%>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	  <tr>
		<td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
				<td valign="bottom" class="td_color_bfdcf3" id="specimenCollectionGroupTab"><a href="#" id="SCGImage"><img src="images/uIEnhancementImages/tab_add_scg.gif" alt="Add Specimen  Collection group"  width="222" height="22" border="0"></a></td>
				<td align="left" valign="bottom" class="td_color_bfdcf3"  id="consentTab"><a href="#" id="consentsImage"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0" onClick="consentPage()"></a></td>
				<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
			</tr></table>
			<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				</td>
			</tr>
			</table>

		<%@ include file="EditSpecimenCollectionGroup.jsp" %>
		</td>
				</tr>
				</table>
	<%
		}
	%>

	<%
			if(pageView.equals("edit"))
			{
		%>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	  <tr>
		<td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
				<td valign="bottom" id="specimenCollectionGroupTab"><a href="#" id="SCGImage"><img src="images/uIEnhancementImages/tab_edit_collection.gif" alt="Edit Specimen Collection Group" width="216" height="22"  border="0" onclick="specimencollgroup()"></a></td>
				<td valign="bottom"  onClick="viewSPR()"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="Inactive View Surgical Pathology Report " width="216" height="22"  border="0"></a></td>
				<td valign="bottom" onClick="showAnnotations()"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22"  border="0"></a></td>
				<td align="left" valign="bottom" class="td_color_bfdcf3"   id="consentTab"><a href="#" id="consentsImage"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0" onClick="consentPage()"></a></td>
				<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
			</tr></table>

			<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				</td>
			</tr>
			</table><!-- Mandar 31Oct08 -->
				<%@ include file="EditSpecimenCollectionGroup.jsp" %>
	<%
	}
	%>

<%
	if(pageView.equals(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="650">
			<tr>
				<td height="20" class="tabMenuItem"  id="specimenCollectionGroupTab"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="editSCG()">
					<bean:message key="specimenCollectionGroupPage.edit.title"/>
				</td>

				<td height="20" class="tabMenuItemSelected"   onClick="">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showAnnotations()">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
					<bean:message key="consents.consents"/>
				</td>
				<td width="300" class="tabMenuSeparator" colspan="1" >&nbsp;</td>
			</tr>
			<tr>
			<td colspan="6">
				<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
				<tr>
					<td>
						<%@ include file="/pages/content/common/ActionErrors.jsp" %>
					</td>
				</tr>
				</table>
			</td>
			</tr>
			<tr>
				<td class="tabField" colspan="6">

				<jsp:include page="ViewSurgicalPathologyReport.jsp" />
				</td>
			</tr>
		</table>
	<%
	}
	%>

	<%
	if(pageView.equals("edit"))
	{
	%>
			</td>
		</tr>
	</table>
	<%
	}
	%>

	<html:hidden property="nextForwardTo" />
	<input type="hidden" id="clinicalDataEntryURL" name="clinicalDataEntryURL" value=""/>
	<input type="hidden" id="isClinicalDataEntry" name="isClinicalDataEntry" value="false"/>
</html:form>
<script language="JavaScript" type="text/javascript">
showPriterTypeLocation();
</script>
</body>