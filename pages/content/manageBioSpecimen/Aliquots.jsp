<!-- 
	This JSP page is to create/display aliquots from/of Parent Specimen.
	Author : Aniruddha Phadnis
	Date   : May 12, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String buttonKey = "";

	if(Constants.PAGEOF_ALIQUOT.equals(pageOf))
	{
		buttonKey = "buttons.submit";
	}
	else if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
	{
		buttonKey = "buttons.resubmit";
	}
%>

<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>

	<script language="JavaScript">
	
	   function onSubmit()
		{
			var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=submit";;
			document.forms[0].submit();
		}
		
		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].specimenLabel.disabled = false;
				document.forms[0].barcode.disabled = true;
			}
			else
			{
				document.forms[0].barcode.disabled = false;
				document.forms[0].specimenLabel.disabled = true;
			}
		}
		
		function onCreate()
		{
			var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
			document.forms[0].submittedFor.value = "ForwardTo";
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=create";
		    document.forms[0].submit();
		}
		
		function onCheckboxClicked() 
		{
		    var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
			document.forms[0].submittedFor.value = "ForwardTo";
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=checkbox";
		    document.forms[0].submit();
		}
		
		function setVirtuallyLocated(element)
		{
			var elementId = element.id;
			var index = elementId.indexOf("_");
			var len = elementId.length;
			var substr = elementId.substring(index+1,len);
			
			var customListBox1 = "customListBox_"+substr+"_0";
			var customListBox2 = "customListBox_"+substr+"_1";
			var customListBox3 = "customListBox_"+substr+"_2";

			var containerName = document.getElementById(customListBox1);
			var pos1 = document.getElementById(customListBox2);
			var pos2 = document.getElementById(customListBox3);

			if(element.checked)
			{
				containerName.disabled = true;
				pos1.disabled = true;
				pos2.disabled = true;
				document.forms[0].mapButton[substr-1].disabled = true;
			}
			else
			{
				containerName.disabled = false;
				pos1.disabled = false;
				pos2.disabled = false;
				document.forms[0].mapButton[substr-1].disabled = false;
				
			} 
		}
		
	</script>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="660">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
	<tr>
		<td class="formMessage" colspan="3">* indicates a required field</td>
	</tr>
	
	<tr>
		<td class="formTitle" height="20" colspan="7">
			<bean:message key="aliquots.createTitle"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNoticeNoBottom">*
			<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
				&nbsp;
			</html:radio>
		</td>
		<td class="formRequiredLabelLeftBorder" width="160" nowrap>
				<label for="parentId">
					<bean:message key="createSpecimen.parentLabel"/>
				</label>
		</td>
		<td class="formField">
			<logic:equal name="aliquotForm" property="checkedButton" value="1">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenLabel" property="specimenLabel" disabled="false"/>
			</logic:equal>
			
			<logic:equal name="aliquotForm" property="checkedButton" value="2">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenLabel" property="specimenLabel" disabled="true"/>
			</logic:equal>
		</td>
		<td class="formRequiredLabelBoth" width="5">*</td>
		<td class="formRequiredLabel">
			<bean:message key="aliquots.noOfAliquots"/>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" styleId="noOfAliquots" property="noOfAliquots"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice"><span class="hideText">*</span>
			<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
				&nbsp;
			</html:radio>
		</td>
		<td class="formRequiredLabel" width="73">
				<label for="barcode">
					<bean:message key="specimen.barcode"/>
				</label>
		</td>
		<td class="formField">
			<logic:equal name="aliquotForm" property="checkedButton" value="1">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcode" property="barcode" disabled="true"/>
			</logic:equal>
			
			<logic:equal name="aliquotForm" property="checkedButton" value="2">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcode" property="barcode"/>
			</logic:equal>
		</td>
		<td class="formLabel" colspan="2">
			<bean:message key="aliquots.qtyPerAliquot"/>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" styleId="quantityPerAliquot" property="quantityPerAliquot"/>
		</td>
	</tr>
	
	<tr>
		<td colspan="5">
			&nbsp;
		</td>
		<td align="right">
			<html:button styleClass="actionButton" property="submitPage" onclick="onSubmit()">
				<bean:message key="<%=buttonKey%>"/>
			</html:button>
		</td>
	</tr>
	</table>
</td>
</tr>

