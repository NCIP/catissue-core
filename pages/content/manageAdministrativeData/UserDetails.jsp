<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants, edu.wustl.catissuecore.domain.ApplicationUser"%>

<%
	Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
	String prevPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+identifier+"&"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE;
	identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
	String nextPage = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.IDENTIFIER+"="+identifier+"&"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE;
%>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<br />
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
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
						<%
							ApplicationUser user = (ApplicationUser)request.getAttribute(Constants.CURRENT_RECORD);
							String userIdentifier = user.getSystemIdentifier().toString();
							String propertyName = "value(user" + userIdentifier + ")";
						%>
						<td>
							<html:hidden property="<%=propertyName%>" value="<%=userIdentifier%>"/>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="3">

						<!-- action buttons begins -->
						<table cellpadding="6" cellspacing="2" border="0">
							<tr>
								<% 
									String backPage = Constants.APPROVE_USER_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>
								<td>
									<a class="contentLink" href="<%=backPage%>">Approve User Home</a>
								</td>
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
							<bean:message key="user.details.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="loginName">
								<bean:message key="user.loginName" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="loginName" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="lastName">
								<bean:message key="user.lastName" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="lastName" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="firstName">
								<bean:message key="user.firstName" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="firstName" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="state">
								<bean:message key="user.institute" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="institute.name" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="email">
								<bean:message key="user.email" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="email" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="state">
								<bean:message key="user.department" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="department.name" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="address">
								<bean:message key="user.address" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.street" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="city">
								<bean:message key="user.city" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.city" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="state">
								<bean:message key="user.state" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.state" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="country">
								<bean:message key="user.country"/>
							</label>
						</td>

						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.country" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="zip">
								<bean:message key="user.zip" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.zip" />		
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="phone">
								<bean:message key="user.phone" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.phone" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="fax">
								<bean:message key="user.fax"/>
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="address.fax" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="role">
								<bean:message key="user.role" />
							</label>
						</td>
						<td class="formField">
							<bean:write name="<%=Constants.CURRENT_RECORD%>" property="role.name" />
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
									backPage = Constants.APPROVE_USER_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>
								<td>
									<a class="contentLink" href="<%=backPage%>">Approve User Home</a>
								</td>
								<td>
									<%
										setOperation = "setOperation('"+Constants.ACTIVITY_STATUS_APPROVE+"')";
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