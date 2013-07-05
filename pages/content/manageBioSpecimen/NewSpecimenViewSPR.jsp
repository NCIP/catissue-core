<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<script src="jss/javaScript.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<head>
<style>
.active-column-1 {width:200px}
</style>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
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
	specimenEntityId = (Long)request.getAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);

	String consentTierCounter =(String)request.getParameter("consentTierCounter");
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;

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

<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<html:form action="<%=formAction%>">

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="0">
				<%@include file="ViewSurgicalPathologyReport.jsp" %>
				</td>
			</tr>
		</table>
		</td></tr></table>

</html:form>