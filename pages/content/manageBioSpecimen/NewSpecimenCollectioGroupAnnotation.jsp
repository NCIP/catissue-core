<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
%>
<script>
function updateHelpURL()
	{
		var URL="";
		URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm")%>";
		return URL;
	}
</script>

<script src="jss/script.js" type="text/javascript"></script>
<!-- Bug Id: 4159
	 Patch ID: 4159_1
	 Description: Including calenderComponent.js to show date in events
-->
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/calendarComponent.js" type="text/javascript"></script>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>

<%
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
		if(pageOf == null)
			pageOf =(String)request.getParameter(Constants.PAGE_OF);
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

<html:form action="<%=formName%>">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="6">
					<%@   include file="DisplayAnnotationDataEntryPage.jsp" %>
				</td>
			</tr>
		</table>
		</td>
			</tr>
		</table>

		</html:form>