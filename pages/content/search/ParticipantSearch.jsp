<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.query.Operator"%>

<%
	String lastName = "value(Participant:LAST_NAME)";
	String firstName = "value(Participant:FIRST_NAME)";
	String middleName = "value(Participant:MIDDLE_NAME)";
	String birthDate = "value(Participant:BIRTH_DATE)";
	String birthDate2 = "value(Participant:BIRTH_DATE:HLIMIT)";
	String gender = "value(Participant:GENDER)";
	String genotype = "value(Participant:GENOTYPE)";
	String race = "value(Participant:RACE)";
	String ethnicity = "value(Participant:ETHNICITY)";
	String ssn = "value(Participant:SOCIAL_SECURITY_NUMBER)";

	String opLastName = "value(Operator:Participant:LAST_NAME)";
	String opFirstName = "value(Operator:Participant:FIRST_NAME)";
	String opMiddleName = "value(Operator:Participant:MIDDLE_NAME)";
	String opBirthDate = "value(Operator:Participant:BIRTH_DATE)";
	String opGender = "value(Operator:Participant:GENDER)";
	String opGenotype = "value(Operator:Participant:GENOTYPE)";
	String opRace = "value(Operator:Participant:RACE)";
	String opEthnicity = "value(Operator:Participant:ETHNICITY)";
	String opSsn = "value(Operator:Participant:SOCIAL_SECURITY_NUMBER)";
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
	<td><html:hidden property="objectName" value="Participant"/></td>
	<td><html:hidden property="selectedNode" /></td>
</tr>
<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" >
	    &nbsp;<img src="images/Participant.GIF" alt="Participant" /> &nbsp;
	    <bean:message key="participant.queryRule"/>
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
 		<label for="lastName">
 			<b><bean:message key="user.lastName"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opLastName%>" styleClass="formFieldSized10" styleId="lastNameCombo" size="1" onchange="onOperatorChange('lastNameCombo','lastName')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="lastName" property="<%=lastName%>" disabled="true"/>
	</td>
</tr>

<!--  SECOND ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="firstName">
 			<b><bean:message key="user.firstName"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opFirstName%>" styleClass="formFieldSized10" styleId="firstNameCombo" size="1" onchange="onOperatorChange('firstNameCombo','firstName')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="firstName" property="<%=firstName%>" disabled="true"/>
	</td>
</tr>

<!--  THIRD ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="middleName">
 			<b><bean:message key="participant.middleName"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opMiddleName%>" styleClass="formFieldSized10" styleId="middleNameCombo" size="1" onchange="onOperatorChange('middleNameCombo','middleName')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="middleName" property="<%=middleName%>" disabled="true"/>
	</td>
</tr>

<!--  FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="birthDate">
			<b><bean:message key="participant.birthDate"/>
		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opBirthDate%>" styleClass="formFieldSized10" styleId="birthDate" size="1" onchange="onDateOperatorChange(this,'birthDate1','birthDate2')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate1" property="<%=birthDate%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('birthDate','advanceSearchForm.birthDate1',false);">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate2" property="<%=birthDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate('birthDate','advanceSearchForm.birthDate2',true);">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
	</td>
</tr>

<!--  FIFTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="gender">
 			<b><bean:message key="participant.gender"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opGender%>" styleClass="formFieldSized10" styleId="genderCombo" size="1" onchange="onOperatorChange('genderCombo','gender')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=gender%>" styleClass="formFieldSized10" styleId="gender" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.GENDER_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- SIXTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="genotype">
 			<b><bean:message key="participant.genotype"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opGenotype%>" styleClass="formFieldSized10" styleId="genotypeCombo" size="1" onchange="onOperatorChange('genotypeCombo','genotype')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=genotype%>" styleClass="formFieldSized10" styleId="genotype" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.GENOTYPE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- SEVENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="race">
 			<b><bean:message key="participant.race"/>
 		</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opRace%>" styleClass="formFieldSized10" styleId="raceCombo" size="1" onchange="onOperatorChange('raceCombo','race')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=race%>" styleClass="formFieldSized10" styleId="race" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="ethnicity">
     		<b><bean:message key="participant.ethnicity"/>
     	</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opEthnicity%>" styleClass="formFieldSized10" styleId="ethnicityCombo" size="1" onchange="onOperatorChange('ethnicityCombo','ethnicity')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=ethnicity%>" styleClass="formFieldSized10" styleId="ethnicity" size="1" disabled="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.ETHNICITY_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- NINETH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="socialSecurityNumber">
     		<b><bean:message key="participant.socialSecurityNumber"/>
     	</label>
	</td>
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
		<html:select property="<%=opSsn%>" styleClass="formFieldSized10" styleId="ssnCombo" size="1" onchange="onOperatorChange('ssnCombo','ssn')"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="ssn" property="<%=ssn%>" disabled="true"/>
	</td>
</tr>

<tr>
	<td colspan="3">&nbsp;</td>
</tr>


<!-- TENTH ROW -->
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