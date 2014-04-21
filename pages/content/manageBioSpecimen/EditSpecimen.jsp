<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.dto.SpecimenDTO"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
    var imgsrc="images/";
    window.dhx_globalImgPath = "dhtmlx_suite/imgs/";
</script>
<style>
.errorStyleOn{
    border: 1px solid #FF0000;
}
</style>
<style type="text/css">
    #myoutercontainer { text-align: center;display:block;float: left; }
    #myinnercontainer { display: block; vertical-align: middle;*overflow: hidden;}    
</style>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_skyblue.css" />

<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>

<link href="css/catissue_suite.css" type="text/css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<link rel="stylesheet" type="text/css" href="css/tag-popup.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_skyblue.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxToolbar/codebase/skins/dhtmlxtoolbar_dhx_blue.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.css"/>


<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTree/codebase/dhtmlxtree.js"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/ext/dhtmlxcombo_whp.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxAccordion/codebase/dhtmlxcontainer.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTree/codebase/ext/dhtmlxtree_li.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTreeGrid/codebase/dhtmlxtreegrid.js"></script>
<script language="JavaScript"type="text/javascript" src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script>


<script type="text/javascript" src="jss/tag-popup.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"    src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"    src="jss/caTissueSuite.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/json2.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/specimen.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>

<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxcontainer.js"></script>

<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxLayout/codebase/dhtmlxcontainer.js"></script>

<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxcontainer.js"></script>


<style>
.noEffect{
    border:4px solid blue;
    color:red;
}
</style>
<script>
    var imgsrc="images/";
    window.dhx_globalImgPath = "dhtmlx_suite/imgs/";
</script>
<!----------------------------------------------------------------------->
<body onload="LoadSCGTabBar('${requestScope.operation}');"> 
<html:form action="NewSpecimenEdit.do">

