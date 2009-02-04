<!-- dataList and columnList are to be set in the main JSP file -->
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
	<script  src="dhtml_comp/js/dhtmlXCommon.js"></script>
	<script  src="dhtml_comp/js/dhtmlXGrid.js"></script>		
	<script  src="dhtml_comp/js/dhtmlXGridCell.js"></script>	
	<script  src="dhtml_comp/js/dhtmlXGrid_mcol.js"></script>	
	

<script>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=Utility.toNewGridFormat(row.get(j))%>,<%}%><%=Utility.toNewGridFormat(row.get(j))%><%="\""%>,<%}%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=Utility.toNewGridFormat(row.get(j))%>,<%}%><%=Utility.toNewGridFormat(row.get(j))%><%="\""%>
];


var columns = <%="\""%><%int col;%><%for(col=0;col<(columnList.size()-1);col++){%><%=columnList.get(col)%>,<%}%><%=columnList.get(col)%><%="\""%>;

var colWidth = <%="\""%><%for(col=0;col<(columnList.size()-1);col++){%><%=100%>,<%}%><%=100%><%="\""%>;

var colTypes = <%="\""%><%=Variables.prepareColTypes(dataList)%><%="\""%>;

</script>

<table width="100%">
	<tr>
		<td>
			<script>	
			//for derive only
			if(useFunction == "derivedSpecimenGrid")	// useFunction == "eventParametersGrid" ||
			{
				document.write("<div id='gridbox' width='100%' height='150px' style='background-color:white;overflow:hidden'></div>");
			}
			else
			{
				document.write("<div id='gridbox' width='100%' height='350px' style='background-color:white;overflow:hidden'></div>");
			}
			</script>
		</td>
	</tr>
</table>

<script>
/*	function rowClick(id)
	{
		var colid = <%=identifierFieldIndex.intValue()%>;
		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		var url = "SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+searchId;
		window.location.href = url;
	}
*/

// function modified to display distribution array.
// problem in query data.
	function rowClick(id)
	{
		var colid = <%=identifierFieldIndex.intValue()%>;
/*		var x=document.getElementsByName("pageOf");
		//alert(x);
		if(x[0].value == "pageOfArrayDistribution")
		{
			colid=0;
		}
		//alert(colid);
*/
		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		var url = "SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+searchId;
		window.location.href = url;
	} 			
 			
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

//	document.write("<hr>"+colWidth+"<hr>");
	if(useFunction == "eventParametersGrid" || useFunction == "derivedSpecimenGrid")
	{
		colWidth = "130,130,130,130,0";
	}
//	document.write("<hr>"+colWidth+"<hr>");
	if(useFunction == "viewSPR")
	{
		colWidth="130,130,130,130,0,130,130";
	}
	mygrid.setInitWidths(colWidth);

	//mygrid.setColAlign("left,left")
	mygrid.setColSorting(colTypes);
	//mygrid.enableMultiselect(true)
	mygrid.init();

	/*
	mygrid.loadXML("dhtmlxgrid/grid.xml");

		clears the dummy data and refreshes the grid.
		fix for grid display on IE for first time.
			mygrid.clearAll();

	*/

	for(var row=0;row<myData.length;row++)
	{
		mygrid.addRow(row+1,myData[row],row+1);
	}
	
	if(useFunction == "eventParametersGrid" || useFunction == "derivedSpecimenGrid")
	{
		mygrid.setHeaderCol(4,"");
		mygrid.setColumnHidden(4,true);
	}
	//mygrid.setOnRowSelectHandler(funcName);
	mygrid.setOnRowDblClickedHandler(funcName);
	mygrid.setSizes();
</script>