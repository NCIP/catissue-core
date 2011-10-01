<%@
	page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"
%>
<%
	request.getSession().setAttribute("mandatory_Message", "true");
	String dynamicExtensionsDataEntryURL =(String) request.getAttribute(AnnotationConstants.DYNAMIC_EXTN_DATA_ENTRY_URL_ATTRIB);
	dynamicExtensionsDataEntryURL= dynamicExtensionsDataEntryURL+"&showInDiv=true";
%>
<html>

<script>
	document.location.href = "<%=dynamicExtensionsDataEntryURL%>";
</script>
</html>