<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"
%>

<html:html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
		<script src="jss/queryModule.js"></script>
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
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
							<tr>
								<td class="formTitle"><bean:message key="savequery.conditionInformationTitle"/></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td class="formLabel">Title :</td>
								<td class="formField"><input type="text" style="width: 300px; display: block;" name="title" value=""/></td>
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
								<td class="formLabel">Description :</td>
								<td>
									<textarea cols="40" rows="4" style="width: 300px;" name="description"></textarea>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
							<tr>
								<td class="formTitle">
									<bean:message key="savequery.setConditionParametersTitle" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
				<tr>
					<td>
						<table>
							<tr>
								<td> <%=request.getAttribute("HTMLContents") %>  </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">
					    <input type="hidden" name="queryString" id="queryString" value=""/>
					    <input type="hidden" name="buildQueryString" id="buildQueryString" value=""/>
						<input type="button" name="preview" value="Preview"/>
						<input type="button" name="save" value="Save" onClick="produceSavedQuery()"/>
						<input type="button" name="cancel" value="Cancel" onClick="window.close();"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html:html>
