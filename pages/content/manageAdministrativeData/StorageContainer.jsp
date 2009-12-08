<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.common.util.global.Status" %>
<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="jss/script.js" type="text/javascript"></script>
	
<%
		//StorageContainerForm form = (StorageContainerForm)request.getAttribute("storageContainerForm");
		String strCheckStatusForCont = "checkActivityStatus(this,'" + Constants.CONTAINER_DELETE_MAPPING + "')";
		String pageOf = request.getParameter(Constants.PAGE_OF);
        String operation = (String) request.getAttribute(Constants.OPERATION);
		//String containerNumber=(String)request.getAttribute("ContainerNumber");
        String formName;
        String printAction ="printStorageContainer";
		//List siteForParent = (List)request.getAttribute("siteForParentList");
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
        boolean readOnlyValue;
        
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_CONTAINER_EDIT_ACTION;
            readOnlyValue = true;            
        }
        else
        {
            formName = Constants.STORAGE_CONTAINER_ADD_ACTION;
            printAction ="printStorageContainer";
            readOnlyValue = false;
        }
	
		Object obj = request.getAttribute("storageContainerForm");

		Map map = null;
		String label1 = null;
		String label2 = null;
		
		StorageContainerForm form;
		if(obj != null && obj instanceof StorageContainerForm)
		{
			form = (StorageContainerForm)obj;
			//map = form.getValues();

			label1 = form.getOneDimensionLabel();
			label2 = form.getTwoDimensionLabel();

			if(label1 == null)
			{
				label1 = "Dimension One";
				label2 = "Dimension Two";
			}
		}
		else
		{
			form = (StorageContainerForm)request.getAttribute("storageContainerForm");
		}
		
		int siteOrContainerSelected = form.getCheckedButton();
		int dropdownOrTextboxSelected = form.getStContSelection();
		
		boolean dropDownDisable = false;
		boolean textBoxDisable = false;		
		boolean containerRadioDisable = false;
		
		
		if(siteOrContainerSelected == 1)
		{
			dropDownDisable = true;
			textBoxDisable = true;			
			containerRadioDisable = true;
		}
		else if(siteOrContainerSelected == 2)
		{				
			if(dropdownOrTextboxSelected == 1)
			{
				textBoxDisable = true;
				dropDownDisable = false;	
			}								
			else if(dropdownOrTextboxSelected == 2)
			{
				dropDownDisable = true;	
				textBoxDisable = false;
			}
		}
		
%>

<head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<style>
	.hidden
	{
	 display:none;
	}

