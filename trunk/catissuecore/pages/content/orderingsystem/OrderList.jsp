<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderSpecimenForm" %>
<%@ page import="edu.wustl.catissuecore.bean.OrderSpecimenBean"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DefineArrayForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm"%>



<!-- Include external css and js files-->

<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
<%
String req=null;
if(request.getAttribute("typeOf").equals("specimen"))
	req="specimen";
if(request.getAttribute("typeOf").equals("specimenArray"))
	req="specimenArray";
if(request.getAttribute("typeOf").equals("pathologyCase"))
	req="pathologyCase";
String enable="false";
%>


<script>
//var rowsCount = 0;	
		
	//Function to check all checkboxes
	function removeAll(chkBoxId,arrayName)
	{
		var	tbodyId = "tbody_"	+ arrayName;
		var tbodyElement = document.getElementById(tbodyId);
			
		var rows = new Array();
		rows = tbodyElement.rows;
			
		for(var i=0;i<rows.length;i++)
		{
			var chkId = arrayName + "_" + i;
			document.getElementById(chkId).checked = chkBoxId.checked;
		}
		if(chkBoxId.checked==true && document.OrderList.orderButton.disabled==false)
			document.OrderList.removeButton.disabled=false;
		else
			document.OrderList.removeButton.disabled=true;
	}

	//Store the requested order items in the database.
	function onOrder()
	{
		var action;
		var temp='<%=req%>';
		if(temp=="specimen")
			action = "<%= Constants.ACTION_SAVE_ORDER_ITEM %>"+"?typeOf=specimen";
		if(temp=="specimenArray")
			action = "<%= Constants.ACTION_SAVE_ORDER_ARRAY_ITEM %>"+"?typeOf=specimenArray";
			
		if(temp=="pathologyCase")
			action = "<%=Constants.ACTION_SAVE_ORDER_PATHOLOGY_ITEM %>"+"?typeOf=pathologyCase";
			
		document.OrderList.action = action ;		
	    document.OrderList.submit();    

	}

	//Remove the selected order items from the requested list.
	function onRemove()
	{	
			var action;
			var temp='<%=req%>';
			if(temp=="specimen")
				action = "<%= Constants.ACTION_REMOVE_ORDER_ITEM %>"+"&typeOf=specimen&remove=yes";
			if(temp=="specimenArray")
				action = "<%= Constants.ACTION_REMOVE_ORDER_ITEM_ARRAY %>"+"&typeOf=specimenArray&remove=yes";

			if(temp=="pathologyCase")
				action = "<%= Constants.ACTION_REMOVE_ORDER_ITEM_PATHOLOGYCASE %>"+"&typeOf=pathologyCase&remove=yes";
		
			//action = action + "&removeChkStatus=" + strRemoveToRemove;
			document.OrderList.action = action;		
			document.OrderList.submit();
	}
	
	//to disable and enable "Remove" button
	function enableRemove()
	{
		var cnt=0;
		if(document.OrderList.itemsToRemove.length>1)
		{
			for(var i=0;i<document.OrderList.itemsToRemove.length;i++)
			{
				if(document.OrderList.itemsToRemove[i].checked==true)
				{
					cnt++;
				}
			}
		}
		else
		{
			if(document.OrderList.itemsToRemove.checked==true)
				cnt++;
		}
		if(cnt>0)
			document.OrderList.removeButton.disabled=false;
		else
			document.OrderList.removeButton.disabled=true;

	}
			


		
		//Function to expand and collapse the specimen data block.	
		function expand(arrayName)
		{			
			switchObj = document.getElementById('switch_'+arrayName);
			dataObj = document.getElementById('data_'+arrayName);
		
			if(dataObj.style.display != 'none') //Clicked on - image
			{
				dataObj.style.display = 'none';				
				switchObj.innerHTML = '<img src="images/nolines_plus.gif" border="0"/>';
			}
			else  							   //Clicked on + image
			{
				if(navigator.appName == "Microsoft Internet Explorer")
				{					
					dataObj.style.display = 'block';
				}
				else
				{
					dataObj.style.display = 'table-row';
				}
				switchObj.innerHTML = '<img src="images/nolines_minus.gif" border="0"/>';
			}
		}
	
