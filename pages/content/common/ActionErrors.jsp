<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${requestScope['org.apache.struts.action.ERROR'] != null }">
	<table border="0" cellspacing="0" cellpadding="3">
		<tr>
			<td valign="top" >
				<img src="images/uIEnhancementImages/alert-icon.gif" alt="error messages"
				width="16" vspace="0" hspace ="0" height="18" valign="top"></td>
			<td class="messagetexterror" align="left">
			<strong><bean:message key="errors.title"/></strong></td>
			</tr>
			<tr><td>&nbsp;</td>
			<td class="messagetexterror" >
			<html:errors /></td>
		</tr>
	</table>
</c:if>
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<table border="0" cellpadding="3" cellspacing="3">
		<tr>
			<td><img src="images/uIEnhancementImages/error-green.gif"
				alt="successful messages" width="16" height="16">
			</td>
			<td class="messagetextsuccess"><%=messageKey%></td>
		</tr>
	</table>
</html:messages>