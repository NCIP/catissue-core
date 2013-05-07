<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.beans.SessionDataBean"%>

<%
	String operation = (String) request.getAttribute(Constants.OPERATION);
	String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  
  <html:form action="<%=Constants.UPDATE_PASSWORD_ACTION%>">
  	<html:hidden property="operation" value="<%=operation%>" />				
	<html:hidden property="pageOf" value="<%=pageOf%>" />
	<html:hidden property="id" value="<%=userId%>" />
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td align="left"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="user.changePassword"/></span></td>
      </tr>
      <tr>
        <td align="left" valign="top" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <form id="form" name="form2" method="post" action="">
              <tr>
                <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td width="16%" align="left" class="black_ar"><bean:message key="user.oldPassword" /></td>
                <td width="83%" align="left" valign="middle"><label><span class="black_ar">
                  <html:password styleClass="black_ar" size="25" styleId="oldPassword" property="oldPassword" />
                  </span></label>                </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar"><bean:message key="user.newPassword" /></td>
                <td align="left" valign="middle"><span class="black_ar">
                  <html:password styleClass="black_ar" size="25" styleId="newPassword" property="newPassword" />
                </span>                   </tr>
              <tr>
                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span></td>
                <td align="left" class="black_ar"><bean:message key="user.confirmNewPassword" /></td>
                <td align="left" valign="middle"><span class="black_ar">
                  <html:password styleClass="black_ar" size="25" styleId="confirmNewPassword" property="confirmNewPassword" />
                </span>                  </tr>
            </form>
        </table></td>
      </tr>
      <tr>
        <td align="left" class="buttonbg"><html:submit styleClass="blue_ar_b" onclick="onSubmitButtonClicked()" accesskey="enter">
									<bean:message  key="buttons.submit" />
								</html:submit>
         	</td>
      </tr>
    </table></td>
  </tr>
  </html:form>
</table>

</body>
</html>
