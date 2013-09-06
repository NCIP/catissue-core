<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<script language="JavaScript" type="text/javascript" src="de/jss/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/combobox.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/combos.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ajax.js"></script>
<script src="jss/configureCPDashboard.js"></script>

<script src="jss/javaScript.js" type="text/javascript"></script>
<script>
	var selectedNodeId, key, parentId,cpNodeId;
	var firstTimeLoad = true;
	//This function calls whenever we select a node from CP tree view
	function setKeys(nodeId, nodeKey,nodesParentId )
	{
	  selectedNodeId = nodeId;
	  key = nodeKey;
	  parentId = nodesParentId;
	 // alert(selectedNodeId +"; " + key +"; " +parentId);
	}
	function selectAllClinicalDiagnosis()
	{
	 	var clinicalDiag = window.frames['SpecimenRequirementView'].document.getElementById('protocolCoordinatorIds');
		if (clinicalDiag != null)
		{
			for (i = clinicalDiag.options.length-1; i >= 0; i--)
			{
				clinicalDiag.options[i].selected=true;
			}
		}
	 }

	 function selectAllCoordinators()
	 {
		 var cpCoordiantors = window.frames['SpecimenRequirementView'].document.getElementById('coordinatorIds');
			if (cpCoordiantors != null)
			{
				for (i = cpCoordiantors.options.length-1; i >= 0; i--)
				{
					cpCoordiantors.options[i].selected=true;
				}
			}
	}

	function openEventPage()
	{
		window.frames['CPTreeView'].saveTreeState();
		var action = "";
		selectAllClinicalDiagnosis();
		selectAllCoordinators();
		if(window.frames['SpecimenRequirementView'].document.forms['CollectionProtocolForm'] != null)
		{
			window.frames['SpecimenRequirementView'].setCSLevelFormData();//save the data in dashboard items grid
		}
		var formId=window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');
		if(formId!=null)
		{
			action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=add";
			if(firstTimeLoad==true && '${requestScope.operation}'=='add'){
			   action = action + "&addCpNode=true";
			   firstTimeLoad = false;
		    } 
		}
		else
		{
			formId=window.frames['SpecimenRequirementView'].document.getElementById('protocolEventDetailsForm');
			action = "SaveProtocolEvents.do?pageOf=newEvent&operation=add";
			if(formId==null)
			{
				formId=window.frames['SpecimenRequirementView'].document.getElementById('createSpecimenTemplateForm');
				action = "CreateSpecimenTemplate.do?pageOf=newEvent&operation=add";
			}
		}
	    formId.action=action;

	    formId.submit();
	}

	function saveCP()
	{	
	
		window.frames['CPTreeView'].saveTreeState();
		
		//var cpDetailsForm = window.frames['SpecimenRequirementView'].document.forms['CollectionProtocolForm'];
		var cpDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');
		var eventDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('protocolEventDetailsForm');
		var specimenDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('createSpecimenTemplateForm');
		var formObject , action, operation;
		
		if( cpDetailsForm != null)
		{
			window.frames['SpecimenRequirementView'].setCSLevelFormData();//save the data in dashboard items grid
		}
		selectAllClinicalDiagnosis();
		selectAllCoordinators();
		var isSaveCollectionProtocol = false;
		formObject = cpDetailsForm;
		
		if(formObject!=null )
		{
			operation = formObject.elements['operation'];
			action="SaveCollectionProtocol.do?Event_Id=dummyId&pageOf=submitSpecimen&operation=${requestScope.operation}&refreshWholePage=true&cpNodeId="+window.parent.cpNodeId;
			//formObject.target = '_top';
			isSaveCollectionProtocol = true;
		}
		else
		{
			formObject=eventDetailsForm;
			if(formObject==null)
			{
				formObject=specimenDetailsForm;
			}
			action="SubmitCollectionProtocol.do?operation=${requestScope.operation}";
			//formObject.target = '_top';
		}
		
		//alert(formObject.target + " " + action);
      	formObject.action=action;
        formObject.submit();
	}

    function submitCP()
	{


       var actvity = window.frames['SpecimenRequirementView'].document.getElementById('activityStatus');
	   if((actvity!=null) && (actvity.value != undefined) && (actvity.value == "Disabled")){
         var go = confirmDialogForDisable();
	     if (go==true){
	 	   saveCP();
	     }
	   }
       else{
	    saveCP();
   }
  }

  function exportCP()
  {
    var cpDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');
	var action="ExportCollectionProtocol.do?title="+cpDetailsForm.title.value+"&shortTitle="+cpDetailsForm.shortTitle.value;	
	mywindow = window.open(action, "Download", "width=10,height=10");
	mywindow.moveTo(0,0);
  }

  function stopSync()
  {
	  request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,onResponseSet,true);	
		request.onreadystatechange = handlerFunction;	
var cpDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');		
		actionURL = "cpTitle="+ cpDetailsForm.title.value;
		var url = "CatissueCommonAjaxAction.do?type=stopSyncCP";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);
  }
  
  function onResponseSet(response)
  {
	  
  }
  function syncCP()
  {
		var go = confirm("This might take sometime to complete. During this time, you cannot edit the protocol or perform data entry. \n Do you still want to continue?");
		if (go==true)
		{
		  request = newXMLHTTPReq();			
		  var actionURL;
		  var handlerFunction = getReadyStateHandler(request,onResponseSetRequester,true);	
		  request.onreadystatechange = handlerFunction;	
		  var cpDetailsForm = window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');		
		  actionURL = "cpTitle="+ cpDetailsForm.title.value;
		  var url = "CatissueCommonAjaxAction.do?type=startSyncCP";
		  request.open("POST",url,true);	
		  request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		  request.send(actionURL);
		}
   	}

	function onResponseSetRequester(response) 
	{
		document.getElementById('syncMsg').style.display="";
		document.getElementById('eventButton').style.visibility="hidden";
		document.getElementById('saveCPButton').style.visibility="hidden";
		document.getElementById('exCPButton').style.visibility="";
		document.getElementById('syncCPButton').style.visibility="hidden";
		document.getElementById('stopCPButton').style.visibility="visible";
	}

  function resizeIframe()
  {
	
	var totalHeight=window.top.document.body.offsetHeight;
	document.getElementById('SpecimenRequirementView').style.height=totalHeight*66/100;
	document.getElementById('CPTreeView').style.height=totalHeight*66/100;
  }

</script>
<body onload='resizeIframe()'>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
 <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="app.collectionProtocol"/></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Collection Protocol" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
        <logic:equal parameter="operation"	value='add'>
				      <td width="5%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                    <td width="5%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
					</logic:equal>
					<logic:equal parameter="operation"	value='edit'>
					<td width="5%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/OpenCollectionProtocol.do?pageOf=pageOfmainCP&operation=add"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
                    <td width="5%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
					</logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
</td>
</tr>
<tr>
	<td>

      <table width="100%" border="0" cellpadding="1" cellspacing="1" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class="bottomtd">
				<!--<div id="labelFormatErrDiv" style="display:none">

					<span class="messagetexterror">
				Label Format is mandatory for custom label generation
					</span>

				</div>-->
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		  </td>
        </tr>

       		<tr>
				<td width="21%" valign="top" style="border-left:1px solid #61a1e3; border-right:1px solid #61a1e3;border-bottom:1px solid #61a1e3;border-top:1px solid #61a1e3;">
					<iframe id="CPTreeView" src="ShowCollectionProtocol.do?operation=${requestScope.operation}&isErrorPage=${requestScope.isErrorPage}"  frameborder="0" width="100%" name="CPTreeView" >
							<bean:message key="errors.browser.not.supports.iframe"/>
					</iframe>
				</td>
							 <td width="80%" valign="top" >
							 <logic:equal name="operation" value="add">
								<iframe name="SpecimenRequirementView"	id="SpecimenRequirementView" src="CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol&isErrorPage=${requestScope.isErrorPage}" marginwidth="0" scrolling="auto" frameborder="0" width="100%" >
									<bean:message key="errors.browser.not.supports.iframe"/>
								</iframe>
							</logic:equal>
							 <logic:equal name="operation" value="edit">
								<iframe name="SpecimenRequirementView"	id="SpecimenRequirementView" src="CollectionProtocol.do?operation=edit&pageOf=pageOfCollectionProtocol&invokeFunction=cp" scrolling="auto" marginwidth="0" frameborder="0" width="100%">
									<bean:message key="errors.browser.not.supports.iframe"/>
								</iframe>
							 </logic:equal>
							 </td>
						</tr>


 <tr>
		 <td colspan="2" class="buttonbg">

<logic:notEqual name="isSyncOn" value="true">
						<html:button styleClass="blue_ar_b" styleId="eventButton" property="forwardPage" onclick="openEventPage()" >
							Add Events >>
						</html:button>

					 <html:button styleClass="blue_ar_b" styleId="saveCPButton" property="forwardPage" value="Save" onclick="submitCP()">
					</html:button>
					<logic:equal parameter="operation"	value='edit'>
					<html:button styleClass="blue_ar_b" styleId="exCPButton" property="forwardPage" value="Export" onclick="exportCP()">
					</html:button>	
					</logic:equal>					
	</logic:notEqual>
	
		<logic:equal name="isSyncOn" value="true">
					<logic:equal parameter="operation"	value='edit'>
					<html:button styleClass="blue_ar_b" property="forwardPage" value="Export" onclick="exportCP()" disabled="true">
					</html:button>	
					</logic:equal>
		</logic:equal>
		
				   </td>
				</tr>
				</table>
	</td>
 </tr>
</table>
</body>