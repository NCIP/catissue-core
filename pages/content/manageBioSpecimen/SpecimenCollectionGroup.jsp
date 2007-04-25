
<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<script src="jss/script.js" type="text/javascript"></script>
<!-- Bug Id: 4159
	 Patch ID: 4159_1			
	 Description: Including calenderComponent.js to show date in events
-->
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/calendarComponent.js" type="text/javascript"></script>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>


<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		boolean isAddNew = false;	

		String appendingPath = "/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
		if (reqPath != null)
			appendingPath = reqPath + "|/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
	
	   		Object obj = request.getAttribute("specimenCollectionGroupForm");
			SpecimenCollectionGroupForm form =null;
	
			if(obj != null && obj instanceof SpecimenCollectionGroupForm)
			{
				form = (SpecimenCollectionGroupForm)obj;
			}	
	
		String formName, pageView = operation ,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;
	   	if(!operation.equals("add") )
	   	{
	   		obj = request.getAttribute("specimenCollectionGroupForm");
	   		
			if(obj != null && obj instanceof SpecimenCollectionGroupForm)
			{
				form = (SpecimenCollectionGroupForm)obj;
		   		appendingPath = "/SpecimenCollectionGroupSearch.do?operation=search&pageOf="+pageOf+"&id="+form.getId() ;
		   		int checkedButton1 = form.getCheckedButton();
		   	}
			
	   	}
			


		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION;
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
		long idToTree = form.getId();
		

/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_20			
 			* Description: Default Date to show in events
			*/
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
		
		//Patch ID: Bug#3184_32
		//Description: Get the actual number of specimen collections
		String numberOfSpecimenCollection = (String)request.getAttribute(Constants.NUMBER_OF_SPECIMEN_REQUIREMENTS);
		if(numberOfSpecimenCollection == null)
		{
			numberOfSpecimenCollection = "0";
		}
%>
<head>

	<%if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
	{
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
	%>
		<script language="javascript">
			var cpId = window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].document.getElementById("cpId").value;
			var participantId = window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].document.getElementById("participantId").value;
			<%if(request.getAttribute(Constants.CP_SEARCH_PARTICIPANT_ID) != null ) {
			String cpParticipantId = (String) request.getAttribute(Constants.CP_SEARCH_PARTICIPANT_ID);%>
			participantId = <%=cpParticipantId%>;
			<%}%>
			window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].location="showCpAndParticipants.do?cpId="+cpId+"&participantId="+participantId;
			window.parent.frames['<%=Constants.CP_TREE_VIEW%>'].location="showTree.do?<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+participantId+"&nodeId=SpecimenCollectionGroup_"+<%=idToTree%>;
			
		</script>
	<%}%>

	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
     <script language="JavaScript">
     
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
        	/*
				Patch ID: Bug#3184_33
			 	Description: Element Id is used in SpecimenCollectionGroupAction.java. This value
					decides whether to set the value of checkbox and the number of specimens on the 
					specimen collection group page to the default values or not. If of id is 
					"collectionProtocolId" then values are set to default i.e. number of specimen to 1
					and checkbox to false.
			*/
			var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&pageOf=<%=pageOf%>&" +
        			"isOnChange=true&changeOn=" + element.id;
        	<%if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
			{%>
				action = "QuerySpecimenCollectionGroup.do?pageOf=<%=pageOf%>&operation=<%=operation%>&"+
						"isOnChange=true";
			<%}%>		
        	changeAction(action);
		}
        function changeAction(action)
        {
			document.forms[0].action = action;
			document.forms[0].submit();
        }
