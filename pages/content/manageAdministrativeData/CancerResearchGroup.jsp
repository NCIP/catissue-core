<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<script src="jss/ajax.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<html:errors/> 
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<!--begin content -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
	<html:form action='/CancerResearchGroupAdd.do'>  
					<html:hidden property="operation" />
				<html:hidden property="submittedFor"/>
	<html:hidden property="id" />
<tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_bfdcf3">
      <tr>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
          <tr>
            <td width="100%" colspan="2
            " valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">

              <tr>
                <td colspan="3" valign="top" class="td_color_bfdcf3"><table width="23%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                    <tr>
                      <td ><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
					  <bean:message key="CancerResearchGroup.header" />
					  </span></td>
                      <td  align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                    </tr>
                </table></td>
              </tr>
			   <tr>
                <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
                <td width="4%" valign="top" class="td_tab_bg">&nbsp;</td>
                <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      
					   <!-- for tabs selection -->
				<logic:equal name="operation" value="add">
                      <td valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                      <td valign="bottom" ><html:link page="/SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                      
					  </logic:equal>
					  <logic:equal name="operation" value="edit">
						<td  valign="bottom"  ><html:link page="/CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup"> <img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border ="0" /></html:link></td>
                      <td  valign="bottom" ><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22"/></td>

					 </logic:equal>
                      <td width="90%" valign="bottom" class="td_tab_bg" >&nbsp;</td>
                      <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>
                    </tr>
                </table></td>
              </tr>
            </table></td>
            </tr>
			<tr>
            <td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;"><table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td colspan="3" align="left"><table width="99%" border="0" cellpadding="1" cellspacing="0">
                    <tr>
                      <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_ffffff">
				              <tr>
                            <td class=" grey_ar_s"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> 
							<bean:message key="commonRequiredField.message" />
							</td>
                          </tr>
                      </table></td>
                    </tr>
                </table></td>
              </tr>
			  <tr>
                <td class="tr_bg_blue1" height="20" colspan="3"><span class="blue_ar_b">
							<logic:equal name="operation" value='${requestScope.operationAdd}'>
								<bean:message key="cancerResearchGroup.title"/>
							</logic:equal>
							<logic:equal name="operation" value='${requestScope.operationEdit}'>
								<bean:message key="cancerResearchGroup.editTitle"/>
							</logic:equal></span>
						</td>
                <td align="right" class="tr_bg_blue1">&nbsp;</td>
              </tr>
			  <tr>
                <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px;">
                    <table width="100%" border="0" cellpadding="3" cellspacing="0">
					<tr>
                          <td width="1%" align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                          <td width="15%" align="left" class="black_ar">
							<label for="Department Name">
								<bean:message key="cancerResearchGroup.name"/>
							</label>
							</td>
							<td width="80%" align="left"><label>
				<html:text styleClass="black_ar" maxlength="255"  size="50" styleId="name" property="name"/>
            </label></td>
            <td width="4%" align="left">&nbsp;</td>
          </tr>
      </table>
</td>
  </tr>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr class="td_color_F7F7F7">
    <td height="35" colspan="4" class="buttonbg">&nbsp;
		<html:submit styleClass="blue_ar_b">
			<bean:message  key="buttons.submit" />
		</html:submit>
     
      &nbsp;|<html:link page="/ManageAdministrativeData.do" onclick="closeUserWindow()" styleClass="cancellink">
													<bean:message key="buttons.cancel" />
												</html:link></td>
  </tr>
 </html:form>
</table>
<!--end content -->