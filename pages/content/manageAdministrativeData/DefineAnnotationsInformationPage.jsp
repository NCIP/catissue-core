<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
			String operation = request.getParameter("operation");


%>
<form id="thisform" name="thisform"  METHOD="GET" >
	<p>&nbsp;</p>
	<p><table summary="" cellpadding="5" cellspacing="0" border="0" width="620" height="160">
		<tr>
			<td align="justify" colspan="2" height="20" style="font-family:arial,helvetica,verdana,sans-serif; font-size:0.8em; font-weight:bold; padding-left:0.6em; background-color:#5C5C5C; color:#FFFFFF;">Warning!</td>
		</tr>

		<tr>
			<td align="justify" colspan="2" height="50" style="font-family:arial,helvetica,verdana,sans-serif; font-size:0.8em; padding-left:0.6em; background-color:#F4F4F5; color:#000000;">Please note that creating or editing a dynamic extension will create or alter tables in the caTissue database.</td>
		</tr>

		<tr>
			<td align="justify" colspan="2" height="60" style="font-family:arial,helvetica,verdana,sans-serif; font-size:0.8em; padding-left:0.6em; background-color:#F4F4F5; color:#000000;">As a good database administration practice, we suggest that you take a backup of the database before creating any new Dynamic Extensions or editing the existing Dynamic Extensions.</td>
		</tr>

		<tr>
			<td align="right" height="*" width="50%" bgcolor="#F4F4F5">
				<input  type="button" class="actionButton" value="Back"  onclick="document.location.href='ManageAdministrativeData.do?dummy=dummy'" />
			</td>
			<td align="left" height="*" width="50%" bgcolor="#F4F4F5">
				<input type="button" class="actionButton" value="Next" onClick="document.location.href='DefineAnnotations.do?op=<%=operation%>'" />
			</td>
		</tr>
	</table>
	</p>
</form>
