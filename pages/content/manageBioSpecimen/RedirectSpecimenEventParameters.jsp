<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<logic:present name="statusMessageKey">
	<%
		String specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
		String parentUrl = "top.window.location.href='NewSpecimenEventParameters.do?pageOf=pageOfNewSpecimenEventParameters&specimenId="+specimenId+"'";
	%>

	<body onload="<%=parentUrl%>" />
</logic:present>