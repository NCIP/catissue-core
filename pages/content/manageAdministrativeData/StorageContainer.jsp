<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.STORAGE_CONTAINER_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_CONTAINER_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_CONTAINER_ADD_ACTION;
            readOnlyValue = false;
        }
        %>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.STORAGE_CONTAINER_ADD_ACTION%>">
		<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<!-- ENTER IDENTIFIER BEGINS-->
			<br />
			<tr>
				<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="storageContainer.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="storageContainer.systemIdentifier" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier" />
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


		<!-- NEW STORAGE CONTAINER REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="storageContainer.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="type">
								<bean:message key="storageContainer.type" />
							</label>
						</td>
						<td class="formField" nowrap>
							<html:select property="type" styleClass="formFieldSized" styleId="type" size="1">
								<html:options name="storageContainerList" labelName="storageContainerList" />
							</html:select>
							&nbsp;
							<html:link page="/StorageType.do?operation=add">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<html:radio styleClass="" styleId="parentContainer" property="parentContainer" value="1" >
								<label for="site">
									<bean:message key="storageContainer.site" />
								</label>
							</html:radio>
						</td>
						<td class="formField">
							<html:select property="site" styleClass="formFieldSized" styleId="site" size="1">
								<html:options name="siteList" labelName="siteList" />
							</html:select>
							&nbsp;
							<html:link page="/Site.do?operation=add">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" nowrap>
							<html:radio styleClass="" styleId="parentContainer" property="parentContainer" value="2">
								<label for="site">
									<bean:message key="storageContainer.parentContainer" />
								</label>
							</html:radio>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="parentContainer" property="parentContainer" readonly="<%=readOnlyValue%>" />
							&nbsp;
							<html:submit styleClass="actionButton" onclick="">
								<bean:message key="buttons.map"/>
							</html:submit>
						</td>
					</tr>
	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="noOfContainers">
								<bean:message key="storageContainer.noOfContainers" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="noOfContainers" property="noOfContainers" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="startNumber">
								<bean:message key="storageContainer.startNumber" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="actionButton" size="30" styleId="startNumber" property="startNumber" readonly="<%=readOnlyValue%>" />
							&nbsp;
							<html:submit styleClass="actionButton" onclick="">
								<bean:message key="buttons.next"/>
							</html:submit>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="startNumber">
								<bean:message key="storageContainer.barcode" />
							</label>
						</td>
						<td class="formField" colspan="1">
							<html:text styleClass="formFieldSized" size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="defaultTemperature">
								<bean:message key="storageContainer.temperature" />
							</label>
						</td>
						<td class="formField" colspan="1">
							<html:text styleClass="formFieldSized" size="30" styleId="defaultTemperature" property="defaultTemperature" readonly="<%=readOnlyValue%>" />
							°C
						</td>
					</tr>
					
					<tr>
						<td class="formSubTableTitle" colspan="3">
							<label for="capacity">
								<bean:message key="storageContainer.capacity" />
							</label>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="oneDimensionLabel">
								<bean:message key="storageContainer.oneDimensionLabel" />
							</label>
						</td>
						<td class="formField" colspan="1">
							<html:text styleClass="formFieldSized" size="30" styleId="oneDimensionLabel" property="oneDimensionLabel" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" nowrap>
							<label for="twoDimensionLabel">
								<bean:message key="storageContainer.twoDimensionLabel" />
							</label>
						</td>
						<td class="formField" colspan="1">
							<html:text styleClass="formFieldSized" size="30" styleId="twoDimensionLabel" property="twoDimensionLabel" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					<tr>
						<td class="formSubTableTitle" colspan="3">
							<label for="details">
								<bean:message key="storageContainer.details" />
							</label>
							<html:submit styleClass="actionButton" onclick="">
								<bean:message key="buttons.addMore"/>
							</html:submit>
						</td>
					</tr>
									
					<tr>
						<td class="formSubTableTitle" width="5">#</td>
						<td class="formSubTableTitle" nowrap>
							<label for="key">
								<bean:message key="storageContainer.key" />
							</label>
						</td>
						<td class="formSubTableTitle" nowrap colspan="1">
							<label for="value">
								<bean:message key="storageContainer.value" />
							</label>
						</td>
					</tr>
					
					<tr>
						<td class="formSerialNumberField" width="5">1.</td>
						<td class="formField" >
							<html:text styleClass="formFieldSized" size="30" styleId="key" property="key" readonly="<%=readOnlyValue%>" />
						</td>
						<td class="formField" colspan="1">
							<html:text styleClass="formFieldSized" size="30" styleId="type" property="type" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					</logic:equal>
				</table>
				
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">	
					<tr>
						<td align="right">
						<%
        					String changeAction = "setFormAction('" + formName + "');";
				        %> 
						
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   		</td>
								<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
								</td> 
							</tr>
						</table>
						<!-- action buttons end -->
						</td>
					</tr>
					
				</logic:notEqual>
			</table>

			</td>
		</tr>

		<!-- NEW STORAGE CONTAINER REGISTRATION ends-->
	</html:form>
</table>