								<%			
/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_18 			
 			* Description: Adding check for changes function
			*/
String printaction = "CPQueryPrintSCGAdd";
if(operation.equals(Constants.ADD))
{
	 printaction = "CPQueryPrintSCGAdd";
}
else if(operation.equals(Constants.EDIT))
{
 	 printaction = "CPQueryPrintSCGEdit";
} 
String normalSubmitFunctionName = "setSubmitted('" + submittedFor+ "','"+printaction+"','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][1]+"')";
String forwardToSubmitFuctionName = "setSubmitted('ForwardTo','"+ printaction +"','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][1]+"')";									
String forwardToSubmitFunctionNameForMultipleSpecimen = "setSubmitted('ForwardTo','"+printaction +"','" + Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][1]+"')";
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
	<%
	    //for Offset change we need Receieved and collected date.So as to change them when any offset is applied.
		Integer received_Year = new Integer(Utility.getYear(currentReceivedDate ));
		Integer received_Month = new Integer(Utility.getMonth(currentReceivedDate ));
		Integer received_Date = new Integer(Utility.getDay(currentReceivedDate ));
		Integer collection_Year = new Integer(Utility.getYear(currentCollectionDate ));
		Integer collection_Month = new Integer(Utility.getMonth(currentCollectionDate ));
		Integer collection_Day = new Integer(Utility.getDay(currentCollectionDate ));
	
	%>
	<script language="javascript">
	function registrationDateChange(newOffsetObject)
		 {
		 	
     		var originalcollectedDate= <%=collection_Month.intValue()%> +"/"+<%=collection_Day.intValue()%> +"/"+<%=collection_Year.intValue()%>;
			var newCollectedDate=dateChange(newOffsetObject,<%=form.getOffset()%>,originalcollectedDate);
			document.getElementById("collectionEventdateOfEvent").value=newCollectedDate;
			
			
			var originalReceiveddate= <%=received_Month.intValue()%> +"/"+<%=received_Date.intValue()%> +"/"+<%=received_Year.intValue()%>;
			var newReceivedDate=dateChange(newOffsetObject,<%=form.getOffset()%>,originalReceiveddate);
			document.getElementById("receivedEventDateOfEvent").value=newReceivedDate;
		 }
			</script>
	<div style="width:100%">	
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" id="Container" >
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
					<html:hidden property="withdrawlButtonStatus"/>
				 </tr>


				<tr>
					<br>
				</tr>
				 <!--Collection Protocol -->
				 <tr>
			     	<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
				    <td class="formFieldNoBordersSimple">
						<label for="collectionProtocolId">
							<b><bean:message key="specimenCollectionGroup.protocolTitle"/></b> 
						</label>
					</td> 
					
					<td class="formFieldNoBordersSimple">
						<label for="collectionProtocolIdValue">
							 <b><%=form.getCollectionProtocolName()%> </b>
						</label>
	
						<input type="hidden" id="collectionProtocolId" value="<%=form.getCollectionProtocolId()%>" />
						<input type="hidden" id="collectionProtocolName" value="<%=form.getCollectionProtocolName()%>" />
					
		        	</td>
				 </tr>
				 
                  <tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="participantName">
							 <b><bean:message key="specimenCollectionGroup.participantNameWitProtocolId" /></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple">
						<label for="participantName">
						<b>	<%=form.getParticipantNameWithProtocolId()%></b>
						</label>
						
					</td>
					<html:hidden property="participantName"/>
					<html:hidden property="protocolParticipantIdentifier"/>
				</tr>
				 <tr>
 			     	<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
				    <td class="formFieldNoBordersSimple">
						<label for="siteId">
							<b><bean:message key="specimenCollectionGroup.site"/></b>
						</label>
					</td>
					
					<td class="formFieldNoBordersSimple">
					 <autocomplete:AutoCompleteTag property="siteId"
										  optionsList = "<%=request.getAttribute(Constants.SITELIST)%>"
										  initialValue="<%=new Long(form.getSiteId())%>"
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
				<%if((!Variables.isSpecimenCollGroupLabelGeneratorAvl) || operation.equals(Constants.EDIT))
				{%>
				<tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="name">
							<b><bean:message key="specimenCollectionGroup.groupName" /></b>
						</label>
					</td>
					
					<td class="formFieldNoBordersSimple">
						<html:text styleClass="formFieldSized" size="30"  maxlength="255" styleId="name" property="name" />						
					</td>
				</tr>
				<%}%>
				<%if((!Variables.isSpecimenCollGroupBarcodeGeneratorAvl) || operation.equals(Constants.EDIT))
				{%>
				<tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="barcode">
							<b><bean:message key="specimenCollectionGroup.barcode" /></b>
						</label>
					</td>
					
					<td class="formFieldNoBordersSimple">
						<html:text styleClass="formFieldSized" size="30"  maxlength="255" styleId="barcode" property="barcode" />						
					</td>
				</tr>
				<%}%>
				 <tr>
				 	<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
				    
				    <td class="formFieldNoBordersSimple">
						<label for="collectionProtocolEventId">
							<b><bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/></b>
						</label>
					</td>
				    <td class="formFieldNoBordersSimple">
