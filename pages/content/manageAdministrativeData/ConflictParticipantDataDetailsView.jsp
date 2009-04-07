<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
			 <table summary="" cellpadding="1" cellspacing="3" border="0" align="center" width="98%">			 
				<tr>
					<td class="nonedittext" width="25%">
				      	<b><bean:message key="participant.name"/></b>				    	
				    </td>
					<td class="nonedittext" width="25%" >
				    	<bean:message key="caTies.conflict.symbol"/>
				    	&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="participantName"/>
				    </td>
					
					
					<td class="nonedittext" width="25%">
				    	<b><bean:message key="participant.socialSecurityNumber"/></b>
					 </td>
					 <td class="nonedittext" width="25%">
						<bean:message key="caTies.conflict.symbol"/>&nbsp;
						<bean:write name="conflictParticipantDataDetailsForm" property="socialSecurityNumber"/>
				    </td>
				</tr>
				<tr>
					<td class="nonedittext" >
						<b><bean:message key="participant.birthDate"/></b> 
					</td>
					<td class="nonedittext" >
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="birthDate"/>
				    </td>
					<td class="nonedittext" >
						<b><bean:message key="participant.activityStatus" /></b>
					 </td>
					 <td class="nonedittext" >
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="activityStatus"/>
				    </td>				
				</tr>
				<tr >
				 	<td class="nonedittext" >
				     	<b><bean:message key="participant.vitalStatus"/></b> 
				    </td>	
					<td class="nonedittext" >
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="vitalStatus"/>
				    </td>
					<td class="nonedittext" >
						<b><bean:message key="participant.deathDate"/></b>
					</td>
					<td class="nonedittext" >
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="deathDate"/>
				    </td>
				</tr>
				<tr >
					<td class="nonedittext" >
				     	<b><bean:message key="participant.gender"/></label></b> 
				    </td>
					<td class="nonedittext" >
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="gender"/>
				    </td>
					<td class="nonedittext">
						<b><bean:message key="participant.genotype"/></label></b> 
					</td>
					<td class="nonedittext">
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="sexGenotype"/>
				    </td>
				</tr>
				<tr >
				 	<td class="nonedittext" valign="top">
				     	<b><bean:message key="participant.race"/></label></b>
					</td>
					<td class="nonedittext"  valign="top">
				    	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				    	<bean:write name="conflictParticipantDataDetailsForm" property="race"/>	
				    </td>
				 	<td class="nonedittext" valign="top">
				     	<b><bean:message key="participant.ethnicity"/></b>
					 </td>
					<td class="nonedittext"  valign="top">
				       	<bean:message key="caTies.conflict.symbol"/>&nbsp;
				       	<bean:write name="conflictParticipantDataDetailsForm" property="ethinicity"/>
				    </td>
			   	</tr>
			</table>