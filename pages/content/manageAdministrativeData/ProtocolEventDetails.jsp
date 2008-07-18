<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ page import="java.util.*"%>

<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
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
	HttpSession newSession = request.getSession();
	CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean)newSession.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
	operationType = collectionProtocolBean.getOperation();
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
function viewSummary()
{
	var action="GenericSpecimenSummary.do?Event_Id=dummyId";
	document.forms[0].action=action;
	document.forms[0].submit();
}
window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=${requestScope.operation}";

</script>
</head>


<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="SaveProtocolEvents.do?pageOf=defineEvents&operation=add">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td valign="bottom" id="eventTab"><img src="images/uIEnhancementImages/cp_event.gif" alt="Collection Protocol Details" width="94" height="20" /><a href="ad_user_edit.html"></a><a href="ad_user_approve.html"></a></td>
                      
                      <td width="95%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td class="cp_tabtable">
                    <br>
                    <table width="100%" border="0" cellpadding="3" cellspacing="0">
                      <tr>
                        <td width="1%" align="center" valign="top" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" /></td>
                        <td width="22%" align="left" class="black_ar"><bean:message key="collectionprotocol.studycalendartitle" /></td>
                        <td width="77%" align="left"><label><html:text styleClass="black_ar_s" size="12" styleId="studyCalendarEventPoint"  maxlength="10" property="studyCalendarEventPoint" />&nbsp; <span class="grey_ar"><bean:message key="collectionprotocol.studycalendarcomment"/></span></label></td>
					 </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.collectionpointlabel" /></td>
                        <td align="left"><html:text styleClass="black_ar" size="30" styleId="collectionPointLabel" maxlength="255"property="collectionPointLabel"/></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/></td>
                        <td align="left"><autocomplete:AutoCompleteTag property="clinicalDiagnosis"
						optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
						initialValue="<%=form.getClinicalDiagnosis()%>"
						styleClass="black_ar"
						size="30"/>&nbsp;
						<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";	
						%>
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
						<img title='Clinical Diagnosis Selector' src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" border="0">
						</a>
					 </td>
                      </tr>
					 
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalStatus"/></td>
                        <td align="left"><autocomplete:AutoCompleteTag property="clinicalStatus"
							  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
							  initialValue="<%=form.getClinicalStatus()%>"
							  styleClass="black_ar"
							  size="30"/>
						</td>
                      </tr>
                      <tr>
                        <td align="center">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="3" class="buttonbg"><html:button styleClass="blue_ar_b" property="submitPage" onclick="submitAllEvents()" disabled="<%=disabled%>">
						<bean:message key="buttons.submit"/>
					</html:button>
                          &nbsp;|
                          <html:button styleClass="blue_ar_b" property="submitPage" onclick="specimenRequirements()" disabled="<%=disabled%>">
						<bean:message key="cpbasedentry.addspecimenrequirements"/>
					</html:button></td>
                      </tr>
                    </table>
               </td>
              </tr>
            </table>
</html:form>
