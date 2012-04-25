<!-- dataList and columnList are to be set in the main JSP file -->
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<script language="JavaScript"  type="text/javascript" src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_validation.js">
<script src="dhtmlx_suite/ext/dhtmlxgrid_validation.js" type="text/javascript" charset="utf-8"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlx_suite/ext/dhtmlxgrid_srnd.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
    <script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
    <link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
	<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
	<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid_dhx_skyblue.css">
<script type="text/javascript" src="dhtmlx_suite/gridexcells/dhtmlxgrid_excell_combo.js"></script>

	




<script>
	var columns =${requestScope.jsonData};
	var statusList ="<%=statusValue%>";
	var temp = statusList.split(",");
	function updateGrid(id)
	{
	if(id.value != -1)
	{
	
	var combo = mygrid.getCombo(5);
	
	document.getElementById("nextStatusId").value=id.value;
	for(var row=0;row<mygrid.getRowsNum();row++)
	{
	var canDistribute = "value(RequestDetailsBean:"+row+"_canDistribute)";
	var assignStat = document.getElementById('select_'+row);
		if(id.value == ("Distributed And Close(Special)"))
		{
			var avlQty = mygrid.cellById(row+1,3).getValue();
			var reqQty = mygrid.cellById(row+1,4).getValue();
			//alert('avlQty '+avlQty+'     reqQty  '+reqQty);
			if(avlQty == reqQty)
			{
				mygrid.cellById(row+1,5).setValue("Distributed And Close");
				assignStat.value="Distributed And Close";
			}
			else
			{
				mygrid.cellById(row+1,5).setValue("Distributed");
				assignStat.value="Distributed";
			}
		}
		else
		{
			mygrid.cellById(row+1,5).setValue(id.value);
			assignStat.value=id.value;
		}
	document.getElementById(canDistribute).value="true";
	
	}
	//heckQuantityforAll
	
	}
	}
</script>
<table width="100%" cellpadding="3" cellspacing="0" border="0"	>
	<tr>
	<td>&nbsp;&nbsp;
		<span class="black_ar_b messagetextwarning">
			Note: Click on the 'Requested Qty', 'Status' or 'Comments' column to change the value.
		</span>
	</td>
		<td align="right">
		<span class="black_ar_b">
		Set Status to All:
		</span>
			<html:select property="status" name="requestDetailsForm" styleClass="formFieldSized18" styleId="nextStatusId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="updateGrid(this)">
											<html:options collection="<%= Constants.REQUESTED_ITEMS_STATUS_LIST %>" labelProperty="name" property="value" />		
									     </html:select>&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<script>	
					document.write("<div id='gridbox' width='100%' height='300px' style='background-color:#d7d7d7;overflow:hidden'></div>");
					document.write("<div id='csPagingArea'></div>");
					document.write("<div id='csInfoArea'></div>");    
			</script>
		</td>
	</tr>
</table>

<script>
	
function init_grid()
{			
	var funcName = "rowClick";

	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtmlx_suite/imgs/");
	mygrid.setHeader("Specimen Label,Specimen Class,Specimen Type,Available Qty,Requested Qty,Status,Comments");
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,,,,,"); 
	mygrid.setEditable("true");
	mygrid.enableAutoHeigth(false);
	//mygrid.enablePaging(true, 15, 5, csPagingArea, true, csInfoArea);
    mygrid.setPagingSkin("bricks");

    mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,10,10,10,10,20,*");
	
	mygrid.setColTypes("ro,ro,ro,ro,ed,coro,ed");
	mygrid.setSkin("light");
	
	mygrid.setColSorting("str,str,str,str,str,str,str");
	mygrid.enableMultiselect(true);
	//mygrid.enableValidation(true,true);
	//mygrid.load(columns, "json");
	//mygrid.setColValidators(",,,,ValidNumeric,");
	//mygrid.cells(1,5).setAttribute("validate","ValidNumeric");
	
	
	mygrid.init();
	
	mygrid.load("RequestDetails.do?splvar=gridData","", "json");
	mygrid.attachEvent("onCellChanged", function(rId,cInd,nValue){etcr(rId,cInd,nValue)});
	mygrid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){return qtyChange(stage,rId,cInd,nValue,oValue)});
	
	//mygrid.attachEvent("onRowDblClicked", function(rId,cInd){rowClick(rId,cInd)});
	
 //mygrid.addRow(1,"aa,s,s,s,s",1);
