<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript" src="jss/combos.js"></script>
<script type="text/javascript" src="jss/examples.js"></script>
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="css/xtheme-gray.css" />
<link rel="stylesheet" type="text/css" href="css/combo.css" />
<link rel="stylesheet" type="text/css" href="css/examples.css" />
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
			Integer received_Year = new Integer(AppUtility.getYear(currentReceivedDate ));
			Integer received_Month = new Integer(AppUtility.getMonth(currentReceivedDate ));
			Integer received_Date = new Integer(AppUtility.getDay(currentReceivedDate ));
			Integer collection_Year = new Integer(AppUtility.getYear(currentCollectionDate ));
			Integer collection_Month = new Integer(AppUtility.getMonth(currentCollectionDate ));
			Integer collection_Day = new Integer(AppUtility.getDay(currentCollectionDate ));
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
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg" id="">
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION BEGINS-->
		 <tr>
          <td colspan="3" align="left"></td>
        </tr>
	    <tr>
          <td colspan="3" align="left" valign="top" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0" id="scgTable">
				 
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
						<html:hidden property="id"/>
						<html:hidden property="onSubmit"/>
						<html:hidden property="redirectTo" value="<%=reqPath%>"/>
						<html:hidden property="withdrawlButtonStatus"/>
					
				 <!--Collection Protocol -->
				 <tr>
			     	<td width="1%" class="black_ar_t"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="3" /></span></td>
				   <td width="12%" align="left" valign="top" class="black_ar_t"><LABEL for="collectionProtocolId"><bean:message key="specimenCollectionGroup.protocolTitle"/></LABEL></td> 
					
					<td width="36%" align="left" valign="top"><span class="black_ar_t"><%=form.getCollectionProtocolName()%></span>
	
						<input type="hidden" id="collectionProtocolId" value="<%=form.getCollectionProtocolId()%>" />
						<input type="hidden" id="collectionProtocolName" value="<%=form.getCollectionProtocolName()%>" />
					
		        	</td>
				    <td width="1%" class="black_ar_t" nowrap><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="3" /> </td>           
                    <td width="18%" align="left" class="black_ar_t"><bean:message key="specimenCollectionGroup.participantNameWitProtocolId" /></td>
					<td width="32%" align="left" nowrap class="black_ar_t"><%=form.getParticipantNameWithProtocolId()%></td> 
					<html:hidden property="participantName"/>
					<html:hidden property="protocolParticipantIdentifier"/>
				</tr>
				 <tr>				 
				<tr>
				<%
					if((!Variables.isSpecimenCollGroupLabelGeneratorAvl) || operation.equals(Constants.EDIT))
						{
				%>
				
					 <td class="black_ar_t" nowrap><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="3" /></td>    
					<td align="left" nowrap><span class="black_ar"><label for="name"><bean:message key="specimenCollectionGroup.groupName" /></label></span></td>
					<td align="left" valign="top" nowrap><html:text styleClass="formFieldSizedSC"   maxlength="255" styleId="name" property="name" /></td>
				
				<%
									}if((!Variables.isSpecimenCollGroupBarcodeGeneratorAvl) || operation.equals(Constants.EDIT))
										{
								%>
					<td class="black_ar_t">&nbsp;</td>
					<td align="left" valign="top" class="black_ar_t"><bean:message key="specimenCollectionGroup.barcode" /></td>
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
						<logic:equal name ="specimenCollectionGroupForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">	
						<td width="18%" align="left" class="black_ar_t">
							<%
								if(form.getBarcode()!=null)
												{
							%>
								<label for="barcode">
									<%=form.getBarcode()%>
								</label>
							<%
								}
												else
												{
							%>
								<label for="barcode">
								</label>
							<%
								}
							%>
						<html:hidden property="barcode"/>
						</td>
						</logic:equal>
						<logic:notEqual name ="specimenCollectionGroupForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
						<td align="left" valign="top" nowrap><html:text styleClass="formFieldSizedSCG" size="30"  maxlength="255" styleId="barcode" property="barcode" /></td>
						</logic:notEqual>			
					</logic:equal>
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
						<td align="left" valign="top" nowrap><html:text styleClass="formFieldSizedSCG" size="30"  maxlength="255" styleId="barcode" property="barcode" /></td>
					</logic:notEqual>
					<%
						}
					%>
				</tr>
				
				<tr>
				    <td class="black_ar_t"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
				    <td align="left" valign="top" class="black_ar"><bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/></td>
				    <td align="left" nowrap class="black_ar">
