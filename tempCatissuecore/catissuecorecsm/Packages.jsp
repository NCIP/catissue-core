<%@page contentType="text/html"%>
<HTML>
<HEAD>
<TITLE>Happy Page</TITLE>

</HEAD>
<BODY>
<%@ page import="gov.nih.nci.system.applicationservice.*,
				 gov.nih.nci.common.util.*,
				 java.lang.reflect.*,
				 java.util.*" %>
<a href="Classes.jsp?package=All" target="packageClasses" >All Classes</a>
<br><br>
Packages
<br>
<% JSPUtils jspUtils= null;
String message=null;
List packages = null;
String selectedDomain=null, selectedSearchDomain=null, query=null;
try
{	
	jspUtils = JSPUtils.getJSPUtils(config);
	if(packages == null)
	{
		packages = jspUtils.getPackages();		
	}	
}
catch(Exception ex){
	message=ex.getMessage();
}
%>
<%
for( Iterator i= packages.iterator(); i.hasNext();)
{
String pkgName = (String)i.next();
%>
<a href="Classes.jsp?package=<%=pkgName %>" target="packageClasses"><%=pkgName %> </a>
<br>
<%
}
%>

</BODY>
</HTML>