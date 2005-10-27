<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.query.Operator"%>

<%
	String principalInvestigator = "value(SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	String title = "value(SpecimenProtocol:TITLE)";
	String shortTitle = "value(SpecimenProtocol:SHORT_TITLE)";
	String irbIdentifier = "value(SpecimenProtocol:IRB_IDENTIFIER)";
	String startDate = "value(SpecimenProtocol:START_DATE)";
	String startDate2 = "value(Participant:START_DATE:HLIMIT)";
	String endDate = "value(SpecimenProtocol:END_DATE)";
	String endDate2 = "value(Participant:END_DATE:HLIMIT)";
	String protocolParticipantId = "value(CollectionProtocolRegistration:PROTOCOL_PARTICIPANT_IDENTIFIER)";
	String regDate = "value(SpecimenProtocol:REGISTRATION_DATE)";
	String regDate2 = "value(Participant:REGISTRATION_DATE:HLIMIT)";
	
	
	String opPrincipalInvestigator = "value(Operator:SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	String opTitle = "value(Operator:SpecimenProtocol:TITLE)";
	String opShortTitle = "value(Operator:SpecimenProtocol:SHORT_TITLE)";
	String opIrbIdentifier = "value(Operator:SpecimenProtocol:IRB_IDENTIFIER)";
	String opStartDate = "value(Operator:SpecimenProtocol:START_DATE)";
	String opEndDate = "value(Operator:SpecimenProtocol:END_DATE)";
	String opProtocolParticipantId = "value(Operator:CollectionProtocolRegistration:PROTOCOL_PARTICIPANT_IDENTIFIER)";
	String opRegDate = "value(Operator:SpecimenProtocol:REGISTRATION_DATE)";
%>

<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script language="JavaScript">
		function onAddRule(action)
		{
			document.forms[0].action=action;
			document.forms[0].sumbit();
		}
		function onDate()
		{
			var dateCombo = document.getElementById("startDate");
			
			if(dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.BETWEEN%>" || dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.NOT_BETWEEN%>")
			{
				show_calendar('advanceSearchForm.startDate2',null,null,'MM-DD-YYYY')
			}
		}
		function onDateOperatorChange(element)
		{
			var dateTxt2  = document.getElementById("startDate2");
			
			if(element.value == "<%=Operator.BETWEEN%>" || element.value == "<%=Operator.NOT_BETWEEN%>")
			{
				dateTxt2.disabled = false;
				dateTxt2.value = "";
			}
			else
			{
				dateTxt2.value = "";
				dateTxt2.disabled = true;
			}
		}
		function onDate()
		{
			var dateCombo = document.getElementById("endDate");
			
			if(dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.BETWEEN%>" || dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.NOT_BETWEEN%>")
			{
				show_calendar('advanceSearchForm.endDate2',null,null,'MM-DD-YYYY')
			}
		}
		
		function onDateOperatorChange(element)
		{
			var dateTxt2  = document.getElementById("endDate2");
			
			if(element.value == "<%=Operator.BETWEEN%>" || element.value == "<%=Operator.NOT_BETWEEN%>")
			{
				dateTxt2.disabled = false;
				dateTxt2.value = "";
			}
			else
			{
				dateTxt2.value = "";
				dateTxt2.disabled = true;
			}
		}
	</script>
</head>

<html:errors />

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">
<tr>
	<td><html:hidden property="objectName" value="CollectionProtocol"/></td>
</tr>
<!--  MAIN TITLE ROW -->
<tr>
	
	<td class="formTitle" height="25" nowrap >
	&nbsp;<img src="images/CollectionProtocol.GIF" alt="CollectionProtocol" />&nbsp;<bean:message key="collectionProtocol.queryRule"/>
	</td>
	<td class="formTitle" nowrap align="right" colspan="2">
		<html:submit property="addRule" styleClass="actionButton" >
			<bean:message key="buttons.addRule"/>
		</html:submit>
		
		<%--html:button property="search" styleClass="actionButton" onclick="">
			<bean:message key="buttons.search"/>
		</html:button--%>
		&nbsp;&nbsp;
		<html:button property="resetQuery" styleClass="actionButton" onclick="">
			<bean:message key="buttons.resetQuery"/>
		</html:button>
	</td>
