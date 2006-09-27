<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables,edu.wustl.common.beans.SessionDataBean;"%>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
    <tr>
      <td height="100%">
        <!-- target of anchor to skip menus --><a name="content" />
        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
          <tr>
            <td>
            
              <!-- welcome begins -->
              <table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
                <tr>
                	<td class="welcomeTitle" height="20">
                		<bean:message key="app.welcomeNote" arg0="<%=ApplicationProperties.getValue("app.name")%>"
                											arg1="<%=ApplicationProperties.getValue("app.version")%>"
															arg2="<%=Variables.applicationCvsTag%>"/>
					</td>
                </tr>
                <tr>
                	<td>
                  		<img src="images/HomeImage.jpg" alt="<%=ApplicationProperties.getValue("app.name")%>" border="0" />
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
                              	<td>
                              		<html:submit styleClass="actionButton">
                              			<bean:message key="app.login"/>
                              		</html:submit>
                              	</td>
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
								<TD class="welcomeContent">
		
									<%Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
									  if(obj!=null)
									  {
									      SessionDataBean sessionData = (SessionDataBean) obj;
									 %>
									
							         Dear <%=sessionData.getLastName()%>, &nbsp;<%=sessionData.getFirstName()%><br>
									 <%}%>			
				                		<bean:message key="app.welcomeNote" arg0="<%=ApplicationProperties.getValue("app.name")%>"
				                											arg1="<%=ApplicationProperties.getValue("app.version")%>"
																			arg2="<%=Variables.applicationCvsTag%>"/>
								</TD>
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