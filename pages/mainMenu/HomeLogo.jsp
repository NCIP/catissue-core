<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="right"><a href="http://bioinformatics.wustl.edu"><img
				border="0" src="images/uIEnhancementImages/siteman_logo.gif"
				alt="Siteman Cancer Center" width="198" height="54" vspace="5" /></a><img
				border="0" src="images/uIEnhancementImages/or_dot.gif"  alt="Divider line" width="1"
				height="50" hspace="10" vspace="5" /><a
				href="http://cabig.nci.nih.gov/"><img border="0"
				src="images/uIEnhancementImages/cabig_logo.jpg" alt="caBIG"
				width="182" height="62" /></a></td>
		</tr>
	</table>
</logic:empty>
<!-- 
<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
	<table width="95%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td width="6%" valign="top"><img width="48" height="24"
				border="0" src="images/uIEnhancementImages/menustartimg.gif" /></td>
			<td width="94%" align="left">
			<div id="toolbarLoggedIn"></div>
			</td>
		</tr>
	</table>
</logic:notEmpty>

-->

