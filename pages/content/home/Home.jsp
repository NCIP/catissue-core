<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                    
                    <!-- banner begins -->
                    <tr>
                      <td class="bannerHome"><img src="images/bannerHome.gif" height="140"></td>
                    </tr>
                    <!-- banner begins -->
                    
                    <tr>
                      <td height="100%">
                        <!-- target of anchor to skip menus --><a name="content" />
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                          <tr>
                            <td>
                            
                              <!-- welcome begins -->
                              <table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
                                <tr><td class="welcomeTitle" height="20">WELCOME TO caTISSUE Core</td>
                                </tr>
                                <tr>
                                  <td class="welcomeContent" valign="top">
								  <ul>
									<li><strong>This is caTISSUE Core Home Page</strong></li>
									<li>CaTISSUE Core allows for the collection, storage, processing, and distribution of multiple types of biospecimens from a large number of intramural and multi-institutional efforts.</li>
                                  </td>
                                </tr>
                              </table>
                              <!-- welcome ends -->
                            
                            </td>
                            <td valign="top">
                              
                              <!-- sidebar begins -->
                              <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                                
                                <!-- login begins -->
                                <tr>
                                  <td valign="top">
								   <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">
									
                                      <tr>
                                        <td class="sidebarTitle" height="20">
                                        	<bean:message key="app.login.title"/>
                                        </td>
                                      </tr>


                                      <tr>
                                        <td class="sidebarContent">
                                          <table cellpadding="2" cellspacing="0" border="0">
										   <form action="dashboard.jsp">
                                            <tr>
                                              <td class="sidebarLogin" align="right">
                                              	<label for="loginID">
                                              		<bean:message key="app.loginId"/>
												</label>
											  </td>
                                              
                                              <td class="formFieldLogin">
                                              	<input class="formField" type="text" name="loginID" size="14" />
                                              </td>
                                            </tr>


                                            <tr>
                                              <td class="sidebarLogin" align="right">
                                              	<label for="password">
                                              		<bean:message key="app.password"/>
                                              	</label>
                                              </td>
                                              
                                              <td class="formFieldLogin">
                                              	<input class="formField" type="password" name="password" size="14" />
                                              </td>
                                            </tr>


                                            <tr>
											 <td>&nbsp;</td>	
                                              <td><input class="actionButton" type="submit" value="Login"/></td>
                                            </tr>
									

					    <tr>
					      <td>
					      	<div>
					      		<a class="loginSignup" href="User.do?operation=add">
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
						

										</form>
                                          </table>
                                        </td>
                                      </tr>
									 
                                    </table>
                                  </td>
                                </tr>
                                <!-- login ends -->
                                
                                <!-- what's new begins -->
                                <tr>
                                  <td valign="top">
                                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" class="sidebarSection">
                                      <tr>
                                        <td class="sidebarTitle" height="20">WHAT'S NEW</td>
                                      </tr>
                                      <tr>
                                        <td class="sidebarContent">What's new message. What's new message. What's new message. What's new message. What's new message. </td>
                                      </tr>
                                    </table>
                                  </td>
                                </tr>
                                <!-- what's new ends -->
                                
                                <!-- did you know? begins -->
                                <tr>
                                  <td valign="top">
                                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
                                    <tr>
                                      <td class="sidebarTitle" height="20">DID YOU KNOW?</td>
                                    </tr>
                                    <tr>
                                      <td class="sidebarContent" valign="top">Did you know message. Did you know message. Did you know message. Did you know message. </td>
                                    </tr>
                                    </table>
                                  </td>
                                </tr>
                                <!-- did you know? ends -->
                                
                                <!-- spacer cell begins (keep for dynamic expanding) -->
                                <tr><td valign="top" height="100%">
                                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
                                    <tr>
                                      <td class="sidebarContent" valign="top">&nbsp;</td>
                                    </tr>
                                    </table>
																</td></tr>
                                <!-- spacer cell ends -->
																
                              </table>
                              <!-- sidebar ends -->
                              
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>