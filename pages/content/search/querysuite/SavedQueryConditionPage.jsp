<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"
%>

<html:html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
		<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/overlib_mini.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/calender.js"></script>
		
		<script>
			function closeSaveQueryWindow()
			{
				var parentWindowForm = window.opener.document.forms[0];
				parentWindowForm.action = "RetrieveQueryAction.do";
				parentWindowForm.submit();
				
				window.self.close();
			}
		</script>
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			<bean:message key="savequery.conditionInformationTitle"/>
		</title>
	</head>

	<body>
		<html:errors/>
		<html:form styleId='saveQueryForm' action='<%=Constants.SAVE_QUERY_ACTION%>'>
			<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
				<tr>
					<td colspan='3 height='20' class="formTitle">
						<bean:message key="savequery.conditionInformationTitle"/>
					</td>
				</tr>
				<tr>
					<td width='5' class="formRequiredNotice">*</td>
					<td class="formRequiredLabel">
					<bean:message key="query.title"/> :</td>
					<td class="formField">
						<html:text  style="width: 300px; display: block;" styleId="title" property="title" />
					</td>
					<!--
					<td rowspan="2" valign="top">Share Query With :</td>
					<td rowspan="2" valign="top">
						<select multiple="multiple" name="userIds" class="dropdownQuery" style="width:150px; display:block;">
							<option>All</option>
							<option>Administrator</option>
						</select>
					</td>
					-->
				</tr>
				<tr>
					<td width='5' class="formRequiredNotice">&nbsp;</td>
					<td class="formLabel"><bean:message key="query.description"/> :</td>
					<td class="formField">
						<html:textarea cols="40" rows="4" style="width: 300px;" styleId="description" property="description"> </html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan='3' class="formTitle" height="20">
						<bean:message key="savequery.setConditionParametersTitle" />
					</td>
				</tr>
				<tr>
					<td colspan='3'>
						<table cellpadding="0" cellspacing="0" border="0" class="contentPage" width='100%'>
							<tr>
								<td><%=request.getAttribute(Constants.HTML_CONTENTS)%></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan='3' align="right">
					    <input type="hidden" name="queryString" id="queryString" value=""/>
					    <input type="hidden" name="buildQueryString" id="buildQueryString" value=""/>
						<input type="button" name="preview" value="Preview"  disabled="true"/>
						<c:choose>
							<c:when test="${querySaved eq 'true'}">
								<input type="button" name="close" value="Close" onClick="closeSaveQueryWindow()"/>
							</c:when>
							<c:otherwise>
								<input type="button" name="save" value="Save" onClick="produceSavedQuery()"/>
								<input type="button" name="cancel" value="Cancel" onClick="window.close();"/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html:html>
