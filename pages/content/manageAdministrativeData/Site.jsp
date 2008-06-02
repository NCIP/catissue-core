<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SiteForm"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%@ include file="/pages/content/common/AdminCommonCode.jsp"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="newMaintable">
	<html:form action='${requestScope.formName}'>
		<html:hidden property="operation" />
		<html:hidden property="submittedFor" />
		<html:hidden property="id" />
		<html:hidden property="onSubmit" />
		<html:hidden property="pageOf" />
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="td_color_bfdcf3">
				<tr>
					<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="whitetable_bg">
						<tr>
							<td width="100%" colspan="2
            " valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">

								<tr>
									<td colspan="3" valign="top" class="td_color_bfdcf3">
									<table width="10%" border="0" cellpadding="0" cellspacing="0"
										background="images/uIEnhancementImages/table_title_bg.gif">
										<tr>
											<td width="74%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
											<bean:message key="site.header" /> </span></td>
											<td width="26%" align="right"><img
												src="images/uIEnhancementImages/table_title_corner2.gif"
												width="31" height="24" /></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
									<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
									<td width="90%" valign="bottom" class="td_color_bfdcf3"
										style="padding-top:4px;">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td width="4%" class="td_tab_bg">&nbsp;</td>
											<!-- for tabs selection -->
											<logic:equal name="operation" value="add">
												<td width="6%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif"><img
													src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add"
													width="57" height="22" /></td>
												<td width="6%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif"><html:link
													page="/SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site&menuSelected=5">
													<img src="images/uIEnhancementImages/tab_edit_notSelected.jpg"
														alt="Edit" width="59" height="22" border="0" />
												</html:link></td>
												<td width="15%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
											</logic:equal>
											<logic:equal name="operation" value="edit">
												<td width="6%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif"><html:link
													page="/Site.do?operation=add&pageOf=pageOfSite&menuSelected=5">
													<img src="images/uIEnhancementImages/tab_add_notSelected.jpg"
														alt="Add" width="57" height="22" border="0" />
												</html:link></td>
												<td width="6%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif"><img
													src="images/uIEnhancementImages/tab_edit_selected.jpg"
													alt="Edit" width="59" height="22" /></td>
												<td width="15%" valign="bottom"
													background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
											</logic:equal>
											<td width="65%" valign="bottom" class="td_tab_bg">&nbsp;</td>
											<td width="1%" align="left" valign="bottom"
												class="td_color_bfdcf3">&nbsp;</td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="td_color_bfdcf3"
								style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
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
									<td class="tr_bg_blue1" height="20" colspan="3"><span
										class="blue_ar_b"> <logic:equal name="operation"
										value='${requestScope.operationAdd}'>
										<bean:message key="site.title" />
									</logic:equal> <logic:equal name="operation"
										value='${requestScope.operationEdit}'>
										<bean:message key="site.editTitle" />
									</logic:equal></span></td>
									<td align="right" class="tr_bg_blue1">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="3" align="left"
										style="padding-top:10px; padding-bottom:15px;">
									<div id="part_det">
									<table width="100%" border="0" cellpadding="3" cellspacing="0">
										<tr>
											<td width="1%" align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td width="20%" align="left" class="black_ar"><label
												for="name"> <bean:message key="site.name" /> </label></td>
											<td width="15%" align="left"><label> <html:text
												styleClass="black_ar" maxlength="255" size="30"
												styleId="name" property="name" /></label></td>
											<td width="19%" align="left">&nbsp;</td>
											<td width="1%" align="left"><span class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td width="20%" align="left" class="black_ar"><label
												for="type"> <bean:message key="site.type" /> </label></td>
											<td width="15%" align="left" class="black_ar"><autocomplete:AutoCompleteTag
												property="type" optionsList='${siteTypeList}'
												initialValue='${siteForm.type}'
												styleClass="formFieldSized12" /></td>
											<td width="9%" align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label
												for="coordinator"> <bean:message
												key="site.coordinator" /> </label></td>
											<td align="left" class="black_new"><html:select
												property="coordinatorId" styleClass="formFieldSizedNew"
												styleId="coordinatorId" size="1"
												onchange="onCoordinatorChange()"
												onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
												<html:options collection="userList" labelProperty="name"
													property="value" />
											</html:select></td>
											<td align="left">&nbsp; <span class="smalllink">
											<html:link href="#" styleId="newCoordinator"
												styleClass="blue_ar_s_b"
												onclick="addNewAction('SiteAddNew.do?addNewForwardTo=coordinator&forwardTo=site&addNewFor=coordinator')">
												<bean:message key="buttons.addNew" />
											</html:link></span></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><label
												for="emailAddress"> <bean:message
												key="site.emailAddress" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="255" size="30" styleId="emailAddress"
												property="emailAddress" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="street">
											<bean:message key="site.street" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="street" property="street" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="city">
											<bean:message key="site.city" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="city" property="city" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="state">
											<bean:message key="site.state" /> </label></td>
											<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
												property="state" optionsList='${stateList}'
												initialValue='${siteForm.state}'
												styleClass="formFieldSized12" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><span
												class="blue_ar_b"><img
												src="images/uIEnhancementImages/star.gif" alt="Mandatory"
												width="6" height="6" hspace="0" vspace="0" /></span></td>
											<td align="left" class="black_ar"><label for="zipCode">
											<bean:message key="site.zipCode" /> </label></td>
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
											<bean:message key="site.country" /> </label></td>
											<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
												property="country" optionsList='${countryList}'
												initialValue='${siteForm.country}'
												styleClass="formFieldSized12" /></td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><label
												for="phoneNumber"> <bean:message
												key="site.phoneNumber" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="phoneNumber"
												property="phoneNumber" /></td>
											<td align="left" valign="top">&nbsp;</td>
										</tr>
										<tr>
											<td align="left" class="black_ar">&nbsp;</td>
											<td align="left" class="black_ar"><label for="faxNumber">
											<bean:message key="site.faxNumber" /> </label></td>
											<td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="faxNumber"
												property="faxNumber" /></td>

											<td align="left" class="black_ar">&nbsp;</td>
											<logic:equal
												name='${requestScope.operationForActivityStatus}'
												value='${requestScope.operationEdit}'>
												<td align="left" class="black_ar"><span
													class="blue_ar_b"><img
													src="images/uIEnhancementImages/star.gif" alt="Mandatory"
													width="6" height="6" hspace="0" vspace="0" /></span></td>
												<td align="left" class="black_ar"><label
													for="activityStatus"> <bean:message
													key="site.activityStatus" /> </label></td>
												<td align="left" class="black_ar"><autocomplete:AutoCompleteTag
													property="activityStatus"
													optionsList='${activityStatusList}'
													initialValue='${siteForm.activityStatus}'
													styleClass="formFieldSized12" onChange='${strCheckStatus}' />
												</td>
												<td align="left" valign="top">&nbsp;</td>
											</logic:equal>
											<logic:notEqual
												name='${requestScope.operationForActivityStatus}'
												value='${requestScope.operationEdit}'>

												<td align="left" class="black_ar">&nbsp;</td>
												<td align="left" class="black_ar"></td>
												<td align="left">&nbsp;</td>
												<td align="left">&nbsp;</td>
											</logic:notEqual>
										</tr>
									</table>
									</div>
									</td>
								</tr>
								<tr class="td_color_F7F7F7">
									<td colspan="4" class="buttonbg"><html:submit
										styleClass="blue_ar_b">
										<bean:message key="buttons.submit" />
									</html:submit> &nbsp;| <span class="cancellink"><html:link
										page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
										<bean:message key="buttons.cancel" />
									</html:link></span></td>
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
