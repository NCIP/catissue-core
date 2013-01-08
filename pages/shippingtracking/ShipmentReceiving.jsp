<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.lang.Integer"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caTissue Suite v 1.1</title>

<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">\
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">

<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/addl_s_t.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/antiSpecAjax.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/shippingtracking/shippingTracking.js"></script>
</head>
<% Map dataMap = null;
String rowNumber = "0";
ShipmentReceivingForm form=(ShipmentReceivingForm)request.getAttribute("shipmentReceivingForm");
List<Specimen> specimenList = (List<Specimen>)form.getSpecimenCollection();

String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
																		if (collectionProtocolId==null)
																			collectionProtocolId=" ";
%>
<script>

function submitForm()
{
	updateContainerNames();
	document.forms[0].submit();
}
function updateContainerNames()
{
	var idArray = getCount();
	for(var i=0;i<idArray.length;i++)
	{
		var variable="specimenItem["+i+"].id";
		var id=getElement("specimenItem["+i+"].id").value;
		var attributeName=document.getElementsByName('specimenDetails(selectedContainerName_'+id+')')[0];
		var elemId = "storageContainerDropDown_"+id;
		var valueToSet =document.getElementById(elemId).value;
		attributeName.value=valueToSet;
	}
}
function doOnLoad()
{
var url="CatissueCommonAjaxAction.do?type=getStorageContainerListForRequestShipment&specimenId=";
<%
for(int i=0;i<specimenList.size();i++){
Specimen specimen=specimenList.get(i);
Long specimenId=specimen.getId();
%>
	
	//Drop Down components information
	containerDropDownInfo_<%=specimenId%> = {gridObj:"storageContainerGrid_<%=specimenId%>", gridDiv:"storageContainer_<%=specimenId%>", dropDownId:"storageContainerDropDown_<%=specimenId%>", 
	pagingArea:"storageContainerPagingArea_<%=specimenId%>", infoArea:"storageContainerInfoArea_<%=specimenId%>", onOptionSelect:
	function (id,ind)
		{
			var containerName=document.getElementById("storageContainerDropDown_<%=specimenId%>").value;
			document.getElementsByName('specimenDetails(selectedContainerName_<%=specimenId%>)')[0].value = id;
			document.getElementById(containerDropDownInfo_<%=specimenId%>['dropDownId']).value = scGrid_<%=specimenId%>.cellById(id,ind).getValue();
			document.getElementsByName('specimenDetails(position1_<%=specimenId%>)')[0].value="";
			document.getElementsByName('specimenDetails(position2_<%=specimenId%>)')[0].value="";
			hideGrid(containerDropDownInfo_<%=specimenId%>['gridDiv']);
			scGridVisible_<%=specimenId%> = false;
		}
		, actionToDo:"CatissueCommonAjaxAction.do?type=getStorageContainerListForRequestShipment&specimenId=<%=specimenId%>", callBackAction:
		function(){
					var containerName= document.getElementsByName('specimenDetails(selectedContainerName_<%=specimenId%>)')[0].value;
					if(containerName != "" && containerName != 0 && containerName != null)
					{
						//document.getElementsByName('specimenDetails(selectedContainerName_<%=specimenId%>)').value = containerName;
						//document.getElementById(containerDropDownInfo_<%=specimenId%>['dropDownId']).value = scGrid_<%=specimenId%>.cellById(containerName,0).getValue();
						//hideGrid(containerDropDownInfo_<%=specimenId%>['gridDiv']);
						//scGridVisible_<%=specimenId%> = false;
					}
				}
			, visibilityStatusVariable:scGridVisible_<%=specimenId%>,propertyId:'selectedContainerName_<%=specimenId%>'};
	// initialising grid
	scGrid_<%=specimenId%> = initDropDownGrid(containerDropDownInfo_<%=specimenId%>,true); 
	<%}%>
}

function setContainerValues()
{
<%if(specimenList!=null)
	{
		for(int i=0;i<specimenList.size();i++)
		{
			Specimen gs2=specimenList.get(i);
			Long stringToAppend=gs2.getId();
			
			String[] selectedContainerName=(String[])form.getSpecimenDetails("Specimen:" + gs2.getId()	+ "_initialValues");
			%>
				document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value='<%=selectedContainerName[0]%>';
<%}}%>	
}

//declaring DHTMLX Drop Down controls required variables
<%
for(int i=0;i<specimenList.size();i++){
Specimen specimen=specimenList.get(i);
Long specimenId=specimen.getId();
%>
var containerDropDownInfo_<%=specimenId%>, scGrid_<%=specimenId%>;
var scGridVisible_<%=specimenId%> = false;

var gridDivObject_<%=specimenId%> ;

<%}%>

function showPopUp(specimenId,storageContainerDropDown,positionDimensionOne,positionDimensionTwo,collectionProtocolId,specimenClassName) 
{
	var storageContainer =document.getElementById(storageContainerDropDown).value;
	if(storageContainer!="")
	{
		loadDHTMLXWindowForMultipleSpecimen(storageContainerDropDown,positionDimensionOne,positionDimensionTwo);
	}
	else
	{
			var frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
			"selectedContainerName=storageContainerDropDown_" +specimenId+
			"&pos1=position1_" +specimenId+
			"&pos2=position2_" +specimenId+
			"&holdSpecimenClass="+specimenClassName+
			"&specimenId="+specimenId;
			openPopupWindow(frameUrl,"newSpecimenPage");
	}
}

function showHideStorageContainerGrid(e,gridDivId, dropDownId,scGridVisible,containerDropDownInfo,scGrid)
{		
		setValue(e,containerDropDownInfo['gridDiv'], containerDropDownInfo['dropDownId']);
		if(containerDropDownInfo['visibilityStatusVariable'])
		{
			hideGrid(containerDropDownInfo['gridDiv']);
			containerDropDownInfo['visibilityStatusVariable'] = false;
		}
		else 
		 {	
			showGrid(containerDropDownInfo['gridDiv'],containerDropDownInfo['dropDownId']);
			containerDropDownInfo['visibilityStatusVariable'] = true;
			var containerName=document.getElementById(containerDropDownInfo['dropDownId']);
			if(null== containerName)
			{
				containerName="";
			}
			scGrid.load(containerDropDownInfo['actionToDo'],"");
		 }
}
</script>
<script><!--
var sid="";
var outerMostDataTable = new Hashtable();
var controlRowNumber="1";
function ApplyToAll(object)
{
	var idArray = getCount();
	var firstSpecimenID=getElement("specimenItem[0].id").value;
	var elemId = "storageContainerDropDown_"+firstSpecimenID;
	//alert(elemId);
	var storageContainerValue =document.getElementById(elemId).value;//container name
	//alert(storageContainerValue);
	var i=0;
	for(i=1;i<idArray.length;i++)	// change values for all remaining
	{
	   setValues(idArray[i],storageContainerValue,idArray[0]);		
	}
}

