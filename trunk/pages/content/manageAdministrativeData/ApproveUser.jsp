<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants, edu.wustl.catissuecore.domain.Address, edu.wustl.catissuecore.domain.User"%>

<html:errors/>

<%
	  int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	  int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	  int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));
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
						<custom:test name="New User Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="ApproveUserShow.do" showPageSizeCombo="<%=true%>" recordPerPageList="<%=Constants.RESULT_PERPAGE_OPTIONS%>"/>
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
						<%int i=1;%>
						<logic:iterate id="currentUser" name="showDomainObjectList">
						
							<tr class="dataRowLight">
								<%
        								User user = (User) currentUser;
										String identifier = user.getId().toString();
										String userDetailsLink = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
        						%>
								<td class="dataCellText">
									<%=i%>
								</td>
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
									<bean:write name="currentUser" property="emailAddress"/>
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="startDate" />
								</td>
							</tr>
							<%i++;%>
						</logic:iterate>
					 </table>
					</td>
				</tr>
				
			</html:form>
		</table>
		</td>
	</tr>
</table>