<%@ tag language="java" isELIgnored="false"%>
<%@ attribute name="participant" required="true"
	type="edu.wustl.catissuecore.domain.Participant"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tr>
	<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
			bundle="msg.ccts" key="DataQueue.field.existentParticipant" />
	</td>
	<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
		class="black_ar_b"><html:link
				title="Click here to see the existent participant's data."
				href="SearchObject.do?pageOf=pageOfParticipant&operation=search&id=${participant.id}">
				<c:out value="${participant.firstName}"></c:out>
				<c:out value="${participant.middleName}"></c:out>
				<c:out value="${participant.lastName}"></c:out>
			</html:link> </span>
</tr>

