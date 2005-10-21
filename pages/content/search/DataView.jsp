<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants"%>

<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>

<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	String title = pageOf + ".searchResultTitle";
	/*if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
	{
		String[] tableSet = (String[]) request.getAttribute(Constants.TABLE_ALIAS_NAME);
	}*/
%>

<script>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"],<%}%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"]
];

var columns = [<%int k;%><%for (k=0;k < (columnList.length-1);k++){%>"<%=columnList[k]%>",<%}%>"<%=columnList[k]%>"];

</script>
<style>
	tr#hiddenCombo
	{
	 display:none;
	}
	
</style>
<html:form action="<%=Constants.CONFIGURE_DISTRIBUTION_ACTION%>">
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	<tr>
		 <td class="formTitle">
			<bean:message key="<%=title%>"/>
		 </td>
	</tr>
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
			
			<%if (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE.equals(pageOf) == false){%>
			var row = new Active.Templates.Row;
			row.setEvent("ondblclick", function(){this.action("myAction")}); 
			
			obj.setTemplate("row", row);
	   		obj.setAction("myAction", 
				function(src){window.location.href = 'SearchObject.do?pageOf=<%=pageOf%>&operation=search&systemIdentifier='+myData[this.getSelectionProperty("index")][0]}); 
			<%}%>
			
			//	write grid html to the page.
			document.write(obj);
	</script>
</div>
</td>
</tr>

<%
if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
{
%>
<tr height="5%">
		<td width="100%" align="right">
			<table cellpadding="5" cellspacing="0" border="0">
			<tr>
					<td>
						<html:button styleClass="actionButton" property="expButton"> 
							<bean:message  key="buttons.export" />
						</html:button>
					
					</td>
					<td>
						<html:button styleClass="actionButton" property="configButton">
							<bean:message  key="buttons.configure" />
						</html:button>
					</td>
			</tr>
			</table>
		</td>
	</tr>
<% } %>
</table>
</html:form>