</style>
	<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
	<script language="JavaScript">
		function checkNoOfContainers(action,formField)
		{
			var operation = "<%=operation%>";
			//alert("operation "+operation);
			if(operation == "add")
			{
				if(document.forms[0].noOfContainers.value > 1)
				{
					action = "<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>";
					action = action + "?pageOf="+"<%=Constants.PAGE_OF_CREATE_SIMILAR_CONTAINERS%>"+"&menuSelected=7";
					/**
					 * Name : Vijay_Pande
					 * Bug ID: 4145
					 * Patch ID: 4145_1 
					 * See also: -
					 * Description: There were two messages while adding storage container. One was an error message that container does not exists.
					 * Error was id of Storage Type was set in the request parameter. Therefore id=0 is set in request parameter.
					 */
					action=action+"&id=0";
				}
				else
				{
					action=action+"?id=0";
				}
				/**-- Patch ends here --*/
			}
			//confirmDisable(action,formField);

			// Added for print
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{
             	if(operation == "add")
                {  
   			    	setSubmitted('ForwardTo','<%=printAction%>','StorageContainerSearch');
				}
				else if(operation == "edit")
				{
					setSubmitted('ForwardTo','<%=printAction%>','StorageContainerEdit');
				}
			}
			confirmDisable(action,formField);
			document.getElementById('printCheckbox').checked = false;
		}

		function setSubmitted(forwardTo,printaction,nextforwardTo)
		{
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{	
				setSubmittedForPrint(forwardTo,printaction,nextforwardTo);
			}
			else
			{
			  setSubmittedFor(forwardTo,nextforwardTo);
			}
		
		}

	/*	function onRadioButtonClick(element)
		{
			var radioArray = document.getElementsByName("stContSelection");		 
			//if site radio button is selected.
			if(element.value == 1)
			{
			  
				document.forms[0].siteId.disabled = false;

				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;				
				document.forms[0].containerMap.disabled=true;

				document.forms[0].stContSelection[0].disabled=true;
				document.forms[0].stContSelection[1].disabled=true;

				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;
				document.forms[0].stContSelection.disabled=true;
			}
			else //if parent container radio button is selected.
			{
				
				document.forms[0].siteId.disabled = true;
				document.forms[0].stContSelection[0].disabled=false;
				document.forms[0].stContSelection[1].disabled=false;

				if (radioArray[0].checked) 
				{
				   
					document.forms[0].customListBox_1_0.disabled = false;
					document.forms[0].customListBox_1_1.disabled = false;
					document.forms[0].customListBox_1_2.disabled = false;
					document.forms[0].containerMap.disabled=true;
					document.forms[0].selectedContainerName.disabled=true;
					document.forms[0].pos1.disabled=true;
					document.forms[0].pos2.disabled=true;	
									
				} 
				else 
				{
				  
					document.forms[0].customListBox_1_0.disabled = true;
					document.forms[0].customListBox_1_1.disabled = true;
					document.forms[0].customListBox_1_2.disabled = true;
					document.forms[0].containerMap.disabled=false;
					document.forms[0].selectedContainerName.disabled=false;
					document.forms[0].pos1.disabled=false;
					document.forms[0].pos2.disabled=false;					
				}
				var ele0 = document.getElementById("customListBox_1_0");
				onParentContainerChange1(ele0);
				//window.location.reload();
			}
		}
	*/	
		function onTypeChange(element)
		{
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&typeChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		// vipin :- without ajax
	/*	function onSiteChange(dd)
		{
			var siteElement = document.getElementById("siteId");
			document.forms[0].siteName.value = siteElement.options[siteElement.selectedIndex].text;
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isSiteChanged=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}*/
		
	/*	function onChangeGetCPs(element)
		{
			var contElement = document.forms[0].customListBox_1_0;
		//	document.forms[0].parentContainerId.value = contElement.options[contElement.selectedIndex].text;
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isContainerChanged=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		function onContManualChange()
		{
			var contElement = document.forms[0].selectedContainerName;
			document.forms[0].selectedContainerName.value = contElement.options[contElement.selectedIndex].text;
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isContainerChanged=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}*/
		// vipin:- with ajax
		function onChangeGetCPs(data)
		{
			
			var request = newXMLHTTPReq();
			request.onreadystatechange = function requestHandler()
			{
				 if(request.readyState == 4)
				 {       //When response is loaded
					if(request.status == 200)
					{   
						var response = request.responseText; 
							
						var jsonResponse = eval('('+ response+')');
						var hasValue = false;

						if(jsonResponse.locations!=null)
						{
						   var num = jsonResponse.locations.length; 
							 var cpSelBoxObj =document.getElementById('collectionIds');
								 clearSelBoxList(cpSelBoxObj);// from catissueSuite.js
								 for(var i=0; i<num;i++)
								{
								   var cpName =jsonResponse.locations[i].cpName;
								   var cpId =jsonResponse.locations[i].cpValue;
									
								   var myNewOption = new Option(cpName,cpId);     
								   cpSelBoxObj.options[i] = myNewOption;
								}
						  }

					   }
				    }
			}
			request.open("POST","StorageContainer.do",true);
            request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            request.send(data);
		}
		function onParentlocationChange(element)
		{
			var parentEleType ="";
			if(element.name == "siteId")
			{
				parentEleType ="parentContSite"  ;
				var data="operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&&isContainerChanged=true&parentEleType="+parentEleType+"&siteId="+element.value;
			}
			else if(element.name == "parentContainerId")
			{
				parentEleType ="parentContAuto"  ;
				var data="operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&&isContainerChanged=true&parentEleType="+parentEleType+"&parentContainerId="+element.value;
			}
			else if(element.name == "selectedContainerName")
			{
				parentEleType ="parentContManual"  ;
				var data="operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&&isContainerChanged=true&parentEleType="+parentEleType+"&selectedContainerName="+element.value;
			}

         onChangeGetCPs(data);
		}
		
		

		function onParentContainerChange(element)
		{
			//out of three drop downs if first dropdown that is the storage container name drop is changed
			//then make a server trip to get all CPs 
			if(element.name == "parentContainerId")
			{
				var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isSiteOrParentContainerChange=true";
				document.forms[0].action = action;
				document.forms[0].submit();
			}	
			
		}
		
	

    /***  code using ajax :gets the default cps without refreshing the whole page  ***/
/*	function onParentContainerChange1(element)
	{
		if(element.name == "parentContainerId")
		{ 
			var request = newXMLHTTPReq();
			var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
		
			//no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
			request.onreadystatechange = handlerFunction;
			var action = "operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isSiteOrParentContainerChange=true&parentContainerId="+element.value;
		
			//Open connection to servlet
			request.open("POST","StorageContainer.do",true);	
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		
			//send data to ActionServlet
			request.send(action);
		}
	}
*/
/*	function onResponseUpdate(collectionIdsStr) 
	{

		var collectionIds = document.getElementById("collectionIds");
	
		for (var i=collectionIds.options.length-1; i >= 0;i--) {
			collectionIds.options[i].selected=false;
		}
		if(collectionIdsStr == "-1")
		{
			collectionIds.options[0].selected=true;
		}
		else
		{
			for (var i=collectionIds.options.length-1; i >= 0;i--) {
				if(collectionIdsStr.indexOf(collectionIds.options[i].value) >= 0)
				{
					
					collectionIds.options[i].selected = true;
				}
			}
		}	
		
		
	}
*/
	/*** code using ajax  ***/

		
		
		
		
//  function to insert a row in the inner block

function insRow(subdivtag)
{

		var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
//	var x=document.getElementById(subdivtag).insertRow(q);
	var x=document.getElementById(subdivtag).insertRow(0);
	
	
	// First Cell
	var spreqno=x.insertCell(0);
	spreqno.className="formSerialNumberField";
	sname=(q+1);
	var identifier = "value(StorageContainerDetails:" + (q+1) +"_id)";
	sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
	spreqno.innerHTML="" + sname;

	//Second Cell
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	spreqtype.colSpan=2;
	sname="";
	
	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterName)";
	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        


	spreqtype.innerHTML="" + sname;

	//Third Cell
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";
	sname="";

	name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";
	sname= "";

	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";

	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        

	spreqsubtype.innerHTML="" + sname;
	
	//Fourth Cell
	var checkb=x.insertCell(3);
	checkb.className="formField";
	checkb.colSpan=2;
	sname="";
	var name = "chk_"+(q+1);
	sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')\">";
	checkb.innerHTML=""+sname;
	
}


