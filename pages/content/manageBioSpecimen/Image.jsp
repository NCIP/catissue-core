<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
var imageDataJson = {};
var equipDataJson = {};

window.dhx_globalImgPath = "dhtmlx_suite/imgs/";

</script>
<script>

window.dhx_globalImgPath = "dhtmlx_suite/imgs/";

</script>
<script src="jss/json2.js" type="text/javascript"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxcombo_whp.js">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />

<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>

<SCRIPT>var imgsrc="images/";</SCRIPT>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>

<!------------------------------------------------------------------------------->

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"    src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"    src="jss/caTissueSuite.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>

<style>
 .fieldset_New_border{
        border-radius:25px;
        -moz-border-radius: 25px;
       -webkit-border-radius: 25px;
}
.alert {
  padding: 8px 35px 8px 14px;
  margin-bottom: 18px;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
  background-color: #fcf8e3;
  border: 1px solid #fbeed5;
  -webkit-border-radius: 4px;
  -moz-border-radius: 4px;
  border-radius: 4px;
  color: #c09853;
}
.alert-success {
  background-color: #dff0d8;
  border-color: #d6e9c6;
  color: #468847;
}
.alert-danger,
.alert-error {
  background-color: #f2dede;
  border-color: #eed3d7;
  color: #b94a48;
}
.alert-info {
  background-color: #d9edf7;
  border-color: #bce8f1;
  color: #3a87ad;
}
</style>
<SCRIPT>var imgsrc="images/";</SCRIPT>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />


<!----------------------------------------------------------------------->
<body >
    <html:form action="NewSpecimenEdit.do">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">

<tr>
        <td class="td_color_bfdcf3">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="td_table_head">
                    <span class="wh_ar_b">
                        <bean:message key="app.newSpecimen" />
                    </span>
                </td>
                <td>
                    <img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen" width="31" height="24" hspace="0" vspace="0" />
                </td>
              </tr>
            </table>
        </td>
      </tr>

      <tr>
        <td class="tablepadding">
   
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="td_tab_bg">
                    <img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td><td valign="bottom"><a onclick="viewSpecimen('${specimenId}')" id="specimenDetailsTab" href="#"><img src="images/uIEnhancementImages/tab_specimen_details2.gif" alt="Specimen Details"  width="126" height="22" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="showEvent('${specimenId}');" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSPR('${identifiedReportId}','pageOfNewSpecimenCPQuery','${specimenId}');"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewSpecimenAnnotation('${specimenRecordEntryEntityId}','${specimenId}','${entityName}')"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3"><a id="consentViewTab" href="#" onClick="newConsentTab('${specimenId}','${identifiedReportId}','${specimenRecordEntryEntityId}','${entityName}')"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3"><a id="consentViewTab" ><img src="images/uIEnhancementImages/tab_image1.gif" alt="Images" width="110" border="0" height="22"></a></td>
                    <td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
                    </td>
                </tr>
            </table>
   
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
            <tr>
            <td>
            <logic:equal name="isImagingConfigurred" value="false">
            <div id="imagenot" style="display:block">
                <table width="100%"  border="0" cellpadding="3" cellspacing="0" >
                    <tr>
                        <td class="black_ar">
                            <b>Support for images is not available in your instance. Please contact your system administrator for more details.</b>
                        </td>
                    </tr>
                    <tr>
                        <td>
                           
                           
                        </td>
                    </tr>
                </table>
            </div>
            </logic:equal>
            <logic:equal name="isImagingConfigurred" value="true">
            <div id="emptyPage" style="display:none">
                <table width="100%"  border="0" cellpadding="3" cellspacing="0" >
                    <tr>
                        <td class="black_ar">
                            <b>There is no image associated with this specimen. Click below to manually enter image information. </b>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <html:button
                styleClass="blue_ar_b" property="submitButton"
                title="Enter ImageInformation"
                value="Enter ImageInformation"
                onclick="addImageInfo()">
            </html:button>
                           
                        </td>
                    </tr>
                </table>
            </div>
           
            <div id="mainTable" style="display:none"><table width="100%"  border="0" cellpadding="3" cellspacing="0" >
                <tr>
                  <td><div id="error" class="alert alert-error" style="display:none">
				<strong>Error!</strong> <span id="errorMsg">Change a few things up and try submitting again.</span>
