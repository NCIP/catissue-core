<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MultipleSpecimenForm"%>

<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

<%MultipleSpecimenForm form = (MultipleSpecimenForm) request
					.getAttribute("multipleSpecimenForm");
			String action = Constants.NEW_MULTIPLE_SPECIMEN_ACTION;
%>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">

	<html:form action="<%=action%>">

		<input type="hidden" id="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
			name="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
			value="<%= request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY) %>" />

		<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0">
			<tr>
				<td class="formTitle" height="20" colspan="3"><logic:equal
					name="operation" value="<%=Constants.ADD%>">
					<bean:message key="specimen.comments" />
				</logic:equal> <logic:equal name="operation"
					value="<%=Constants.EDIT%>">
					<bean:message key="specimen.comments" />
				</logic:equal></td>
			</tr>

			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formRequiredLabel"><label for="name"><bean:message
					key="specimen.comments" /> </label></td>
				<td class="formField"><html:textarea styleClass="formFieldSized"
					rows="3" styleId="comments" property="comments" /><%form.getComments();

				%></td>
			</tr>
			<tr>
				<td align="right" colspan="3"><!-- action buttons begins -->
				<table cellpadding="4" cellspacing="0" border="0">
					<tr>
						<td><html:submit styleClass="actionButton"
							onclick="submitComments();">
							<bean:message key="buttons.submit" />
						</html:submit></td>
						<td><html:reset styleClass="actionButton" onclick="self.close();">
							<bean:message key="buttons.cancel" />
						</html:reset></td>
					</tr>
				</table>
				<!-- action buttons end --></td>
			</tr>
		</table>
		</td>
		</tr>
	</html:form>

	<logic:equal name="output" value="success">
		<script language="JavaScript" type="text/javascript">self.close(); 
	</script>
	</logic:equal>
	<logic:equal name="output" value="init">
		<script language="JavaScript" type="text/javascript">window.focus(); 
	</script>
	</logic:equal>
</table>
