<%
	String annotationLink = request.getContextPath() + "/LoadAnnotationDataEntryPage.do?entityId=223&entityRecordId=1";
	String operation = (String)request.getParameter("operation");
	Long id = (Long)request.getAttribute("id");
	String queryString = request.getQueryString();

	if(queryString==null)
	{
			queryString = queryString  + "operation=edit&pageOf=pageOfParticipant";
	}
	queryString = queryString  +"&id="+id;
	
	
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
			/*if(top.location!=location)
			{
				top.location.href = location.href;

			}*/
			
			var opr = '<%=operation%>';
			alert(opr);
			alert("Quesry String " +'<%=queryString%>');
			if((opr == 'null')||(opr=="add"))
			{	
				initiallizeAddParticipantTabs('<%=request.getContextPath()%>');
			}
			else
			{
				initiallizeEditParticipantTabs('<%=request.getContextPath()%>','<%=queryString%>');
			} 
</script>
</body>
</html>