</div>


<div class="alert alert-success" id="success-div" style="display:none">
					Changes Updated Successfully
</div>
</td>
                </tr>
                <tr>
                  <td align="justify">
                 
                  </td>
                </tr>
                <tr class="tr_alternate_color_lightGrey">
                  <td align="justify">
                    <table border="0" width="100%"><tr>
                   
                  <logic:equal name="operation" value="add">
                  <td class="black_ar align_right_style">
                  <bean:message key="image.imageId"/>
                  </td>
                  <td>
                    <span><html:text styleClass="black_ar" size="40" maxlength="255"  styleId="equipmentImageId" name="imageDTO" property="equipmentImageId" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/></span>
                    </td>
                  </logic:equal>
                  <logic:notEqual name="operation" value="add">
                  <td class="black_ar align_right_style">
                  <bean:message key="image.select.details"/>
                  </td>
                  <td class="black_new">
                    <span><html:select styleClass="formFieldSized" styleId="equipmentImageId" name="imageDTO" property="equipmentImageId" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
                        <html:options collection="imageList"
                                labelProperty="name" property="value" />
                    </html:select>
                    </span>
                    </td>
                  </logic:notEqual>
                 
                  </tr>
                  </table>
                  </td>
                </tr>
                <logic:equal name="operation" value="edit">
                <tr>
                    <td align="left" class="showhide">
                        <fieldset class="field_set fieldset_New_border">
 <legend class="blue_ar_b legend_font_size"> <b>  View Image </b></legend>
                       
                        <table width="100%" border="0" cellpadding="3" cellspacing="0" id="imageView">
                        
                            <tr>   
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                         
                          <td width="17%" align="left" class="black_ar " >
                                    <a href="#" class="view" onclick="viewInWebScope('ffffff')"><span title="Download Image" style="vertical-align:top">View in WebScope(Web-Based Application)</span></a></td>
                                </td>
                            <td width="34%"/>
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%"/>
                            <td width="34%"/>   
                            </tr>
                            <tr>   
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                         
                          <td width="17%" align="left" class="black_ar " >
                                    <a href="#" onclick="viewImageLocally('${imageDTO.physicalLocationUrl}')" class="view"><span title="Download Image" style="vertical-align:top">View in ImageScope(Desktop Application)</span></a></td>
                                </td>
                            <td width="34%"/>
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%"/>
                            <td width="34%"/>   
                            </tr>
                            
                            <tr>   
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                         
                          <td width="17%" align="left" class="black_ar " >
                                    <a href="#" class="view" onclick="downloadImage('${imageDTO.physicalLocationUrl}')"><span title="Download Image" style="vertical-align:top">Download Image</span><img name="download image" src="images/uIEnhancementImages/down.png" width="16" height="16" valign="bottom"/></a></td>
                                </td>
                            <td width="34%"/>
                                <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%"/>
                            <td width="34%"/>   
                            </tr>
                        </table>
                        </fieldset>
                    </td>
                </tr>
                </logic:equal>
                <tr>
                    <td align="left" class="showhide">
                  <fieldset class="field_set fieldset_New_border">
 <legend class="blue_ar_b legend_font_size"> <b>  <bean:message key="image.device"/> </b></legend>
                    <table width="100%" border="0" cellpadding="3" cellspacing="0" id="editable" style="display:none">
                   
                       
                        <tr class="tr_alternate_color_lightGrey">
                          <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="deviceName">
                                <bean:message key="image.display.name"/>
                               
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_new">
                                <label for="displayName">
                                    <div id="equipTxt" style="display:block"/>
                                    <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="displayName" name="equipmentDTO" property="displayName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processEquipData(this)"/>
                                    </div>
                                   
                                    <html:hidden property="equipmentId" styleId="equipmentId" name="equipmentDTO" />
                                </label>
                                <div id="equipSelect" style="display:none"/>
                                <html:select styleClass="formFieldSizedNew" size="1" styleId="displayName" name="equipmentDTO" property="displayName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)" onchange="getEquipmentData(this)">
                        <html:options collection="deviceList"
                                labelProperty="name" property="value" />
                    </html:select>
                    <html:link href="#" styleClass="view" styleId="newInstitution"
                                    onclick="addEquipment();">
                                    <bean:message key="buttons.addNew" />
                                </html:link>
                                </div>
                               
                          </td>
						  
                          <td colspan="3" align="right">
                           
                            <input type="button" name="edit" value="Edit" id='edit'/>
                       
                            </td>
               
                         
                         
               
                        </tr>
                       
                         <tr class="tr_alternate_color_white">
                          <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="deviceName">
                                <bean:message key="image.device.name"/>
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_ar">
                                <label for="deviceName">
                                    <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="deviceName" name="equipmentDTO" property="deviceName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processEquipData(this)"/>
                                    <html:hidden property="id" styleId="equipId" name="equipmentDTO" />
                                </label>
                          </td>
               
                          <td width="1%" align="center">
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="lineage">
                                <bean:message key="image.device.site"/>
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_new">
                            <label for="site">
                           
                        <html:select styleClass="formFieldSized" styleId="siteId" name="equipmentDTO" property="siteId" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
                            <html:options collection="siteList"
                                labelProperty="name" property="value" />
                    </html:select>
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="siteName" name="equipmentDTO" property="siteName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processEquipData(this)" style="display:none"/>
                            </label>
                          </td>
               
                        </tr>
                        <tr class="tr_alternate_color_lightGrey">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="serial">
                                    <bean:message key="image.device.serial"/>
                                </label>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="deviceSerialNumber" name="equipmentDTO" property="deviceSerialNumber" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processEquipData(this)"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="manufacturer">
                                    <bean:message key="image.device.manufacturer.name"/>
                                </label>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <label for="manufacturer">
                                    <html:text name="equipmentDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="manufacturerName" property="manufacturerName" onblur="processEquipData(this)"/>
                               
                                    </label>
                                </td>
                        </tr>

                        <tr class="tr_alternate_color_white">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="model">
                                    <bean:message key="image.device.manufacturer.modelName"/>
                                </label>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="manufacturerModelName" name="equipmentDTO" property="manufacturerModelName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processEquipData(this)"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="software-version">
                                    <bean:message key="image.device.software.version"/>
                                </label>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <label for="software-version">
                                    <html:text name="equipmentDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="softwareVersion" property="softwareVersion" onblur="processEquipData(this)"/>
                               
                                    </label>
                                </td>
                        </tr>
