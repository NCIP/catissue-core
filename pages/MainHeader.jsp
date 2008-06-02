<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="6%" valign="top">
			<img src="images/uIEnhancementImages/top_bg1.jpg" width="53" height="20" />
		</td>
		<td width="94%" valign="top"
			background="images/uIEnhancementImages/top_bg.jpg"
			style="background-repeat:repeat-x;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>			
				<td width="86%" align="right" valign="top"> 
					<a href="ReportProblem.do?operation=add" class="white">
					  <img src="images/uIEnhancementImages/ic_report.gif" alt="Reported Problems" width="15" hspace="5" vspace="2" height="12" border="0"
						align="absmiddle"> 
					  <bean:message key="app.reportedProblems" />
					</a>
					<a href="ContactUs.do?PAGE_TITLE_KEY=app.contactUs&FILE_NAME_KEY=app.contactUs.file" class="white">
					  <img src="images/uIEnhancementImages/ic_mail.gif" alt="Summary" width="16" height="12" hspace="5" vspace="2" border="0" align="absmiddle" />
					  <bean:message key="app.contactUs" />
					</a>&nbsp;
					<a href="/catissuecore/Summary.do" class="white">
					<img src="images/uIEnhancementImages/ic_summary.gif" alt="Summary"
					width="12" height="12" hspace="5" vspace="2" border="0"
					align="absmiddle" /><bean:message key="app.summary" /></a>
					<a target="_NEW" href="RedirectToHelp.do" class="white">
					<img src="images/uIEnhancementImages/ic_help.gif" alt="Help" width="12"
					height="12" hspace="5" vspace="2" align="absmiddle" border="0"/><bean:message
					key="app.help" /></a>&nbsp;</td>
				<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
				<td width="14%" align="right" valign="top">
					<a href="/catissuecore/Logout.do">
					<img src="images/uIEnhancementImages/logout_button1.gif" name="Image1"
						width="86" height="19" id="Image1" onmouseover="MM_swapImage('Image1','','images/uIEnhancementImages/logout_button.gif',1)"
						onmouseout="MM_swapImgRestore()" />
					</a>
				</td>
				</logic:notEmpty>
				<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
					<td width="14%" valign="top" align="right"><a
						href="/catissuecore/Home.do" class="white">
						<bean:message key="app.loginMessage" />
						</a>
						<img src="images/uIEnhancementImages/spacer.gif" width="10" height="10"
						align="absmiddle" />
					</td>
				</logic:empty>
			</tr>
		</table>
		</td>
	</tr>
</table>
