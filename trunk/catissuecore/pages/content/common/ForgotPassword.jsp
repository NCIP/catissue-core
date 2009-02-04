<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

	<html:errors/>
    
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="/ForgotPasswordSearch">	
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
                 <br/>
				 <tr> 
		            <td colspan="3" class="formMessage">
						<font color="#000000" size="2" face="Verdana">
							<bean:message  key="forgotpassword.subtitle" />
						</font>
					</td>
                 </tr>
				 <tr>
				     <td class="formTitle" height="20" colspan="3"><bean:message key="forgotpassword.title"/></td>
				 </tr>
				 
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
						<label for="emailAddress">
							<bean:message key="user.emailAddress"/>
						</label>
					 </td>
				     <td class="formField">
					 	<html:text styleClass="formFieldSized" size="30" styleId="emailAddress" property="emailAddress"/>
					 </td>
				 </tr>
				 <tr>
				  <td align="right" colspan="3">

			<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
								<html:submit styleClass="actionButton">
									<bean:message  key="buttons.send" />
								</html:submit>
							</td>
							<%-- td>
								<html:reset styleClass="actionButton" >
									<bean:message  key="buttons.reset" />
								</html:reset>
							</td --%>
						</tr>
					</table>
			<!-- action buttons end -->

				  </td>
				 </tr>
				</table>
				
			  </td>
			 </tr>
			 </html:form>
			 </table>

