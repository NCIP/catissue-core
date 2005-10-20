<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.query.ShoppingCart"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/lib/gridcheckbox.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<style>
.active-column-0 {width:30px}
.active-column-4 {width:150px}
.active-column-6 {width:150px}
</style>


<%
	ShoppingCartForm form = (ShoppingCartForm)request.getAttribute("shoppingCartForm");
%>
<head>
<%
	String [] columnList = Constants.SHOPPING_CART_COLUMNS;
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

if(dataList.size() != 0)
{
%>

	<script>
		var myData = [<%int xx;%><%for (xx=0;xx<(dataList.size()-1);xx++){%>
	<%
		List row = (List)dataList.get(xx);
  		int j;
	%>
		[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"],<%}%>
	<%
		List row = (List)dataList.get(xx);
  		int j;
	%>
		[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"]
		];
		
		var columns = [<%int k;%><%for (k=0;k < (columnList.length-1);k++){%>"<%=columnList[k]%>",<%}%>"<%=columnList[k]%>"];
	</script>

<% } %>

	<script language="javascript" src="jss/script.js"></script>
	<script language="javascript">
	var colZeroDir='ascending';
		function onDelete()
		{
			var isChecked = "false";
			for (var i=0;i < document.forms[0].elements.length;i++)
		    {
		    	var e = document.forms[0].elements[i];
		    	
		        if (e.type == "checkbox" && e.checked == true)
		        {
		        	isChecked = "true";
		        	break;
		        }
		    }
		    
		    if(isChecked == "true")
		    {
				var flag = confirm("Are you sure you want to delete the selected item(s)?");
				if(flag)
				{
					var action = "/catissuecore/ShoppingCart.do?operation=delete";
					document.forms[0].operation.value="delete";
					document.forms[0].action = action;
					document.forms[0].target = "_parent";
					document.forms[0].submit();
				}
			}
		}
		
		function onExport()
		{
			var action = "/catissuecore/ShoppingCart.do?operation=export";
			document.forms[0].operation.value="export";
			document.forms[0].action = action;
			document.forms[0].target = "_blank";
			document.forms[0].submit();
		}
		
		var selected;

		function addCheckBoxValuesToArray(checkBoxName)
		{
			var theForm = document.forms[0];
		    selected=new Array();
		
		    for(var i=0,j=0;i<theForm.elements.length;i++)
		    {
		 	  	if(theForm[i].type == 'checkbox' && theForm[i].checked==true)
			        selected[j++]=theForm[i].value;
			}
		}		
	</script>
</head>

<html:form action="<%=Constants.SHOPPING_CART_OPERATION%>">		
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<%
		if(dataList.size() != 0)
		{
%>
	<tr height="95%">
		<td width="100%">
			<div STYLE="overflow: auto; width:100%; height:100%; padding:0px; margin: 0px; border: 1px solid">
				<script>
					
						//	create ActiveWidgets Grid javascript object.
						var obj = new Active.Controls.Grid;
						
						//	set number of rows/columns.
						obj.setRowProperty("count", <%=dataList.size()%>);
						obj.setColumnProperty("count", <%=columnList.length%>);
						
						//	provide cells and headers text
						obj.setDataProperty("text", function(i, j){return myData[i][j]});
						obj.setColumnProperty("text", function(i){return columns[i]});
						
						//	set headers width/height.
						obj.setRowHeaderWidth("28px");
						obj.setColumnHeaderHeight("20px");
						
						//original sort method  
						var _sort = obj.sort; 
						//overide sort function to meet our requirenemnt
					    obj.sort = function(index, direction, alternateIndex){ 
					        
					    //if check box column is clicked
					    //then sort on the flag those are in 8th column
					        if(index==0)
					        {
					        	index=7;
					        	direction=colZeroDir;
								if(colZeroDir=='ascending')colZeroDir='descending';
								else colZeroDir='ascending';
					        	
					        } 
					        
				            _sort.call(this, index, direction); 
					        
					        return true;
					    }
						//	write grid html to the page.
						document.write(obj);
				</script>
			</div>
		</td>
	</tr>

	<tr height="5%">
		<td width="100%" align="right">
			<table cellpadding="5" cellspacing="0" border="0">
			<tr>
				<td>
					<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
						<bean:message key="buttons.delete"/>
					</html:button>
				</td>
				<td>
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td> 
			</tr>
			</table>
		</td>
	</tr>
<% } else  {%>
	<tr height="10%">
		<td><b>The Shopping Cart is empty.</b></td>
	</tr>
<% } %>

	<tr>
		<td><html:hidden property="operation" value=""/></td>
	</tr>
</table>
</html:form>