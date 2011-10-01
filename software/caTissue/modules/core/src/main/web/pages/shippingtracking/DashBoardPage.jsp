<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	

<head>
<link href="css/shippingtracking/addl_s_t.css" rel="stylesheet" type="text/css">
<link href="css/shippingtracking/shippingTracking.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
	
<script>
function showHide(elementid)
{
	if (document.getElementById(elementid).style.display == 'none')
	{
		document.getElementById(elementid).style.display = '';
		document.getElementById(elementid+"_arrowDiv").innerHTML='<img src="images/shippingtracking/up_arrow2.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />';
	}
	else
	{
		document.getElementById(elementid).style.display = 'none';
		document.getElementById(elementid+"_arrowDiv").innerHTML='<img src="images/shippingtracking/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />';		
	}
}
function createNewShipment()
{
	document.forms[0].action="BaseShipment.do";
	document.forms[0].submit();
} 
function createNewRequest()
{
  document.forms[0].action="ShipmentRequestAction.do";
  document.forms[0].submit();
}

function recordsPerPageChanged(selectBox)
{
	document.forms[0].action="ShowDashboardAction.do";
	document.forms[0].submit();
}
</script>
</head>

<body>
<table width="100%">
	<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="dashboard.name" bundle="msg.shippingtracking"/></span></td>
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action='/ShowDashboardAction'>

<html:hidden property="recordsPerPage" styleId="recordsPerPage"/>

