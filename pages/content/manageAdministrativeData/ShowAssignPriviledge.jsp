<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>

<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

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


<%
	//Object obj=  request.getAttribute(Constants.ACTIONLIST);
	//String pageOf = (String) request.getAttribute(Constants.PAGEOF);
	//String operation = (String) request.getAttribute(Constants.OPERATION);
	List siteList = (List) request.getAttribute(Constants.SITELIST);
	List userList = (List) request.getAttribute(Constants.USERLIST);
	List roleList = (List) request.getAttribute(Constants.ROLELIST);
	List actionList = (List) request.getAttribute(Constants.ACTIONLIST);
%>

<body>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script type="text/javascript" src="jss/queryModule.js"></script>

<div id="errorMess" style="display:none"></div>

<form name="apForm" method="POST">
<table summary="" cellpadding="0" cellspacing="0" border="0"
	style="padding-left:0;padding-right:0;" width="710">
	<tr>
		<td valign="bottom">
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
				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					style="background-color:#FFFFFF">

					<tr>
						<td width="6%" align="right" valign="top" class="black_ar"><img
							src="images/uIEnhancementImages/star.gif" alt="Mandatory"
							width="6" height="6" hspace="0" vspace="0" />&nbsp;<img
							src="images/uIEnhancementImages/number_1.gif" alt="Number 1"
							width="18" height="18" align="absmiddle"></td>
						<td width="12%" align="left" valign="top" class="black_ar"><bean:message
							key="Site.header" /></td>
						<td width="36%" align="left"><select class="formFieldSized18"
							id="siteIds" size="4" multiple="multiple"
							
							onchange="getUsersForThisSites(this)">
							<%
									for (int i = 0; i < siteList.size(); i++) {
									String siteName = ""
									+ ((NameValueBean) siteList.get(i)).getName();
									String siteValue = ""
									+ ((NameValueBean) siteList.get(i)).getValue();
							%>
							<option value="<%=siteValue%>" onmouseover="Tip('<%=siteName%>',WIDTH,200)"><%=siteName%></option>
							<%
							}
							%>
						</select></td>
						<td width="5%" align="right" valign="top" class="black_ar"><img
							src="images/uIEnhancementImages/number_2.gif" alt="Number 2"
							width="18" height="18" align="absmiddle"></td>

						<td width="7%" align="left" valign="top" class="black_ar"><bean:message
							key="User.header" /></td>
						<td width="34%"><select class="formFieldSized18" id="userIds"
							size="4" multiple="multiple" >
							<%
									for (int i = 0; i < userList.size(); i++) {
									String userName = ""
									+ ((NameValueBean) userList.get(i)).getName();
									String userValue = ""
									+ ((NameValueBean) userList.get(i)).getValue();
							%>
							<option value="<%=userValue%>" onmouseover="Tip('<%=userName%>',WIDTH,200)"><%=userName%></option>
							<%
							}
							%>
						</select></td>
					</tr>

					<tr>
						<td align="right" valign="top" class="black_ar">&nbsp;<img
							src="images/uIEnhancementImages/number_3.gif" alt="Number 3"
							width="18" height="18" align="absmiddle"></td>
						<td align="left" valign="top" class="black_ar"><label
							for="protocolCoordinatorIds"><bean:message
							key="collectionprotocol.protocolcoordinator" /></label></td>
						<td align="left" valign="top" class="black_ar"><select
							class="formFieldSized18" id="roleIds"
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
							<option value="<%=roleValue%>" <%=selected%> ><%=roleName%></option>
							<%
							}
							%>
						</select></td>
						<td align="right" valign="top" class="black_ar"><img
							src="images/uIEnhancementImages/number_4.gif" alt="Number 4"
							width="18" height="18" align="absmiddle"></td>
						<td align="left" valign="top" class="black_ar"><bean:message
							key="Action.header" /></td>
						<td><select class="formFieldSized18" id="actionIds" size="4"
							multiple="multiple">
							<%
									for (int i = 0; i < actionList.size(); i++) {
									String actionName = ""
									+ ((NameValueBean) actionList.get(i)).getName();
									String actionValue = ""
									+ ((NameValueBean) actionList.get(i)).getValue();
							%>
							<option value="<%=actionValue%>" onmouseover="Tip('<%=actionName%>',WIDTH,200)"><%=actionName%></option>
							<%
							}
							%>
						</select></td>
					</tr>

				</table>
				</td>
			</tr>
			<tr>
				<td width="66%" class="dividerline">&nbsp;</td>
				<td width="34%" class="dividerline">&nbsp;<html:button
					property="addKeyValue" styleClass="blue_ar_b"
					onclick="getUserPrivilegeSummary()">
					<bean:message key="app.add" />
				</html:button></td>
			</tr>
			<tr class="td_color_F7F7F7">
				<td colspan="3" align="left" class="toptd"></td>

			</tr>

			<tr class="td_color_F7F7F7">

				<td height="25" colspan="2" align="left" class="tr_bg_blue1"><span
					class="blue_ar_b">&nbsp; <bean:message
					key="assignPrivileges.userPriviledgeSummary" /></span></td>
			</tr>
			<tr class="td_color_F7F7F7">
				<td colspan="3" align="left" class="toptd"></td>
			</tr>

			<tr class="td_color_F7F7F7">
				<td colspan="2">
				<table width="100%" border="0" cellspacing="0" cellpadding="4">
					<tr class="tableheading">
						<td width="13%" class="black_ar_b"><label for="delete"
							align="center"><bean:message key="app.select" /></label></td>
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
							style="height: 80px; background-color: #ffffff;overflow: auto;">
						<table border="0" width="100%" cellspacing="0" cellpadding="0">
							<tbody id="summaryTableId">
								<%
							List list=(List)request.getAttribute("listOnLoad");
							if(list!=null){
							int valueCounter=list.size();
							for(int i=0;i<valueCounter;i++){
								String chkName="chk_"+i;
								String[] arr =(String [])(list.get(i));
							%>
								<tr id="<%=arr[4]%>">
									<td width="13%" class="black_ar">
									<input type='checkbox'
										name='<%=chkName %>' id='<%=chkName %>'
										onclick="enableDeleteButton(this)" /></td>
									<td width="24%" class="black_ar" onmouseover="Tip('<%=arr[1]%>',WIDTH,200)">
									<% if(arr[1].length() >20){
									arr[1]=arr[1].substring(0,17)+"...";
									}
								%><span><%=arr[1]%></span>
									</td>

									<td width="23%" class="black_ar" >
									<span><%=arr[0]%></span></td>
									<td width="20%" class="black_ar"><span><%=arr[2]%></span>
									</td>
									
									<td width="20%" class="black_ar" onmouseover="Tip('<%=arr[3]%>',WIDTH,200)">
									<% if(arr[3].length() >20){
									arr[3]=arr[3].substring(0,17)+"...";
									}
								%>
									<span><%=arr[3]%></span></td>
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
</body>

