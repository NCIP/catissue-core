<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="edu.wustl.catissuecore.query.ShoppingCart"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
	ShoppingCartForm form = (ShoppingCartForm)request.getAttribute("shoppingCartForm");
%>
<head>
	<script language="javascript" src="jss/script.js"></script>
	<script language="javascript">
		function onDelete()
		{
			var flag = confirm("Are you sure you want to delete the selected item(s)?");
			if(flag)
			{
				var action = "/catissuecore/ShoppingCart.do?operation=delete";
				document.forms[0].operation.value="delete";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
		}
		
		function onExport()
		{
			var action = "/catissuecore/ShoppingCart.do?operation=export";
			document.forms[0].operation.value="export";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
	</script>
</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.SHOPPING_CART_OPERATION%>">

	<!-- SHOPPING CART BEGINS-->
	<tr>
	<td>
	
		<table summary="" cellpadding="3" cellspacing="0" border="0">
			<tr>
				<td><html:hidden property="operation" value=""/></td>
			</tr>
			
			<tr>
				<td class="formMessage" colspan="7">&nbsp;</td>
			</tr>

			<tr>
				 <td class="formTitle" height="20" colspan="7">
					<bean:message key="shoppingCart.title"/>
				 </td>
			</tr>
	
			<!-- FIRST ROW -->
			
			<tr>
				<td class="formSerialNumberField" width="5">
					<html:checkbox property="checkAll" onclick="javaScript:CheckAll(this)" value="off"/>
				</td>
				<td class="formRequiredLabelCenter">
					<label for="type">
						<bean:message key="specimen.identifier"/> 
					</label>
				</td>
				<td class="formRequiredLabelCenter">
					<label for="type">
						<bean:message key="shoppingCart.type"/> 
					</label>
				</td>
				<td class="formRequiredLabel">
					<label for="subType">
						<bean:message key="shoppingCart.subType"/> 
					</label>
				</td>
				<td class="formRequiredLabelCenter">
					<label for="tissueSite">
						<bean:message key="specimen.tissueSite"/> 
					</label>
				</td>
				<td class="formRequiredLabelCenter">
					<label for="tissueSide">
						<bean:message key="specimen.tissueSide"/> 
					</label>
				</td>
				<td class="formRequiredLabelCenter">
					<label for="pathologicalStatus">
						<bean:message key="specimen.pathologicalStatus"/> 
					</label>
				</td>
				<%--td class="formRequiredLabelCenter">
					<label for="quantity">
						<bean:message key="specimen.quantity"/> 
					</label>
				</td--%>
			</tr>
			
			<% int i=1; %>
			
			<logic:iterate id="element" name="shoppingCart" property="cart" scope="session">
			<bean:define id="specimen" name="element" property="value" />
			<bean:define id="specimenCharacteristics" name="specimen" property="specimenCharacteristics" />
			<tr>
				<td class="formSerialNumberField" width="5">
					<%
						String chkName = "value(ShoppingCart:" + ((Specimen)specimen).getSystemIdentifier() + ")";
					%>
					<html:checkbox property="<%=chkName%>">
						<%=i%>
					</html:checkbox>
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimen" property="systemIdentifier" />
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimen" property="className" />
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimen" property="type" />
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimenCharacteristics" property="tissueSite" />
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimenCharacteristics" property="tissueSide" />
				</td>
				<td class="formLabelLeft">
					<bean:write name="specimenCharacteristics" property="pathologicalStatus" />
				</td>
				<%--td class="formLabelLeft">
					<bean:write name="specimen" property="quantity" />&nbsp;
				</td--%>
			</tr>
			<% i++; %>
			</logic:iterate>
			
			<%-- Buttons --%>
			<tr>
				<td align="right" colspan="7">
					<table cellpadding="4" cellspacing="0" border="0">
					<tr>
						<td>
							<html:button styleClass="actionButton" property="deleteCart" onclick="onDelete()">
								<bean:message key="buttons.delete"/>
							</html:button>
						</td>
						<td colspan="2">
							<html:button styleClass="actionButton" property="exportCart" onclick="onExport()">
								<bean:message key="buttons.export"/>
							</html:button>
						</td> 
					</tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
	</tr>
</html:form>
</table>