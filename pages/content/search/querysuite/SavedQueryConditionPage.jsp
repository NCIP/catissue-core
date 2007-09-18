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

	<%
		String contents = (String) request.getAttribute("HTMLContents");
		request.getSession().setAttribute("HTMLContents", contents);
	%>

	<body>
		<html:errors/>
		<html:form styleId='saveQueryForm' action='<%=Constants.SAVE_QUERY_ACTION%>'>
			<table>
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
								<td><bean:message key="savequery.queryTitle"/>:</td>
								<td><input type="text" style="width: 150px; display: block;" name="title" value=""/></td>
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
								<td><bean:message key="savequery.queryDescription"/>:</td>
								<td>
									<textarea cols="20" rows="4" style="width: 150px;" name="description"></textarea>
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
								<td>User Defined</td>
								<td>Display Label</td>
								<td>Conditions</td>
								<td align="right">
									<a href="LoadSaveQueryPage.do">Show all attributes</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td><!-- <%=request.getAttribute("HTMLContents")%> --></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">
						<input type="button" name="preview" value="Preview"/>
						<input type="button" name="save" value="Save" onClick="saveQuery()"/>
						<input type="button" name="cancel" value="Cancel" onClick="window.close();"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html:html>
