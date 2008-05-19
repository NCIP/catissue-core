<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page language="java" isELIgnored="false"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/menus.css" />
<link rel="STYLESHEET" type="text/css" href="css/dhtmlxtabbar.css">
<link rel="stylesheet" type="text/css" href="css/examples.css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<script type="text/javascript"></script>
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript" src="jss/states.js"></script>
<script type="text/javascript" src="js/dhtmlxcommon.js"></script>
<script type="text/javascript" src="js/dhtmlxtabbar.js"></script>
</head>

<body>
<html:form>
	<table width="95%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td width="6%" valign="top"><img width="48" height="24"
				src="images/uIEnhancementImages/menustartimg.gif" /></td>
			<td width="94%" align="left"><logic:notEmpty scope="session"
				name="<%=Constants.SESSION_DATA%>">
				<script type="text/javascript" src="jss/menus.js"></script>
				<div id="toolbarLoggedIn"></div>
			</logic:notEmpty> <logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
				<script type="text/javascript" src="jss/menu_home.js"></script>
				<div id="toolbarLoggedOut"></div>
			</logic:empty></td>
		</tr>

	</table>
</html:form>
</body>

</html>

