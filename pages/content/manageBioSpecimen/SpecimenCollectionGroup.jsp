<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName, pageView = operation;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}
%>


<head>
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
			var action = "/catissuecore/SpecimenCollectionGroup.do?operation=<%=operation%>&pageOf=pageOfSpecimenCollectionGroup&isOnChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
	</script>
</head>

<html:errors />

<html:form action="<%=Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION%>">

	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION BEGINS-->
	    <tr><td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td><html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/></td>
				 </tr>
				 
				 <tr>
					<td><html:hidden property="systemIdentifier"/></td>
				 </tr>

				 <tr>
				 	<td class="formMessage" colspan="4">* indicates a required field</td>
				 </tr>
				 
				 <tr>
					<td class="formTitle" height="20" colspan="3">
						<logic:equal name="operation" value="<%=Constants.ADD%>">
							<bean:message key="specimenCollectionGroup.add.title"/>
						</logic:equal>
						<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="specimenCollectionGroup.edit.title"/>
						</logic:equal>
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
				     	<html:select property="collectionProtocolId" styleClass="formFieldSized" styleId="collectionProtocolId" size="1" disabled="<%=readOnlyForAll%>" onchange="onChangeEvent(this)">
							<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
						</html:select>
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
				     	<html:select property="siteId" styleClass="formFieldSized" styleId="siteId" size="1" disabled="<%=readOnlyForAll%>">
  							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	</td>
				 </tr>
				 
				 
				 
				 <tr>
				    <td class="formRequiredNotice" >
				     	<html:radio styleClass=""  property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
  				     	    <label for="participantId">
								<%--<bean:message key="specimenCollectionGroup.collectedByParticipant" />--%>
							</label>
				     	</html:radio>
				        <br>
				       	<html:radio styleClass="" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
  				       	    <label for="protocolParticipantIdentifier">
								<%--<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber" />--%>
							</label>
				     	</html:radio>
				    </td>
				    
				    <td class="formRequiredLabel">
						<label for="participantId">
					        <bean:message key="specimenCollectionGroup.collectedByParticipant" />
						</label>
  					    <br>
						<label for="protocolParticipantIdentifier">
							<bean:message key="specimenCollectionGroup.collectedByProtocolParticipantNumber" />
						</label>
					</td>
					
  			        <td class="formField">
  						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="1">
				     	     <html:select property="participantId" styleClass="formFieldSized" styleId="ParticipantId" size="1" onchange="onChangeEvent(this)">
                         	     <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>				     	
  						     </html:select>
  						</logic:equal>     
						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">
				     	     <html:select property="participantId" styleClass="formFieldSized" styleId="ParticipantId" size="1" onchange="onChangeEvent(this)" disabled="true">
                         	     <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>				     	
  						     </html:select>
  						</logic:equal>

			  		    <html:link page="/Participant.do?operation=add" styleId="newParticipant">
	 						<bean:message key="buttons.addNew" />
 						</html:link>
					
                        <%-- LOGIC TAG FOR PARTICPANT NUMBER --%> 												
                        <logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="1">						
   						 	<html:select property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" size="1" disabled="true">
                         		<html:options collection="<%=Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST%>" labelProperty="name" property="value"/>				     					     	
							</html:select>
 						</logic:equal>
 						
 						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">						
   						 	<html:select property="protocolParticipantIdentifier" styleClass="formFieldSized" styleId="protocolParticipantIdentifier" size="1" >
                         		<html:options collection="<%=Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST%>" labelProperty="name" property="value"/>				     					     	
							</html:select>
 						</logic:equal>
 						<html:link page="/CollectionProtocolRegistration.do?operation=add" styleId="newParticpantRegistration">
		 						<bean:message key="buttons.addNew" />
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
				     	<html:select property="collectionProtocolEventId" styleClass="formFieldSized" styleId="collectionProtocolEventId" size="1" onchange="onChangeEvent(this)" >
                         	<html:options collection="<%=Constants.STUDY_CALENDAR_EVENT_POINT_LIST%>" labelProperty="name" property="value"/>				     					     					     	
						</html:select>
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
						<html:select property="clinicalDiagnosis" styleClass="formFieldSized" styleId="clinicalDiagnosis" size="1" >
							<html:options collection="<%=Constants.CLINICAL_DIAGNOSIS_LIST%>" labelProperty="name" property="value"/>				     					     					     	
						</html:select>
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
				     	<html:select property="clinicalStatus" styleClass="formFieldSized" styleId="clinicalStatus" size="1" disabled="<%=readOnlyForAll%>">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>		
						</html:select>
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
				     		<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="<%=readOnlyForAll%>">
                         		<html:options collection="<%=Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</logic:equal>
						<logic:equal name="specimenCollectionGroupForm" property="checkedButton" value="2">
					     	<html:select property="participantsMedicalIdentifierId" styleClass="formFieldSized" styleId="participantsMedicalIdentifierId" size="1" disabled="true">
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
				    <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/>
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
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
					
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
				 
			</table>
		</td></tr>
		<!-- NEW SPECIMEN COLLECTION GROUP REGISTRATION ENDS-->
	</table>
</html:form>		