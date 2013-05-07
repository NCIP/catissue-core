<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page
	import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm "%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page
	import="java.util.HashMap,edu.wustl.common.beans.SessionDataBean"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><tiles:getAsString name="title" ignore="true" /></title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="css/addl_s_t.css" rel="stylesheet" type="text/css">

<script src="jss/shippingtracking/shippingTracking.js"
	type="text/javascript"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
function showHide(elementid){
if (document.getElementById(elementid).style.display == 'none'){
document.getElementById(elementid).style.display = '';
} else {
document.getElementById(elementid).style.display = 'none';
}
}

function editShipment()
{
	//Edit shipment call
	var id=document.getElementById("id").value;
	document.forms[0].action="OutgoingShipment.do?id="+id;
	document.forms[0].submit();
}
  

function printShipment()
{	
	//window.print();
	document.forms[0].action = "PrintShipmentSummary.do"; 
	document.forms[0].method = "post"; 
	document.forms[0].target = "_blank";
	document.forms[0].submit(); 	

}

//-->
</SCRIPT>

<%
	Object obj=request.getAttribute("shipmentForm");
	String shipmentDate="";
	ShipmentForm shipmentForm=(ShipmentForm)obj;
	if(obj instanceof ShipmentForm)
	{
		shipmentDate=((ShipmentForm)obj).getSendDate();
	}
	 
%>
</head>
<body>

