<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table summary="" cellpadding="0" cellspacing="0" border="0">
    <tr>
    
      <td height="20" class="footerMenuItem" onmouseover="changeMenuStyle(this,'footerMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'footerMenuItem'),hideCursor()" onclick="document.location.href='ContactUs.do?PAGE_TITLE_KEY=app.contactUs&FILE_NAME_KEY=app.contactUs.file'">
        &nbsp;&nbsp;
		<a class="footerMenuLink" href="ContactUs.do?PAGE_TITLE_KEY=app.contactUs&FILE_NAME_KEY=app.contactUs.file">
			<bean:message key="app.contactUs" />
		</a>&nbsp;&nbsp;
      </td>
      
      <td>
      	<img src="images/ftrMenuSeparator.gif" width="1" height="16" alt="" />
      </td>
      
      <td height="20" class="footerMenuItem" onmouseover="changeMenuStyle(this,'footerMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'footerMenuItem'),hideCursor()" onclick="document.location.href='PrivacyNotice.do?PAGE_TITLE_KEY=app.privacyNotice&FILE_NAME_KEY=app.privacyNotice.file'">
        &nbsp;&nbsp;
        <a class="footerMenuLink" href="PrivacyNotice.do?PAGE_TITLE_KEY=app.privacyNotice&FILE_NAME_KEY=app.privacyNotice.file">
        	<bean:message key="app.privacyNotice" />
        </a>&nbsp;&nbsp;
      </td>
      
      <td>
      	<img src="images/ftrMenuSeparator.gif" width="1" height="16" alt="" />
      </td>
      
      <td height="20" class="footerMenuItem" onmouseover="changeMenuStyle(this,'footerMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'footerMenuItem'),hideCursor()" onclick="document.location.href='Disclaimer.do?PAGE_TITLE_KEY=app.disclaimer&FILE_NAME_KEY=app.disclaimer.file'">
        &nbsp;&nbsp;
        <a class="footerMenuLink" href="Disclaimer.do?PAGE_TITLE_KEY=app.disclaimer&FILE_NAME_KEY=app.disclaimer.file">
        	<bean:message key="app.disclaimer" />
        </a>&nbsp;&nbsp;
      </td>
      
      <td>
      	<img src="images/ftrMenuSeparator.gif" width="1" height="16" alt="" />
      </td>
      
      <td height="20" class="footerMenuItem" onmouseover="changeMenuStyle(this,'footerMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'footerMenuItem'),hideCursor()" onclick="document.location.href='Accessibility.do?PAGE_TITLE_KEY=app.accessibility&FILE_NAME_KEY=app.accessibility.file'">
        &nbsp;&nbsp;
        <a class="footerMenuLink" href="Accessibility.do?PAGE_TITLE_KEY=app.accessibility&FILE_NAME_KEY=app.accessibility.file">
        	<bean:message key="app.accessibility" />
        </a>&nbsp;&nbsp;
      </td>
      
      <td>
      	<img src="images/ftrMenuSeparator.gif" width="1" height="16" alt="" />
      </td>
      
      <td height="20" class="footerMenuItem" onmouseover="changeMenuStyle(this,'footerMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'footerMenuItem'),hideCursor()" onclick="document.location.href='ReportProblem.do?operation=add'">
        &nbsp;&nbsp;
        <a class="footerMenuLink" href="ReportProblem.do?operation=add">
        	<bean:message key="app.reportProblem" />
        </a>&nbsp;&nbsp;
      </td>
      
      <td>
      	<img src="images/ftrMenuSeparator.gif" width="1" height="16" alt="" />
      </td>
    </tr>
  </table>