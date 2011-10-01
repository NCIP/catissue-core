<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm "%>
<%@ page import="edu.wustl.common.util.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.HashMap,edu.wustl.common.beans.SessionDataBean"%>
<%@ page language="java" isELIgnored="false"%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" media="screen" />
<link href="css/addl_s_t.css" rel="stylesheet" type="text/css">

<%
	Object obj=request.getAttribute("shipmentForm");
	ShipmentForm shipmentForm = null;
	if(obj instanceof ShipmentForm)
	{
		shipmentForm =(ShipmentForm)obj;		
	}
%>

<table width="100%" border="0" cellpadding="5" cellspacing="0"
	class="whitetable_bg">
		<tr class="tableheading">

					<td height="25" width="25%" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.contents" bundle="msg.shippingtracking" /></span></td>
					<td height="25" width="25%" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.description" bundle="msg.shippingtracking" /></span></td>
					<td height="25" width="25%" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.print.Label" bundle="msg.shippingtracking" /></span></td>
					<td height="25" width="25%" align="left" valign="middle"
						class="tr_bg_blue1"><span class="blue_ar_b"><bean:message
						key="shipment.print.barcode" bundle="msg.shippingtracking" /></span></td>

				</tr>
			<tr>

				<c:if test="${shipmentForm.specimenCounter>0}">
					<c:forEach var="specimenNumber" begin="1"
						end="${shipmentForm.specimenCounter}">

						<jsp:useBean id="specimenNumber" type="java.lang.Integer" />
						<tr>

							<td class="black_ar" width="25%"><label for="tissueSite">
							<bean:message key="shipment.contentsSpecimenLabel"
								bundle="msg.shippingtracking" /> </label></td>
							<td class="black_ar" width="25%"></td>

							<td class="black_ar" width="25%">
							<%
							   String parameterLbl = "specimenLabel_"+specimenNumber;						  
							%> <%=shipmentForm.getSpecimenDetails(parameterLbl)%></td>
							<td class="black_ar" width="25%">
							<%
							   String parameter = "specimenBarcode_"+specimenNumber;						  
							%> <%=shipmentForm.getSpecimenDetails(parameter)%></td>
						</tr>
					</c:forEach>

				</c:if>
				<c:if test="${shipmentForm.specimenCounter==0}">
		&nbsp;
	</c:if>
			</tr>
			<tr>

				<c:if test="${shipmentForm.containerCounter>0}">

					<c:forEach var="containerNumber" begin="1"
						end="${shipmentForm.containerCounter}">
						<jsp:useBean id="containerNumber" type="java.lang.Integer" />
						<tr>

							<td class="black_ar" width="25%"><label for="tissueSite">
							<bean:message key="shipment.contentsContainerLabel"
								bundle="msg.shippingtracking" /> </label></td>
							<td class="black_ar" width="25%"></td>
							<td class="black_ar" width="25%">
							<%
						   String containerParameterLbl = "containerLabel_"+containerNumber;						  
					%> <%=shipmentForm.getContainerDetails(containerParameterLbl)%></td>
							<td class="black_ar" width="25%">
							<%
						   String containerParameter = "containerBarcode_"+containerNumber;						  
					%> <%=shipmentForm.getContainerDetails(containerParameter)%></td>
						</tr>
					</c:forEach>

				</c:if>
				<c:if test="${shipmentForm.containerCounter==0}">
		&nbsp;
	</c:if>
			</tr>
		</table>
		
