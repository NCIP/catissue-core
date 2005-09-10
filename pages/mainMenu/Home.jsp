<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

	<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20">
            <tr>
              <td width="1"><!-- anchor to skip main menu -->
              	<a href="#content"><img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" /></a>
              </td>
             
              <!-- link 1 begins -->
			  <td height="20" class="mainMenuItemOver" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
				onmouseout="changeMenuStyle(this,'mainMenuItemOver'),hideCursor()" onclick="document.location.href='Home.do'">
				<html:link styleClass="mainMenuLink" page="/Home.do">
					<bean:message key="app.home" />
				</html:link>
			  </td>
			  <!-- link 1 ends -->
			  
			  <!-- Links that need to be shown when user is logged in -->
			  <logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">	
			  <td><img src="images/mainMenuSeparator.gif" width="1" height="16"
				   alt="" /></td>
				   
			  <!-- link 2 begins -->
			  <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
				onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='ManageAdministrativeData.do'">
				<html:link styleClass="mainMenuLink" page="/ManageAdministrativeData.do">
					<bean:message key="app.administrativeData" />
				</html:link>
			  </td>
			  <!-- link 2 ends -->
			
			  <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  <!-- link 3 begins -->
			  <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
				onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='ManageBioSpecimen.do'">
				<html:link styleClass="mainMenuLink" page="/ManageBioSpecimen.do">
					<bean:message key="app.biospecimen" />
				</html:link>
			  </td>
			  <!-- link 3 ends -->
			  
			 <td><img src="images/mainMenuSeparator.gif" width="1" height="16"
			 	 	alt="" /></td>
			 	 	
			 <!-- link 4 begins -->
			 <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
				onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='Search.do'">
				<html:link styleClass="mainMenuLink" page="/Search.do">
					<bean:message key="app.search" />
				</html:link>
			 </td>
			 <!-- link 4 ends -->
		
		
			<td><img src="images/mainMenuSeparator.gif" width="1" height="16"
				alt="" /></td>
			
			 <!-- link 5 begins -->
			 <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='LoggedInSummary.do'">
                <html:link styleClass="mainMenuLink" page="/LoggedInSummary.do">
                	<bean:message key="app.summary" />
                </html:link>
             </td>
             <!-- link 5 ends -->
			 
			 <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  
             <!-- link 6 begins -->
             <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='LoggedInHelp.do'">
               <html:link styleClass="mainMenuLink" page="/LoggedInHelp.do">
               		<bean:message key="app.help" />
               </html:link>
             </td>
             <!-- link 6 ends -->
             
             <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			 </logic:notEmpty>
			<!-- End of links that need to be shown when user is logged in -->
		
			<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">	
			<!-- link 1 ends -->
			<td><img src="images/mainMenuSeparator.gif" width="1" height="16"
				alt="" /></td>
			
			<!-- link 2 begins -->
			<td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='Summary.do'">
                <html:link styleClass="mainMenuLink" page="/Summary.do">
                	<bean:message key="app.summary" />
                </html:link>
              </td>
              <!-- link 2 ends -->
			<td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
			  
            <!-- link 3 begins -->
            <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='Help.do'">
                <html:link styleClass="mainMenuLink" page="/Help.do">
                	<bean:message key="app.help" />
                </html:link>
            </td>
            <!-- link 3 ends -->
            
            <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
             </logic:empty> 
			</tr>
          </table>