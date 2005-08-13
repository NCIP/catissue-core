<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName,prevPage=null,nextPage=null;
		
		//Change this to Constants.USER_EDIT_ACTION
        String searchFormName = new String(Constants.USER_EDIT_ACTION);

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);        

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.USER_EDIT_ACTION;
			Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
			prevPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+identifier;
			identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+identifier;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.USER_ADD_ACTION;

			if (pageOf.equals(Constants.PAGEOF_SIGNUP))
			{
				formName = Constants.SIGNUP_USER_ADD_ACTION;
			}
            readOnlyValue = false;
        }
%>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.USER_ADD_ACTION%>">
	
	   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_APPROVE_USER%>">
  	    	  <tr>
			  	<td align="right" colspan="3">

					<!-- action buttons begins -->
					<table cellpadding="6" cellspacing="2" border="0">
						<tr>
							<% 
								String backPage = Constants.APPROVE_USER_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
							%>
							<td>
								<a class="contentLink" href="<%=backPage%>">Approve User Home</a>
							</td>
							
							<td>
								<logic:notEmpty name="prevpage">
									<a class="contentLink" href="<%=prevPage%>">
										<bean:message key="approveUser.previous"/>
									</a>
								</logic:notEmpty> |
								<logic:notEmpty name="nextPage">
									<a class="contentLink" href="<%=nextPage%>">
										<bean:message key="approveUser.next"/>
									</a>
								</logic:notEmpty> 
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
						
				</td>
			</tr>		   	
		</logic:equal>
		
		<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_APPROVE_USER%>">
			<!-- ENTER IDENTIFIER BEGINS-->
			<br />
			<tr>
				<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="user.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="user.identifier" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier" />
						</td>
					</tr>
					<%
        				String changeAction = "setFormAction('" + searchFormName
	                							  + "');setOperation('" + Constants.SEARCH + "')";
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
		</logic:notEqual>


		<!-- NEW USER REGISTRATION BEGINS-->
		<tr>
		  <td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="systemIdentifier" />
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>" />
					</td>
				</tr>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="user.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="lastName">
								<bean:message key="user.lastName" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="lastName" property="lastName" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="firstName">
								<bean:message key="user.firstName" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="firstName" property="firstName" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="street">
								<bean:message key="user.street" />
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
								<bean:message key="user.city" />
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
								<bean:message key="user.state" />
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
							<label for="zipCode">
								<bean:message key="user.zipCode" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="zipCode" property="zipCode" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="country">
								<bean:message key="user.country"/>
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
							<label for="phoneNumber">
								<bean:message key="user.phoneNumber" />
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
								<bean:message key="user.faxNumber"/>
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="faxNumber" property="faxNumber" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="emailAddress">
								<bean:message key="user.emailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="emailAddress" property="emailAddress" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="institutionId">
								<bean:message key="user.institution" />
							</label>
						</td>
						<td class="formField">
							<html:select property="institutionId" styleClass="formFieldSized" styleId="institutionId" size="1">
								<html:options collection="institutionList" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="departmentId">
								<bean:message key="user.department" />
							</label>
						</td>
						<td class="formField">
							<html:select property="departmentId" styleClass="formFieldSized" styleId="departmentId" size="1">
								<html:options collection="departmentList" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="cancerResearchGroupId">
								<bean:message key="user.cancerResearchGroup" />
							</label>
						</td>
						<td class="formField">
							<html:select property="cancerResearchGroupId" styleClass="formFieldSized" styleId="cancerResearchGroupId" size="1">
								<html:options collection="cancerResearchGroupList" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>
					</logic:notEqual>
					</table>
				</td>
			</tr>
			
		 	<tr>
			  <td>
			    <br>
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="440">
					
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_SIGNUP%>">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="user.administrativeDetails.title" />
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_APPROVE_USER%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" width>
								<label for="status">
									<bean:message key="user.approveOperation" />
								</label>
							</td>
						<td class="formField">
							<html:select property="status" styleClass="formFieldSized" styleId="status" size="1">
								<html:options name="statusList" labelName="statusList" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
										
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="role">
								<bean:message key="user.role" />
							</label>
						</td>
						<td class="formField">
							<html:select property="role" styleClass="formFieldSized" styleId="role" size="1">
								<html:options name="roleIdList" labelName="roleList" />
							</html:select>
						</td>
					</tr>
					
    				 <tr>
			     		<td class="formRequiredNotice" width="5">&nbsp;</td>
				    	<td class="formLabel">
							<label for="comments">
								<bean:message key="user.comments"/>
							</label>
						</td>
				    	<td class="formField" colspan="4">
				    		<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments"/>
				    	</td>
				 	 </tr>
					</logic:notEqual>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_ADMIN%>">
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="activityStatus">
									<bean:message key="user.activityStatus" />
								</label>
							</td>
						<td class="formField">
							<html:select property="activityStatus" styleClass="formFieldSized" styleId="activityStatus" size="1">
								<html:options name="activityStatusList" labelName="activityStatusList" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
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
									<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
										<bean:message  key="buttons.submit" />
									</html:submit>
								</td>
								<td>
									<html:reset styleClass="actionButton" >
										<bean:message  key="buttons.reset" />
									</html:reset>
								</td>
							</tr>
						</table>
						
						<!-- action buttons end -->
						</td>
					</tr>
				</table>
			</td>
		</tr>

		<!-- NEW USER REGISTRATION ends-->
	</html:form>
</table>