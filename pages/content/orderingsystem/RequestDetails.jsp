<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.bean.RequestViewBean" %>
<script src="jss/ajax.js"></script>	   
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxtabbar.css">
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlx_suite/js/dhtmlxtabbar.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>

<head>
<%
List storing = (List)request.getAttribute("newStatList");
String statusValue = "";
for (Object object : storing) 
		{
				NameValueBean bean = (NameValueBean)object;
				statusValue+=bean.getName()+",";
				
		}
List requestDetailsList = null;
if(request.getAttribute(Constants.REQUEST_DETAILS_LIST) != null )
	requestDetailsList = (List) request.getAttribute(Constants.REQUEST_DETAILS_LIST);	
else 
	requestDetailsList = (List) session.getAttribute(Constants.REQUEST_DETAILS_LIST);	
int disabledStatus;
int count=0;
if(requestDetailsList!=null && requestDetailsList.size()>0 )
{
 count=requestDetailsList.size();	
}
String checkQuantityforAll = "checkQuantityforAll("+count+")";
String form_action = Constants.SUBMIT_REQUEST_DETAILS_ACTION+"?submittedFor=ForwardTo&noOfRecords="+count;
%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/OrderingSystem.js"></script>
<script language="javascript">
var newWindow;
var selectedTab = '${requestScope.selectedTab}';
function changeCreateButtonStatus(noOfItems,arrayRowCounter,assignStatusArraycount)
{
	var buttonCreateArrayId = "buttonCreateArrayId"+arrayRowCounter;
	var obj = document.getElementById(buttonCreateArrayId);
	obj.disabled=false;
	var index = assignStatusArraycount-noOfItems;
	for(i=index;i<assignStatusArraycount;i++)
	{

		var assignStatusArrayId = "value(DefinedArrayDetailsBean:"+i+"_assignedStatus)"+arrayRowCounter; 	
		var assignStatusArrayValue =  document.getElementById(assignStatusArrayId).value;
		if(assignStatusArrayValue!="Ready For Array Preparation")
		{
			obj.disabled=true;
			break;
		}
		
	}

}

function tabToDisplay()
{
	var tabIndex = document.getElementById("tabIndexId").value;
	if(tabIndex == 1)
	{
		gotoSpecimenRequestTab();
	}
	else
	{
		gotoArrayRequestTab();
	}
}
	function getUnit(classname,type)
		{
			if(classname == "Tissue")
			{
				if(type == "<%=Constants.FROZEN_TISSUE_SLIDE%>" || type =="<%=Constants.FIXED_TISSUE_BLOCK%>" || type == "<%=Constants.FROZEN_TISSUE_BLOCK%>" || type == "<%=Constants.NOT_SPECIFIED%>" || type == "<%=Constants.FIXED_TISSUE_SLIDE%>")
				{
					return("<%=Constants.UNIT_CN%>");
				}	
				else 
				{
						if(type == "<%=Constants.MICRODISSECTED%>")
						{
							return("<%=Constants.UNIT_CL%>");
						}
						else
						{
							return("<%=Constants.UNIT_GM%>");
						}
				}	
			}
			else if(classname == "Fluid")
			{
				return("<%=Constants.UNIT_ML%>");
			}
			else if(classname == "Cell")
			{
				return("<%=Constants.UNIT_CC%>");
			}
			else if(classname == "Molecular")
			{
				return("<%=Constants.UNIT_MG%>");
			}

		}
function showSpecimenDetails(id)
{
	var id = "requestFor"+id;
	var objId = document.getElementById(id).value;
	
	if(objId != "#")
	{
		showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&target=orderDetails&id=' + objId );
		
	} else {

		alert("No specimen selected : Select specimen to view details.");
	}
}

function viewSpecimenDetails(id)
{
	showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&target=orderDetails&id=' + id );
}


function createAliquots(id)
{

	var id = "requestFor"+id;
	var objId = document.getElementById(id).value;
	if(objId != "#")
	{
		var url = 'createAliquotSpecimen.do?specimenId='+objId;
		window.parent.location.href=url;
	} else {

		alert("No specimen selected : Select specimen to create Aliquot");
	}
	//showNewPage('createAliquotSpecimen.do?pageOf=pageOfCreateAliquot&operation=add&id=' + objId );
}

