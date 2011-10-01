<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html:form action="/CctsEventNotifications">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class="maintable">
		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"> <bean:message bundle="msg.ccts"
						key="CctsEventNotifications.header" /> </span></td>
					<td align="right"><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title - CCTS Event Notifications" width="31" height="24" /></td>
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
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">

				<tr>
					<td width="99%" align="left" class="bottomtd"><%@ include
						file="/pages/content/common/ActionErrors.jsp"%></td>
				</tr>
				<tr>
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
					&nbsp;<bean:message bundle="msg.ccts" key="CctsEventNotifications.pageTitle" /> </span><span
						class="black_ar_s">(${totalResults}
					record${totalResults==1?'':'s'} found)</span></td>
				</tr>
				<tr>

					<td align="right" colspan="2"><c:if
						test='${totalResults > 10}'>
						<custom:test name=" " pageNum='${pageNum}'
							totalResults='${totalResults}'
							numResultsPerPage='${numResultsPerPage}'
							pageName="CctsEventNotifications.do"
							showPageSizeCombo="<%=true%>"
							recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}' />
					</c:if></td>
				</tr>
				<tr class="td_color_F7F7F7">
					<td align="center" class="showhide">
					<table width="99%" border="0" cellspacing="0" cellpadding="4">
						<logic:empty name="data">
							<tr>
								<td class="grey_ar" colspan="5"><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.noNotifications" /></td>
							</tr>
						</logic:empty>
						<logic:notEmpty name="data">
							<tr>
								<%-- <td width="2%" class="tableheading"><strong><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.NotificationId" /></strong></td> --%>
								<td width="25%" class="tableheading"><strong><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.App" /></strong></td>
								<td width="18%" class="tableheading"><strong><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.Event" /></strong></td>
								<td width="28%" class="tableheading"><strong><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.Status" /></strong></td>
								<td width="3%" class="tableheading" nowrap="nowrap"><strong><bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.Date" /></strong> <span
									class="grey_ar_s"> <bean:message bundle="msg.ccts"
									key="CctsEventNotifications.datatable.DateFormat" /> </span></td>
								<td class="tableheading" nowrap="nowrap">&nbsp;</td>
							</tr>
							<logic:iterate id="notification" name="data" indexId="index">
								<c:set var="style"
									value="${index % 2 == 0 ? 'tabletd1':'black_ar'}" />
								<tr>
									<%-- <td class='${style}'><c:out value='${notification.id}' /></td> --%>
									<td class='${style}'><bean:message bundle="msg.ccts" name="notification"
										property="application.name" /></td>
									<td class='${style}'><bean:message bundle="msg.ccts" name="notification"
										property="eventType.name" /></td>
									<td class='${style}'><bean:message bundle="msg.ccts" name="notification"
										property="processingStatus.name" /></td>
									<td class='${style}' nowrap="nowrap"><fmt:formatDate
										value="${notification.dateSent}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td class='${style}'><html:link action="CctsEventNotification" 
										paramId="msgId" paramName="notification" paramProperty="id">Details</html:link></td>
								</tr>
							</logic:iterate> 
						</logic:notEmpty>
					</table>
					</td>
				</tr>
				<tr>
					<td align="left" class="buttonbg"><html:button onclick="window.location='Home.do';"
								onkeypress="window.location='Home.do';" property="backButton"
								styleClass="blue_ar_b" accesskey="Enter">
								<bean:message bundle="msg.ccts" key="DataQueue.btn.exit" />
							</html:button></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>