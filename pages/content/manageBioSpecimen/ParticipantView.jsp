<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxcombo_extra.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/participantView.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<script>
      window.dhx_globalImgPath="dhtmlx_suite/imgs/";
     var eventPointLabels = ${requestScope.eventPointLabels}; 
     var scgLabels = ${requestScope.scgLabels}; 
     var specLabelString = ${requestScope.specLabelString};
</script>
<html>
<head>
<LINK type=text/css rel=stylesheet href="css/participantEffects.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css" />
</head>
<body onload="initComboForSCGEvents()">

<input type="hidden" name="requestFrom" value="participantView" />
<input type="hidden" name="CPQuery" value="CPQuery" />
<input type="hidden" name="pId" id="pId" value="${participantDto.participantId}" />
<input type="hidden" name="cpId" id="cpId" value="${participantDto.cpId}" />
<input type="hidden" name="cprId" id="cprId" value="${requestScope.cprId}" />


<table width="100%" border="0"  cellpadding="10" cellspacing="0" class="whitetable_bg"> 
    <tr class="tr_bg_blue1 blue_ar_b">
            <td  class="heading_text_style">
                <bean:message key="participant.view.profile.summary"/> <span id="summaryParticipantName"></span>
                [<bean:write name="participantDto" property="ppid" />]
                &nbsp <a href="#" onClick="showEditPage()">Edit</a>
            </td>
    </tr>
    <tr>
        <td class="bottomtd"></td>
    </tr>
    
</table>

<div id="participantDetails" class="align_left_style">
<fieldset class="field_set"> 
  <legend class="blue_ar_b legend_font_size"> <bean:message key="participant.view.participant.details"/></legend>
    <table width="100%" border="0"  cellpadding="5px" cellspacing="0"   class="whitetable_bg">
        <tr>
            <td  align="right" class="black_ar  padding_right_style" width="20%">
                <b><bean:message key="participant.view.birth.date"/></b> 
            </td>
            <td class="black_ar" width="20%">
                <c:if test="${empty participantDto.dob}">
                    <bean:message key="participant.view.not.specified"/>
                 </c:if>
                <fmt:formatDate value="${participantDto.dob}" pattern="${datePattern}" />
            </td>
            <td align="right" class="black_ar padding_right_style" width="30%"> 
                <b><bean:message key="participant.view.MRN"/></td></b>
            <td class="black_ar" width="30%"> 
                  <logic:iterate id="partMrn" name="participantDto"  property="mrns" indexId="indx">
<c:if test="${indx > 0}">
                      ,
                   </c:if>
                    <bean:write name="partMrn" property="mrn" /> ( <bean:write name="partMrn" property="siteName" /> )
                 </logic:iterate>  
                 <c:if test="${empty participantDto.mrns}">
                    <bean:message key="participant.view.not.specified"/>
                 </c:if>
       </tr>
       
       <tr class="tr_alternate_color_lightGrey">
         <td  align="right" class="black_ar bottomtd  padding_right_style" width="20%"> 
            <b><bean:message key="participant.view.registration.date"/></b>
         </td> 
         <td class="black_ar bottomtd" width="20%">
                <fmt:formatDate value="${participantDto.registrationDate}" pattern="${datePattern}" />
                <c:if test="${empty participantDto.registrationDate}">
                    <bean:message key="participant.view.not.specified"/>
                 </c:if>
        </td>
        <td  align="right" class="black_ar bottomtd  padding_right_style" width="30%"> 
                         <b><bean:message key="participant.view.isConsented"/></b>
        </td>
        <td class="black_ar bottomtd" width="30%"> 
            <bean:write name="participantDto" property="isConsented" />
        </td>
      </tr>
    
    <tr>
        <td  width="20%" align="right" class="black_ar padding_right_style">
            <b><bean:message key="participant.gender"/></b>
        </td>
        <td  width="20%" class="black_ar"> <bean:write name="participantDto" property="gender" />
        <c:if test="${empty participantDto.gender}">
                    <bean:message key="participant.view.not.specified"/>
                 </c:if>
        </td>
        <td  width="30%" align="right" class="black_ar padding_right_style"><b><bean:message key="participant.race"/></b></td>
        <td  width="30%" class="black_ar" > <logic:iterate id="prace" name="participantDto"  property="race" indexId="indx">
