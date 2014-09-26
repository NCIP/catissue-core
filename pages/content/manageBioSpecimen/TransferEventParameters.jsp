<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.actionForm.TransferEventParametersForm"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<head>
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxcontainer.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.js"></script>
<link rel="stylesheet" type="text/css"	href="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxToolbar/codebase/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxTree/codebase/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script>
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
<script language="JavaScript" type="text/javascript"	src="javascripts/de/jquery-1.3.2.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">

<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->
</head>
<%


List<String[]> initValueForContainer = (List<String[]>)request.getAttribute("initValues");
String[] containerValues=initValueForContainer.get(0);
String containerName=containerValues[0];
String pos1=containerValues[1];
String pos2=containerValues[2];
String className=(String)request.getAttribute(Constants.CLASS_NAME);
String sptype=(String)request.getAttribute(Constants.TYPE);
String collectionProtocolId=(String)request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
Long transferEventParametersId=(Long)request.getAttribute("transferEventParametersId");
%>
<script language="JavaScript">

function toStoragePositionChange(element)
{
	var autoDiv = document.getElementById("AutoDiv");
	var manualDiv = document.getElementById("ManualDiv");

	if(element.value == 1)
	{
		manualDiv.style.display='none';
		autoDiv.style.display  = 'block';
	}
	else
	if(element.value == 2)
	{
		autoDiv.style.display  = 'none';
		manualDiv.style.display = 'block';
	}
}
function submitForm()
{
	document.forms[0].submit();
}
</SCRIPT>
<script language="JavaScript">
//declaring DHTMLX Drop Down controls required variables
var containerDropDownInfo, scGrid;
var scGridVisible = false;





function showPopUp() 
{
	var storageContainer =document.getElementById("storageContainerDropDown").value;
    if(storageContainer!="")
	{
		loadDHTMLXWindowForMultipleSpecimen('storageContainerDropDown','pos1','pos2','<%=className%>','<%=sptype%>','<%=collectionProtocolId%>','pageOfTransfer')
		
	}
	else
	{
	<% String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&selectedContainerName="
				+ "storageContainerDropDown&pos1=pos1&pos2=pos2&containerId=containerId"
				+ "&holdSpecimenType=" + sptype
				+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS + "=" + className + "&"
				+ Constants.CAN_HOLD_COLLECTION_PROTOCOL + "=" + collectionProtocolId;
%>
	mapButtonClickedOnNewSpecimen("<%=url%>",'transferEvents');
	}
}
//will be called whenever a participant is selected from the participant grid/dropdown
function containerOnRowSelect(id,ind)
{	
	document.getElementsByName('storageContainer')[0].value = id;
	document.getElementById(containerDropDownInfo['dropDownId']).value = scGrid.cellById(id,ind).getValue();
	hideGrid(containerDropDownInfo['gridDiv']);
	scGridVisible = false;
	document.getElementById("pos1").value="";
	document.getElementById("pos2").value="";
}


function onContainerListReady()
	{
		var containerName = '${newSpecimenForm.selectedContainerName}';
		if(containerName != "" && containerName != 0 && containerName != null)
			containerOnRowSelect(containerName,0);
	}
	


function doOnLoad()
{
var className="<%=className%>";
var sptype="<%=sptype%>";
var collectionProtocolId="<%=collectionProtocolId%>";
//var containerName=document.getElementById("storageContainerDropDown").value;
var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&isTransferEvent=true&transferEventParametersId=<%=transferEventParametersId%>&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="+className+"&specimenType="+sptype+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId;


	//Drop Down components information
	containerDropDownInfo = {gridObj:"storageContainerGrid", gridDiv:"storageContainer", dropDownId:"storageContainerDropDown", pagingArea:"storageContainerPagingArea", infoArea:"storageContainerInfoArea", onOptionSelect:"containerOnRowSelect", actionToDo:url,
	callBackAction:onContainerListReady, visibilityStatusVariable:scGridVisible, propertyId:'selectedContainerName'};
	// initialising grid
	scGrid = initDropDownGrid(containerDropDownInfo,false); 
}

function makeContainerGridReadonly() 
{
	$('#scDropDownIddiv :input').attr('readonly', 'readonly');
	$('#scDropDownIddiv :img').attr('readonly', 'readonly');
	if("Virtual"=='<%=containerName%>')
	{
		$('#pos1').attr('style', 'display:none');
		$('#pos2').attr('style', 'display:none');
	}
	else
	{
		$('#pos1').attr('readonly', 'readonly');
		$('#pos2').attr('readonly', 'readonly');
	}
}

