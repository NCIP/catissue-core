<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<html:form action="/CctsEventNotification">
		<tr>
			<td class="td_color_bfdcf3"><table border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td class="td_table_head" nowrap="nowrap"><span
							class="wh_ar_b"><bean:message bundle="msg.ccts"
									key="CctsEventNotification.header" /> </span></td>
						<td><img
							src="images/uIEnhancementImages/table_title_corner2.gif"
							width="31" height="24"
							alt="Page Title - CCTS Event Notification Details" /></td>
					</tr>
				</table></td>
		</tr>
		<tr>
			<td class="tablepadding"><table width="100%" border="0"
					cellpadding="0" cellspacing="0">
					<tr>
						<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					class="whitetable_bg">
					<tr>
						<td align="left" class="bottomtd"><%@ include
								file="/pages/content/common/ActionErrors.jsp"%></td>
					</tr>
					<c:if test="${notification!=null}">
						<tr>
							<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message
										bundle="msg.ccts" key="CctsEventNotification.title" /> </span></td>
						</tr>
						<tr>
							<td align="left" valign="top" class="showhide">

								<table>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts"
												key="CctsEventNotification.field.eventType" /></td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b"><bean:message bundle="msg.ccts"
													name="notification" property="eventType.name" /> </span>
									</tr>

									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts"
												key="CctsEventNotification.field.application" /></td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b">${notification.application}</span>
									</tr>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts" key="CctsEventNotification.field.dateTime" />
										</td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b"><fmt:formatDate
													value="${notification.dateSent}"
													pattern="yyyy-MM-dd HH:mm:ss" /> </span>
									</tr>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts"
												key="CctsEventNotification.field.objectIdValue" /></td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b"><c:out
													value="${notification.objectIdValue}" /> </span>
									</tr>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts"
												key="CctsEventNotification.field.processingStatus" /></td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b"><bean:message bundle="msg.ccts"
													name="notification" property="processingStatus.name" /> </span>
									</tr>
								</table>
							</td>
						</tr>
						<c:if test="${not empty notification.processingLog}">
							<tr>
								<td align="left" class="tr_bg_blue1"><span
									class="blue_ar_b">&nbsp;<bean:message bundle="msg.ccts"
											key="CctsEventNotification.processingLog" /> </span></td>
							</tr>
							<tr>
								<td align="left" valign="top" class="showhide">
									<table width="99%" cellspacing="0" cellpadding="4">
										<tr>
											<td class="tableheading" nowrap="nowrap" width="25%"><b><bean:message
														bundle="msg.ccts"
														key="CctsEventNotification.processingLog.time" /> </b>
											</td>
											<td class="tableheading" nowrap="nowrap" width="10%"><b><bean:message
														bundle="msg.ccts"
														key="CctsEventNotification.processingLog.result" /> </b>
											</td>
											<td class="tableheading" nowrap="nowrap" width="10%"><b><bean:message
														bundle="msg.ccts"
														key="CctsEventNotification.processingLog.errCode" /> </b>
											</td>
											<td class="tableheading"><b><bean:message
														bundle="msg.ccts"
														key="CctsEventNotification.processingLog.payload" /> </b>
											</td>
										</tr>
										<c:forEach items="${notification.processingLog}"
											var="logEntry">
											<tr>
												<td class="black_ar" nowrap="nowrap"><fmt:formatDate
														value="${logEntry.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
												</td>
												<td class="black_ar" nowrap="nowrap">
													${logEntry.processingResult}</td>
												<td class="black_ar">
													${logEntry.errorCode}</td>
												<td class="black_ar" nowrap="nowrap"><c:choose>
														<c:when test="${empty logEntry.payload}">
													None
													</c:when>
														<c:otherwise>
															<textarea readonly="true" 
																class="black_ar_s" style="width: 99%;" 
																rows="6"><c:out value="${logEntry.payload }"/></textarea>
														</c:otherwise>
													</c:choose></td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
						</c:if>
						<tr>
							<td align="left" class="buttonbg"><html:button
									onclick="window.history.back();"
									onkeypress="window.history.back()" property="backButton"
									styleClass="blue_ar_b" accesskey="Enter">
									<bean:message key="buttons.back" />
								</html:button>
							</td>
						</tr>
					</c:if>
				</table></td>
		</tr>
	</html:form>
</table>
