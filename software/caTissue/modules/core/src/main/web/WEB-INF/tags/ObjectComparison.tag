<%@ tag language="java" isELIgnored="false"%>
<%@ attribute name="object" required="true"
	type="edu.wustl.common.domain.AbstractDomainObject"%>
<%@ attribute name="comparisonResults" required="true"
	type="java.util.Collection"%>
<%@ attribute name="title" required="false"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!-- Table showing participant info delta -->
<table cellpadding="5" cellspacing="5">
	<c:if test="${not empty title}">
		<tr>
			<td></td>
			<td colspan="2" align="center" class="black_ar_b">${title}</td>
		</tr>	
	</c:if>
	<tr>
		<td class="min_len_250">&nbsp;</td>
		<td align="center" class="tableheading min_len_250" nowrap="nowrap"><b><bean:message
					bundle="msg.ccts" key="DataQueue.field.newValue" /> </b>
		</td>
		<c:if test="${object!=null}">
			<td align="center" class="tableheading min_len_250" nowrap="nowrap"><b><bean:message
						bundle="msg.ccts" key="DataQueue.field.oldValue" /> </b>
			</td>
		</c:if>
	</tr>
	<c:forEach items="${comparisonResults}" var="line">
		<c:set var="extra_style"
			value="${line.different && object!=null?'important_red':''}"></c:set>
		<tr>
			<td align="right" class="black_ar_b" nowrap="nowrap"><b>${line.fieldName}:</b>
			</td>
			<td class="black_ar ${extra_style}"><c:out
					value="${line.newValue}"></c:out>
			</td>
			<c:if test="${object!=null}">
				<td class="black_ar ${extra_style}"><c:out
						value="${line.oldValue}"></c:out>
				</td>
			</c:if>
		</tr>
	</c:forEach>
</table>
<!-- End table showing participant info delta-->
