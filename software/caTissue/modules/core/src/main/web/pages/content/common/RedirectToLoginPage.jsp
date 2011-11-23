<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%
	String parentUrl = null;

boolean isCasAvlbl = Variables.isCasAvl;
	System.out.println(isCasAvlbl);
if(isCasAvlbl)
{
	parentUrl = "top.window.location.href='CasLogin.do'";
}
else
{
	parentUrl = "top.window.location.href='Login.do'";
}

	
%>

<body onload="<%=parentUrl%>" />