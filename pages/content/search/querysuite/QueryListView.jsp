<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"
%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
</head>

<body>
	<html:form styleId='saveQueryForm' action='<%=Constants.FETCH_QUERY_ACTION%>'>
		<table cellpadding='0' cellspacing='0' border='0' align='center' width='95%'>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="formTitle" height="20"><bean:message key="query.savedQueries.label"/></td>
			</tr>
			<tr>
				<td><html:errors /></td>
			</tr>
			<tr>
				<td align='right'>
					<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
						<%=messageKey%>
					</html:messages>
				</td>
			</tr>
			<tr>
				<td>
					<div style="width:100%; height:500px; overflow-y:auto;">
						<table cellpadding='0' cellspacing='0' border='0' width='100%' class='contentPage'>
							<c:set var="parameterizedQueryCollection" value="${saveQueryForm.parameterizedQueryCollection}" />
							<jsp:useBean id="parameterizedQueryCollection" type="java.util.Collection" />
					
							<c:forEach items="${parameterizedQueryCollection}" var="parameterizedQuery" varStatus="queries">
							<jsp:useBean id="parameterizedQuery" type="edu.wustl.common.querysuite.queryobject.IParameterizedQuery" />
								<tr>
									<%String target = "executeQuery('"+parameterizedQuery.getId()+"')"; %>
									<td valign="top" height='20'>
										<img src="images/savedQuery.bmp"/>
									</td>
									<td width='100%' height='20' valign="top" style="padding-left:0.7em;">
										<html:link styleClass='formQueryLink' href='#' onclick='<%=target%>'>
											<b><c:out value='${parameterizedQuery.name}' /></b>
										</html:link><br/>
										<b>Description: &nbsp;</b><c:out value='${parameterizedQuery.description}' />
									</td>
								</tr>
								<tr><td colspan='2'>&nbsp;</td></tr>
							</c:forEach>
						</table>
					</div>
				</td>
			</tr>
		</table>
		
		<html:hidden styleId="queryId" property="queryId" />
	</html:form>
</body>