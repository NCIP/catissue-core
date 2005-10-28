<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.query.Operator"%>

<%
	String className = "value(Specimen:CLASS_NAME)";
	String type = "value(Specimen:TYPE)";
	String tissueSite = "value(SpecimenCharacteristics:TISSUE_SITE)";
	String tissueSide = "value(SpecimenCharacteristics:TISSUE_SIDE)";
	String pathologicalStatus = "value(SpecimenCharacteristics:PATHOLOGICAL_STATUS)";
	String concentration1 = "value(Specimen:CONCENTRATION)";
	String concentration2 = "value(Specimen:CONCENTRATION:HLIMIT)";
	String quantity1 = "value(Specimen:QUANTITY)";
	String quantity2 = "value(Specimen:QUANTITY:HLIMIT)";
	String biohazardType = "value(Biohazard:TYPE)";
	String biohazardName = "value(Biohazard:NAME)";

	String opClassName = "value(Operator:Specimen:CLASS_NAME)";
	String opType = "value(Operator:Specimen:TYPE)";
	String opTissueSite = "value(Operator:SpecimenCharacteristics:TISSUE_SITE)";
	String opTissueSide = "value(Operator:SpecimenCharacteristics:TISSUE_SIDE)";
	String opPathologicalStatus = "value(Operator:SpecimenCharacteristics:PATHOLOGICAL_STATUS)";
	String opConcentration = "value(Operator:Specimen:CONCENTRATION)";
	String opQuantity = "value(Operator:Specimen:QUANTITY)";
	String opBarcode = "value(Operator:Specimen:BARCODE)";
	String opBiohazardType = "value(Operator:Biohazard:TYPE)";
	String opBiohazardName = "value(Operator:Biohazard:NAME)";
%>

<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script src="jss/AdvancedSearchScripts.js" type="text/javascript"></script>
</head>

<html:errors />

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">

<tr>
	<td><html:hidden property="objectName" value="Specimen"/></td>
</tr>
<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" nowrap>
	&nbsp;<img src="images/Specimen.GIF" alt="Specimen" /> &nbsp;<bean:message key="specimen.queryRule"/>
	</td>
	<td nowrap align="right"  colspan="2" class="formTitle">
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
 		<label for="className">
 			<b><bean:message key="specimen.type"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opClassName%>" styleClass="formFieldSized10" styleId="opClassName" size="1" onchange="onOperatorChange('opClassName','className')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=className%>" styleClass="formFieldSized10" styleId="className" size="1" disabled="true">
			<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  SECOND ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="type">
 			<b><bean:message key="specimen.subType"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opType%>" styleClass="formFieldSized10" styleId="opType" size="1" onchange="onOperatorChange('opType','type')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=type%>" styleClass="formFieldSized10" styleId="type" size="1" disabled="true">
			<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  THIRD ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="tissueSite">
 			<b><bean:message key="specimen.tissueSite"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opTissueSite%>" styleClass="formFieldSized10" styleId="opTissueSite" size="1" onchange="onOperatorChange('opTissueSite','tissueSite')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=tissueSite%>" styleClass="formFieldSized10" styleId="tissueSite" size="1" disabled="true">
			<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="tissueSide">
 			<b><bean:message key="specimen.tissueSide"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opTissueSide%>" styleClass="formFieldSized10" styleId="opTissueSide" size="1" onchange="onOperatorChange('opTissueSide','tissueSide')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=tissueSide%>" styleClass="formFieldSized10" styleId="tissueSide" size="1" disabled="true">
			<html:options collection="<%=Constants.TISSUE_SIDE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!--  FIFTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="pathologicalStatus">
 			<b><bean:message key="specimen.pathologicalStatus"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opPathologicalStatus%>" styleClass="formFieldSized10" styleId="opPathologicalStatus" size="1" onchange="onOperatorChange('opPathologicalStatus','pathologicalStatus')">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=pathologicalStatus%>" styleClass="formFieldSized10" styleId="pathologicalStatus" size="1" disabled="true">
			<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- SIXTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="concentration">
 			<b><bean:message key="specimen.concentration"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opConcentration%>" styleClass="formFieldSized10" styleId="opConcentration" size="1" onchange="onDateOperatorChange(this,'concentration1','concentration2')">
				<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="concentration1" property="<%=concentration1%>" disabled="true"/>
						&nbsp;To&nbsp;
		<html:text styleClass="formFieldSized10" styleId="concentration2" property="<%=concentration2%>" disabled="true"/>
	</td>
</tr>

<!-- SEVENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="quatity">
 			<b><bean:message key="specimen.quantity"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opQuantity%>" styleClass="formFieldSized10" styleId="opQuantity" size="1" onchange="onDateOperatorChange(this,'quantity1','quantity2')">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<html:text styleClass="formFieldSized10" styleId="quantity1" property="<%=quantity1%>" disabled="true"/>
						&nbsp;To&nbsp;
		<html:text styleClass="formFieldSized10" styleId="quantity2" property="<%=quantity2%>" disabled="true"/>
	</td>
</tr>

<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="biohazardType">
     		<b><bean:message key="specimen.biohazardType"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opBiohazardType%>" styleClass="formFieldSized10" styleId="opBiohazardType" size="1" onchange="onOperatorChange('opBiohazardType','biohazardType')">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=biohazardType%>" styleClass="formFieldSized10" styleId="biohazardType" size="1" disabled="true">
			<html:options collection="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>

<!-- NINETH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="biohazardName">
     		<b><bean:message key="specimen.biohazardName"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opBiohazardName%>" styleClass="formFieldSized10" styleId="opBiohazardName" size="1" onchange="onOperatorChange('opBiohazardName','biohazardName')">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="biohazardName" property="<%=biohazardName%>" disabled="true"/>
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