<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables;"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="6%" valign="top"><img src="images/uIEnhancementImages/top_bg1.jpg"
			width="53" height="20" /></td>
		<td width="94%" valign="top" background="images/uIEnhancementImages/top_bg.jpg"
			style="background-repeat:repeat-x;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="67%" align="left" valign="middle"><span
					class="wh_ar_b"><bean:message key="app.welcomeNote"
					arg0="<%=ApplicationProperties.getValue("app.name")%>"
					arg1="<%=ApplicationProperties.getValue("app.version")%>"
					arg2="<%=Variables.applicationCvsTag%>" /> </span></td>
				<td width="33%" align="right" valign="top"><img
					src="images/uIEnhancementImages/ic_summary.gif" alt="Summary" width="12" height="12"
					hspace="5" vspace="2" border="0" align="absmiddle" /><a
					href="/catissuecore/Summary.do" class="white"><bean:message
					key="app.summary" /></a>&nbsp;<img src="images/uIEnhancementImages/ic_help.gif" alt="Help"
					width="12" height="12" hspace="5" vspace="2" align="absmiddle" /><a
					href="RedirectToHelp.do" class="white"><bean:message
					key="app.help" /></a>&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
