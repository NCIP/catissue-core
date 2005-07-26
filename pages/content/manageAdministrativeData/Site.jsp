<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.SITE_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.SITE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.SITE_ADD_ACTION;
            readOnlyValue = false;
        }
%>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.SITE_ADD_ACTION%>">
		<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<!-- ENTER IDENTIFIER BEGINS-->
			<br />
			<tr>
				<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="site.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="systemIdentifier">
								<bean:message key="site.systemIdentifier" />
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


		<!-- NEW SITE REGISTRATION BEGINS-->
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
							<bean:message key="site.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="name">
								<bean:message key="site.name" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="name" property="name" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
			
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="type">
								<bean:message key="site.type"/>
							</label>
						</td>

						<td class="formField">
							<html:select property="type" styleClass="formFieldSized" styleId="type" size="1">
								<html:options name="siteTypeList" labelName="siteTypeList" />
							</html:select>
						</td>
					</tr>
			
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="coordinator">
								<bean:message key="site.coordinator" />
							</label>
						</td>
						<td class="formField">
							<html:select property="coordinatorId" styleClass="formFieldSized" styleId="coordinatorId" size="1">
								<html:options name="userIdList" labelName="userList" />
								<%-- <html:options name="userList" labelName="userList" /> --%>
							</html:select>
							&nbsp;
							<html:link page="/User.do?operation=add&amp;pageOf=">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>
		
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="emailAddress">
								<bean:message key="site.emailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="emailAddress" property="emailAddress" />
						</td>
					</tr>
									
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="street">
								<bean:message key="site.street" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="street" property="street" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="city">
								<bean:message key="site.city" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="city" property="city" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="state">
								<bean:message key="site.state" />
							</label>
						</td>
						<td class="formField">
							<html:select property="state" styleClass="formFieldSized" styleId="state" size="1">
								<html:options name="stateList" labelName="stateList" />
							</html:select>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="country">
								<bean:message key="site.country"/>
							</label>
						</td>

						<td class="formField">
							<html:select property="country" styleClass="formFieldSized" styleId="country" size="1">
								<html:options name="countryList" labelName="countryList" />
							</html:select>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="zipCode">
								<bean:message key="site.zipCode" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="zipCode" property="zipCode" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="phoneNumber">
								<bean:message key="site.phoneNumber" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="phoneNumber" property="phoneNumber" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="faxNumber">
								<bean:message key="site.faxNumber"/>
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="faxNumber" property="faxNumber" />
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="activityStatus">
									<bean:message key="site.activityStatus" />
								</label>
							</td>
						<td class="formField">
							<html:select property="activityStatus" styleClass="formFieldSized" styleId="activityStatus" size="1">
								<html:options name="activityStatusList" labelName="activityStatusList" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
					
					<tr>
						<td align="right" colspan="3">
						<%
        					String changeAction = "setFormAction('" + formName + "');";
				        %> 
						
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
								</td>
								<td>
									<html:reset styleClass="actionButton" />
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

		<!-- NEW SITE REGISTRATION ends-->
	</html:form>
</table>