<c:if test="${indx > 0}">
                      ,
                   </c:if>
                    <bean:write name="prace" /> 
                 </logic:iterate>   
                 <c:if test="${empty participantDto.race}">
                    <bean:message key="participant.view.not.specified"/>
                 </c:if></td>
    </tr>
    <tr class="tr_alternate_color_lightGrey">
        <td  width="30%" align="right" class="black_ar padding_right_style">
            <b><bean:message key="participant.view.profile.associated.cp"/></b>
        </td>
        <td colspan="3"  width="70%"  class="black_ar" > 
            ${cpTitleList}
        </td>
    </tr>
    <!--tr class="tr_alternate_color_lightGrey">
            <td width="20%" align="right" class="black_ar padding_right_style">
                <html:button  styleClass="blue_ar_b"    property="editParticipant"  title="Edit Only" value="Edit"  onclick="showEditPage()">
                </html:button>
            </td>
            <td width="20%">
                
        
    </tr-->
    </table>
</fieldset>
</div>
<p></p>


    <div id="specimen&SCGDetails" class="align_left_style">
    <fieldset class="field_set">    
     
    <table width="100%" border="0"  cellpadding="5px" cellspacing="0" class="whitetable_bg">
    
        <tr class="tr_bg_blue1 blue_ar_b">
            <td  class="heading_text_style" colspan="4">
               <b><bean:message key="participant.view.scg.actions"/></b>
            </td>
      </tr> 
            <tr>    
            <td align="right" width="20%" class="black_ar padding_right_style"> 
                <b><bean:message key="participant.view.select.event.point"/></b>
                </td>
            <td width="35%">
                <div id="eventsList"> </div>
            </td>
            <td width="35%" class="black_ar">   
             <span style="vertical-align:bottom">
                <a href="javascript:addNewScg()" title="Add" style="padding-right:7px;"><bean:message key="label.addSCG"/></a>
                
                
                <!-- <a href="#" title="View"> <img src="images/Action-view.png" alt="View" onclick="scgOperation('view')"></a> &nbsp;
                <a href="#" title="Delete"><img src="images/Action-close.png" alt="Delete" onclick="scgOperation('delete')"></a> &nbsp;
                <a href="#" title="Print"><img src="images/Action-print.png" alt="Print" onclick="scgOperation('print')"></a> -->
             </span>
            </td>
</td> <td width="20%"></td>
</tr>
<tr class="tr_alternate_color_lightGrey">   
            <td width="20%" class="black_ar padding_right_style"> 
                <span style="float:left"  class="blue_ar_b"><b>OR</b></span>
                <span style="float:right">
                <b><bean:message key="label.selectSCG"/> </b></span>
                </td>
            <td width="35%">
                <div id="scgList"> </div>
            </td>
            <td width="35%" class="black_ar">   
             <span style="vertical-align:bottom">
                
                <a href="javascript:editScg()" title="Edit" style="padding-right:7px;"><bean:message key="label.editSCG"/></a>
                
                
             </span>
            </td>
</td> <td width="20%"></td>
</tr>
            <!--          -->
        <tr>
         
                <td class="black_ar" width="20%">
                
                                <span style="margin-left:5px;">
                                <span class="black_ar" style="vertical-align:2px;float:right;">
                                <b><bean:message key="label.collectSpecimen"/></b> &nbsp;&nbsp;</span>
                </td><td  width="35%">
                     <input type="radio" value="1" id="multipleChk" name="specimenChild" onclick="disableButtonsOnCheck(this)" checked="true"/>
                                <span class="black_ar" style="vertical-align:2px">
                                <b><bean:message key="label.collect.perCP"/></b>
                                </span>
                                </span>
                                <span style="margin-left:30px;">
                        <input type="radio" value="2" id="multipleChk" name="specimenChild" onclick="disableButtonsOnCheck(this)"/>
                                <span class="black_ar" style="vertical-align:2px">
                                <b><bean:message key="label.adhocSpecimen"/></b>
                                
                                &nbsp;
                                <input type="text" style="text-align:right;border:0px;" size="1" maxlength="2" id="numberOfSpecimens" name="numberOfSpecimens"  onblur="isNumeric(this)" readOnly="true"/>
                        </span></span>
                        </td>
                <td style="vertical-align:1px"  width="35%">
                        <span align="left" style="vertical-align:1px">
                        <html:button  styleClass="blue_ar_b" property="aliquot" title="Aliqut" value=" Go " onclick="createNewSpecimens()">
                </html:button>
                        </span>
