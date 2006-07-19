<!-- 
	This JSP page is to create specimen events. Specimens are to be searched based on specimen id or barCode.
	Author : Mandar Deshmukh
	Date   : July 03, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.QuickEventsForm"%>
<%@ page import="java.util.*"%>


<head>
	
	<script language="JavaScript">
		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].specimenID.disabled = false;
				document.forms[0].barCode.disabled = true;
			}
			else
			{
				document.forms[0].barCode.disabled = false;
				document.forms[0].specimenID.disabled = true;
			}
		}
	</script>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.QUICKEVENTS_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
	<tr>
		<td class="formMessage" colspan="3">* indicates a required field</td>
	</tr>
	
	<tr>
		<td class="formTitle" height="20" colspan="7">
			<bean:message key="quickEvents.title"/>
		</td>
	</tr>
	
	<tr>
		
		<td class="formRequiredNoticeNoBottom">*
			<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
				&nbsp;
			</html:radio>
		</td>
		<TD class="formRequiredLabelRightBorder" width="73">
			<label for="parentId">
				<bean:message key="quickEvents.specimenID"/>
			</label>
		</TD>
		<td class="formField">
			<logic:equal name="quickEventsForm" property="checkedButton" value="1">
				<html:select property="specimenID" styleClass="formField" styleId="specimenID" size="1">
					<html:options collection="<%=Constants.SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
				</html:select>
			</logic:equal>
			
			<logic:equal name="quickEventsForm" property="checkedButton" value="2">
				<html:select property="specimenID" styleClass="formField" styleId="specimenID" size="1" disabled="true">
					<html:options collection="<%=Constants.SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
				</html:select>
			</logic:equal>
		</td>
		<td class="formRequiredLabelBoth" width="5">*</td>
		<td class="formRequiredLabel">
			<bean:message key="quickEvents.eventparameters"/>
		</td>
		<td class="formField" colspan="2">
			<html:select property="specimenEventParameter" styleClass="formFieldSized15" styleId="className" size="1" disabled="false"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options name="<%=Constants.EVENT_PARAMETERS_LIST%>" labelName="<%=Constants.EVENT_PARAMETERS_LIST%>"/>
			</html:select>
		</td>

	</tr>
	
	<tr>
		<td class="formRequiredNotice"><span class="hideText">*</span>
			<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
				&nbsp;
			</html:radio>
		</td>
		<td class="formRequiredLabel" width="73">
			<label for="barCode">
				<bean:message key="quickEvents.barcode"/>
			</label>
		</td>
		<td class="formField">
			<logic:equal name="quickEventsForm" property="checkedButton" value="1">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barCode" property="barCode" disabled="true"/>
			</logic:equal>
			
			<logic:equal name="quickEventsForm" property="checkedButton" value="2">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barCode" property="barCode"/>
			</logic:equal>
		</td>
		<td class="formLabel" colspan="4">
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td colspan="5">
			&nbsp;
		</td>
		<td align="right">
			<html:submit styleClass="actionButton">
				<bean:message  key="quickEvents.add" />
			</html:submit>
		</td>
	</tr>
	</table>
</td>
</tr>


</table>
</html:form>