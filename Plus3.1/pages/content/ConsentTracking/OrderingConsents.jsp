<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>			
<html>
	<head>
		<title></title>
			<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
			<script src="jss/script.js" type="text/javascript"></script>
			<script src="jss/overlib_mini.js" type="text/javascript"></script>
	</head>
	<body>		
			<%-- Set Variables according to the pages --%>
			<%-- Main table Start --%>
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
			<tr>
			<td>
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
			<tr>
			<td>
			<div style="background-color: #ffffff;overflow: auto;">
		<!--	<div width="600px" style="oveflow:auto;display:block" id="consent">-->
			
			<table width="100%" border="0" cellpadding="3" cellspacing="0" id="table4" >
				<c:set var="consentExist" value='false' scope="page"/>
				<c:forEach var="consentDTO" items="${requestScope.ConsentDTOs}">
				<c:set var="consentExist" value='true' scope="page"/>
				<tr>
					<td class="showhide">
						<table cellpadding="3" cellspacing="0" border="0" width="100%" id="table5">
							
							<tr>
								<td>
								</td>
								<td width="7%" class="black_ar align_right_style" width="35%" style="padding:10px;">
									<strong>
									<bean:message key="orderDistribution.consent.barcodelable"/></strong>
								</td>
								<td width="1%">
								</td>
								<td class="black_ar" >
									&nbsp;&nbsp;<textarea disabled cols="130" rows="2"><c:forEach var="specimenLabel" items="${consentDTO.specimenLabels}">${specimenLabel}  </c:forEach></textarea>
							   </td>
							</tr>
							
						</table>
					</td>
				</tr>			
				<%-- Inner table that will show Consents Start--%>								
				<tr>
					<td>
						<table cellpadding="3" cellspacing="0" border="0" width="100%" id="consentTable">
							<%-- Serial No # --%>	
							<tr>
								<td class="tableheading">
									<div align="left">
										<bean:message key="requestlist.dataTabel.serialNo.label" />
									</div>
								</td>
								<%-- Title ( Consent Tiers) --%>									
								<td class="tableheading">
									<div>	
										<bean:message key="collectionprotocolregistration.consentTiers" />
									</div>	
								</td>
								<%--Title (Participant response) --%>										
								
								<td class="tableheading">
									<div align="left">
										<bean:message key="consent.responsestatus" />
									</div>
								</td>
							</tr>
							<%-- Get Consents and Responses from DB --%>	
							<!-- Setting the local variable count for the row counting for setting row color-->
							<c:set var="count" value='1' scope="page"/>
							<%-- For loop Start --%>	
							<c:set var="style" value="black_ar" scope="page" />	
							
							<%-- Serial No # --%>

						<c:forEach var="consentTierDTO" items="${consentDTO.consentTierDTOCollection}">						
							<tr>

								<td class='${pageScope.style}' width="3%">
									${count}
								</td>
								<%-- Get Consents # --%>										
								<td class='${pageScope.style}' width="40%">
									${consentTierDTO.consentStatment}
								</td>
								
								<td align="left" class='${pageScope.style}'>
									${consentTierDTO.status}
								</td>
							</tr>
							<c:set var="count" value='${pageScope.count+1}' scope="page" />
							</c:forEach>
							 <tr>
                                 <td class="black_ar " colspan="3">
                                     <input type="checkbox" name="verifyAllCheckBox"/>
                                     <label><b><bean:message key="consent.verificationmessage"/><b></label>
                                 </td>
                             </tr>
							
						</table>	
						<%-- Inner table that will show Consents--%>
					</td>	
				</tr>
				<%-- action button --%>	
				  </tr>
				</c:forEach>
					<tr>
				     <c:if test="${consentExist=='false'}">						
				      <td class="black_ar ">
					 <label><b><bean:message key="orderDistribution.consent.does.not.exists"/><b></label>
				      </td>		
				     </c:if>
				   </tr>		
				   
				   <tr>
          <td class="buttonbg" style="padding:6px;">
					<input type="button" class="blue_ar_b" value="Submit" onclick="window.parent.submitOrderNew(document.getElementsByName('verifyAllCheckBox'))" accesskey="Enter">
					
			</td>
		</tr>
				</table>
				
				</div>
				
				</td>
				</tr>
				
			</table>
			</td>
			</tr>
			</table>
			<%-- Main table End --%>
	</body>
</html>