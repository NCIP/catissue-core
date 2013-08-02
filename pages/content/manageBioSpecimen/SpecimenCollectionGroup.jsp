<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.CDMSIntegrationConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.css"/>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>

<script>
      window.dhx_globalImgPath="dhtmlx_suite/imgs/";
</script>

<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/calendarComponent.js" type="text/javascript"></script>
<LINK href="css/calanderComponent.css" type="text/css" rel="stylesheet"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/specimenCollectionGroup.js"></script>

<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/ext/dhtmlxcombo_whp.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css"/>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	Long reportIdVal=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID);
%>
<script>
function updateHelpURL()
	{
		var URL="";
		if("pageOfSpecimenCollectionGroupCPQuery"=="<%=pageOf%>")
		{
			URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm")%>";
		}
		return URL;
	}
</script>
<%
 	String operation = (String)request.getAttribute(Constants.OPERATION);
	String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String signedConsentDate = "";
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	String warning_unsaved_data = ApplicationProperties.getValue("unsaved.data.warning");
	String clinportalUrl =(String) request.getAttribute(CDMSIntegrationConstants.CALLBACK_URL);
	boolean isAddNew = false;
	Long scgEntityId = null;
	scgEntityId = (Long)request.getAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
	String participantId=null;
	Object obj = request.getAttribute("specimenCollectionGroupForm");
	SpecimenCollectionGroupForm form =null;
	if(obj != null && obj instanceof SpecimenCollectionGroupForm)
	{
 		form=(SpecimenCollectionGroupForm)obj;
 		participantId=""+form.getParticipantId();
	}
	String id = request.getParameter("id");
	String appendingPath = "/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
	if (reqPath != null)
 	appendingPath = reqPath + "|/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
 	if(form  != null)
 	{
 		if(id==null)
 		{
 			id=String.valueOf(form.getId());
 		}
 	}
	String nodeId="";
	String formName, pageView = operation ,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=false;
	if(!operation.equals("add") )
	{
		if(form != null)
		{
			appendingPath = "/SpecimenCollectionGroupSearch.do?operation=search&pageOf="+pageOf+"&id="+form.getId() ;
					int radioButtonForParticipant1 = form.getRadioButtonForParticipant();
			nodeId= "SpecimenCollectionGroup_"+form.getId();

		}

 	   	}

	if(Constants.EDIT.equals(operation)|| ("viewAnnotations").equals(operation))
 		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION +"?";
			readOnlyValue=true;
			if(Constants.QUERY.equals(pageOf))
				formName = Constants.QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
			if(Constants.PAGE_OF_SCG_CP_QUERY.equals(pageOf))
			{
				formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
			}
 		}
 		else
 		{
			formName = Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION;
			if(Constants.PAGE_OF_SCG_CP_QUERY.equals(pageOf))
			{
				formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION + "?pageOf="+pageOf;
			}
 			readOnlyValue=false;
 		}
 		long idToTree =0;
 		if(form!=null)
 		{
 			idToTree = form.getId();
 		}
 		String currentReceivedDate = "";
 		String currentCollectionDate = "";
 		if (form != null)
 		{
		currentReceivedDate = form.getReceivedEventDateOfEvent();
		if(currentReceivedDate == null)
				currentReceivedDate = "";
		currentCollectionDate = form.getCollectionEventdateOfEvent();
		if(currentCollectionDate == null)
 			currentCollectionDate = "";
 		}

 		String formNameForCal = "specimenCollectionGroupForm";
 		String numberOfSpecimenCollection = (String)request.getAttribute(Constants.NUMBER_OF_SPECIMEN_REQUIREMENTS);
 		if(numberOfSpecimenCollection == null)
 		{
 	numberOfSpecimenCollection = "0";
 		}
 %>
