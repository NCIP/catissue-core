<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>

<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.*"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script>
<script type="text/javascript" src="jss/caTissueSuite.js"></script>
<script>
function viewSummary()
{
	var action="GenericSpecimenSummary.do?Event_Id=dummyId";
	document.forms[0].action=action;
	document.forms[0].submit();
}
function updateCPTree()
{ 			  window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
}
</script>

<script language="JavaScript">
	var errorMessForRole='${requestScope.errorMessageForRole}';
	var errorMessForSite='${requestScope.errorMessageForSite}';
	var errorMessForUser='${requestScope.errorMessageForUser}';
	var errorMessForCP='${requestScope.errorMessageForCP}';
	var errorMessForPriv='${requestScope.errorMessageForPrivilege}';
</script>
<%
	//Object obj=  request.getAttribute(Constants.ACTIONLIST);
	//String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
	String operation = (String) request
			.getAttribute(Constants.OPERATION);
	List siteList = (List) request.getAttribute(Constants.SITELIST);
	List userList = (List) request.getAttribute(Constants.USERLIST);
	List roleList = (List) request.getAttribute(Constants.ROLELIST);
	List actionList = (List) request.getAttribute(Constants.ACTIONLIST);
%>

<body>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script type="text/javascript" src="jss/queryModule.js"></script>

