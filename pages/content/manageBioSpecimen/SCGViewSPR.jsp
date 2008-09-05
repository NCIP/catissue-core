<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<style>
.active-column-1 {width:200px}
</style>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	
	String operation = (String)request.getAttribute(Constants.OPERATION);
		
		String formAction = Constants.VIEW_SPR_ACTION;
			
		
		String staticEntityName=null;
		staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP;
		Long scgEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId") != null)
		{
			scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId");
		}
		else
		{
			scgEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
			CatissueCoreCacheManager.getInstance().addObjectToCache("scgEntityId",scgEntityId);		
		}	
		
String id = request.getParameter("id");
		
		
%>
<script>



function showAnnotations()
		{
			var action="DisplayAnnotationDataEntryPage.do?entityId=<%=scgEntityId%>&entityRecordId=<%=id%>&staticEntityName=<%=staticEntityName%>&pageOf=<%=pageOf%>&operation=viewAnnotations";
			document.forms[0].action=action;
			document.forms[0].submit();
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

<html:form action="<%=formAction%>">

	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"></td>
		  </tr>
		  <tr>
			<td class="tablepadding">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>				
			
				<td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" border="0" height="1" vspace="0" hspace="0"></td>
				<td valign="bottom" ><a href="#" onclick="editSCG()"><img src="images/uIEnhancementImages/tab_edit_collection2.gif" border="0" alt="Edit SCG" width="216" height="22" border="0" vspace="0" hspace="0"></a></td><td valign="bottom"><img src="images/uIEnhancementImages/tab_view_surgical1.gif" alt="View Surgical Pathology Report" width="216" height="22" vspace="0" hspace="0"></td>
				<td valign="bottom"><a href="#" onClick="showAnnotations()"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" border="0" alt="View Annotation" width="116" height="22" vspace="0" hspace="0"></a></td><td valign="bottom"><a href="#" id="consentTab" onClick="consentPage()"><img src="images/uIEnhancementImages/tab_consents2.gif" border="0" alt="Consents" width="76" height="22" vspace="0" hspace="0"></a></td><td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
				</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
				<tr>
				<td>
					<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				</td>
				</tr>

				<tr>
				<td colspan="0">
					<%@include file="ViewSurgicalPathologyReport.jsp" %>
				<!--</td>
				</tr>
				</table>-->
				</td>
			</tr>
		</table>		
</html:form>