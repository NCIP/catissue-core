<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm "%>
<%@ page import="edu.wustl.common.util.Utility"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><tiles:getAsString name="title" ignore="true" /></title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" media="screen"/> 
<link href="css/shippingtracking/addl_s_t.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
function showHide(elementid){
if (document.getElementById(elementid).style.display == 'none'){
document.getElementById(elementid).style.display = '';
} else {
document.getElementById(elementid).style.display = 'none';
}
}

function openInEdit(id)
{
	document.forms[0].action="/SNTSearchObject.do?pageOf=pageOfShipment&id="+id;
	document.forms[0].submit();
}


function createShipmentForRequest()
{
	var id=document.getElementById("id").value;
	document.forms[0].action="RequestReceived.do?operation=add&pageOf=pageOfShipment&id="+id;
	document.forms[0].submit();
}

function rejectRequest()
{
	document.getElementById("activityStatus").value='<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACTIVITY_STATUS_REJECTED%>';
	document.forms[0].action="EditShipmentRequestForwardToDashboard.do?operation=edit&amp;pageOf=pageOfShipmentRequest";
	document.forms[0].submit();
}

function saveDraftShipmentRequest() {
	document.getElementById("activityStatus").value = '<%=edu.wustl.catissuecore.util.shippingtracking.Constants.ACTIVITY_STATUS_DRAFTED%>';
	document.forms[0].action="SaveDraftShipmentRequest.do";
	document.forms[0].submit();
}

function createRequestForDraft() {
	var id=document.getElementById("id").value;
	document.forms[0].action="ViewRequestSummary.do?operation=add&draftedRequestId=" + id;
	document.forms[0].submit();
}
//-->
</SCRIPT>

<%
	Object obj=request.getAttribute("shipmentRequestForm");
	String shipmentDate="";
	if(obj instanceof ShipmentRequestForm)
	{
		shipmentDate=((ShipmentRequestForm)obj).getSendDate();
	}
%>
</head>
<body>

<table width="100%">
		<tr>
				<td class="td_color_bfdcf3">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_table_head"><span class="wh_ar_b"><bean:message
										key="shipmentRequest.name" bundle="msg.shippingtracking"/></span></td>
									<td align="right"><img
										src="images/uIEnhancementImages/table_title_corner2.gif"
										alt="Page Title" width="31" height="24" /></td>
								</tr>
							</table>
					</td>
	</tr>
	<tr>
		<td width="100%">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<c:set var="operation" value="${requestScope.operation}"/>
		<jsp:useBean id="operation" type="java.lang.String"/>
		
		<c:set var="readOnlyStatus" value="${operation=='view'}"/>
		<jsp:useBean id="readOnlyStatus" type="java.lang.Boolean"/>
		
		<c:set var="formAction" value="/ViewRequestSummary"/>
		
		<logic:equal name="operation" value="edit">
			<c:set var="formAction" value="/EditShipmentRequest"/>
		</logic:equal>
		
		<logic:equal name="operation" value="view">
			<c:set var="formAction" value="/EditShipmentRequest"/>
		</logic:equal>
	
		<jsp:useBean id="formAction" type="java.lang.String"/>
			
		<html:form action="<%=formAction%>">
		
		<html:hidden property="operation" styleId="operation" value="<%=operation%>"/>
		<html:hidden property="id" styleId="id"/>
		<html:hidden property="activityStatus" styleId="activityStatus"/>
		<html:hidden property="createdDate" styleId="createdDate"/>
		
		<c:set var="noOfSpecimens" value="${shipmentRequestForm.specimenCounter}"/>
		<c:set var="noOfContainers" value="${shipmentRequestForm.containerCounter}"/>  
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0">
		      <tr>
		        <td colspan="2" class="toptd"></td>
		      </tr>
		      <tr>
		        <td colspan="2" class="tablepadding"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="whitetable_bg">
		          <tr>
		            <td height="25" colspan="2" align="left" valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
		            		<c:if test="${operation=='add'}">
	`			            	<bean:message key="newShipmentRequest" bundle="msg.shippingtracking"/>
							</c:if>
							
							<c:if test="${operation=='edit'}">
								<bean:message key="edit.shipmentRequest" bundle="msg.shippingtracking"/>
							</c:if>
		            	</span>
		            </td>
		          </tr>
		          <tr>
		          	<td colspan="2">
		          <!-- include shipemnt details jsp here-->
		          		<%@ include file="/pages/shippingtracking/ShipmentRequestDetails.jsp"%>
		          	</td>
		          </tr>  
		         </table></td>
		          </tr>
		          <tr>
		          	<td colspan="2">
		          <!-- import contents jsps -->
		          		<%@ include file="/pages/shippingtracking/ShipmentContents.jsp" %>
		          	</td>
		          </tr>
		          <tr>
		            <td colspan="2" align="left">&nbsp;</td>
		          </tr>
		          <tr >
		            <td height="30" colspan="1" class="buttonbg">
			            	<c:if test="${operation=='add'}">
				            	<html:submit styleClass="blue_ar_b" accesskey="A"><bean:message key="buttons.create" /></html:submit>
								&nbsp;<img src="images/shippingtracking/bl_sep.gif" width="1" height="19" align="absmiddle">&nbsp;
								
								<!-- Save Draft -->	
								<html:button property="saveDraft" styleClass="blue_ar_b" styleId="saveDraft" onclick="saveDraftShipmentRequest()">	
									<bean:message key="buttons.save.draft" bundle="msg.shippingtracking"/>
								</html:button>
							</c:if>
							
							<c:if test="${operation=='edit'}">
								<c:choose>
									<c:when test="${shipmentRequestForm.activityStatus == 'Drafted'}">
										<html:submit styleClass="blue_ar_b" accesskey="A"><bean:message key="buttons.edit" /></html:submit>
										&nbsp;<img src="images/shippingtracking/bl_sep.gif" width="1" height="19" align="absmiddle">&nbsp;
										<html:button property="createRequest" styleClass="blue_ar_b" styleId="createRequest" onclick="createRequestForDraft()">	
											<bean:message key="buttons.create" bundle="msg.shippingtracking"/>
										</html:button>
									</c:when>
								
									<c:when test="${shipmentRequestForm.activityStatus == 'In Progress'}">
										<html:submit styleClass="blue_ar_b" accesskey="A"><bean:message key="buttons.edit" /></html:submit>
									</c:when>

									<c:otherwise>
										<html:submit styleClass="blue_ar_b" accesskey="A" disabled="true"><bean:message key="buttons.edit" /></html:submit>
									</c:otherwise>
								</c:choose>
							</c:if>							
		            </td>
		            <td height="30" colspan="1" class="buttonbg" align="right">
		            	<logic:equal name="operation" value='view'>
							<html:button property="reject" styleClass="blue_ar_b" styleId="reject" onclick="rejectRequest()">
								<bean:message key="buttons.reject" bundle="msg.shippingtracking"/>
							</html:button>
							&nbsp;<img src="images/shippingtracking/bl_sep.gif" width="1" height="19" align="absmiddle">&nbsp;
							<html:button property="createShipment" styleClass="blue_ar_b" styleId="createShipment" onclick="createShipmentForRequest()">	
								<bean:message key="buttons.create.shipment" bundle="msg.shippingtracking"/>
							</html:button>
						</logic:equal>
						<logic:notEqual name="operation" value='view'>
							&nbsp;
						</logic:notEqual>
		            </td>
		          </tr>
		        </table>        
		        </td>
		      </tr>
		    </table></td>
		  </tr>
		</table>
		</html:form>
		</td>
	</tr>
</table>
</body>
</html>