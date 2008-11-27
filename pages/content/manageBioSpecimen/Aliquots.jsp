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
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script src="jss/Hashtable.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>

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
		document.forms[0].action = action + "?pageOf=" + '${requestScope.PAGEOF_CREATE_ALIQUOT}' + "&operation=add&buttonClicked=submit";
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
			
			<logic:notEqual name="aliquotForm" property="forwardTo" value="orderDetails" >
				document.forms[0].forwardTo.value= "printAliquot";
			</logic:notEqual>
			
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
		 //Get the element identifier
	var elementId = element.id;

	//Get the position of first occurrence of underscore
	var firstIndex = elementId.indexOf("_");

	//Get the position of last occurrence of underscore
	var lastIndex = elementId.lastIndexOf(")");
  
	//Retrieve the row number on which the list box is placed
	var rowNo = elementId.substring(firstIndex+1,lastIndex);
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


		var autoDiv = document.getElementById("auto_"+rowNo);
		var manualDiv = document.getElementById("manual_"+rowNo);
		if(element.value == 1)
		{
			manualDiv.style.display='none';
			autoDiv.style.display = 'none';
		}
		else if(element.value == 2)
		{
			manualDiv.style.display='none';
			autoDiv.style.display = 'block';
		}
		else
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'block';
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
	
	function checkForStoragePosition()
	{
		<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
			var autoDiv = document.getElementById("auto_${counter+1}");
			var manualDiv = document.getElementById("manual_${counter+1}");
			var selectBox = document.getElementById("value(radio_${counter+1})");
		if(selectBox.value == 1)
		{
			manualDiv.style.display='none';
			autoDiv.style.display = 'none';
		}
		else if(selectBox.value == 2)
		{
			manualDiv.style.display='none';
			autoDiv.style.display = 'block';
		}
		else
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'block';
		}

		</logic:iterate>
	}
</script>
</head>
<body onload="checkForStoragePosition()">
<html:form action="${requestScope.ALIQUOT_ACTION}">

<!------------------New code--------------->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<logic:empty name="CPQuery">
	<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head">
					<span class="wh_ar_b"><bean:message key="aliquots.header" />
					</span>
				</td>
				<td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Aliqout" width="31" height="24" />
				</td>
			</tr>
	    </table>
	</td>
  </tr>
</logic:empty>
  <tr>
  <td class="tablepadding">
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<logic:empty name="CPQuery">
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg"  border="0"alt="Edit" width="59" height="22" /></html:link></td>
        <td valign="bottom"><html:link page="/CreateSpecimen.do?operation=add&pageOf=pageOfDeriveSpecimen&virtualLocated=true"><img src="images/uIEnhancementImages/tab_derive2.gif" alt="Derive" width="56" height="22" border="0" /></html:link></td>
</logic:empty>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_aliquot.gif" alt="Aliquot" width="66" height="22" /></td>
<logic:empty name="CPQuery">
        <td valign="bottom"><html:link page="/QuickEvents.do?operation=add"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" border="0"/></html:link></td>
        <td align="left" valign="bottom" class="td_color_bfdcf3" ><html:link page="/MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu"><img src="images/uIEnhancementImages/tab_multiple2.gif" alt="Multiple" width="66" height="22" border="0" /></html:link></td>
</logic:empty>
        <td width="90%" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
