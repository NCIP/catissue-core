<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables,edu.wustl.catissuecore.util.global.Constants"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName,prevPage=null,nextPage=null;
		boolean readOnlyValue;
		if (operation.equals(Constants.EDIT))
        {
            formName = Constants.REPORTED_PROBLEM_EDIT_ACTION;
			Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
			prevPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
			identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.REPORTED_PROBLEM_ADD_ACTION;
            readOnlyValue = false;
        }
%>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

	<html:errors/>
	<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
		<%=messageKey%>
	</html:messages>
	
    
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=formName%>">		
		   	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
  	    	  <tr>
			  	<td align="right" colspan="3">

					<!-- action buttons begins -->
					<table cellpadding="6" cellspacing="2" border="0">
						<tr>
							<% 
								String backPage = Constants.REPORTED_PROBLEM_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
							%>
							<td>
								<a class="contentLink" href="<%=backPage%>">Reported Problem Home</a>
							</td>
							
							<td>
								<logic:notEmpty name="prevpage">
									<a class="contentLink" href="<%=prevPage%>">
										<bean:message key="approveUser.previous"/>
									</a>
								</logic:notEmpty> |
								<logic:notEmpty name="nextPage">
									<a class="contentLink" href="<%=nextPage%>">
										<bean:message key="approveUser.next"/>
									</a>
								</logic:notEmpty> 
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
						
				</td>
			</tr>		   	
			</logic:equal>
	    	 <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	 <tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
					</td>
				</tr>	
				
				<tr>
					<td>
						<html:hidden property="id" />
					</td>
				</tr>
				
				 <logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
                 <tr> 
		            <td colspan="3" class="formMessage">
		            	<font color="#000000" size="2" face="Verdana">
		            		<bean:message  key="pagesubtitle.contactus" />
		            	</font>
		            </td>
                 </tr>
				</logic:equal>                 
				 <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="reportProblem.title"/>
				     </td>
				 </tr>
					
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
				     	<label for="from">
				     		<bean:message key="fields.from"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized"  maxlength="255"  size="30" styleId="from" property="from" readonly="<%=readOnlyValue%>"/>
				     </td>
				 </tr>
<!-- nameOfReporter -->
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
				     	<label for="nameOfReporter">
				     		<bean:message key="fields.nameofreporter"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="nameOfReporter" property="nameOfReporter" readonly="<%=readOnlyValue%>"/>
				     </td>
				 </tr>
<!-- affiliation -->
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
				     	<label for="affiliation">
				     		<bean:message key="fields.affiliation"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="affiliation" property="affiliation" readonly="<%=readOnlyValue%>"/>
				     </td>
				 </tr>

				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
				     	<label for="subject">
				     		<bean:message key="fields.title"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized"  maxlength="255" size="30" styleId="subject" property="subject" readonly="<%=readOnlyValue%>"/>
				     </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">*</td>
				     <td class="formRequiredLabel">
				     	<label for="messageBody">
				     		<bean:message key="fields.message" />
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:textarea styleClass="formFieldSized" property="messageBody" styleId="messageBody" cols="150" rows="7" readonly="<%=readOnlyValue%>"/>
				     </td>
				 </tr>
				 <logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				 <tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="activityStatus">
							<bean:message key="reportProblem.status" />
						</label>
					</td>
					<td class="formField">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized" styleId="activityStatus" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="activityStatusList" labelName="activityStatusList" />
						</html:select>
					</td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="comments">
							<bean:message key="approveUser.comments" />
						</label>
					</td>
					<td class="formField">
						<html:textarea styleClass="formFieldSized" rows="10" styleId="comments" property="comments" />
					</td>
				</tr> 
				</logic:equal>
				<tr>
				  <td align="right" colspan="3">

			<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
								<html:submit styleClass="actionButton">
									<bean:message  key="buttons.submit" />
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

