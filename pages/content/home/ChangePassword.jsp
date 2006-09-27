<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.beans.SessionDataBean"%>

<%
	String operation = (String) request.getAttribute(Constants.OPERATION);
	String pageOf = (String) request.getAttribute(Constants.PAGEOF);
	SessionDataBean sessionData = null;
	if(session.getAttribute(Constants.TEMP_SESSION_DATA) != null) 
	{
	sessionData = (SessionDataBean)session.getAttribute(Constants.TEMP_SESSION_DATA);
	} else 
	{
	sessionData = (SessionDataBean)session.getAttribute(Constants.SESSION_DATA);
	}
	String userId = sessionData.getUserId().toString();
%>

<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>

	<script language="JavaScript">
	
	   function onSubmitButtonClicked()
		{
			var action = '<%=Constants.UPDATE_PASSWORD_ACTION%>?access=denied';
			document.forms[0].action = action;
		    document.forms[0].submit();
		}
		
	</script>
</head>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.UPDATE_PASSWORD_ACTION%>">
	<!-- CHANGE PASSWORD BEGINS -->
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
						<html:hidden property="pageOf" value="<%=pageOf%>" />
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="id" value="<%=userId%>" />
					</td>
				</tr>

				<tr>
					<td class="formMessage" colspan="3">* indicates a required field</td>
				</tr>
					
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="user.changePassword"/>
					</td>
				</tr>

				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="oldPassword">
							<bean:message key="user.oldPassword" />
						</label>
					</td>
					<td class="formField">
						<html:password styleClass="formFieldSized" size="30" styleId="oldPassword" property="oldPassword" />
					</td>
				</tr>

				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="newPassword">
							<bean:message key="user.newPassword" />
						</label>
					</td>
					<td class="formField">
						<html:password styleClass="formFieldSized" size="30" styleId="newPassword" property="newPassword" />
					</td>
				</tr>
			
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="confirmNewPassword">
							<bean:message key="user.confirmNewPassword" />
						</label>
					</td>
					<td class="formField">
						<html:password styleClass="formFieldSized" size="30" styleId="confirmNewPassword" property="confirmNewPassword" />
					</td>
				</tr>
	
				<tr>
					<td align="right" colspan="3">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton" onclick="onSubmitButtonClicked()">
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
	<!-- CHANGE PASSWORD BEGINS-->	
	</html:form>
</table>

		