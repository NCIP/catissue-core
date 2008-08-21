<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="javascript">
 refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','');	
</script>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<tr>
		<td colspan="2">
		   <ul>
				<li><font color="#000000" size="3" face="Verdana"><strong>This is user Workspace</strong></font></li>
		   </ul>
		</td>
	</tr>
</table>