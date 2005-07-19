<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables,edu.wustl.catissuecore.util.global.Constants"%>

	<html:errors/>
    
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="/ReportedProblemAdd">		

		   <%
				String toAddress = Variables.toAddress;
		   %>
  	    	 		   	
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	 <tr>
				 	<td>
						<html:hidden property="operation" />
			     	</td>
			 	 </tr>	
			 	 
                 <tr> 
		            <td colspan="3" class="formMessage">
		            	<bean:message  key="pagesubtitle.contactus" />
		            </td>
                 </tr>
                 
				 <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="reportproblem.title"/>
				     </td>
				 </tr>
					
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="from"><o:><bean:message  key="fields.to" />:</o:></label>
				     </td>
				     <td class="formField"><%=toAddress%></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel"><label for="from"><bean:message key="fields.from"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="from" property="from"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel"><label for="subject"><bean:message key="fields.title"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="subject" property="subject"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel"><label for="messageBody"><bean:message key="fields.message" />:</label></td>
				     <td class="formField"><html:textarea styleClass="formFieldSized" property="messageBody" styleId="messageBody" cols="32" rows="2"/></td>
				 </tr>
				 <tr>
				  <td align="right" colspan="3">

			<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<%
									String setOperation = "setOperation('"+Constants.ADD+"')";
								%>
								<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
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