</tr>

<!--  FIRST ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="principalInvestigator">
 			<bean:message key="collectionprotocol.principalinvestigator"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opPrincipalInvestigator%>" styleClass="formFieldSized10" styleId="<%=opPrincipalInvestigator%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=principalInvestigator%>" styleClass="formFieldSized10" styleId="principalInvestigator" size="1">
			<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  SECOND ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="protocoltitle">
 			<bean:message key="collectionprotocol.protocoltitle"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opTitle%>" styleClass="formFieldSized10" styleId="<%=opTitle%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="title" property="<%=title%>"/>
	</td>
</tr>

<!--  THIRD ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="shorttitle">
 			<b><bean:message key="collectionprotocol.shorttitle"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opShortTitle%>" styleClass="formFieldSized10" styleId="<%=opShortTitle%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="shortTitle" property="<%=shortTitle%>"/>
	</td>
</tr>

<!--  FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="irbIdentifier">
			<b><bean:message key="collectionprotocol.irbid"/>
		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opIrbIdentifier%>" styleClass="formFieldSized10" styleId="<%=opIrbIdentifier%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="irbIdentifier" property="<%=irbIdentifier%>"/>
	</td>
</tr>

<!--  FIFTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="startDate">
 			<b><bean:message key="collectionprotocol.startdate"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opStartDate%>" styleClass="formFieldSized10" styleId="opStartDate" size="1" onchange="onDateOperatorChange(this)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
		<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="startDate1" property="<%=startDate%>" />
					 &nbsp;
		<a href="javascript:show_calendar('advanceSearchForm.startDate1',null,null,'MM-DD-YYYY');">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="startDate2" property="<%=startDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate();">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
	</td>
</tr>

<!-- SIXTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="endDate">
 			<b><bean:message key="collectionprotocol.enddate"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opEndDate%>" styleClass="formFieldSized10" styleId="opEndDate" size="1" onchange="onDateOperatorChange(this)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
		<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="endDate1" property="<%=endDate%>" />
					 &nbsp;
		<a href="javascript:show_calendar('advanceSearchForm.startDate1',null,null,'MM-DD-YYYY');">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="endDate2" property="<%=endDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate();">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
	</td>
</tr>
<!-- SEVENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="protocolParticipantId">
 			<bean:message key="collectionProtocolReg.participantProtocolID"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opProtocolParticipantId%>" styleClass="formFieldSized10" styleId="<%=opProtocolParticipantId%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="protocolParticipantId" property="<%=protocolParticipantId%>"/>
	</td>
</tr>


<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="endDate">
 			<b><bean:message key="collectionProtocolReg.participantRegistrationDate"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opRegDate%>" styleClass="formFieldSized10" styleId="opRegDate" size="1" onchange="onDateOperatorChange(this)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
		<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="regDate1" property="<%=regDate%>" />
					 &nbsp;
		<a href="javascript:show_calendar('advanceSearchForm.startDate1',null,null,'MM-DD-YYYY');">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="regDate2" property="<%=regDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate();">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
	</td>
</tr>


<!-- Buttons -->
<tr>
	<td colspan="2">&nbsp</td>
	<td nowrap align="right">
		<html:submit property="addRule" styleClass="actionButton" >
			<bean:message key="buttons.addRule"/>
		</html:submit>
		
		<%--html:button property="search" styleClass="actionButton" onclick="">
			<bean:message key="buttons.search"/>
		</html:button--%>
		&nbsp;&nbsp;
		<html:button property="resetQuery" styleClass="actionButton" onclick="">
			<bean:message key="buttons.resetQuery"/>
		</html:button>
	</td>
</tr>

</table>
</html:form>
