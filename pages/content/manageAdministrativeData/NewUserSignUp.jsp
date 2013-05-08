<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>


<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="org.apache.struts.action.Action"%>
<%@ page import="org.apache.struts.action.ActionError"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="org.apache.struts.action.ActionErrors"%>

<%@ page
	import="org.apache.struts.action.Action,org.apache.struts.action.ActionError,edu.wustl.common.util.global.ApplicationProperties,org.apache.struts.action.ActionErrors"%>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script src="jss/script.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<link rel="stylesheet" type="text/css" href="css/login.css" />
<link rel="stylesheet" type="text/css" href="css/login-theam.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>

</head>

<body>

<script>
var target_combo;
function disableIdpDetails(disableStatus)
{
	target_combo.disable(disableStatus);
	document.getElementById("targetLoginName").disabled=disableStatus;
	document.getElementById("targetPassword").disabled=disableStatus;
	//showHide('idpContentDiv');
	var display="";
	if(disableStatus){
		display="none";
	}
	document.getElementById('idpContentDiv').style.display = display;
}
</script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script type="text/javascript" src="jss/queryModule.js"></script>
<%
    //Object obj=  request.getAttribute(Constants.ACTIONLIST);
	String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
	String operation = (String) request
			.getAttribute(Constants.OPERATION);
	List siteList = (List) request.getAttribute(Constants.SITELIST);
	List roleList = (List) request.getAttribute(Constants.ROLELIST);
	List actionList = (List) request.getAttribute(Constants.ACTIONLIST);
	List cpList = (List) request.getAttribute(Constants.CPLIST);
