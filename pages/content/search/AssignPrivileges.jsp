<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AssignPrivilegesForm"%>

<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%
List usersForUse = (List) request.getAttribute(Constants.USERS_FOR_USE_PRIVILEGE);
List usersForRead = (List) request.getAttribute(Constants.USERS_FOR_READ_PRIVILEGE);
AssignPrivilegesForm form = (AssignPrivilegesForm)request.getAttribute("assignPrivilegesForm");
%>
	<SCRIPT language="JavaScript">
	var usersNamesForRead = new Array();
	var usersIdsForRead = new Array();
	var i=0;

	<%
		Iterator usersForReadItr = usersForRead.iterator();
		while(usersForReadItr.hasNext())
		{	NameValueBean  nameIdBean = (NameValueBean)usersForReadItr.next();
			%>
			usersNamesForRead[i]="<%=nameIdBean.getName()%>";
			usersIdsForRead[i]="<%=nameIdBean.getValue()%>";
			i++;
	<%	} %>
	var usersNamesForUse = new Array();
	var usersIdsForUse= new Array();

	i=0;
	<%
		Iterator usersForUseItr = usersForUse.iterator();
		while(usersForUseItr.hasNext())
		{	NameValueBean  nameIdBean = (NameValueBean)usersForUseItr.next();
			%>
			usersNamesForUse[i]="<%=nameIdBean.getName()%>";
			usersIdsForUse[i]="<%=nameIdBean.getValue()%>";
			i++
	<%	} %>

	
	
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
			var action = "AssignPrivilegesPage.do?pageOf=pageOfAssignPrivilegesPage";
			document.forms[0].action = action;
			document.forms[0].submit();
		}

		function onPrivilegeChange(element)
		{
			var objectCombo = document.getElementById("objectTypes");
			var recordCombo = document.getElementById("recordIds");
			var userCombo = document.getElementById("groups");
			
			objectCombo.options.length = 0;
			recordCombo.options.length = 0;
			
			if(element.options[element.selectedIndex].text == "READ")
			{
				// Gautam : Commented as per Marks comments.
				//objectCombo.options[0] = new Option("Participant","edu.wustl.catissuecore.domain.Participant");
				objectCombo.options[0] = new Option("Collection Protocol","edu.wustl.catissuecore.domain.CollectionProtocol");
				//objectCombo.options[1] = new Option("Distribution Protocol","edu.wustl.catissuecore.domain.DistributionProtocol");
				//objectCombo.options[3] = new Option("Specimen Collection","edu.wustl.catissuecore.domain.SpecimenCollectionGroup");
				//objectCombo.options[4] = new Option("Specimen","edu.wustl.catissuecore.domain.Specimen");
				userCombo.options.length = 0;
				for(var i=0;i<usersNamesForRead.length;i++)
				{
					userCombo.options[i] = new Option(usersNamesForRead[i],usersIdsForRead[i]);
				}

			}
			else if(element.options[element.selectedIndex].text == "USE")
			{
				objectCombo.options[0] = new Option("Site","edu.wustl.catissuecore.domain.Site");
				objectCombo.options[1] = new Option("Storage Container","edu.wustl.catissuecore.domain.StorageContainer");
				userCombo.options.length = 0;
				for(var i=0;i<usersNamesForUse.length;i++)
				{
					
					userCombo.options[i] = new Option(usersNamesForUse[i],usersIdsForUse[i]);
				}
			}
		}

		function assign()
		{
			var action = "AssignPrivileges.do?pageOf=pageOfAssignPrivileges";
			document.forms[0].action = action;
			document.forms[0].submit();
		}

	</SCRIPT>
</head>

<html:errors/>

<html:form action="<%=Constants.ASSIGN_PRIVILEGES_ACTION%>">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="500">

<tr>
	<td>&nbsp;&nbsp;</td>
	<td colspan="3">
		&nbsp;
	</td>
</tr>

<!-- Title Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td class="formTitle" height="25" colspan="3">
		<bean:message key="assignPrivileges.title"/>
	</td>
</tr>
<!-- Title Row -->

<!-- SubTitle-1 Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td class="formLeftSubTableTitle" height="25">
     	<bean:message key="assignPrivileges.assign"/>
    </td>

    <td class="formRightSubTableTitle" colspan="2">
		<bean:message key="assignPrivileges.privileges"/>
	</td>
</tr>
<!-- SubTitle-1 Row -->

<!-- Assign/Privilege Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td class="formSerialNumberField" >
<!-- Mandar : 434 : for tooltip -->
     	<html:select property="assignOperation" styleClass="formFieldSized15" styleId="assignOperation" size="1"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options name="<%=Constants.ASSIGN%>" labelName="<%=Constants.ASSIGN%>"/>
		</html:select>
	</td>

	<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
     	<html:select property="privilege" styleClass="formFieldSized15" styleId="privileges" size="1" onchange="onPrivilegeChange(this)"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.PRIVILEGES%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
</tr>
<!-- Assign/Privilege Row -->

<!-- SubTitle-2 Row -->
<tr>
	<td height="25">&nbsp;&nbsp;</td>
    <td class="formLeftSubTableTitle">
		<bean:message key="assignPrivileges.objectType"/>
	</td>

	<td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.recordId"/>
	</td>

	<td class="formRightSubTableTitle">
		<bean:message key="assignPrivileges.group"/>
	</td>
</tr>
<!-- SubTitle-2 Row -->

<!-- Objct Type/.../Group Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td class="formSerialNumberField">
<!-- Mandar : 434 : for tooltip -->
     	<html:select property="objectType" styleClass="formFieldSized15" styleId="objectTypes" size="10" onchange="onObjectTypeChange(this)"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.OBJECT_TYPES%>" labelProperty="name" property="value"/>
		</html:select>
	</td>
	
	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
     	<html:select property="recordIds" styleClass="formFieldSized15" styleId="recordIds" size="10" multiple="true" onchange="selectAll(this)"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.RECORD_IDS%>" labelProperty="name" property="value"/>
		</html:select>
	</td>

	<td class="formField">
<!-- Mandar : 434 : for tooltip -->
     	<html:select property="groups" styleClass="formFieldSized15" styleId="groups" size="10" multiple="true"
		 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
		<% if(form.getPrivilege()!=null )
			{
			 if(form.getPrivilege().equals("READ")) 
			 {
		%>
			<html:options collection="<%=Constants.USERS_FOR_READ_PRIVILEGE%>" labelProperty="name" property="value"/>
		<%	} else if(form.getPrivilege().equals("USE")){
		%>
			<html:options collection="<%=Constants.USERS_FOR_USE_PRIVILEGE%>" labelProperty="name" property="value"/>
		<% } }
		%>
			
		</html:select>
	</td>
</tr>
<!-- Objct Type/.../Group Row -->

<!-- Empty Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td colspan="3">
		&nbsp;
	</td>
</tr>
<!-- Empty Row -->

<!-- Button Row -->
<tr>
	<td>&nbsp;&nbsp;</td>
	<td align="right" colspan="3">
		<html:button property="assignButton" styleClass="actionButton" styleId="assignButton" onclick="assign()">
	   		<bean:message key="buttons.assign"/>
	   	</html:button>
	</td>
</tr>
<!-- Button Row -->

</table>
</html:form>