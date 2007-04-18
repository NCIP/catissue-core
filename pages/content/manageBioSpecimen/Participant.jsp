<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ParticipantForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>

<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<script src="jss/script.js"></script>
<!-- Mandar 11-Aug-06 : For calendar changes --> 
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<!-- Mandar 11-Aug-06 : calendar changes end -->

<style>
.active-column-0 {width:30px}
tr#hiddenCombo
{
 display:none;
}
</style>


<script src="jss/script.js" type="text/javascript"></script>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<% 
		String parentUrl = null;
		String cpId = null;
		List siteList = (List)request.getAttribute(Constants.SITELIST);

		String participantId=(String)request.getAttribute("participantId");

		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);		
		String forwardTo=(String)request.getAttribute(Constants.FORWARD_TO);		
		boolean isRegisterButton = false;
		boolean isAddNew = false;
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName, pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		
		String staticEntityName=null;
		staticEntityName = AnnotationConstants.ENTITY_NAME_PARTICIPANT;
		
		Long participantEntityId = null;
				if (CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId") != null)
		{
			participantEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId");
		}
		else
		{
			participantEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
			CatissueCoreCacheManager.getInstance().addObjectToCache("participantEntityId",participantEntityId);		
		}
		
		String id = request.getParameter("id");

		 pageView=operation;
		if(operation.equals(Constants.EDIT))
		{
			
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.PARTICIPANT_EDIT_ACTION;
			readOnlyValue=true;
			if(pageOf.equals(Constants.QUERY))
				formName = Constants.QUERY_PARTICIPANT_EDIT_ACTION + "?pageOf="+pageOf;
			if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{
				formName = Constants.CP_QUERY_PARTICIPANT_EDIT_ACTION + "?pageOf="+pageOf;
			}
		}
		else
		{
			formName = Constants.PARTICIPANT_LOOKUP_ACTION;
			if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{
				formName = Constants.CP_QUERY_PARTICIPANT_LOOKUP_ACTION;
			}

			readOnlyValue=false;
		}


		Object obj = request.getAttribute("participantForm");
		int noOfRows=0;
		Map map = null;
		String currentBirthDate = "";
		String currentDeathDate = "";
		if(obj != null && obj instanceof ParticipantForm)
		{
			
			ParticipantForm form = (ParticipantForm)obj;
			map = form.getValues();
			noOfRows = form.getCounter();
			currentBirthDate = form.getBirthDate(); 
			currentDeathDate = form.getDeathDate(); 
		}
		ViewSurgicalPathologyReportForm formSPR=null;
		if(operation.equals("viewSPR"))
		{
		
			
			obj = request.getAttribute("viewSurgicalPathologyReportForm");
	   		
			if(obj != null && obj instanceof ViewSurgicalPathologyReportForm)
			{
				
				formName=Constants.SPR_VIEW_ACTION;
				formSPR=(ViewSurgicalPathologyReportForm)obj;
				map = formSPR.getValues();
				noOfRows = formSPR.getCounter();
			}
		}
%>

<head>
	<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
	{
	strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
	}%>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	
	<%
	String participantIdentifier="0";
	List columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);

		
	String title = "ParticipantList";

	boolean isSpecimenData = false;

	int IDCount = 0;
	%>
	
	<script language="JavaScript">
		//function to insert a row in the inner block
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
			var identifier = "value(ParticipantMedicalIdentifier:" + (q+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML="" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_Site_id)";
