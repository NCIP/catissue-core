<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.dto.SpecimenDTO"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXGrid.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css" href="css/catissue_suite.css">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="jss/ajax.js" type="text/javascript"></script>

<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlx.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTree.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmXTreeCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXGridCell.js"></script>

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>

<script type="text/javascript" src="jss/tag-popup.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>

<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/json2.js" type="text/javascript"></script>
<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script	src="dhtmlx_suite/ext/dhtmlxcombo_whp.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/specimen.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>

	<script>
      window.dhx_globalImgPath="dhtmlx_suite/imgs/";
	  var pageOf = '${requestScope.pageOf}';
	  var isLabelGenAvl = '${requestScope.isSpecimenLabelGeneratorAvl}';
	  var selectedCPID='';
	  var deriveLabel='';
    </script>

</head>
<script>
function initPrepareSpecimenType()
{
	prepareSpecimenTypeOptions('${cellTypeListJSON}','${molecularTypeListJSON}','${tissueTypeListJSON}','${fluidTypeListJSON}');
}
</script>

<body onload="initCombo(); initPrepareSpecimenType()">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4" valign="middle" class="bottomtd"></td>
				</tr>
				<tr class="tablepadding">
					 <td width="4%" class="td_tab_bg">
							<img src="images/spacer.gif" alt="spacer" width="50" height="1">
					 </td>
					 <td valign="bottom">
							<img src="images/uIEnhancementImages/tab_derive.gif" alt="Derive" width="56" height="22"      border="0"/>
					 </td>
					 <td width="90%" align="left" valign="bottom" class="td_tab_bg">&nbsp;</td>
				</tr>
				<tr>
					 <td colspan="4" valign="middle" class="bottomtd"></td>
				</tr>
		</table>				
	
		<html:form action="/CPQueryCreateSpecimen.do">
				<!-- Mandar 13Nov08 -->
					<table width="100%"  border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
					 <tr>
							<td colspan="4" align="left">
								<div id="errorMsg" style="display:none"/>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="left">
								<div id="printErrorMsg" style="display:none"/>
							</td>
						</tr>
					    <tr>
							<td colspan="4" align="left" class="tr_bg_blue1">
								<span class="blue_ar_b">&nbsp;
									<bean:message key="parent.specimen.details.label"/>
								</span>
							</td>
						</tr>
						
						<tr class="tr_alternate_color_lightGrey">
							<td align="left" class="black_ar align_right_style">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
									</span>
									<bean:message key="parent.specimen.details.label"/>
							</td>
							
							<td colspan="3" align="left" valign="middle" nowrap>
								<table width="55%" border="0" cellspacing="0" cellpadding="0" style="border-collapse: collapse;">
									<tr>
										<td valign="middle" nowrap>
											<input type="radio" class="" id="checkedButton" name="radioButton" value="1"
											            onclick="onRadioButtonClick(this)"/>
											
											<span class="black_ar">
												<bean:message key="specimen.label"/>&nbsp;
												<html:text styleClass="black_ar"  name="deriveDTO" maxlength="50"  size="21" styleId="parentSpecimenLabel"
														   property="parentSpecimenLabel" disabled="false" onblur="validateLabelBarcode(this,'label')"/>
												&nbsp;&nbsp;
											</span>
										</td>
										<td align="left" valign="middle" nowrap="nowrap">
											<input type="radio" class="" id="checkedButton" name="radioButton" value="2" 		onclick="onRadioButtonClick(this)"/>
											
											<span class="black_ar">
											<bean:message key="storageContainer.barcode"/>&nbsp;
											<html:text name="deriveDTO" styleClass="black_ar"  maxlength="50"  size="20" styleId="parentSpecimenBarcode"
													   property="parentSpecimenBarcode" disabled="false" onblur="validateLabelBarcode(this,'barcode')"/>
											</span>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						
						<tr>
							<td align="left" class="black_ar align_right_style">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
								</span>
									<bean:message key="specimen.type"/>
							</td>
										
							<td class="black_ar" align="left">
								<html:select name="deriveDTO" property="className"  
										     styleClass="formFieldSized19" styleId="className" size="1">
										     <html:options collection="specimenClassList"
									                  labelProperty="name" property="value" />
						        </html:select>
							</td>
							
							<td class="black_ar align_right_style">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
							   </span>
							   <bean:message key="specimen.subType"/>
							</td>
							<td align="left" class="black_ar" >
								<html:select name="deriveDTO" property="type" 
											 styleClass="formFieldSized19" styleId="type" size="1">
											  <html:options collection="cellType"
									                  labelProperty="name" property="value" />
								</html:select>
							</td>
						</tr>
					  <tr class="tr_alternate_color_lightGrey">										
							<td align="left" class="black_ar align_right_style">
							<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
								</span>
								<bean:message key="specimen.label"/>
							</td>
							
							<td align="left">
								
								<html:text styleClass="black_ar" size="30" disabled="false" name="deriveDTO" styleId="derLabel" property="label" onblur="processDeriveData(this)"/>
							</td>
							<td align="left" class="align_right_style">
								<span class="black_ar"><bean:message key="specimen.barcode"/></span>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" size="30" disabled="false" name="deriveDTO" styleId="derBarcode" property="barcode" onblur="processDeriveData(this)"/>
							</td>
					  </tr>
					 <tr>										
							<td align="left" class="black_ar align_right_style">
								<bean:message key="specimen.concentration"/>
							</td>
							<td align="left">
								<span class="grey_ar">
								<html:text styleClass="black_ar" size="10" maxlength="10"  name="deriveDTO" styleId="concentration"
										   property="concentration" style="text-align:right" disabled="true" onblur="processDeriveData(this)"/>
										   <bean:message key="specimen.concentrationUnit"/>
										   <div id="concentrationErrorMsg" style="display:none; color:red;">
									 </div>
								</span>
							</td>
							<td align="left" class="align_right_style">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" />
								</span>
								<span class="black_ar"><bean:message key="specimen.quantity"/></span>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" size="8" name="deriveDTO" maxlength="10"  styleId="initialQuantity" property="initialQuantity" onblur="processDeriveData(this)"
								 style="text-align:right"/>
								<span id="unitSpan" class="black_ar">${requestScope.unitSpecimen}</span>
								<html:hidden property="unit"/>
								<div id="quantityErrorMsg" style="display:none; color:red;">
									 </div>
							</td>
					  </tr>
					  
					  <tr class="tr_alternate_color_lightGrey">
							<td align="left" class="black_ar align_right_style">
								<bean:message key="specimen.createdDate"/>
									
							</td>
							<td align="left">
							<input type="text" name="createdOn" class="black_ar"
									   id="createdOn" size="10" onclick="doInitCalendar('createdOn',false,'${uiDatePattern}');" onblur="processDeriveData(this)" value='<fmt:formatDate value="${deriveDTO.createdOn}" pattern="${datePattern}" />'/>
									 <span class="grey_ar_s capitalized"> [<bean:message key="date.pattern" />]</span>&nbsp;
							</td>
							<td colspan="2"align="left">&nbsp;</td>
					</tr>
					
					<tr>
						<td align="left" class="black_ar align_right_style">
							<bean:message key="specimen.positionInStorageContainer"/>
						</td>
						<td colspan="3">
						<html:hidden name="deriveDTO" property="containerName" styleId="containerName"/>
						<html:hidden name="deriveDTO" property="pos1" styleId="pos1"/>
						<html:hidden name="deriveDTO" property="pos2" styleId="pos2"/>
						<html:hidden name="deriveDTO" property="containerId" styleId="containerId"/>
							<input type="text" size="30" maxlength="255"  class="black_ar"  value='Virtually Located' readonly style="border:0px;" id="storageContainerPosition" title="Virtually Located"/>
							<a href="#" onclick="javascript:loadDHTMLXWindowForDeriveSpecimen();return false">
							<img src="images/uIEnhancementImages/grid_icon.png" alt="Displays the positions for the selected container"  width="16" height="16" border="0" style="vertical-align: middle" title="Displays the positions for the selected container"></a>
							
							<a href="#" onclick="javascript:openViewMap();return false">
			<img src="images/uIEnhancementImages/Tree.gif" style="vertical-align: middle" alt="Displays the containers in tree view" title="Displays the containers in tree view" width="16" height="16" border="0">
		</a>
						</td>
						
					</tr>
					
					<tr class="tr_alternate_color_lightGrey">
						<td align="left" valign="top" class="black_ar_t align_right_style"><bean:message key="specimen.comments"/></td>
						<td colspan="3" align="left">
							<html:textarea styleClass="black_ar" name="deriveDTO" cols="67" rows="4" styleId="comments" property="comments"
										   onblur="processDeriveData(this)"/>
						</td>
					</tr>
					
     				<tr>
						<td width="20%"  class="black_ar align_right_style">
							<bean:message key="specimen.externalIdentifier"/>
						</td>
						<td width="30%">
							<a id="addExternalId" title="Add New External Identifier" class="link" onclick=  "showAddExternalIdDiv()">Add New</a>
						</td>
						<td  width="50%" class="black_ar" colspan="2">
							<div id="addExternalIdDiv" style="display:none;">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr valign="bottom">
										<td width="44%"> 
											<input id="extIdName" name="extIdName" type="text" class="black_ar" size="20" maxlength="255" class="black_ar" />
										</td>
										<td width="44%">
											<input id="extIdValue" type="text" class="black_ar" size="20" maxlength="255" style="text-align:right;" class="black_ar" />
										</td>
										<td width="44%">
											<input id="addEditExtIdButton" name="addEditExtIdButton" type="button" value="Add" class="black_ar" onclick="addEditExtIdTag(this)" />
										</td>
									</tr>
								  </table>
							</div>
						</td>
					</tr>
					
					 <tr>
								<td width="20%" > &nbsp;	</td>
								<td colspan="3" align="left"  valign="middle">
									<ul id="externalIDList" class="tagEditor">
											<c:if test="${not empty specimenDTO.externalIdentifiers}">
													<c:forEach var="externalId" items="${specimenDTO.externalIdentifiers}">
														<c:if test="${not empty externalId.name}">
														<li id="li${externalId.id}" title="Edit">
															<span id="Ext_${externalId.id}" name="ExtIds" onclick="editTag(this)">${externalId.name} - ${externalId.value}</span>
															<a title="Delete" onclick="deleteTag(this)">X</a>
															<input type="hidden" name="Ext_${externalId.id}Status" id="Ext_${externalId.id}Status" value=${externalId.status}>
														</li>
														</c:if>
													</c:forEach>		
											</c:if>
									</ul>
								 </td>
					</tr>
					
					
					
					<tr>
						<td colspan="4" valign="middle" class="bottomtd"></td>
					</tr>
					
					<tr>
						<td colspan="4" valign="middle" class="tr_bg_blue1">
								<span class="blue_ar_b">&nbsp;<bean:message key="cpbasedentry.aliquots"/></span>
						</td>
				   </tr>
				   
				    <tr>
						<td colspan="4" valign="middle">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="35%" class="black_ar">
										<input type="checkbox" name="aliCheckedButton" id="aliquotChk" onclick="onCheckboxButtonClick(this)" />
										<bean:message key="specimen.aliquot.message"/>
									</td>
									<td width="25%" class="black_ar">
										<bean:message key="aliquots.noOfAliquots"/>
										<input type="text" id="noOfAliquots" name="noOfAliquots" class = "formFieldSized5" style="text-align:right"disabled="true" />
									</td>
									<td width="40%" class="black_ar">
										<bean:message key="specimenArrayAliquots.qtyPerAliquot"/>
										<input type="text" id="quantityPerAliquot" name="quantityPerAliquot" class = "formFieldSized5" disabled="true" style="text-align:right"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="left" class="dividerline">
							<span class="black_ar">
						</td>
				   </tr>
				   <tr>
						<td colspan="2" class="formLabelNoBackGround" width="40%">
							<span class="black_ar">
							<html:checkbox name="deriveDTO" property="disposeParentSpecimen">
							&nbsp;<bean:message key="aliquots.disposeParentSpecimen" />
							</html:checkbox>
							</span>
						</td>
						<td colspan="2">
							<html:checkbox name="deriveDTO" styleId="isToPrintLabel" property="isToPrintLabel" value="true" onclick="showPriterTypeLocation()">
										<span class="black_ar">
											<bean:message key="print.checkboxLabel"/>
										</span>
										</html:checkbox>
						</td>
				   </tr>
				  <tr>
					   <td colspan="4" valign="middle">
						<table>
							<tr>
								 <td  nowrap  width="20%" colspan="1">
									 
								  </td>

							  </tr>
						 </table>
						 </td>
				   </tr>
					
					<tr>
						<td colspan="4" class="bottomtd"></td>
					</tr>
					<tr>
						<td colspan="4" align="left" class="buttonbg">
							<div id="createDiv" style="display:block"><html:button styleClass="blue_ar_b" styleId="derSubmitButton" property="submitButton" onclick="submitDeriveData()">
								<bean:message key="buttons.create"/>
							</html:button>&nbsp;
							</div>
						<div id="eventdiv" style="display:none">
						<html:button styleId="eventButton"
							styleClass="blue_ar_b" property="Add Events"
							title="Add Events"
							value="Add Events" onclick="openEventPage()">
							Add events
						</html:button>&nbsp;|&nbsp;
						<html:button
							styleClass="blue_ar_b" property="Add To Specimen List"
							title="Add To Specimen List"
							value="Add To Specimen List"
							onclick="ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')">
						</html:button>
						</div>
						<input type="hidden" id="assignTargetCall" name="assignTargetCall" value=""/>