function setContainerValues()
{
<%if(!"".equalsIgnoreCase(containerName)) {%>
	document.getElementById(containerDropDownInfo['dropDownId']).value='<%=containerName%>';
	document.getElementById("pos1").value='<%=pos1%>';
	document.getElementById("pos2").value='<%=pos2%>';
<%if(!transferEventParametersId.equals(0L)){%>
	makeContainerGridReadonly();
	document.getElementById("mapButton").style.visibility='hidden';
<%}}%>	
}
function updateStorageContainerValue()
	{
		var containerName=document.getElementById(containerDropDownInfo['dropDownId']).value;
		document.getElementById("selectedContainerName").value=containerName;
	}
</script>

<body onload="doOnLoad();initWindow();setContainerValues()">
<%@ include file="/pages/content/common/ActionErrors.jsp" %>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="id" />
	<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
	<tr>
         <td align="left" class="tr_bg_blue1" >
			<span class="blue_ar_b">&nbsp;<bean:message  key="eventparameters"/> &quot;<em><bean:message key="transfereventparameters"/></em>&quot;</span></td>
        </tr>
		<tr>
          <td  class="showhide1"></td>
        </tr>
		<!-- Name of the transfereventparameters -->
<!-- User -->
		<tr>
          <td colspan="4" class="showhide"><table width="100%" border="0" cellpadding="1" cellspacing="0">
               <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
                  <td  align="left" valign="middle" class="black_ar" width="30%"><html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
				</html:select></td>
				<td width="1%" colspan="4"></td>
                </tr>

<!-- date -->
		<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/></td>
                  <td colspan="" align="left" >
				  <logic:notEmpty name="currentEventParametersDate">
					<ncombo:DateTimeComponent name="dateOfEvent"
					  id="dateOfEvent"
							  formName="transferEventParametersForm"
			                  month='${requestScope.eventParametersMonth}'
							  year='${requestScope.eventParametersYear}'
							  day='${requestScope.eventParametersDay}'
							  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
							  value='${requestScope.currentEventParametersDate}'
							  styleClass="black_ar" />
				</logic:notEmpty>
				<logic:empty name="currentEventParametersDate">
					<ncombo:DateTimeComponent name="dateOfEvent"
						  id="dateOfEvent"
						  formName="transferEventParametersForm"
						  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
						  styleClass="black_ar" size="5" />
				</logic:empty>
                    <span class="grey_ar_s capitalized">[${datePattern}]</span>&nbsp;</td>

                  <td align="center" class="black_ar" width="1%"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="8%"><bean:message key="eventparameters.time"/></td>
                  <td colspan="0" align="left">
				
				
				
				<div style="width:100%"  class="black_ar"><div style="float:left;">
						<select id="timeInHours1" styleClass="black_ar" styleId="timeInHours" size="1"> 
						<logic:iterate id="hourListd" name="hourList">
								
									<option value="<bean:write name='hourListd'/>" selected><bean:write name='hourListd'/></option>
								
							</logic:iterate>
						<select></div><div style="float:left;">&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
						</div><div style="float:left;">
						<select id="timeInMinutes1" styleClass="black_ar" styleId="timeInMinutes" size="1"> 
						<logic:iterate id="minutesId" name="minutesList">
								
									<option value="<bean:write name='minutesId'/>" selected><bean:write name='minutesId'/></option>
								
							</logic:iterate>
						</select></div><div>&nbsp;&nbsp;<bean:message key="eventparameters.timeinminutes"/>
						</div>
<html:hidden property="timeInHours" value='${transferEventParametersForm.timeInHours}'/>
<html:hidden property="timeInMinutes"  value='${transferEventParametersForm.timeInMinutes}'/>

						</div>
								<script>
							 window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
							  var timeHr = new dhtmlXCombo("timeInHours1","timeInHours1","100px");
							  timeHr.setSize(60);
							  timeHr.enableFilteringMode(true);
							  if('${transferEventParametersForm.timeInHours}'!=0){
								timeHr.setComboValue('${transferEventParametersForm.timeInHours}');
							  }
							  timeHr.attachEvent("onChange", function(){
								document.getElementsByName("timeInHours")[0].value= timeHr.getSelectedValue();
							  });  

							   var timeMinute = new dhtmlXCombo("timeInMinutes1","timeInMinutes1","100px");
							  timeMinute.setSize(60);
							  timeMinute.enableFilteringMode(true);
							  if('${transferEventParametersForm.timeInMinutes}'!=0){
								timeMinute.setComboValue('${transferEventParametersForm.timeInMinutes}');
							  }
							  timeMinute.attachEvent("onChange", function(){
								document.getElementsByName("timeInMinutes")[0].value= timeMinute.getSelectedValue();
							  });  

						</script>	
	  
				
				
				</td>
                </tr>



