<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
	<SCRIPT language="JavaScript">

		function selectAll(element)
		{
			if(element.options[element.selectedIndex].text == "<%=Constants.ANY%>")
			{
				for(var i=1;i<element.length;i++)
				{
					element.options[i].selected = true;
				}
				
				element.options[0].selected = false;
			}
		}

		function onObjectTypeChange(element)
		{
			selectAll(element);
			var action = "/catissuecore/AssignPrivileges.do?pageOf=pageOfAssignPrivileges";
			document.forms[0].action = action;
			document.forms[0].submit();
		}

		function assign()
		{
			var action = "/catissuecore/AssignPrivileges.do?pageOf=pageOfAssignPrivileges";
			document.forms[0].action = action;
			document.forms[0].submit();
		}

	</SCRIPT>
</head>

<html:form action="<%=Constants.ASSIGN_PRIVILEGES_ACTION%>">
<table summary="" cellpadding="0" cellspacing="0" border="0" width="600">

<tr>
	<td colspan="6">
		&nbsp;
	</td>
</tr>

<!-- First Row -->
<tr>
	<td class="formTitle" height="20" colspan="6">
		<bean:message key="assignPrivileges.title"/>
	</td>
</tr>
<tr>
	<td class="formLeftSubTableTitle">
     	<bean:message key="assignPrivileges.assign"/>
    </td>
    <td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.privileges"/>
	</td>
    <td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.objectType"/>
	</td>
	<td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.recordId"/>
	</td>
	<td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.attribute"/>
	</td>
	<td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.group"/>
	</td>
</tr>
<!-- First Row -->

<!-- Second Row -->
<tr>
	<td class="formField">
     	<html:select property="assignOperation" styleClass="formFieldSized15" styleId="assignOperation" size="10">
			<html:options name="<%=Constants.ASSIGN%>" labelName="<%=Constants.ASSIGN%>"/>
		</html:select>
	</td>

	<td class="formField">
     	<html:select property="privileges" styleClass="formFieldSized15" styleId="privileges" size="10" multiple="true" onchange="selectAll(this)">
			<%--html:options name="<%=Constants.PRIVILEGES%>" labelName="<%=Constants.PRIVILEGES%>"/--%>
			<html:options collection="<%=Constants.PRIVILEGES%>" labelProperty="name" property="value"/>
		</html:select>
	</td>

	<td class="formField">
     	<html:select property="objectTypes" styleClass="formFieldSized15" styleId="objectTypes" size="10" multiple="true" onchange="onObjectTypeChange(this)">
			<%--html:options name="<%=Constants.OBJECT_TYPE_VALUES%>" labelName="<%=Constants.OBJECT_TYPES%>"/--%>
			<html:options collection="<%=Constants.OBJECT_TYPES%>" labelProperty="name" property="value"/>
		</html:select>
	</td>

	<td class="formField">
     	<html:select property="recordIds" styleClass="formFieldSized15" styleId="recordIds" size="10" multiple="true" onchange="selectAll(this)">
			<html:options collection="<%=Constants.RECORD_IDS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>

	<td class="formField">
     	<html:select property="attributes" styleClass="formFieldSized15" styleId="attributes" size="10" multiple="true" onchange="selectAll(this)">
			<html:options name="<%=Constants.ATTRIBUTES%>" labelName="<%=Constants.ATTRIBUTES%>"/>
		</html:select>
	</td>

	<td class="formField">
     	<html:select property="groups" styleClass="formFieldSized15" styleId="groups" size="10" multiple="true">
			<html:options collection="<%=Constants.GROUPS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- Second Row -->

<tr>
	<td colspan="6">
		&nbsp;
	</td>
</tr>

<tr>
	<td align="right" colspan="6">
		<html:button property="assignButton" styleClass="actionButton" styleId="assignButton" onclick="assign()">
	   		<bean:message key="buttons.assign"/>
	   	</html:button>
	</td>
</tr>
</table>
</html:form>