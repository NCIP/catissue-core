<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.querysuite.QueryShoppingCart"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>

<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

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
	
	String isSpecimenArrayPresent = (String)request.getAttribute(Constants.IS_SPECIMENARRAY_PRESENT);
	
	String disabled = "";
	boolean disabledList = false;
	if(isSpecimenArrayPresent!= null && isSpecimenArrayPresent.equals("true"))
	{
		disabled = "DISABLED";
		disabledList = true;
	}
	
	String disabledOrder = "";
	boolean disabledButton = false;
	if(isSpecimenIdPresent.equals("false"))
	{
		disabled = "DISABLED";
		disabledList = true;
		disabledButton = true;
		disabledOrder = "DISABLED";
	}

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

function onSubmit()
{
	if(document.forms[0].chkName[0].checked == true)
	{
		if(document.getElementById('specimenEventParameter').value == "Transfer")
		{
			dobulkTransferOperations();
		}
		else if(document.getElementById('specimenEventParameter').value == "Disposal")
		{
			dobulkDisposals();
		}
		else
		{
			alert("Only Transfer and Disposal bulk functionality is available in this version");
		}
	}
	else if(document.forms[0].chkName[1].checked == true)
	{
		editMultipleSp();
	}
	else if(document.forms[0].chkName[2].checked == true)
	{
		addToOrderList();
	}
}
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
					var action = "AddDeleteCart.do?operation=delete";
					document.forms[0].action = action;
					document.forms[0].target = "_parent";
					document.forms[0].submit();
				}
			}
			else
			{
				alert("Please select at least one checkbox");
			}
		}

function onExport()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var action = "ExportCart.do?operation=export";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
			}
		}
		
function dobulkTransferOperations()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var action = "BulkCart.do?operation=bulkTransfers";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
			}
		}
		
function dobulkDisposals()
		{
			var isChecked = updateHiddenFields();
		    
		    if(isChecked == "true")
		    {
				var action = "BulkCart.do?operation=bulkDisposals";
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
				var action = "BulkCart.do?operation=addToOrderList";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
			else
			{
				alert("Please select at least one checkbox");
			}
		}
		function editMultipleSp()
		{
		
			 var isChecked = updateHiddenFields();
		    if(isChecked == "true")
		    {
				var action = "BulkCart.do?operation=editMultipleSp";
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
<html:form action="AddDeleteCart.do">

<%
   if(dataList!=null && dataList.size()!=0)
   {
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="700" height="100%">

	<tr >
		 <td class="formTitle" colspan="2">
			<bean:message key="shoppingCart.title"/>
		 </td>
	</tr>

	<tr>
		 <td colspan="2">
			&nbsp;
		 </td>
	</tr>
	
	<tr width="700">
	<td width="700" class="formFieldNoBordersSimple" align="left">
		<input type='checkbox' name='checkAll1' id='checkAll1' onClick='checkAll(this)'>
				<bean:message key="buttons.checkAll" />
		</td>
		<td nowrap align="right" width="50%" class="padding-reight:3em">
			<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
				<bean:message key="buttons.delete"/>
			</html:button>
		
			<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
				<bean:message key="buttons.export"/>
			</html:button>
		</td>
	<tr>
	<tr>
		<td colspan="2" width="100%">
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
		<td colspan="2">
			&nbsp;
		</td>
	</tr>		
	<tr>
		<td nowrap class="formFieldNoBordersSimpleBold" colspan="2">
			<label for="selectLabel">&nbsp;&nbsp;
				<bean:message key="mylist.label.selectLabel"/>
			</label>
		</td>
	</tr>

	<tr>
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table cellpadding="2" cellspacing="0" border="0" width="300">
			<tr>
				<td nowrap class="formFieldNoBordersSimpleNotBold">
					<INPUT TYPE='RADIO' NAME='chkName' value="Events" <%=disabled%> ><bean:message key="mylist.label.specimenEvent"/></INPUT>
				</td>
				<% if(!disabledList)
					{
				%>
				<td nowrap class="formFieldNoBordersSimpleNotBold">
					<autocomplete:AutoCompleteTag property="specimenEventParameter"
						  optionsList = "<%=request.getAttribute(Constants.EVENT_PARAMETERS_LIST)%>"
						  initialValue="Transfer"
						  />
				</td>
				<%
					}
					else
					{
				%>
					<td nowrap class="formFieldNoBordersSimpleNotBold">
						<input type="text" id="specimenEventParameter" name="specimenEventParameter" value="Transfer" readonly="<%=disabledList%>"/>
					</td>
				<%
					}
				%>
			</tr>
			<tr>
				<td nowrap class="formFieldNoBordersSimpleNotBold">
					<INPUT TYPE='RADIO' NAME='chkName' value="Specimenpage" <%=disabled%> ><bean:message key="mylist.label.multipleSpecimenPage"/></INPUT>	
				</td>
				<td nowrap class="formFieldNoBordersSimple">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td nowrap class="formFieldNoBordersSimpleNotBold">
					<INPUT TYPE='RADIO' NAME='chkName' value="OrderSpecimen" checked=true <%=disabledOrder%> ><bean:message key="mylist.label.orderBioSpecimen"/></INPUT>
				</td>
				<td nowrap class="formFieldNoBordersSimple">
					&nbsp;
				</td>
			</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td  colspan="2" nowrap class="formFieldNoBordersSimpleNotBold">
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td colspan="2" nowrap class="formFieldNoBordersSimpleNotBold">&nbsp;&nbsp;
			<html:button styleClass="actionButton" property="proceed" onclick="onSubmit()" disabled="<%=disabledButton%>" >
				<bean:message key="buttons.submit"/>	
			</html:button>
		</td>
	</tr>

	</table>
	<%}else{
			%>
     <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
     <tr >
		<td > <html:errors/></td>

	</tr>
	</table>
	<%}%>
	</body>
	</html:form>
	</html:html>