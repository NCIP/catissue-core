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
<%@ include file="/pages/content/common/CollectionProtocolCommon.jsp" %>

<%@ page import="java.util.*"%>

<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>

<%
	Object obj = request.getAttribute("protocolEventDetailsForm");
	ProtocolEventDetailsForm form =null;
	if(obj != null && obj instanceof ProtocolEventDetailsForm)
	{
		form = (ProtocolEventDetailsForm)obj;
	}	

%>
<head>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
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

window.parent.frames['SpecimenEvents'].location="showTree.do?pageOf=specimenEventsPage";

</script>
</head>
<body>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="ProtocolEventsDetails.do">
<table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
	<tr>
		<td height="20" width="9%" nowrap class="tabMenuItemSelected" onclick="defineEvents()" id="collectionProtocolTab">Protocol Event Details</td>
	
		<td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
	</tr>
	<tr>
	 <td class="tabField" colspan="3">
	 
	  <table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
		<tr>
			<td class="formMessage" colspan="6">
				<bean:message key="requiredfield.message"/>  
			</td>
		</tr>	
	  </table>
	
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
		<tr>
			<td class="formTitle" height="20" width="100%" colspan="7">
				<bean:message key="cpbasedentry.defineevents" />						
			</td>
		</tr>
			<tr>
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td colspan="1" class="formFieldNoBordersSimple">
					<label for="studyCalendarEventPoint">
						<b><bean:message key="collectionprotocol.studycalendartitle" /></b>
					</label>
				</td>
				<td class="formFieldNoBordersSimple" colspan="5">
					<html:text styleClass="formFieldSized5" size="30" 
							styleId="studyCalendarEventPoint"  maxlength="10" 
							property="studyCalendarEventPoint" 
							/>
					<bean:message key="collectionprotocol.studycalendarcomment"/>
				</td>
			</tr>	
			<tr>
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td colspan="1" class="formFieldNoBordersSimple">
					<label for="collectionPointLabel">
	                   <b><bean:message key="collectionprotocol.collectionpointlabel" /></b>
					</label>
				</td>
				<td class="formFieldNoBordersSimple" colspan="5">
					<html:text styleClass="formFieldSized" size="30" 
							styleId="collectionPointLabel" maxlength="255" 
							property="collectionPointLabel"/> 
				</td>
			</tr>
			<tr>
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td colspan="1" class="formFieldNoBordersSimple">
					<label for="clinicalDiagnosis">
						<b><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/></b>
					</label>
				</td>
				<td class="formFieldNoBordersSimple" colspan="5">
					 <autocomplete:AutoCompleteTag property="clinicalDiagnosis"
						optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
						initialValue="<%=form.getClinicalDiagnosis()%>"
						styleClass="formFieldSized"
						size="30"
				/>
				<%
					String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";			
				%>
					<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
						<img src="images\Tree.gif" border="0" width="26" height="22" title='Clinical Diagnosis Selector'>
					</a>
				</td>
			</tr>

			<tr>
				 <td class="formFieldNoBordersSimple" width="5">*</td>
				 <td class="formFieldNoBordersSimple">
					<label for="clinicalStatus">
						<b><bean:message key="specimenCollectionGroup.clinicalStatus"/></b>
					</label>
				 </td>
				 <td class="formFieldNoBordersSimple" colspan="5">
					 <autocomplete:AutoCompleteTag property="clinicalStatus"
							  optionsList = "<%=request.getAttribute(Constants.CLINICAL_STATUS_LIST)%>"
							  initialValue="<%=form.getClinicalStatus()%>"
							  styleClass="formFieldSized"
							 
					/>
				</td>
			</tr>
			<tr>	
				<html:hidden property="collectionEventId" />
				<html:hidden property="collectionEventSpecimenId" />
				<td class="formFieldNoBordersSimple" width="5">*</td>
	 			<td class="formFieldNoBordersSimple"> 
					<label for="user">
						<b><bean:message key="specimen.collectedevents.username"/> </b>
					</label>
				</td>						
				<td colspan="2" class="formFieldNoBordersSimple">
					<autocomplete:AutoCompleteTag property="collectionEventUserId"
								  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
								  initialValue="<%=form.getCollectionEventUserId()%>"
								  staticField="false"
					/>		
				</td>		
				<!-- RecievedEvent fields -->
				<html:hidden property="receivedEventId" />
				<html:hidden property="receivedEventSpecimenId" />
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td class="formFieldNoBordersSimple">
					<label for="type">
						<b><bean:message key="specimen.receivedevents.username"/> </b>
					</label>
				</td>
				<td colspan="2" class="formFieldNoBordersSimple">
					<autocomplete:AutoCompleteTag property="receivedEventUserId"
						  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
						  initialValue="<%=form.getReceivedEventUserId()%>"
						  staticField="false"
				/>	
			</tr>
			<tr>					
				<!-- CollectionEvent fields -->	
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td class="formFieldNoBordersSimple">
					<label for="collectionprocedure">
						<b><bean:message key="cpbasedentry.collectionprocedure"/></b>
					</label>
				</td>
				<td class="formFieldNoBordersSimple" colspan="2">
						<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
								  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
								  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
				/>							
				</td>						
				
				<!-- RecievedEvent fields -->
				<td class="formFieldNoBordersSimple" width="5"rowspan="2">*</td>
				<td class="formFieldNoBordersSimple"rowspan="2"> 
					<label for="quality">
						<b><bean:message key="cpbasedentry.receivedquality"/></b>
					</label>
				</td>						
				<!-- receivedeventparameters.receivedquality -->
				<td class="formFieldNoBordersSimple"rowspan="2" colsapn ="2">
					<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
								  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
								  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
				/>
				</td>
			</tr>
			<!-- CollectionEvent fields -->	
			<tr>							
				<td class="formFieldNoBordersSimple" width="5">*</td>
				<td class="formFieldNoBordersSimple">
					<label for="container">
						<b><bean:message key="cpbasedentry.collectioncontainer"/></b>
					</label>
				</td>
				<td class="formFieldNoBordersSimple">
					<autocomplete:AutoCompleteTag property="collectionEventContainer"
							  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
							  initialValue="<%=form.getCollectionEventContainer()%>"
			    />
				</td>
			</tr>	
		<table>
		&nbsp;
		<table>
			<tr>
				<td>
					<html:button styleClass="actionButton" property="submitPage" onclick="submitAllEvents()">
						<bean:message key="buttons.submit"/>
					</html:button>
					<html:button styleClass="actionButton" property="submitPage" onclick="specimenRequirements()">
						<bean:message key="cpbasedentry.addspecimenrequirements"/>
					</html:button>
				</td>
			</tr>
		</table>
	</td>
  </tr>
</table>
&nbsp;
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
		<tr>
			<td width="400">
			&nbsp;
			</td>
			<td align="right">
				<html:button styleClass="actionButton" property="submitPage" onclick="">
					<bean:message key="cpbasedentry.savecollectionprotocol"/>
				</html:button>
			</td>
		</tr>
</table>
</html:form>
</body>

