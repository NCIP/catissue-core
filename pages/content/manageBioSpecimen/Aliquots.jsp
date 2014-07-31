<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.bean.AliquotBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page language="java" isELIgnored="false" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<% AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");


%>
<head>
<script src="jss/ajax.js"></script>
<script src="jss/script.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script src="jss/Hashtable.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>


<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>

<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">

<script language="JavaScript" type="text/javascript" src="jss/aliquots.js"></script>
<script language="JavaScript">
//declaring DHTMLX Drop Down controls required variables
<%

List aliquotBeanList= (List)request.getAttribute("aliquotBeanList");

for(int i=1;i<=aliquotBeanList.size();i++){
%>
var containerDropDownInfo_<%=i%>, scGrid_<%=i%>;
var scGridVisible_<%=i%> = false;

var gridDivObject_<%=i%> ;

<%}%>


function showPopUp(storageContainerDropDown,selectedContainerName,positionDimensionOne,positionDimensionTwo,containerId,specimenClassName,spType,cpId,storageContainerIdFromMap) 
{
	//alert(document.getElementById('value(Specimen:1_StorageContainer_id_fromMap)').value);
	var storageContainer =document.getElementById(storageContainerDropDown).value;
			if(storageContainer!="")
			{
				loadDHTMLXWindowForMultipleSpecimen(storageContainerDropDown,positionDimensionOne,positionDimensionTwo,specimenClassName,spType,cpId,"pageOfAliquot");
			}
			else
			{
				var frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
					"selectedContainerName=" + storageContainerDropDown +
					"&pos1=" + positionDimensionOne +
					"&pos2=" + positionDimensionTwo +
					"&containerId=" +containerId +
					"&StorageContainerIdFromMap=" + storageContainerIdFromMap +
					"&holdSpecimenClass="+specimenClassName +
					"&holdSpecimenType="+spType +
					"&holdCollectionProtocol=" + cpId ;
					frameUrl+="&storageContainerName="+storageContainer;
					//alert(frameUrl);
					openPopupWindow(frameUrl,'newSpecimenPage');
			}
}

function doOnLoad()
{
var className="<%=form.getClassName()%>";
var sptype="<%=form.getType()%>";
var collectionProtocolId="<%=form.getColProtId()%>";

var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="
+className+"&specimenType="+sptype+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId;

<%
for(int i=1;i<=aliquotBeanList.size();i++){
%>
	
	//Drop Down components information
	containerDropDownInfo_<%=i%> = {gridObj:"storageContainerGrid_<%=i%>", gridDiv:"storageContainer_<%=i%>", dropDownId:"storageContainerDropDown_<%=i%>", pagingArea:"storageContainerPagingArea_<%=i%>", infoArea:"storageContainerInfoArea_<%=i%>", onOptionSelect:
	function (id,ind)
		{
		
			var containerName=document.getElementById("storageContainerDropDown_<%=i%>").value;
			document.getElementsByName('value(Specimen:<%=i%>_StorageContainer_id_fromMap)')[0].value = id;
			document.getElementById(containerDropDownInfo_<%=i%>['dropDownId']).value = scGrid_<%=i%>.cellById(id,ind).getValue();
			hideGrid(containerDropDownInfo_<%=i%>['gridDiv']);
			scGridVisible_<%=i%> = false;
			document.getElementsByName('value(Specimen:<%=i%>_positionDimensionOne_fromMap)')[0].value = "";
			document.getElementsByName('value(Specimen:<%=i%>_positionDimensionTwo_fromMap)')[0].value = "";
		}
		, actionToDo:url, callBackAction:
		function(){
		document.getElementsByName('value(Specimen:<%=i%>_StorageContainer_id_fromMap)')[0].value = "";
		}
			, visibilityStatusVariable:scGridVisible_<%=i%>, propertyId:'selectedContainerName_<%=i%>'};
	// initialising grid
	scGrid_<%=i%> = initDropDownGrid(containerDropDownInfo_<%=i%>,false); 
	<%}%>
}

