	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	   		   
			
		<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
		<%--logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	 
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
					<td><html:hidden property="counter"/></td>
					<td><html:hidden property="onSubmit" /></td>
					<td><html:hidden property="id" /><html:hidden property="redirectTo"/></td>
					<td><html:hidden property="pageOf" value="<%=pageOf%>"/></td>
				 </tr>
				 
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>

				 <%--<tr>
				     <td class="formTitle" height="20" colspan="4">
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
				 	<td class="formTitle" height="20" colspan="4"">
					 <logic:equal name="operation" value="<%=Constants.ADD%>">
						<bean:message key="participant.add.title"/>
					</logic:equal>
					<logic:equal name="operation" value="<%=Constants.EDIT%>">
						<bean:message key="participant.edit.title"/>
					</logic:equal>
					</td>
				</tr>	
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="lastName">
				     		<bean:message key="user.lastName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     <html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="lastName" name="participantForm" property="lastName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				  <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="firstName">
				     		<bean:message key="user.firstName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="firstName" property="firstName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="middleName">
				     		<bean:message key="participant.middleName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     <html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="middleName" property="middleName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="birthDate">
							<bean:message key="participant.birthDate"/>
						</label>
					</td>
					 
					 <td class="formField" colspan="2">
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="vitalStatus">
				     		<bean:message key="participant.vitalStatus"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
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
	<td class="formRequiredNotice" width="5">&nbsp;</td>
	<td class="formLabel">
		<label for="deathDate">
			<bean:message key="participant.deathDate"/>
		</label>
	</td>	 
	<td class="formField" colspan="2">
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="gender"><bean:message key="participant.gender"/></label>
				     </td>
				     <td class="formField" colspan="2">
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="genotype"><bean:message key="participant.genotype"/></label>
					</td>
				     <td class="formField" colspan="2">
					 
					  <autocomplete:AutoCompleteTag property="genotype"
										  optionsList = "<%=request.getAttribute(Constants.GENOTYPE_LIST)%>"
										  initialValue="<%=form.getGenotype()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
					     <label for="race"><bean:message key="participant.race"/></label>
				     </td>
				     <td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="raceTypes" styleClass="formFieldSized" styleId="race" size="4" multiple="true" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="ethnicity">
				     		<bean:message key="participant.ethnicity"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
					 
					   <autocomplete:AutoCompleteTag property="ethnicity"
										  optionsList = "<%=request.getAttribute(Constants.ETHNICITY_LIST)%>"
										  initialValue="<%=form.getEthnicity()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 <tr>
					 <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="socialSecurityNumber">
				     		<bean:message key="participant.socialSecurityNumber"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized2" maxlength="3" styleId="socialSecurityNumberPartA" property="socialSecurityNumberPartA" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartB');"/>
				     	-
				     	<html:text styleClass="formFieldSized1" maxlength="2" styleId="socialSecurityNumberPartB" property="socialSecurityNumberPartB" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartC');"/>
				     	-
				     	<html:text styleClass="formFieldSized3" maxlength="4" styleId="socialSecurityNumberPartC" property="socialSecurityNumberPartC" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     </td>
				 </tr>
				 
				 <!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="participant.activityStatus" />
						</label>
					</td>
					<td class="formField" colspan="2">
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
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="participant.medicalIdentifier"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formTitle" align="Right">
						<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addMore','Participant.do?operation=<%=operation%>&pageOf=pageOfParticipant&status=true',document.forms[0].counter,'chk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="medicalrecord.source"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="medicalrecord.number"/>
					</td>
					<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
				 </tr>
				 
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
				 	<td class="formSerialNumberField" width="5"><%=i%>.
				 		<html:hidden property="<%=identifier%>" />
				 	</td>
				    <td class="formField">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=siteName%>" styleClass="formFieldSized15" styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized15" maxlength="50" size="30" styleId="<%=medicalRecordNumber%>" property="<%=medicalRecordNumber%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    <%
							String key = "ParticipantMedicalIdentifier:" + i +"_id";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')">		
						</td>
				    
				 </tr>
				 <%
				}
				%>
				 </tbody>
								
				  					
				  <!-- Medical Identifiers End here -->
				  <tr><td colspan=4>&nbsp;</td></tr>
				  
				  <!---Following is the code for Data Grid. Participant Lookup Data is displayed-->
				<%if(request.getAttribute(Constants.SPREADSHEET_DATA_LIST)!=null && dataList.size()>0){
					isRegisterButton=true;
					if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){
						isRegisterButton=false;
					}%>	
				
			
				<tr><td colspan="4"><table summary="" cellpadding="0" cellspacing="0" border="0" width="600">
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
						//------------
						window.location.href = 'ParticipantSelect.do?pageOf=<%=pageOf%>&operation=add&participantId='+pid
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
				 	<td align="center" colspan="4" class="formFieldWithNoTopBorder">
						<INPUT TYPE='RADIO' NAME='chkName' value="Add" onclick="CreateNewClick()"><font size="2">Ignore matches and create new participant </font></INPUT>&nbsp;&nbsp;
						<INPUT TYPE='RADIO' NAME='chkName' value="Lookup" onclick="LookupAgain()" checked=true><font size="2">Lookup again </font></INPUT>
					</td>
				</tr>		
				</table></td></tr>
				<%}%>
				<!--Participant Lookup end-->				
								 <!-----action buttons-->
				 <tr>
				 	<td align="center" colspan="4" valign="top">
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
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[1][1]+"')";									
									String forwardToSCGFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[2][1]+"')";									
									String normalSubmit = normalSubmitFunctionName ;
									String forwardToSubmit = forwardToSubmitFunctionName ;
									String forwardToSCG = forwardToSCGFunctionName ;
								%>
																
								<!-- PUT YOUR COMMENT HERE -->

								
								<td nowrap class="formFieldNoBorders">
								<html:button styleClass="actionButton"
										property="submitPage" 
										title="Submit Only"
										value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>" 
										disabled="<%=isAddNew%>"
										onclick="<%=normalSubmit%>"> 
								</html:button>
								</td>
									
								
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
								<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">								
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit and Register to protocol"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][0]%>" 
											disabled="<%=isRegisterButton%>"
					  						onclick="<%=forwardToSubmit%>">
									</html:button>
								</td>
								</logic:equal>
								</logic:equal>
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit and Register to protocol"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][0]%>" 
											disabled="<%=isRegisterButton%>"
					  						onclick="<%=forwardToSubmit%>">
									</html:button>
								</td>
								</logic:notEqual>	
								</logic:notEqual>
								
							<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">								
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit and Register to protocol"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[2][0]%>" 
					  						onclick="<%=forwardToSCG%>">
									</html:button>
								</td>
						    </logic:equal>						
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
