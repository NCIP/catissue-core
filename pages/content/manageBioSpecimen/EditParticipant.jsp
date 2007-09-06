<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	   		   
			
		<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
		<%--logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" >
			 	 
				  <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="user.searchTitle"/>
				     </td>
				  </tr>
				  
				  <tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="user.identifier"/>
							</label>
						</td>
					    <td class="formField">
					    	<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier" readonly="<%=readOnlyForAll%>"/>
					    </td>
				  </tr>	

				 <%
					String changeAction = "setFormAction('"+Constants.PARTICIPANT_SEARCH_ACTION+"');setOperation('"+Constants.SEARCH+"');";
				 %>
 
				  <tr>
				   <td align="right" colspan="3">
					 <table cellpadding="4" cellspacing="0" border="0">
						 <tr>
						    <td>
						    	<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>"/></td>
						 </tr>
					 </table>
				   </td>
				  </tr>

				 </table>
			    </td>
			  </tr>
			  <!-- ENTER IDENTIFIER ENDS-->
			  	</logic:notEqual>
			  </logic:notEqual--%>
			  
			   	

	  <!-- NEW PARTICIPANT REGISTRATION BEGINS-->
		<tr><td>
			 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td>
						<input type="hidden" name="participantId" value="<%=participantId%>"/>
						<input type="hidden" name="cpId" id="cpId"/>
						<input type="hidden" name="radioValue"/>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value="<%=forwardTo%>"/>
					</td>
					<td><html:hidden property="valueCounter"/></td>
					<td><html:hidden property="collectionProtocolRegistrationValueCounter"/></td>
					<td><html:hidden property="onSubmit" /></td>
					<td><html:hidden property="id" /><html:hidden property="redirectTo"/></td>
					<td><html:hidden property="pageOf" value="<%=pageOf%>"/></td>
				 </tr>
				 
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>

				 <%--<tr>
				     <td class="formTitle" height="20" colspan="6">
				     <%title = "participant."+pageView+".title";%>
				     <bean:message key="participant.add.title"/>
					<%
						if(pageView.equals("edit"))
						{
					%>
				     &nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="participantForm" property="id" />
					<%
						}
					%>
				     </td>
				 </tr>--%>
				 
				 
				 <tr>
				 	<td class="formTitle" height="20" colspan="7">
					 <logic:equal name="operation" value="<%=Constants.ADD%>">
						<bean:message key="participant.add.title"/>
					</logic:equal>
					<logic:equal name="operation" value="<%=Constants.EDIT%>">
						<bean:message key="participant.edit.title"/>
					</logic:equal>
					</td>
				</tr>
				
				 <tr>
					 <td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					 <td class="formLabelBorderless">
				     	<label for="socialSecurityNumber">
				     		<bean:message key="participant.socialSecurityNumber"/>
				     	</label>
				     </td>
				     <td class="formFieldWithoutBorder" colspan="5">
				     	<html:text styleClass="formFieldSized2" maxlength="3" styleId="socialSecurityNumberPartA" property="socialSecurityNumberPartA" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartB');"/>
				     	-
				     	<html:text styleClass="formFieldSized1" maxlength="2" styleId="socialSecurityNumberPartB" property="socialSecurityNumberPartB" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartC');"/>
				     	-
				     	<html:text styleClass="formFieldSized3" maxlength="4" styleId="socialSecurityNumberPartC" property="socialSecurityNumberPartC" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     </td>
				 </tr>
				 	
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formFieldWithoutBorder" align="right">
						<table summary="" cellpadding="3" cellspacing="0" border="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="formLabelBorderless">
									<label for="Name">
										<bean:message key="participant.Name"/>
									</label>
								</td>
							</tr>
						 </table>
					 </td>
				    <td class="formFieldWithoutBorder" colspan="5">
					 <table summary="" cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td class="formLabelBorderlessLeft">
								<label for="lastName">
									<bean:message key="participant.lastName"/>
								</label>
							 </td>
							 <td class="formLabelBorderlessLeft">
								<label for="firstName">
									<bean:message key="participant.firstName"/>
								</label>
							  </td>
							<td class="formLabelBorderlessLeft">
								<label for="middleName">
									<bean:message key="participant.middleName"/>
								</label>
							 </td>
						</tr>
					 	<tr>
							 <td class="formFieldWithoutBorder">
								<html:text styleClass="formFieldSized10" maxlength="255" styleId="lastName" name="participantForm" property="lastName" readonly="<%=readOnlyForAll%>" onkeyup="moveToNext(this,this.value,'firstName')"/>
							 </td>
					     	 <td class="formFieldWithoutBorder">
					     		<html:text styleClass="formFieldSized10" maxlength="255" styleId="firstName" property="firstName" readonly="<%=readOnlyForAll%>" onkeyup="moveToNext(this,this.value,'middleName')"/>
							</td>
					     	 <td class="formFieldWithoutBorder">
					     		<html:text styleClass="formFieldSized10" maxlength="255" styleId="middleName" property="middleName" readonly="<%=readOnlyForAll%>"/>
							</td>
						</tr>
					 </table>
				    </td>
				 </tr>
				 
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
						<label for="birthDate">
							<bean:message key="participant.birthDate"/>
						</label>
					</td>
					 
					 <td class="formFieldWithoutBorder" colspan="5">
