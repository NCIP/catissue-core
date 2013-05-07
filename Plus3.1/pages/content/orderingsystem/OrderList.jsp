<!--OrderList.jsp Which shows the orderd specimens,arrays,pathologyReports-->

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
String requestFromPage=(String)request.getAttribute("requestFromPage");
System.out.println("----------------------");
System.out.println(requestFromPage);
%>


<script>
	//Function to check all checkboxes
	function removeAll(chkBoxId,arrayName)
	{
		var	tbodyId = "tbody_"	+ arrayName;
		var tbodyElement = document.getElementById(tbodyId);

		var rows = new Array();
		if(tbodyElement != null)
		{
		rows = tbodyElement.rows;
		}

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
			action = "<%= Constants.ACTION_SAVE_ORDER_ITEM %>"+"?typeOf=specimen"+"&requestFromPage="+'<%=requestFromPage%>';
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
				switchObj.innerHTML = '<img src="images/uIEnhancementImages/maximize.png" height="16" width="16" border="0"/>';
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
				switchObj.innerHTML = '<img src="images/uIEnhancementImages/minimize.png" height="16" width="16" border="0"/>';
			}
		}

</script>

<html:form action="<%= Constants.ACTION_SAVE_ORDER_ITEM %>" type="edu.wustl.catissuecore.actionForm.OrderSpecimenForm"
			name="OrderList" scope="request">
