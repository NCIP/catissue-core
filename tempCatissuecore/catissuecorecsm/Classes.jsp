<HTML>
<HEAD>
<TITLE>Happy Page</TITLE>

</HEAD>
<BODY>
<%@ page import="gov.nih.nci.common.util.*,
				 java.lang.reflect.*,
				 java.util.*" %>
<% JSPUtils jspUtils= null;
List classes = null;
String message = null;
int pos=0;
String packageName = request.getParameter("package");
try
{	
	jspUtils = JSPUtils.getJSPUtils(config);
	classes = jspUtils.getClassNames(packageName);
	if(((String)classes.get(0)).equals("Please choose")) classes.remove(0);
	
}
catch(Exception ex){
	message=ex.getMessage();
}
if(classes != null)
{
	String className="";
	if(packageName == null || packageName.equalsIgnoreCase("All"))
	{
	%>All Classes
	<%}
	else
	{ %>	
	<%=packageName%>
	<%}%>
	<br><br>
	<%
	for(int i=0; i< classes.size(); i++)
	{		
		className = (String)classes.get(i);
		pos = className.lastIndexOf(".");
		String klass = "";
		klass = className.substring(pos + 1);
		if(klass != null && klass.length() <= 0)
		{// in case there is no package
			klass = className;
		}
		
%>
<a href="Criteria.jsp?klassName=<%=className %>" target="classFrame"><%=klass %> </a>
<br>
<%
		
		
	}
}
%>

</BODY>
</HTML>

