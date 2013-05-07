<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.shippingtracking.Constants"%>

<html:html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Shipping Tracking Login page</title>
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" media="screen"/> 
	<link href="css/shippingtracking/addl_s_t.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	<tr>
	  	<td height="25" align="left" valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;Shipping-Tracking Login</span></td>
	</tr>
	
	<tr>
	<td>	
		<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
		<html:form styleId="form1" styleClass="whitetable_bg" action="/ShippingTrackingLogin.do">
			<table width="98%" border="0" cellpadding="4" cellspacing="0">
				<tr>
					<td width="20%" align="right" valign="top" class="black_ar"><bean:message key="app.loginId" bundle="msg.shippingtracking" />
					</td>
					<td width="3%" align="left" valign="top">&nbsp;:&nbsp;</td>
					<td width="77%" align="left" valign="top"><html:text styleClass="black_ar" property="loginName"
						size="25" /></td>
				</tr>
				<tr>
					<td width="20%" align="right" valign="top" class="black_ar"><bean:message key="app.password" bundle="msg.shippingtracking" />
					</td>
					<td width="3%" align="left" valign="top">&nbsp;:&nbsp;</td>
                    <td width="77%" align="left" valign="top"><html:password styleClass="black_ar"
						property="password" size="25" /></td>
				</tr>
				<tr>
					<td width="20%" align="left" valign="top" colspan="2">&nbsp;</td>
					<td align="left" valign="middle">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><input name="Submit" type="submit"
								class="blue_ar_b" value="Login" /> <a href="#"
								class="blue"><span class="wh_ar_b"></span></a></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</html:form>
	</logic:empty>
	</td>
	</tr>
</table>
</body>
</html:html>