function createDerivatives(id)
{
	var id = "requestFor"+id;
	var objId = document.getElementById(id).value;
	if(objId != "#")
	{
		var url = 'createDerivedSpecimen.do?specimenId='+objId;
		window.parent.location.href=url;
	} else {

		alert("No specimen selected : Select specimen to create derivative");
	}
	//showNewPage('createDerivedSpecimen.do?rowNumber='+1);
//	showNewPage('CreateSpecimen.do?pageOf=success&operation=add&id=' + objId );
}

function createSpecimen(id)
{
	var url = "createSpecimenFromOrder.do?rowNumber="+id;
	window.parent.location.href=url;	

}

function showSpecimenArrayDetails(id)
{
	showNewPage('SearchObject.do?pageOf=pageOfSpecimenArray&operation=search&id=' + id );
}
function showNewPage(action)
{
   	if(newWindow!=null)
	{
	   newWindow.close();
	}
     newWindow = window.open(action,'','scrollbars=yes,status=yes,resizable=yes,width=860, height=600');
     newWindow.focus(); 
	
}	
function submitAndNotify()
{
	document.getElementById("mailNotification").value= "true";
	setGridValuesToForm();
	document.forms[0].submit();
}
function setGridValuesToForm()
{
var gridStat = document.getElementById('gridStatus').value;
if(gridStat != '' && gridStat != null && gridStat == 'grid')
	{
		for(var row=0;row<mygrid.getRowsNum();row++)
		{
			var reqQty = document.getElementById("requestedQtyId"+row);
		
			var comments = document.getElementById("descriptionId"+row);
			//alert(mygrid.cellById(row+1,5).getValue());
				var desc = mygrid.cellById(row+1,6).getValue();
				var gridReqQty = mygrid.cellById(row+1,4).getValue();
				
				comments.value=desc;
				reqQty.value=gridReqQty;
				
	var canDistribute = "value(RequestDetailsBean:"+row+"_canDistribute)";
	var assignStat = document.getElementById('select_'+row);
	assignStat.value=mygrid.cellById(row+1,5).getValue();
	document.getElementById(canDistribute).value="true";
		//mygrid.cellById(row,5).setValue(id.value);
		//alert(mygrid.cellById(row,5).getAttribute('status'));
		//combo.put(temp[row],temp[row]);
	
	checkQuantity(row);
		}
	}
}

function directDistribute()
{
	document.getElementById("isDirectDistribution").value= "true";
	onDistriProtSet();
}

