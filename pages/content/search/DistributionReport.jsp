<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionReportForm" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ConfigureResultViewForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.SendFile"%>
<%
        List dataList = (List)request.getAttribute(Constants.DISTRIBUTED_ITEMS_DATA);
        String []columnNames = (String []) request.getAttribute(Constants.COLUMN_NAMES_LIST);
        DistributionReportForm distForm = (DistributionReportForm)request.getAttribute(Constants.DISTRIBUTION_REPORT_FORM);
		ConfigureResultViewForm form = (ConfigureResultViewForm)request.getAttribute("configureResultViewForm");
		String []selectedColumns=form.getSelectedColumnNames();
		
%> 
<script language="JavaScript">
	function changeAction()
	{
		document.forms[0].reportAction.value="false";
		selectOptions(document.forms[0].selectedColumnNames);
		setFormAction("<%=Constants.DISTRIBUTION_REPORT_SAVE_ACTION%>");
		//document.forms[0].action = "<%=Constants.DISTRIBUTION_REPORT_ACTION%>";
		//document.forms[0].submit();
	}
	
	function changeActionOnConfig()
	{
		document.forms[0].reportAction.value="true";
		selectOptions(document.forms[0].selectedColumnNames);
		setFormAction("<%=Constants.CONFIGURE_DISTRIBUTION_ACTION%>");
		//document.forms[0].action = "<%=Constants.DISTRIBUTION_REPORT_ACTION%>";
		//document.forms[0].submit();
	}
	
	function selectOptions(element)
	{
		for(i=0;i<element.length;i++) 
		{
			element.options[i].selected=true;
		}
	}
	
</script>
<style>
	tr#hiddenCombo
	{
	 display:none;
	}
	
</style>
<html:form action="<%=Constants.CONFIGURE_DISTRIBUTION_ACTION%>">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
	<tr>
		<td align="right" colspan="3">
			<html:hidden property="distributionId" />
			<html:hidden property="nextAction"/>
		</td>
		
		<td align="right" colspan="3">
			<html:hidden property="reportAction" value="true"/>
		</td>
		
	</tr>

	<!-- NEW distribution REGISTRATION BEGINS-->
	<tr> 
	<td>
	
	<TABLE cellSpacing=0 cellPadding=3 summary="" border=1 width="100%">
		
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
		<tr id="hiddenCombo" rowspan="4">
			<td class="formField" colspan="4">
	       		<html:select property="selectedColumnNames" styleClass="selectedColumnNames"  size="1" styleId="selectedColumnNames" multiple="true">
	       		<%
	       			for(int j=0;j<selectedColumns.length;j++)
	       			{
	       		%>
						<html:option value="<%=selectedColumns[j]%>"><%=selectedColumns[j]%></html:option>
				<%
					}
				%>
           	 	</html:select>
        	</td>
		</tr>
	</table>
	
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
<!--  Distributed Item begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="<%=columnNames.length+1%>">
				     	<bean:message key="distribution.distributedItem"/>
				     </td>
				     
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				 	<% 
				 		for(int i=0;i<columnNames.length;i++)
				 		{
				 	%>
						    <td class="formLeftSubTitle" >
								<%=columnNames[i]%>		
							</td>
					<%
						}
					%>
					
				 </tr>
				 <%
				 	Iterator itr= dataList.iterator();
				 	int i=1;
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
				 			<td class="formSerialNumberField" width="5"><%=i%>
				 			</td>
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
				 			i++;
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
					<%--
						String changeAction = "setFormAction('"+Constants.DISTRIBUTION_REPORT_ACTION+"')";
				 	--%>
					<td>
						<html:submit property="expButton" styleClass="actionButton" onclick="changeAction()" >
							<bean:message  key="buttons.export" />
						</html:submit>
					
					</td>
					<td>
						<html:submit styleClass="actionButton" onclick="changeActionOnConfig()">
							<bean:message  key="buttons.configure" />
						</html:submit>
					</td>
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
