<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<head>
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

  <html:form action="/ForgotPasswordSearch">	
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td align="left" class=" grey_ar_s">&nbsp; <bean:message  key="forgotpassword.subtitle" /></td>
      </tr>
	  <tr>
        <td align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="forgotpassword.title"/></span></td>
      </tr>
      <tr>
        <td align="left" valign="top" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            
              <tr>
                <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td width="16%" align="left" class="black_ar"><bean:message key="user.emailAddress"/></td>
                <td width="83%" align="left" valign="middle"><label><span class="black_ar">
                  <html:text styleClass="black_ar" size="30" styleId="emailAddress" property="emailAddress"/>
                  </span></label>                  </tr>
           
        </table></td>
      </tr>
      <tr>
        <td align="left" class="buttonbg"><html:submit styleClass="blue_ar_b" accesskey="Enter">
						<bean:message  key="buttons.submit" />
					</html:submit>
          </td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>
</body>
</html>
