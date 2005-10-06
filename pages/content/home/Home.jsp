<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
    <tr>
      <td height="100%">
        <!-- target of anchor to skip menus --><a name="content" />
        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
          <tr>
            <td>
            
              <!-- welcome begins -->
              <table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
                <tr><td class="welcomeTitle" height="20">Welcome to caTISSUE Core</td>
                </tr>
                <tr>
                  <td class="welcomeContent" valign="top">
				  <ul>
					<li>
						<bean:message key="home.text.bullet1"/>
					</li>
					<li>
						<bean:message key="home.text.bullet2"/>
					</li>
                  </td>
                </tr>
              </table>
              <!-- welcome ends -->
            
            </td>
            <td valign="top" halign="right">
              
              <!-- sidebar begins -->
              <table summary="" cellpadding="0" cellspacing="0" border="0" width="220" height="100%">
                
                <!-- login begins -->
                <tr>
                  <td valign="top">
				   <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">
					
                      <tr>
                        <td class="sidebarTitle" height="20">
                        </td>
                      </tr>

                      <tr>
                        <td class="sidebarContent">
                          <table cellpadding="2" cellspacing="0" border="0">
                          <html:errors />
						  <logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">	
						   <html:form action="Login.do">
					
                            <tr>
                              <td class="sidebarLogin" align="right">
                              	<label for="loginName">
                              		<bean:message key="app.loginId"/>
								</label>
							  </td>
                              
                              <td class="formFieldLogin">
                                 	<html:text styleClass="formField" property="loginName" size="14" />
                              </td>
                            </tr>


                            <tr>
                              <td class="sidebarLogin" align="right">
                              	<label for="password">
                              		<bean:message key="app.password"/>
                              	</label>
                              </td>
                              
                              <td class="formFieldLogin">
                              	<html:password styleClass="formField" property="password" size="14" />
                              </td>
                            </tr>


                            <tr>
							 <td>&nbsp;</td>	
                              <td><html:submit styleClass="actionButton"><bean:message key="app.login"/></html:submit></td>
                            </tr>
					

						    <tr>
						      <td>
						      	<div>
						      		<a class="loginSignup" href="SignUp.do?operation=add&amp;pageOf=pageOfSignUp">
						      			<bean:message key="app.signup"/>
						      		</a>
						      	</div>
						      </td>	
						    </tr>
	
						    <tr>
						      <td colspan="2">
						      	<a class="loginSignup" href="ForgotPassword.do">
						      		<bean:message key="app.requestPassword"/>
						      	</a>
						      </td>
						    </tr>	
		

						</html:form>
						</logic:empty>
						<logic:notEmpty scope="session"	name="<%=Constants.SESSION_DATA%>">
								<tr>
									<TD class="welcomeContent"><bean:message key="app.welcomeNote"/></TD>
								</tr>			
						</logic:notEmpty>
                          </table>
                        </td>
                      </tr>
					 
                    </table>
                  </td>
                </tr>
                <!-- login ends -->
                
                <!-- spacer cell begins (keep for dynamic expanding)-->
                <tr>
                <td valign="top" height="100%">
                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
                    <tr>
                      <td class="sidebarContent" valign="top">&nbsp;</td>
                    </tr>
                    </table>
				</td>
				</tr>
                <!-- spacer cell ends -->
												
              </table>
              <!-- sidebar ends -->
              
            </td>
          </tr>
        </table>
      </td>
    </tr>
</table>