</script>

<html:form action="<%= Constants.ACTION_SAVE_ORDER_ITEM %>" type="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"
			name="OrderList" scope="request">
<div height="90%">
 <table summary="" width="100%" cellspacing='0' cellpadding='0' border=0 class="tabPage">
  <tr>
	  	<td height="30" nowrap class="formSubTableTitle" colspan="3" style="border-top:1px solid #5C5C5C;" align="left">
	  		<label for="orderListTitle">
	  			<bean:message key="orderingSystem.header.label.orderList" />
	  		</label>  		
	  	</td>
  </tr>

<%
	if(request.getAttribute("OrderPathologyCaseForm")!=null)
	{
	request.setAttribute("typeOf","pathologyCase");
	OrderPathologyCaseForm pathology=(OrderPathologyCaseForm)request.getAttribute("OrderPathologyCaseForm");
%>
    <tr>
		<td height="30" class="formTitle" colspan="3" align="left" nowrap>
		    <label for="orderListTitle">
				<bean:message key="orderingsystem.label.orderRequestName"/>:
				<%=pathology.getOrderForm().getOrderRequestName()%>
	    	</label>
		</td>
	</tr>
	<tr>
		<td height="30" class="formTitle" colspan="3" align="left">
			<label for="orderListTitle">
				<bean:message key="distribution.protocol"/>:
				<%=pathology.getDistrbutionProtocol()%>
			</label>
	  	</td>
   </tr>
<%  }

	else if(request.getAttribute("OrderBiospecimenArrayForm")!=null)
	{
	request.setAttribute("typeOf","specimenArray");
	OrderBiospecimenArrayForm form=(OrderBiospecimenArrayForm)request.getAttribute("OrderBiospecimenArrayForm");
%>
    <tr>
		<td height="30" class="formTitle" colspan="3">
		    <label for="orderListTitle">
				<bean:message key="orderingsystem.label.orderRequestName"/>:
				<%=form.getOrderForm().getOrderRequestName()%>
	    	</label>
		</td>
	</tr>
	<tr>
		<td height="30" class="formTitle" colspan="3">
			<label for="orderListTitle">
				<bean:message key="distribution.protocol"/>:
				<%=form.getDistrbutionProtocol()%>
			</label>
	  	</td>
   </tr>
<%  }
	else
	{
	request.setAttribute("typeOf","specimen");
	OrderSpecimenForm orderSpecimenForm=(OrderSpecimenForm)request.getAttribute("OrderSpecimenForm");
%>
	<tr>
		<td height="30" class="formTitle" colspan="3">
		    <label for="orderListTitle">
				<bean:message key="orderingSystem.header.label.orderRequestName"/>:
				<%=orderSpecimenForm.getOrderForm().getOrderRequestName()%>
			</label>
		</td>
	</tr>
	<tr>
		<td height="30" class="formTitle" colspan="3">
			<label for="orderListTitle">
				<bean:message key="distribution.protocol"/>:
				<%=orderSpecimenForm.getDistrbutionProtocol()%>
			</label>
    	</td>
	</tr>

 
    <%
	  }
    %>
	<!-- Block for OrderList Starts here-->
  <tr>
    <td nowrap style="padding-left=0em;padding-right=0em;">
		<table cellspacing="0" cellpadding="3" border='0' width="100%" summary="Display Specimen name,Requested Qty" class="dataTable">
				<tr>
					<th align="middle" class="dataTableHeader">
						<input type="checkbox" name="listChkBox" id="listChkBox" onClick="removeAll(this,'None')" />
					</th>
					<th align="left" class="dataTableHeader">
						<bean:message key="orderingSystem.orderTable.column.orderRequests" />
					</th>
					<th align="left" class="dataTableHeader">
						<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
					</th>
  				</tr>
		
