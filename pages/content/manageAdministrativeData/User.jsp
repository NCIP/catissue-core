<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.actionForm.UserForm"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName,prevPage=null,nextPage=null;
		
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);   
		
		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);  

        boolean readOnlyValue,roleStatus=false;

		if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
		{
			Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
			prevPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
			identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
		}

        if (operation.equals(Constants.EDIT))
        {
			if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
			{
				formName = Constants.APPROVE_USER_EDIT_ACTION;
			}
			else if (pageOf.equals(Constants.PAGEOF_USER_PROFILE))
			{
				formName = Constants.USER_EDIT_PROFILE_ACTION;
			}
			else
			{
            	formName = Constants.USER_EDIT_ACTION;
			}
            readOnlyValue = true;
        }
        else
        {
			if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
			{
				formName = Constants.APPROVE_USER_ADD_ACTION;
			}
			else
			{
            	formName = Constants.USER_ADD_ACTION;
				if (pageOf.equals(Constants.PAGEOF_SIGNUP))
				{
					formName = Constants.SIGNUP_USER_ADD_ACTION;
				}
			}

            readOnlyValue = false;
        }
        UserForm userForm = new UserForm();
		Object obj = request.getAttribute("userForm");
		if(obj != null && obj instanceof UserForm)
		{
		
			userForm = (UserForm)obj;
			if (pageOf.equals(Constants.PAGEOF_APPROVE_USER) &&
			   (userForm.getStatus().equals(Constants.APPROVE_USER_PENDING_STATUS) || 
				userForm.getStatus().equals(Constants.APPROVE_USER_REJECT_STATUS) ||
				userForm.getStatus().equals(Constants.SELECT_OPTION)))
			{
				roleStatus = true;
				if (userForm.getStatus().equals(Constants.APPROVE_USER_PENDING_STATUS))
				{
					operation = Constants.EDIT;
				}
			}
		}
		
		if (pageOf.equals(Constants.PAGEOF_USER_PROFILE))
		{
				roleStatus = true;
		}
%>
<script src="jss/script.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script>
//If the administrator keeps the user status pending update the user record and disable role.
function handleStatus(status)
{
	document.forms[0].role.value=<%=Constants.SELECT_OPTION_VALUE%>;
	document.forms[0].role.disabled=true;
	if (status.value == "<%=Constants.APPROVE_USER_APPROVE_STATUS%>")
	{
		document.forms[0].role.disabled=false;
	}
}
</script>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="620">
	
	<html:form action="<%=formName%>">
	
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
							&nbsp;
							<td>
								<logic:notEmpty name="prevpage">
								&nbsp;&nbsp;|&nbsp;<a class="contentLink" href="<%=prevPage%>">
										<bean:message key="approveUser.previous"/>
									</a>&nbsp;
								</logic:notEmpty>
								<logic:notEmpty name="nextPage">
								&nbsp;&nbsp;|&nbsp;<a class="contentLink" href="<%=nextPage%>">
										<bean:message key="approveUser.next"/>
									</a>&nbsp;
								</logic:notEmpty> 
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
						
				</td>
			</tr>		   	
		</logic:equal>
		
		<!-- NEW USER REGISTRATION BEGINS-->
		<tr>
		  <td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>	
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="id" />
						<html:hidden property="csmUserId" />
						<html:hidden property="<%=Constants.REQ_PATH%>" value="<%=reqPath%>" />
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
							<logic:equal name="operation" value="<%=Constants.ADD%>">
								<bean:message key="user.title"/>
							</logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="user.editTitle"/>
							</logic:equal>
						</td>
					</tr>
					
					<%
						boolean readOnlyEmail = false;
						if (operation.equals(Constants.EDIT) && pageOf.equals(Constants.PAGEOF_USER_PROFILE))
						{
							readOnlyEmail = true;
						}
					%>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="emailAddress">
								<bean:message key="user.emailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="emailAddress" property="emailAddress" readonly="<%=readOnlyEmail%>" />
						</td>
					</tr>
<!-- Mandar 24-Apr-06 : bugid 972 : Confirm Email address -->
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_PROFILE%>">				
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="confirmEmailAddress">
								<bean:message key="user.confirmemailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="confirmEmailAddress" property="confirmEmailAddress" readonly="<%=readOnlyEmail%>" />
						</td>
					</tr>
				</logic:notEqual>
