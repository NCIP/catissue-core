<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables,edu.wustl.common.beans.SessionDataBean"%>

<style type="text/css">
table#browserDetailsContainer
{
	font-family:arial,helvetica,verdana,sans-serif;
	font-size:0.7em;
	padding-left:10px;
}
</style>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="5" class="td_orange_line" height="1"></td>
	</tr>
	<tr>
		<td width="23%" align="center" valign="top"><br />
		<table width="95%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_boxborder">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr class="wh_ar_b">
						<td width="20" colspan="3" align="left">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td nowrap="nowrap" class="td_table_head"><span
									class="wh_ar_b"><bean:message key="app.quickLinks" /></span></td>
								<td><img
									src="images/uIEnhancementImages/table_title_corner.gif"
									alt="Title" width="31" height="24" /></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="3" align="right" class="showhide1">
						<table width="98%" border="0" cellspacing="0" cellpadding="6">
							<tr>
								<td><a href="https://cabig.nci.nih.gov/" target="_blank"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.cabigHome" /></a>
							</tr>
							<tr>

								<td><a href="http://ncicb.nci.nih.gov/" target="_blank"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.ncicbHome" /></a></td>
							</tr>
							<tr>
								<td><a
									href="PrivacyNotice.do?PAGE_TITLE_KEY=app.privacyNotice&FILE_NAME_KEY=app.privacyNotice.file"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.privacyNotice" /></a></td>
							</tr>
							<tr>
								<td><a
									href="Disclaimer.do?PAGE_TITLE_KEY=app.disclaimer&FILE_NAME_KEY=app.disclaimer.file"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.disclaimer" /></a></td>

							</tr>
							<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
							<tr>
								<td><a
									href="Accessibility.do?PAGE_TITLE_KEY=app.accessibility&FILE_NAME_KEY=app.accessibility.file"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.accessibility" /></a></td>
							</tr>
							<tr>
								<td style="padding-bottom:16px;"><a
									href="RedirectToHelp.do" class="view"><span class="wh_ar_b"></span><bean:message
									key="app.help" /></a></td>
							</tr>
							</logic:empty>
							<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
							<tr>
								<td style="padding-bottom:16px;"><a
									href="Accessibility.do?PAGE_TITLE_KEY=app.accessibility&FILE_NAME_KEY=app.accessibility.file"
									class="view"><span class="wh_ar_b"></span><bean:message
									key="app.accessibility" /></a></td>
							</tr>
							</logic:notEmpty>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td width="5"
					background="images/uIEnhancementImages/shadow_right.gif" style="background-repeat: repeat-y"></td>

			</tr>
			<tr>
				<td background="images/uIEnhancementImages/shadow_down.gif"></td>
				<td><img src="images/uIEnhancementImages/shadow_corner.gif" 
				alt="Shadow Corner"
					width="5" height="5" /></td>
			</tr>
		</table>
		<br />
		</td>
		<td width="0%" rowspan="3" valign="top"><img
			src="images/uIEnhancementImages/sep_left.gif" width="8" height="458" /></td>
		<td width="49%" rowspan="3" align="center" valign="top"><img
			src="images/uIEnhancementImages/concept_image_s.jpg"
			alt="caTissue Suite" width="450" height="470" /></td>

		<td width="0%" rowspan="3" valign="top"><img
			src="images/uIEnhancementImages/sep_right.gif" width="8" height="458" /></td>
		<td width="28%" align="center" valign="top"><br />

		<table width="95%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_boxborder">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr class="wh_ar_b">
						<td width="20" colspan="3" align="left">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>

								<td nowrap="nowrap" class="td_table_head"><span
									class="wh_ar_b"><bean:message key="app.loginMessage" /></span></td>
								<td><img
									src="images/uIEnhancementImages/table_title_corner.gif"
									alt="Title" width="31" height="24" /></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="3" align="left" class="showhide1">
						<%@ include file="/pages/content/common/ActionErrors.jsp" %>
						<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
							<html:form styleId="form1" styleClass="whitetable_bg"
								action="/Login.do">
								<table width="98%" border="0" cellpadding="4" cellspacing="0">
									<tr>
										<td class="black_ar"><bean:message key="app.loginId" />
										</td>
										<td><html:text styleClass="black_ar" property="loginName"
											size="20" /></td>
									</tr>
									<tr>
										<td class="black_ar"><bean:message key="app.password" />
										</td>
										<td><html:password styleClass="black_ar"
											property="password" size="20" /></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td align="left" valign="middle">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td><input name="Submit" type="submit"
													class="blue_ar_b" value="Login" /> <a href="#"
													class="blue"><span class="wh_ar_b"></span></a></td>
												<td><img src="images/uIEnhancementImages/or_dot.gif"
												 alt="Divider line"
													width="1" height="15" hspace="5" /></td>
												<td><a
													href="SignUp.do?operation=add&amp;pageOf=pageOfSignUp"
													class="view"><bean:message key="app.signup" /></a></td>
											</tr>
										</table>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td><td>&nbsp;</td>
										<!-- <td align="left" valign="middle"><a
											href="ForgotPassword.do" class="view"><bean:message
											key="app.requestPassword" /> </a></td>
										 -->
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
				<td width="5"
					background="images/uIEnhancementImages/shadow_right.gif"></td>
			</tr>
			<tr>
				<td background="images/uIEnhancementImages/shadow_down.gif"></td>
				<td><img src="images/uIEnhancementImages/shadow_corner.gif"
				alt="Shadow Corner"
					width="5" height="5" /></td>
			</tr>
		</table>

		</td>
	</tr>
	<tr>
		<td align="center" valign="bottom">&nbsp;</td>
		<td rowspan="2" align="right" valign="bottom">
		<table width="96%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="blue_ar_b"><bean:message
					key="app.certified.browsers" /></td>
			</tr>

			<tr>
				<td class="black_ar"><img
					src="images/uIEnhancementImages/logo_ie.gif"
					alt="Internet Explorer 6.0" width="16" height="16" hspace="3"
					vspace="3" align="absmiddle" /><bean:message
					key="app.certified.ie" /><br />
				<img src="images/uIEnhancementImages/logo_firefox.gif"
					alt="Mozilla Firefox-2.0.0.3 " width="16" height="16" hspace="3"
					vspace="3" align="absmiddle" /><bean:message
					key="app.certified.mozilla" /> <br />
				<img src="images/uIEnhancementImages/logo_safari.gif"
					alt="Mac Safari 2.0.3 " width="16" height="16" hspace="3"
					vspace="3" align="absmiddle" /><bean:message
					key="app.certified.mac" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>

			</tr>
			<tr>
				<td class="blue_ar_b"><bean:message
					key="app.optimal.resolutions" /></td>
			</tr>
			<tr>
				<td class="black_ar"><img
					src="images/uIEnhancementImages/logo_windows.gif" alt="Windows "
					width="16" height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
					key="app.optimal.resolutions.win" /><br />
				<img src="images/uIEnhancementImages/logo_mac.gif" alt="Mac"
					width="16" height="16" hspace="3" vspace="3" align="absmiddle" /><bean:message
					key="app.optimal.resolutions.mac" /><br>

				<br />
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center" valign="bottom">
		<table width="95%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center"
					background="images/uIEnhancementImages/box_bg.gif"
					style="background-repeat:no-repeat; background-position:center;">
				<table width="100%" border="0" cellspacing="0" cellpadding="4">
					<tr>

						<td width="50%" height="72" align="center">
						<p><a href="http://www.cancer.gov/"><img
							src="images/uIEnhancementImages/logo1.gif"
							alt="National Cancer Institute Logo" width="57" height="50"
							border="0" /></a><br />
						</p>
						</td>
						<td width="50%" align="center"><a href="http://www.nih.gov/"><img
							src="images/uIEnhancementImages/logo2.gif" 
							alt="National Institues of Health" width="56" height="50"
							border="0" /></a></td>
					</tr>
					<tr>
						<td align="center"><a href="http://www.dhhs.gov/"><img
							src="images/uIEnhancementImages/logo3.gif"
							alt="Department of Health &amp; Human Services" width="54" height="50"
							border="0" /></a></td>
						<td align="center"><a href="http://www.firstgov.gov/"><img
							src="images/uIEnhancementImages/logo4.gif"
							alt="First Government Logo" width="92" height="50" border="0" /></a></td>
					</tr>
				</table>
				</td>

			</tr>
		</table>
		<br>
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center" valign="top" bgcolor="#bcbcbc"><img
			src="images/uIEnhancementImages/spacer.gif" alt="Spacer" width="1" height="1" /></td>
	</tr>
</table>
