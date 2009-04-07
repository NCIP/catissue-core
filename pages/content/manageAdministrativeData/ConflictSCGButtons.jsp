<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/viewSPR.js"></script>

 <table cellpadding="0" cellspacing="0" border="0" width ="100%">
	
	<tr>
		
		<td nowrap class="buttonbg" >
		
			<html:button styleClass="blue_ar_c" 
					property="" 
					title=""
					value="Overwrite Report" 
					onclick="submitValue('overwriteReport')">
 	       	</html:button> &nbsp;|&nbsp;
			
	    	<html:button styleClass="blue_ar_c" 
						property="" 
						title=""
						value="Ignore New Report" 
						onclick="submitValue('ignoreNewReport')">
		 
		    </html:button> 
    	</td>
	</tr>
</table>

<script>
function submitValue(buttonPressed)
{
	onButtonClick(buttonPressed, "<%=reportQueueId%>");
}
</script>