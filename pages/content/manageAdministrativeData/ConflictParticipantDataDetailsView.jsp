<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="40%">
			
	<tr>
		<td>
			 <table summary="" cellpadding="3" cellspacing="0" border="0" width="450" styleClass="formFieldSized">
				<tr>
					<td class="formFieldNoBordersSimple" styleClass="formFieldSized">
				      	<b><bean:message key="participant.name"/> : </b>
				    	<bean:write name="conflictParticipantDataDetailsForm" property="participantName"/>
				    </td>
				 	<td class="formFieldNoBordersSimple">
						<b><bean:message key="participant.birthDate"/> : </b> 
						<bean:write name="conflictParticipantDataDetailsForm" property="birthDate"/>
					</td>	
					
				</tr>
				<tr >
				 	<td class="formFieldNoBordersSimple">
				     	<b><bean:message key="participant.vitalStatus"/> : </b> 
				     	<bean:write name="conflictParticipantDataDetailsForm" property="vitalStatus"/>
				    </td>	
					<td class="formFieldNoBordersSimple">
						<b><bean:message key="participant.deathDate"/> : </b>
						<bean:write name="conflictParticipantDataDetailsForm" property="deathDate"/>
					</td>
				</tr>
				<tr >
					<td class="formFieldNoBordersSimple">
				     	<b><bean:message key="participant.gender"/></label> : </b> 
				     	<bean:write name="conflictParticipantDataDetailsForm" property="gender"/>
				    </td>
					<td class="formFieldNoBordersSimple">
						<b><bean:message key="participant.genotype"/></label> : </b> 
						<bean:write name="conflictParticipantDataDetailsForm" property="sexGenotype"/>
					</td>
				</tr>
				<tr >
				 	<td class="formFieldNoBordersSimple">
				     	<b><bean:message key="participant.race"/></label> : </b>
						<bean:write name="conflictParticipantDataDetailsForm" property="race"/>	
					</td>	
				 	<td class="formFieldNoBordersSimple">
				     	<b><bean:message key="participant.ethnicity"/> : </b>
			    		<bean:write name="conflictParticipantDataDetailsForm" property="ethinicity"/>
			
				    </td>	
			   	</tr>
				
				<tr>
					<td class="formFieldNoBordersSimple">
				    	<b><bean:message key="participant.socialSecurityNumber"/> : </b>
				    	<bean:write name="conflictParticipantDataDetailsForm" property="socialSecurityNumber"/>
					 </td>
					<td class="formFieldNoBordersSimple" >
						<b><bean:message key="participant.activityStatus" /> : </b>
					    <bean:write name="conflictParticipantDataDetailsForm" property="activityStatus"/>
					 </td>
				
				</tr>
				
				
			</table>
		
		</td>	
	
	</tr>
		
</table>


