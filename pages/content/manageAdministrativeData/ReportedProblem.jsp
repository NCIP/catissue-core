<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html:errors/>
</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">
	<tr>
		<td>
<html:form action="/ReportedProblemShow">
		<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						<bean:message key="reportedProblem.pageTitle" />	
					</td>
				</tr>
				
				<!-- paging begins -->
				<tr>
					<td colspan = "8" class="dataPagingSection">
						<custom:test name=" Reported Problem Search Results " pageNum='${requestScope.pageNum}' totalResults='${requestScope.totalResults}' numResultsPerPage='${requestScope.numResultsPerPage}' pageName="ReportedProblemShow.do" showPageSizeCombo="<%=true%>" recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}'/>
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
<!-- Mandar : 10-Apr-06 Bug id:1291 : Display Problem ID -->
							<th class="formSerialNumberLabelForTable" scope="col">
								Problem ID
							</th>
<!-- Mandar : 10-Apr-06 Bug id:1291 : end -->
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
						
						
						<c:set var="count" value="1" scope="page"/>
						<logic:iterate id="problem" name="showDomainObjectList">
							<tr class="dataRowLight">
								<td class="dataCellText">
								<c:out value='${pageScope.count}'/>
								</td>
<!-- Mandar : 10-Apr-06 Bug id:1291 : Display Problem ID -->
								<td class="dataCellText">
									<bean:write name="problem" property="id"/>
								</td>
<!-- Mandar : 10-Apr-06 Bug id:1291 : Display Problem ID end -->

								<td class="dataCellText">
										<bean:write	name="problem" property="from" /> 
								</td>
								<td class="dataCellText">
									<a href="${requestScope.problemDetailsLink}${problem.id}">
										<bean:write name="problem" property="subject" />
									</a>
								</td>
								<td class="dataCellText">
									<bean:write name="problem" property="reportedDate" />
								</td>
							</tr>
							<c:set var="count" value='${pageScope.count+1}' scope="page"/>
							</logic:iterate>
					 </table>
					</td>
				</tr>
		</table>
</html:form>
		</td>
	</tr>
</table>