function setValues(idArrayI,valueToSet,idArray0)
{
	//alert("setValues");
	var specimenId=(idArrayI);
	//alert("specimenId : "+specimenId);
	var controlId="storageContainerDropDown_"+specimenId;
	//alert("controlId : "+controlId);
	document.getElementById(controlId).value=valueToSet;
	
	elemName = "position1_"+specimenId;
	document.getElementById(elemName).value="";
	
	elemName = "position2_"+specimenId;
	document.getElementById(elemName).value="";
	
	var elemName = "specimenDetails(selectedContainerName_"+idArray0+")";
	var firstContainerId=valueToSet;
	updateField(specimenId,valueToSet,firstContainerId);
}

function getElement(name)
{
	var fields = document.getElementsByName(name);
	if(fields.length > 0)
	{
		return fields[0];
	}
	else
		return "";
}

	function updateField(i,valueToSet,firstContainerId)
{
	//alert("updateField");
	elemName = "specimenDetails(selectedContainerName_"+i+")";
	getElement(elemName).value =firstContainerId;

	var ele1 = document.getElementById("selectedContainerName_"+i);
	if(ele1!=null)
	{
      ele1.value=valueToSet;
	}
  	elemName = "specimenDetails(position1_"+i+")";
	getElement(elemName).value = "";
	elemName = "specimenDetails(position2_"+i+")";
	getElement(elemName).value ="";
}
		
	 function getCount()
    {
		var count=0;
		var fields = document.getElementsByClassName("classForCount");
		//alert(fields.length);
		var ids = new Array(); 
		var i=0;
		for (i=0; i<fields.length;i++)
		{
			var fname = fields[i].name;
			var text=fname.split("_");
			var fid=text[1];
			fid=fid.replace(")","");
			ids[count]=fid;
			count = count+1;
		}
		return ids;
	}
function addAutoManualDiv(element,isApplyToAll)
{
	  	var specimenId=(element.id).split("_")[1];
		var autoDiv=document.getElementById("autoDiv_"+specimenId);
		var manualDiv=document.getElementById("manualDiv_"+specimenId);
        if(element.value==1)
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'none';
		}
		else if(element.value==2)
		{
			autoDiv.style.display = 'block';
			manualDiv.style.display = 'none';
			//if(!isApplyToAll)
			{
				onCollOrClassChange(element);
			}
		}
		else
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'block';
			//if(!isApplyToAll)
			{
			  onCollOrClassChange(element);
			}
		}
}
	//Function called on storage position change
    function onStorageRadioClickInSpecimen(element,rowNumber,isApplyToAll)
	{
       	controlRowNumber=rowNumber;
		addAutoManualDiv(element,isApplyToAll);
	}

	function onCollOrClassChange(element)
		{			
			var specimenId=(element.id).split("_")[1];
			sid = specimenId;
			var value;
			if(element.value=='1')
			{
				resetVirtualLocated('specimenStorageLocation_'+specimenId);
				value=true;
			}
			else
			{
				value=false;
			}
			
			if(element.value=='2')
			{

				var responseHandlerFn = setSCLocation;
		        var bool = true;
				var reqType = "GET";
				var action = "StorageContainerAjaxAction.do?virtualLocated=false&stContSelection="+(element.value)+"&value="+value;
				var url = action + "&rowNumber="+controlRowNumber+"&onCollOrClassChange=true&requestFor=storageLocation&specimenId="+specimenId;

		        ajaxCall(reqType, url, bool, responseHandlerFn);
			}
			else
			{ 
				
			<%
				String actionOnCollOrClassChange = "";%>
				var action = "StorageContainerAjaxAction.do?virtualLocated=false&stContSelection="+(element.value)+"&value="+value;

				var url= action + "&onCollOrClassChange=true&requestFor=storageLocation&specimenId="+specimenId;
				var responseHandlerFn = setCPId;
		        var bool = true;
				var reqType = "GET";

				ajaxCall(reqType, url, bool, responseHandlerFn);
				//setContainerType(element.value,specimenId);

			}
		}



   function setSCLocation()
   {
	  //readyState == 4 - indicates that the request is complete and we can use the returned data.
 	if ((xmlHttpReq.readyState == 4) && (xmlHttpReq.status == 200))
	{
		var msg = xmlHttpReq.responseText;		
		if(msg.indexOf('#') != -1)
		{
			var tag=(msg).split("#")[1];
			var tableData = (msg).split("#")[0];
			var elemId = "autoDiv_"+sid;
			var previousInnerHTML = new String();
			previousInnerHTML = document.getElementById(elemId).innerHTML;
			document.getElementById(elemId).innerHTML=tag;
			var functionForTable = 'function addTableData(){'+tableData+'};';
			eval(functionForTable);	
			addTableData();
		}		
	  }
   }
    function onCollectionGroupChange(element)
	{
		if(element.value=='1')
		{
			resetVirtualLocated('specimenStorageLocation_'+specimenId);
			value=true;
		}
		else
		{
			value=false;
		}
		var action = "st_ShowShipmentReceving.do?virtualLocated=false&stContSelection="+(element.value)+"&value="+value;
		document.forms[0].action = action + "&onCollOrClassChange=true&requestFor=storageLocation";
		document.forms[0].submit();
	}

	function resetVirtualLocated(id)
	{	
		document.getElementById(id).value=1;
		document.getElementById("autoDiv").style.display="none";
		document.getElementById("manualDiv").style.display="none";
	}

	function mapButtonClickedOnReceiveShipment(name,selectedContControlId,frameUrl)
	{
		var storageContainer = selectedContControlId.value;
		frameUrl+="&storageContainerName="+storageContainer;
		openPopupWindow(frameUrl,name);
	}
			
