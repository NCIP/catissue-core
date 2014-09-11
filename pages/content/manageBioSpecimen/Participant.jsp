<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ParticipantForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ page language="java" isELIgnored="false"%>


<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>

<script>
      window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
</script>
<script src="jss/ajax.js"></script> 
<script src="jss/script.js"></script>
<!-- Mandar 11-Aug-06 : For calendar changes --> 

<script src="jss/calendarComponent.js"></script>
<script src="jss/titli.js"></script>
<script src="jss/wz_tooltip.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>

<script src="jss/participant.js"></script>

<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css"/>

<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar_start.js"></script>

<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/ext/dhtmlxcombo_whp.js"></script>


<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
%>

<script src="jss/script.js" type="text/javascript"></script>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%
		String parentUrl = null;
		String cpId = null;
		List siteList = (List)request.getAttribute(Constants.SITELIST);
		List collectionProtocolList = (List)request.getAttribute(Constants.PROTOCOL_LIST);

		// participantId used to pass to flex method to show currenr particpant as selected
		String participantId=(String)request.getAttribute("participantId");

		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		String forwardTo=(String)request.getAttribute(Constants.FORWARD_TO);
		boolean isRegisterButton = false;
		boolean isAddNew = false;
		boolean isSpecimenRegistration = true;
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName, pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;
		//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
		String staticEntityName="Participant";
		//Falguni:Performance Enhancement.
		Long participantEntityId = null;
		participantEntityId = (Long)request.getAttribute("particiapntRecordEntryEntityId");

		String id = request.getParameter("id");

		 pageView=operation;
		if(Constants.EDIT.equals(operation))
		{
	editViewButton="buttons."+Constants.VIEW;
	formName = Constants.PARTICIPANT_EDIT_ACTION;
	readOnlyValue=true;
	if(Constants.QUERY.equals(pageOf))
		formName = Constants.QUERY_PARTICIPANT_EDIT_ACTION + "?pageOf="+pageOf;
	if(Constants.PAGE_OF_PARTICIPANT_CP_QUERY.equals(pageOf))
	{
		formName = Constants.CP_QUERY_PARTICIPANT_EDIT_ACTION + "?pageOf="+pageOf;
	}
		}
		else
		{
	formName = Constants.PARTICIPANT_LOOKUP_ACTION;
	if(Constants.PAGE_OF_PARTICIPANT_CP_QUERY.equals(pageOf))
	{
		formName = Constants.CP_QUERY_PARTICIPANT_LOOKUP_ACTION;
	}

	readOnlyValue=false;
		}

		Object obj = request.getAttribute("participantForm");
		int noOfRows=0;
		int noOrRowsCollectionProtocolRegistration = 0;
		Map map = null;
		Map mapCollectionProtocolRegistration = null;
		String currentBirthDate = "";
		String currentDeathDate = "";
		int regID = 0;
		if(obj != null && obj instanceof ParticipantForm)
		{
	ParticipantForm form = (ParticipantForm)obj;

	map = form.getValues();
	mapCollectionProtocolRegistration = form.getCollectionProtocolRegistrationValues();
	noOfRows = form.getValueCounter();
	noOrRowsCollectionProtocolRegistration = form.getCollectionProtocolRegistrationValueCounter();
	currentBirthDate = form.getBirthDate();
	currentDeathDate = form.getDeathDate();
		}
		else
		{
	Object obj1 = request.getAttribute("viewSurgicalPathologyReportForm");
	if(obj1 != null && obj1 instanceof ViewSurgicalPathologyReportForm)
	{
		ViewSurgicalPathologyReportForm viewSPRForm = (ViewSurgicalPathologyReportForm)obj1;
		participantId = String.valueOf(viewSPRForm.getParticipantIdForReport());
	}
		}

		if(noOfRows == 0)
		{
	noOfRows =1;
		}

		if(operation.equals(Constants.ADD))
		{
	if(noOrRowsCollectionProtocolRegistration == 0)
	{
		noOrRowsCollectionProtocolRegistration =1;
	}
		}
		
		boolean isGenerateEMPIDisabled=false;
		boolean isSubmitDisabled=false;
		String generateeMPIButtonName="";
		generateeMPIButtonName=org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute(Constants.GENERATE_EMPI_ID_NAME));
		String csEMPIStatus = org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute("csEMPIStatus"));
		final String isMatchedFromEMPI=org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute(edu.wustl.common.participant.utility.Constants.MATCHED_PARTICIPANTS_FOUND_FROM_EMPI));
		String empiGenerationFieldsInsufficient=org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute(edu.wustl.common.participant.utility.Constants.EMPI_GENERATION_FIELDS_INSUFFICIENT));
		String isEnableLinkedParticipantButton=org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute("isEnableLinkedParticipantButton"));
		String catissueMatchedParticipantFound=org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute("CaTissueMatchedParticpant"));
		
		//amol changes
		String fromProcessMessage = (String) request
		.getAttribute(edu.wustl.common.participant.utility.Constants.IS_GENERATE_EMPI_PAGE);
		if(fromProcessMessage == null){
			request
		.setAttribute(edu.wustl.common.participant.utility.Constants.IS_GENERATE_EMPI_PAGE,"false");
		}
		
		//ends
		String normalSubmitForEMPIGenerate = "setSubmittedForParticipanteMPIGenerate('"
				+ submittedFor + "','" + Constants.PARTICIPANT_FORWARD_TO_LIST[0][1] + "')";
				
		Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID);		
		

		if (pageView.equals("edit") && "true".equals(XMLPropertyHandler.getValue(Constants.EMPI_ENABLED)))
		{
%>
		<logic:equal name="participantForm" property="empiIdStatus"
			value="<%=edu.wustl.common.participant.utility.Constants.EMPI_ID_PENDING%>">
			<%
				isGenerateEMPIDisabled = true;
			isSubmitDisabled=true;
							generateeMPIButtonName = edu.wustl.common.participant.utility.Constants.GENERATE_EMPI_ID;
			%>

		</logic:equal>
		<logic:equal name="participantForm" property="empiIdStatus"
			value="<%=edu.wustl.common.participant.utility.Constants.EMPI_ID_CREATED%>">
			<%
				generateeMPIButtonName = edu.wustl.common.participant.utility.Constants.REGENERATE_EMPI_ID;
			%>
		</logic:equal>
	<%} 
