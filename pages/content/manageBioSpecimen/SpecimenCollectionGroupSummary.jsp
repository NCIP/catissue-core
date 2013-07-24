<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script	src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script	src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script	src="dhtmlx_suite/ext/dhtmlxcombo_extra.js"></script>
<script	src="dhtmlx_suite/ext/dhtmlxcombo_whp.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css" />

<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<LINK type=text/css rel=stylesheet href="css/participantEffects.css" />

<script src="jss/scgSummary.js"></script>
<script src="jss/json2.js" type="text/javascript"></script>
<fmt:formatDate type="time" value="${scgSummaryDTO.collectedDate}" pattern="H" var="collectedTimeinHr"/>
<fmt:formatDate type="time" value="${scgSummaryDTO.collectedDate}" pattern="m" var="collectedTimeinmin"/>
<fmt:formatDate type="time" value="${scgSummaryDTO.receivedDate}" pattern="H"  var="receivedTimeinHr"/>
<fmt:formatDate type="time" value="${scgSummaryDTO.receivedDate}" pattern="m"  var="receivedTimeinMin"/>
<fmt:formatDate value="${scgSummaryDTO.receivedDate}" pattern="${dateFormat}" var="receivedformatedDate"/>
<fmt:formatDate value="${scgSummaryDTO.collectedDate}" pattern="${dateFormat}" var="collectedformatedDate"/>


<div id="errorDiv" class="messagetexterror" style="padding-left:30px;display:none;">
<img src="images/uIEnhancementImages/alert-icon.gif" alt="error messages"
				width="16" vspace="0" hspace ="0" height="18" valign="top">
</div>

<div id="scgDetails" class="align_left_style">
<fieldset class="field_set"> 
  <legend class="blue_ar_b legend_font_size"> <bean:message key="scg.details"/></legend>
<table width="100%" border="0"  cellpadding="3" cellspacing="0" class="whitetable_bg">	

	<tr>
		<td class="black_new padding_right_style black_ar" align="right" width="27%">
		<b><bean:message key="specimenCollectionGroup.groupName"/></b></td>
		<td colspan="3" width="25%"><html:text property="scgName" styleId="scgName" name="scgSummaryDTO" styleClass="formFieldSizedText22 black_ar" onblur="processData(this)"/> </td>
		<td class="black_new padding_right_style black_ar" align="right" width="25%">
		 <b><bean:message key="specimenCollectionGroup.site"/> </b>
		</td>
		<td align="left" class="black_new padding_right_style" colspan="3" width="25%">
							<html:select property="site" name="scgSummaryDTO" 
								styleClass="formFieldSized" styleId="site" size="1"
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
								<html:options collection="siteList"
								 labelProperty="name" property="value" />
							</html:select>
		</td>
	</tr>
           <tr class="tr_alternate_color_lightGrey">
		<td class="black_new padding_right_style black_ar" align="right">
		<b> <bean:message key="specimen.collectedevents.username"/> </b>
		</td>
		<td align="left" class="black_new" colspan="3">
		    <html:select property="collector" name="scgSummaryDTO" 
			 styleClass="formFieldSized" styleId="collector" size="1"
			 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
			  <html:options collection="userList"
			  labelProperty="name" property="value" /></html:select>
		</td>
		<td class="black_new padding_right_style black_ar" align="right">
		 <b> <bean:message key="specimen.receivedevents.username"/> </b>
		</td>
		<td align="left" class="black_new padding_right_style" colspan="3">
							<html:select property="receiver" name="scgSummaryDTO" 
								styleClass="formFieldSized" styleId="receiver" size="1"
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
								<html:options collection="userList"
									labelProperty="name" property="value" />
							</html:select>
		</td>

	</tr>

        <tr>

	<td class="black_new padding_right_style black_ar" align="right">
	 <b> <bean:message key="scg.collectedTime"/> </b>       
	</td>
        <td colspan="3">
