<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.common.query.ShoppingCart"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="java.util.ArrayList"%>


<script src="jss/script.js"></script>

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
	String [] columnList1 = Constants.SHOPPING_CART_COLUMNS;
	List columnList = new ArrayList();
	for(int i=0;i<columnList1.length;i++)
	{
		columnList.add(columnList1[i]);
	}
	//columnList.add(0," ");
    String checkAllPages = (String)session.getAttribute("checkAllPages");
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
%>

	<script language="javascript">
	var colZeroDir='ascending';
		function onDelete()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var flag = confirm("Are you sure you want to delete the selected item(s)?");
				if(flag)
				{
					var action = "ShoppingCart.do?operation=delete";
					document.forms[0].operation.value="delete";
					document.forms[0].action = action;
					document.forms[0].target = "_parent";
					document.forms[0].submit();
				}
			}
		}
		
		function onExport()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var action = "ShoppingCart.do?operation=export";
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
		
		function addToOrderList()
		{			
		    var isChecked = updateHiddenFields();
		    if(isChecked == "true")
		    {
				var action = "ShoppingCart.do?operation=addToOrderList";
				document.forms[0].operation.value="addToOrderList";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
			}
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
	mygrid.setEditable(true);
	var state=element.checked;
	rowCount = mygrid.getRowsNum();
	for(i=1;i<=rowCount;i++)
	{
		var cl = mygrid.cells(i,0);
		if(cl.isCheckbox())
		cl.setChecked(state);
	}
}
	</script>
</head>
<%@ include file="/pages/content/common/ActionErrors.jsp" %>  
<html:form action="<%=Constants.SHOPPING_CART_OPERATION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">


<%
	if(dataList.size() != 0)
		{
%>
	<tr height="5%">
		 <td class="formTitle" width="100%">
			<bean:message key="shoppingCart.title"/>
		 </td>
	</tr>
	
	<tr height="5%">
		<td width="100%">
			<table cellpadding="5" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="10%" nowrap>
					<input type='checkbox' name='checkAll1' id='checkAll1' onClick='checkAll(this)'>
					<bean:message key="buttons.checkAll" />
				</td>
				<td width="80%">
					&nbsp;
				</td>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
						<bean:message key="buttons.delete"/>
					</html:button>
				</td>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td> 

				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton"  property="orderButton" onclick="addToOrderList()">
						<bean:message key="buttons.addtolist"/>	
					</html:button>
				</td>
			

			</tr>
			</table>
		</td>
	</tr>
	
	<tr height="85%">
		<td width="100%">
<!--  **************  Code for New Grid  *********************** -->
			<script>
					function shopingcart(id)
					{
						//do nothing
					} 				

					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "shopingcart";	
			</script>
			<%@ include file="/pages/content/search/AdvanceGrid.jsp" %>
<!--  **************  Code for New Grid  *********************** -->
		</td>
	</tr>

	<tr height="5%">
		<td width="100%">
			<table cellpadding="5" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="10%" nowrap>
					<input type='checkbox' name='checkAll2' id='checkAll2' onClick='checkAll(this)'>
					<bean:message key="buttons.checkAll" />
				</td>
				<td width="80%">
					&nbsp;
				</td>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
						<bean:message key="buttons.delete"/>
					</html:button>
				</td>
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td> 
				<td width="5%" nowrap align="right">
					<html:button styleClass="actionButton"  property="orderButton" onclick="addToOrderList()" >
					<bean:message key="buttons.addtolist"/>	
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
