<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" isELIgnored="false"%>
<script src="jss/ajax.js"></script>	   
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxtabbar.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxgrid_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css" href="css/alretmessages.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxtabbar.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtabbar_start.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_mcol.js"></script>
<script src="jss/json2.js" type="text/javascript"></script>
<c:set var="tr_white_color" value="tr_alternate_color_white" />
<c:set var="tr_grey_color" value="tr_alternate_color_lightGrey" />
<head>

<script language="javascript">
var tabDataJSON = {};
var tabbar;
var mygrid;
var site_combo;
var distributionProtocolNameCombo;

function processData(obj)
{
	tabDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
}

function submitOrderNew(consentVerifiedValues)
{	
	if(document.getElementById('orderName').value=="")
	{
		alert("Please specify Order Name.");
		return;	
	}	
	if(distributionProtocolNameCombo.getActualValue()=="" || distributionProtocolNameCombo.getActualValue()=="-1")
	{
		alert("Please select distribution protocol.");
		return;
	}
	
	if(site_combo.getActualValue()=="" || site_combo.getActualValue()=="-1")
	{
		alert("Please select distribution site.");
		return;
	}

	for (var i = 0; i < consentVerifiedValues.length; ++i) 
	{
	  if(!consentVerifiedValues[i].checked){
	     alert("Please verify the Consents for the specimen(s)");
	     return;	
   	 }
   	}
	
	tabDataJSON["gridXML"] = mygrid.serialize();
	tabDataJSON["id"]=${requestScope.id};
	tabDataJSON["orderName"]=document.getElementById('orderName').value;
	tabDataJSON["disptributionProtocolId"]=distributionProtocolNameCombo.getActualValue();
	tabDataJSON["disptributionProtocolName"]=distributionProtocolNameCombo.getComboText();
	tabDataJSON["site"]=site_combo.getActualValue();
	tabDataJSON["requestorEmail"]='<bean:write name="DisplayOrderDTO" property="requestorEmail" scope="request"/>';
	tabDataJSON["requestorName"]='<bean:write name="DisplayOrderDTO" property="requestorName" scope="request"/>';
	

	var loader=dhtmlxAjax.postSync("SubmitOrder.do","dataJSON="+JSON.stringify(tabDataJSON));
		
	if(loader.xmlDoc.responseText != null)
	{
		if(loader.xmlDoc.responseText == "Success")
		{
			var numberOfcolumns=mygrid.getColumnsNum();	
			mygrid.setColumnHidden(numberOfcolumns-1,true);
			document.getElementById('error').style.display='none';
			document.getElementById('success').style.display='block';
		}
		else
		{
			var numberOfcolumns=mygrid.getColumnsNum();		
			mygrid.setColumnHidden(numberOfcolumns-1,false);
			
			var obj=JSON.parse(loader.xmlDoc.responseText);
			var orderErrorDTOs=obj['orderErrorDTOs'];
			
			for(var i=0;i<orderErrorDTOs.length;i++)
			{
			   var specimenlabel=orderErrorDTOs[i].specimenLabel;
			   for(var row=0;row<mygrid.getRowsNum();row++)
			   {
				if(specimenlabel == mygrid.cellById(row+1,0).getValue())
				{	
				  mygrid.cellById(row+1,numberOfcolumns-1).setValue(orderErrorDTOs[i].error);
				  mygrid.cellById(row+1,6).setValue(orderErrorDTOs[i].newStatus);	
				  break;
				}
			   }
			}
			document.getElementById('success').style.display='none';
			document.getElementById('error').style.display='block';
			tabbar.setTabActive('SimpleViewDiv');
			mygrid.refreshFilters();

		}
	}
}


	function updateGrid(id)
	{
		if(id.value != -1)
		{
	
		var combo = mygrid.getCombo(5);
	
		document.getElementById("nextStatusId").value=id.value;
		for(var row=0;row<mygrid.getRowsNum();row++)
		{
			if(id.value == ("Distributed And Close(Special)"))
			{
				var avlQty = Number(mygrid.cellById(row+1,3).getValue());
				var reqQty = Number(mygrid.cellById(row+1,5).getValue());
				if(avlQty == reqQty)
				{
					mygrid.cellById(row+1,6).setValue("Distributed And Close");
				}
				else
				{
					mygrid.cellById(row+1,6).setValue("Distributed");
					
				}
			}
			else
			{
				mygrid.cellById(row+1,6).setValue(id.value);
				
			}
		
	
	}
	
	}
	}
