<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.dto.SpecimenDTO"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<script language="JavaScript" type="text/javascript" src="jss/specimen.js"></script>
	<script src="jss/script.js"></script>
	<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>

	
	<script>
      window.dhx_globalImgPath="dhtmlx_suite/imgs/";
	  var pageOf = '${requestScope.pageOf}';
	  var isLabelGenAvl = '${requestScope.isSpecimenLabelGeneratorAvl}';
	  var selectedCPID='';
    </script>


<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>


<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />



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

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>

<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
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
												<html:text styleClass="black_ar"  name="deriveDTO" maxlength="50"  size="20" styleId="parentSpecimenLabel"
														   property="parentSpecimenLabel" disabled="false" onblur="validateLabelBarcode(this)"/>
												&nbsp;&nbsp;
											</span>
										</td>
										<td align="left" valign="middle" nowrap="nowrap">
											<input type="radio" class="" id="checkedButton" name="radioButton" value="2" 		onclick="onRadioButtonClick(this)"/>
											
											<span class="black_ar">
											<bean:message key="storageContainer.barcode"/>&nbsp;
											<html:text name="deriveDTO" styleClass="black_ar"  maxlength="50"  size="20" styleId="parentSpecimenBarcode"
													   property="parentSpecimenBarcode" disabled="false" onblur="validateLabelBarcode(this)"/>
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
							<input type="button" class="blue_ar_b" value="Select Container" id="mapButton" disabled="true" onclick="loadDHTMLXWindowForDeriveSpecimen()" />
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
										<input type="checkbox" name="aliCheckedButton" onclick="onCheckboxButtonClick(this)" />
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
						<td colspan="4" class="formLabelNoBackGround" width="40%">
							<span class="black_ar">
							<html:checkbox name="deriveDTO" property="disposeParentSpecimen">
							&nbsp;<bean:message key="aliquots.disposeParentSpecimen" />
							</html:checkbox>
							</span>
						</td>
				   </tr>
				  
				   <tr>
						<td colspan="4" align="left" class="dividerline">
							<span class="black_ar">
						</td>
				   </tr>
				  
				  <tr>
					   <td colspan="4" valign="middle">
						<table>
							<tr>
								 <td  nowrap  width="20%" colspan="1">
									 <html:checkbox name="deriveDTO" styleId="isToPrintLabel" property="isToPrintLabel" value="true" onclick="showPriterTypeLocation()">
										<span class="black_ar">
											<bean:message key="print.checkboxLabel"/>
										</span>
										</html:checkbox>
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
							<html:button styleClass="blue_ar_b" styleId="derSubmitButton" property="submitButton" onclick="submitDeriveData()">
								<bean:message key="buttons.create"/>
							</html:button>&nbsp;
							
						
						<html:button
							styleClass="blue_ar_b" property="Add Events"
							title="Add Events"
							value="Add Events"
							onclick="openEventPage('rty')">
						</html:button>
						</td>
					</tr>
				</table>
			</html:form>	
</body>
<script>
function loadDHTMLXWindowForDeriveSpecimen()
{

//alert(document.getElementById('barcode'));
var w =700;
var h =450;
var x = (screen.width / 3) - (w / 2);
var y = 0;
dhxWins = new dhtmlXWindows(); 
dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
//var pos1 = document.getElementById('pos1').value;
//var pos2 = document.getElementById('pos2').value;
var className = classNameCombo.getSelectedText();
var type = typeCombo.getSelectedText();
//var containerName = document.getElementById('containerName').value;
//var isVirtual = document.getElementById('isVirtual').value;

var url = "ShowStoragePositionGridView.do?pageOf=pageOfderivative&forwardTo=gridView&holdSpecimenClass="+className+"&holdSpecimenType="+type+"&collectionProtocolId="+selectedCPID+"&pos1=&pos2=";

dhxWins.window("containerPositionPopUp").attachURL(url);                     
//url : either an action class or you can specify jsp page path directly here
dhxWins.window("containerPositionPopUp").button("park").hide();
dhxWins.window("containerPositionPopUp").allowResize();
dhxWins.window("containerPositionPopUp").setModal(true);
dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}