%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="5" class="td_orange_line" height="1"></td>
	</tr>
	<tr>
		<td>
		<div style=" text-align:center;width:100%;">
			<html:form action='${requestScope.formName}'>
				<html:hidden property="operation" styleId="operation" />
				<html:hidden property="submittedFor" />
				<html:hidden property="pageOf" />
				<html:hidden property="id" />
				<html:hidden property="csmUserId" />
				<html:hidden property='${requestScope.redirectTo}' />
				<logic:equal name="pageOf" value='${requestScope.pageOfSignUp}'>
					<html:hidden property="activityStatus" />
				</logic:equal>

			<logic:notEmpty name="idpsList">
				<div class="black_ar box-border box-content box-background form-main-div">
					<div class="black_ar help-header theam-font-color form-header-spacing" >
						<span><bean:message key="app.signup" /></span>
					</div>
					<div class="black_ar help-header theam-font-color form-header-spacing" >
						<div class="black_ar">
							<%@ include	file="/pages/content/common/ActionErrors.jsp"%>
						</div>
						<span style="font-size: 14px;"><bean:message key="idp.detials"/></span>
					</div>
					<div style="width:100%; height: auto;">
						<div style="width:49%;float:left; ">
							<div class="form-inner-div margin-form-field form-label">
								<bean:message key="source.idp.question"/><br />
										<div id="idpNames">
										<logic:iterate id="idpDetails" name="idpsList">
											<bean:write name="idpDetails" property="name"/>,
										</logic:iterate>
										</div>
							</div>
							
						</div>
						<div style="width:49%;float:left">
							<div class="form-inner-div margin-form-field">
									<logic:equal name="idpSelection" value="yes">
										<input type="radio" name="idpSelection" value="yes" checked onclick="disableIdpDetails(false)"/>
									</logic:equal> 
									<logic:notEqual name="idpSelection" value="yes">
										<input type="radio" name="idpSelection" value="yes" onclick="disableIdpDetails(false)"/>
									</logic:notEqual>
									<span class="black_ar"><bean:message key="user.yes"/></span> &nbsp; 
									<logic:equal name="idpSelection" value="no">
										<input type="radio" name="idpSelection" value="no" checked onclick="disableIdpDetails(true)"/>
									</logic:equal> <logic:notEqual name="idpSelection" value="no">
										<input type="radio" name="idpSelection" value="no" onclick="disableIdpDetails(true)"/>
									</logic:notEqual><span class="black_ar" /><bean:message key="user.no"/></span>
							</div>
						</div>
					</div>
					
					
					<div style="width:100%; height: auto;float:left;display:block;" id="idpContentDiv">
						<div style="width:49%;float:left; ">
							<div class="form-inner-div margin-form-field">
								<span class="form-label"><bean:message key="idp.selection.text"/></span> </br>
								<div class="dhtmlx-combo-margin"><select name="targetIdp"  id="target_combo">
										<logic:iterate id="idpDetails" name="idpsList">
											<option value="<bean:write name='idpDetails' property='name'/>"><bean:write name="idpDetails" property="name"/></option>
										</logic:iterate>
								</select></div>
								<script>
									  //common init code
									  dhtmlx.skin ='dhx_skyblue';
									  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
									  target_combo = new dhtmlXCombo("target_combo","target_combo","100px");
									  target_combo.enableFilteringMode(true);
									  target_combo.setSize(200);
									  target_combo.setComboValue("");
								</script>
								
							</div>
							<div class="form-inner-div margin-form-field">
									<span class="form-label"><bean:message key="user.loginName"/></span> </br>
									<html:text styleClass="black_ar  form-text-field" maxlength="255" size="30" styleId="targetLoginName" property="targetLoginName" readonly='${requestScope.readOnlyEmail}' />
							</div>
						</div>
						<div style="width:49%;float:left; ">
							<div class="form-inner-div margin-form-field">
								
								
							</div>
							<div class="form-inner-div margin-form-field" style="margin-top:47px">
								<span class="form-label"><bean:message key="user.password"/></span> </br>
								<html:password styleClass="black_ar  form-text-field" maxlength="255" size="30" styleId="targetPassword" property="targetPassword" readonly='${requestScope.readOnlyEmail}' />
							</div>
						</div>
					</div>
					
					
				</div>
				</logic:notEmpty>
					
					
				<div class="black_ar box-border box-content box-background form-main-div">
				<logic:empty name="idpsList">
					<div class="black_ar help-header theam-font-color form-header-spacing" >
						<span><bean:message key="app.signup" /></span>
					</div>
				</logic:empty>
					<div class="black_ar help-header theam-font-color form-header-spacing" >
						<logic:empty name="idpsList">
						<div class="black_ar">
						<%@ include	file="/pages/content/common/ActionErrors.jsp"%>
						</div>
						</logic:empty>
						<span style="font-size: 14px;"><bean:message key="user.details.title" /></span>
					</div>
					<div style="width:49%;float:left; ">
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.emailAddress" /></span> </br>
							<html:text tabindex="1" styleClass=" black_ar form-text-field" maxlength="255" size="30" styleId="emailAddress" property="emailAddress" readonly='${requestScope.readOnlyEmail}' />
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.lastName" /></span> </br>
							<html:text  tabindex="3" styleClass=" black_ar form-text-field" maxlength="255" size="30" styleId="lastName" property="lastName" />
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.street" /></span> </br>
							<html:text  tabindex="5" styleClass="black_ar  form-text-field " maxlength="255" size="30" styleId="street" property="street" onfocus="removeClass(this,'optional-background')" onblur="addClass(this)"/>
							
						</div>
						<script>
							function hasClass(ele,cls) {
								return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
							}

							function removeClass(ele,cls) {
									if (hasClass(ele,cls)) {
										var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
										ele.className=ele.className.replace(reg,' ');
									}
								}
							function addClass(ele){
								if(ele.value==""){
									ele.className+=" optional-background";
								}
							}

						</script>
						<div class="form-inner-div margin-form-field">
							<!--img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /-->
							<span class="form-label"><bean:message key="user.state" /></span> </br>
							<div  class="dhtmlx-combo-margin">
							<select  tabindex="7"  name="state"  id="state_combo" class="  optional-background">
								<logic:iterate id="stateList" name="stateList">
									<logic:notEqual name="stateList"  property='name' value="-- Select --">
										<option value="<bean:write name='stateList' property='value'/>"><bean:write name="stateList" property="name"/></option>
									</logic:notEqual>
								</logic:iterate>
							</select>
							</div>
						</div>
						<script>
							  //common init code
							  dhtmlx.skin ='dhx_skyblue';
							  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
							  var state_combo = new dhtmlXCombo("state_combo","state_combo","100px");
							  state_combo.enableFilteringMode(true);
							  state_combo.setSize(200);
							  state_combo.setComboValue("");
							  state_combo.setComboValue('${userForm.state}');

							  
     					</script>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.country" /></span> </br>
							<div  class="dhtmlx-combo-margin">
							<select  tabindex="9"  name="country"  id="country_combo" class="form-text-field">
								<logic:iterate id="countryList" name="countryList">
									<logic:notEqual name="countryList"  property='name' value="-- Select --">
										<option value="<bean:write name='countryList' property='value'/>"><bean:write name="countryList" property="name"/></option>
									</logic:notEqual>
								</logic:iterate>
							</select>
							</div>
							
							  <script>
							  //common init code
							  dhtmlx.skin ='dhx_skyblue';
							  window.dhx_globalImgPath="dhtmlx_suite/imgs/";
							  var country_combo = new dhtmlXCombo("country_combo","country_combo","100px");
							  country_combo.enableFilteringMode(true);
							  country_combo.setSize(200);
							  country_combo.setComboValue('${userForm.country}');
							  country_combo.attachEvent("onChange", stateChange); 
							  if('${userForm.country}'!='${defaultCountry}'){
								state_combo.disable(true);
							  }
							 function stateChange(obj){
								var defaultCntr = '${defaultCountry}'
								   
									if(country_combo.getSelectedValue() ==defaultCntr){
										state_combo.disable(false);
									}else{
										state_combo.setComboText("");
										state_combo.setComboValue("");
										state_combo.disable(true);
									}
								}
								  
							  </script>
							
							
						</div>
						
						<div class="form-inner-div margin-form-field">
							<!--img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /-->
							<span class="form-label"><bean:message key="user.faxNumber" /></span> </br>
							<html:text  tabindex="11" styleClass="black_ar form-text-field" style="text-align:right" maxlength="50" size="30" 	styleId="faxNumber" property="faxNumber"  onfocus="removeClass(this,'optional-background')" onblur="addClass(this)"/>
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message	key="user.department" /></span> </br>
							<div  class="dhtmlx-combo-margin">
							<select  tabindex="13" name="departmentId"  id="department_combo" class="form-text-field">
								<logic:iterate id="departmentList" name="departmentList">
									<logic:notEqual name="departmentList"  property='name' value="-- Select --">
										<option value="<bean:write name='departmentList' property='value'/>"><bean:write name="departmentList" property="name"/></option>
									</logic:notEqual>
								</logic:iterate>
							</select>
							</div>
													
							  <script>
							  //common init code
							  var department_combo = new dhtmlXCombo("department_combo","department_combo","100px");
							  department_combo.enableFilteringMode(true);
							  department_combo.setSize(200);
							  department_combo.setComboValue("");
							  department_combo.setComboValue('${userForm.departmentId}');
							   if('${userForm.departmentId}'!=0){
								department_combo.setComboValue('${userForm.departmentId}');
							  }else{
								department_combo.setComboValue('');
							  }
							  </script>
							
						</div>
						<div>
							<html:submit styleClass="blue_ar_b submit-signup">
								<bean:message key="buttons.submit" />
							</html:submit>
						</div>
							
					</div>
					<div style="float:left;">
						<div class="form-inner-div margin-form-field">
							<label for="confirmEmailAddress" class="black_ar form-label"><bean:message key="user.confirmemailAddress" /></label>
							</br><html:text tabindex="2" styleClass=" black_ar form-text-field" maxlength="255" size="30" styleId="confirmEmailAddress" property="confirmEmailAddress" readonly='${requestScope.readOnlyEmail}' />
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.firstName" /></span> </br>
							<html:text  tabindex="4" styleClass="black_ar  form-text-field" maxlength="255" size="30" styleId="firstName" property="firstName" />
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.city" /></span> </br>
							<html:text  tabindex="6" styleClass="black_ar  form-text-field" maxlength="50" size="30" styleId="city" property="city" />
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="address.zipCode" /></span> </br>
							<html:text  tabindex="8" style="text-align:right" styleClass="black_ar  form-text-field" maxlength="30" size="30" styleId="zipCode" property="zipCode" onfocus="removeClass(this,'optional-background')" onblur="addClass(this)"/>
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.phoneNumber" /></span> </br>
							<html:text  tabindex="10" styleClass="black_ar form-text-field" style="text-align:right" maxlength="50" size="30" styleId="phoneNumber" property="phoneNumber"/>
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.institution" /></span> </br>
							<div  class="dhtmlx-combo-margin">
							<select  tabindex="12" name="institutionId"  id="institution_combo" >
								<logic:iterate id="instituteList" name="instituteList">
										<logic:notEqual name="instituteList"  property='name' value="-- Select --">
										<option value="<bean:write name='instituteList' property='value'/>"><bean:write name="instituteList" property="name"/></option>
									</logic:notEqual>
								</logic:iterate>
							</select>
							</div>
													
							  <script>
							  //common init code
							  var institute_combo = new dhtmlXCombo("institution_combo","institution_combo","100px");
							  institute_combo.enableFilteringMode(true);
							  institute_combo.setSize(200);
							  institute_combo.setComboValue('${userForm.institutionId}');
							  if('${userForm.institutionId}'!=0){
								institute_combo.setComboValue('${userForm.institutionId}');
							  }else{
								institute_combo.setComboValue('');
							  }
							  
							  </script>
							
						</div>
						
						<div class="form-inner-div margin-form-field">
							<span class="form-label"><bean:message key="user.cancerResearchGroup" /></span> </br>
							<div  class="dhtmlx-combo-margin">
							<select  tabindex="14"  name="cancerResearchGroupId"  id="resrarch_combo">
								<logic:iterate id="cancerResearchGroupList" name="cancerResearchGroupList">
								<logic:notEqual name="cancerResearchGroupList"  property='name' value="-- Select --">
										<option value="<bean:write name='cancerResearchGroupList' property='value'/>"><bean:write name="cancerResearchGroupList" property="name"/></option>
									</logic:notEqual>	
								</logic:iterate>
							</select>
							</div>
													
							  <script>
							  //common init code
							  var resrarch_combo = new dhtmlXCombo("resrarch_combo","resrarch_combo","100px");
							  resrarch_combo.enableFilteringMode(true);
							  resrarch_combo.setSize(200);
							  if('${userForm.cancerResearchGroupId}'!=0){
								resrarch_combo.setComboValue('${userForm.cancerResearchGroupId}');
							  }else{
								resrarch_combo.setComboValue('');
							  }
							 
							  
							  
							  </script>
							  
						</div>
						
					</div>
					
				</div>
					
			</html:form>
		</div>
		</td>
	</tr>
	</table>

</body>
<script>

addClass(document.getElementById("street"));
addClass(document.getElementById("faxNumber"));
addClass(document.getElementById("zipCode"));
<logic:equal name="idpSelection" value="no">
	disableIdpDetails(true);
	
</logic:equal>
<logic:equal name="idpSelection" value="yes">
	disableIdpDetails(false);
</logic:equal>

function updateIdpNamesContents()
{
	var content=document.getElementById("idpNames").innerHTML;
	content=content.trim();
	document.getElementById("idpNames").innerHTML=content.substring(0,content.length-1)+".";
}
<logic:notEmpty name="idpsList">
	updateIdpNamesContents();
</logic:notEmpty>
</script>