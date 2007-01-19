<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
								
									<td>
										<table>
											<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
												<% 	
													isAddNew=true;
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
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>" 
															onclick="<%=normalSubmit%>"	>
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
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>" 
															onclick="<%=str%>">
											     	</html:button>
												<%	
												}
												%>
												</td>
											
												<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
												<td nowrap class="formFieldNoBorders">
													<html:button styleClass="actionButton"  
															property="submitPage" 
															title="Submit and Add Specimen"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][0]%>" 
															disabled="<%=isAddNew%>" 
															onclick="<%=forwardToSubmit%>">
						  				     	    
											     	</html:button>
												</td>
												<td nowrap class="formFieldNoBorders">
													<html:button styleClass="actionButton"  
															property="submitPage" 
															title="Submit and Add Multiple Specimen"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][0]%>" 
															disabled="<%=isAddNew%>" 
															onclick="<%=forwardToSubmitForMultipleSpecimen%>">
						  				     	    
											     	</html:button>
												</td>
												</logic:notEqual>		
											</tr>
										</table>
									</td>					
													   			
									
									<%-- td>
										<html:reset styleClass="actionButton" >
											<bean:message  key="buttons.reset" />
										</html:reset>
									</td --%>
								</tr>
							</table>
							<!-- action buttons end -->