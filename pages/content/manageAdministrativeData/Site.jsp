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
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	<html:form action='${requestScope.formName}'>
		<html:hidden property="operation" />
		<html:hidden property="submittedFor" />
		<html:hidden property="id" />
		<html:hidden property="onSubmit" />
		<html:hidden property="pageOf" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="site.header" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Site" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4%" class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	<logic:equal name="operation" value="add">
        <td valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
        <td valign="bottom"><html:link
													page="/SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
	</logic:equal>
	<logic:equal name="operation" value="edit">
		 <td valign="bottom" ><html:link
													page="/Site.do?operation=add&pageOf=pageOfSite"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
	</logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td align="left"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<logic:equal name="operation" value='${requestScope.operationAdd}'><bean:message key="site.title" /></logic:equal><logic:equal name="operation" value='${requestScope.operationEdit}'><bean:message key="site.editTitle" /></logic:equal></span></td>
        </tr>
        <tr>
          <td align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
             
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="16%" align="left" class="black_ar"><bean:message key="site.name" /> </td>
                  <td width="17%" align="left"><label>
				  <html:text
												styleClass="black_ar" maxlength="255" size="30"
												styleId="name" property="name" />
                    
                  </label></td>
                  <td width="16%" align="left">&nbsp;</td>
                  <td width="1%" align="center"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="16%" align="left"><label for="type" class="black_ar"><bean:message key="site.type" /> </label></td>
                  <td width="21%" align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag
												property="type" optionsList='${siteTypeList}'
												initialValue='${siteForm.type}'
												styleClass="black_ar"
												size="27"/></td>
                  <td width="11%" align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label
												for="coordinator"> <bean:message
												key="site.coordinator" /> </label></td>
                  <td align="left" nowrap class="black_new"><html:select
												property="coordinatorId" styleClass="formFieldSizedNew"
												styleId="coordinatorId" size="1"
												onchange="onCoordinatorChange()"
												onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
												<html:options collection="userList" labelProperty="name"
													property="value" />
											</html:select></td>
                  <td align="left"><html:link href="#" styleId="newCoordinator"
												styleClass="view"
												onclick="addNewAction('SiteAddNew.do?addNewForwardTo=coordinator&forwardTo=site&addNewFor=coordinator')">
												<bean:message key="buttons.addNew" />
											</html:link></td>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label
												for="emailAddress"> <bean:message
												key="site.emailAddress" /> </label></td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="255" size="30" styleId="emailAddress"
												property="emailAddress" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="street">
											<bean:message key="site.street" /> </label> </td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="street" property="street" /></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="city">
											<bean:message key="site.city" /> </label> </td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="city" property="city" /></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="state">
											<bean:message key="site.state" /> </label> </td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag
												property="state" optionsList='${stateList}'
												initialValue='${siteForm.state}'
												styleClass="black_ar" 
												size="27" /></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="zipCode">
											<bean:message key="site.zipCode" /> </label></td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="30" size="30" styleId="zipCode"
												property="zipCode"  style="text-align:right"/></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="country">
											<bean:message key="site.country" /> </label></td>
                  <td align="left" nowrap class="black_ar"><autocomplete:AutoCompleteTag
												property="country" optionsList='${countryList}'
												initialValue='${siteForm.country}'
												styleClass="black_ar" 
												size="27"/></td>
                  <td align="left" class="black_ar">&nbsp;</td>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label
												for="phoneNumber"> <bean:message
												key="site.phoneNumber" /> </label></td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="phoneNumber"
												property="phoneNumber" style="text-align:right"/></td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label for="faxNumber">
											<bean:message key="site.faxNumber" /> </label></td>
                  <td align="left"><html:text styleClass="black_ar"
												maxlength="50" size="30" styleId="faxNumber"
												property="faxNumber" style="text-align:right"/></td>
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
													styleClass="black_ar" size="27" onChange='${strCheckStatus}' />
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
              
          </table></td>
        </tr>
        <tr>
          <td class="buttonbg"><html:submit
										styleClass="blue_ar_b">
										<bean:message key="buttons.submit" />
									</html:submit>            &nbsp;| <html:link
										page="/ManageAdministrativeData.do" styleClass="cancellink">
										<bean:message key="buttons.cancel" />
									</html:link></td>
        </tr>
      </table></td>
  </tr>
  </html:form>
</table>