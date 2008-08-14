<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables;"%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top"><img
			src="images/uIEnhancementImages/top_bg1.jpg" alt="Top corner"
			width="53" height="20" /></td>
		<td width="95%" valign="top"
			background="images/uIEnhancementImages/top_bg.jpg"
			style="background-repeat:repeat-x;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="50%" valign="middle"><span class="wh_ar_b"><bean:message
					key="app.welcomeNote"
					arg0="<%=ApplicationProperties.getValue("app.name")%>"
					arg1="<%=ApplicationProperties.getValue("app.version")%>"
					arg2="<%=Variables.applicationCvsTag%>" /> </span></td>
				<td width="50%" align="right" valign="top"><a
					href="ReportProblem.do?operation=add" class="white"> <img
					src="images/uIEnhancementImages/ic_report.gif" alt="Report Problems" width="15"
					height="12" border="0" hspace="3" vspace="0"> <bean:message
					key="app.reportedProblems" /></a>&nbsp; <a
					href="ContactUs.do?PAGE_TITLE_KEY=app.contactUs&FILE_NAME_KEY=app.contactUs.file"
					class="white"> <img
					src="images/uIEnhancementImages/ic_mail.gif" alt="Summary"
					width="16" height="12" hspace="3" vspace="0" border="0" /><bean:message
					key="app.contactUs" /></a>&nbsp;<a href="/catissuecore/Summary.do"
					class="white"><img
					src="images/uIEnhancementImages/ic_summary.gif" alt="Summary"
					width="10" height="10" hspace="3" vspace="0" border="0" /><bean:message
					key="app.summary" /></a>&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>



