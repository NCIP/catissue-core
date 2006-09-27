<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="java.util.*"%>


<head>
<script language="JavaScript" type="text/javascript"
	src="jss/javascript.js"></script>
</head>

<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>

<%Collection specimenCollection = (Collection) request
					.getAttribute(Constants.SAVED_SPECIMEN_COLLECTION);
%>

<html:errors />

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="660">
		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"
				width="660">

				<tr>
					<td class="formTitle" height="20" colspan="2"><bean:message
						key="multipleSpecimen.report.specimens" /></td>
				</tr>
				<tr>
					<td class="formSerialNumberField" width="5">#</td>
					<td class="formField"><bean:message key="specimen.label" /></td>
				</tr>
				<%int i = 0;
				Iterator specimenItr = specimenCollection.iterator();
				while (specimenItr.hasNext())
				{
					i++;
					Specimen specimen = (Specimen) specimenItr.next();

					%>
				<tr>
					<td class="formSerialNumberField" width="5"><%=i%></td>
					<td class="formField">&nbsp;<%=specimen.getLabel()%></td>
				</tr>
				<%}

			%>
			</table>
			</td>
		</tr>
	</table>
</html:form>
