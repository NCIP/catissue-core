
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>


<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script language="JavaScript">
		function onCpChange(element)
		{
			var action = "showCpAndParticipants.do";
			document.forms[0].action = action;

			document.forms[0].submit();

		}
		
		function onParticipantChange(element)
		{
			var cpId = document.getElementById("cpId");
			var participantId = document.getElementById("participantId");
			window.parent.frames[2].location = "QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=edit&<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value+"&id="+participantId.value;
			window.parent.frames[1].location = "showTree.do?<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+participantId.value+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value;			
			
			
		}
		
		function RegisterParticipants()
		{
			var cpId = document.getElementById("cpId");
			if(cpId.value == "-1")
			{
				alert("please select collection protocol.");
			}
			else
			{
				document.forms[0].action="CPQueryCollectionProtocolRegistration.do?<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value+"&pageOf="+<%=Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY%>;
				document.forms[0].submit();
			}
		}	
		

</script>	
	
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="showCpAndParticipants.do">

<table summary="" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder">
			Collection Protocol :
		</td>
	</tr>		
	<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
		<td class="formField" nowrap>
			<html:select property="cpId" styleClass="formFieldSized15" styleId="cpId" size="1" onchange="onCpChange(this)"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.CP_LIST%>" labelProperty="name" property="value"/>
			</html:select>
			
			
		</td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder">
			Participant :
		</td>
	</tr>		
	
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
		<td class="formField" nowrap>

			<html:select property="participantId" styleClass="formFieldSized15" styleId="participantId" size="7" onchange="onParticipantChange(this)"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.REGISTERED_PARTICIPANT_LIST%>" labelProperty="name" property="value"/>
			</html:select>

		</td>
	</tr>
</table>		
</html:form>