<!-- Mandar : 14Nov08 table with data -->
	<table width="100%" border="0" height="98%" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
				<bean:message key="aliquots.createTitle"/>
			</span>
		</td>
	</tr>
	<tr>
          <td align="left" valign="top" class="showhide">
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<td width="1%" align="center" class="black_ar">
				<span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
				</td>
                  <td width="15%" align="left" nowrap="nowrap" class="black_ar">
					<bean:message key="createSpecimen.parentLabel"/>&nbsp;&nbsp;
				  </td>
                  <td align="left">
				  <table border="0" cellspacing="0" cellpadding="0" width="55%" >
                  <tr class="groupElements">
                    <td valign="middle" nowrap>
				 
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
						&nbsp;
					</html:radio>
					<span class="black_ar">
					Label&nbsp;&nbsp;&nbsp;
					
					<logic:equal name="aliquotForm" property="checkedButton" value="1">
						<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="specimenLabel" property="specimenLabel" disabled="false"/>
					</logic:equal>
		
					<logic:equal name="aliquotForm" property="checkedButton" value="2">
						<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="specimenLabel" property="specimenLabel" disabled="true"/>
					</logic:equal>
					</span>
				</td>
		 
                  <td align="left" valign="middle" nowrap="nowrap" >
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
					&nbsp;
					</html:radio>
					<span class="black_ar">
					
						<bean:message key="specimen.barcode"/>
					
					</span>
					&nbsp;
					<span class="black_ar">
				<logic:equal name="aliquotForm" property="checkedButton" value="1">
					<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="barcode" property="barcode" disabled="true"/>
				</logic:equal>
		
				<logic:equal name="aliquotForm" property="checkedButton" value="2">
					<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="barcode" property="barcode"/>
				</logic:equal>
				</span>
				</td>
                </tr>
				</table></td></tr>
				<tr>
				
                  <td align="center"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
				  </td>
				  <td valign="middle" class="black_ar" nowrap>
					<bean:message key="aliquots.noOfAliquots"/>
				</td>
                  <td align="left" >

				  <table border="0" cellspacing="0" cellpadding="0" >
                  <tr>
                    
				<td align="left" width="15%">
					<html:text styleClass="black_ar"  maxlength="50"  size="10" styleId="noOfAliquots" property="noOfAliquots" style="text-align:right"/></td>
				<td width="20%">&nbsp;</td>
				<td class="black_ar" width="25%">	
					<bean:message key="aliquots.qtyPerAliquot"/>
				</td>
				<td width="40%">

					<html:text styleClass="black_ar"  maxlength="50"  size="10" styleId="quantityPerAliquot" property="quantityPerAliquot" style="text-align:right"/>
				</td>
				</tr>
				</table>
				</td>
				
			</tr>
			</table></td>
        </tr>
		<tr>
	
	<td align="left" class="buttonbg" >
		<html:button styleClass="blue_ar_b" property="submitPage" onclick="onSubmit()" accesskey="Enter">
			<bean:message key='${requestScope.buttonKey}'/>
		</html:button>
	</td>
</tr>
<tr>
<td class="bottomtd"></td>
</tr>
<logic:notEqual name="pageOf" value="${requestScope.PAGEOF_ALIQUOT}">

<html:hidden property="id"/>
		<html:hidden property="operation" value="${requestScope.operation}"/>
		<html:hidden property="submittedFor"/>


<tr>
	<td align="left" class="tr_bg_blue1" ><span class="blue_ar_b">&nbsp;
		<bean:message key="aliquots.title"/>
		</span>
	</td>
</tr>
<td align="left" class="showhide1">
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
	<tr>
                <td class="noneditable">
				<label for="type">
				<strong><bean:message key="specimen.type"/></strong>
				</label>
				</td>
				<td class="noneditable">-${aliquotForm.className}
				<html:hidden property="className" />
				</td>
				<td class="noneditable">
				<label for="subType">
				<strong><bean:message key="specimen.subType"/></strong>
				</label>
				</td>
				<td class="noneditable">
					-${aliquotForm.type}
					<html:hidden property="type" />
				</td>
				</tr>
			<tr>
				<td class="noneditable">
				<label for="tissueSite">
				<strong><bean:message key="specimen.tissueSite"/></strong>
				</label>
				</td>
				<td class="noneditable">-${aliquotForm.tissueSite}
		<html:hidden property="tissueSite" />
				</td>
				<td class="noneditable">
		<label for="tissueSide">
		<strong><bean:message key="specimen.tissueSide"/></strong>
		</label>
	</td>
	<td class="noneditable"> -${aliquotForm.tissueSide}
		<html:hidden property="tissueSide" />
	</td>
</tr>
<tr>
                <td class="noneditable">
					<label for="pathologicalStatus">
			<strong><bean:message key="specimen.pathologicalStatus"/></strong>
		</label>
	</td>
	<td class="noneditable"> -${aliquotForm.pathologicalStatus}
		<html:hidden property="pathologicalStatus" />
	</td>
	<td class="noneditable">
		<label for="concentration">
		<strong><bean:message key="specimen.concentration"/></strong>
		</label>
	</td>
	<td class="noneditable" >
		<logic:notEmpty name="aliquotForm" property="concentration">
			-${aliquotForm.concentration}
			<html:hidden property="concentration" />
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
		</logic:notEmpty>
		<logic:empty name="aliquotForm" property="concentration">
			&nbsp;
		</logic:empty>
	</td>
