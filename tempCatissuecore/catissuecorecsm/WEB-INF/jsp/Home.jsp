<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import="gov.nih.nci.common.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title><s:text name="home.title"/></title>
<link rel="stylesheet" type="text/css" href="styleSheet.css" />
<script src="script.js" type="text/javascript"></script>
</head>
<body>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">

<%@ include file="include/header.inc" %>	
	
  <tr>
    <td height="100%" align="center" valign="top">
      <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="771">

<%@ include file="include/applicationHeader.inc" %>

        <tr>
          <td valign="top">
            <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
              <tr>
                <td height="20" class="mainMenu">

<%@ include file="include/mainMenu.inc" %>                

                </td>
              </tr>
              
<!--_____ main content begins _____-->
              <tr>
                <td valign="top">
                  <!-- target of anchor to skip menus --><a name="content" />
                  <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">
                    
                    <!-- banner begins -->
                    <tr>
                      <td class="bannerHome"><img src="images/bannerHome2.gif"></td>
                    </tr>
                    <!-- banner begins -->
                    
                    <tr>
                      <td height="100%">
                        
												<!-- target of anchor to skip menus --><a name="content" />
												
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                          <tr>
                            <td width="70%">
                            
                              <!-- welcome begins -->
                              <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                                <tr><td class="welcomeTitle" height="20"><s:text name="home.welcome.title"/></td>
                                </tr>
                                <tr>
                                  <td class="welcomeContent" valign="top">
										<s:text name="home.welcome.intro"/>
										<br> 
										<br>
										<ul>
											<li>UML Modeling</li>
											<li>N-tier architecture with open API's</li>
											<li>Controlled vocabularies</li> 
											<li>Registered metadata</li>
										</ul>
	
										A system built along these lines is said to be 'semantically integrated', that is, 
										there exists runtime metadata that describes every class and class attribute in the API.<br> 
										<br>
										The SDK consists of three components. The <b>Semantic Connector</b> assists in the 
										annotation of the UML with appropriate controlled vocabulary terms. The <b>UML Loader</b>
										registers the annotated model in the caDSR, a metadata repository, and the <b>Code Generator</b>
										creates the software system in Java.<br>
										<br>
										References:<br>
										
										<ul>
											<li><a href="https://mail.nih.gov/exchweb/bin/redir.asp?URL=http://gforge.nci.nih.gov/projects/cacoresdk/" >caCORE SDK GForge site</a> - Contains news, information, documents, defects, feedback, and reports</li>
											<li><a href="https://mail.nih.gov/exchweb/bin/redir.asp?URL=http://ncicb.nci.nih.gov/infrastructure/cacore_overview/cacoresdk" >caCORE SDK Download site</a> - Contains documents, information, and downloads for the caCORE SDK</li>
											<li><a href="ftp://ftp1.nci.nih.gov/pub/cacore/SDK/v3.2/caCORE_SDK_32_notes.txt" >caCORE 3.2 Release Notes</a> - Release Notes for all caCORE products for version 3.2</li>
											<li><a href="#" >caCORE 3.2 javadocs</a> - Coming soon!!!</li>
										</ul>
									</td>
                                </tr>
                              </table>
                              <!-- welcome ends -->
                            
                            </td>
                            <td valign="top" width="30%">
                              
                              <!-- sidebar begins -->
	                              <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
	                                
	                                <!-- login/continue form begins -->
	                                <tr>
	                                  <td valign="top">
	                                          
<%	gov.nih.nci.system.server.mgmt.SecurityEnabler securityEnabler =  new gov.nih.nci.system.server.mgmt.SecurityEnabler(Constant.APPLICATION_NAME);
	if (securityEnabler.getSecurityLevel() > 0)	      
	{%>
                                  
	                                    <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">	
	                                      <tr>
	                                        <td class="sidebarTitle" height="20"><s:text name="home.login"/></td>
	                                      </tr>
	                                      <tr>
	                                        <td class="sidebarContent">
	                                        <s:form method="post" action="Login.action" name="loginForm" theme="css_xhtml">   	
	                                          <table cellpadding="2" cellspacing="0" border="0">
	                                          
	                                            <tr>
	                                              <td class="sidebarLogin" align="left"><s:text name="home.loginID"/></td>
	                                              <td class="formFieldLogin"><s:textfield name="username" cssClass="formField" size="14" /></td>
	                                            </tr>
	                                            <tr>
	                                              <td class="sidebarLogin" align="left"><s:text name="home.password"/></td>
	                                              <td class="formFieldLogin"><s:password name="password" cssClass="formField" size="14" /></td>
	                                            </tr>
	                                            <tr>
	                                              <td>&nbsp;</td>
	                                              <td><s:submit cssClass="actionButton" type="submit" value="Login" /></td>
	                                            </tr>
	                                          </table>
											</s:form>		                                          
	                                        </td>
	                                      </tr>	  
	                                    </table>	                                       
	<%} else {%>

	                                    <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">							  			
	                                      <tr>
	                                        <td class="sidebarTitle" height="20">SELECT CRITERIA</td>
	                                      </tr>
	                                      <tr>
	                                        <td class="sidebarContent" align="center">
								  				<s:form method="post" action="ShowDynamicTree.action" name="continueForm" theme="simple"> 	                                        
		                                        	<s:submit cssClass="actionButton" value="Continue" theme="simple"/>
		                                        </s:form>  	
	                                        </td>
	                                      </tr>	  
	                                    </table>                                                                  	
	<%} %>
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
	                                        <td class="sidebarContent">
												<ul>
													<li>New User Interface</li>
													<li>New hierarchical package/class browsing</li>
													<li>New single-session authentication</li>
												</ul>

											</td>
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
	                                      <td class="sidebarContent" valign="top">&nbsp;</td>
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
                </td>
              </tr>
<!--_____ main content ends _____-->
              
              <tr>
                <td height="20" class="footerMenu">
                
                  <!-- application ftr begins -->
					<%@ include file="include/applicationFooter.inc" %>                  
                  <!-- application ftr ends -->
                  
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>
    
<%@ include file="include/footer.inc" %>
    
    </td>
  </tr>
</table>
</body>
</html>
