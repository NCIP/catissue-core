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
</head>

<body>

<script>
function disableIdpDetails(disableStatus)
{
	if(disableStatus==true)
	{
		document.getElementById("enabledTargetIdpList").style.display="none";
		document.getElementById("disabledTargetIdpList").style.display="block";

	}
	else
	{
		document.getElementById("enabledTargetIdpList").style.display="block";
		document.getElementById("disabledTargetIdpList").style.display="none";
	}
	document.getElementById("targetLoginName").disabled=disableStatus;
	document.getElementById("targetPassword").disabled=disableStatus;
	showHide('idpDetails');
}

</script>
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
		<html:hidden property="pageOf" />
		<html:hidden property="id" />
		<html:hidden property="csmUserId" />
		<html:hidden property='${requestScope.redirectTo}' />
		<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
			<html:hidden property="activityStatus" />
		</logic:equal>

		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="app.signup" /></span></td>
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
			</logic:equal>
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">

				<tr>
					<td colspan="2" align="left" class="bottomtd"><%@ include
						file="/pages/content/common/ActionErrors.jsp"%>
					</td>
				</tr>

				<tr>
					<td colspan="2" class="messagetexterror">
					<div id="errorMessImgDiv" style="display: none">

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
					<div id="errorMess" style="display: none"></div>
					</td>
				</tr>
				<logic:notEmpty name="idpsList">
					<tr>
						<td colspan="2" align="left">
						<table width="100%" border="0" cellpadding="3" cellspacing="0">

							<tr>
								<td width="1%" align="center" class="black_ar"><span
									class="blue_ar_b"><img
									src="images/uIEnhancementImages/star.gif" alt="Mandatory"
									width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td width="35%" align="left" class="black_ar_b_13"><bean:message key="source.idp.question"/><br />
									<div id="idpNames">
									<logic:iterate id="idpDetails" name="idpsList">
										<bean:write name="idpDetails" property="name"/>,
									</logic:iterate>
									</div>
								</td>
								<td align="left"><logic:equal name="idpSelection"
									value="yes">
									<input type="radio" name="idpSelection" value="yes" checked onclick="disableIdpDetails(false)"/>
								</logic:equal> <logic:notEqual name="idpSelection" value="yes">
									<input type="radio" name="idpSelection" value="yes" onclick="disableIdpDetails(false)"/>
								</logic:notEqual> <span class="black_ar"><bean:message key="user.yes"/></span> &nbsp; <logic:equal
									name="idpSelection" value="no">
									<input type="radio" name="idpSelection" value="no" checked onclick="disableIdpDetails(true)"/>
								</logic:equal> <logic:notEqual name="idpSelection" value="no">
									<input type="radio" name="idpSelection" value="no" onclick="disableIdpDetails(true)"/>
								</logic:notEqual><span class="black_ar" /><bean:message key="user.no"/></span></td>
							</tr>
							<tr>
								<td colspan="3" class="bottomtd"></td>
							</tr>
						</table>
					</tr>
						<tr>
						<td width="96%" align="left" class="tr_bg_blue1"><span
							class="blue_ar_b">&nbsp;<bean:message key="idp.detials"/></span></td>
						<td width="4%" align="right" class="tr_bg_blue1"><div style="display:none;"><a
							id="imgArrow_idpDetails"><img
							src="images/uIEnhancementImages/up_arrow.gif" width="80"
							height="9" hspace="10" border="0" /></a></div></td>
					</tr>
					<tr>
						<td colspan="2" align="left" class="showhide">
						<div id="idpDetails" style="display:block" >
						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr>
								<td width="1%" align="center" class="black_ar"><span
									class="blue_ar_b"><img
									src="images/uIEnhancementImages/star.gif" alt="Mandatory"
									width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td width="17%" align="left" class="black_ar"><bean:message key="idp.selection.text"/></td>
								<td width="19%" nowrap class="black_ar">
									<div id="enabledTargetIdpList">
										<autocomplete:AutoCompleteTag
										property="targetIdp" optionsList='${requestScope.idpsList}'
										initialValue='${userForm.targetIdp}' styleClass="black_ar"
										staticField="false" size="27"/>
									</div>
									<div id="disabledTargetIdpList">
									<autocomplete:AutoCompleteTag
									property="targetIdp" optionsList='${requestScope.idpsList}'
									initialValue='${userForm.targetIdp}' styleClass="black_ar"
									staticField="false" size="27" disabled="true"/>
									</div>
								</td>
								<td width="13%" align="left">&nbsp;</td>
								<td width="1%" align="center">&nbsp;</td>
								<td width="17%" align="left">&nbsp;</td>
								<td width="19%" align="left">&nbsp;</td>
								<td width="13%" align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="8" class="bottomtd"></td>
							</tr>
							<tr>
								<td width="1%" align="center" class="black_ar"><span
									class="blue_ar_b"><img
									src="images/uIEnhancementImages/star.gif" alt="Mandatory"
									width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td width="17%" align="left" class="black_ar"><bean:message key="user.loginName"/></td>
								<td width="19%" align="left"><html:text
									styleClass="black_ar" maxlength="255" size="30"
									styleId="targetLoginName" property="targetLoginName"
									readonly='${requestScope.readOnlyEmail}' /></td>
								<td width="13%" align="left">&nbsp;</td>
								<td width="1%" align="center"><span class="blue_ar_b"><img
									src="images/uIEnhancementImages/star.gif" alt="Mandatory"
									width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td width="17%" align="left"><label for="targetPassword"
									class="black_ar"><bean:message key="user.password"/></label></td>
								<td width="19%" align="left"><html:password
									styleClass="black_ar" maxlength="255" size="30"
									styleId="targetPassword" property="targetPassword"
									readonly='${requestScope.readOnlyEmail}' /></td>
								<td width="13%" align="left" valign="top">&nbsp;</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
				</logic:notEmpty>
				<tr>
					<td colspan="2" align="left" class="tr_bg_blue1"><span
						class="blue_ar_b">&nbsp; <bean:message
						key="user.details.title" /> </span></td>
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
								readonly='${requestScope.readOnlyEmail}' /></td>
							<td width="13%" align="left">&nbsp;</td>
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
						</tr>
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
							<td align="left">&nbsp;</td>
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
							<td align="left">&nbsp;</td>
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
							<td align="left">&nbsp;</td>
						</tr>
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
					</html:submit></td>
				</tr>
			</table>
			</td>
		</tr>
	</html:form>
</table>
</body>
<script>
<logic:equal name="idpSelection" value="no">
	disableIdpDetails(true);
</logic:equal>
<logic:equal name="idpSelection" value="yes">
	disableIdpDetails(false);
	showHide('idpDetails');
</logic:equal>

function updateIdpNamesContents()
{
	var content=document.getElementById("idpNames").innerHTML;
	content=content.trim();
	document.getElementById("idpNames").innerHTML=content.substring(0,content.length-1)+".";
}
<logic:notEmpty name="idpsList">
	updateIdpNamesContents();
</logic:notEmpty>
</script>