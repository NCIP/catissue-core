<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<script src="jss/script.js" type="text/javascript"></script>



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
        	var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&pageOf=<%=pageOf%>&" +
        			"isOnChange=true";
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
 			* Bug ID: Multiple Specimen Bug
 			* Patch ID: Multiple Specimen Bug_2 
 			* See also: 1-8
 			* Description: Remove the page on which number of multiple specimens are entered while going to multiple specimen page.
			*/
		function disablebuttons()
		{
			var enteredValue = document.getElementById("numberOfSpecimen").value;
			var submitButton = document.getElementById("submitOnly");
			var submitAndAddButton = document.getElementById("submitAndAdd");
			if(enteredValue > 1)
			{			
				submitButton.disabled = true;
				submitAndAddButton.disabled = true;
			}
			else
			{			
				submitButton.disabled = false;
				submitAndAddButton.disabled = false;
			}
		}
		
	</script>
</head>

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
			String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][1]+"')";
			String forwardToSubmitFuctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][1]+"')";									
			String forwardToSubmitFunctionNameForMultipleSpecimen = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][1]+"')";									
			String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
			String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
			String forwardToSubmit = forwardToSubmitFuctionName + ","+confirmDisableFuncName;
			/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: Multiple Specimen Bug
 			* Patch ID: Multiple Specimen Bug_2 
 			* See also: 1-8
 			* Description: Remove the page on which number of multiple specimens are entered while going to multiple specimen page.
			*/
			String confirmDisableFuncNameForMultipleSpecimen =  "confirmDisable('" + formName +"?button=multipleSpecimen',document.forms[0].activityStatus)";
			String forwardToSubmitForMultipleSpecimen = forwardToSubmitFunctionNameForMultipleSpecimen + ","+confirmDisableFuncNameForMultipleSpecimen;

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
				 	<td class="formRequiredNoticeNoBottom">
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
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','250','330','no');return false">
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
	<!--
 * Name : Ashish Gupta
 * Reviewer Name : Sachin Lale 
 * Bug ID: Multiple Specimen Bug
 * Patch ID: Multiple Specimen Bug_1 
 * See also: 1-8
 * Description: Remove the page on which number of multiple specimens are entered while going to multiple specimen page.
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
					
					<td class="formLabel" colspan="2">
						<bean:message key="multipleSpecimen.numberOfSpecimen" />
					</td>
					<td class="formField" colspan="3">
						<!-- html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimen"  /-->
						<html:text  styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimens" onkeyup="disablebuttons()"/>
					</td>
				</tr>			
			</table>
			</td>
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