</tr>
<tr>
	<td class="noneditable">
		<label for="availableQuantity">
			<strong><bean:message key="aliquots.initialAvailableQuantity"/></strong>
		</label>
	</td>
	<td class="noneditable"> -${aliquotForm.initialAvailableQuantity}
		<html:hidden property="initialAvailableQuantity" />
		&nbsp; ${requestScope.unit}
	</td>
	<td class="noneditable">
		<label for="availableQuantity">
			<strong><bean:message key="aliquots.currentAvailableQuantity"/></strong>
		</label>
	</td>
	<td class="noneditable"> -${aliquotForm.availableQuantity}
		<html:hidden property="availableQuantity" />
		&nbsp; ${requestScope.unit}
	</td>
	</tr>
	<tr>			
	<td class="black_ar" >
	  <label for="createdDate">
		<bean:message key="specimen.createdDate"/>
	  </label>								
	 </td>
	<td class="black_ar" colspan="3" >
	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>				
			<td class="black_ar" >
				<ncombo:DateTimeComponent name="createdDate"
		  			id="createdDate"
		  			formName="aliquotForm"
		  			value='${requestScope.createdDate}'
		  			styleClass="black_ar"/>
					<span class="grey_ar_s">
				<bean:message key="page.dateFormat" />
					</span>
			</td>
		</tr>
	</table>
	</td>
</tr>
</table>
</td>
</tr>
<tr>
          <td class="showhide1">
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr class="tableheading">
                <td width="2%" align="left" class="black_ar_b" ><bean:message key="reportedProblem.serialNumber" /></td>
		<logic:equal name="isSpecimenLabelGeneratorAvl" value="false">
				<td width="12%" align="left" nowrap="nowrap" class="black_ar_b" ><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0"/>&nbsp;<bean:message key="specimen.label"/></td>
		</logic:equal>  
		<logic:equal name="isSpecimenBarcodeGeneratorAvl" value="false">
		<td  width="12%" class="black_ar_b" >
			<bean:message key="specimen.barcode"/>
		</td>
	</logic:equal> 
                <td width="12%" align="left" nowrap="nowrap" class="black_ar_b" ><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0"/>&nbsp;<bean:message key="itemrecord.quantity"/>&nbsp;
				</td>

                <td width="43%" nowrap="nowrap" class="black_ar_b" align="left" colspan=${requestScope.colspanValue1}><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0"/>&nbsp;<bean:message key="cpbasedentry.storagelocation"/>
				</td>
<td align="right" width="19%">
	<input type="button" name="option1" class="black_ar" value="Apply first to all" size="3" onclick="applyFirstToAll(this)" onmouseover="Tip(' Apply first location to all')"
/>
	<span align="right" class="black_ar_b" >
	<!--<bean:message key="requestdetails.tooltip.updateAllRequestStatus"/>-->
	</span>&nbsp;
	
				</td>
              </tr>
	${requestScope.JSForOutermostDataTable}
<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
${aliquotBean.jsScript}
	<tr>
		<td align="left" class="black_ar" >${counter+1}.
	    </td>
		<logic:equal name="isSpecimenLabelGeneratorAvl" value="false">
		   <td >
				<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="label" property="${aliquotBean.labelKey}" disabled="false"/>
			</td>
	    </logic:equal>
	    <logic:equal name="isSpecimenBarcodeGeneratorAvl" value="false">
		    <td >
				<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="barcodes" property="${aliquotBean.barKey}" disabled="false"/>
			</td>
	    </logic:equal>
		<td >
			<html:text styleClass="black_ar"  maxlength="50"  size="2" styleId="quantity" property="${aliquotBean.qtyKey}" disabled="false" style="text-align:right"/>
			<span class="black_ar_t" valign="center" >${requestScope.unit}</span>
		</td>
		<td colspan='${requestScope.colspanValue1+1}' >
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
		<td width="20%">
			<html:hidden styleId="${aliquotBean.containerIdStyle}" property="${aliquotBean.containerIdFromMapKey}"/>
			<html:select property="${aliquotBean.stContSelection}" styleClass="black_new"
											styleId="${aliquotBean.stContSelection}" size="1"	onmouseover="showTip(this.id)"
											onmouseout="hideTip(this.id)" onchange="onStorageRadioClickInAliquot(this)">
											<html:options collection="storageList"
														labelProperty="name" property="value" />
			</html:select> 
			
		</td>
		<td width="80%" >
			<div Style="display:block" id="auto_${counter+1}" >
									<ncombo:nlevelcombo dataMap="${requestScope.dataMap}" 
										attributeNames="${aliquotBean.attrNames}" 
										initialValues="${aliquotBean.initValues}"  
										styleClass = "black_new" 
										tdStyleClass = "black_new" 
										labelNames="${requestScope.labelNames}" 
										rowNumber="${aliquotBean.rowNumber}" 
										onChange = "${aliquotBean.onChange}"
										formLabelStyle="nComboGroup"
										disabled = "false"
										tdStyleClassArray="${requestScope.tdStyleClassArray}"
										noOfEmptyCombos = "3"/>
								    	</tr>
										</table>
			</div>
			<div style="display:none" id="manual_${counter+1}"  >
					<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<td class="groupelements"><html:text styleClass="black_ar"  size="20" styleId="${aliquotBean.containerStyle}" onmouseover="showTip(this.id)" property="${aliquotBean.containerNameFromMapKey}" />&nbsp;
							</td>
							<td class="groupelements"><html:text styleClass="black_ar" size="2" styleId="${aliquotBean.pos1Style}" property="${aliquotBean.pos1FromMapKey}" />&nbsp;
							</td>
							<td class="groupelements"><html:text styleClass="black_ar"  size="2" styleId="${aliquotBean.pos2Style}" property="${aliquotBean.pos2FromMapKey}" />&nbsp;
							</td>
							<td class="groupelements"><html:button styleClass="black_ar" styleId="${aliquotBean.containerMapStyle}" property="${aliquotBean.containerMap}" onclick="${aliquotBean.buttonOnClicked}"  >
									<bean:message key="buttons.map"/>
									</html:button>
							</td>
						</tr>
					</table>
			</div>
					</td>
					</tr>
					</table>
				</td>
			<%-- n-combo-box end --%>
	</tr>
	<logic:equal name="exceedsMaxLimit" value="true">
		<tr>
			<td colspan="5" class="black_ar">
				<bean:message key="container.maxView"/>
			</td>
		</tr>
	</logic:equal>