<%
	 if(currentBirthDate.trim().length() > 0)
	{
			Integer birthYear = new Integer(Utility.getYear(currentBirthDate ));
			Integer birthMonth = new Integer(Utility.getMonth(currentBirthDate ));
			Integer birthDay = new Integer(Utility.getDay(currentBirthDate ));
%>
			<ncombo:DateTimeComponent name="birthDate"
									  id="birthDate"
 									  formName="participantForm"	
									  month= "<%=birthMonth %>"
									  year= "<%=birthYear %>"
									  day= "<%= birthDay %>" 
									  value="<%=currentBirthDate %>"
									  styleClass="formDateSized10"
											 />		
<% 
	}
	else
	{  
 %>
			<ncombo:DateTimeComponent name="birthDate"
									  id="birthDate"
 									  formName="participantForm"	
									  styleClass="formDateSized10" 
											 />		
<%
	}
%>
<bean:message key="page.dateFormat" />&nbsp;
					 </td>
				 </tr>			
				 
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
				     	<label for="vitalStatus">
				     		<bean:message key="participant.vitalStatus"/>
				     	</label>
				     </td>
				     <td class="formFieldWithoutBorder" colspan="5">
<!-- Mandar : 434 : for tooltip -->
						<%--
				     	<html:select property="vitalStatus" styleClass="formFieldSized" styleId="vitalStatus" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.VITAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>--%>
						<logic:iterate id="nvb" name="<%=Constants.VITAL_STATUS_LIST%>">
						<%	NameValueBean nameValueBean=(NameValueBean)nvb;%>
						<html:radio property="vitalStatus" onclick="onVitalStatusRadioButtonClick(this)" value="<%=nameValueBean.getValue()%>"><%=nameValueBean.getName()%> </html:radio>
						</logic:iterate>
						
		        	  </td>
				 </tr>
				 
				
				<%-- added by chetan for death date --%>
 <tr>
	<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
	<td class="formLabelBorderless">
		<label for="deathDate">
			<bean:message key="participant.deathDate"/>
		</label>
	</td>	 
	<td class="formFieldWithoutBorder" colspan="5">
