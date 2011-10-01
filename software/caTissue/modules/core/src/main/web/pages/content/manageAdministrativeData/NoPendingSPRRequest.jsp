<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<c:set var="pageOf" value="${viewSurgicalPathologyReportForm.pageOf}"/>
<jsp:useBean id="pageOf" type="java.lang.String"/>

<html:form action="<%=Constants.VIEW_SPR_ACTION%>">
<table>
	<tr height="5%">
		<td>
<%
if(pageOf.equals(Constants.PAGE_OF_REVIEW_SPR))
{
%>
	<b>
		<bean:message key="reviewSPR.noPendingRequest.msg"/>
	</b>
<%
}
else if(pageOf.equals(Constants.PAGE_OF_QUARANTINE_SPR))
{
%>
	<b>
		<bean:message key="quarantineSPR.noPendingRequest.msg"/>
	</b>

<%
}
%>
		</td>
	</tr>
</table>
</html:form>