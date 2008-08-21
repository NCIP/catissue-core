<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<html:form action="ArrayTypeAdd.do">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
	<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
			<tr>
				<td class="formMessage" colspan="3">* indicates a required field</td>
			</tr>
			
			<tr>
				<td class="formTitle" height="20" colspan="5">
					<bean:message key="arrayType.title"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="name">
						<bean:message key="arrayType.name" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="name" property="name"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="specimenClass">
						<bean:message key="arrayType.specimenClass" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="specimenClass" styleClass="formFieldSized" styleId="state" size="1">
						<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="specimenType">
						<bean:message key="arrayType.specimenType" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="specimenType" styleClass="formFieldSized" styleId="state" size="1">
						<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel" width="140">
					<label for="comments">
						<bean:message key="arrayType.comments"/>
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments"/>
				</td>
			</tr>
			
			<tr>
				<td class="formTitle" colspan="5">
					<label for="capacity">
						<bean:message key="arrayType.capacity" />
					</label>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="oneDimensionCapacity">
						<bean:message key="arrayType.oneDimensionCapacity" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="twoDimensionCapacity">
						<bean:message key="arrayType.twoDimensionCapacity" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" colspan="3">
				<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton">
									<bean:message  key="buttons.submit" />
								</html:submit>
							</td>
						</tr>
					</table>
				<!-- action buttons end -->
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</html:form>