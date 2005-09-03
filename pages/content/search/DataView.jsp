<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants"%>

<style> body, html {margin:0px; padding: 0px; overflow: hidden;} </style>

<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>

<!-- grid format -->

	<style>
		.active-controls-grid {height: 100%; font: menu;}
		
		.active-column-0 {width:  80px;}
		.active-column-1 {width: 200px;}
		.active-column-2 {text-align: right;}
		.active-column-3 {text-align: right;}
		.active-column-4 {text-align: right;}
		
		.active-grid-column {border-right: 1px solid threedlightshadow;}
		.active-grid-row {border-bottom: 1px solid threedlightshadow;}
	</style>

	<!--  grid data -->
<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>

<script>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"],<%}%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"]
];

var columns = [<%int k;%><%for (k=0;k < (columnList.length-1);k++){%>"<%=columnList[k]%>",<%}%>"<%=columnList[k]%>"];

</script>

<script>

	//	create ActiveWidgets Grid javascript object.
	var obj = new Active.Controls.Grid;
	
	//	set number of rows/columns.
	obj.setRowProperty("count", <%=dataList.size()%>);
	obj.setColumnProperty("count", <%=columnList.length%>);
	
	//	provide cells and headers text
	obj.setDataProperty("text", function(i, j){return myData[i][j]});
	obj.setColumnProperty("text", function(i){return columns[i]});
	obj.setDataProperty("value", function(i){return myData[i][0]});
	
	//	set headers width/height.
	obj.setRowHeaderWidth("28px");
	obj.setColumnHeaderHeight("20px");
	
	//	set click action handler.
	obj.setAction("click", function(src){window.location.href = 'SearchObject.do?pageOf=<%=pageOf%>&operation=search&systemIdentifier='+src.getDataProperty("value")});
	
	//	write grid html to the page.
	document.write(obj);
	
</script>