<!-- Mandar : 434 : for tooltip -->				    
				     	<html:select property="collectionProtocolEventId" styleClass="formFieldSizedSCG" styleId="collectionProtocolEventId" size="1" onchange="onChangeEvent(this)" 
				     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         	<html:options collection="<%=Constants.STUDY_CALENDAR_EVENT_POINT_LIST%>" labelProperty="name" property="value"/>				     					     					     	
						</html:select>&nbsp;
						<bean:message key="collectionprotocol.studycalendarcomment"/>
		        	</td>
					<td class="black_ar_t"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="3" /></td>
                    <td align="left" class="black_ar_t"><bean:message key="specimenCollectionGroup.site"/></td>
					<td align="left"  class="black_ar">
					 <autocomplete:AutoCompleteTag property="siteId"
										  optionsList = "<%=request.getAttribute(Constants.SITELIST)%>"
										  initialValue="<%=new Long(form.getSiteId())%>"
										  styleClass="formFieldSizedAutoSCG"
										  
										  staticField="false"										 
									    />
					

						<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						&nbsp;
						<html:link href="#" styleId="newSite" styleClass="view" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=site&forwardTo=specimenCollectionGroup&addNewFor=site')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
		        	</td>
					
					
				</tr>
				 <tr>
				 	 <td align="center" nowrap>&nbsp;</td>   
					 <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.offset" /></td>  
					 <td align="left" nowrap class="black_ar"><html:text styleClass="formFieldSizedSCG" size="10"  maxlength="10" styleId="offset" property="offset" onblur="registrationDateChange(this)"/></td>
					 <td align="center" nowrap>&nbsp;</td>
					<td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/></td>  
					<td align="left" nowrap class="black_ar">
				     	<html:text styleClass="formFieldSizedSC" size="30"  maxlength="50"  styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
					     	<!-- This feature will be implemented in next release
							&nbsp;
							<html:submit styleClass="actionButton" disabled="true">
								<bean:message key="buttons.getPathologyReport"/>
							</html:submit>
							-->
				    </td>  
				</tr>
				 <tr>
				     <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
				     <td align="left" valign="top" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/></td>
					 <td align="left" class="black_ar" colspan="1">
                             <!--<autocomplete:AutoCompleteTag property="clinicalDiagnosis"
										  optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
										  initialValue="<%=form.getClinicalDiagnosis()%>"
										  styleClass="formFieldSizedAutoSCG"
										  
					        />-->
							<!---->
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<div>
										<input property="clinicalDiagnosis" type="text" id="clinicaldiagnosis" name="clinicalDiagnosis" 
										value="<%=request.getAttribute("clinicalDiagnosis")%>" onmouseover="showTip(this.id)"/>
										</div>
									</td>			
									<td>
									<%
										String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";
									%>
									<!-- // Patch ID: Bug#3090_22 -->
									&nbsp;<!--  <a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false"><span class="black_ar"><img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="16" height="16" title='CLinical Diagnosis Selector'></span></a>--></td></tr></table>
								</td>
				     <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
				     <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalStatus"/></td>
					 <td align="left" class="black_ar">					 
					 			 <autocomplete:AutoCompleteTag property="clinicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getClinicalStatus()%>"
										   styleClass="formFieldSizedAutoSCG"
										  							 
									    />
		        	  </td>
				 </tr>
				 
			<!--	 <tr>
			     	<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.medicalRecordNumber"/></td>
				     <td align="left" nowrap>
   						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="1">
				-->
<!-- Mandar : 434 : for tooltip -->   						
				  <!--   		<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSizedSCG" styleId="participantsMedicalIdentifierId" size="1" disabled="<%=readOnlyForAll%>"
				     		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                         		<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
						<logic:equal name="specimenCollectionGroupForm" property="radioButtonForParticipant" value="2">

				-->
