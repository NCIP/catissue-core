<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@ page import="org.apache.struts.Globals, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionErrors"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page language="java" isELIgnored="false"%>

<%@ include file="/pages/content/common/EventAction.jsp" %>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/jquery-1.3.2.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/javascripts/de/form_plugin.js" type="text/javascript"></script>


<%
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
	Long scgEntityId = null;
	scgEntityId = (Long)request.getAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);
	String id = request.getParameter("id");
	String specimenIdentifier = null;//request.getParameter("id");
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
	List sppNameList=(List)request.getAttribute("sppNameList");
	SpecimenCollectionGroupForm form =null;
	String participantId=null;
	Object obj = request.getAttribute("specimenCollectionGroupForm");
	if(obj != null && obj instanceof SpecimenCollectionGroupForm)
	{
 		form=(SpecimenCollectionGroupForm)obj;
 		participantId=""+form.getParticipantId();
	}
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
	request.setAttribute("showSPPHeader","true");
	request.setAttribute("showSkipEventCheckBoxes","false");
	request.setAttribute("showSPPDropdown","true");
%>
<script language="JavaScript">
	function showConsents()
	  {
		var showConsents = "<%=tab%>";
		if(showConsents=="<%=Constants.NULL%>" || showConsents=="scgPage")
		{
			specimencollgroup();
		}
		else
		{
			consentPage();
		}
	  }

	function viewSPR()
	{
		<% Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID); %>
		var reportId='<%=reportId%>';
		if(reportId==null || reportId==-1)
		{
			alert("There is no associate report in the system!");
		}
		else if(reportId==null || reportId==-2)
		{
			alert("Associated report is under quarantined request! Please contact administrator for further details.");
		}
		else
		{
			var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&reportId="+reportId;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
	}

	function submitSPPEvents()
	{
		var scgId = <%=id%>;
		var action="SaveSPPEventAction.do?operation=insertDEData&isSCG=true&id="+scgId+"&pageOf=pageOfSCG";

		var search = 'Control';
		var search1 = 'comboControl';
		var iframeList = document.getElementsByTagName('iframe');
		for(j =0;j<iframeList.length;j++)
		{
			var containerId= iframeList[j].name
			var oDoc = iframeList[j].contentWindow || iframeList[j].contentDocument;
			if (oDoc.document) {
				oDoc = oDoc.document;
			}
			var inputCollection = oDoc.getElementsByTagName('input');
			for(i=0; i<inputCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
				}
			}
			var selectCollection = oDoc.getElementsByTagName('select');
			for(i=0; i<selectCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
			}
			var textAreaCollection = oDoc.getElementsByTagName('textarea');
			for(i=0; i<textAreaCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+containerId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
				else
				{
					action=action+'&'+containerId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
			}


			oDoc = oDoc.getElementById("name1").contentWindow || iframeList[j].contentDocument;
			if (oDoc.document) {
				oDoc = oDoc.document;
			}
			var inputCollection = oDoc.getElementsByTagName('input');
			for(i=0; i<inputCollection.length ;i++)
			{
				if(inputCollection[i].name.indexOf(search) == 0 || inputCollection[i].name.indexOf(search1) == 0)
				{
					if(action.indexOf('?') == -1)
					{
						action=action+'?'+inputCollection[i].name+'='+inputCollection[i].value;
					}
					else
					{
						action=action+'&'+inputCollection[i].name+'='+inputCollection[i].value;
					}
				}
			}
			var a = document.getElementsByTagName('input');
			var i=0;
			for(i=0; i<a.length ;i++)
			{
				if(a[i].name.indexOf(search) == 0 || a[i].name.indexOf(search1) == 0)
				{
					if(action.indexOf('?') == -1)
					{
						action=action+'?'+a[i].name+'='+a[i].value;
					}
					else
					{
						action=action+'&'+a[i].name+'='+a[i].value;
					}
				}
			}
		}
		document.forms[0].action=action;
		//alert(action);
	}

	function confirmDisableForSCG(action,formField)
		{
			var temp = action+"&applyToSpecimenValue="+applyToSpecimen;
			if((formField != undefined) && (formField.value == "Disabled"))
			{
				var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
				if (go==true)
				{
					if(document.forms[0].nextForwardTo.value!=null)
					{
					 temp = temp + "&domainObject=SCG&nextForwardTo="+document.forms[0].nextForwardTo.value;
					}
				    document.forms[0].action = temp;
					document.forms[0].submit();
				}
			}
			else
			{
				document.forms[0].action = temp;
				document.forms[0].submit();
			}
		}

		function editSCG()
		{
			//alert("hello");
			//alert(<%=id%>);
			var tempId='<%=request.getParameter("id")%>';
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId;
			if('<%=pageOf%>'=='<%=Constants.PAGE_OF_SCG_CP_QUERY%>')
			{
				action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=search&id="+tempId;
			}
			//alert(action);
			document.forms[0].action=action;
			document.forms[0].submit();
		}

		function setTarget()
		{
			var fwdPage="<%=pageOf%>";
			if(!fwdPage=="pageOfSpecimenCollectionGroupCPQuery")
				document.forms[0].target = '_top';
		}
	function showAnnotations()
	{
		var action="DisplayAnnotationDataEntryPage.do?entityId=<%=scgEntityId%>&entityRecordId=<%=id%>&staticEntityName=<%=staticEntityName%>&pageOf=<%=pageOf%>&operation=viewAnnotations&id=<%=id%>";
		document.forms[0].action=action;
		document.forms[0].submit();
	}

	function goToConsentPage()
		{
			var tempId=<%=id%>;
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId+"&tab=consent";
			document.forms[0].action=action;
			document.forms[0].submit();
		}


	function onParameterChange(element)
	{
	var action = "";
	action = "DisplaySPPEventsForSCG.do?operation=add&sppName="+element.value+"&isSCG=true&id="+<%=id%>;
	document.forms[0].action=action;
	document.forms[0].submit();
	}

</script>

</head>

<html:form action="SaveSPPEventAction.do?operation=insertDEData">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		<tr>
			<td class="tablepadding">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_tab_bg" >
							<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1">
						</td>
						<td valign="bottom">
							<a href="#" onclick="editSCG()"><img src="images/uIEnhancementImages/tab_edit_collection2.gif" alt="SCG Details" width="216" height="22" border="0"></a>
						</td>
						<td valign="bottom">
							<img src="images/uIEnhancementImages/tab_spp1.gif" alt="SPP" width="42" height="22" border="0">
						</td>
						<td valign="bottom">
							<a href="#" onClick="viewSPR()"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="Inactive View Surgical Pathology Report " width="216" height="22" border="0"></a>
						</td>
						<td valign="bottom">
							<a href="#" onClick="showAnnotations()"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0"></a>
						</td>
						<td align="left" valign="bottom" class="td_color_bfdcf3" >
							<a href="#" onClick="goToConsentPage()" id="consentTab"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0"></a>
						</td>
						<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
					</tr>
				</table>
				<%@ include file="/pages/content/manageBioSpecimen/SPPEventsFromDashboard.jsp" %>
			</td>
		</tr>
	</table>
</html:form>