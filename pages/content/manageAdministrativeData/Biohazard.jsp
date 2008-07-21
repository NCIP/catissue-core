<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>
<html:errors/>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation"/>
	<html:hidden property="id" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="Biohazard.header" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<logic:equal name="operation" value="add">
        <td valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfBioHazard&aliasName=Biohazard" ><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
        </logic:equal>
		<logic:equal name="operation" value="edit">
		<td valign="bottom" ><html:link page="/Biohazard.do?operation=add&pageOf=pageOfBioHazard"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		</logic:equal>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      <tr>
        <td align="left" class=" grey_ar_s">&nbsp;<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> <bean:message key="commonRequiredField.message" /></td>
      </tr>
      <tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<logic:equal name="operation" value='${requestScope.operationAdd}'><bean:message key="biohazard.title"/></logic:equal>
					<logic:equal name="operation" value='${requestScope.operationEdit}'><bean:message key="biohazard.editTitle"/></logic:equal></span></td>
      </tr>
      <tr>
        <td align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            
              <tr>
                <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td width="16%" align="left" class="black_ar"><bean:message key="biohazard.name"/>  </td>
                <td width="18%" align="left"><html:text styleClass="black_ar"  maxlength="255"  styleId="name" property="name" style="width:100%;"/></td>
				<td  width="65%">&nbsp;</td>
              </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar"><bean:message key="biohazard.type"/></td>
                <td align="left"><html:select property="type" styleClass="formFieldSizedNew" styleId="type" size="1" style="width:100%;" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection='${requestScope.biohazard_Type_List}' labelProperty="name" property="value"/></html:select></td>
				<td>&nbsp;</td>
			  </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="left" valign="top" class="black_ar"><bean:message key="biohazard.comments"/> </td>
                <td align="left"><html:textarea styleClass="black_ar" property="comments" styleId="comments"  rows="3" style="width:100%;"/></textarea></td>
				<td>&nbsp;</td>
			  </tr>
           
        </table></td>
      </tr>
      <tr>
        <td class="buttonbg"><html:submit styleClass="blue_ar_b" accesskey="Enter"><bean:message key="buttons.submit"/></html:submit>
          &nbsp;| <span ><html:link page="/ManageAdministrativeData.do" styleClass="cancellink"><bean:message key="buttons.cancel" /></html:link></span></td>
      </tr>
    </table></td>
  </tr>
  </html:form>
</table>