<c:set var="identifierFieldIndex" value="${requestScope.identifierFieldIndex}"/>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  				<tr>
    				<td class="td_color_bfdcf3">
    					<c:set var="isAdmin" value="${requestScope.isAdmin}"/>
    					
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
				  			<tr>
								<td height="18" colspan="2" class="tablepadding"></td>
      						</tr>
      						<tr>
		        				<td colspan="2" class="tablepadding">
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
										<tr>
											<td align="left" valign="middle" class="tr_bg_blue1">
													&nbsp;<span class="blue_ar_b">
														<c:if test="${isAdmin}">
															<bean:message key="dashboard.shipmentRequests" bundle="msg.shippingtracking"/>
														</c:if>
														<c:if test="${!isAdmin}">
															<bean:message key="dashboard.requestreceived" bundle="msg.shippingtracking"/>
														</c:if>
													</span>
													<span class="black_ar">
														<c:if test="${!isAdmin}">
																&nbsp;<bean:message key="dashboard.requestreceived.explanation" bundle="msg.shippingtracking"/>
														</c:if>
													</span>
											</td>
											<td align="left" valign="middle" class="tr_bg_blue1">
												&nbsp;
											</td>
											<td width="12%" align="right" class="tr_bg_blue1">
												<html:link href=" #" onclick="showHide('requestsReceivedDiv')">
													<div id="requestsReceivedDiv_arrowDiv">
														<img src="images/shippingtracking/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />
													</div>
												</html:link>
											</td>
										</tr>
										<tr>
											<td colspan="5">	
												<logic:equal name="requestFor" value="reqReceivedNextPage">
													<div id="requestsReceivedDiv" style="overflow:auto;height:150px" >
												</logic:equal>
												<logic:notEqual name="requestFor" value="reqReceivedNextPage">
													<div id="requestsReceivedDiv" style="display:none;overflow:auto;height:150px" >
												</logic:notEqual>
												
              										<%@ include file="/pages/shippingtracking/RequestsReceived.jsp" %>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
		        				<td colspan="2" class="tablepadding">
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
										<tr>
											<td align="left" valign="middle" class="tr_bg_blue1">
													&nbsp;<span class="blue_ar_b">
														<c:if test="${isAdmin==false}">
															<bean:message key="dashboard.shipmentreceived" bundle="msg.shippingtracking"/>
														</c:if>
														<c:if test="${isAdmin}">
															<bean:message key="dashboard.shipments" bundle="msg.shippingtracking"/>
														</c:if>
													</span>
													<span class="black_ar">
														<c:if test="${!isAdmin}">
																&nbsp;<bean:message key="dashboard.shipmentreceived.explanation" bundle="msg.shippingtracking"/>
														</c:if>
													</span>
											</td>
											<td align="left" valign="middle" class="tr_bg_blue1">
												<span class="black_ar_s">
													&nbsp;
												</span>
											</td>
											<td width="12%" align="right" class="tr_bg_blue1">
												<html:link href="javascript:showHide('incomingShipmentsDiv')">
													<div id="incomingShipmentsDiv_arrowDiv">
														<img src="images/shippingtracking/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />
													</div>
												</html:link>
											</td>
										</tr>
										<tr>
											<td colspan="5">
												<logic:equal name="requestFor" value="incomingShipNextPage">
													<div id="incomingShipmentsDiv" style="overflow:auto;height:150px" >
												</logic:equal>
												<logic:notEqual name="requestFor" value="incomingShipNextPage">
													<div id="incomingShipmentsDiv" style="display:none;overflow:auto;height:150px" >
												</logic:notEqual>	
												
              										<%@ include file="/pages/shippingtracking/IncomingShipments.jsp" %>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<c:if test="${isAdmin==false}">
								<tr>
			        				<td colspan="2" class="tablepadding">
										<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
											<tr>
												<td align="left" valign="middle" class="tr_bg_blue1">
														&nbsp;<span class="blue_ar_b"><bean:message key="dashboard.outgoingShipments" bundle="msg.shippingtracking"/></span>
														<span class="black_ar">
																&nbsp;<bean:message key="dashboard.outgoingShipments.explanation" bundle="msg.shippingtracking"/>
														</span>
												</td>
												<td align="left" valign="middle" class="tr_bg_blue1">
													<span class="black_ar_s">
														&nbsp;
													</span>
												</td>
												<td width="12%" align="right" class="tr_bg_blue1">
													<html:link href="javascript:showHide('outGoingShipmentsDiv')">
														<div id="outGoingShipmentsDiv_arrowDiv">
															<img src="images/shippingtracking/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />
														</div>
													</html:link>
												</td>
											</tr>
											<tr>
												<td colspan="5">	
													<logic:equal name="requestFor" value="outgoingShipNextPage">
														<div id="outGoingShipmentsDiv" style="overflow:auto;height:150px" >
													</logic:equal>
													<logic:notEqual name="requestFor" value="outgoingShipNextPage">
														<div id="outGoingShipmentsDiv" style="display:none;overflow:auto;height:150px" >
													</logic:notEqual>	
	              										<%@ include file="/pages/shippingtracking/OutgoingShipments.jsp" %>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:if>
							<c:if test="${isAdmin==false}">
							<tr>
		        				<td colspan="2" class="tablepadding">
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
										<tr>
											<td align="left" valign="middle" class="tr_bg_blue1">
													&nbsp;<span class="blue_ar_b">
															<bean:message key="dashboard.requestsSent" bundle="msg.shippingtracking"/>
													</span>
													<span class="black_ar">
																&nbsp;<bean:message key="dashboard.requestsSent.explanation" bundle="msg.shippingtracking"/>
														</span>
											</td>
											<td align="left" valign="middle" class="tr_bg_blue1">
												<span class="black_ar_s">
													&nbsp;
												</span>
											</td>
											<td width="12%" align="right" class="tr_bg_blue1">
												<html:link href="javascript:showHide('reqSentDiv')">
													<div id="reqSentDiv_arrowDiv">
														<img src="images/shippingtracking/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0" />
													</div>
												</html:link>
											</td>
										</tr>
										<tr>
											<td colspan="5">
												<logic:equal name="requestFor" value="reqSentpNextPage">
													<div id="reqSentDiv" style="overflow:auto;height:200px" >
												</logic:equal>
												<logic:notEqual name="requestFor" value="reqSentpNextPage">
													<div id="reqSentDiv" style="display:none;overflow:auto;height:150px" >
												</logic:notEqual>	
              										<%@ include file="/pages/shippingtracking/MyShipmentRequests.jsp" %>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							</c:if>
							
							<tr>
      							<td valign="middle" class="tablepadding">
  											<html:button  property="createShipment" styleId="createShipment" styleClass="blue_ar_b" accesskey="createnewshipment" value="Create New Shipment" onclick="createNewShipment()"></html:button>
											&nbsp;<img src="images/shippingtracking/bl_sep.gif" width="1" height="19" align="absmiddle">&nbsp;
  											<html:button  property="createRequest" styleId="createRequest" styleClass="blue_ar_b" accesskey="createnewrequest" value="Create New Request" onclick="createNewRequest()"></html:button>
											</td>
								<td align="right" class="blue_ar_b">
									<html:select property="recordsPerPage" styleClass="black_ar" styleId="recordsPerPage" onchange="recordsPerPageChanged(this)">
											<html:options collection="<%=Constants.RESULTS_PER_PAGE%>" labelProperty="name" property="value"/>
									</html:select>
									&nbsp; <bean:message key="dashboard.recordsPerPage" bundle="msg.shippingtracking"/>
								</td> 
							</tr>
						</table>
					</td>
				</tr>
		</table>
 </html:form>
</table>
</td>
</tr>
</table>
</body>
