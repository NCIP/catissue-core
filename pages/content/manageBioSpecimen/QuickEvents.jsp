<!-- 
	This JSP page is to create specimen events. Specimens are to be searched based on specimen id or barCode.
	Author : Mandar Deshmukh
	Date   : July 03, 2006
-->

<%@ page import="org.apache.struts.action.ActionMessages"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.QuickEventsForm"%>
<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ include file="/pages/content/common/EventAction.jsp" %> 


<head>


	
	<script language="JavaScript">
		function onRadioButtonClick(element)
		{
	
			if(element.value == 1)
			{
				document.forms[0].specimenLabel.disabled = false;
				document.forms[0].barCode.disabled = true;
			}
			else
			{
				document.forms[0].barCode.disabled = false;
				document.forms[0].specimenLabel.disabled = true;
			}
		}
		
		// called when the event is selected from the combo
		function onParameterChange()
		{
						
			document.forms[0].action = "QuickEventsSearch.do";//action;			
			document.forms[0].submit();		
		}		
		
	</script>
</head>


<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form method='POST' action="<%=Constants.QUICKEVENTS_ACTION%>">

<%

String eventSelected = (String)request.getAttribute(Constants.EVENT_SELECTED);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
String iframeSrc="blankScreenAction.do";
if(eventSelected != null)	
{
	iframeSrc = getEventAction(eventSelected, specimenIdentifier);
	//formAction = Constants.QUICKEVENTSPARAMETERS_ACTION;
}

session.setAttribute("EventOrigin", "QuickEvents");
%>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
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
		<TD class="formRequiredLabelLeftBorder" width="120">
			<label for="parentId">
				<bean:message key="quickEvents.specimenLabel"/>
			</label>
		</TD>
		<td class="formField">
			<logic:equal name="quickEventsForm" property="checkedButton" value="1">
					<html:text styleClass="formFieldSized10"  maxlength="50"  size="30"  styleId="specimenLabel" property="specimenLabel" disabled="false"/>
			</logic:equal>
			
			<logic:equal name="quickEventsForm" property="checkedButton" value="2">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30"  styleId="specimenLabel" property="specimenLabel" disabled="true"/>
			</logic:equal>
		</td>
		<td class="formRequiredLabelBoth" width="5">*</td>
		<td class="formRequiredLabel">
			<bean:message key="quickEvents.eventparameters"/>
		</td>
		<td class="formField" colspan="2">
		
		 <autocomplete:AutoCompleteTag property="specimenEventParameter"
										  optionsList = "<%=request.getAttribute(Constants.EVENT_PARAMETERS_LIST)%>"
										  initialValue="<%=request.getParameter("specimenEventParameter")%>"
			
									    />
			
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
			<%--<html:submit styleClass="actionButton">
				<bean:message  key="quickEvents.add" />
			</html:submit>--%>
			<html:button property="quickEventsButton" onclick="onParameterChange()">
			<bean:message  key="quickEvents.add" />
		</html:button>
			
		</td>
	</tr>
	</table>
</td>
</tr>

<tr>
<td>
	<iframe name="newEventFrame" id="newEventFrame" src="<%=iframeSrc %>" width="650" height="400" frameborder="0" scrolling="auto">
	</iframe>
</td>
</tr>


</table>
</html:form>




