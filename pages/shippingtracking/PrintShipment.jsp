<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm "%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
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

<table width="100%" border="0"  cellpadding="5" cellspacing="0" class="whitetable_bg">
															
		 <tr>
			
		    <td class="black_ar" width="13%">
					<label for="tissueSite">
						<bean:message key="shipment.label" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar" width="13%">
				<c:out value="${shipmentForm.label}"/>			
			</td>
			</tr>


			<tr>
		
			 <td class="black_ar" width="13%">
				
					<label for="sName">
						<bean:message key="shipment.senderName" bundle="msg.shippingtracking"/>
						
					</label> 
			  </td>


			<td class="black_ar" width="30%">
					<c:out value="${shipmentForm.senderName}"/>
			</td>
		
			
			<td class="black_ar"  width="13%">
					<label for="tissueSite">
						<bean:message key="shipment.request.senderComments" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar"  width="20%">
					
					<c:out value="${shipmentForm.senderComments}"/>&nbsp;					
			</td>
			</tr>



			<tr>
			
				<td class="black_ar" width="13%">
					<label for="tissueSite">
						<bean:message key="shipment.senderSite" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar"  width="30%">
					<c:out value="${shipmentForm.senderSiteName}"/>
			</td>
			
		
			<td class="black_ar"  width="13%">
					<label for="tissueSite">
						<bean:message key="shipment.barcode" bundle="msg.shippingtracking"/> 
					</label>
			</td>
			<td class="black_ar"  width="20%">
					<c:out value="${shipmentForm.barcode}"/>		
									
			</td>
	  </tr>


	<tr>
		
			<td class="black_ar" width="13%">
			
					<label for="sName">
						<bean:message key="shipment.senderPhone" bundle="msg.shippingtracking"/>
						
					</label> 
			</td>
			<td class="black_ar"  width="30%">
					<c:out value="${shipmentForm.senderPhone}"/>
			</td>

			
		
			<td class="black_ar"  width="13%">
					<label for="tissueSite">
							<bean:message key="shipment.request.sentOn" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar"  width="20%">
					<c:out value="${shipmentForm.sendDate}"/>		
								
			</td>

			</tr>

			<tr>
		
			<td class="black_ar" width="13%">
			
					<label for="tissueSite">
							<bean:message key="shipment.senderEmail" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar"  width="30%">
					<c:out value="${shipmentForm.senderEmail}"/>																			
			</td>
			
		
			<td class="black_ar" width="13%">
					<label for="tissueSite">
						<bean:message key="shipment.receiverSite" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar" width="20%">
				<c:out value="${shipmentForm.receiverSiteName}"/>
			</td>

			</tr>

			<tr>
		   
			<td class="black_ar" width="13%">
			
					<label for="rName">
						<bean:message key="shipment.receiverName" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar" width="30%">
				<c:out value="${shipmentForm.receiverSiteCoordinator}"/>
			</td>
					
			<td class="black_ar" width="13%">
			
					<label for="rName">
						<bean:message key="shipment.receiverPhone" bundle="msg.shippingtracking"/> 
					</label>
			</td>

			<td class="black_ar" width="20%">
				<c:out value="${shipmentForm.receiverSiteCoordinatorPhone}"/>
			</td>
			</tr>
			
	</table>
	