<html:hidden name="specimenDTO" property="generateLabel"/>
<html:hidden name="specimenDTO" property="operation"/>
<html:hidden name="specimenDTO" property="parentSpecimenId"/>
<html:hidden name="specimenDTO" property="id" styleId="id"/>
<html:hidden name="specimenDTO" property="specimenCollectionGroupId" styleId="scgId"/>
                                
    <table width="100%" border="0" cellpadding="0" cellspacing="0" height="100%">
      <tr>
        <td>
            <div id="specimen_tabbar" style="width:99%; height:98%;position:absolute;">
            <div id='specimenDetailsDiv' style="overflow:auto;height:100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
            <tr>
            <td>
                <div id="mainTable"style="display:block">
                <table width="100%"  border="0" cellpadding="3" cellspacing="0" >
                <tr>
                    <td>
                        <div id="error" class="alert alert-error" style="display:none">
                            <strong>Error!</strong> <span id="errorMsg">Change a few things up and try submitting again.</span>
                        </div>
                        <div id="print-error" class="alert alert-error" style="display:none">
                            Change a few things up and try submitting again.
                        </div>
                        <div class="alert alert-success" id="success" style="display:none">
                           Specimen Updated Sucessfully.
                        </div>
                        <div class="alert alert-success" id="print-success" style="display:none">
                           Specimen Label Printed successfully.
                        </div>
                        
                    </td>
                </tr>
                <tr>
                  <td align="left" class="showhide">
                    <table width="100%" border="0" cellpadding="3" cellspacing="0" >
                        <!-- NEW SPECIMEN REGISTRATION BEGINS-->
                        <tr class="tr_alternate_color_lightGrey">
                <logic:empty name="specimenDTO" property="parentSpecimenName">
                <logic:notEmpty name="specimenDTO" property="specimenCollectionGroupName">
                         <td width="20%" class="black_ar align_right_style">
                            <label for="specimenCollectionGroupName">
                                <bean:message key="newSpecimen.groupName"/>
                            </label>
                         </td>
                         <td width="30%" align="left" class="black_ar">
                            <html:hidden name="specimenDTO" property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
                                <label for="specimenCollectionGroupName">
                                    <bean:write name="specimenDTO" property="specimenCollectionGroupName" scope="request"/>
                                </label>
                         </td>
                </logic:notEmpty>
                </logic:empty>
                
                <logic:notEmpty name="specimenDTO" property="parentSpecimenName">
                          <td width="20%" class="black_ar align_right_style">
                                <label for="parentSpecimenId">
                                    <bean:message key="newSpecimen.parentLabel"/>
                                </label>
                          </td>
                          <td width="30%" align="left" class="black_ar">
                                
                                <html:hidden name="specimenDTO" property="parentSpecimenName"/>
                                <label for="parentSpecimenId">
                                    <bean:write name="specimenDTO" property="parentSpecimenName" scope="request"/>
                                </label>

                          </td>
                </logic:notEmpty>

                          <td width="20%"  class="black_ar align_right_style">
                                <label for="lineage">
                                    <bean:message key="specimen.lineage"/>
                                </label>
                          </td>
                          <td width="30%" align="left" class="black_ar">
                            <label for="lineage">
                                <bean:write name="specimenDTO" property="lineage" scope="request"/>
                                <html:hidden name="specimenDTO" property="lineage" styleId="lineage"/>
                            </label>
                          </td>
                    </tr>
                        <tr class="tr_alternate_color_white">
                            <td width="20%" class="black_ar align_right_style">
                                <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
                                <label for="label">
                                    <bean:message key="specimen.label"/>
                                </label>
                            </td>
                            <td align="left" width="30%">
                                <html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" name="specimenDTO" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)" disabled="false" />
                            </td>

                            <td width="20%" class="black_ar align_right_style">
                                <label for="barcode">
                                    <bean:message key="specimen.barcode"/>
                                </label>
                            </td>
                        
                            <td width="30%" align="left" class="black_ar">
                                <label for="barcode">
                                    <html:text name="specimenDTO" 
                                               styleClass="black_ar" maxlength="255" size="30"
                                               styleId="barcode" property="barcode" onblur="processData(this)" disabled="false"/>
                                </label>
                            </td>
                        </tr>
                        <tr class="tr_alternate_color_lightGrey">
                            <td  width="20%" class="black_ar align_right_style">
                                <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                <label for="className">
                                    <bean:message key="specimen.type"/>
                                </label>
                            </td>
                            <td width="30%" align="left" class="black_new">
                            <html:select property="className" name="specimenDTO" 
                                         styleClass="formFieldSized19" styleId="className" size="1"
                                         onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                                <html:options collection="specimenClassList"
                                    labelProperty="name" property="value" />
                           </html:select>
                            </td>

                            <td width="20%" class="black_ar align_right_style">
                                <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                <label for="type">
                                     <bean:message key="specimen.subType"/>
                                </label>
                            </td>
                            <td width="30%" align="left" class="black_new">
                            <html:select property="type" name="specimenDTO" 
                            styleClass="formFieldSized19" styleId="type" size="1"
                            onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                            <html:options collection="specimenTypeList"
                                labelProperty="name" property="value" />
                            </html:select>
                        
                              </td>
                            </tr>
                            
                            <tr class="tr_alternate_color_white">
                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="tissueSite">
                                        <bean:message key="specimen.tissueSite"/>
                                    </label>
                                </td>

                                <td>
                                <table style="border-collapse: collapse;">
                                    <tr>
                                    <td>    
                                    <html:select property="tissueSite" name="specimenDTO" 
                                    styleClass="formFieldSized19" styleId="tissueSite" size="1"
                                    onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                                    <html:options collection="tissueSiteList"
                                        labelProperty="name" property="value" />
                                    </html:select> 
                                    </td>
                                    </tr>
                                    </table>
                                </td>

                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="tissueSide">
                                        <bean:message key="specimen.tissueSide"/>
                                    </label>
                                </td>
                                <td width="30%" align="left" class="black_new">
                                <html:select property="tissueSide" name="specimenDTO" 
                            styleClass="formFieldSized19" styleId="tissueSide" size="1"
                            onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
                            <html:options collection="tissueSideList"
                                labelProperty="name" property="value" />
                        </html:select>
                                </td>
                            </tr>
                            
                            <tr class="tr_alternate_color_lightGrey">
                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="pathologicalStatus">
                                        <bean:message key="specimen.pathologicalStatus"/>
                                    </label>
                                </td>
                            
                                <td width="30%" align="left" class="black_new">
                                <html:select property="pathologicalStatus" name="specimenDTO" 
                            styleClass="formFieldSized19" styleId="pathologicalStatus" size="1"
                            onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
                            <html:options collection="pathologicalStatusList"
                                labelProperty="name" property="value" />
                        </html:select>
                                </td>
                            
                                <td width="20%" class="black_ar align_right_style">
                                    <label for="createdDate">
                                        <bean:message key="specimen.createdDate"/>
                                    </label>
                                </td>
                                <td width="30%" class="black_ar" >
                                <input type="text" name="createdDate" class="black_ar"
                                   id="createdDate" size="10" onblur="processData(this)" value='<fmt:formatDate value="${specimenDTO.createdDate}" pattern="${datePattern}" />'/>
                                <span id="dateId" class="grey_ar_s capitalized">[<bean:message key="date.pattern" />]</span>&nbsp;
                                </td>
                            </tr>
                            
                            <tr class="tr_alternate_color_white">
                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="quantity">
                                        <bean:message key="specimen.quantity"/>
                                    </label>
                                </td>
                                <td width="30%" align="left" class="black_ar">
                                    <html:text styleClass="black_ar" size="10" maxlength="10"  styleId="quantity" property="quantity" name="specimenDTO"  style="text-align:right" onblur="processData(this)"/>
                                     <span id="unitSpan">
                
                                     </span>
                                     <html:hidden property="unit"/>
                                     <div id="quantityErrorMsg" style="display:none; color:red;">
                                     </div>
                                </td>
                                <td width="20%" class="black_ar align_right_style">
                                    <label for="concentration">
                                        <bean:message key="specimen.concentration"/>
                                    </label>
                                </td>
                                <td  width="30%" align="left" class="black_ar">
                                        <html:text styleClass="black_ar" maxlength="10"  size="10" styleId="concentration" property="concentration" style="text-align:right" name="specimenDTO" onblur="chkeEmptyNumber(this);processData(this)"
                                         disabled="false"/>
                                        <bean:message key="specimen.concentrationUnit"/>
                                        <div id="concentrationErrorMsg" style="display:none; color:red;">
                                     </div>
                                </td>
                            </tr>
            
                            <tr class="tr_alternate_color_lightGrey">
                                 <td width="20% class="black_ar">&nbsp;
                                 </td>
                                 <td width="30%" align="left" valign="top" >
                                 <logic:equal name="operation" value="add">
                                    <input type="checkbox" name="available" id="available" onblur="processData(this)" disabled checked="checked"/>
                                </logic:equal>
                                <logic:equal name="operation" value="edit">
                                        <html:checkbox property="available" styleId="available" onclick="processData(this)" >
                                    </html:checkbox>
                                </logic:equal>
                                    
                                    <span class="black_ar" style="padding-bottom:7px">
                                        <label for="available">
                                            <bean:message key="specimen.available" />
                                        </label>
                                    </span>
                                </td>
                                <td width="20%" class="black_ar align_right_style">
                                    <label for="availableQuantity">
                                            <bean:message key="specimen.availableQuantity" />
                                    </label>
                                </td>
                                <td width="30%" align="left" class="black_ar">
                                <logic:equal name="operation" value="add">
                                    <html:text styleClass="black_ar" maxlength="10"  size="10" styleId="availableQuantity" readonly="true" property="availableQuantity"     name="specimenDTO" style="text-align:right" onblur="processData(this)"/>
                                </logic:equal>
                                <logic:equal name="operation" value="edit">
                                    <html:text styleClass="black_ar" maxlength="10"  size="10" styleId="availableQuantity" property="availableQuantity"     name="specimenDTO" style="text-align:right" onblur="processData(this)"/>
                                </logic:equal>
                                    <span id="unitSpan1"></span>
                                    <div id="avlQuantityErrorMsg" style="display:none; color:red;">
                                     </div>
                                </td>
                            </tr>
                        
                            <tr class="tr_alternate_color_white">
                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="collectionStatus">
                                    <bean:message key="specimenCollectionGroup.collectionStatus" />

                                    </label>
                                </td>
                                <td width="30%" class="black_new">
                                <logic:equal name="operation" value="edit">
                                <div id="" style="display:block"/>
                                </logic:equal>
                                <logic:equal name="operation" value="add">
                                
                                    <bean:write name="specimenDTO" property="collectionStatus"/>                                                
                                    <div id="" style="display:none"/>
                                </logic:equal>
                                <html:select property="collectionStatus" name="specimenDTO" 
                                             styleClass="formFieldSized19" styleId="collectionStatus" size="1"
                                             onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                                <html:options collection="collectionStatusList"
                                              labelProperty="name" property="value" />
                                </html:select>
                                </div>
                                
                                </td>

                                <td width="20%" class="black_ar align_right_style">
                                    <label for="activityStatus">
                                        <bean:message key="participant.activityStatus" />
                                    </label>
                                </td>
                                <td width="30%" align="left" class="black_ar">
                                    <c:choose>
                                        <c:when test="${specimenDTO.activityStatus == 'Active'}">
                                            <label for="activityStatus">
                                                <bean:write name="specimenDTO" property="activityStatus" scope="request"/>
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <html:select property="activityStatus" name="specimenDTO" 
                                                     styleClass="formFieldSized19" styleId="activityStatus" size="1"
                                                     onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                                            <html:options collection="activityStatusList"
                                                      labelProperty="name" property="value" />
                                            </html:select>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        
                            <tr class="tr_alternate_color_lightGrey">
                                <td width="20%" class="black_ar align_right_style">
                                    <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />    
                                    <label for="className">
                                        <bean:message key="specimen.positionInStorageContainer"/>
                                    </label>
                                </td>
                                                                
                                <td colspan="3" class="black_ar">
                    <!-------Select Box Begins----->
                    
                                <logic:equal name="specimenDTO" property="isVirtual" value="true">
                                    
                                    <input type="text" size="30" maxlength="255"  class="black_ar tr_alternate_color_lightGrey"  value='Virtually Located' readonly style="border:0px;" id="storageContainerPosition" title="Virtually Located"/>
                                </logic:equal>
                                <logic:equal name="specimenDTO" property="isVirtual" value="false">
                                <input type="text" size="30" maxlength="255"  class="black_ar tr_alternate_color_lightGrey"  value='<bean:write name="specimenDTO" property="containerName" scope="request"/>:(<bean:write name="specimenDTO" property="pos1" scope="request"/>,<bean:write name="specimenDTO" property="pos2" scope="request"/>)' readonly style="border:0px" id="storageContainerPosition" title='<bean:write name="specimenDTO" property="containerName" scope="request"/>:(<bean:write name="specimenDTO" property="pos1" scope="request"/>,<bean:write name="specimenDTO" property="pos2" scope="request"/>)'/>
                                    
                                </logic:equal>
                                <logic:equal name="operation" value="add">
                                <a href="#" onclick="javascript:loadDHTMLXWindowForTransferEvent();return false">
                            <img src="images/uIEnhancementImages/grid_icon.png" alt="Displays the positions for the selected container"  width="16" height="16" border="0" style="vertical-align: middle" title="Displays the positions for the selected container"></a>
                                    
                                </logic:equal>
                                <logic:equal name="operation" value="edit">
                                <logic:equal name="specimenDTO" property="collectionStatus" value="Collected">
                                    <a href="#" onclick="javascript:loadDHTMLXWindowForTransferEvent();return false">
                            <img src="images/uIEnhancementImages/grid_icon.png" alt="Displays the positions for the selected container"  width="16" height="16" border="0" style="vertical-align: middle" title="Displays the positions for the selected container"></a>
                                </logic:equal>
                                </logic:equal>
                                <logic:equal name="specimenDTO" property="collectionStatus" value="Pending">
                                    <a href="#" onclick="javascript:loadDHTMLXWindowForTransferEvent();return false">
                            <img src="images/uIEnhancementImages/grid_icon.png" alt="Displays the positions for the selected container"  width="16" height="16" border="0" style="vertical-align: middle" title="Displays the positions for the selected container"></a>
                                </logic:equal>
                                <span style="left-padding:5px">
                                <a href="#" onclick="javascript:openViewMap();return false">
            <img src="images/uIEnhancementImages/Tree.gif" border="0" width="16" height="16" style="vertical-align: bottom" title="select positions from hierarchical view"/>
        </a>
                                </span>
                                <html:hidden name="specimenDTO" property="isVirtual" styleId="isVirtual"/>
                                <html:hidden name="specimenDTO" property="containerName" styleId="containerName"/>
                                <html:hidden name="specimenDTO" property="pos1" styleId="pos1"/>
                                <html:hidden name="specimenDTO" property="pos2" styleId="pos2"/>
                                <html:hidden name="specimenDTO" property="containerId" styleId="containerId" />
                                </td>
                            </tr>

                            <tr class="tr_alternate_color_white">
                                <td width="20%" valign="top" class="black_ar align_right_style">
                                    <label for="comments">
                                        <bean:message key="specimen.comments"/>
                                    </label>
                                </td>
                                <td align="left" valign="top" colspan="3">
                                    <html:textarea styleClass="black_ar_s"  rows="2" cols="90" name="specimenDTO" styleId="comments" property="comments" onblur="processData(this)"/>
                                </td>
                            </tr>
                                
                            <tr class="tr_alternate_color_lightGrey">
                                <td width="20%"  class="black_ar align_right_style">
                                    <bean:message key="specimen.externalIdentifier"/>
                                </td>
                                <td width="30%">
                                    <a id="addExternalId" title="Add New External Identifier" class="link" onclick="showAddExternalIdDiv()">Add New</a>
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
                            
                            <tr class="tr_alternate_color_lightGrey">
                                <td width="20%" > &nbsp;    </td>
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
                                
                                <tr class="tr_alternate_color_white">
                                    <td width="20%" class="black_ar align_right_style">
                                        <bean:message key="specimen.biohazards"/>
                                    </td>

                                    <td width="30%">
                                        <a id="addBioHazard" title="Add New BioHazard" class="link" onclick="showAddBioHazardDiv()">Add New</a>
                                    </td>
                                    <td  width="50%" class="black_ar" colspan="2">
                                        <div id="addBioHazardDiv" style="display:none;">
                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                            <tr valign="bottom">
                                                <td width="44%"> 
                                                    <div id="biohazardTypeSelect"></div>
                                                </td>
                                                <td width="44%">
                                                    <div id="biohazardSelect"></div>
                                                </td>
                                                <td width="44%">
                                                    <input id="addEditBioHazButton" name="addEditBioHazButton" type="button" value="Add" class="black_ar" onclick="addEditBioHazTag(this)" />
                                                </td>
                                            </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                                
                                <tr class="tr_alternate_color_white">
                                    <td width="20%"> &nbsp;</td>    
                                    <td align="left" colspan="3" valign="middle">
                                        <ul id="bioHazardList" class="tagEditor">
                                            <c:if test="${not empty specimenDTO.bioHazards}">
                                                    <c:forEach var="biohazard" items="${specimenDTO.bioHazards}">
                                                        <li id="li${biohazard.id}" title="Edit">
                                                            <span id="Bio_${biohazard.id}" name="Biohazards" onclick="editBiohazardTag(this)">${biohazard.type} - ${biohazard.name}</span>
                                                            <a title="Delete" onclick="deleteTag(this)">X</a>
                                                            <input type="hidden" name="Bio_${biohazard.id}Status" id="Bio_${biohazard.id}Status" value=${biohazard.status}>
                                                        </li>
                                                    </c:forEach>        
                                            </c:if>
                                        </ul>
                                    </td>
                                </tr>
                        <tr>
                          <td colspan="4" valign="middle" class="tr_bg_blue1">
                            <span class="blue_ar_b">&nbsp;<bean:message key="childSpecimen.label" /></span>
                          </td>
                        </tr>
                                
                            </table>
                          </td>
                        </tr>
                        
                    
                        
                            <!-- collectionstatus -->
                        </table>
                    </div>
                </td>
            </tr>
            
            <tr>
                                <!--
          <td valign="middle" class="black_ar" >
          -->
          <td valign="top">
          <table width="100%" border="0" cellpadding="3" cellspacing="0">
               <tr>
                  <td width="17%" align="left" nowrap class="black_ar" colspan="2"><div id="myinnercontainer">
                        <input type="radio" value="1" id="aliquotCheck" name="specimenChild" onclick="onCheckboxButtonClick(this)" checked="true" style="vertical-align: middle;"/>
                                <span class="black_ar" style="vertical-align: middle;">
                                <bean:message key="app.none" />&nbsp;
                                </span>
                        <input type="radio" value="2" id="aliquotChk" name="specimenChild" onclick="onCheckboxButtonClick(this)" style="vertical-align: middle;"/>
                        <span class="black_ar" style="vertical-align: middle;">
                                <bean:message key="aliquots.title"/>
                                &nbsp;
                                </span>
                        <input type="radio" value="3" id="deriveChk" name="specimenChild" onclick="onCheckboxButtonClick(this)" style="vertical-align: middle;"/>
                                <span class="black_ar" style="vertical-align: middle;">
                                <bean:message key="specimen.derivative" />
                                &nbsp;</span>
                        <!-- 11706 S Desctiption : Remove equal check for Edit operation only....-->
                        <input type="radio" value="4" id="createCpChildCheckBox" name="specimenChild" onclick="onCheckboxButtonClick(this)" style="vertical-align: middle;"/>
                        <span class="black_ar" style="vertical-align: middle;">
                                <bean:message key="create.childAsperCP"/>
                                </span></div>
                        <!-- 11706 E -->
                    </td>
                </tr>

                <!--specimenPageButton-->
                <tr><td colspan="2"></td></tr>
                <tr>
                    <td class="black_ar" width="18%" nowrap colspan="2">
                             <div style="display:none" id="derivedDiv">
                             
                             <bean:message key="derive.noOfSpecimens"/>&nbsp;
                            <html:text styleClass="black_ar" styleId="numberOfSpecimens" size="10" property="numberOfSpecimens" style="text-align:right"/>
                        </div>
                        <div style="display:none" id="aliquotDiv">
                            
                            <bean:message key="aliquots.noOfAliquots" />&nbsp;
                            <html:text styleClass="black_ar" styleId="noOfAliquots" size="10" property="noOfAliquots" disabled="true" style="text-align:right"/>
                            &nbsp;
                            <bean:message key="aliquots.qtyPerAliquot"/>&nbsp;

                            <html:text styleClass="black_ar" styleId="quantityPerAliquot" size="10" property="quantityPerAliquot" disabled="true" style="text-align:right"/>
                            </div>
                    </td>
                    
                            
                            
                </tr>
                 <tr>
                                <td class="dividerline" colspan="3"><span class="black_ar"></td>
                                </tr>
                                <tr>
                                

                                            <td colspan="1" valign="center">
                                                    <html:checkbox styleId="printCheckbox" property="printCheckbox" value="true">
                                                        <span class="black_ar">
                                                            <bean:message key="print.checkboxLabel"/>
                                                        </span>
                                                        </html:checkbox>
                                            </td>

                                
    <!--  Added for displaying  printer type and location -->
                                  <td>
                                    </td>

                            </tr>
                                                
                <tr>
                    <td align="left" colspan="2" class="buttonbg">
                        <table cellpadding="0" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
                            <tr>
                                <td class="buttonbg">
                                <table><tr><td>
                                    <input type="button" value="Submit" onclick="submitTabData('${requestScope.operation}')" class="blue_ar_b"/>
                                    </td><td>
                                    <div id="specListDiv" style="display:none">
                                    | <input type="button" value="Add To Specimen List"
                                            onclick="organizeTarget()" class="blue_ar_b" />
                                    
                                    </div>
                                    </td></tr></table>
                                    <input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="${specimenDTO.id}" checked/>
                                </td>
                            </tr>
                        </table>
                         <input type="hidden" id="assignTargetCall" name="assignTargetCall" value="giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem','Select at least one existing list or create a new list.','No specimen has been selected to assign.','${specimenDTO.id}')"/>
                        <%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
                    </td>
                </tr>

