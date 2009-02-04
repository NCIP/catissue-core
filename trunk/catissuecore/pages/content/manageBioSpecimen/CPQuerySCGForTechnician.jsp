<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>



<%
	String scgId = (String)request.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID);
%>
<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
	function AddSpecimen()
	{
		document.forms[0].action = "CPQueryNewSpecimen.do?operation=add&pageOf=pageOfNewSpecimenCPQuery&menuSelected=15&virtualLocated=true&<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>";
		document.forms[0].submit();
	}
	function AddMultipleSpecimen()
	{
		document.forms[0].action = "CPQueryInitMultipleSpecimen.do?operation=add&pageOf=pageOfMultipleSpecimenCPQuery&<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>";
		document.forms[0].submit();
	
	}

	</script>
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:form action="CPQueryNewSpecimen.do?operation=add&pageOf=pageOfNewSpecimenCPQuery&menuSelected=15&virtualLocated=true<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>">
<table cellpadding="4" cellspacing="0" border="0">
	<tr>
		<td>
			<table>
				<tr>
					<td colspan="2">
				
					</td>
				</tr>
			
				<tr>
					<td nowrap class="formFieldNoBorders">
					<html:button styleClass="actionButton"  
					property="submitPage" 
					title="Add Specimen"
					value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][0]%>" 
					onclick="AddSpecimen();">
			     	</html:button>
					</td>
					<td nowrap class="formFieldNoBorders">
					<html:button styleClass="actionButton"  
					property="submitPage" 
					title="Add Multiple Specimen"
					value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][0]%>" 
					onclick="AddMultipleSpecimen();">
			     	</html:button>
					</td>
				</tr>
			</table>
		</td>					
	</tr>
</table>
</html:form>