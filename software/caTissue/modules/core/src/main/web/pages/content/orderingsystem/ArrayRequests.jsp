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
<table cellpadding="0" cellspacing="0" border="0" width="100%" >
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	 <tr>
	 <td>
		<table border="0" width="100%" cellspacing="0" cellpadding="3">
			
		<%
			//Retrieve List from request attribute for order items in defined arrays
			List definedArrayRequestMapList = new ArrayList();
			if(request.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST)!= null)
				definedArrayRequestMapList = (ArrayList)request.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
			else
				definedArrayRequestMapList = (ArrayList)session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);	
			if(definedArrayRequestMapList != null)
			{
				int arrayRowCounter = 0;
				int assignStatusArraycount = 0;
				session.setAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST,definedArrayRequestMapList);
		%>
		<%							
							int rowNumber =0 ;
							String disabledCreateButton="true";
		%>
			<!-- Iterate the list -->
		<logic:iterate id="defineArrayMap" collection="<%=definedArrayRequestMapList%>" type="java.util.Map">
			<% if(rowNumber == 0)
				{
			%>
				<tr>
								<td class="tr_bg_blue1" align="left" ><span class="blue_ar_b" >
									<label for="existingArray">&nbsp;
												New Array
									</label>
									</span>
									</td>
									<td align="right" class="tr_bg_blue1" colspan="5">
									&nbsp;
									
								</td>
					</tr>
					<tr>
					<td class="bottomtd"></td>
					</tr>
					<tr>
					
						
						
							<td class="tableheading" width="3%">&nbsp;</td>
							<td class="tableheading" width="25%">
								<strong>
								<bean:message key="orderingsystem.label.defineArrayName" />
								</strong>
							</td>
							<td class="tableheading" width="15%">
								<strong>
								<bean:message key="orderingSystem.tableheader.label.dimensions" />
								</strong>
							</td>
							<td class="tableheading" width="15%">
								<strong>
								<bean:message key="query.class" />
								</strong>
							</td>
							<td class="tableheading" width="15%">
							<strong>
								<bean:message key="specimen.subType" />
								</strong>
							</td>
							<td class="tableheading" width="27%">
							<strong>
								<bean:message key="orderingSystem.tableheader.label.arrayStatus" />
								</strong>
							</td>
							</tr>
						
		 <%}
				Set defineArraySet = defineArrayMap.keySet();
		%>
				<tbody id="arrayTbody">
			<logic:iterate id="definedArrayRequestBean" collection="<%=defineArraySet%>" type="edu.wustl.catissuecore.bean.DefinedArrayRequestBean">
		<%
					String mainRowStyle="tabletd1";
							if(rowNumber%2 == 0)
							{
								mainRowStyle="black_ar";
							}
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
					assignStatusArraycount=assignStatusArraycount+noOfItems;
					String arrayId = "array_" + arrayRowCounter;
					boolean disableDefineArray = false;
		 %>

		 <!-- Block to display Defined Array Name,Dimension,Class and type -->
		 <tr>
						<td colspan="6" class="bottomtd"></td>
					</tr>
			<tr>
							
							<td align="center" class="<%=mainRowStyle%>">
							<%
									String switchArray = "switchArray" + arrayRowCounter; 
									String dataArray = "dataArray" + arrayRowCounter; 
									String headerArray = "headerArray" + arrayRowCounter;
									String btnCreateArrayId = "btnCreateArray" + arrayRowCounter;
									String buttonCreateArrayId ="buttonCreateArrayId"+arrayRowCounter;
									

									//String orderItemInBean = definedArrayRequestBean.getOrderItemId();
									//String arrayIdInBean = definedArrayRequestBean.getArrayId();
							%>
								<html:hidden name="requestDetailsForm" property="<%=orderItemIdInDefineArray%>" /> 
								<html:hidden name="requestDetailsForm" property="<%=definedArrayId%>" /> 
								<a id="<%=switchArray%>" style="text-decoration:none" href="javascript:switchDefinedArrayBlock('<%=arrayRowCounter%>');" >
									<img src="images/uIEnhancementImages/maximize.png" alt="Expand" height="16" width="16" border="0" title="Show Array Detail" />
								</a>
							</td>

							<td class="<%=mainRowStyle%>" >
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
							<td class="<%=mainRowStyle%>" >
							<!--	<label for="dimensions">
									<bean:message key='orderingSystem.tableheader.label.dimensions'/> : 
								</label>-->
								 <span class="black_ar">
									 <%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_oneDimensionCapacity"))%>,<%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_twoDimensionCapacity"))%>
								</span>
								
							</td>		
							<td class="<%=mainRowStyle%>" >
							<!--	<label for="class">
									<bean:message key='orderingSystem.tableheader.label.class' /> :
								</label>-->
								<span class="black_ar">
									<%=(String)(requestDetailsForm.getValue("DefinedArrayRequestBean:"+arrayRowCounter+"_arrayClass"))%>
								</span>
							</td>
							<td class="<%=mainRowStyle%>" >
							<!--	<label for="type">
									<bean:message key='orderingSystem.tableheader.label.type' /> :
								</label>-->
								<span class="black_ar">
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
							<td class="<%=mainRowStyle%>" >
								<!--<bean:message key='orderingSystem.tableheader.label.arrayStatus' />:-->
								<html:select property="<%= arrayStatus %>" name="requestDetailsForm" styleClass="formFieldSized12" styleId="<%=arrayId%>" 				 								
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableDefineArray %>"> 
									<html:options collection="<%=Constants.ITEM_STATUS_LIST%>" labelProperty="name" property="value"/>											 				   
								</html:select>
							</td>
							
					</tr>
					<tr>
						<td colspan="6" class="bottomtd"></td>
					</tr>
		 		
		 
		 <!-- Block to display the table headings -->
		 <tr id="<%=headerArray%>" style="display:none">
		 	<td colspan="6">
		 		<table cellpadding="3" cellspacing="0" border="0" width="100%" >
		 		  <tr>
			 		 	<td class="tableheading" scope="col" width="50%" align="center" valign="top" colspan='5'>
							<strong>
								 	<bean:message key="requestdetails.header.RequestedSpecimenDetails" />
							</strong>
								 	</td>
								 	
									<!-- th class="dataTableHeader" scope="col" align="left" width="10%" rowspan="2" scope="col">
										<bean:message key='requestdetails.datatable.label.AssignQty'/>
									</th-->
								
									<td class="tableheading" scope="col" align="left" width="25%" rowspan="2" valign="top" >
										<strong>
										<bean:message key='requestdetails.datatable.label.AssignStatus'/>
										</strong>
									</td>
									<td class="tableheading" width="25%" scope="col" align="left" rowspan="2" valign="top">
									<strong>
										<bean:message key="requestdetails.header.label.Comments" />
									</strong>
									</td>
								 </tr>
								 <tr>	
									<td class="subtd" scope="col" align="left" width="18%" colspan='2'>
										<bean:message key='requestdetails.datatable.label.RequestItem'/>
									</td>
								
									
									<td class="subtd" scope="col" align="left" width="12%" nowrap="nowrap"
									>
										<bean:message key='orderingSystem.tableheader.label.type'/>,
										<bean:message key='requestdetails.datatable.label.RequestedQty'/>
									</td>

									<td class="subtd" scope="col" align="left" width="16%">
										<bean:message key='requestdetails.datatable.label.RequestFor'/>
									</td>
								
									<td class="subtd" scope="col" align="left" width="4%">
										<bean:message key='requestdetails.datatable.label.AvailableQty'/>
									</td>
		 		  </tr>
		
			
					<!-- Block to display the order items in the array-->			
					<%-- Iterate the list for each definedArrayBean Object --%>
						
						
						<% for(int i=0; i<noOfItems; i++)
						{
							String rowStyle="tabletd1";
							if(i%2 == 0)
								rowStyle="black_ar";

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
							String assignStatusArrayId = "value(DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus)"+arrayRowCounter; 
							

							if(!((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) 								
							{
								disabledCreateButton="false";
								

							}
							%>


						
							
						<%	//This is to update available qty for the specimen selected from requestFor drop down.
							String updateAvaiQtyForItemInArray = "avaiQty" + rowNumber+"A";

							String requestForIdInArray = "requestFor" + rowNumber+"A";
							String onChangeValueForRequestForInArray = "updateQuantity('"+ requestForIdInArray  +"')";
							String changeCreateButtonStatus = "changeCreateButtonStatus('"+ noOfItems +"','"+arrayRowCounter+"','"+assignStatusArraycount+"')";
							if(((requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))) != null && !((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_specimenId"))).equals(""))
							{
						%>
								<html:hidden name="requestDetailsForm" property="<%= specimenIdInMapArray %>"  />
							
						  <%}
				 		    else
						    {%>
						    <html:hidden name="requestDetailsForm" property="<%= specimenCollGrpId %>" />
							<%}	%>
						<tr id="<%=dataArray%>">
							<html:hidden name="requestDetailsForm" property="<%=requestedQuantity %>"/>
							<html:hidden name="requestDetailsForm" property="<%=rqstdItem %>"/>
							<html:hidden name="requestDetailsForm" property="<%=avaiQty %>"/>
							<html:hidden name="requestDetailsForm" property="<%=spClass %>"/>
							<html:hidden name="requestDetailsForm" property="<%=spType %>"/>
							<html:hidden name="requestDetailsForm" property="<%= actualSpecimenClass %>" />	
							<html:hidden name="requestDetailsForm" property="<%= actualSpecimenType %>" />	
							<html:hidden name="requestDetailsForm" property="<%=assignStatusArray%>"  />
							
									
										 	
								 	<td class="<%=rowStyle%>" colspan="2">						 			<% 
									   if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Derived"))
									   {
									%>			
										<%-- Display derivative icon for child specimens --%>								
										<img src="images/Distribution.GIF" border="0"/>
									<% }
									   else if(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological")
												|| ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Pathological"))
										{%>
										 <img src="images/Participant.GIF" border="0"/>
										<%}String toolTipTypeClass = "Class:"+((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenClass")))+", Type:"+((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_actualSpecimenType"))); %>
							 		<span title="<%= toolTipTypeClass %>">									
										<bean:write name="requestDetailsForm" property="<%=rqstdItem %>" />
									</span>
								 	</td>

									<%
									 	//if(!(((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_assignedStatus"))).trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)))
									 	//{
									 	//	disableArrayOrderItemRow=false;												
									 	//}
									 	String orderItemClassNameInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_className")));
									 	String orderItemtypeInArray = ((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_type")));
									 %>
								 	
								 	
								 	
									<td class="<%=rowStyle%>" >
									 		<bean:write name="requestDetailsForm" property="<%= spClass %>" />
									
								 			<bean:write name="requestDetailsForm" property="<%=requestedQuantity %>" />
								 			<span>		
												<script>
													var orderItemUnitInArray = getUnit('<%= orderItemClassNameInArray %>','<%= orderItemtypeInArray %>');
													document.write(orderItemUnitInArray);
												</script>
											</span>
								 	</td>

								 	<td class="<%=rowStyle%>">
								 	<%if(!((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Existing"))
									 			{%>
								 			<html:select property="<%= requestForArray %>" name="requestDetailsForm" styleClass="formFieldSized6" styleId="<%= requestForIdInArray %>" onchange="<%= onChangeValueForRequestForInArray %>"				 								
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
												<img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="24" height="18" title="<%= ApplicationProperties.getValue("requestdetails.tooltip.specimenTreeTooltip") %>">
											</a>
											<!-- Displaying add new link if specimen does not exist -->
												<% if(((requestDetailsForm.getRequestFor("RequestForDropDownListArray:"+rowNumber)) == null) || (((List)(requestDetailsForm.getRequestFor("RequestForDropDownListArray:"+rowNumber))).size() <= 1))
													{
														if((((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological")) || (((String)(requestDetailsForm.getValue("DefinedArrayDetailsBean:"+rowNumber+"_instanceOf"))).trim().equalsIgnoreCase("Pathological")))
												 		{												 			
													%>
														 <a href="createSpecimenFromOrder.do?rowNumber=<%=rowNumber%>&bean=definedArray" class="view">
														 	<bean:message key="orderingSystem.label.create" />
														</a> 
												  <%	}
														else
														{ %>
														<a href="createDerivedSpecimen.do?rowNumber=<%=rowNumber%>&bean=definedArray" class="view">
														 <!-- OrderingSystemAddNew.do?addNewForwardTo=deriveSpecimen&forwardTo=orderDetails&addNewFor=specimenId -->
															<bean:message key="orderingSystem.label.create" />
														</a> 
												      <%}
												    }
											}
								 			else
								 			{%>
									    		<span title="<%= toolTipTypeClass %>" >									
													<bean:write name="requestDetailsForm" property="<%=rqstdItem %>" />
												</span>
									      <%}%> 
							 		</td>
								 	
								 
								 	
								 	<td class="<%=rowStyle%>" >
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
								 	</td   onchange="javascript: var noOfItems='<%=noOfItems%>';changeButtonState(noOfItems,'btnCreateArray');"-->
								 	<td class="<%=rowStyle%>" >
							 				<html:select property="<%=assignStatusArray%>" name="requestDetailsForm" styleClass="formFieldSized11" styleId="<%=assignStatusArrayId%>" 
								 				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="<%= disableDefineArray %>" onchange="<%= changeCreateButtonStatus %>">
								 			 	<html:options collection="<%=Constants.ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY%>" labelProperty="name" property="value"/>	
															
											</html:select>
								 	</td>
									<td class="<%=rowStyle%>">
												 
													<html:textarea name="requestDetailsForm" styleId="description" styleClass="black_ar"  property="<%= descriptionArray %>" cols='20' rows='2' disabled="<%= disableDefineArray %>"/>
									</td>
										
							</tr>
								<!-- Block to display the expanded/collapsed row starts here-->
							
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
							<td colspan='8'>
								<table width="100%" border="0" >
									<tr> 
										<td align="left">
										<% if(disabledCreateButton.equals("false"))
											{
										 %>
											<input type="button" id="<%= buttonCreateArrayId%>" name="btnCreateArray" class="blue_ar_c" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>','<%= rowNumber-noOfItems %>')" disabled="disabled" />
										<%  }else{
										%>
											<input type="button" id="<%= buttonCreateArrayId%>" name="btnCreateArray" class="blue_ar_c" value="Create Array" onClick="gotoCreateArrayPage('<%= arrayRowCounter %>','<%= rowNumber-noOfItems %>')" />
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
		
		<tr><td class="bottomtd" colspan="6"></td></tr>
		<tr onclick="showHide('existingArrayId')">
								<td width="12%" class="tr_bg_blue1" align="left" ><span class="blue_ar_b" >
									<label for="existingArray">&nbsp;
									
										<bean:message key='orderingsystem.label.existingArray'/>
									</label>
									</span>
									</td>
									<td width="88%" align="right" class="tr_bg_blue1" colspan="5">
									<a href="#" id="imgArrow_existingArrayId"><img src="images/uIEnhancementImages/dwn_arrow1.gif" width="80" height="9" hspace="10" border="0" alt="Show Details"/></a>
									
								</td>
							</tr>
		
				<!-- Table to display the data of existing array -->
				<tr>
			<td colspan="6" valign="top" class="showhide1">
						
					
				<!--tbody class="dataTable" id="existingArrayId"-->
				
					<table summary="Display Existing Array Data" cellspacing="0" width="100%" cellpadding="3" border="0" id="existingArrayId" style="display:none">
						 <tr>
						<td class="tableheading" valign="top" scope="col" align="left" width="20%"><strong><bean:message key='orderingsystem.label.arrayName'/></strong></td>
						<td class="tableheading" valign="top" scope="col" align="left" width="10%"><strong><bean:message key='requestdetails.datatable.label.RequestedQty'/></strong></td>
						<td class="tableheading" valign="top" scope="col" align="left" width="10%" nowrap><strong><bean:message key='requestdetails.datatable.label.AssignQty'/></strong></td>
						<td class="tableheading" valign="top" scope="col" align="left" width="17%"><strong><bean:message key='orderingSystem.requestdetails.label.description'/></strong></td>
						<td class="tableheading" valign="top" scope="col" align="left" width="17%"><strong><bean:message key='orderingsystem.arrayRequests.datatable.label.AddDescription'/></strong></td>
						<td class="tableheading" valign="top" scope="col" align="left" width="26%"><strong><bean:message key='requestdetails.datatable.label.AssignStatus'/></strong></td>
						 </tr>
				 	<%
						int rowCounter = 0;
						int rows=0;
						if(request.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST) != null || session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST)!= null)
						{
							List arrayRequestList = new ArrayList();
							
							if(request.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST) != null)
								arrayRequestList = (ArrayList)request.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
							else
								arrayRequestList = (ArrayList)session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
							
							session.setAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST,arrayRequestList);
					%>
				<logic:iterate id="existingArrayRequestObj"	collection="<%=arrayRequestList%>" type="edu.wustl.catissuecore.bean.ExistingArrayDetailsBean">
					<%
							String fontStyle="black_ar";
							rows++;
							if(rows<=rowCounter)
							if(rows%2 == 0)
							{
								fontStyle="tabletd1";
							}
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
							String specimenArrayClickFunction = "showSpecimenArrayDetails("+existingArrayRequestObj.getArrayId()+")";
					%>
					
					<!-- Block to display Existing Array Data -->
				 	<tr>
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
							
					 		
					 		<td class="<%=fontStyle%>">
					 			<!-- bean:write name="existingArrayRequestObj" property="bioSpecimenArrayName" /-->
								<html:link href="#" styleId="label" styleClass="view" onclick="<%=specimenArrayClickFunction%>">
									<bean:write name="requestDetailsForm" property="<%=requestedItem %>" />
								</html:link>
	 				 		</td>
							
							<td class="<%=fontStyle%>" >
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
							
							<td class="<%=fontStyle%>">
							<%
							  //If Existing Array,then,display 'NA' in the textbox for assign qty
											if(((String)(requestDetailsForm.getValue("ExistingArrayDetailsBean:"+rowCounter+"_requestedQuantity"))).equals("0.0"))
											{
							%>
								<html:text property="<%=existingAssignedQty%>" styleClass="black_ar" maxlength="8"  size="5" value="NA" disabled="true"/>
								<html:hidden name="requestDetailsForm" property="<%=existingAssignedQty%>" value="0.0" />
								<%
											}
							  //If Tissue Slides from Block,then,admin assigns the qty
							  //Enable/Disable the textbox depending upon the status
											else
											{	
							%>
								<html:text property="<%=existingAssignedQty%>" styleClass="black_ar" maxlength="8"  size="5" disabled="<%=disableExistingArrayOrderItem%>"/>
								&nbsp;&nbsp;<%=Constants.UNIT_CN%>
							<%
											}
							%>
							</td>
					 		
					 		<td class="<%=fontStyle%>">					 		
						 		<html:textarea name="requestDetailsForm" styleId="description" styleClass="black_ar" property="<%= description %>" cols='25' rows='2' readonly="true"/>
					 		</td>
					 		
					 		<td class="<%=fontStyle%>">
					 			<html:textarea name="requestDetailsForm" styleClass="black_ar" cols="25" rows="2" styleId="addDescription" property="<%=addDescription%>" disabled="<%=disableExistingArrayOrderItem%>"/>
					 		</td>
					 		
					 		<td class="<%=fontStyle%>">
					 			<html:select property="<%=assignedStatus%>" name="requestDetailsForm" styleClass="formFieldSizedNew" styleId="<%=existingArraySelectId%>" 
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
							//session.removeAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
						}
				 %>
				</table> <!-- table with id='existingArrayId' ends here -->
			</td></tr>
		  
	
	 </table> <!-- table5_arrayDataTable ends here -->
	 </td>
	 </tr>
	 </table>
	 