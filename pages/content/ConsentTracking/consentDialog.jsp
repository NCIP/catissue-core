<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>


<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: consentDialog.jsp
	 Description: This jsp is associated with ConsentTracking.jsp for pageOf CollectionprotocolResistration,
	 SpecimenCollectionGroup and NewSpecimen.
	 Company: Washington University, School of Medicine, St. Louis.
-->		


<%
	String withdrawAll = request.getParameter("withrawall");
	System.out.println(withdrawAll);
%>
<script language="JavaScript">

function getButtonStatus(element)
{
	parent.opener.document.forms[0].withdrawlButtonStatus.value=element.value;
	//alert(parent.opener.document.forms[0].name);
	if(parent.opener.document.forms[0].name == "newSpecimenForm")
	{
		parent.opener.document.forms[0].onSubmit.value="<%=Constants.BIO_SPECIMEN%>";
		parent.opener.document.forms[0].activityStatus.value="<%=Constants.ACTIVITY_STATUS_DISABLED%>" ;
	}
	parent.opener.document.forms[0].submit();
	self.close();
}


</script>

<html>
	<head>
		<%
			if(withdrawAll.equals("true"))
			{
		%>	
				<title><bean:message key="consent.withdrawconsents"/></title>	 
		<%		
			}
			else
			{
		%>	
				<title><bean:message key="consent.withdrawconsenttier" /></title>	 
		<%		
			}	
		%>	
		<link rel="stylesheet" type="text/css" href="../../../css/styleSheet.css" />
	</head>
		<body class="formRequiredNotice">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formTitle" height="20%" colspan="2">
					<%
						if(withdrawAll.equals("true"))
						{
					%>	
							<b><bean:message key="consent.withdrawspecimens" /></b>
					<%		
						}
						else
						{
					%>	
							<b><bean:message key="consent.withdrawquestion" /></b>
					<%		
						}
					%>		
					</td>
				</tr>
				<tr>
					<td class="tabrightmostcell">
						<b>
							<bean:message key="consent.discard" />
						</b>	
					</td>
					<td align="right" class="formField">
						<input type="button" style="actionButton" style="width:100%" value="<%=Constants.WITHDRAW_RESPONSE_DISCARD %>"  onclick="getButtonStatus(this)"/>
					</td>
				</tr>
				<tr>
					<td class="tabrightmostcell">
						<b>
							<bean:message key="consent.returntocollectionsite" />
						<b>
					</td>
					<td align="right" class="formField">
						<input type="button" style="actionButton" style="width:100%" value="<%=Constants.WITHDRAW_RESPONSE_RETURN%>"  onclick="getButtonStatus(this)"/>
					</td>
				</tr>
				<%
				if(withdrawAll.equals("false"))									
				{
				%>
				<tr>
					<td class="tabrightmostcell">
						<b>
							<bean:message key="consent.noaction" />
						<b>
					</td>
					<td align="right" class="formField">
						<input type="button" style="actionButton" value="<%=Constants.WITHDRAW_RESPONSE_RESET %>" style="width:100%" onclick="getButtonStatus(this)"/>
					</td>
				</tr>
				<%
				}
				%>
			</table>
		</body>
</html>