<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AdvanceSearchForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.Utility"%>
<%@ page import="edu.wustl.common.util.Utility"%>


<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/lib/gridcheckbox.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="jss/script.js"></script>

<style>
.active-column-0 {width:30px}
</style>


<%
	AdvanceSearchForm form = (AdvanceSearchForm)request.getAttribute("advanceSearchForm");
%>
<head>
<%
	List columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	String title = pageOf + ".searchResultTitle";

	boolean isSpecimenData = false;

	/*for(int i=0;i<columnList.size();i++)
	{
		String columnName = (String)columnList.get(i);
		if(columnName.equalsIgnoreCase("Specimen Id"))
		{
			isSpecimenData = true;
			break;
		}
	}*/

if(dataList != null && dataList.size() != 0)
{
%>
	
	<script>
		var myData = [<%int xx;%><%for (xx=0;xx<(dataList.size()-1);xx++){%>
	<%
		List row = (List)dataList.get(xx);
  		int j;
  		//Bug 700: changed the variable name for the map values as it was same in both AdvanceSearchForm and SimpleQueryInterfaceForm
		String chkName = "value1(CHK_" + xx + ")";
	%>
		["<INPUT TYPE='CHECKBOX' NAME='<%=chkName%>' id='<%=xx%>' onClick='changeData(this)'>",<%for (j=0;j < (row.size()-1);j++){%>"<%=Utility.toGridFormat(row.get(j))%>",<%}%>"<%=Utility.toGridFormat(row.get(j))%>","1"],<%}%>
	<%
		List row = (List)dataList.get(xx);
  		int j;
  		//Bug 700: changed the variable name for the map values as it was same in both AdvanceSearchForm and SimpleQueryInterfaceForm
		String chkName = "value1(CHK_" + xx + ")";
	%>
		["<INPUT TYPE='CHECKBOX' NAME='<%=chkName%>' id='<%=xx%>' onClick='changeData(this)'>",<%for (j=0;j < (row.size()-1);j++){%>"<%=Utility.toGridFormat(row.get(j))%>",<%}%>"<%=Utility.toGridFormat(row.get(j))%>","1"]
		];
		
		var columns = ["",<%int k;%><%for (k=0;k < (columnList.size()-1);k++){%>"<%=columnList.get(k)%>",<%}%>"<%=columnList.get(k)%>"];
	</script>

<% } %>

	<script language="javascript">
	var colZeroDir='ascending';
		function onAddToCart()
		{
			var isChecked = "false";
			for (var i=0;i < document.forms[0].elements.length;i++)
		    {
		    	var e = document.forms[0].elements[i];
		    	
		        if (e.name != "checkAll" && e.type == "checkbox" && e.checked == true)
		        {
		        	isChecked = "true";
		        	break;
		        }
		    }
		    
		    if(isChecked == "true")
		    {
				var action = "ShoppingCart.do?operation=add";
				document.forms[0].operation.value="add";
				document.forms[0].action = action;
				document.forms[0].target = "myframe1";
				document.forms[0].submit();
			}
		}
		
		function onExport()
		{
			var isChecked = "false";
			for (var i=0;i < document.forms[0].elements.length;i++)
		    {
		    	var e = document.forms[0].elements[i];
		    	
		        if (e.name != "checkAll" && e.type == "checkbox" && e.checked == true)
		        {
		        	isChecked = "true";
		        	break;
		        }
		    }
		    
		    if(isChecked == "true")
		    {
				var action = "SpreadsheetExport.do";
				document.forms[0].operation.value="export";
				document.forms[0].action = action;
				document.forms[0].target = "_blank";
				document.forms[0].submit();
			}
		}
		//function that is called on click of Define View button for the configuration of search results
		function onSimpleConfigure()
		{
				action="ConfigureSimpleQuery.do?pageOf=pageOfSimpleQueryInterface";
				document.forms[0].action = action;
				document.forms[0].target = "_parent";
				document.forms[0].submit();
		}

		function onAdvanceConfigure()
		{
				action="ConfigureSimpleQuery.do?pageOf=pageOfQueryResults";
				document.forms[0].action = action;
				document.forms[0].target = "myframe1";
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
		
		
		function checkAll(element)
		{
			if(element.name == "checkAll1")
			{
				var check2 = document.getElementById("checkAll2");
				check2.checked = element.checked;
				checkUncheck(element);
			}
			else
			{
				var check1 = document.getElementById("checkAll1");
				check1.checked = element.checked;
				checkUncheck(element);
			}
		}
		function callAction(action)
		{
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	</script>
<%
	String configAction = new String();
	if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
		configAction = "onSimpleConfigure()";
	else
		configAction = "onAdvanceConfigure()";
%>

</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<html:form action="<%=Constants.SPREADSHEET_EXPORT_ACTION%>">

<%
		if(dataList == null && pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
		{
%>
			<bean:message key="advanceQuery.noRecordsFound"/>
<%
		}
		else if(dataList != null && dataList.size() != 0)
		{
%>
	<tr height="5%">
		 <td class="formTitle" width="100%">
			<bean:message key="<%=title%>"/>
		 </td>
	</tr>
	
	<tr height="5%">
		<td width="100%">
			<table cellpadding="5" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="10%" nowrap>
					<input type='checkbox' name='checkAll1' id='checkAll1' onClick='checkAll(this)'>
					<span class="formLabelNoBackGround"><bean:message key="buttons.checkAll" /></span>
				</td>
				<td width="80%">
					&nbsp;
				</td>
				
				<%if(pageOf.equals(Constants.PAGEOF_QUERY_RESULTS)){%>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="addToCart" onclick="onAddToCart()">
						<bean:message key="buttons.addToCart"/>
					</html:button>&nbsp;
				</td>
				<%}else{%>
				<td width="5%" nowrap align="right">
					&nbsp;
				</td>
				<%}%>
				
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td> 
				
				<td>
					<html:button styleClass="actionButton" property="configureButton" onclick="<%=configAction%>">
					<bean:message  key="buttons.configure" />
					</html:button>
				</td>
			</tr>
			</table>
		</td>
	</tr>
	
	<tr height="85%">
		<td width="100%">
			<div STYLE="overflow: auto; width:100%; height:100%; padding:0px; margin: 0px; border: 1px solid">
				<script>
					
						//	create ActiveWidgets Grid javascript object.
						var obj = new Active.Controls.Grid;
						
						//	set number of rows/columns.
						obj.setRowProperty("count", <%=dataList.size()%>);
						obj.setColumnProperty("count", <%=columnList.size() + 1%>);
						
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
					        	index=myData[0].length-1;
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
		<td width="100%">
			<table cellpadding="5" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="10%" nowrap>
					<input type='checkbox' name='checkAll2' id='checkAll2' onClick='checkAll(this)'>
					<span class="formLabelNoBackGround"><bean:message key="buttons.checkAll" /></span>
				</td>
				<td width="80%">
					&nbsp;
				</td>
				
				<%if(pageOf.equals(Constants.PAGEOF_QUERY_RESULTS)){%>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="addToCart" onclick="onAddToCart()">
						<bean:message key="buttons.addToCart"/>
					</html:button>&nbsp;
				</td>
				<%}else{%>
				<td width="5%" nowrap align="right">
					&nbsp;
				</td>
				<%}%>
				
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td>
				<td>
					<html:button styleClass="actionButton" property="configureButton" onclick="<%=configAction%>">
						<bean:message  key="buttons.configure" />
					</html:button>
				</td>
				
				
			</tr>
			</table>
		</td>
	</tr>
<% } %>

	<tr>
		<td><html:hidden property="operation" value=""/></td>
	</tr>
</html:form>
</table>