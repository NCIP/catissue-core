<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- Main Ordering jsp which include OrderItem.jsp, OrderBiospecimenArray.jsp, orderPathologyCase.jsp, OrderList.jsp-->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
		        <td class="td_table_head">
					<span class="wh_ar_b"><bean:message key="orderingSystem.header" />
					</span>
				</td>
		        <td align="right">
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Biospecimen Order" width="31" height="24" />
				</td>
		    </tr>
	    </table>
	</td>
  </tr>
  <tr>
    <td class="tablepadding">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
	        <tr>
		      <td align="left" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="75%" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
						      <tr>
								<td valign="top">
									<table border="0" cellspacing="0" cellpadding="0" width="100%" valign="top">
										<tr>
											<td>
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
								                        <td><img src="images/uIEnhancementImages/no1.gif" alt="Number1" width="25" height="21"></td>
								                        <td class="black_ar" style="border-bottom:1px solid #bababa;">&nbsp;<bean:message key="orderingSystem.createOrder" />&nbsp;</td>
								                        <td><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="30" height="1"></td>
								                        <td><img src="images/uIEnhancementImages/no2A.gif" alt="Number2" width="25" height="21"></td>
								                        <td class="black_ar" style="border-bottom:1px solid #f47c28;">&nbsp;<strong><bean:message key="requestdetails.name" /></strong> &nbsp; </td>
								                     </tr>
							                    </table>
											</td>
										</tr>
										<tr>
											<td  width="100%" valign="top" >
						<%
									if(request.getAttribute("typeOf").equals("specimen"))
						{%>
								<!-- OrderItem.jsp -->
												<tiles:insert definition=".catissuecore.orderingsystem.orderItem" />
			
						<%}%>
	
								<!-- Specimen Array page-->
	
						<%			if(request.getAttribute("typeOf").equals("specimenArray"))
						{%>
			
												<tiles:insert definition=".catissuecore.orderingsystem.orderbiospecimenarray" />
			
						<%}%>
	
								<!-- Pathology Case page-->
	
						<%			if(request.getAttribute("typeOf").equals("pathologyCase"))
						{%>
							
												<tiles:insert definition=".catissuecore.orderingsystem.orderpathologycase"/>
			
						<%}%>
											</td>
										</tr>
									</table>
								</td>
								<td valign="top">
									<img src="images/spacer.gif" alt="spacer" width="10" height="1">
								</td>
					            <td width="25%" valign="top">
						<!-- Order List page -->
			
			 						<tiles:insert definition=".catissuecore.orderingsystem.orderList" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<!------------End------------------------------------>
