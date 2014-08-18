<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" isELIgnored="false"%>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
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
<c:if test="${requestScope['org.apache.struts.action.ERROR'] != null }">
	<table border="0" cellspacing="0" cellpadding="3">
		<tr>
			<td valign="top" >
				<img src="images/uIEnhancementImages/alert-icon.gif" alt="error messages"
				width="16" vspace="0" hspace ="0" height="18" valign="top"></td>
			<td class="messagetexterror" align="left">
			<strong>Error:</strong></td>
			</tr>
			<tr><td>&nbsp;</td>
			<td class="messagetexterror" >
			<html:errors /></td>
		</tr>
	</table>
</c:if>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	class="contentPage" width="600">
	<tr>
		<td colspan="2">
		<c:if test="${requestScope['org.apache.struts.action.ERROR'] == null }">
		<ul>
			<li><font color="#000000" size="3" face="Verdana"><strong>This
			is user Workspace</strong></font></li>
		</ul>
		</c:if>
		</td>
	</tr>
</table>
