<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<head>

<title>caTissue Suite v 1.0. RC1</title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript">
	function cancelButtonPressed()
	{
		document.forms[0].action="RedirectHome.do";
		document.forms[0].submit();
	}
</script>
</head>
<body >
	<html:errors/>
	<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
		<%=messageKey%>
	</html:messages>
  <html:form action="/ForgotPasswordSearch">	
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
      <tr>
        <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="3" valign="top" class="td_color_bfdcf3">&nbsp;</td>
            </tr>
            <tr>
              <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
              <td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
              <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="td_tab_bg" >&nbsp;</td>
                    <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" ></td>
                    </tr>
              </table></td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td colspan="2" valign="top" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;"><table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
            <tr>
              <td height="15" colspan="3" align="left"><span class=" grey_ar_s">&nbsp;<bean:message  key="forgotpassword.subtitle" /></span></td>
              </tr>
            <tr>
              <td width="42%" height="25" colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="forgotpassword.title"/></span></td>
              <td width="61%" height="25" align="left" class="tr_bg_blue1">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="3" align="left" valign="top" style="padding-top:10px; padding-bottom:15px; padding-left:6px;"><table width="100%" border="0" cellpadding="3" cellspacing="0">
                <form id="form" name="form2" method="post" action="">
                  <tr>
                    <td width="1%" align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif"  width="6" height="6" hspace="0" vspace="0" /></span></td>
                    <td width="10%" align="left" class="black_ar"><span class="black_ar">&nbsp;<bean:message key="user.emailAddress"/></span></td>
                    <td align="left" valign="middle"><label><span class="black_ar">
                  <html:text styleClass="black_ar" size="30" styleId="emailAddress" property="emailAddress"/>
                    </span></label>                  
                  </tr>
                </form>
              </table></td>
            </tr>
            <tr  class="td_color_F7F7F7">
		 	<td>
					<html:submit styleClass="blue_ar_b">
						<bean:message  key="buttons.submit" />
					</html:submit>
		       &nbsp;&nbsp;|&nbsp; <span class="cancellink">
			   <html:link page="/RedirectHome.do" styleClass="black_ar">
							<bean:message key="buttons.cancel" />
				</html:link></span></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>
</body>
</html>
