<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables,edu.wustl.common.beans.SessionDataBean;"%>

<style type="text/css">
table#browserDetailsContainer
{
	font-family:arial,helvetica,verdana,sans-serif;
	font-size:0.7em;
	padding-left:10px;
}
</style>

<table summary="" cellpadding="0" border="0" cellspacing="0"
	width="100%" height="100%">
	<tr>
		<td colspan="5" class="td_orange_line" height="1">
		</td>
	</tr>
	<tr height="100%">
		<td valign="top"><jsp:include page="/pages/subMenu/Home.jsp" /></td>
		<td valign="top"><img src="images/uIEnhancementImages/sep_left.gif" width="8" /></td>
		<td align="center" valign="top"><img
			src="images/uIEnhancementImages/concept_image_s.jpg" alt="caTissue Suite" /></td>
		<td valign="top"><img src="images/uIEnhancementImages/sep_right.gif" width="8" /></td>
		<td valign="top" align="center" height="100%" width="250">
		<table width="100%" height="100%" cellpadding="0" cellspacing="0"
			border="0">
			<tr height="1%">
				<td colspan="3">&nbsp;</td>
			</tr>
			<tr height="*">
				<td>&nbsp;</td>
				<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_boxborder" valign="top">
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							bgcolor="#FFFFFF">
							<tr>
								<td align="left" class="td_table_head">&nbsp;<span
									class="wh_ar_b"><bean:message key="app.loginMessage" /></span>
								</td>
								<td align="right" class="td_table_head"><img
									src="images/uIEnhancementImages/table_title_corner.gif" width="31" height="24" /></td>
								<td width="20">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="3" align="left"><html:errors /> <logic:empty
									scope="session" name="<%=Constants.SESSION_DATA%>">
									<html:form styleId="form1" styleClass="whitetable_bg"
										action="/Login.do">
										<table width="100%" height="25" border="0" cellpadding="4"
											cellspacing="0">
											<tr height="25%">
												<td class="black_ar"><label for="loginName"> <bean:message
													key="app.loginId" /> </label></td>
												<td><html:text styleClass="black_ar"
													property="loginName" size="15" /></td>
											</tr>
											<tr height="25%">
												<td class="black_ar"><label for="password"> <bean:message
													key="app.password" /> </label></td>
												<td><html:password styleClass="black_ar"
													property="password" size="15" /></td>
											</tr>
											<tr height="25%">
												<td>&nbsp;</td>
												<td align="left" valign="middle">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td><input name="Submit" type="submit"
															class="button_main" value="Login" /> <a href="#"
															class="blue"><span class="wh_ar_b"></span></a></td>
														<td><img src="images/uIEnhancementImages/or_dot.gif" width="1"
															height="15" hspace="5" /></td>
														<td><a
															href="SignUp.do?operation=add&amp;pageOf=pageOfSignUp"
															class="blue"><bean:message key="app.signup" /></a></td>
													</tr>
												</table>
												</td>
											</tr>
											<tr height="25%">
												<td height="20" align="left" valign="middle"><a
													href="#" class="orange"></a></td>
												<td height="20" align="left" valign="middle"><a
													href="ForgotPassword.do" class="orange"><bean:message
													key="app.requestPassword" /> </a></td>
											</tr>
										</table>
									</html:form>

								</logic:empty> <logic:notEmpty scope="session"
									name="<%=Constants.SESSION_DATA%>">
									<tr>
										<TD class="welcomeContent">
										<%
													Object obj = request.getSession().getAttribute(
													Constants.SESSION_DATA);
											if (obj != null) {
												SessionDataBean sessionData = (SessionDataBean) obj;
										%> Dear <%=sessionData.getLastName()%>, &nbsp;<%=sessionData.getFirstName()%><br>
										<%
										}
										%> <bean:message key="app.welcomeNote"
											arg0="<%=ApplicationProperties.getValue("app.name")%>"
											arg1="<%=ApplicationProperties.getValue("app.version")%>"
											arg2="<%=Variables.applicationCvsTag%>" /></TD>
									</tr>
								</logic:notEmpty></td>
							</tr>
						</table>
						</td>
						<td height="1%" width="5" background="images/uIEnhancementImages/shadow_right.gif">
					</tr>


					<tr>
						<td background="images/uIEnhancementImages/shadow_down.gif"></td>
						<td height="5"><img src="images/uIEnhancementImages/shadow_corner.gif" width="5"
							height="5" /></td>

					</tr>
				</table>
				</td>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td colspan="3" height="100">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="3" align="left" valign="middle" height="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					height="100%">
					<tr height="12%">
						<td width="15" height="12%" rowspan="5">&nbsp;</td>
						<td class="blue_ar_b"><bean:message
							key="app.certified.browsers" /></td>
					</tr>
					<tr height="39%">

						<td colspan="2" height="39%" class="black_ar"><img
							src="images/uIEnhancementImages/logo_ie.gif" alt="Internet Explorer 6.0" width="16"
							height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
							key="app.certified.ie" /><br />

						<img src="images/uIEnhancementImages/logo_firefox.gif" alt="Mozilla Firefox-2.0.0.3 "
							width="16" height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
							key="app.certified.mozilla" /><br />
						<img src="images/uIEnhancementImages/logo_safari.gif" alt="Mac Safari 2.0.3 "
							width="16" height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
							key="app.certified.mac" /></td>
					</tr>
					<tr height="13%">
						<td colspan="2" height="13%">&nbsp;</td>
					</tr>

					<tr height="12%">
						<td colspan="2" height="12%" class="blue_ar_b"><bean:message
							key="app.optimal.resolutions" /></td>
					</tr>
					<tr height="26%">
						<td colspan="2" height="26%" class="black_ar"><img
							src="images/uIEnhancementImages/logo_windows.gif" alt="Windows " width="16"
							height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
							key="app.optimal.resolutions.win" /> <br />
						<img src="images/uIEnhancementImages/logo_mac.gif" alt="Mac" width="16" height="16"
							hspace="3" vspace="3" align="absmiddle" /><bean:message
							key="app.optimal.resolutions.mac" /> <br />
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

