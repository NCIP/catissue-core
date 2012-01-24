<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="org.apache.struts.Globals, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionErrors"%>
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
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
String typeObject = (String)request.getAttribute("typeObject");
if(specimenIdentifier == null || specimenIdentifier.equals("0"))
	specimenIdentifier = (String)request.getParameter(Constants.SPECIMEN_ID);

if(specimenIdentifier != null && !specimenIdentifier.equals("0"))
	session.setAttribute(Constants.SPECIMEN_ID,specimenIdentifier);

if(specimenIdentifier == null || specimenIdentifier.equals("0"))
{
	specimenIdentifier= (String)session.getAttribute(Constants.SPECIMEN_ID);
}

String specimenPath ="'NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
String consentTab="'NewSpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimen&id="+specimenIdentifier+"'" ;
if(pageOf != null && pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
{
	specimenPath ="'QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
	consentTab = "'QuerySpecimenSearch.do?operation=search&tab=consent&pageOf=pageOfNewSpecimenCPQuery&id="+specimenIdentifier+"'" ;
}
String staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
Long specimenEntityId = 1943L;//(Long)request.getAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);

String formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
{
	formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
}
formNameAction = formNameAction+"&specimenId="+specimenIdentifier+"&menuSelected=15";
request.setAttribute("showSPPHeader","true");
%>

<script language="JavaScript">
	function showConsents()
	{
		addNewAction(<%= consentTab %>);
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
		var specimenId = "<%=specimenIdentifier%>";
		var action="SaveSPPEventAction.do?pageOf=<%=pageOf%>&operation=insertDEData&specimenId="+specimenId+"&typeObject=Specimen";

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
	}

	function onEventClicked()
	{
		//var consentTier=document.forms[0].consentTierCounter.value;
		var formName = "<%=formNameAction%>&consentTierCounter="+0;
		confirmDisable(formName,'Active');
	}

	if ( document.getElementById && !(document.all) )
	{
		var slope=-37;
	}
	else
	{
		var slope=-42;
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
							<a href="#" onclick="addNewAction(<%= specimenPath %>);"><img src="images/uIEnhancementImages/tab_specimen_details2.gif" alt="Specimen Details" width="126" height="22" border="0"></a>
						</td>
						<td valign="bottom">
							<a href="#" onclick="onEventClicked();"><img src="images/uIEnhancementImages/tab_events2.gif" border="0" alt="Events" width="56" height="22"></a>
						</td>
						<td valign="bottom">
							<img src="images/uIEnhancementImages/tab_spp1.gif" alt="SPP" width="42" height="22" border="0">
						</td>
						<td valign="bottom">
							<a href="#" onClick="viewSPR()"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="Inactive View Surgical Pathology Report " width="216" height="22" border="0"></a>
						</td>
						<td valign="bottom">
							<a href="#" onClick="viewAnnotations(<%=specimenEntityId%>,<%=specimenIdentifier%>,'','<%=staticEntityName%>','<%=pageOf%>');"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0"></a>
						</td>
						<td align="left" valign="bottom" class="td_color_bfdcf3" >
							<a href="#" onClick="showConsents();" id="consentTab"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0"></a>
						</td>
						<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
					</tr>
				</table>
			<%@ include file="/pages/content/manageBioSpecimen/SPPEventsFromDashboard.jsp" %>
			</td>
		</tr>
	</table>
</html:form>