// Mandar : 434 : for tooltip 
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%
				if(siteList!=null)
				{
					Iterator iterator = siteList.iterator();
					while(iterator.hasNext())
					{
						NameValueBean bean = (NameValueBean)iterator.next();
			%>
						sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%		}
				}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue(ParticipantMedicalIdentifier:1_medicalRecordNumber)
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			sname="";
		
			name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_medicalRecordNumber)";
			sname= "";
			sname="<input type='text' name='" + name + "' size='30' maxlength='50'  class='formFieldSized15' id='" + name + "'>";
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
		
		function textLimit(field) 
		{
			if(field.value.length>0) 
				field.value = field.value.replace(/[^\d]+/g, '');
				
			/*if (element.value.length > maxlen + 1)
				alert('your input has been truncated!');*/
			/*if (field.value.length > maxlen)
			{
				//field.value = field.value.substring(0, maxlen);
				field.value = field.value.replace(/[^\d]+/g, '');
			}*/
		}
		function intOnly(field) 
		{
			if(field.value.length>0) 
			{
				field.value = field.value.replace(/[^\d]+/g, ''); 
			}
		}
		//this function is called when participant clicks on radiao button 
		function onParticipantClick(participant_id)
		{
			//mandar for grid
			var cl = mygrid.cells(participant_id,mygrid.getColumnCount()-1);
			var pid = cl.getValue();
			//alert(pid);
			//participant_id = pid;
			//------------
			//document.forms[0].participantId.value=participant_id;
			document.forms[0].participantId.value=pid;
			document.forms[0].id.value=pid;
			document.forms[0].submitPage.disabled=true;
			document.forms[0].registratioPage.disabled=false;
		
		
		}
		//This Function is called when user clicks on 'Add New Participant' Button
		function AddParticipant()
		{
			document.forms[0].action="<%=Constants.PARTICIPANT_ADD_ACTION%>";
			<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{%>
			document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_ADD_ACTION%>";
			<%}%>
			document.forms[0].submit();
		}
		//This function is called when user clicks on 'Use Selected Participant' Button
		function UseSelectedParticipant()
		{
		
			if(document.forms[0].participantId.value=="" || document.forms[0].participantId.value=="0")
			{
				alert("Please select the Participant from the list");
			}
			else
			{
			
				document.forms[0].action="ParticipantSelect.do?operation=add&id="+document.forms[0].participantId.value;
				alert(document.forms[0].action);
				document.forms[0].submit();
				//window.location.href="ParticipantSelect.do?operation=add&participantId="+document.forms[0].participantId.value+"&submittedFor="+document.forms[0].submittedFor.value+"&forwardTo="+document.forms[0].forwardTo.value;
			}
			
		}
		
		function CreateNewClick()
		{
			document.forms[0].submitPage.disabled=false;
			document.forms[0].registratioPage.disabled=false;
			<%if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){%>
				document.forms[0].submitPage.disabled=true;
			<%}%>
			
			document.forms[0].radioValue.value="Add";
			
			document.forms[0].action="<%=Constants.PARTICIPANT_ADD_ACTION%>";
			<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{%>
			document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_ADD_ACTION%>";
			<%}%>
			
			
		}
	
		function LookupAgain()
		{
			
			document.forms[0].submitPage.disabled=false;
			document.forms[0].registratioPage.disabled=true;
			<%if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){%>
				document.forms[0].submitPage.disabled=true;
				document.forms[0].registratioPage.disabled=false;
			<%}%>
			document.forms[0].radioValue.value="Lookup";
		}
		
		function setSubmittedForParticipant(submittedFor,forwardTo)
		{

			document.forms[0].submittedFor.value = submittedFor;
			document.forms[0].forwardTo.value    = forwardTo;
			<%if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){%>
				document.forms[0].submittedFor.value = "AddNew";
			<%}%>			
			<%if(request.getAttribute(Constants.SPREADSHEET_DATA_LIST)!=null && dataList.size()>0){%>	

				if(document.forms[0].radioValue.value=="Add")
				{
					document.forms[0].action="<%=Constants.PARTICIPANT_ADD_ACTION%>";
					<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
					{%>
					document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_ADD_ACTION%>";
					<%}%>
				}
				else
				{
					if(document.forms[0].radioValue.value=="Lookup")
					{
						document.forms[0].action="<%=Constants.PARTICIPANT_LOOKUP_ACTION%>";
						<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
						{%>
						document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_LOOKUP_ACTION%>";
						<%}%>												
						document.forms[0].submit();
					}
				}		
			<%}%>	
			
	if((document.forms[0].activityStatus != undefined) && (document.forms[0].activityStatus.value == "Disabled"))
   	{
	    var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
		if (go==true)
		{
			document.forms[0].submit();
		}
	} 
	else
	{
			document.forms[0].submit();		
	}
}
		
		function onVitalStatusRadioButtonClick(element)
		{
		
			if(element.value == "Dead")
			{
				document.forms[0].deathDate.disabled = false;				
			}
			else
			{
				document.forms[0].deathDate.disabled = true;
			}
		}
		
		//View SPR Vijay pande
        function viewSPR()
        {
			var tempId=document.forms[0].id.value;
        	var action="<%=Constants.SPR_VIEW_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&id="+tempId;
			document.forms[0].action=action;
			document.forms[0].submit();
        }
		function editParticipant()
		{
			var tempId=document.forms[0].id.value;
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
		function showAnnotations()
		{
			var action="DisplayAnnotationDataEntryPage.do?entityId=<%=participantEntityId%>&entityRecordId=<%=id%>&staticEntityName=<%=staticEntityName%>&pageOf=pageOfParticipant&operation=viewAnnotations";
			document.forms[0].action=action;
			document.forms[0].submit();
		}
		//View SPR Vijay pande


	</script>
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
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
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItemSelected">Edit</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
								
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showAnnotations()">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
					<%@ include file="EditParticipant.jsp" %>
				</td>
			</tr>
		</table>
	<%
	}
	%>
	<%
	if(pageView.equals("viewSPR"))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="editParticipant()">Edit</td>

				<td height="20" class="tabMenuItemSelected" onmouseover="" onmouseout="" onClick="">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
								
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showAnnotations()">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
				<%@ include file="ViewSurgicalPathologyReport.jsp" %>	
				
				</td>
			</tr>
		</table>
	<%
	}
	%>
<%
	if(pageView.equals("viewAnnotations"))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="400" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="document.forms[0].target = '_top';editParticipant()">Edit</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="document.forms[0].target = '_top';viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
								
				
				<td height="20" class="tabMenuItemSelected"  onClick="">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr width = "100%" height = "100%">
				<td class="tabField" colspan="6"  width = "100%" height = "100%">
				<%@ include file="DisplayAnnotationDataEntryPage.jsp" %>
				
			
				</td>
			</tr>
		</table>
	<%
	}
	%>

	<%-- this is done at the end beacuse we want to set CpId value --%>
	<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
	{%>
	<script language="javascript">
			var cpId = window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].document.getElementById("cpId").value;
			document.getElementById("cpId").value=cpId;
			var participantId = window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].document.getElementById("participantId").value;
			window.parent.frames['<%=Constants.CP_AND_PARTICIPANT_VIEW%>'].location="showCpAndParticipants.do?cpId="+cpId+"&participantId="+participantId;
	</script>

	<%}%>
	
	
	 </html:form>