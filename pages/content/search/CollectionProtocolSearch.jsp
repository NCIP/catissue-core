<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.query.Operator"%>

<%
	String principalInvestigator = "value(SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	//String protocolCoordinator = "value(CollectionCoordinators:USER_ID)";
	String title = "value(SpecimenProtocol:TITLE)";
	String shortTitle = "value(SpecimenProtocol:SHORT_TITLE)";
	String irbIdentifier = "value(SpecimenProtocol:IRB_IDENTIFIER)";
	String startDate = "value(SpecimenProtocol:START_DATE)";
	String startDate2 = "value(Participant:START_DATE:HLIMIT)";
	String endDate = "value(SpecimenProtocol:END_DATE)";
	String endDate2 = "value(Participant:END_DATE:HLIMIT)";
	String enrollment = "value(SpecimenProtocol:ENROLLMENT)";
	String descriptionURL = "value(SpecimenProtocol:DESCRIPTION_URL)";
	String clinicalStatus = "value(CollectionProtocolEvent:CLINICAL_STATUS)";
	String studyCalendarEventPoint = "value(CollectionProtocolEvent:STUDY_CALENDAR_EVENT_POINT)";
	String specimenType = "value(SpecimenRequirement:SPECIMEN_TYPE)";
	String tissueSite = "value(SpecimenRequirement:TISSUE_SITE)";
	String pathologicalStatus = "value(SpecimenRequirement:PATHOLOGICAL_STATUS)";

	String opPrincipalInvestigator = "value(Operator:SpecimenProtocol:PRINCIPAL_INVESTIGATOR_ID)";
	//String opProtocolCoordinator = "value(Operator:CollectionCoordinators:USER_ID)";
	String opTitle = "value(Operator:SpecimenProtocol:TITLE)";
	String opShortTitle = "value(Operator:SpecimenProtocol:SHORT_TITLE)";
	String opIrbIdentifier = "value(Operator:SpecimenProtocol:IRB_IDENTIFIER)";
	String opStartDate = "value(Operator:SpecimenProtocol:START_DATE)";
	String opEndDate = "value(Operator:SpecimenProtocol:END_DATE)";
	String opEnrollment = "value(Operator:SpecimenProtocol:ENROLLMENT)";
	String opDescriptionURL = "value(Operator:SpecimenProtocol:DESCRIPTION_URL)";
	String opClinicalStatus = "value(Operator:CollectionProtocolEvent:CLINICAL_STATUS)";
	String opStudyCalendarEventPoint = "value(Operator:CollectionProtocolEvent:STUDY_CALENDAR_EVENT_POINT)";
	String opSpecimenType = "value(Operator:SpecimenRequirement:SPECIMEN_TYPE)";
	String opTissueSite = "value(Operator:SpecimenRequirement:TISSUE_SITE)";
	String opPathologicalStatus = "value(Operator:SpecimenRequirement:PATHOLOGICAL_STATUS)";
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
	
	<td class="formTitle" height="25" colspan="3">
	    <bean:message key="collectionProtocol.queryRule"/>
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
 		<label for="enrollment">
 			<b><bean:message key="collectionprotocol.participants"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opEnrollment%>" styleClass="formFieldSized10" styleId="opEnrollment" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="enrollment" property="<%=enrollment%>"/>
	</td>
</tr>

<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="descriptionURL">
     		<b><bean:message key="collectionprotocol.descriptionurl"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opDescriptionURL%>" styleClass="formFieldSized10" styleId="<%=opDescriptionURL%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="descriptionURL" property="<%=descriptionURL%>"/>
	</td>
</tr>

<!-- NINETH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="clinicalStatus">
     		<b><bean:message key="collectionprotocol.clinicalstatus"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opClinicalStatus%>" styleClass="formFieldSized10" styleId="<%=opClinicalStatus%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=clinicalStatus%>" styleClass="formFieldSized10" styleId="clinicalStatus" size="1">
			<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- TENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="studyCalendarEventPoint">
     		<b><bean:message key="collectionprotocol.studycalendartitle"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opStudyCalendarEventPoint%>" styleClass="formFieldSized10" styleId="<%=opStudyCalendarEventPoint%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="studyCalendarEventPoint" property="<%=studyCalendarEventPoint%>"/>
	</td>
</tr>
<!-- ELEVENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="tissueSite">
     		<b><bean:message key="collectionprotocol.specimensite"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opTissueSite%>" styleClass="formFieldSized10" styleId="<%=opTissueSite%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=tissueSite%>" styleClass="formFieldSized10" styleId="tissueSite" size="1">
			<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- TWELTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="pathologicalStatus">
     		<b><bean:message key="collectionprotocol.specimenstatus"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opPathologicalStatus%>" styleClass="formFieldSized10" styleId="<%=opPathologicalStatus%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=pathologicalStatus%>" styleClass="formFieldSized10" styleId="pathologicalStatus" size="1">
			<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- THIRTEENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="specimenType">
     		<b><bean:message key="collectionprotocol.specimetype"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opSpecimenType%>" styleClass="formFieldSized10" styleId="<%=opSpecimenType%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=specimenType%>" styleClass="formFieldSized10" styleId="specimenType" size="1">
			<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<tr>
	<td colspan="3">&nbsp;</td>
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