/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_21 			
 			* Description: Function to check whether user has entered any data in events and to prompt him whether he wants to propagate it to all specimens under this scg
			*/
		var applyToSpecimen;
		function checkForChanges()
		{
			//alert("in check for changes");
			//user entered values
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
			
			//alert("collectionEventdateOfEvent "+collectionEventdateOfEvent+" collectionEventdateOfEventForm"+collectionEventdateOfEventForm);
			//alert("collectionEventUserIdForm "+collectionEventUserIdForm+" collectionEventUserId"+collectionEventUserId);
			//alert("collectionEventTimeInHoursForm"+collectionEventTimeInHoursForm+" collectionEventTimeInHours"+collectionEventTimeInHours);
			//alert("collectionEventTimeInMinutesForm"+collectionEventTimeInMinutesForm+" collectionEventTimeInMinutes"+collectionEventTimeInMinutes);
			//alert("collectionEventCollectionProcedureForm"+collectionEventCollectionProcedureForm+" collectionEventCollectionProcedure"+collectionEventCollectionProcedure);
			//alert("collectionEventContainerForm"+collectionEventContainerForm+" collectionEventContainer"+collectionEventContainer);
			//alert("receivedEventUserIdForm"+receivedEventUserIdForm+ " receivedEventUserId"+receivedEventUserId);
			//alert("currentReceivedDateForm"+currentReceivedDateForm + " receivedEventdateOfEvent"+receivedEventdateOfEvent);
			//alert("receivedEventTimeInHoursForm"+receivedEventTimeInHoursForm +" receivedEventTimeInHours"+receivedEventTimeInHours);
			//alert("receivedEventTimeInMinutesForm"+receivedEventTimeInMinutesForm+" receivedEventTimeInMinutes"+receivedEventTimeInMinutes);
			//alert("receivedEventReceivedQualityForm"+receivedEventReceivedQualityForm+" receivedEventReceivedQuality"+receivedEventReceivedQuality);
			
			
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
				var appResources = '<%=ApplicationProperties.getValue("specimenCollectionGroup.changeEvents.confirm")%>';			
				
				var answer = confirm(appResources);
				if(answer)
				{
				//alert("Confirm OK");
					applyToSpecimen = 'true';	
				}
				else
				{
				//alert("Confirm CANCEL");
					applyToSpecimen = 'false';	
				}
			}
		}
		function confirmDisableForSCG(action,formField)
		{		
			var temp = action+"?applyToSpecimenValue="+applyToSpecimen;		
			if((formField != undefined) && (formField.value == "Disabled"))
			{
				var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
				if (go==true)
				{	document.forms[0].action = temp;
					document.forms[0].submit();
				}
			}
			else
			{
				document.forms[0].action = temp;
				document.forms[0].submit();
			}			
		}
        /**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: Multiple Specimen Bug
 			* Patch ID: Multiple Specimen Bug_2 
 			* See also: 1-8
 			* Description: Function to disable "Submit" and "Add Specimen" buttons if number of specimens entered  > 1
			*/
		function disablebuttons()
		{
			var enteredValue = document.getElementById("numberOfSpecimen").value;
			var submitButton = document.getElementById("submitOnly");
			var submitAndAddButton = document.getElementById("submitAndAdd");
			
			/**
			 * Patch ID: Bug#3184_10
			 * Description: The Add Specimen button must be enabled if the restrict checkbox is checked.
			 */
			var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
			
			// Patch ID: Bug#3184_34
			var submitAndAddMultipleButton =  document.getElementById("submitAndAddMultiple");
			
			if(enteredValue > 1)
			{			
				submitButton.disabled = true;
				submitAndAddButton.disabled = true;
				submitAndAddMultipleButton.disabled = false;
			}
			else if(restrictCheckbox.checked && enteredValue == 1)
			{
				submitButton.disabled = true;
				submitAndAddButton.disabled = false;
				submitAndAddMultipleButton.disabled = true;
			}
			else
			{			
				submitButton.disabled = false;
				submitAndAddButton.disabled = false;
				submitAndAddMultipleButton.disabled = false;
			}
		}
		
		/**
		 * Patch ID: Bug#3184_11
		 * Description: The following functions enables and disables the Submit and Add Specimen buttons as and when
		 * needed.
		 */
		function disableButtonsOnCheck(restrictCheckbox)
		{
			var submitButton = document.getElementById("submitOnly");
			var addSpecimenButton = document.getElementById("submitAndAdd");
			// Patch ID: Bug#3184_35
			var submitAndAddMultipleButton =  document.getElementById("submitAndAddMultiple");
			
			var numberOfSpecimen = document.getElementById("numberOfSpecimen").value;
			if(restrictCheckbox.checked)
			{
				if(numberOfSpecimen != null && numberOfSpecimen == 1)
				{
					addSpecimenButton = false;
					submitAndAddMultipleButton.disabled = true;
				}
				submitButton.disabled = true;
			}
			else
			{
				disablebuttons();
			}
		}
				
		function initializeSCGForm()
		{
			var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
			var valueForCheckbox = <%=form.getRestrictSCGCheckbox()%>
			if(valueForCheckbox!=null && valueForCheckbox)
			{
				disableButtonsOnCheck(restrictCheckbox);
			}
		}
	</script>
