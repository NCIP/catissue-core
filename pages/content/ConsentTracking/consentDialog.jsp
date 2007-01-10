<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: consentDialog.jsp
	 Description: This jsp is associated with ConsentTracking.jsp for pageOf SCG and pageOf NewSpecimen.
	 Company: Washington University, School of Medicine, St. Louis.
-->		


<%
	String withdrawAll = request.getParameter("withrawall");
	System.out.println(withdrawAll);
%>

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
						<html:button styleClass="actionButton" style="width:100%" value="Discard" property="discard"/>
					</td>
				</tr>
				<tr>
					<td class="tabrightmostcell">
						<b>
							<bean:message key="consent.returntocollectionsite" />
						<b>
					</td>
					<td align="right" class="formField">
						<html:button styleClass="actionButton" style="width:100%" value="Return" property="return"/>
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
						<html:button styleClass="actionButton" value="Reset" style="width:100%" property="reset"/>
					</td>
				</tr>
				<%
				}
				%>
			</table>
		</body>
</html>