//this function used to view all the consents
function showAllSpecimen(count)
{

	var speciemnIdValue="";
	var labelIndexCount="";
	var verifiedRows="";
	var iCount=0;

	for(i=0;i<count;i++)
	{
		var consentVerificationkey= "value(RequestDetailsBean:"+i+"_consentVerificationkey)";
		var rowstatus= "value(RequestDetailsBean:"+i+"_rowStatuskey)";
		
		var statusValue =  document.getElementById(rowstatus).value;
		
		var status = document.getElementById(consentVerificationkey).value;
	
		if(status=="<%=Constants.VIEW_CONSENTS%>"||status=="<%=Constants.VERIFIED%>" ||status=="Waived" && statusValue!="disable")
		{
			//var specimenkey= "value(RequestDetailsBean:"+i+"_specimenId)";
			//var specimenObj= document.getElementById(specimenkey);

			var id = "requestFor"+i;
        	var specimenIdentifier = document.getElementById(id).value;
			var specimenLabel = document.getElementById('requestedItemId'+i).value;
			if(specimenIdentifier != "#") 
			{
			
			  speciemnIdValue= speciemnIdValue+specimenIdentifier+","+specimenLabel;
			  labelIndexCount=labelIndexCount+i;
            } 
			
			if(i!=count && specimenIdentifier != "#")
			{
				speciemnIdValue=speciemnIdValue+"|";
				labelIndexCount=labelIndexCount+"|";
			}
			
			if(status=="<%=Constants.VERIFIED%>" || status=="Waived")
			{
				verifiedRows=verifiedRows+(i-iCount)+",";
			}

		}
		else
		{
			iCount=iCount+1;
		}
	
	}

	if(speciemnIdValue == "")
	{
		alert("No specimen selected : Select specimen to view consents");
	} else 
	{

		if(count==(iCount))
		{
			alert("No consents available");
		}
		else
		{
			var consentVerifiedRows = document.getElementById('consentVerifiedRows').value;
			var url= 'ViewAllConsents.do?operation=&pageOf=pageOfOrdering&specimenConsents=yes&verifiedRows='+consentVerifiedRows+'&noOfRows='+count+'&labelIndexCount='+labelIndexCount;
			
			//alert("verifiedRows  : "+consentVerifiedRows);
			//alert(consentVerifiedRows.length);
			//model window popup
			allConsentWindow=dhtmlmodal.open('Institution', 'iframe', url,'Consent Form', 'width=800px,height=350px,center=1,resize=0,scrolling=1')
			allConsentWindow.onclose=function()
		  { 
				return true;
		  }
		  
		}
	}
}
//This function called to view the consent page
function showNewConsentPage(specimenIdentifierkey,labelStatus,consentVerificationkey,id)
{	
	var status = document.getElementById(consentVerificationkey).value;
	var id = "requestFor"+id;
	var specimenIdentifier = document.getElementById(id).value;

	var url ='ConsentVerification.do?operation=&pageOf=pageOfOrdering&status='+status+'&labelStatusId='+labelStatus+'&consentVerificationkey='+consentVerificationkey+'&showConsents=yes&specimenId='+specimenIdentifier;

	if(specimenIdentifier != "#")
	{
		if(status!="No Consents")
		{
			
		  consentWindow=dhtmlmodal.open('Institution', 'iframe', url,'Consent Form', 'width=600px,height=260px,center=1,resize=0,scrolling=1')
		  consentWindow.onclose=function()
		  { 
				return true;
		  }
		}
		
	} else {

		alert("No specimen selected : Select specimen to view consents");
	}


	
}

function checkQuantity(index)
{
	
	var availableQty = document.getElementById("availableQtyId" + index).value;	
	var requiredQty = document.getElementById("requestedQtyId" + index).value;	
	var status = document.getElementById("select_" + index).value;
	var canDistribute = "value(RequestDetailsBean:"+index+"_canDistribute)";
	var answer;
			
	if(status == "<%=Constants.ORDER_REQUEST_STATUS_DISTRIBUTED%>" || status == "<%=Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE%>" )
	{

		if(availableQty > requiredQty)
		{
			//answer= confirm("Available Quantity is greater then the Ordered Quantity :Select OK if you still want to distribute the specimen");
		

		}else if(availableQty < requiredQty)
		{
			//answer= confirm("Available Quantity is less than the Ordered Quantity :Select OK if you still want to distribute the specimen");
		}

		if(availableQty == requiredQty)
		{
			document.getElementById(canDistribute).value="true";
		}
	} else
	{
		document.getElementById(canDistribute).value="false";
	}


}

