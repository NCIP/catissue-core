<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page
	import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.query.util.global.AQConstants"%>							
							<c:if test="${not empty MatchedParticpant}">
								<tr>
									<td align="left" class="tr_bg_blue1"><span
										class="blue_ar_b">&nbsp;<bean:message bundle="msg.ccts"
												key="DataQueue.title.participantMatching" /> </span></td>
								</tr>
								<tr>
									<td align="left" class="black_ar_b user_msg"><span>
											<img alt="" src="images/i.gif"> <bean:message
												bundle="msg.ccts" key="DataQueue.msg.participantMatching" />
									</span></td>
								</tr>
								<tr>
									<td colspan="2" class="bottomtd"></td>
								</tr>
								<c:catch var="gridComponentException">
								<tr>
									<td colspan="2">
										<table width="100%" border="0">
											<tr height=110 valign=top>
												<td valign=top class="formFieldAllBorders"><%@ include
														file="/pages/content/search/AdvanceGrid.jsp"%>
												</td>
											</tr>
											<tr>
												<td><html:button onclick="resetSelection();"
														onkeypress="resetSelection();" property="resetButton"
														styleClass="blue_ar_b">
														<bean:message bundle="msg.ccts" key="DataQueue.btn.reset" />
													</html:button></td>
											</tr>
										</table></td>
								</tr>
								</c:catch>
								<c:if test="${gridComponentException!=null}">
								<tr>
									<td class="black_ar_b">										
										<span class="user_msg"><img alt=""
											src="images/uIEnhancementImages/alert-icon.gif">
												<bean:message bundle="msg.ccts" key="DataQueue.msg.gridErr" />
											</span>											
									</td>
								</tr>								
								</c:if>
								<tr>
									<td colspan="2" class="bottomtd"></td>
								</tr>
							</c:if>
