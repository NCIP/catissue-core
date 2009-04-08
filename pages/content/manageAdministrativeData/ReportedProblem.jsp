<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<html:form action="/ReportedProblemShow">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b">
									<bean:message key="ReportedProblems.header" /> </span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif"
								alt="Page Title - Reported Problem"		width="31" height="24" /></td>
								</tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td width="99%" align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message
								key="reportedProblem.pageTitle" /> </span><span class="black_ar_s">(${requestScope.totalResults}
							records found)</span></td>
						</tr>
						<tr>
							
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
							<td align="center" class="showhide"><table width="99%" border="0" cellspacing="0" cellpadding="4">
								<logic:notEmpty name="showDomainObjectList">
								<tr>
									<td width="2%" class="tableheading"><strong><bean:message
										key="reportedProblem.serialNumber" /></strong></td>
									<td width="25%" class="tableheading"><strong><bean:message
										key="reportedProblem.title" /></strong></td>
									<td width="18%" class="tableheading"><strong><bean:message
										key="reportedProblem.problemId" /></strong></td>
									<td width="28%" class="tableheading"><strong><bean:message
										key="reportedProblem.from" /></strong></td>
									<td width="27%" class="tableheading"><strong><bean:message
										key="reportedProblem.reportedDate" /></strong> <span class="grey_ar_s">
									<bean:message key="reportedProblem.dateFormat" /> </span></td>
								</tr>
								</logic:notEmpty>
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
												class="view"> <bean:write name="problem"
												property="subject" /> </a></td>
											<td class='${pageScope.style}'><bean:write
												name="problem" property="id" /></td>
											<td class='${pageScope.style}'><bean:write
												name="problem" property="from" /></td>
					 <c:set var="date" value='${problem.reportedDate}' scope="request" />
					 <%
					 	// Added by Geeta for date format change
					 			   Date date=(Date)request.getAttribute("date");
					 			   String reportedDate=AppUtility.parseDateToString(date,CommonServiceLocator.getInstance().getDatePattern());
					 %>

				<!-- <td class='${pageScope.style}'><bean:write name="problem" property="reportedDate" /></td> -->
												
						<td class='${pageScope.style}'><%= reportedDate %></td>
										</tr>
										<c:set var="count" value='${pageScope.count+1}' scope="page" />
									</logic:iterate>
								</logic:notEmpty>
							</table>
							</td>
						</tr>
					 </table></td>
  </tr>
</table>
</html:form>