<!-- fromPosition -->
		<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="transfereventparameters.fromposition"/></td>
                  <td colspan="5" align="left"><span class="black_ar">
                    <html:hidden property="fromPositionDimensionOne" value='${requestScope.posOne}'/>
				<html:hidden property="fromPositionDimensionTwo" value='${requestScope.posTwo}' />
				<html:hidden property="fromStorageContainerId" value='${requestScope.storContId}' />
				<html:hidden property="containerId" styleId="containerId"/>
				<!-- Checking the fromPositionData is null -->
				<logic:empty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="black_ar" size="30" styleId="fromPosition" property="fromPosition" readonly="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</logic:empty>

				<logic:notEmpty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="black_ar" size="30" styleId="fromPositionData" property="fromPositionData" readonly="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</logic:notEmpty>
                  </span></td>
                </tr>

<!-- toPosition -->
		<tr>
			<td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="transfereventparameters.toposition"/></td>
			<%-- n-combo-box start --%>
			<td class="black_ar" align = "left" colspan="4">
				<div  id="manualDiv" style="display:block">
											<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<td class="groupelements" size="48">
								
								<td width="50%" align="left" class="black_ar">
						<html:hidden property="storageContainer" styleId="selectedContainerName" />
						<div>
							<table border="0" width="29%" id="outerTable2" cellspacing="0" cellpadding="0">
								<tr>
									<td class="black_ar" align="left" width="88%" height="100%" >
										<div id="scDropDownIddiv" class="x-form-field-wrap" >
											<input id="storageContainerDropDown"
													onkeydown="keyNavigation(event,containerDropDownInfo,scGrid,scGridVisible);"
													onKeyUp="autoCompleteControl(event,containerDropDownInfo,scGrid);"
													onClick="noEventPropogation(event)"
													autocomplete="off"
													size="30"
													class="black_ar x-form-text x-form-field x-form-focus"/><img id="scDropDownId" style="top : 0px !important;" class="x-form-trigger x-form-arrow-trigger" 
												onclick="showHideStorageContainerGrid(event,'storageContainer','storageContainerDropDown',containerDropDownInfo);"
												src="images/uIEnhancementImages/s.gif"/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									<div id="storageContainer" class="black_ar" style="z-index: 100"
										onClick="noEventPropogation(event)">
									<div id="storageContainerGrid" class="black_ar" style="height: 40px;"
										onClick="noEventPropogation(event)"></div>
									<div id="storageContainerPagingArea" class="black_ar" onClick="noEventPropogation(event)"></div>
									<div id="storageContainerInfoArea" class="black_ar" onClick="noEventPropogation(event)"></div>
									</div>
									</td>
								</tr>
							</table>
					</td>
					</td>

							</td>
							<td>&nbsp;&nbsp;</td>
							<td class="groupelements"  width="10%">
								<html:text styleClass="black_ar"  size="3" styleId="pos1" property="pos1"  disabled= "false" style="display:block" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
							</td>
							<td class="groupelements" width="10%">
								<html:text styleClass="black_ar"  size="3" styleId="pos2" property="pos2" disabled= "false" style="display:block" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
							</td>
							<td class="groupelements">
								<html:button styleClass="black_ar" property="containerMap" styleId="mapButton" onclick="showPopUp()">
											<bean:message key="buttons.map"/>
								</html:button>
							</td>
						</tr>
					</table>
					</div>
			</td>



<%--		 n-combo-box end --%>

		</tr>




<!-- comments -->
		<tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t">
						<bean:message key="eventparameters.comments"/></td>
                  <td colspan="5" align="left">
						<html:textarea styleClass="black_ar"  styleId="comments" property="comments" cols="73" rows="4"/>
				</td>
                </tr>
				</table></td>
        </tr>

<!-- buttons -->
		<tr>
          <td class="buttonbg">
			<input type="button" value="Submit"  class="blue_ar_b"  onclick="updateStorageContainerValue();'${requestScope.changeAction}';submitForm()" />
			</td>
        </tr>

		</table>

	  </td>
	 </tr>

	 <!-- NEW TRANSFER_EVENT_PARAMETERS ends-->

	 </html:form>
 </table>
 </body>