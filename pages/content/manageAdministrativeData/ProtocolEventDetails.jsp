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
<link href="css/styleSheet.css" rel="stylesheet" type="text/css" /> 
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
<body>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="SaveProtocolEvents.do?pageOf=defineEvents&operation=add">

<table summary="" cellpadding="0" cellspacing="0" height="20"   width="85%" border="0">
  
    <tr >
      <td colspan="4" align="right">
		<span class="smalllink">
		  	<html:link href="#"  styleId="newUser" onclick="viewSummary()">
				<bean:message key="cpbasedentry.viewsummary" /> 
		    </html:link>
		</span>
       </td>   
   </tr>
	<tr>
		
		<td height="20" width="9%" nowrap valign="bottom" id="eventTab" background="images/empty.GIF"><img src="images/uIEnhancementImages/cp_event.gif"	alt="Consents" width="95" height="22" /></td>
		
		<td width="91%" class="cp_tabbg" colspan="2">&nbsp;</td>
	</tr>
	<tr>
	 <td class="cp_tabtable" colspan="4">
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" >
		   <tr><td width="700" class="buttonbg" colspan="3">&nbsp;</td></tr>
			<tr>
			   <td width="1%" align="left" valign="top" class="black_ar">
				 <span class="blue_ar_b">
				    	<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="3" />
				 </span>
			   </td>
			   <td width="22%" align="left" class="black_ar">
					<bean:message key="collectionprotocol.studycalendartitle" />
			   </td>
				<td align="left">
		        <html:text styleClass="black_ar_s" size="12" styleId="studyCalendarEventPoint"  maxlength="10" 
						property="studyCalendarEventPoint" />
	            &nbsp; 
				<span class="grey_ar">
					<bean:message key="collectionprotocol.studycalendarcomment"/>
				</span>
			</td>
			</tr>	
			<tr>
				 <td align="left" class="black_ar">
				<span class="blue_ar_b">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</span>
			  </td>
	          <td align="left" class="black_ar">
				<bean:message key="collectionprotocol.collectionpointlabel" />
			  </td>
			  <td align="left">
				<html:text styleClass="black_ar" size="30" styleId="collectionPointLabel" maxlength="255" 
							property="collectionPointLabel"/>
			  </td>
			</tr>
			<tr>
			  <td align="left" class="black_ar">
				<span class="blue_ar_b">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</span>
			  </td>
	          <td align="left" class="black_ar">
					<label for="clinicalDiagnosis">
						<bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
					</label>
			  </td>
	          <td align="left" class="black_new">
					<autocomplete:AutoCompleteTag property="clinicalDiagnosis"
						optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
						initialValue="<%=form.getClinicalDiagnosis()%>"
						styleClass="formFieldSized12"
						size="30"
					/> &nbsp;
	<%
			String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";	
	%>
					<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
						<img title='Clinical Diagnosis Selector' src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" border="0">
					</a>
				</td>
	        </tr>

			 <tr>
			  <td align="left" class="black_ar">
				<span class="blue_ar_b">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</span>
			  </td>
	          <td align="left" class="black_ar">
				<label for="clinicalStatus">
						<bean:message key="specimenCollectionGroup.clinicalStatus"/>
				</label>
			  </td>
	          <td align="left" class="black_new">
					<autocomplete:AutoCompleteTag property="clinicalStatus"
							  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
							  initialValue="<%=form.getClinicalStatus()%>"
							  styleClass="formFieldSized12"
							 
					/>
			  </td>
	        </tr>
		</table>
		&nbsp;
		<table>
			<tr>
				<td class="buttonbg" width="700">
					<html:button styleClass="blue_ar_b" property="submitPage" onclick="submitAllEvents()" disabled="<%=disabled%>">
						<bean:message key="buttons.submit"/>
					</html:button>
					&nbsp;|
					<html:button styleClass="blue_ar_b" property="submitPage" onclick="specimenRequirements()" disabled="<%=disabled%>">
						<bean:message key="cpbasedentry.addspecimenrequirements"/>
					</html:button>
				</td>
			</tr>
		</table>
	</td>
  </tr>
</table>

</html:form>
</body>

