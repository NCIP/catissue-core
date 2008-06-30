<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page language="java" isELIgnored="false" %>
<head>
<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script src="jss/Hashtable.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}	
</script>

<logic:notEmpty name="CPQuery">
		<script language="javascript">
			${requestScope.refreshTree}
		</script>
</logic:notEmpty>

<script language="JavaScript">

   function onSubmit()
	{
		var action = '${requestScope.CREATE_ALIQUOT_ACTION}';
		document.forms[0].action = action + "?pageOf=" + '${requestScope.PAGEOF_CREATE_ALIQUOT}' + "&operation=add&menuSelected=15&buttonClicked=submit";
		<logic:notEmpty name="CPQuery">
			document.forms[0].action = '${requestScope.action1}';
		</logic:notEmpty>
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
		var action = '${requestScope.CREATE_ALIQUOT_ACTION}';
		document.forms[0].submittedFor.value = "ForwardTo";
		document.forms[0].action = action + "?pageOf=" + '${requestScope.PAGEOF_CREATE_ALIQUOT}' + "&operation=add&menuSelected=15&buttonClicked=create";

		<logic:notEmpty name="CPQuery">
			document.forms[0].action = '${requestScope.action2}';
			document.forms[0].forwardTo.value= "CPQueryPrintAliquot";
		</logic:notEmpty>
		<logic:empty name="CPQuery">
			document.forms[0].forwardTo.value= "printAliquot";
		</logic:empty>
		
		document.forms[0].nextForwardTo.value = "success";
		document.forms[0].submit();
	}
	
	function onCheckboxClicked() 
	{
	    var action = '${requestScope.CREATE_ALIQUOT_ACTION}';
		document.forms[0].submittedFor.value = "ForwardTo";
		document.forms[0].action = action + "?pageOf=" + '${requestScope.PAGEOF_CREATE_ALIQUOT}' + "&operation=add&menuSelected=15&buttonClicked=checkbox";
		
		<logic:notEmpty name="CPQuery">
			document.forms[0].action = '${requestScope.action3}';
		</logic:notEmpty>
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
	
	function onStorageRadioClickInAliquot(element)
	{		
		var index1 =  element.name.lastIndexOf('_');
		var index2 =  element.name.lastIndexOf(')');
		var i = (element.name).substring(index1+1,index2);
		var st1 = "container_" + i + "_0";
		var pos1 = "pos1_" + i + "_1";
		var pos2 = "pos2_" + i + "_2";
		var st2="customListBox_" + i + "_0";
		var pos11="customListBox_" + i + "_1";
		var pos12="customListBox_" + i + "_2";
		var mapButton="mapButton_" + i ;
		var stContainerNameFromMap = document.getElementById(st1);
		var pos1FromMap = document.getElementById(pos1);
		var pos2FromMap = document.getElementById(pos2);    		    		
		var stContainerNameFromDropdown = document.getElementById(st2);
		var pos1FromDropdown = document.getElementById(pos11);
		var pos2FromDropdown = document.getElementById(pos12);    		    		
		var containerMapButton =  document.getElementById(mapButton);

		if(element.value == 1)
		{
			stContainerNameFromMap.disabled = true;
			pos1FromMap.disabled = true;
			pos2FromMap.disabled = true;

			containerMapButton.disabled = true;
			stContainerNameFromDropdown.disabled = true;
			pos1FromDropdown.disabled = true;
			pos2FromDropdown.disabled = true;
		}
		else if(element.value == 2)
		{
			stContainerNameFromMap.disabled = true;
			pos1FromMap.disabled = true;
			pos2FromMap.disabled = true;

			containerMapButton.disabled = true;
			stContainerNameFromDropdown.disabled = false;
			pos1FromDropdown.disabled = false;
			pos2FromDropdown.disabled = false;
		}
		else
		{
			stContainerNameFromMap.disabled = false;
			pos1FromMap.disabled = false;
			pos2FromMap.disabled = false;

			containerMapButton.disabled = false;
			stContainerNameFromDropdown.disabled = true;
			pos1FromDropdown.disabled = true;
			pos2FromDropdown.disabled = true;
		}
	}		
	
	function mapButtonClickedInAliquot(frameUrl,count)
	{
	   	var storageContainer = document.getElementById("container_" + count + "_0").value;
		frameUrl+="&storageContainerName="+storageContainer;
		NewWindow(frameUrl,'name','800','600','no');
    }
    
    function onAddToCart()
	 {	
		var action = '${requestScope.CREATE_ALIQUOT_ACTION}';
	
		document.forms[0].forwardTo.value    ="addSpecimenToCart";
		document.forms[0].action ='${requestScope.action2}';
		document.forms[0].nextForwardTo.value = "success";
		document.forms[0].submit();
	}
			
</script>
</head>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
${requestScope.messageKey}
</html:messages>
<html:errors/>

<html:form action="${requestScope.ALIQUOT_ACTION}">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="660">
<tr>
<td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
<tr>
	<td class="formMessage" colspan="3"><bean:message key="requiredfield.message"/></td>
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
			<bean:message key='${requestScope.buttonKey}'/>
		</html:button>
	</td>
</tr>
</table>
</td>
</tr>
<logic:notEqual name="pageOf" value="${requestScope.PAGEOF_ALIQUOT}">
<tr>
<td>
        <html:hidden property="id"/>
		<html:hidden property="operation" value="${requestScope.operation}"/>
		<html:hidden property="submittedFor"/>
<table summary="" cellpadding="5" cellspacing="0" border="0" width="660">
<tr>
	<td class="formTitle" height="20" colspan="4" >
		<bean:message key="aliquots.title"/>
	</td>
</tr>
<tr>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="type">
		<b>	<bean:message key="specimen.type"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.specimenClass}
		<html:hidden property="specimenClass" />
	</td>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="subType">
			<b><bean:message key="specimen.subType"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.type}
		<html:hidden property="type" />
	</td>