</logic:iterate>
</table>
</td>
</tr>
<tr>
          <td align="left" valign="middle" class="bottomtd"></td>
        </tr>
		<tr>
          <td align="left" class="black_ar">
			<html:checkbox property="aliqoutInSameContainer" onclick="onCheckboxClicked()">
		<bean:message key="aliquots.storeAllAliquotes" />
		</html:checkbox>
		&nbsp;&nbsp;
		<html:checkbox property="disposeParentSpecimen" >
		<bean:message key="aliquots.disposeParentSpecimen" />
		</html:checkbox>
		</td>
		</tr>	
		<tr>
          <td align="left" class="dividerline"><span class="black_ar">
				<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
		<bean:message key="print.checkboxLabel"/>
	</html:checkbox>
	</td>
</tr>
<tr>
          <td class="toptd"></td>
        </tr>
		<tr>
          <td colspan="2" class="buttonbg">
			<html:button styleClass="blue_ar_b" property="submitButton" onclick="onCreate()">
			<bean:message key="buttons.submit"/>
		</html:button>
		
		<html:button styleClass="blue_ar_c"		property="submitPage" onclick="onAddToCart()">
				<bean:message key="buttons.addToCart"/>
		</html:button>
		&nbsp;|&nbsp; 

		<logic:empty name="CPQuery">
		<html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
		<bean:message key="buttons.cancel" />
		</html:link>
		</logic:empty>
		<logic:notEmpty name="CPQuery">
		<html:link page="/QueryManageBioSpecimen.do" styleClass="cancellink">
		<bean:message key="buttons.cancel" />
		</html:link>
		</logic:notEmpty>

		</logic:notEqual>
		</td>
        </tr>
      </table></td>
  </tr>
</table>

<html:hidden property="specimenID"/>
<html:hidden property="spCollectionGroupId"/>
<html:hidden property="nextForwardTo" />
<html:hidden property="forwardTo" />
</html:form>
</body>
<script language="JavaScript" type="text/javascript">
function applyFirstToAll(object)
{
	var type = 'Specimen';
	//value(Specimen:1_StorageContainer_id
	//if(object.checked)
	//	{
			//alert(document.getElementById("container_1_0").value);
			var position=document.getElementById("container_1_0").value;
				var fields = document.getElementById("value(radio_1)");
				
				if(fields.value == 2)
			{	
					<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
						var autoDiv = document.getElementById("auto_${counter+1}");
						var manualDiv = document.getElementById("manual_${counter+1}");
						document.getElementById("value(radio_${counter+1})").value=2;
						manualDiv.style.display='none';
						autoDiv.style.display = 'block';
					</logic:iterate>			
			}
					if(fields.value == 1)
			{	
					<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
						var autoDiv = document.getElementById("auto_${counter+1}");
						var manualDiv = document.getElementById("manual_${counter+1}");
						document.getElementById("value(radio_${counter+1})").value=1;
						manualDiv.style.display='none';
						autoDiv.style.display = 'none';
					</logic:iterate>			
			}
					if(fields.value == 3)
			{	
					<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
						var autoDiv = document.getElementById("auto_${counter+1}");
						var manualDiv = document.getElementById("manual_${counter+1}");
						document.getElementById("value(radio_${counter+1})").value=3;
						
						manualDiv.style.display='block';
						autoDiv.style.display = 'none';
						document.getElementById("container_${counter+1}_0").value=position;
					</logic:iterate>			
			}



		//}
}
</script>
<!------------------New code--------------->