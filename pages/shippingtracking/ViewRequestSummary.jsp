<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm "%>
<%@ page import="edu.wustl.common.util.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><tiles:getAsString name="title" ignore="true" /></title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" media="screen"/> 
<link href="css/addl_s_t.css" rel="stylesheet" type="text/css">

<script src="jss/shippingtracking/shippingTracking.js" type="text/javascript"></script>

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
	document.forms[0].action="RequestReceived.do?operation=view&id="+id;
	document.forms[0].submit();
}

function editShipmentRequest()
{
	var id=document.getElementById("id").value;
	document.forms[0].action="OutgoingShipment.do?id="+id;
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
	
		<html:form action="/AddEditShipmentRequest">
		
		<html:hidden property="operation" styleId="operation" value="<%=operation%>"/>
		<html:hidden property="id" styleId="id"/>
		<html:hidden property="activityStatus" styleId="activityStatus"/>
		<html:hidden property="createdDate" styleId="createdDate"/>
		
		<c:set var="noOfSpecimens" value="${shipmentRequestForm.specimenCounter}"/>
		<c:set var="noOfContainers" value="${shipmentRequestForm.containerCounter}"/>  
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			  <tr>
				    <td>
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
									<tr>
											<td height="25" colspan="6" align="left" valign="middle" class="formTitle"><span class="">&nbsp;View Summary</span>
											</td>
									</tr>
		          					<tr>
											<td class="formRequiredNotice" width="5">&nbsp;</td>
											<td class="formRequiredLabel">
													<label for="tissueSite">
															<bean:message key="shipment.label" bundle="msg.shippingtracking"/> 
													</label>
											</td>
											<td class="formField">
													<c:out value="${shipmentRequestForm.label}"/>		
													<html:hidden styleId="label" property="label"/>					
											</td>
											<td class="formRequiredNotice" width="5">&nbsp;</td>
											<td class="formRequiredLabel">
													<label for="tissueSite">
															<bean:message key="shipment.request.requesterSite" bundle="msg.shippingtracking"/> 
													</label>
											</td>
											<td class="formField">
													<html:select property="senderSiteId" style="display:none" styleId="senderSiteId">
													<html:options collection="<%=edu.wustl.catissuecore.util.shippingtracking.Constants.REQUESTERS_SITE_LIST%>" labelProperty="name" property="value"/>
													</html:select>
																				
													<c:out value="${requestScope.senderSiteName}"/>
											</td>
									</tr>
									<tr>
											<td class="formRequiredNotice" width="5">&nbsp;</td>
											<td class="formRequiredLabel">
													<label for="tissueSite">
															<bean:message key="shipment.request.sentOn" bundle="msg.shippingtracking"/> 
													</label>
											</td>
												
											<td class="formField">
													<c:out value="${shipmentRequestForm.sendDate}"/>		
													<html:hidden styleId="sendDate" property="sendDate"/>					
											</td>
											<td class="formRequiredNotice" width="5">&nbsp;</td>
											<td class="formRequiredLabel">
													<label for="tissueSite">
															<bean:message key="shipment.request.senderComments" bundle="msg.shippingtracking"/> 
													</label>
											</td>
											<td class="formField">
														<html:hidden styleId="senderComments" property="senderComments"/>			
																<c:out value="${shipmentRequestForm.senderComments}"/>							
											</td>
									</tr>
						<!--	</table>
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">  -->
										<c:set var="noOfSpecimens" value="${requestScope.specimenCountArr}"/>
										<c:set var="noOfContainers" value="${requestScope.containerCountArr}"/>

										<jsp:useBean id="noOfSpecimens" type="java.lang.Integer[]"/>
										<jsp:useBean id="noOfContainers" type="java.lang.Integer[]"/>

										<c:set var="specimenLabelArr" value="${requestScope.specimenLabelArr}"/>
										<c:set var="containerLabelArr" value="${requestScope.containerLabelArr}"/>  
										<c:set var="recieverSiteNameArr" value="${requestScope.recieverSiteNameArr}"/> 

										<jsp:useBean id="specimenLabelArr" type="java.lang.String[]"/>
										<jsp:useBean id="containerLabelArr" type="java.lang.String[]"/>
										<jsp:useBean id="recieverSiteNameArr" type="java.lang.String[]"/>

										<% int specimenCounter=0;
										int containerCounter=0;
										%>

										<c:set var="siteCount" value="${requestScope.siteCount}"/>
										<jsp:useBean id="siteCount" type="java.lang.Integer"/>
															
										<c:forEach var="siteNumber" begin="0" end="${siteCount-1}">
										<jsp:useBean id="siteNumber" type="java.lang.Integer"/>
																					
										<tr width="100%">
													<td height="25" colspan="6" align="left" valign="middle" class="formTitle"><span class="">&nbsp;&nbsp;Request for site '<%=recieverSiteNameArr[siteNumber]%>'</span>
													</td>
										</tr>
										<tr width="100%">
													<c:set var="specimenCount"><%=noOfSpecimens[siteNumber]%></c:set>
													<c:if test="${specimenCount>0}">
													<td colspan="3" valign="top">
																<table width="100%" valign="top">
																			<tr>
																						<td class="formRequiredNotice" width="5">&nbsp;</td>
																						<td class="formRequiredLabel" width="10">
																									<label for="tissueSite">
																													No.	
																													</label>
																						</td>	
																						<td class="formRequiredLabel">
																									<label for="tissueSite">
																															Specimens Label/Barcode	
																													</label>
																						</td>	
																				</tr>
																				<c:forEach var="specimenNumber" begin="1" end="${specimenCount}">
																				<tr>
																							<td class="formRequiredNotice" width="5">&nbsp;</td>
																							<td class="formRequiredLabel" width="10">
																									<c:out value="${specimenNumber}"/>
																							</td>
																							<td class="formRequiredLabel">
																																<label for="tissueSite">
																																		<c:set var="specimenlabelName">specimenDetails_<c:out value="${siteNumber}"/>_<c:out value="${specimenNumber}"/></c:set>
																																		<jsp:useBean id="specimenlabelName" type="java.lang.String"/>
																																		
																																		<html:hidden styleId="<%=specimenlabelName%>" property="<%=specimenlabelName%>" value="<%=specimenLabelArr[specimenCounter]%>"/>	 
																																		<%=specimenLabelArr[specimenCounter++]%>
																																</label>
																								</td>
																				</tr>
																					</c:forEach>
																		</table>
														</td>
														</c:if>
														<c:if test="${specimenCount==0}"> 
														<td colspan=3>
																							&nbsp;
														</td>
														</c:if>  
														<c:set var="containerCount"><%=noOfContainers[siteNumber]%></c:set>
														<c:if test="${containerCount>0}"> 
														<td width="50%" valign="top" colspan=3>
																		<table width="100%" valign="top">
																				<tr>
																							<td class="formRequiredNotice" width="5">&nbsp;</td>
																							<td class="formRequiredLabel" width="10">
																									<label for="tissueSite">
																													No.	
																													</label>
																							</td>	
																							<td class="formRequiredLabel">
																													<label for="tissueSite">
																															Containers Name/Barcode	
																													</label>
																							</td>
																					</tr>
																					<c:forEach var="containerNumber" begin="1" end="${containerCount}">
																					<tr>
																									<td class="formRequiredNotice" width="5">&nbsp;</td>
																									<td class="formRequiredLabel" width="10">
																										<c:out value="${containerNumber}"/>
																									</td>
																									<td class="formRequiredLabel">
																																<label for="tissueSite">
																																		<c:set var="containerlabelName">containerDetails<c:out value="${siteNumber}"/>_<c:out value="containerNumber"/></c:set>
																																		<jsp:useBean id="containerlabelName" type="java.lang.String"/>
																																		
																																		<html:hidden styleId="<%=containerlabelName%>" property="<%=containerlabelName%>" value="<%=containerLabelArr[containerCounter]%>"/>	    	
																																		<%=containerLabelArr[containerCounter++]%>
																																</label>
																									</td>
																					</tr>
																									</c:forEach>
																		</table>
														</td>
														</c:if>
														<c:if test="${containerCount>0}"> 
														<td colspan=3>
																								&nbsp;
														</td>
														</c:if>  
										</tr>
										</c:forEach>
							</table>
					</td>
		      </tr>
			  <tr>
								<td height="30" class="buttonbg">
										<html:button property="dashbaord" styleId="dashbaord" styleClass="blue_ar_b" accesskey="A" onclick="viewDashboard()">
											<bean:message key="buttons.viewDashboard" bundle="msg.shippingtracking"/>
										</html:button>
								</td>
			  </tr>
		</table>
		</html:form>
	</td>
 </tr>
</table>
</body>
</html>
