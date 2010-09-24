<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>


<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="org.apache.struts.action.Action"%>
<%@ page import="org.apache.struts.action.ActionError"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="org.apache.struts.action.ActionErrors"%>

<%@ page
	import="org.apache.struts.action.Action,org.apache.struts.action.ActionError,edu.wustl.common.util.global.ApplicationProperties,org.apache.struts.action.ActionErrors"%>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script src="jss/script.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>

<script language="JavaScript">
	var errorMessForRole='${requestScope.errorMessageForRole}';
	var errorMessForSite='${requestScope.errorMessageForSite}';
	var errorMessForUser='${requestScope.errorMessageForUser}';
	var errorMessForCP='${requestScope.errorMessageForCP}';
	var errorMessForPriv='${requestScope.errorMessageForPrivilege}';
</script>

</head>

<body>

<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script type="text/javascript" src="jss/queryModule.js"></script>
<%
	//Object obj=  request.getAttribute(Constants.ACTIONLIST);
	String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
	String operation = (String) request
			.getAttribute(Constants.OPERATION);
	List siteList = (List) request.getAttribute(Constants.SITELIST);
	List roleList = (List) request.getAttribute(Constants.ROLELIST);
	List actionList = (List) request.getAttribute(Constants.ACTIONLIST);
	List cpList = (List) request.getAttribute(Constants.CPLIST);
%>

