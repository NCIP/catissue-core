						  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>

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
	String title = null;
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String consentTierCounter =(String)request.getParameter("consentTierCounter");
	Integer identifierFieldIndex = new Integer(0);
	String specimenIdentifier = (String)request.getAttribute(Constants.ID);
	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
		specimenIdentifier = (String)request.getParameter(Constants.ID);

	if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	           session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

	if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	{
 		specimenIdentifier= (String) session.getAttribute(Constants.SPECIMEN_ID);//,specimenIdentifier);
 	//	session.removeAttribute(Constants.SPECIMEN_ID); 		
	}


	//------------- Mandar 04-july-06 QuickEvents
	String eventSelected = (String)request.getAttribute(Constants.EVENT_SELECTED);

		String iframeSrc="";
		String formAction = Constants.SPECIMEN_ADD_ACTION;
		String specimenPath ="'NewSpecimenSearch.do?operation=search&tab=specimen&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
		
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
		{
			specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
		}		
		//System.out.println("iframeSrcanno : "+ specimenIdentifier +":"+request.getParameter(Constants.ID));
	//------------- Mandar 04-july-06 QuickEvents

%>


<script>
function viewSPR(specimenId)
{
	var action="<%=Constants.SPR_VIEW_ACTION%>?operation=viewSPR&pageOf=pageOfNewSpecimen&id="+specimenId+"&consentTierCounter=<%=consentTierCounter%>";
	document.forms[0].action=action;
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



</script>

<html:form action="<%=formAction%>">
<!-- Mandar 05-July-06 Code for tabs start -->
	 	<table summary="" cellpadding="0" cellspacing="0" border="0" height="400" class="tabPage" width="87%">  
			<tr>
				<td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="addNewAction(<%= specimenPath %>)">
					<bean:message key="tab.specimen.details"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showEvent()">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewSPR(<%=specimenIdentifier%>)">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				
				<td height="20" class="tabMenuItemSelected"  onClick="">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				</td>
				   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showConsents()" id="consentTab">
					<bean:message key="consents.consents"/>            
				</td>

				<td width="350" class="tabMenuSeparator" colspan="1">&nbsp;</td>
			</tr>
			<tr>
				<td class="tabField" colspan="6"  width = "100%" height = "100%">
					<%@ include file="DisplayAnnotationDataEntryPage.jsp" %>				
				</td>
			</tr>
			</table>
</html:form>