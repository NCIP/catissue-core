<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="java.util.ArrayList"%>

<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: consentDialog.jsp
	 Description: This jsp is associated with ConsentTracking.jsp for pageOf CollectionprotocolResistration,
	 SpecimenCollectionGroup and NewSpecimen.
	 Company: Washington University, School of Medicine, St. Louis.
-->		


<%
	String withdrawAll = request.getParameter(Constants.WITHDRAW_ALL);
	String getConsentResponse = request.getParameter(Constants.RESPONSE);
	String pageOf = request.getParameter("pageOf");
	Integer identifierFieldIndex = 4;
%>
<script language="JavaScript">

function getButtonStatus(element)
{
	var answer;
	if(element.value=="<%=Constants.WITHDRAW_RESPONSE_DISCARD %>")
	{
		answer= confirm("Are you sure you want to discard the Specimen and all Sub Specimen(disable)?");
	}
	else
	{
		answer= confirm("Are you sure you want to return Specimen to Collection Site?");	
	}
	if(answer)
	{
		parent.opener.document.forms[0].withdrawlButtonStatus.value=element.value;
		if(parent.opener.document.forms[0].name == "<%=Constants.NEWSPECIMEN_FORM%>")
		{
			if(element.value != "<%=Constants.WITHDRAW_RESPONSE_RESET %>")
			{
				parent.opener.document.forms[0].activityStatus.value="<%=Constants.ACTIVITY_STATUS_DISABLED%>" ;
				parent.opener.document.forms[0].onSubmit.value="<%=Constants.BIO_SPECIMEN%>";
				parent.opener.document.forms[0].target = "_top";
			}
			else
			{
					<%	if(pageOf.equals(Constants.PAGE_OF_SPECIMEN))
					{
					%>	
						parent.opener.document.forms[0].action="<%=Constants.SPECIMEN_EDIT_ACTION%>";	
					<%
					}
					else
				    {
					%>
						parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_EDIT_ACTION%>";				
					<%
					}
					%>
			}
		}
		parent.opener.document.forms[0].submit();
		self.close();
	}

}

function getStatus(element)
{
	var answer= confirm("Are you sure about your action on the Specimens");
	if(answer)
	{
		parent.opener.document.forms[0].applyChangesTo.value=element.value;
		if(parent.opener.document.forms[0].name == "<%=Constants.NEWSPECIMEN_FORM%>")
		{
			<%	
			if(pageOf.equals(Constants.PAGE_OF_SPECIMEN))
			{
			%>	
				parent.opener.document.forms[0].action="<%=Constants.SPECIMEN_EDIT_ACTION%>";
			<%
			}
			else
			{
			%>
				parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_EDIT_ACTION%>";
			<%
			}
			%>
		}
		else
		{
			parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION%>";
		}
		parent.opener.document.forms[0].submit();
		self.close();
	}
}
var useDefaultRowClickHandler=2;
var useFunction = "derivedSpecimenGrid";	

</script>

<html>
	<head>
		<%
			if(withdrawAll.equals(Constants.TRUE))
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
		
	</head>
		<body class="formRequiredNotice">
		<link rel="stylesheet" type="text/css" href="../../../css/styleSheet.css" />
		<link rel="STYLESHEET" type="text/css" href="../../../dhtml_comp/css/dhtmlXGrid.css"/>
		<script  src="../../../dhtml_comp/js/dhtmlXCommon.js"></script>
		<script  src="../../../dhtml_comp/js/dhtmlXGrid.js"></script>		
		<script  src="../../../dhtml_comp/js/dhtmlXGridCell.js"></script>	
		<script  src="../../../dhtml_comp/js/dhtmlXGrid_mcol.js"></script>	
		<%
			List dataList = (List)session.getAttribute(Constants.SPECIMEN_LIST);
			List columnList = (List)session.getAttribute(Constants.COLUMNLIST);
			if(dataList!=null&&dataList.size()>0)
			{
			%>
	
				<%@ include file="/pages/content/search/GridPage.jsp" %>

		<%
			}
		%>
		<%
		if(getConsentResponse.equals(Constants.WITHDRAW))
		{
		%>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
					<tr>
						<td class="formTitle" height="20%" colspan="2">
						<%
							if(withdrawAll.equals(Constants.TRUE))
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
					if(withdrawAll.equals(Constants.FALSE))									
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
		<%
		}
		else
		{
		%>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="tabrightmostcell">
						<b>
							<bean:message key="consent.currentstatusonnonconflictingspecimen" />
						</b>	
					</td>
					<td align="right" class="formField">
							<input type="button" style="actionButton" style="width:100%" value="<%=Constants.APPLY %>"  onclick="getStatus(this)"/>
						</td>
					</tr>
					<tr>
						<td class="tabrightmostcell">
							<b>
								<bean:message key="consent.currentstatusonallspecimen" />	
							<b>
						</td>
						<td align="right" class="formField">
							<input type="button" style="actionButton" style="width:100%" value="<%=Constants.APPLY_ALL%>"  onclick="getStatus(this)"/>
						</td>
					</tr>
			</table>
		<%
		}
		%>
		</body>
</html>