/*
// using createelement functions
function insRow(subdivtag)
{

		var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	
	var q = r.length;
	var x=document.getElementById(subdivtag).insertRow(q);
	
	// First Cell
	var spreqno=x.insertCell(0);
	spreqno.className="formSerialNumberField";
	sname=(q+1);
	var textNode = document.createTextNode(sname);
	spreqno.appendChild(textNode);

// -------------------------------------------------
	//Second Cell
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	spreqtype.colSpan=2;
	sname="";
	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterName)";
	var txtInp = document.createElement('input');

	txtInp.setAttribute('type', 'text');
	txtInp.setAttribute('name', name);
	txtInp.setAttribute('class', 'formFieldSized10');
	txtInp.setAttribute('id', name );
//--		document.storageContainerForm.appendChild(txtInp);
		
	spreqtype.appendChild(txtInp);
// -------------------------------------------------


	//Third Cell
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";

	var name1 = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";
	var txtInp1 = document.createElement('input');

	txtInp1.setAttribute('type', 'text');
	txtInp1.setAttribute('name', name1);
	txtInp1.setAttribute('class', 'formFieldSized10');
	txtInp1.setAttribute('id', name1 );
	
//--		document.storageContainerForm.appendChild(txtInp1);
	spreqsubtype.appendChild(txtInp1);
// -------------------------------------------------

}
*/

function validate(action,formField)
{
	
	if(validateAny(document.forms[0].holdsStorageTypeIds)==false)
	{
		alert("Selecting All and Other Container Type is not allowed");
	}
	else
	{	
		if(validateAny(document.forms[0].holdsSpecimenClassTypes)==false)
		{
			alert("Selecting All and Other Specimen Class is not allowed");
		}
		else
		{
			if(validateAny(document.forms[0].holdsSpecimenArrTypeIds)==false)
			{	
				alert("Selecting All and Other Specimen Array Type is not allowed");
			}
			else
			{	
				checkNoOfContainers(action,formField);
			}
		}
	}
		
}

