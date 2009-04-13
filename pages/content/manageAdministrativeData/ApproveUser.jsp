<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<SCRIPT LANGUAGE="JavaScript" SRC="jss/javaScript.js"></SCRIPT>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.*"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
</head>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action="/ApproveUser">
	<html:hidden property="id" />
	<html:hidden property="operation" />
  <tr>
    <td><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="user.name" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
        <td valign="bottom" ><html:link page="/User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border="0" /></html:link><a href="#"></a></td>
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link><a href="#"></a></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_approve_user1.jpg" alt="Approve New Users" width="139" height="22" border="0" /></td>
        <td width="90%" valign="bottom" class="td_tab_bg" >&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td align="left" class="toptd">
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		  </td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="approveUser.title" />  </span><span class="black_ar_s">(${requestScope.totalResults} records		found)</span></td>
        </tr>
		<!-- paging begins -->
		<tr>
			<td align="right" colspan = "2" ><c:if test='${requestScope.totalResults > 10}'><custom:test name=" "pageNum='${requestScope.pageNum}' totalResults='${requestScope.totalResults}'	numResultsPerPage='${requestScope.numResultsPerPage}' pageName="ApproveUserShow.do" showPageSizeCombo="<%=true%>" recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}' /></c:if></td>
		</tr>
		<!-- paging ends -->
        <tr>
          <td align="center" class="showhide">
		  <table width="99%" border="0" cellspacing="0" cellpadding="4">
		  <c:if test='${requestScope.totalResults > 0}'>
              <tr>
                <td width="2%" class="tableheading"><strong>#</strong></td>
                <td width="25%" class="tableheading"><strong><bean:message key="user.loginName" /> </strong></td>
                <td width="20%" class="tableheading"><strong><bean:message key="user.userName" /></strong></td>
                <td width="28%" class="tableheading"><strong><bean:message key="user.emailAddress" /></strong></td>
                <td width="25%" class="tableheading"><strong><bean:message key="approveUser.registrationDate" /> </strong> <span class="grey_ar_s">[<bean:message key="page.dateFormat" />] </span></td>
              </tr>
			  </c:if>
			  <logic:empty name="showDomainObjectList">
			  <tr>
				<td>&nbsp;</td>
				<td colspan="5" align="center" class="grey_ar">
					<bean:message key="approveUser.newUsersNotFound" />
				</td>
			  </tr>
			</logic:empty>
			<!-- For showing the results -->
			<c:set var="count" value='${param.numResultsPerPage * (param.pageNum - 1) + 1}' scope="page"/>
			 <logic:notEmpty name="showDomainObjectList">
			  <logic:iterate id="currentUser" name="showDomainObjectList">
				<c:set var="style" value="black_ar" scope="page" />
				 <tr>
					<c:if test='${pageScope.count % 2 == 0}'>
						<c:set var="style" value="tabletd1" scope="page" />
					</c:if>
					<td width="3%" class='${pageScope.style}'><c:out value='${pageScope.count}' /></td>
					<td width="21%" class='${pageScope.style}'><a href='${requestScope.userDetailsLink}${currentUser.id}' class="view"><bean:write name="currentUser" property="loginName" /></a></td>
					<td width="20%" class='${pageScope.style}'><bean:write name="currentUser" property="lastName" /><bean:write name="currentUser" property="firstName" /></td>
					<td width="25%" class='${pageScope.style}'><bean:write name="currentUser" property="emailAddress"/>	</td>
                    <c:set var="date" value='${currentUser.startDate}' scope="request" />
                     <%
                     	// Added by Geeta for date format change
                     			   Date date=(Date)request.getAttribute("date");
                     			   String startDate=AppUtility.parseDateToString(date,CommonServiceLocator.getInstance().getDatePattern());
                     %>
					
<!--<td width="31%" class='${pageScope.style}'><bean:write name="currentUser" property="startDate" /></td>-->
					<td width="31%" class='${pageScope.style}'><%=startDate%></td>
					<c:set var="count" value='${pageScope.count+1}' scope="page" />
				</logic:iterate>
			</logic:notEmpty>
				</tr>		                
          </table>	  
		  </td>
        </tr>
      </table></td>
  </tr>
  </html:form>
</table>
