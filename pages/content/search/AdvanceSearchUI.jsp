<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.vo.SearchFieldData"%>
<%@ page import="edu.wustl.catissuecore.vo.AdvanceSearchUI"%>
<%@ page import="edu.wustl.common.util.SearchUtil"%>

<%
	AdvanceSearchUI advSearch = (AdvanceSearchUI)request.getAttribute("AdvanceSearchUI");
	SearchFieldData[] searchFieldData = advSearch.searchFieldData;
	int div = 0;
	String tempDiv = "overDiv";
	String overDiv = tempDiv;
	
	
%>

<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script src="jss/AdvancedSearchScripts.js" type="text/javascript"></script>
</head>

<html:errors />

<html:form action="AdvanceSearch.do">
<table summary="" cellpadding="5" cellspacing="0" border="0" width="600">
<tr>
	<td><html:hidden property="objectName" value="<%=advSearch.iconAltText%>"/></td>
	<td><html:hidden property="selectedNode" /></td>
	<td><html:hidden property="itemNodeId" /></td>
</tr>
<!--  MAIN TITLE ROW -->
<tr>
	<td class="formTitle" height="25" nowrap>
	    &nbsp;<img src="<%=advSearch.iconSrc%>" alt="<%=advSearch.iconAltText%>" /> &nbsp;
	    <bean:message key="<%=advSearch.titleKey%>"/>
	</td>
	<td class="formTitle" nowrap align="right" colspan="2">
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

<% for(int i = 0; i < searchFieldData.length; i++) 
	{
%>
		<tr>
			<td class="formSerialNumberField" nowrap>
		 		<label for="<%=searchFieldData[i].oprationField.id%>">
		 			<b><bean:message key="<%=searchFieldData[i].labelKey%>"/>
		 		</label>
			</td>
			
			<td class="formField">
				<html:select property="<%=searchFieldData[i].oprationField.name%>" styleClass="formFieldSized10" styleId="<%=searchFieldData[i].oprationField.id%>" size="1" onchange="<%= searchFieldData[i].functionName%>">
					<html:options collection="<%=searchFieldData[i].oprationField.dataListName%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		
		<%
		if((searchFieldData[i].dataType).equals(SearchUtil.STRING))
		{
			if((searchFieldData[i].valueField.dataListName).equals(""))
			{
		%>
				<td class="formField">
					<html:text styleClass="formFieldSized10" styleId="<%=searchFieldData[i].valueField.id%>" property="<%=searchFieldData[i].valueField.name%>" disabled="<%=searchFieldData[i].valueField.isDisabled%>"/>
				</td>
		<%
			}
			else
			{	
		%>
			<td class="formField">
				<html:select property="<%=searchFieldData[i].valueField.name%>" styleClass="formFieldSized10" styleId="<%=searchFieldData[i].valueField.id%>" size="1" disabled="<%=searchFieldData[i].valueField.isDisabled%>">
					<html:options collection="<%=searchFieldData[i].valueField.dataListName%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		<%
			}
		}
		else if((searchFieldData[i].dataType).equals(SearchUtil.DATE))
		{
			 
		%>
			<td class="formField" nowrap>
				<div id="<%=overDiv%>" style="position:absolute; visibility:hidden; z-index:1000;"></div>
				<html:text styleClass="formDateSized10" size="10" styleId="<%=searchFieldData[i].valueField.id + 1%>" property="<%=searchFieldData[i].valueField.name + ")"%>" disabled="<%=searchFieldData[i].valueField.isDisabled%>"/>
							 &nbsp;
				<a href="javascript:onDate('<%=searchFieldData[i].oprationField.id %>','<%=(advSearch.formName +"." + searchFieldData[i].valueField.id + 1)%>',false);">
					<img src="images\calendar.gif" width=24 height=22 border=0>
				</a>
							&nbsp;To&nbsp;
				<html:text styleClass="formDateSized10" size="10" styleId="<%=searchFieldData[i].valueField.id + 2%>" property="<%=searchFieldData[i].valueField.name + ":HLIMIT)"%>" disabled="<%=searchFieldData[i].valueField.isDisabled%>"/>
							 &nbsp;
				<a href="javascript:onDate('<%=searchFieldData[i].oprationField.id%>','<%=(advSearch.formName +"." + searchFieldData[i].valueField.id + 2)%>',true);">
					<img src="images\calendar.gif" width=24 height=22 border=0>
				</a>
			</td>
		<%
			//For different id of div tag
			div++;
			overDiv = tempDiv + div;
		}
		else if((searchFieldData[i].dataType).equals(SearchUtil.NUMERIC))
		{
		%>
			<td class="formField">
				<html:text styleClass="formFieldSized10" styleId="<%=searchFieldData[i].valueField.id + 1%>" property="<%=searchFieldData[i].valueField.name + 1%>" disabled="<%=searchFieldData[i].valueField.isDisabled%>"/> 
								&nbsp;To&nbsp;
				<html:text styleClass="formFieldSized10" styleId="<%=searchFieldData[i].valueField.id + 2%>" property="<%=searchFieldData[i].valueField.name + 2%>" disabled="<%=searchFieldData[i].valueField.isDisabled%>"/> 
				<bean:message key="<%=searchFieldData[i].unitFieldKey%>"/>
			</td>
		<%
		}
	}
	%>
	
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