function onRadioButtonClickOfSpecimen(element)
{
	var specimenClass = document.getElementById("holdsSpecimenClassTypeIds");
	var specimenType = document.getElementById("holdsSpecimenTypes");
	var specimenArray = document.getElementById("holdsSpecimenArrTypeIds");

	if(element == "Specimen")
	{
		specimenClass.disabled = false;
		specimenType.disabled = false;
		specimenArray.disabled = true;
		var len = specimenArray.length;
		for (var i = 0; i < len; i++) 
		{
			specimenArray.options[i].selected = false;
		}
		
	}
	if(element == "SpecimenArray")
	{
		specimenClass.disabled = true;
		specimenType.disabled = true;
		specimenArray.disabled = false;
		var len = specimenClass.length;
		for (var i = 0; i < len; i++) 
		{
			specimenClass.options[i].selected = false;
		}
	}
		
}

function onEditChange()
{

	var ele0 = document.getElementById("customListBox_1_0");
	var ele1 = document.getElementById("customListBox_1_1");
	var ele2 = document.getElementById("customListBox_1_2");
	var operation = "<%=operation%>";
	if(operation == "edit" && document.forms[0].checkedButton[1].checked==true)
	{
		ele0.remove(0);
		ele1.remove(0);
		ele2.remove(0);
	}
}
function vieMapTabSelected(){
 window.parent.tabSelected("viewmapTab");
 var activityStatus= window.parent.getActivityStatus();
 var action= "OpenStorageContainer.do?<%=Constants.SYSTEM_IDENTIFIER%>=${requestScope.storageContainerIdentifier}&<%=Constants.PAGE_OF%>=viewMapTab&<%=Status.ACTIVITY_STATUS.toString()%>="+activityStatus+""; 
	document.forms[0].action=action;
	document.forms[0].submit();
}
function refresh_tree()
{	
	window.parent.frames['SCTreeView'].location="<%=Constants.STORAGE_CONTAINER_TREE_ACTION%>?<%=Constants.PAGE_OF%>=<%=pageOf%>&<%=Constants.RELOAD%>=true&<%=Constants.TREE_NODE_ID%>=${requestScope.storageContainerIdentifier}";
}

function parentContainerTypeChanged(element)
{	
	var selectedParentSite=document.getElementById("parentContainerSite");
	var selectedParentAuto=document.getElementById("parentContainerAuto");
	var selectedParentManual=document.getElementById("parentContainerManual");
	
	if(element.value == "Site")
    {
		selectedParentSite.style.display="block"; 	
		selectedParentAuto.style.display="none";
		selectedParentManual.style.display="none";
	//	document.getElementById("siteId").value=-1;
		document.forms[0].siteId.value=-1;
	
	}
	else if(element.value == "Auto")
	{
		selectedParentSite.style.display="none"; 	
		selectedParentAuto.style.display="block";
		selectedParentManual.style.display="none";
	/*	document.forms[0].customListBox_1_0.value=-1;
		document.forms[0].customListBox_1_1.value = -1;
		document.forms[0].customListBox_1_2.value = -1;		
		*/
		onParentlocationChange(document.forms[0].customListBox_1_0);
	}
	else if(element.value == "Manual")
	{
		selectedParentSite.style.display="none"; 	
		selectedParentAuto.style.display="none";
		selectedParentManual.style.display="block";
		document.forms[0].selectedContainerName.value = "";
		document.forms[0].pos1.value = "";
		document.forms[0].pos2.value = "";
		
	}
	clearSelBoxList(document.getElementById('collectionIds'));
	
}

// Patch ID: Bug#3090_11
window.parent.selectTab('${requestScope.operation}');