function setContainerValues()
{
	<%
	for(int i=1;i<=aliquotBeanList.size();i++)
	{
		AliquotBean aliquotBean=(AliquotBean)aliquotBeanList.get(0);
	%>
		document.getElementById(containerDropDownInfo_<%=i%>['dropDownId']).value = "<%=aliquotBean.getAliquotMap().get("Specimen:"+i+"_StorageContainer_name_fromMap")%>";
		<%}%>
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

			<logic:equal name="aliquotForm" property="forwardTo" value="orderDetails" >
				document.forms[0].forwardTo.value= "orderDetails";
			</logic:equal>

		</logic:empty>
		document.forms[0].nextForwardTo.value = "success";
		document.forms[0].submit();
	}

	function onCheckboxClicked()
	{
		var checkbox = document.getElementById("aliquotchk");
		if(checkbox.checked)
		{
			var action = '${requestScope.CREATE_ALIQUOT_ACTION}';
			document.forms[0].submittedFor.value = "ForwardTo";
			document.forms[0].action = action + "?pageOf=" + '${requestScope.PAGEOF_CREATE_ALIQUOT}' + "&operation=add&menuSelected=15&buttonClicked=checkbox";

			<logic:notEmpty name="CPQuery">
				document.forms[0].action = '${requestScope.action3}';
			</logic:notEmpty>
			document.forms[0].submit();
		}
	}



	function mapButtonClickedInAliquot(frameUrl,count)
	{
	   	var storageContainer = document.getElementById("container_" + count + "_0").value;
		frameUrl+="&storageContainerName="+storageContainer;
		NewWindow(frameUrl,'name','800','600','no');
    }

    /* function onAddToCart()
	 {
		var action = '${requestScope.CREATE_ALIQUOT_ACTION}';

		document.forms[0].forwardTo.value    ="addSpecimenToCart";
		document.forms[0].action ='${requestScope.action2}';
		document.forms[0].nextForwardTo.value = "success";
		document.forms[0].submit();
	} */

	function checkForStoragePosition()
	{
		<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">
			var manualDiv = document.getElementById("manual_${counter+1}");
			manualDiv.style.display='block';
		</logic:iterate>
	}

</script>
</head>
<body onload="doOnLoad();initWindow();checkForStoragePosition();setContainerValues()">
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
						<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="specimenLabel" property="specimenLabel" disabled="false" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
					</logic:equal>

					<logic:equal name="aliquotForm" property="checkedButton" value="2">
						<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="specimenLabel" property="specimenLabel" disabled="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
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
					<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barcode" disabled="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</logic:equal>

				<logic:equal name="aliquotForm" property="checkedButton" value="2">
					<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barcode" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
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
				<td class="black_ar" width="25%" noWrap>
					<bean:message key="aliquots.qtyPerAliquot"/>
				</td>
				<td width="5%">&nbsp;</td>
				<td width="40%" >
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
				<td class="noneditable">${aliquotForm.className}
				<html:hidden styleId="className" property="className" />
				</td>
				<td class="noneditable">
				<label for="subType">
				<strong><bean:message key="specimen.subType"/></strong>
				</label>
				</td>
				<td class="noneditable">
					 ${aliquotForm.type}
					<html:hidden styleId="type" property="type" />
				</td>
				</tr>
			<tr>
				<td class="noneditable">
				<label for="tissueSite">
				<strong><bean:message key="specimen.tissueSite"/></strong>
				</label>
				</td>
				<td class="noneditable">${aliquotForm.tissueSite}
		<html:hidden property="tissueSite" />
				</td>
				<td class="noneditable">
		<label for="tissueSide">
		<strong><bean:message key="specimen.tissueSide"/></strong>
		</label>
	</td>
	<td class="noneditable">${aliquotForm.tissueSide}
		<html:hidden property="tissueSide" />
	</td>