</table>       
<table width="100%" border="0" cellpadding="3" cellspacing="0" id="readonly" style="display:block">
                   
                       
                        <tr class="tr_alternate_color_lightGrey">
                          <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="deviceName">
                                <bean:message key="image.display.name"/>
                               
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_ar" >
                                    <div id="equipmentId_read" style="display:none">
                                    </div>
                                <logic:notEqual name="operation" value="add">   
                                    <div id="displayName_read" style="display:block">
                                    <bean:write name="equipmentDTO" property="displayName"/>
                                    </div>
                                </logic:notEqual>
                                <logic:equal name="operation" value="add">                   
                                    <div id="displayName_read" style="display:none">
                                    </div>
                                <html:select styleClass="formFieldSizedNew" styleId="displayName" name="equipmentDTO" property="displayName" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)" onchange="getEquipmentData(this)">
                        <html:options collection="deviceList"
                                labelProperty="name" property="value" />
                    </html:select>
                    <html:link href="#" styleClass="view" styleId="newInstitution"
                                    onclick="addEquipment();">
                                    <bean:message key="buttons.addNew" />
                                </html:link>
                                </logic:equal>
                               
                               
                          </td>
               
                          <td colspan="3" align="right">
                           
                            <input type="button" name="edit" value="Edit" id='edit' onclick="deviceEdit('read')"/>
                       
                            </td>
                         
               
                        </tr>
                       
                         <tr class="tr_alternate_color_white">
                          <td width="1%" align="center" class="black_ar">
                            <span class="blue_ar_b">
                            </span>
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="deviceName">
                                <bean:message key="image.device.name"/>
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_ar">
                                <div id="deviceName_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="deviceName"/>
                                    </div>
                               
                          </td>
               
                          <td width="1%" align="center">
                          </td>
                          <td width="17%" align="left" class="black_ar align_right_style">
                            <label for="lineage">
                                <bean:message key="image.device.site"/>
                            </label>
                          </td>
                          <td width="34%" align="left" class="black_ar">
                            <div id="siteName_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="siteName"/>
                            </div>
                           
                          </td>
               
                        </tr>
                        <tr class="tr_alternate_color_lightGrey">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="serial">
                                    <bean:message key="image.device.serial"/>
                                </label>
                            </td>
                            <td align="left" class="black_ar">
                                <div id="deviceSerialNumber_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="deviceSerialNumber"/>
                            </div>
                               
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="manufacturer">
                                    <bean:message key="image.device.manufacturer.name"/>
                                </label>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <div id="manufacturerName_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="manufacturerName"/>
                            </div>
                                   
                                </td>
                        </tr>

                        <tr class="tr_alternate_color_white">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="model">
                                    <bean:message key="image.device.manufacturer.modelName"/>
                                </label>
                            </td>
                            <td align="left" class="black_ar">
                            <div id="manufacturerModelName_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="manufacturerModelName"/>
                            </div>
                               
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="software-version">
                                    <bean:message key="image.device.software.version"/>
                                </label>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <div id="softwareVersion_read" style="display:block">
                                        <bean:write name="equipmentDTO" property="softwareVersion"/>
                            </div>
                                   
                                </td>
                        </tr>