</td>
            </tr>
            <!--          -->

<tr> <td colspan="4"></td> </tr>
    <tr class="tr_bg_blue1 blue_ar_b">
        <td  class="heading_text_style" colspan="4">
                <bean:message key="participant.view.specimen.actions"/>
            </td> 
     </tr>      
    <tr>    
        <td class="black_ar  padding_right_style" width="20%" align="right"> 
                     <b><bean:message key="participant.view.select.specimen"/></b>
        </td>
        <td width="35%">
                <div id="specimenLabels"></div>
        </td>
        <td width="25%" class="black_ar">       
            <span style="vertical-align:bottom">
                <a href="javascript:editSpecimen()" title="Edit"><bean:message key="specimen.edit.title"/></a>&nbsp;
                <!-- <a href="#" title="View"> <img src="images/Action-view.png" alt="View"></a> &nbsp;
                <a href="#" title="Delete"><img src="images/Action-close.png" alt="Delete"></a> &nbsp;
                <a href="#" title="Print"><img src="images/Action-print.png" alt="Print"></a> -->
             </span>
        </td> <td width="20%"></td>
    </tr>

    <tr width="100%" class="tr_alternate_color_lightGrey">
        <td width="20%"  align="right" class="black_ar  padding_right_style">
                    <b><bean:message key="participant.view.create.aliquot"/></b>
        </td>
        <td width="25%" >
            <input type="text" name="noOfAliquots" id="noOfAliquots" size ="5" value="Count"  class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
                        
            <input type="text" name="quantityPerAliquot" id="quantityPerAliquot" size ="5" value="Quantity" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
        </td>
        <td width="25%" >
                <html:button  styleClass="blue_ar_b" property="aliquot" title="Aliqut" value=" Go " onclick="createAliquote()">
                </html:button>
                
        </td> <td width="25%" ></td>
    </tr>

    <tr>
        <td width="20%"  class="black_ar padding_right_style" align="right" >
                    <b><bean:message key="participant.view.create.derivative"/></b>
        </td>
        <td width="25%">
             <input type="text" name="count" id="derivative_count" size ="5" value="Count" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
        </td>
        <td width="25%">
                <html:button  styleClass="blue_ar_b" property="derivative" title="derivative" onclick="createDerivative()" value=" Go ">
                </html:button>
                
        </td>
        <td width="25%" ></td>
    </tr>

<!--    <tr height="33px" class="tr_alternate_color_lightGrey">
        <td class="black_ar padding_right_style" width="25%" align="right">
                    <bean:message key="participant.view.add.event"/>  
        </td>
        <td width="25%">    
                <div class="black_ar" id="addSpecimenEvents"></div>
        </td>
        <td width="25%" >
             <html:button  styleClass="blue_ar_b" property="addEvent" title="Add Event" value=" Go ">
             </html:button>
        </td>
        <td width="25%"> </td>
    </tr> -->
</table>
</fieldset>
</div>
</body>
</html>
<script>

                
var summaryFirstName = '<bean:write name="participantDto" property="firstName" />';
var summaryLastName = '<bean:write name="participantDto" property="lastName" />';
function disableButtonsOnCheck(chkbox)
{
    if(chkbox.value == 1)
    {
        document.getElementById('numberOfSpecimens').value="";
        document.getElementById('numberOfSpecimens').readOnly = true;
        document.getElementById('numberOfSpecimens').style.border="0px";
    }
    else if(chkbox.value == 2)
    {
        document.getElementById('numberOfSpecimens').readOnly = false;
        document.getElementById('numberOfSpecimens').value=1;
        document.getElementById('numberOfSpecimens').style.border="1px solid grey";
        document.getElementById('numberOfSpecimens').focus();
    }
}

function isNumeric(number) {
value = number.value;
if(value==Number(value))
return true;
else
{
    alert("Invalid Number, Please enter valid number.");
    number.value="";
    setTimeout(function(){number.focus();}, 1);
}
}
</script>