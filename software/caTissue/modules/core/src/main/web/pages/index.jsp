<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%
boolean isCasAvlbl =Variables.isCasAvl;
if(isCasAvlbl)
{
%>
	<jsp:forward page="/Home.do" />

<%
}
else
{
%>
	<jsp:forward page="/RedirectHome.do" />
<%
}
%>

