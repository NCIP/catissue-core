<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>

<script>
if ( document.getElementById && !(document.all) ) 
	{
		var slope=5;
	}
	else
	{
		var slope=0;
	}

// window.onload = function() { adjFrmHt('dynamicExtensionsFrame', .9,slope);}
// window.onresize = function() { adjFrmHt('dynamicExtensionsFrame', .9,slope); }
// <!-- Mandar 19Nov08 :  -->
var pcnt=53.7;
window.onresize = function() {  mdFrmResizer("dynamicExtensionsFrame",pcnt); }
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
	
	%><table height="100%" width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg"><tr height="100%"><td height="100%"><!-- Mandar : 24Nov08 adjusted height -->
	<iframe src = "<%=url%>" scrolling="no" height = "100%" width = "99%" name = "dynamicExtensionsFrame" id = "dynamicExtensionsFrame" frameborder="0" marginheight="0" marginwidth="0">
	</iframe>
	<html:hidden property="id" /><html:hidden property="pageOf"/>
	</td></tr>
	<!-- <tr height="*"><td>&nbsp;</td></tr> -->
	</table>
</html:form>
<SCRIPT LANGUAGE="JavaScript">
<!--
mdFrmResizer("dynamicExtensionsFrame",pcnt);
//-->
</SCRIPT>
</html>