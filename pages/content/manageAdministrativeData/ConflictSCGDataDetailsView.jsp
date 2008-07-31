<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<table summary="" cellpadding="1" cellspacing="3" border="0" align="center" width="98%">
						<tr>
							<td class="black_ar" styleClass="formFieldSized" width="35%">
								<b><label for="site">
									<bean:message key="specimen.specimenCollectionGroupName"/>
								</label></b>
							</td>
							<td class="black_ar" styleClass="formFieldSized" width="63%">
								 <bean:message key="caTies.conflict.symbol"/>
								 &nbsp;<bean:write name="specimenCollectionGroupForm" property="name"/>
							 </td>
						</tr>
						
											
						<tr >
							<td class="black_ar">
								<b><label for="clinicalDiagnosis">
									<bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
								</label></b>
							</td>
							 <td class="black_ar">
								 <bean:message key="caTies.conflict.symbol"/>
								 &nbsp;<bean:write name="specimenCollectionGroupForm" property="clinicalDiagnosis"/>
							 </td>	 
								
						</tr>
						<tr >
							<td class="black_ar">
								<b><label for="clinicalStatus">
									<bean:message key="specimenCollectionGroup.clinicalStatus"/></label></b>
							 </td>
							  <td class="black_ar">
								 <bean:message key="caTies.conflict.symbol"/>
								 &nbsp;<bean:write name="specimenCollectionGroupForm" property="clinicalStatus"/>
							 </td>
								
						</tr>
									
						<tr >
							<td class="black_ar">
								<b><label for="surgicalPathologyNumber">
									<bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
								</label></b>
							 </td>	
							  <td class="black_ar">
								 <bean:message key="caTies.conflict.symbol"/>
								 &nbsp;<bean:write name="specimenCollectionGroupForm" property="surgicalPathologyNumber"/>
							 </td>
						</tr>
							
						<tr>
							<td class="black_ar">
								<b><label for="surgicalPathologyNumber">
									<bean:message key="caTies.conflict.collection.event.date"/> 
								</label></b>
							</td>
							 <td class="black_ar">
								 <bean:message key="caTies.conflict.symbol"/>
								 &nbsp;<bean:write name="specimenCollectionGroupForm" property="collectionEventdateOfEvent"/>
							 </td>
						</tr>	
					</table>