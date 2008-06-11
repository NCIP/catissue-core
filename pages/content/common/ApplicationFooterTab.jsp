<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String content = (String)request.getAttribute("CONTENTS");
	String pageName = (String)request.getAttribute("PAGE_TITLE");
%>
<head>

<title>caTissue Suite v 1.0. RC1</title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 


</head>
<body >
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
              <td height="15" colspan="3" align="left"></td>
              </tr>
            <tr>
			 <logic:equal name="PAGE_TITLE" value='app.privacyNotice'>	
              <td height="25" colspan="2" class="tr_bg_blue1" align="left" style="padding-left:7px;"><img src="images/uIEnhancementImages/privacy_icon.gif" alt="Accessibility" width="24" height="24" /></td>
</logic:equal>
 <logic:equal name="PAGE_TITLE" value='app.disclaimer'>	
              <td height="25" colspan="2" class="tr_bg_blue1" align="left" style="padding-left:7px;"><img src="images/uIEnhancementImages/disclaimer_icon.gif" alt="Accessibility" width="24" height="24" /></td>
</logic:equal>
 <logic:equal name="PAGE_TITLE" value='app.accessibility'>	
              <td height="25" colspan="2" class="tr_bg_blue1" align="left" style="padding-left:7px;"><img src="images/uIEnhancementImages/accessibility_icon.gif" alt="Accessibility" width="24" height="24" /></td>
</logic:equal>
              <td width="97%" height="25" align="left" class="tr_bg_blue1"><span class="blue_ar_b"><bean:message key="<%=pageName%>"/></span></td>
              </tr>
            
            <tr>
              <td colspan="3" align="left" valign="top" style="padding-top:10px; padding-bottom:15px; padding-left:6px;"><table width="100%" border="0" cellpadding="3" cellspacing="0">
               
                  <tr>
                    <td width="1%" align="left" class="black_ar" style="padding-right:10px; line-height:17px;"><%=content%>

</td>
                    </tr>
               
              </table></td>
            </tr>
            
            

        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