function setParentContainerType(containerBtnName)
{
	var containerIId=(containerBtnName).split("_")[1];
	var selectedParentSite=document.getElementById("parentContainerSite"+containerIId);
	var selectedParentAuto=document.getElementById("parentContainerAuto"+containerIId);
	var selectedParentManual=document.getElementById("parentContainerManual"+containerIId);
	
	if('${requestScope.operation}'!= null)
    {
	
		if('${requestScope.parentContainerSelected}' == "Site")
		{
			selectedParentSite.style.display="block"; 	
			selectedParentAuto.style.display="none";
			selectedParentManual.style.display="none";
		}
		else if('${requestScope.parentContainerSelected}' == "Auto")
		{
			selectedParentSite.style.display="none"; 	
			selectedParentAuto.style.display="block";
			selectedParentManual.style.display="none";
		}
		else if('${requestScope.parentContainerSelected}' == "Manual")
		{
			selectedParentSite.style.display="none"; 	
			selectedParentAuto.style.display="none";
			selectedParentManual.style.display="block";
		}
	}
}

function parentContainerTypeChanged(element)
{	
	var containerId=(element.id).split("_")[1];
	var selectedParentSite=document.getElementById("parentContainerSite_"+containerId);
	var selectedParentAuto=document.getElementById("parentContainerAuto_"+containerId);
	var selectedParentManual=document.getElementById("parentContainerManual_"+containerId);
	
	if(element.value == "Site")
    {
		selectedParentSite.style.display="block"; 	
		selectedParentAuto.style.display="none";
		selectedParentManual.style.display="none";
	}
	else if(element.value == "Auto")
	{
		selectedParentSite.style.display="none"; 	
		selectedParentAuto.style.display="block";
		selectedParentManual.style.display="none";
	    onParentContainerSelectChange(element.value,containerId);
	}
	else if(element.value == "Manual")
	{
		selectedParentSite.style.display="none"; 	
		selectedParentAuto.style.display="none";
		selectedParentManual.style.display="block";
		onParentContainerSelectChange(element.value,containerId);
	}
}

function onParentContainerSelectChange(selectedOption,containerId)
{
	var action = "st_ShowShipmentReceving.do?virtualLocated=false&parentContainerSelected="+selectedOption+"&containerObjectId="+containerId;
	action = action + "&onCollOrClassChange=true&requestFor=storageLocation";
	document.forms[0].action = action;
	document.forms[0].submit();
}
--></script>

</head>

<body onload="initWindow();doOnLoad();setContainerValues()">
<%

		String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
		String[] initValues = new String[3];
		//String rowNumber = "1";
		String styClass = "formFieldSized5";
		String tdStyleClass = "customFormField";
		String noOfEmptyCombos = "3";
		boolean dropDownDisable = false;
		boolean textBoxDisable = false;
		String autoDisplayStyle= null;
		String manualDisplayStyle=null;
		