<!-- NEW SPECIMEN REGISTRATION ends-->
                </table>
            </td>
        </tr>
        <tr>
            <td height="*">&nbsp;</td>
        </tr>
        </table>
        </div> </div>
     </td>
   </tr>
</table>
</html:form>

<script>
var specimenId='${specimenDTO.id}';
var reportId='${identifiedReportId}';
var entityId='${specimenRecordEntryEntityId}';
var staticEntityName='${entityName}';
<logic:equal name="operation" value="edit">
var hasConsents = ${hasConsents};
</logic:equal>
var isImageEnabled = ${isImageEnabled};


var showEventsTab = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery&specimenId="+specimenId+"&menuSelected=15";


var showViewSPRTab="ViewSurgicalPathologyReport.do?operation=viewSPR&pageOf=pageOfNewSpecimenCPQuery&reportId="+reportId+"&id="+specimenId;

var showAnnotationTab="LoadAnnotationDataEntryPage.do?entityId="+entityId+"&entityRecordId="+specimenId+"&pageOf=pageOfNewSpecimenCPQuery&operation=viewAnnotations&staticEntityName="+staticEntityName+"&id="+specimenId;

var showConsentsTab="FetchConsents.do?consentLevelId="+specimenId+"&consentLevel=specimen&reportId="+reportId+"&pageof=pageOfNewSpecimenCPQuery&entityId="+entityId+"&staticEntityName="+staticEntityName;

