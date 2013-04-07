<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%@ page import="edu.common.dynamicextensions.xmi.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<style>
.active-column-1 {width:200px}
</style>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/fileUploader.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/specimenCollectionGroup.js"></script>
<script>
function showSCGAnnotations(){
	var action="DisplayAnnotationDataEntryPage.do?entityId=${entityId}&entityRecordId=${consentLevelId}&staticEntityName=${staticEntityName}&pageOf=pageOfSpecimenCollectionGroupCPQuery&operation=viewAnnotations&id=${consentLevelId}";
			document.location=action;
			
}
function editSCG()
		{
			var tempId='${consentLevelId}';
			action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=search&id="+tempId;
			
			document.location=action;
			
		}
function viewParticipantSPR(reportId1,pageOf)
		{
			var reportId=reportId1;
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
		    	var action="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf="+pageOf+"&reportId="+reportId+"&cpId=${cpId}&cprId=${consentLevelId}";
				document.location=action;
				
			}
		}
		
	function editParticipant()
		{
			//bug 7530 .Report id becomes the participant id thats why extra field PARTICIPANTIDFORREPORT added in the report.
			var tempId='${participantId}';
			if(tempId==null)
			{
				tempId=document.forms[0].id.value;
			}
			var action="SearchObject.do?pageOf=${pageof}&operation=search&id="+tempId;
//if('${pageof}'=='pageOfParticipantCPQueryEdit')
			{
			
	//		alert("Hi");
				action="QueryParticipantSearch.do?pageOf=pageOfParticipantCPQueryEdit&operation=search&id="+tempId+"&cpSearchCpId=${cpId}";
			}
			document.location=action;
			
		}
	
				
		function showParticipantAnnotations()
		{
			var fwdPage="${pageof}";
			var cpId = '${cpId}';
			var cprId = '${consentLevelId}';
			var action="DisplayAnnotationDataEntryPage.do?entityId=${participantEntityId}&entityRecordId=${participantId}&id=${participantId}&staticEntityName=${staticEntityName}&pageOf="+fwdPage+"&operation=viewAnnotations&cpId="+cpId+"&cprId=${consentLevelId}";
			document.location=action;
		}
	
	function viewSpecimenSPR(reportIdValue,pageOf,specimenId)
	{
		var reportId=reportIdValue;
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
			var action="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf="+pageOf+"&reportId="+reportId+"&id="+specimenId;
			document.location=action;
		
			//document.forms[0].submit();
		}
	}

	function viewSpecimen(){
		action = "QuerySpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimenCPQuery&id=${consentLevelId}" ;
		document.location=action;
	}
	
	function viewSpecimenAnnotation(){

		var action="DisplayAnnotationDataEntryPage.do?entityId=${entityId}&entityRecordId=${consentLevelId}&pageOf=pageOfNewSpecimenCPQuery&operation=viewAnnotations&staticEntityName=${staticEntityName}&id=${consentLevelId}";
		document.location=action;
	
	}
	
	function showEvent()
{
		var id = '${consentLevelId}';

		var formName = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery&specimenId="+id+"&menuSelected=15";
		document.location=formName;
		
}
	
		
</script>
<logic:equal name="consentLevel" value="scg">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="maintable">
			<tr>
				<td class="tablepadding">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
							<td valign="bottom" ><a href="#" onclick="editSCG()"><img src="images/uIEnhancementImages/tab_edit_collection2.gif" border="0" alt="Edit SCG" width="216" height="22" border="0" vspace="0" hspace="0"></a></td>
							<td valign="bottom"><a href="#" onClick="viewSCGSPR('${identifiedReportId}','pageOfSpecimenCollectionGroupCPQuery','${consentLevelId}')" id="viewSPR"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="Inactive View Surgical Pathology Report " width="216" height="22"  border="0"></a></td>
							<td valign="bottom"><a href="#" onClick="showSCGAnnotations()" id="showAnnotation"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22"  border="0"></a></td>
							<td align="left" valign="bottom" class="td_color_bfdcf3" ><a id="consentViewTab" href="#"><img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'></a></td>
							<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
						</tr>
					</table>
					<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
				</td>
			</tr>
		</table>
</logic:equal> 
<logic:equal name="consentLevel" value="participant">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="maintable">
			<tr>
				<td class="tablepadding">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="td_tab_bg"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
							<td valign="bottom" align="left" ><a href="#" onClick="editParticipant()"><img src="images/uIEnhancementImages/tab_edit_participant1.gif" border="0" vspace="0" hspace="0" alt="Edit Participant" width="116" height="22" border="0"></a></td>
							<td valign="bottom"><a href="#" onClick="viewParticipantSPR('${reportId}','${pageof}')" id="viewSPR"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0"></a></td>
							<td valign="bottom" ><html:link href="#" onclick="showParticipantAnnotations()" styleId="showAnnotation"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22"  border="0"></html:link></td>
							<td align="left" valign="bottom" class="td_color_bfdcf3" ><a id="consentViewTab" href="#"><img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'></a></td>
							<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
						</tr>
					</table>
					<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
				</td>
			</tr>
		</table>
</logic:equal> 
<logic:equal name="consentLevel" value="specimen">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">

	
	  
	<tr>
		<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
					  <tr>
						<td class="td_tab_bg" >
							<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
								<td valign="bottom">
									<a onclick="viewSpecimen()" id="specimenDetailsTab" href="#">	
										<img src='images/uIEnhancementImages/tab_specimen_details2.gif' alt='Specimen Details'  width='115' height='22' border='0'>
									</a>
								</td>
							<td valign="bottom">
									<a href="#"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="showEvent('');" border="0"></a>
							</td>
							<td valign="bottom">
								<a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSpecimenSPR('${reportId}','${pageof}','${consentLevelId}');"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewSpecimenAnnotation()"></a>
							</td>
							<td align="left" valign="bottom" class="td_color_bfdcf3" >
								<a id="consentViewTab" href="#"><img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'></a>
							</td>
							<td align="left" valign="bottom" class="td_color_bfdcf3" >
							<a id="imageViewTab" href="#" onClick="newImageTab('${consentLevelId}')"><img src="images/uIEnhancementImages/tab_image2.gif" alt="Images" width="126" border="0" height="22" >
							<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
							</td>
						</tr>
		    </table>
		
			<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
			
		</td>
	  </tr>
	   </table>
	</logic:equal>