<table width="100%">
	<tr>
		<td class="whitetable_bg"><!--class="td_color_bfdcf3"> -->
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_table_head"><span class="wh_ar_b"><bean:message
					key="shipment.name" bundle="msg.shippingtracking" /></span></td>
				<td align="right"><img
					src="images/uIEnhancementImages/table_title_corner2.gif"
					alt="Page Title" width="31" height="24" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td width="100%"><%@ include
			file="/pages/content/common/ActionErrors.jsp"%>
		</td>
	</tr>
	<tr>
		<td width="100%"><c:set var="operation"
			value="${requestScope.operation}" /> <jsp:useBean id="operation"
			type="java.lang.String" /> <html:form action="/CreateNewShipment">

			<html:hidden property="operation" styleId="operation"
				value="<%=operation%>" />
			<html:hidden property="id" styleId="id" />
			<html:hidden property="activityStatus" styleId="activityStatus" />
			<html:hidden property="createdDate" styleId="createdDate" />

			<c:set var="noOfSpecimens" value="${shipmentForm.specimenCounter}" />
			<c:set var="noOfContainers" value="${shipmentForm.containerCounter}" />
			<html:hidden styleId="specimenCounter" property="specimenCounter"
				value="${noOfSpecimens}" />
			<html:hidden styleId="containerCounter" property="containerCounter"
				value="${noOfContainers}" />
			<table width="100%" border="0" align="center" cellpadding="4"
				cellspacing="0" class="whitetable_bg">
				<tr class="tableheading">
					<td height="25" colspan="8" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.viewSummary" bundle="msg.shippingtracking" /></span></td>
				</tr>
				<tr>

					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.label" bundle="msg.shippingtracking" />
					</label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.label}" /> <html:hidden styleId="label"
						property="label" /></td>
				</tr>
				<tr>

					<td class="black_ar" colspan="2"><label for="sName"> <bean:message
						key="shipment.senderName" bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.senderName}" /> <html:hidden
						styleId="senderName" property="senderName" /></td>


					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.request.senderComments"
						bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><html:hidden
						styleId="senderComments" property="senderComments" /> <c:out
						value="${shipmentForm.senderComments}" />&nbsp;</td>
				</tr>

				<tr>

					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.senderSite"
						bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${requestScope.senderSiteName}" /> <html:hidden
						styleId="senderSiteName" property="senderSiteName" /></td>


					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.barcode" bundle="msg.shippingtracking" />
					</label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.barcode}" /> <html:hidden styleId="barcode"
						property="barcode" />&nbsp;</td>
				</tr>


				<tr>

					<td class="black_ar" colspan="2"><label for="sName"> <bean:message
						key="shipment.senderPhone" bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.senderPhone}" /> <html:hidden
						styleId="senderPhone" property="senderPhone" /></td>


					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.request.sentOn"
						bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.sendDate}" /> <html:hidden
						styleId="sendDate" property="sendDate" /></td>
				</tr>

				<tr>

					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.senderEmail"
						bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.senderEmail}" /> <html:hidden
						styleId="senderEmail" property="senderEmail" /></td>


					<td class="black_ar" colspan="2"><label for="tissueSite">
					<bean:message key="shipment.receiverSite"
						bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${requestScope.receiverSiteName}" /> <html:hidden
						styleId="receiverSiteName" property="receiverSiteName" /></td>
				</tr>
				<!-- bug 11026 start -->
				<tr>



					<td class="black_ar" colspan="2"><label for="rName"> <bean:message
						key="shipment.receiverName" bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.receiverSiteCoordinator}" /> <html:hidden
						styleId="receiverSiteCoordinator"
						property="receiverSiteCoordinator" /></td>



					<td class="black_ar" colspan="2"><label for="rName"> <bean:message
						key="shipment.receiverPhone" bundle="msg.shippingtracking" /> </label></td>

					<td class="black_ar" colspan="2"><c:out
						value="${shipmentForm.receiverSiteCoordinatorPhone}" /> <html:hidden
						styleId="receiverSiteCoordinatorPhone"
						property="receiverSiteCoordinatorPhone" /></td>
				</tr>
				<tr class="tableheading">

					<td height="25" colspan="2" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.contents" bundle="msg.shippingtracking" /></span></td>
					<td height="25" colspan="2" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.description" bundle="msg.shippingtracking" /></span></td>
					<td height="25" colspan="2" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.print.Label" bundle="msg.shippingtracking" /></span></td>
					<td height="25" colspan="2" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.print.barcode" bundle="msg.shippingtracking" /></span></td>

				</tr>

				<tr>

					<c:if test="${noOfSpecimens>0}">

						<c:forEach var="specimenNumber" begin="1" end="${noOfSpecimens}">
							<jsp:useBean id="specimenNumber" type="java.lang.Integer" />
							<tr>

								<td class="black_ar" colspan="2"><label for="tissueSite">
								<bean:message key="shipment.contentsSpecimenLabel"
									bundle="msg.shippingtracking" /> </label></td>
								<td class="black_ar" colspan="2"></td>
								<td class="black_ar" colspan="2"><c:set var="labelName">specimenDetails(specimenLabel_<c:out
										value="${specimenNumber}" />)</c:set> <jsp:useBean id="labelName"
									type="java.lang.String" /> <html:hidden
									property="<%=labelName%>" /> <%=shipmentForm.getSpecimenDetails("specimenLabel_"+specimenNumber)%>
								</td>
								<td class="black_ar" colspan="2"><c:set var="barcode">specimenDetails(specimenBarcode_<c:out
										value="${specimenNumber}" />)</c:set> <jsp:useBean id="barcode"
									type="java.lang.String" /> <html:hidden property="<%=barcode%>" />

								<%=shipmentForm.getSpecimenDetails("specimenBarcode_"+specimenNumber)%>
								</td>
							</tr>
						</c:forEach>

					</c:if>
					<c:if test="${noOfSpecimens==0}">
					&nbsp;
					</c:if>
				</tr>

				<tr>

					<c:if test="${noOfContainers>0}">

						<c:forEach var="containerNumber" begin="1" end="${noOfContainers}">
							<jsp:useBean id="containerNumber" type="java.lang.Integer" />
							<tr>

								<td class="black_ar" colspan="2"><label for="tissueSite">
								<bean:message key="shipment.contentsContainerLabel"
									bundle="msg.shippingtracking" /> </label></td>
								<td class="black_ar" colspan="2"></td>
								<td class="black_ar" colspan="2"><c:set
									var="labelNameContainer">containerDetails(containerLabel_<c:out
										value="${containerNumber}" />)</c:set> <jsp:useBean
									id="labelNameContainer" type="java.lang.String" /> <html:hidden
									property="<%=labelNameContainer%>" /> <%=shipmentForm.getContainerDetails("containerLabel_"+containerNumber)%>
								</td>
								<td class="black_ar" colspan="2"><c:set
									var="barcodeContainer">containerDetails(containerBarcode_<c:out
										value="${containerNumber}" />)</c:set> <jsp:useBean
									id="barcodeContainer" type="java.lang.String" /> <html:hidden
									property="<%=barcodeContainer%>" /> <%=shipmentForm.getContainerDetails("containerBarcode_"+containerNumber)%>
								</td>
							</tr>
						</c:forEach>

					</c:if>
					<c:if test="${noOfContainers==0}">
						&nbsp;
					</c:if>

				</tr>

			</table>
		</html:form></td>
	</tr>

	<tr>
		<td class="whitetable_bg" width="5">&nbsp;</td>
	</tr>
	<tr>
		<td height="30" class="buttonbg"><html:button
			property="editRequest" styleId="editRequest" styleClass="blue_ar_b"
			accesskey="A" onclick="editShipment()">Edit</html:button> &nbsp;<img
			src="images/shippingtracking/bl_sep.gif" width="1" height="19"
			align="absmiddle">&nbsp; <html:button property="dashbaord"
			styleId="dashbaord" styleClass="blue_ar_b" accesskey="A"
			onclick="viewDashboard()">
			<bean:message key="buttons.viewDashboard"
				bundle="msg.shippingtracking" />
		</html:button> &nbsp;<img src="images/shippingtracking/bl_sep.gif" width="1"
			height="19" align="absmiddle">&nbsp; <html:button
			property="printShip" styleId="printShip" styleClass="blue_ar_b"
			accesskey="A" onclick="printShipment()">
			<bean:message key="buttons.print" />
		</html:button></td>
	</tr>
</table>
</body>
</html>
