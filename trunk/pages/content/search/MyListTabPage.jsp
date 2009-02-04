
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
		var contextPath = "<%=request.getContextPath()%>";
		tabbar= new dhtmlXTabBar("a_tabbar","top");
		tabbar.setImagePath("dhtml_comp/imgs/");
		tabbar.setHrefMode("iframes-on-demand");
		var tabIds = ["specimen","pathologicalCase","array"];
		var tabNames = ["BioSpecimen" , "Pathological Case", "BioSpecimen Array"];
		var tabHREFs = [contextPath + "/ShoppingCart.do?tabIndex=1"  , contextPath + "/ShoppingCart.do?tabIndex=2" ,contextPath + "/ShoppingCart.do?tabIndex=3"];
		initializeTabs(tabIds,tabNames,tabHREFs);
</script>
</body>
</html>