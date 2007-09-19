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
							<td class="formFieldNoBordersBold" styleClass="formFieldSized">
								<label for="site">
									<bean:message key="specimenCollectionGroup.site"/>
								</label>
							</td>
							<td class="formFieldNoBordersSimple" styleClass="formFieldSized">
								 <bean:write name="specimenCollectionGroupForm" property="name"/>
							 </td>
						</tr>
						
											
						<tr >
							<td class="formFieldNoBordersBold">
								<label for="clinicalDiagnosis">
									<bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
								</label>
							</td>
							 <td class="formFieldNoBordersSimple">
								 <bean:write name="specimenCollectionGroupForm" property="clinicalDiagnosis"/>
							 </td>	 
								
						</tr>
						<tr >
							<td class="formFieldNoBordersBold">
								<label for="clinicalStatus">
									<bean:message key="specimenCollectionGroup.clinicalStatus"/></label>
							 </td>
							  <td class="formFieldNoBordersSimple">
								 <bean:write name="specimenCollectionGroupForm" property="clinicalStatus"/>
							 </td>
								
						</tr>
									
						<tr >
							<td class="formFieldNoBordersBold">
								<label for="surgicalPathologyNumber">
									<bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
								</label>
							 </td>	
							  <td class="formFieldNoBordersSimple">
								 <bean:write name="specimenCollectionGroupForm" property="surgicalPathologyNumber"/>
							 </td>
						</tr>
							
						
					</table>
				
				</td>	
			</tr>
				
</table>


