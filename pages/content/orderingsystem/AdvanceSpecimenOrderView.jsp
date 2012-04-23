<table border="0" width="100%" cellpadding="3" cellspacing="0" >
						
					<tr>
						<td class="bottomtd" colspan="11"></td>
					</tr>
					<tr>
						  <% 
									if(requestDetailsList != null)
									{
									    session.setAttribute(Constants.REQUEST_DETAILS_LIST,requestDetailsList);
									 	int i = 0; 
										String rowStatusValue ="";
						 %>
								<td colspan="3" align="center" valign="top" class="tableheading" width="30%">
									<strong>
									<bean:message key="requestdetails.header.orderedSpecimenDetails" />
									</strong>
								</td>
					             <td colspan="3" align="center" valign="top" class="tableheading" width="30%">
									<strong>
										<bean:message key="requestdetails.header.RequestedSpecimenDetails" />
									</strong>
								 </td>
												               
				                <td width="10%" rowspan="2" valign="top" class="tableheading">
									<strong>
										<bean:message key="consent.consentforspecimen"/>
									</strong>	
								</td>
				                <td width="18%" rowspan="2" valign="top" class="tableheading">
									<strong>
										<bean:message key='requestdetails.datatable.label.AssignStatus'/>
									</strong>
									</br>
					                <span class="black_new">
									     <html:select property="status" name="requestDetailsForm" styleClass="formFieldSized11" styleId="nextStatusId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=checkQuantityforAll%>">
											<html:options collection="<%= Constants.REQUESTED_ITEMS_STATUS_LIST %>" labelProperty="name" property="value" />		
									     </html:select>
								</td>	
				                <td rowspan="2" valign="top" class="tableheading" width="10%">
									<strong>
										<bean:message key="requestdetails.header.label.Comments" />
									</strong>
								</td>
				              </tr>
				              <tr>
								<td width="15%" class="subtd" >
									<bean:message key='requestdetails.datatable.label.RequestItem'/>
								</td>
								<td class="subtd" width="12%">
								<bean:message key="orderingSystem.tableheader.label.type" />,
								<bean:message key='requestdetails.datatable.label.AvailableQty'/>
								</td>
								<!--<td class="subtd" width="10%">
								
								</td>-->
								<td bgcolor="#d7d7d7" width="2%">&nbsp;
								</td>
								
								<td width="17%" class="subtd"  valign="top">
									<bean:message key='requestdetails.datatable.label.RequestFor'/>
								</td>
				              
								<td width="12%" class="subtd"  valign="top">
									<strong>
										<bean:message key="orderingSystem.tableheader.label.type" />,
										<bean:message key='requestdetails.datatable.label.AvailableQty'/>
									</strong>
								</td>	

				                <td width="1%" align="left" bgcolor="#d7d7d7" >&nbsp;
								</td>
				              </tr>
   <!----------------rows for the specimen request tab------------------>
							  <tbody id="tbody">	
				<%          int rows=0;
				%>
					<logic:iterate id="requestDetailsBeanObj" collection="<%= requestDetailsList%>" type="edu.wustl.catissuecore.bean.RequestDetailsBean">
				<%			String fontStyle="black_ar";
							String bgStyle="background-color:#ffffff;";
							rows++;
							if(rows<=count)
							if(rows%2 == 0)
							{
								fontStyle="tabletd1";
								bgStyle="background-color:#f6f6f6;";
							}
							
						 	String requestFor = "value(RequestDetailsBean:"+i+"_requestFor)"; 
						 	String assignQty = "value(RequestDetailsBean:"+i+"_assignedQty)"; 
						 	String assignStatus = "value(RequestDetailsBean:"+i+"_assignedStatus)"; 	
							String description = "value(RequestDetailsBean:"+i+"_description)";
							String instanceOf = "value(RequestDetailsBean:"+i+"_instanceOf)";
							String orderItemId = "value(RequestDetailsBean:"+i+"_orderItemId)";
							String specimenIdInMap = "value(RequestDetailsBean:"+i+"_specimenId)";
							String consentVerificationkey = "value(RequestDetailsBean:"+i+"_consentVerificationkey)";
							String rowStatuskey = "value(RequestDetailsBean:"+i+"_rowStatuskey)";
							String specimenQuantityUnit = "value(RequestDetailsBean:"+i+"_specimenQuantityUnit)";
							String labelStatus="labelStatus"+i;
							String selectedSpecimenType="value(RequestDetailsBean:"+i+"_selectedSpecimenType)";
							String selectedSpecimenQuantity="value(RequestDetailsBean:"+i+"_selectedSpecimenQuantity)";
							String requestedItem = "value(RequestDetailsBean:"+i+"_requestedItem)";
							String requestedQty = "value(RequestDetailsBean:"+i+"_requestedQty)";
							String availableQty = "value(RequestDetailsBean:"+i+"_availableQty)";
							String spClass = "value(RequestDetailsBean:"+i+"_className)";
							String spType = "value(RequestDetailsBean:"+i+"_type)";
							String distributedItemId = "value(RequestDetailsBean:"+i+"_distributedItemId)"; 
							String specimenList = "requestFor(RequestForDropDownList:"+i+")";
									
							String specimenCollGroupId = "value(RequestDetailsBean:"+i+"_specimenCollGroupId)";
							String actualSpecimenType = "value(RequestDetailsBean:"+i+"_actualSpecimenType)";
							String actualSpecimenClass = "value(RequestDetailsBean:"+i+"_actualSpecimenClass)";
							String canDistribute = "value(RequestDetailsBean:"+i+"_canDistribute)";
							String specimenClickFunction = "showSpecimenDetails("+i+")";
							String viewSpecimenDetailsFunction = "viewSpecimenDetails("+requestDetailsBeanObj.getSpecimenId()+")";
							String createSpecimenFunction = "createSpecimen("+i+")";
							String aliquoteClickFunction = "createAliquots("+i+")";
							String derivativeCreateFunction = "createDerivatives("+i+")";
							String changeAvailableQuantity = " onSpecimenChange("+i+")";
							String checkQuantity = "checkQuantity("+i+")";
								
							//added for consent page:	
							String showNewConsentPageFunction = "showNewConsentPage("+requestDetailsBeanObj.getSpecimenId()+")";
							boolean disableRow = false;
							if(((((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)) || (((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
							&& (!((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_distributedItemId"))).trim().equals("")))
							{
										disableRow=true;
										disabledStatus=i;
										rowStatusValue =	"disable";	
				%>
									
								<html:hidden name="requestDetailsForm" property="<%= assignStatus %>" />
								<html:hidden name="requestDetailsForm" property="<%= requestFor %>" />
								<html:hidden name="requestDetailsForm" property="<%= description %>" />
								<html:hidden name="requestDetailsForm" property="<%= assignQty %>" />
								
								<!--	<html:hidden name="requestDetailsForm" property="<%= consentVerificationkey %>"/>	-->
				<%
							}
							else{ rowStatusValue =	"enable";
							}
								 	//Added By Ramya.Construct corresponding rowids for request row expansion purpose.								 	
							String data = "data" + i;
							String switchText = "switch" + i;
							//Added By Ramya.Construct select element ids for corresponding rows.
									//This is to update all rows' status in one shot.
							String select = "select_" +	i;
									//This is to update available qty for the specimen selected from requestFor drop down.
							String updateAvaiQty = "avaiQty" + i;
							String availableQtyId = "availableQtyId"+i;
							String selectedSpecimenTypeId = "selectedSpecimenTypeId"+i;
							String specimenQuantityUnitId = "specimenQuantityUnitId"+i;
							String requestedQtyId = "requestedQtyId"+i;
							String requestForId = "requestFor" + i;
							String onChangeValueForRequestFor = "updateQuantity('"+ requestForId  +"')";
							if(((requestDetailsForm.getValue("RequestDetailsBean:"+i+"_specimenId"))) != null && !((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_specimenId"))).equals(""))
							{
				%>
								<html:hidden name="requestDetailsForm" property="<%= specimenIdInMap %>" />
				<%			}
							else
							{
				%>
								<html:hidden name="requestDetailsForm" property="<%= specimenCollGroupId %>" />
				<%			}
			    %>	<!-- Html hidden variables for all static fields -->	 
								
								 <html:hidden name="requestDetailsForm" property="<%= requestedItem %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= requestedQty %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= availableQty %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= spClass %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= spType %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= actualSpecimenClass %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= actualSpecimenType %>" />	
								 <html:hidden name="requestDetailsForm" property="<%=canDistribute%>"  styleId="<%=canDistribute%>" />
								
								<tr>	
									<td class="<%=fontStyle%>" >
									<%													                                                     if(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim(            ).equalsIgnoreCase("Derived"))
								{
					%>											
										<img src="images/Distribution.GIF" border="0"/>

									 		<!-- bean:write name="requestDetailsBeanObj" property="requestedItem" /-->
			 		<%			}
								else																					if(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))			).trim().equalsIgnoreCase("DerivedPathological")
										|| ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological"))
									{
					%>
										<img src="images/Participant.GIF" border="0"/>
					<%				}
						
										String toolTipTypeClass = "Type:"+((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_actualSpecimenType")))+", Quantity:"+((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_availableQty")).toString()); %>
									 										 		
					<%				if(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological")
												|| ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological")) 
									{
					%>
												
											<bean:write name="requestDetailsForm" property="<%= requestedItem%>" />									 	
					<%
									}
									else
									{
					%>
									<span title="<%= toolTipTypeClass %>">
										<html:link href="#" styleClass="view" styleId="label" onclick="<%=viewSpecimenDetailsFunction%>">
									 		<bean:write name="requestDetailsForm" property="<%= requestedItem %>" />	
										</html:link>
					<%
									}
									String className = ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_className")));
									String type = ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_type")));
					%>
							 		</span>
									</td>
								
			 						<td class="<%=fontStyle%>" >
										<bean:write name="requestDetailsForm" property="<%= spType %>" />,
											
					<%
										if((((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Existing")||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological"))&&(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_distributedItemId"))).trim().equals("")))
										{
											disableRow=false;												
										}
										
					%>
										</br>
											<html:text styleClass="formFieldSmallNoBorder" styleId="<%=requestedQtyId%>" property="<%= requestedQty %>" readonly="true" style="<%=bgStyle%>"/>
											<span>		
													<script>
															var v= getUnit('<%= className %>','<%= type %>');
															document.write(v);
														</script>
											</span>	
										</td>

									<td class="<%=fontStyle%>">
									</td>


								<td class="<%=fontStyle%>" >
				
										<html:select property="<%= requestFor %>" name="requestDetailsForm" size="l" styleClass="formFieldSizedText" styleId="<%= requestForId %>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%= changeAvailableQuantity%>" disabled="<%= disableRow %>">
										<%
										List valList = requestDetailsForm.getRequestFor("RequestForDropDownList:"+i);
										for (Object object : valList) 
										{
											NameValueBean valBean = (NameValueBean)object;
											%>
											<option title="<%=valBean.getName()%>" value="<%=valBean.getValue()%>">
																	<%=valBean.getName()%></option>
											<%
										}
										%>
										
																
																	
																
														
										</html:select> 		
										</br>
										<logic:equal name="requestDetailsForm" property="<%=rowStatuskey%>" value="enable">
										
											<logic:equal name="requestDetailsForm" property="<%=instanceOf%>" value="Pathological">
												<a href="#" onclick="<%=createSpecimenFunction%>">
												   <img src="images/Cycle_col.gif" border="0" alt="Create Specimen"  title="Create Specimen"/>
												</a> 
											</logic:equal>
										<logic:equal name="requestDetailsForm" property="<%=instanceOf%>" value="DerivedPathological">
											<a href="#" onclick="<%=createSpecimenFunction%>">
												   <img src="images/Cycle_col.gif" border="0" alt="Create Specimen"  title="Create Specimen"/>
												</a> 
										</logic:equal>
							
										<a href="#" onclick="<%=specimenClickFunction%>">
											<img src="images/uIEnhancementImages/ic_specimen.gif" border="0" alt="View Specimen"  title="View Specimen"/>
										</a> 
										<a href="#" onclick="<%=aliquoteClickFunction%>">
											<img src="images/uIEnhancementImages/a.gif" border="0"  alt="Create Aliquot" title="Create Aliquot"/>
										</a> 
										<a href="#" onclick="<%=derivativeCreateFunction%>">
											<img src="images/uIEnhancementImages/ic_d.gif" border="0" alt="Create Derivative"  title="Create Derivative"/>
										</a> 
										</logic:equal>
					 		
		  					 	</td>
						
							 	<td class="<%=fontStyle%>">
				
																		
									<html:text styleClass="formFieldSmallNoBorderlargeSize" styleId="<%=selectedSpecimenTypeId%>" property="<%= selectedSpecimenType %>" readonly="true" style="<%=bgStyle%>"/>
										</br>
										
									<html:text styleClass="formFieldSmallNoBorder" styleId="<%=availableQtyId%>" property="<%=selectedSpecimenQuantity%>" readonly="true" style="<%=bgStyle%>" />

									<html:text styleClass="formFieldSmallNoBorder" styleId="<%=specimenQuantityUnitId%>" property="<%=specimenQuantityUnit%>" readonly="true" style="<%=bgStyle%>"/>
								
								</td>
								
								<td class="<%=fontStyle%>">
									</td>
	

			   <%
								String consentVerificationStatus=
										((String)(requestDetailsForm.getValue("value(RequestDetailsBean:"+i+"_consentVerificationkey)")));
								String specimenIdValue=((String)(requestDetailsForm.getValue("value(RequestDetailsBean:"+i+"_specimenId)")));
									
				%>
					
							
								<td class="<%=fontStyle%>"  > 
									<span class="view">
									<html:hidden property="<%=specimenIdInMap%>" styleId="<%=specimenIdInMap%>"  value="<%=specimenIdValue%>"/>
									<html:hidden property="<%=rowStatuskey%>" styleId="<%=rowStatuskey%>"  value="<%=rowStatusValue%>"/>		

											
				<%																							
					        if(
							((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological") ||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological"))
							{
					
				%>					
								<%=Constants.NO_CONSENTS%>
								<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.NO_CONSENTS%>"/>
							
				<%			}
				%>

				<%																																
							if(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Derived") ||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Existing"))
							{
				%>

				<%
								if(!disableRow)
								{
				%>	
									<logic:equal name="requestDetailsForm" property="<%=consentVerificationkey%>" value="<%=Constants.NO_CONSENTS%>">
											<%=Constants.NO_CONSENTS%>
											<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.NO_CONSENTS%>"/>
									</logic:equal>
									<logic:notEqual name="requestDetailsForm" property="<%=consentVerificationkey%>" value="<%=Constants.NO_CONSENTS%>">
											<a  id="<%=labelStatus%>" class="view" href="javascript:showNewConsentPage('<%=specimenIdInMap%>','<%=labelStatus%>','<%=consentVerificationkey%>','<%=i%>')">
									<logic:notEmpty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<bean:write name="requestDetailsForm" property="<%=consentVerificationkey%>"/>
									</logic:notEmpty>
									<logic:empty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<%=Constants.VIEW_CONSENTS %>
									</logic:empty>
									<logic:notEmpty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=consentVerificationStatus%>"/>
									</logic:notEmpty>
									<logic:empty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.VIEW_CONSENTS %>"/>
									</logic:empty>
										   </a>
									</logic:notEqual>
				<%
								}
								else
								{ 
				%>
										<%=Constants.VERIFIED%>
										<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.VERIFIED%>"/>
				<%
								}
				%>				</span>
								</td>
				<%		}
				%>
									 	
							 	<td class="<%=fontStyle%>" >	<span class="black_new">
								 		<html:select property="<%=assignStatus %>" name="requestDetailsForm" styleClass="formFieldSized11" styleId="<%=select%>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableRow %>" onclick="<%=checkQuantity%>">
					 					<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>		</html:select>
										</span>
									 	</td>
										<td class="<%=fontStyle%>" >												 
											<html:textarea styleId="description" styleClass="black_ar"  property="<%= description%>" cols='20' rows='2' disabled="<%= disableRow %>"/>
								</td>
							 </tr>

								  <!-- Block for row expansion ends here -->
								  <!--  Flag for instanceOf  -->
	
										<html:hidden name="requestDetailsForm" property="<%= instanceOf %>" />
										<html:hidden name="requestDetailsForm" property="<%= orderItemId %>" />
										<html:hidden name="requestDetailsForm" property="<%= distributedItemId %>" />
									 <% i++; %>
					 </logic:iterate>
				<%	 } //End Outer IF 
				%>
						</table>
						