<%

	ParticipantForm form = (ParticipantForm) request.getAttribute("participantForm");
	Boolean deathDisable = new Boolean("false");
	if(!form.getVitalStatus().trim().equals("Dead"))
	{
		deathDisable = new Boolean("true");
	}
	 if(currentDeathDate.trim().length() > 0)
	{
			Integer deathYear = new Integer(Utility.getYear(currentDeathDate ));
			Integer deathMonth = new Integer(Utility.getMonth(currentDeathDate ));
			Integer deathDay = new Integer(Utility.getDay(currentDeathDate ));
%>
			<ncombo:DateTimeComponent name="deathDate"
									  id="deathDate"
 									  formName="participantForm"	
									  month= "<%=deathMonth %>"
									  year= "<%=deathYear %>"
									  day= "<%= deathDay %>" 
									  value="<%=currentDeathDate %>"
									  styleClass="formDateSized10"
									  disabled="<%=deathDisable%>"
											 />		
<% 
	}
	else
	{  
 %>
			<ncombo:DateTimeComponent name="deathDate"
									  id="deathDate"
 									  formName="participantForm"	
									  styleClass="formDateSized10" 
									  disabled="<%=deathDisable%>"
											 />		
<%
	}
%>
<bean:message key="page.dateFormat" />&nbsp;
	</td>
</tr> 
				 
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
				     	<label for="gender"><bean:message key="participant.gender"/></label>
				     </td>
				     <td class="formFieldWithoutBorder" colspan="5">
<!-- Mandar : 434 : for tooltip -->
				     	<%--<html:select property="gender" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.GENDER_LIST%>" labelProperty="name" property="value"/>
						</html:select>--%>
						<logic:iterate id="nvb" name="<%=Constants.GENDER_LIST%>">
						<%	NameValueBean nameValueBean=(NameValueBean)nvb;%>
						<html:radio property="gender" value="<%=nameValueBean.getValue()%>"><%=nameValueBean.getName()%> </html:radio>
						</logic:iterate>
						
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
						<label for="genotype"><bean:message key="participant.genotype"/></label>
					</td>
				     <td class="formFieldWithoutBorder" colspan="5">
					 
					  <autocomplete:AutoCompleteTag property="genotype"
										  optionsList = "<%=request.getAttribute(Constants.GENOTYPE_LIST)%>"
										  initialValue="<%=form.getGenotype()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
					     <label for="race"><bean:message key="participant.race"/></label>
				     </td>
				     <td class="formFieldWithoutBorder" colspan="5">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="raceTypes" styleClass="formFieldSized" styleId="race" size="4" multiple="true" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">&nbsp;</td>
					<td class="formLabelBorderless">
				     	<label for="ethnicity">
				     		<bean:message key="participant.ethnicity"/>
				     	</label>
				     </td>
				     <td class="formFieldWithoutBorder" colspan="5">
					 
					   <autocomplete:AutoCompleteTag property="ethnicity"
										  optionsList = "<%=request.getAttribute(Constants.ETHNICITY_LIST)%>"
										  initialValue="<%=form.getEthnicity()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 
				 <!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNoticeWithoutBorder" width="5">*</td>
					<td class="formLabelBorderless" >
						<label for="activityStatus">
							<bean:message key="participant.activityStatus" />
						</label>
					</td>
					<td class="formFieldWithoutBorder" colspan="5">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
				
				 <!-- Medical Identifiers Begin here -->
				   <tr>
				 	<td align="left" colspan="7" valign="top">
					<table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
				     <td class="formTitle" height="20" colspan="5">
				     	<bean:message key="participant.medicalIdentifier"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formTitle" align="Right">
						<html:button property="deleteMedicalIdentifierValue" styleClass="actionButton" onclick="deleteChecked('addMore','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true',document.forms[0].valueCounter,'chk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				  </tr>
				 <tr>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="medicalrecord.source"/>
					</td>
				    <td class="formSubTitleWithoutBorder" colspan="3">
						<bean:message key="medicalrecord.number"/>
					</td>
					<td class="formSubTitleWithoutBorder" colspan="3">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
				 </tr>
				 <script> document.forms[0].valueCounter.value = <%=noOfRows%> </script>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String siteName = "value(ParticipantMedicalIdentifier:"+i+"_Site_id)";
					String medicalRecordNumber = "value(ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber)";
					String identifier = "value(ParticipantMedicalIdentifier:" + i +"_id)";
					String check = "chk_"+i;
				%>
				 <tr>
					<td class="formFieldWithoutBorder">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=siteName%>" styleClass="formFieldSized10" styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formFieldWithoutBorder" colspan="3">
				     	<html:text styleClass="formFieldSized10" maxlength="50" size="30" styleId="<%=medicalRecordNumber%>" property="<%=medicalRecordNumber%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    	<%
							String key = "ParticipantMedicalIdentifier:" + i +"_id";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formFieldWithoutBorder" width="5" colspan="3">
							<html:hidden property="<%=identifier%>" />
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteMedicalIdentifierValue,document.forms[0].valueCounter,'chk_')">		
						</td>
				    
				 </tr>
				 <%
				}
				%>
				 </tbody>
				  					
