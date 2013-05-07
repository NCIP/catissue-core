
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>



<!-- Make the Ajax javascript available -->
<script type="text/javascript" src="jss/ajax.js"></script> 
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
<%=messageKey%>
</html:messages>
<%
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String operation = (String)request.getAttribute(Constants.OPERATION);
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
	<tr>
		<td class="tabField" colspan="6">
		<%@   include file="/pages/content/manageBioSpecimen/ViewSurgicalPathologyReport.jsp" %>
		</td>
	</tr>
</table>


</html> 