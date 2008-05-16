<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:errors />

<html:form action="/ReportedProblemShow">
	<table width="100%" border="1" cellpadding="0" cellspacing="0"
		class="newMaintable">
		<tr>
			<td class="td_color_bfdcf3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="whitetable_bg">
				<tr>
					<td width="100%" colspan="2" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4" valign="top" class="td_color_bfdcf3">
							<table width="18%" border="0" cellpadding="0" cellspacing="0"
								background="images/uIEnhancementImages/table_title_bg.gif">
								<tr>
									<td width="80%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
									<bean:message key="ReportedProblems.header" /> </span></td>
									<td width="20%" align="right"><img
										src="images/uIEnhancementImages/table_title_corner2.gif"
										width="31" height="24" /></td>
								</tr>

							</table>
							</td>
						</tr>
						<tr>
							<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
							<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
							<td width="89%" valign="bottom" class="td_tab_bg"
								style="padding-top: 4px;">&nbsp;</td>
							<td width="1%" valign="bottom" class="td_color_bfdcf3"
								style="padding-top: 4px;">&nbsp;</td>
						</tr>
					</table>
					</td>

				</tr>
				<tr>
					<td colspan="2" class="td_color_bfdcf3"
						style="padding-left: 10px; padding-right: 10px; padding-bottom: 10px;">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						bgcolor="#FFFFFF">
						<tr>
							<td width="1%" align="left">&nbsp;</td>
							<td width="99%" align="left">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" class="tr_bg_blue1">&nbsp;</td>
							<td height="25" align="left" class="tr_bg_blue1"><span
								class="blue_ar_b"> <bean:message
								key="reportedProblem.pageTitle" /> </span><span class="black_ar_s">(${requestScope.totalResults}
							records found)</span></td>
						</tr>
						<tr>
							<td></td>
							<td align="right" colspan="2"><c:if
								test='${requestScope.totalResults > 10}'>
								<custom:test name=" " pageNum='${requestScope.pageNum}'
									totalResults='${requestScope.totalResults}'
									numResultsPerPage='${requestScope.numResultsPerPage}'
									pageName="ReportedProblemShow.do" showPageSizeCombo="<%=true%>"
									recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}' />
							</c:if></td>
						</tr>
						<tr class="td_color_F7F7F7">
							<td>&nbsp;</td>
							<td style="padding-top: 10px; padding-bottom: 15px;">
							<table width="99%" border="0" cellspacing="0" cellpadding="4">
								<tr>
									<td width="2%" class="tableheading"><bean:message
										key="reportedProblem.serialNumber" /></td>
									<td width="20%" class="tableheading"><bean:message
										key="reportedProblem.title" /></td>
									<td width="17%" class="tableheading"><bean:message
										key="reportedProblem.problemId" /></td>
									<td width="24%" class="tableheading"><bean:message
										key="reportedProblem.from" /></td>
									<td width="37%" class="tableheading"><bean:message
										key="reportedProblem.reportedDate" /><span class="grey_ar">
									<bean:message key="reportedProblem.dateFormat" /> </span></td>
								</tr>
								<logic:empty name="showDomainObjectList">
									<tr>
										<td class="grey_ar" colspan="5"><bean:message
											key="reportedProblem.noNewProblemFound" /></td>
									</tr>
								</logic:empty>
								<c:set var="count"
									value='${param.numResultsPerPage * (param.pageNum - 1) + 1}'
									scope="page" />
								<logic:notEmpty name="showDomainObjectList">
									<logic:iterate id="problem" name="showDomainObjectList">
										<c:set var="style" value="black_ar" scope="page" />
										<tr>
											<c:if test='${pageScope.count % 2 == 0}'>
												<c:set var="style" value="tabletd1" scope="page" />
											</c:if>
											<td class='${pageScope.style}'><c:out
												value='${pageScope.count}' /></td>
											<td class='${pageScope.style}'><a
												href="${requestScope.problemDetailsLink}${problem.id}"
												class="link"> <bean:write name="problem"
												property="subject" /> </a></td>
											<td class='${pageScope.style}'><bean:write
												name="problem" property="id" /></td>
											<td class='${pageScope.style}'><bean:write
												name="problem" property="from" /></td>
											<td class='${pageScope.style}'><bean:write
												name="problem" property="reportedDate" /></td>
										</tr>
										<c:set var="count" value='${pageScope.count+1}' scope="page" />
									</logic:iterate>
								</logic:notEmpty>
							</table>
							</td>
						</tr>
						<tr class="td_color_F7F7F7">
							<td class="buttonbg">&nbsp;</td>
							<td class="buttonbg"><span class="cancellink"> <html:link
								page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
								<bean:message key="buttons.cancel" />
							</html:link></span></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>