<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@ page import="edu.common.dynamicextensions.xmi.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>


<head>
<style>
.active-column-1 {width:200px}
</style>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/fileUploader.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="jss/calendarComponent.js"></script>
<script src="jss/script.js" type="text/javascript"></script>

<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />

<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script src="jss/json2.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>

<script>
var comboData = new Array();
var statusId = new Array();
var isConsentResponseWithdrawn = false;

function checkDisable(){
	var hideCheck = true;
	for(var cnt = 0;cnt<comboData.length;cnt++){
		var selectedField = comboData[cnt];
		if(selectedField.getSelectedValue()=='Withdrawn'){
			//alert('Withdrawn');
			document.getElementById("disableConsentCheckboxDiv").style.display = "block";
			hideCheck = false;
		}
	}
	if(hideCheck){
		document.getElementById("disableConsentCheckboxDiv").style.display = "none";
		document.getElementsByName("disableConsentCheckbox")[0].checked  = false;
	}
}
</script>


			<input type="hidden" id= "consentLevelId" name="consentLevelId" value="${consentsDto.consentLevelId}">
			<input type="hidden" id= "consentLevel" name="consentLevel" value="${consentsDto.consentLevel}">
			
			
			<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
			<tr>
				<td>
					<div id="error" class="alert alert-error" style="display:none">
						<strong>Error!</strong><span id="errorMsg"></span>
					</div>
					<div class="alert alert-success" id="success" style="display:none">
					   Consents updated Sucessfully.
					</div>
				</td>
		   </tr>
				<tr><td></td></tr>
				<tr>
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
										
						<div style="margin-top:2px;">
							<bean:message key="collectionprotocolregistration.consentform"/>
						</div>
					</span>
				  </td>
				</tr>
				<tr>
					<td>
					
						<table width="100%" border="0" cellspacing="0" cellpadding="3">
								<%--Signed URL --%>				
								<tr>
									<td class="noneditable" width="39%">
										&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.signedurlconsent"/>
										
									</td>
									<td class="noneditable">
										<logic:equal name="consentLevel" value="participant">
												<input type="text" size="30" maxlength="255" class="black_ar"  value='${consentsDto.consentUrl}' id="consentUrl" />
										</logic:equal>
										<logic:notEqual name="consentLevel" value="participant">
											${consentsDto.consentUrl}
										</logic:notEqual>
										
									</td>
								</tr>
								<%--Witness Name --%>									
								<tr>
									<td class="noneditable">
										&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.witnessname"/>
									</td>	
									<td class="noneditable">
										<logic:equal name="consentLevel" value="participant">
											<select name="witnessId"  id="witnessIdCombo">
												<logic:iterate id="witnessList" name="witnessList">
													<logic:notEqual name="witnessList"  property='name' value="${consentsDto.witnessId}">
														<option value="<bean:write name='witnessList' property='value'/>"><bean:write name="witnessList" property="name"/></option>
													</logic:notEqual>
													<logic:equal name="witnessList"  property='name' value="${consentsDto.witnessId}">
														<option value="<bean:write name='witnessList' property='value'/>" selected><bean:write name="witnessList" property="name"/></option>
													</logic:equal>
												</logic:iterate>
											</select>
											<script>
												 window.dhx_globalImgPath="dhtmlx_suite/imgs/";
												  var witness = new dhtmlXCombo("witnessIdCombo","witnessIdCombo","100px");
												  //response_combo.enableFilteringMode(true);
												  witness.setSize(200);
												  if('${consentsDto.witnessId}'!=0){
													witness.setComboValue('${consentsDto.witnessId}');
												  }
												 // comboData.push(response_combo);
												  //statusId.push("${consentTierDTO.consentTierId}");
											</script>
													
										</logic:equal>
										<logic:notEqual name="consentLevel" value="participant">
											${consentsDto.witnessName}
										</logic:notEqual>
									
									
									</td>
								</tr>
								<tr>
									<td class="noneditable">
										&nbsp;&nbsp;&nbsp;<bean:message key="collectionprotocolregistration.consentdate"/>
									</td>	
									<td class="noneditable">
										<logic:equal name="consentLevel" value="participant">
												<input type="text" size="10" name = "consentDate" class="black_ar"  value='<fmt:formatDate value="${consentsDto.consentDate}" pattern="${datePattern}" />' id="consentDate"  onclick="doInitCalendar('consentDate',false,'${uiDatePattern}');" />
												<span class="grey_ar_s capitalized"> [<bean:message key="date.pattern" />]</span>&nbsp;
										</logic:equal>
										<logic:notEqual name="consentLevel" value="participant">
											<fmt:formatDate value="${consentsDto.consentDate}" pattern="${datePattern}" />
										</logic:notEqual>
										
									</td>
								</tr>
						</table>
					
					
					
					</td>
				</tr>
				
				<tr>
					<td>
						<table cellpadding="3" cellspacing="0" border="0" width="100%" id="consentTable">
							<%-- Serial No # --%>	
							<tr>
								<td class="tableheading">
									<div align="left">
									<bean:message key="requestlist.dataTabel.serialNo.label" />
									</div>
								</td>
								<%-- Title ( Consent Tiers) --%>	
								<td class="tableheading">
									<div>	
									<bean:message key="collectionprotocolregistration.consentTiers" />
									</div>	
								</td>
								<%-- Title ( Consent Tiers) --%>	
								<td class="tableheading">
									<div>	
									<bean:message key="collectionprotocolregistration.participantResponses" />
									</div>	
								</td>
								<%--Title (Participant response) --%>	
								<logic:equal name="consentLevel" value="participant">
									<td class="tableheading"  width="31%">
									</td>
								</logic:equal>
								<logic:notEqual name="consentLevel" value="participant">
								<td class="tableheading">
									<div align="left">
									<bean:message key="consent.responsestatus" />
									</div>
								</td>
								</logic:notEqual>
							</tr>
							<c:set var="count" value='1' scope="page"/>
							<c:forEach var="consentTierDTO" items="${consentsDto.consentTierList}">	
								<tr>
									<td class='black_ar'>
									${count}
									</td>
									<%-- Get Consents # --%>	
									<td class='black_ar' width="31%">
									${consentTierDTO.consentStatment}
									</td>
									
									<logic:equal name="consentLevel" value="participant">
										<td class='black_ar' width="31%">
										<select name="participantResponse"  id="${consentTierDTO.id}">
										<c:forEach items="${requestScope.specimenResponseList}" var="response" >
											<option value="${response.value}">${response.name}</option>        
										</c:forEach>	
										</select>
										<script>
										  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
										  var response_combo = new dhtmlXCombo("${consentTierDTO.id}","${consentTierDTO.id}","100px");
										  //response_combo.enableFilteringMode(true);
										  response_combo.setSize(200);
										  response_combo.setComboValue('${consentTierDTO.participantResponses}');
										  response_combo.attachEvent("onChange",checkDisable);
										  comboData.push(response_combo);
										  statusId.push("${consentTierDTO.id}");
										  if('${consentTierDTO.participantResponses}'=='Withdrawn'){
											isConsentResponseWithdrawn = true;
										  }
										</script>
										</td>
										<td align="left" class='black_ar'  width="31%"></td>
									</logic:equal>
									<logic:notEqual name="consentLevel" value="participant">
									<td class='black_ar' width="31%">
										${consentTierDTO.participantResponses}
									
									</td>
									<td align="left" class='black_ar'>
									
									<select name="consentStatus"  id="${consentTierDTO.id}">
										<c:forEach items="${requestScope.specimenResponseList}" var="response" >
											<option value="${response.value}">${response.name}</option>        
										</c:forEach>	
									</select>
									<script>
										  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
										  var response_combo1 = new dhtmlXCombo("${consentTierDTO.id}","${consentTierDTO.id}","100px");
										  //response_combo.enableFilteringMode(true);
										  response_combo1.setSize(200);
										  response_combo1.setComboValue('${consentTierDTO.status}');
										  response_combo1.attachEvent("onChange",checkDisable);
										  comboData.push(response_combo1);
										  statusId.push("${consentTierDTO.id}");
										   if('${consentTierDTO.status}'=='Withdrawn'){
											isConsentResponseWithdrawn = true;
										  }
										  
									</script>
									
									</td>
									</logic:notEqual>
								</tr>
								<c:set var="count" value='${pageScope.count+1}' scope="page" />
							</c:forEach>
						</table>
					</td>
				</tr>			

				<tr>
					<td align="left" colspan="2" class="buttonbg">
						<div style="display:none;" id="disableConsentCheckboxDiv"><input type='checkbox' name="disableConsentCheckbox"  style="vertical-align: middle" ><span style="vertical-align:middle" class="black_ar">Disable specimens?</span>
						</div>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2" class="buttonbg">
						<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
							<tr>
							<td class="buttonbg">
							<input type="button" value="Submit"
							onclick="onSubmit()" class="blue_ar_b">
							</td>
							</tr>
						</table>
					</td>
				</tr>
			 </table>
			
		<script>
			if(isConsentResponseWithdrawn){
				document.getElementById("disableConsentCheckboxDiv").style.display = "block";
			}
			if(typeof String.prototype.trim !== 'function') {
			  String.prototype.trim = function() {
				return this.replace(/^\s+|\s+$/g, ''); 
			  }
			}
			function onSubmit(){
				var consentDto = {};
				<logic:equal name="consentLevel" value="participant">
					consentDto.consentUrl = document.getElementById("consentUrl").value;
					consentDto.witnessId = witness.getSelectedValue();
					consentDto.consentDate= document.getElementById("consentDate").value;
				</logic:equal>
				var tabDataJSON =  new Array();
				var selectFields = document.getElementsByTagName("select");
				var consentLevel = document.getElementById("consentLevel").value;
				for(var cnt = 0;cnt<comboData.length;cnt++){
					var selectedField = comboData[cnt];
					var jsonObj = {};
					if(selectedField.getSelectedText().trim()==""){
						document.getElementById('error').style.display='block';
						if(consentLevel=="specimen"||consentLevel=="scg"){
							document.getElementById('errorMsg').innerHTML=" Please enter valid consent status.";
						}else{
							document.getElementById('errorMsg').innerHTML=" Please enter valid participant response.";
						}
						return;
					}
					
					jsonObj.id = statusId[cnt];
					jsonObj.status = selectedField.getSelectedText();
					jsonObj.participantResponses = selectedField.getSelectedText();
					jsonObj.consentStatusId =  0;
					tabDataJSON.push(jsonObj);
				}
				//consentDto.consentTierList = JSON.stringify(tabDataJSON);
				consentDto.consentLevel = document.getElementById("consentLevel").value;
				consentDto.consentLevelId = document.getElementById("consentLevelId").value;
				
				var consentLevelId = document.getElementById("consentLevelId").value;
				var disposeSpecimen = document.getElementsByName("disableConsentCheckbox")[0].checked ;
				var lodder = dhtmlxAjax.postSync("CatissueCommonAjaxAction.do","type=updateConsentTierStatus&consentLevel="+consentLevel+"&consentLevelId="+consentLevelId+"&dataJSON="+JSON.stringify(tabDataJSON)+"&disposeSpecimen="+disposeSpecimen+"&consentDto="+JSON.stringify(consentDto));
				if(lodder.xmlDoc.responseText != null)
				{
					var response = eval('('+lodder.xmlDoc.responseText+')')
					if(response.success == "success")
					{
						document.getElementById('success').style.display='block';
						var nodeId= "";
						if(consentLevel=="specimen"){
							nodeId= "Specimen_"+consentLevelId;
							refreshTree(null,null,null,null,nodeId);
						}else if(consentLevel=="scg"){
							nodeId= "SpecimenCollectionGroup_"+consentLevelId;
							refreshTree(null,null,null,null,nodeId);
						}
						
					}
					else
					{
						document.getElementById('error').style.display='block';
						document.getElementById('errorMsg').innerHTML=response.msg;
					}
				}
		}
		</script>
	