<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="jss/prototype.js"></script>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<tr>
		<td class="td_color_bfdcf3"><table border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td class="td_table_head" nowrap="nowrap"><span
						class="wh_ar_b"><bean:message bundle="msg.ccts"
								key="AccessDenied.header" /> </span></td>
					<td><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						width="31" height="24" alt="Page Title - Access Denied" />
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
				<!-- 
				<tr>
					<td align="left" valign="top" class="grey_ar">&nbsp;<bean:message
							bundle="msg.ccts" key="AccessDenied.text" /><br /> <br />
					</td>
				</tr>
				 -->
				<tr>
					<td align="left" class="buttonbg" nowrap="nowrap"><html:button
							onclick="window.location='Home.do';"
							onkeypress="window.location='Home.do';" property="backButton"
							styleClass="blue_ar_b" accesskey="Enter">
							<bean:message bundle="msg.ccts" key="DataQueue.btn.exit" />
						</html:button>
					</td>
				</tr>

			</table></td>
	</tr>

</table>
