<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>

<script type="text/javascript">
function onButtonClick(action)
{
	var actionUrl = "ConflictResolver.do?button="+action;
	document.forms[0].action = actionUrl;
	document.forms[0].submit();
	
}
</script>

<body>
<html:form action="ConflictResolver.do">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" id="table1_OrderRequestHeader">
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">				
					<tr>
						<td class="dataCellText" height="20" colspan="5">Participant ID
							<html:text styleClass="formFieldSized3" maxlength="4"  property="participantId"  />
						</td>
						<td class="dataCellText" height="20" colspan="5">SCG ID
							<html:text styleClass="formFieldSized3" maxlength="4"  property="scgId"  />
						</td>
					</tr>
					<tr>				  
						<td class="" height="20" colspan="5">
							<input type="button" value="Create Participant" onclick="onButtonClick('createParticipant')"/>
						</td>
						<td class="" height="20" colspan="5">
							<input type="button" value="Associate Participant" onclick="onButtonClick('associateParticipant')"/>
						</td>
						
					</tr>
				</table>
			</td>
		</tr>
	</table>
			<html:hidden property="reportQueueId" />
</html:form>
</body>