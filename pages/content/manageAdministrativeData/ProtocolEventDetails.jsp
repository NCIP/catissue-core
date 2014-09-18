<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript" src="jss/combos.js"></script>
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.tree.QueryTreeNodeData"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ page import="java.util.*"%>

<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"	href="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxToolbar/codebase/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script>


<%
	Object obj = request.getAttribute("protocolEventDetailsForm");
	String operation = "add";
	ProtocolEventDetailsForm form =null;
	if(obj != null && obj instanceof ProtocolEventDetailsForm)
	{
		form = (ProtocolEventDetailsForm)obj;
	}
	String operationType=null;
	boolean disabled = false;
	operationType = (String)request.getAttribute("opr");
	if(operationType!=null && operationType.equals("update"))
	{
		disabled = true;
	}
%>
<head>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script src="jss/caTissueSuite.js" language="JavaScript" type="text/javascript"></script>
<script language="JavaScript">
var siteDropDownInfo, dsGrid, clinicalStatusDropDownInfo, csGrid;
var dsGridVisible = false, csGridVisible = false;

function specimenRequirements()
{
	window.parent.frames['CPTreeView'].saveTreeState();
	var action ="SaveProtocolEvents.do?pageOf=specimenRequirement&operation=add&parentNodeId="+window.parent.cpNodeId;
	document.forms[0].action = action;
	document.forms[0].submit();
}

function createDuplicateEvent()
{
	window.parent.frames['CPTreeView'].saveTreeState();
	var action ="SaveProtocolEvents.do?pageOf=defineEvents&operation=add&"+"<%=Constants.CREATE_DUPLICATE_EVENT%>"+"=true&parentNodeId="+window.parent.cpNodeId;
	document.forms[0].action = action;
	document.forms[0].submit();
}

