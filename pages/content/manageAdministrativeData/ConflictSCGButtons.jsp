<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>

 <table cellpadding="0" cellspacing="0" border="0" width ="100%">
	
	<tr>
		<td width = "70%">
			&nbsp;
		</td>
		<td nowrap class="formFieldNoBorders" >
		
			<html:button styleClass="actionButton" 
					property="" 
					title=""
					value="Overwrite Report" 
					onclick="onButtonClick('overwriteReport')">
 	       	</html:button>
	    </td>
		
		<td width = "5%">
			&nbsp;
		</td>
	    
	    <td nowrap class="formFieldNoBorders">
	    	<html:button styleClass="actionButton" 
						property="" 
						title=""
						value="Ignore New Report" 
						onclick="onButtonClick('ignoreNewReport')">
		 
		    </html:button>
		</td>
		

		<td width = "5%">
			&nbsp;
		</td>
	</tr>
</table>

