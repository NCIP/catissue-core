<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css"
	href="dhtml_comp/css/dhtmlXGrid.css" />

<script src="dhtml_comp/js/dhtmlXCommon.js"></script> <script src="dhtml_comp/js/dhtmlXGrid.js"></script> <script src="dhtml_comp/js/dhtmlXGridCell.js"></script> <script src="dhtml_comp/js/dhtmlXGrid_mcol.js"></script> <script> var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%> <% List row = (List)dataList.get(i); int j; %> <%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=AppUtility.toNewGridFormat(row.get(j))%>,<%}%><%=AppUtility.toNewGridFormat(row.get(j))%><%="\""%>,<%}%> <% List row = (List)dataList.get(i); int j; %> <%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=AppUtility.toNewGridFormat(row.get(j))%>,<%}%><%=AppUtility.toNewGridFormat(row.get(j))%><%="\""%> ];




	var columns = <%="\""%><%int col;%><%for(col=0;col<(columnList.size()-1);col++){%><%=columnList.get(col)%>,<%}%><%=columnList.get(col)%><%="\""%>;

	var colWidth = <%="\""%><%for(col=0;col<(columnList.size()-1);col++){%><%=100%>,<%}%><%=100%><%="\""%>;

	var colTypes = <%="\""%><%=Variables.prepareColTypes(dataList,true)%><%="\""%>;

	var colDataTypes = colTypes;

	while(colDataTypes.indexOf("str") !=-1)
	{
		colDataTypes=colDataTypes.replace(/str/,"ro");
	}

</script>

<%
	/**
	 * Name: Chetan Patil
	 * Reviewer: Sachin Lale
	 * Bug ID: SpecimenEventSpaceUtilization
	 * Patch ID: SpecimenEventSpaceUtilization_1
	 * See also: SpecimenEventSpaceUtilization_2
	 * Description: The height of each row and column of grid is 20 pixel. Hence to make the height of grid dynamic, the (number of rows + 2) is multiplied
	 				by (number of pixels for height).
	 */
	int noOfRows = dataList.size();
	int heightOfGrid = (noOfRows + 2) * 20;

	// If the height of grid is more than the height required to display the grid with 12 rows then
	// make the heightOfGrid constant so that only 11 rows including the header row will be displayed.
	if(heightOfGrid > 240)
	{
		heightOfGrid = 230; // 230px is the exact height to display 11 rows, including the header.
	}
%>

<table width="100%" cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td><script>
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
					document.write("<div id='gridbox' width='100%' height='300px' style='background-color:#d7d7d7;overflow:hidden'></div>");
				}
			</script></td>
	</tr>
</table>

<script>

		function onParticipantClick(participant_id)
		{
			var cl = mygrid.cells(participant_id,1);
			var pid = cl.getValue();
			document.forms[0].participantId.value=pid;	document.forms[0].action="ProcessMatchedParticipants.do?pageOf=pageOfMatchedParticipant&isDelete=yes&participantId="+pid+"&identifierFieldIndex=0";
		}

		function deleteProcesedParticipant(){
			document.forms[0].isDelete.value="yes";
			document.forms[0].submit();
		}
	

		// function modified to display distribution array.
		// problem in query data.
		function rowClick(id)
		{
			//alert(id);
			var colid = <%=identifierFieldIndex.intValue()%>;
			colid=1;
			var cl = mygrid.cells(id,colid);
			var searchId = cl.getValue();
			//alert(colid)
			//alert(searchId);
			var isHashedOut = "false";
			for(var col=0;col < <%=columnList.size()%> && isHashedOut=="false";col++)
			{
			   var c2 = mygrid.cells(id,col);
			   var colData = c2.getValue();
			   if(colData=="##")
			   {
				   isHashedOut="true";
			   }
			}
			if(isHashedOut=="false")
			{
				var url = "SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+searchId;
				if(<%=pageOf.equals("pageOfCollectionProtocol")%>)
				{
					url = "RetrieveCollectionProtocol.do?&id="+searchId;
				}
				window.location.href = url;
			}
			else
			{
				var url = "AccessDenied.do";
				window.location.href = url;
			}

		}
	/**
	 * Name : Vijay_Pande
	 * Bug ID: Sachin_Lale
	 * Patch ID: 4060_1
	 * See also: --
	 * Description: Grid was appearindg disordered since it was not refreshed/resized.
	 */
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

		mygrid.entBox.style.width="603px";
		colDataTypes=colDataTypes.replace(/ch/,"ra");
		colDataTypes=colDataTypes.replace(/int/,"ro");
		columns=","+columns+",";
		//alert(columns)
		colWidth = colWidth+",100,0";
		mygrid.setOnCheckHandler(onParticipantClick);

		mygrid.setHeader(columns);
		mygrid.enableAutoHeigth(false);
		mygrid.setColTypes(colDataTypes);
		mygrid.enableAlterCss("even","uneven");
		mygrid.setSkin("light");
		mygrid.enableRowsHover(true,'grid_hover');
	    mygrid.setEditable(true);




		//document.write("<hr>"+colWidth+"<hr>");
		if(useFunction == "eventParametersGrid" || useFunction == "derivedSpecimenGrid")
		{
		  	colWidth = "130,130,130,130,0";
		}

		if(useFunction == "goToConflictDetails")
		{	
			colWidth = "130,130,200,130,130,130,200";
		}

		if(navigator.userAgent.toString().toLowerCase().indexOf("firefox")!= -1)
		{ 
		   <% if(columnList.size()<=10)
			  { %>
				//var colWidthP = "<%=edu.wustl.query.util.global.Utility.getColumnWidthP(columnList)%>";
				var colWidthP = "16.6,16.6,16.6,16.6,16.6,16.6,16.6";
				mygrid.setInitWidthsP(colWidthP);
				//mygrid.entBox.style.width="100%";
			 <%}
			  else
			  { %>
			 		mygrid.setInitWidths(colWidth);
				//mygrid.entBox.style.width=gridWidth;
			<%}%>
		}
	  else
	  {

		 mygrid.setInitWidths(colWidth);
		 //mygrid.entBox.style.width=gridWidth;
	  }

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

		if(useFunction == "eventParametersGrid" || useFunction == "derivedSpecimenGrid" )
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


		mygrid.setColumnHidden(mygrid.getColumnCount()-1,true);


		// :To hide ID columns
			//var hideCols = getIDColumns();

			//for(i=0;i<hideCols.length;i++)
			//{

				//mygrid.setHeaderCol(hideCols[i],"");
				//mygrid.setColumnHidden(hideCols[i],true);
		//	}

		mygrid.setHeaderCol(1,"");
		mygrid.setColumnHidden(1,true);
		mygrid.setSizes();
}

window.onload=init_grid;
</script>