<%@ page import="java.util.ArrayList"%>

<script>
	function openReceivedReq(id)
	{
		document.forms[0].action="RequestReceived.do?id="+id;
		document.forms[0].submit();
	}
</script>

<table width="100%" border="0" cellpadding="3" cellspacing="0" valign="top">
				<logic:notEmpty name="requestsReceivedList" scope="request">
					<c:set var="requestsReceivedHeader" value="${requestScope.requestsReceivedHeader}"/>
					<jsp:useBean id="requestsReceivedHeader" type="java.util.ArrayList"/>
						<tr class="">
							<td align="right" class="blacknormalText" colspan="<%=requestsReceivedHeader.size()%>">
									Page 
													<c:set var="reqReceivedTotalPages" value="${sessionScope.reqReceivedTotalPages}"/>  
													<jsp:useBean id="reqReceivedTotalPages" type="java.lang.Integer"/>
													<c:forEach var="receivedReqsPageCounter" begin="1" end="${reqReceivedTotalPages}">
														<c:set var="receivedReqsLinkURL">
															ShowDashboardAction.do?menuSelected=1&requestFor=reqReceivedNextPage&reqReceivedCurrentPageNo=<c:out value="${receivedReqsPageCounter}"/>
														</c:set>
														<jsp:useBean id="receivedReqsLinkURL" type="java.lang.String"/>
														<c:if test="${reqReceivedCurrentPageNo == receivedReqsPageCounter}">
																<c:out value="${receivedReqsPageCounter}"/> |
														</c:if>
														<c:if test="${reqReceivedCurrentPageNo != receivedReqsPageCounter}">
															<a class="dataPagingLink" href="<%=receivedReqsLinkURL%>"> 
																<c:out value="${receivedReqsPageCounter}"/> 
															</a>
															<c:if test="${receivedReqsPageCounter != reqReceivedTotalPages}">
																|
														</c:if>
														</c:if>
													</c:forEach>
							</td>
						</tr>
						<tr class="tableheading">
							<c:forEach var="receivedShipmenytsheaderColumn" items="${requestsReceivedHeader}">
								<td height="25" align="left" valign="middle">
									<span class="black_ar_b"><c:out value="${receivedShipmenytsheaderColumn}"/></span>
								</td>
							</c:forEach>
						</tr>

						<c:set var="receivedReqUserNameIndex" value="${requestScope.receivedReqUserNameIndex}"/>

						<c:set var="requestsReceivedList" value="${requestScope.requestsReceivedList}"/>
						<jsp:useBean id="requestsReceivedList" type="java.util.ArrayList"/>
						
						<c:forEach var="requestsReceviedRow" items="${requestsReceivedList}">
						<jsp:useBean id="requestsReceviedRow" type="java.lang.Object[]"/>
								<tr>
									<c:set var="receivedShipColCount" value="<%=requestsReceviedRow.length%>"/>
									<jsp:useBean id="receivedShipColCount" type="java.lang.Integer"/>
									
									<c:set var="receivedReqsOpenLink">javascript:openReceivedReq(<c:out value="${requestsReceviedRow[identifierFieldIndex]}"/>)</c:set>
									<jsp:useBean id="receivedReqsOpenLink" type="java.lang.String"/>
									
									<c:forEach var="index" begin="2" end="${receivedShipColCount}">
										<td align="left" valign="top" class="black_ar">
											<label>
												<c:if test="${index==receivedReqUserNameIndex}">
													<a href="<%=receivedReqsOpenLink%>"><c:out value="${requestsReceviedRow[index]}"/> <c:out value="${requestsReceviedRow[1]}"/></a>
												</c:if>
												<c:if test="${index!=receivedReqUserNameIndex}">
													<a href="<%=receivedReqsOpenLink%>"><c:out value="${requestsReceviedRow[index]}"/></a>
												</c:if>
											</label>
										</td>
									</c:forEach>
								</tr>
						</c:forEach>
				</logic:notEmpty>
				<logic:empty name="requestsReceivedList" scope="request">
					<tr>
						<td class="black_ar">
							<bean:message key="incoming.shipmentRequestList.empty" bundle="msg.shippingtracking"/>
						</td>
					</tr>
				</logic:empty>
</table>