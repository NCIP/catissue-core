<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%
	String parentUrl = null;

boolean isCasAvlbl = Variables.isCasAvl;
if(isCasAvlbl)
{
	parentUrl = "top.window.location.href='CasLogin.do'";
}
else
{
	parentUrl = "top.window.location.href='RedirectHome.do'";
}

	
%>

<body onload="<%=parentUrl%>" />