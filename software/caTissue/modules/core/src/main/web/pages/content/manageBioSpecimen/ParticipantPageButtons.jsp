
				<table cellpadding="4" cellspacing="0" border="0">
								<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
									<% 
										isAddNew=true;
									%>
								</logic:equal>
								<tr>
								
								<!-- PUT YOUR COMMENT HERE -->
								<!--logic:equal name="<%=Constants.PAGE_OF%>" value="<%=Constants.QUERY%>"-->
						   			<td nowrap class="formFieldNoBorders">
										<html:button styleClass="actionButton" 
												property="submitPage" 
												value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>" 
												onclick="<%=normalSubmit%>"> 
										</html:button>
									</td>
								<!--/logic:equal-->	
								
								<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.QUERY%>">
									<td nowrap class="formFieldNoBorders">									
										<html:button styleClass="actionButton"  
												property="submitPage" 
												value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][0]%>" 
												disabled="<%=isAddNew%>"
												onclick="<%=forwardToSubmit%>">
										</html:button>
									</td>
								</logic:notEqual>
								
								<td>
						   			<html:submit styleClass="actionButton" disabled="true">
						   				<bean:message key="buttons.getClinicalData"/>
						   			</html:submit>
						   		</td>	
							</tr>
							<!---Following is the code button ParticipantLookupAgain-->
							
							<%if(request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST)!=null){%>	
								<tr>
									<td>

						   				<html:button styleClass="actionButton" property="submitPage" onclick="participantLookupAction();">
					   					<bean:message key="buttons.participantLookupAgain"/>
					   				</html:button>
									</td>
								</tr>	
							<%}%>
									<!-- end -->
					</table>