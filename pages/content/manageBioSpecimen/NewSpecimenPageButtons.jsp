<!-- action buttons begins -->
<table cellpadding="4" cellspacing="0" border="0">
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% isAddNew=true;%>
	</logic:equal>
	<tr>
		<td nowrap class="formFieldNoBorders">
		  <%
			if(operation.equals(Constants.ADD))
			{
	   	 %>
				<html:button
					styleClass="actionButton" property="submitButton"
					title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
					value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
					onclick="onNormalSubmit()">
   			    </html:button>
		 <%	
			}
		    else
			{
				ConsentTierData consentForm =(ConsentTierData)form;
				List consentTier=(List)consentForm.getConsentTiers();
				String str = "onNormalSubmit()";
				if(consentTier.size()>0)
				{
					str = "popupWindow('"+ consentTier.size() +"')";
				}
		 %>	
				<html:button
					styleClass="actionButton" property="submitButton"
					title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
					value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
					onclick="<%=str%>">
				</html:button>
		 <%
			}
		 %>     			
		</td>
		<logic:notEqual name="<%=Constants.PAGEOF%>"
			value="<%=Constants.QUERY%>">
			<td nowrap class="formFieldNoBorders">
				<html:button
					styleClass="actionButton" property="deriveButton"
					title="<%=Constants.SPECIMEN_BUTTON_TIPS[1]%>"
					value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[1][0]%>"
					disabled="<%=isAddNew%>" onclick="<%=deriveNewSubmit%>"
					>
				</html:button>
			</td>
		</logic:notEqual>
		<!-- Commented as per bug 2115. Mandar. 10-July-06
											</tr>
											<tr>							
											<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
												<td class="formFieldNoBorders" nowrap>
													<html:button styleClass="actionButton"  
															property="submitPage"
															title="<%=Constants.SPECIMEN_BUTTON_TIPS[2]%>"
															value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[2][0]%>" 
															disabled="<%=isAddNew%>" 
															onclick="<%=addEventsSubmit%>">
						  				     	    
											     	</html:button>
												</td>
-->
		<td class="formFieldNoBorders" nowrap>
			<html:button
				styleClass="actionButton" property="moreButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
				disabled="<%=isAddNew%>" onclick="<%=addMoreSubmit%>"
			>
			</html:button>
		</td>

		<td class="formFieldNoBorders" nowrap>
			<html:button
				styleClass="actionButton" property="submitAndDistributeButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[4]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[4][0]%>"
				onclick="<%=submitAndDistribute%>">
			</html:button>
		</td>
		</logic:notEqual>
	</tr>
</table>
<!-- action buttons end -->
