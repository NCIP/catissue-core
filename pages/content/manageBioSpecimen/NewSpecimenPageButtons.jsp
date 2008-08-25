
<!-- action buttons begins -->
<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% isAddNew=true;%>
	</logic:equal>


	<tr>

		<td class="buttonbg">
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
		
			|&nbsp;<html:button
				styleClass="blue_ar_c" property="moreButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
				disabled="<%=isAddNew%>" onclick="<%=addMoreSubmit%>"
			>
			</html:button>
		


		</logic:notEqual>
		
		
		
		|&nbsp;<html:button
				styleClass="blue_ar_c" property="addToCart"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
				onclick="onAddToCart()">
				<bean:message key="buttons.addToCart"/>
		</html:button>&nbsp;| 
		<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
		<html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
		<bean:message key="buttons.cancel" />
		</html:link>
		</logic:notEqual>
		<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
		<html:link page="/QueryManageBioSpecimen.do" styleClass="cancellink">
		<bean:message key="buttons.cancel" />
		</html:link>
		</logic:equal>
		</td>
	</tr>
</table>
<!-- action buttons end -->
