<!--script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script-->
<!--script type="text/javascript" src="jss/combos.js"></script-->
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
<c:set var="tr_white_color" value="tr_alternate_color_white" />
<c:set var="tr_grey_color" value="tr_alternate_color_lightGrey" />
<c:set var="i" value="1" scope="request" />

<%
	/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale
 			* Bug ID: 2741
 			* Patch ID: 2741_18
 			* Description: Adding check for changes function
	*/
String printaction = "CPQueryPrintSCGAdd";
if(Constants.ADD.equals(operation))
{
	 printaction = "CPQueryPrintSCGAdd";
}
else if(Constants.EDIT.equals(operation))
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
	if(Constants.PAGE_OF_SCG_CP_QUERY.equals(pageOf))
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
	if(Constants.EDIT.equals(operation))
	{
		confirmDisableFuncName = "confirmDisableForSCG('" + formName +"',document.forms[0].activityStatus)";
		normalSubmit = "checkForChanges(),"+normalSubmitFunctionName + ","+confirmDisableFuncName;
		forwardToSubmit = "checkForChanges(),"+ forwardToSubmitFuctionName + ","+confirmDisableFuncName;

		if(Constants.PAGE_OF_SCG_CP_QUERY.equals(pageOf))
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
						<html:hidden property="id" styleId="id"/>
						<html:hidden property="onSubmit"/>
						<html:hidden property="redirectTo" value="<%=reqPath%>"/>
						<html:hidden property="withdrawlButtonStatus"/>
						<c:if  test="${operation eq 'edit'}">
							<html:hidden property="collectionProtocolEventId"/>
						</c:if>

				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>
				<c:set var="i" value="${i+1}" scope="request" />

				 <td width="20%" align="right" valign="top" class="black_ar">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
						<b><bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/> </b>
					</td>
				    
					<td width="30%"  align="left" nowrap class="black_ar align_left_style1">
					<!-- Mandar : 434 : for tooltip -->
						<c:choose>
							<c:when  test="${operation eq 'edit'}">
								${CollectionEventPointLabel}
							</c:when>
							<c:otherwise>
								<html:select property="collectionProtocolEventId" styleClass="formFieldSizedSCG" styleId="collectionProtocolEventId" size="1" 
								>
								<html:options collection="<%=Constants.STUDY_CALENDAR_EVENT_POINT_LIST%>" labelProperty="name" property="value"/>
								</html:select>&nbsp;
								
								
							</c:otherwise>
						</c:choose>
		
		        	</td>
						<td width="20%" align="right" class="black_ar">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
					<b><bean:message key="site.activityStatus" /></b></td>
					<td width="30%" align="left" class="black_ar  align_left_style1">
					<html:select property="activityStatus" styleClass="black_ar" styleId="activityStatus" size="1">
								<logic:iterate name="activityStatusList" id="listStatusId">
									<html:option  value="${listStatusId}"> ${listStatusId} </html:option>
							    </logic:iterate>
					</html:select>
					</td>
				</tr>
				
				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>

				
				<%
					if((!Variables.isSpecimenCollGroupLabelGeneratorAvl) || Constants.EDIT.equals(operation))
						{
				%>
					<c:set var="i" value="${i+1}" scope="request" />	
					<td width="20%" align="right" valign="top" class="black_ar">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="3" />	
						<span class="black_ar"><label for="name">
							<b>	<bean:message key="specimenCollectionGroup.groupName" /> </b></label>
						</span>
					</td>
					<td width="30%"  align="left" nowrap class="black_ar align_left_style1">
							<html:text  styleClass="newformFieldSizedSC"   size="26"  maxlength="255" styleId="name" property="name" />
					</td>

				<%
									}%>

				   
				<%if((!Variables.isSpecimenCollGroupBarcodeGeneratorAvl) || Constants.EDIT.equals(operation))
										{
								%>
					<td width="20%" align="right" class="black_ar">
					<b><bean:message key="specimenCollectionGroup.barcode" /> </b></td>
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
						<logic:equal name ="specimenCollectionGroupForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
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
								<label for="barcode"><b></b>
								</label>
							<%
								}
							%>
						<html:hidden property="barcode"/>
						</logic:equal>
						<logic:notEqual name ="specimenCollectionGroupForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
							<td width="30%"  align="left" nowrap class="black_ar align_left_style1">
								<html:text styleClass="newformFieldSizedSCG"  size="26"  maxlength="255" styleId="barcode" property="barcode" />
						    </td>
						</logic:notEqual>
					   </logic:equal>
					   <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
						 <td width="30%"  align="left" nowrap class="black_ar align_left_style1">
								<html:text styleClass="newformFieldSizedSCG" size="26"  maxlength="255" styleId="barcode" property="barcode" />
						</td>
					  </logic:notEqual>
					<%
						}
					%>
					
				</tr>
				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>
				<c:set var="i" value="${i+1}" scope="request" />
				<!-- activitystatus -->
				
					
                    <td  width="20%" align="right" class="black_ar">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />	
						<b><bean:message key="specimenCollectionGroup.site"/></b>
					</td>
					<td width="30%" align="left"  class="black_ar align_left_style1">
					
						<html:select property="siteId" styleClass="black_ar" styleId="siteId" size="1">
							       <html:options collection="siteList" labelProperty="name" property="value" />
					    </html:select>
						
					   <logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.PAGE_OF_SCG_CP_QUERY%>">
						&nbsp;
						<html:link href="#" styleId="newSite" styleClass="view" onclick="addNewAction('SpecimenCollectionGroupAddNew.do?addNewForwardTo=site&forwardTo=specimenCollectionGroup&addNewFor=site')">
							<bean:message key="buttons.addNew" />
						</html:link>
						</logic:notEqual>
		        	</td>

					<!-- collectionstatus -->
					<td width="20%" align="right" nowrap class="black_ar">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
					<b>	<bean:message key="specimenCollectionGroup.collectionStatus" /> </b></td>
					<td  width="30%" align="left" class="black_ar align_left_style1"> 
					<html:select property="collectionStatus" styleClass="black_ar" styleId="collectionStatus" size="1">
								   <logic:iterate name="collectionStatusList" id="collectionStatusId">
										 <html:option  value="${collectionStatusId}" > ${collectionStatusId} </html:option>
							       </logic:iterate>
					</html:select>
					
					</td>
				</tr>
				
				
				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>
				<c:set var="i" value="${i+1}" scope="request" />
				  	 
					 <td width="20%" align="right" class="black_ar"><b><bean:message key="specimenCollectionGroup.offset" /></b></td>
					 <td width="30%" align="left" nowrap class="black_ar align_left_style1">
						<html:text styleClass="newformFieldSizedSCG" size="26"  maxlength="10" styleId="offset" property="offset" onblur="registrationDateChange(this)"/></td>
					 
					<td  width="20%"align="right" class="black_ar"><b><bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/></b></td>
					<td  width="30%" align="left" nowrap class="black_ar align_left_style1">
				     	<html:text styleClass="newformFieldSizedSC" size="26"  maxlength="50"  styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
					     	<!-- This feature will be implemented in next release
							&nbsp;
							<html:submit styleClass="actionButton" disabled="true">
								<bean:message key="buttons.getPathologyReport"/>
							</html:submit>
							-->
				    </td>
				</tr>
				
				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>
				<c:set var="i" value="${i+1}" scope="request" />
				     <td  width="20%" align="right" valign="top" class="black_ar">
					 <img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
					 <b> <bean:message key="specimenCollectionGroup.clinicalDiagnosis"/> </b></td>
					 <td width="30%" align="left" class="black_ar align_left_style1" colspan="1">
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
										   <html:select property="clinicalDiagnosis" styleClass="black_ar" styleId="clinicalDiagnosis" size="1">
											</html:select>
										</div>
									</td>
									<script>
									var clinicalDiagnosisValue = '${clinicalDiagnosis}';
					
					</script>
									<td>
									<%
										String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";
									%>
									<!-- // Patch ID: Bug#3090_22 -->
									&nbsp;<!--  <a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false"><span class="black_ar"><img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="16" height="16" title='CLinical Diagnosis Selector'></span></a>--></td></tr></table>
								</td>
				     <td width="20%" align="right" class="black_ar">
					 <img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
					 <b><bean:message key="specimenCollectionGroup.clinicalStatus"/></b></td>
					 <td width="30%" align="left" class="black_ar align_left_style1">
					 		<html:select property="clinicalStatus"
							             styleClass="black_ar" styleId="clinicalStatus" size="1">
							       <html:options collection="cinicalStatusList" labelProperty="name" property="value" />
					        </html:select>	
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
				 				
				<c:if test="${i%2 == 0}">
					<tr class="${tr_white_color}">
				</c:if>
				<c:if test="${i%2 == 1}">
					<tr class="${tr_grey_color}">
				</c:if>
				<c:set var="i" value="${i+1}" scope="request" />
				
					<td width="20%" align="right" class="black_ar_t"><b><bean:message key="app.comments"/></b></td>
					<td width="30%" colspan="4" align="left"  class="align_left_style1">
						<html:textarea styleClass="black_ar" style="width: 64%; height:40px;" property="comment"/>
						
							<%
					 			String changeAction = "setFormAction('"+formName+"')";
							%>
					</td>
					</tr>
			</table>
		</td>
	</tr>
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION ENDS-->
		<tr>
			<td colspan="3">
				<div >
				<%@ include file="CollAndRecEvents.jsp" %>
				</div>
			</td>
		</tr>

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