<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.beans.SessionDataBean"%>

<%
	String operation = (String) request.getAttribute(Constants.OPERATION);
	String pageOf = (String) request.getAttribute(Constants.PAGEOF);
	SessionDataBean sessionData = null;
	if(session.getAttribute(Constants.TEMP_SESSION_DATA) != null) 
	{
	sessionData = (SessionDataBean)session.getAttribute(Constants.TEMP_SESSION_DATA);
	} else 
	{
	sessionData = (SessionDataBean)session.getAttribute(Constants.SESSION_DATA);
	}
	String userId = sessionData.getUserId().toString();
%>

<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>

	<script language="JavaScript">
	
	   function onSubmitButtonClicked()
		{
			var action = '<%=Constants.UPDATE_PASSWORD_ACTION%>?access=denied';
			document.forms[0].action = action;
		    document.forms[0].submit();
		}
		
	</script>
</head>



<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<body >
<!--<table width="100%" border="0" class="whitetable_bg">
<tr>
<td class="bigError"><b><html:errors /></b></td></tr></table>-->





  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
  <tr class="whitetable_bg">
<td class="bigError" ><html:errors /></td></tr>
  <html:form action="<%=Constants.UPDATE_PASSWORD_ACTION%>">
  	<html:hidden property="operation" value="<%=operation%>" />				
	<html:hidden property="pageOf" value="<%=pageOf%>" />
	<html:hidden property="id" value="<%=userId%>" />
  <tr>
    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
      <tr>
        <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="100%" height="25" colspan="3" valign="top" class="td_color_bfdcf3">&nbsp;</td>
            </tr>
            
            
        </table></td>
      </tr>
      <tr>
        <td colspan="2" valign="top" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;"><table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
            <tr>
              <td height="15" colspan="3" align="left" class="td_tab_bg">&nbsp;</td>
            </tr>
            <tr>
              <td height="15" colspan="3" align="left"><span class=" grey_ar_s"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /> indicates a required field</span></td>
              </tr>
            <tr>
              <td width="42%" height="25" colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="user.changePassword"/></span></td>
              <td width="61%" height="25" align="left" class="tr_bg_blue1">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="3" align="left" valign="top" style="padding-top:10px; padding-bottom:15px; padding-left:6px;"><table width="100%" border="0" cellpadding="3" cellspacing="0">
                <form id="form" name="form2" method="post" action="">
                  <tr>
                    <td width="1%" align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                    <td width="15%" align="left" class="black_ar"><span class="black_ar"><label for="oldPassword">
							<bean:message key="user.oldPassword" />
						</label></span></td>
                    <td align="left" valign="middle"><label><span class="black_ar">
                  <html:password styleClass="formFieldSized" size="30" styleId="oldPassword" property="oldPassword" />
                    </span></label>                  </tr>
                  <tr>
                    <td align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                    <td align="left" class="black_ar"><label for="newPassword">
							<bean:message key="user.newPassword" />
						</label></td>
                    <td align="left" valign="middle"><span class="black_ar">
                      <html:password styleClass="formFieldSized" size="30" styleId="newPassword" property="newPassword" />
                    </span>                  </tr>
                  <tr>
                    <td align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                    <td align="left" class="black_ar"><label for="confirmNewPassword">
							<bean:message key="user.confirmNewPassword" />
						</label></td>
                    <td align="left" valign="middle"><span class="black_ar">
                     <html:password styleClass="formFieldSized" size="30" styleId="confirmNewPassword" property="confirmNewPassword" />
                    </span>                  </tr>
                </form>
              </table></td>
            </tr>
            
            <tr  class="td_color_F7F7F7">
              <td colspan="3" align="left" class="buttonbg" style="padding-left:10px;">
			  <html:submit styleClass="blue_ar_b" onclick="onSubmitButtonClicked()">
									<bean:message  key="buttons.submit" />
								</html:submit>
								
               &nbsp;&nbsp;|&nbsp; <span class="cancellink">
			   <html:link page="/ManageAdministrativeData.do" styleClass="black_ar">							<bean:message key="buttons.cancel" />
				</html:link></span></td>
            </tr>
            
        </table></td>
      </tr>
    </table></td>
  </tr>
  	</html:form>
</table>
</body>
</html>
