<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.domain.User,edu.wustl.catissuecore.util.global.Constants"%>

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
						<html:hidden property="identifier" />
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
					<td class="dataTablePrimaryLabel" height="20">
						<html:checkbox property="value(userselectAll)" onclick="CheckAll(this)">
							<bean:message key="app.selectAll"/>
						</html:checkbox>
					</td>
				</tr>
				
				<tr>
					<td>
					<table summary="Enter summary of data here" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
						
						<tr>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="approveUser.approvereject" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.loginName" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.userName" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="user.email" />
							</th>
							<th class="dataTableHeader" scope="col" align="center">
								<bean:message key="approveUser.registrationDate" />
							</th>
						</tr>
						<%
							int i=0;
						%>
						
						<logic:iterate id="currentUser" name="showDomainObjectList">
							<tr class="dataRowLight">
								<%
        								User user = (User) currentUser;
        								String checkBoxName = "value(user" + i + ")";
										String checkBoxValue = user.getIdentifier().toString();
										String userDetailsLink = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+checkBoxValue;
										i++;
        						%>
								<td class="dataCellText">
									<html:checkbox property="<%=checkBoxName%>" value="<%=checkBoxValue%>" />
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
									<bean:write name="currentUser" property="email" />
								</td>
								<td class="dataCellText">
									<bean:write name="currentUser" property="dateAdded" />
								</td>
							</tr>
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
									String setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_APPROVE+"')";
								%>
								<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
									<bean:message  key="buttons.approve" />
								</html:submit>
							</td>
							<td>
								<%
									setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_REJECT+"')";
								%>
								<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
									<bean:message  key="buttons.reject" />
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