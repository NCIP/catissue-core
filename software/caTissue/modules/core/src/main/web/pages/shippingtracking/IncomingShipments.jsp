<script>
	function openIncomingShip(id)
	{
		document.forms[0].action="st_ShowShipmentReceving.do?id=" + id + "&fromPage=dashboard&showRequest=showRequest";
		document.forms[0].submit();
	}
</script>

<%@ page import="java.util.ArrayList"%>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<logic:notEmpty name="incomingShipmentsList" scope="request">
					<c:set var="incomingShipmentsHeader" value="${requestScope.incomingShipmentsHeader}"/>
					<jsp:useBean id="incomingShipmentsHeader" type="java.util.ArrayList"/>
						<tr class="">
							<td align="right" class="blacknormalText" colspan="<%=incomingShipmentsHeader.size()%>">
								Page
													<c:set var="incomingShipTotalPages" value="${sessionScope.incomingShipTotalPages}"/>
													<jsp:useBean id="incomingShipTotalPages" type="java.lang.Integer"/>
													<c:forEach var="incomingReqPageCounter" begin="1" end="${incomingShipTotalPages}">
														<c:set var="incomingReqsLinkURL">
															ShowDashboardAction.do?menuSelected=1&requestFor=incomingShipNextPage&incomingShipCurrentPageNo=<c:out value="${incomingReqPageCounter}"/>
														</c:set>
														<jsp:useBean id="incomingReqsLinkURL" type="java.lang.String"/>
														<c:if test="${incomingShipCurrentPageNo == incomingReqPageCounter}">
																<c:out value="${incomingReqPageCounter}"/>
														</c:if>
														<c:if test="${incomingShipCurrentPageNo != incomingReqPageCounter}">
															<a class="dataPagingLink" href="<%=incomingReqsLinkURL%>">
																<c:out value="${incomingReqPageCounter}"/>
															</a>
														</c:if>
														<c:if test="${incomingReqPageCounter != incomingShipTotalPages}">
																|
														</c:if>
													</c:forEach>
							</td>
						</tr>
						<tr class="tableheading">

							<c:forEach var="incomingShipHeaderColumn" items="${incomingShipmentsHeader}">
								<td height="25" align="left" valign="middle">
									<span class="black_ar_b"><c:out value="${incomingShipHeaderColumn}"/></span>
								</td>
							</c:forEach>
						</tr>

						<c:set var="incomingShipUserNameIndex" value="${requestScope.incomingShipUserNameIndex}"/>

						<c:set var="incomingShipmentsList" value="${requestScope.incomingShipmentsList}"/>
						<jsp:useBean id="incomingShipmentsList" type="java.util.ArrayList"/>

						<c:forEach var="incomingShipmentsRow" items="${incomingShipmentsList}">
						<jsp:useBean id="incomingShipmentsRow" type="java.lang.Object[]"/>
								<tr>
									<c:set var="incomingShipColCount" value="<%=incomingShipmentsRow.length%>"/>
									<jsp:useBean id="incomingShipColCount" type="java.lang.Integer"/>

									<c:set var="incomingShipOpenLink">javascript:openIncomingShip(<c:out value="${incomingShipmentsRow[identifierFieldIndex]}"/>)</c:set>
									<jsp:useBean id="incomingShipOpenLink" type="java.lang.String"/>

									<c:forEach var="index" begin="2" end="${incomingShipColCount}">
										<td align="left" valign="top" class="black_ar">
											<label>
												<c:if test="${index==incomingShipUserNameIndex}">
													<a href="<%=incomingShipOpenLink%>"><c:out value="${incomingShipmentsRow[index]}"/> <c:out value="${incomingShipmentsRow[1]}"/></a>
												</c:if>
												<c:if test="${index!=incomingShipUserNameIndex}">
													<a href="<%=incomingShipOpenLink%>"><c:out value="${incomingShipmentsRow[index]}"/></a>
												</c:if>
											</label>
										</td>
									</c:forEach>
								</tr>
						</c:forEach>
				</logic:notEmpty>
				<logic:empty name="incomingShipmentsList" scope="request">
					<tr>
						<td class="blacknormalText">
							<bean:message key="incoming.shipmentList.empty" bundle="msg.shippingtracking"/>
						</td>
					</tr>
				</logic:empty>
</table>