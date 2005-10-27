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
	String postionD1 = "value(Specimen:POSITION_DIMENSION_ONE)";
	String postionD12 = "value(Specimen:POSITION_DIMENSION_ONE:HLIMIT)";
	String postionD2 = "value(Specimen:POSITION_DIMENSION_TWO)";
	String postionD22 = "value(Specimen:POSITION_DIMENSION_TWO:HLIMIT)";
	String barcode = "value(Specimen:BARCODE)";

	String opClassName = "value(Operator:Specimen:CLASS_NAME)";
	String opType = "value(Operator:Specimen:TYPE)";
	String opTissueSite = "value(Operator:SpecimenCharacteristics:TISSUE_SITE)";
	String opTissueSide = "value(Operator:SpecimenCharacteristics:TISSUE_SIDE)";
	String opPathologicalStatus = "value(Operator:SpecimenCharacteristics:PATHOLOGICAL_STATUS)";
	String opPostionD1 = "value(Operator:Specimen:POSITION_DIMENSION_ONE)";
	String opPostionD2 = "value(Operator:Specimen:POSITION_DIMENSION_TWO)";
	String opBarcode = "value(Operator:Specimen:BARCODE)";
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
				
		function onDimensionOperatorChange(element)
		{
			var dimULimit = null;
			
			if(element.id == "dimension1")
			{
				dimULimit = document.getElementById("postionD12");
			}
			else
			{
				dimULimit = document.getElementById("postionD22");
			}
						
			if(element.value == "<%=Operator.BETWEEN%>" || element.value == "<%=Operator.NOT_BETWEEN%>")
			{
				dimULimit.disabled = false;
				dimULimit.value = "";
			}
			else
			{
				dimULimit.value = "";
				dimULimit.disabled = true;
			}
		}
		
	</script>
</head>

<html:errors />

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">

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
		<html:select property="<%=opClassName%>" styleClass="formFieldSized10" styleId="<%=opClassName%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=className%>" styleClass="formFieldSized10" styleId="className" size="1">
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
		<html:select property="<%=opType%>" styleClass="formFieldSized10" styleId="<%=opType%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=type%>" styleClass="formFieldSized10" styleId="type" size="1">
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

<!--  FOURTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="tissueSide">
 			<b><bean:message key="specimen.tissueSide"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opTissueSide%>" styleClass="formFieldSized10" styleId="<%=opTissueSide%>" size="1">
			<html:options collection="<%=Constants.ENUMERATED_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:select property="<%=tissueSide%>" styleClass="formFieldSized10" styleId="tissueSide" size="1">
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

<!-- SIXTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="positionDimensionOne">
 			<b><bean:message key="specimen.positionDimensionOne"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opPostionD1%>" styleClass="formFieldSized10" styleId="dimenstion1" size="1" onchange="onDimensionOperatorChange(this)">
				<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="postionD1" property="<%=postionD1%>"/>
						&nbsp;To&nbsp;
		<html:text styleClass="formFieldSized10" styleId="postionD12" property="<%=postionD12%>" disabled="true"/>
	</td>
</tr>

<!-- SEVENTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="positionDimensionTwo">
 			<b><bean:message key="specimen.positionDimensionTwo"/>
 		</label>
	</td>
	<td class="formField">
		<html:select property="<%=opPostionD2%>" styleClass="formFieldSized10" styleId="dimenstion2" size="1" onchange="onDimensionOperatorChange(this)">
			<html:options collection="<%=Constants.DATE_NUMERIC_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField" nowrap>
		<html:text styleClass="formFieldSized10" styleId="postionD2" property="<%=postionD2%>"/>
						&nbsp;To&nbsp;
		<html:text styleClass="formFieldSized10" styleId="postionD22" property="<%=postionD22%>" disabled="true"/>
	</td>
</tr>

<!-- EIGHTH ROW -->
<tr>
	<td class="formSerialNumberField" nowrap>
 		<label for="barcode">
     		<b><bean:message key="specimen.barcode"/>
     	</label>
	</td>
	<td class="formField">
		<html:select property="<%=opBarcode%>" styleClass="formFieldSized10" styleId="<%=opBarcode%>" size="1">
			<html:options collection="<%=Constants.STRING_OPERATORS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized10" styleId="barcode" property="<%=barcode%>"/>
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