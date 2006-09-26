<!-- 
	This is the aliquot summary page.
	Author : Aniruddha Phadnis
	Date   : Jul 12, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<%
	AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
	String unit = "";

	if(form != null)
	{
		unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
	}
%>

<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
		
	<tr>
		<td class="formTitle" height="20" colspan="3">
			<bean:message key="aliquots.summaryTitle"/>
		</td>
	</tr>
	
	<%--tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="spCollectionGroupId">
				<bean:message key="specimen.specimenCollectionGroupId"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="spCollectionGroupId" property="spCollectionGroupId" readonly="true"/>
		</td>
	</tr--%>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="type">
				<bean:message key="specimen.type"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenClass" property="specimenClass" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="subType">
				<bean:message key="specimen.subType"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="type" property="type" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSite">
				<bean:message key="specimen.tissueSite"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSite" property="tissueSite" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSide">
				<bean:message key="specimen.tissueSide"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSide" property="tissueSide" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="pathologicalStatus">
				<bean:message key="specimen.pathologicalStatus"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="pathologicalStatus" property="pathologicalStatus" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="specimen.availableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="concentration">
				<bean:message key="specimen.concentration"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="concentration" property="concentration" readonly="true"/>
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
		</td>
	</tr>
	</table>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	<tr>
		<td class="formLeftSubTableTitle" width="5">
	     	#
	    </td>
	    <td class="formLeftSubTableTitle" width="5">
	     	Id
	    </td>
	    <td class="formRightSubTableTitle">
			<bean:message key="specimen.quantity"/>
		</td>
		<td class="formRightSubTableTitle">
			<bean:message key="specimen.barcode"/>
		</td>
		<td class="formRightSubTableTitle">
			<bean:message key="aliquots.location"/>
		</td>
	</tr>
	
	<%
		Map aliquotMap = new HashMap();
		int counter=0;
        Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
		if(form != null)
		{
			counter = Integer.parseInt(form.getNoOfAliquots());
			aliquotMap = form.getAliquotMap();
		}

		for(int i=1;i<=counter;i++)
		{
			String qtyKey = "value(Specimen:" + i + "_quantity)";
			String barKey = "value(Specimen:" + i + "_barcode)";
			String containerKey = "value(Specimen:" + i + "_StorageContainer_label)";
			String pos1Key = "value(Specimen:" + i + "_positionDimensionOne)";
			String pos2Key = "value(Specimen:" + i + "_positionDimensionTwo)";
			String idKey = "Specimen:" + i + "_id";
			
			String id = Utility.toString(aliquotMap.get(idKey));
	%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>
		    <td class="formField" width="5">
		     	<%=id%>
		    </td>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="<%=qtyKey%>" readonly="true"/>
				&nbsp; <%=unit%>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" readonly="true"/>
			</td>
			<td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" property="<%=containerKey%>" readonly="true"/>
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