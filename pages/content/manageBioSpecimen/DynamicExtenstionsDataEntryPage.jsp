<%@
	page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"
%>
<%
	String dynamicExtensionsDataEntryURL =(String) request.getAttribute(AnnotationConstants.DYNAMIC_EXTN_DATA_ENTRY_URL_ATTRIB);
%>
<html>

<script>
	document.location.href = "<%=dynamicExtensionsDataEntryURL%>";
</script>
</html>