<div >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		<tr>
			<td class="bottomtd">&nbsp;</td>
		</tr>
		<tr>
             <td class="black_ar">
				<table width="100%" border="0" cellpadding="3" cellspacing="0" class="searchbox">
					<tr>
						<td colspan="2" align="left" class="tr_bg_blue1 blue_ar_b">
							<label for="orderListTitle">
					  			<bean:message key="orderingSystem.header.label.orderList" />
					  		</label>
						</td>
                     </tr>
                     <tr>

			<%
					if(request.getAttribute("OrderPathologyCaseForm")!=null)
			{
					request.setAttribute("typeOf","pathologyCase");
					OrderPathologyCaseForm pathology=(OrderPathologyCaseForm)request.getAttribute("OrderPathologyCaseForm");
			%>
				    <tr>
						<td class="black_ar" colspan="3" align="left" nowrap>
						    <label for="orderListTitle">
								<b><bean:message key="orderingsystem.label.orderRequestName"/>:</b>
									<%=pathology.getOrderForm().getOrderRequestName()%>
					    	</label>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="3" align="left">
							<label for="orderListTitle">
								<b><bean:message key="distribution.protocol"/>:</b>
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
						<td class="black_ar" colspan="3">
						    <label for="orderListTitle">
								<b><bean:message key="orderingsystem.label.orderRequestName"/>:</b>
									<%=form.getOrderForm().getOrderRequestName()%>
					    	</label>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="3">
							<label for="orderListTitle">
								<b><bean:message key="distribution.protocol"/>:</b>
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
						<td class="black_ar" colspan="3">
						    <label for="orderListTitle">
								<b><bean:message key="orderingSystem.header.label.orderRequestName"/>:</b>
									<%=orderSpecimenForm.getOrderForm().getOrderRequestName()%>
							</label>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="3">
							<label for="orderListTitle">
								<b><bean:message key="distribution.protocol"/>:</b>
									<%=orderSpecimenForm.getDistrbutionProtocol()%>
							</label>
				    	</td>
					</tr>
			    <%
				  }
			    %>
					<!-- Block for OrderList Starts here-->
				    <tr>
					    <td align="left" valign="top" class="showhide">
							<table width="100%" border="0" cellspacing="0" cellpadding="4">
                              <tr>
                                <td class="tableheading">
									<input type="checkbox" name="listChkBox" id="listChkBox" onClick="removeAll(this,'None')" />
								</td>
								<td class="tableheading">
									<bean:message key="orderingSystem.orderTable.column.orderRequests" />
								</td>
								<td class="tableheading">
									<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
								</td>
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

				    		  <tr>
								<td align="left" class="black_ar" >
									<html:multibox property="itemsToRemove" value="<%=keyToRemove%>" styleId="<%=checkId%>" onclick="enableRemove()"/>
								</td>
								<td align="left" class="black_ar">
				<%			if(orderSpecimenBeanObj.getTypeOfItem().equals("pathologyCase"))
							{
				%>
									<img src="images/Participant.GIF" border="0"/>
				<%
							}
							else
							{
				%>
				<%				if(orderSpecimenBeanObj.getIsDerived().equals("true"))
								{
				%>
							  		<img src="images/Distribution.GIF" border="0"/>
				<%				}
							}
				%>
									<%=orderSpecimenBeanObj.getSpecimenName()%>
								</td>
								<td align="left" class="black_ar">

				<%if(orderSpecimenBeanObj.getTypeOfItem().equals("specimenArray") &&		orderSpecimenBeanObj.getIsDerived().equals("false"))
					{
				%>
										1
				<%	}
					else
					{
				%>
										<%=orderSpecimenBeanObj.getRequestedQuantity()%>&nbsp;
				<%
					}
				%>
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
			<td nowrap class="dividerline">
		 		<!-- Block to display Array starts here  -->
				<table cellspacing="0" cellpadding="0" border="0" summary="Display the defined array information" width="100%">
					<tr>
						<td>

								<!-- table to display array info Starts-->
								 <table cellspacing="0" cellpadding="3" border="0" summary="Display Defined Array information" width="100%" >
									<tr>
										<td align="left" width="100%" nowrap colspan="2" class="black_ar_b">
											<label for="arrayName" >
												<bean:message key="requesteddetails.arrayName" /><%=defineArrayFormObj.getArrayName()%>
											</label>
										</td>
									</tr>
									<tr>
										<td align="left" nowrap class="black_ar" colspan="2">
											<label for="dimensions">
												<bean:message key="orderingSystem.tableheader.label.dimensions" />:
												<%= defineArrayFormObj.getDimenmsionX() %> , <%= defineArrayFormObj.getDimenmsionY() %>
											</label>
										</td>

									</tr>
									<tr>
										<td align="left" nowrap class="black_ar" colspan="2">
											<label for="type">
												<bean:message key="orderingSystem.tableheader.label.type" />:
												<%= defineArrayFormObj.getArrayTypeName() %>
											</label>
										</td>
									</tr>
									<tr>
										<td align="left" nowrap width="95%" class="black_ar">
											<label for="class">
												<bean:message key="orderingSystem.tableheader.label.class" />:
												<%= defineArrayFormObj.getArrayClass() %>
											</label>
										</td>
										<td align="middle" width="5%" class="black_ar">
												<a id="<%= switchArray %>" style="text-decoration:none" href="javascript:expand('<%=defineArrayFormObj.getArrayName()%>');">
										 		<img src="images/uIEnhancementImages/minimize.png" height="16" width="16" border="0"/>
										</td>
									</tr>
								 </table>
								<!-- table to display array info Ends-->
							</td>
						</tr>
						<tr id="<%= dataArray %>">
							 <td class="showhide">

							<!-- table to display checkbox,speicmen name and count -->
								<table cellspacing="0" cellpadding="3" border="0" summary="Display Specimen Name,Req Qty" width="100%" >
									<tr>
										<td class="tableheading">
											<input type='checkbox' name="arrayChkBox" id="arrayChkBox" onClick="removeAll(this,'<%=defineArrayFormObj.getArrayName()%>')"/>
										</td>
										<td align="left" class="tableheading" nowrap>
											<bean:message key="orderingSystem.tableheader.label.specimenName" />
										</td>
										<td align="left" class="tableheading">
											<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
										</td>
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

												  <tr>
													  	<td>
															<!--input type="checkbox" id="chkidArray" name="chkidArray" /-->
															<html:multibox property="itemsToRemove" value="<%=keyToRemove%>" styleId="<%=checkId%>" onclick="enableRemove()"/>
														</td>

													  	<td align="left" class="black_ar">
														<%if(orderSpecimenBeanObj.getTypeOfItem().equals("pathologyCase"))
														{%>
															<img src="images/Participant.GIF" border="0"/>
														<%}
														else
														{%>
														<%if(orderSpecimenBeanObj.getIsDerived().equals("true"))
														{%>

															<img src="images/Distribution.GIF" border="0"/>
														<%}
														}%>
													  		<%=orderSpecimenBeanObj.getSpecimenName()%>
													  	</td>

													  	<td align="left" class="black_ar">
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
	<td nowrap colspan="3">
		 		<!-- Block to display Array starts here  -->
		 <table cellspacing="0" cellpadding="0" border="0" summary="Display the defined array information" width="100%">
					<tr>
						<td class="dividerline">

								<!-- table to display array info Starts-->
								 <table cellspacing="0" cellpadding="3" border="0" summary="Display Defined Array information" width="100%" >
									<tr>
										<td colspan="2" align="left" class="black_ar_b">
											<label for="arrayName">
												<%=defineArrayFormObj.getArrayName() + " Array"%>
											</label>
										</td>
									</tr>
									<tr>
										<td align="left" nowrap width="20%" class="black_ar" colspan="2">
											<label for="dimensions">
												<bean:message key="orderingSystem.tableheader.label.dimensions" />:
												<%= defineArrayFormObj.getDimenmsionX() %> , <%= defineArrayFormObj.getDimenmsionY() %>
											</label>
										</td>
									</tr>
									<tr>
										<td align="left" nowrap width="80%" class="black_ar" colspan="2">
											<label for="type">
												<bean:message key="orderingSystem.tableheader.label.type" />:
												<%= defineArrayFormObj.getArrayTypeName() %>
											</label>
										</td>
									</tr>
									<tr>

										<td align="left" nowrap width="95%" class="black_ar">
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
										 		<img src="images/uIEnhancementImages/minimize.png" height="16" width="16" border="0"/>
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
					 <td>

							<!-- table to display checkbox,speicmen name and count -->
								<table cellspacing="0" cellpadding="3" border="0" summary="Display Specimen Name,Req Qty" width="100%" >
									  <tr>
											<td class="tableheading">
												<input type='checkbox' name="arrayChkBox" id="arrayChkBox" onClick="removeAll(this,'<%=defineArrayFormObj.getArrayName()%>')"/>
											</td>
											<td align="left" class="tableheading" nowrap>
												<bean:message key="orderingSystem.tableheader.label.specimenName" />
											</td>
											<td align="left" class="tableheading">
												<bean:message key="orderingSystem.orderTable.column.requestQuantity" />
											</td>
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
<tr><td class="bottomtd"></td></tr>
<tr>
  <td class="buttonbg" colspan="3">
		<!-- action buttons begins -->
						<html:button styleClass="blue_ar_b" property="orderButton" onclick="onOrder()" styleId="order" disabled="true">
						  	<bean:message key="orderingSystem.orderListPage.buttons.order"/>
						  </html:button>&nbsp;|&nbsp;<html:button styleClass="blue_ar_c" property="removeButton" onclick="onRemove()" disabled="true" >
									<bean:message key="orderingSystem.orderListPage.buttons.remove"/>
						  </html:button>
						  <script>
							  var temp='<%=enable%>';
							  if(temp=="true")
							  {
								document.OrderList.orderButton.disabled=false;
							  }
						  </script>

		<!-- action buttons end -->
	</td>
</tr>

</table> <!--main table ends -->
</div>
</html:form>