</table>   
</fieldset>       
</td>
</tr>
<tr><td>
 <fieldset class="field_set fieldset_New_border">
 <legend class="blue_ar_b legend_font_size"> <b>  <bean:message key="image.details"/> </b></legend>
                    <table width="100%" border="0" cellpadding="3" cellspacing="0" >
                        <tr>
                            <td width="1%"/>
                            <td width="17%"/>
                            <td width="34%"/>
                            <td width="1%"/>
                            <td width="17%"/>
                            <td width="34%"/>
                        </tr>
                        <tr class="tr_alternate_color_lightGrey">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style" >
                                <label for="description">
                                    <bean:message key="image.description"/>
                                </label>
                            </td>
                            <td align="left" colspan="4">
                                <html:textarea rows="1" cols="40" styleClass="black_ar" styleId="description" name="imageDTO" property="description" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
                                <html:hidden property="id" styleId="imageId" name="imageDTO" />
                            </td>

                        </tr>
                       
                        <tr class="tr_alternate_color_white">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="height">
                                    <bean:message key="image.height"/>
                                </label>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="height" name="imageDTO" property="height" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
   
                                    <bean:message key="image.width"/>
   
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <label for="width">
                                    <html:text name="imageDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="width" property="width" onblur="processData(this)"/>
                                    </label>
                                </td>
                        </tr>
                       
                        <tr class="tr_alternate_color_lightGrey">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                <label for="scan-date">
                                    <bean:message key="image.scan.date"/>
                                </label>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="scanDate" name="imageDTO" property="scanDate"
                                onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)" readonly="true" onclick="doInitCalendar('scanDate',false,'${uiDatePattern}')"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                    <bean:message key="image.lastUpdate.date"/>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <html:text name="imageDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="lastUpdateDate" property="lastUpdateDate" onblur="processData(this)" readonly="true" onclick="doInitCalendar('lastUpdateDate',false,'${uiDatePattern}')"/>
                                </td>
                        </tr>
                       
                        <tr class="tr_alternate_color_white">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                    <bean:message key="image.quality"/>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="quality" name="imageDTO" property="quality" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                <bean:message key="image.resolution"/>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <html:text name="imageDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="resolution" property="resolution" onblur="processData(this)"/>
                                </td>
                        </tr>
                       
                        <tr class="tr_alternate_color_lightGrey">
                            <td align="center" class="black_ar">
               
                            </td>
                            <td class="black_ar align_right_style" >
                                    <bean:message key="image.status"/>
                            </td>
                            <td align="left">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="status" name="imageDTO" property="status" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
                            </td>

                            <td align="center" class="black_ar">&nbsp;</td>
                            <td align="left" class="black_ar align_right_style">
                                    <bean:message key="image.stain.name"/>
                            </td>
                       
                                <td width="34%" align="left" class="black_ar">
                                    <html:text name="imageDTO"
                                styleClass="black_ar" maxlength="255" size="30"
                                styleId="stainName" property="stainName" onblur="processData(this)"/>
                                </td>
                        </tr>
                       
                        <tr>
                            <td align="center" class="black_ar">
               
                            </td>
                            <td align="left" class="black_ar align_right_style">
                                    <bean:message key="image.physical.location.url"/>
                            </td>
                            <td align="left" colspan="4">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="physicalLocationUrl" name="imageDTO" property="physicalLocationUrl" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
                                <html:hidden name="imageDTO" property="thumbnail" styleId="thumbnail"/>
                            </td>

                        </tr>
                       
                    </table>
                </fieldset>
                            </td>
                            </tr>

                            <tr>
                            <td width="100%" class="bottomtd">

        <input type="button" value="Save Image"
                            onclick="submitTabData()" class="blue_ar_b">

   
       
                            </td></tr>

        <tr>
        <td>
       
                            </td>
                            </tr>
                            </table>
                            </div>
