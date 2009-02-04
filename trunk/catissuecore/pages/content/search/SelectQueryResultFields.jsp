<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<html:form action="/DataView.do">
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">

					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="selectFields.participant.title" />
						</td>
					</tr>

					<tr>
						<td class="formField" rowspan="2">
							<html:textarea styleClass="formFieldSized" rows="3" styleId="participantFields" property="participantFields" />
						</td>
						<td>
							<html:button property="select">
								<bean:message key="selectFields.select" />
							</html:button>
						</td>
						<td class="formField" rowspan="2">
							<html:textarea styleClass="formFieldSized" rows="3" styleId="selectedParticipantFields" property="selectedParticipantFields" />
						</td>
					</tr>
					<tr>
						<td>
							<html:button property="deSelect">
								<bean:message key="selectFields.deSelect" />
							</html:button>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="3">

						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton" value="Submit" />
								</td>
								<%-- td>
									<html:reset styleClass="actionButton" />
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