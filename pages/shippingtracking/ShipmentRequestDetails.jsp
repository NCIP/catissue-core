<!-- For Calendar Component -->
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>	
<%@ page import = "edu.wustl.common.util.global.CommonServiceLocator" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.Utility" %>

<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

		<table width="100%">
			<tr class="tableheading">
            <td height="25" colspan="2" align="left"><b>&nbsp;&nbsp;Shipment Request details </b></td>
          </tr>
          <tr>
            <td height="111" colspan="2" align="left" class="showhide"><table border="0" cellpadding="3" cellspacing="2">
	              <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.label" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar">
                    	<logic:equal name="operation" value="view">
                    		<c:out value="${shipmentRequestForm.label}"/>
                    		<html:hidden styleId="label" property="label"/>
                    	</logic:equal>
						<logic:equal name="operation" value="viewNonEditable">
						 	<c:out value="${shipmentRequestForm.label}"/>
                    		<html:hidden styleId="label" property="label"/>
                    	</logic:equal>

						 <c:if test="${operation=='add' or operation=='edit'}">
							<html:text styleClass="black_ar" size="29" styleId="label" property="label"/>
						</c:if>
                    </td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.request.requesterSite" bundle="msg.shippingtracking"/>
                    </td>
                    <td align="left" valign="top" class="black_ar"><label>
                    	 <c:if test="${operation=='view' or operation=='viewNonEditable'}">
						   	<c:out value="${requestScope.senderSiteName}"/>
	                    	<html:hidden styleId="senderSiteId" property="senderSiteId"/>
	                    </c:if>	
						<c:if test="${operation=='add' or operation=='edit'}">   
							<c:set var="senderSiteId" value="${shipmentRequestForm.senderSiteId}"/>
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
						 </c:if>
                    </label></td>
                  </tr>
                  <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b"><img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></span></td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="shipment.sendDate" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar"><label>
                    	  <c:if test="${operation=='add' or operation=='edit'}">  
	                    	<%
							 if(shipmentDate!=null && shipmentDate.trim().length() > 0)
							{
									Integer year = new Integer(Utility.getYear(shipmentDate ));
									Integer month = new Integer(Utility.getMonth(shipmentDate ));
									Integer day = new Integer(Utility.getDay(shipmentDate ));
						%>
									<ncombo:DateTimeComponent name="sendDate"
															  id="sendDate"
						 									  formName="shipmentRequestForm"	
															  month= "<%=month %>"
															  year= "<%=year %>"
															  day= "<%= day %>" 
															    pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
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
						 									  formName="shipmentRequestForm"	
						 									  month= "<%=month %>"
															  year= "<%=year %>"
															  day= "<%= day %>" 
															    pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
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
															    pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
						 									  formName="shipmentRequestForm"	
															  styleClass="black_ar" 
																	 />		
								
						<%
								}
							}
						%>
							<span class="black_ar">
									<c:set var="sendTimeHour" value="${shipmentRequestForm.sendTimeHour}"/>
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
									<c:set var="sendTimeMinutes" value="${shipmentRequestForm.sendTimeMinutes}"/>
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
				  		</c:if>
				  		 <c:if test="${operation=='view' or operation=='viewNonEditable'}">
				  			<html:hidden styleId="sendDate" property="sendDate"/>
				  			<html:hidden styleId="sendTimeHour" property="sendTimeHour"/>
				  			<html:hidden styleId="sendTimeMinutes" property="sendTimeMinutes"/>
				  			<c:set var="sendDate"><c:out value="${shipmentRequestForm.sendDate}"/> <c:out value="${shipmentRequestForm.sendTimeHour}"/>:<c:out value="${shipmentRequestForm.sendTimeMinutes}"/></c:set>
				  			<c:out value="${sendDate}"/>
						</c:if>
                    </label></td>
                    <td width="20" align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top"><span class="blue_ar_b"></span></td>
                    <td align="left" valign="top" class="black_ar">&nbsp;</td>
                    <td align="left" valign="top">&nbsp;</td>
                  </tr>
                  <tr>
                    <td align="center" valign="top" class="black_ar"><span class="blue_ar_b">&nbsp;</span></td>
                    <td align="left" valign="top" nowrap class="black_ar"><bean:message key="shipment.senderComments" bundle="msg.shippingtracking"/></td>
                    <td align="left" valign="top" class="black_ar">
				    	 <c:if test="${operation=='view' or operation=='viewNonEditable'}">
                    		<html:hidden styleId="senderComments" property="senderComments"/>
	                    	<c:out value="${shipmentRequestForm.senderComments}"/>
	                    </c:if>
	                    <c:if test="${operation=='add' or operation=='edit'}"> 
	                    	<html:textarea styleClass="black_ar" rows="2" cols="50" styleId="senderComments" property="senderComments"/>
	                    </c:if>
					</td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top" class="black_ar">&nbsp;</td>
                    <td align="left" valign="top"><label>&nbsp;</label></td>
                  </tr>
              </table>