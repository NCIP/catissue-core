<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<body>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
				<div style="margin-top:2px;">
					 Event Details "Distribution Event"
				</div>
			</span>
		  </td>
		</tr>
		<tr><td></td></tr>
		<tr>
			<td>
			
				<table width="100%" border="0" cellspacing="0" cellpadding="4">
					<%--Signed URL --%>				
					<tr>
						<td class="noneditable" width="30%">
						<bean:message key="eventparameters.distributed.by" />
							
						</td>
						<td class="noneditable">
							${distEventDTO.user}
						</td>
					</tr>
					<tr>
						<td class="noneditable" width="30%">
							<bean:message key="eventparameters.distributed.on" />
						</td>
						<td class="noneditable">
						
						<fmt:formatDate value="${distEventDTO.eventDate}" pattern="${datePattern}" />
						<fmt:formatDate type="time" value="${distEventDTO.eventDate}" pattern="HH" />:
						<fmt:formatDate type="time" value="${distEventDTO.eventDate}" pattern="m"/>
						</td>
					</tr>
					<tr>
						<td class="noneditable" width="30%">
							<bean:message key="eventparameters.distributed.quantity"/>
						</td>
						<td class="noneditable">
							${distEventDTO.distributedQuantity}
						</td>
					</tr>
					<tr>
						<td class="noneditable" width="30%">
							<bean:message key="eventparameters.comments"/>
						</td>
						<td class="noneditable">
							${distEventDTO.comment}
						</td>
					</tr>
					
					
					
				</table>
			</td>
		</tr>
		
			
			
		
</table>

</body>