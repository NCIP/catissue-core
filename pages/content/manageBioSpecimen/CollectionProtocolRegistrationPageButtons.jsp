<table cellpadding="4" cellspacing="0" border="0">
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% 
			isAddNew = true; 
		%>
	</logic:equal>
	
	<tr>
		<td nowrap class="formFieldNoBorders">
		<%
			if(operation.equals(Constants.ADD))
			{
		%>
				<html:button styleClass="actionButton" 
						property="submitPage" 
						title="Submit Only"
						value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][0]%>" 
						onclick="<%=normalSubmit%>">				  				     	    
		     	</html:button>
	    <%	
			}
			else
			{
				ConsentTierData consentForm =(ConsentTierData)form;
				List consentTier=(List)consentForm.getConsentTiers();
				String str=normalSubmit;
				if(consentTier.size()>0)
				{
					str = "popupWindow('"+ consentTier.size() +"')";
				}
		%>	
				<html:button styleClass="actionButton" 
						property="submitPage" 
						title="Submit Only"
						value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][0]%>" 
						onclick="<%=str%>">			  				     	    
		     	</html:button>
		<%
			}
		%>     	
     	</td>

		<td nowrap class="formFieldNoBorders"> 
			<html:button styleClass="actionButton"  
					property="submitPage" 
					title="Submit and Add Specimen collection group"
					value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[1][0]%>"
					disabled="<%=isAddNew%>" 
					onclick="<%=forwardToSubmit%>">
	     	</html:button>
	     	
		</td>

	</tr>
</table>