function checkQuantityforAll(count)
{
	var status = document.getElementById("nextStatusId").value;
	var availableQty;
	var requiredQty ;
	var canDistribute;
	var answer;
	var isQuantityValid = true;

	if(status == "<%=Constants.ORDER_REQUEST_STATUS_DISTRIBUTED%>" || status == "<%=Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE%>")
	{
		for(i=0;i<count;i++)
		{
			availableQty = document.getElementById("availableQtyId" + i).value;	
			requiredQty = document.getElementById("requestedQtyId" + i).value;	
			if(availableQty != requiredQty) {
				answer= confirm("Some of the specimens have Available Quantity greater then or less then the Ordered Quantity : Select Ok if you still want to distribute the specimen");
				isQuantityValid = false;
				break;
			}
		
		}
		if(answer || isQuantityValid)
		{
			for(i=0;i<count;i++)
			{
				canDistribute = "value(RequestDetailsBean:"+i+"_canDistribute)";
				document.getElementById(canDistribute).value="true";
			}
			updateAllStatus();
		}
		
	} else {
		updateAllStatus();
	}

}



 /***  code using ajax :gets the emailAddress of the coordinator without refreshing the whole page  ***/
	function onSpecimenChange(iden)
	{
		var id = "requestFor"+iden;
		request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);	
		request.onreadystatechange = handlerFunction;				
		actionURL = "specimenId="+ document.getElementById(id).value+"&isOnChange=true"+"&identifier="+iden;			
		var url = "RequestDetails.do";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);	
	}

	function onResponseUpdate(response) 
	{
		var values = response.split("#@#");
		
		var selectedSpecimenTypeId = "selectedSpecimenTypeId" +  values[0];
		var availableQtyId = "availableQtyId" + values[0];	
        var specimenQuantityUnitId = "specimenQuantityUnitId" +  values[0];
		
		document.getElementById(selectedSpecimenTypeId).value	= values[2];
		if(values[2] != "NA")
		{
			document.getElementById(availableQtyId).value	= values[1];
			document.getElementById(specimenQuantityUnitId).value	= values[3];
		} else {
			document.getElementById(availableQtyId).value	= "";
			document.getElementById(specimenQuantityUnitId).value	= "";
		}
		
		var consentVerificationkey= "value(RequestDetailsBean:"+values[0]+"_consentVerificationkey)";
		var labelStatus="labelStatus"+values[0];
		var parentId=window.parent.document.getElementById(labelStatus);
		parentId.innerHTML="View"+"<input type='hidden' name='" + consentVerificationkey + "' value='View' id='" + consentVerificationkey + "'/>";


	}

	/*** code using ajax  ***/

	
	/***  code using ajax :to get the requester name  ***/
	function onDistriProtSet()
	{
	
		request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,onResponseSetRequester,true);	
		request.onreadystatechange = handlerFunction;				
		actionURL = "distributionProtId="+ document.getElementById("distributionProtocolId").value+"&isOnChange=true"+"&identifier=";
		var url = "RequestDetails.do";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);	
	}

	function onResponseSetRequester(response) 
	{
		document.getElementById("requesterName").value	= response;
	}

	/*** code using ajax  ***/	
//window.onload="loadTab();init_grid()";

</script>
</head>  
<body onload="loadTab();init_grid();">
<script type="text/javascript" src="jss/wz_tooltip.js"></script>

