<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		String selectProperty="";
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		boolean isAddNew = false;	
		String signedConsentDate = "";
		Long scgEntityId = null;
		String staticEntityName=null;
		staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP;
		
				if (CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId") != null)
		{
			scgEntityId = (Long)CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId");
		}
		else
		{
			scgEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
			CatissueCoreCacheManager.getInstance().addObjectToCache("scgEntityId",scgEntityId);		
		}

		String id = request.getParameter("id");
		String appendingPath = "/SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup";
		if (reqPath != null)
			appendingPath = reqPath + "|/SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup";
	
	   		Object obj = request.getAttribute("specimenCollectionGroupForm");
			SpecimenCollectionGroupForm form =null;
	
			if(obj != null && obj instanceof SpecimenCollectionGroupForm)
			{
				form = (SpecimenCollectionGroupForm)obj;
			}	
	
	   	if(!operation.equals("add") )
	   	{
	   		obj = request.getAttribute("specimenCollectionGroupForm");
	   		
			if(obj != null && obj instanceof SpecimenCollectionGroupForm)
			{
				form = (SpecimenCollectionGroupForm)obj;
		   		appendingPath = "/SpecimenCollectionGroupSearch.do?operation=search&pageOf=pageOfSpecimenCollectionGroup&id="+form.getId() ;
		   		int checkedButton1 = form.getCheckedButton();
		   	}
	   	}
			
		String formName="", pageView = operation ,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;
		ViewSurgicalPathologyReportForm formSPR=null;
		if(operation.equals(Constants.EDIT) || operation.equals("viewAnnotations"))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION;
			readOnlyValue=true;
			if(pageOf.equals(Constants.QUERY))
				formName = Constants.QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;

		}
		if(operation.equals(Constants.ADD))
		{
			formName = Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION;
			readOnlyValue=false;
		}
		Map map = null;
		int noOfRows=0;
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
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
     <script language="JavaScript">
     		function showAnnotations()
		{
			var action="DisplayAnnotationDataEntryPage.do?entityId=<%=scgEntityId%>&entityRecordId=<%=id%>&staticEntityName=<%=staticEntityName%>&pageOf=pageOfSpecimenCollectionGroup&operation=viewAnnotations";
			document.forms[0].action=action;
			document.forms[0].submit();
		}


		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].participantId.disabled = false;
				document.forms[0].protocolParticipantIdentifier.disabled = true;
				document.forms[0].participantsMedicalIdentifierId.disabled = false;
			}
			else
			{
				document.forms[0].participantId.disabled = true;
				document.forms[0].protocolParticipantIdentifier.disabled = false;

				
				//disable Medical Record number field.
				document.forms[0].participantsMedicalIdentifierId.disabled = true;
			}
		} 
		
		//Consent Tracking Module (Virender Mehta)		
		function onChangeEvent(element)
		{
			var getCPID=document.getElementById('collectionProtocolId');
			var cpID=getCPID.value;
        	var getID=document.getElementById(element);
		    var index=getID.selectedIndex;			    
			if(index<0)
			{
				alert("Please Select Valid Value");
			}
	else
			{       	
	        	if(element=='collectionProtocolEventId')
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
		function changeAction(action)
        {
			document.forms[0].action = action;
			document.forms[0].submit();
        }
		
		//Consent Tracking Module Virender mehta
		
		//View SPR Vijay pande
        function viewSPR()
        {
			var tempId=document.forms[0].id.value;
        	var action="<%=Constants.SPR_VIEW_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&id="+tempId;
			document.forms[0].action=action;
			document.forms[0].submit();
        }
		function editSCG()
		{
			var tempId=document.forms[0].id.value;
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId;
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

<html:form action="<%=formName%>" >
	<%
	if(pageView.equals("add"))
	{
	%>
		<%@ include file="EditSpecimenCollectionGroup.jsp" %>
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
				<td class="tabField" colspan="6" >
					<%@ include file="EditSpecimenCollectionGroup.jsp" %>
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
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="editSCG()">Edit</td>

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
					<%@   include file="ViewSurgicalPathologyReport.jsp" %>
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
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="450" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="document.forms[0].target = '_top'; editSCG()">Edit</td>

				<td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()"  onClick="document.forms[0].target = '_top'; viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
								
				
				<td height="20" class="tabMenuItemSelected"  onClick="">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr width = "100%" height = "100%">
				<td class="tabField" colspan="6"  width = "100%" height = "100%">
					<%@   include file="DisplayAnnotationDataEntryPage.jsp" %>
				</td>
			</tr>
		</table>
	<%
	}
	%>
		
</html:form>