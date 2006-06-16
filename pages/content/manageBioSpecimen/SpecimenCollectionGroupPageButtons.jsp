<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
									<td>
										<table>
											<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
												<% 
													boolean isAddNew = false;	
													isAddNew=true;
												%>
											</logic:equal>
											<tr>
												<td nowrap class="formFieldNoBorders">
													<html:button styleClass="actionButton" 
															property="submitPage" 
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>" 
															onclick="<%=normalSubmit%>">
						  				     	    
											     	</html:button>
												</td>
												
												<td nowrap class="formFieldNoBorders">
													<html:button styleClass="actionButton"  
															property="submitPage" 
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][0]%>" 
															disabled="<%=isAddNew%>" 
															onclick="<%=forwardToSubmit%>">
						  				     	    
											     	</html:button>
												</td>		
											</tr>
										</table>
									</td>					
									</logic:notEqual>						   			
									
									<%-- td>
										<html:reset styleClass="actionButton" >
											<bean:message  key="buttons.reset" />
										</html:reset>
									</td --%>
								</tr>
							</table>
							<!-- action buttons end -->