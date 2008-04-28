<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script>
function onCoordinatorChange()
{
	var submittedForValue = document.forms[0].submittedFor.value;
	var action = "Site.do?"+"operation="+document.forms[0].operation.value+"&pageOf=pageOfSite&isOnChange=true&coordinatorId="+document.getElementById("coordinatorId").value+"&submittedFor="+submittedForValue;
	document.forms[0].action = action;
	document.forms[0].submit();
}
	
</script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>


<html:form action='${requestScope.formName}'>
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">	
	<!-- NEW SITE REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation"/>
						<html:hidden property="submittedFor"/>	
					</td>
				</tr>
				
				<tr>
					<td><html:hidden property="id" /></td>
					<td><html:hidden property="onSubmit"/></td>
				</tr>

				<tr>
					<td><html:hidden property="pageOf"/></td>
				</tr>

					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<logic:equal name="operation" value='${requestScope.operationAdd}'>
								<bean:message key="site.title"/>
							</logic:equal>
							<logic:equal name="operation" value='${requestScope.operationEdit}'>
								<bean:message key="site.editTitle"/>
							</logic:equal>
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
							<html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="name" property="name"/>
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
						
							 
							 <autocomplete:AutoCompleteTag property="type"
										  optionsList = '${siteTypeList}'
										  initialValue='${siteForm.type}'
										  styleClass="formFieldSized"
									    />
							
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
						
							<html:select property="coordinatorId" styleClass="formFieldSized" styleId="coordinatorId" size="1" onchange="onCoordinatorChange()"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="userList" labelProperty="name" property="value"/>
							</html:select>
						
							
							&nbsp;
							<html:link href="#" styleId="newCoordinator" onclick="addNewAction('SiteAddNew.do?addNewForwardTo=coordinator&forwardTo=site&addNewFor=coordinator')">
								<bean:message key="buttons.addNew" />
							</html:link>
						</td>
					</tr>
		
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="emailAddress">
								<bean:message key="site.emailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="255"  size="30" styleId="emailAddress" property="emailAddress" />
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
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="street" property="street" />
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
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="city" property="city" />
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
						
						 <autocomplete:AutoCompleteTag property="state"
										  optionsList ='${stateList}'
										  initialValue='${siteForm.state}'
										  styleClass="formFieldSized"
									    />

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
						
						 <autocomplete:AutoCompleteTag property="country"
										  optionsList ='${countryList}'
										  initialValue='${siteForm.country}'
										  styleClass="formFieldSized"
									    />

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
							<html:text styleClass="formFieldSized"  maxlength="30"  size="30" styleId="zipCode" property="zipCode" />
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
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="phoneNumber" property="phoneNumber" />
							<bean:message key="format.phoneNumber" />
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
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="faxNumber" property="faxNumber" />
						</td>
					</tr>
					
					<logic:equal name='${requestScope.operationForActivityStatus}' value='${requestScope.operationEdit}'>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="activityStatus">
									<bean:message key="site.activityStatus" />
								</label>
							</td>
						<td class="formField">
						
						<autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList ='${activityStatusList}'
										  initialValue='${siteForm.activityStatus}'
										  styleClass="formFieldSized"
										  onChange='${strCheckStatus}'
									    />

						</td>
					</tr>
					</logic:equal>
					
					<tr>
						<td align="right" colspan="3">
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton">
										<bean:message  key="buttons.submit" />
									</html:submit>
								</td>
								<%-- td>
									<html:reset styleClass="actionButton" >
										<bean:message  key="buttons.reset" />
									</html:reset>
								</td --%>
							</tr>
						</table>
						<!-- action buttons end -->
						</td>
					</tr>

			</table>
		</td></tr>
		<!-- NEW SITE REGISTRATION ends-->
</table>
</html:form>