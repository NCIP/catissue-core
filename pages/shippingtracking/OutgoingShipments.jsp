<%@ page import="java.util.ArrayList"%>

<script>
	function openOutgoingShip(id)
	{
		document.forms[0].action="OutgoingShipment.do?id="+id;
		document.forms[0].submit();
	}
</script>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<logic:notEmpty name="outgoingShipmentsList" scope="request">
					<c:set var="outgoingShipmentsHeader" value="${requestScope.outgoingShipmentsHeader}"/>
					<jsp:useBean id="outgoingShipmentsHeader" type="java.util.ArrayList"/>
						<tr class="">
							<td align="right" class="blacknormalText" colspan="<%=outgoingShipmentsHeader.size()%>">Page 
											<c:set var="outgoingShipTotalPages" value="${sessionScope.outgoingShipTotalPages}"/>  
											<jsp:useBean id="outgoingShipTotalPages" type="java.lang.Integer"/>

											<c:forEach var="outgoingShipPageCounter" begin="1" end="${outgoingShipTotalPages}">
												<c:set var="outgoingShipLinkURL">
													ShowDashboardAction.do?menuSelected=1&requestFor=outgoingShipNextPage&outgoingShipCurrentPageNo=<c:out value="${outgoingShipPageCounter}"/>
												</c:set>
												<jsp:useBean id="outgoingShipLinkURL" type="java.lang.String"/>
												<c:if test="${outgoingShipCurrentPageNo == outgoingShipPageCounter}">
													<c:out value="${outgoingShipPageCounter}"/> |
												</c:if>
												<c:if test="${outgoingShipCurrentPageNo != outgoingShipPageCounter}">
													<a class="dataPagingLink" href="<%=outgoingShipLinkURL%>"> 
														<c:out value="${outgoingShipPageCounter}"/> |
													</a>
												</c:if>
											</c:forEach>
							</td>
						</tr>
						
						<tr class="tableheading">
							<c:forEach var="headerColumn" items="${outgoingShipmentsHeader}">
								<td height="25" align="left" valign="middle">
									<span class="black_ar_b"><c:out value="${headerColumn}"/></span>
								</td>
							</c:forEach>
						</tr>

						<c:set var="outgoingShipUserNameIndex" value="${requestScope.outgoingShipUserNameIndex}"/>

						<c:set var="outgoingShipmentsList" value="${requestScope.outgoingShipmentsList}"/>
						<jsp:useBean id="outgoingShipmentsList" type="java.util.ArrayList"/>
						
						<c:forEach var="outgoingShipmentsRow" items="${outgoingShipmentsList}">
						<jsp:useBean id="outgoingShipmentsRow" type="java.lang.Object[]"/>
								<tr>
									<c:set var="outgoingShipColCount" value="<%=outgoingShipmentsRow.length%>"/>
									<jsp:useBean id="outgoingShipColCount" type="java.lang.Integer"/>
									
									<c:set var="outgoingShipOpenLink">javascript:openOutgoingShip(<c:out value="${outgoingShipmentsRow[identifierFieldIndex]}"/>)</c:set>
									<jsp:useBean id="outgoingShipOpenLink" type="java.lang.String"/>

									<c:forEach var="index" begin="2" end="${outgoingShipColCount}">
										<td align="left" valign="top" class="black_ar">
											<label>
												<c:if test="${index==outgoingShipUserNameIndex}">
													<a href="<%=outgoingShipOpenLink%>"><c:out value="${outgoingShipmentsRow[index]}"/> <c:out value="${outgoingShipmentsRow[1]}"/></a>
												</c:if>
												<c:if test="${index!=outgoingShipUserNameIndex}">
													<a href="<%=outgoingShipOpenLink%>"><c:out value="${outgoingShipmentsRow[index]}"/></a>
												</c:if>
											</label>
										</td>
									</c:forEach>
								</tr>
						</c:forEach>
				</logic:notEmpty>

				<logic:empty name="outgoingShipmentsList" scope="request">
					<tr>
						<td class="black_ar">
							<bean:message key="outgoing.shipmentList.empty" bundle="msg.shippingtracking"/>
						</td>
					</tr>
				</logic:empty>

</table>