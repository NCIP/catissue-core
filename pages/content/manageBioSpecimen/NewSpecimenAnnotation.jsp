						  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="jss/newSpecimen.js"></script>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<style>
.active-column-1 {width:200px}
</style>
<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
%>
<script>
function updateHelpURL()
	{
		var URL="";
		URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm")%>";
		return URL;
	}
</script>
<%
	String title = null;
	//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
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

<html:form action="<%=formAction%>">
<!-- Mandar 05-July-06 Code for tabs start -->
	 	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="maintable">  
		 <tr>
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
	
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
