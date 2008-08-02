<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<html:errors/> 

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="/RequestToOrderSubmit.do">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					
					<tr><td></td></tr>
					<tr><td></td></tr>

					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.note"/>
						</td>
					</tr>
					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.note1"/>
						</td>
					</tr>
					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.a"/>
						</td>
					<tr>
					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.b"/>
						</td>
					</tr>
					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.c"/>
						</td>
					</tr>
					<tr>
						<td colspan='4' class="formMessage">
							<bean:message key="errors.ordering.d"/>
						</td>
						
					</tr>
					
					<tr><td></td></tr>
					<tr><td></td></tr>
					<tr>
						<td colspan='4' class="formMessage">	
							<bean:message key="requiredfield.message"/>
						</td>
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="orderingsystem.label.requestOrder"/>
						</td>
					</tr>
					
					<!-- Request Name -->
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="orderRequestName">
								<bean:message key="orderingsystem.label.orderRequestName"/>
							</label>	
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="50" size="30" styleId="orderRequestName" property="orderRequestName"/>
						</td>
					</tr>
					
					<!-- Distribution Protocol dropdown-->
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="distributionProtocol">
								<bean:message key="orderingsystem.label.distributionProtocol"/>
							</label>
						</td>
						<td class="formField">
							<html:select property="distributionProtocol" styleClass="formFieldSized" styleId="distributionProtocol" 
									size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.DISTRIBUTIONPROTOCOLLIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>
					
					<!-- Comments field-->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="orderingsystem.label.comments"/>
							</label>
						</td>
						<td class="formField">
							<html:textarea styleClass="formFieldSized" rows="3" cols="40" styleId="comments" property="comments"/>
						</td>
					</tr>
		
					<!-- action buttons -->
					<tr>
						<td align="right" colspan="3">
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
									<td>
										<html:submit styleClass="actionButton" title="<%=ApplicationProperties.getValue("orderingsystem.tooltip.button.submit")%>">
											<bean:message  key="orderingsystem.button.submit"/>
										</html:submit>
									</td>
				
								</tr>
							</table>
						</td>
					</tr>					
				</table>
			</td>
		</tr>
	</table>
</html:form>