function deleteEvent()
{
	window.parent.frames['CPTreeView'].saveTreeState();
	var answer = confirm ("Are you sure want to delete event?")
	if(answer)
	{
		//document.forms[0].target = '_top';
		var action ="DeleteNodeFromCP.do?pageOf=cpEvent&operation=edit&nodeId="+window.parent.selectedNodeId+"&key="+window.parent.key+"&parentNodeId="+window.parent.parentId+"&nodeDeleted=true";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
}
function submitAllEvents()
{
	window.parent.frames['CPTreeView'].saveTreeState();
	var action = "SaveProtocolEvents.do?pageOf=defineEvents&operation=add&parentNodeId="+window.parent.cpNodeId+"&nodeId="+window.parent.selectedNodeId;
	document.forms[0].action = action;
	document.forms[0].submit();
}


function siteOnRowSelect(id,ind)
{	
	rowSelectEvent(id,ind,siteDropDownInfo,dsGrid);
	dsGridVisible = false;
}

function clinicalStatusOnRowSelect(id,ind)
{	
	rowSelectEvent(id,ind,clinicalStatusDropDownInfo,csGrid);
	dsGridVisible = false;
}


//This will select option on the basis of left , right, up 0r down key press
function keyNavigationCall(event, gridDropDownInfo,gridObj)
{
	keyNavigation(event,gridDropDownInfo,gridObj,gridDropDownInfo['visibilityStatusVariable']);
}


function setFocus(e, dropDownId)
{
		document.getElementById(dropDownId).focus();
		noEventPropogation(e);
}

//On grid load
function onSiteListReady()
	{
		var defaultSiteId = '${protocolEventDetailsForm.defaultSiteId}';
		if(defaultSiteId != "" && defaultSiteId != 0 && defaultSiteId != null)
			siteOnRowSelect(defaultSiteId,0);
	}
	
function onClinicalStatusListReady()
	{
		var clinicalStatus = '${protocolEventDetailsForm.clinicalStatus}';
		if(clinicalStatus != "" && clinicalStatus != null)
		{
			clinicalStatusOnRowSelect(clinicalStatus,0);
		}	
	}
	
function doOnLoad()
{
	if("<%=request.getParameter("addCpNode")%>" == 'true' )
	{
	  window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
	}
	
	if('${protocolEventDetailsForm.studyCalendarEventPoint}' == 0.0 && '${requestScope.isPersistent}' != 'true')
	{
		document.getElementById("studyCalendarEventPoint").value = 0;
	}
	
       siteDropDownInfo = {propertyId:'defaultSiteId',gridObj:"defaultSiteGrid", gridDiv:"defaultSite", dropDownId:"defaultSiteDropDown", pagingArea:"dsPagingArea", infoArea:"dsInfoArea", onOptionSelect:siteOnRowSelect, actionToDo:"CatissueCommonAjaxAction.do?type=getAllSiteList", callBackAction:onSiteListReady,visibilityStatusVariable:dsGridVisible};
	dsGrid = initDropDownGrid(siteDropDownInfo,true); //initialize DropDown control for Site List
        clinicalStatusDropDownInfo = {propertyId:'clinicalStatus',gridObj:"clinicalStatusGrid", gridDiv:"clinicalStatusDiv", dropDownId:"clinicalStatusDropDown", pagingArea:"csPagingArea", infoArea:"csInfoArea", onOptionSelect:clinicalStatusOnRowSelect, actionToDo:"CatissueCommonAjaxAction.do?type=getClinicalStatusList", callBackAction:onClinicalStatusListReady,visibilityStatusVariable:csGridVisible};
	csGrid = initDropDownGrid(clinicalStatusDropDownInfo,true); //initialize DropDown control for Clinical Status List
	//If user creating Duplicate event
	if('${requestScope.setFocus}'=="true"){
			document.getElementById("collectionPointLabel").focus();}
	
	if('${requestScope.deleteNode}' != "")
	{
		window.parent.frames['CPTreeView'].deleteCPTreeNode('${requestScope.deleteNode}',false)
		window.parent.frames['CPTreeView'].setGlobalNodeKeys("<%=request.getSession().getAttribute("nodeId")%>",true);
	}
	
	<%
		Vector treeData = (Vector)request.getAttribute("nodeAdded");
		if(treeData != null && treeData.size() != 0)
		{
			Iterator itr  = treeData.iterator();
			while(itr.hasNext())
			{
				QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
				String objectType = data.getObjectType();
				String nodeId = data.getObjectName()+ "_" + data.getIdentifier().toString();
				String img = null;
				String parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();
				if(objectType.startsWith("N")) 	{
					img = "Participant.GIF";
				} else if(objectType.startsWith("A"))
				{
					img = "aliquot_specimen.gif";
				} else if(objectType.startsWith("D"))
				{
					img = "derived_specimen.gif";
				} else	{
					img = "cp_event.gif";
				}
			%>
					window.parent.frames['CPTreeView'].addCPTreeNode("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>","<%=img%>");
			<%
			}
		}
		%>
		
	var selectedNodeId = "<%=(String)request.getSession().getAttribute("nodeId")%>";
	window.parent.frames['CPTreeView'].setGlobalNodeKeys(selectedNodeId,true);	
}
</script>
</head>

<body onload="doOnLoad()">
<html:form action="SaveProtocolEvents.do?pageOf=defineEvents&operation=add" styleId="protocolEventDetailsForm">
	<logic:equal name="isParticipantReg" value="true">
		<%@ include file="/pages/content/manageAdministrativeData/PersistentProtocolEvent.jsp" %>
	</logic:equal>
	<logic:notEqual name="isParticipantReg" value="true">
		<%@ include file="/pages/content/manageAdministrativeData/NonPersistentProtocolEvent.jsp" %>
	</logic:notEqual>
</html:form>

<script>
window.top.document.getElementById("errorRow").innerHTML = "";
</script>
<link rel="STYLESHEET" type="text/css" href="css/dhtmldropdown.css">
<style>
div.gridbox table.obj tr.rowselected td.cellselected, div.gridbox table.obj td.cellselected {
    background-color: rgb(216, 216, 216);
    color: black;
    border-color: skyblue;
}

div.gridbox table.obj td {
    border: 0px solid rgb(97, 161, 227);
    padding: 2px;
    font-size: 12px;
    font-family: verdana;
    color: black;
}
</style>
</body>