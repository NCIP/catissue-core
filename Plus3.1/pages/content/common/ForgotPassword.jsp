<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<link rel="stylesheet" type="text/css" href="css/login.css" />
<link rel="stylesheet" type="text/css" href="css/login-theam.css" />
<script language="JavaScript">
	function cancelButtonPressed()
	{
		document.forms[0].action="RedirectHome.do";
		document.forms[0].submit();
	}
</script>
</head>
<body >

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="5" class="td_orange_line" height="1"></td>
		</tr>
		<tr>
			<td>
				<html:form action="/ForgotPasswordSearch">	
					<div style=" text-align:center;width:100%;">
						<div class="black_ar box-border box-content box-background form-main-div" style="width: 282px;">
							
							<div class="black_ar help-header theam-font-color form-header-spacing" >
								<span><bean:message key="forgotpassword.title"/></span>
							</div>
							<div style="width:100%; height: auto;">
								<div class="form-inner-div margin-form-field">
									<span class="form-label"><bean:message key="user.emailAddress"/></span> </br>
									<html:text styleClass="black_ar  form-text-field" size="30" styleId="emailAddress" property="emailAddress"/>
									
								</div>
							</div>
							<html:submit styleClass="blue_ar_b submit-signup" accesskey="Enter">
								<bean:message  key="buttons.submit" />
							</html:submit>
							
						</div>
					</div>
				</html:form>
			</td>
		<tr>
	</table>

</body>
</html>
