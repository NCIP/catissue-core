						  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
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
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	if(pageOf == null)
	    pageOf =(String)request.getParameter(Constants.PAGE_OF);

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
		String specimenPath ="'NewSpecimenSearch.do?operation=search&tab=specimen&pageOf="+pageOf+"&id="+specimenIdentifier+"'" ;
		String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf="+pageOf+"&id="+specimenIdentifier+"'" ;

		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
		{
			specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
			consentTab="'QuerySpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
		}
		//System.out.println("iframeSrcanno : "+ specimenIdentifier +":"+request.getParameter(Constants.ID));
	//------------- Mandar 04-july-06 QuickEvents

%>


<script>

function showSPPEvents()
{
	var specimenIdentifier = "<%=specimenIdentifier%>";
	var consentTier=0;
	var action= "DisplaySPPEventsAction.do?pageOf=<%=pageOf%>&menuSelected=15&specimenId="+specimenIdentifier+"&consentTierCounter="+consentTier;
	document.forms[0].action = action;
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
				String formNameAction = "ListSpecimenEventParameters.do?pageOf="+pageOf;
				    //pageOfListSpecimenEventParameters";
				if(pageOf != null && pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>

				var formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15&consentTierCounter=<%=consentTierCounter%>";
				document.forms[0].action=formName;
				document.forms[0].submit();
}

		//View SPR Vijay pande
		function viewSPR()
		{
			<% Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID); %>
			var reportId='<%=reportId%>';
			if(reportId==null || reportId==-1)
			{
				alert("There is no associate report in the system!");
			}
			else if(reportId==null || reportId==-2)
			{
				alert("Associated report is under quarantined request! Please contact administrator for further details.");
			}
			else
			{
		    	var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&reportId="+reportId;
				document.forms[0].action=action;
				document.forms[0].submit();
			}
		}


</script>

<html:form action="<%=formAction%>">
<!-- Mandar 05-July-06 Code for tabs start -->
	 	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="maintable">
		 <tr>
    <td class="tablepadding">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">

			<tr>
				<td class="td_tab_bg" >
					<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1" border="0"></td>
				<td valign="bottom" onclick="addNewAction(<%= specimenPath %>)">
					<html:link styleId="specimenDetailsTab" href="#" >
					<img src="images/uIEnhancementImages/tab_specimen_details2.gif" alt="Specimen Details"  width="126" height="22" border="0"></html:link></td>
				<td valign="bottom">
					<html:link href="#">
					<img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="showEvent()" border="0"></html:link></td>
				<td valign="bottom">
					<html:link href="#">
					<img src="images/uIEnhancementImages/tab_spp2.gif" alt="SPP" width="42" height="22" onclick="showSPPEvents()" border="0"></html:link></td>
				<td valign="bottom">
					<html:link href="#"  onclick="viewSPR()">
					<img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="Inactive View Surgical Pathology Report " width="216" height="22" border="0"></html:link></td>
				<td valign="bottom">
					<html:link href="#">
					<img src="images/uIEnhancementImages/tab_view_annotation1.gif" alt="View Annotation" width="116" height="22" border="0" ></html:link></td>
				<td align="left" valign="bottom" class="td_color_bfdcf3" >
					<html:link styleId="consentViewTab" href="#" onclick="showConsents()">
					<img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22" ></html:link></td>
				<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
					</td>
			</tr>

			<tr>
				<td  colspan="8" >
					<%@ include file="DisplayAnnotationDataEntryPage.jsp" %>
				</td>
			</tr>
			</table>

				</td>
			</tr>
			</table>

</html:form>