<html:form
	action="DefineEvents.do?Event_Id=dummyId&pageOf=submitSpecimen&operation=${requestScope.operation}"
	styleId="CollectionProtocolForm">
	<table summary="" cellpadding="0" cellspacing="0" border="0"
		style="padding-left:0;padding-right:0;" width="100%">
		<html:hidden property="shortTitle" />
		<html:hidden property="startDate" />
		<html:hidden property="title" />
		<html:hidden property="principalInvestigatorId" />
    	<html:hidden property="irbID" />

    	<html:hidden property="specimenLabelFormat" />
    	<html:hidden property="derivativeLabelFormat" />
    	<html:hidden property="aliquotLabelFormat" />
		<html:hidden property="descriptionURL" />
		<html:hidden property="enrollment" />
		<html:hidden property="endDate" />
		<html:hidden property="coordinatorIds" />
		<html:hidden property="outerCounter" />
		<html:hidden property="aliqoutInSameContainer" />
		<html:hidden property="unsignedConsentURLName" />
		<html:hidden property="consentTierCounter" />
		<html:hidden property="consentWaived" />
		<html:hidden property="pageOf" />

		<html:hidden property="sequenceNumber" />
		<html:hidden property="type" />
		<html:hidden property="studyCalendarEventPoint" />
		<html:hidden property="parentCollectionProtocolId" />

		<logic:notEqual name="noOfConsents" value="0">
			<c:forEach var="counter" begin="0"
				end='${requestScope.noOfConsents -1}' step="1">
				<html:hidden
					property="consentValue(ConsentBean:${counter}_statement)" />
				<html:hidden
					property="consentValue(ConsentBean:${counter}_consentTierID)" />
			</c:forEach>
		</logic:notEqual>



		<tr>
			<td valign="bottom" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="10%" valign="bottom" id="collectionProtocolTab"><a
						href="CollectionProtocol.do?pageOf=pageOfCollectionProtocol&invokeFunction=invokeFunction&operation=${requestScope.operation}">
					<img src="images/uIEnhancementImages/cp_details1.gif"
						alt="Collection Protocol Details" width="174" height="20"
						border="0" /></a></td>
					<td width="10%" valign="bottom" id="consentTab"><a
						href="CollectionProtocol.do?pageOf=pageOfCollectionProtocol&invokeFunction=invokeFunction&operation=${requestScope.operation}&tabSel=consentTab"><img
						src="images/uIEnhancementImages/cp_consents1.gif" alt="Consents"
						width="94" height="20" border="0" /></a></td>
					<td width="10%" valign="bottom" id="consentTab"><a> <img
						src="images/uIEnhancementImages/cp_privileges.gif"
						alt="Privileges" width="94" height="20" border="0"
						onclick="showAssignPrivilegePage()"></a></td>
					<td width="100%" valign="bottom" class="cp_tabbg">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>

		<tr>
			<td colspan="2" class="cp_tabtable">
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				id="table1" width="100%">

				<tr>
					<td colspan="2" align="left"
						style="padding-top:5px; padding-bottom:10px;">
					<table width="100%" border="0" cellpadding="4" cellspacing="0"
						style="background-color:#FFFFFF">
						<div id="editMessageDivId"
							style="display:none; padding-bottom:10px"></div>
						<tr>
							<td colspan="7" class="messagetexterror">
							<div id="errorMessImgDiv" style="display:none">
							<table>
								<tr>
									<td valign="top"><img
										src="images/uIEnhancementImages/alert-icon.gif"
										alt="error messages" width="16" vspace="0" hspace="0"
										height="18" valign="top"></td>
									<td class="messagetexterror" align="left"><strong><bean:message
										key="errors.title" /></strong></td>
								</tr>
							</table>
							</div>
							<div id="errorMess" style="display:none"></div>
							</td>
						</tr>
						<tr class="td_color_F7F7F7">
							<td colspan="7" align="left" class="bottomtd"></td>
						</tr>
						<tr>
							<td width="6%" align="right" class="black_ar_t"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" align="absmiddle" />&nbsp;<img
								src="images/uIEnhancementImages/number_1.gif" alt="Number 1"
								width="18" height="18" align="absmiddle"></td>
							<td width="7%" align="left" class="black_ar_t"><bean:message
								key="Site.header" /></td>
							<td width="28%" align="left"><select
								class="formFieldSizedNew" id="siteIds" size="4"
								multiple="multiple" onchange="getUsersForThisSites(this)">
								<%
									String siteName = "";
									String siteValue = "";
									if ((siteList != null) && !(siteList.isEmpty())) {
										for (int i = 0; i < siteList.size(); i++) {
											siteName = "" + ((NameValueBean) siteList.get(i)).getName();
											siteValue = ""
											+ ((NameValueBean) siteList.get(i)).getValue();
								%>
								<option value="<%=siteValue%>"
									onmouseover="Tip('<%=siteName%>',WIDTH,200)"><%=siteName%></option>
								<%
									}
									}
								%>
							</select></td>
							<td width="15%" class="black_ar_t"><input type="checkbox"
								 id="customizeChkId"
								onclick="onChkBoxClickForCP('customizeChkId','userIds','siteIds','roleIds','actionIds')"><bean:message
								key="assignPrivileges.customize" /></td>
							<td width="6%" align="right" class="black_ar_t"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" align="absmiddle" />&nbsp;<img
								src="images/uIEnhancementImages/number_2.gif" alt="Number 2"
								width="18" height="18" align="absmiddle"></td>

							<td width="10%" align="left" class="black_ar_t"><bean:message
								key="User.header" /></td>
							<td width="28%"><select class="formFieldSizedNew"
								id="userIds" size="4" multiple="multiple" disabled="true">
								<%
									String userName = "";
									String userValue = "";
									if ((userList != null) && !(userList.isEmpty())) {
										for (int i = 0; i < userList.size(); i++) {
											userName = "" + ((NameValueBean) userList.get(i)).getName();
											userValue = ""
											+ ((NameValueBean) userList.get(i)).getValue();
								%>
								<option value="<%=userValue%>"
									onmouseover="Tip('<%=userName%>',WIDTH,200)"><%=userName%></option>
								<%
									}
									}
								%>
							</select></td>
						</tr>

						<tr>
							<td align="right" class="black_ar_t"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" align="absmiddle" />&nbsp;<img
								src="images/uIEnhancementImages/number_3.gif" alt="Number 3"
								width="18" height="18" align="absmiddle"></td>
							<td align="left" class="black_ar_t"><label
								for="coordinatorIds"><bean:message
								key="user.role" /></label></td>
							<td align="left" class="black_ar_t"><select
								class="formFieldSizedNew" id="roleIds"
								onchange="getActionsForThisRole(this,'siteIds','cpIds')"
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
								<%
									String roleName = "";
									String roleValue = "";
									if ((roleList != null) && !(roleList.isEmpty())) {
										for (int i = 0; i < roleList.size(); i++) {
											roleName = "" + ((NameValueBean) roleList.get(i)).getName();
											roleValue = ""
											+ ((NameValueBean) roleList.get(i)).getValue();
											String selected = "";
											if (roleValue.equals("0")) {
										selected = "SELECTED";
											}
								%>
								<option value="<%=roleValue%>"><%=roleName%></option>
								<%
									}
									}
								%>
							</select></td>
							<td width="10%">&nbsp;</td>
							<td align="right" class="black_ar_t"><img
								src="images/uIEnhancementImages/star.gif" alt="Mandatory"
								width="6" height="6" hspace="0" vspace="0" align="absmiddle" />&nbsp;<img
								src="images/uIEnhancementImages/number_4.gif" alt="Number 4"
								width="18" height="18" align="absmiddle"></td>
							<td align="left" class="black_ar_t"><bean:message
								key="app.Privileges" /></td>
							<td><select class="formFieldSizedNew" id="actionIds"
								size="4" onchange="getCustomRole('roleIds',this)"
								multiple="multiple" disabled="true">
								<%
									String actionName = "";
									String actionValue = "";
									if ((actionList != null) && !(actionList.isEmpty())) {
										for (int i = 0; i < actionList.size(); i++) {
											actionName = ""
											+ ((NameValueBean) actionList.get(i)).getName();
											actionValue = ""
											+ ((NameValueBean) actionList.get(i)).getValue();
								%>
								<option value="<%=actionValue%>"
									onmouseover="Tip('<%=actionName%>',WIDTH,200)"><%=actionName%></option>
								<%
									}
									}
								%>
							</select></td>
						</tr>

					</table>
					</td>
				</tr>
				<tr>
					<c:set var="functionName1">getUserPrivilegeSummary('<c:out
							value="${requestScope.operation}" />')</c:set>
					<jsp:useBean id="functionName1" type="java.lang.String" />

					<td width="97%" align="right" class="dividerline">&nbsp;<html:button
						property="addKeyValue" styleClass="black_ar"
						onclick="<%=functionName1%>">
						<bean:message key="buttons.addPrivileges" />
					</html:button></td>
					<td width="3%" class="dividerline">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="bottomtd"></td>

				</tr>
				<tr>
				  <td  colspan="2" width="50%" align="center" class="black_ar_new" style="display:none">
					<html:select property="protocolCoordinatorIds" styleId="protocolCoordinatorIds" size="4" multiple="true" style="width:170"><html:options collection="selectedCoordinators" labelProperty="name" property="value"/></html:select>
		          </td>
				</tr>

				<tr class="td_color_F7F7F7">

					<td height="25" colspan="2" align="left" class="tr_bg_blue1"><span
						class="blue_ar_b">&nbsp; <bean:message
						key="assignPrivileges.privilegeDetails" /></span></td>
				</tr>
				<tr class="td_color_F7F7F7">
					<td colspan="2" align="left" class="toptd"></td>
				</tr>

				<tr>
					<td colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="3">
						<tr class="tableheading">
							<td width="6%" class="black_ar_b"><label for="delete"
								align="left"><bean:message key="app.select" /></label></td>
							<td width="27%" class="black_ar_b" align="left"><bean:message
								key="assignPrivileges.site(s)" /></td>
							<td width="22%" class="black_ar_b" align="left"><bean:message
								key="user.name" /></td>
							<td width="40%" class="black_ar_b" align="left"><bean:message
								key="app.Privileges" /></td>
							<td width="5%" class="black_ar_b">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5" width="100%">
							<div
								style="height: 80px; background-color: #ffffff;overflow: auto;">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tbody id="summaryTableId">
									<%
												List list = (List) request
												.getAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD);
										if (list != null) {
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

										<td width="6%" class="black_ar"><input type='checkbox'
											name='<%=chkName %>' id='<%=chkName %>'
											onclick="enableDeleteButton('summaryTableId','deleteButtonId')" /></td>
										<td width="27%" class="black_ar"
											onmouseover="Tip('<%=arr[1]%>',WIDTH,200)">
										<%
												if (arr[1].length() > 30) {
												arr[1] = arr[1].substring(0, 27) + "...";
													}
										%><span><%=arr[1]%></span></td>

										<td width="22%" class="black_ar"><span><%=arr[0]%></span></td>

										<td width="40%" class="black_ar"
											onmouseover="Tip('<%=arr[3]%>',WIDTH,200)">
										<%
												if (arr[3].length() > 45) {
												arr[3] = arr[3].substring(0, 42) + "...";
													}
										%> <span><%=arr[3]%></span></td>

										<td width="5%"><a href='#' class="view"
											onclick="editRow('<%=arr[4]%>')"><bean:message
											key="app.edit" /></a></td>
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
						<tr>

							<c:set var="functionName">deleteCheckedRows('<c:out
									value="${requestScope.operation}" />','summaryTableId',this.id)</c:set>
							<jsp:useBean id="functionName" type="java.lang.String" />
							<td class="black_ar" colspan="6"><html:button
								property="deleteButton" styleId="deleteButtonId"
								styleClass="black_ar" onclick="<%=functionName%>"
								disabled="true">
								<bean:message key="buttons.delete" />
							</html:button></td>
						</tr>
						<tr>
							<td colspan="2" class="bottomtd"></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>