</head>
			<!-- 
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: Multiple Specimen Bug
 			* Patch ID: Multiple Specimen Bug_2 
 			* See also: 1-8
 			* Description: Call to function to disable "Submit" and "Add Specimen" buttons if number of specimens entered  > 1 on body refreshing
			*/
			-->

<!--
	Patch ID: Bug#3184_12
-->
<body onload="disablebuttons();initializeSCGForm()">
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
	<%
	if(pageView.equals("edit"))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItemSelected" onclick="document.location.href='ManageAdministrativeData.do'">Edit</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
								
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
	<%
	}
	%>
	
	<%
/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_18 			
 			* Description: Adding check for changes function
			*/
	String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][1]+"')";
	String forwardToSubmitFuctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][1]+"')";									
	String forwardToSubmitFunctionNameForMultipleSpecimen = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][1]+"')";									
	String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
	/**
		* Name : Ashish Gupta
		* Reviewer Name : Sachin Lale 
		* Bug ID: Multiple Specimen Bug
		* Patch ID: Multiple Specimen Bug_2 
		* See also: 1-8
		* Description: passing "button=multipleSpecimen"with the url so that validation is done only on click of "Add Multiple Specimen" button
	*/
	String confirmDisableFuncNameForMultipleSpecimen = "";
	if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
	{
		// In case of CP based view query, formName variable already has 
		// some parameter appended to the url. hence appending the button parameter by "&"
		confirmDisableFuncNameForMultipleSpecimen =  "confirmDisable('" + formName +"&button=multipleSpecimen',document.forms[0].activityStatus)";
	}
	else
	{
		confirmDisableFuncNameForMultipleSpecimen =  "confirmDisable('" + formName +"?button=multipleSpecimen',document.forms[0].activityStatus)";
	}
	String normalSubmit = "";
	String forwardToSubmit = "";
	String forwardToSubmitForMultipleSpecimen = "";
	if(operation.equals(Constants.EDIT))
	{
		confirmDisableFuncName = "confirmDisableForSCG('" + formName +"',document.forms[0].activityStatus)";
		normalSubmit = "checkForChanges(),"+normalSubmitFunctionName + ","+confirmDisableFuncName;
		forwardToSubmit = "checkForChanges(),"+ forwardToSubmitFuctionName + ","+confirmDisableFuncName;
		
		if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
		{
			// In case of CP based view query, formName variable already has 
			// some parameter appended to the url. hence appending the button parameter by "&"
			confirmDisableFuncNameForMultipleSpecimen =  "confirmDisableForSCG('" + formName +"&button=multipleSpecimen',document.forms[0].activityStatus)";
		}
		else
		{
			confirmDisableFuncNameForMultipleSpecimen =  "confirmDisableForSCG('" + formName +"?button=multipleSpecimen',document.forms[0].activityStatus)";
		}
		
		forwardToSubmitForMultipleSpecimen = "checkForChanges(),"+forwardToSubmitFunctionNameForMultipleSpecimen + ","+confirmDisableFuncNameForMultipleSpecimen;
	}
	else
	{			
		normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
		forwardToSubmit = forwardToSubmitFuctionName + ","+confirmDisableFuncName;			
		forwardToSubmitForMultipleSpecimen = forwardToSubmitFunctionNameForMultipleSpecimen + ","+confirmDisableFuncNameForMultipleSpecimen;
	}
	%>
		
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION BEGINS-->
		
	    <tr><td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value=""/>
					</td>
				 </tr>
				 
				 <tr>
					<td><html:hidden property="id"/></td>
					<td><html:hidden property="onSubmit"/></td>
					<td><html:hidden property="redirectTo" value="<%=reqPath%>"/></td>
				 </tr>
				 <tr>
				 	<td class="formMessage" colspan="4">* indicates a required field</td>
				 </tr>
				 
				<tr>
					<td class="formTitle" height="20" colspan="4">
						<%String title = "specimenCollectionGroup."+pageView+".title";%>
							<bean:message key="<%=title%>"/>						
					</td>
				</tr>

				 
				 <!--Collection Protocol -->
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
						<label for="collectionProtocolId">
							<bean:message key="specimenCollectionGroup.protocolTitle"/>
						</label>
					</td>
					
					<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="collectionProtocolId" styleClass="formFieldSized" styleId="collectionProtocolId" size="1" disabled="<%=readOnlyForAll%>" onchange="onChangeEvent(this)"
				     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
						</html:select>
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						<html:link href="#" styleId="newCollectionProtocol" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=collectionProtocol&forwardTo=specimenCollectionGroup&addNewFor=collectionProtocol')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
		        	</td>
				 </tr>

				 <tr>
 			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
						<label for="siteId">
							<bean:message key="specimenCollectionGroup.site"/>
						</label>
					</td>
					
					<td class="formField">
					 <autocomplete:AutoCompleteTag property="siteId"
										  optionsList = "<%=request.getAttribute(Constants.SITELIST)%>"
										  initialValue="<%=form.getSiteId()%>"
										  styleClass="formFieldSized"
										  staticField="false"
										 
									    />
					

						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						&nbsp;
						<html:link href="#" styleId="newSite" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=site&forwardTo=specimenCollectionGroup&addNewFor=site')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
		        	</td>
				 </tr>
				 
				 <tr>
				 	<td class="formRequiredNoticeNoBottom">*
				     	<html:radio styleClass=""  property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
  				     	    <label for="participantId">
								<%--<bean:message key="specimenCollectionGroup.collectedByParticipant" />--%>
							</label>
				     	</html:radio>
 				    </td>
 				    <td class="formRequiredLabelLeftBorder" width="186">
 				    	<label for="participantId">
					        <bean:message key="specimenCollectionGroup.collectedByParticipant" />
						</label>
  					</td>
  					<td class="formField">
  						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="1">