<table><tr>
 
	<td align="left" class="black_new date_text_field" >
						<input type="text" name="collectedDate" class="black_ar date_text_field"  onblur="processData(this)"
					id="collectedDate" size="10" value="${collectedformatedDate}"
							 onclick="doInitCalendar('collectedDate',false,'${uiDatePattern}');" /> </td>
        
            <td align="left" style="padding-left:4px">
            
            <select  class="formFieldSized" size="1" id="hoursTime" name="collectHrTime"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
						<logic:iterate id="hr" name="hourList">
                                                           <c:if test="${hr == collectedTimeinHr}">
                                                            <option value="${hr}" selected><bean:write name="hr"/></option>
                                                           </c:if> 
                                                           <c:if test="${hr != collectedTimeinHr}">
                                                            <option value="${hr}" ><bean:write name="hr"/></option>
                                                           </c:if>
                                                  </logic:iterate>  
	</select> 
         </td>
            <td align="left" style="padding-left:4px" >
<select  class="formFieldSized" id="minuteTime" size="1" name="collectMinTime"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<logic:iterate id="min" name="minutesList">
                                                          <c:if test="${min == collectedTimeinmin}">
                                                            <option value="${min}" selected><bean:write name="min"/></option>
                                                           </c:if> 
                                                           <c:if test="${min != collectedTimeinmin}"> 
                                                            <option value="${min}"><bean:write name="min"/></option>
                                                            </c:if>
                                                        </logic:iterate>  
						</select> </td>        
</tr></table>	</td>
	<td class="black_new padding_right_style black_ar" align="right">
	 <b> <bean:message key="scg.receivedTime"/> </b>
	</td>
 <td colspan="3">
 <table> <tr> 
	<td align="left" class="black_new date_text_field" >
        
						<input type="text" class="black_ar date_text_field"  name="receivedDate" onblur="processData(this)"
							id="receivedDate" size="10" value="${receivedformatedDate}" 
							 onclick="doInitCalendar('receivedDate',false,'${uiDatePattern}');" />
	</td>
         <td align="left" style="padding-left:4px">
                 <select  class="frmFieldSized"  size="1" id="receivehoursTime" name="receiveHrTime"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
						<logic:iterate id="hr1" name="hourList">
                                                           <c:if test="${hr1 == receivedTimeinHr}">
                                                            <option value="${hr1}" selected><bean:write name="hr1"/></option>
                                                           </c:if> 
                                                           <c:if test="${hr1 != receivedTimeinHr}">
                                                            <option value="${hr1}" ><bean:write name="hr1"/></option>
                                                           </c:if>
                                                  </logic:iterate>  
	</select>
         </td>

         <td align="left" style="padding-left:4px">
<select  class="formFieldSized" id="receiveminuteTime" size="1" name="receiveMinTime"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)">
							<logic:iterate id="min1" name="minutesList">
                                                         <c:if test="${min1 == receivedTimeinMin}">
                                                            <option value="${min1}" selected><bean:write name="min1"/></option>
                                                           </c:if> 
                                                           <c:if test="${min1!= receivedTimeinMin}">  
                                                          <option value="${min1}"><bean:write name="min1"/></option>
                                                          </c:if>
                                                        </logic:iterate>  
						</select> </td>
<td></td>
</tr></table></td>
 	</tr>
<tr class="tr_alternate_color_lightGrey"><td class="black_new padding_right_style black_ar" align="right">
		 <b> <bean:message key="specimenCollectionGroup.collectionStatus"/> </b>
		</td>
<td align="left" class="black_new padding_right_style" colspan="7">
 	<html:select property="collectionStatus"
							             styleClass="black_ar"  name="scgSummaryDTO"  styleId="collectionStatus" size="1">
							       <html:options collection="collectionStatusList" labelProperty="name" property="value" />
					        </html:select>	
</td>
    
<tr> <td class="bottomtd" colspan="6"></td></tr>
</table>
</fieldset>
</div>
<p></p>
<script>

var scgDataJSON = {};
scgDataJSON["scgId"] = ${scgSummaryDTO.scgId};
convertSelectToCombo();
</script>