function switchToOlderView()
{
	var action="RequestDetails.do?id="+${requestScope.id};
	document.forms[0].action=action;
	document.forms[0].submit();
}
</script>
</head>  
<body onload="loadTab();init_grid();">
<script type="text/javascript" src="jss/wz_tooltip.js"></script>

<html:form action="/SubmitOrder.do">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b">
								<bean:message key="requestdetails.header" />
							</span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Order" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
						  
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" >&nbsp;</td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
    <tr>
			<td><div id="mainTable"style="display:block;"><table width="100%"  border="0" cellpadding="0" cellspacing="0" >
				
    	<tr>
		          <td><div id="error" class="alert alert-error" style="display:none">
    Encountered errors with some of the specimens in the order. Please check the below table for specific error.
</div>
<div class="alert alert-success" id="success" style="display:none">
   Order Submitted Sucessfully.
</div>
</td>
		        </tr>
	
        <tr>
          <td align="left" class="showhide"><table width="100%" border="0" cellspacing="0" cellpadding="2" >
				<tr class="${tr_grey_color}">
					<td width="17%" class="black_ar align_right_style">
						<strong>
						<bean:message key='requestlist.dataTabel.OrderName.label'/>
						</strong>
					</td>

	                <td width="27%" class="black_ar">
	                		<html:text property="orderName" styleClass="black_ar" styleId="orderName" size="49" name="DisplayOrderDTO" onblur="processData(this)" style="width:30em;"/>
					</td>
					<td width="10%" class="black_ar align_right_style"></td>
					<td width="17%" class="black_ar align_right_style"></td>
				<td width="29%" class="black_ar align_right_style"><html:link href="#" styleId="olderViewLink" styleClass="view"   onclick="switchToOlderView()">Switch To Older View</html:link>
				</td>
				</tr>

				<tr class="${tr_white_color}">
					<td class="black_ar align_right_style">
                		<strong>
						<bean:message key='requestlist.dataTabel.DistributionProtocol.label'/>
						</strong>
					</td>
 					<td class="black_ar">
						<html:select property="distributionProtocolName" name="DisplayOrderDTO" styleClass="formFieldSized" styleId="distributionProtocolName" size="100" onblur="processData(this)" style="width:30em;" >
							<html:options collection="distributionProtocolList" labelProperty="name" property="value" />
					       </html:select>
						
					</td>
					<script>
						distributionProtocolNameCombo = dhtmlXComboFromSelect("distributionProtocolName");
						distributionProtocolNameCombo.setSize(330);
						distributionProtocolNameCombo.enableFilteringMode(true);
						if(distributionProtocolNameCombo.getOptionByLabel('${requestScope.DisplayOrderDTO.distributionProtocolName}')==null)
						{
						  distributionProtocolNameCombo.setComboValue("-1");		
						}
						else
						{
						 distributionProtocolNameCombo.setComboValue(distributionProtocolNameCombo.getOptionByLabel('${requestScope.DisplayOrderDTO.distributionProtocolName}').value);
						}
    					</script>
					<td width="10%" class="black_ar align_right_style"></td>						
					<td  class="black_ar align_right_style"><strong>
								<bean:message key='requestdetails.header.label.RequestorName'/>
							</strong>
						</td>
								
			            	<td> 
  
				           <span class="link">
								<a class="view" href='mailto:<bean:write name='DisplayOrderDTO'  property='requestorEmail' scope='request' />' >
									<bean:write name="DisplayOrderDTO" property="requestorName" scope="request"/> 
								</a>
							</span>

						</td>					
              </tr>
            
			<tr class="${tr_grey_color}">
                
					<td class="black_ar align_right_style"><strong>
							 <bean:message key='requestlist.dataTabel.label.Site'/> 
 					</strong></td>
					<td class="black_ar" style="background-color:#f6f6f6;" >
							<div  class="dhtmlx-combo-margin">
							 <html:select property="siteName" name="DisplayOrderDTO" styleClass="formFieldSized" styleId="siteName" size="100" onblur="processData(this)" style="width:30em;">
							 <html:options collection="sites" labelProperty="name" property="value" />
					       </html:select>
					       
							</div>
						 		
					</td>

					<script>
								  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
								  site_combo = new dhtmlXComboFromSelect("siteName");
								  site_combo.setSize(330);
								  site_combo.enableFilteringMode(true);
 							  	  if(site_combo.getOptionByLabel('${requestScope.DisplayOrderDTO.siteName}')==null)
								  {
 							  		site_combo.setComboValue("-1");		
								  }
								  else
								  {
									  site_combo.setComboValue(site_combo.getOptionByLabel('${requestScope.DisplayOrderDTO.siteName}').value);
								  }
    					</script>	
		<td width="10%" class="black_ar align_right_style"></td>
		<td class="black_ar align_right_style"><strong>
								<bean:message key='requestlist.dataTabel.label.RequestDate'/>
							</strong></td>
                <td class="black_ar"> 
					<fmt:formatDate value="${DisplayOrderDTO.requestedDate}" pattern="${datePattern}" />					
						</td>			
              </tr>
			    <tr>
				<td class="black_ar align_right_style"><strong>
							<bean:message key="requestdetails.header.label.DistributorsComments" />
 					</strong></td>
				<td class="black_ar_t">
					<html:textarea name="DisplayOrderDTO" styleClass="black_ar" cols="47" rows="2" styleId="distributorsComment" property="distributorsComment" onblur="processData(this)" style="width:30em;height:40px;"/>
		 			</td>
				<td width="10%" class="black_ar align_right_style"></td>
		 		 <td class="black_ar align_right_style"><strong>
								<bean:message key='requestdetails.requestor.Comments'/> 
								
							</strong></td>
                <td class="black_ar">&nbsp;<html:textarea name="DisplayOrderDTO" styleClass="black_ar" cols="47" rows="2" styleId="comments" property="comments" readonly="true" style="width:30em;height:40px;"/></td>
			   </tr>
          </table></td>
        </tr>
		 </table>
	     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
        <tr>
          <td align="left">
		<!-----------------------table for the tabs------------>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><input type="hidden" id="gridStatus" name="gridStatus"/>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr><td>
						<div id="tabbar_div" align="left" style="width:auto;height:300px;">
						<div id="SimpleViewDiv" style="width:auto;height:300px;">						
						<table width="100%" cellpadding="0" cellspacing="0" border="0"	>
		<tr>
			<td>&nbsp;&nbsp;
				<span class="black_ar_b messagetextwarning">
					Note: Click on the 'Distributed Qty', 'Status' or 'Comments' column to change the value.
				</span>
			</td>
			<td align="right">
				<span class="black_ar_b">
				Set Status to All:
				</span>
				<select class="formFieldSized18" id="nextStatusId" size="1" onchange="updateGrid(this)">					<c:forEach items="${requestScope.itemsStatusList}" var="status" >                  
									<option value="${status.value}">${status.name}</option>                    
	    							</c:forEach>		
				 </select>&nbsp;
			</td>
		</tr>
		
		<tr>
			<td colspan="2" width="100%">
				<script>	
						document.write("<div id='gridbox' width='100%' height='240px' style='background-color:#d7d7d7;overflow:hidden'></div>");
						document.write("<div id='csPagingArea'></div>");
						document.write("<div id='csInfoArea'></div>");    
				</script>
			</td>
		</tr>
		
 <tr>
          <td class="buttonbg" colspan="2">
					<input type="button" class="blue_ar_b" value="Next >>" onclick="goToConsentTabFromNext()" accesskey="Enter">
					<input type="button" class="blue_ar_b" value="Export Order" onclick="exportOrder('${requestScope.id}')" accesskey="Enter">
				
					
			</td>
		</tr>
