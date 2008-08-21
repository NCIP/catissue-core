<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.query.Operator"%>

<%
	String siteName = "value(SpecimenCollectionGroup:SITE_ID)";
	String studyCalendarEventPoint1 = "value(CollectionProtocolEvent:STUDY_CALENDAR_EVENT_POINT)";
	String studyCalendarEventPoint2 = "value(CollectionProtocolEvent:STUDY_CALENDAR_EVENT_POINT:HLIMIT)";
	String clinicalDiagnosis = "value(SpecimenCollectionGroup:CLINICAL_DIAGNOSIS)";
	String clinicalStatus = "value(SpecimenCollectionGroup:CLINICAL_STATUS)";
	String medicalRecordNo = "value(ParticipantMedicalIdentifier:MEDICAL_RECORD_NUMBER)";
	String surgicalPathologyNo = "value(ClinicalReport:SURGICAL_PATHOLOGICAL_NUMBER)";

	String opSiteName = "value(Operator:SpecimenCollectionGroup:SITE_ID)";
	String opStudyCalendarEventPoint = "value(Operator:CollectionProtocolEvent:STUDY_CALENDAR_EVENT_POINT)";
	String opClinicalDiagnosis = "value(Operator:SpecimenCollectionGroup:CLINICAL_DIAGNOSIS)";
	String opClinicalStatus = "value(Operator:SpecimenCollectionGroup:CLINICAL_STATUS)";
	String opMedicalRecordNo = "value(Operator:ParticipantMedicalIdentifier:MEDICAL_RECORD_NUMBER)";
	String opSurgicalPathologyNo = "value(Operator:ClinicalReport:SURGICAL_PATHOLOGICAL_NUMBER)";
%>

<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script src="jss/AdvancedSearchScripts.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">
<tr>
	<td><html:hidden property="objectName" value="SpecimenCollectionGroup"/></td>
	<td><html:hidden property="selectedNode" /></td>
</tr>
<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" nowrap>
	    &nbsp;<img src="images/SpecimenCollectionGroup.GIF" alt="Participant" /> &nbsp;
	    <bean:message key="spg.queryRule"/>
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
 		<label for="site">
 			<b><bean:message key="specimenCollectionGroup.site"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opSiteName%>" styleClass="formFieldSized10" styleId="opSiteName" size="1" onchange="onOperatorChange('opSiteName','siteName')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="siteName" property="<%=siteName%>" disabled="true"/>
	</td>
</tr>

<!--  SECOND ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="studyCalendarEventPoint">
 			<b><bean:message key="specimenCollectionGroup.studyCalendarEventPoint"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opStudyCalendarEventPoint%>" styleClass="formFieldSized10" styleId="opStudyCalendarEventPoint" size="1" onchange="onDateOperatorChange(this,'studyCalendarEventPoint1','studyCalendarEventPoint2')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<html:text styleClass="formFieldSized10" styleId="studyCalendarEventPoint1" property="<%=studyCalendarEventPoint1%>" disabled="true"/>
						&nbsp;To&nbsp;
		<html:text styleClass="formFieldSized10" styleId="studyCalendarEventPoint2" property="<%=studyCalendarEventPoint2%>" disabled="true"/>
	</td>
</tr>

<!--  THIRD ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="clinicalDiagnosis">
 			<b><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opClinicalDiagnosis%>" styleClass="formFieldSized10" styleId="opClinicalDiagnosis" size="1" onchange="onOperatorChange('opClinicalDiagnosis','clinicalDiagnosis')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=clinicalDiagnosis%>" styleClass="formFieldSized15" styleId="clinicalDiagnosis" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.CLINICAL_DIAGNOSIS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="clinicalStatus">
 			<b><bean:message key="specimenCollectionGroup.clinicalStatus"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opClinicalStatus%>" styleClass="formFieldSized10" styleId="opClinicalStatus" size="1" onchange="onOperatorChange('opClinicalStatus','clinicalStatus')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=clinicalStatus%>" styleClass="formFieldSized10" styleId="clinicalStatus" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- FIFTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="medicalRecordNumber">
     		<b><bean:message key="specimenCollectionGroup.medicalRecordNumber"/>
     	</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opMedicalRecordNo%>" styleClass="formFieldSized10" styleId="opMedicalRecordNo" size="1" onchange="onOperatorChange('opMedicalRecordNo','medicalRecordNo')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="medicalRecordNo" property="<%=medicalRecordNo%>" disabled="true"/>
	</td>
</tr>

<!-- SIXTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="surgicalPathologyNumber">
     		<b><bean:message key="specimenCollectionGroup.surgicalPathologyNumber"/>
     	</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opSurgicalPathologyNo%>" styleClass="formFieldSized10" styleId="opSurgicalPathologyNo" size="1" onchange="onOperatorChange('opSurgicalPathologyNo','surgicalPathologyNo')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="surgicalPathologyNo" property="<%=surgicalPathologyNo%>" disabled="true"/>
	</td>
</tr>

<tr>
	<td colspan="3">&nbsp;</td>
</tr>


<!-- LAST ROW -->
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