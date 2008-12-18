<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<!-- action buttons begins -->
							<div id="scgPageButtons">
								
											<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
												<% 	
													isAddNew=true;
												%>
											</logic:equal>
											<!--
												Patch ID: Bug#3184_13
												Description: Added restrict checkbox
											-->
											 <tr>
									          <td colspan="3" align="left" class="dividerline" ><span class="black_ar">
													<html:checkbox styleId="restrictSCGCheckbox" property="restrictSCGCheckbox" value="true" onclick="disableButtonsOnCheck(this)">
														<bean:message key="specimen.checkboxLabel"/>
													</html:checkbox>
												</td>
											</tr>
											<tr>
													<td colspan="1" valign="center" nowrap>
													<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
														<span class="black_ar">
															<bean:message key="print.checkboxLabel"/>
														</span>
														</html:checkbox>
											        </td>
												       <td>
					   			                         <%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 				                          </td>
														</tr>	
										<!--  End : Displaying   printer type and location -->	
							
											 <tr>
												<td colspan="3" class="bottomtd"></td>
											</tr>
											<tr>
												<td colspan="3" align="left" class="buttonbg">
												<%
												if(operation.equals(Constants.ADD))
												{
												%>
													<html:button styleClass="blue_ar_b" 
															property="submitPage" 
															title="Submit Only"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>" 
															onclick="<%=normalSubmit%>"
						  				     	    		styleId = "submitOnly">
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
													<html:button styleClass="blue_ar_b" 
															property="submitPage" 
															title="Submit Only"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>" 
															onclick="<%=str%>"
															styleId = "submitOnly">
											     	</html:button>
												<%	
												}
												%>
												
											
												<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
												
													<!-- Patch ID: Bug#4227_7 -->
													<html:button styleClass="blue_ar_c"  
															property="submitPage" 
															title="Submit and Add Specimen"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][0]%>" 
															disabled="<%=isAddNew%>" 
															onmousedown="setButtonType(this)" 
															onkeydown="setButtonType(this)" 
															onclick="<%=forwardToSubmit%>"
						  				     	    		styleId = "submitAndAdd">
											     	</html:button>
												
												
													<!-- 
														Patch ID: Bug#3184_37
													 	Description: Added id to the button. Needed to enable and disable in the javascript
													-->
													<!-- Patch ID: Bug#4227_6 -->
													<html:button styleClass="blue_ar_c"  
															property="submitPage" 
															title="Submit and Add Multiple Specimen"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][0]%>" 
															disabled="<%=isAddNew%>" 
															onmousedown="setButtonType(this)" 
															onkeydown="setButtonType(this)" 
															onclick="<%=forwardToSubmitForMultipleSpecimen%>" 
															styleId="submitAndAddMultiple">
											     	</html:button>
											
												</logic:notEqual>	
												<!-- delete button added for disabling the objects -->
												
													<%
														String deleteAction="deleteObject('" + formName +"','" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
													%>
													<html:button styleClass="blue_ar_c"
														property="disableRecord"
														title="Delete or Disable Record"
														value="Delete"
														onclick="<%=deleteAction%>">
													</html:button>
												</td>

											</tr>
															
													   			
									
									<%-- td>
										<html:reset styleClass="actionButton" >
											<bean:message  key="buttons.reset" />
										</html:reset>
									</td --%>
								
							</div>
							<!-- action buttons end -->