mygrid.enableEditEvents(true);
//mygrid.setHeaderCol(6,"");
	//mygrid.setColumnHidden(6,true);
	//combo.put("aaaa","aaaa");
	
	//alert(mygrid.getAllRowIds());

	//mygrid.cellById(4,5).setValue("New");

//var cel = mygrid.cells(1,4);
//cel.setValue("rrrr");
//cel.setValue("rrrr");
//alert(mygrid.getIndexByValue("Distributed"));
 /*mygrid.forEachRow(
      function callout(row_id) {alert('hh');
         initializeActionCombo(mygrid, row_id);
      }
	  );*/
//alert('fff');
	//mygrid.setSizes();
	document.getElementById('gridStatus').value='grid';
	setComboValues();
}

window.onload=init_grid;

function qtyChange(stage,rId,cInd,nValue,oValue)
{
//alert(stage);
	if(cInd == 4 && stage == 2)
	{
		var avlQty = mygrid.cellById(rId,3).getValue();
		if(nValue > avlQty)
		{
			alert('Requested quantity cannot be greater than available quantity');
//			alert(mygrid.cellById(rId,4).getValue());
			//mygrid.cellById(rId,4).setValue(oValue);
			return false;
		}
		else
		{
			return true;
		}
	}
	else
	return true;
}
function etcr(rId,cInd,nValue)
{
	/*if(cInd == 4)
	{
		var avlQty = mygrid.cellById(rId,3).getValue();
		if(nValue > avlQty)
		{
			alert('Requested quantity cannot be greater than available quantity');
			alert(mygrid.cellById(rId,4).getValue());
			mygrid.cellById(rId,4).setValue(avlQty);
		}
	}*/
	rId = rId-1;
	var canDistribute = "value(RequestDetailsBean:"+rId+"_canDistribute)";
	var assignStat = document.getElementById('select_'+rId);
	assignStat.value=nValue;
	document.getElementById(canDistribute).value="true";
		//mygrid.cellById(row,5).setValue(id.value);
		//alert(mygrid.cellById(row,5).getAttribute('status'));
		//combo.put(temp[row],temp[row]);
	
	checkQuantity(rId);
}
function setComboValues()
{
var combo = mygrid.getCombo(5);
//alert(temp);
 for(var row=0;row<temp.length;row++)
	{
		combo.put(temp[row],temp[row]);
	}
}
function absc(rid)
{
alert(rid);
}
</script>
<% 
									if(requestDetailsList != null)
									{
									    session.setAttribute(Constants.REQUEST_DETAILS_LIST,requestDetailsList);
									 	int i = 0; 
										String rowStatusValue ="";
						 %>
					
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
							String descriptionId = "descriptionId"+i;
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
								<html:hidden name="requestDetailsForm" property="<%= description%>" styleId="<%= descriptionId%>" />
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
								 <html:hidden name="requestDetailsForm" property="<%= requestedQty %>" styleId="<%=requestedQtyId%>" />	
								 <html:hidden name="requestDetailsForm" property="<%= availableQty %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= spClass %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= spType %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= actualSpecimenClass %>" />	
								 <html:hidden name="requestDetailsForm" property="<%= actualSpecimenType %>" />	
								 <html:hidden name="requestDetailsForm" property="<%=canDistribute%>"  styleId="<%=canDistribute%>" />
								
							
									
					<%				
						
										String toolTipTypeClass = "Type:"+((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_actualSpecimenType")))+", Quantity:"+((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_availableQty")).toString()); %>
									 										 		
					
					<%
									
									String className = ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_className")));
									String type = ((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_type")));
					%>
																
					<%
										if((((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Existing")||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological"))&&(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_distributedItemId"))).trim().equals("")))
										{
											disableRow=false;												
										}
										
					%>
										
				
										<html:hidden name="requestDetailsForm" property="<%= requestFor %>" styleId="<%= requestForId %>" />
										
										
										
						
							 	<html:hidden name="requestDetailsForm" property="<%= selectedSpecimenType %>" styleId="<%=selectedSpecimenTypeId%>"/>
																		
									<html:hidden name="requestDetailsForm" property="<%= selectedSpecimenQuantity %>" styleId="<%=availableQtyId%>"/>
										
									<html:hidden name="requestDetailsForm" property="<%= specimenQuantityUnit %>" styleId="<%=specimenQuantityUnitId%>"/>

									<html:hidden name="requestDetailsForm" property="<%= requestedQty %>" styleId="<%=requestedQtyId%>"/>
									
									
									
								
								
	

			   <%
								String consentVerificationStatus=
										((String)(requestDetailsForm.getValue("value(RequestDetailsBean:"+i+"_consentVerificationkey)")));
								String specimenIdValue=((String)(requestDetailsForm.getValue("value(RequestDetailsBean:"+i+"_specimenId)")));
									
				%>
					
							
								
									<html:hidden property="<%=specimenIdInMap%>" styleId="<%=specimenIdInMap%>"  value="<%=specimenIdValue%>"/>
									<html:hidden property="<%=rowStatuskey%>" styleId="<%=rowStatuskey%>"  value="<%=rowStatusValue%>"/>		

											
				<%																							
					        if(
							((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Pathological") ||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("DerivedPathological"))
							{
					
				%>					
								
								<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.NO_CONSENTS%>"/>
							
				<%			}
				%>
									<div style="display:none">
											<a  id="<%=labelStatus%>" class="view" href="javascript:showNewConsentPage('<%=specimenIdInMap%>','<%=labelStatus%>','<%=consentVerificationkey%>','<%=i%>')">
									
										   </a>
										   </div>
				<%																																
							if(((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Derived") ||((String)(requestDetailsForm.getValue("RequestDetailsBean:"+i+"_instanceOf"))).trim().equalsIgnoreCase("Existing"))
							{
				%>

				<%
								if(!disableRow)
								{
				%>	
									<logic:equal name="requestDetailsForm" property="<%=consentVerificationkey%>" value="<%=Constants.NO_CONSENTS%>">
											
											<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.NO_CONSENTS%>"/>
									</logic:equal>
									<logic:notEqual name="requestDetailsForm" property="<%=consentVerificationkey%>" value="<%=Constants.NO_CONSENTS%>">
									
									
									
									<logic:notEmpty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=consentVerificationStatus%>"/>
									</logic:notEmpty>
									<logic:empty name="requestDetailsForm" property="<%=consentVerificationkey%>">
												<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.VIEW_CONSENTS %>"/>
									</logic:empty>
								
									</logic:notEqual>
				<%
								}
								else
								{ 
				%>
										
										<html:hidden property="<%=consentVerificationkey%>" styleId="<%=consentVerificationkey%>"  value="<%=Constants.VERIFIED%>"/>
				<%
								}
				
						}
				%>
									 	
				<html:hidden property="<%=assignStatus%>" styleId="<%=select%>" />
				<html:hidden property="<%=description%>" styleId="<%=descriptionId%>"/>
											
								

								  <!-- Block for row expansion ends here -->
								  <!--  Flag for instanceOf  -->
	
										<html:hidden name="requestDetailsForm" property="<%= instanceOf %>" />
										<html:hidden name="requestDetailsForm" property="<%= orderItemId %>" />
										<html:hidden name="requestDetailsForm" property="<%= distributedItemId %>" />
									 <% i++; %>
					 </logic:iterate>
				<%	 } //End Outer IF 
				%>

