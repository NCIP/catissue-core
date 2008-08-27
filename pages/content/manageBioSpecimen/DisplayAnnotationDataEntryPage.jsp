<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>

<script>
if ( document.getElementById && !(document.all) ) 
	{
		var slope=-40;
	}
	else
	{
		var slope=-40;
	}

window.onload = function() { setFrameHeight('dynamicExtensionsFrame', .9,slope);}
window.onresize = function() { setFrameHeight('dynamicExtensionsFrame', .9,slope); }

</script>
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

	
	%><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
	<iframe src = "<%=url%>" scrolling="no" height = "380" width = "99%" name = "dynamicExtensionsFrame" id = "dynamicExtensionsFrame" frameborder="0" marginheight="0" marginwidth="0">
	</iframe>
	</td></tr></table>

	<html:hidden property="id" /><html:hidden property="pageOf"/>
</html:form>
</html>