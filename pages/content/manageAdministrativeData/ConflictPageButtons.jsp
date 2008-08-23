<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/viewSPR.js"></script>


 <table cellpadding="0" cellspacing="0" border="0" width ="100%">
	
	<tr>
		<%if(conflictStatus.equals(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT))
			{
		%>
			
		<%} else {%>
			
		<%}%>

		<td nowrap class="buttonbg" align="left">
		
		<%if(conflictStatus.equals(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT))
			{
		%>
			<html:button styleClass="blue_ar_c" 
					styleId="useSelPart"
					property="" 
					title=""
					value="Use Selected Participant" 
					disabled="<%=useSelPartDisable%>" 
					onclick="submitValue('createNewSCG')">
 	    
	     	</html:button> &nbsp;|&nbsp;
			<%}%>	   
	    	<html:button styleClass="blue_ar_c" 
						styleId="useSelSCG"
						property="" 
						title=""
						value="Use Selected SCG" 
						disabled="<%=useSelSCGDisable%>" 
						onclick="submitValue('associateSCG')">								  				     	    
		    </html:button>
		
		<%if(conflictStatus.equals(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT))
			{
		%>
			&nbsp;|&nbsp;
			<html:button styleClass="blue_ar_c" 
					styleId="crtNewPart"
					property="" 
					title=""
					value="Create New Participant"
					disabled="<%=crtNewPartDisable%>" 
					onclick="submitValue('creatNewParticipant')">
	     	</html:button>
		<%} else
		 	 {	
		%>
			<html:button styleClass="blue_ar_c"  
					styleId="crtNewSCG"
					property="" 
					title=""
					value="Create New SCG"
					disabled="<%=crtNewSCGDisable%>" 
					onclick="submitValue('createNewSCG')">
	     	</html:button>

		<%}%>
		 &nbsp;|&nbsp;
		 <html:link	page="/ManageAdministrativeData.do" styleClass="cancellink">
				<bean:message key="buttons.cancel" />
		</html:link>

	    </td>
		
	</tr>
</table>

<script>
function submitValue(buttonPressed)
{
	onButtonClick(buttonPressed, "<%=reportQueueId%>");
}
</script>