function setParentContainerType()
{
	var selectedParentSite=document.getElementById("parentContainerSite");
	var selectedParentAuto=document.getElementById("parentContainerAuto");
	var selectedParentManual=document.getElementById("parentContainerManual");
	
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
function addNewTypeAction(action)
{
	var action = action;
	document.forms[0].target="_top";
	document.forms[0].action = action;
	document.forms[0].submit();
}
	</script>
</head>



<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<body >
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<input type="hidden" name="eventOnSelectedContainerNameObj" id="eventOnSelectedContainerNameObj" value="onchange"/>

<html:form action="<%=formName%>" method="post">	
<table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!-- NEW STORAGE CONTAINER REGISTRATION BEGINS-->
		<tr>
           <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
                    <td  valign="bottom" ><img src="images/uIEnhancementImages/sc_info.gif" alt="Container Info" width="111" height="20" /></td>
					 <td width="90%" valign="bottom" class="cp_tabbg">&nbsp;</td>
					</logic:notEqual>
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					 <td valign="bottom" ><img src="images/uIEnhancementImages/sc_edit.gif" alt="Container Info" width="158" height="20"  /><a href="#"></a></td>
					<td  valign="bottom"><a href="#"><img src="images/uIEnhancementImages/cp_containerMap1.gif" alt="View Map" width="111" height="20" border="0" onclick="vieMapTabSelected()"/></a></td>
                    <td width="80%" valign="bottom" class="cp_tabbg">&nbsp;</td>
					</logic:equal>
                   </tr>
              </table>
			 </td>
          </tr>
		<tr>
		<td class="cp_tabtable">
			<table width="100%" border="0" cellpadding="3" cellspacing="0">					
						<html:hidden property="operation" value="<%=operation%>" />
						<%--  <html:hidden property="containerNumber" value="<%=containerNumber%>" /> --%>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>	
						<input type="hidden" name="radioValue">
						<html:hidden property="containerId" styleId="containerId"/>
						<html:hidden property="forwardTo" />
						<html:hidden property="nextForwardTo" />
				
						<html:hidden property="id" />
						<html:hidden property="typeName"/>
						<html:hidden property="siteName"/>
					
						<html:hidden property="positionInParentContainer" />
						<html:hidden property="siteForParentContainer"/>

					
			
					<html:hidden property="onSubmit" />
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<html:hidden property="isFull" />
						</logic:equal>					
					
					
				<tr>
				   <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
						<tr>
						 <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                        <td width="20%" align="left" class="black_ar"><bean:message key="storageContainer.type" /></td>
						 <td width="23%" align="left"><label><html:select property="typeId" styleClass="formFieldSizedNew" styleId="typeId" size="1" onchange="onTypeChange(this)" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">	<%-- html:options name="storageTypeIdList" labelName="storageTypeList" /--%><html:options collection="<%=Constants.STORAGETYPELIST%>" labelProperty="name" property="value"/></html:select></label></td>
						 <td colspan="4" align="left">
							<%if(operation.equals(Constants.ADD))
							{
							%>
								<html:link href="#" styleId="newStorageType" styleClass="view"   onclick="addNewTypeAction('StorageContainerAddNew.do?addNewForwardTo=storageType&forwardTo=storageContainer&addNewFor=storageType')">
									<bean:message key="buttons.addNew" />
								</html:link>
							<% } %>										
										
						</td>
						
					</tr>
					<%
							Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
							
							session.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,dataMap);							

							String[] initValues = new String[3];
							List initValuesList = (List)request.getAttribute("initValues");
							if(initValuesList != null)
							{
								initValues = (String[])initValuesList.get(0);
							}

							// labelNames = {"Name","Pos1","Pos2"};
							String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;
							String[] attrNames = { "parentContainerId", "positionDimensionOne", "positionDimensionTwo"};
							String[] tdStyleClassArray = { "formFieldSizedNew", "black_ar", "black_ar"}; 
							
							//String[] initValues = new String[3];
							//initValues[0] = Integer.toString((int)form.getParentContainerId());
							//initValues[0] = form.getPositionInParentContainer();
							//initValues[1] = Integer.toString(form.getPositionDimensionOne());
							//initValues[2] = Integer.toString(form.getPositionDimensionTwo());
							
							String rowNumber = "1";
							String styClass = "black_ar";
							String tdStyleClass = "black_ar";
							boolean disabled = true;
							String onChange = "";
							onChange = "onCustomListBoxChange(this),onParentlocationChange(this)";
							//String onChange = "onCustomListBoxChange(this);onParentContainerChange()";
							boolean buttonDisabled = true;
							//String buttonOnClicked  = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";							
							
							/**
							* Smita_kadam
							* Reviewer: Sachin
							* Bug ID: 4596
							* Patch ID: 4596_1
							* Description: '&amp;' sequence in URL string is replaced with '&' to be supported by Mozilla
							**/
							String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&selectedContainerName=selectedContainerName&pos1=pos1&pos2=pos2&containerId=containerId&storageContainer=true&storageType=";							
							
							String noOfEmptyCombos = "3";
							
						/*	 int radioSelected = form.getStContSelection();
								boolean dropDownDisable = false;
								boolean textBoxDisable = false;
								
								if(radioSelected == 1)
								{
									dropDownDisable = true;
									textBoxDisable = true;
								}
								else if(radioSelected == 2)
								{									
									textBoxDisable = true;
								}
								else if(radioSelected == 3)
								{
									dropDownDisable = true;									
								}
								*/
							
							//String buttonId = "Map_1";			
							
						%>
					
						<%=ScriptGenerator.getJSForOutermostDataTable()%>
						<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
					
					<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
					<tr>
                      <td class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                      <td align="left" class="black_ar">Parent Location Details</td>
					  <td><html:select  styleClass="formFieldSizedNew" property="parentContainerSelected" size="1"	onchange= "parentContainerTypeChanged(this)"><html:options collection="parentContainerTypeList"labelProperty="name" property="value"  /></html:select></td>
					  <td colspan="4" align="left" class="black_ar">
					  
					  <div id="parentContainerSite"  style="display:block">
					    <table width="100%" border="0" cellpadding="0" cellspacing="0" >
						<tr><td>
						  <label>
							<html:select property="siteId" styleClass="formFieldSizedNew" styleId="siteId" size="1" onchange="onParentlocationChange(this)"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
							</html:select>
							</label>						   	
							&nbsp;
							<html:link href="#" styleId="newSite"  styleClass="view" onclick="addNewAction('StorageContainerAddNew.do?addNewForwardTo=site&forwardTo=storageContainer&addNewFor=site')">
								<bean:message key="buttons.addNew" />
							</html:link>	
					    </td></tr></table>
						 </div>
					 
					
					<div id="parentContainerAuto" style="display:none">
					  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
					  <tr>
						
							<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClassArray="<%=tdStyleClassArray%>"
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>"
											formLabelStyle="nComboGroup"
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
											
                       
					     </tr>  
                         </table>
						 </div>
						 
						<div id="parentContainerManual" style="display:none">
    					  <table width="59%" border="0" cellpadding="0" cellspacing="0" class="groupElements">
						<tr>
							 <td width="24%"><html:text styleClass="grey_ar"   size="30" styleId="selectedContainerName" onmouseover="showTip(this.id)" onchange="onParentlocationChange(this)" property="selectedContainerName"/></td>
							 <td width="13%"><html:text styleClass="black_ar_s"  size="5" styleId="pos1" property="pos1"/> </td>	
							 <td width="13%"><html:text styleClass="black_ar_s" size="5" styleId="pos2" property="pos2"/></td>	
							 <td width="14%"><html:button styleClass="black_ar"  property="containerMap" styleId="containerMap" onclick="mapButtonClicked()"><bean:message key="buttons.map"/></html:button> </td>
							 
						</tr>
                         </table>
						 </div>
						
						 </td>
						
					</tr>
					<script>
						setParentContainerType();
						// Patch ID: Bug#3090_11
						function mapButtonClicked()
						 {
						    var platform = navigator.platform.toLowerCase();
							if (platform.indexOf("mac") != -1)
							 {
							   	StorageMapWindow('<%=frameUrl%>','name',screen.width,screen.height,'no');
							 }
							else
							 {
							   	StorageMapWindow('<%=frameUrl%>','name','800','600','no');
							  }
						}
					</script>
					<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
							<bean:message key="container.maxView"/>
						</td>
					</tr>	
					</logic:equal>
					<% if(!Variables.isStorageContainerLabelGeneratorAvl || operation.equals(Constants.EDIT))
					{
					%>
					<tr>
						<td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></td>
                        <td align="left" class="black_ar"><bean:message key="storageContainer.containerName" /></td>
						<td align="left"><html:text styleClass="black_ar" maxlength="255"  size="30" styleId="containerName" property="containerName"/></td>
					
					<%
					}
					%>
					<% if(!Variables.isStorageContainerBarcodeGeneratorAvl || operation.equals(Constants.EDIT))
					{%>		
						<td width="10%" align="left" class="black_ar">&nbsp;</td>
                        <td  align="left" class="black_ar"><bean:message key="storageContainer.barcode" /></td>
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
							<td width="18%" align="left" class="black_ar_t">
							<logic:equal name="storageContainerForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
							<%
							if(form.getBarcode()!=null)
							{%>
								<label for="barcode">
									<%=form.getBarcode()%>
								</label>
							<%}
							else
							{%>
								<label for="barcode">
								</label>
							<%}%>
							<html:hidden property="barcode"/>
							</logic:equal>
							<logic:notEqual name="storageContainerForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
							<html:text styleClass="formFieldSized10" maxlength="255"  size="30" styleId="barcode" property="barcode"/>
							</logic:notEqual>
							</td>
							<td width="10%" align="left" class="black_ar">&nbsp;</td>
						</tr>
						</logic:equal>
						<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
							<td width="30%" align="left"><html:text styleClass="formFieldSized10" maxlength="255"  size="30" styleId="barcode" property="barcode"/></td>
						</logic:notEqual>
					</tr>
					<%
					}
					%>
					<tr>
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						<td  align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
						<td  align="left" valign="top" class="black_ar"><label for="noOfContainers"><bean:message key="storageContainer.noOfContainers" /></label></td>
						<td  class="grey_ar"><html:text styleClass="black_ar" style="text-align:right" maxlength="10" size="15" styleId="noOfContainers" property="noOfContainers" readonly="<%=readOnlyValue%>" /></td>
					</logic:notEqual>
						
						<td align="center" class="black_ar"><span class="blue_ar_b">&nbsp;</span></td>
						<td align="left" class="black_ar"><label for="defaultTemperature"><bean:message key="storageContainer.temperature" /></label></td>
						<td align="left" nowrap><span class="grey_ar"><html:text styleClass="black_ar" style="text-align:right" maxlength="10" size="15" styleId="defaultTemperature" property="defaultTemperature"/></span><span class="black_ar">&nbsp;<sup>0</sup>C</span></td>
						<td width="10%" align="left" class="black_ar">&nbsp;</td>
					</tr>
					
