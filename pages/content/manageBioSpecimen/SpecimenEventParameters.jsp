<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@ include file="/pages/content/common/EventAction.jsp" %>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css"/ >
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="jss/newSpecimen.js"></script>

<style>
.active-column-1 {width:200px}
</style>
<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
%>
<script>
function updateHelpURL()
	{
		var URL="";
		if("pageOfListSpecimenEventParametersCPQuery"=="<%=pageOf%>")
		{
			URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm")%>";
		}
		return URL;
	}
</script>
<%
String[] columnList1 = Constants.EVENT_PARAMETERS_COLUMNS;
List columnList = new ArrayList();
for(int i=0;i<columnList1.length;i++)
{
	columnList.add(columnList1[i]);
}
String title = null;
List dataList = (List) request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST);
String label=(String)request.getAttribute(Constants.SPECIMEN_LABEL);
//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
Integer identifierFieldIndex = new Integer(0);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	specimenIdentifier = (String)request.getParameter(Constants.SPECIMEN_ID);

if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

if(specimenIdentifier == null || specimenIdentifier.equals("0"))
{
	specimenIdentifier= (String)session.getAttribute(Constants.SPECIMEN_ID);
}

//------------- Mandar 04-july-06 QuickEvents
String eventSelected = (String)request.getAttribute(Constants.EVENT_SELECTED);
//P.G. - Start 24May07:Bug 4291:Added source as initial action for blank screen
String iframeSrc="blankScreenAction.do";
//P.G. - End
String formAction = Constants.SPECIMEN_ADD_ACTION;
String specimenPath ="'NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
if(pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
{
	specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
	consentTab = "'QuerySpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
}
if(eventSelected != null)
{
	iframeSrc = getEventAction(eventSelected, specimenIdentifier);
	formAction = Constants.QUICKEVENTSPARAMETERS_ACTION;
}
//------------- Mandar 04-july-06 QuickEvents
session.setAttribute("EventOrigin", "SpecimenEventParameters");
%>

<%if(pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
{
	String nodeId = "Specimen_"+specimenIdentifier;
%>
<script language="javascript">
refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');
</script>
<%}%>

<script language="JavaScript">
function showConsents()
{
	addNewAction(<%= consentTab %>)
}
function onParameterChange(element)
{
	var action = "";
	var iFrame = document.getElementById("newEventFrame");

	if(element.value == "Cell Specimen Review")
	action = "CellSpecimenReviewParameters.do?operation=add&pageOf=pageOfCellSpecimenReviewParameters";
	else if(element.value == "Check In Check Out")
	action = "CheckInCheckOutEventParameters.do?operation=add&pageOf=pageOfCheckInCheckOutEventParameters";
	else if(element.value == "Collection")
	action = "CollectionEventParameters.do?operation=add&pageOf=pageOfCollectionEventParameters";
	else if(element.value == "Disposal")
	action = "DisposalEventParameters.do?operation=add&pageOf=pageOfDisposalEventParameters";
	else if(element.value == "Embedded")
	action = "EmbeddedEventParameters.do?operation=add&pageOf=pageOfEmbeddedEventParameters";
	else if(element.value == "Fixed")
	action = "FixedEventParameters.do?operation=add&pageOf=pageOfFixedEventParameters";
	else if(element.value == "Fluid Specimen Review")
	action = "FluidSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfFluidSpecimenReviewParameters";
	else if(element.value == "Frozen")
	action = "FrozenEventParameters.do?operation=add&pageOf=pageOfFrozenEventParameters";
	else if(element.value == "Molecular Specimen Review")
	action = "MolecularSpecimenReviewParameters.do?operation=add&pageOf=pageOfMolecularSpecimenReviewParameters";
	else if(element.value == "Procedure")
	action = "ProcedureEventParameters.do?operation=add&pageOf=pageOfProcedureEventParameters";
	else if(element.value == "Received")
	action = "ReceivedEventParameters.do?operation=add&pageOf=pageOfReceivedEventParameters";
	else if(element.value == "Spun")
	action = "SpunEventParameters.do?operation=add&pageOf=pageOfSpunEventParameters";
	else if(element.value == "Thaw")
	action = "ThawEventParameters.do?operation=add&pageOf=pageOfThawEventParameters";
	else if(element.value == "Tissue Specimen Review")
	action = "TissueSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfTissueSpecimenReviewParameters";
	else if(element.value == "Transfer")
	{
		action = "TransferEventParameters.do?operation=add&pageOf=pageOfTransferEventParameters";
	}

	var specimenIdentifier = "<%=specimenIdentifier%>";
	action = action + "&specimenId=" + specimenIdentifier;
	iFrame.src = action;

	if(element.value == "<%=Constants.SELECT_OPTION%>")
	{
		iFrame.src = "blankScreenAction.do";
	}
}

if( document.getElementById && !(document.all) )
{
var slope=-37;
}
else
{
var slope=-42;
}

function mdResDetector()
{
//	alert(screen.width + "*" + screen.height);
	var ht = screen.height;
	var frmHt = Math.round(ht * 35/100);
//	alert(frmHt);
	document.getElementById("newEventFrame").height=frmHt+" px"	;
}

window.onresize = function() { mdResDetector(); }
//window.onload = function() { adjFrmHt('test', .1,slope);}
//window.onresize = function() { adjFrmHt('test', .1,slope); }
</script>
<!-- Mandar : 434 : for tooltip -->

</head>

<html:form action="<%=formAction%>">
<!-- Mandar 05-July-06 Code for tabs start -->
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		<tr>
			<td>
				
				<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
					<tr>
						<td colspan="3" align="left" >
							<%@ include file="/pages/content/common/ActionErrors.jsp" %>
						</td>
					</tr>
					<tr>
						<td class="bottomtd" colspan="3"></td>
					</tr>
					<tr>
						<%
						if(dataList!=null && dataList.size() != 0)
						{
						title = java.text.MessageFormat.format(Constants.EVENTS_TITLE_MESSAGE,new String[]{"'"+label+"'"});
						%>
						<td colspan="3" align="left" class="tr_bg_blue1">
							<span class="blue_ar_b">&nbsp;<%=title%> </span>
						</td>
					</tr>
					<tr>
						<td align="left" colspan="3"class="showhide">
							<table width="100%" border="0" cellspacing="0" cellpadding="3">
								<tr>
									<td>
										<script>
											function eventParametersGrid(id)
											{
											var cl = mygrid.cells(id,4);
											var pageOf = cl.getValue();
											var c2 = mygrid.cells(id,0);
											var eventId = c2.getValue();

											var url = "SearchObject.do?pageOf="+pageOf+"&operation=search&id="+eventId;
											var frame = document.getElementById("newEventFrame");
											frame.src = url;
											}
											/*
											to be used when you want to specify another javascript function for row selection.
											useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
											useFunction = "";  Function to be used.
											*/
											var useDefaultRowClickHandler =2;
											var gridFor="<%=Constants.GRID_FOR_EVENTS%>";
											var useFunction = "eventParametersGrid";
										</script>
										<%@ include file="/pages/content/search/GridPage.jsp" %>
									</td>
								</tr>
							</table>
						<% } else
						{
						title = "No Specimen Event Paremeters are available for Identifier : " + specimenIdentifier;
						%>
					<tr>
						<td align="left" colspan="3"class="showhide">
							<table width="100%" border="0" cellspacing="0" cellpadding="3">
								<tr>
									<td align="left" class="tr_bg_blue1">
										<span class="blue_ar_b"><%=title%></span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
						<% } %>
					<!-- tr>
						<td align="left" width="33%" class="black_ar">
							&nbsp;&nbsp;<bean:message key="specimenEventParameters.caption"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<html:select property="specimenEventParameter" styleClass="formFieldSized15" styleId="className" size="1" disabled="false" onchange="onParameterChange(this)" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.EVENT_PARAMETERS_LIST%>" labelName="<%=Constants.EVENT_PARAMETERS_LIST%>"/>
							</html:select>
						</td>
					</tr -->
					<tr>
						<td class="whitetable_bg" colspan="3" height="70%">
							<%
								if(dataList!=null && dataList.size() != 0)
							{%>
								<iframe name="newEventFrame" id="newEventFrame" src="<%=iframeSrc %>" width="100%" height="100%" frameborder="0" scrolling="auto">
								</iframe>
							<%}
							else
							{%>
								<iframe name="newEventFrame" id="newEventFrame" src="<%=iframeSrc %>" width="100%" height="99%" frameborder="0" scrolling="no">
								</iframe>
								<iframe name="test" id="test" width="100%" frameborder="0" scrolling="no">
								</iframe>
							<%}%>
						</td>
					</tr>
<!--					<tr height="*">
						<td>&nbsp;</td>
					</tr>
-->				</table>
			</td>
		</tr>
	</table>
</html:form>
<SCRIPT LANGUAGE="JavaScript">
<!--
 mdResDetector();
//-->
</SCRIPT>
