<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>

<%
	//Object obj=  request.getAttribute(Constants.ACTIONLIST);
	//String pageOf = (String) request.getAttribute(Constants.PAGEOF);
	//String operation = (String) request.getAttribute(Constants.OPERATION);
	List siteList = (List) request.getAttribute(Constants.SITELIST);
	List userList = (List) request.getAttribute(Constants.USERLIST);
	List roleList = (List) request.getAttribute(Constants.ROLELIST);
	List actionList = (List) request.getAttribute(Constants.ACTIONLIST);
%>
<script>
function viewSummary()
{
	var action="DefineEvents.do?Event_Id=dummyId&pageOf=ViewSummary&operation=${requestScope.operation}";
	document.forms[0].action=action;
	document.forms[0].submit();
}
function updateCPTree()
{	
  window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
}
</script>
<form name="apForm" method="POST">
<table width="100%" border="0" cellpadding="3" cellspacing="0"
	bgcolor="#FFFFFF" summary="">
	<tr>
		<td valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<td width="10%" valign="bottom" id="collectionProtocolTab"
					onclick="collectionProtocolPageForPrivilege('${requestScope.cpOperation}')"><a> <img
					src="images/uIEnhancementImages/cp_details1.gif"
					alt="Collection Protocol Details" width="174" height="20"
					border="0" /></a></td>
				<td width="10%" valign="bottom" onclick="consentPageForPrivilege('${requestScope.cpOperation}')"
					id="consentTab"><img
					src="images/uIEnhancementImages/cp_consents1.gif" alt="Consents"
					width="94" height="20" border="0" /></td>
				<td width="10%" valign="bottom" id="consentTab"><a> <img
					src="images/uIEnhancementImages/cp_privileges.gif" alt="Privileges"
					width="94" height="20" border="0"
					onclick="showAssignPrivilegePage()"></a></td>
				<td width="100%" valign="bottom" class="cp_tabbg">&nbsp;</td>
			</tr>
		</table>
		</td>
		<td align="right" valign="top" class="cp_tabbg" border="0"><span
			class="smalllink"> <html:link href="#" styleId="newUser"
			onclick="updateCPTree();viewSummary()">
			<bean:message key="cpbasedentry.viewsummary" />
		</html:link> </span></td>
	</tr>
	<tr>
		<td colspan="2" class="cp_tabtable">
		<table summary="" cellpadding="0" cellspacing="0" border="0"
			id="table1" width="100%">
			<tr>
				<td colspan="2" align="left"
					style="padding-top:5px; padding-bottom:10px;">
				<table width="100%" border="0" cellpadding="3" cellspacing="3"
					style="background-color:#FFFFFF">
					<tr>
						<td height="15" colspan="6" align="left" class="tr_bg_blue1"><strong
							class="blue_ar_b"><bean:message
							key="assignPrivileges.bloodCancerProtocol" /></strong></td>
					</tr>
					
					<tr>
						<td width="1%" align="left" valign="top" class="black_ar"
							style="padding-top:6px;"><span class="blue_ar_b"><img
							src="images/uIEnhancementImages/star.gif" alt="Mandatory"
							width="6" height="6" hspace="0" vspace="0" /></span></td>
						<td width="15%" align="left" valign="top" class="black_ar"><bean:message
							key="Site.header" /></td>
						<td width="36%" align="left"><select class="formFieldSized12"
							id="siteIds" size="4" multiple="multiple"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"
							onchange="getUsersForThisSites(this)">
							<%
									for (int i = 0; i < siteList.size(); i++) {
									String siteName = ""
									+ ((NameValueBean) siteList.get(i)).getName();
									String siteValue = ""
									+ ((NameValueBean) siteList.get(i)).getValue();
							%>
							<option value="<%=siteValue%>"><%=siteName%></option>
							<%
							}
							%>
						</select></td>
						<td width="5%" align="left" class="black_ar">&nbsp;</td>

						<td width="9%" align="left" valign="top" class="black_ar"><bean:message
							key="User.header" /></td>
						<td width="34%"><select class="formFieldSized12" id="userIds"
							size="4" multiple="multiple" onmouseover="showTip(this.id)"
							onmouseout="hideTip(this.id)">
							<%
									for (int i = 0; i < userList.size(); i++) {
									String userName = ""
									+ ((NameValueBean) userList.get(i)).getName();
									String userValue = ""
									+ ((NameValueBean) userList.get(i)).getValue();
							%>
							<option value="<%=userValue%>"><%=userName%></option>
							<%
							}
							%>
						</select></td>
					</tr>
					
					<tr>
						<td align="left" class="black_ar">&nbsp;</td>
						<td align="left" class="black_ar"><label
							for="protocolCoordinatorIds"><bean:message
							key="collectionprotocol.protocolcoordinator" /></label></td>
						<td width="15%" align="left" class="black_ar"><select
							class="formFieldSized12" id="roleIds"
							onchange="getActionsForThisRole(this)"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<%
									for (int i = 0; i < roleList.size(); i++) {
									String roleName = ""
									+ ((NameValueBean) roleList.get(i)).getName();
									String roleValue = ""
									+ ((NameValueBean) roleList.get(i)).getValue();
									String selected = "";
									if (roleValue.equals("1")) {
										selected = "SELECTED";
									}
							%>
							<option value="<%=roleValue%>" <%=selected%>><%=roleName%></option>
							<%
							}
							%>
						</select></td>
						<td width="5%" align="left" class="black_ar">&nbsp;</td>
						<td width="9%" align="left" valign="top" class="black_ar"><bean:message
							key="Action.header" /></td>
						<td><select class="formFieldSized12" id="actionIds" size="4"
							multiple="multiple" onmouseover="showTip(this.id)"
							onmouseout="hideTip(this.id)">
							<%
									for (int i = 0; i < actionList.size(); i++) {
									String actionName = ""
									+ ((NameValueBean) actionList.get(i)).getName();
									String actionValue = ""
									+ ((NameValueBean) actionList.get(i)).getValue();
							%>
							<option value="<%=actionValue%>"><%=actionName%></option>
							<%
							}
							%>
						</select></td>
					</tr>

				</table>
				</td>

			</tr>
			<tr class="td_color_F7F7F7">
				<td width="66%" class="buttonbg">&nbsp;</td>
				<td width="34%" class="buttonbg"><html:button
					property="addKeyValue" styleClass="blue_ar_b"
					onclick="getUserPrivilegeSummary()">
					<bean:message key="app.add" />
				</html:button></td>
			</tr>
			<tr class="td_color_F7F7F7">

				<td height="15" colspan="2" align="left" class="tr_bg_blue1"><span
					class="blue_ar_b"> <bean:message key="participant.details" /><bean:message
					key="assignPrivileges.userPriviledgeSummary" /></span></td>
			</tr>
			<tr class="td_color_F7F7F7">
				<td colspan="2" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="4">
					<tr class="tableheading" valign="top">
						<td width="13%" class="black_ar_b"><label for="delete"
							align="center"><bean:message key="addMore.delete" /></label></td>
						<td width="24%" class="black_ar_b"><bean:message
							key="assignPrivileges.site(s)" /></td>
						<td width="23%" class="black_ar_b"><bean:message
							key="user.name" /></td>
						<td width="20%" class="black_ar_b"><bean:message
							key="user.role" /></td>
						<td width="20%" class="black_ar_b"><bean:message
							key="assignPrivileges.action(s)" /></td>
					</tr>
					<tr>
						<td colspan="5">
						<div
							style="height: 80px; background-color: #ffffff; border: 1px solid Silver; overflow: auto;">
						<table border="0" width="100%" cellspacing="0" cellpadding="0">
							<tbody id="summaryTableId">
							</tbody>
						</table>
						</div>
						</td>
					</tr>
					<tr class="tabletd1">
						<td class="black_ar"><html:button property="deleteButton"
							styleClass="blue_ar_b" onclick="deleteCheckedRows()"
							disabled="true">
							<bean:message key="buttons.delete" />
						</html:button></td>
						<td valign="bottom">&nbsp;</td>
						<td valign="bottom">&nbsp;</td>
						<td valign="bottom">&nbsp;</td>
						<td valign="bottom">&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
