<%
	String annotationLink = request.getContextPath() + "/LoadAnnotationDataEntryPage.do?entityId=223&entityRecordId=1";
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
<div id="a_tabbar" style="width:600;height:500;background-color:white;overflow:auto" ></div>
<script>
            tabbar= new dhtmlXTabBar("a_tabbar","top");
			tabbar.setImagePath("<%=request.getContextPath()%>/dhtml_comp/imgs/");
			tabbar.setHrefMode("iframes-on-demand");
			var tabIds = ["addTab","viewSPRTab","annotationsTab"];
			var tabNames = ["Add Partitipant" , "View SPR", "Annotations"];
			var tabHREFs = ["<%=request.getContextPath()%>/pages/content/manageBioSpecimen/EditParticipant.jsp","<%=request.getContextPath()%>/pages/content/manageBioSpecimen/ViewSurgicalPathologyReport.jsp","<%=request.getContextPath()%>/LoadAnnotationDataEntryPage.do?entityId=223&entityRecordId=1"];
			initializeTabs(tabIds,tabNames,tabHREFs);
</script>


</html>