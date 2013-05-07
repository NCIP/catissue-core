<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script language="javascript" type="text/javascript">
if(top.frames["cpAndParticipantView"] != undefined)
{

<% String isParticipantDisable = request.getParameter( "disableParticipant" );
if(isParticipantDisable!=null && !isParticipantDisable.trim().equals("" ) && isParticipantDisable.equals("true"))
{%>
	top.frames["cpAndParticipantView"].disableParticipant();
	
<%}
else {%>
top.frames["cpAndParticipantView"].refreshCpParticipants("");
<%}%>

   
 }
</script>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">
	<tr>
		<td colspan="2">
		<ul>
			<li><font color="#000000" size="3" face="Verdana"><strong>This
			is user Workspace</strong></font></li>
		</ul>
		</td>
	</tr>
</table>
