<%@ page import="java.util.ArrayList"%>

<script>
	function reqSentShip(id)
	{
		document.forms[0].action="OutgoingShipmentRequest.do?id="+id;
		document.forms[0].submit();
	}
</script>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<logic:notEmpty name="requestsSentList" scope="request">
					<c:set var="requestsSentHeader" value="${requestScope.requestsSentHeader}"/>
					<jsp:useBean id="requestsSentHeader" type="java.util.ArrayList"/>
						<tr class="">
							<td align="right" class="blacknormalText" colspan="<%=requestsSentHeader.size()%>">Page 
											<c:set var="reqSentTotalPages" value="${sessionScope.reqSentTotalPages}"/>  
											<jsp:useBean id="reqSentTotalPages" type="java.lang.Integer"/>

											<c:forEach var="reqSentPageCounter" begin="1" end="${reqSentTotalPages}">
												<c:set var="reqSentLinkURL">
													ShowDashboardAction.do?menuSelected=1&requestFor=reqSentpNextPage&reqSentCurrentPageNo=<c:out value="${reqSentPageCounter}"/>
												</c:set>
												<jsp:useBean id="reqSentLinkURL" type="java.lang.String"/>
												<c:if test="${reqSentCurrentPageNo == reqSentPageCounter}">
													<c:out value="${reqSentPageCounter}"/> 
												</c:if>
												<c:if test="${reqSentCurrentPageNo != reqSentPageCounter}">
													<a class="dataPagingLink" href="<%=reqSentLinkURL%>"> 
														<c:out value="${reqSentPageCounter}"/> 
													</a>
												</c:if>
												<c:if test="${reqSentPageCounter != reqSentTotalPages}">
													 |
												</c:if>
											</c:forEach>
							</td>
						</tr>
						
						<tr class="tableheading">
							<c:forEach var="reqSentHeaderColumn" items="${requestsSentHeader}">
								<td height="25" align="left" valign="middle">
									<span class="black_ar_b"><c:out value="${reqSentHeaderColumn}"/></span>
								</td>
							</c:forEach>
						</tr>

						<c:set var="requestsSentList" value="${requestScope.requestsSentList}"/>

						<c:set var="requestsSentList" value="${requestScope.requestsSentList}"/>
						<jsp:useBean id="requestsSentList" type="java.util.ArrayList"/>
						
						<c:forEach var="reqSentRow" items="${requestsSentList}">
						<jsp:useBean id="reqSentRow" type="java.lang.Object[]"/>
								<tr>
									<c:set var="reqSentColCount" value="<%=reqSentRow.length%>"/>
									<jsp:useBean id="reqSentColCount" type="java.lang.Integer"/>
									
									<c:set var="reqSentOpenLink">javascript:reqSentShip(<c:out value="${reqSentRow[identifierFieldIndex]}"/>)</c:set>
									<jsp:useBean id="reqSentOpenLink" type="java.lang.String"/>

									<c:forEach var="reqSentIndex" begin="2" end="${reqSentColCount}">
										<td align="left" valign="top" class="black_ar">
											<label>
												<c:choose>
													<c:when test="${reqSentIndex==sentReqUserNameIndex}">
														<a href="<%=reqSentOpenLink%>"><c:out value="${reqSentRow[reqSentIndex]}"/> <c:out value="${reqSentRow[1]}"/></a>
													</c:when>
												
													<c:when test="${(reqSentIndex==sentReqReceiverSiteNameIndex) && (reqSentRow[sentReqActivityStatusIndex] == 'Drafted')}">
														<bean:message key="blank.cloumn" bundle="msg.shippingtracking"/>	
													</c:when>

													<c:otherwise>
														<a href="<%=reqSentOpenLink%>"><c:out value="${reqSentRow[reqSentIndex]}"/></a>
													</c:otherwise>
												</c:choose>
											</label>
										</td>
									</c:forEach>
								</tr>
						</c:forEach>
				</logic:notEmpty>

				<logic:empty name="requestsSentList" scope="request">
					<tr>
						<td class="blacknormalText">
							<bean:message key="outgoing.shipmentRequestList.empty" bundle="msg.shippingtracking"/>
						</td>
					</tr>
				</logic:empty>

</table>