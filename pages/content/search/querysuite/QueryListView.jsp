<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
</head>

<body>
	<html:form styleId='saveQueryForm' action='<%=Constants.FETCH_AND_EXECUTE_QUERY_ACTION%>'>
		<html:errors />
		<table cellpadding='0' cellspacing='0' border='0' class="contentPage" width="620">
			<c:set var="parameterizedQueryCollection" value="${saveQueryForm.parameterizedQueryCollection}" />
			<jsp:useBean id="parameterizedQueryCollection" type="java.util.Collection" />
	
			<c:forEach items="${parameterizedQueryCollection}" var="parameterizedQuery" varStatus="queries">
			<jsp:useBean id="parameterizedQuery" type="edu.wustl.common.querysuite.queryobject.IParameterizedQuery" />
				<tr>
					<%String target = "executeQuery('"+parameterizedQuery.getId()+"')"; %>
					<td>
						<html:link href='#' onclick='<%=target%>'>
							<c:out value='${parameterizedQuery.name}' />
						</html:link><br/>
						<b>Description:</b><c:out value='${parameterizedQuery.description}' />
					</td>
				</tr>
			</c:forEach>
		</table>
		<html:hidden styleId="queryId" property="queryId" />
	</html:form>
</body>