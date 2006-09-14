<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.Utility"%>


<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>

<%
	List columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.PAGINATION_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	Integer identifierFieldIndex = (Integer)request.getAttribute(Constants.IDENTIFIER_FIELD_INDEX);
	String title = pageOf + ".searchResultTitle";
	int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	int totalResults = Integer.parseInt((String)request.getAttribute(Constants.TOTAL_RESULTS));
	int numResultsPerPage = Integer.parseInt((String)request.getAttribute(Constants.RESULTS_PER_PAGE));
	String pageName = "SpreadsheetView.do";		
%>

<script>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=Utility.toGridFormat(row.get(j))%>",<%}%>"<%=Utility.toGridFormat(row.get(j))%>"],<%}%>
<%
	List row = (List)dataList.get(i);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=Utility.toGridFormat(row.get(j))%>",<%}%>"<%=Utility.toGridFormat(row.get(j))%>"]
];

var columns = [<%int k;%><%for (k=0;k < (columnList.size()-1);k++){%>"<%=columnList.get(k)%>",<%}%>"<%=columnList.get(k)%>"];

</script>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
	<tr>
		 <td class="formTitle">
			<bean:message key="<%=title%>"/>
		 </td>
	</tr>	
</table>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="6%">
	<tr>
		<td class="dataPagingSection">
			<html:form action="/SpreadsheetExport">				
				<custom:test name="Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" />
				<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>"/>
				<html:hidden property="isPaging" value="true"/>
				<html:hidden property="identifierFieldIndex" value="<%=identifierFieldIndex.toString()%>"/>
			</html:form>
		</td>
	</tr>
</table>
<div STYLE="overflow:auto; width:100%; height:90%; padding:0px; margin: 0px; border: 1px solid">

	<script>
		
			//	create ActiveWidgets Grid javascript object.
			var obj = new Active.Controls.Grid;
			
			//	set number of rows/columns.
			obj.setRowProperty("count", <%=dataList.size()%>);
			obj.setColumnProperty("count", <%=columnList.size()%>);
						
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
				function(src){window.location.href = 'SearchObject.do?pageOf=<%=pageOf%>&operation=search&id='+myData[this.getSelectionProperty("index")][<%=identifierFieldIndex.intValue()%>]}); 				
				
			<%}%>
			
			//	write grid html to the page.
			document.write(obj);			
			
			
	</script>		
</div>


