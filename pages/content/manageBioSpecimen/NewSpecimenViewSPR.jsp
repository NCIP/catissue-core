<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<script src="jss/javaScript.js" type="text/javascript"></script>
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
//      Falguni:Performance Enhancement.
	Long specimenEntityId = null;
	specimenEntityId = (Long)request.getAttribute("specimenEntityId");
	
	String consentTierCounter =(String)request.getParameter("consentTierCounter");
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN;
	
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
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:form action="<%=formAction%>">

	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"></td>
		  </tr>
		  <tr>
			<td class="tablepadding">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" border="0" height="1"></td>
		        <td valign="bottom" ><a href="#" onclick="addNewAction(<%= specimenPath %>)"><img src="images/uIEnhancementImages/tab_specimen_details2.gif" border="0" alt="Specimen Details" width="126" height="22" border="0"></a></td>
		        <td valign="bottom"><a href="#" onclick="showEvent()"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Show Events" border="0" width="56" height="22"></a></td>
		        <td valign="bottom"><img src="images/uIEnhancementImages/tab_view_surgical1.gif" border="0" alt="View Annotation" width="216" height="22"></td>
				<td valign="bottom"><a href="#" onClick="viewAnnotations(<%=specimenEntityId%>,<%=specimenIdentifier%>,<%=consentTierCounter%>,'<%=staticEntityName%>','<%=pageOf%>')"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" border="0" alt="Consents" width="116" height="22"></a></td>
				<td valign="bottom"><a href="#" onClick="addNewAction(<%= consentTab %>)" id="consentTab">
					<img src="images/uIEnhancementImages/tab_consents2.gif" border="0" alt="Consents" width="76" height="22"></a></td>
		        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
		</tr>

			<tr>
				<td colspan="7">
				<%@include file="ViewSurgicalPathologyReport.jsp" %>
				</td>
			</tr>
		</table>
		</td></tr></table>
	
</html:form>