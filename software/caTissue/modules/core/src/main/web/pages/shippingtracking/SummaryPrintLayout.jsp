<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ page import="java.io.File"%>
<%@ page import="edu.wustl.catissuecore.util.shippingtracking.Constants"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="css/addl_s_t.css" rel="stylesheet" type="text/css">
<html>
<head>
<title>Packing Slip</title>
</head>
<body>
<%
String headerFile = XMLPropertyHandler.getValue(Constants.PACKING_SLIP_HEADER_FILE);
String footerFile = XMLPropertyHandler.getValue(Constants.PACKING_SLIP_FOOTER_FILE);
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="top">
		<tiles:insert page="<%=headerFile%>">
		</tiles:insert>
    </td>
	</tr> 
    
	<tr>
		<td class="whitetable_bg" width="5">&nbsp;</td>
	</tr>
	<tr>
		<td class="blue_ar_b_17"><tiles:getAsString name="title"
			ignore="true" /></td>
	</tr>
	<tr>
		<td class="whitetable_bg" width="5">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" align="right" valign="top"><tiles:insert
			attribute="summary" >
		</tiles:insert></td>
	</tr>
	<tr>
		<td class="whitetable_bg" width="5">&nbsp;</td>
	</tr>
	<tr>

		<td width="100%" valign="top"><tiles:insert attribute="contents"></tiles:insert>
		</td>
	</tr>
	<tr>
		<td class="whitetable_bg" width="5">&nbsp;</td>
	</tr>
	<tr>
		<td valign="top">
		<tiles:insert page="<%=footerFile%>">
		</tiles:insert>
    </td>
	</tr> 
    

</table>
</body>
</html>