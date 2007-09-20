<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.querysuite.QueryShoppingCart"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>

<script src="jss/script.js"></script>

<style>
.active-column-0 {width:30px}
.active-column-4 {width:150px}
.active-column-6 {width:150px}
</style>
<%
	String checkAllPages = (String)session.getAttribute("checkAllPages");
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String isSpecimenIdPresent = (String)request.getAttribute(Constants.IS_SPECIMENID_PRESENT);
	if(isSpecimenIdPresent==null)
	 isSpecimenIdPresent = "";
	
    QueryShoppingCart cart = (QueryShoppingCart)session.getAttribute(Constants.QUERY_SHOPPING_CART);
	List columnList = new ArrayList();
	List dataList = new ArrayList() ;
    
	if(cart!=null)
	{
	  columnList = cart.getColumnList();
	  if(columnList!=null)
	   columnList.add(0,"");
      dataList = cart.getCart();
	}
%>
<head>
<script language="javascript">


function setCheckBoxState()
		{
			var chkBox = document.getElementById('checkAll1');
			chkBox.checked = true;
				rowCount = mygrid.getRowsNum();
				for(i=1;i<=rowCount;i++)
				{
					var cl = mygrid.cells(i,0);
					if(cl.isCheckbox())
					cl.setChecked(true);
				}
		
		}

function onDelete()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var flag = confirm("Are you sure you want to delete the selected item(s)?");
				if(flag)
				{
					var action = "QueryAddToCart.do?operation=delete";
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
				var action = "QueryAddToCart.do?operation=export";
				document.forms[0].action = action;
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
				var action = "QueryAddToCart.do?operation=addToOrderList";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
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
<body onload="setCheckBoxState()">
<html:html>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>

<html:form action="QueryAddToCart.do">

<%
   if(dataList!=null && dataList.size()!=0)
   {
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">

	<tr height="5%">
		 <td class="formTitle" width="100%">
			<bean:message key="shoppingCart.title"/>
		 </td>
	</tr>

	<tr height="5%">
		 <td width="100%">
			&nbsp;
		 </td>
	</tr>
	
	
	 <tr height="85%">
		<td width="100%">
<!--  **************  Code for New Grid  *********************** -->
			<script>
					function queryshopingcart(id)
					{
						//do nothing
					} 				

					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "queryshopingcart";	
			</script>
			<%@ include file="/pages/content/search/AdvanceGrid.jsp" %>
<!--  **************  Code for New Grid  *********************** -->
		</td>
	</tr>

			<tr>
			<td>
			<table cellpadding="5" cellspacing="0" border="0" width="90%">
			<tr>
				<td width="10%" nowrap>
					<input type='checkbox' name='checkAll1' id='checkAll1' onClick='checkAll(this)'>
					<bean:message key="buttons.checkAll" />
				</td>
				<td width="70%">
					&nbsp;
				</td>
				<td width="10%" nowrap align="left">
					<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
						<bean:message key="buttons.delete"/>
					</html:button>
				</td>
				<td width="10%" nowrap align="left">
					<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
						<bean:message key="buttons.export"/>
					</html:button>
				</td> 
				
				<td width="10%" nowrap align="left">
				<%if(isSpecimenIdPresent.equals("true")){
					%>
					<html:button styleClass="actionButton"  property="orderButton" onclick="addToOrderList()">
						<bean:message key="buttons.addtolist"/>	
					</html:button>
					<%}else{%>
                     <html:button styleClass="actionButton"  disabled="true" property="orderButton" onclick="addToOrderList()">
						<bean:message key="buttons.addtolist"/>	
					</html:button>
					<%}%>
				</td>

			
			</tr>
			</table>
			</td>
			</tr>
			</table>
	<%}else{
			%>
     <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
     <tr height="5%">
		 <td class="formTitle" width="100%">
			<bean:message key="ShoppingCart.emptyCartTitle"/>
		 </td>
	</tr>
	</table>
	<%}%>
	</body>
	</html:form>
	</html:html>

