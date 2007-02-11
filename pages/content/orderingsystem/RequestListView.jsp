<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		function filteringAction()
		{
			document.forms[0].action="RequestListView.do?pageNum=1&menuSelected=17";
			document.forms[0].submit();
		}		
	</script>
</head>
<body>
  <html:form action="RequestListView.do"> 
	  <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
		<tr>
			<td >
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" >
				<!-- Header Part Begins -->
					  <tr>
						<td class="dataTablePrimaryLabel" height="20" colspan="2"><bean:message key='header.requestList.label'/></td>
					  </tr>					 
					  <jsp:useBean id="requestListForm" class="edu.wustl.catissuecore.actionForm.RequestListFilterationForm" scope="request"/>
					  <tr>				
						<td class="formRequiredLabel" width="20%" colspan="2" style="border-left:1px solid #5C5C5C">
							<bean:message key='order.totalRequests'/> :<% int totalRequests=requestListForm.getNewRequests()+requestListForm.getPendingRequests();%>
							<%= totalRequests %>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<bean:message key='order.totalRequestsNew'/> : <bean:write name="requestListForm" property="newRequests"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<bean:message key='order.totalRequestsPending'/> : <bean:write name="requestListForm" property="pendingRequests"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
						</td>						  
						
					  </tr>				  
					  <tr>
						<td class="formLabel" style="border-left:1px solid #5C5C5C"><bean:message key='request.status.show.label'/></td>
						<td  class="formField">
						<html:select property="requestStatusSelected" name="requestListForm" styleClass="formFieldSized15"  styleId="requestStatusSelected" size="1"   
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="filteringAction()">
								<html:options collection="<%= Constants.REQUEST_LIST %>" labelProperty="name" property="value"/>		
						</html:select>
						</td>
					  </tr>
				</table>
					  <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" >
					  <!-- paging begins -->
					  <%
						  int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
						  int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
						  int numResultsPerPage = Constants.NUMBER_RESULTS_PER_PAGE;
					   %>
						<tr>
							<td colspan = "8" class="dataPagingSection">
								<custom:test name=" Request List Queue: " pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="RequestListView.do"/>
							</td>
						</tr>
				
						<!-- paging ends -->
					  <!-- Header Part Ends -->
					  </br>
					  <tr>					
						<td colspan="6">
						<!-- For DataList Display -->
							<table cellpadding="3" cellspacing="0" border="0" class="dataTable" width="100%" >
								<tr>
									<th class="formSerialNumberLabelForTable" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.serialNo.label'/> 
									</th>							
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.OrderName.label'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.DistributionProtocol.label'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.label.RequestedBy'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.label.Email'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.label.RequestDate'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left">
										<bean:message key='requestlist.dataTabel.label.Status'/>
									</th>
								</tr>
									<%  List requestListData = (List)request.getAttribute("RequestList");						
									%>	
								<logic:iterate id="rowDataString" collection="<%=requestListData%>" type="edu.wustl.catissuecore.bean.RequestViewBean">
								<tr class="dataRowLight">
									<td class="dataCellText">
										<bean:write name="rowDataString" property="serialNo"/>
								    </td>												
									<td class="dataCellText">
										<a href="RequestDetails.do?id=<bean:write name='rowDataString' property='requestId'/>&menuSelected=17"><bean:write name="rowDataString" property="orderName"/></a>
									</td>
									<td class="dataCellText">
										 <bean:write name="rowDataString" property="distributionProtocol"/>
									</td>
									<td class="dataCellText">
										 <bean:write name="rowDataString" property="requestedBy"/>
									</td>
									<td class="dataCellText">
										 <a href='mailto:<bean:write name="rowDataString" property="email"/>' ><bean:write name="rowDataString" property="email"/> <a/>
										 
									</td>
									<td class="dataCellText">
										 <bean:write name="rowDataString" property="requestedDate"/>
									</td>
									<td class="dataCellText">
										 <bean:write name="rowDataString" property="status"/>
									</td>				
									</tr>	
															
								</logic:iterate>						
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	 </table>
  </html:form>   
</body>
