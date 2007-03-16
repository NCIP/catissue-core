<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>

<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<style>
.active-column-1 {width:200px}
</style>

<%
	int noOfRows=0;
	Map map = null;
	String formName=null;
	String consentTierCounter =(String)request.getParameter("consentTierCounter");
	ViewSurgicalPathologyReportForm viewSurgicalPathologyReportForm=null;
	ViewSurgicalPathologyReportForm formSPR=viewSurgicalPathologyReportForm;
	Object obj = request.getAttribute("viewSurgicalPathologyReportForm");
	if(obj != null && obj instanceof ViewSurgicalPathologyReportForm)
	{
		formSPR = (ViewSurgicalPathologyReportForm)obj;
		map = formSPR.getValues();
		noOfRows = formSPR.getCounter();
	}
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	Long specimenEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
	String specimenIdentifier = (String)request.getAttribute(Constants.ID);
	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
		specimenIdentifier = (String)request.getParameter(Constants.ID);

	if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	           session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	{
 		specimenIdentifier= (String) session.getAttribute(Constants.SPECIMEN_ID);//,specimenIdentifier);
	}

		String iframeSrc="";
		String formAction = Constants.SPECIMEN_ADD_ACTION;
		String specimenPath ="'NewSpecimenSearch.do?operation=search&tab=specimen&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
		{
			specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
		}		
		
		String staticEntityName=null;
		staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN;
		
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
				var formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15&consentTierCounter=<%=consentTierCounter%>";		
				document.forms[0].action=formName;
				document.forms[0].submit();
}
function showConsents()
{
	<%
		if(consentTierCounter.equals("0"))			
		{
	%>
			alert("No consents available for selected Specimen Collection Group");		
	<%
		}
		else
		{
	%>
			addNewAction(<%= consentTab %>)			
	<%
		}
	%>
}
</script>

<html:form action="<%=formAction%>">
<!-- Mandar 05-July-06 Code for tabs start -->
	 	<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" class="tabPage" width="87%">  
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

				<td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewAnnotations(<%=specimenEntityId%>,<%=specimenIdentifier%>,<%=consentTierCounter%>,'<%=staticEntityName%>')">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				</td>
				   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showConsents()" id="consentTab">
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