<!-- Mandar : 434 : for tooltip --> 						
				     	     <html:select property="participantId" styleClass="formFieldSized" styleId="ParticipantId" size="1" onchange="onChangeEvent(this)"
				     	      onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         	     <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>				     	
  						     </html:select>
  						</logic:equal>     
						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">
<!-- Mandar : 434 : for tooltip -->						
				     	     <html:select property="participantId" styleClass="formFieldSized" styleId="ParticipantId" size="1" onchange="onChangeEvent(this)" disabled="true"
				     	      onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         	     <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>				     	
  						     </html:select>
  						</logic:equal>
						
						&nbsp;
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						<html:link href="#" styleId="newParticipant" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=participantRegistration&forwardTo=specimenCollectionGroup&addNewFor=participant')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						<html:link href="#" styleId="newParticipant" onclick="addNewAction('CPQuerySpecimenCollectionGroupAddNew.do?addNewForwardTo=participantRegistration&forwardTo=specimenCollectionGroup&addNewFor=participant')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:equal>
						
 						</logic:notEqual>
					</td>
  					
				 </tr>
				 
				 <tr>
				    <td class="formRequiredNotice">
				       	<html:radio styleClass="" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
  				       	    <label for="protocolParticipantIdentifier">
								<%--<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber" />--%>
							</label>
				     	</html:radio>
				    </td>
				    <td class="formRequiredLabel"  width="186">
						<label for="protocolParticipantIdentifier">
							<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber" />
						</label>
					</td>
					
  			        <td class="formField">
  					<%-- LOGIC TAG FOR PARTICPANT NUMBER --%> 												
                        <logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="1">						
<!-- Mandar : 434 : for tooltip -->
   						 	<html:select property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" size="1" disabled="true"
   						 	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         		<html:options collection="<%=Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST%>" labelProperty="name" property="value"/>				     					     	
							</html:select>
 						</logic:equal>
 						
 						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">						
