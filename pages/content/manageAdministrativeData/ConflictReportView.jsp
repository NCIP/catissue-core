<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
</br>

<%
	boolean readOnlyValue=true;
%>
<table summary="" cellpadding="4" cellspacing="5" border="0" width="100%" height="40%">
	<tr>
		<td class="formSubTitle" >
			<bean:message key="caTies.conflict.report.information"/>
		</td>
	</tr>
	<tr>
		 <td class="formFieldWithNoBorderFontSize">
		 	
				<div ><PRE><bean:write name="conflictSCGForm" property="newConflictedReport"/></PRE>
				</div>
			
		 </td>	
	</tr>

</table>


