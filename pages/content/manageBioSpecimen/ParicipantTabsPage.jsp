<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%

	String operation = (String)request.getParameter(Constants.OPERATION);
	if (operation == null)
	{
		operation="add";
	}
	String submittedFor=(String)request.getParameter(Constants.SUBMITTED_FOR);
	System.out.println("************////////////"+submittedFor);
	String forwardTo=(String)request.getParameter(Constants.FORWARD_TO);
	String participantId = request.getParameter("id");
	Long id = (Long)request.getAttribute("id");
	String queryString = request.getQueryString();
	
	if(queryString==null)
	{

			queryString = "operation="+operation+"&pageOf=pageOfParticipant&menuSelected=12";
	}
	else 
	{
		queryString = queryString  + "&operation="+operation+"&pageOf=pageOfParticipant&menuSelected=12";
	}
	queryString = queryString + "&id="+id+"&"+"submittedFor="+submittedFor+"&"+Constants.FORWARD_TO+"="+forwardTo;

	Long participantEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
	String participantAnnotationsQueryString = "?entityId="+participantEntityId+"&entityRecordId="+participantId;
%>
<html>
<head>
	<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXTabbar.css"/>
	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<script  src="<%=request.getContextPath()%>/jss/javaScript.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXTabbar.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXTabBar_start.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid.js"></script>
		<script  src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGridCell.js"></script>
		<script src="<%=request.getContextPath()%>/jss/javaScript.js" type="text/javascript"></script>

</head>
<body>
<div id="a_tabbar" style="width:800;height:600" ></div>
<script>
			var opr = '<%=operation%>';
			var queryString = '<%=queryString%>';
			if(opr == "afterAdd")
			{
				top.location.href="<%=request.getContextPath()%>/LoadParticipantPage.do?operation=edit&id=<%=id%>";
			}
			else
			{
				if((opr == 'null')||(opr=="add"))
				{
					initiallizeAddParticipantTabs('<%=request.getContextPath()%>','<%=queryString%>');
				}
				else
				{
					initiallizeEditParticipantTabs('<%=request.getContextPath()%>','<%=queryString%>','<%=participantAnnotationsQueryString%>');
				}
			}
</script>
</body>
</html>