<!-- Mandar : 434 : for tooltip -->					     	
				<!--	     	<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSizedSCG" styleId="participantsMedicalIdentifierId" size="1" disabled="true"
					     	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                	         	<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
		        	</td>					
				 	<td align="center" nowrap>&nbsp;</td>
					<td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/></td>  
					<td align="left" nowrap class="black_ar">
				     	<html:text styleClass="formFieldSizedSC" size="30"  maxlength="50"  styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
					-->     	<!-- This feature will be implemented in next release
							&nbsp;
							<html:submit styleClass="actionButton" disabled="true">
								<bean:message key="buttons.getPathologyReport"/>
							</html:submit>
							-->
				<!--    </td>
				
				 </tr>
				 -->
				 <!--comments -->
				 <!-- 
				 * Name: Shital Lawhale
			     * Bug ID: 3052
			     * Patch ID: 3052_1_1
			     * See also: 1_1 to 1_5
				 * Description : Added <TR> for comment field .				 
				-->	 
				 <tr>
					
				<!-- activitystatus -->	
				
					<td align="center" nowrap><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
					<td align="left" class="black_ar"><bean:message key="site.activityStatus" /></td> 
					<td align="left" class="black_ar">
							<autocomplete:AutoCompleteTag property="activityStatus"
								  optionsList = "<%=request.getAttribute(Constants.ACTIVITYSTATUSLIST)%>"
								  initialValue="<%=form.getActivityStatus()%>"
								  onChange="<%=strCheckStatus%>"
								   styleClass="formFieldSizedAutoSCG"
									
							/>
					</td>
				
					<!-- collectionstatus -->
					<td align="center" nowrap><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>            
					<td align="left" nowrap class="black_ar"><bean:message key="specimenCollectionGroup.collectionStatus" /></td>
					<td align="left" class="black_ar">
							<autocomplete:AutoCompleteTag property="collectionStatus"
								optionsList = "<%=request.getAttribute(Constants.COLLECTIONSTATUSLIST)%>"
								initialValue="<%=form.getCollectionStatus()%>"
								onChange="<%=strCheckStatus%>"
								 styleClass="formFieldSizedAutoSCG"
								
							/>
					</td>
				</tr>
				 <tr><td colspan="3" class="bottomtd"></td></tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="black_ar_t"><bean:message key="app.comments"/></td>
					<td colspan="4" align="left" ><html:textarea styleClass="black_ar" rows="3"  cols="73" property="comment"/></td>
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
	
 
			
	<!--  Consent Tracking Module Virender mehta	 -->
	<%
		List requestParticipantResponse = (List)request.getAttribute("specimenCollectionGroupResponseList");
			if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
			{
	%>
	<tr>
		<td colspan="3">
		<div>
	    	<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
			</div>
			</td>
		</tr>
		
	<%
				}
			%>
	<!--  Consent Tracking Module Virender mehta -->	

		<tr>
			<td colspan="3">
				<div >
				<%@ include file="CollAndRecEvents.jsp" %>
				</div>
			</td>
		</tr>
		 <tr><td colspan="3" class="bottomtd"></td></tr>
	<!--
 * Name : Ashish Gupta
 * Reviewer Name : Sachin Lale 
 * Bug ID: Multiple Specimen Bug
 * Patch ID: Multiple Specimen Bug_1 
 * See also: 1-8
 * Description: Table to display number of specimens text field
	-->

	<!-- For Multiple Specimen-----Ashish -->
		<div id="multiplespecimenTable">
		<tr>		
		<td colspan="3" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="multipleSpecimen.mainTitle" /> </span></td>
		</tr>
		<tr>
          <td colspan="3"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
					 <td width="1%" align="left" class="black_ar">&nbsp;</td>
					 <td width="34%" align="left" class="black_ar">&nbsp;<bean:message key="multipleSpecimen.numberOfSpecimen" /></td>
					 <td width="65%" align="left" nowrap>
						<!-- html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimen"  /-->
						<html:text styleClass="black_ar" style="text-align:right" maxlength="50" size="20" styleId="numberOfSpecimen" property="numberOfSpecimens" />
					</td>
			</tr>			
			</table></td>
			 </tr>
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
	</div>
	
	<%@ include file="SpecimenCollectionGroupPageButtons.jsp" %>
	</table>
	</div>