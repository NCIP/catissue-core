<%@page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.struts.action.ActionMessages"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page
	import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<head>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.css">
<link rel="stylesheet" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"
	href="dhtmlxSuite_v35/dhtmlxToolbar/codebase/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript"
	src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/ext/dhtmlxtree_li.js"></script>
<script language="JavaScript" type="text/javascript"
	src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script type="text/javascript"
	src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"
	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript"
	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"
	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"
	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"
	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"
	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"
	href="css/clinicalstudyext-all.css" />
<%
	String selectText = "--Select--";
	//CollectionProtocolForm newformName = (CollectionProtocolForm)request.getAttribute("formName");
	//System.out.println("formName &&****#$#$#$#$$##$#$ : "+ newformName.isGenerateLabel());
%>
<script>
// Defining Coordinator Combobox done :)
//declaring DHTMLX Drop Down controls required variables
var investigatorDropDownInfo, piGrid, parentProtocolDropDownInfo, ppGrid;
var piGridVisible = false, ppGridVisible = false;
var tabbar;
if('${requestScope.tabSel}'=="consentTab"){
window.onload=consentPage;
tabbar.setTabActive("consentTab");
}
//will be called whenever a participant is selected from the participant grid/dropdown
function investigatorOnRowSelect(id,ind)
{
rowSelectEvent(id,ind,investigatorDropDownInfo,piGrid);
}
function parentProtocolOnRowSelect(id,ind)
{
rowSelectEvent(id,ind,parentProtocolDropDownInfo,ppGrid);
}
//This will select option on the basis of left , right, up 0r down key press
function keyNavigationCall(event, gridDropDownInfo, gridObj)
{
keyNavigation(event,gridDropDownInfo,gridObj,gridDropDownInfo['visibilityStatusVariable']);
}
//This called on every key up event on DHTMLX drop down control
//function autoCompleteControlCall(event,gridContainerDiv,dropDownId)
function autoCompleteControlCall(event,gridDropDownInfo,gridObj)
{
if(event.keyCode != 13)
{
if(document.getElementById(gridDropDownInfo['dropDownId']).value=="")
{
gridDropDownInfo['visibilityStatusVariable'] = false;
document.getElementsByName(gridDropDownInfo['propertyId']).value = "";
}
else
{
gridDropDownInfo['visibilityStatusVariable'] = true;
}
autoCompleteControlGeneric(gridDropDownInfo['gridDiv'],gridDropDownInfo['dropDownId'],gridObj);
}
}
function onInvestigatorListReady()
{
var principalInvestigatorsId = '${collectionProtocolForm.principalInvestigatorId}';
if(principalInvestigatorsId != "" && principalInvestigatorsId != 0 && principalInvestigatorsId != null)
investigatorOnRowSelect(principalInvestigatorsId,0);
}
function onParentProtocolListReady()
{
var parentProtocolId = '${collectionProtocolForm.parentCollectionProtocolId}';
if(parentProtocolId != "" && parentProtocolId != 0 && parentProtocolId != null)
parentProtocolOnRowSelect(parentProtocolId,0);
}
function doOnLoad()
{
if("<%=request.getParameter("isErrorPage")%>"=='true' && '${requestScope.operation}'=='add')
{
window.parent.firstTimeLoad = true; //this require to create cp node when user directly click on Add Event button
}
//Drop Down components information
investigatorDropDownInfo = {propertyId:'principalInvestigatorId',gridObj:"principleInvestigatorGrid", gridDiv:"principleInvestigator", dropDownId:"principleInvestigatorDropDown", pagingArea:"principleInvestigatorPagingArea", infoArea:"principleInvestigatorInfoArea", onOptionSelect:investigatorOnRowSelect, actionToDo:"CatissueCommonAjaxAction.do?type=allUserList", callBackAction:onInvestigatorListReady, visibilityStatusVariable:piGridVisible};
parentProtocolDropDownInfo = {propertyId:'parentCollectionProtocolId',gridObj:"parentProtocolGrid", gridDiv:"parentProtocol", dropDownId:"parentProtocolDropDown", pagingArea:"ppPagingArea", infoArea:"ppInfoArea", onOptionSelect:parentProtocolOnRowSelect, actionToDo:"CatissueCommonAjaxAction.do?type=getAllCPList", callBackAction:onParentProtocolListReady, visibilityStatusVariable:ppGridVisible};
// initialising grid
piGrid = initDropDownGrid(investigatorDropDownInfo,true); //initialize DropDown control for priciple Investigator
ppGrid = initDropDownGrid(parentProtocolDropDownInfo,true); //initialize DropDown control for priciple Investigator
enableDisableParentProtocol('${collectionProtocolForm.type}');
//initializing tab buttons
tabbar = new dhtmlXTabBar("tabbar_div", "top");
tabbar.setSkin('dhx_skyblue');
tabbar.setImagePath("dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
tabbar.addTab("collectionProtocolTab", "Collection Protocol Details", "170px");
tabbar.addTab("consentTab", "Consents", "100px");
tabbar.addTab("privilege", "Privileges", "100px");
tabbar.addTab("defineDashboardItemsTab", "Configure Dashboard", "130px");
tabbar.setContent("collectionProtocolTab","collectionProtocolContentDiv");
tabbar.setContent("consentTab","consentDiv");
tabbar.setContent("privilege","previlegDiv");
tabbar.setContent("defineDashboardItemsTab","dashbordDiv");
tabbar.enableAutoReSize(true);
tabbar.showInnerScroll();
var url = "DefineEvents.do?pageOf=pageOfAssignPrivilegePage&cpOperation=AssignPrivilegePage&operation=${requestScope.operation}";
if('${requestScope.tabSel}'=='consentTab')
tabbar.setTabActive("consentTab");
else if (('${requestScope.tabSel}'=='defineDashboardItemsTab') )
tabbar.setTabActive("defineDashboardItemsTab");
else
tabbar.setTabActive("collectionProtocolTab");
if('${requestScope.deleteNode}' != "")
{
window.parent.frames['CPTreeView'].deleteCPTreeNode('${requestScope.deleteNode}',false)
}
<%String nodeToBeSelected = (String) request.getSession()
					.getAttribute("nodeId");
			if (nodeToBeSelected != null
					&& nodeToBeSelected.startsWith("cpName")) {%>
window.parent.frames['CPTreeView'].setGlobalNodeKeys("<%=request.getSession().getAttribute("nodeId")%>",true);
<%} else {%>
window.parent.frames['CPTreeView'].setGlobalNodeKeys("<%=request.getSession().getAttribute("nodeId")%>",false);
<%}%>
if("<%=request.getParameter("nodeClicked")%>" != 'true' )
{
window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}&isErrorPage=${requestScope.isErrorPage}";
}
}
function defineEvents()
{
var action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=${requestScope.operation}";
document.forms[0].action=action;
document.forms[0].submit();
}
function updateCPTree()
{
window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
}
function enableDisableParentProtocol(associationType)
{
var imgObj = document.getElementById("ppDropDownId");
if(associationType == 'Parent')
{
document.getElementsByName("parentCollectionProtocolId")[0].value = "";
document.getElementById("parentProtocolDropDown").value="";
document.getElementById("parentProtocolMendatorySymbol").innerHTML = "&nbsp;";
document.getElementById("parentProtocolDropDown").setAttribute("disabled",true);
imgObj.onclick =function (e) {};
hideGrid(parentProtocolDropDownInfo['gridDiv']);
piGridVisible = false;
document.getElementById("studyCalendarEventPoint").setAttribute("readOnly",true);
document.getElementById("sequenceNumber").setAttribute("readOnly",true);
document.getElementById("studyCalendarEventPoint").value="";
document.getElementById("sequenceNumber").value="";
document.getElementById("parentProtocolRow").style.display = 'none';
document.getElementById("ScepAndSequenceNumberRow").style.display = 'none';
}
else
{
document.getElementById("parentProtocolMendatorySymbol").innerHTML = "<image src='images/uIEnhancementImages/star.gif' alt='Mandatory'>";
document.getElementById("parentProtocolDropDown").removeAttribute("disabled");
//imgObj.onclick =function (e) {showHideParentProtocolGrid(e,'parentProtocol','parentProtocolDropDown');};
imgObj.onclick =function (e) {showHideGrid(e,parentProtocolDropDownInfo,ppGrid);};
document.getElementById("studyCalendarEventPoint").removeAttribute("readOnly");
document.getElementById("sequenceNumber").removeAttribute("readOnly");
document.getElementById("parentProtocolRow").style.display = '';
document.getElementById("ScepAndSequenceNumberRow").style.display = '';
}
}
</script>
<style>
div#d1 {
	display: none;
}