</tr>
<tr>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="tissueSite">
			<b><bean:message key="specimen.tissueSite"/></b> 
		</label>
	</td>
	<td class="formField" > ${aliquotForm.tissueSite}
		<html:hidden property="tissueSite" />
	</td>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="tissueSide">
		<b><bean:message key="specimen.tissueSide"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.tissueSide}
		<html:hidden property="tissueSide" />
	</td>
</tr>
<tr>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="pathologicalStatus">
			<b><bean:message key="specimen.pathologicalStatus"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.pathologicalStatus}
		<html:hidden property="pathologicalStatus" />
	</td>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="concentration">
		<b>	<bean:message key="specimen.concentration"/> </b>
		</label>
	</td>
	<td class="formField" >
		<logic:notEmpty name="aliquotForm" property="concentration">
			${aliquotForm.concentration}
			<html:hidden property="concentration" />
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
		</logic:notEmpty>
		<logic:empty name="aliquotForm" property="concentration">
			&nbsp;
		</logic:empty>
	</td>
</tr>
<tr>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="availableQuantity">
			<b><bean:message key="aliquots.initialAvailableQuantity"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.initialAvailableQuantity}
		<html:hidden property="initialAvailableQuantity" />
		&nbsp; ${requestScope.unit}
	</td>
	<td class="formRequiredLabelWithLeftBottomBorder">
		<label for="availableQuantity">
			<b><bean:message key="aliquots.currentAvailableQuantity"/> </b>
		</label>
	</td>
	<td class="formField"> ${aliquotForm.availableQuantity}
		<html:hidden property="availableQuantity" />
		&nbsp; ${requestScope.unit}
	</td>
</tr>
<tr>			
	<td class="formRequiredLabelWithLeftBottomBorder" >
	  <label for="createdDate">
		<bean:message key="specimen.createdDate"/>
	  </label>								
	 </td>
	<td class="formField" colspan="3" >
	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>				
			<td class="message" >
				<ncombo:DateTimeComponent name="createdDate"
		  			id="createdDate"
		  			formName="aliquotForm"
		  			value='${requestScope.createdDate}'
		  			styleClass="formDateSized10"/>
				<bean:message key="page.dateFormat" />&nbsp;
			</td>
		</tr>
	</table>
	</td>
