<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.domain.ReportedProblem,edu.wustl.catissuecore.util.global.Constants"%>

<html:errors/>

<%
	  int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	  int totalResults = Integer.parseInt((String)request.getAttribute(Constants.TOTAL_RESULTS));
	  int numResultsPerPage = Integer.parseInt((String)request.getAttribute(Constants.RESULTS_PER_PAGE));
%>

</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">
	<tr>
		<td>
		<table summary="" cellpadding="0" cellspacing="0" border="0">
			<html:form action="/ProblemPendingClose">
				<tr>
					<td>
						<html:hidden property="operation" />
					</td>
				</tr>
				
				<tr>
					<td>
						<html:hidden property="systemIdentifier" />
					</td>
				</tr>
				
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						<bean:message key="reportedProblem.pageTitle" />	
					</td>
				</tr>
				
				<!-- paging begins -->
				<tr>
					<td colspan = "8" class="dataPagingSection">
						<custom:test name=" Reported Problem Search Results " pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="ReportedProblemShow.do"/>
					</td>
				</tr>
				
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						<html:checkbox property="value(userselectAll)" onclick="CheckAll(this)">
							<bean:message key="app.selectAll"/>
						</html:checkbox>
					</td>
				</tr>
				
				<!-- paging ends -->

				<tr>
					<td>
					<table summary="Enter summary of data here" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
						
						<tr>
							<th class="formSerialNumberLabelForTable" scope="col">
				     			#
				    		</th>
							<th class="dataTableHeader" scope="col" align="left">
								<bean:message key="reportedProblem.pendingClose" />
							</th>
							<th class="dataTableHeader" scope="col" align="left">
								<bean:message key="reportedProblem.from" />
							</th>
							<th class="dataTableHeader" scope="col" align="left">
								<bean:message key="reportedProblem.title" />
							</th>
							<th class ="dataTableHeader" scope="col" align="left">
								<bean:message key="reportedProblem.reportedDate" />
							</th>
						</tr>
						<logic:empty name="showDomainObjectList">
							<tr>
								<td class="dataTableWhiteCenterHeader" colspan="5">  
									<bean:message key="reportedProblem.noNewProblemFound" />
								</td>
							</tr>
						</logic:empty>
						<%int i=1;%>
						<logic:iterate id="problem" name="showDomainObjectList">
							<tr class="dataRowLight">
								<%
        								ReportedProblem reportedProblem = (ReportedProblem) problem;
										String checkBoxValue = reportedProblem.getSystemIdentifier().toString();
        								String checkBoxName = "value(problem" + checkBoxValue + ")";
										String problemDetailsLink = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.IDENTIFIER+"="+checkBoxValue;				
        						%>
        						<td class="dataCellText">
									<%=i%>
								</td>
								<td class="dataCellText">
									<html:checkbox property="<%=checkBoxName%>" value="<%=checkBoxValue%>" />
								</td>
								<td class="dataCellText">
										<bean:write	name="problem" property="from" /> 
								</td>
								<td class="dataCellText">
									<a href="<%=problemDetailsLink%>">
										<bean:write name="problem" property="subject" />
									</a>
								</td>
								<td class="dataCellText">
									<bean:write name="problem" property="reportedDate" />
								</td>
							</tr>
							<%i++;%>
						</logic:iterate>
					 </table>
					</td>
				</tr>
				
				<tr>
					<td align="right" class="actionSection">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
					
					<br/>
						<tr>
							<td>
								<%
									String setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_CLOSED+"')";
								%>
								<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
									<bean:message  key="buttons.close" />
								</html:submit>
							</td>
							<td>
								<%
									setOperation = "setOperation('"+Constants.APPROVE_USER_PENDING_STATUS+"')";
								%>
								<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
									<bean:message  key="buttons.pending" />
								</html:submit>
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
					</td>
				</tr>
			</html:form>
		</table>
		</td>
	</tr>
</table>
