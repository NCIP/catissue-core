<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js">
	</script>
</head>
<body onload="checkItDefault()" onunload='closeWaitPage()'>
<html:form method="GET" action="OpenDecisionMakingPage.do">
<html:hidden property="noOfResults" value="" />

<input type="hidden" name="isQuery" value="true">
<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table2" >		
	<tr>	
		<td width="33%" align="center" class="bgWizardImage">
			<img src="images/1_inactive.gif" /> 
		</td>
		<td width="33%" align="center" class="bgWizardImage">
			<img src="images/2_inactive.gif" /> 
		</td>
		<td width="33%" align="center" class="bgWizardImage">
			<img src="images/3_active.gif" /> 
		</td>
	</tr>
	<tr height="5%" >
		<td width="23%" height="5%" colspan="3">&nbsp;
	</td>
	</tr>
	<tr id="radio_view" colspan="3">
		<td colspan="3" align="center">
			<table  border="0" width="80%" align="center" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" class="tbBordersAllbordersBlack">	
				<tr>
					<td colspan="5" align="left" class="formMessagewithoutcolor">
						<html:errors/>
					</td>
				</tr>
				<tr>
				<td class="formMessagewithoutcolor">&nbsp;
						</td>
					<td colspan="5" class="formMessageBold">
						<bean:message key="query.options.message"/>
					</td>
				</tr>
				<tr>
					<td class="formMessagewithoutcolor">&nbsp;
						</td>
				</tr>
				<tr>
				<td class="formMessagewithoutcolor">&nbsp;
						</td>
					<td class="formMessagewithoutcolor">
						<%--html:radio property="options" value="redefineQuery" /><bean:message key="query.options.redefine.query"/--%>
						<input type="radio" name="options"  value="redefineQuery" \>
						<bean:message key="query.options.redefine.query"/>
					</td>
				</tr>
				<tr>
				<td class="formMessagewithoutcolor">&nbsp;
						</td>
					<td class="formMessagewithoutcolor">
						<%--html:radio property="options" value="viewLimitedRecords" /><bean:message key="query.options.view.few.results"/><bean:message key="query.options.note"/--%>
						<input type="radio" name="options"  value="viewLimitedRecords" checked="checked"> <bean:message key="query.options.view.few.results"/><bean:message key="query.options.note"/>
					</td>
				</tr>
				<tr>
				<td class="formMessagewithoutcolor">&nbsp;
						</td>
					<td class="formMessagewithoutcolor">
						<%--html:radio property="options" value="viewAllRecords" /><bean:message key="query.options.all.records"/--%>
						<input type="radio" name="options" value="viewAllRecords"> <bean:message key="query.options.all.records"/>
					</td>
				</tr>
			<tr>
					<td class="formMessagewithoutcolor">&nbsp;
						</td>
				</tr>
			</table>
		</td>
	</tr>		
	<tr>
	<td colspan="3">&nbsp;
	</td>
	</tr>
		<tr height="10%" valign="center" align="middle" >
		<td valign="center" width="6%" colspan="6">
			<html:button property="proceed" onclick="proceedClicked()"><bean:message key="query.proceed.button"/>	</html:button>
		</td>
	</tr>
</table>
</body>
</html:form>
</html>