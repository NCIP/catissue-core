<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<link rel="stylesheet" type="text/css" href="css/menus.css" />
<link rel="stylesheet" type="text/css" href="css/examples.css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript" src="jss/menus.js"></script>


<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
		 <tr>
			<td align="right" valign="middle" width="100%">
				<img src="images/uIEnhancementImages/siteman_logo.gif" alt="Siteman Cancer Center" width="198" height="54" vspace="5" />
				<img src="images/uIEnhancementImages/or_dot.gif" width="1" height="50" hspace="10" vspace="5" />
				<img src="images/uIEnhancementImages/cabig_logo.jpg" alt="caBIG" width="182" height="62" />
			</td>
		</tr>
	
	
	</table>
</logic:empty>
<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table width="95%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td width="6%" valign="top"><img width="48" height="24"
				src="images/uIEnhancementImages/menustartimg.gif" /></td>
			<td width="94%" align="left">
				<div id="toolbarLoggedIn"></div></td>
		</tr>
	</table>
	</logic:notEmpty>
