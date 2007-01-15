<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.bean.DefinedArrayRequestBean" %>
<%@ page import="edu.wustl.catissuecore.bean.DefinedArrayDetailsBean" %>
<%@ page import="edu.wustl.catissuecore.bean.ExistingArrayDetailsBean" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>

			<!--Tab page for Array Requests -->
	<table summary="Display Tabbed page" cellpadding="0" cellspacing="0" border="0" width="100%" id="table5_arrayDataTable" style="display:none;border-right:1px solid #5C5C5C;">
	 	
		<%
			//Retrieve List from request attribute for order items in defined arrays
			List definedArrayRequestMapList = (ArrayList)request.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
			if(definedArrayRequestMapList != null)
			{
				int arrayRowCounter = 0;
				session.setAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST,definedArrayRequestMapList);
		%>
			<!-- Iterate the list -->
		<logic:iterate id="defineArrayMap" collection="<%=definedArrayRequestMapList%>" type="java.util.Map">
		 <%
				Set defineArraySet = defineArrayMap.keySet();
		%>
				<tbody id="arrayTbody">
			<logic:iterate id="definedArrayRequestBean" collection="<%=defineArraySet%>" type="edu.wustl.catissuecore.bean.DefinedArrayRequestBean">
		<%
					List arrayDetailsBeanList = (ArrayList)defineArrayMap.get(definedArrayRequestBean);
					String arrayStatus = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_assignedStatus)";
					String orderItemIdInDefineArray = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_orderItemId)";
					String definedArrayId = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_arrayId)";
					String arrayId = "array_" + arrayRowCounter;
		 %>

		 <!-- Block to display Defined Array Name,Dimension,Class and type -->
		 <tr>
		 	<td height="30">
		 		<table cellpadding="3" cellspacing="0" border="0" width="100%" class="formSubTableTitle" style="border-top:1px solid #5C5C5C;border-right:1px solid #5C5C5C">	
			 			<tr>
							<td nowrap style="font-size:0.9em;">
								<label for="arrayName">
									<%= definedArrayRequestBean.getArrayName() + " Array" %>
								</label>
							</td>
							<td nowrap style="font-size:0.9em;">
								<label for="dimensions">
									<bean:message key='orderingSystem.tableheader.label.dimensions'/> : 
								</label>
								 <span style="font-weight:normal;">
									 <%=definedArrayRequestBean.getOneDimensionCapacity()%>,<%=definedArrayRequestBean.getTwoDimensionCapacity()%>
								</span>
							</td>		
							<td nowrap style="font-size:0.9em;">
								<label for="class">
									<bean:message key='orderingSystem.tableheader.label.class' /> :
								</label>
								<span style="font-weight:normal;">
									<%=definedArrayRequestBean.getArrayClass()%>
								</span>
							</td>
							<td nowrap style="font-size:0.9em;">
								<label for="type">
									<bean:message key='orderingSystem.tableheader.label.type' /> :
								</label>
								<span style="font-weight:normal;">
									<%=definedArrayRequestBean.getArrayType()%>
								</span>
							</td>
							<td nowrap>
								<bean:message key='orderingSystem.tableheader.label.arrayStatus' />:
								<html:select property="<%= arrayStatus %>" name="requestDetailsForm" styleClass="formFieldSized2" styleId="<%=arrayId%>" 				 								
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" > 
									<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
								</html:select>
							</td>
							<td nowrap>
							<%
									String switchArray = "switchArray" + arrayRowCounter; 
									String dataArray = "dataArray" + arrayRowCounter; 
									String headerArray = "headerArray" + arrayRowCounter;
									String btnCreateArrayId = "btnCreateArray" + arrayRowCounter;

									String orderItemInBean = definedArrayRequestBean.getOrderItemId();
									String arrayIdInBean = definedArrayRequestBean.getArrayId();
							%>
								<html:hidden name="requestDetailsForm" property="<%=orderItemIdInDefineArray%>" value="<%= orderItemInBean %>"/> 
								<html:hidden name="requestDetailsForm" property="<%=definedArrayId%>" value="<%= arrayIdInBean %>"/> 
								<a id="<%=switchArray%>" style="text-decoration:none" href="javascript:switchDefinedArrayBlock('<%=arrayRowCounter%>');">  
									<img src="images/nolines_minus.gif" border="0"/>
								</a>
							</td>
					</tr>
		 		</table>
		 	</td>
		 </tr>
		 
		 <!-- Block to display the table headings -->
		 <tr id="<%=headerArray%>">
		 	<td>
		 		<table cellpadding="3" cellspacing="0" border="0" width="100%" class="dataTable">
		 		  <tr>
			 		 	<th class="dataTableHeader" scope="col" align="left" colspan="2">
							<bean:message key='orderingSystem.tableheader.label.specimenName'/>
						</th>
						<th class="dataTableHeader" scope="col" align="left">
							<bean:message key='requestdetails.datatable.label.RequestFor'/>
						</th>
						<th class="dataTableHeader" scope="col" align="left">
							<bean:message key='requestdetails.datatable.label.RequestedQty'/>
						</th>
						<th class="dataTableHeader" scope="col" align="left">
							<bean:message key='requestdetails.datatable.label.AvailableQty'/>
						</th>
						<th class="dataTableHeader" scope="col" align="left">
							<bean:message key='requestdetails.datatable.label.AssignQty'/>
						</th>
						<th class="dataTableHeader" scope="col" align="left">
							<bean:message key='requestdetails.datatable.label.AssignStatus'/>
						</th>
		 		  </tr>
		
			
					<!-- Block to display the order items in the array-->			
					<%-- Iterate the list for each definedArrayBean Object --%>
						<%							
							int rowNumber =0 ;
						%>
						<logic:iterate id="defineArrayDetailsBeanObj" collection="<%=arrayDetailsBeanList%>" type="edu.wustl.catissuecore.bean.DefinedArrayDetailsBean">				
						<%
							//Variables required to set the id of each row.It is used in expandOrderItemsInArray() Js function for expand/collapse purpose.
							String switchDefinedArray = "switchdefineArray" + rowNumber + "_array" + arrayRowCounter;
							String dataDefinedArray = "dataDefinedArray" + rowNumber + "_array" + arrayRowCounter;
							
							String requestForArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_requestFor)"; 
							String assignQtyArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_assignedQty)"; 
							String assignStatusArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus)"; 	
							String descriptionArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_description)";
							String instanceOfArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_instanceOf)";
							String orderItemIdArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_orderItemId)";
							String specimenIdInMapArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_speicmenId)";
							boolean disableArrayOrderItemRow = false;

							if(defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST) 
								|| defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE) 
								|| defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE) )
							{
								disableArrayOrderItemRow = true;
							}

							//This is to update available qty for the specimen selected from requestFor drop down.
							String updateAvaiQtyForItemInArray = "avaiQty" + rowNumber;

							String requestForIdInArray = "requestForArray" + rowNumber;
							String onChangeValueForRequestForInArray = "updateQuantity('"+ requestForIdInArray  +"')";
						%>
							<tr class="dataRowLight" id="<%=dataArray%>">
									<td class="dataCellText" width="3%">
						 					<a id="<%=switchDefinedArray%>" style="text-decoration:none" href="javascript:expandOrderItemsInArray('<%=rowNumber%>','<%=arrayRowCounter%>');">  
											<img src="images/nolines_plus.gif" border="0"/>
								 	</td>
										 	
								 	<td class="dataCellText" width="20%">
						 			<% 
									   if(defineArrayDetailsBeanObj.getInstanceOf().trim().equalsIgnoreCase("Derived"))
									   {
									%>			
										<%-- Display derivative icon for child specimens --%>								
										<img src="images\Distribution.GIF" border="0"/>
									<% }
									%>
										<bean:write name="defineArrayDetailsBeanObj" property="requestedItem" />
								 	</td>
								 	
								 	<%
									 		if(defineArrayDetailsBeanObj.getInstanceOf().trim().equalsIgnoreCase("Existing"))
									 		{
									 			disableArrayOrderItemRow=true;
									 		}
									%>
								 	<td class="formField">
								 			<html:select property="<%= requestForArray %>" name="requestDetailsForm" styleClass="formFieldSized10" styleId="<%= requestForIdInArray %>" onchange="<%= onChangeValueForRequestForInArray %>"				 								
												disabled="<%= disableArrayOrderItemRow %>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" > 
											 	<html:optionsCollection property="specimenList" name="defineArrayDetailsBeanObj" label="name" value="value"/>
											</html:select>
		
											<bean:define id="specimenTypeInArray" name="defineArrayDetailsBeanObj" property="type" type="java.lang.String"/>
											<bean:define id="specimenClassInArray" name="defineArrayDetailsBeanObj" property="className" type="java.lang.String"/>
											<bean:define id="specimenObjIdInArray" name="defineArrayDetailsBeanObj" property="speicmenId" type="java.lang.String"/>
											<bean:define id="specimenCollGrpIdInArray" name="defineArrayDetailsBeanObj" property="specimenCollGroupId" type="java.lang.String"/>
											<%
									 			if(!specimenObjIdInArray.equals(""))
										 			session.setAttribute(Constants.SPECIMEN_TREE_SPECIMEN_ID,specimenObjIdInArray);
										 		else
										 			session.setAttribute(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID,specimenCollGrpIdInArray);	
												
												String url = "ShowFramedPage.do?pageOf=pageOfSpecimenTree&propertyName=" + requestForArray + "&type=" + specimenTypeInArray + "&specimenClass=" + specimenClassInArray;
												String appletWindow = "";
												if(disableArrayOrderItemRow == false)
													appletWindow = "javascript:NewWindow('"+url+"','name','375','330','yes');return false";
											%>
											<a href="#" onclick="">
												<img src="images\Tree.gif" border="0" width="24" height="18" title="<%= ApplicationProperties.getValue("requestdetails.tooltip.specimenTreeTooltip") %>">
											</a>
							 		</td>
								 	
								 	<%
									 	if(!(defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST) 
												|| defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE) 
												|| defineArrayDetailsBeanObj.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE)))
									 	{
									 		disableArrayOrderItemRow=false;												
									 	}
									 	String orderItemClassNameInArray = defineArrayDetailsBeanObj.getClassName();
									 	String orderItemtypeInArray = defineArrayDetailsBeanObj.getType();
									 %>
								 	
								 	<td class="dataCellText" width="13%">
								 			<bean:write name="defineArrayDetailsBeanObj" property="requestedQuantity" />
								 			<span>		
												<script>
													var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
													document.write(orderItemUnitInArray);
												</script>
											</span>
								 	</td>
								 	
								 	<td class="dataCellText" width="10%">
									 		<div id="updateAvaiQtyForItemInArray">
									 			<bean:write name="defineArrayDetailsBeanObj" property="availableQuantity" />
										 		<span>		
													<script>
														var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
														document.write(orderItemUnitInArray);
													</script>
												</span>	
											</div>
							 		</td>
							 		<%
										String assignedQty = defineArrayDetailsBeanObj.getAssignedQty();
									%>
							 		<td class="dataCellText" width="10%">
							 				<html:text name="requestDetailsForm" styleClass="formFieldSized3" maxlength="4"  property="<%= assignQtyArray %>" value="<%= assignedQty %>" disabled="<%= disableArrayOrderItemRow %>" />
							 				<span>		
												<script>
													var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
													document.write(orderItemUnitInArray);
												</script>
											</span>	
								 	</td>
								 	<td class="dataCellText" width="30%">
							 				<html:select property="<%=assignStatusArray%>" name="requestDetailsForm" styleClass="formFieldSized15"  
								 				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableArrayOrderItemRow %>">
								 			 	<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
											</html:select>
								 	</td>
							</tr>
								<!-- Block to display the expanded/collapsed row starts here-->
							<tr id="<%=dataDefinedArray%>" style="display:none">
								<td colspan='8'>
								   	<table class="rowExpansionTable" width="100%"> 
									   		<tr>
									   			<td width="15%" nowrap>
									   				<label for="LabelType">
									   					<bean:message key="specimen.type" />
									   				</label> : <bean:write name="defineArrayDetailsBeanObj" property="className" />
									   			</td>
									   			<td width="10%" nowrap>
									   				<label for="LabelDescription">
									    				<bean:message key="orderingSystem.requestdetails.label.description" />
									   				</label> : 				
									   			</td>
												<td rowspan='2' width="60%" nowrap>
												 <bean:define id="specimenInArrayDescription" name="defineArrayDetailsBeanObj" property="description" type="java.lang.String" />
													<html:textarea styleId="description" styleClass="formFieldSized2" value="<%=specimenInArrayDescription%>" property="description" cols='60' rows='2' disabled="<%= disableArrayOrderItemRow %>"/>
												</td>
									   		</tr>
									   		<tr>
									   			<td width="15%" nowrap>
									   				<label for="LabelSubType">
									    				<bean:message key="specimen.subType" />
									   				</label> :  <bean:write name="defineArrayDetailsBeanObj" property="type" />
									   			</td>									   												   			
									   		</tr>	
								   	</table>									   		
								</td> 
							</tr>
								<!-- Block to display the expanded/collapsed row ends here-->
							 <% 
									String orderItemIdInArray = defineArrayDetailsBeanObj.getOrderItemId();
								  	String instanceOfObjInArray = defineArrayDetailsBeanObj.getInstanceOf();
								  	String specimenIdInArray = defineArrayDetailsBeanObj.getSpeicmenId();
							%>
							<html:hidden name="requestDetailsForm" property="<%= instanceOfArray %>" value="<%= instanceOfObjInArray %>"/>
							<html:hidden name="requestDetailsForm" property="<%= orderItemIdArray %>" value="<%= orderItemIdInArray %>" />
							<html:hidden name="requestDetailsForm" property="<%= specimenIdInMapArray %>" value="<%= specimenIdInArray %>" />
							
							<% rowNumber++; %>
						</logic:iterate>
							<!-- Create Array Button -->								
						<tr id="<%=btnCreateArrayId%>">
							<td colspan='7'>
								<table width="100%" style="border-bottom:1px solid #5C5C5C;">
									<tr> 
										<td align="right">
										<% if(definedArrayRequestBean.getArrayId() == null)
											{
										 %>
											<input type="button" id="btnCreateArray" name="btnCreateArray" class="actionButton" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>')" />
										<%  }else{
										%>
											<input type="button" id="btnCreateArray" name="btnCreateArray" class="actionButton" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>')" disabled/>
										<% }
											String defineArrayName = "defineArrayName_" + arrayRowCounter; 
										%>
											<input type="hidden" name="defineArrayName" id="<%=defineArrayName%>" value="<%=definedArrayRequestBean.getArrayName()%>" />
										</td>&nbsp;
									</tr>
								</table>
							</td>
						</tr>
				</table>
			</td>
		</tr>	
		
		 <%			arrayRowCounter = arrayRowCounter + 1; 	
		 %>
		 		</logic:iterate>
		</logic:iterate>
		<input type="hidden" name="definedArrayRows" id="definedArrayRows" value="<%=arrayRowCounter%>" />
		 <%
			}//End If
		 %>
		
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		
		<tr>
			<td>
				<!-- Table to display the data of existing array -->
				<table cellpadding="3" cellspacing="0" border="0" width="100%" summary="Display Existing Array Header Info">
					
				<tr>
					<td>
						<table class="formSubTableTitle" width="100%" style="border-top:1px solid #5C5C5C;">
							<tr>
								<td align="left" width="90%">
									<label for="existingArray">
										<bean:message key='orderingsystem.label.existingArray'/>
									</label>
								</td>
								<td style="border-right:1px solid #5C5C5C;" width="5%">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a id="switchExistingArray" style="text-decoration:none" href="javascript:expandArrayBlock()">  
										<img src="images/nolines_minus.gif" border="0"/>
									</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
					
				<!--tbody class="dataTable" id="existingArrayId"-->
				<tr><td style="padding-right=0em;" nowrap>
					<table summary="Display Existing Array Data" cellspacing="0" width="100%" cellpadding="3" class="dataTable" border="0" id="existingArrayId">
						 <tr width="100%" style="border-top:1px solid #5C5C5C;">
								<th class="dataTableHeader" scope="col" align="left" nowrap style="border-left:1px solid #5C5C5C;" width="20%">
									<bean:message key='orderingsystem.label.arrayName'/>
								</th>
								<th class="dataTableHeader" scope="col" align="left" width="10%">							
									<bean:message key='requestdetails.datatable.label.RequestedQty'/>
								</th>
								<th class="dataTableHeader" scope="col" align="left" width="10%" nowrap>							
									<bean:message key='requestdetails.datatable.label.AssignQty'/>
								</th>
								<th class="dataTableHeader" scope="col" align="left" width="20%">
									<bean:message key='orderingSystem.requestdetails.label.description'/>
								</th>
								<th class="dataTableHeader" scope="col" align="left" width="15%">
									<bean:message key='orderingsystem.arrayRequests.datatable.label.AddDescription'/>
								</th>
								<th class="dataTableHeader" scope="col" align="left" width="25%">
									<bean:message key='requestdetails.datatable.label.AssignStatus'/>
								</th>
						 </tr>
				 	<%
						int rowCounter = 0;
						if(request.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST) != null)
						{
							List arrayRequestList = (ArrayList)request.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
							session.setAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST,arrayRequestList);
					%>
				<logic:iterate id="existingArrayRequestObj"	collection="<%=arrayRequestList%>" type="edu.wustl.catissuecore.bean.ExistingArrayDetailsBean">
					<%
							String assignedStatus = "value(ExistingArrayDetailsBean:"+rowCounter+"_assignedStatus)";
							String addDescription = "value(ExistingArrayDetailsBean:"+rowCounter+"_addDescription)";
							String existingArrayId = "value(ExistingArrayDetailsBean:"+rowCounter+"_arrayId)";	
							String existingArrayOrderItemId = "value(ExistingArrayDetailsBean:"+rowCounter+"_orderItemId)";	
							//String existingRequestedQty = "value(ExistingArrayDetailsBean:"+rowCounter+"_requestedQuantity)";
							String existingAssignedQty = "value(ExistingArrayDetailsBean:"+rowCounter+"_assignedQuantity)";				
							String description = "value(ExistingArrayDetailsBean:"+rowCounter+"_description)";
							
							//To assign id to select element
							String existingArraySelectId = "existingArray_" + rowCounter;
							boolean disableExistingArrayOrderItem = false;
					%>
					
					<!-- Block to display Existing Array Data -->
				 	<tr class="dataRowLight">
				 			<%if(existingArrayRequestObj.getAssignedStatus().equalsIgnoreCase("Distributed"))
							{
								disableExistingArrayOrderItem = true;
							}
					 		%>
					 		
					 		<td class="dataCellText" width="20%" nowrap style="border-left:1px solid #5C5C5C;">
					 			<bean:write name="existingArrayRequestObj" property="bioSpecimenArrayName" />
	 				 		</td>
							
							<td class="dataCellText" nowrap width="10%">
	 						<%
							  //If Existing Array,then,display 'NA' in the textbox for requested qty
											if(new Double(existingArrayRequestObj.getRequestedQuantity()).doubleValue() == 0.0)
											{
							%>
								<%--html:text property="<%=existingRequestedQty%>" styleClass="formFieldSized3" maxlength="8"  size="5" value="NA" disabled="true"/--%>
														NA
								<%--html:hidden name="requestDetailsForm" property="<%=existingRequestedQty%>" value="0.0" /--%>
							<%
											}
							  //If Tissue Slides from Block,then,display the quantity in textbox
							  //Enable/Disable the textbox depending upon the status
											else
											{	
							%>
								<!--html:text property="requestedQuantity" name="existingArrayRequestObj" styleClass="formFieldSized3" maxlength="8"  size="5" disabled="true"/-->
								<bean:write name="existingArrayRequestObj" property="requestedQuantity" />&nbsp;&nbsp;
								<%=Constants.UNIT_CN%>
							<%
											}
							%>
							</td>
							
							<td class="dataCellText" nowrap width="10%">
							<%
							  //If Existing Array,then,display 'NA' in the textbox for assign qty
											if(new Double(existingArrayRequestObj.getRequestedQuantity()).doubleValue() == 0.0)
											{
							%>
								<html:text property="<%=existingAssignedQty%>" styleClass="formFieldSized3" maxlength="8"  size="5" value="NA" disabled="true"/>
								<html:hidden name="requestDetailsForm" property="<%=existingAssignedQty%>" value="0.0" />
								<%
											}
							  //If Tissue Slides from Block,then,admin assigns the qty
							  //Enable/Disable the textbox depending upon the status
											else
											{	
							%>
								<html:text property="<%=existingAssignedQty%>" styleClass="formFieldSized3" maxlength="8"  size="5" disabled="<%=disableExistingArrayOrderItem%>"/>
								&nbsp;&nbsp;<%=Constants.UNIT_CN%>
							<%
											}
							%>
							</td>
					 		
					 		<td class="dataCellText" width="20%">					 		
						 		<html:textarea name="requestDetailsForm" styleId="description" styleClass="formFieldSized" property="<%= description %>" cols='60' rows='2' readonly="true"/>
					 		</td>
					 		
					 		<td class="dataCellText" width="15%">
					 			<html:textarea name="requestDetailsForm" styleClass="formFieldSized" cols="60" rows="2" styleId="addDescription" property="<%=addDescription%>" />
					 		</td>
					 		
					 		<td class="dataCellText" width="25%">
					 			<html:select property="<%=assignedStatus%>" name="requestDetailsForm" styleClass="formFieldSized15" styleId="<%=existingArraySelectId%>" 
								 			disabled="<%=disableExistingArrayOrderItem%>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								 	<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
								</html:select>
					 		</td>
		
					 		<%
								String arrayId = existingArrayRequestObj.getArrayId();
								String orderItemId = existingArrayRequestObj.getOrderItemId();
							%>
					 		<html:hidden name="requestDetailsForm" property="<%= existingArrayId %>" value="<%= arrayId %>" />
					 		<html:hidden name="requestDetailsForm" property="<%= existingArrayOrderItemId %>" value="<%= orderItemId %>" />
				  </tr>
				
				 	<%
						rowCounter++;
				 	%>
				 </logic:iterate>
				 	<input type="hidden" name="numOfExistingArrays" id="numOfExistingArrays" value="<%=rowCounter%>" />
				
				 <%
				 		}//End If
						else
						{
							session.removeAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
						}
				 %>
				</table> <!-- table with id='existingArrayId' ends here -->
			</td></tr>
		  </table>
			</td>
		</tr>
	
	 </table> <!-- table5_arrayDataTable ends here -->