div#d999 {
	display: none;
}
</style>
</head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<LINK href="css/calanderComponent.css" type="text/css" rel="stylesheet">
<body onload="doOnLoad();doCPDashboardInitGrid();">
<script language="JavaScript" type="text/javascript"	src="jss/wz_tooltip.js"></script>

<html:form action='${requestScope.formName}'
	styleId="CollectionProtocolForm">

   <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
		<tr height="100%">
			<td height="100%">
                             
                         
			<table width="100%" border="0" cellpadding="0" cellspacing="0" height="100%">
				<tr>
					<td>
						<%@ include file="/pages/content/common/ActionErrors.jsp"%>
					</td>
				</tr>
				<tr height="100%">
					<td width="100%" height="100%" >
						<div id="tabbar_div" style="width:auto;height:100%;">

					</td>
				</tr>
			</table>
			</td>
		</tr>
		</table>
		<div id="collectionProtocolContentDiv">
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				id="table1">
				<html:hidden property="operation" />
				<html:hidden property="submittedFor" />
				<html:hidden property="onSubmit" />
				<html:hidden property="id" />
				<html:hidden property="redirectTo" />
				<html:hidden property="pageOf" />
				<!-- html:hidden property="type" /-->
				<tr>
					<td width="1%" align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td width="30%" align="left" class="black_ar"><bean:message
							key="collectionprotocol.typeofassiciation" /></td>
					<td width="69%" align="left" class="black_ar"><logic:iterate
							id="associationTypeValue"
							collection="<%=request
							.getAttribute(Constants.CP_ASSOCIATION_TYPE_LIST)%>">
							<c:if
								test="${collectionProtocolForm.type == associationTypeValue}">
								<div style="float: left;">
									<input type="radio" value="${associationTypeValue}"
										onclick="enableDisableParentProtocol(this.value);" name="type"
										checked="true">
									<bean:write name="associationTypeValue" />
									&nbsp;</input>
								</div>
							</c:if>
							<c:if
								test="${collectionProtocolForm.type != associationTypeValue}">
								<div style="float: left;">
									<input type="radio" value="${associationTypeValue}"
										onclick="enableDisableParentProtocol(this.value);" name="type">
									<bean:write name="associationTypeValue" />
									&nbsp;</input>
								</div>
							</c:if>
						</logic:iterate></td>
				</tr>
				<tr id="parentProtocolRow">
					<td width="1%" align="center" class="black_ar"><span
						id="parentProtocolMendatorySymbol"></span></td>
					<td width="30%" align="left" class="black_ar"><bean:message
							key="collectionprotocol.parentcollectionprotocol" /></td>
					<td width="69%" align="left" class="black_ar"><html:hidden
							property="parentCollectionProtocolId" />
						<div>
							<table border="0" width="28%" id="outerTable2" cellspacing="0"
								cellpadding="0">
								<tr>
									<td align="left" width="88%" height="100%">
										<div id="ppDropDownIddiv" class="x-form-field-wrap "
											style="width: 137px;">
											<input id="parentProtocolDropDown"
												onkeydown="keyNavigationCall(event,parentProtocolDropDownInfo,ppGrid);"
												onKeyUp="autoCompleteControlGeneric(event,parentProtocolDropDownInfo,ppGrid);"
												onClick="noEventPropogation(event)" autocomplete="off"
												size="18"
												class="black_ar_new x-form-text x-form-field x-form-focus" /><img
												id="ppDropDownId" style="top: 0px !important;"
												class="x-form-trigger x-form-arrow-trigger"
												src="images/uIEnhancementImages/s.gif" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div id="parentProtocol" style="z-index: 100;"
											onClick="noEventPropogation(event);">
											<div id="parentProtocolGrid" style="height: 200px;"
												onClick="noEventPropogation(event)"></div>
											<div id="ppPagingArea" onClick="noEventPropogation(event)"></div>
											<div id="ppInfoArea" onClick="noEventPropogation(event)"></div>
										</div>
									</td>
								</tr>
							</table>
						</div></td>
				</tr>
				<tr id="ScepAndSequenceNumberRow">
					<td width="1%" align="center" class="black_ar">&nbsp;</td>
					<td width="30%" align="left" class="black_ar"><bean:message
							key="collectionprotocol.studyCalEventPoint" /></td>
					<td width="69%" align="left" class="black_ar">
						<table width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td width="31%" align="left" class="black_ar"><html:text
										styleClass="black_ar" maxlength="50" size="18"
										styleId="studyCalendarEventPoint"
										property="studyCalendarEventPoint"
										readonly='${requestScope.hasParent}' /></td>
								<td width="1%" align="left" class="black_ar">&nbsp;</td>
								<td width="23%" align="left" class="black_ar"><bean:message
										key="collectionprotocol.sequence.number" /></td>
								<td width="45%" align="left" class="black_ar"><html:text
										styleClass="black_ar" maxlength="50" size="20"
										styleId="sequenceNumber" property="sequenceNumber"
										readonly='${requestScope.hasParent}' /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="1%" align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td width="30%" align="left" class="black_ar"><bean:message
							key="collectionprotocol.principalinvestigator" /></td>
					<td width="69%" align="left" class="black_ar"><html:hidden
							property="principalInvestigatorId" />
						<div>
							<table border="0" width="28%" id="outerTable2" cellspacing="0"
								cellpadding="0">
								<tr>
									<td align="left" width="88%" height="100%">
										<div id="piDropDownIddiv" class="x-form-field-wrap"
											style="width: 137px;">
											<input id="principleInvestigatorDropDown"
												onkeydown="keyNavigationCall(event,investigatorDropDownInfo,piGrid);"
												onKeyUp="autoCompleteControlGeneric(event,investigatorDropDownInfo,piGrid);"
												onClick="noEventPropogation(event)" autocomplete="off"
												size="18"
												class="black_ar_new x-form-text x-form-field x-form-focus" /><img
												id="piDropDownId" style="top: 0px !important;"
												class="x-form-trigger x-form-arrow-trigger"
												onclick="showHideGrid(event,investigatorDropDownInfo,piGrid);"
												src="images/uIEnhancementImages/s.gif" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div id="principleInvestigator" style="z-index: 100"
											onClick="noEventPropogation(event)">
											<div id="principleInvestigatorGrid" style="height: 100px;"
												onClick="noEventPropogation(event)"></div>
											<div id="principleInvestigatorPagingArea"
												onClick="noEventPropogation(event)"></div>
											<div id="principleInvestigatorInfoArea"
												onClick="noEventPropogation(event)"></div>
										</div>
									</td>
								</tr>
							</table>
						</div></td>
				</tr>
				<tr>
					<td align="center" class="black_ar" width="1%">&nbsp;</td>
					<td align="left" valign="top" class="black_ar" width="30%"><label
						for="cpcoordinatorIds"><bean:message
								key="collectionprotocol.protocolcoordinator" /></label></td>
					<td class="black_ar" width="69%">
						<div style="width: 100%;">
							<table width="100%" cellpadding="0">
								<tr>
									<td class="black_ar" align="left" width="57%"><mCombo:multiSelectUsingCombo
											identifier="pcoordinatorIds" size="18"
											styleClass="black_ar_new"
											addNewActionStyleClass="black_ar_new"
											addButtonOnClick="moveOptions('pcoordinatorIds','coordinatorIds', 'add')"
											removeButtonOnClick="moveOptions('coordinatorIds','pcoordinatorIds', 'edit')"
											selectIdentifier="coordinatorIds"
											collection="${requestScope.selectedCPCoordinatorIds}"
											numRows="4" /></td>
									<td><label><html:link href="#" styleId="newUser"
												styleClass="view"
												onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')">
												<bean:message key="buttons.addNew" />
											</html:link></label></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td align="left" class="black_ar"><bean:message
							key="collectionprotocol.protocoltitle" /></td>
					<!-- Added by geeta -->
					<%
						if (Variables.isCPTitleChange) {
					%>
					<td align="left"><html:textarea styleClass="black_ar"
							cols="28" rows="3" styleId="title" property="title"
							readonly='${requestScope.readOnlyValue}' /></td>
					<%
						} else {
					%>
					<td align="left"><html:text styleClass="black_ar"
							maxlength='${requestScope.fieldWidth}' size="30" styleId="title"
							property="title" readonly='${requestScope.readOnlyValue}' /></td>
					<%
						}
					%>
				</tr>
				<tr>
					<td align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td align="left" class="black_ar"><bean:message
							key="collectionprotocol.shorttitle" /></td>
					<td align="left"><html:text styleClass="black_ar"
							maxlength="50" size="30" styleId="shortTitle"
							property="shortTitle" readonly='${requestScope.readOnlyValue}' /></td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><label for="irbID"><bean:message
								key="collectionprotocol.irbid" /></label></td>
					<td align="left"><html:text styleClass="black_ar"
							maxlength="50" size="30" styleId="irbID" property="irbID"
							readonly='${requestScope.readOnlyValue}' /></td>
				</tr>
				<tr>
					<td align="center" class="black_ar"><img
						src="images/uIEnhancementImages/star.gif" alt="Mandatory"
						width="6" height="6" hspace="0" vspace="0" /></td>
					<td align="left" class="black_ar"><label for="startDate"><bean:message
								key="collectionprotocol.startdate" /> </label></td>
					<td align="left" valign="top"><ncombo:DateTimeComponent
							name="startDate" id="startDate" formName="collectionProtocolForm"
							month='${requestScope.collectionProtocolMonth}'
							year='${requestScope.collectionProtocolYear}'
							day='${requestScope.collectionProtocolDay}'
							pattern="<%=CommonServiceLocator.getInstance().getDatePattern() %>"
							value='${requestScope.currentCollectionProtocolDate}'
							styleClass="black_ar" /> <span class="grey_ar_s capitalized">
							[${datePattern}] </span>&nbsp;</td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><label for="consentWaived"><bean:message
								key="consent.consentwaived" /></label></td>
					<td align="left" class="black_ar"><html:radio
							property="consentWaived" styleId="consentWaived" value="true" />&nbsp;<label
						for="consentWaived"><bean:message
								key="consent.consentwaivedyes" /></label>&nbsp;&nbsp;<html:radio
							property="consentWaived" styleId="consentWaived" value="false" /><label
						for="consentWaived">&nbsp;<bean:message
								key="consent.consentwaivedno" /></label></td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><bean:message
							key="collectionprotocol.participants" /></td>
					<td align="left"><html:text styleClass="black_ar"
							maxlength="10" size="30" styleId="enrollment"
							property="enrollment" readonly='${requestScope.readOnlyValue}' /></td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><bean:message
							key="collectionprotocol.ppidformat" /></td>
					<td align="left"><div style="width: 100%;">
							<div style="float: left;">
								<div>
									<span class="black_ar">Prefix</span>
								</div>
								<div>
									<html:text styleClass="black_ar" maxlength="255" size="10"
										styleId="prefixPid" property="prefixPid"
										readonly='${requestScope.readOnlyValue}'
										onblur="genSamplePPID()" />
								</div>
							</div>
							<div style="float: left; margin-left: 10px;">
								<div>
									<span class="black_ar">Id Size</span>
								</div>
								<div>
									<html:text styleClass="black_ar" maxlength="255" size="10"
										styleId="noOfDigitPid" property="noOfDigitPid"
										readonly='${requestScope.readOnlyValue}'
										onblur="genSamplePPID()" />
								</div>
							</div>
							<div style="float: left; margin-left: 10px;">
								<div>
									<span class="black_ar">Postfix</span>
								</div>
								<div>
									<html:text styleClass="black_ar" maxlength="255" size="10"
										styleId="postfixPid" property="postfixPid"
										readonly='${requestScope.readOnlyValue}'
										onblur="genSamplePPID()" />
								</div>
							</div>
							<div style="float: left; margin-left: 10px;">
								<div>
									<span class="black_ar">Preview</span>
								</div>
								<span id="samplePPID" class="black_ar"></span>
							</div>
						</div></td>
				</tr>
				<script>