<!-- Medical Identifiers End here -->
				 
				 
				 
<!-- Participant Registration Begin here -->
				
				   <tr>
				     <td class="formTitle" height="20" colspan="5">
				     	<bean:message key="participant.collectionProtocolReg"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="participantRegRow('addMoreParticipantRegistration')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formTitle" align="Right">
						<html:button property="deleteParticipantRegistrationValue" styleClass="actionButton" onclick="deleteChecked('addMoreParticipantRegistration','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true&deleteRegistration=true',document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				  </tr>
				 <tr>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.protocolTitle"/>
					</td>
				    <td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.participantProtocolID"/>
					</td>
					<td class="formSubTitleWithoutBorder" colspan="2">
						<bean:message key="participant.collectionProtocolReg.participantRegistrationDate"/>
					</td>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.activityStatus" />
					</td>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.consent"/>
					</td>
					<td class="formSubTitleWithoutBorder">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
					</td>
				 </tr>
				<script> document.forms[0].collectionProtocolRegistrationValueCounter.value = <%=noOrRowsCollectionProtocolRegistration%> </script>
				 
				<tbody id="addMoreParticipantRegistration">
				<%
				for(int i=1;i<=noOrRowsCollectionProtocolRegistration;i++)
				{
					String collectionProtocolTitle = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"+i+"_CollectionProtocol_id)";
					String collectionProtocolParticipantId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"+i+"_protocolParticipantIdentifier)";
					String collectionProtocolRegistrationDate = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_registrationDate)";
					String collectionProtocolIdentifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_id)";
					String collectionProtocolRegistrationActivityStatus = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_activityStatus)";
					String collectionProtocolCheck = "CollectionProtocolRegistrationChk_"+i;
					String key = "CollectionProtocolRegistration:" + i +"_id";
					String collectionProtocolConsentCheck = "CollectionProtocolConsentChk_"+i;
					String anchorTagKey = "ConsentCheck_"+i;
					String consentCheckStatus="consentCheckStatus_"+i;
					
					String consentResponseDisplay = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_isConsentAvailable)";
					String consentResponseDisplayKey = "CollectionProtocolRegistration:" + i +"_isConsentAvailable";
					String consentResponseDisplayValue = (String)form.getCollectionProtocolRegistrationValue(consentResponseDisplayKey);
					
					
					if(consentResponseDisplayValue ==null)
					{
						consentResponseDisplayValue = Constants.NO_CONSENTS_DEFINED;
					}
					boolean CollectionProtocolRegConditionBoolean = Utility.isPersistedValue(mapCollectionProtocolRegistration,key);
					boolean activityStatusCondition=false;
					if(!CollectionProtocolRegConditionBoolean)
						activityStatusCondition = true;
					
					String onChangeFun ="getConsent('"+collectionProtocolConsentCheck+"', '"+collectionProtocolTitle+"','"+i+"','"+anchorTagKey+"','"+consentCheckStatus+"')";

				%>
					
				 <tr>
					<td class="formFieldWithoutBorder" width="40">
				 		<html:select property="<%=collectionProtocolTitle%>" styleClass="formFieldSized15" styleId="<%=collectionProtocolTitle%>" size="1" 
				 		onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=onChangeFun%>">
						    <html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>															
					    </html:select>
					</td>
				    <td class="formFieldWithoutBorder">
						<html:text styleClass="formFieldSized10" maxlength="50"  styleId="<%=collectionProtocolParticipantId%>" property="<%=collectionProtocolParticipantId%>" />
					</td>
				    <td class="formFieldWithoutBorder" colspan="2">
				    	<!-- <html:text styleClass="formFieldSized15" maxlength="50"  styleId="<%=collectionProtocolRegistrationDate%>" property="<%=collectionProtocolRegistrationDate%>" onclick = "this.value = ''" onblur = "if(this.value=='') {this.value = 'MM-DD-YYYY or MM/DD/YYYY';}" onkeypress="return titliOnEnter(event, this, document.getElementById('<%=collectionProtocolRegistrationDate%>'))"/> -->
				    	<html:text styleClass="formFieldSized10" maxlength="50"  styleId="<%=collectionProtocolRegistrationDate%>" property="<%=collectionProtocolRegistrationDate%>" />
				    </td>
					<td class="formFieldWithoutBorder">
						<html:select property="<%=collectionProtocolRegistrationActivityStatus%>" styleClass="formFieldSized10" styleId="<%=collectionProtocolRegistrationActivityStatus%>" size="1" disabled='<%=activityStatusCondition%>' onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				    <td class="formFieldWithoutBorder">
				    	<span id="<%=consentCheckStatus%>">
						<%
							
							if(!consentResponseDisplayValue.equals(Constants.NO_CONSENTS_DEFINED))
							{
								if(operation.equals(Constants.EDIT))
								{
									consentResponseDisplayValue = Constants.PARTICIPANT_CONSENT_EDIT_RESPONSE;
								}
						%>
								<a id="<%=anchorTagKey%>" href="javascript:openConsentPage('<%=collectionProtocolTitle%>','<%=i%>','<%=consentResponseDisplayValue%>')">
								<%=consentResponseDisplayValue%><br>
								<input type='hidden' name="<%=collectionProtocolConsentCheck%>" value='Consent' id="<%=collectionProtocolConsentCheck%>" >
								<input type='hidden' name="<%=consentResponseDisplay%>" value="<%=consentResponseDisplayValue%>" id="<%=consentResponseDisplay%>" >
								</a>
						<%
							}
							else
							{
						%>
								<%=consentResponseDisplayValue%>
								<input type='hidden' name="<%=collectionProtocolConsentCheck%>" value='Consent' id="<%=collectionProtocolConsentCheck%>" >
								<input type='hidden' name="<%=consentResponseDisplay%>" value="<%=consentResponseDisplayValue%>" id="<%=consentResponseDisplay%>" >
							
						<%
							}
						%>
						</span>
					</td>
				       <%
							String CollectionProtocolRegCondition = "";
							if(CollectionProtocolRegConditionBoolean)
								CollectionProtocolRegCondition = "disabled='disabled'";
						%>
					<td class="formFieldWithoutBorder" width="5">
						<html:hidden property="<%=collectionProtocolIdentifier%>" />
						<input type=checkbox name="<%=collectionProtocolCheck %>" id="<%=collectionProtocolCheck %>" <%=CollectionProtocolRegCondition%> onClick="javascript:enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')"> 		
					</td>
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 </table>
				</td>
			 	</tr>
				 
				 
				  