<%
				
		if((session.getAttribute("RequestedBioSpecimens"))!=null)
		{
			enable="false";
			Map orderItemsMap = new HashMap();
			orderItemsMap = (HashMap) session.getAttribute("RequestedBioSpecimens");
			int i=0;
				if(orderItemsMap.containsKey("None"))
				{		
					String tbodyId = "tbody_None";
	%>
			<tr>
				<td>
					<!-- Block to display order items in the orderlist -->	
						<tbody id="<%=tbodyId%>">
	<%
					int itemIndex=0;				
					List orderItemsList=(List)orderItemsMap.get("None");
					if(orderItemsList.size()>0)
						enable="true";
					if(session.getAttribute("DefineArrayFormObjects") != null)
					{
					 	List defineArrayObjList = (ArrayList)session.getAttribute("DefineArrayFormObjects");					 	
					 	Iterator iter = defineArrayObjList.iterator();
					 	while(iter.hasNext())
					 	{
					 		DefineArrayForm daF = (DefineArrayForm)iter.next();
					 		if(orderItemsMap.containsKey(daF.getArrayName()))
					 		{	
					 			List itemsInArrayList = (ArrayList)orderItemsMap.get(daF.getArrayName());					 			
					 			if(itemsInArrayList.size() < 1)
					 			{
					 				enable = "false";
					 				break;
					 			}
					 		}
					 		else
					 		{
					 			enable = "false";
				 				break;
					 		}					 			
					 	}
					}
					

					for(Iterator listItr = orderItemsList.iterator();listItr.hasNext();)
					{
						OrderSpecimenBean orderSpecimenBeanObj=(OrderSpecimenBean)listItr.next();	
						
						String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
						String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
						String unitRequestedQuantity="value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
						String checkedToRemove = "value(OrderSpecimenBean:"+i+"_checkedToRemove)";
						String isDerived = "value(OrderSpecimenBean:"+i+"_isDerived)";
						String specimenClass = "value(OrderSpecimenBean:"+i+"_specimenClass)";
						String specimenType = "value(OrderSpecimenBean:"+i+"_specimenType)";
						String specimenId = "value(OrderSpecimenBean:"+i+"_specimenId)";
						String description = "value(OrderSpecimenBean:"+i+"_description)";
						String availableQuantity = "value(OrderSpecimenBean:"+i+"_availableQuantity)";
						String arrayName = "value(OrderSpecimenBean:"+i+"_arrayName)";
						String typeOfItem = "value(OrderSpecimenBean:"+i+"_typeOfItem)";
						
						String pathologicalStatus="value(OrderSpecimenBean:"+i+"_pathologicalStatus)";
						String tissueSite="value(OrderSpecimenBean:"+i+"_tissueSite)";
						String specimenCollectionGroup="value(OrderSpecimenBean:"+i+"_specimenCollectionGroup)";
							
	
						String keyToRemove = "None_" + orderSpecimenBeanObj.getSpecimenName() + "_" + new Integer(itemIndex).toString();

						String checkId = "None_" + new Integer(itemIndex).toString();
	%>
 			
				    		  <tr class="dataRowLight">
									<td align="middle" class="dataCellText" width="5%">					
										<!-- input type="checkbox" id="chkid" name="chkid" /-->
										<html:multibox property="itemsToRemove" value="<%=keyToRemove%>" styleId="<%=checkId%>" onclick="enableRemove()"/>
									</td>
									<td align="left" style="background-color:white" class="dataCellText">
										<%if(orderSpecimenBeanObj.getTypeOfItem().equals("pathologyCase"))
										{%>
											<img src="images\Participant.GIF" border="0"/>
										<%}
										else
										{%>
										<%if(orderSpecimenBeanObj.getIsDerived().equals("true"))
										{%>
									  		<img src="images\Distribution.GIF" border="0"/>
										<%}
										}%>
										<%=orderSpecimenBeanObj.getSpecimenName()%>
									</td>
									<td align="left" style="background-color:white" class="dataCellText">
										
									<%if(orderSpecimenBeanObj.getTypeOfItem().equals("specimenArray") && orderSpecimenBeanObj.getIsDerived().equals("false"))			  		{%>	
										1
									<%}
									else
									{%>
										<%=orderSpecimenBeanObj.getRequestedQuantity()%>&nbsp;
									<%}%>
										<%=orderSpecimenBeanObj.getUnitRequestedQuantity()%>

										<html:hidden property="<%=arrayName%>" value="None"/>
										<html:hidden property="<%=isDerived%>" value="<%=orderSpecimenBeanObj.getIsDerived()%>"/>	
										<html:hidden property="<%=specimenName%>" value="<%=orderSpecimenBeanObj.getSpecimenName()%>"/>	
										<html:hidden property="<%=specimenClass%>" value="<%=orderSpecimenBeanObj.getSpecimenClass()%>"/>	
										<html:hidden property="<%=specimenType%>" value="<%=orderSpecimenBeanObj.getSpecimenType()%>"/>	
										<html:hidden property="<%=specimenId%>" value="<%=orderSpecimenBeanObj.getSpecimenId()%>"/>
										<html:hidden property="<%=description%>" value="<%=orderSpecimenBeanObj.getDescription()%>"/>	
										<html:hidden property="<%=availableQuantity%>" value="<%=orderSpecimenBeanObj.getAvailableQuantity()%>"/>
										<html:hidden property="<%=typeOfItem%>" value="<%=orderSpecimenBeanObj.getTypeOfItem()%>"/>	
										<html:hidden property="<%=requestedQuantity%>" value="<%=orderSpecimenBeanObj.getRequestedQuantity()%>"/>
										<html:hidden property="<%=unitRequestedQuantity%>" value="<%=orderSpecimenBeanObj.getUnitRequestedQuantity()%>"/>
										<html:hidden property="<%=pathologicalStatus%>" value="<%=orderSpecimenBeanObj.getPathologicalStatus()%>"/>	
										<html:hidden property="<%=tissueSite%>" value="<%=orderSpecimenBeanObj.getTissueSite()%>"/>
										<html:hidden property="<%=specimenCollectionGroup%>" value="<%=orderSpecimenBeanObj.getSpecimenCollectionGroup()%>"/>
												
									</td>
							 </tr>			
	<%		
						 i++;
						itemIndex++;
					}//End for loop to iterate orderItemsList
	%>
		   			</tbody>
		   		 
			   		<!-- Block to display order items in the orderlist ends here -->		   			
			  </td>
		 </tr> 
	<%
				}//End If(orderItemsMap.containsKey("None"))
	%>		
	   </table>
	</td>
 </tr>	
 	<!-- Block for OrderList Ends here-->
	<%
			//If some array is defined.
				if(session.getAttribute("DefineArrayFormObjects") != null)
				{
				 	List defineArrayFormList = (ArrayList)session.getAttribute("DefineArrayFormObjects");
	%>
		<logic:iterate id="defineArrayFormObj"	collection="<%=defineArrayFormList%>" type="edu.wustl.catissuecore.actionForm.DefineArrayForm">
	<%
						//DefineArrayForm defineArrayFormObj = (DefineArrayForm)defineArrayFormListItr.next();
						String switchArray = "switch_" + defineArrayFormObj.getArrayName();
						String dataArray = "data_" + defineArrayFormObj.getArrayName();
	%>
<tr>
	<td nowrap style="padding-left=0em;padding-right=0em;border-top:1px;">
		 		<!-- Block to display Array starts here  -->		
				<table cellspacing="0" cellpadding="0" border="0" summary="Display the defined array information" width="100%">
				<tr>
						<td height="30">
								
								<!-- table to display array info Starts-->
								 <table cellspacing="0" cellpadding="0" border="0" summary="Display Defined Array information" width="100%" class="defineArrayBlock">
									<tr colspan="2">
										<td height="30" align="left" width="100%" nowrap colspan="2">
											<label for="arrayName">
												<%=defineArrayFormObj.getArrayName() + " Array"%> 
											</label>
										</td>
									</tr>
									<tr>
										<td height="30" align="left" nowrap width="20%">
											<label for="dimensions">
												<bean:message key="orderingSystem.tableheader.label.dimensions" />:
												<%= defineArrayFormObj.getDimenmsionX() %> , <%= defineArrayFormObj.getDimenmsionY() %> 
											</label>
										</td>
										<td height="30" align="left" nowrap width="80%">
											<label for="type">
												<bean:message key="orderingSystem.tableheader.label.type" />:
												<%= defineArrayFormObj.getArrayTypeName() %>
											</label>
										</td>
									</tr>
									<tr>
										<td height="30" align="left" nowrap width="95%">
											<label for="class">
												<bean:message key="orderingSystem.tableheader.label.class" />:
												<%= defineArrayFormObj.getArrayClass() %>
											</label>
										</td>
										<td align="middle" width="5%">
												<a id="<%= switchArray %>" style="text-decoration:none" href="javascript:expand('<%=defineArrayFormObj.getArrayName()%>');">  
										 		<img src="images/nolines_minus.gif" border="0"/>
										</td>
									</tr>
								 </table>
								<!-- table to display array info Ends-->
						</td>
				</tr>	
				<tr id="<%= dataArray %>">
					 <td height="30">
							
							<!-- table to display checkbox,speicmen name and count -->
								<table cellspacing="0" cellpadding="3" border="0" summary="Display Specimen Name,Req Qty" width="100%" class="dataTable">
									  <tr>
											<th class="dataTableHeader">
												<input type='checkbox' name="arrayChkBox" id="arrayChkBox" onClick="removeAll(this,'<%=defineArrayFormObj.getArrayName()%>')"/>
											</th>
											<th align="left" class="dataTableHeader" nowrap>
												<bean:message key="orderingSystem.tableheader.label.specimenName" />
											</th>
											<th align="left" class="dataTableHeader">
												<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
											</th>
									  </tr>
	<%
							//Check if items are added to this array
						if(orderItemsMap.containsKey(defineArrayFormObj.getArrayName()))
						{
							String tbodyId = "tbody_" + defineArrayFormObj.getArrayName();
	%>
								<tr>
									<td>
									  <!--Block to display specimens added to array -->
											 <tbody id="<%=tbodyId%>">
	<%
							int itemIndex=0;
							List orderItemsList=(List)orderItemsMap.get(defineArrayFormObj.getArrayName());
							if(orderItemsList.size()>0)
								enable="true";

							
							for(Iterator listItr = orderItemsList.iterator();listItr.hasNext();)
							{
								OrderSpecimenBean orderSpecimenBeanObj=(OrderSpecimenBean)listItr.next();	
						
								String specimenName = "value(OrderSpecimenBean:"+i+"_specimenName)";
								String requestedQuantity = "value(OrderSpecimenBean:"+i+"_requestedQuantity)";
								String unitRequestedQuantity="value(OrderSpecimenBean:"+i+"_unitRequestedQuantity)";
								String checkedToRemove = "value(OrderSpecimenBean:"+i+"_checkedToRemove)";
								String isDerived = "value(OrderSpecimenBean:"+i+"_isDerived)";
								String specimenClass = "value(OrderSpecimenBean:"+i+"_specimenClass)";
								String specimenType = "value(OrderSpecimenBean:"+i+"_specimenType)";
								String specimenId = "value(OrderSpecimenBean:"+i+"_specimenId)";
								String description = "value(OrderSpecimenBean:"+i+"_description)";
								String availableQuantity = "value(OrderSpecimenBean:"+i+"_availableQuantity)";
								String arrayName = "value(OrderSpecimenBean:"+i+"_arrayName)";
								String typeOfItem = "value(OrderSpecimenBean:"+i+"_typeOfItem)";	
								
								String pathologicalStatus="value(OrderSpecimenBean:"+i+"_pathologicalStatus)";
								String tissueSite="value(OrderSpecimenBean:"+i+"_tissueSite)";
								String specimenCollectionGroup="value(OrderSpecimenBean:"+i+"_specimenCollectionGroup)";

								
								String keyToRemove = defineArrayFormObj.getArrayName() + "_" + orderSpecimenBeanObj.getSpecimenName() + "_" + new Integer(itemIndex).toString();
								String checkId = defineArrayFormObj.getArrayName() + "_" + new Integer(itemIndex).toString();
	%>
									 
												  <tr class="dataRowLight">
													  	<td class="dataCellText">					
															<!--input type="checkbox" id="chkidArray" name="chkidArray" /-->
															<html:multibox property="itemsToRemove" value="<%=keyToRemove%>" styleId="<%=checkId%>" onclick="enableRemove()"/>
														</td>
														
													  	<td align="left" class="dataCellText">
														<%if(orderSpecimenBeanObj.getTypeOfItem().equals("pathologyCase"))
														{%>
															<img src="images\Participant.GIF" border="0"/>
														<%}
														else
														{%>
														<%if(orderSpecimenBeanObj.getIsDerived().equals("true"))
														{%>
															<img src="images\Distribution.GIF" border="0"/>
														<%}
														}%>
													  		<%=orderSpecimenBeanObj.getSpecimenName()%>
													  	</td>
													  	
													  	<td align="left" class="dataCellText">
													  	<%=orderSpecimenBeanObj.getRequestedQuantity()%>&nbsp;
													  	<%=orderSpecimenBeanObj.getUnitRequestedQuantity()%>
													  	</td>
												  </tr>
											  
										<html:hidden property="<%=arrayName%>" value="<%=defineArrayFormObj.getArrayName()%>"/>
										<html:hidden property="<%=isDerived%>" value="<%=orderSpecimenBeanObj.getIsDerived()%>"/>	
										<html:hidden property="<%=specimenName%>" value="<%=orderSpecimenBeanObj.getSpecimenName()%>"/>	
										<html:hidden property="<%=specimenClass%>" value="<%=orderSpecimenBeanObj.getSpecimenClass()%>"/>	
										<html:hidden property="<%=specimenType%>" value="<%=orderSpecimenBeanObj.getSpecimenType()%>"/>	
										<html:hidden property="<%=specimenId%>" value="<%=orderSpecimenBeanObj.getSpecimenId()%>"/>
										<html:hidden property="<%=description%>" value="<%=orderSpecimenBeanObj.getDescription()%>"/>	
										<html:hidden property="<%=availableQuantity%>" value="<%=orderSpecimenBeanObj.getAvailableQuantity()%>"/>
										<html:hidden property="<%=typeOfItem%>" value="<%=orderSpecimenBeanObj.getTypeOfItem()%>"/>	
										<html:hidden property="<%=requestedQuantity%>" value="<%=orderSpecimenBeanObj.getRequestedQuantity()%>"/>
										<html:hidden property="<%=unitRequestedQuantity%>" value="<%=orderSpecimenBeanObj.getUnitRequestedQuantity()%>"/>
										<html:hidden property="<%=pathologicalStatus%>" value="<%=orderSpecimenBeanObj.getPathologicalStatus()%>"/>	
										<html:hidden property="<%=tissueSite%>" value="<%=orderSpecimenBeanObj.getTissueSite()%>"/>
										<html:hidden property="<%=specimenCollectionGroup%>" value="<%=orderSpecimenBeanObj.getSpecimenCollectionGroup()%>"/>

										
	<%
								itemIndex++;
							  i++;
							}//End for
	%>
										</tbody>									 
	 						 		</td>
								</tr>							
	<%	
						}//End if(map contains arrayName)
	%>
					</table>
					<!-- table to display checkbox,speicmen name and qty ends here -->
				  </td>
			  </tr>
		</table>
	<!-- Block to display Array ends here  -->	
    </td>
</tr>	
	</logic:iterate>
	<%
				}//End IF(session.getAttribute("DefineArrayFormObjects") != null)
		}//End if(session.getAttribute("RequestedBioSpecimens"))!=null)
		else  //when no orderitems added,then,display empty defined arrays
		{
			if(session.getAttribute("DefineArrayFormObjects") != null)
			{
				 	List defineArrayFormList = (ArrayList)session.getAttribute("DefineArrayFormObjects");
	%>				
		<logic:iterate id="defineArrayFormObj"	collection="<%=defineArrayFormList%>" type="edu.wustl.catissuecore.actionForm.DefineArrayForm">
  <tr>
	<td nowrap style="padding-left=0em;padding-right=0em;border-top:1px;" colspan="3">
		 		<!-- Block to display Array starts here  -->	
		 <table cellspacing="0" cellpadding="0" border="0" summary="Display the defined array information" width="100%">
					<tr>
						<td height="30">
								
								<!-- table to display array info Starts-->
								 <table cellspacing="0" cellpadding="0" border="0" summary="Display Defined Array information" width="100%" class="defineArrayBlock">
									<tr colspan="2">
										<td height="30" align="left" nowrap width="100%" colspan="2">
											<label for="arrayName">
												<%=defineArrayFormObj.getArrayName() + " Array"%> 
											</label>
										</td>										
									</tr>
									<tr>
										<td height="30" align="left" nowrap width="20%">
											<label for="dimensions">
												<bean:message key="orderingSystem.tableheader.label.dimensions" />:
												<%= defineArrayFormObj.getDimenmsionX() %> , <%= defineArrayFormObj.getDimenmsionY() %> 
											</label>
										</td>
										<td height="30" align="left" nowrap width="80%">
											<label for="type">
												<bean:message key="orderingSystem.tableheader.label.type" />:
												<%= defineArrayFormObj.getArrayTypeName() %>
											</label>
										</td>																				
									</tr>
									<tr>
										
										<td height="30" align="left" nowrap width="95%">
											<label for="class">
												<bean:message key="orderingSystem.tableheader.label.class" />:
												<%= defineArrayFormObj.getArrayClass() %>
											</label>
										</td>
										<td align="middle" width="5%">
										<%
											String switchArray = "switch_" + defineArrayFormObj.getArrayName();
										%>
												<a id="<%= switchArray %>" style="text-decoration:none" href="javascript:expand('<%=defineArrayFormObj.getArrayName()%>');">  
										 		<img src="images/nolines_minus.gif" border="0"/>
										</td>
									</tr>
								 </table>
								<!-- table to display array info Ends-->
						</td>
				</tr>	
				<%
					String dataArray = "data_" + defineArrayFormObj.getArrayName();
				%>
				<tr id="<%= dataArray %>">
					 <td height="30">
							
							<!-- table to display checkbox,speicmen name and count -->
								<table cellspacing="0" cellpadding="3" border="0" summary="Display Specimen Name,Req Qty" width="100%" class="dataTable" >
									  <tr class="dataRowLight">
											<th class="dataTableHeader">
												<input type='checkbox' name="arrayChkBox" id="arrayChkBox" onClick="removeAll(this,'<%=defineArrayFormObj.getArrayName()%>')"/>
											</th>
											<th align="left" class="dataTableHeader" nowrap>
												<bean:message key="orderingSystem.tableheader.label.specimenName" />
											</th>
											<th align="left" class="dataTableHeader">
												<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
											</th>
									  </tr>
								</table>
						</td>
				</tr>
		</table>
	</td>
 </tr>											
		</logic:iterate>
	<%		   	
			 } //End if(session.getAttribute("DefineArrayFormObjects") != null)
		} //End else(when no orderitems added,then,display empty defined arrays)
	%>
<tr>
  <td align="right" class="tabField" colspan="3">
		<!-- action buttons begins -->
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						 <html:button styleClass="actionButton" property="removeButton" onclick="onRemove()" disabled="true" >
									<bean:message key="orderingSystem.orderListPage.buttons.remove"/>
						  </html:button>
					</td>
					<td>
						  <html:button styleClass="actionButton" property="orderButton" onclick="onOrder()" styleId="order" disabled="true">
						  	<bean:message key="orderingSystem.orderListPage.buttons.order"/>
						  </html:button>
						  <script>
							  var temp='<%=enable%>';
							  if(temp=="true")
							  {
								document.OrderList.orderButton.disabled=false;
							  }
						  </script>
					</td>
				</tr>
			</table>
		<!-- action buttons end -->
	</td>		 
</tr>

</table> <!--Table with class=tabPage -->
</div>
</html:form>