function validateLabelBarcode(label)
{
	//alert(label.name);
	var barcode = document.getElementById('parentSpecimenBarcode').value;
	var loader = dhtmlxAjax.postSync("SpecimenAjaxAction.do","type=getParentDetails&label="+label.value+"&barcode="+barcode);
	if(loader.xmlDoc.responseText != null && loader.xmlDoc.responseText != '')
	{
		var response = eval('('+loader.xmlDoc.responseText+')')
		//alert(loader.xmlDoc.responseText);
		if(response.msg == 'success')
		{
			deriveLabelSubmit=true;
			deriveBarcodeSubmit=true;
			deriveDataJSON[label.name] = label.value;
			deriveDataJSON['parentSpecimenId'] = response.parentId;
			deriveDataJSON['specimenCollGroupId'] = response.scgId;
			deriveDataJSON['cpId'] = response.cpId;
			selectedCPID = response.cpId;
			enableMapButton();
			label.className = label.className.replace(/errorStyleOn/g,"");
		}
		else
		{
			deriveLabelSubmit = false;
			deriveBarcodeSubmit = false;
			label.className += " errorStyleOn";
		}
	}
	else
	{
		deriveLabelSubmit = false;
		deriveBarcodeSubmit = false;
		label.className += " errorStyleOn";
	}
}
function enableMapButton()
{
	if(submitDeriveCombo && deriveLabelSubmit && deriveBarcodeSubmit)
	{
		document.getElementById('mapButton').disabled=false;
	}
}
var deriveDataJSON = {};
var deriveLabelSubmit=false;
var deriveBarcodeSubmit=false;
var deriveCreatedOnSubmit=true;
var deriveQtySubmit=true;
var deriveConcentration=true;
var derLabelSubmit=true;
var cpId=0;
var submitDeriveCombo = true;