<%
	String operation = Utility.toString(Constants.OPERATION);
	AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
	String unit = "";

	if(form != null)
	{
		unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
	}

	if(!Constants.PAGEOF_ALIQUOT.equals(pageOf))
	{
%>

	
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
	<tr>
		<td>
			<html:hidden property="id"/>
			<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
			<html:hidden property="submittedFor"/>
		</td>
	</tr>
	
	<%--tr>
		<td class="formMessage" colspan="3">* indicates a required field</td>
	</tr--%>
	
	<tr>
		<td class="formTitle" height="20" colspan="3">
			<bean:message key="aliquots.title"/>
		</td>
	</tr>
	
	<%--tr>
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
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
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="aliquots.initialAvailableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="initialAvailableQuantity" property="initialAvailableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="aliquots.currentAvailableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
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
	    <td class="formRightSubTableTitle">*
			<bean:message key="specimen.label"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="specimen.quantity"/>
		</td>
		<td class="formRightSubTableTitle">&nbsp;
			<bean:message key="specimen.barcode"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="aliquots.location"/>
		</td>
	</tr>
	
	<%=ScriptGenerator.getJSForOutermostDataTable()%>
	
	<%
		Map aliquotMap = new HashMap();
		int counter=0;

		if(form != null)
		{
			counter = Integer.parseInt(form.getNoOfAliquots());
			aliquotMap = form.getAliquotMap();
		}

		/* Retrieving a map of available containers */
		Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
		String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;


		for(int i=1;i<=counter;i++)
		{
			String labelKey = "value(Specimen:" + i + "_label)";
			String qtyKey = "value(Specimen:" + i + "_quantity)";
			String barKey = "value(Specimen:" + i + "_barcode)";
			String virtuallyLocatedKey = "value(Specimen:" + i + "_virtuallyLocated)";
			String containerKey = "value(Specimen:" + i + "_StorageContainer_id)";
			String pos1Key = "value(Specimen:" + i + "_positionDimensionOne)";
			String pos2Key = "value(Specimen:" + i + "_positionDimensionTwo)";
			
			String virtuallyLocatedStyleId = "chkBox_"+ i;
			//Preparing data for custom tag
			String[] attrNames = {containerKey, pos1Key, pos2Key};

			String[] initValues = new String[3];
			initValues[0] = (String)aliquotMap.get("Specimen:" + i + "_StorageContainer_id");
			initValues[1] = (String)aliquotMap.get("Specimen:" + i + "_positionDimensionOne");
			initValues[2] = (String)aliquotMap.get("Specimen:" + i + "_positionDimensionTwo");

			String rowNumber = String.valueOf(i);
			String noOfEmptyCombos = "3";
			String styClass = "formFieldSized5";
			String tdStyleClass = "customFormField";
			String onChange = "onCustomListBoxChange(this)";

			String containerStyleId = "customListBox_" + rowNumber + "_0";
			String pos1StyleId = "customListBox_" + rowNumber + "_1";
			String pos2StyleId = "customListBox_" + rowNumber + "_2";

			String buttonOnClicked = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=" + containerStyleId + "&amp;xDimStyleId=" + pos1StyleId + "&amp;yDimStyleId=" + pos2StyleId + "','name','810','320','yes');return false";
	%>
	<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="label" property="<%=labelKey%>" disabled="false"/>
			</td>
			<td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="<%=qtyKey%>" disabled="false"/>
				&nbsp; <%=unit%>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" disabled="false"/>
			</td>
			<td class="formField" nowrap>
				<html:checkbox property="<%=virtuallyLocatedKey%>" onclick="setVirtuallyLocated(this)" value="true" styleId="<%=virtuallyLocatedStyleId%>"/><bean:message key="specimen.virtuallyLocated" />
				<ncombo:containermap dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>"
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"
											buttonName="mapButton" 
											value="Map"
											buttonOnClick = "<%=buttonOnClicked%>"
											formLabelStyle="formLabelBorderless"
											buttonStyleClass="actionButton" />
			</td>
			
		</tr>
	<%
		} //For
	%>
	</table>
</td>
</tr>

<tr>
	<td colspan="4">&nbsp;</td>
</tr>

<tr>
  <td align="left" colspan="2">
	<!-- action buttons begins -->
	<table cellpadding="4" cellspacing="0" border="0">
	<tr>
	
	    <td colspan="3" class="formLabelNoBackGround" width="40%">
			<html:checkbox property="aliqoutInSameContainer" onclick="onCheckboxClicked()">
			<bean:message key="aliquots.storeAllAliquotes" />
			</html:checkbox>
		</td>
		<td width="50%">
			&nbsp;
		</td>
		<td align="right">
			<html:button styleClass="actionButton" property="submitButton" onclick="onCreate()">
				<bean:message key="buttons.create"/>
			</html:button>
		</td>
		
		
		<%--td>
			<html:reset styleClass="actionButton">
				<bean:message key="buttons.reset"/>
			</html:reset>
		</td--%> 
	</tr>
	</table>				
	<!-- action buttons end -->
	</td>
</tr>
<%
	} //If pageOf != "Aliquot Page"
%>
</table>
<html:hidden property="specimenID"/>
</html:form>