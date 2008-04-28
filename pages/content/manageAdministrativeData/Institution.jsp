<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<html:errors/> 
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action='${requestScope.formName}'>
<!-- NEW Institution REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td>
				<html:hidden property="operation"/>
				<html:hidden property="submittedFor"/>
			</td>
		</tr>
		<tr>
			<td><html:hidden property="id" /></td>
		</tr>

		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
			 	<logic:equal name="operation" value='${requestScope.operationAdd}'>
					<bean:message key="institution.title"/>
				</logic:equal>
				<logic:equal name="operation" value='${requestScope.operationEdit}'>
					<bean:message key="institution.editTitle"/>
				</logic:equal>
			 </td>
		</tr>

		<!-- Name of the institution -->
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="name">
					<bean:message key="institution.name"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="name" property="name"/>
			</td>
		</tr>

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

	 <!-- NEW institution REGISTRATION ends-->
	 
	 </html:form>
 </table>