<head>

	<%
		String refreshTree = (String)request.getAttribute("refresh");
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
		if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY) && (refreshTree==null || !(refreshTree.equalsIgnoreCase("false"))))
		{
	%>
		<script language="javascript">
		//Added by Falguni to refresh participant tree
		var nodeid =  "<%=nodeId%>";
	//	top.frames["cpAndParticipantView"].editParticipant(<%=participantId%>,nodeid);
		if(nodeid!=""&&nodeid!="1")
		{
	//	alert("nodeid: "+nodeid);
		refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');
		}
		</script>
	<%
		}
	%>

	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript" >
		var isPHIVIEW =   ${specimenCollectionGroupForm.isPhiView};
		var disablePHIView = !isPHIVIEW;	
     </script>
	<script language="JavaScript">
	function makeClinPortalUrl()
	{
		var submitButton = document.getElementById("submitOnly");
		submitButton.disabled = false;
		document.getElementById('isClinicalDataEntry').value="true";
		var scgId=document.getElementById("id").value;
		var url='<%=clinportalUrl%>'+"&scgId="+scgId;
		if (confirm("<%=warning_unsaved_data%>"))
		{
			document.getElementById('clinicalDataEntryURL').value=url;
			if (window.ActiveXObject)
			{
				submitButton.click();
			}
			else
			{
				submitButton.enable();
				if(submitButton.enable())
				{
					submitButton.click();
				}
				else
				{
					makeClinPortalUrl();
				}
			}
		}
		else
		{
			var clinportalPath = url.split("?");
			var clinportalPath1 = clinportalPath[0];
			var clinportalPath2 = clinportalPath[1];
			var request = newXMLHTTPReq();
			request.onreadystatechange = getReadyStateHandler(request,openClinportalPage,true);
			request.open("POST","AjaxAction.do?method=encryptData",true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			var dataToSend = clinportalPath2;
			request.send(dataToSend);
		}
	}
	function logout()
	{
		var request = newXMLHTTPReq();
		request.onreadystatechange = getReadyStateHandler(request,"",true);
		request.open("POST","AjaxAction.do?method=logout",true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var dataToSend = "";
		request.send(dataToSend);
	}

	function openClinportalPage(dataString)
	{
		var clinportalUrl='<%=clinportalUrl%>';
		var clinportalUrlPath = clinportalUrl.split("?");
		var clinportalUrlPath1 = clinportalUrlPath[0];
		logout();
		window.top.location=clinportalUrlPath1 + "?method=login&path=" + dataString;
	}
		
	function onChangeEvent(element,cpID)
	{
		//var getCPID=document.getElementById('collectionProtocolId');
		//var cpID=getCPID.value;
		//var getID=document.getElementById(element);
		var index=element.getSelectedText();
		if(index<0)
		{
			alert("Please Select Valid Value");
		}
		else
		{
			if(element.name=='collectionProtocolEventId')
			{
				var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&protocolEventId=true&showConsents=yes&pageOf=pageOfSpecimenCollectionGroup&" +
					"isOnChange=true&cpID="+cpID;
			}
			else
			{
				var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&protocolEventId=false&showConsents=yes&pageOf=pageOfSpecimenCollectionGroup&" +
					"isOnChange=true&cpID="+cpID;

			}
			changeAction(action);
		}
	}
	function onChange(element)
	{
		var action = "SpecimenCollectionGroup.do?operation=<%=operation%>&pageOf=pageOfSpecimenCollectionGroup&" +
				"isOnChange=true";
		changeAction(action);
	}
	
	function switchToTab(selectedTab)
	{
		var displayKey="block";
		var showAlways="block";
		if(!document.all)
		{
			displayKey="table";
			showAlways="table";
		}

		var displayTable=displayKey;
		var tabSelected="none";
		if(selectedTab=="specimenCollectionGroupTab")
		{
			tabSelected=displayKey;
			displayTable="none";
		}

		var display=document.getElementById('collectionEvent');
		display.style.display=tabSelected;

		var display=document.getElementById('scgTable');
		display.style.display=tabSelected;

		var display=document.getElementById('multiplespecimenTable');
		display.style.display=tabSelected;

		var display=document.getElementById('scgPageButtons');
		display.style.display=tabSelected;

		var displayConsentTable=document.getElementById('consentTabForSCG');
		if(displayConsentTable!=null)
		{
			displayConsentTable.style.display=displayTable;
		}
		//var collectionTab=document.getElementById('specimenCollectionGroupTab');
		var consentTab=document.getElementById('consentTab');

  }
	
		function editSCG()
		{
			var tempId='<%=request.getParameter("id")%>';
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId;
			if('<%=pageOf%>'=='<%=Constants.PAGE_OF_SCG_CP_QUERY%>')
			{
				action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=search&id="+tempId;
			}
			document.forms[0].action=action;
			document.forms[0].submit();
		}
	 </script>
</head>			
<!-- As it was giving javascript error on disableButtons() as the scg form is not loaded for DE -->
<%
	if(pageView != null && !("viewAnnotations").equals(pageView) && !(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT).equals(pageView))
	{
%>
	<body onload="loadSCGTabbar();disablebuttons();initializeSCGForm('<%=form%>','<%=form.getRestrictSCGCheckbox()%>');showConsents('<%=tab%>','<%=form%>','<%=form.getConsentTierCounter()%>');initializeSCGCombo('${operation}');">
<%
	}else{
%>
	<body>
 <%
 	}
 %>
<html:form action="<%=formName%>">
	<%
		if(("add").equals(pageView))
		{
	%>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	  <tr>
		<td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
				<td valign="bottom" class="td_color_bfdcf3" id="specimenCollectionGroupTab"><a href="#" id="SCGImage"><img src="images/uIEnhancementImages/tab_add_scg.gif" alt="Add Specimen  Collection group"  width="222" height="22" border="0"></a></td>
				<td align="left" valign="bottom" class="td_color_bfdcf3"  id="consentTab"><a href="#" id="consentsImage"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0" onClick="newConsentTab()"></a></td>
				<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
			</tr></table>
			<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				</td>
			</tr>
			</table>

		<%@ include file="EditSpecimenCollectionGroup.jsp" %>
		</td>
				</tr>
				</table>
	<%
		}
	%>

	<%
	   if(("edit").equals(pageView))
	  {
		%>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" height="100%">
	  <tr>
		<td>
		<div id="SCG_tabbar" style="width:99%; height:98%;position:absolute;"/>
			<div id='SCGDiv' style="overflow:auto;height:100%">	
		
			<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
				<tr>
					<td>
						<%@ include file="/pages/content/common/ActionErrors.jsp" %>
					</td>
				</tr>
		   </table><!-- Mandar 31Oct08 -->
				<%@ include file="EditSpecimenCollectionGroup.jsp" %>
			</div>
		</td>
		</tr>
	</table>
	<%
	}
	%>

<%
	if(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT.equals(pageView))
	{
%>
	<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="650">
			<tr>
				<td height="20" class="tabMenuItem"  id="specimenCollectionGroupTab"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="editSCG()">
					<bean:message key="specimenCollectionGroupPage.edit.title"/>
				</td>
				<td height="20" class="tabMenuItemSelected"   onClick="">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showAnnotations('<%=scgEntityId%>','<%=id%>','<%=staticEntityName%>','<%=pageOf%>')">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="newConsentTab()" id="consentTab">
					<bean:message key="consents.consents"/>
				</td>
				<td width="300" class="tabMenuSeparator" colspan="1" >&nbsp;</td>
			</tr>
			<tr>
			<td colspan="6">
				<table border="0" width="100%" class="whitetable_bg"  cellpadding="3" cellspacing="0">
				<tr>
					<td>
						<%@ include file="/pages/content/common/ActionErrors.jsp" %>
					</td>
				</tr>
				</table>
			</td>
			</tr>
			<tr>
				<td class="tabField" colspan="6">

				<jsp:include page="ViewSurgicalPathologyReport.jsp" />
				</td>
			</tr>
		</table>
	<%
	}
	%>

	<html:hidden property="nextForwardTo" />
	<input type="hidden" id="clinicalDataEntryURL" name="clinicalDataEntryURL" value=""/>
	<input type="hidden" id="isClinicalDataEntry" name="isClinicalDataEntry" value="false"/>
</html:form>
<script language="JavaScript" type="text/javascript">
initializeSCGCombo('${operation}');

var reportId='${sessionScope.identifiedReportId}';
var pageOfValue='${requestScope.pageOf}';
var scgEntityIdValue='${requestScope.scgRecordEntryEntityId}';

var idValue='<%=id%>';

var staticEntityNameValue='<%=staticEntityName%>'; 

var consentLevelId = document.getElementById("id").value;

var showViewSPRTab="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf="+pageOfValue+"&reportId="+reportId;

var showAnnotationTab="DisplayAnnotationDataEntryPage.do?entityId="+scgEntityIdValue+"&entityRecordId="+idValue+"&staticEntityName="+staticEntityNameValue+"&pageOf="+pageOfValue+"&operation=viewAnnotations";

var showConsentsTab="FetchConsents.do?consentLevelId="+consentLevelId+"&consentLevel=scg&entityId="+scgEntityIdValue+"&staticEntityName="+staticEntityNameValue;

showPriterTypeLocation();
if(disablePHIView)
{
  disableTabs();
}
</script>
</body>