</tr>
<tr>
                <td class="noneditable">
					<label for="pathologicalStatus">
			<strong><bean:message key="specimen.pathologicalStatus"/></strong>
		</label>
	</td>
	<td class="noneditable">${aliquotForm.pathologicalStatus}
		<html:hidden property="pathologicalStatus" />
	</td>
	<td class="noneditable">
		<label for="concentration">
		<strong><bean:message key="specimen.concentration"/></strong>
		</label>
	</td>
	<td class="noneditable" >
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
	<td class="noneditable">
		<label for="availableQuantity">
			<strong><bean:message key="aliquots.initialAvailableQuantity"/></strong>
		</label>
	</td>
	<td class="noneditable">${aliquotForm.initialAvailableQuantity}
		<html:hidden property="initialAvailableQuantity" />
		&nbsp; ${requestScope.unit}
	</td>
	<td class="noneditable">
		<label for="availableQuantity">
			<strong><bean:message key="aliquots.currentAvailableQuantity"/></strong>
		</label>
	</td>
	<td class="noneditable">${aliquotForm.availableQuantity}
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
		  			pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
		  			styleClass="black_ar"/>
					 <span class="grey_ar_s capitalized"> [${datePattern}]</span>&nbsp;
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


		<% if((!form.isGenerateLabel())) {	%>
			<td width="12%" align="left" nowrap="nowrap" class="black_ar_b" ><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0"/>&nbsp;<bean:message key="specimen.label"/></td>
		<%} %>

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
	<input type="button" name="option1" class="black_ar" value="Apply first to all" size="3" onclick="applyFirstToAll()" onmouseover="Tip(' Apply first location to all')"
/>
	<span align="right" class="black_ar_b" >
	<!--<bean:message key="requestdetails.tooltip.updateAllRequestStatus"/>-->
	</span>&nbsp;

				</td>
              </tr>
<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">

	<tr>
		<td align="left" class="black_ar" >${counter+1}.
	    </td>
<% if((!form.isGenerateLabel())) {	%>
		   <td >
				<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="${aliquotBean.labelKey}" property="${aliquotBean.labelKey}" disabled="false" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
			</td>
<%} %>
	    <logic:equal name="isSpecimenBarcodeGeneratorAvl" value="false">
		    <td >
				<html:text styleClass="black_ar"  maxlength="50"  size="17" styleId="${aliquotBean.barKey}" property="${aliquotBean.barKey}" disabled="false" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
			</td>
	    </logic:equal>
		<td >
			<html:text styleClass="black_ar"  maxlength="50"  size="10" styleId="${aliquotBean.qtyKey}" property="${aliquotBean.qtyKey}" disabled="false" style="text-align:right" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
			<span class="black_ar_t" valign="center" >${requestScope.unit}</span>
		</td>
		<td colspan='${requestScope.colspanValue1+1}' >
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
		<td width="80%" >
			
			<div style="display:block" id="manual_${counter+1}"  >
					<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<!--td class="groupelements"><html:text styleClass="black_ar"  size="20" styleId="${aliquotBean.containerStyle}" onmouseover="showTip(this.id)" property="${aliquotBean.containerNameFromMapKey}" />&nbsp;
							</td-->
							<td class="groupelements" size="48">
								
								<td width="50%" align="left" class="black_ar">
								<html:hidden property="${aliquotBean.containerIdFromMapKey}" styleId="${aliquotBean.containerIdFromMapKey}"/>
						<!--input type="hidden" name="selectedContainerName_${counter+1}" styleId="selectedContainerName_${counter+1}" value="" /-->
						<div>
							<table border="0" width="29%" id="outerTable2" cellspacing="0" cellpadding="0">
								<tr>
									<td class="black_ar" align="left" width="88%" height="100%" >
										<div id="scDropDownIddiv_${counter+1}" class="x-form-field-wrap" >
											<input id="storageContainerDropDown_${counter+1}"
													onkeydown="keyNavigation(event,containerDropDownInfo_${counter+1},scGrid_${counter+1},scGridVisible_${counter+1});"
													onKeyUp="autoCompleteControl(event,containerDropDownInfo_${counter+1},scGrid_${counter+1});"
													onClick="noEventPropogation(event)"
													autocomplete="off"
													size="30"
													class="black_ar x-form-text x-form-field x-form-focus"/><img id="scDropDownId" style="top : 0px !important;" class="x-form-trigger x-form-arrow-trigger" 
												onclick="showHideStorageContainerGrid(event,'storageContainer_${counter+1}','storageContainerDropDown_${counter+1}',scGridVisible_${counter+1},containerDropDownInfo_${counter+1},scGrid_${counter+1});"
												src="images/uIEnhancementImages/s.gif"/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									<div id="storageContainer_${counter+1}" class="black_ar" style="z-index: 100"
										onClick="noEventPropogation(event)">
									<div id="storageContainerGrid_${counter+1}" class="black_ar" style="height: 40px;"
										onClick="noEventPropogation(event)"></div>
									<div id="storageContainerPagingArea_${counter+1}" class="black_ar" onClick="noEventPropogation(event)"></div>
									<div id="storageContainerInfoArea_${counter+1}" class="black_ar" onClick="noEventPropogation(event)"></div>
									</div>
									</td>
								</tr>
							</table>
					</td>
					</td>

							</td>
							<td class="groupelements" style="padding-left:25"><html:text styleClass="black_ar" size="1" styleId="${aliquotBean.pos1Style}" property="${aliquotBean.pos1FromMapKey}" />&nbsp;
							</td>
							<td class="groupelements"><html:text styleClass="black_ar"  size="1" styleId="${aliquotBean.pos2Style}" property="${aliquotBean.pos2FromMapKey}" />&nbsp;
							</td>
							<script>
							
							</script>
							<td class="groupelements"><html:button styleClass="black_ar" styleId="${aliquotBean.containerMapStyle}" property="${aliquotBean.containerMap}" 
							onclick="showPopUp('storageContainerDropDown_${counter+1}','${aliquotBean.containerNameFromMapKey}','${aliquotBean.pos1Style}','${aliquotBean.pos2Style}','1','${aliquotForm.className}','${aliquotForm.type}','${aliquotForm.colProtId}','value(Specimen:${counter+1}_StorageContainer_id_fromMap)')"  >
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
			<html:checkbox property="aliqoutInSameContainer" styleId = "aliquotchk" onclick="onCheckboxClicked()">
				<bean:message key="aliquots.storeAllAliquotes" />
			</html:checkbox>
				&nbsp;&nbsp;
			<html:checkbox property="disposeParentSpecimen" >
				<bean:message key="aliquots.disposeParentSpecimen" />
			</html:checkbox>
		 </td>
		</tr>
        <tr>
		<td colspan="3" align="left" class="dividerline">
			<span class="black_ar">
		</td>
		</tr>

		<tr>
          <td valign="center" align="left">
		  <table border="0"><tr><td nowrap>
		  							<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
									<span class="black_ar">
										<bean:message key="print.checkboxLabel"/>
									</span>
								</html:checkbox>
								<td>
								<td id="plcol2">
								<td>
					   			<%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 				   </td>
        					  	</td>
		               </tr></table>

			 				  </td>
		   				</tr>
			<!--  End : Displaying   printer type and location -->
