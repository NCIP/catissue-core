<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>


<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
		 <tr>
			<td align="right" valign="middle" width="100%">
				<img src="images/uIEnhancementImages/siteman_logo.gif" alt="Siteman Cancer Center" width="198" height="54" vspace="5" />
				<img src="images/uIEnhancementImages/or_dot.gif" width="1" height="50" hspace="10" vspace="5" />
				<img src="images/uIEnhancementImages/cabig_logo.jpg" alt="caBIG" width="182" height="62" />
			</td>
		</tr>
	
	
	</table>
</logic:empty>
<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
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
			  <logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
              <td height="20" class="mainMenuItemSelected" onclick="document.location.href='ManageAdministrativeData.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/ManageAdministrativeData.do?dummy=dummy">
                	<bean:message key="app.administrativeData" />
                </html:link>
              </td>
			  </logic:notEmpty>
              <!-- link 2 ends -->
			 <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  
              <!-- link 3 begins -->
			  <logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='ManageBioSpecimen.do?dummy=dummy'">
                <html:link styleClass="mainMenuLink" page="/ManageBioSpecimen.do?dummy=dummy">
                	<bean:message key="app.biospecimen" />
                </html:link>
              </td>
			  </logic:notEmpty>
             <!-- link 3 ends -->
              <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
		      <!-- link 4 begins -->
			  <logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()">
                <html:link styleClass="mainMenuLink" page="/RetrieveQueryAction.do">
                	<bean:message key="app.search" />
                </html:link>
              </td>
			  </logic:notEmpty>
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
              <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" >
                <html:link styleClass="mainMenuLink" target="_NEW"
				href="RedirectToHelp.do">
                	<bean:message key="app.help" />
                </html:link>
              </td>
              <!-- link 5 ends -->
              <td>
              	<img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" />
              </td>
			</tr>
          </table>
	</logic:notEmpty>
