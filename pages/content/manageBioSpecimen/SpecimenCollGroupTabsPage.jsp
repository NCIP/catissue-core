<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%
	String annotationLink = request.getContextPath() + "/LoadAnnotationDataEntryPage.do?entityId=223&entityRecordId=1";
	String operation = (String)request.getParameter("operation");
	Long id = (Long)request.getAttribute("id");
	String queryString = request.getQueryString();

	if(queryString==null)
	{
			queryString = queryString  + "operation=edit&pageOf=pageOfParticipant";
	}
	queryString = queryString + "&pageOf=pageOfParticipant&id="+id;
	Long participantEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
	String participantAnnotationsQueryString = "?entityId="+participantEntityId+"&entityRecordId="+id;	
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
			if(opr == "afterAdd")
			{
				top.location.href="<%=request.getContextPath()%>/LoadSpecimenCollGroupPage.do?operation=edit&id=<%=id%>";
			}
			else
			{
				if((opr == 'null')||(opr=="add"))
				{	
					initiallizeAddSCGTabs('<%=request.getContextPath()%>');
				}
				else
				{
					initiallizeEditSCGTabs('<%=request.getContextPath()%>','<%=queryString%>','<%=participantAnnotationsQueryString%>');
				}
			} 
</script>
</body>
</html>