<tr>
          <td class="toptd"></td>
        </tr>
		<tr>
          <td colspan="2" class="buttonbg">
			<html:button styleClass="blue_ar_b" property="submitButton" onclick="onCreate()">
			<bean:message key="buttons.submit"/>
		</html:button>

		</logic:notEqual>
		</td>
        </tr>
      </table></td>
  </tr>
</table>

<html:hidden property="specimenID"/>
<html:hidden styleId="colProtId" property="colProtId"/>
<html:hidden property="spCollectionGroupId"/>
<html:hidden property="nextForwardTo" />
<html:hidden property="forwardTo" />
<html:hidden property="generateLabel" />
</html:form>
</body>
<script language="JavaScript" type="text/javascript">
function applyFirstToAll()
{
		//var containerName= document.getElementsByName('value(Specimen:1_StorageContainer_id_fromMap)')[0].value;
		
		var containerName=document.getElementById('storageContainerDropDown_1').value;
		var containerId=document.getElementsByName('value(Specimen:1_StorageContainer_id_fromMap)')[0].value;
		
			<logic:iterate id="aliquotBean" name="aliquotBeanList" indexId="counter">	
				document.getElementsByName('value(Specimen:${counter+2}_StorageContainer_id_fromMap)')[0].value = containerId;
				document.getElementById(containerDropDownInfo_${counter+2}['dropDownId']).value =containerName;// scGrid_${counter+2}.cellById(containerName,0).getValue();
				hideGrid(containerDropDownInfo_${counter+2}['gridDiv']);
				scGridVisible_${counter+2} = false;
				document.getElementsByName('value(Specimen:${counter+2}_positionDimensionOne_fromMap)')[0].value = "";
				document.getElementsByName('value(Specimen:${counter+2}_positionDimensionTwo_fromMap)')[0].value = "";
			</logic:iterate>
}
<logic:notEqual name="pageOf" value="${requestScope.PAGEOF_ALIQUOT}">
showPriterTypeLocation();
</logic:notEqual>
</script>
<!------------------New code--------------->