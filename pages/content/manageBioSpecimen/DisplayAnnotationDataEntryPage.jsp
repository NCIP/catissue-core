<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>


<html>
	<head>
			<title></title>
			<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
			<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
			<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>
			<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
	</head>

<html:form action="LoadDynamicExtentionsDataEntryPage">



	
	<%
	
	String pageOf1 = (String) request.getAttribute("pageOf");
	String id1 = (String) request.getAttribute("id");
	String participantEntityId1 = (String) request.getAttribute("entityId");
	String entityRecordId = (String) request.getAttribute("entityRecordId");
	String staticEntityName1 = (String) request.getAttribute("staticEntityName");

	String url = "LoadAnnotationDataEntryPage.do?pageOf="+pageOf1+"&id="+id1+"&entityId="+participantEntityId1+"&entityRecordId="+entityRecordId+"&staticEntityName="+staticEntityName1;

	
	%>
	<iframe src = "<%=url%>" style = "overflow-y:auto" height = 100% width = "100%" name = "dynamicExtensionsFrame" id = "dynamicExtensionsFrame" frameborder="0">
	</iframe>

	<html:hidden property="id" /><html:hidden property="pageOf"/>
</html:form>
</html>