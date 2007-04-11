<%@
	page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"
%>
<%
	String dynamicExtensionsURL =(String) request.getAttribute(AnnotationConstants.DYNAMIC_EXTN_URL_ATTRIB);
%>

<html>
<body >
	<iframe name = "dynamicExtensionsFrame" scrolling = "yes" id = "dynamicExtensionsFrame" width = "100%" height = "100%" src = "<%=dynamicExtensionsURL%>" frameborder="0">
	</iframe>
</body>
</html>