genSamplePPID();
function genSamplePPID(){
var prefix = document.getElementById("prefixPid").value;
var postfix = document.getElementById("postfixPid").value;
if((prefix==undefined || prefix == "")&&(postfix==undefined || postfix == "")){
document.getElementById("samplePPID").textContent = "";
document.getElementById("samplePPID").innerText = "";
return;
}
var sampleid = ""+prefix+getSampleDigit()+ postfix;
document.getElementById("samplePPID").textContent = sampleid;
document.getElementById("samplePPID").innerText = sampleid;
}
function getSampleDigit(){
var num = document.getElementById("noOfDigitPid").value;
if(num==undefined || num == ""){
return 7;
}else{
var retVal = "";
for(var cnt =0; cnt<num-1; cnt++){
retVal += "0"
}
retVal += "7";
return retVal;
}
}
</script>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><label for="departmentId"><bean:message
								key="collectionprotocol.descriptionurl" /> </label></td>
					<td align="left"><html:text styleClass="black_ar"
							maxlength="200" size="30" styleId="descriptionURL"
							property="descriptionURL"
							readonly='${requestScope.readOnlyValue}' /></td>
				</tr>
				<logic:equal name='${requestScope.operation}' value="edit">
					<tr>
						<td align="center" class="black_ar">&nbsp;</td>
						<td align="left" class="black_ar"><bean:message
								key="site.activityStatus" /></td>
						<td align="left" class="black_ar"><logic:iterate
								id="activityStatusValue"
								collection="${requestScope.activityStatusList}" offset="1">
								<c:if
									test="${collectionProtocolForm.activityStatus == activityStatusValue}">
									<div style="float: left;">
										<input type="radio" value="${activityStatusValue}"
											valign="middle" onclick='${strCheckStatus}'
											name="activityStatus" checked="true">&nbsp;
										<bean:write name="activityStatusValue" />
										&nbsp;</input>
									</div>
								</c:if>
								<c:if
									test="${collectionProtocolForm.activityStatus != activityStatusValue}">
									<div style="float: left;">
										<input type="radio" value="${activityStatusValue}"
											valign="middle" onclick='${strCheckStatus}'
											name="activityStatus">&nbsp;
										<bean:write name="activityStatusValue" />
										&nbsp;</input>
									</div>
								</c:if>
							</logic:iterate></td>
					</tr>
				</logic:equal>
				<%
					if ("true".equals(XMLPropertyHandler
								.getValue(Constants.EMPI_ENABLED))) {
				%>
				<tr>
					<td width="1%" align="center" class="black_ar">&nbsp</td>
					<td width="16%" align="left" class="black_ar"><label
						for="iseMPIEnable"> <bean:message
								key="collectionProtocol.isEMPIEnable" />
					</label></td>
					<td align="left" class="black_ar"><html:radio
							property="isEMPIEnable" styleId="consentWaived" value="true" />&nbsp;<label
						for="consentWaived"><bean:message
								key="consent.consentwaivedyes" /></label>&nbsp;&nbsp;<html:radio
							property="isEMPIEnable" styleId="consentWaived" value="false" /><label
						for="consentWaived">&nbsp;<bean:message
								key="consent.consentwaivedno" /></label></td>
				</tr>
				<%
					}
				%>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><bean:message
							key="specimenCollectionGroup.clinicalDiagnosis" /></td>
					<td align="left" class="black_ar"><mCombo:multiSelectUsingCombo
							identifier="coord" size="18" styleClass="black_ar_new"
							addNewActionStyleClass="black_ar_new"
							addButtonOnClick="moveOptions('coord','protocolCoordinatorIds', 'add')"
							removeButtonOnClick="moveOptions('protocolCoordinatorIds','coord', 'edit')"
							selectIdentifier="protocolCoordinatorIds"
							collection="${requestScope.selectedCoordinators}" numRows="4" /></td>
				</tr>
				<!------------------------------------------------------------------------>
				<%
					if (Variables.isTemplateBasedLblGeneratorAvl) {
				%>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar">Parent Specimen Label Format</td>
					<td><html:text styleClass="black_ar" maxlength="255"
							size="108" styleId="specimenLabelFormat"
							property="specimenLabelFormat" /></td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar">Derivative Specimen Label
						Format</td>
					<td><html:text styleClass="black_ar" maxlength="255"
							size="108" styleId="derivativeLabelFormat"
							property="derivativeLabelFormat" /></td>
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar">Aliquot Specimen Label
						Format</td>
					<td><html:text styleClass="black_ar" maxlength="255"
							size="108" styleId="aliquotLabelFormat"
							property="aliquotLabelFormat" /></td>
				</tr>
				<%
					}
				%>
				<tr height="8">
					<td colspan="3" />
				</tr>
				<tr>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" class="black_ar"><label>Store all
							aliquot(s) in same container?</label>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td align="left" class="black_ar"><html:radio
							property="aliqoutInSameContainer"
							styleId="aliqoutInSameContainer" value="true" />&nbsp;<bean:message
							key="consent.consentwaivedyes" />&nbsp;&nbsp;<html:radio
							property="aliqoutInSameContainer"
							styleId="aliqoutInSameContainer" value="false" />&nbsp;<label
						for="consentWaived"><bean:message
								key="consent.consentwaivedno" /></label></td>
				</tr>
				<!------------------------------------------------------------------------->
				<tr height="10">
					<td colspan="3" />
				</tr>
			</table>
		</div>
		<div id="consentDiv" height="100%" overflow="auto">
			<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp"%>
		</div>
		</div>
		<div id="dashbordDiv">
			<%@ include
				file="/pages/content/manageAdministrativeData/ConfigureCPDashboard.jsp"%>
		</div>
		<div id="previlegDiv">
			<%@ include
				file="/pages/content/manageAdministrativeData/ShowAssignPriviledge.jsp"%>
		</div>
		</td>
		</tr>
		</table>
		</div>
	</html:form>
	<script>
