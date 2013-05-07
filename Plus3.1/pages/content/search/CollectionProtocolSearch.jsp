<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.query.Operator"%>

<%
	String principalInvestigator = "value(SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	String title = "value(SpecimenProtocol:TITLE)";
	String shortTitle = "value(SpecimenProtocol:SHORT_TITLE)";
	String irbIdentifier = "value(SpecimenProtocol:IRB_IDENTIFIER)";
	String startDate = "value(SpecimenProtocol:START_DATE)";
	String startDate2 = "value(SpecimenProtocol:START_DATE:HLIMIT)";
	String endDate = "value(SpecimenProtocol:END_DATE)";
	String endDate2 = "value(SpecimenProtocol:END_DATE:HLIMIT)";
	String protocolParticipantId = "value(CollectionProtocolRegistration:PROTOCOL_PARTICIPANT_ID)";
	String regDate = "value(CollectionProtocolRegistration:REGISTRATION_DATE)";
	String regDate2 = "value(CollectionProtocolRegistration:REGISTRATION_DATE:HLIMIT)";
	
	
	String opPrincipalInvestigator = "value(Operator:SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	String opTitle = "value(Operator:SpecimenProtocol:TITLE)";
	String opShortTitle = "value(Operator:SpecimenProtocol:SHORT_TITLE)";
	String opIrbIdentifier = "value(Operator:SpecimenProtocol:IRB_IDENTIFIER)";
	String opStartDate = "value(Operator:SpecimenProtocol:START_DATE)";
	String opEndDate = "value(Operator:SpecimenProtocol:END_DATE)";
	String opProtocolParticipantId = "value(Operator:CollectionProtocolRegistration:PROTOCOL_PARTICIPANT_ID)";
	String opRegDate = "value(Operator:CollectionProtocolRegistration:REGISTRATION_DATE)";
%>

<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script src="jss/AdvancedSearchScripts.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>

<html:form action="<%=Constants.ADVANCED_SEARCH_ACTION%>">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">
<tr>
	<td><html:hidden property="objectName" value="CollectionProtocol"/></td>
	<td><html:hidden property="selectedNode" /></td>
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
		<%-- html:button property="resetQuery" styleClass="actionButton" onclick="">
			<bean:message key="buttons.resetQuery"/>
		</html:button --%>
	</td>
</tr>

<!--  FIRST ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="principalinvestigator">
 			<bean:message key="collectionprotocol.principalinvestigator"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opPrincipalInvestigator%>" styleClass="formFieldSized10" styleId="principalInvestigatorCombo" size="1" onchange="onOperatorChange('principalInvestigatorCombo','principalInvestigator')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=principalInvestigator%>" styleClass="formFieldSized10" styleId="principalInvestigator" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opTitle%>" styleClass="formFieldSized10" styleId="titleCombo" size="1" onchange="onOperatorChange('titleCombo','title')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="title" property="<%=title%>" disabled="true" />
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opShortTitle%>" styleClass="formFieldSized10" styleId="shortTitleCombo" size="1" onchange="onOperatorChange('shortTitleCombo','shortTitle')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="shortTitle" property="<%=shortTitle%>" disabled="true"/>
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opIrbIdentifier%>" styleClass="formFieldSized10" styleId="irbIdentifierCombo" size="1" onchange="onOperatorChange('irbIdentifierCombo','irbIdentifier')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="irbIdentifier" property="<%=irbIdentifier%>" disabled="true"/>
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opStartDate%>" styleClass="formFieldSized10" styleId="startDate" size="1" onchange="onDateOperatorChange(this,'startDate1','startDate2')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="startDate1" property="<%=startDate%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('startDate','advanceSearchForm.startDate1',false);">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="startDate2" property="<%=startDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('startDate','advanceSearchForm.startDate2',true);">
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opEndDate%>" styleClass="formFieldSized10" styleId="endDate" size="1" onchange="onDateOperatorChange(this,'endDate1','endDate2')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv1" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="endDate1" property="<%=endDate%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('endDate','advanceSearchForm.endDate1',false);">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="endDate2" property="<%=endDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('endDate','advanceSearchForm.endDate2',true);">
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
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opProtocolParticipantId%>" styleClass="formFieldSized10" styleId="protocolParticipantIdCombo" size="1" onchange="onOperatorChange('protocolParticipantIdCombo','protocolParticipantId')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="protocolParticipantId" property="<%=protocolParticipantId%>" disabled="true"/>
	</td>
</tr>


<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="regDate">
			<b><bean:message key="collectionProtocolReg.participantRegistrationDate"/>
		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opRegDate%>" styleClass="formFieldSized10" styleId="regDate" size="1" onchange="onDateOperatorChange(this,'regDate1','regDate2')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv2" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="regDate1" property="<%=regDate%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('regDate','advanceSearchForm.regDate1',false);">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="regDate2" property="<%=regDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('regDate','advanceSearchForm.regDate2',true);">
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