<html:form action="<%=form_action%>">

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
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
    <tr>
       <td align="left" class=" bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="requestdetails.name" />
		</span></td>
        </tr>
        <tr>

  			<jsp:useBean id="requestDetailsForm" class="edu.wustl.catissuecore.actionForm.RequestDetailsForm" scope="request"/>	
			<% session.setAttribute("REQUEST_DETAILS_FORM",requestDetailsForm);%>
				    
          <td align="left" class="showhide"><table width="100%" border="0" cellspacing="0" cellpadding="3" >

 				<logic:equal name="requestDetailsForm" property="isDirectDistribution" value="true">
				<tr>	 
					<td class="noneditable"><span class="noneditable"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory"  /></span>
						<strong>
						<bean:message key='requestlist.dataTabel.OrderName.label'/>
						</strong>
					</td>
					<td >
					 	<html:text styleClass="black_ar" maxlength="50" size="30" styleId="orderName" property="orderName"/>
					</td>

  				</tr>
				<tr>
					<td class="noneditable"><span class="noneditable"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory"  /></span>
                	<strong>
						<bean:message key='requestlist.dataTabel.DistributionProtocol.label'/>
					</strong>
					</td>
					
					<td>
					
						<html:select property="distributionProtocolId" styleClass="formFieldSizedNew" styleId="distributionProtocolId"
									size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="directDistribute()">
								<html:options collection="<%=Constants.DISTRIBUTIONPROTOCOLLIST%>" labelProperty="name" property="value"/>
						</html:select>
								
					</td>
				</tr>

 				<tr>
						
						<td width="17%" class="noneditable"><strong>
								<bean:message key='requestdetails.header.label.RequestorName'/>
							</strong>
						</td>
								
			            <td class="noneditable">- 
			            <html:text styleClass="formFieldSmallNoBorderlargeSize" 
						name="<%=  Constants.REQUEST_HEADER_OBJECT %>" styleId="requesterName" property="requestedBy" readonly="true" style="background-color:#f6f6f6;"/>
			            
			            <!--  <span class="link">
								<a class="view" href='mailto:<bean:write name='<%=  Constants.REQUEST_HEADER_OBJECT  %>'  property='email' scope='request' />' >
									<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT %>" property="requestedBy" scope="request"/> 
								</a>
							</span>-->
						</td>
              </tr>


				</logic:equal>
			
				<logic:notEqual  name="requestDetailsForm" property="isDirectDistribution" value="true">
				<tr>
					<td class="noneditable">
						<strong>
						<bean:message key='requestlist.dataTabel.OrderName.label'/>
						</strong>
					</td>

	                <td class="noneditable">-
	                		<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT  %>" property="orderName" scope="request"/>
					</td>
				</tr>
				<tr>
					<td class="noneditable">
                		<strong>
						<bean:message key='requestlist.dataTabel.DistributionProtocol.label'/>
						</strong>
					</td>
 					<td class="noneditable">-
							<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT  %>" property="distributionProtocol" scope="request"/>
					</td>
				</tr>

				<tr>
						
						<td width="17%" class="noneditable"><strong>
								<bean:message key='requestdetails.header.label.RequestorName'/>
							</strong>
						</td>
								
			            <td class="noneditable">- 
  
				           <span class="link">
								<a class="view" href='mailto:<bean:write name='<%=  Constants.REQUEST_HEADER_OBJECT  %>'  property='email' scope='request' />' >
									<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT %>" property="requestedBy" scope="request"/> 
								</a>
							</span>

						</td>
              </tr>
	

				</logic:notEqual>	

           <tr>
                <td class="noneditable"><strong>
								<bean:message key='requestlist.dataTabel.label.RequestDate'/>
							</strong></td>
                <td class="noneditable">- 
							<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT  %>" property="requestedDate" scope="request"/>
						</td>
              </tr>
            
			<tr >
                
					<td class="noneditable"><strong>
							<bean:message key='requestlist.dataTabel.label.Site'/> 
 					</strong></td>
					<td>
							<html:select property="site" name="requestDetailsForm" styleClass="formFieldSized51" styleId="siteId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%= Constants.SITE_LIST_OBJECT  %>" labelProperty="name" property="value"/>		
							</html:select>
						 			
					</td>
					
			</tr>
 			<tr>
				<logic:notEqual  name="requestDetailsForm" property="isDirectDistribution" value="true">
                <td class="noneditable"><strong>
								<bean:message key='requestdetails.header.label.Comments'/> 
								<bean:write name="<%=  Constants.REQUEST_HEADER_OBJECT  %>"  property="comments"  scope="request" />
							</strong></td>
                <td class="noneditable">&nbsp;</td>
				</logic:notEqual>	
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
			<tr>
			
						<td colspan="3" align="right" valign="top">
							<img src="images/uIEnhancementImages/viewall_icon.gif" alt="View All" />
								
							<a href="javascript:showAllSpecimen('<%=count%>')" class="view" >
													<bean:message key="requestdetails.link.ViewAllConsents" />
							                    </a>
						</td>
					
			</tr>

				<tr>
					<td width="100%" >
					<table border="0" width="100%"><tr><td>
						<div id="tabbar_div" align="left" style="width:auto;height:350px;overflow:auto;"/>
						</td>
						</tr></table>
						<table width="100%">
						<tr><td>
						<div id="SimpleViewDiv">
						<logic:notEqual name="selectedTab" value="AdvancedViewTab">
							<%@ include file="/pages/content/orderingsystem/SpecimenOrderGrid.jsp" %>
						</logic:notEqual>
			</div>	
			</td></tr>
			<tr><td>
			<div id="AdvancedViewDiv" style="width:auto;height:340px;overflow:auto;">
			<logic:equal name="selectedTab" value="AdvancedViewTab">
							<%@ include file="/pages/content/orderingsystem/AdvanceSpecimenOrderView.jsp" %>
						</logic:equal>
				
			</div>	
			</td></tr>
			<tr><td>
			<div id="ArrayRequestDiv">
				<%@ include file="/pages/content/orderingsystem/ArrayRequests.jsp" %>
			</div>
			</td></tr></table>
					</td>
				</tr>
				<tr>
				<td>
				
				</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
 					<td valign="top" class="black_ar_t">&nbsp;<br />
                  &nbsp;
 						<bean:message key="requestdetails.header.label.Comments" />
	 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 						<html:textarea name="requestDetailsForm" styleClass="black_ar" cols="90" rows="3" property="administratorComments"  />
		 			</td>
					<td valign="top" class="black_ar"></td>
              </tr>
			  <%							
			  String operationUpdate = "update";
			  String distributionProtocol = ((RequestViewBean)request.getAttribute(Constants.REQUEST_HEADER_OBJECT)).getDistributionProtocolId(); 
			  String orderName = ((RequestViewBean)request.getAttribute(Constants.REQUEST_HEADER_OBJECT)).getOrderName();
		%>
						<html:hidden name="requestDetailsForm" property="id" />
						<html:hidden name="requestDetailsForm" property="operation" value="<%= operationUpdate %>"/>
						<html:hidden name="requestDetailsForm" property="distributionProtocolId" value="<%= distributionProtocol %>"/>	
						<html:hidden name="requestDetailsForm" property="orderName" styleId="orderName" value="<%= orderName %>"/>					
						<html:hidden name="requestDetailsForm" property="tabIndex" styleId="tabIndexId"/>			
						<html:hidden name="requestDetailsForm" property="mailNotification" styleId="mailNotification"/>	
						<html:hidden name="requestDetailsForm" property="isDirectDistribution" styleId="isDirectDistribution"/>	
		<tr>
          <td class="bottomtd"></td>
        </tr>
        <tr>
          <td class="buttonbg" style="padding:6px;">
					<input type="button" class="blue_ar_b" value="Submit" onclick="submitPage()" accesskey="Enter">
					<input type="button" class="blue_ar_c" value="Submit And Notify" onclick="submitAndNotify()">
					</td>
				</tr>
		</table>
    </td>
  </tr>