function validateAndProcessDeriveComboData(obj)
{
//deriveDataJSON[obj.name] = obj.getSelectedText();
obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
	if(obj.getSelectedValue()=='-1' || obj.getSelectedText()=='-- Select --' || obj.getSelectedText().trim()=="")
	{
		obj.DOMelem.className += " errorStyleOn";
		//alert(obj.DOMelem.className);
		submitDeriveCombo=false;
	}
	else
	{
		deriveDataJSON[obj.name] = obj.getSelectedText();
		
		if(obj.name=='className' && typeCombo.getComboText()=='-- Select --')
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
			typeCombo.DOMelem.className += " errorStyleOn";
			submitDeriveCombo=false;
			return;
		}
		var index = obj.DOMelem.className.indexOf("errorStyleOn");
		if(index != -1)
		{
			obj.DOMelem.className = obj.DOMelem.className.replace(/errorStyleOn/g,"");
		}
		submitDeriveCombo=true;
		
	}
	enableMapButton();
	
}
function submitDeriveData()
{
	var obj = document.getElementById('parentSpecimenLabel');
	
	if(obj!=null && obj.value.trim()=="" && obj.disabled==false)
	{
		deriveLabelSubmit=false;
		deriveBarcodeSubmit=false;
		obj.className += " errorStyleOn";
	}
	var derLabel = document.getElementById('derLabel');
	
	if(derLabel.value != null && derLabel.value.trim()=="" && derLabel.disabled==false)
	{
		derLabelSubmit=false;
		derLabel.className += " errorStyleOn";
	}
	
	if(submitDeriveCombo && deriveLabelSubmit && deriveBarcodeSubmit && deriveCreatedOnSubmit && deriveQtySubmit && derLabelSubmit)
	{
		var deriveExtidJSON = createExtIdJSON();
		//alert(deriveExtidJSON);
		document.getElementById('errorMsg').style.display='none';
		document.getElementById('errorMsg').innerHTML = '';
		var contName = document.getElementById("storageContainerPosition").value;
		//alert(contName);
		if(contName != 'Virtually Located')
		{
			deriveDataJSON["containerName"]= document.getElementById("containerName").value;
			deriveDataJSON["pos1"]= document.getElementById("pos1").value;
			deriveDataJSON["pos2"]= document.getElementById("pos2").value;
			deriveDataJSON["containerId"]= document.getElementById("containerId").value;
		}
		deriveDataJSON["className"]= classNameCombo.getSelectedText();
		deriveDataJSON["type"]= typeCombo.getSelectedText();
		deriveDataJSON["createdOn"]= document.getElementById('createdOn').value;
		deriveDataJSON["initialQuantity"]= document.getElementById('initialQuantity').value;
		
		
		var loader = dhtmlxAjax.postSync("CreateDeriveAction.do","dataJSON="+JSON.stringify(deriveDataJSON)+"&extidJSON="+JSON.stringify(deriveExtidJSON));
		if(loader.xmlDoc.responseText != null)
		{
			//alert(loader.xmlDoc.responseText);
			var response = eval('('+loader.xmlDoc.responseText+')')
			var errMsgDiv = document.getElementById('errorMsg');
			if(response.success == "success")
			{
				errMsgDiv.style.display='block';
				errMsgDiv.className='alert-success';
				errMsgDiv.innerHTML=response.msg;
				var specimenDto = JSON.parse(response.specimenDto);
				document.getElementById('derLabel').value=specimenDto.label;
				document.getElementById('derBarcode').value=specimenDto.barcode;
				document.getElementById('derSubmitButton').disabled=true;
				var nodeId= "Specimen_"+specimenDto.id;
				if(pageOf != "pageOfDeriveSpecimen")
				{
					refreshTree(null,null,null,null,nodeId);
				}

				//alert(specimenDto.id);
				//redirectTo(specimenDto.id);
				//window.location.href = "SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id="+specimenDto.id;
				//window.parent.frames[1].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+specimenDto.id+"&refresh=false&cpSearchParticipantId=&cpSearchCpId=";
			}
			else
			{
				errMsgDiv.style.display='block';
				errMsgDiv.className='alert-error';
				errMsgDiv.innerHTML=response.msg;
			}
			//alert(response);
		}
	}
	else
	{
		var msg="Unable to submit. Please resolve higlighted issue(s).";
		document.getElementById('errorMsg').style.display='block';
	document.getElementById('errorMsg').innerHTML = msg;
	document.getElementById('errorMsg').className = 'alert-error';
		//scrollToTop();
	}
}
function redirectTo(id)
{
	if(pageOf=="pageOfDeriveSpecimen")
	{
		window.location.href = "SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id="+id;
	}
	else
	{
		window.parent.frames[1].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+id+"&refresh=false&cpSearchParticipantId=&cpSearchCpId=";
	}
}
function onRadioButtonClick(element)
		{
     		if(element.value == 1)
			{
				
				document.forms[0].parentSpecimenLabel.disabled = false;
				document.forms[0].parentSpecimenBarcode.className = document.forms[0].parentSpecimenBarcode.className.replace(/errorStyleOn/g,"");
				document.forms[0].parentSpecimenBarcode.value='';
				document.forms[0].parentSpecimenBarcode.disabled = true;
			}
			else
			{
				
				document.forms[0].parentSpecimenBarcode.disabled = false;
				document.forms[0].parentSpecimenLabel.className = document.forms[0].parentSpecimenLabel.className.replace(/errorStyleOn/g,"");
				document.forms[0].parentSpecimenLabel.value='';
				document.forms[0].parentSpecimenLabel.disabled = true;
			}
		}
var labelDoc = document.getElementById('parentSpecimenLabel');

	if(pageOf=="pageOfDeriveSpecimen")
	{
	}
	else
	{
		validateLabelBarcode(labelDoc);
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

</script>