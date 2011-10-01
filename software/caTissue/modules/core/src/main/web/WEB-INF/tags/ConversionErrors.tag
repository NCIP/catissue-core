<%@ tag language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<c:if test="${not empty convErrors}">
	<tr>
		<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message
					bundle="msg.ccts" key="DataQueue.title.errors" /> </span>
		</td>
	</tr>
	<tr>
		<td class="black_ar_b">
			<ul>
				<c:forEach items="${convErrors}" var="msg">
					<li class="user_msg"><img alt=""
						src="images/uIEnhancementImages/alert-icon.gif"> <c:out
							value="${msg}"></c:out>
					</li>
				</c:forEach>
			</ul>
		</td>
	</tr>
</c:if>
