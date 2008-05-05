<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<script src="jss/script.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script>
//If the administrator keeps the user status pending update the user record and disable role.
function handleStatus(status)
{
	document.forms[0].role.value='${requestScope.SELECT_OPTION_VALUE}';
	document.forms[0].role.readOnly=true;
	document.getElementById("displayrole").readOnly=true;
	if (status.value == '${requestScope.Approve}')
	{
    	document.forms[0].role.readOnly=false;
	   	document.getElementById("displayrole").readOnly=false;
	}
	else
	{
		document.getElementById("displayrole").value="";
		document.getElementById("role").value="";
	}
}
</script>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="620">
	
	<html:form action='${requestScope.formName}'>
	  <logic:equal name='${requestScope.pageOfforJSP}' value='${pageOfApproveUser}'>
  	    	  <tr>
			  	<td align="right" colspan="3">

					<!-- action buttons begins -->
					<table cellpadding="6" cellspacing="2" border="0">
						<tr>
							<td>
							<a class="contentLink" href='${requestScope.backPage}'>Approve User Home</a>
							</td>
							&nbsp;
							<td>
								<logic:notEmpty name="prevpage">
								&nbsp;&nbsp;|&nbsp;<a class="contentLink" href='${requestScope.prevPage}'>
										<bean:message key="approveUser.previous"/>
									</a>&nbsp;
								</logic:notEmpty>
								<logic:notEmpty name="nextPage">
								&nbsp;&nbsp;|&nbsp;<a class="contentLink" href='${requestScope.nextPage}'>
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
						<html:hidden property="operation"/>
						<html:hidden property="submittedFor"/>
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="id" />
						<html:hidden property="csmUserId" />
						<html:hidden property='${requestScope.redirectTo}'/>
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property='${requestScope.pageOfforJSP}'/>
					</td>
				</tr>

				<logic:notEqual name='${requestScope.operationforJSP}' value='${requestScope.searchforJSP}'>
					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<logic:equal name="operation" value='${requestScope.addforJSP}'>
								<bean:message key="user.title"/>
							</logic:equal>
							<logic:equal name="operation" value='${requestScope.editforJSP}'>
								<bean:message key="user.editTitle"/>
							</logic:equal>
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
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="emailAddress" property="emailAddress" readonly='${requestScope.readOnlyEmail}'/>
						</td>
					</tr>
<!-- Mandar 24-Apr-06 : bugid 972 : Confirm Email address -->
				<logic:notEqual name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfUserProfile}'>				
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="confirmEmailAddress">
								<bean:message key="user.confirmemailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="confirmEmailAddress" property="confirmEmailAddress" readonly='${requestScope.readOnlyEmail}'/>
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
					
					<logic:equal name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfUserAdmin}'>
					<logic:equal name='${requestScope.operationforJSP}'value='${requestScope.editforJSP}'>
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
										  optionsList ='${requestScope.stateList}'
										  initialValue='${userForm.state}'
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
										  optionsList = '${requestScope.countryList}'
										  initialValue='${userForm.country}'
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
										  optionsList ='${requestScope.instituteList}'
										  initialValue='${requestScope.institutionId}'
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />
					
							&nbsp;
						<logic:notEqual name='${requestScope.pageOfforJSP}'value='${requestScope.pageOfSignUp}'>
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
										  optionsList ='${requestScope.departmentList}'
										  initialValue='${requestScope.departmentId}'
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />

							&nbsp;
							<logic:notEqual name='${requestScope.pageOfforJSP}'value='${requestScope.pageOfSignUp}'>
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
										  optionsList ='${requestScope.cancerResearchGroupList}'
										  initialValue='${requestScope.cancerResearchGroupId}'
										  styleClass="formFieldSized"
                                          staticField="false"
										  
									    />
							&nbsp;
							<logic:notEqual name='${requestScope.pageOfforJSP}'value='${requestScope.pageOfSignUp}'>
							<html:link href="#" styleId="newCancerResearchGroup" onclick="addNewAction('UserAddNew.do?addNewForwardTo=cancerResearchGroup&forwardTo=user&addNewFor=cancerResearchGroup')">
								<bean:message key="buttons.addNew" />
							</html:link>
							</logic:notEqual>
						</td>
					</tr>
					
					<logic:equal name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfUserProfile}'>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel" width="140">
							<label for="role">
								<bean:message key="user.role" />
							</label>
						</td>
						<td class="formField">
						
								 <autocomplete:AutoCompleteTag property="role"
										  optionsList ='${requestScope.roleList}'
										  initialValue='${userForm.role}'
										  styleClass="formFieldSized"
										  staticField="false"
										  readOnly='${requestScope.roleStatus}'
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
					
					<logic:notEqual name='${requestScope.pageOfforJSP}'value='${requestScope.pageOfSignUp}'>
					<logic:notEqual name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfUserProfile}'>
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="user.administrativeDetails.title" />
						</td>
					</tr>
					
					<logic:equal name='${requestScope.pageOfforJSP}' value='${pageOfApproveUser}'>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" width="140">
								<label for="status">
									<bean:message key="user.approveOperation" />
								</label>
							</td>
						<td class="formField">
                              	<html:select property="status" styleClass="formFieldSized" styleId="status" size="1" onchange="javascript:handleStatus(this)" 
						    	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="statusList" labelName="statusList" />
							</html:select>
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
										  optionsList ='${requestScope.roleList}'
										  initialValue='${userForm.role}'
										  styleClass="formFieldSized"
                                          staticField="false"
										  readOnly='${requestScope.roleStatus}'
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
					<logic:equal name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfSignUp}'>
                    <html:hidden property="activityStatus"/>
                    </logic:equal>
		
					<logic:equal name='${requestScope.pageOfforJSP}' value='${requestScope.pageOfUserAdmin}'>
					<logic:equal name='${requestScope.operationforJSP}' value='${requestScope.editforJSP}'>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" width="140">
								<label for="activityStatus">
									<bean:message key="user.activityStatus" />
								</label>
							</td>
						<td class="formField">
						
						 <autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = '${requestScope.activityStatusList}'
										  initialValue='${userForm.activityStatus}'
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