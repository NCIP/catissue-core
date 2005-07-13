<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.C"%>

<%
	Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
	String prevPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.IDENTIFIER+"="+identifier;
	identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
	String nextPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.IDENTIFIER+"="+identifier;
%>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<br />
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
					<html:form action="/ProblemPendingClose">
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
						<%
							ReportedProblem reportedProblem = (ReportedProblem)request.getAttribute(Constants.CURRENT_RECORD);
							String reportedProblemIdentifier = reportedProblem.getIdentifier().toString();
							String propertyName = "value(problem" + reportedProblemIdentifier + ")";
						%>
						<td>
							<html:hidden property="<%=propertyName%>" value="<%=reportedProblemIdentifier%>"/>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="3">

						<!-- action buttons begins -->
						<table cellpadding="6" cellspacing="2" border="0">
							<tr>
								<% 
									String backPage = Constants.REPORTED_PROBLEM_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>
								<td>
									<a class="contentLink" href="<%=backPage%>">Reported Problem Home</a>
								</td>
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
										setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_PENDING+"')";
									%>
									<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
										<bean:message  key="buttons.pending" />
									</html:submit>
								</td>
								<td>
									<logic:notEmpty name="prevpage">
										<a class="contentLink" href="<%=prevPage%>">
											<bean:message key="approveUser.previous"/>
										</a>
									</logic:notEmpty> |
									<logic:notEmpty name="nextPage">
										<a class="contentLink" href="<%=nextPage%>">
											<bean:message key="approveUser.next"/>
										</a>
									</logic:notEmpty> 
								</td>
							</tr>
						</table>
						<!-- action buttons end -->
						
						</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="reportedProblem.details.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="from">
								<bean:message key="reportedProblem.from" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="from" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="subject">
								<bean:message key="reportedProblem.title" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="subject" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="messageBody">
								<bean:message key="reportedProblem.message" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="messageBody" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="approveUser.comments" />
							</label>
						</td>
						<td class="formField">
							<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" />
						</td>
					</tr>
					<tr>
						<td align="right" colspan="3">

						<!-- action buttons begins -->
						<table cellpadding="6" cellspacing="2" border="0">
							<tr>
								<% 
									backPage = Constants.REPORTED_PROBLEM_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>
								<td>
									<a class="contentLink" href="<%=backPage%>">Reported Problem Home</a>
								</td>
								<td>
									<%
										setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_CLOSED+"')";
									%>
									<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
										<bean:message  key="buttons.close" />
									</html:submit>
								</td>
								<td>
									<%
										setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_PENDING+"')";
									%>
									<html:submit styleClass="actionButton" onclick="<%=setOperation%>">
										<bean:message  key="buttons.pending" />
									</html:submit>
								</td>
								<td>
									<logic:notEmpty name="prevpage">
										<a class="contentLink" href="<%=prevPage%>">
											<bean:message key="approveUser.previous"/>
										</a>
									</logic:notEmpty> |
									<logic:notEmpty name="nextPage">
										<a class="contentLink" href="<%=nextPage%>">
											<bean:message key="approveUser.next"/>
										</a>
									</logic:notEmpty> 
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