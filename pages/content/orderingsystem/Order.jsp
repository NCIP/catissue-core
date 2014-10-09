<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" isELIgnored="false"%>
<script src="jss/ajax.js"></script>	   
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_skyblue.css"/>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxToolbar/codebase/skins/dhtmlxtoolbar_dhx_blue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css" href="css/alretmessages.css">
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxcommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTree/codebase/ext/dhtmlxtree_dragin.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxAccordion/codebase/dhtmlxcontainer.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxcontainer.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar_start.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_mcol.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<script src="jss/json2.js" type="text/javascript"></script>
<c:set var="tr_white_color" value="tr_alternate_color_white" />
<c:set var="tr_grey_color" value="tr_alternate_color_lightGrey" />
<head>

<script language="javascript">
var tabDataJSON = {};
var tabbar;
var mygrid;
var site_combo;
var user_combo;
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
	var orderRejected=false;
	for(var row=1;row<=mygrid.getRowsNum();row++)
	{
		var statusValue=mygrid.cellById(row,7).getValue();
		if(statusValue == 'Rejected - Inappropriate Request' || statusValue == 'Rejected - Specimen Unavailable' || statusValue == 'Rejected - Unable to Create')
		{
		   orderRejected=true;	
		}
					
	}	

	if(distributionProtocolNameCombo.getActualValue()=="" || distributionProtocolNameCombo.getActualValue()=="-1")
	{
		if(!orderRejected)
		{
                  alert("Please select distribution protocol.");
                  return;	
		} 
        
	}
	
	if(site_combo.getActualValue()=="" || site_combo.getActualValue()=="-1")
	{
		if(!orderRejected)
		{
			alert("Please select distribution site.");
			return;
		}
	}

	for (var i = 0; i < consentVerifiedValues.length; ++i) 
	{
	  if(!consentVerifiedValues[i].checked){
	     alert("Please verify the Consents for the specimen(s)");
	     return;	
   	 }
   	}
	
	if(user_combo.getActualValue()=="" || user_combo.getActualValue()=="-1")
	{
		alert("Please select Requestor Name.");
		return;
	}

	tabDataJSON["gridXML"] = mygrid.serialize();
	tabDataJSON["id"]=${requestScope.id};
	tabDataJSON["orderName"]=document.getElementById('orderName').value;
	if(distributionProtocolNameCombo.getActualValue()!="-1")
	{
		tabDataJSON["disptributionProtocolId"]=distributionProtocolNameCombo.getActualValue();
	}
    tabDataJSON["disptributionProtocolName"]=distributionProtocolNameCombo.getComboText();
	if(site_combo.getActualValue()!="-1")
	{
		tabDataJSON["site"]=site_combo.getActualValue();
	}
	tabDataJSON["requestorEmail"]='<bean:write name="DisplayOrderDTO" property="requestorEmail" scope="request"/>';
	tabDataJSON["requestorId"]=user_combo.getActualValue();
	tabDataJSON["requestedDate"]=document.getElementById('requestedDate').value;

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
				  mygrid.cellById(row+1,7).setValue(orderErrorDTOs[i].newStatus);	
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
	
		var combo = mygrid.getCombo(6);
	
		document.getElementById("nextStatusId").value=id.value;
		for(var row=0;row<mygrid.getRowsNum();row++)
		{
			if(id.value == ("Distributed And Close(Special)"))
			{
				var avlQty = Number(mygrid.cellById(row+1,4).getValue());
				var reqQty = Number(mygrid.cellById(row+1,6).getValue());
				if(avlQty == reqQty)
				{
					mygrid.cellById(row+1,7).setValue("Distributed And Close");
				}
				else
				{
					mygrid.cellById(row+1,7).setValue("Distributed");
					
				}
			}
			else
			{
				mygrid.cellById(row+1,7).setValue(id.value);
				
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
				<td width="29%" class="black_ar align_right_style">
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
 							<div  class="dhtmlx-combo-margin">
							 <html:select property="requestorName" name="DisplayOrderDTO" styleClass="formFieldSized" styleId="requestorName" size="100" onblur="processData(this)" style="width:30em;">
							 <html:options collection="userList" labelProperty="name" property="value" />
					       	</html:select>
					       </div>
						</td>
						<script>
								  window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
								  user_combo = new dhtmlXComboFromSelect("requestorName");
								  user_combo.setSize(330);
								  user_combo.enableFilteringMode(true);
								  
 							  	  if(user_combo.getOptionByLabel('${requestScope.DisplayOrderDTO.requestorName}')==null)
								  {
 							  		user_combo.setComboValue("-1");		
								  }
								  else
								  {
									  user_combo.setComboValue(user_combo.getOptionByLabel('${requestScope.DisplayOrderDTO.requestorName}').value);
								  }
    					</script>					
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
								  window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
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
				<input type="text" name="requestedDate" class="black_ar"
                                   id="requestedDate" size="10" onblur="processData(this)" value='<fmt:formatDate value="${DisplayOrderDTO.requestedDate}" pattern="${datePattern}" />' onclick="doInitCalendar('requestedDate',false,'${uiDatePattern}');"/>
                                <span id="dateId" class="grey_ar_s capitalized">[${datePattern}]</span>&nbsp;
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
					<input type="button" class="blue_ar_b" value="Export Order" onclick="defineView('${requestScope.id}')" accesskey="Enter">
				
					
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
<div id="defineViewPop" style="display:none">
	<div style="height:50px" class="black_ar_b">Please select the fields that you wish to export</div>
	<!--div style="width:100%;height:200px;overflow:scroll;overflow-x:hidden;"-->
				  <div id="treeBox"  style="width:100%;height:200px;"></div>
	<!--/div-->
	<div>
		<input type='button' name='Export' onClick='exportReport()' value='Export' class="blue_ar_b"><input class="blue_ar_b" type='button'  value='Cancel' name='Cancel' onClick='closeTermWindow()'style='margin-left:6px'>
	</div>
</div>
   <!-- main table ends here -->
</html:form>

	<iframe id = "orderExportFrame" width = "0" height = "0" frameborder="0"></iframe>
</body>

<script>
//alert("${requestScope.distriTree}");
var tree;
function initTree(){
tree=new dhtmlXTreeObject("treeBox","100%","100%",0);
			tree.setImagePath("dhtmlxSuite_v35/dhtmlxTree/codebase/imgs/");
			tree.setSkin('dhx_skyblue');
			tree.enableTreeImages(false);
			tree.enableTreeLines(false);
			tree.enableSmartXMLParsing(true);
			
			tree.attachEvent("onOpenEnd",tonclick);
			tree.enableCheckBoxes(1);
			tree.enableThreeStateCheckboxes(true); 
			tree.loadXMLString("${requestScope.distriTree}");
			document.getElementById('treeBox').style.overflowY ="scroll";
			//<?xml version="1.0"?> 

			//tree.setOnClickHandler(tonclick);
			//tree.setOnOpenHandler(expand);
}
function tonclick(id)
{
	document.getElementById('treeBox').style.overflowY ="scroll";
 };
function expand(id,mode)
	{
			document.getElementById('treeBox').style.overflowY =
   (document.getElementById('treeBox').offsetHeight > 399) ? 'auto' : 'hidden';
		if(mode ==1 || mode==0)
		{
			return true;
		}
		
		
		return true;
	}
function closeTermWindow(){
            aliquotNameSpace.dhxWins.window("containerPositionPopUp").close();
        }
function loadTab()
{
	tabbar = new dhtmlXTabBar("tabbar_div", "top");
	tabbar.setHrefMode("iframes-on-demand");	
	tabbar.setSkin('dhx_skyblue');
	tabbar.setImagePath("dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
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
			var statusValue=mygrid.cellById(row,7).getValue();
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
	mygrid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
	mygrid.setHeader("Specimen Label,Specimen Class,Specimen Type,Specimen Position,Available Qty,Requested Qty,Distributed Qty,Status,Comments,,,Error");
   	mygrid.setColumnHidden(9,true);
   	mygrid.setColumnHidden(10,true);
   	mygrid.setColumnHidden(11,true);
	mygrid.attachHeader("#text_filter,#select_filter,#select_filter,#text_filter,,,,,,,,#select_filter"); 
	mygrid.setEditable("true");
	mygrid.enableAutoHeigth(false);
   	mygrid.enableRowsHover(true,'grid_hover')
	mygrid.setInitWidthsP("20,7,7,20,5,5,5,15,*,0,0,20");
	mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ed,coro,ed,ro,ro,ro");
	mygrid.setSkin("dhx_skyblue");
	mygrid.setColSorting("str,str,str,str,str,str,str,str,str,str,str,str");
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

var combo = mygrid.getCombo(7);

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
	if(cInd == 6 && stage == 2)
	{
	
		var avlQty = Number(mygrid.cellById(rId,4).getValue());
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
function exportReport(){
var orderId = '${requestScope.id}';
var chkdNodes = tree.getAllChecked();
if(chkdNodes.length == 0){
alert("Please select atleast one node to export");
return;
}
var dwdIframe = document.getElementById("orderExportFrame");
var itemsList = tree.getAllChecked();
itemsList = itemsList.replace("scg,","");
itemsList = itemsList.replace("specimen,","");
itemsList = itemsList.replace("participant,","");
itemsList = itemsList.replace("cpr,","");
itemsList = itemsList.replace("protocol,","");
dwdIframe.src = "ExportAction.do?type=exportOrder&orderId="+orderId+"&items="+itemsList;
closeTermWindow();
}
</script>
<script>
var imgsrc="images/";
    window.dhx_globalImgPath = "dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
		var activityStatusCombo={};
        var aliquotDateErr = false;
        var aliquotGrid;
        var aliquotPopUpParam = {};
        var aliquotNameSpace = {};
function defineView(orderId){
            if(aliquotNameSpace.dhxWins == undefined){
                aliquotNameSpace.dhxWins = new dhtmlXWindows();
                aliquotNameSpace.dhxWins.setSkin("dhx_skyblue");
                aliquotNameSpace.dhxWins.enableAutoViewport(true);
            }
        //    aliquotNameSpace.dhxWins.setImagePath("");
            if(aliquotNameSpace.dhxWins.window("containerPositionPopUp")==null){
                var w =500;
                var h =360;
                var x = (screen.width / 3) - (w / 2);
                var y = 150;
                aliquotNameSpace.dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
                //aliquotNameSpace.dhxWins.setPosition(x, y);
                //aliquotNameSpace.dhxWins.window("containerPositionPopUp").center();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").allowResize();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setModal(true);
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setText("Define View");
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("minmax1").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("park").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("close").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setIcon("images/terms-conditions.png", "images/terms-conditions.png");
             
			 if(document.getElementById("defineViewPop")==null){
				  var div = document.createElement("div");
            
                div.id="defineViewPop";
				div.innerHTML = windowDivStr;
				  document.body.appendChild(div);
              
			 }else{
				windowDivStr = document.getElementById("defineViewPop").innerHTML;
			 }
			 initTree();
			 aliquotNameSpace.dhxWins.window("containerPositionPopUp").attachObject("defineViewPop");
			 
            }
			  
        }
		var windowDivStr;
</script>
<!----------------------------------------------->	