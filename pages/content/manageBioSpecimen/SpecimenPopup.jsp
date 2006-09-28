<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MultipleSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

<%MultipleSpecimenForm form = (MultipleSpecimenForm) request
					.getAttribute("multipleSpecimenForm");

		String submitFunName;
		boolean readOnlyValue=false,readOnlyForAll=false;
		Map map = form.getExternalIdentifier();
		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows = form.getBhCounter();
		}

		List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);

			String action = Constants.NEW_MULTIPLE_SPECIMEN_ACTION;
%>

<html:errors />

<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>
	<html:form action="<%=action%>">
<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">

	<logic:equal name="output" value="success">
		<script language="JavaScript" type="text/javascript">self.close(); 
	</script>
	</logic:equal>
	<logic:equal name="output" value="init">
		<script language="JavaScript" type="text/javascript">window.focus(); 
	</script>
	</logic:equal>


		<input type="hidden" id="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
			name="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
			value="<%= request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY) %>" />
		<html:hidden property="type"/>
		<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0">
			<logic:equal name="type" value="<%=Constants.EXTERNALIDENTIFIER_TYPE%>">
				<%submitFunName = submitExternalIdentifiers();%>
				<%@ include file="ExternalIdentifiers.jsp" %>
			</logic:equal>
			<logic:equal name="type" value="<%=Constants.BIOHAZARD_TYPE%>">
				<%submitFunName = submitBioHazards();%>
				<%@ include file="BioHazards.jsp" %>
			</logic:equal>
	
  			<tr>
				<td align="right" colspan="3"><!-- action buttons begins -->
				<table cellpadding="4" cellspacing="0" border="0">
					<tr>
						<td>
						<html:submit styleClass="actionButton"
							onclick="<%=submitFunName%>">
							<bean:message key="buttons.submit" />
						</html:submit>
						
						</td>
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


</table>
	</html:form>