<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
	<script language="javascript">
		
	</script>
</head>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.STORAGE_TYPE_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_TYPE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_TYPE_ADD_ACTION;
            readOnlyValue = false;
        }
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.STORAGE_TYPE_ADD_ACTION%>">

	<logic:notEqual name="operation" value="<%=Constants.ADD%>"> 
	<!-- ENTER IDENTIFIER BEGINS-->	
	<br/>	
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="storageType.searchTitle"/>
					</td>
				</tr>	
		  
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="systemIdentifier">
							<bean:message key="storageType.systemIdentifier"/>
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier"/>
					</td>
				</tr>	
				<%
					String changeAction = "setFormAction('" + searchFormName
							  + "');setOperation('" + Constants.SEARCH + "');";
				%>
				<tr>
					<td align="right" colspan="3">
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<!-- ENTER IDENTIFIER ENDS-->	
	</logic:notEqual> 
	

	<!-- NEW Institute REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>

		<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<bean:message key="storageType.title"/>
			 </td>
		</tr>

		<!-- Name of the storageType -->
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="storageType.type"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10" size="30" styleId="type" property="type"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="defaultTemperature">
					<bean:message key="storageType.defaultTemperature"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10" size="30" styleId="defaultTemperature" property="defaultTemperature"/>
				°C
			</td>
		</tr>


		<tr>			
			<td class="formTitle" colspan="3">
				<label for="defaultCapacity">
					<bean:message key="storageType.defaultCapacity"/>
				</label>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionCapacity">
					<bean:message key="storageType.oneDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
					<html:text styleClass="formFieldSized10" size="10" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="twoDimensionCapacity">
					<bean:message key="storageType.twoDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10" size="10" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionLabel">
					<bean:message key="storageType.oneDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10" size="30" styleId="oneDimensionLabel" property="oneDimensionLabel"/>
			</td>
		</tr>


		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="twoDimensionLabel">
					<bean:message key="storageType.twoDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10" size="30" styleId="twoDimensionLabel" property="twoDimensionLabel"/>
			</td>
		</tr>

		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<%
        		String changeAction = "setFormAction('" + formName + "');";
			%> 
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td>
					<td><html:reset styleClass="actionButton"/></td> 
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</logic:notEqual>
		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Institute REGISTRATION ends-->
	 
	 </html:form>
 </table>