<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>
<%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
						</td>
					</tr>
				</table>
			</html:form>	
</body>
<script>
var deriveDataJSON = {};
var deriveLabelSubmit=false;
var deriveBarcodeSubmit=false;
var deriveCreatedOnSubmit=true;
var deriveQtySubmit=true;
var deriveConcentration=true;
var derLabelSubmit=true;
var cpId=0;
var submitDeriveCombo = true;
var labelDoc = document.getElementById('parentSpecimenLabel');
	if(pageOf=="pageOfDeriveSpecimen")
	{
	}
	else
	{
		validateLabelBarcode(labelDoc,'label');
	}
document.forms[0].parentSpecimenBarcode.disabled = true;
document.getElementById('checkedButton').checked=true;
<logic:equal name="isSpecimenLabelGeneratorAvl" value="true">
document.getElementById('derLabel').disabled = true;
document.getElementById('derLabel').value="AutoGenerated";
</logic:equal>
<logic:equal name="isBarcodeGeneratorAvl" value="true">
document.getElementById('derBarcode').disabled = true;
document.getElementById('derBarcode').value="AutoGenerated";
</logic:equal>

function onCheckboxButtonClick(chkbox)
{
	
	if(chkbox.checked)
	{
		document.getElementById('noOfAliquots').disabled = false;
		document.getElementById('quantityPerAliquot').disabled = false;
		
	}
}

function openViewMap()
{
var className = classNameCombo.getSelectedText();
var sptype = typeCombo.getSelectedText();
	
	var parentSpecimenLabel=document.getElementById("parentSpecimenLabel").value;
		var parentSpecimenBarcode=document.getElementById("parentSpecimenBarcode").value;
		var frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&selectedContainerName=containerName&pos1=pos1&pos2=pos2&containerId=containerId"
						+ "&holdSpecimenClass="+className+"&parentSpecimenLabel="+parentSpecimenLabel+"&parentSpecimenBarcode="+parentSpecimenBarcode+ "&holdSpecimenType="+sptype	+ "&holdCollectionProtocol=" + selectedCPID;
		mapButtonClickedOnSpecimen(frameUrl,'newSpecimenPage','containerName');
}
</script>