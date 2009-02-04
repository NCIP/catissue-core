<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName = Constants.INSTITUTION_ADD_ACTION;
		
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		
        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.INSTITUTION_EDIT_ACTION;
            readOnlyValue = true;
        }
        else//Add
        {
            formName = Constants.INSTITUTION_ADD_ACTION;
            readOnlyValue = false;
        }
%>		
			
<html:errors/> 
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=formName%>">
<!-- NEW Institution REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td>
				<html:hidden property="operation" value="<%=operation%>"/>
				<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
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
			 	<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="institution.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
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