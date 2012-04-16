<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<script language="JavaScript"  type="text/javascript" src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlx_suite/ext/dhtmlxgrid_srnd.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
    <link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
	<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<script type="text/javascript" src="dhtmlx_suite/gridexcells/dhtmlxgrid_excell_combo.js"></script>
	

<%

%>


<script>
	var columns =${requestScope.jsonData};
	var statusList ="<%=statusValue%>";
	var temp = statusList.split(",");
	function updateGrid(id)
	{
	//alert(mygrid.cellById(1,5).isEdited());
	//alert(document.getElementById('select_1'));
	//alert(document.getElementById('select_1').value);
	var combo = mygrid.getCombo(5);
	//alert(combo.getText());
	//alert(mygrid.getRowsNum());
	document.getElementById("nextStatusId").value=id.value;
	for(var row=0;row<mygrid.getRowsNum();row++)
	{
	var canDistribute = "value(RequestDetailsBean:"+row+"_canDistribute)";
	var assignStat = document.getElementById('select_'+row);
		if(id.value == ("Distributed And Close(Special)"))
		{
			var avlQty = mygrid.cellById(row+1,3).getValue();
			var reqQty = mygrid.cellById(row+1,4).getValue();
			//alert('avlQty '+avlQty+'     reqQty  '+reqQty);
			if(avlQty == reqQty)
			{
				mygrid.cellById(row+1,5).setValue("Distributed And Close");
				assignStat.value="Distributed And Close";
			}
			else
			{
				mygrid.cellById(row+1,5).setValue("Distributed");
				assignStat.value="Distributed";
			}
		}
		else
		{
			mygrid.cellById(row+1,5).setValue(id.value);
			assignStat.value=id.value;
		}
	document.getElementById(canDistribute).value="true";
		//mygrid.cellById(row,5).setValue(id.value);
		//alert(mygrid.cellById(row,5).getAttribute('status'));
		//combo.put(temp[row],temp[row]);
	}
	<%=checkQuantityforAll%>
	//var combo = mygrid.getCombo(5);
	//combo.put(id.value,id.value,1);
	//combo.save();
	//combo.restore();
	}
</script>
<table width="100%" cellpadding="3" cellspacing="0" border="0"	>
	<tr>
		<td align="right">
			<html:select property="status" name="requestDetailsForm" styleClass="formFieldSized11" styleId="nextStatusId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="updateGrid(this)">
											<html:options collection="<%= Constants.REQUESTED_ITEMS_STATUS_LIST %>" labelProperty="name" property="value" />		
									     </html:select>
		</td>
	</tr>
	<tr>
		<td>
			<script>	
					document.write("<div id='gridbox' width='100%' height='250px' style='background-color:#d7d7d7;overflow:hidden'></div>");
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
	mygrid.setHeader("Specimen Label,Specimen Tpe,Specimen Class,Available Qty,Requested Qty,Status");
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,,,,"); 
	mygrid.setEditable("true");
	mygrid.enableAutoHeigth(false);
	//mygrid.enablePaging(true, 15, 5, csPagingArea, true, csInfoArea);
    //mygrid.setPagingSkin("bricks");

    mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,10,10,10,10,*");
	
	mygrid.setColTypes("ro,ro,ro,ro,ro,coro");
	mygrid.setSkin("light");
	
	mygrid.setColSorting("str,str,str,str,str,str");
	mygrid.enableMultiselect(true);
	
	//mygrid.load(columns, "json");
	
	
	mygrid.init();
	
	mygrid.load("RequestDetails.do?splvar=gridData","", "json");
	mygrid.attachEvent("onCellChanged", function(rId,cInd,nValue){etcr(rId,cInd,nValue)});
	//mygrid.attachEvent("onRowDblClicked", function(rId,cInd){rowClick(rId,cInd)});
 //mygrid.addRow(1,"aa,s,s,s,s",1);
mygrid.enableEditEvents(true);
//mygrid.setHeaderCol(6,"");
	//mygrid.setColumnHidden(6,true);
	//combo.put("aaaa","aaaa");
	
	//alert(mygrid.getAllRowIds());

	//mygrid.cellById(4,5).setValue("New");

//var cel = mygrid.cells(1,4);
//cel.setValue("rrrr");
//cel.setValue("rrrr");
//alert(mygrid.getIndexByValue("Distributed"));
 /*mygrid.forEachRow(
      function callout(row_id) {alert('hh');
         initializeActionCombo(mygrid, row_id);
      }
	  );*/
//alert('fff');
	//mygrid.setSizes();
	setComboValues();
}

window.onload=init_grid;

function etcr(rId,cInd,nValue)
{
rId = rId-1;
	var canDistribute = "value(RequestDetailsBean:"+rId+"_canDistribute)";
	var assignStat = document.getElementById('select_'+rId);
	assignStat.value=nValue;
	document.getElementById(canDistribute).value="true";
		//mygrid.cellById(row,5).setValue(id.value);
		//alert(mygrid.cellById(row,5).getAttribute('status'));
		//combo.put(temp[row],temp[row]);
	
	checkQuantity(rId);
}
function setComboValues()
{
var combo = mygrid.getCombo(5);
 for(var row=0;row<temp.length;row++)
	{
		combo.put(temp[row],temp[row]);
	}
}
</script>