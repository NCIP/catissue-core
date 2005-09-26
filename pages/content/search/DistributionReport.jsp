<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionReportForm" %>
<%
        List dataList = (List)request.getAttribute("listOfData");
        String []columnNames = (String []) request.getAttribute("columnNames");
        DistributionReportForm distForm = (DistributionReportForm)request.getAttribute("distributionReportForm");
        String reportName = (String) request.getAttribute("reportName");
        //String reportName = "defaultReport";
%> 

<html:form action="ConfigureDistribution.do">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<tr>
		<td align="right" colspan="3">
			<html:hidden property="distributionId"/>
		</td>
	</tr>

	<!-- NEW distribution REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<TABLE cellSpacing=0 cellPadding=3 summary="" border=1 width="556">
		
		<tr>
			 <td class="formTitle" height="20" colspan="3">
					<bean:message key="distribution.reportTitle"/>
			 </td>
		</tr>

		<!-- Name of the distribution -->
<!-- Distribution Protocol Title -->		
		<tr>
			
			<td class="formRequiredLabel" width="200">
				<label for="type">
					<bean:message key="distribution.protocol"/> 
				</label>	
			</td>	
			<td class="formField" width="300">
				<%=distForm.getDistributionProtocolTitle()%>				
			</td>	
		</tr>

<!-- User -->		
		<tr>
			
			<td class="formRequiredLabel" width="200">
				<label for="User">
					<bean:message key="eventparameters.user"/> 
				</label>	
			</td>	
			<td class="formField" width="300">
				<%=distForm.getUserName()%>		
			</td>	
		</tr>

<!-- date -->		
		<tr>
			
			<td class="formRequiredLabel" width="200">
				<label for="date">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField" width="300">
				 <%=distForm.getDateOfEvent()%>
			</td>
		</tr>

<!-- hours & minutes -->		
		<tr>
			
			<td class="formLabel" width="200">
				<label for="time">
					<bean:message key="eventparameters.time"/>&nbsp; 
					
				</label>
			</td>
			<td class="formField" width="300">
				&nbsp;<%=distForm.getTimeInHours()%>:<%=distForm.getTimeInMinutes()%>
			</td>
		</tr>
<!-- fromSite -->		
		<tr>
			
			<td class="formRequiredLabel" width="200">
				<label for="fromSite">
					<bean:message key="distribution.fromSite"/> 
				</label>
			</td>
			<td class="formField" width="300">
				<%=distForm.getFromSite()%>
			</td>
		</tr>
<!-- toSite -->		
		<tr>
			
			<td class="formRequiredLabel" width="200">
				<label for="toSite">
					<bean:message key="distribution.toSite"/> 
				</label>
			</td>
			<td class="formField" width="300">
				
				<%=distForm.getToSite()%>
			</td>
		</tr>				

<!-- comments -->		
		<tr>
			
			<td class="formLabel" width="200">
				<label for="type">
					<bean:message key="eventparameters.comments"/> 
				</label>
			</td>
			<td class="formField" width="300">
				&nbsp;<%=distForm.getComments()%>
			</td>
		</tr>
	</table>
	
	<table cellpadding="3" cellspacing="0" border="0">
		<tr></tr>
		<tr></tr>
		<tr></tr>
	</table>
	
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="433">
<!--  Distributed Item begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="<%=columnNames.length%>">
				     	<bean:message key="distribution.distributedItem"/>
				     </td>
				     
				  </tr>
				 <tr>
				 	<% 
				 		for(int i=0;i<columnNames.length;i++)
				 		{
				 	%>
						    <td class="formLeftSubTableTitle" >
								<%=columnNames[i]%>		
							</td>
					<%
						}
					%>
					
				 </tr>
				 <%
				 	Iterator itr= dataList.iterator();
				 	while(itr.hasNext())
				 	{
				 %>
				 	<tr>
				 <%
				 		List rowData = (List)itr.next();
				 		Iterator innerItr= rowData.iterator();
				 		while(innerItr.hasNext())
				 		{
				 %>
				 		<tr>
				 <%
				 			List rowElements = (List)innerItr.next();
				 			Iterator elementItr= rowElements.iterator();
				 			while(elementItr.hasNext())
				 			{
				 %>
				 				<td class="formField">
				 					&nbsp;<%=elementItr.next()%>	
				 				</td>
				 	<%
				 			}
				 	%>
				 		</tr>
				 	<%
				 		}
				 	%>
				 	</tr>
				 <%
					}
				%>
			<!-- Distributed item End here -->
<!-- buttons -->
		<tr>
		  <td align="right" colspan="6">
			<!-- action buttons begins -->
			
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						<html:submit styleClass="actionButton" value="Print" />
					</td>
					<td>
						<html:submit styleClass="actionButton" value="Save" />
					</td>
					<%
					if("defaultReport".equals(reportName))
						{
					%>	
						<td>
							<html:submit styleClass="actionButton" value="Configure" />
						</td>
					<%
						}
					%>
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Distribution ends-->
	 
	 </table>
</html:form>			