</tr>
</table>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
<tr>
	<td class="formRightSubTableTitleWithBorder" width="5">
     	#
    </td>
    <logic:equal name="isSpecimenLabelGeneratorAvl" value="false">
	<td class="formRightSubTableTitleWithBorder">*
		<bean:message key="specimen.label"/>
	</td>	
	</logic:equal>   
	<logic:equal name="isSpecimenBarcodeGeneratorAvl" value="false">
		<td class="formRightSubTableTitleWithBorder">&nbsp;
			<bean:message key="specimen.barcode"/>
		</td>
	</logic:equal> 
	<td class="formRightSubTableTitleWithBorder" colspan=${requestScope.colspanValue1} >*
		<bean:message key="specimen.quantity"/>
	</td>
	<td class="formRightSubTableTitleWithBorder">*
		<bean:message key="aliquots.location"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<b><bean:message key="requestdetails.tooltip.updateAllRequestStatus"/></b>
	<input type="checkbox" name="option1" onclick="applyFirstToAll(this)"> 
	</td>
</tr>
${requestScope.JSForOutermostDataTable}
<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">

${aliquotBean.jsScript}
	<tr>
		<td class="formSerialNumberField" width="5">
	     	${counter}.
	    </td>
	    <logic:equal name="isSpecimenLabelGeneratorAvl" value="false">
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="label" property="${aliquotBean.labelKey}" disabled="false"/>
			</td>
	    </logic:equal>
	    <logic:equal name="isSpecimenBarcodeGeneratorAvl" value="false">
		    <td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="${aliquotBean.barKey}" disabled="false"/>
			</td>
	    </logic:equal>
		<td class="formField" nowrap colspan='${requestScope.colspanValue1}'>
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="${aliquotBean.qtyKey}" disabled="false"/>
			&nbsp; ${requestScope.unit}
		</td>
		<td class="formField" nowrap>
						<table border="0">
							<tr>
								<td><html:hidden styleId="${aliquotBean.containerIdStyle}" property="${aliquotBean.containerIdFromMapKey}"/>
								<html:radio value="1" onclick="onStorageRadioClickInAliquot(this)" styleId="${aliquotBean.stContSelection}" property="${aliquotBean.stContSelection}" /></td>
								<td class="formFieldNoBorders">
									<logic:equal name="operation" value="${requestScope.ADD}">									
										<bean:message key="specimen.virtuallyLocated" />
									</logic:equal>	
								</td>
							</tr>
								<tr>
								<td ><html:radio value="2" onclick="onStorageRadioClickInAliquot(this)" styleId="${aliquotBean.stContSelection}" property="${aliquotBean.stContSelection}"/></td>
								<td>
									<ncombo:nlevelcombo dataMap="${requestScope.dataMap}" 
										attributeNames="${aliquotBean.attrNames}" 
										initialValues="${aliquotBean.initValues}"  
										styleClass = "formFieldSized5" 
										tdStyleClass = "customFormField" 
										labelNames="${requestScope.labelNames}" 
										rowNumber="${aliquotBean.rowNumber}" 
										onChange = "${aliquotBean.onChange}"
										formLabelStyle="formLabelBorderless"
										disabled = "${aliquotBean.dropDownDisable}"
										tdStyleClassArray="${requestScope.tdStyleClassArray}"
										noOfEmptyCombos = "3"/>
								    	</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td ><html:radio value="3" onclick="onStorageRadioClickInAliquot(this)" styleId="${aliquotBean.stContSelection}" property="${aliquotBean.stContSelection}"/></td>
								<td class="formLabelBorderlessLeft">
									<html:text styleClass="formFieldSized10"  size="30" styleId="${aliquotBean.containerStyle}" property="${aliquotBean.containerNameFromMapKey}" disabled = "${aliquotBean.textBoxDisable}"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="${aliquotBean.pos1Style}" property="${aliquotBean.pos1FromMapKey}" disabled = "${aliquotBean.textBoxDisable}"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="${aliquotBean.pos2Style}" property="${aliquotBean.pos2FromMapKey}" disabled = "${aliquotBean.textBoxDisable}"/>
									<html:button styleClass="actionButton" styleId = "${aliquotBean.containerMapStyle}" property="${aliquotBean.containerMap}" onclick="${aliquotBean.buttonOnClicked}" disabled = "${aliquotBean.textBoxDisable}">
										<bean:message key="buttons.map"/>
									</html:button>
								</td>
							</tr>
						</table>

						</td>
			<%-- n-combo-box end --%>
	</tr>
	<logic:equal name="exceedsMaxLimit" value="true">
		<tr>
			<td>
				<bean:message key="container.maxView"/>
			</td>
		</tr>
	</logic:equal>
