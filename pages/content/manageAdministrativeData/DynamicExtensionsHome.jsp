<%@
	page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"
%>
<%
	String dynamicExtensionsURL =(String) request.getAttribute(AnnotationConstants.DYNAMIC_EXTN_URL_ATTRIB);
          boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && (os.toString().toLowerCase().indexOf("mac")!=-1 || os.toString().indexOf("Safari")!=-1))
			{
			    mac = true;
			}
	String height = "100%";		
	if(mac)
	{
	  height="450";
    }
%>
<html>
<body >
	<iframe name = "dynamicExtensionsFrame" scrolling = "yes" id = "dynamicExtensionsFrame" width = "100%" height = "<%=height%>"  src = "<%=dynamicExtensionsURL%>" frameborder="0">
	</iframe>
</body>
</html>