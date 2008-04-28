<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:errors/>
</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">
	<tr>
		<td>
		<table summary="" cellpadding="0" cellspacing="0" border="0">
			<html:form action="/ApproveUser">
				<tr>
					<td>
						<html:hidden property="operation" />
					</td>
				</tr>
				<tr>
					<td>
						<html:hidden property="id" />
					</td>
				</tr>
				
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						<bean:message key="approveUser.title" />	
					</td>
				</tr>

				<!-- paging begins -->
				<tr>
					<td colspan = "8" class="dataPagingSection">
						<custom:test name="New User Search Results" pageNum='${requestScope.pageNum}' totalResults='${requestScope.totalResults}' numResultsPerPage='${requestScope.numResultsPerPage}'pageName="ApproveUserShow.do" showPageSizeCombo="<%=true%>" recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}'/>
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
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.loginName" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.userName" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.emailAddress" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="approveUser.registrationDate" />
			<!-- Mandar : 10-Apr-06 : bugid: 1380 changes reverted -->
						</tr>
						<logic:empty name="showDomainObjectList">
						<tr>
							<td class="dataTableWhiteCenterHeader" colspan="5">  
								<bean:message key="approveUser.newUsersNotFound" />
							</td>
						</tr>
						</logic:empty>
						<c:set var="count" value="1" scope="page"/>
						<logic:iterate id="currentUser" name="showDomainObjectList">
						
							<tr class="dataRowLight">
								<td class="dataCellText">
									<c:out value='${pageScope.count}'/>
								</td>
								<td class="dataCellText">
									<a href='${requestScope.userDetailsLink}${currentUser.id}' >
										<bean:write	name="currentUser" property="loginName" />
									</a>
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="lastName" />,
									<bean:write name="currentUser" property="firstName" />
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="emailAddress"/>
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="startDate" />
								</td>
							</tr>
							<c:set var="count" value='${pageScope.count+1}'scope="page"/>
						</logic:iterate>
					 </table>
					</td>
				</tr>
				
			</html:form>
		</table>
		</td>
	</tr>
</table>