</logic:equal>
                            </td>
                            </tr>
                           

      

                               
        <tr>
            <td height="*">&nbsp;</td>
        </tr>
        </table>
     </td>
   </tr>
</table>
</html:form>
</body>
<script>
<logic:equal name="imageAvl" value="false">
document.getElementById('emptyPage').style.display="block";
</logic:equal>
<logic:equal name="imageAvl" value="true">
document.getElementById('mainTable').style.display="block";
</logic:equal>
function processData(obj)
{
    imageDataJson[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
}

function addImageInfo()
{
    document.getElementById('emptyPage').style.display="none"
    document.getElementById('mainTable').style.display="block";
    document.getElementById('equipTxt').style.display="none";
    document.getElementById('equipSelect').style.display="block";
           
}
function submitTabData()
{
    
    if(isEmpty(equipDataJson) && isEmpty(imageDataJson))
    {
        alert("No changes to submit");
        return false;
    }   
    else if( !isEmpty(imageDataJson))
    {
        if(document.getElementById("imageId").value != "")
        {
            imageDataJson["id"] = document.getElementById("imageId").value;
        }
        if(document.getElementById("equipmentId").value != "")
        {
            imageDataJson["equipmentId"] = document.getElementById("equipmentId").value;
        }
        imageDataJson["specimenId"] = ${requestScope.specimenId};
    }
    if(!isEmpty(equipDataJson))
    {
        if(document.getElementById("equipId").value != "")
        {
            equipDataJson["id"] = document.getElementById("equipId").value;
        }
    }
	
    var loader = dhtmlxAjax.postSync("EditSpecimenImage.do","imageJSON="+JSON.stringify(imageDataJson)+"&equipJSON="+JSON.stringify(equipDataJson)+"&specimenId=${requestScope.specimenId}");
    if(loader.xmlDoc.responseText != null)
    {
   
    var resp = loader.xmlDoc.responseText;
        if(resp == "success")
        {
            document.getElementById('success-div').style.display="block";
        }
        else
        document.getElementById('error').style.display="block";
    }
}
function isEmpty(obj) { for(var i in obj) { return false; } return true; }
function getSelectedData(eqImageId)
{
    var loader = dhtmlxAjax.postSync("ImageAjaxAction.do","type=getImageData&id="+eqImageId);
    //alert(loader.xmlDoc.responseText);
    var resp = eval('( '+ loader.xmlDoc.responseText +')');
    var data = resp[0];
    refreshPage();
    for(var key in data){
            if (data.hasOwnProperty(key)) {
    //alert(key + " -> " + data[key]);
    if(key != 'equipmentImageId')
    {
        if(key == 'id')
            document.getElementById('imageId').value=data[key];
        else   
        document.getElementById(key).value=data[key];
       
    }
  }
  }
  data = resp[1];
  for(var key in data){
            if (data.hasOwnProperty(key)) {
    //alert(key + " -> " + data[key]);
    if(key != 'siteId')
    {
        if(key =='id')
            document.getElementById('equipId').value=data[key];
        else
        {
            document.getElementById(key+'_read').innerHTML=data[key];
            document.getElementById(key).value=data[key];
        }
    }
   
  }
            //alert("attrName:   "+attrName+"   attrValue:    "+attrValue);
        }
    return true;
}
function getEquipmentData(obj)
{
    var loader = dhtmlxAjax.postSync("ImageAjaxAction.do","type=getEquipmentData&id="+obj.value);
    //alert(loader.xmlDoc.responseText);
    var data = eval('( '+ loader.xmlDoc.responseText +')');
   
   
    for(var key in data){
            if (data.hasOwnProperty(key)) {
    //alert(key + " -> " + data[key]);
    if(key == 'id')
    {
        document.getElementById('equipmentId').value=data[key];
    }
	if(key == 'siteId')
	{
		siteCombo.setComboValue(data[key]);
		siteCombo.setComboText(data['siteName']);
	}
    else
    {//alert(key);
        document.getElementById(key).value=data[key];
		 document.getElementById(key+'_read').innerHTML=data[key];
    }
    //alert(key);
   
  }
   
        }
}
function refreshPage()
{
    document.getElementById('deviceName').value='';
    document.getElementById('deviceSerialNumber').value='';
    document.getElementById('manufacturerName').value='';
    document.getElementById('manufacturerModelName').value='';
    document.getElementById('siteName').value='';
    document.getElementById('softwareVersion').value='';
    document.getElementById('displayName').value='';
    document.getElementById('equipmentId').value='';
    //document.getElementById('specimenBarcode').value='';
    document.getElementById('imageId').value='';
    document.getElementById('equipmentId').value='';
    document.getElementById('description').value='';
    //document.getElementById('equipmentImageId').value='';
    document.getElementById('height').value='';
    document.getElementById('lastUpdateDate').value='';
    document.getElementById('physicalLocationUrl').value='';
    document.getElementById('quality').value='';
    document.getElementById('resolution').value='';
    document.getElementById('scanDate').value='';
    document.getElementById('stainName').value='';
    document.getElementById('status').value='';
    document.getElementById('width').value='';
    //document.getElementById('siteId').value='';
    //document.getElementById('').value='';
}

function addEquipment()
{
    document.getElementById('equipTxt').style.display="block";
    document.getElementById('equipSelect').style.display="none";
    refreshPage();
    deviceEdit('ddd');
}
 <logic:notEqual name="operation" value="add">
var imageIdCombo = dhtmlXComboFromSelect("equipmentImageId");
imageIdCombo.setSize(302);
imageIdCombo.attachEvent("onChange",function()
                       {
                               //var activityValue=imageIdCombo.getSelectedValue();
                               getSelectedData(imageIdCombo.getSelectedValue());
                               return true;
                               //alert(activityValue);
                       });
imageIdCombo.attachEvent("onBlur",function(){processComboData(this.name,this.getSelectedText());});
            </logic:notEqual>

			
var siteCombo = dhtmlXComboFromSelect("siteId");
//siteCombo.setOptionWidth(102);
siteCombo.setSize(202);
siteCombo.attachEvent("onChange",function(){});
siteCombo.attachEvent("onBlur",function(){processComboData(this.name,this.getSelectedValue());});


function deviceEdit(key)
{
    document.getElementById('editable').style.display="block";
    document.getElementById('readonly').style.display="none";
    document.getElementById('edit').disabled="disabled";
}
function processComboData(objName,objValue)
{
	equipDataJson[objName] = objValue;
}
function processEquipData(obj)
{
    equipDataJson[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
}
function viewImageLocally(imageURL)
{//alert(imageURL);
//imageURL='http://localhost/imageserver/@32';
    var action = "OpenAperioImage.do?imageURL="+imageURL+"&operation=imageScope";
    mywindow=window.open(action,"Download","width=10,height=10");
    mywindow.moveTo(0,0);
}
function downloadImage(imageURL)
{
//imageURL='http://localhost/imageserver/@32';
//alert(imageURL);
    var loader = dhtmlxAjax.postSync("OpenAperioImage.do","imageURL="+imageURL+"&operation=download");
    if(loader.xmlDoc.responseText != null)
    {
        var resp = loader.xmlDoc.responseText;
		
        var openurl = "http://"+resp;
		
        window.open(resp);
    }
}
function viewInWebScope(imageURL)
{
//imageURL='http://localhost/imageserver/@32';
//alert(imageURL);
    
        window.open(imageURL);
    
}



</script>