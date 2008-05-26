<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<script src="jss/script.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>

<script type="text/javascript">

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
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>

<!--new style code begins-->

<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="newMaintable">
	<html:form action='${requestScope.formName}'>
		<html:hidden property="operation" />
		<html:hidden property="submittedFor" />
		<html:hidden property="pageOf" />
		<html:hidden property="id" />
		<html:hidden property="csmUserId" />
		<html:hidden property='${requestScope.redirectTo}' />
		<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
			<html:hidden property="activityStatus" />
		</logic:equal>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="td_color_bfdcf3">
				<tr>
					<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="whitetable_bg">
						<tr>
							<td width="100%" colspan="2" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3" valign="top" class="td_color_bfdcf3">
									<table width="10%" border="0" cellpadding="0" cellspacing="0"
										background="images/uIEnhancementImages/table_title_bg.gif">
										<tr>
											<td width="74%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
											<bean:message key="user.name" /> </span></td>
											<td width="26%" align="right"><img
												src="images/uIEnhancementImages/table_title_corner2.gif"
												width="31" height="24" /></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
										<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;
										</td>
										<td width="97%" valign="top" class="td_color_bfdcf3">&nbsp;
										</td>
									</logic:equal>
									<logic:notEqual name="pageOf"
										value='${requestScope.pageOfSignUp}'>
										<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;
										</td>
										<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
										<td width="90%" valign="bottom" class="td_color_bfdcf3"
											style="padding-top: 4px;">
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td width="4%" class="td_tab_bg">&nbsp;</td>
												<!-- for tabs selection -->
												<logic:equal name="operation"
													value='${requestScope.addforJSP}'>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><img
														src="images/uIEnhancementImages/tab_add_user.jpg"
														alt="Add" width="57" height="22" /></td>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><html:link
														page="/SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1">
														<img src="images/uIEnhancementImages/tab_edit_user.jpg"
															alt="Edit" width="59" height="22" border="0" />
													</html:link></td>
													<td width="15%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><html:link
														page="/ApproveUserShow.do?pageNum=1&menuSelected=1">
														<img src="images/uIEnhancementImages/tab_approve_user.jpg"
															alt="Approve New Users" width="139" height="22"
															border="0" />
													</html:link></td>
												</logic:equal>
												<logic:equal name="operation"
													value='${requestScope.editforJSP}'>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><html:link
														page="/User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1">
														<img src="images/uIEnhancementImages/tab_add_user1.jpg"
															alt="Add" width="57" height="22" hspace="1" border="0" />
													</html:link></td>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><img
														src="images/uIEnhancementImages/tab_edit_user1.jpg"
														alt="Edit" width="59" height="22" hspace="1" /></td>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><html:link
														page="/ApproveUserShow.do?pageNum=1&menuSelected=1">
														<img src="images/uIEnhancementImages/tab_approve_user.jpg"
															alt="Approve New Users" width="139" height="22"
															border="0" />
													</html:link></td>
												</logic:equal>
												<td width="65%" valign="bottom" class="td_tab_bg">&nbsp;
												</td>
												<td width="1%" align="left" valign="bottom"
													class="td_color_bfdcf3">&nbsp;</td>
											</tr>
										</table>
										</td>
									</logic:notEqual>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="td_color_bfdcf3"
								style="padding-left: 10px; padding-right: 10px; padding-bottom: 10px;">
							<table width="100%" border="0" cellpadding="3" cellspacing="0"
								bgcolor="#FFFFFF">
								<tr>
									<td colspan="3" align="left">
									<table width="99%" border="0" cellpadding="1" cellspacing="0">
										<tr>
											<td>
											<table width="100%" border="0" cellpadding="0"
												cellspacing="0" class="td_color_ffffff">
												<tr>
													<td class=" grey_ar_s"><img
														src="images/uIEnhancementImages/star.gif" alt="Mandatory"
														width="6" height="6" hspace="0" vspace="0" /> <bean:message
														key="commonRequiredField.message" /></td>
												</tr>
											</table>
											</td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td colspan="3" align="left">
									<div id="part_det">
									<table width="100%" border="0" cellpadding="3" cellspacing="0">
										<logic:equal name="pageOf" value='${pageOfApproveUser}'>
											<tr>
												<td align="right" colspan="3"><!-- action buttons begins -->
												<table cellpadding="0" cellspacing="0" border="0">
													<tr>
														<td><span class="smalllink"> <a
															class="blue_ar_s_b" href='${requestScope.backPage}'>
														<bean:message key="buttons.approveUserHome" /> </a></span>&nbsp;</td>
														<logic:notEmpty name="prevpage">
															<td>|&nbsp; <span class="smalllink"> <a
																class="blue_ar_s_b" href='${requestScope.prevPageURL}'>
															<bean:message key="approveUser.previous" /> </a></span>&nbsp;</td>
														</logic:notEmpty>
														<logic:notEmpty name="nextPage">
															<td>&nbsp; <span class="smalllink"> <a
																class="blue_ar_s_b" href='${requestScope.nextPageURL}'>
															<bean:message key="approveUser.next" /> </a></span>&nbsp;</td>
														</logic:notEmpty>
													</tr>
												</table>
												<!-- action buttons end --></td>
											</tr>
										</logic:equal>

										<tr>

											<td colspan="8" align="left" bgcolor="#f3f8fb"
												class="blue_ar_b"><logic:equal name="operation"
												value='${requestScope.addforJSP}'>
												<bean:message key="user.details.title" />
											</logic:equal> <logic:equal name="operation"
												value='${requestScope.editforJSP}'>
												<bean:message key="user.editTitle" />
											</logic:equal></td>
										</tr>
										<tr height="1">
											<td></td>
										</tr>

										<tr>
											<td width="1%" align="left" class="black_ar"><span
												class="blue_ar_b"> <img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td width="20%" align="left" class="black_ar"><label
												for="emailAddress"> <bean:message
												key="user.emailAddress" /> </label></td>
											<td width="15%" align="left"><label> <html:text
												styleClass="black_ar" maxlength="255" size="30"
												styleId="emailAddress" property="emailAddress"
												readonly='${requestScope.readOnlyEmail}' /> </label></td>
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfUserProfile}'>
												<td width="19%" align="left">&nbsp;</td>
												<td width="1%" align="left"><span class="blue_ar_b">
												<img src="images/uIEnhancementImages/star.gif"
													alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
												</td>
												<td width="20%" align="left"><label
													for="confirmEmailAddress" class="black_ar"> <bean:message
													key="user.confirmemailAddress" /> </label></td>
												<td width="15%" align="left"><html:text
													styleClass="black_ar" maxlength="255" size="30"
													styleId="confirmEmailAddress"
													property="confirmEmailAddress"
													readonly='${requestScope.readOnlyEmail}' /></td>
												<td width="9%" align="left" valign="top">&nbsp;</td>
											</logic:notEqual>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><bean:message
												key="user.lastName" /></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="255" size="30" styleId="lastName"
												property="lastName" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><bean:message
												key="user.firstName" /></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="255" size="30" styleId="firstName"
												property="firstName" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<logic:equal name="pageOf"
											value='${requestScope.pageOfUserAdmin}'>
											<logic:equal name="operation"
												value='${requestScope.editforJSP}'>
												<tr>
													<td width="1%" align="left" class="black_ar"><span
														class="blue_ar_b"><img
														src="images/uIEnhancementImages/star.gif" alt="Mandatory"
														width="6" height="6" hspace="0" vspace="0" /></span></td>
													<td width="15%" align="left" class="black_ar"><label
														for="newPassword"> <bean:message
														key="user.newPassword" /> </label></td>
													<td align="left"><html:password styleClass="black_ar"
														size="30" styleId="newPassword" property="newPassword" /></td>

													<td align="left" class="black_ar">&nbsp;</td>
													<td width="1%" align="left" class="black_ar"><span
														class="blue_ar_b"><img
														src="images/uIEnhancementImages/star.gif" alt="Mandatory"
														width="6" height="6" hspace="0" vspace="0" /></span></td>
													<td width="15%" align="left" class="black_ar"><label
														for="confirmNewPassword"> <bean:message
														key="user.confirmNewPassword" /> </label></td>
													<td align="left"><html:password styleClass="black_ar"
														size="30" styleId="confirmNewPassword"
														property="confirmNewPassword" /></td>
													<td align="left" valign="top">&nbsp;</td>
												</tr>
											</logic:equal>
										</logic:equal>
										<tr>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><label for="street">
											<bean:message key="user.street" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="255" size="30" styleId="street" property="street" />
											</td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="city">
											<bean:message key="user.city" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="city" property="city" />
											</td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="state">
											<bean:message key="user.state" /> </label></td>
											<td align="left" class="black_new"><autocomplete:AutoCompleteTag
												property="state" optionsList='${requestScope.stateList}'
												initialValue='${userForm.state}'
												styleClass="formFieldSized12" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="zipCode">
											<bean:message key="user.zipCode" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="30" size="30" styleId="zipCode"
												property="zipCode" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="country">
											<bean:message key="user.country" /> </label></td>
											<td align="left" class="black_new"><autocomplete:AutoCompleteTag
												property="country" optionsList='${requestScope.countryList}'
												initialValue='${userForm.country}'
												styleClass="formFieldSized12" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label
												for="phoneNumber"> <bean:message
												key="user.phoneNumber" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="phoneNumber"
												property="phoneNumber" /> <bean:message
												key="format.phoneNumber" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><label for="faxNumber">
											<bean:message key="user.faxNumber" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="faxNumber"
												property="faxNumber" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label
												for="institutionId"> <bean:message
												key="user.institution" /> </label></td>
											<td align="left" class="black_new"><autocomplete:AutoCompleteTag
												property="institutionId"
												optionsList='${requestScope.instituteList}'
												initialValue='${userForm.institutionId}'
												styleClass="formFieldSized12" staticField="false" /></td>
											<td align="left">&nbsp; <span class="smalllink">
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfSignUp}'>
												<html:link href="#" styleClass="blue_ar_s_b"
													styleId="newInstitution" onclick="openInstitutionWindow();">
													<bean:message key="buttons.addNew" />
												</html:link>
											</logic:notEqual> </span></td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label
												for="departmentId"><bean:message
												key="user.department" /> </label></td>
											<td align="left" class="black_new"><autocomplete:AutoCompleteTag
												property="departmentId"
												optionsList='${requestScope.departmentList}'
												initialValue='${userForm.departmentId}'
												styleClass="formFieldSized12" staticField="false" /></td>
											<td align="left">&nbsp; <span class="smalllink">
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfSignUp}'>
												<html:link href="#" styleClass="blue_ar_s_b"
													styleId="newDepartment" onclick="openDepartmentWindow();">
													<bean:message key="buttons.addNew" />
												</html:link>
											</logic:notEqual> </span></td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label
												for="cancerResearchGroupId"> <bean:message
												key="user.cancerResearchGroup" /> </label></td>
											<td align="left" class="black_new"><autocomplete:AutoCompleteTag
												property="cancerResearchGroupId"
												optionsList='${requestScope.cancerResearchGroupList}'
												initialValue='${userForm.cancerResearchGroupId}'
												styleClass="formFieldSized12" staticField="false" /></td>
											<td align="left">&nbsp; <span class="smalllink">
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfSignUp}'>
												<html:link href="#" styleClass="blue_ar_s_b"
													styleId="newCancerResearchGroup" onclick="openCRGWindow();">
													<bean:message key="buttons.addNew" />
												</html:link>
											</logic:notEqual> </span></td>
										</tr>
									</table>
									</div>
									</td>
								</tr>
								<tr>
									<td colspan="3">&nbsp;</td>
								</tr>
								<logic:notEqual name="pageOf"
									value='${requestScope.pageOfSignUp}'>
									<logic:notEqual name="pageOf"
										value='${requestScope.pageOfUserProfile}'>
										<tr onclick="javascript:showHide('add_id')">
											<td align="left" class="tr_bg_blue1"><span
												class="blue_ar_b"><bean:message
												key="user.administrativeDetails.title" /></span></td>
											<td align="right" class="tr_bg_blue1"><a href="#"><img
												src="images/uIEnhancementImages/dwn_arrow1.gif" width="7"
												height="8" hspace="10" border="0" class="tr_bg_blue1" /></a></td>
										</tr>
										<tr>
											<td colspan="3" style="padding-top:10px;">
											<div id="add_id" style="display: none">
											<table width="100%" border="0" cellpadding="2"
												cellspacing="0">

												<logic:equal name="pageOf" value='${pageOfApproveUser}'>
													<tr>
														<td width="1%" height="25" align="left" class="black_ar">
														<span class="blue_ar_b"><img
															src="images/uIEnhancementImages/star.gif" alt="Mandatory"
															width="6" height="6" hspace="0" vspace="0" /></span></td>
														<td width="16%" align="left" class="black_ar"><label
															for="status"> <bean:message
															key="user.approveOperation" /> </label></td>
														<td width="83%" colspan="4" align="left" valign="top"
															class="black_new"><html:select property="status"
															styleClass="formFieldSizedNew" styleId="status" size="1"
															onchange="javascript:handleStatus(this)"
															onmouseover="showTip(this.id)"
															onmouseout="hideTip(this.id)">
															<html:options name="statusList" labelName="statusList" />
														</html:select></td>
													</tr>
												</logic:equal>
												<tr>
													<td width="1%" height="25" align="left" class="black_ar">
													<span class="blue_ar_b"><img
														src="images/uIEnhancementImages/star.gif" alt="Mandatory"
														width="6" height="6" hspace="0" vspace="0" /></span></td>
													<td width="16%" align="left" class="black_ar"><label
														for="role"><bean:message key="user.role" /> </label></td>
													<td width="83%" colspan="4" align="left" valign="top"
														class="black_new"><autocomplete:AutoCompleteTag
														property="role" optionsList='${requestScope.roleList}'
														initialValue='${userForm.role}'
														styleClass="formFieldSized12" staticField="false"
														readOnly='${requestScope.roleStatus}' /></td>
												</tr>
												<tr>
													<td height="25" align="left" class="black_ar">&nbsp;</td>
													<td align="left" class="black_ar"><label
														for="comments"> <bean:message key="user.comments" />
													</label></td>
													<td colspan="4" align="left" valign="top"><label>
													<html:textarea styleClass="black_ar_s" cols="34" rows="3"
														styleId="comments" property="comments" /> </label></td>
												</tr>
												<logic:equal name="pageOf"
													value='${requestScope.pageOfUserAdmin}'>
													<logic:equal name="operation"
														value='${requestScope.editforJSP}'>
														<tr>
															<td width="1%" height="25" align="left" class="black_ar"><span
																class="blue_ar_b"><img
																src="images/uIEnhancementImages/star.gif"
																alt="Mandatory" width="6" height="6" hspace="0"
																vspace="0" /></span></td>
															<td width="16%" align="left" class="black_ar"><label
																for="activityStatus"> <bean:message
																key="user.activityStatus" /> </label></td>
															<td width="83%" colspan="4" align="left" valign="top"
																class="black_new"><autocomplete:AutoCompleteTag
																property="activityStatus"
																optionsList='${requestScope.activityStatusList}'
																initialValue='${userForm.activityStatus}'
																styleClass="formFieldSized12" /></td>
														</tr>

													</logic:equal>
												</logic:equal>
											</table>
											</div>
											</td>
										</tr>
									</logic:notEqual>
								</logic:notEqual>
								<tr class="td_color_F7F7F7">
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr class="td_color_F7F7F7">
									<td colspan="3" class="buttonbg"><html:submit
										styleClass="blue_ar_b">
										<bean:message key="buttons.submit" />
									</html:submit> &nbsp;| <span class="cancellink"> <logic:notEqual
										name="pageOf" value='${requestScope.pageOfSignUp}'>
										<html:link onclick="closeUserWindow()" page="/ManageAdministrativeData.do"
											styleClass="blue_ar_s_b">
											<bean:message key="buttons.cancel" />
										</html:link>
									</logic:notEqual> <logic:equal name="pageOf"
										value='${requestScope.pageOfSignUp}'>
										<html:link page="/RedirectHome.do" styleClass="blue_ar_s_b">
											<bean:message key="buttons.cancel" />
										</html:link>
									</logic:equal> </span></td>
								</tr>
							</table>
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

<p><!--end content --></p>
