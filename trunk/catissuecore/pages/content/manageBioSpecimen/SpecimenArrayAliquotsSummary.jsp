<!-- 
	This is the Specimen Array Aliquots summary page.
	Author : Jitendra Agrawal	
	Date   : 23rd Sep, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:errors/>

<html:form action="<%=Constants.SPECIMEN_ARRAY_ALIQUOT_ACTION%>">
<%
	SpecimenArrayAliquotForm form = (SpecimenArrayAliquotForm)request.getAttribute("specimenArrayAliquotForm");	
%>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
		
		<tr>
			<td class="formTitle" height="20" colspan="3">
				<bean:message key="specimenArrayAliquots.summaryTitle"/>
			</td>
		</tr>
	
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="specimenArrayType">
					<bean:message key="specimenArrayAliquots.specimenArrayType"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenArrayType" property="specimenArrayType" readonly="true"/>
			</td>
		</tr>
				
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="specimenClass">
					<bean:message key="specimenArrayAliquots.specimenClass"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenClass" property="specimenClass" readonly="true"/>
			</td>
		</tr>
					
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="specimenType">
					<bean:message key="specimenArrayAliquots.specimenType"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="specimenTypes" styleClass="formFieldVerySmallSized" styleId="state" size="4" multiple="true" disabled="true">
					<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		</tr>		
	</table>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
		<tr>
			<td class="formLeftSubTableTitle" width="5">
				#
			</td>
			<td class="formRightSubTableTitle">
				<bean:message key="specimenArrayAliquots.label"/>
			</td>					
			<td class="formRightSubTableTitle">
				<bean:message key="specimenArrayAliquots.barcode"/>
			</td>
			<td class="formRightSubTableTitle">
				<bean:message key="specimenArrayAliquots.location"/>
			</td>
		</tr>	
		<%
			Map aliquotMap = new HashMap();
			int counter=0;

			if(form != null)
			{
				counter = Integer.parseInt(form.getAliquotCount());
				aliquotMap = form.getSpecimenArrayAliquotMap();
			}

			for(int i=1;i<=counter;i++)
			{				
				String labelKey = "value(SpecimenArray:" + i + "_label)";
				String barKey = "value(SpecimenArray:" + i + "_barcode)";
				String containerKey = "value(SpecimenArray:" + i + "_StorageContainer_name)";
				String pos1Key = "value(SpecimenArray:" + i + "_positionDimensionOne)";
				String pos2Key = "value(SpecimenArray:" + i + "_positionDimensionTwo)";
				
		%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>		   
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="label" property="<%=labelKey%>" readonly="true"/>				
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" readonly="true"/>
			</td>
			<td class="formField" nowrap>
				<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" property="<%=containerKey%>" readonly="true"/>
					&nbsp;
				<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" property="<%=pos1Key%>" readonly="true"/>
					&nbsp;
				<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" property="<%=pos2Key%>" readonly="true"/>
			</td>
		</tr>
	<%
		} //For
	%>
	</table>
</td>
</tr>
</table>
</html:form>