<%-- MD : Code for isContainerfull
     Bug id 1007
 --%>						
 					
					
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
                         <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></span></td>
						 <td align="left" class="black_ar"><bean:message key="site.activityStatus" /></td>
						 <td align="left" nowrap>
							<html:select property="activityStatus" styleClass="formFieldSizedNew" styleId="activityStatus" size="1" onchange="<%=strCheckStatusForCont%>"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
							</html:select>
						</td>
						<td align="left" class="black_ar">&nbsp;</td>                      

<!-- Mandar : 434 : for tooltip -->

						<td width="1%"align="left" class="black_ar" colspan="2"><span class="blue_ar_b"></span><html:checkbox property="isFull" value="true"/><bean:message key="storageContainer.isContainerFull" />?</td>
						  <td align="center" class="black_ar">&nbsp;</td>
						</td>
					</tr>
					</logic:equal>
					</table>
                      </td>
                    </tr>
					<tr onclick="showHide('add_id')">
					    <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="storageContainer.restrictions" /></span></td>
						<td align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_add_id"><img src="images/uIEnhancementImages/up_arrow.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/></a></td>
					</tr>
					<tr>
					   <td colspan="5" class="showhide"><div id="add_id" style="display:block" >
                           <table width="100%" border="0" cellpadding="3" cellspacing="0">
                            <tr>
                                <td width="1%" align="left" valign="top" class="black_ar">&nbsp;</td>
								<td width="20%" align="left" valign="top" class="black_ar_t"><bean:message key="storageContainer.collectionProtocolTitle" /></td>
								<td width="79%" colspan="4" align="left" valign="top"><html:select property="collectionIds" styleClass="formFieldSizedSC" styleId="collectionIds" size="4"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" multiple="true" ><html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/></html:select></td>
							</tr>
							<tr>
                                <td align="left" valign="top" class="black_ar">&nbsp;</td>
                                <td align="left" valign="top" class="black_ar_t"><bean:message key="storageContainer.holds" /></td>
                                <td colspan="4" align="left" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
							 <tr>
                                <td width="25%" align="left" class="tabletd1"><bean:message key="storageContainer.containerType"/></td>
								<td width="25%" align="left" class="tabletd1"><label><html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClickOfSpecimen('Specimen')"/> <bean:message key="storageContainer.specimenClass"/> </label></td>
								<td width="25%" align="left" class="tabletd1"><label><bean:message key="storageContainer.specimenType"/> </label></td>
							    <td width="25%" align="left" class="tabletd1"><label><html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClickOfSpecimen('SpecimenArray')"/> <bean:message key="storageContainer.specimenArrayType"/> </label></td>
							</tr>

							<tr>
                                <td width="26%" align="left" class="tabletd1"><html:select property="holdsStorageTypeIds" styleClass="formFieldSizedSC" styleId="holdsStorageTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection="<%=Constants.HOLDS_LIST1%>" labelProperty="name" property="value"/></html:select></td>	
						<td width="26%" align="left" class="tabletd1">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenClassTypeIds"
							size="4" multiple="true" onchange="selectType(this)" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray"><html:select property="holdsSpecimenClassTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true"><html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>
						</td>
						<td width="26%" align="left" class="tabletd1">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenTypes" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection="<%=Constants.HOLDS_LIST4%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray"><html:select property="holdsSpecimenClassTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true"><html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>						</td>
						<td width="26%" align="left" class="tabletd1">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSizedSC" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">	<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSizedSC"  styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true"><html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/></html:select>
							</logic:equal>
							
						</td>
						</tr>
						 
						</table>				
						</td>
					</tr>
					</table></td>
                    </tr>
					 <tr onclick="javascript:showHide('cap_id')">
                        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="storageContainer.capacity" /></span></td>
						<td align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_cap_id"><img src="images/uIEnhancementImages/up_arrow.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/></a></td>
					</tr>
					  <td colspan="2" class="showhide1"><div id="cap_id" style="display:block" >
                        <table width="100%" border="0" cellpadding="3" cellspacing="0">
					 <tr>
                         <td width="1%" align="center" valign="top" class="black_ar_t"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></td>
						 <td width="20%" align="left" valign="top" class="black_ar_t"><label for="twoDimensionCapacity" onmouseover="Tip(' <%=label1%>')">
						  <%
							String displayLabel1 = label1;
							int label1Lenght=label1.length();
							if(label1Lenght >= 20)
							  displayLabel1 = label1.substring(0,20)+"...";
							else
							  displayLabel1 =label1.substring(0,label1Lenght);
						%>
						 <%=displayLabel1%>
						 
						 </label></td>
						 <td width="25%" colspan="4" align="left" valign="top"><html:text styleClass="black_ar" maxlength="10"  style="text-align:right" size="15" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/></td>
						 <td width="1%" align="center" valign="top" class="black_ar_t">&nbsp;</td>
						 <td width="15%" align="left" valign="top" class="black_ar_t"><label for="twoDimensionLabel" onmouseover="Tip(' <%=label2%>')"> 
							<%
								if(label2 != null || !(label2.equals("")))
								{
								  String displayLabel2 = label2;
								  label1Lenght=label2.length();
							      if(label1Lenght >= 20)
							        displayLabel2 = label2.substring(0,20)+"...";
							      else
							        displayLabel2 =label2.substring(0,label1Lenght);
							%>
							<%=displayLabel2%>
							<%
								}
							%>
							</label>
						</td>
						<td width="38%" align="left" valign="top">
							<html:text styleClass="black_ar" maxlength="10"  style="text-align:right" size="15" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
						</td>
					</tr>
					 </table></div></td>

                      <tr>
						<td class="dividerline" colspan="5"><span class="black_ar"></td>
					</tr>

                    <tr>
                     <td> 
					  <table>
					    <tr>
						  <td colspan="1" width="20%" nowrap>
							<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
								<span class="black_ar">
									<bean:message key="print.checkboxLabel"/>
								</span>
							</html:checkbox>
						 </td>						
	<!--  Added for displaying  printer type and location -->
						 <td>
					   	    <%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 			 </td>
						</tr>
                       </table>
                      </td> 
					 </tr>	

					<tr>                        
					<!-- delete button added for disabling the objects :Nitesh 
						<td colspan="3" class="buttonbg"></td> -->
						<%
							String deleteAction="deleteStorageContainer('" + formName +"','" + Constants.CONTAINER_DELETE_MAPPING + "')";
						%>
								
						
						<td colspan="5" class="buttonbg">
								<%
						   			String action = "validate('" + formName +"?pageOf=pageOfStorageContainer"+"',document.forms[0].activityStatus)";
						   		%>
						   			<html:button styleClass="blue_ar_b" property="submitPage" onclick="<%=action%>">
						   				<bean:message key="buttons.submit"/>
						   			</html:button>&nbsp;<logic:equal name="<%=Constants.OPERATION%>" value="edit">|&nbsp;<html:button styleClass="blue_ar_c" property="deletePage"
									onclick="<%=deleteAction%>">
										<bean:message key="buttons.delete"/>&nbsp;
									</html:button>
					          		</logic:equal>
                           </td>
                                   					     
				   		</tr>
				</table>
				
			<!-- /td-->
		</tr>

		<!-- NEW STORAGE CONTAINER REGISTRATION ends-->
	</table>
	</td>
    </tr>
	</table>
</html:form>
<script language="JavaScript" type="text/javascript">
showPriterTypeLocation();
</script>
</body>