var showImagesTab="EditSpecimenImage.do?id="+specimenId;

// alert(showViewSPRTab);

setLabelBarcodeVisibility('${isSpecimenLabelGeneratorAvl}', '${isSpecimenBarcodeGeneratorAvl}', '${specimenDTO.collectionStatus}','${requestScope.operation}');
doInitCal('createdDate',false,'${uiDatePattern}');
var nodeId= "Specimen_"+document.getElementById("id").value;
//refreshTree(null,null,null,null,nodeId);
                
var tabDataJSON = {};
var spId = document.getElementById("id").value;
if(spId != null && spId != "")
{
    tabDataJSON["id"] = document.getElementById("id").value; 
}

tabDataJSON["specimenCollectionGroupId"] = document.getElementById("scgId").value; 


//initialization of clinicalstudytab page
function initialize(startDateObj,endDateObj)
{
    var startDate = calendar.init(startDateObj,"%m-%d-%Y");
    var endDate = calendar.init(endDateObj,"%m-%d-%Y");
}

function loadDHTMLXWindowForTransferEvent()
{
//alert(document.getElementById('barcode'));
var w =700;
var h =450;
var x = (screen.width / 3) - (w / 2);
var y = 0;
var containerName = document.getElementById('containerName').value;
var isVirtual = document.getElementById('isVirtual').value;


if(isVirtual == 'true')
{
containerName='';
}

dhxWins = new dhtmlXWindows(); 
dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
var pos1 = document.getElementById('pos1').value;
var pos2 = document.getElementById('pos2').value;
var className = classNameCombo.getSelectedText();
var type = typeCombo.getSelectedText();



<logic:equal name="operation" value="edit">
var collStatus="<bean:write name='specimenDTO' property='collectionStatus' scope='request'/>";
</logic:equal>
<logic:equal name="operation" value="add">
var collStatus="Pending";
</logic:equal>

var url = "ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&forwardTo=gridView&pos1="+pos1+"&pos2="+pos2+"&holdSpecimenClass="+className+"&holdSpecimenType="+type+"&containerName="+containerName+"&collectionProtocolId=${requestScope.cpId}&collStatus="+collStatus+"&isVirtual="+isVirtual;

dhxWins.window("containerPositionPopUp").attachURL(url);                     
//url : either an action class or you can specify jsp page path directly here
dhxWins.window("containerPositionPopUp").button("park").hide();
dhxWins.window("containerPositionPopUp").allowResize();
dhxWins.window("containerPositionPopUp").setModal(true);
dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}
initSpecimenCombo();
initializeSpecimenPage('${biohazardTypeNameListJSON}');
prepareSpecimenTypeOptions('${cellTypeListJSON}','${molecularTypeListJSON}','${tissueTypeListJSON}','${fluidTypeListJSON}');

<c:if test="${specimenDTO.collectionStatus=='Collected' and operation=='edit'}">
{
    document.getElementById('specListDiv').style.display='block';
}
</c:if>
function chkeEmptyNumber(obj)
{
    if(obj.value.trim()=="" || obj.value == null)
    {
        obj.value=0;
    }
}
function openViewMap()
{
var className=classNameCombo.getSelectedText();
    var sptype=typeCombo.getSelectedText();
    var frameUrl="ShowFramedPage.do?pageOf=pageOfEditSpecimen&selectedContainerName=containerName&pos1=pos1&pos2=pos2&containerId=containerId"
                        + "&holdSpecimenClass="+className+ "&holdSpecimenType="+sptype  + "&holdCollectionProtocol=${requestScope.cpId}&collStatus=<bean:write name='specimenDTO' property='collectionStatus' scope='request'/>";
        mapButtonClickedOnSpecimen(frameUrl,'SpecimenPage',"containerName");
}
</script>
</body>
