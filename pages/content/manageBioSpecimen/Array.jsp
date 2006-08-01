<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="ArrayAdd.do">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
	<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
			<tr>
				<td class="formMessage" colspan="3">* indicates a required field</td>
			</tr>
			
			<tr>
				<td class="formTitle" height="20" colspan="5">
					<bean:message key="array.title"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="createdBy">
						<bean:message key="array.createdBy" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="createdBy" styleClass="formFieldSized" styleId="state" size="1">
						<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="specimenClass">
						<bean:message key="array.specimenClass" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:select property="specimenClass" styleClass="formFieldSized" styleId="state" size="1">
						<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="specimenType">
						<bean:message key="array.specimenType" />
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
				<td class="formLabel">
					<label for="concentration">
						<bean:message key="array.concentration"/> 
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="concentration" property="concentration"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="quantity">
						<bean:message key="array.quantity"/> 
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="quantity"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="barcode">
						<bean:message key="array.barcode"/> 
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="barcode" property="barcode"/>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel">
					<label for="className">
						<bean:message key="array.positionInStorageContainer"/>
					</label>
				</td>
				<td class="formField">
				 	&nbsp;<bean:message key="array.storageContainerId" />	
		     		<html:text styleClass="formFieldSized3" styleId="storageContainer" maxlength="10"  property="storageContainer"/>
		     		&nbsp;<bean:message key="array.positionOne" />
		     		<html:text styleClass="formFieldSized3" styleId="positionDimensionOne" maxlength="10"  property="positionDimensionOne"/>
		     		&nbsp;<bean:message key="array.positionTwo" />
		     		<html:text styleClass="formFieldSized3" styleId="positionDimensionTwo" maxlength="10"  property="positionDimensionTwo"/>
					&nbsp;
				</td>
				<td class="formField" colspan="2">
					<html:button property="mapButton" styleClass="actionButton" styleId="Map"
						onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false" >
						<bean:message key="buttons.map"/>
					</html:button>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel" width="140">
					<label for="comments">
						<bean:message key="array.comments"/>
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments"/>
				</td>
			</tr>
			
			<tr>
				<td class="formTitle" colspan="5">
					<label for="capacity">
						<bean:message key="array.capacity" />
					</label>
				</td>
			</tr>
			
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				<td class="formLabel">
					<label for="oneDimensionCapacity">
						<bean:message key="array.oneDimensionCapacity" />
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
						<bean:message key="array.twoDimensionCapacity" />
					</label>
				</td>
				<td class="formField" colspan="3">
					<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" colspan="5">
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