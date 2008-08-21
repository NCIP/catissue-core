<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>	
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="submittedFor"/>
	<html:hidden property="id" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="Institution.header" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Institution" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4%" class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
         <logic:equal name="operation" value="add">
                      <td  valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                      <td  valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                       </logic:equal>
					  <logic:equal name="operation" value="edit">
						<td  valign="bottom" ><html:link page="/Institution.do?operation=add&pageOf=pageOfInstitution"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border ="0" /></html:link></td>
                      <td  valign="bottom" ><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22"/></td>
                      </logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
       <tr>
          <td align="left">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		  </td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="operation" value='${requestScope.operationAdd}'><bean:message key="institution.title"/></logic:equal><logic:equal name="operation" value='${requestScope.operationEdit}'><bean:message key="institution.editTitle"/></logic:equal></span></td>
        </tr>
        <tr>
          <td align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
             
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="15%" align="left" class="black_ar"><bean:message key="institution.name"/> </td>
                  <td width="84%" align="left"><label>
                   <html:text styleClass="black_ar" maxlength="255"  size="30" styleId="name" property="name"/>
                  </label></td>
                </tr>
             
          </table></td>
        </tr>
        <tr>
          <td class="buttonbg"><html:submit styleClass="blue_ar_b">
			<bean:message  key="buttons.submit" />
		</html:submit>&nbsp;|&nbsp;<html:link page="/ManageAdministrativeData.do"  onclick="closeUserWindow()" styleClass="cancellink"><bean:message key="buttons.cancel" /></html:link></td>
        </tr>
      </table></td>
  </tr>
   </html:form>
</table>

<!--end content -->