<!-- Mandar : 434 : for tooltip -->
   						 	<html:select property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" size="1" 
   						 	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         		<html:options collection="<%=Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST%>" labelProperty="name" property="value"/>				     					     	
							</html:select>
 						</logic:equal>
					
						&nbsp;
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
 						<html:link href="#" styleId="newParticipant" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=participantRegistration&forwardTo=specimenCollectionGroup&addNewFor=protocolParticipantIdentifier')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						<html:link href="#" styleId="newParticipant" onclick="addNewAction('CPQuerySpecimenCollectionGroupAddNew.do?addNewForwardTo=participantRegistration&forwardTo=specimenCollectionGroup&addNewFor=protocolParticipantIdentifier')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:equal>
	 					</logic:notEqual>
		        	</td>
				 </tr>
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="name">
							<bean:message key="specimenCollectionGroup.groupName" />
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" size="30"  maxlength="255" styleId="name" property="name" />
						&nbsp;
						<%String resetAction = "changeAction('SpecimenCollectionGroup.do?operation="+operation+"&pageOf=pageOfSpecimenCollectionGroup&resetName=Yes')"; 
						if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY)){
							resetAction = "changeAction('QuerySpecimenCollectionGroup.do?operation="+operation+"&pageOf=pageOfSpecimenCollectionGroupCPQuery&resetName=Yes')"; 
						}%>
						<html:link href="#" styleId="resetName" onclick="<%=resetAction%>">
							<bean:message key="link.resetName" />
						</html:link>
					</td>
				</tr>
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
				    
				    <td class="formRequiredLabel">
						<label for="collectionProtocolEventId">
							<bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/>
						</label>
					</td>
				    <td class="formField">
<!-- Mandar : 434 : for tooltip -->				    
				     	<html:select property="collectionProtocolEventId" styleClass="formFieldSized" styleId="collectionProtocolEventId" size="1" onchange="onChangeEvent(this)" 
				     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         	<html:options collection="<%=Constants.STUDY_CALENDAR_EVENT_POINT_LIST%>" labelProperty="name" property="value"/>				     					     					     	
						</html:select>&nbsp;
						<bean:message key="collectionprotocol.studycalendarcomment"/>
		        	</td>
				 </tr>
				 
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
						<label for="clinicalDiagnosis">
							<bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
						</label>
					 </td>
				     <td class="formField">
                             <autocomplete:AutoCompleteTag property="clinicalDiagnosis"
										  optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
										  initialValue="<%=form.getClinicalDiagnosis()%>"
										  styleClass="formFieldSized"
										  size="30"
					        />
							
						<%
						String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";			
						%>
						<!-- // Patch ID: Bug#3090_22 -->
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
							<img src="images\Tree.gif" border="0" width="26" height="22" title='CLinical Diagnosis Selector'>
					</a>
		        	 </td>
				 </tr>
				 
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
						<label for="clinicalStatus">
							<bean:message key="specimenCollectionGroup.clinicalStatus"/>
						</label>
					 </td>
					 
				     <td class="formField">
					 
					 			 <autocomplete:AutoCompleteTag property="clinicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getClinicalStatus()%>"
										  styleClass="formFieldSized"
										 
									    />

		        	  </td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="participantsMedicalIdentifierId">
							<bean:message key="specimenCollectionGroup.medicalRecordNumber"/>
						</label>
					</td>
                    <td class="formField">
   						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="1">
<!-- Mandar : 434 : for tooltip -->   						
				     		<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="<%=readOnlyForAll%>"
				     		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         		<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">
<!-- Mandar : 434 : for tooltip -->					     	
					     	<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="true"
					     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                	         	<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
		        	</td>					
				 </tr>
				 
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="surgicalPathologyNumber">
							<bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
						</label>
					</td>					
				    <td class="formField" noWrap="true">
				     	<html:text styleClass="formFieldSized" size="30"  maxlength="50"  styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
					     	<!-- This feature will be implemented in next release
							&nbsp;
							<html:submit styleClass="actionButton" disabled="true">
								<bean:message key="buttons.getPathologyReport"/>
							</html:submit>
							-->
				    </td>
				
				 </tr>
				 <!--comments -->
				 <!-- 
				 * Name: Shital Lawhale
			     * Bug ID: 3052
			     * Patch ID: 3052_1_1
			     * See also: 1_1 to 1_5
				 * Description : Added <TR> for comment field .				 
				-->	 
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="comments">
							<bean:message key="app.comments"/>
						</label>
					</td>					
				   <td class="formField" colspan="4">
				    		<html:textarea styleClass="formFieldSized" rows="3"  property="comment"/>
			    	</td>
				 </tr>
				 

				<!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="site.activityStatus" />
						</label>
					</td>
					<td class="formField">
