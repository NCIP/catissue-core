<!-- For Calendar Component -->
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>		
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

		<table width="100%">
			<tr class="tableheading">
            <td height="25" colspan="2" align="left"><b>&nbsp;&nbsp;<bean:message key="shipment.details" bundle="msg.shippingtracking"/></b></td>
          </tr>
          <tr>
            <td height="111" colspan="2" align="left" class="showhide"><table border="0" cellpadding="3" cellspacing="2">
                  <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.label" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar">
                    	<logic:equal name="operation" value="view">
	                    	<c:out value="${shipmentForm.label}"/>
	                    	<html:hidden styleId="label" property="label"/>
	                    </logic:equal>
	                    <logic:notEqual name="operation" value="view">
	                    	<html:text styleClass="black_ar" size="29" styleId="label" property="label"/>
	                    </logic:notEqual>
                    </td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top" class="black_ar">
                      <bean:message key="shipment.barcode" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar">
                       	<logic:equal name="operation" value="view">
	                    	<c:out value="${shipmentForm.barcode}"/>
	                    	<html:hidden styleId="barcode" property="barcode"/>
	                    </logic:equal>

	                    <logic:equal name="operation" value="add">
	                    	<html:text styleClass="black_ar" size="29" styleId="barcode" property="barcode"/>
                    	</logic:equal>

					  <logic:equal name="operation" value="edit">
                       <logic:equal name="shipmentForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
						    <c:out value="${shipmentForm.barcode}"/>
	                    	<html:hidden styleId="barcode" property="barcode"/>							
	                    </logic:equal>

                         <logic:equal name="shipmentForm" property="isBarcodeEditable" value="<%=Constants.TRUE%>">
	                    	<html:text styleClass="black_ar" size="29" styleId="barcode" property="barcode"/>
	                    </logic:equal>
					 </logic:equal> 
                    </td>
                  </tr>
                  <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.senderSite" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar" class="black_ar"><label>	
                    	<logic:equal name="operation" value="view">
	                    	<c:out value="${requestScope.senderSiteName}"/>
	                    	<html:hidden styleId="senderSiteId" property="senderSiteId"/>
	                    </logic:equal>
	                    <logic:notEqual name="operation" value="view">		   
							<c:set var="senderSiteId" value="${shipmentForm.senderSiteId}"/>
							<c:set var="initialSiteSelected" value="${requestScope.initialSiteSelected}"/>
							<jsp:useBean id="initialSiteSelected" type="java.lang.String"/>
							<logic:notEmpty name="senderSiteId">
									<jsp:useBean id="senderSiteId" type="java.lang.Long"/>

									<logic:equal name="senderSiteId" value="0">
											<autocomplete:AutoCompleteTag
											property="senderSiteId"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.shippingtracking.Constants.SENDERS_SITE_LIST)%>"
											initialValue="<%=initialSiteSelected%>"
											styleClass="black_ar"
											staticField="false" size="27" />
									</logic:equal>

									<logic:notEqual name="senderSiteId" value="0">
											<autocomplete:AutoCompleteTag
											property="senderSiteId"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.shippingtracking.Constants.SENDERS_SITE_LIST)%>"
											initialValue="<%=senderSiteId%>"
											styleClass="black_ar"
											staticField="false" size="27" />
									</logic:notEqual>									
							</logic:notEmpty>
							<logic:empty name="senderSiteId">
										<autocomplete:AutoCompleteTag
										property="senderSiteId"
										optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.shippingtracking.Constants.SENDERS_SITE_LIST)%>"
										initialValue="<%=initialSiteSelected%>"
										styleClass="black_ar"
										staticField="false" size="27" />
							</logic:empty>
						 </logic:notEqual>
						</label>
					</td>
                    <td width="20" align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.receiverSite" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar">
                    	<logic:equal name="operation" value="view">
	                    	<c:out value="${requestScope.receiverSiteName}"/>
	                    	<html:hidden styleId="receiverSiteId" property="receiverSiteId"/>
	                    </logic:equal>
	                    <logic:notEqual name="operation" value="view">	
								<c:set var="receiverSiteId" value="${shipmentForm.receiverSiteId}"/>
								<logic:notEmpty name="receiverSiteId">
										<jsp:useBean id="receiverSiteId" type="java.lang.Long"/>
										<autocomplete:AutoCompleteTag
											property="receiverSiteId"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.shippingtracking.Constants.RECIEVERS_SITE_LIST)%>"
											initialValue="<%=receiverSiteId%>"
											styleClass="black_ar"
											staticField="false" size="27" />
								</logic:notEmpty>
								<logic:empty name="receiverSiteId">
											<autocomplete:AutoCompleteTag
											property="receiverSiteId"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.shippingtracking.Constants.RECIEVERS_SITE_LIST)%>"
											initialValue=''
											styleClass="black_ar"
											staticField="false" size="27" />
								</logic:empty>
						</logic:notEqual>
                    </td>
                  </tr>
                  <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.sendDate" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar"><label>
                    	<logic:equal name="operation" value="view">
	                    	<c:set var="sendDate"><c:out value="${shipmentRequestForm.sendDate}"/> <c:out value="${shipmentRequestForm.sendTimeHour}"/>:<c:out value="${shipmentRequestForm.sendTimeMinutes}"/></c:set>
				  			<c:out value="${sendDate}"/>
				  			<html:hidden styleId="sendDate" property="sendDate"/>
				  			<html:hidden styleId="sendTimeHour" property="sendTimeHour"/>
				  			<html:hidden styleId="sendTimeMinutes" property="sendTimeMinutes"/>
	                    </logic:equal>
	                    <logic:notEqual name="operation" value="view">	
                    	<%
							 if(shipmentDate!=null && shipmentDate.trim().length() > 0)
							{
									Integer year = new Integer(Utility.getYear(shipmentDate ));
									Integer month = new Integer(Utility.getMonth(shipmentDate ));
									Integer day = new Integer(Utility.getDay(shipmentDate ));
						%>
									<ncombo:DateTimeComponent name="sendDate"
															  id="sendDate"
						 									  formName="shipmentForm"	
															  month= "<%=month %>"
															  year= "<%=year %>"
															  day= "<%= day %>" 
															  pattern="<%=Variables.dateFormat%>"
															  value="<%=shipmentDate %>"
															  styleClass="black_ar"
																	 />		
						<% 
							}
							else
							{  
								String initialShippingDate=(String)request.getAttribute("initialShippingDate");
								if(initialShippingDate!=null && initialShippingDate.trim().length()>0)
								{
									Integer year = new Integer(Utility.getYear(initialShippingDate ));
									Integer month = new Integer(Utility.getMonth(initialShippingDate ));
									Integer day = new Integer(Utility.getDay(initialShippingDate ));
						 %>
									<ncombo:DateTimeComponent name="sendDate"
															  id="sendDate"
						 									  formName="shipmentForm"	
						 									  month= "<%=month %>"
															  year= "<%=year %>"
															  day= "<%= day %>" 
															    pattern="<%=Variables.dateFormat%>"
															  value="<%=initialShippingDate%>"
															  styleClass="black_ar" 
																	 />		
								<%
								}
								else
								{
								%>
									<ncombo:DateTimeComponent name="sendDate"
															  id="sendDate"
						 									  formName="shipmentForm"
						 									  
						 									    pattern="<%=Variables.dateFormat%>"
															  styleClass="black_ar" 
																	 />		
								
						<%
								}
							}
						%>
						</logic:notEqual>
                    <span class="black_ar">
								<c:set var="sendTimeHour" value="${shipmentForm.sendTimeHour}"/>
								<logic:notEmpty name="sendTimeHour">
										<jsp:useBean id="sendTimeHour" type="java.lang.String"/>
										<autocomplete:AutoCompleteTag property="sendTimeHour"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.global.Constants.HOUR_LIST)%>"
											initialValue="<%=sendTimeHour%>"
											styleClass="black_ar"
											staticField="false" size="3" />
								</logic:notEmpty>
								<logic:empty name="sendTimeHour">
										<c:set var="initialShippingHour" value="${requestScope.initialShippingHour}"/>
										<jsp:useBean id="initialShippingHour" type="java.lang.String"/>

											<autocomplete:AutoCompleteTag property="sendTimeHour"
											optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.global.Constants.HOUR_LIST)%>"
											initialValue="<%=initialShippingHour%>"
											styleClass="black_ar"
											staticField="false" size="3" />
								</logic:empty>
								&nbsp;<bean:message key="shipment.send.time.hour" bundle="msg.shippingtracking"/>&nbsp;&nbsp;
								<c:set var="sendTimeMinutes" value="${shipmentForm.sendTimeMinutes}"/>
								<logic:notEmpty name="sendTimeMinutes">
										<jsp:useBean id="sendTimeMinutes" type="java.lang.String"/>
										<autocomplete:AutoCompleteTag property="sendTimeMinutes"
										   optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.global.Constants.MINUTES_LIST)%>"
										   initialValue="<%=sendTimeMinutes%>"
										   styleClass="black_ar"
										   staticField="false" size="3" />	  
								</logic:notEmpty>
								<logic:empty name="sendTimeMinutes">
										<c:set var="initialShippingmMinute" value="${requestScope.initialShippingmMinute}"/>
										<jsp:useBean id="initialShippingmMinute" type="java.lang.String"/>

											<autocomplete:AutoCompleteTag property="sendTimeMinutes"
										   optionsList="<%=request.getAttribute(edu.wustl.catissuecore.util.global.Constants.MINUTES_LIST)%>"
										   initialValue="<%=initialShippingmMinute%>"
										   styleClass="black_ar"
										   staticField="false" size="3" />	  
								</logic:empty>

			                
			   &nbsp;<bean:message key="shipment.send.time.minute" bundle="msg.shippingtracking"/></span>
                    </label></td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.senderComments" bundle="msg.shippingtracking"/>
                    </td>
                    <td align="left" valign="top" class="black_ar">
                    	<logic:equal name="operation" value="view">
				  			<c:out value="${shipmentForm.senderComments}"/>
				  			<html:hidden styleId="senderComments" property="senderComments"/>
	                    </logic:equal>
	                    <logic:notEqual name="operation" value="view">	
                    		<html:textarea styleClass="black_ar" rows="2" cols="50" styleId="senderComments" property="senderComments"/>
                   		</logic:notEqual>
                    </td>
                  </tr>
              </table>