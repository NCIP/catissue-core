<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />


<%
	boolean readOnlyValue=true;
%>
<table summary="" cellpadding="4" cellspacing="5" border="0" width="100%" height="40%">
	<tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="caTies.conflict.report.information"/></span></td>
    </tr>

	<tr>
		 <td class="formFieldWithNoBorderFontSize">
		 	
				<div ><PRE><bean:write name="conflictSCGForm" property="newConflictedReport"/></PRE>
				</div>
			
		 </td>	
	</tr>

</table>


