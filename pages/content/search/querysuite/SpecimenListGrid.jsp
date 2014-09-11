<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js">
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js" type="text/javascript" charset="utf-8"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_srnd.js"></script>
    <script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
    <script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
    <link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
	<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
	<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css">
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/excells/dhtmlxgrid_excell_combo.js"></script>

<table width="100%">
<tr>
<td>

<div id='gridbox' width='100%'></div>
<div id='csPagingArea'></div>
<div id='csInfoArea'></div>

</td>
</tr>
</table>

<script>
	
function init_grid()
{			
	var funcName = "rowClick";

	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
	mygrid.setHeader("Specimen Label,Specimen Class,Specimen Type,Available Qty,Requested Qty,Status,Comments");
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,,,,,"); 
	mygrid.setEditable("true");
	mygrid.enableAutoHeigth(false);
	//mygrid.enablePaging(true, 15, 5, csPagingArea, true, csInfoArea);
    mygrid.setPagingSkin("bricks");

    mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,10,10,10,10,20,*");
	
	mygrid.setColTypes("ro,ro,ro,ro,ed,coro,ed");
	mygrid.setSkin("light");
	
	mygrid.setColSorting("str,str,str,str,str,str,str");
	mygrid.enableMultiselect(true);
	
	
	mygrid.init();
	var gridQString = "LoadGridServlet?paramJson=1&aliasName=aliasName&pageOf=pageOf";//save query string to global variable (see step 5 for details)
	 
	 mygrid.loadXML(gridQString+"&connector=true");
	
	

mygrid.enableEditEvents(true);

}

window.onload=init_grid;
</script>