<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"
%>

<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
		<script src="jss/queryModule.js"></script>
				<script src="jss/calender.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			<bean:message key="savequery.queryConditionTitle"/>
		</title>
	</head>

	

	<body>
		<html:errors/>
		<form id='fetchQueryForm' name='fetchQueryForm' action='<%=Constants.EXECUTE_QUERY_ACTION%>' >
			<table width="100%">
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
							<tr>
								<td class="formTitle"><bean:message key="savequery.queryConditionTitle"/></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td> <%=request.getAttribute(Constants.HTML_CONTENTS) %>  </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
							<tr>
								<td class="formTitle">
									<bean:message key="savequery.definedConditionTitle" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
	   			<tr>
					<td align="right">
					    <input type="hidden" name="queryString" value="" />
						<input type="button" name="execute" value="Execute" onClick="ExecuteSavedQuery()"/>
						<input type="button" name="cancel" value="Cancel" onClick="window.close();"/>
					</td>
				</tr>			
			</table>
		</form>
	</body>
</html>
				
				