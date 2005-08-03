<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<head>
     <script language="JavaScript">
        function onTypeChange(element)
		{
			var action = "/catissuecore/SpecimenCollectionGroup.do?operation=add&typeSelected=" + element.value;
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		

		function participantNameButton(element)
		{
		     document.forms[0].participantId.disabled = false;
		      document.forms[0].protocolParticipantNumber.disabled = true;
		}
		
		
		function participantNumberListButton(element)
		{
		      document.forms[0].participantId.disabled = true;
		      document.forms[0].protocolParticipantNumber.disabled = false;

		}
		
		
	</script>
</head>
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
				     	<html:select property="collectionProtocolId" styleClass="formFieldSized" styleId="collectionProtocolId" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)">
							<html:options name="protocolIdList" labelName="protocolList"/>		
						</html:select>
		        	</td>
				 </tr>

				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="protocolTitle">
							<bean:message key="specimenCollectionGroup.site"/>
						</label>
					</td>
					
					<td class="formField">
				     	<html:select property="siteId" styleClass="formFieldSized" styleId="siteId" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)">
							<html:options name="siteIdList" labelName="protocolList"/>		
						</html:select>
		        	</td>
				 </tr>
				 
				 <tr>
				 	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formField">
				     	<html:radio property="radioProperty" value="123" onclick="participantNameButton(this)">
				     		<bean:message key="specimenCollectionGroup.collectedByParticipant"/>
				     	</html:radio>
				    </td>
				    
				    <td class="formField">
				     	<html:select property="participantId" styleClass="formFieldSized" styleId="ParticipantId" size="1" disabled="true">
							<html:options name="participantIdList" labelName="participantList"/>		
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formField">
				     	<html:radio property="radioProperty" value="123" onclick="participantNumberListButton(this)">
				     		<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber"/>
				     	</html:radio>
				    </td>
				    <td class="formField">
				     	<html:select property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" size="1" disabled="true">
							<html:options name="protocolParticipantNumberIdList" labelName="protocolParticipantNumberList"/>		
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
				     	<html:select property="collectionProtocolEventId" styleClass="formFieldSized" styleId="collectionProtocolEventId" size="1">
							<html:options name="studyCalendarEventPointIdList" labelName="studyCalendarEventPointList"/>		
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
				     	<html:text property="clinicalDiagnosis" styleClass="formFieldSized" size="30" styleId="clinicalDiagnosis"  readonly="<%=readOnlyForAll%>"/>
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
				     	<html:text styleClass="formFieldSized" size="30" styleId="participantsMedicalIdentifierId" property="participantsMedicalIdentifierId" readonly="<%=readOnlyForAll%>"/>
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