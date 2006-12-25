<table cellpadding="4" cellspacing="0" border="0">
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% 
			isAddNew = true; 
		%>
	</logic:equal>
	
	<tr>
		<td nowrap class="formFieldNoBorders">
			<html:button styleClass="actionButton" 
					property="submitPage" 
					title="Submit Only"
					value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][0]%>" 
					onclick="<%=normalSubmit%>">				  				     	    
	     	</html:button>
		</td>
		<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY%>">
		<td nowrap class="formFieldNoBorders"> 
			<html:button styleClass="actionButton"  
					property="submitPage" 
					title="Submit and Add Specimen collection group"
					value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[1][0]%>"
					disabled="<%=isAddNew%>" 
					onclick="<%=forwardToSubmit%>">
	     	</html:button>
	     	
		</td>
		</logic:notEqual>
	</tr>
</table>