<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page
	import="edu.wustl.common.util.global.Constants,edu.wustl.common.beans.SessionDataBean"%>

<%
	
	SessionDataBean sessionData = null;
	sessionData=(SessionDataBean)session.getAttribute(Constants.SESSION_DATA) ;
	Long userId=0L;
	if(session.getAttribute(Constants.SESSION_DATA) != null) 
		userId = sessionData.getUserId();

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="css/menus.css" />
<link rel="stylesheet" type="text/css" href="css/examples.css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<script type="text/javascript" src="jss/caTissueSuite.js"></script>
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript">
function editUserProfile(item){
	document.location.href = "UserProfileEdit.do?pageOf=pageOfUserProfile";
}
</script>

</head>
<body>
<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="right"><a href="<bean:message key="institution.logo.href"/>" title="<bean:message key="institution.logo.alt"/>"><img
				border="0" src="<bean:message key="institution.logo.filename"/>"
				alt="<bean:message key="institution.logo.alt"/>" width="198" height="54" vspace="5" /></a><img
				border="0" src="images/uIEnhancementImages/or_dot.gif"  alt="Divider line" width="1"
				height="50" hspace="10" vspace="5" /><a
				href="<bean:message key="cabig.url" />"><img border="0"
				src="images/uIEnhancementImages/cabig_logo.jpg" alt="caBIG"
				width="182" height="62" /></a></td>
		</tr>
	</table>
</logic:empty>
<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
<table width="95%" cellspacing="0" cellpadding="0" border="0" align="top">
	<tr>
		<td width="3%" valign="top"><img width="42" height="24"
			src="images/uIEnhancementImages/menustartimg.gif" /></td>
		<td width="97%" align="left">
		<logic:notEmpty scope="session"
			name="<%=Constants.SESSION_DATA%>">
			<script type="text/javascript" src="jss/menus.js"></script>
			<div id="toolbarLoggedIn"></div>
		</logic:notEmpty>
		<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
			<script type="text/javascript" src="jss/menu_home.js"></script>
			<div id="toolbarLoggedOut"></div>
		</logic:empty></td>
	</tr>
	
</table>
</logic:notEmpty>
</body>


