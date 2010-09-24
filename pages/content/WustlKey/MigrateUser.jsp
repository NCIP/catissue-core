<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<script language="JavaScript">
		function doNotMigrate()
		{
			var loginform = document.getElementById("doNotAsk").checked;
			var action = "NeverMigrate.do?doNotAskAgain="+loginform;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
		function proceedWithoutMigration()
		{
			var loginform = document.getElementById("doNotAsk").checked;
			var action = "ProceedWithoutMigration.do?doNotAskAgain="+loginform;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<html:form action='/MigrateUser.do' styleId="migrationForm">
		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="Wustlkey.header" /></span></td>
					<td><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title - Wustlkey" width="31" height="24" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			</table>

			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">
				<tr>
					<td align="left" class="bottomtd"><%@ include
						file="/pages/content/common/ActionErrors.jsp"%>
					</td>
				</tr>
				<tr>
					<td width="15%" align="left" class="black_ar"><i><bean:message
						key="wustlkey.message" /> <logic:iterate id="idpDetails" name="migrationIdps">
										<bean:write name="idpDetails" property="name"/>.
									</logic:iterate> </i></td>
				</tr>
				<tr>
					<td width="15%" align="left" class="black_ar"><html:select
						property="targetIdp" styleId="targetIdp" style="display:none;">
						<html:options collection="migrationIdps" labelProperty="name"
							property="value" />
					</html:select></td>
				</tr>
				<tr>
					<td align="left" class="showhide">
					<table width="65%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="15%" align="left" class="black_ar"><bean:message
								key="user.loginName" /></td>
							<td width="84%" align="left"><label> <html:text
								styleClass="black_ar" maxlength="255" size="30"
								styleId="migratedLoginName" property="migratedLoginName" /> </label></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align="left" class="showhide">
					<table width="65%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="15%" align="left" class="black_ar"><bean:message
								key="app.password" /></td>
							<td width="84%" align="left"><label> <html:password
								styleClass="black_ar" maxlength="255" size="30"
								styleId="migratedPassword" property="migratedPassword" /> </label></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="buttonbg" align="left" class="showhide"><input
						id="doNotAsk" type="checkbox" /> <label for="donotaskagain"
						class="black_ar"><bean:message
						key="app.wustlkey.donotaskagain" /> </label></td>
				</tr>
				<tr>
					<td align="left" class="showhide">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="buttonbg"><html:submit styleClass="blue_ar_b">
								<bean:message key="buttons.migrate" />
							</html:submit> &nbsp;&nbsp; <input name="button" type="button"
								class="blue_ar_b" onClick="proceedWithoutMigration()"
								value="Proceed Without Migration" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</html:form>
</table>