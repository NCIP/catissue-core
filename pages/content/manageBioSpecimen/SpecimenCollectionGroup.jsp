<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			formName = Constants.SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}
%>

	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
		
		   <html:form action="<%=Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION%>">
		   
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
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
					    	<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier" readonly="<%=readOnlyForAll%>"/>
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
			  
			   	
			  <!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
					</td>
				 </tr>
				 
				 <tr>
					<td>
						<html:hidden property="systemIdentifier"/>
					</td>
				 </tr>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
				 <tr>
				 	<td class="formMessage" colspan="3">* indicates a required field</td>
				 </tr>
				 <tr>
				    <td class="formTitle" height="20" colspan="3">
				    <%String title = "specimenCollectionGroup."+operation+".title";%>
				    	<bean:message key="<%=title%>"/>
				    </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="protocolTitle">
							<bean:message key="specimenCollectionGroup.protocolTitle"/>
						</label>
					</td>
					<td class="formField">
				     	<html:select property="protocolTitle" styleClass="formFieldSized" styleId="protocolTitle" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="protocolTitleList" labelName="protocolTitleList"/>		
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				 	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formField">
				     	<html:radio property="collectedByParticipant" value="123">
				     		<bean:message key="specimenCollectionGroup.collectedByParticipant"/>
				     	</html:radio>
				    </td>
				    <td class="formField">
				     	<html:select property="participantName" styleClass="formFieldSized" styleId="participantName" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="participantNameList" labelName="participantNameList"/>		
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formField">
				     	<html:radio property="collectedByProtocolParticipantNumber" value="122">
				     		<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber"/>
				     	</html:radio>
				    </td>
				    <td class="formField">
				     	<html:select property="protocolParticipantNumber" styleClass="formFieldSized" styleId="protocolParticipantNumber" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="protocolParticipantNumberList" labelName="protocolParticipantNumberList"/>		
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="studyCalendarEventPoint">
							<bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/>
						</label>
					</td>
				     <td class="formField">
				     	<html:select property="studyCalendarEventPoint" styleClass="formFieldSized" styleId="studyCalendarEventPoint" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="studyCalendarEventPointList" labelName="studyCalendarEventPointList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="clinicalDiagnosis">
							<bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
						</label>
					</td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="clinicalDiagnosis" property="clinicalDiagnosis" readonly="<%=readOnlyForAll%>"/>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="clinicalStatus">
							<bean:message key="specimenCollectionGroup.clinicalStatus"/>
						</label>
					 </td>
				     <td class="formField">
				     	<html:select property="clinicalStatus" styleClass="formFieldSized" styleId="clinicalStatus" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="cinicalStatusList" labelName="cinicalStatusList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="medicalRecordNumber">
							<bean:message key="specimenCollectionGroup.medicalRecordNumber"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="medicalRecordNumber" property="medicalRecordNumber" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="surgicalPathologyNumber">
							<bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
						</label>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>

 			   	 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">		
				 	<tr>
				  		<td align="right" colspan="3">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
						   			<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
									<td>
										<html:submit styleClass="actionButton">
											<bean:message key="buttons.submitAndAddNewSpecimen"/>
										</html:submit>
									</td>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 </logic:notEqual>
				 
				</logic:notEqual>
				</table>
			  </td>
			 </tr>
			 
			  <!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION ENDS-->
			 </html:form>
		</table>