<!-- Mandar : 434 : for tooltip -->						
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
					
				 		<tr>
				  		<td align="right" colspan="3">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
				
				  		</td>
				 	</tr>
			</table>
		</td></tr>
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION ENDS-->
	</table>
	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="600">
		<tr>
			<td>
				<%@ include file="CollAndRecEvents.jsp" %>
			</td>
		</tr>
	</table>
	<!--
 * Name : Ashish Gupta
 * Reviewer Name : Sachin Lale 
 * Bug ID: Multiple Specimen Bug
 * Patch ID: Multiple Specimen Bug_1 
 * See also: 1-8
 * Description: Table to display number of specimens text field
	-->

	<!-- For Multiple Specimen-----Ashish -->
		<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="600">
		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"
				width="100%">
				
				<tr>
					<td class="formTitle" " colspan="6" height="20">
						<bean:message key="multipleSpecimen.mainTitle" />
					</td>
				</tr>
				<tr>
					
					<td class="formLabel" colspan="2" style="border-left:1px solid #5C5C5C;">
						<bean:message key="multipleSpecimen.numberOfSpecimen" />
					</td>
					<td class="formField" colspan="3">
						<!-- html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimen"  /-->
						<html:text  styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimens" onkeyup="disablebuttons()"/>
					</td>
				</tr>			
			</table>
			</td>
			<!-- Hidden fields for events 
			/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_19 			
 			* Description: Hidden fields for events
			*/-->
			<input type="hidden" id="collectionEventdateOfEventForm" value="<%=currentCollectionDate%>"  />
			<input type="hidden" id="collectionEventUserIdForm" value="<%=form.getCollectionEventUserId()%>"  />
			<input type="hidden" id="collectionEventTimeInHoursForm" value="<%=form.getCollectionEventTimeInHours()%>"  />
			<input type="hidden" id="collectionEventTimeInMinutesForm" value="<%=form.getCollectionEventTimeInMinutes()%>"  />
			<input type="hidden" id="collectionEventCollectionProcedureForm" value="<%=form.getCollectionEventCollectionProcedure()%>"  />
			<input type="hidden" id="collectionEventContainerForm" value="<%=form.getCollectionEventContainer()%>"  />
			<input type="hidden" id="collectionEventCommentsForm" value="<%=form.getCollectionEventComments()%>"  />
			<html:hidden property="collectionEventId"/>
			
			<input type="hidden" id="receivedEventUserIdForm" value="<%=form.getReceivedEventUserId()%>"  />
			<input type="hidden" id="currentReceivedDateForm" value="<%=currentReceivedDate%>"  />
			<input type="hidden" id="receivedEventTimeInHoursForm" value="<%=form.getReceivedEventTimeInHours()%>"  />
			<input type="hidden" id="receivedEventTimeInMinutesForm" value="<%=form.getReceivedEventTimeInMinutes()%>"  />
			<input type="hidden" id="receivedEventReceivedQualityForm" value="<%=form.getReceivedEventReceivedQuality()%>"  />
			<input type="hidden" id="receivedEventCommentsForm" value="<%=form.getReceivedEventComments()%>"  />
			<html:hidden property="receivedEventId"/>
			<!-- Patch ID: Bug#3184_36 -->
			<input type="hidden" id="actualNumberOfSpecimen" name="actualNumberOfSpecimen" value="<%=numberOfSpecimenCollection%>"/>
			
		</tr>
	</table>
	
	<%@ include file="SpecimenCollectionGroupPageButtons.jsp" %>
	
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
</html:form>
</body>