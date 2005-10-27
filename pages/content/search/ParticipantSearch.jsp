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
			//parent.queryFrame.location.href = "/catissuecore/AdvanceSearch.do";
			document.forms[0].action = "/catissuecore/AdvanceSearch.do";
			document.forms[0].submit();
		}
		
		//This is the wrapper function over show_calendar() that allows to select the date only if the operator is not 'ANY'
		function onDate1()
		{
			var dateCombo = document.getElementById("birthDate");
			
			if(dateCombo.options[dateCombo.selectedIndex].value != "<%=Constants.ANY%>")
			{
				show_calendar('advanceSearchForm.birthDate1',null,null,'MM-DD-YYYY')
			}
		}

		//This is the wrapper function over show_calendar() that allows to select the date only if the operator is 'BETWEEN' or 'NOT BETWEEN'
		function onDate2()
		{
			var dateCombo = document.getElementById("birthDate");
			
			if(dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.BETWEEN%>" || dateCombo.options[dateCombo.selectedIndex].value == "<%=Operator.NOT_BETWEEN%>")
			{
				show_calendar('advanceSearchForm.birthDate2',null,null,'MM-DD-YYYY')
			}
		}

		/*Generic function to enable/disable value fields as per the operator selected
		  opratorListId : Id of the operators list box
		  valueFieldId  : Id of the value field (Textbox/List) which is to be enabled/disabled
		*/
		function onOperatorChange(operatorListId,valueFieldId)
		{
			var opCombo  = document.getElementById(operatorListId);
			var valField = document.getElementById(valueFieldId);
			
			if(opCombo.options[opCombo.selectedIndex].value == "<%=Constants.ANY%>")
			{
				if(valField.type == "text")
				{
					valField.value = "";
					valField.disabled = true;
				}
				else
				{
					valField.disabled = true;
				}
			}
			else
			{
				valField.disabled = false;
			}
		}
		
		//This function enables the second date field only if the operator is 'BETWEEN' or 'NOT BETWEEN'
		//& disables both the date fields if operator is 'ANY'
		function onDateOperatorChange(element)
		{
			var dateTxt1  = document.getElementById("birthDate1");
			var dateTxt2  = document.getElementById("birthDate2");
			
			if(element.value == "<%=Operator.BETWEEN%>" || element.value == "<%=Operator.NOT_BETWEEN%>")
			{
				dateTxt1.disabled = false;
				dateTxt2.disabled = false;
			}
			else if(element.value == "<%=Constants.ANY%>")
			{
				dateTxt1.value = "";
				dateTxt1.disabled = true;

				dateTxt2.value = "";
				dateTxt2.disabled = true;
			}
			else
			{
				dateTxt1.disabled = false;

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
	<td><html:hidden property="objectName" value="Participant"/></td>
</tr>
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
		<html:select property="<%=opLastName%>" styleClass="formFieldSized10" styleId="lastNameCombo" size="1" onchange="onOperatorChange('lastNameCombo','lastName')">
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
		<html:select property="<%=opFirstName%>" styleClass="formFieldSized10" styleId="firstNameCombo" size="1" onchange="onOperatorChange('firstNameCombo','firstName')">
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
		<html:select property="<%=opMiddleName%>" styleClass="formFieldSized10" styleId="middleNameCombo" size="1" onchange="onOperatorChange('middleNameCombo','middleName')">
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
		<html:select property="<%=opBirthDate%>" styleClass="formFieldSized10" styleId="birthDate" size="1" onchange="onDateOperatorChange(this)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate1" property="<%=birthDate%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate1();">
			<img src="images\calendar.gif" width=24 height=22 border=0>
		</a>
					&nbsp;To&nbsp;
		<html:text styleClass="formDateSized10" size="10" styleId="birthDate2" property="<%=birthDate2%>" disabled="true"/>
					 &nbsp;
		<a href="javascript:onDate2();">
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
		<html:select property="<%=opGender%>" styleClass="formFieldSized10" styleId="genderCombo" size="1" onchange="onOperatorChange('genderCombo','gender')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=gender%>" styleClass="formFieldSized10" styleId="gender" size="1" disabled="true">
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
		<html:select property="<%=opGenotype%>" styleClass="formFieldSized10" styleId="genotypeCombo" size="1" onchange="onOperatorChange('genotypeCombo','genotype')">
				<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=genotype%>" styleClass="formFieldSized10" styleId="genotype" size="1" disabled="true">
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
		<html:select property="<%=opRace%>" styleClass="formFieldSized10" styleId="raceCombo" size="1" onchange="onOperatorChange('raceCombo','race')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<html:select property="<%=race%>" styleClass="formFieldSized10" styleId="race" size="1" disabled="true">
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
		<html:select property="<%=opEthnicity%>" styleClass="formFieldSized10" styleId="ethnicityCombo" size="1" onchange="onOperatorChange('ethnicityCombo','ethnicity')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=ethnicity%>" styleClass="formFieldSized10" styleId="ethnicity" size="1" disabled="true">
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
		<html:select property="<%=opSsn%>" styleClass="formFieldSized10" styleId="ssnCombo" size="1" onchange="onOperatorChange('ssnCombo','ssn')">
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