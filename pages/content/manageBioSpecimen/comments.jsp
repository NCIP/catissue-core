<tr>
	<td class="formTitle" height="20" colspan="3"><logic:equal
		name="operation" value="<%=Constants.ADD%>">
		<bean:message key="specimen.comments" />
	</logic:equal> <logic:equal name="operation"
		value="<%=Constants.EDIT%>">
		<bean:message key="specimen.comments" />
	</logic:equal></td>
</tr>
<tr>
	<td class="formRequiredNotice" width="5">&nbsp;</td>
	<td class="formRequiredLabel"><label for="name"><bean:message
		key="specimen.comments" /> </label></td>
	<td class="formField"><html:textarea styleClass="formFieldSized"
		rows="3" styleId="comments" property="comments" /><%form.getComments();%></td>
</tr>