<!-- Mandar : 434 : for tooltip -->				    
				     	<html:select property="collectionProtocolEventId" styleClass="formFieldSized" styleId="collectionProtocolEventId" size="1" onchange="onChangeEvent(this)" 
				     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         	<html:options collection="<%=Constants.STUDY_CALENDAR_EVENT_POINT_LIST%>" labelProperty="name" property="value"/>				     					     					     	
						</html:select>&nbsp;
						<bean:message key="collectionprotocol.studycalendarcomment"/>
		        	</td>
				 </tr>
				<tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple" >
						<label for="offset">
							<bean:message key="specimenCollectionGroup.offset" />
						</label>
					</td>
					<td class="formFieldNoBordersSimple">
						<html:text styleClass="formFieldSized" size="10"  maxlength="10" styleId="offset" property="offset" onblur="registrationDateChange(this)"/>						
					</td>
				</tr>
				 <tr>
				     <td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
				     <td class="formFieldNoBordersSimple">
						<label for="clinicalDiagnosis">
							<b><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/></b>
						</label>
					 </td>
				     <td class="formFieldNoBordersSimple">
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
							<img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="16" height="16" title='CLinical Diagnosis Selector'>
					</a>
		        	 </td>
				 </tr>
				 
				 <tr>
				     <td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
				     <td class="formFieldNoBordersSimple">
						<label for="clinicalStatus">
							<b><bean:message key="specimenCollectionGroup.clinicalStatus"/></b>
						</label>
					 </td>
					 
				     <td class="formFieldNoBordersSimple">
					 
					 			 <autocomplete:AutoCompleteTag property="clinicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getClinicalStatus()%>"
										  styleClass="formFieldSized"
										 
									    />

		        	  </td>
				 </tr>
				 
				 <tr>
			     	<td class="formFieldNoBordersSimple" colspan="2" width="5">&nbsp;</td>
				    <td class="formFieldNoBordersSimple">
						<label for="participantsMedicalIdentifierId">
							<bean:message key="specimenCollectionGroup.medicalRecordNumber"/>
						</label>
					</td>
                    <td class="formFieldNoBordersSimple">
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
					<td class="formFieldNoBordersSimple" colspan="2" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
						<label for="surgicalPathologyNumber">
							<bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
						</label>
					</td>					
				    <td class="formFieldNoBordersSimple" noWrap="true">
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
					<td class="formFieldNoBordersSimple" colspan="2" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
						<label for="comments">
							<bean:message key="app.comments"/>
						</label>
					</td>					
				   <td class="formFieldNoBordersSimple" colspan="4">
				    		<html:textarea styleClass="formFieldSized" rows="3"  property="comment"/>
			    	</td>
				 </tr>
				 

				<!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="activityStatus">
							<b><bean:message key="site.activityStatus" /></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple">
							<autocomplete:AutoCompleteTag property="activityStatus"
								  optionsList = "<%=request.getAttribute(Constants.ACTIVITYSTATUSLIST)%>"
								  initialValue="<%=form.getActivityStatus()%>"
								  onChange="<%=strCheckStatus%>"
							/>
					</td>
				</tr>
				</logic:equal>
				<!-- collectionstatus -->	
				<tr>
					<td class="formFieldNoBordersSimple" colspan="2" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="collectionStatus">
							<b><bean:message key="specimenCollectionGroup.collectionStatus" /></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple">
							<autocomplete:AutoCompleteTag property="collectionStatus"
								optionsList = "<%=request.getAttribute(Constants.COLLECTIONSTATUSLIST)%>"
								initialValue="<%=form.getCollectionStatus()%>"
								onChange="<%=strCheckStatus%>"
							/>
					</td>
				</tr>
					
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
		List requestParticipantResponse = (List)request.getAttribute("specimenCollectionGroupResponseList");
		if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
		{
	%>
	    	<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
	<%
		}
	%>
	<!--  Consent Tracking Module Virender mehta -->	

	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" id="collAndRecEvents">
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
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage"  id="multiplespecimenTable">
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
						<html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimens" />
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
	</div>