
<!-- action buttons begins -->
<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% isAddNew=true;%>
	</logic:equal>


	<tr>

		<td nowrap class="buttonbg">
		  <%
			if(operation.equals(Constants.ADD))
			{
	   	 %>
			<html:button
				styleClass="blue_ar_b" property="submitButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
				onclick="onDeriveSubmit()">
			</html:button>
		 <%	
			}
		    else
			{
				
		 %>	
				<html:button
					styleClass="blue_ar_b" property="submitButton"
					title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
					value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
					onclick="onDeriveSubmit()">
				</html:button>
		 <%
			}
		 %>     			
		
		
		<logic:notEqual name="<%=Constants.PAGEOF%>"
			value="<%=Constants.QUERY%>">
			
				
			
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
		
			<html:button
				styleClass="blue_ar_b" property="moreButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
				disabled="<%=isAddNew%>" onclick="<%=addMoreSubmit%>"
			>
			</html:button>
		


		</logic:notEqual>
		
		
		
		<html:button
				styleClass="blue_ar_b" property="addToCart"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
				onclick="onAddToCart()">
				<bean:message key="buttons.addToCart"/>
		</html:button>&nbsp;| <a href="#" class="cancellink">Cancel</a>
		</td>
	</tr>
</table>
<!-- action buttons end -->
