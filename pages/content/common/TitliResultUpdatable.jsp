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
			this.grid.setHeader("ENTITY,Matches"); 
			this.grid.setInitWidths("300,100"); 
			this.grid.setColAlign("left,left"); 
			this.grid.setColTypes("ro,ro"); 
			this.grid.setColSorting("str,str") ;
			this.grid.setEditable(false);
			
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

<body>
<form name="editForm" id = "editForm" action="TitliFetch.do">
	<table>
	
	
		<tr>
			
			<td>
			<span class = "message">
				<font size="3"><b>
				<c:out value="Search String : "/> <bold><c:out value="${titliSearchForm.searchString}" /></bold></br>
				<c:out value="Found ${titliSearchForm.numberOfMatches} matches in ${titliSearchForm.timeTaken} seconds" />
				</b></font>
				</span>
								
				<input type = "hidden" id = "selectedLabel" name = "selectedLabel" />
								
			</td>
			
		</tr>
		
		<tr>
			<td>
				<div id="gridbox" style="width:400;height:400"></div>				
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

</html>
