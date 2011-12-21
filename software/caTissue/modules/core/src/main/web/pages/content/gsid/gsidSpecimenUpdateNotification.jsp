<%@ page language="java" isELIgnored="false"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="edu.wustl.catissuecore.GSID.GSIDConstant" %>
<% 
String message= (String)request.getAttribute("GSIDMessage");
if (message!=null)
	out.println(message);
%>
