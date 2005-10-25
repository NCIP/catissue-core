<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.query.Operator"%>

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
	<script language="JavaScript">
		function onAddRule()
		{
			parent.queryFrame.location.href = "/catissuecore/Home.do";
		}
	</script>
</head>

<html:errors />

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">

<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" colspan="3">
	    <bean:message key="participant.queryRule"/>
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
		<html:select property="<%=opLastName%>" styleClass="formFieldSized10" styleId="<%=opLastName%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="lastName" property="<%=lastName%>"/>
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
		<html:select property="<%=opFirstName%>" styleClass="formFieldSized10" styleId="<%=opFirstName%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="firstName" property="<%=firstName%>"/>
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
		<html:select property="<%=opMiddleName%>" styleClass="formFieldSized10" styleId="<%=opMiddleName%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="middleName" property="<%=middleName%>"/>
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
		<html:select property="<%=opBirthDate%>" styleClass="formFieldSized10" styleId="birthDate" size="1">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate1" property="<%=birthDate%>" />
					 &nbsp;
		<a href="javascript:show_calendar('participantForm.birthDate1',null,null,'MM-DD-YYYY');">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate2" property="<%=birthDate2%>" />
					 &nbsp;
		<a href="javascript:show_calendar('participantForm.birthDate2',null,null,'MM-DD-YYYY');">
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
		<html:select property="<%=opGender%>" styleClass="formFieldSized10" styleId="<%=opGender%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=gender%>" styleClass="formFieldSized10" styleId="gender" size="1">
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
		<html:select property="<%=opGenotype%>" styleClass="formFieldSized10" styleId="<%=opGenotype%>" size="1">
				<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=genotype%>" styleClass="formFieldSized10" styleId="genotype" size="1">
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
		<html:select property="<%=opRace%>" styleClass="formFieldSized10" styleId="<%=opRace%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<html:select property="<%=race%>" styleClass="formFieldSized10" styleId="race" size="1">
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
		<html:select property="<%=opEthnicity%>" styleClass="formFieldSized10" styleId="<%=opEthnicity%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=ethnicity%>" styleClass="formFieldSized10" styleId="ethnicity" size="1">
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
		<html:select property="<%=opSsn%>" styleClass="formFieldSized10" styleId="<%=opSsn%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="ssn" property="<%=ssn%>"/>
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