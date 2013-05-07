<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript"  type="text/javascript" src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlx_suite/ext/dhtmlxgrid_srnd.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
    <link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
    <link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxgrid_dhx_skyblue.css">

<script>
	var columns =${requestScope.columns};
</script>
<table width="100%" cellpadding="3" cellspacing="0" border="0"	>
	<tr>
		<td>
			<script>	
					document.write("<div id='gridbox' width='100%' height='400px' style='background-color:#d7d7d7;overflow:hidden'></div>");
					document.write("<div id='csPagingArea'></div>");
					document.write("<div id='csInfoArea'></div>");    
			</script>
		</td>
	</tr>
</table>

<script>
	
function init_grid()
{			
	var funcName = "rowClick";

	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtmlx_suite/imgs/");
	mygrid.setHeader(columns);
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,#text_filter,#select_filter"); 
	mygrid.setEditable("FALSE");
	mygrid.enableAutoHeigth(false);
	mygrid.enablePaging(true, 15, 5, csPagingArea, true, csInfoArea);
    mygrid.setPagingSkin("bricks");

    mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,20,20,15,*");
	
	
    mygrid.setSkin("dhx_skyblue");
	
	mygrid.setColSorting("str,str,str,str,str");
	mygrid.enableMultiselect(true);
	mygrid.load("RequestListView.do?requestFor=gridData","", "json");
	mygrid.init();

	mygrid.setSizes();
}
function refreshGrid(dpName)
{
	mygrid.clearAll();
	mygrid.load("RequestListView.do?requestFor=gridData&dpName="+dpName,"", "json");
}
function showPendingNewOrders()
{
	mygrid.clearAll();
	mygrid.load("RequestListView.do?requestFor=gridData","", "json");
}
window.onload=init_grid;
</script>