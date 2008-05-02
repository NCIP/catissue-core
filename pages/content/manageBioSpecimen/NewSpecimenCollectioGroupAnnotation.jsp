<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="java.util.*"%>

<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>


<script src="jss/script.js" type="text/javascript"></script>
<!-- Bug Id: 4159
	 Patch ID: 4159_1			
	 Description: Including calenderComponent.js to show date in events
-->
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/calendarComponent.js" type="text/javascript"></script>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		if(pageOf == null)
			pageOf =(String)request.getParameter(Constants.PAGEOF);
		String signedConsentDate = "";
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		boolean isAddNew = false;	
		String formName, pageView = operation ,editViewButton="buttons."+Constants.EDIT;

		String id = request.getParameter("id");
		String appendingPath = "/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
		if (reqPath != null)
			appendingPath = reqPath + "|/SpecimenCollectionGroup.do?operation=add&pageOf="+pageOf;
	
	   		Object obj = request.getAttribute("specimenCollectionGroupForm");
			SpecimenCollectionGroupForm form =null;
	
			if(obj != null && obj instanceof SpecimenCollectionGroupForm)
			{
				form = (SpecimenCollectionGroupForm)obj;
			}	
			
			
			if(operation.equals(Constants.EDIT)|| operation.equals("viewAnnotations"))
			{
				//editViewButton="buttons."+Constants.VIEW;
				formName = Constants.SPECIMEN_COLLECTION_GROUP_EDIT_ACTION;
			//	readOnlyValue=true;
				if(pageOf.equals(Constants.QUERY))
					formName = Constants.QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
				if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
				{
					formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION + "?pageOf="+pageOf;
				}
			}
			else
			{
				formName = Constants.SPECIMEN_COLLECTION_GROUP_ADD_ACTION;
				if(pageOf.equals(Constants.PAGE_OF_SCG_CP_QUERY))
				{
					formName = Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION + "?pageOf="+pageOf;
				}
			//	readOnlyValue=false;
			}			
					
%>
<script>
		//View SPR Vijay pande
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
		    	var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&id="+reportId;
				document.forms[0].action=action;
				document.forms[0].submit();
			}
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
		
		function setTarget()
		{
			var fwdPage="<%=pageOf%>";
			if(!fwdPage=="pageOfSpecimenCollectionGroupCPQuery")
				document.forms[0].target = '_top';
		}
		
		function goToConsentPage()
		{
			var tempId=document.forms[0].id.value;
			var action="SearchObject.do?pageOf=<%=pageOf%>&operation=search&id="+tempId+"&tab=consent";
			document.forms[0].action=action;
			document.forms[0].submit();
		}
</script>	
	
<html:form action="<%=formName%>">	
<table summary="" cellpadding="0" cellspacing="0" border="0" height="90%" class="tabPage" width="90%">
			<tr>
				<td height="20" class="tabMenuItem" id="specimenCollectionGroupTab" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="setTarget(); editSCG()">
					<bean:message key="specimenCollectionGroupPage.edit.title"/>
				</td>		

				<td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()"  onClick="viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>								
				
				<td height="20" class="tabMenuItemSelected"  onClick="">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="goToConsentPage()" id="consentTab">
					<bean:message key="consents.consents"/>            
				</td>

				<td width="450" class="tabMenuSeparator" colspan="3">&nbsp;</td>
			</tr>

			<tr width = "100%" height = "100%">
				<td class="tabField" colspan="6"  width = "100%" height = "100%">
					<%@   include file="DisplayAnnotationDataEntryPage.jsp" %>
				</td>
			</tr>
		</table>
		
		</html:form>