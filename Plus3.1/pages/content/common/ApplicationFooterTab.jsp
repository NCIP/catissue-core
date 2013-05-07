<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String content = (String)request.getAttribute("CONTENTS");
	String pageName = (String)request.getAttribute("PAGE_TITLE");
%>
<head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
</head>
<body >
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td colspan="3" align="left" class="toptd"></td>
      </tr>
      <tr>
        <td colspan="2" class="tr_bg_blue1" align="left" style="padding-left:7px;">
		<logic:equal name="PAGE_TITLE" value='app.privacyNotice'>	
		<img src="images/uIEnhancementImages/privacy_icon.gif" alt="Disclaimer" width="24" height="24" />
		</logic:equal>
		<logic:equal name="PAGE_TITLE" value='app.disclaimer'>
		<img src="images/uIEnhancementImages/disclaimer_icon.gif" alt="Disclaimer" width="24" height="24" />
		</logic:equal>
		
		<logic:equal name="PAGE_TITLE" value='app.accessibility'>	
		<img src="images/uIEnhancementImages/accessibility_icon.gif" alt="Disclaimer" width="24" height="24" />
		</logic:equal>

		<logic:equal name="PAGE_TITLE" value='app.contactUs'>	
		<img src="images/uIEnhancementImages/contactus_icon.gif" alt="Contact Us" width="24" height="24" />
		</logic:equal>
		</td>
        <td width="97%" align="left" class="tr_bg_blue1"><span class="blue_ar_b"><bean:message key="<%=pageName%>"/></span></td>
      </tr>
      <tr>
        <td colspan="3" align="left" valign="top" class=" showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
              <td width="1%" align="left" class="black_ar" style=" line-height:17px;"><%=content%></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
