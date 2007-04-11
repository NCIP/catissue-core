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
		<%							
							int rowNumber =0 ;
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
					
					String requestedItem = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_arrayName)";
					String oneDimensionCapacity = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_oneDimensionCapacity)";
					String twoDimensionCapacity = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_twoDimensionCapacity)";
					String specimenClass = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_arrayClass)";
					String specimenType = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_arrayType)";
					String distributedItemId = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_distributedItemId)";
					String createArrayCondition = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_createArrayButtonDisabled)";
					String noOfItemsString = "value(DefinedArrayRequestBean:"+arrayRowCounter+"_noOfItems)";
					String noOfItemsValue = ((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_noOfItems")));
					Integer items = new Integer(noOfItemsValue);
					int noOfItems = items.intValue();
					
					String arrayId = "array_" + arrayRowCounter;
					boolean disableDefineArray = false;
		 %>

		 <!-- Block to display Defined Array Name,Dimension,Class and type -->
		 <tr>
		 	<td height="30">
		 		<table cellpadding="3" cellspacing="0" border="0" width="100%" class="formSubTableTitle" style="border-top:1px solid #5C5C5C;border-right:1px solid #5C5C5C">	
			 			<tr>
							<td nowrap style="font-size:0.9em;">
								<label for="arrayName">
									<%= ((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_arrayName"))) + " Array" %>
								</label>
							<html:hidden name="requestDetailsForm" property="<%=requestedItem%>" /> 
							<html:hidden name="requestDetailsForm" property="<%=oneDimensionCapacity%>" /> 
							<html:hidden name="requestDetailsForm" property="<%=twoDimensionCapacity%>" /> 	
							<html:hidden name="requestDetailsForm" property="<%=specimenClass%>" /> 
							<html:hidden name="requestDetailsForm" property="<%=specimenType%>" /> 
							<html:hidden name="requestDetailsForm" property="<%=distributedItemId%>" /> 
							<html:hidden name="requestDetailsForm" property="<%=noOfItemsString%>" /> 
							</td>
							<td nowrap style="font-size:0.9em;">
								<label for="dimensions">
									<bean:message key='orderingSystem.tableheader.label.dimensions'/> : 
								</label>
								 <span style="font-weight:normal;">
									 <%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_oneDimensionCapacity"))%>,<%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_twoDimensionCapacity"))%>
								</span>
								
							</td>		
							<td nowrap style="font-size:0.9em;">
								<label for="class">
									<bean:message key='orderingSystem.tableheader.label.class' /> :
								</label>
								<span style="font-weight:normal;">
									<%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_arrayClass"))%>
								</span>
							</td>
							<td nowrap style="font-size:0.9em;">
								<label for="type">
									<bean:message key='orderingSystem.tableheader.label.type' /> :
								</label>
								<span style="font-weight:normal;">
									<%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_arrayType"))%>
								</span>
							</td>
							<%if((((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
									&& (!((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_distributedItemId"))).trim().equals(""))) 								
							{
								disableDefineArray = true;
							%>
							<html:hidden name="requestDetailsForm" property="<%=arrayStatus%>" /> 	
							<%}%>
							<td nowrap>
								<bean:message key='orderingSystem.tableheader.label.arrayStatus' />:
								<html:select property="<%= arrayStatus %>" name="requestDetailsForm" styleClass="formFieldSized2" styleId="<%=arrayId%>" 				 								
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableDefineArray %>"> 
									<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
								</html:select>
							</td>
							<td nowrap>
							<%
									String switchArray = "switchArray" + arrayRowCounter; 
									String dataArray = "dataArray" + arrayRowCounter; 
									String headerArray = "headerArray" + arrayRowCounter;
									String btnCreateArrayId = "btnCreateArray" + arrayRowCounter;

									//String orderItemInBean = definedArrayRequestBean.getOrderItemId();
									//String arrayIdInBean = definedArrayRequestBean.getArrayId();
							%>
								<html:hidden name="requestDetailsForm" property="<%=orderItemIdInDefineArray%>" /> 
								<html:hidden name="requestDetailsForm" property="<%=definedArrayId%>" /> 
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
			 		 	<th class="dataTableHeader" scope="col" align="center"  colspan='5'>
								 	Requested
								 	</th>
								 	<th class="dataTableHeader" scope="col" align="left" width="5%" colspan="" rowspan="2" scope="col">
										<bean:message key='requestdetails.datatable.label.AvailableQty'/>
									</th>
								
									<!-- th class="dataTableHeader" scope="col" align="left" width="10%" rowspan="2" scope="col">
										<bean:message key='requestdetails.datatable.label.AssignQty'/>
									</th-->
								
									<th class="dataTableHeader" scope="col" align="left" width="20%" rowspan="2" scope="col">
										<bean:message key='requestdetails.datatable.label.AssignStatus'/>
									</th>		
								 </tr>
								 <tr>	
									<th class="dataTableHeader" scope="col" align="left" width="20%" colspan='2'>
										<bean:message key='requestdetails.datatable.label.RequestItem'/>
									</th>
								
									<th class="dataTableHeader" scope="col" align="left" width="25%">
										<bean:message key='requestdetails.datatable.label.RequestFor'/>
									</th>
									<th class="dataTableHeader" scope="col" align="left" width="10%">
										Class, Type
									</th>
								
									<th class="dataTableHeader" scope="col" align="left" width="5%">
										<bean:message key='requestdetails.datatable.label.RequestedQty'/>
									</th>
		 		  </tr>
		
			
					<!-- Block to display the order items in the array-->			
					<%-- Iterate the list for each definedArrayBean Object --%>
						
						
						<% for(int i=0; i<noOfItems; i++)
						{
							//Variables required to set the id of each row.It is used in expandOrderItemsInArray() Js function for expand/collapse purpose.
							String switchDefinedArray = "switchdefineArray" + rowNumber + "_array" + arrayRowCounter;
							String dataDefinedArray = "dataDefinedArray" + rowNumber + "_array" + arrayRowCounter;
							
							String requestForArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_requestFor)"; 
							String assignQtyArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_assignedQty)"; 
							String assignStatusArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus)"; 	
							String descriptionArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_description)";
							String instanceOfArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_instanceOf)";
							String orderItemIdArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_orderItemId)";
							String specimenIdInMapArray = "value(DefinedArrayDetailsBean:"+rowNumber+"_specimenId)";	

							String requestedQuantity = "value(DefinedArrayDetailsBean:"+rowNumber+"_requestedQuantity)";
							String rqstdItem = "value(DefinedArrayDetailsBean:"+rowNumber+"_requestedItem)";					
							String avaiQty = "value(DefinedArrayDetailsBean:"+rowNumber+"_availableQuantity)";
							String spClass = "value(DefinedArrayDetailsBean:"+rowNumber+"_className)";
							String spType = "value(DefinedArrayDetailsBean:"+rowNumber+"_type)";
							
							//String distributedItemId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_distributedItemId";
							String specimenList = "requestFor(RequestForDropDownListArray:"+rowNumber+")";
							String specimenCollGrpId = "value(DefinedArrayDetailsBean:"+rowNumber+"_specimenCollGroupId)";
							String actualSpecimenType = "value(DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenType)";
							String actualSpecimenClass = "value(DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenClass)";
						
							boolean disableArrayOrderItemRow = false;

							if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) 								
							{
								//disableArrayOrderItemRow = true;
						%>
						<!-- <html:hidden name="requestDetailsForm" property="<%=assignStatusArray%>" /> 
						<html:hidden name="requestDetailsForm" property="<%= requestForArray %>" />
						<html:hidden name="requestDetailsForm" property="<%= descriptionArray %>" />  -->
						
						<%
							}

							//This is to update available qty for the specimen selected from requestFor drop down.
							String updateAvaiQtyForItemInArray = "avaiQty" + rowNumber+"A";

							String requestForIdInArray = "requestFor" + rowNumber+"A";
							String onChangeValueForRequestForInArray = "updateQuantity('"+ requestForIdInArray  +"')";
							if(((requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))) != null && !((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))).equals(""))
							{
						%>
								<html:hidden name="requestDetailsForm" property="<%= specimenIdInMapArray %>"  />
						  <%}
				 		    else
						    {%>
						    <html:hidden name="requestDetailsForm" property="<%= specimenCollGrpId %>" />
							<%}	%>
						<tr class="dataRowLight" id="<%=dataArray%>">
							<html:hidden name="requestDetailsForm" property="<%=requestedQuantity %>"/>
							<html:hidden name="requestDetailsForm" property="<%=rqstdItem %>"/>
							<html:hidden name="requestDetailsForm" property="<%=avaiQty %>"/>
							<html:hidden name="requestDetailsForm" property="<%=spClass %>"/>
							<html:hidden name="requestDetailsForm" property="<%=spType %>"/>
							<html:hidden name="requestDetailsForm" property="<%= actualSpecimenClass %>" />	
							<html:hidden name="requestDetailsForm" property="<%= actualSpecimenType %>" />	
									<td class="dataCellText" width="3%">
						 					<a id="<%=switchDefinedArray%>" style="text-decoration:none" href="javascript:expandOrderItemsInArray('<%=rowNumber%>','<%=arrayRowCounter%>');">  
											<img src="images/nolines_plus.gif" border="0"/>
								 	</td>
										 	
								 	<td class="dataCellText" width="20%">
						 			<% 
									   if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Derived"))
									   {
									%>			
										<%-- Display derivative icon for child specimens --%>								
										<img src="images\Distribution.GIF" border="0"/>
									<% }
									   else if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological")
												|| ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Pathological"))
										{%>
											<img src="images\Participant.GIF" border="0"/>
										<%}String toolTipTypeClass = "Class:"+((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenClass")))+", Type:"+((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenType"))); %>
							 		<span title="<%= toolTipTypeClass %>">									
										<bean:write name="requestDetailsForm" property="<%=rqstdItem %>" />
									</span>
								 	</td>
								 	
								 	<%
									 		//if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Existing"))
									 		//{
									 		//	disableArrayOrderItemRow=true;
									 		//}
									%>
								 	<td class="formField">
								 	<%if(!((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Existing"))
									 			{%>
								 			<html:select property="<%= requestForArray %>" name="requestDetailsForm" styleClass="formFieldSized10" styleId="<%= requestForIdInArray %>" onchange="<%= onChangeValueForRequestForInArray %>"				 								
												disabled="<%= disableDefineArray %>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" > 
											 	<html:optionsCollection property="<%=specimenList %>" name="requestDetailsForm" label="name" value="value"/>
											</html:select>
		
											<bean:define id="specimenTypeInArray" name="requestDetailsForm" property="<%=spType %>" type="java.lang.String"/>
											<bean:define id="specimenClassInArray" name="requestDetailsForm" property="<%=spClass %>" type="java.lang.String"/>
											<!-- bean:define id="specimenObjIdInArray" name="requestDetailsForm" property="<%=specimenIdInMapArray %>" type="java.lang.String"/-->
											<!-- bean:define id="specimenCollGrpIdInArray" name="requestDetailsForm" property="<%=specimenCollGrpId %>" type="java.lang.String"/-->
											<%
												String urlInArray = "";
												if(((requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))) != null && !((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))).equals(""))
												{
								 					String specimenObjIdInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId")));
													urlInArray = "ShowFramedPage.do?pageOf=pageOfSpecimenTree&" + Constants.PROPERTY_NAME + "=" + requestForArray + "&" + Constants.SPECIMEN_TYPE + "=" + specimenTypeInArray + "&" + Constants.SPECIMEN_CLASS +"=" + specimenClassInArray + "&" + Constants.SPECIMEN_TREE_SPECIMEN_ID + "=" + specimenObjIdInArray;
													//session.setAttribute(Constants.SPECIMEN_TREE_SPECIMEN_ID,specimenObjId);
													%>
													
												<%}
										 		else
												{
										 			String specimenCollGrpIdInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenCollGroupId")));
													urlInArray = "ShowFramedPage.do?pageOf=pageOfSpecimenTree&" + Constants.PROPERTY_NAME + "=" + requestForArray + "&" + Constants.SPECIMEN_TYPE + "=" + specimenTypeInArray + "&" + Constants.SPECIMEN_CLASS + "=" + specimenClassInArray + "&" + Constants.SPECIMEN_TREE_SPECCOLLGRP_ID + "=" + specimenCollGrpIdInArray;
										 			//session.setAttribute(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID,specimenCollGrpId);
										 			%>
		 										
												<%}	
												String appletWindow = "";
												if(disableArrayOrderItemRow == false)
													appletWindow = "javascript:NewWindow('"+urlInArray+"','name','375','330','yes');return false";
											%>
											<a href="#" onclick=" <%= appletWindow %>">
												<img src="images\Tree.gif" border="0" width="24" height="18" title="<%= ApplicationProperties.getValue("requestdetails.tooltip.specimenTreeTooltip") %>">
											</a>
											<!-- Displaying add new link if specimen does not exist -->
												<% if(((requestDetailsForm.getRequestFor("RequestForDropDownListArray:"+rowNumber)) == null) || (((List)(requestDetailsForm.getRequestFor("RequestForDropDownListArray:"+rowNumber))).size() == 0))
													{
														if((((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological")) || (((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Pathological")))
												 		{												 			
													%>
														 <a href="createSpecimenFromOrder.do?rowNumber=<%=rowNumber%>&bean=definedArray">
														 	<bean:message key="orderingSystem.label.create" />
														</a> 
												  <%	}
														else
														{ %>
														<a href="createDerivedSpecimen.do?rowNumber=<%=rowNumber%>&bean=definedArray">
														 <!-- OrderingSystemAddNew.do?addNewForwardTo=deriveSpecimen&forwardTo=orderDetails&addNewFor=specimenId -->
															<bean:message key="orderingSystem.label.create" />
														</a> 
												      <%}
												    }
											}
								 			else
								 			{%>
									    		<span title="<%= toolTipTypeClass %>">									
													<bean:write name="requestDetailsForm" property="<%=rqstdItem %>" />
												</span>
									      <%}%> 
							 		</td>
								 	<td class="dataCellText" width="10%">
									 		<bean:write name="requestDetailsForm" property="<%= spClass %>" />, <bean:write name="requestDetailsForm" property="<%= spType %>" />
									 </td>
								 	<%
									 	//if(!(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)))
									 	//{
									 	//	disableArrayOrderItemRow=false;												
									 	//}
									 	String orderItemClassNameInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_className")));
									 	String orderItemtypeInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_type")));
									 %>
								 	
								 	<td class="dataCellText" width="13%">
								 			<bean:write name="requestDetailsForm" property="<%=requestedQuantity %>" />
								 			<span>		
												<script>
													var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
													document.write(orderItemUnitInArray);
												</script>
											</span>
								 	</td>
								 	
								 	<td class="dataCellText" width="10%">
									 		<div id="<%=updateAvaiQtyForItemInArray%>">
									 			<bean:write name="requestDetailsForm" property="<%=avaiQty %>" />
									 		</div>
										 		<span>		
													<script>
														var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
														document.write(orderItemUnitInArray);
													</script>
												</span>	
											
							 		</td>
							 		<%
										//String assignedQty = defineArrayDetailsBeanObj.getAssignedQty();
									%>
							 		<!-- td class="dataCellText" width="10%">
							 				<html:text name="requestDetailsForm" styleClass="formFieldSized3" maxlength="4"  property="<%= assignQtyArray %>"  disabled="<%= disableArrayOrderItemRow %>" />
							 				<span>		
												<script>
													var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
													document.write(orderItemUnitInArray);
												</script>
											</span>	
								 	</td-->
								 	<td class="dataCellText" width="30%">
							 				<html:select property="<%=assignStatusArray%>" name="requestDetailsForm" styleClass="formFieldSized15"  
								 				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableDefineArray %>" ><!-- disabled="<%= disableArrayOrderItemRow %>" -->
								 			 	<html:options collection="<%=Constants.ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY%>" labelProperty="name" property="value"/>											 				   
											</html:select>
								 	</td>
							</tr>
								<!-- Block to display the expanded/collapsed row starts here-->
							<tr id="<%=dataDefinedArray%>" style="display:none">
								<td colspan='8'>
								   	<table class="rowExpansionTable" width="100%"> 
									   		<tr>
									   			<!--td width="15%" nowrap>
									   				<label for="LabelType">
									   					<bean:message key="specimen.type" />
									   				</label> : <bean:write name="requestDetailsForm" property="<%=actualSpecimenClass %>" />
									   			</td-->
									   			<td width="10%" nowrap>
									   				<label for="LabelDescription">
									    				<bean:message key="orderingSystem.requestdetails.label.description" />
									   				</label> : 				
									   			</td>
												<td rowspan='2' width="60%" nowrap>
												 
													<html:textarea name="requestDetailsForm" styleId="description" styleClass="formFieldSized2"  property="<%= descriptionArray %>" cols='60' rows='2' disabled="<%= disableDefineArray %>"/>
												</td>
									   		</tr>
									   		<!-- tr>
									   			<td width="15%" nowrap>
									   				<label for="LabelSubType">
									    				<bean:message key="specimen.subType" />
									   				</label> :  <bean:write name="requestDetailsForm" property="<%=actualSpecimenType %>" />
									   			</td>									   												   			
									   		</tr-->	
								   	</table>									   		
								</td> 
							</tr>
								<!-- Block to display the expanded/collapsed row ends here-->
							 <% 
									//String orderItemIdInArray = defineArrayDetailsBeanObj.getOrderItemId();
								  	//String instanceOfObjInArray = defineArrayDetailsBeanObj.getInstanceOf();
								  	//String specimenIdInArray = defineArrayDetailsBeanObj.getSpecimenId();
							%>
							<html:hidden name="requestDetailsForm" property="<%= instanceOfArray %>" />
							<html:hidden name="requestDetailsForm" property="<%= orderItemIdArray %>"  />
							

							
							<% rowNumber++;
							}
							%>
						
							<!-- Create Array Button -->								
						<tr id="<%=btnCreateArrayId%>">
							<td colspan='7'>
								<table width="100%" style="border-bottom:1px solid #5C5C5C;">
									<tr> 
										<td align="right">
										<% if((((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_createArrayButtonDisabled"))).equals("false")))
											{
										 %>
											<input type="button" id="btnCreateArray" name="btnCreateArray" class="actionButton" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>')" />
										<%  }else{
										%>
											<input type="button" id="btnCreateArray" name="btnCreateArray" class="actionButton" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>')" disabled/>
										<% }
											String defineArrayName = "defineArrayName_" + arrayRowCounter; 
											String nameOfArray =((String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_arrayName")));
										%>
											<input type="hidden" name="defineArrayName" id="<%=defineArrayName%>" value="<%=nameOfArray%>" />
											<html:hidden name="requestDetailsForm" property="<%= createArrayCondition %>"  />
											<!-- html:hidden name="requestDetailsForm" styleId="<%=defineArrayName%>" property="<%=requestedItem%>"  /-->
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
							String existingRequestedQty = "value(ExistingArrayDetailsBean:"+rowCounter+"_requestedQuantity)";
							String existingAssignedQty = "value(ExistingArrayDetailsBean:"+rowCounter+"_assignedQuantity)";				
							String description = "value(ExistingArrayDetailsBean:"+rowCounter+"_description)";
							
							String requestedItem = "value(ExistingArrayDetailsBean:"+rowCounter+"_bioSpecimenArrayName)";
							String distributedItemId = "value(ExistingArrayDetailsBean:"+rowCounter+"_distributedItemId)";
							//To assign id to select element
							String existingArraySelectId = "existingArray_" + rowCounter;
							boolean disableExistingArrayOrderItem = false;
					%>
					
					<!-- Block to display Existing Array Data -->
				 	<tr class="dataRowLight">
				 			<%if((((String)(requestDetailsForm.getValue("ExistingArrayDetailsBean:"+rowCounter+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
									&& (!((String)(requestDetailsForm.getValue("ExistingArrayDetailsBean:"+rowCounter+"_distributedItemId"))).trim().equals("")))
							{
								disableExistingArrayOrderItem = true;
							%>
							<html:hidden name="requestDetailsForm" property="<%=assignedStatus%>"  />
							<html:hidden name="requestDetailsForm" property="<%= description %>" />							
							<%
							}
					 		%>
					 		
					 		<td class="dataCellText" width="20%" nowrap style="border-left:1px solid #5C5C5C;">
					 			<!-- bean:write name="existingArrayRequestObj" property="bioSpecimenArrayName" /-->
					 			<bean:write name="requestDetailsForm" property="<%=requestedItem %>" />
	 				 		</td>
							
							<td class="dataCellText" nowrap width="10%">
	 						<%
							  //If Existing Array,then,display 'NA' in the textbox for requested qty
											if(((String)(requestDetailsForm.getValue("ExistingArrayDetailsBean:"+rowCounter+"_requestedQuantity"))).equals("0.0"))
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
								<!-- bean:write name="existingArrayRequestObj" property="requestedQuantity" /-->
								<bean:write name="requestDetailsForm" property="<%=existingRequestedQty %>" />&nbsp;&nbsp;
								<%=Constants.UNIT_CN%>
							<%
											}
							%>
							</td>
							
							<td class="dataCellText" nowrap width="10%">
							<%
							  //If Existing Array,then,display 'NA' in the textbox for assign qty
											if(((String)(requestDetailsForm.getValue("ExistingArrayDetailsBean:"+rowCounter+"_requestedQuantity"))).equals("0.0"))
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
						 		<html:textarea name="requestDetailsForm" styleId="description" styleClass="formFieldSized10" property="<%= description %>" cols='60' rows='2' readonly="true"/>
					 		</td>
					 		
					 		<td class="dataCellText" width="15%">
					 			<html:textarea name="requestDetailsForm" styleClass="formFieldSized10" cols="60" rows="2" styleId="addDescription" property="<%=addDescription%>" disabled="<%=disableExistingArrayOrderItem%>"/>
					 		</td>
					 		
					 		<td class="dataCellText" width="25%">
					 			<html:select property="<%=assignedStatus%>" name="requestDetailsForm" styleClass="formFieldSized15" styleId="<%=existingArraySelectId%>" 
								 			disabled="<%=disableExistingArrayOrderItem%>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								 	<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
								</html:select>
					 		</td>
		
					 		<html:hidden name="requestDetailsForm" property="<%= existingArrayId %>"  />
					 		<html:hidden name="requestDetailsForm" property="<%= existingArrayOrderItemId %>"  />
					 		<html:hidden name="requestDetailsForm" property="<%= distributedItemId %>" />
					 		<html:hidden name="requestDetailsForm" property="<%= requestedItem %>" />
					 		<html:hidden name="requestDetailsForm" property="<%= existingRequestedQty %>" />
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