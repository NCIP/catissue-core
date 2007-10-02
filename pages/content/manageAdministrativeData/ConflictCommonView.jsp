<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<%@ page import="edu.wustl.catissuecore.caties.util.CaTIESConstants"%>

<%
	String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);
	String conflictStatus = (String)request.getParameter(Constants.CONFLICT_STATUS);
%>

<script language="JavaScript">
   
   //This function will be called to view the report
   	function reportPage()
	{
		var reportId=<%=reportQueueId%> 
		var url="ConflictReportAction.do?reportQueueId="+reportId;
		window.open(url,'ConflictSCGForm','height=330,width=800,scrollbars=1,resizable=1');
	} 
</script>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td>
			<table cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formTitle">
						<bean:message key="caTies.conflicting.report.title"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table summary="" cellpadding="" cellspacing="0" border="0"  width="100%">
		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" style="table-layout:fixed" width="90%" styleClass="formFieldSized">
				<tr >
					<td class="formFieldNoBordersBold" styleClass="formFieldSized">
						<bean:message key="caTies.conflict.report.participant.name"/>
					</td>
					<td  class="formFieldNoBordersSimple">
						 <bean:write name="conflictCommonForm" property="participantName"/>
					</td>
					
				</tr>
				<tr>
					<td  class="formFieldNoBordersBold">
						<bean:message key="participant.birthDate"/>
					</td>
					<td class="formFieldNoBordersSimple">
						<bean:write name="conflictCommonForm" property="birthDate"/>
					</td>
				
				</tr>
				<tr>
					<td  class="formFieldNoBordersBold">
						<bean:message key="participant.socialSecurityNumber"/>
					</td>
					<td  class="formFieldNoBordersSimple">
						<bean:write name="conflictCommonForm" property="socialSecurityNumber"/>
					</td>
				</tr>	
			</table>
		</td>

		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" style="table-layout:fixed" width="90%" styleClass="formFieldSized">
				<tr >
					<td class="formFieldNoBordersBold" styleClass="formFieldSized">
						<bean:message key="caTies.conflict.report.spn"/>
					</td>
					<td  class="formFieldNoBordersSimple">
						 <bean:write name="conflictCommonForm" property="surgicalPathologyNumber"/>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <a href="javascript:reportPage()">
								<b><bean:message key="caTies.conflict.view.report"/></b>
						</a>
					</td>
					
				</tr>
				<tr>
					<td  class="formFieldNoBordersBold">
						<bean:message key="caTies.conflict.report.loaded.date"/>
					</td>
					<td class="formFieldNoBordersSimple">
						<bean:write name="conflictCommonForm" property="reportcreationDate"/>
					</td>
				
				</tr>
				<tr>
					<td  class="formFieldNoBordersBold">
						<bean:message key="caTies.conflict.site"/>
					</td>
					<td  class="formFieldNoBordersSimple">
						<bean:write name="conflictCommonForm" property="siteName"/>
					</td>
				</tr>	
			</table>
		</td>

		</tr>
		
</table>

<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%">
	<tr>
		<td>
			<table cellspacing="0" border="0" width="100%">
			 <tr height = "0">
				&nbsp;
			 </tr>


				<tr>
					<td class="formSubTitle">
					<%if(conflictStatus.equals(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT))
					  {
					%>
						<bean:message key="caTies.conflict.matching.participant"/>
					<%
					  }else
					  {
					%>	
						<bean:message key="caTies.conflict.matching.accession"/>
					<%}%>

					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
