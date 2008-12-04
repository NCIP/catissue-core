<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>

<%@ page import="edu.wustl.common.util.TitliResultGroup" %>
<%@ page import="titli.controller.interfaces.SortedResultMapInterface" %>
<%@ page import="edu.wustl.common.actionForm.TitliSearchForm" %>
<%@ page import="edu.wustl.common.util.TitliTableMapper" %>


<html>

<head>
	<style>
	.message
	{ /* for the non-required form labels */
  font-family:arial,helvetica,verdana,sans-serif;
  font-size:0.7em;
  color:#000000; /* constant: black */
  text-align:left;
}
	</style>
	<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/dhtml_comp/css/dhtmlXGrid.css"> 
	
	<script src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXCommon.js"></script>
	<script src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGrid.js"></script> 
	<script src="<%=request.getContextPath()%>/dhtml_comp/js/dhtmlXGridCell.js"></script> 
	
	<% TitliTableMapper mapper = TitliTableMapper.getInstance(); %>
	
	<script language="JavaScript" type="text/javascript">
	
		function MyGrid()
		{
			this.size=0;
		}
		
		MyGrid.prototype.init=function()
		{	
					
			this.grid = new dhtmlXGridObject("gridbox");
			 
			// or 
			//mygrid = new dhtmlXGridObject();
			//mygrid.attachToObject(document.body);
			 
			this.grid.setImagePath("dhtml_comp/imgs/"); 
			this.grid.setHeader("Entity,Matches"); 
			this.grid.setInitWidths("300,100"); 
			this.grid.setColAlign("left,left"); 
			this.grid.setColTypes("ro,ro"); 
			this.grid.setColSorting("str,str") ;
			this.grid.setEditable(false);
			
			this.grid.enableAutoHeigth(false);
			this.grid.setEditable("FALSE");
			this.grid.enableAutoHeigth(false);
			this.grid.setSkin("light");
			this.grid.enableAlterCss("even","uneven");
			this.grid.enableRowsHover(true,'grid_hover')

			//mygrid.enableDragAndDrop(true);
			//mygrid.setDropHandler(dropFn);
			
			this.grid.setOnRowDblClickedHandler(selectLabel);
			
			//mygrid.setOnRowSelectHandler(controlSelected);
			
			this.grid.init();
		
		}
		
		MyGrid.prototype.addRow=function(row)
		{
			this.grid.addRow(this.size,row,this.size);
			this.size++;
		}
		
		function selectLabel(rowId, columnIndex)
		{
			var label = this.cells(rowId,0).getValue();
			
			document.getElementById('selectedLabel').value = label;
			editForm.submit();
		
		}
		
	</script>
	
</head>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head">
					<span class="wh_ar_b">
						Titli Search Results
					</span>
				</td>
		        <td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Search Results" width="31" height="24" hspace="0" vspace="0" />
				</td>
		      </tr>
		    </table>
	<tr>
	  <td class="tablepadding">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	</tr>
	</table>

<body>
<form name="editForm" id = "editForm" action="TitliFetch.do">
	
	<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg"> 
      <tr>
        <td align="left" class="toptd"></td>
      </tr>
		<tr>
			<!--<td>
			<span class = "message">-->
				<td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;
				Search String : <bold><c:out value="'${titliSearchForm.searchString}'" /></bold>&nbsp;
				<c:out value="${titliSearchForm.displayStats}" />
				</span>					
				<input type = "hidden" id = "selectedLabel" name = "selectedLabel" />					
			</td>	
		</tr>
		
		<tr>
			<td>
				<div id='gridbox' width='100%' height='340px' style='background-color:#d7d7d7;overflow:hidden'></div>						
				<script language="JavaScript" type="text/javascript">  
				 	//grid1 is still acccessible in other script tags !
				 	var grid1 = new MyGrid();
					grid1.init();
						
					<c:forEach items="${titliSearchForm.titliResultMap}" var="groupEntry" >		
						grid1.addRow("<c:out value="${groupEntry.value.label}" /> ,<c:out value="${groupEntry.value.nativeGroup.numberOfMatches}" />");
					</c:forEach>	
				
		 		</script>
			</td>	
		</tr>
	</table>

</form>
</body>
</td>
 </tr>
</table>
</html>