</table>

			</div>
			<div id="ConsentViewDiv" style="width:auto;height:300px;">
			</div>	
			</div>
			</td></tr>		
			</table>
			</td></tr>
			</table>
			</td>
		</tr>
		</table>
    </td>
  </tr>
</table>
   <!-- main table ends here -->
</html:form>

	<iframe id = "orderExportFrame" width = "0" height = "0" frameborder="0"></iframe>
</body>

<script>
function loadTab()
{
	tabbar = new dhtmlXTabBar("tabbar_div", "top");
	tabbar.setHrefMode("iframes-on-demand");	
	tabbar.setSkin('dhx_skyblue');
	tabbar.setImagePath("dhtmlx_suite/imgs/");
	tabbar.addTab("SimpleViewDiv", "Specimens", "100px");	
	tabbar.addTab("ConsentViewDiv", "Consents", "100px");
	tabbar.enableAutoSize(false,true);
	tabbar.setContent("SimpleViewDiv","SimpleViewDiv");
	tabbar.setContent("ConsentViewDiv","ConsentViewDiv");
	tabbar.setTabActive('SimpleViewDiv');
	
	tabbar.attachEvent("onSelect", function(id,last_id) {
	if(id == 'ConsentViewDiv')
	{
		gotoconsentTab();	
		return true;	
	}
	else
	{
	  return true;
	}});
	
       return true;

}
function gotoconsentTab()
{
		var specimenLabels = new Array();
		var arrayCount=0;
		
		for(var row=1;row<=mygrid.getRowsNum();row++)
		{
			var statusValue=mygrid.cellById(row,6).getValue();
			if(statusValue == 'Distributed' || statusValue == 'Distributed And Close' || statusValue == 'Distributed And Close(Special)')
			{
			   	specimenLabels[arrayCount] = mygrid.cellById(row,0).getValue();
			    arrayCount++;	
			}
						
		}	

		var loader=dhtmlxAjax.postSync("OrderConsents.do","specimenLabels="+specimenLabels);
		document.getElementById('ConsentViewDiv').innerHTML=loader.xmlDoc.responseText;

}
function goToConsentTabFromNext()
{
	gotoconsentTab();
	tabbar.setTabActive('ConsentViewDiv');
}
function init_grid()
{		
		
	var funcName = "rowClick";

	mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtmlx_suite/imgs/");
	mygrid.setHeader("Specimen Label,Specimen Class,Specimen Type,Available Qty,Requested Qty,Distributed Qty,Status,Comments,,,Error");
   	mygrid.setColumnHidden(8,true);
   	mygrid.setColumnHidden(9,true);
   	mygrid.setColumnHidden(10,true);
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,,,,,,,,#select_filter"); 
	mygrid.setEditable("true");
	mygrid.enableAutoHeigth(false);
   	mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,7,7,5,5,5,15,*,0,0,20");
	mygrid.setColTypes("ro,ro,ro,ro,ro,ed,coro,ed,ro,ro,ro");
	mygrid.setSkin("dhx_skyblue");
	mygrid.setColSorting("str,str,str,str,str,str,str,str,str,str,str");
	mygrid.enableMultiselect(true);
	mygrid.init();
	mygrid.loadXML("OrderDetails.do?id="+${requestScope.id});
	mygrid.enableEditEvents(true);
	mygrid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){return qtyChange(stage,rId,cInd,nValue,oValue)});
	document.getElementById('gridStatus').value='grid';
	setComboValues();
}
function setComboValues()
{

var combo = mygrid.getCombo(6);

var status = ["Distributed","Distributed And Close"
		,"Distributed And Close(Special)","New","Pending - For Distribution","Pending - Protocol Review","Specimen Preparation",
		"Rejected - Inappropriate Request","Rejected - Specimen Unavailable","Rejected - Unable to Create"];	

for(var row=0;row<status.length;row++)
	{
		combo.put(status[row],status[row]);
	}


}
function qtyChange(stage,rId,cInd,nValue,oValue)
{
	if(cInd == 5 && stage == 2)
	{
	
		var avlQty = Number(mygrid.cellById(rId,3).getValue());
		var reqQty = Number(nValue);
		if(reqQty > avlQty)
		{
			alert('Distributed quantity cannot be greater than available quantity');
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
function exportOrder(orderId){
	var dwdIframe = document.getElementById("orderExportFrame");
	dwdIframe.src = "ExportAction.do?type=exportOrder&orderId="+orderId;
}
</script>
<!----------------------------------------------->	