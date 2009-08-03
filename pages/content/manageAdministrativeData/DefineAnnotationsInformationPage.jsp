<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
			String operation = request.getParameter("operation");
			

%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">

<tr>
<td class="tablepadding">
	<form id="thisform" name="thisform"  METHOD="GET" >	
	<p>
	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" class="whitetable_bg">		 
		<tr><td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				  <tr>
					<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
				  </tr>
			</table>
		</td></tr>
		<tr>
			<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;Message</span></td>
		</tr>

		<tr>
			<td align="justify" colspan="2" height="50" >&nbsp;<bean:message key="de.message.disable" /></td>
		</tr>
		<tr>
			<td align="center" height="*" width="50%" class="buttonbg">
				<input  type="button" class="actionButton" value="Back"  onclick="document.location.href='ManageAdministrativeData.do?dummy=dummy'" />
			</td>
		</tr>
	</table>
	</p>
</form> 
</td>
</tr>
</table>