<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page
	import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.query.util.global.AQConstants"%>
<%
	List columnList = (List) request
			.getAttribute(edu.wustl.common.util.global.Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request
			.getAttribute(edu.wustl.common.participant.utility.Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
%>
<script type="text/javascript" src="jss/prototype.js"></script>
<script type="text/javascript">
	function confirmAccept() {
		return confirm('<bean:message bundle="msg.ccts" key="DataQueue.msg.confirmAccept" />');
	}
	function confirmReject() {
		return confirm('<bean:message bundle="msg.ccts" key="DataQueue.msg.confirmReject" />');
	}

	function resetSelection() {
		$('matchedParticipantId').value = '';
		mygrid.setEditable(true);
		rowCount = mygrid.getRowsNum();
		for (i = 1; i <= rowCount; i++) {
			var cl = mygrid.cells(i, 0);
			cl.setChecked(false);
		}
	}

	// grid plumbing. Copied from /catissuecore-v2.0_BDA/software/caTissue/src/web/pages/content/manageBioSpecimen/ParticipantLookup.jsp
	function participant(id) {
		//onParticipantClick(id);
	}
	function onParticipantClick(id) {
		var cl = mygrid.cells(id, mygrid.getColumnCount() - 1);
		var pid = cl.getValue();
		$('matchedParticipantId').value = pid;
		//alert(pid);		
	}

	var useDefaultRowClickHandler = 2;
	var useFunction = "participant";
	var wdt = getWidth(90);
	if (wdt > 1000)
		wdt = getWidth(63.5);
	wdt = 990;
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<html:form action="/DataQueue">
		<html:hidden property="matchedParticipantId" value=""
			styleId="matchedParticipantId" />
		<tr>
			<td class="td_color_bfdcf3"><table border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td class="td_table_head" nowrap="nowrap"><span
							class="wh_ar_b"><bean:message bundle="msg.ccts"
									key="DataQueue.header" /> </span></td>
						<td><img
							src="images/uIEnhancementImages/table_title_corner2.gif"
							width="31" height="24" alt="Page Title - Data Queue Processing" />
						</td>
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
					<c:if test="${queueItem==null}">
						<tr>
							<td align="left" valign="top" class="grey_ar">&nbsp;<bean:message
									bundle="msg.ccts" key="DataQueue.msg.noItems" /><br /> <br />
							</td>
						</tr>
					</c:if>
					<c:if test="${queueItem!=null}">
						<tr>
							<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message
										bundle="msg.ccts" key="DataQueue.title" /> </span></td>
						</tr>
						<tr>
							<td align="left" class="black_ar_b user_msg"><span> <img
									alt="" src="images/i.gif"> <c:choose>
										<c:when test="${total>1}">
											<bean:message arg0="${total}" bundle="msg.ccts"
												key="DataQueue.msg.summaryPlural" />
										</c:when>
										<c:otherwise>
											<bean:message bundle="msg.ccts"
												key="DataQueue.msg.summarySingular" />
										</c:otherwise>
									</c:choose> </span></td>
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
													name="queueItem" property="notification.eventType.name" />
										</span>
									</tr>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts" key="DataQueue.field.eventDate" /></td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b"><fmt:formatDate
													value="${queueItem.notification.dateSent}"
													pattern="yyyy-MM-dd HH:mm:ss" /> </span>
									</tr>
									<tr>
										<td width="17%" align="right" class="black_ar" nowrap="nowrap"><bean:message
												bundle="msg.ccts"
												key="CctsEventNotification.field.application" />
										</td>
										<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
											class="black_ar_b">${queueItem.notification.application}</span>
									</tr>
								</table>
							</td>
						</tr>
						<c:if test="${queueItem.participantRelated}">
							<tr>
								<td align="left" class="tr_bg_blue1"><span
									class="blue_ar_b">&nbsp;<bean:message bundle="msg.ccts"
											key="DataQueue.title.participant" /> </span>
								</td>
							</tr>
							<tr>
								<td align="left" valign="top" class="showhide">
									<table>
										<tr>
											<td width="17%" align="right" class="black_ar"
												nowrap="nowrap"><bean:message bundle="msg.ccts"
													key="DataQueue.field.action" /></td>
											<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
												class="black_ar_b"> <c:if
														test="${queueItem.participant!=null}">
														<bean:message bundle="msg.ccts"
															key="DataQueue.msg.participantUpdated" />
													</c:if> <c:if test="${queueItem.participant==null}">
														<bean:message bundle="msg.ccts"
															key="DataQueue.msg.participantCreated" />
													</c:if> </span>
										</tr>
										<c:if test="${queueItem.participant!=null}">
											<tag:ParticipantLink participant="${queueItem.participant}"/>										
										</c:if>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td colspan="2" nowrap="nowrap">												
											<tag:ObjectComparison comparisonResults="${partCompResults}" object="${queueItem.participant}"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<%@ include file="/pages/content/ccts/ParticipantMatching.jsp"%>
							<tag:ConversionErrors/>

						</c:if>
						
						<!-- Registration related section starts here -->
						<c:if test="${queueItem.registrationRelated}">
							<tr>
								<td align="left" class="tr_bg_blue1"><span
									class="blue_ar_b">&nbsp;<bean:message bundle="msg.ccts"
											key="DataQueue.title.registration" /> </span>
								</td>
							</tr>
							<tr>
								<td align="left" valign="top" class="showhide">
									<table>
										<tr>
											<td width="17%" align="right" class="black_ar"
												nowrap="nowrap"><bean:message bundle="msg.ccts"
													key="DataQueue.field.action" /></td>
											<td width="83%" align="left" valign="middle" nowrap="nowrap"><span
												class="black_ar_b"> <c:if
														test="${queueItem.registration!=null}">
														<bean:message bundle="msg.ccts"
															key="DataQueue.msg.registrationUpdated" />
													</c:if> <c:if test="${queueItem.registration==null}">
														<bean:message bundle="msg.ccts"
															key="DataQueue.msg.registrationCreated" />
													</c:if> </span>
										</tr>
										<c:if test="${convertedReg.participant!=null && convertedReg.participant.id!=null}">
											<tag:ParticipantLink participant="${convertedReg.participant}"/>
										</c:if>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td colspan="2" nowrap="nowrap">
												<tag:ObjectComparison title="PARTICIPANT INFORMATION" comparisonResults="${partCompResults}" object="${convertedReg.participant!=null && convertedReg.participant.id!=null ? convertedReg.participant : null }"/>
											</td>
										</tr>
										<tr>
											<td colspan="2" nowrap="nowrap">
												<tag:ObjectComparison title="REGISTRATION INFORMATION" object="${queueItem.registration}" comparisonResults="${regCompResults}"/>
											</td>
										</tr>										
									</table>
								</td>
							</tr>
							<%@ include file="/pages/content/ccts/ParticipantMatching.jsp"%>
							<tag:ConversionErrors/>
						</c:if>
					</c:if>
					<tr>
						<td align="left" class="buttonbg" nowrap="nowrap"><c:if
								test="${queueItem!=null}">								
								<html:submit onkeypress="return confirmAccept()"										
										onclick="return confirmAccept()" styleClass="blue_ar_b"
										property="accept">
										<bean:message bundle="msg.ccts" key="DataQueue.btn.accept" />
								</html:submit>								
								<html:submit onkeypress="return confirmReject()"
									onclick="return confirmReject()" styleClass="blue_ar_b"
									property="reject">
									<bean:message bundle="msg.ccts" key="DataQueue.btn.reject" />
								</html:submit>
							</c:if> <html:button onclick="window.location='Home.do';"
								onkeypress="window.location='Home.do';" property="backButton"
								styleClass="blue_ar_b" accesskey="Enter">
								<bean:message bundle="msg.ccts" key="DataQueue.btn.exit" />
							</html:button>
						</td>
					</tr>

				</table></td>
		</tr>
	</html:form>
</table>
