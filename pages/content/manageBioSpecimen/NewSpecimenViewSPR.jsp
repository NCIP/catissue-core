<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>

<head>
<style>
.active-column-1 {width:200px}
</style>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String specimenIdentifier = (String)request.getAttribute(Constants.ID);
	String formName=null;
	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
		specimenIdentifier = (String)request.getParameter(Constants.ID);

	if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	           session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	{
 		specimenIdentifier= (String) session.getAttribute(Constants.SPECIMEN_ID);//,specimenIdentifier);
	}

		String iframeSrc="";
		String formAction = Constants.VIEW_SPR_ACTION;
		String specimenPath ="'NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
			consentTab="'QuerySpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
		}		
		
%>
<script>

function showEvent()
{
		var id = <%=specimenIdentifier%>;
		
				<%
				String formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
				if(pageOf != null && pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>
				var formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15";		
				document.forms[0].action=formName;
				document.forms[0].submit();
}
</script>

<html:form action="<%=formAction%>">
	<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">  
			<tr>
				<td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="addNewAction(<%= specimenPath %>)">
					<bean:message key="tab.specimen.details"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showEvent()">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItemSelected"  onClick="">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<%=Constants.CLINICAL_ANNOTATIONS %>
				</td>
				</td>
				   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="addNewAction(<%= consentTab %>)" id="consentTab">
					<bean:message key="consents.consents"/>            
				</td>

				<td width="350" class="tabMenuSeparator" colspan="1">&nbsp;</td>
			</tr>
			<tr>
			<td class="tabField" colspan="6">
				<%@ include file="ViewSurgicalPathologyReport.jsp" %> 			
			</td>
		</tr>
	</table>
</html:form>