<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!--
 Kapil: For splitter UI. A dummy parameter added to the URL to have forcefully execution of the JSP, otherwise browser uses
 page from cache. This is required to do since becuase of chache splitter was not working correctly.
 This is added to main menu pages.
-->
	<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
            <tr>
              <td width="1"><!-- anchor to skip main menu --><a href="#content"><img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" /></a></td>
              <!-- link 1 begins -->
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='Home.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/Home.do?dummy=dummy">
					<bean:message key="app.home" />
				</html:link>
              </td>
              <!-- link 1 ends -->
			   <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  <!-- link 2 begins -->
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='ManageAdministrativeData.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/ManageAdministrativeData.do?dummy=dummy">
                	<bean:message key="app.administrativeData" />
                </html:link>
              </td>
              <!-- link 2 ends -->
			 <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>

              <!-- link 3 begins -->
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='ManageBioSpecimen.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/ManageBioSpecimen.do?dummy=dummy">
                	<bean:message key="app.biospecimen" />
                </html:link>
              </td>
             <!-- link 3 ends -->
              <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
		      <!-- link 4 begins -->
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()">
                <html:link styleClass="mainMenuLink" page="/RetrieveQueryAction.do">
                	<bean:message key="app.search" />
                </html:link>
              </td>
              <!-- link 4 ends -->
              <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  <!-- link 2 begins -->
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='LoggedInSummary.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/LoggedInSummary.do?dummy=dummy">
                	<bean:message key="app.summary" />
                </html:link>
              </td>
              <!-- link 2 ends -->
			  <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
  		      <!-- link 5 begins -->
              <td height="20" class="mainMenuItemOver" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItemOver'),hideCursor()" >
                 <html:link styleClass="mainMenuLink" target="_NEW"
				href="RedirectToHelp.do">
                	<bean:message key="app.help" />
                </html:link>              </td>
              <!-- link 5 ends -->

              <!--
              <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  <td height="20" class="mainMenuItem mainMenuLink" >
			  <script src="<%=request.getContextPath()%>/jss/titli.js"></script>
              <input style = "font-size:0.9em" value = "TiTLi Search" onclick = "this.value = ''" onblur = "if(this.value=='') {this.value = 'TiTLi Search';}" type ="text" name = "searchString" id = "searchString" onkeypress="return titliOnEnter(event, this, document.getElementById('go'))" />
				<a class = "mainMenuLink"  id="go" href ="TitliInitialiseSearch.do" onclick = "this.href= this.href + '?searchString='+document.getElementById('searchString').value">Go</a>
              </td> -->

              <td>
              	<img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" />
              </td>
			</tr>
          </table>