%>
  <table width="100%">
	<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="shipment.name" bundle="msg.shippingtracking"/></span></td>
					<td align="right"><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title" width="31" height="24" /></td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td width="100%">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>

	<tr>
	<td width="100%">
	<html:form action="/ProcessShipmentReceived">
		<html:hidden property="id" styleId="id"/>
		<html:hidden property="operation" styleId="operation" />
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
				<tr>
					<td class="td_color_bfdcf3">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="2" class="toptd"></td>
							</tr>
							<tr>
								<td colspan="2" class="tablepadding">
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="whitetable_bg">
										<tr>
								            <td height="25" align="left" valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="receivedShipment" bundle="msg.shippingtracking"/></span></td>
										</tr>
										<tr class="tableheading">
											<td height="25" align="left" ><b>&nbsp;&nbsp;<bean:message key="shipment.details" bundle="msg.shippingtracking"/></b></td>
										</tr>
										<tr>
											<td align="left" >
												<table border="0" cellpadding="3" cellspacing="2">
													<tr>
										                  <td width="6" align="center" valign="top" class="black_ar">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.label" bundle="msg.shippingtracking"/></td>
										                  <td align="left" valign="top" class="black_ar"><c:out value="${shipmentReceivingForm.label}"/>
											                  	<html:hidden name="shipmentReceivingForm" property="label"/>
											              </td>
										                  <td align="left" valign="top">&nbsp;</td>
										                  <td width="6" align="left" valign="top">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.barcode" bundle="msg.shippingtracking"/></td>
										                  <td align="left" valign="top" class="black_ar"><c:out value="${shipmentReceivingForm.barcode}"/>
									                  		<html:hidden name="shipmentReceivingForm" property="barcode"/>
									                  	  </td>
													</tr>
													<tr>
										                  <td align="center" valign="top" class="black_ar">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.senderSite" bundle="msg.shippingtracking"/></td>
														  <td align="left" valign="top" class="black_ar">
														  		<c:out value="${shipmentReceivingForm.senderSiteName}"/>
															  	<html:hidden name="shipmentReceivingForm" property="senderSiteName"/>
															  	<html:hidden name="shipmentReceivingForm" property="senderSiteId"/>
														  </td>
														  <td width="20" align="left" valign="top">&nbsp;</td>
										                  <td align="left" valign="top">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.receiverSite" bundle="msg.shippingtracking"/></td>
										                  <td align="left" valign="top" class="black_ar">
										                  		<c:out value="${shipmentReceivingForm.receiverSiteName}"/>
															  	<html:hidden name="shipmentReceivingForm" property="receiverSiteName"/>
															  	<html:hidden name="shipmentReceivingForm" property="receiverSiteId"/>
										                  </td>
                									</tr>
                									<tr>
														<td align="center" valign="top" class="black_ar">&nbsp;</td>
														<td align="left" valign="top" nowrap class="black_ar"><bean:message key="shipment.sender" bundle="msg.shippingtracking"/></td>
														<td align="left" valign="top" class="black_ar"><c:out value="${shipmentReceivingForm.senderContactPersonName}"/>
															  	<html:hidden name="shipmentReceivingForm" property="senderContactPersonName"/>
															  	<html:hidden name="shipmentReceivingForm" property="senderContactId"/></td>
														<td align="left" valign="top">&nbsp;</td>
														<td align="left" valign="top">&nbsp;</td>
														<td align="left" valign="top" class="black_ar"><bean:message key="shipment.receiver" bundle="msg.shippingtracking"/></td>
														<td align="left" valign="top" class="black_ar"><c:out value="${shipmentReceivingForm.receiverContactPersonName}"/>
														  	<html:hidden name="shipmentReceivingForm" property="receiverContactPersonName"/>
														  	<html:hidden name="shipmentReceivingForm" property="receiverContactId"/>
														 </td>
													</tr>
                
													<tr>
										                  <td align="center" valign="top" class="black_ar">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.senton" bundle="msg.shippingtracking"/></td>
										                  <td align="left" valign="top" class="black_ar"><label>
															<c:out value="${shipmentReceivingForm.sendDate}"/>
										                  	<html:hidden name="shipmentReceivingForm" property="sendDate"/>
										                  </label></td>
										                  <td align="left" valign="top">&nbsp;</td>
										
										                  <td align="left" valign="top">&nbsp;</td>
										                  <td align="left" valign="top" class="black_ar"><bean:message key="shipment.senderComments" bundle="msg.shippingtracking"/>:</td>
										                  <td align="left" valign="top" class="black_ar">
										                  	<c:out value="${shipmentReceivingForm.senderComments}"/>
										                  	<html:hidden name="shipmentReceivingForm" property="senderComments"/>
										                  </td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td align="left" >&nbsp;</td>
										</tr>
          								<!-- Specimens -->
										<logic:notEmpty name="shipmentReceivingForm" property="specimenCollection">
										<% int specimenCount=0;%>
											<tr>
												<td align="left" valign="top">
													<table width="100%" border="0" cellpadding="4" cellspacing="0" >
									           			<tr>
															<td height="25" colspan="7"  class="tableheading"><span class="black_ar_b">&nbsp;
																<bean:message key="specimen.details"  bundle="msg.shippingtracking"/> </span>
															</td>
														</tr>
														<tr class="subtd">
																<td width="5%">
																	<input type="checkbox" name="chkSelectAllSpecimens" id="chkSelectAllSpecimens" value="chkSelectAllSpecimens" onClick ="selectAllCheckBox('chkSelectAllSpecimens','chkSpecimenId')">
																</td>
																<td width="15%">
																	<bean:message key="specimen.label" bundle="msg.shippingtracking"/>
																</td>
																<td width="15%">
																	<bean:message key="specimen.barcode" bundle="msg.shippingtracking"/>
																</td>
																<td width="15%">
																	<bean:message key="shipment.status" bundle="msg.shippingtracking"/>
																</td>
																<td width="15%">
																	<bean:message key="shipment.storage.location" bundle="msg.shippingtracking"/>
																</td>
																<td width="15%">
																	<A class="black_ar"
						    name="parent" HREF="#parent" onClick="ApplyToAll(this)"
						    onmouseover="Tip(' Apply first location to all')"><bean:message
						    key="aliquots.applyFirstToAll" /></A>
																</td>

																<td width="*">
																	&nbsp;
																</td>
														</tr>

														<c:set var="Rulecounter" value="0" scope="page"/>
														<logic:iterate id="specimenItem" name="shipmentReceivingForm" property="specimenCollection">
														<%
														specimenCount++;
														rowNumber=""+specimenCount;
														dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
														%>
														<c:set var="Rulecounter" value="${Rulecounter+1}"/>
									                  	<tr>
																<td class="black_ar" width="2%">
																	<input type="checkbox" name="chkSpecimenId" id="chkSpecimenId" value="<c:out value="${specimenItem.id}"/>"/>
										                    		<html:hidden name="specimenItem" property="id" indexed="true" />
																</td>
																<td class="black_ar" width="15%">
																	<c:out value="${specimenItem.label}"/>
																	<html:hidden name="specimenItem" property="label" indexed="true" />
																</td>
																<td class="black_ar" width="15%">
																	<c:out value="${specimenItem.barcode}"/>
																	<html:hidden name="specimenItem" property="barcode" indexed="true" />
																</td>
																<td class="black_ar" width="15%"> 
																	<label>
																	<html:select name="specimenItem" property="activityStatus" styleClass="black_ar" indexed="true">
																		<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACCEPT%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACCEPT%></html:option>
																		<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_DESTROY%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_DESTROY%></html:option>
																		<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND%></html:option>
																	</html:select></label>       
																</td>
															
																	
																	
																	<%  String operation = (String)request.getAttribute(Constants.OPERATION); 
																		
																		boolean readOnly=true;
																		if(operation.equals(Constants.ADD))
																			readOnly=false;
																	%>
																							
																	<%-- n-combo-box start --%>
								<%
								String[] labelNames = {"ID","Pos1","Pos2"};
								labelNames = Constants.STORAGE_CONTAINER_LABEL;
								String[] attrNames = { "specimenDetails(containerId_"+((edu.wustl.catissuecore.domain.Specimen)specimenItem).getId()+")", "specimenDetails(pos1_"+((edu.wustl.catissuecore.domain.Specimen)specimenItem).getId()+")", "specimenDetails(pos2_"+((edu.wustl.catissuecore.domain.Specimen)specimenItem).getId()+")"};
								initValues=(String[])form.getSpecimenDetails("Specimen:"+(specimenCount)+"_initialValues");
								if(initValues==null)
								{
									initValues=new String[3];
								}																	
								String onChange = "onCustomListBoxChange(this)";																	
								
								                                        boolean disabled = false;
																		boolean buttonDisabled = false;
																		if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
																		{
																			disabled = true;
																		}	
																					
									String specimenId=(String)request.getAttribute("specimenId");
									edu.wustl.catissuecore.domain.Specimen specimen=(edu.wustl.catissuecore.domain.Specimen)specimenItem;
									int radioSelected = 1;
											if(specimenId!=null && !specimenId.trim().equals("") && specimenId.equals(specimen.getId().toString()))
																		{
																				if(request.getAttribute("stContSelection")!=null)
																				{
																					radioSelected=Integer.parseInt((String)request.getAttribute("stContSelection"));
																				}
																		}

																		if(specimenId!=null && !specimenId.trim().equals("") && !specimenId.equals(specimen.getId().toString()))
																		{
																				String radioSelectedString=(String)form.getSpecimenDetails("specimenStorageLocation_"+specimen.getId());
																				if(radioSelectedString!=null && !radioSelectedString.trim().equals(""))
																				{
																						radioSelected=Integer.parseInt(radioSelectedString);
																				}
																		}
																		else if(specimenId==null)
																		{
																				String radioSelectedString=(String)form.getSpecimenDetails("specimenStorageLocation_"+specimen.getId());
																				if(radioSelectedString!=null && !radioSelectedString.trim().equals(""))
																				{
																						radioSelected=Integer.parseInt(radioSelectedString);
																				}
																		}
																	%>
									
																	<c:set var="storageLocSelectName">specimenDetails(specimenStorageLocation_<bean:write name="specimenItem" property="id"/>)</c:set>
																    <jsp:useBean id="storageLocSelectName" type="java.lang.String"/>
																	<c:set var="storageLocSelectId">specimenStorageLocation_<bean:write name="specimenItem" property="id"/></c:set>
																    <jsp:useBean id="storageLocSelectId" type="java.lang.String"/>				
																					
																	<html:hidden property="<%=storageLocSelectName%>" styleId="<%=storageLocSelectId%>" styleClass="black_ar" value="3"/>
																	
																<td width="*">
																	<c:set var="spAutoDivName">autoDiv_<c:out value="${specimenItem.id}"/></c:set>
																	<jsp:useBean id="spAutoDivName" type="java.lang.String"/>

																																				</div>
																	<c:set var="spManualDivName">manualDiv_<c:out value="${specimenItem.id}"/></c:set>
																	<jsp:useBean id="spManualDivName" type="java.lang.String"/>

																		<div style="display:block" id="<%=spManualDivName%>">
																		<table cellpadding="0" cellspacing="0" border="0" >
																				<tr>
																					<td class="groupelements">
												<c:set var="selectedContainerName">specimenDetails(selectedContainerName_<bean:write name="specimenItem" property="id"/>)</c:set>
																						<jsp:useBean id="selectedContainerName" type="java.lang.String"/>
											<c:set var="selectedContainerId">selectedContainerName_<bean:write name="specimenItem" property="id"/></c:set>
																						<jsp:useBean id="selectedContainerId" type="java.lang.String"/>
									
					
																	<td width="50%" align="left" class="black_ar">
								<input type="hidden" class="classForCount" name="<%=selectedContainerName%>" id="<%=selectedContainerName%>"/>
						<div>
							<table border="0" width="29%" id="outerTable2" cellspacing="0" cellpadding="0">
								<tr>
									<td align="left" width="88%" height="100%" >
										<div id="scDropDownIddiv_<bean:write name='specimenItem' property='id'/>" class="x-form-field-wrap" >
											<input id="storageContainerDropDown_<bean:write name='specimenItem' property='id'/>"
													onkeydown="keyNavigation(event,containerDropDownInfo_<bean:write name='specimenItem' property='id'/>,scGrid_<bean:write name='specimenItem' property='id'/>,scGridVisible_<bean:write name='specimenItem' property='id'/>);"
													onKeyUp="autoCompleteControl(event,containerDropDownInfo_<bean:write name='specimenItem' property='id'/>,scGrid_<bean:write name='specimenItem' property='id'/>);"
													onClick="noEventPropogation(event)"
													autocomplete="off"
													size="30"
													class="black_ar_new x-form-text x-form-field x-form-focus"/><img id="scDropDownId" style="top : 0px !important;" class="x-form-trigger x-form-arrow-trigger" 
												onclick="showHideStorageContainerGrid(event,'storageContainer_<bean:write name="specimenItem" property="id"/>','storageContainerDropDown_<bean:write name="specimenItem" property="id"/>',scGridVisible_<bean:write name='specimenItem' property='id'/>,containerDropDownInfo_<bean:write name='specimenItem' property='id'/>,scGrid_<bean:write name='specimenItem' property='id'/>);"
												src="images/uIEnhancementImages/s.gif"/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									<div id="storageContainer_<bean:write name='specimenItem' property='id'/>" style="z-index: 100"
										onClick="noEventPropogation(event)">
									<div id="storageContainerGrid_<bean:write name='specimenItem' property='id'/>" style="height: 40px;"
										onClick="noEventPropogation(event)"></div>
									<div id="storageContainerPagingArea_<bean:write name='specimenItem' property='id'/>" onClick="noEventPropogation(event)"></div>
									<div id="storageContainerInfoArea_<bean:write name='specimenItem' property='id'/>" onClick="noEventPropogation(event)"></div>
									</div>
									</td>
								</tr>
							</table>
					</td>
					</td>
																					</td>
																					<td class="groupelements" style="padding-left:25">
																						<c:set var="pos1Name">specimenDetails(position1_<bean:write name="specimenItem" property="id"/>)</c:set>
																						<jsp:useBean id="pos1Name" type="java.lang.String"/>
																						<c:set var="pos1Id">position1_<bean:write name="specimenItem" property="id"/></c:set>
																						<jsp:useBean id="pos1Id" type="java.lang.String"/>
									
																						<html:text styleClass="black_ar"  size="1" styleId="<%=pos1Id%>" property="<%=pos1Name%>" disabled= "false"/>
																					</td>
																					<td class="groupelements">
																						<c:set var="pos2Name">specimenDetails(position2_<bean:write name="specimenItem" property="id"/>)</c:set>
																						<jsp:useBean id="pos2Name" type="java.lang.String"/>
																						<c:set var="pos2Id">position2_<bean:write name="specimenItem" property="id"/></c:set>
																						<jsp:useBean id="pos2Id" type="java.lang.String"/>
									
																						<html:text styleClass="black_ar"  size="1" styleId="<%=pos2Id%>" property="<%=pos2Name%>" disabled= "false"/>
																					</td>
																					<td class="groupelements">
	
																						<c:set var="stContainerName">specimenDetails(containerId_<bean:write name="specimenItem" property="id"/>)</c:set>
																						<jsp:useBean id="stContainerName" type="java.lang.String"/>
																						<c:set var="stContainerId">containerId_<bean:write name="specimenItem" property="id"/></c:set>
																						<jsp:useBean id="stContainerId" type="java.lang.String"/>
									
																						<html:hidden styleId="<%=stContainerId%>" property="<%=stContainerName%>"/>
																						<c:set var="functionCall">showPopUp('<bean:write name="specimenItem" property="id"/>','storageContainerDropDown_<bean:write name="specimenItem" property="id"/>','<c:out value="${pos1Id}"/>','<c:out value="${pos2Id}"/>','<c:out value="${stContainerId}"/>','<c:out value="${specimenItem.specimenClass}"/>','')</c:set>
																						<jsp:useBean id="functionCall" type="java.lang.String"/>
									
																						<c:set var="buttonName">containerMap_<bean:write name="specimenItem" property="id"/></c:set>
																						<jsp:useBean id="buttonName" type="java.lang.String"/>

																						<html:button
																						styleId="mapButton_${Rulecounter}" styleClass="blue_ar" property="<%=buttonName%>" onclick="<%=functionCall%>" disabled= "false">
																									<bean:message key="buttons.map" bundle="msg.shippingtracking"/>
																						</html:button>
																					</td>
																				</tr>
																			</table>
																	</div>			
																</td>
									                  		</tr>
														</logic:iterate>
													</table>           
												</td>
											</tr>
											<tr>
							                    	<td height="40" colspan=6 class="black_ar">
											                	<img src="images/shippingtracking/ic_reject_return.gif" alt="<bean:message key="specimen.reject.resend.tooltip" bundle="msg.shippingtracking"/>" width="122" height="16"  align="absmiddle" onclick="setRejectAndResend('specimen')">
											                    <img src="images/shippingtracking/sep.gif" width="1" height="19" hspace="5" align="absmiddle">
											                    <img src="images/shippingtracking/ic_reject_destroy.gif" alt="<bean:message key="specimen.reject.destroy.tooltip" bundle="msg.shippingtracking"/>" width="127" height="16"  align="absmiddle" onclick="setRejectAndDestroy('specimen')">
							                    	</td>
										 	</tr>
										</logic:notEmpty>
										<tr>
											<td align="left" valign="top" colspan=6>&nbsp;</td>
										</tr>
										<!-- Containers -->
										<logic:notEmpty name="shipmentReceivingForm" property="containerCollection">
										<% int containerCount=1;%>
											<tr>
												<td align="left" valign="top">
													<table width="100%" border="0" cellpadding="4" cellspacing="0" class="whitetable_bg">
														<tr>
															<td height="25" colspan="6"  class="tableheading"><span class="black_ar_b">&nbsp;<bean:message key="container.details" bundle="msg.shippingtracking"/></span><span class="style1"></span></td>
														</tr>
														<tr class="subtd">
															<td width="5%" height="25"><span class="black_ar">
																<input type="checkbox" name="chkSelectAllContainers" id="chkSelectAllContainers" value="checkbox" onClick ="selectAllCheckBox('chkSelectAllContainers','chkStorgaeContainerId')"></span>
															</td>

											                <td width="130"><b><bean:message key="container.label" bundle="msg.shippingtracking"/></b></td>
											                <td width="130"><b><bean:message key="container.barcode" bundle="msg.shippingtracking"/></b></td>
											                <td width="130"><b><bean:message key="shipment.status" bundle="msg.shippingtracking"/></b></td>
											                <td width="130"><b><bean:message key="shipment.storage.location" bundle="msg.shippingtracking"/></b></td>
											                <td width="40%">&nbsp;</td>
														</tr>
              											
              											
														<logic:iterate id="containerItem" name="shipmentReceivingForm" property="containerCollection" >
															<tr>
																<td height="25" class="black_ar" width="2%">
																	<input type="checkbox" name="chkStorgaeContainerId" id="chkStorgaeContainerId" value="<c:out value="${containerItem.id}"/>"/>
																	<html:hidden name="containerItem" property="id" indexed="true" />
																</td>
																<td class="black_ar" width="15%"><c:out value="${containerItem.name}"/>
																		<html:hidden name="containerItem" property="name" indexed="true" />
																</td>
																<td class="black_ar" width="15%"><c:out value="${containerItem.barcode}"/>
																		<html:hidden name="containerItem" property="barcode" indexed="true" />
																</td>
																<td width="15%"><label>
												                    	<html:select name="containerItem" property="activityStatus" styleClass="black_ar" indexed="true">
																			<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACCEPT%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACCEPT%></html:option>
																			<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_DESTROY%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_DESTROY%></html:option>
																			<html:option value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND%>"><%=edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND%></html:option>
																		</html:select></label>                    
																</td>
																<td class="black_ar" width="15%">
																	
												<%	
												//ShipmentReceivingForm form=(ShipmentReceivingForm)request.getAttribute("shipmentReceivingForm");										
												String[] labelNames = new String[]{"ID","Pos1","Pos2"};
												labelNames = Constants.STORAGE_CONTAINER_LABEL;
												String[] attrNames =new String[] { "containerDetails(parentContainerId_"+((edu.wustl.catissuecore.domain.Container)containerItem).getId()+")", "containerDetails(positionDimensionOne_"+((edu.wustl.catissuecore.domain.Container)containerItem).getId()+")", "containerDetails(positionDimensionTwo_"+((edu.wustl.catissuecore.domain.Container)containerItem).getId()+")"};
																								
																		String[] initValuesContainer = new String[3];
																		List initValuesListContainer = (List)request.getAttribute("containerInitValues");
																		/*if(initValuesListContainer != null)
																		{
																			initValuesContainer = (String[])initValuesListContainer.get(0);
																		}*/
																		
																		initValuesContainer=(String[])form.getContainerDetails("Container:"+(containerCount++)+"_initialValues");
																		if(initValuesContainer==null)
																		{
																			initValuesContainer=new String[3];
																		}
																		String onChange = "onCustomListBoxChange(this),onParentContainerChange1(this)";
																	
																		edu.wustl.catissuecore.domain.Container container=(edu.wustl.catissuecore.domain.Container)containerItem;
																			
																		boolean disabled = false;
																		boolean buttonDisabled = false;
																		if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
																		{
																			disabled = true;
																		}	
																				
																		String radioSelectedCotainer = "Site";

																		String containerId=(String)request.getAttribute("containerObjectId");
																		
																		if(containerId!=null && !containerId.trim().equals("") && containerId.equals(container.getId().toString()))
																		{
																				if(request.getAttribute("parentContainerSelected")!=null)
																				{
																					radioSelectedCotainer=(String)request.getAttribute("parentContainerSelected");
																						
																				}
																		}

																		if(containerId!=null && !containerId.trim().equals("") && !containerId.equals(container.getId().toString()))
																		{
																						radioSelectedCotainer=(String)form.getContainerDetails("containerStorageLocation_"+container.getId());
																		}
																		else if(containerId==null)
																		{
																				String radioSelected=(String)form.getContainerDetails("containerStorageLocation_"+container.getId());
																				if(radioSelected!=null && !radioSelected.trim().equals(""))
																				{
																						radioSelectedCotainer=radioSelected;
																				}
																		}

																		String virtualDisplayStyle="";
																		if(radioSelectedCotainer.equals("Site"))
																		{
																			virtualDisplayStyle="display:block";
																			autoDisplayStyle = "display:none";
																			manualDisplayStyle = "display:none";
																		}
																		else if(radioSelectedCotainer.equals("Auto"))
																		{						
																			virtualDisplayStyle="display:none";
																			autoDisplayStyle = "display:block";
																			manualDisplayStyle = "display:none";
																		}
																		else if(radioSelectedCotainer.equals("Manual"))
																		{
																			virtualDisplayStyle="display:none";
																			autoDisplayStyle = "display:none";
																			manualDisplayStyle = "display:block";							
																		}
																	%>
																	
																	<%=ScriptGenerator.getJSForOutermostDataTable()%>
																	<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
																	
																	<%
																		if(dataMap == null)
																		{
																			dataMap = new HashMap();	
																		}
																	%>																	
																	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
									
																	<c:set var="contStLocName">containerDetails(containerStorageLocation_<bean:write name="containerItem" property="id"/>)</c:set>
																    <jsp:useBean id="contStLocName" type="java.lang.String"/>
																	<c:set var="contStLocId">containerStorageLocation_<bean:write name="containerItem" property="id"/></c:set>
																    <jsp:useBean id="contStLocId" type="java.lang.String"/>				
																					
																	<html:select property="<%=contStLocName%>" styleId="<%=contStLocId%>" styleClass="black_ar" onchange="parentContainerTypeChanged(this)">
																		<html:option value="Site">Site</html:option>
																		<!-- <html:option value="Auto">Auto</html:option> -->
																		<html:option value="Manual">Manual</html:option>
																	</html:select>
																</td>
																<td class="black_ar" width="15%">

																	<c:set var="contParentContSiteName">parentContainerSite_<c:out value="${containerItem.id}"/></c:set>
																	<jsp:useBean id="contParentContSiteName" type="java.lang.String"/>

																	<div id="<%=contParentContSiteName%>" style="<%=virtualDisplayStyle%>">
																		  <table border="0" cellpadding="0" cellspacing="0" class="groupElements">
																			<tr>				
																				<label>
																				<c:set var="siteControlName">containerDetails(siteId_<bean:write name="containerItem" property="id"/>)</c:set>
																			    <jsp:useBean id="siteControlName" type="java.lang.String"/>

																				<c:set var="siteControlId">siteId_<bean:write name="containerItem" property="id"/></c:set>
																			    <jsp:useBean id="siteControlId" type="java.lang.String"/>

																				<html:select property="<%=siteControlName%>" styleClass="black_ar" styleId="<%=siteControlId%>" size="1" 
																				   onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
																					<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
																				</html:select>
																				</label>						
																				&nbsp;
																				<html:link href="#" styleId="newSite"  styleClass="view" onclick="addNewAction('StorageContainerAddNew.do?addNewForwardTo=site&forwardTo=storageContainer&addNewFor=site')">
																					<bean:message key="buttons.addNew" bundle="msg.shippingtracking"/>
																				</html:link>
																				</tr>
													                         </table>
																	</div>

																	<c:set var="contParentContAutoName">parentContainerAuto_<c:out value="${containerItem.id}"/></c:set>
																	<jsp:useBean id="contParentContAutoName" type="java.lang.String"/>
																	
																	<div id="<%=contParentContAutoName%>" style="<%=autoDisplayStyle%>">
																		  <table border="0" cellpadding="0" cellspacing="0" class="groupElements">
																			<tr>
																				<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
																								attributeNames="<%=attrNames%>" 
																								initialValues="<%=initValuesContainer%>"  
																styleClass = "<%=styClass%>" 
																								tdStyleClassArray="<%=tdStyleClassArray%>"
																								tdStyleClass = "<%=tdStyleClass%>" 
																	labelNames="<%=labelNames%>" 
																			rowNumber="<%=rowNumber%>" 
																								onChange = "<%=onChange%>"
																								formLabelStyle="nComboGroup"
																								noOfEmptyCombos = "3"/>
																								</tr>
													
																			</tr>
													                      </table>
																	</div>
																	
																	<c:set var="contParentContManualName">parentContainerManual_<c:out value="${containerItem.id}"/></c:set>
																	<jsp:useBean id="contParentContManualName" type="java.lang.String"/>

																	<div id="<%=contParentContManualName%>" style="<%=manualDisplayStyle%>">
												    					 <table cellpadding="0" cellspacing="0" border="0" >
																									<tr>
																										<td class="groupelements">
																											<c:set var="contSelectedContainerName">containerDetails(selectedContainerNameCont_<bean:write name="containerItem" property="id"/>)</c:set>
																											<jsp:useBean id="contSelectedContainerName" type="java.lang.String"/>
																											<c:set var="contSelectedContainerId">selectedContainerNameCont_<bean:write name="containerItem" property="id"/></c:set>
																											<jsp:useBean id="contSelectedContainerId" type="java.lang.String"/>
														
																											<html:text styleClass="black_ar"  size="20" styleId="<%=contSelectedContainerId%>" property="<%=contSelectedContainerName%>" disabled= "false"/>
																										</td>
																										<td class="groupelements">
																											<c:set var="contpos1Name">containerDetails(contPosition1_<bean:write name="containerItem" property="id"/>)</c:set>
																											<jsp:useBean id="contpos1Name" type="java.lang.String"/>
																											<c:set var="contpos1Id">contPosition1_<bean:write name="containerItem" property="id"/></c:set>
																											<jsp:useBean id="contpos1Id" type="java.lang.String"/>
														
																											<html:text styleClass="black_ar"  size="1" styleId="<%=contpos1Id%>" property="<%=contpos1Name%>" disabled= "false"/>
																										</td>
																										<td class="groupelements">
																											<c:set var="contpos2Name">containerDetails(contPosition2_<bean:write name="containerItem" property="id"/>)</c:set>
																											<jsp:useBean id="contpos2Name" type="java.lang.String"/>
																											<c:set var="contpos2Id">contPosition2_<bean:write name="containerItem" property="id"/></c:set>
																											<jsp:useBean id="contpos2Id" type="java.lang.String"/>
														
																											<html:text styleClass="black_ar"  size="1" styleId="<%=contpos2Id%>" property="<%=contpos2Name%>" disabled= "false"/>
																										</td>
																										<td class="groupelements">
																										
																											<c:set var="containerIdControlName">containerDetails(containerIdContainer_<bean:write name="containerItem" property="id"/>)</c:set>
																											<jsp:useBean id="containerIdControlName" type="java.lang.String"/>
																											<c:set var="containerIdControlid">containerIdContainer_<bean:write name="containerItem" property="id"/></c:set>
																											<jsp:useBean id="containerIdControlid" type="java.lang.String"/>
														
																											<html:hidden styleId="<%=containerIdControlid%>" property="<%=containerIdControlName%>"/>
																											
																											<c:set var="contbuttonName">containerMapCont_<bean:write name="containerItem" property="id"/></c:set>
																											<jsp:useBean id="contbuttonName" type="java.lang.String"/>

																											<c:set var="mapButtonClickedMethodName">mapButtonClicked_<bean:write name="containerItem" property="id"/>()</c:set>
																											<jsp:useBean id="mapButtonClickedMethodName" type="java.lang.String"/>
														
																											<html:button styleClass="blue_ar" property="<%=contbuttonName%>" styleId="<%=contbuttonName%>" onclick="<%=mapButtonClickedMethodName%>" disabled= "false">
																														<bean:message key="buttons.map" bundle="msg.shippingtracking"/>
																											</html:button>
																											
																											<c:set var="frameUrlCont">ShowFramedPage.do?pageOf=pageOfSpecimen&selectedContainerName=<c:out value="${contSelectedContainerId}"/>&pos1=<c:out value="${contpos1Id}"/>&pos2=<c:out value="${contpos2Id}"/>&containerId=<c:out value="${containerIdControlid}"/>&storageType=</c:set>
																											<jsp:useBean id="frameUrlCont" type="java.lang.String"/>
																											<script>
																										//		setParentContainerType("<%=contbuttonName%>");
																												// Patch ID: Bug#3090_11
																												function mapButtonClicked_<bean:write name="containerItem" property="id"/>()
																												 {	
																												    var platform = navigator.platform.toLowerCase();
																													var selectedContainerNameCont='selectedContainerNameCont_'+<bean:write name="containerItem" property="id"/>;
																													var contPosition1='contPosition1_'+<bean:write name="containerItem" property="id"/>;
																													var contPosition2='contPosition2_'+<bean:write name="containerItem" property="id"/>;
																													var selectedContainerNameContValue=document.forms[0].selectedContainerNameCont_<bean:write name="containerItem" property="id"/>.value;
																													var contPosition1Value=document.forms[0].contPosition1_<bean:write name="containerItem" property="id"/>.value;
																													var contPosition2Value=document.forms[0].contPosition2_<bean:write name="containerItem" property="id"/>.value;
																													if(""!=selectedContainerNameContValue)
																													{
																														loadDHTMLXWindowForMultipleSpecimen(selectedContainerNameCont,contPosition1,contPosition2);
																													}
																													else
																													{
																													if (platform.indexOf("m"))
																													 {
																													    StorageMapWindowShipReceive('<%=frameUrlCont%>','name',screen.width,screen.height,'no','<%=contSelectedContainerId%>',selectedContainerNameContValue,contPosition1Value,contPosition2Value);
																													 }
																													else
																													 {
																													   	StorageMapWindowShipReceive('<%=frameUrlCont%>','name','800','600','no','<%=contSelectedContainerId%>',selectedContainerNameContValue,contPosition1Value,contPosition2Value);
																													  }
																													  }
																												}																																																						
																											</script>																											
																										</td>
																				</tr>
																			</table>
																	</div>
																</td>
															</tr>
														</logic:iterate> 
														<tr>
															<td height="40" colspan="6" >
											                   <span class="black_ar">
											                	<img src="images/shippingtracking/ic_reject_return.gif" alt="<bean:message key="container.reject.resend.tooltip" bundle="msg.shippingtracking"/>" width="122" height="16"  align="absmiddle" onclick="setRejectAndResend('container')">
											                	<img src="images/shippingtracking/sep.gif" width="1" height="19" hspace="5" align="absmiddle">
											                	<img src="images/shippingtracking/ic_reject_destroy.gif" alt="<bean:message key="container.reject.destroy.tooltip" bundle="msg.shippingtracking"/>" width="127" height="16"  align="absmiddle" onclick="setRejectAndDestroy('container')">
											                   </span>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
									<tr>
            							<td align="left" valign="top" colspan=6>&nbsp;</td>
									</tr>
									<tr>
										<td align="left" valign="top" colspan=6>
											<table width="100%" border="0" cellpadding="4" cellspacing="0" class="whitetable_bg">
												<tr>
													<td height="25"  class="tableheading"><span class="black_ar_b">&nbsp;<bean:message key="shipment.receiver.comments" bundle="msg.shippingtracking"/></span><span class="style1"></span></td>
												</tr>
												<tr>
													<td height="25" class="black_ar"><span class="black_ar_b">
														<html:textarea property="receiverComments" styleId="receiverComments" name="shipmentReceivingForm" styleClass="black_ar" cols="90" rows="3" />
														</span>
													</td>
												</tr>
												<tr>
													<td height="15" ></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td height="30" valign="middle" class="buttonbg">
								            	<logic:notEqual name="shipmentReceivingForm" property="activityStatus" value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACTIVITY_STATUS_RECEIVED%>">
					<input type="button" value="Submit" class="blue_ar_b" accesskey="A" onclick="submitForm()"/>
            	</logic:notEqual>
            	<logic:equal name="shipmentReceivingForm" property="activityStatus" value="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACTIVITY_STATUS_RECEIVED%>">
					<input type="button" value="Submit" class="blue_ar_b" accesskey="A" disabled="true" onclick="submitForm()"/>
            	</logic:equal>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
  </td>
 </tr>
</table>
</body>
</html>

