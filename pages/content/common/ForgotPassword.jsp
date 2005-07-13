<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
	String operation = (String)request.getAttribute(Constants.OPERATION);
%>
	<html:errors/>
    
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="/ForgotPasswordSearch">	
			 <tr>
				 <td> <html:hidden property="operation"  value="<%=operation%>"/></td>
			 </tr>		
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
                 <br/>
				 <tr> 
		            <td colspan="3" class="formMessage"><bean:message  key="forgotpassword.subtitle" /></td>
                 </tr>
				 <tr>
				     <td class="formTitle" height="20" colspan="3"><bean:message key="forgotpassword.title"/></td>
				 </tr>
				 
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel"><label for="loginName"><bean:message key="user.loginName"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="loginName" property="loginName"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel"><label for="email"><bean:message key="user.email"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="email" property="email"/></td>
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
							<td>
								<html:reset styleClass="actionButton">
									<bean:message  key="buttons.reset" />
								</html:reset>
							</td> 
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

