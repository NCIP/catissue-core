				<!-- dataList and columnList are to be set in the main JSP file -->
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
	<script  src="dhtml_comp/js/dhtmlXCommon.js"></script>
	<script  src="dhtml_comp/js/dhtmlXGrid.js"></script>		
	<script  src="dhtml_comp/js/dhtmlXGridCell.js"></script>	

<script>
// --------------------  FUNCTION SECTION
			//checks or unchecks all the check boxes in the grid.
					function checkAll(element)
					{
						var state=element.checked;
						rowCount = mygrid.getRowsNum();
						//alert("rowCount : "+ rowCount);
						for(i=1;i<=rowCount;i++)
						{
							var cl = mygrid.cells(i,0);
							if(cl.isCheckbox())
							cl.setChecked(state);
						}
					}

					//function to update hidden fields as per check box selections.
		function updateHiddenFields()
		{
			var isChecked = "false";
			var checkedRows = mygrid.getCheckedRows(0);
			if(checkedRows.length > 0)
			{
	        	isChecked = "true";
				var cb = checkedRows.split(",");
				rowCount = mygrid.getRowsNum();
				for(i=1;i<=rowCount;i++)
				{
					var cl = mygrid.cells(i,0);
					if(cl.isChecked())
					{
						var cbvalue = document.getElementById(""+(i-1));
						cbvalue.value="1";
						cbvalue.disabled=false;
					}
					else
					{
						var cbvalue = document.getElementById(""+(i-1));
						cbvalue.value="0";
						cbvalue.disabled=true;
					}
				}
			}
			else
			{
				isChecked = "false";
			}
			return isChecked;
		}	

// ------------------------------  FUNCTION SECTION END
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

var colWidth = "<%=Utility.getColumnWidth(columnList)%>";
var colTypes = <%="\""%><%=Variables.prepareColTypes(dataList,true)%><%="\""%>;

var colDataTypes = colTypes;

while(colDataTypes.indexOf("str") !=-1)
colDataTypes=colDataTypes.replace(/str/,"ro");
/*
document.write("<hr>myData[0] : "+myData[0]+"<hr>");
document.write("<hr>columns : "+columns+"<hr>");
document.write("<hr>colDataTypes : "+colDataTypes+"<hr>");
document.write("<hr>colWidth : "+colWidth+"<hr>");
*/

function getIDColumns()
{
	var hiddenColumnNumbers = new Array();
	var i=0;
	<%
		int cols = 0;
		
		for(col=0;col<columnList.size();col++)
		{
			if (columnList.get(col).toString().trim().equals("ID"))
			{%>
				hiddenColumnNumbers[i] = <%=col%>;
				i++;
			<%}
		}
	%>
	return hiddenColumnNumbers;
}
</script>

<table width="100%">
	<tr>
		<td>
			<div id="gridbox" width="100%" height="350px" style="background-color:white;overflow:hidden"></div>
		</td>
	</tr>
</table>


<script>
	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtml_comp/imgs/");

	if(useFunction == "participant")
	{
//		alert("test");
		colDataTypes=colDataTypes.replace(/ch/,"ra");
		colDataTypes=colDataTypes.replace(/int/,"ro");
		columns=","+columns+",";
		colWidth = colWidth+",100,0";
		mygrid.setOnCheckHandler(onParticipantClick);
		mygrid.setOnRowDblClickedHandler(useFunction);
/*document.write("<hr>myData[0] : "+myData[0]+"<hr>");
document.write("<hr>columns : "+columns+"<hr>");
document.write("<hr>colDataTypes : "+colDataTypes+"<hr>");
document.write("<hr>colWidth : "+colWidth+"<hr>");
*/
	}

	mygrid.setHeader(columns);
	//mygrid.setEditable("FALSE");
	mygrid.enableAutoHeigth(false);

	mygrid.setInitWidths(colWidth);
	mygrid.setColTypes(colDataTypes);
	mygrid.enableMultiselect(true);
//	mygrid.chNoState = "0";

	//mygrid.setColAlign("left,left")
	mygrid.setColSorting(colTypes);
	//mygrid.enableMultiselect(true)
	mygrid.init();

/*
	mygrid.loadXML("dhtmlxgrid/grid.xml");
	
//		clears the dummy data and refreshes the grid.
	mygrid.clearAll();
*/
	for(var row=0;row<myData.length;row++)
	{
		if(useFunction == "shopingcart" )
		{
			data = myData[row];
		}
		else 
		{
			data = "0,"+myData[row];
		}

		mygrid.addRow(row+1,data,row+1);
	}
	for(var row=0;row<myData.length;row++)
	{
		var chkName="";
		if(useFunction == "shopingcart" )
		{
			var data = myData[row];
			var specId = data.split(",");
			chkName = "value1(CHK_" + specId[0] + ")";
		}
		else 
		{
			chkName = "value1(CHK_" + row + ")";
		}

		//var chkName = "value1(CHK_" + row + ")";
		document.write("<input type='hidden' name='"+chkName +"' id='"+row+"' value='1'>");
	}

	if(useFunction == "participant")
	{
		mygrid.setColumnHidden(mygrid.getColumnCount()-1,true);
	}

	// Mandar : 30-Jan-07 :To hide ID columns
	var hideCols = getIDColumns();
	for(x in hideCols)
	{
		mygrid.setHeaderCol(hideCols[x],"");
		mygrid.setColumnHidden(hideCols[x],true);
	}

	//fix for grid display on IE for first time.
	mygrid.setSizes();
</script>
<%
	columnList.remove(0);
%>