<!-- Participant Registration End here -->
				  
				  <tr><td colspan=7>&nbsp;</td></tr>
				  
				  <!---Following is the code for Data Grid. Participant Lookup Data is displayed-->
				<%if(request.getAttribute(Constants.SPREADSHEET_DATA_LIST)!=null && dataList.size()>0){
					isRegisterButton=true;
					if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){
						isRegisterButton=false;
					}%>	
				
			
				<tr><td colspan="7"><table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
				     <td class="formTitle" height="25">
				     	<bean:message key="participant.lookup"/>
				     </td>
       		    </tr>				
	  			<tr height=110 valign=top>
					<td valign=top class="formFieldAllBorders">
<!--  **************  Code for New Grid  *********************** -->
			<script>
					function participant(id)
					{
						//do nothing
						//mandar for grid
						var cl = mygrid.cells(id,mygrid.getColumnCount()-1);
						var pid = cl.getValue();
						//alert(pid);
						/* 
							 Resolved bug# 4240
	                    	 Name: Virender Mehta
	                    	 Reviewer: Sachin Lale
	                    	 Description: removed URL On  onclick
	                     */
						 document.forms[0].submittedFor.value = "AddNew";
						 var pageOf = "<%=pageOf%>";
						if(pageOf == "<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>")
						{
							window.location.href = 'CPQueryParticipantSelect.do?submittedFor=AddNew&operation=add&participantId='+pid
						}
						else
						{
							window.location.href = 'ParticipantLookup.do?submittedFor=AddNew&operation=add&participantId='+pid
						}						
					} 				

					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "participant";	
			</script>
			<%@ include file="/pages/content/search/AdvanceGrid.jsp" %>
