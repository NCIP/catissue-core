<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="css/mktree.css" rel="stylesheet" type="text/css" />

<SCRIPT LANGUAGE="JavaScript" SRC="jss/javaScript.js"></SCRIPT>

<html:errors/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
<html:form action="/ApproveUser">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_bfdcf3">
      <tr>
        <td>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
            <tr>
             <td width="100%" colspan="2" valign="top">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="3" valign="top" class="td_color_bfdcf3">
					<table width="10%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                    <tr>
                      <td width="74%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
					  <bean:message key="user.name" />
					  </span></td>
                      <td width="26%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                    </tr>
                </table></td>
              </tr>
											<tr>
												<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
				<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
                <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="4%" class="td_tab_bg" >&nbsp;</td>
                      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1"><img src="images/uIEnhancementImages/tab_add_user1.jpg" alt="Add" width="57" height="22" border="0" /></html:link></td>
                      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1"><img src="images/uIEnhancementImages/tab_edit_user.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                      <td width="15%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_approve_user1.jpg" alt="Approve New Users" width="139" height="22" border="0" /></td>
                      <td width="65%" valign="bottom" class="td_tab_bg" >&nbsp;</td>
                      <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>		</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="td_color_bfdcf3" style="padding-left: 10px; padding-right:		10px; padding-bottom: 10px;">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											bgcolor="#FFFFFF">
											<tr>
												<td width="1%" align="left">&nbsp;</td>
												<td width="99%" colspan="3" align="left">&nbsp;</td>
											</tr>
											<tr>
												<td align="left" class="tr_bg_blue1">&nbsp;</td>
												<td height="25" colspan="3" align="left" class="tr_bg_blue1">
													<span class="blue_ar_b"> 
														<bean:message key="approveUser.title" /> 
													</span>
													<span class="black_ar_s">(${requestScope.totalResults} records		found) 
													</span>
												</td>
											</tr>

											<tr>
												<td align="left">&nbsp;
												</td>
												<td colspan="3" align="left">&nbsp;
												</td>
											</tr>
											<tr>
												<td><html:hidden property="operation" /></td>
												<td><html:hidden property="id" /></td>
											</tr>
							<!-- paging begins -->
											<tr>
												<td align="left">&nbsp;</td>
												<td height="25" align="right" colspan="2" class="black_ar">
													<c:if test='${pageScope.count > 10}'>
														<custom:test name="New User Search Results"
															pageNum='${requestScope.pageNum}'
															totalResults='${requestScope.totalResults}'
															numResultsPerPage='${requestScope.numResultsPerPage}'
															pageName="ApproveUserShow.do" showPageSizeCombo="<%=true%>"
															recordPerPageList='${requestScope.RESULT_PERPAGE_OPTIONS}' />
													</c:if>
												</td>
												<td width="438" align="left">&nbsp;</td>
											</tr>
							<!-- paging ends -->
											<tr>
												<td align="left">&nbsp;</td>
												<td colspan="3" align="left">&nbsp;</td>
											</tr>
											<tr class="td_color_F7F7F7">
												<td>&nbsp;</td>
												<td colspan="3">
													<table width="99%" border="0" cellspacing="0" cellpadding="4">
													<tr>
														<td width="2%" class="tableheading">#</td>
														<td width="21%" class="tableheading">
															<bean:message key="user.loginName" />
														</td>
														<td width="20%" class="tableheading">
															<bean:message key="user.userName" />
														</td>
														<td width="25%" class="tableheading">
															<bean:message key="user.emailAddress" />
														</td>
														<td width="31%" class="tableheading">
															<bean:message key="approveUser.registrationDate" /> 
															<span class="grey_ar">[YYYY-MM-DD]
															</span>
														</td>
													</tr>
												<logic:empty name="showDomainObjectList">
													<tr>
														<td>&nbsp;</td>
														<td colspan="5" align="center" class="grey_ar">
															<bean:message key="approveUser.newUsersNotFound" />
														</td>
													</tr>
												</logic:empty>
									<!-- For showing the results -->
									<c:set var="count" value="1" scope="page" />
										<logic:notEmpty name="showDomainObjectList">
											<logic:iterate id="currentUser" name="showDomainObjectList">
												<c:set var="style" value="black_ar" scope="page" />
													<tr>
														<c:if test='${pageScope.count % 2 == 0}'>
															<c:set var="style" value="tabletd1" scope="page" />
														</c:if>
														<td width="3%" class='${pageScope.style}'>
															<c:out value='${pageScope.count}' />
														</td>
														<td width="21%" class='${pageScope.style}'>
															<a href='${requestScope.userDetailsLink}${currentUser.id}'
															class="link">
															<bean:write name="currentUser" property="loginName" />
															</a>
														</td>
														<td width="20%" class='${pageScope.style}'>
															<bean:write name="currentUser" property="lastName" />
															<bean:write name="currentUser" property="firstName" />
														</td>
														<td width="25%" class='${pageScope.style}'>
															<bean:write name="currentUser" property="emailAddress"/>
														</td>
														<td width="31%" class='${pageScope.style}'>
															<bean:write name="currentUser" property="startDate" />
														</td>
													</tr>
												<c:set var="count" value='${pageScope.count+1}' scope="page" />
											</logic:iterate>
										</logic:notEmpty>
												</table>
											</td>
										</tr>
										<tr class="td_color_F7F7F7">
											<td>&nbsp;</td>
											<td colspan="3" align="center">&nbsp;</td>
										</tr>
										<tr class="td_color_F7F7F7">
											<td class="buttonbg">&nbsp;</td>
											<td height="35" colspan="3" class="buttonbg">
												<span class="cancellink">
												<html:link page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
													<bean:message key="buttons.cancel" />
												</html:link>
												</span>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</html:form>
</table>