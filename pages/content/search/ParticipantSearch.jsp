<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.query.Operator"%>

<%
	String lastName = "value(Participant:LAST_NAME:" + Operator.LIKE + ")";
	String firstName = "value(Participant:FIRST_NAME:" + Operator.LIKE + ")";
	String middleName = "value(Participant:MIDDLE_NAME:" + Operator.LIKE + ")";
	String birthDate = "value(Participant:BIRTH_DATE:" + Operator.LIKE + ")";
	String gender = "value(Participant:GENDER:" + Operator.LIKE + ")";
	String genotype = "value(Participant:GENOTYPE:" + Operator.LIKE + ")";
	String race = "value(Participant:RACE:" + Operator.LIKE + ")";
	String ethnicity = "value(Participant:ETHNICITY:" + Operator.LIKE + ")";
	String ssn = "value(Participant:SOCIAL_SECURITY_NUMBER:" + Operator.LIKE + ")";
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

<html:form action="ParticipantSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">

<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" colspan="6">
	    <bean:message key="participant.queryRule"/>
	</td>
</tr>

<!--  FIRST ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="lastName">
 			<bean:message key="user.lastName"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="lastNameOp" name="lastNameOp">
			<option>-- ANY --</option>
            <option>LIKE</option>
            <option>NOT LIKE</option>
            <option>STARTS WITH</option>
            <option>ENDS WITH</option>
		</select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="lastName" property="<%=lastName%>"/>
	</td>
	<td class="formField" nowrap>
 		<label for="firstName">
 			<bean:message key="user.firstName"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="firstNameOp" name="firstNameOp">
			<option>-- ANY --</option>
            <option>LIKE</option>
            <option>NOT LIKE</option>
            <option>STARTS WITH</option>
            <option>ENDS WITH</option>
		</select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="firstName" property="<%=firstName%>"/>
	</td>
</tr>

<!--  SECOND ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="middleName">
 			<bean:message key="participant.middleName"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="middleNameOp" name="middleNameOp">
			<option>-- ANY --</option>
            <option>LIKE</option>
            <option>NOT LIKE</option>
            <option>STARTS WITH</option>
            <option>ENDS WITH</option>
		</select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="middleName" property="<%=middleName%>"/>
	</td>
	<td class="formField" nowrap>
 		<label for="birthDate">
			<bean:message key="participant.birthDate"/>
		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="bDateOp" name="bDateOp">
			<option>-- ANY --</option>
            <option>IS</option>
            <option>GREATER THAN</option>
            <option>LESS THAN</option>
		</select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate" property="<%=birthDate%>" />
					 &nbsp;<bean:message key="page.dateFormat" />&nbsp;
		<a href="javascript:show_calendar('participantForm.birthDate',null,null,'MM-DD-YYYY');">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
	</td>
</tr>

<!--  THIRD ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="gender">
 			<bean:message key="participant.gender"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="genderOp" name="genderOp">
			<option>-- ANY --</option>
            <option>IN</option>
            <option>NOT IN</option>
		</select>
	</td>
	<td class="formField">
		<html:select property="<%=gender%>" styleClass="formFieldSized10" styleId="gender" size="1">
			<html:options collection="<%=Constants.GENDER_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>	
	<td class="formField" nowrap>
 		<label for="genotype">
 			<bean:message key="participant.genotype"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="genotypeOp" name="genotypeOp">
			<option>-- ANY --</option>
            <option>IN</option>
            <option>NOT IN</option>
		</select>
	</td>
	<td class="formField">
		<html:select property="<%=genotype%>" styleClass="formFieldSized10" styleId="genotype" size="1">
			<html:options collection="<%=Constants.GENOTYPE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="race">
 			<bean:message key="participant.race"/>
 		</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="raceOp" name="raceOp">
			<option>-- ANY --</option>
            <option>IN</option>
            <option>NOT IN</option>
		</select>
	</td>
	<td class="formField" nowrap>
		<html:select property="<%=race%>" styleClass="formFieldSized10" styleId="race" size="1">
			<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
 		<label for="ethnicity">
     		<bean:message key="participant.ethnicity"/>
     	</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="ethnicityOp" name="ethnicityOp">
			<option>-- ANY --</option>
            <option>IN</option>
            <option>NOT IN</option>
		</select>
	</td>
	<td class="formField">
		<html:select property="<%=ethnicity%>" styleClass="formFieldSized10" styleId="ethnicity" size="1">
			<html:options collection="<%=Constants.ETHNICITY_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- FIFTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="socialSecurityNumber">
     		<bean:message key="participant.socialSecurityNumber"/>
     	</label>
	</td>
	<td class="formField">
		<select size="1" class="formFieldSized10" id="ethnicityOp" name="ethnicityOp">
            <option>-- ANY --</option>
            <option>IS</option>
		</select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="ssn" property="<%=ssn%>"/>
	</td>
	<td class="formField" colspan="3">
		&nbsp;
	</td>
</tr>

<tr>
	<td colspan="6">&nbsp;</td>
</tr>


<!-- SIXTH ROW -->
<tr>
	<td colspan="3">&nbsp</td>
	<td colspan="3" nowrap align="right">
		<html:button property="addRule" styleClass="actionButton" onclick="onAddRule()">
			<bean:message key="buttons.addRule"/>
		</html:button>
		&nbsp;&nbsp;
		<html:button property="search" styleClass="actionButton" onclick="">
			<bean:message key="buttons.search"/>
		</html:button>
		&nbsp;&nbsp;
		<html:button property="resetQuery" styleClass="actionButton" onclick="">
			<bean:message key="buttons.resetQuery"/>
		</html:button>
	</td>
</tr>

</table>
</html:form>