<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">

	<html:form action='${requestScope.formName}'>
		<html:hidden property="operation" styleId="operation" />
		<html:hidden property="submittedFor" />
		<html:hidden property="targetLoginName" />
		<html:hidden property="pageOf" />
		<html:hidden property="id" />
		<html:hidden property="csmUserId" />
		<html:hidden property='${requestScope.redirectTo}' />
		<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
			<html:hidden property="activityStatus" />
		</logic:equal>

		<logic:notEqual name="targetLoginName" value=''>
			<html:hidden property="lastName"/>
			<html:hidden property="firstName"/>
		</logic:notEqual>

		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="user.name" /></span></td>
					<td align="right"><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title - User" width="31" height="24" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="tablepadding"><logic:equal name="pageOf"
				value='${requestScope.pageOfSignUp}'>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
						<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
						<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
						<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
						<td width="90%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
					</tr>
				</table>
			</logic:equal> <logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_tab_bg"><img
							src="images/uIEnhancementImages/spacer.gif" alt="spacer"
							width="50" height="1"></td>
						<logic:equal name="operation" value='${requestScope.addforJSP}'>
							<td valign="bottom"><img
								src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add"
								width="57" height="22" /><a href="#"></a></td>
							<logic:notEqual parameter="openInCPFrame" value='yes'>
								<td valign="bottom"><html:link
									page="/SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1">
									<img src="images/uIEnhancementImages/tab_edit_notSelected.jpg"
										alt="Edit" width="59" height="22" border="0" />
								</html:link><a href="#"></a></td>
								<td valign="bottom"><html:link
									page="/ApproveUserShow.do?pageNum=1&menuSelected=1">
									<img src="images/uIEnhancementImages/tab_approve_user.jpg"
										alt="Approve New Users" width="139" height="22" border="0" />
								</html:link><a href="#"></a></td>
							</logic:notEqual>
						</logic:equal>
						<logic:equal name="operation" value='${requestScope.editforJSP}'>
							<td valign="bottom"><html:link
								page="/User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1">
								<img src="images/uIEnhancementImages/tab_add_notSelected.jpg"
									alt="Add" width="57" height="22" />
							</html:link><a href="#"></a></td>
							<logic:notEqual parameter="openInCPFrame" value='yes'>
								<td valign="bottom"><img
									src="images/uIEnhancementImages/tab_edit_selected.jpg"
									alt="Edit" width="59" height="22" border="0" /></td>
								<td valign="bottom"><html:link
									page="/ApproveUserShow.do?pageNum=1&menuSelected=1">
									<img src="images/uIEnhancementImages/tab_approve_user.jpg"
										alt="Approve New Users" width="139" height="22" border="0" />
								</html:link><a href="#"></a></td>
							</logic:notEqual>
						</logic:equal>
						<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
					</tr>
				</table>
			</logic:notEqual>
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">

				<tr>
					<td colspan="2" align="left" class="bottomtd"><%@ include
						file="/pages/content/common/ActionErrors.jsp"%>
					</td>
				</tr>

				<tr>
					<td colspan="2" class="messagetexterror">
					<div id="errorMessImgDiv" style="display:none">

					<table>
						<tr>
							<td><c:if
								test="${requestScope['org.apache.struts.action.ERROR'] == null }">
								<tr>
									<td valign="top"><img
										src="images/uIEnhancementImages/alert-icon.gif"
										alt="error messages" width="16" vspace="0" hspace="0"
										height="18" valign="top"></td>
									<td class="messagetexterror" align="left"><strong><bean:message
										key="errors.title" /></strong></td>
								</tr>
							</c:if></td>
						</tr>
					</table>
					</div>
					<div id="errorMess" style="display:none"></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="tr_bg_blue1"><span
						class="blue_ar_b">&nbsp;<logic:equal name="operation"
						value='${requestScope.addforJSP}'><bean:message key="user.details.title" />
					</logic:equal> <logic:equal name="operation" value='${requestScope.editforJSP}'>
						<bean:message key="user.editTitle" />
					</logic:equal> </span></td>
				</tr>
				<tr>
					<td colspan="2" align="left">
					<table width="100%" border="0" cellpadding="3" cellspacing="0">
						<tr>
							<td width="1%" align="center" class="black_ar"><span
								class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td width="17%" align="left" class="black_ar"><bean:message
								key="user.emailAddress" /></td>
							<td width="19%" align="left"><html:text
								styleClass="black_ar" maxlength="255" size="30"
								styleId="emailAddress" property="emailAddress"
								readonly='${requestScope.readOnlyEmail}'/></td>
							<td width="13%" align="left">&nbsp;</td>

							<logic:notEqual name="pageOf"
								value='${requestScope.pageOfUserProfile}'>
								<td width="1%" align="center"><span class="blue_ar_b"><img
									src="images/uIEnhancementImages/star.gif" alt="Mandatory"
									width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td width="17%" align="left"><label
									for="confirmEmailAddress" class="black_ar"><bean:message
									key="user.confirmemailAddress" /></label></td>
								<td width="19%" align="left"><html:text
									styleClass="black_ar" maxlength="255" size="30"
									styleId="confirmEmailAddress" property="confirmEmailAddress"
									readonly='${requestScope.readOnlyEmail}' /></td>
								<td width="13%" align="left" valign="top">&nbsp;</td>
							</logic:notEqual>
							<logic:equal name="pageOf"
								value='${requestScope.pageOfUserProfile}'>
								<td width="32%" align="left" colspan="3" valign="top">&nbsp;</td>
							</logic:equal>
						</tr>
						<logic:notEqual name="targetLoginName" value=''>
						<tr>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.lastName" /></td>
							<td width="22%" align="left" class="black_ar">
								<label>
								 	${requestScope.userForm.lastName}
								</label>
							</td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.firstName" /></td>
							<td width="22%" align="left" class="black_ar">

								<label>
									${requestScope.userForm.firstName}
								</label>
							</td>
							<td align="left" valign="top">&nbsp;</td>
						</tr>
						</logic:notEqual>
						<logic:equal name="targetLoginName" value=''>
						<tr>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.lastName" /></td>
							<td align="left"><html:text styleClass="black_ar"
								maxlength="255" size="30" styleId="lastName" property="lastName" /></td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.firstName" /></td>
							<td align="left"><html:text styleClass="black_ar"
								maxlength="255" size="30" styleId="firstName"
								property="firstName" /></td>
							<td align="left" valign="top">&nbsp;</td>
						</tr>
						</logic:equal>

						<logic:equal name="pageOf" value='${requestScope.pageOfUserAdmin}'>
							<logic:equal name="operation" value='${requestScope.editforJSP}'>
								<tr>
									<td align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td align="left" class="black_ar"><label for="newPassword">
									<bean:message key="user.newPassword" /> </label></td>
									<td align="left"><html:password styleClass="black_ar"
										size="30" styleId="newPassword" property="newPassword"
										value="<%=Constants.DUMMY_PASSWORD%>" /></td>
									<td align="left" class="black_ar">&nbsp;</td>
									<td align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td align="left" class="black_ar"><label
										for="confirmNewPassword"> <bean:message
										key="user.confirmNewPassword" /> </label></td>
									<td align="left"><html:password styleClass="black_ar"
										size="30" styleId="confirmNewPassword"
										property="confirmNewPassword"
										value="<%=Constants.DUMMY_PASSWORD%>" /></td>
									<td align="left" valign="top">&nbsp;</td>
								</tr>
							</logic:equal>
						</logic:equal>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar"><bean:message
								key="user.street" /></td>
							<td align="left"><html:text styleClass="black_ar"
								maxlength="255" size="30" styleId="street" property="street" /></td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.city" /></td>
							<td align="left"><html:text styleClass="black_ar"
								maxlength="50" size="30" styleId="city" property="city" /></td>
							<td align="left" valign="top">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.state" /></td>
							<td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag
								property="state" optionsList='${requestScope.stateList}'
								initialValue='${userForm.state}' styleClass="black_ar" size="27" /></td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.zipCode" /></td>
							<td align="left" class="black_ar"><html:text
								style="text-align:right" styleClass="black_ar" maxlength="30"
								size="30" styleId="zipCode" property="zipCode" /></td>
							<td align="left" valign="top">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.country" /></td>
							<td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag
								property="country" optionsList='${requestScope.countryList}'
								initialValue='${userForm.country}' styleClass="black_ar"
								size="27" /></td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><bean:message
								key="user.phoneNumber" /></td>
							<td align="left"><html:text styleClass="black_ar"
								style="text-align:right" maxlength="50" size="30"
								styleId="phoneNumber" property="phoneNumber" /></td>
							<td align="left" valign="top">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar"><bean:message
								key="user.faxNumber" /></td>
							<td align="left"><html:text styleClass="black_ar"
								style="text-align:right" maxlength="50" size="30"
								styleId="faxNumber" property="faxNumber" /></td>
							<td align="left" class="black_ar">&nbsp;</td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><label for="institutionId"><bean:message
								key="user.institution" /></label></td>
							<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
								property="institutionId"
								optionsList='${requestScope.instituteList}'
								initialValue='${userForm.institutionId}' styleClass="black_ar"
								staticField="false" size="27" /></td>
							<td align="left"><logic:notEqual name="pageOf"
								value='${requestScope.pageOfSignUp}'>
								<html:link href="#" styleClass="view" styleId="newInstitution"
									onclick="openInstitutionWindow();">
									<bean:message key="buttons.addNew" />
								</html:link>
							</logic:notEqual></td>
						</tr>
						<tr>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><label for="departmentId"><bean:message
								key="user.department" /> </label></td>
							<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
								property="departmentId"
								optionsList='${requestScope.departmentList}'
								initialValue='${userForm.departmentId}' styleClass="black_ar"
								staticField="false" size="27" /></td>
							<td align="left"><a href="#" class="view"> <logic:notEqual
								name="pageOf" value='${requestScope.pageOfSignUp}'>
								<html:link href="#" styleClass="view" styleId="newDepartment"
									onclick="openDepartmentWindow();">
									<bean:message key="buttons.addNew" />
								</html:link>
							</logic:notEqual></a></td>
							<td align="center" class="black_ar"><span class="blue_ar_b"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" /></span></td>
							<td align="left" class="black_ar"><label
								for="cancerResearchGroupId"><bean:message
								key="user.cancerResearchGroup" /> </label></td>
							<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
								property="cancerResearchGroupId"
								optionsList='${requestScope.cancerResearchGroupList}'
								initialValue='${userForm.cancerResearchGroupId}'
								styleClass="black_ar" staticField="false" size="27" /></td>
							<td align="left"><a href="#" class="view"> <logic:notEqual
								name="pageOf" value='${requestScope.pageOfSignUp}'>
								<html:link href="#" styleClass="view"
									styleId="newCancerResearchGroup" onclick="openCRGWindow();">
									<bean:message key="buttons.addNew" />
								</html:link>
							</logic:notEqual></a></td>
						</tr>

						<logic:notEqual name="targetLoginName" value=''>
							<tr id="autoDiv">
									<td align="center" class="black_ar">&nbsp;</td>
									<td styleId="abc" align="left" class="black_ar"><bean:message
										key="app.wustlkey" /></td>
									<td width="22%" align="left" class="black_ar">
										<label>
											${requestScope.userForm.targetLoginName}
										</label>
									</td>
									<td align="left" class="black_ar">&nbsp;</td>
							</tr>
						</logic:notEqual>
					</table>
					</td>
				</tr>
				<logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'>
						<tr>
							<td colspan="2" class="bottomtd"></td>
						</tr>
						<tr>
							<td colspan="2" align="left" class="tr_bg_blue1"><span
								class="blue_ar_b">&nbsp;<bean:message
								key="assignPrivileges.privilegeDetails" /></span></td>
							<td colspan="1" align="right" class="tr_bg_blue1">&nbsp;</td>
						</tr>
					<tr>
						<td colspan="2">
						<logic:notEqual name="pageOf"
							value='${requestScope.pageOfUserProfile}'>
						<!--	<div id="privilegesDiv" style="display:none">  -->
							<table width="100%" border="0" cellpadding="3" cellspacing="0">
								<tr>
									<td width="1%" align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td width="17%" align="left" class="black_ar"><label><bean:message
										key="user.role" /></label></td>
									<td width="19%" class="black_ar"><html:select
										property="role" styleId="roleIds"
										styleClass="formFieldSizedNew"
										onchange="preEventsOnRoleSelect(this,'cpCheckId')">
										<html:options collection="roleList" labelProperty="name"
											property="value" />
									</html:select></td>
									<td width="13%" align="left" valign="top">&nbsp;</td>
									<td width="1%" align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td width="17%" align="left" class="black_ar"><bean:message
										key="assignPrivileges.site(s)" /></td>
									<td rowspan="2" width="19%" align="left" class="black_ar">

									<c:if test='${requestScope.flagForSARole==true}'>
										<html:select property="siteIds" styleId="siteIds" size="4"
											styleClass="formFieldSizedNew" multiple="multiple"
											onchange="getCPsForThisSites(this)" disabled="true">
											<html:options collection="siteList" labelProperty="name"
												property="value" />
										</html:select>
									</c:if>
									<c:if test='${requestScope.flagForSARole!=true}'>
										<html:select property="siteIds" styleId="siteIds" size="4"
											styleClass="formFieldSizedNew" multiple="multiple"
											onchange="getCPsForThisSites(this)">
											<html:options collection="siteList" labelProperty="name"
												property="value" />
										</html:select>
									</c:if>
									</td>
									<td width="13%" align="left" valign="top" class="black_ar_t">&nbsp;</td>
								</tr>
								<tr><td colspan="4" rowspan="1">&nbsp;</td></tr>
								<tr>
									<td width="1%" align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td width="17%" align="left" class="black_ar"><bean:message
										key="assignPrivileges.collectionProtocol(s)" /></td>
									<td width="19%" class="black_ar"><input type="checkbox"
										checked="checked" id="cpCheckId"
										onclick="eventOnChkBoxClick('cpCheckId','cpIds','siteIds','roleIds')"
										onchange="eventOnChkBoxClick('cpCheckId','cpIds','siteIds','roleIds')"/><bean:message
										key="assignPrivileges.allCurrentAndFuture" /></td>
									<td width="13%" class="black_ar">&nbsp;</td>
									<td width="1%" align="center" class="black_ar"><span
										class="blue_ar_b"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></span></td>
									<td width="17%" align="left" class="black_ar"><bean:message
										key="assignPrivileges.privilege(s)" /></td>
									<td rowspan="2" class="black_ar_t"
										valign="top"><c:if test='${requestScope.flagForSARole==true}'>

										<select class="formFieldSizedNew" id="actionIds" size="5"
											onchange="getCustomRole('roleIds',this)" multiple="multiple"
											disabled="true">
									</c:if> <c:if test='${requestScope.flagForSARole!=true}'>
										<select class="formFieldSizedNew" id="actionIds" size="4"
											onchange="getCustomRole('roleIds',this)" multiple="multiple">
									</c:if> <%
											String actionName = "";
											String actionValue = "";
											if ((actionList != null) && !(actionList.isEmpty())) {
												for (int i = 0; i < actionList.size(); i++) {
													actionName = ""
													+ ((NameValueBean) actionList.get(i)).getName();
													actionValue = ""
													+ ((NameValueBean) actionList.get(i)).getValue();
										%>
									<option value="<%=actionValue%>"><%=actionName%></option>
									<%
											}
											}
										%> </select></td>
									<td width="13%" align="left" valign="top">&nbsp;</td>
								</tr>
								<tr>
									<td width="1%" align="center" class="black_ar">&nbsp;</td>
									<td width="17%" align="left" class="black_ar"></td>
									<td width="19%" class="black_ar"><select
										class="formFielDisabled" id="cpIds" size="4"
										multiple="multiple"
										onchange="getActionsForThisCPs(this.id,'siteIds','roleIds','cpCheckId')"
										disabled="true">

										<%
											String cpActionName = "";
											String cpActionValue = "";
											if ((cpList != null) && !(cpList.isEmpty())) {
												for (int i = 0; i < cpList.size(); i++) {
													cpActionName = ""
													+ ((NameValueBean) cpList.get(i)).getName();
													cpActionValue = ""
													+ ((NameValueBean) cpList.get(i)).getValue();
										%>
										<option value="<%=cpActionValue%>"><%=cpActionName%></option>
										<%
											}
											}
										%>
									</select></td>
								</tr>
								<tr>
									<c:set var="functionName1">getUserPrivilegeSummary('<c:out
											value="${requestScope.operation}" />')</c:set>
									<jsp:useBean id="functionName1" type="java.lang.String" />

									<td colspan="7" width="97%" align="right" >&nbsp;
									<html:button property="addKeyValue" styleClass="black_ar"
										onclick="<%=functionName1%>">
										<bean:message key="buttons.addPrivileges" />
									</html:button></td>
									<td width="3%" >&nbsp;</td>
								</tr>
								<tr class="td_color_F7F7F7">
									<td height="25" colspan="8" align="left"><span
										class="blue_ar_b">&nbsp;<bean:message key="app.summary" /></span></td>
								</tr>
								<tr>
									<td colspan="8"></logic:notEqual>
									<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<tr class="tableheading">
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfUserProfile}'>
												<td width="6%" class="black_ar_b"><label for="delete"
													align="left"><bean:message key="app.select" /></label></td>
											</logic:notEqual>

											<td width="27%" class="black_ar_b" align="left"><bean:message
												key="assignPrivileges.site(s)" /></td>
											<td width="22%" class="black_ar_b" align="left"><bean:message
												key="assignPrivileges.collectionProtocol(s)" /></td>

											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfUserProfile}'>
												<td width="40%" class="black_ar_b" align="left"><bean:message
													key="app.Privileges" /></td>

												<td width="5%" class="black_ar_b">&nbsp;</td>
											</logic:notEqual>
											<logic:equal name="pageOf"
												value='${requestScope.pageOfUserProfile}'>
												<td colspan="3" width="51%" class="black_ar_b" align="left"><bean:message
													key="app.Privileges" /></td>
											</logic:equal>
										</tr>
										<tr>
											<td colspan="5" width="100%">
											<div
												style="height: 80px; background-color: #ffffff;overflow: auto;">
											<logic:notEqual name="pageOf"
												value='${requestScope.pageOfUserProfile}'>
												<table border="0" width="100%" cellspacing="0"
													cellpadding="0">
													</logic:notEqual>
													<logic:equal name="pageOf"
														value='${requestScope.pageOfUserProfile}'>
														<table border="0" width="100%" cellspacing="0"
															cellpadding="3">
															</logic:equal>
															<tbody id="summaryTableId">
																<%
																			List list = (List) request
																			.getAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD);
																	if (list != null && !list.isEmpty()) {
																		int valueCounter = list.size();
																		for (int i = 0; i < valueCounter; i++) {
																			String chkName = "chk_" + i;
																			String[] arr = (String[]) (list.get(i));
																%>
																<%
																if (i % 2 != 0) {
																%>
																<tr id="<%=arr[4]%>" class="tabletd1">
																	<%
																	} else {
																	%>

																<tr id="<%=arr[4]%>">
																	<%
																	}
																	%>

																	<logic:notEqual name="pageOf"
																		value='${requestScope.pageOfUserProfile}'>
																		<td width="6%" class="black_ar"><input
																			type='checkbox' name='<%=chkName %>'
																			id='<%=chkName %>'
																			onclick="enableDeleteButton('summaryTableId','deleteButtonId')" /></td>
																	</logic:notEqual>
																	<td width="27%" class="black_ar"
																		onmouseover="Tip('<%=arr[1]%>',WIDTH,200)">
																	<%
																			if (arr[1].length() > 20) {
																			arr[1] = arr[1].substring(0, 17) + "...";
																				}
																	%><span><%=arr[1]%></span></td>

																	<td width="22%" class="black_ar"><span><%=arr[0]%></span></td>

																	<logic:notEqual name="pageOf"
																		value='${requestScope.pageOfUserProfile}'>

																		<td width="40%" class="black_ar"
																			onmouseover="Tip('<%=arr[3]%>',WIDTH,200)">
																		<%
																				if (arr[3].length() > 60) {
																				arr[3] = arr[3].substring(0, 57) + "...";
																					}
																		%> <span><%=arr[3]%></span></td>


																		<td width="5%"><a href='#' class="view"
																			'${requestScope.readOnlyForPrivOnEdit}'
																onclick="editRow('<%=arr[4]%>')">
																		<bean:message key="app.edit" /> </a></td>


																	</logic:notEqual>

																	<logic:equal name="pageOf"
																		value='${requestScope.pageOfUserProfile}'>
																		<td colspan="2" width="51%" class="black_ar"
																			onmouseover="Tip('<%=arr[3]%>',WIDTH,200)">
																		<%
																				if (arr[3].length() > 65) {
																				arr[3] = arr[3].substring(0, 62) + "...";
																					}
																		%> <span><%=arr[3]%></span></td>
																	</logic:equal>
																</tr>
																<%
																	}
																	}
																%>

															</tbody>
														</table>
														</div>
														</td>
														</tr>
														<logic:notEqual name="pageOf"
															value='${requestScope.pageOfUserProfile}'>
															<tr>
																<td class="black_ar" colspan="7"><html:button
																	property="deleteButton" styleId="deleteButtonId"
																	styleClass="black_ar"
																	onclick="deleteCheckedRows(document.getElementById('operation').value,'summaryTableId',this.id)"
																	disabled="true">
																	<bean:message key="buttons.delete" />
																</html:button></td>
															</tr>
														</logic:notEqual>
												</table>
												<logic:notEqual name="pageOf"
													value='${requestScope.pageOfUserProfile}'>
											</td>
										</tr>
									</table>

								<!--	</div>-->
									</logic:notEqual></td>
								</tr>
								</logic:notEqual>

								<tr>
									<td colspan="2">
									<table width="100%" border="0" cellpadding="3" cellspacing="0">
										<logic:equal name="pageOf"
											value='${requestScope.pageOfUserAdmin}'>
											<logic:equal name="operation"
												value='${requestScope.editforJSP}'>
												<tr>
													<td width="1%" height="25" align="left" class="black_ar_t">
													<span class="blue_ar_b"><img
														src="images/uIEnhancementImages/star.gif" alt="Mandatory"
														width="6" height="6" hspace="0" vspace="0" /></span></td>
													<td width="17%" align="left" class="black_ar_t"><label
														for="activityStatus"> <bean:message
														key="user.activityStatus" /> </label></td>
													<td width="19%" align="left" nowrap class="black_ar_t"><autocomplete:AutoCompleteTag
														property="activityStatus"
														optionsList='${requestScope.activityStatusList}'
														initialValue='${userForm.activityStatus}'
														styleClass="black_ar" size="27" /></td>
													<td width="13%" align="left">&nbsp;</td>
													<td width="1%" height="25" align="left" class="black_ar_t">&nbsp;</td>
													<td width="17%" align="left" class="black_ar_t"><label
														for="comments"> <bean:message key="user.comments" />
													</label></td>
													<td width="19%" colspan="1" align="left" valign="top"><label>
													<html:textarea styleClass="black_ar_s" cols="33" rows="3"
														styleId="comments" property="comments" /> </label></td>
													<td width="13%" align="left">&nbsp;</td>
												</tr>
											</logic:equal>
										</logic:equal>

										<logic:equal name="pageOf" value='${pageOfApproveUser}'>
											<tr>
												<td width="1%" height="25" align="left" class="black_ar_t">
												<span class="blue_ar_b"><img
													src="images/uIEnhancementImages/star.gif" alt="Mandatory"
													width="6" height="6" hspace="0" vspace="0" /></span></td>
												<td width="17%" align="left" class="black_ar_t"><label
													for="status"> <bean:message
													key="user.approveOperation" /> </label></td>

												<td width="19%" colspan="1" align="left" valign="top"
													class="black_ar_t"><html:select property="status"
													styleClass="formFieldSizedNew" styleId="status" size="1"
													onmouseover="showTip(this.id)"
													onmouseout="hideTip(this.id)">
													<html:options name="statusList" labelName="statusList" />
												</html:select></td>
												<td width="13%" align="left">&nbsp;</td>

												<td width="1%" height="25" align="left" class="black_ar_t">&nbsp;</td>
												<td width="17%" align="left" class="black_ar_t"><label
													for="comments"> <bean:message key="user.comments" />
												</label></td>
												<td width="19%" colspan="1" align="left" valign="top"><label>
												<html:textarea styleClass="black_ar_s" cols="33" rows="3"
													styleId="comments" property="comments" /> </label></td>
												<td width="13%" align="left">&nbsp;</td>
											</tr>
										</logic:equal>
										<logic:equal name="pageOf"
											value='${requestScope.pageOfUserAdmin}'>
											<logic:notEqual name="operation"
												value='${requestScope.editforJSP}'>
												<tr>
													<td width="1%" height="25" align="left" class="black_ar_t">&nbsp;</td>
													<td width="17%" align="left" class="black_ar_t"><bean:message
														key="user.comments" /></td>
													<td width="82%" colspan="6" align="left" valign="top"><label>
													<html:textarea styleClass="black_ar_s" cols="90" rows="3"
														styleId="comments" property="comments" /> </label></td>

												</tr>
											</logic:notEqual>
										</logic:equal>
									</table>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="bottomtd"></td>
								</tr>
								<tr>
									<td colspan="2" class="buttonbg"><html:submit
										styleClass="blue_ar_b">
										<bean:message key="buttons.submit" />
									</html:submit>
<!-- &nbsp;|<logic:notEqual name="pageOf"
										value='${requestScope.pageOfSignUp}'>
										<logic:notEqual parameter="openInCPFrame" value='yes'>
											<html:link onclick="closeUserWindow()"
												page="/ManageAdministrativeData.do" styleClass="cancellink">
												<bean:message key="buttons.cancel" />
											</html:link>
										</logic:notEqual>
										<logic:equal parameter="openInCPFrame" value='yes'>
											<html:link
												page="/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol"
												styleClass="cancellink">
												<bean:message key="buttons.cancel" />
											</html:link>
										</logic:equal>
									</logic:notEqual> <logic:equal name="pageOf"
										value='${requestScope.pageOfSignUp}'>
										<a href="#" class="cancellink"><html:link
											page="/RedirectHome.do" styleClass="cancellink">
											<bean:message key="buttons.cancel" />
										</html:link></a>
									</logic:equal>-->
									</td>
								</tr>
							</table></td>
					</tr>
					</html:form>
			</table>
</body>