Ext.onReady(function(){
var commonActionURL = 'CatissueCommonAjaxAction.do?';
// Defining Cinical Diagnosis Combobox
var diagnosisDS = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: commonActionURL}),
baseParams:{type:'getClinicalDiagnosisValues'},
reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
[{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});
var diagnosisCombo = new Ext.form.ComboBox({
store: diagnosisDS,hiddenName: 'CB_coord',displayField:'excerpt',valueField: 'id',
typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',
mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,
cls:Ext.isIE?'forIe':'',
emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'coord'});
diagnosisCombo.on("expand", function() {
if(Ext.isIE || Ext.isIE7){diagnosisCombo.list.setStyle("width", "250");diagnosisCombo.innerList.setStyle("width", "250");}
else{diagnosisCombo.list.setStyle("width", "auto");diagnosisCombo.innerList.setStyle("width", "auto");}}, {single: true});
diagnosisDS.on('load',function(){
if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(diagnosisCombo.getRawValue().toLowerCase())) {diagnosisCombo.typeAheadDelay=50;}
else {diagnosisCombo.typeAheadDelay=60000}});
// Defining Cinical Diagnosis Combobox defined
// Defining Coordinator Combobox
var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: commonActionURL }),
baseParams:{type:'getUserListAsJSon'},
reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
[{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});
var coordinatorCombo = new Ext.form.ComboBox({
store: ds,hiddenName: 'CB_pcoordinatorIds',displayField:'excerpt',valueField: 'id',
typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',
cls:Ext.isIE?'forIe':'',
mode: 'remote',triggerAction: 'all',minChars : 1,queryDelay:500,lazyInit:true,
emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'pcoordinatorIds'});
coordinatorCombo.on("expand", function() {
if(Ext.isIE || Ext.isIE7){coordinatorCombo.list.setStyle("width", "250");coordinatorCombo.innerList.setStyle("width", "250");}
else{coordinatorCombo.list.setStyle("width", "auto");coordinatorCombo.innerList.setStyle("width", "auto");}}, {single: true});
ds.on('load',function(){
if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(coordinatorCombo.getRawValue().toLowerCase())) {coordinatorCombo.typeAheadDelay=50;}
else {coordinatorCombo.typeAheadDelay=60000}});
});
if(document.getElementById("errorRow")!=null && document.getElementById("errorRow").innerHTML.trim()!="")
{
window.top.document.getElementById("errorRow").innerHTML = "";
}
</script>
<link rel="STYLESHEET" type="text/css" 
	href="css/dhtmldropdown.css"/>
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

div.dhx_page_active {
    background-color: rgb(191, 220, 243);
}

.dhx_pbox {
    margin-top: 3px;
    border-width: 1px 1px 1px;
    border-style: solid;
    border-color: grey;
}

div.gridbox_drop{
    border:1px solid grey;
}
</style>
</body>
