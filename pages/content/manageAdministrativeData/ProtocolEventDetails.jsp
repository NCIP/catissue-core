<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
<script type="text/javascript" src="jss/combos.js"></script>
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ page import="java.util.*"%>

<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />


<%
	Object obj = request.getAttribute("protocolEventDetailsForm");
	String operation = "add";
	ProtocolEventDetailsForm form =null;
	if(obj != null && obj instanceof ProtocolEventDetailsForm)
	{
		form = (ProtocolEventDetailsForm)obj;
	}
	String operationType=null;
	boolean disabled = false;
	operationType = (String)request.getAttribute("opr");
	if(operationType!=null && operationType.equals("update"))
	{
		disabled = true;
	}
%>
<head>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script src="jss/caTissueSuite.js" language="JavaScript" type="text/javascript"></script>
<script language="JavaScript">

function specimenRequirements()
{
	var action ="SaveProtocolEvents.do?pageOf=specimenRequirement&operation=add";
	document.forms[0].action = action;
	document.forms[0].submit();
}

function deleteEvent()
{
	var answer = confirm ("Are you sure want to delete event?")
	if(answer)
	{
		document.forms[0].target = '_top';
		var action ="DeleteNodeFromCP.do?pageOf=cpEvent&operation=edit";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
}
function submitAllEvents()
{
	var action = "SaveProtocolEvents.do?pageOf=defineEvents&operation=add";
	document.forms[0].action = action;
	document.forms[0].submit();
}
function collectionProtocolPage()
{
	var action ="CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&invokeFunction=initCollectionProtocolPage";
	document.forms[0].action = action;
	document.forms[0].submit();
}
function consentPage()
{
	var action ="CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&invokeFunction=initCollectionProtocolPage&tab=consentTab";
	document.forms[0].action = action;
	document.forms[0].submit();
}
window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=${requestScope.operation}";

</script>
</head>


<html:form action="SaveProtocolEvents.do?pageOf=defineEvents&operation=add" styleId="protocolEventDetailsForm">
	<logic:equal name="isParticipantReg" value="true">
		<%@ include file="/pages/content/manageAdministrativeData/PersistentProtocolEvent.jsp" %>
	</logic:equal>
	<logic:notEqual name="isParticipantReg" value="true">
		<%@ include file="/pages/content/manageAdministrativeData/NonPersistentProtocolEvent.jsp" %>
	</logic:notEqual>
</html:form>

<script>
window.top.document.getElementById("errorRow").innerHTML = "";
</script>
