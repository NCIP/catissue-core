<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants, edu.wustl.catissuecore.domain.Address, edu.wustl.catissuecore.domain.ApplicationUser"%>

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
			<html:form action="/ApproveUser">
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
						<bean:message key="approveUser.title" />	
					</td>
				</tr>

				<!-- paging begins -->
				<tr>
					<td colspan = "8" class="dataPagingSection">
						<custom:test name="New User Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="ApproveUserShow.do"/>
					</td>
				</tr>
				<!-- paging ends -->				
				
				<tr>
					<td>
					<table summary="Enter summary of data here" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
						
						<tr>
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
							</th>
						</tr>
						
						<logic:iterate id="currentUser" name="showDomainObjectList">
							<tr class="dataRowLight">
								<%
        								ApplicationUser user = (ApplicationUser) currentUser;
										String identifier = user.getSystemIdentifier().toString();
										//String userDetailsLink = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+identifier;
										String userDetailsLink = "User.do?operation=edit&amp;pageOf=query";
        						%>
								<td class="dataCellText">
									<a href="<%=userDetailsLink%>" >
										<bean:write	name="currentUser" property="loginName" />
									</a>
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="lastName" />,
									<bean:write name="currentUser" property="firstName" />
								</td>
								<td class="dataCellText">
									<bean:define id="csmUser" name="currentUser" property="user"/>
									<bean:write name="csmUser" property="emailAddress"/>
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="dateAdded" />
								</td>
							</tr>
						</logic:iterate>
					 </table>
					</td>
				</tr>
				
			</html:form>
		</table>
		</td>
	</tr>
</table>