<!--  **************  Code for New Grid  *********************** -->

					</td>
				  </tr>
				  <tr>
				 	<td align="center" colspan="7" class="formFieldWithNoTopBorder">
						<INPUT TYPE='RADIO' NAME='chkName' value="Add" onclick="CreateNewClick()"><font size="2">Ignore matches and create new participant </font></INPUT>&nbsp;&nbsp;
						<INPUT TYPE='RADIO' NAME='chkName' value="Lookup" onclick="LookupAgain()" checked=true><font size="2">Lookup again </font></INPUT>
					</td>
				</tr>		
				</table></td></tr>
				<%}%>
				<!--Participant Lookup end-->				
								 <!-----action buttons-->
				 <tr>
				 	<td align="right" colspan="7" valign="top">
						<%
							String changeAction = "setFormAction('"+formName+"')";
						%>
						<!-- action buttons begins -->

						<table cellpadding="4" cellspacing="0" border="0">
							<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
							<% 
								isAddNew=true;
							%>
							</logic:equal>
							
							<tr>
								<%--
									String normalSubmitFunctionName = "setSubmittedForParticipant('" + submittedFor+ "','" + Constants.PARTICIPANT_FORWARD_TO_LIST[0][1]+"')";
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[1][1]+"')";									
									String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
									String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
									String forwardToSubmit = forwardToSubmitFunctionName + ","+confirmDisableFuncName;
								--%>
								<%
								    
									String normalSubmitFunctionName = "setSubmittedForParticipant('" + submittedFor+ "','" + Constants.PARTICIPANT_FORWARD_TO_LIST[0][1]+"')";
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[3][1]+"')";
									String forwardToSCGFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[2][1]+"')";
									String normalSubmit = normalSubmitFunctionName ;
									String forwardToSubmit = forwardToSubmitFunctionName ;
									String forwardToSCG = forwardToSCGFunctionName ;

								%>
																
								<!-- PUT YOUR COMMENT HERE -->

								
								<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit Only"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
											onclick="<%=forwardToSubmit%>">
									</html:button>
								</td>
								</logic:equal>
								
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit Only"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
											onclick="<%=normalSubmit%>">
									</html:button>
								</td>
								</logic:notEqual>
								
								<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit and register Specimen Collection Group"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[2][0]%>"
											onclick="<%=forwardToSCG%>">
									</html:button>
								</td>
								</logic:equal>
							
								
								<%--<td>
									<html:submit styleClass="actionButton" disabled="true">
							   		<bean:message key="buttons.getClinicalData"/>
									</html:submit>
								</td>	--%>
							</tr>
						</table>
							<!-- action buttons end -->
			  		</td>
			 	 </tr>
								 
				<!--	extra </logic:notEqual>-->
				
				 <!-- end --> 
			</table>
		</td></tr>
	</table>			
