
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>

<%
	String access = null;
	access = (String)session.getAttribute("Access");
		

%>

<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">
		function onCpChange(element)
		{
			var cpId = document.getElementById("cpId");
			var participantId = document.getElementById("participantId");
			 <%if(access != null && access.equals("Denied"))
			{%>
				window.parent.frames['<%=Constants.CP_TREE_VIEW%>'].location = "showTree.do?<%=Constants.CP_SEARCH_PARTICIPANT_ID%>=-1&<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value;			
			<%} else {%>
			window.parent.frames['<%=Constants.CP_TREE_VIEW%>'].location = "showTree.do";							
			var action = "showCpAndParticipants.do?cpChange=true";
			document.forms[0].action = action;
			
			document.forms[0].submit();
			<%}%>
		}
		
		function onParticipantChange(element)
		{
			var cpId = document.getElementById("cpId");
			var participantId = document.getElementById("participantId");
			window.parent.frames[2].location = "QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=edit&<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value+"&id="+participantId.value;
			window.parent.frames['<%=Constants.CP_TREE_VIEW%>'].location = "showTree.do?<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+participantId.value+"&<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value;			
			
			
		}
		
		function RegisterParticipants()
		{
			var cpId = document.getElementById("cpId");
			if(cpId.value == "-1")
			{
				alert("please select collection protocol.");
			}
			else
			{
				window.parent.frames[2].location = "CPQueryCollectionProtocolRegistration.do?<%=Constants.CP_SEARCH_CP_ID%>="+cpId.value+"&operation=add&pageOf=<%=Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY%>";
			}
		}	
		

</script>	
	
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="showCpAndParticipants.do">

<table summary="" cellpadding="0" cellspacing="0" border="0">
	<tr>

		<td class="formLabelBorderlessLeft">
			<b>Collection Protocol :</b> <%if(access == null || !access.equals("Denied"))
	{%> <html:link href="#" styleId="register" onclick="RegisterParticipants()">Register</html:link><%}%>
		</td>
	</tr>		
	<tr>

		<td class="formField" nowrap>
			<html:select property="cpId" styleClass="formFieldSized15" styleId="cpId" size="1" onchange="onCpChange(this)"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.CP_LIST%>" labelProperty="name" property="value"/>
			</html:select>
			
			
		</td>
	</tr>
		
	
	 <%if(access != null && access.equals("Denied"))
	{%>
	<tr>

		<td nowrap>
			<html:hidden property="participantId" styleId="participantId" value="-1"/>
		</td>
	</tr>
	<%} else {%>	
	<tr>

		<td class="formLabelBorderlessLeft">
			<b>Participant :</b> Name (Protocol Id) 
		</td>
	</tr>	
	
	<tr>

		<td class="formField" nowrap>
			<html:select property="participantId" styleClass="formFieldSized15" styleId="participantId" size="8" onchange="onParticipantChange(this)"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<html:options collection="<%=Constants.REGISTERED_PARTICIPANT_LIST%>" labelProperty="name" property="value"/>
			</html:select>
	
		</td>
	</tr>
		<%}%>
</table>		
</html:form>