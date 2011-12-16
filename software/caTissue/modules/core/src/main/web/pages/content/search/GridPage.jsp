<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css" href="newDhtmlx/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript"  type="text/javascript" src="newDhtmlx/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="newDhtmlx/dhtmlxgrid.js"></script>
<script   language="JavaScript" type="text/javascript" src="newDhtmlx/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="newDhtmlx/ext/dhtmlxgrid_srnd.js"></script>
<script>
	var myData =${requestScope.myData};
	var columns =${requestScope.columns};
	var colWidth =${requestScope.colWidth};
	var isWidthInPercent=${requestScope.isWidthInPercent};
	var colTypes =${requestScope.colTypes};
</script>
<table width="100%" cellpadding="3" cellspacing="0" border="0"	>
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
				else if(useFunction == "goToConflictDetails") 
				{
                      document.write("<div id='gridbox' width='100%' height='325px' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
				else
				{
					document.write("<div id='gridbox' width='100%' height='340px' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
			</script>
		</td>
	</tr>
</table>

<script>
	// function modified to display distribution array.
	// problem in query data.
	function getIDColumn()
	{
		var hiddenColumnNumbers = new Array();
		${requestScope.hiddenColumnNumbers}
		return hiddenColumnNumbers;
	}
	
	function checkHashed(id,hiddenColumnNumbers)
	{
		for(i=0;i<hiddenColumnNumbers;i++)
		{
			if(mygrid.cells(id,i).getValue()=="##")
			{
				return 0;
			}
		}
		return 1;
	}
	
	function rowClick(id,colid)
	{
		
		colid ='${requestScope.identifierFieldIndex}';
		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		
		var url = "SearchObject.do?pageOf=${requestScope.pageOf}&operation=search&id="+searchId;
		var hiddenColumnNumbers= getIDColumn();
		var pageOf="${requestScope.pageOf}";
		if(pageOf=="pageOfNewSpecimen")
		{
			hiddenColumnNumbers=17;
		}
		if(pageOf=="pageOfCollectionProtocol")
		{
			url = "RetrieveCollectionProtocol.do?&id="+searchId;
		}
		var flag=checkHashed(id,hiddenColumnNumbers);
		if(flag==1)
		{
			window.location.href = url;
		}	
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
	mygrid.setImagePath("newDhtmlx/imgs/");
	mygrid.setHeader(columns);
	mygrid.setEditable("FALSE");
	mygrid.enableAutoHeigth(false);

	//document.write("<hr>"+colWidth+"<hr>");


	if(useFunction == "eventParametersGrid")
	{
		//colWidth = "167,167,167,167,11";
		colWidth = "10,35,30,25,0";
	}

	if(useFunction == "goToConflictDetails" )
	{
		colWidth = "33.3,0,33,34,0,0,0";
	}
	
	//document.write("<hr>"+colWidth+"<hr>");
    mygrid.enableRowsHover(true,'grid_hover')

	if(isWidthInPercent)
	{
		mygrid.setInitWidthsP(colWidth);
	}
	else
	{
		mygrid.setInitWidths(colWidth);
	}
	
	mygrid.setSkin("light");
	mygrid.enableAlterCss("even","uneven");
	//mygrid.setColAlign("left,left")
	alert(colTypes);
	mygrid.setColSorting(colTypes);
	mygrid.enableMultiselect(true)
	mygrid.init();

	/*
	mygrid.loadXML("dhtmlxgrid/grid.xml");
	// clears the dummy data and refreshes the grid.
	// fix for grid display on IE for first time.
	mygrid.clearAll();
	*/

	for(var row=0;row<myData.length;row++)
	{
		mygrid.addRow(row+1,myData[row],row);
	}

	//useFunction == "eventParametersGrid" || 
	if (useFunction == "derivedSpecimenGrid" )
	{
		mygrid.setHeaderCol(4,"");	
		mygrid.setColumnHidden(4,true);
	}

	//mygrid.setOnRowSelectHandler(funcName);
	mygrid.attachEvent("onRowDblClicked", function(rId,cInd){rowClick(rId,cInd)});  
//	mygrid.setOnRowDblClickedHandler(funcName);
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