<!-- Mandar 24-Apr-06 : bugid 972 : Confirm Email address end -->

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="lastName">
								<bean:message key="user.lastName" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="lastName" property="lastName" />
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
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="firstName" property="firstName" />
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_ADMIN%>">
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="newPassword">
							<bean:message key="user.newPassword" />
						</label>
					</td>
					<td class="formField">
						<html:password styleClass="formFieldSized" size="30" styleId="newPassword" property="newPassword" />
					</td>
				</tr>
			
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="confirmNewPassword">
							<bean:message key="user.confirmNewPassword" />
						</label>
					</td>
					<td class="formField">
						<html:password styleClass="formFieldSized" size="30" styleId="confirmNewPassword" property="confirmNewPassword" />
					</td>
				</tr>
				</logic:equal>
				</logic:equal>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="street">
								<bean:message key="user.street" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="street" property="street" />
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
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="city" property="city" />
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
						
							 <autocomplete:AutoCompleteTag property="state"
										  optionsList = "<%=request.getAttribute(Constants.STATELIST)%>"
										  initialValue="<%=userForm.getState()%>"
										  styleClass="formFieldSized"
									    />

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
							<html:text styleClass="formFieldSized"  maxlength="30" size="30" styleId="zipCode" property="zipCode" />
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
						
						 <autocomplete:AutoCompleteTag property="country"
										  optionsList = "<%=request.getAttribute(Constants.COUNTRYLIST)%>"
										  initialValue="<%=userForm.getCountry()%>"
										  styleClass="formFieldSized"
									    />

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
							<html:text styleClass="formFieldSized"  maxlength="50" size="30" styleId="phoneNumber" property="phoneNumber" />
							<bean:message key="format.phoneNumber" />
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
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="faxNumber" property="faxNumber" />
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
						
						
                                        <autocomplete:AutoCompleteTag property="institutionId"
										  optionsList = "<%=request.getAttribute("institutionList")%>"
										  initialValue="<%=userForm.getInstitutionId()%>"
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />
					
							&nbsp;
						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_SIGNUP%>">
							<html:link href="#" styleId="newInstitution" onclick="addNewAction('UserAddNew.do?addNewForwardTo=institution&forwardTo=user&addNewFor=institution')">
								<bean:message key="buttons.addNew" />
							</html:link>
						</logic:notEqual>	
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
						
						
                                        <autocomplete:AutoCompleteTag property="departmentId"
										  optionsList = "<%=request.getAttribute("departmentList")%>"
										  initialValue="<%=userForm.getDepartmentId()%>"
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />

							&nbsp;
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_SIGNUP%>">
							<html:link href="#" styleId="newDepartment" onclick="addNewAction('UserAddNew.do?addNewForwardTo=department&forwardTo=user&addNewFor=department')">
								<bean:message key="buttons.addNew" />
							</html:link>
							</logic:notEqual>
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
						
						    <autocomplete:AutoCompleteTag property="cancerResearchGroupId"
										  optionsList = "<%=request.getAttribute("cancerResearchGroupList")%>"
										  initialValue="<%=userForm.getCancerResearchGroupId()%>"
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />
							&nbsp;
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_SIGNUP%>">
							<html:link href="#" styleId="newCancerResearchGroup" onclick="addNewAction('UserAddNew.do?addNewForwardTo=cancerResearchGroup&forwardTo=user&addNewFor=cancerResearchGroup')">
								<bean:message key="buttons.addNew" />
							</html:link>
							</logic:notEqual>
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_PROFILE%>">
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel" width="140">
							<label for="role">
								<bean:message key="user.role" />
							</label>
						</td>
						<td class="formField">
						
								 <autocomplete:AutoCompleteTag property="role"
										  optionsList = "<%=request.getAttribute("roleList")%>"
										  initialValue="<%=userForm.getRole()%>"
										  styleClass="formFieldSized"
										  staticField="false"
										  readOnly="<%=roleStatus + ""%>"
									    />
						</td>
					</tr>
						
					</logic:equal>
					</logic:notEqual>
					</table>
				</td>
			</tr>
			
		 	<tr>
			  <td>
			    <br>
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="480">
					
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_SIGNUP%>">
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_PROFILE%>">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="user.administrativeDetails.title" />
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_APPROVE_USER%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" width="140">
								<label for="status">
									<bean:message key="user.approveOperation" />
								</label>
							</td>
						<td class="formField">
                                        <autocomplete:AutoCompleteTag property="role"
										  optionsList = "<%=request.getAttribute("roleList")%>"
										  initialValue="<%=userForm.getRole()%>"
										  styleClass="formFieldSized"
										  staticField="false"
										  readOnly="<%=roleStatus + ""%>"
									    />
						</td>
					</tr>
					</logic:equal>
										
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" width="140">
							<label for="role">
								<bean:message key="user.role" />
							</label>
						</td>
						<td class="formField">
                                        <autocomplete:AutoCompleteTag property="role"
										  optionsList = "<%=request.getAttribute("roleList")%>"
										  initialValue="<%=userForm.getRole()%>"
										  styleClass="formFieldSized"
                                          staticField="false"
										  readOnly="<%=roleStatus + ""%>"
									    />
						</td>
					</tr>
					
    				 <tr>
			     		<td class="formRequiredNotice" width="5">&nbsp;</td>
				    	<td class="formLabel" width="140">
							<label for="comments">
								<bean:message key="user.comments"/>
							</label>
						</td>
				    	<td class="formField" colspan="4">
				    		<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments"/>
				    	</td>
				 	 </tr>
					</logic:notEqual>
					</logic:notEqual>
					
					<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGEOF_USER_ADMIN%>">
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" width="140">
								<label for="activityStatus">
									<bean:message key="user.activityStatus" />
								</label>
							</td>
						<td class="formField">
						
						 <autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = "<%=request.getAttribute("activityStatusList")%>"
										  initialValue="<%=userForm.getActivityStatus()%>"
										  styleClass="formFieldSized"
				 
									    />

						</td>
					</tr>
					</logic:equal>
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
			</td>
		</tr>

		<!-- NEW USER REGISTRATION ends-->
	</html:form>
</table>