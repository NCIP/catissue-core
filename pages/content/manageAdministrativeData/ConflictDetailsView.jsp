<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.caties.util.CaTIESConstants"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<%
	String frame1Ysize = "120";
	String frame2Ysize = "360";
	String frame3Ysize = "360";
	
	String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);
	String conflictStatus = (String)request.getParameter(Constants.CONFLICT_STATUS);
	String surgicalPathologyNumber = (String)request.getParameter(Constants.SURGICAL_PATHOLOGY_NUMBER);
	String reportDate = (String)request.getParameter(Constants.REPORT_DATE);
	String siteName = (String)request.getParameter(Constants.SITE_NAME);
%>


<head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<html:errors />
<body>
<script language="javascript">

//This function is called when action button is pressed to resolve the conflict:
function onButtonClick(buttonPressed)
	{
				
		var actionUrl = "ConflictResolver.do?reportQueueId=<%=reportQueueId%>&conflictButton="+buttonPressed;
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		
	}
</script>
<html:form action="ConflictResolver.do">

	<table border="0"  width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%"  valign="top">
						<iframe id="<%=Constants.CONFLICT_COMMON_VIEW%>" name="<%=Constants.CONFLICT_COMMON_VIEW%>" src="<%=Constants.CONFLICT_COMMON_VIEW_ACTION%>?conflictStatus=<%=conflictStatus%>&reportQueueId=<%=reportQueueId%>&surgicalPathologyNumber=<%=surgicalPathologyNumber%>&reportDate=<%=reportDate%>&siteName=<%=siteName%>" scrolling="no" frameborder="0" width="100%" height="<%=frame1Ysize%>" marginheight=0 marginwidth=0>
									Your Browser doesn't support IFrames.
						</iframe>
					</td>
			    </tr>
				<tr>
					<td width="100%" valign="top">
					 <%if(conflictStatus.equals(CaTIESConstants.STATUS_SCG_CONFLICT))
						{
					%>
						<iframe name="" src="ConflictSCGAction.do?reportQueueId=<%=reportQueueId%>" scrolling="no" frameborder="0" width="100%" height="<%=frame3Ysize%>">
									Your Browser doesn't support IFrames.
						</iframe>

					<%}
					   else
						{
					%>
						<table border="0" width="100%" >
							<tr>
								<td width="30%"  valign="top">		
			
								<iframe id="<%=Constants.CONFLICT_TREE_VIEW%>" name="<%=Constants.CONFLICT_TREE_VIEW%>" src="<%=Constants.CONFLICT_TREE_VIEW_ACTION%>?reportQueueId=<%=reportQueueId%>&conflictStatus=<%=conflictStatus%>" scrolling="no" frameborder="0" width="100%" height="<%=frame2Ysize%>" marginheight=0 marginwidth=0 valign="top">
									Your Browser doesn't support IFrames.
								</iframe>
			
								</td>
							
								<td width="70%"  valign="top" >				
									<iframe name="<%=Constants.CONFLICT_DATA_VIEW%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="no" frameborder="0" width="100%" height="<%=frame3Ysize%>">
									Your Browser doesn't support IFrames.
									</iframe>
								</td>
							</tr>	
						</table>	
				<%}%>
					</td>
					
				</tr>
				<tr >
					<td>
					<%if(conflictStatus.equals(CaTIESConstants.STATUS_SCG_CONFLICT))
						{
					%>
						<%@ include file="/pages/content/manageAdministrativeData/ConflictSCGButtons.jsp"%>
					<%}
					   else
						{
					%>
						<%@ include file="/pages/content/manageAdministrativeData/ConflictPageButtons.jsp"%>

					<%}%>
					</td>
				</tr>
			</table>	
	
</html:form>