<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script  src="dhtml_comp/js/dhtmlXCommon.js"></script>
<script  src="dhtml_comp/js/dhtmlXGrid.js"></script>		
<script  src="dhtml_comp/js/dhtmlXGridCell.js"></script>	
<script  src="dhtml_comp/js/dhtmlXGrid_mcol.js"></script>	
<script>
	var myData =${requestScope.myData};
	var columns =${requestScope.columns};
	var colWidth =${requestScope.colWidth};
	var colWidthp =${requestScope.colWidthInPercent};
	var colTypes =${requestScope.colTypes};
</script>
<table width="100%">
	<tr>
		<td>
			<script>	
				//for derive only
				if(useFunction == "derivedSpecimenGrid")	// useFunction == "eventParametersGrid" ||
				{
					document.write("<div id='gridbox' width='100%' height='150px' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
				// Patch ID: SpecimenEventSpaceUtilization_2
				else if(useFunction == "eventParametersGrid") 
				{
					document.write("<div id='gridbox' width='100%' height='125px' border='0' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
				else
				{
					document.write("<div id='gridbox' width='100%' height='320px' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
			</script>
		</td>
	</tr>
</table>

<script>
	// function modified to display distribution array.
	// problem in query data.
	function rowClick(id)
	{
		var colid ='${requestScope.identifierFieldIndex}';
		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		var url = "SearchObject.do?pageOf=${requestScope.pageOf}&operation=search&id="+searchId;
		var pageOf="${requestScope.pageOf}";
		if(pageOf=="pageOfCollectionProtocol")
		{
			url = "RetrieveCollectionProtocol.do?&id="+searchId;
		}
		window.location.href = url;
	}
	
function init_grid()
{			
	var funcName = "rowClick";
	if(useDefaultRowClickHandler == 1)
	{
		funcName = "rowClick";	
	}
	else
	{
		funcName=useFunction;
	}

	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtml_comp/imgs/");
	mygrid.setHeader(columns);
	mygrid.setEditable("FALSE");
	mygrid.enableAutoHeigth(false);

	//document.write("<hr>"+colWidth+"<hr>");
	if(useFunction == "derivedSpecimenGrid")
	{
		colWidth = "130,130,130,130,0";
	}

	if(useFunction == "eventParametersGrid")
	{
		//colWidth = "167,167,167,167,11";
		colWidth = "10,35,30,25,0";
	}

	if(useFunction == "goToConflictDetails" )
	{
		colWidth = "33,0,33,34,0,0,0";
	}

	//document.write("<hr>"+colWidth+"<hr>");
    mygrid.enableRowsHover(true,'grid_hover')
	if(useFunction == "eventParametersGrid" || useFunction == "goToConflictDetails")
	{
		mygrid.setInitWidthsP(colWidth);
	}
	else
	{
		if(colWidthp!="")
		{
			mygrid.setInitWidthsP(colWidthp);
		}
		else
		{
			mygrid.setInitWidths(colWidth);
		}
	}
	
	mygrid.setSkin("light");
	mygrid.enableAlterCss("even","uneven");
	//mygrid.setColAlign("left,left")
	mygrid.setColSorting(colTypes);
	//mygrid.enableMultiselect(true)
	mygrid.init();

	/*
	mygrid.loadXML("dhtmlxgrid/grid.xml");
	// clears the dummy data and refreshes the grid.
	// fix for grid display on IE for first time.
	mygrid.clearAll();
	*/

	for(var row=0;row<myData.length;row++)
	{
		mygrid.addRow(row+1,myData[row],row+1);
	}

	//useFunction == "eventParametersGrid" || 
	if (useFunction == "derivedSpecimenGrid" )
	{
		mygrid.setHeaderCol(4,"");	
		mygrid.setColumnHidden(4,true);
	}

	//mygrid.setOnRowSelectHandler(funcName);
	mygrid.setOnRowDblClickedHandler(funcName);
	// :To hide ID columns by kalpana
	function getIDColumns()
		{
			var hiddenColumnNumbers = new Array();
			${requestScope.hiddenColumnNumbers}
			return hiddenColumnNumbers;
		}
	
	
	// :To hide ID columns
		var hideCols = getIDColumns();
		for(i=0;i<hideCols.length;i++)
		{
			mygrid.setHeaderCol(hideCols[i],"");
			mygrid.setColumnHidden(hideCols[i],true);
		}


	mygrid.setSizes();
}

window.onload=init_grid;
</script>