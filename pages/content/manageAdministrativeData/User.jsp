<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script src="jss/script.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<SCRIPT LANGUAGE="JavaScript">



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
</SCRIPT>
</head>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
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
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="user.name" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding">
	<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
       <tr>
			<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
			<td valign="top" class="td_color_bfdcf3">&nbsp;	</td>
			<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
			<td valign="top" class="td_color_bfdcf3">&nbsp;</td>
			<td width="90%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
	   </tr>
    </table>
	</logic:equal>
	<logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<logic:equal name="operation" value='${requestScope.addforJSP}'>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /><a href="#"></a></td>
        <logic:notEqual parameter="openInCPFrame"value='yes'>
		<td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link><a href="#"></a></td>
        <td valign="bottom"><html:link page="/ApproveUserShow.do?pageNum=1&menuSelected=1"><img src="images/uIEnhancementImages/tab_approve_user.jpg" alt="Approve New Users" width="146" height="22" border="0" /></html:link><a href="#"></a></td>
        </logic:notEqual>
		</logic:equal>
		<logic:equal name="operation" value='${requestScope.editforJSP}'>
		<td valign="bottom"><html:link page="/User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link><a href="#"></a></td>
        <logic:notEqual parameter="openInCPFrame"value='yes'>
		<td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
        <td valign="bottom"><html:link page="/ApproveUserShow.do?pageNum=1&menuSelected=1"><img src="images/uIEnhancementImages/tab_approve_user.jpg" alt="Approve New Users" width="146" height="22" border="0" /></html:link><a href="#"></a></td>
		</logic:notEqual>
		</logic:equal>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
	</logic:notEqual>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class=" grey_ar_s">&nbsp;<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /><bean:message key="commonRequiredField.message" /></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<logic:equal name="operation" value='${requestScope.addforJSP}'><bean:message key="user.details.title" /></logic:equal><logic:equal name="operation" value='${requestScope.editforJSP}'><bean:message key="user.editTitle" /></logic:equal></span></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
              
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="17%" align="left" class="black_ar"><bean:message key="user.emailAddress" /> </td>
                  <td width="18%" align="left"><html:text styleClass="black_ar" maxlength="255" size="30" styleId="emailAddress" property="emailAddress"readonly='${requestScope.readOnlyEmail}' /></td>
                  <td width="14%" align="left">&nbsp;</td>
                  <td width="1%" align="center"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <logic:notEqual name="pageOf"	value='${requestScope.pageOfUserProfile}'>
				  <td width="17%" align="left"><label for="confirmEmailAddress" class="black_ar"><bean:message key="user.confirmemailAddress" /></label></td>
                  </logic:notEqual>
				  <td width="17%" align="left"><html:text styleClass="black_ar" maxlength="255" size="30" styleId="confirmEmailAddress"property="confirmEmailAddress"readonly='${requestScope.readOnlyEmail}' /></td>
                  <td width="14%" align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.lastName" /></td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="255" size="30" styleId="lastName" property="lastName" /></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.firstName" /> </td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="255" size="30" styleId="firstName" property="firstName" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
				<logic:equal name="pageOf"	value='${requestScope.pageOfUserAdmin}'>
					 <logic:equal name="operation"	value='${requestScope.editforJSP}'>
				 <tr>
					<td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
					<td align="left" class="black_ar"><label for="newPassword"> <bean:message key="user.newPassword" /> </label></td>
					<td align="left"><html:password styleClass="black_ar"size="30" styleId="newPassword" property="newPassword" /></td>
					<td align="left" class="black_ar">&nbsp;</td>
					<td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory"	width="6" height="6" hspace="0" vspace="0" /></span></td>
					<td align="left" class="black_ar"><label for="confirmNewPassword"> <bean:message key="user.confirmNewPassword" /> </label></td>													
					<td align="left"><html:password styleClass="black_ar"size="30" styleId="confirmNewPassword"	property="confirmNewPassword" /></td>
					<td align="left" valign="top">&nbsp;</td>
 				 </tr>
					</logic:equal>
				</logic:equal>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><bean:message key="user.street" /></td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="255" size="30" styleId="street" property="street" /></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.city" /> </td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="50" size="30" styleId="city" property="city" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.state" /> </td>
                  <td align="left" nowrap  class="black_ar"><autocomplete:AutoCompleteTag	property="state" optionsList='${requestScope.stateList}' initialValue='${userForm.state}'styleClass="black_ar"  size="30"/></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.zipCode" /></td>
                  <td align="left"  class="black_ar"><html:text styleClass="black_ar"	maxlength="30" size="30" styleId="zipCode" property="zipCode" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.country" /></td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag	property="country" optionsList='${requestScope.countryList}' initialValue='${userForm.country}'	styleClass="black_ar" size="30"/></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="user.phoneNumber" /></td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="50" size="30" styleId="phoneNumber" property="phoneNumber" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><bean:message key="user.faxNumber" /></td>
                  <td align="left"><html:text styleClass="black_ar"	maxlength="50" size="30" styleId="faxNumber" property="faxNumber" /></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="institutionId"><bean:message key="user.institution" /></label></td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag	property="institutionId" optionsList='${requestScope.instituteList}' initialValue='${userForm.institutionId}' styleClass="black_ar" staticField="false" size="30"/></td>
                  <td align="left"><logic:notEqual name="pageOf"value='${requestScope.pageOfSignUp}'><html:link href="#" styleClass="view"	styleId="newInstitution" onclick="openInstitutionWindow();"><bean:message key="buttons.addNew" /></html:link></logic:notEqual></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="departmentId"><bean:message	key="user.department" /> </label></td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag property="departmentId" optionsList='${requestScope.departmentList}' initialValue='${userForm.departmentId}' styleClass="black_ar" staticField="false" size="30"/></td>
                  <td align="left"><a href="#" class="view"><logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'><html:link href="#" styleClass="view"	styleId="newDepartment" onclick="openDepartmentWindow();"><bean:message key="buttons.addNew" /></html:link></logic:notEqual></a></td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="cancerResearchGroupId"><bean:message key="user.cancerResearchGroup" /> </label></td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag property="cancerResearchGroupId" optionsList='${requestScope.cancerResearchGroupList}'	initialValue='${userForm.cancerResearchGroupId}' styleClass="black_ar" staticField="false" size="30"/></td>
                  <td align="left"><a href="#" class="view"><logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'><html:link href="#" styleClass="view" styleId="newCancerResearchGroup" onclick="openCRGWindow();"><bean:message key="buttons.addNew" /></html:link></logic:notEqual></a></td>
                </tr>
              
          </table></td>
        </tr>
		<logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'>
			<logic:notEqual name="pageOf" value='${requestScope.pageOfUserProfile}'>
        <tr onclick="showHide('add_id')">
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="user.administrativeDetails.title" /></span></td>
          <td align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_add_id"><img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0"/></a></td>
        </tr>
        <tr>
          <td colspan="2" class="showhide1"><div id="add_id" style="display:none" >
              <table width="100%" border="0" cellpadding="3" cellspacing="0">
                <tr>
                  <td width="1%" align="center" class="black_ar">&nbsp;</td>
                  <td width="16%" align="left" class="black_ar"><bean:message key="user.adminUser" /></td>
	              <td align="left" valign="top" class="black_ar"><html:radio styleClass="black_ar" styleId="adminuser" property="adminuser" value="true" ><label for="siteId"><bean:message key="user.yes"/></label> &nbsp  &nbsp &nbsp &nbsp  </html:radio><html:radio styleClass="black_ar" styleId="adminuser1" property="adminuser" value="false"><label for="siteId"><bean:message key="user.no"/></label></html:radio></td>
				</tr>
				<tr>
				<td width="1%" height="25" align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory"	width="6" height="6" hspace="0" vspace="0" /></span></td>												
				<td width="16%" align="left" class="black_ar"><label for="site"><bean:message key="user.site" /> </label></td>																
				<td align="left"><html:select	property="siteIds" styleClass="formFieldSizedNew"	styleId="siteIds" size="5" multiple="true"	onmouseover="showTip(this.id)"	onmouseout="hideTip(this.id)"><html:options collection='${requestScope.siteListforJSP}'	labelProperty="name" property="value" /></html:select></td>
				</tr>
                <tr>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar"><bean:message key="user.comments" /></td>
                  <td align="left" valign="top"><html:textarea styleClass="formFieldSizedNew" cols="34" rows="3"styleId="comments" property="comments" /></td>
                </tr>
              </table>
          </td>
        </tr>
		</logic:notEqual>
		</logic:notEqual>
        <tr>
          <td colspan="2" class="toptd"></td>
        </tr>
        <tr>
          <td colspan="2" class="buttonbg"><html:submit	styleClass="blue_ar_b"><bean:message key="buttons.submit" /></html:submit>
            &nbsp;|<logic:notEqual name="pageOf" value='${requestScope.pageOfSignUp}'><logic:notEqual parameter="openInCPFrame"	value='yes'><html:link onclick="closeUserWindow()" page="/ManageAdministrativeData.do" styleClass="cancellink"><bean:message key="buttons.cancel" /></html:link></logic:notEqual>
										 <logic:equal parameter="openInCPFrame"	value='yes'><html:link  page="/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol" styleClass="cancellink"><bean:message key="buttons.cancel" /></html:link></logic:equal></logic:notEqual>
				   <logic:equal name="pageOf"value='${requestScope.pageOfSignUp}'> <a href="#" class="cancellink"><html:link page="/RedirectHome.do" styleClass="cancellink"><bean:message key="buttons.cancel" /></html:link></a></logic:equal></td>
        </tr>
      </table>    </td>
  </tr>
  	</html:form>
</table>

