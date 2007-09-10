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
		
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="650" >
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION BEGINS-->
		
	    <tr><td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="scgTable">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value=""/>
						<html:hidden property="participantId" />
						<html:hidden property="stringOfResponseKeys"/>
						<html:hidden property="applyChangesTo"/>
						<html:hidden property="consentTierCounter"/>
						<html:hidden property="collectionProtocolId"/>
						<html:hidden property="participantNameWithProtocolId"/>
						<html:hidden property="participantName"/>
						<html:hidden property="collectionProtocolName"/>
						<html:hidden property="protocolParticipantIdentifier"/>
						
					</td>
				 </tr>
				 
				 <tr>
					<td><html:hidden property="id"/></td>
					<td><html:hidden property="onSubmit"/></td>
					<td><html:hidden property="redirectTo" value="<%=reqPath%>"/></td>
				 </tr>
				 <tr>
				 	<td class="formMessage" colspan="6">
						<html:hidden property="withdrawlButtonStatus"/>
						<bean:message key="requiredfield.message"/>  
					</td>
				 </tr>
				 
				<tr>
					<td class="formTitle" height="20" colspan="5">
						<%String title = "specimenCollectionGroup."+pageView+".title";%>
							<bean:message key="<%=title%>"/>						
					</td>
				</tr>

				 
				 <!--Collection Protocol -->
				 <tr>
			     	<td class="formRequiredNotice" colspan="2" width="5">*</td>
				    <td class="formRequiredLabel">
						<label for="collectionProtocolId">
							<bean:message key="specimenCollectionGroup.protocolTitle"/> 
						</label>
					</td> 
					
					<td class="formField">
				<%	
					if(pageView.equals("add"))
					{					
				%>
						<label for="collectionProtocolIdValue">
							 <b><%=form.getCollectionProtocolName()%> </b>
						</label>
	
						<input type="hidden" id="collectionProtocolId" value="<%=form.getCollectionProtocolId()%>" />
						<input type="hidden" id="collectionProtocolName" value="<%=form.getCollectionProtocolName()%>" />
					
				<% 
					}
					else
					 {
				%>
						<!-- Mandar : 434 : for tooltip -->
				
				      	<html:select property="collectionProtocolId" styleClass="formFieldSized" styleId="collectionProtocolId" size="1" disabled="<%=readOnlyForAll%>" onchange="onChangeEvent(this)"
				     	 onmouseover="showToolTip(this)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
						</html:select>

						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						<html:link href="#" styleId="newCollectionProtocol" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=collectionProtocol&forwardTo=specimenCollectionGroup&addNewFor=collectionProtocol')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
       				<% 
						}
					%>
		        	</td>
				 </tr>

				 <tr>
 			     	<td class="formRequiredNotice" colspan="2" width="5">*</td>
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
				 <%	if(pageView.equals("add"))
					{				
				%>
				<tr>
					<td class="formRequiredNotice" colspan="2" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="participantName">
							 <bean:message key="specimenCollectionGroup.participantNameWitProtocolId" />
						</label>
					</td>
					<td class="formField">
						<label for="participantName">
						<b>	<%=form.getParticipantNameWithProtocolId()%></b>
						</label>
						
					</td>
					<html:hidden property="participantName"/>
					<html:hidden property="protocolParticipantIdentifier"/>
				</tr>
				<% }else {
				%>
				<html:hidden property="participantName"/>
				<html:hidden property="protocolParticipantIdentifier"/>

				 <tr>
					 <td class="formRequiredNoticeNoBottom">*</td>
					 <td class="formRequiredNoticeWithoutBorder">
				     	<html:radio styleClass=""  property="radioButtonForParticipant" value="1" onclick="onRadioButtonClick(this)">
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
  						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="1">
<!-- Mandar : 434 : for tooltip --> 						
				     	    <html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="participantName" property="participantName" />
  						</logic:equal>     
						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="2">
<!-- Mandar : 434 : for tooltip -->						
				     	     <html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="participantName" property="participantName" disabled="true"/>
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
				    <td class="formRequiredNotice" align="right">&nbsp;</td>
					<td class="formRequiredNoticeWithoutLeftBorder">
					<html:radio styleClass="" property="radioButtonForParticipant" value="2" onclick="onRadioButtonClick(this)">
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
                        <logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="1">						
<!-- Mandar : 434 : for tooltip -->
   						 	<html:text property="protocolParticipantIdentifier" maxlength="255" size="30"  styleClass="formFieldSized" styleId="protocolParticipantIdentifier" disabled="true">
							</html:text>
 						</logic:equal>
 						
 						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="2">						
<!-- Mandar : 434 : for tooltip -->
   						 	<html:text property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" maxlength="255" size="30" >
							</html:text>
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
				<% }%>
				<tr>
					<td class="formRequiredNotice" colspan="2" width="5">*</td>
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
				 	<td class="formRequiredNotice" colspan="2" width="5">*</td>
				    
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
				     <td class="formRequiredNotice" colspan="2" width="5">*</td>
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
				     <td class="formRequiredNotice" colspan="2" width="5">*</td>
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
			     	<td class="formRequiredNotice" colspan="2" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="participantsMedicalIdentifierId">
							<bean:message key="specimenCollectionGroup.medicalRecordNumber"/>
						</label>
					</td>
                    <td class="formField">
   						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="1">
<!-- Mandar : 434 : for tooltip -->   						
				     		<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="<%=readOnlyForAll%>"
				     		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         		<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="2">
<!-- Mandar : 434 : for tooltip -->					     	
					     	<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="true"
					     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                	         	<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
		        	</td>					
				 </tr>
				 
				 <tr>
					<td class="formRequiredNotice" colspan="2" width="5">&nbsp;</td>
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
					<td class="formRequiredNotice" colspan="2" width="5">&nbsp;</td>
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
					<td class="formRequiredNotice" colspan="2" width="5">*</td>
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
 
			
	<!--  Consent Tracking Module Virender mehta	 -->
	<%
		List requestParticipantResponse = (List)request.getAttribute("specimenCollectionGroupResponseList");					if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
		{
	%>
	    	<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
	<%
		}
	%>
	<!--  Consent Tracking Module Virender mehta -->	

	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="650">
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
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="650" id="multiplespecimenTable">
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
						<html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimens" onkeyup="disablebuttons()"/>
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
			<!-- Patch ID: Bug#4227_4 -->
			<html:hidden styleId="buttonType" property="buttonType"/>
			
		</tr>
	</table>
	
	<%@ include file="SpecimenCollectionGroupPageButtons.jsp" %>