%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript" >
		//Set last refresh time
        var isPHIVIEW =   ${participantForm.pHIView};
		var disablePHIView = !isPHIVIEW;
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}
	</script>
	<%
			String participantIdentifier="0";
			List columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
			List dataList = (List) request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST);


			String title = "ParticipantList";

			boolean isSpecimenData = false;

			int IDCount = 0;
	%>

	<script language="JavaScript">
	
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].valueCounter.value);
			val = val + 1;
			document.forms[0].valueCounter.value = val;

			var r = new Array();
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(q);

			var checkb=x.insertCell(0);
			checkb.className="black_ar";
			checkb.colSpan=1;
			sname="";

			var identifier = "value(ParticipantMedicalIdentifier:" + (q+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";

			var name = "chk_"+(q+1);
			sname = sname +"<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteMedicalIdentifierValue,document.forms[0].valueCounter,'chk_')\">";
			checkb.innerHTML=""+sname;
			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="black_ar_s";
			sname="";
			var identifier = "value(ParticipantMedicalIdentifier:" + (q+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			var name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_Site_id)";
			sname = sname +"<select name='" + name + "' size='1' class='formFieldSized12' id='" + name + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%if(siteList!=null)
				{
					Iterator iterator = siteList.iterator();
					while(iterator.hasNext())
					{
						NameValueBean bean = (NameValueBean)iterator.next();%>
						sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%}
				}%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;

			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="black_ar";
			spreqsubtype.colSpan=1;
			sname="";

			name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_medicalRecordNumber)";
			sname= "";
			sname="<input type='text' name='" + name + "' maxlength='50' size='15' class='black_ar' id='" + name + "'>";
			spreqsubtype.innerHTML="" + sname;
		}

		//this function is called when participant clicks on radiao button
		function onParticipantClick(participant_id)
		{
			//mandar for grid
			var cl = mygrid.cells(participant_id,mygrid.getColumnCount()-1);
			var pid = cl.getValue();
			
			var clicked_Row = participant_id;
			if(document.forms[0].generateeMPIIdforPartiId.value == ""){
				document.forms[0].generateeMPIIdforPartiId.value = document.forms[0].participantId.value;
			}
			if(document.forms[0].clinPortalPartiId.value==""){
				document.forms[0].clinPortalPartiId.value = document.forms[0].participantId.value;
			}
			document.forms[0].clickedRowSelected.value=clicked_Row;
			
			document.forms[0].participantId.value=pid;
			document.forms[0].id.value=pid;
			document.forms[0].forwardTo.value="pageOfParticipant";
			document.forms[0].action="ParticipantRegistrationSelect.do?operation=edit&pageOf=pageOfParticipant";
			document.forms[0].isGenerateEMPIID.value="YES";
			<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{%>
					document.forms[0].forwardTo.value="pageOfParticipantCPQuery";
					document.forms[0].action="CPQueryParticipantRegistrationSelect.do?operation=edit&pageOf=pageOfParticipantCPQuery";
			<%}%>

			document.forms[0].radioValue.value="";
			for (var i = 0 ; i < document.forms[0].chkName.length; i++)
			{
				document.forms[0].chkName[i].checked = false;
			}
		}



		function GenerateEMPIID()
		{
			document.forms[0].radioValue.value="GenerateHL7Mes";
			document.forms[0].action="<%=edu.wustl.common.participant.utility.Constants.PARTICIPANT_EMPI_GENERATION_ACTION%>";
			document.forms[0].isGenerateHL7.value="yes";
			<%if(Constants.PAGE_OF_PARTICIPANT_CP_QUERY.equals(pageOf))
			{%>
				document.forms[0].action="<%=edu.wustl.common.participant.utility.Constants.CP_QUERY_PARTICIPANT_EMPI_GENERATION_ACTION%>";
			<%}%>
                document.forms[0].registratioPage.disabled=false;
		}
		
		function consentTab(){
			var cprId = '${cprId}';
			var cpId = '${cpId}';
			var action="FetchConsents.do?consentLevelId="+cprId+"&consentLevel=participant&reportId=<%=reportId%>&pageof=<%=pageOf%>&participantEntityId=${particiapntRecordEntryEntityId}&participantId=<%=participantId%>&cpId="+cpId+"&cprId="+cprId;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
	</script>
</head>
<body onload="loadParticipantTabbar('${requestScope.operation}');initializeCombo()"></body>

<html:form action="<%=formName%>" style="height:100%,width=100%">
<html:hidden property="cpId" />
	<%
		if(pageView.equals("add"))
		{
	%>
		<%@ include file="EditParticipant.jsp" %>
	<%
				}
			%>
	<%
		if(pageView.equals("edit"))
		{
	%>
		<%@ include file="EditParticipant.jsp" %>
	<%
	}
	%>
	<%
	if(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT.equals(pageView))
	{
	
	%>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable" style="height:100%">
		<tr height="100%">
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:95%">
					<tr height="100%">
						<td>
						<%@include file="ViewSurgicalPathologyReport.jsp" %>	
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<%
	}
	%>
		<%
	if(pageView.equals("viewAnnotations"))
	{
		%><!-- Mandar : 24Nov08 -->
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <!--tr height="1%">
		    <td class="td_color_bfdcf3"></td>
		  </tr-->
		  <tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td colspan="6">
					<jsp:include page="DisplayAnnotationDataEntryPage.jsp" />
				</td>
				</tr>
				</table>
			</td>
	   </tr>
	  </table>
            
		<%
	}
	%>

	  <%-- this is done at the end beacuse we want to set CpId value --%>
	<%

	String refreshTree = (String)request.getAttribute("refresh");
   System.out.println("refreshTree: "+refreshTree);
	if(refreshTree==null || refreshTree.equalsIgnoreCase("true"))
	{
	%>

	<script language="javascript">
	
	 if(top.frames["cpAndParticipantView"] != undefined)
	 {
		top.frames["cpAndParticipantView"].refreshCpParticipants(<%=participantId%>);
	 }

	</script>

	<%}
	%>

	</html:form>
<script>
function updateHelpURL()
{
	var URL="";
	
	var activeTab = participantTabbar.getActiveTab();
	if("pageOfParticipantCPQuery"=="<%=pageOf%>")
	{
		if("annotationTab"==activeTab)
		{
			URL="<%=HelpXMLPropertyHandler.getValue("FormDataEntry")%>";
		}
		else
		{
			URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.ParticipantForm")%>";
		}
	}
	return URL;
}

if(disablePHIView)
{
	disableTabs();
}
</script>