</logic:iterate>
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
	<td colspan="3" class="formLabelNoBackGround" width="40%">
		<html:checkbox property="disposeParentSpecimen" >
		<bean:message key="aliquots.disposeParentSpecimen" />
		</html:checkbox>
	</td>
</tr>	
<tr>					
	<td class="formFieldNoBorders" colspan="5"  height="20" >
	<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
		<bean:message key="print.checkboxLabel"/>
	</html:checkbox>
	</td>
</tr>	
<tr>
	<td colspan="5" align="right">
		<html:button styleClass="actionButton" property="submitButton" onclick="onCreate()">
			<bean:message key="buttons.submit"/>
		</html:button>
		
		<html:button styleClass="actionButton" property="submitPage" onclick="onAddToCart()">
				<bean:message key="buttons.addToCart"/>
		</html:button>
	</td>
</tr>	
</table>				
<!-- action buttons end -->
</td>
</tr>
</logic:notEqual>	<%-- 		} //If pageOf != "Aliquot Page"		--%>
</table>
<html:hidden property="specimenID"/>
<html:hidden property="spCollectionGroupId"/>
<html:hidden property="nextForwardTo" />
<html:hidden property="forwardTo" />
</html:form>
<script language="JavaScript" type="text/javascript">
function applyFirstToAll(object)
{
	var type = 'Specimen';
	//value(Specimen:1_StorageContainer_id
	if(object.checked)
		{
				var fields = document.getElementsByTagName("select");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				var firstRadioButton= true;
				var j=0;
				var firstRadioValue = 0;
				var updateFlag=false;
				var firstRadioButton = document.getElementsByName("value(radio_1)");
				for(var k = 0 ; k < firstRadioButton.length ; k++)
				 {
					if(firstRadioButton[k].checked == true)
					{
						firstRadioValue = firstRadioButton[k].value;
						if(firstRadioValue == 2)
						{
							updateFlag=true;
						}
					}
				 }
				var radioButtons = document.getElementsByTagName("input");
				for(j=0 ; j < radioButtons.length ; j++)
				 {
					if(radioButtons[j].type == 'radio' && radioButtons[j].value == firstRadioValue)
					{
						radioButtons[j].checked = true;
					}
				if((radioButtons[j].type == 'text' && radioButtons[j].name.indexOf("fromMap")>0) || (radioButtons[j].type == 'button' && radioButtons[j].name.indexOf("mapButton")>0))
					{
						if(firstRadioValue == 3)
							{
								radioButtons[j].disabled = false;
							}
						else
							{
								radioButtons[j].disabled = true;
							}
				
					}
					
				 }
				for (i=0; i<fields.length;i++)
				 {
					fields[i].disabled = true;
					if(updateFlag)
						{
							text = fields[i].name;
							fields[i].disabled = false;
							if(text.indexOf(type)>=0 && text.indexOf("StorageContainer")>=0)
							{
								if(isFirstField)
								{
									valueToSet = fields[i].value;
									isFirstField = false;
								}
								if(valueToSet != "")
								{
									fields[i].value = valueToSet;
								}
							}
						}
					}
		}
}
</script>


