<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="org.apache.struts.Globals, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionErrors"%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%
	String parentUrl = "";
	String specimenId = null;
	if(request.getAttribute(Constants.SPECIMEN_ID) != null)
	{
		specimenId = request.getAttribute(Constants.SPECIMEN_ID).toString();
	}
	parentUrl = "window.parent.location.href='ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&specimenId="+specimenId+"'";

	if(session.getAttribute("CPQuery") != null)
	{
		parentUrl = "window.parent.location.href='CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery&specimenId="+specimenId+"'";
	}
	//System.out.println("parentUrl  ::: "+parentUrl);
	if(request.getAttribute(Globals.ERROR_KEY)!=null)
	{
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		request.getSession().setAttribute("errors", errors);
	}
%>

<body  onload="<%=parentUrl%>" />

