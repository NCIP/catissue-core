<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<logic:present name="statusMessageKey">
	<%
		Long eventId = (Long)request.getAttribute(Constants.SYSTEM_IDENTIFIER);
		
		System.out.println("EventID on RedirectEventParameters.jsp===>"+eventId);
		String disposalflag = request.getParameter("disposal");
		String parentUrl = "";
		

		if(eventId == null)
			eventId=new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER));
	
		System.out.println("disposal: -------------"+request.getParameter("disposal"));
	    if(disposalflag!=null&&disposalflag.equals("true"))
		{
			parentUrl = "window.parent.location.href='manageBioSpecimenHomeAction.do?'";
		}
		else
		{
			parentUrl = "window.parent.location.href='ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&eventId="+eventId.toString()+"'";
		}
	
		if(session.getAttribute("CPQuery") != null)
		{
			System.out.println("disposal: "+request.getParameter("disposal"));
			if(disposalflag!=null&&disposalflag.equals("true"))
			{
				parentUrl = "window.parent.location.href='homeAction.do?'";
			}
			else
			{
				parentUrl = "window.parent.location.href='CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery&eventId="+eventId.toString()+"'";
			}	
		}			


	%>
	<body onload="<%=parentUrl%>" />
</logic:present>

