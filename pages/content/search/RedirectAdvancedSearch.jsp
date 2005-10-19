<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

	<%
		//String specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
		String parentUrl = "top.window.location.href='AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&operation=refresh'";
	%>

	<body onload="<%=parentUrl%>" />
