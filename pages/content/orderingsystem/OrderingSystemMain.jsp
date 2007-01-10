<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" style="height:100%">
	<tr valign="top">
			<!-- Specimen page-->
			<%if(request.getAttribute("typeOf").equals("specimen"))
			{%>
			<td  width="70%">
				<tiles:insert definition=".catissuecore.orderingsystem.orderItem" />
			</td>
			<%}%>
	
			<!-- Specimen Array page-->
	
			<%if(request.getAttribute("typeOf").equals("specimenArray"))
			{%>
			<td  width="70%">
				<tiles:insert definition=".catissuecore.orderingsystem.orderbiospecimenarray" />
			</td>
			<%}%>
	
			<!-- Pathology Case page-->
	
			<%if(request.getAttribute("typeOf").equals("pathologyCase"))
			{%>
			<td  width="70%">
				<tiles:insert definition=".catissuecore.orderingsystem.orderpathologycase"/>
			</td>
			<%}%>
			
			<!-- Order List page -->
			<td width="30%">
			 	<tiles:insert definition=".catissuecore.orderingsystem.orderList" />
			</td>
	</tr>
</table>

 

