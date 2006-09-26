<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
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
tr#hiddenCombo
{
 display:none;
}
</style>
<head>
<%
	
	int pageNum = Integer.parseInt((String)session.getAttribute(Constants.PAGE_NUMBER));
	int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	int numResultsPerPage = Constants.NUMBER_RESULTS_PER_PAGE_SEARCH;
	String pageName = "SpreadsheetView.do";	
	
	AdvanceSearchForm form = (AdvanceSearchForm)session.getAttribute("advanceSearchForm");
	List columnList = (List) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	if(columnList==null)
		columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) session.getAttribute(Constants.PAGINATION_DATA_LIST);
//	if(dataList==null)
//		dataList = (List) request.getAttribute(Constants.PAGINATION_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String title = pageOf + ".searchResultTitle";
	boolean isSpecimenData = false;	
	int IDCount = 0;
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
			
			var columns = ["",<%int k;%><%for (k=0;k < (columnList.size()-1);k++){if (columnList.get(k).toString().trim().equals("ID")){IDCount++;}%>"<%=columnList.get(k)%>",<%}if (columnList.get(k).toString().trim().equals("ID")){IDCount++;}%>"<%=columnList.get(k)%>"];
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
				//document.forms[0].target = "_blank";
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
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
				action="ConfigureAdvanceSearchView.do?pageOf=pageOfQueryResults";
				document.forms[0].action = action;
				document.forms[0].target = "myframe1";
				document.forms[0].submit();
		}
		function onRedefineSimpleQuery()
		{
			action="SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&operation=redefine";
			document.forms[0].action = action;
			document.forms[0].target = "_parent";
			document.forms[0].submit();
		}
		function onRedefineAdvanceQuery()
		{
			action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&operation=redefine";
			document.forms[0].action = action;
			document.forms[0].target = "_parent";
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
			checkUncheck(element);
		
		}
		
		function setDefaultView(element)
		{
			action="DefaultSpecimenView.do?pageOf=pageOfQueryResults&<%=Constants.SPECIMENT_VIEW_ATTRIBUTE%>="+element.checked+"&view=<%=Constants.SPECIMEN%>"+"&isPaging=false";
			document.forms[0].action = action;
			document.forms[0].target = "myframe1";
			document.forms[0].submit();
		}
		function callAction(action)
		{
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	</script>
	<%
		String configAction = new String();
		String redefineQueryAction = new String();
		if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
		{
			configAction = "onSimpleConfigure()";
			redefineQueryAction = "onRedefineSimpleQuery()";
		}
		else
		{
			configAction = "onAdvanceConfigure()";
			redefineQueryAction = "onRedefineAdvanceQuery()";
		}
	%>
	<!-- Mandar : 434 : for tooltip -->
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<tr>
	<td>
		<html:errors /> <!--Prafull:Added errors tag inside the table-->
	</td>
</tr>
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
			<td class="dataPagingSection">					
				<custom:test name="Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" />
				<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>"/>
				<html:hidden property="isPaging" value="true"/>			
			</td>
		</tr>
		<%
		if(pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
		{			
			String []selectedColumns=form.getSelectedColumnNames();
		%>
		
		<tr id="hiddenCombo" rowspan="4" height="1%">
			<td class="formField" colspan="4">
	<!-- Mandar : 434 : for tooltip -->
	   			<html:select property="selectedColumnNames" styleClass="selectedColumnNames"  size="1" styleId="selectedColumnNames" multiple="true"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
	   				<%
					for(int j=0;j<selectedColumns.length;j++)
	   				{
	   				%>
						<html:option value="<%=selectedColumns[j]%>"><%=selectedColumns[j]%></html:option>
					<%
	   				}
	   				%>
	   	 		</html:select>
			</td>
		</tr>
		<% } 
		%>
		
		<tr height="85%">
			<td width="100%">
				<div STYLE="overflow: auto; width:100%; height:100%; padding:0px; margin: 0px; border: 1px solid">
					<script>
						
							//	create ActiveWidgets Grid javascript object.
							var obj = new Active.Controls.Grid;
							
							//	set number of rows/columns.
							obj.setRowProperty("count", <%=dataList.size()%>);
							obj.setColumnProperty("count", <%=(columnList.size()-IDCount) + 1%>);
							
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
					<%
						Object obj = session.getAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE);
						boolean isDefaultView = (obj!=null);
					%>
					<td width="10%" nowrap>
					<%if(pageOf.equals(Constants.PAGEOF_QUERY_RESULTS)){%>
						<input type='checkbox' <%if (isDefaultView){%>checked='checked' <%}%>name='checkDefaultSpecimenView' id='checkDefaultSpecimenView' onClick='setDefaultView(this)'>
						<span class="formLabelNoBackGround"><bean:message key="buttons.defaultSpecimenView" /></span>
					<%}else{%>
						&nbsp;
					<%}%>
					</td>
					<td width="70%">
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
					<td>
						<html:button styleClass="actionButton" property="redefineButton" onclick="<%=redefineQueryAction%>">
							<bean:message  key="buttons.redefineQuery" />
						</html:button>
					</td>
					<td>
					
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