</table>
   <!-- main table ends here -->
</html:form>
</body>
<script>
function loadTab()
{
tabbar = new dhtmlXTabBar("tabbar_div", "top");
	tabbar.setSkin('dhx_skyblue');
	tabbar.setImagePath("dhtmlx_suite/imgs/");
	
	
	tabbar.addTab("SimpleViewTab", "Simple View", "100px");
	tabbar.addTab("AdvancedViewTab", "Advance View", "100px");
	tabbar.addTab("ArrayRequestTab", "Array Request", "100px");
	
	
	
	
	tabbar.setContent("SimpleViewTab","SimpleViewDiv");
	tabbar.setContent("AdvancedViewTab","AdvancedViewDiv");
	tabbar.setContent("ArrayRequestTab","ArrayRequestDiv");
	
	//alert('${requestScope.selectedTab}');
	tabbar.setTabActive('${requestScope.selectedTab}');
	var answer;
	tabbar.attachEvent("onSelect", function(id,last_id) {
	if(id == 'AdvancedViewTab' && (selectedTab =='SimpleViewTab' || last_id == 'SimpleViewTab'))
	{
		answer= confirm("By clicking on this tab you will lose all the changes you have made, click ok to continue");
		if(answer)
		{//alert('<bean:write name="requestDetailsForm" property="id" scope="request"/>');
			document.forms[0].action='RequestDetails.do?selectedTab=AdvancedViewTab&id=<bean:write name="requestDetailsForm" property="id" scope="request"/>';
			document.forms[0].submit();
		}
		else
		{
			return false;
		}
	}
	else if(id == 'SimpleViewTab' && (selectedTab =='AdvancedViewTab' || last_id == 'AdvancedViewTab'))
	{
		answer= confirm("By clicking on this tab you will lose all the changes you have made, click ok to continue");
		if(answer)
		{//alert('<bean:write name="requestDetailsForm" property="id" scope="request"/>');
			document.forms[0].action='RequestDetails.do?selectedTab=SimpleViewTab&id=<bean:write name="requestDetailsForm" property="id" scope="request"/>';
			document.forms[0].submit();
		}
		else
		{
			return false;
		}
	}
    return true;
});
}

</script>
<!----------------------------------------------->	