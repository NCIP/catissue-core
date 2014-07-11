<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxcombo_extra.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/participantView.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_skyblue.css" />
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>

<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<style type="text/css">
    #myoutercontainer { text-align: center;display:block;float: left; }
    #myinnercontainer { display: block; vertical-align: middle;*overflow: hidden;}   
    .cprHeadingMargin {margin-left:50px;}
    .windowElementPaddingLeft {padding-left:10px}
    .windowElementPaddingTop {padding-top:15px;}
</style>
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
            <div style="width : 100%;">
            <div style="float:left;height:100%;max-width:70%;">
            <span id="cpTitleList">${cpTitleList}</span>
            </div>
            <div style="float:left;margin-left:25px;;height:100%;">
               <input type="button" class="blue_ar_b" value="Register New" onclick="createNewCPR()" />
            </div>
                
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
                </td><td  width="35%"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr ><td>
                  <div id="myinnercontainer"><span>
                     <input type="radio" value="1" id="multipleChk" name="specimenChild" onclick="disableButtonsOnCheck(this)" checked="true" style="vertical-align: middle;margin-bottom:1px;"/>
                                <span style="font-family: verdana;font-size: 11px;">
                                <label style="vertical-align: middle;">Planned</label>
                                </span></span>
                                <span style="margin-left:30px;">
                        <input type="radio" value="2" id="multipleChk" name="specimenChild" onclick="disableButtonsOnCheck(this)" style="vertical-align: middle;margin-bottom:1px"/>
                                <span style="font-family: verdana;font-size: 11px;">
                                <label style="vertical-align: middle;">Unplanned</label>
                        </span></span>
                        &nbsp;<input type="text" style="text-align:right;border:0px;font-family: verdana;font-size: 11px;" size="1" maxlength="2" id="numberOfSpecimens" name="numberOfSpecimens"  onblur="isNumeric(this)" readOnly="true"/>
                 </div></td></tr></table>
                        </td>
                <td width="35%">
                        <span align="left">
                        <html:button  styleClass="blue_ar_b" property="aliquot" title="Collect Specimens" value=" Go " onclick="createNewSpecimens()">
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
var partView = {};
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

var dhxWins;
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
var popupCPCombo;
var cprDateCal;
function createNewCPR(){
    if(dhxWins == undefined){
        dhxWins = new dhtmlXWindows();
        dhxWins.setSkin("dhx_skyblue");
        dhxWins.enableAutoViewport(true);
    }
    dhxWins.setImagePath("");
    if(dhxWins.window("registerCPRPopUP")==null){
            var w =525;
            var h =190;
            var x = (screen.width / 3) - (w / 2);
            var y = 0;
            dhxWins.createWindow("registerCPRPopUP", x, y, w, h);
            dhxWins.window("registerCPRPopUP").center();
            dhxWins.window("registerCPRPopUP").setModal(true);
            dhxWins.window("registerCPRPopUP").setText("Register to additional protocol");
            dhxWins.window("registerCPRPopUP").button("minmax1").hide();
            dhxWins.window("registerCPRPopUP").button("park").hide();
            dhxWins.window("registerCPRPopUP").button("close").hide();
             //dhxWins.window("containerPositionPopUp").setModal(false);
            
        }else{
            dhxWins.window("registerCPRPopUP").show();
        }
        var div = document.createElement("div");
        div.id = "popupDiv";
        //var div = document.getElementById("popupDiv");
        div.innerHTML = "  <div id='errorMsg' style='min-height:18px;'></div> <div class='black_ar windowElementPaddingLeft '>  <div style='float:left;'><b>Collection Protocol <span style='color:red'>*<span></b> <div id='popupCpList' style='margin-top:4px;'> </div></div>  <div style='float:left; margin-left: 10px; margin-right: 10px;'> <b>Participant Protocol ID <span style='color:red'>*<span></b><div style='margin-top:4px;'><input type='text' id='popupPPID' style = 'font-size: 12px;width:140px;'/> <br><span id='egAutogeneratedString' style='font-size: 9px;color: #808080;  font-size: 9px;font-style: italic;display:none;'>e.g of auto generated PPID</span><br><input type='button'  value='Register' onClick='registerParticipant()'> <input type='button'  value='Cancel'  onClick='closeMultipleCprWindow()'></div></div>  <div ><b>Registration Date</b> <div style='margin-top:4px;'><input type='text' id='popupCPRDate' style = 'font-size: 12px;'/></div></div> </div>"
        
        +""+
        
        "";
        

        document.body.appendChild(div);
        dhxWins.window("registerCPRPopUP").attachObject("popupDiv");
        popupCPCombo = new dhtmlXCombo("popupCpList", "popupCpList", 150);
        popupCPCombo.enableFilteringMode(true);
        popupCPCombo.attachEvent("onSelectionChange",checkPPIDFormat);
        cprDateCal = doInitCal('popupCPRDate',false,'${uiDatePattern}');
        loadCpList(popupCPCombo);
}
function checkPPIDFormat(){
    var cpID = popupCPCombo.getSelectedValue()
    var ppIdCompleteFormat = getPPIDFormat(cpID);
    if(ppIdCompleteFormat == null || ppIdCompleteFormat == undefined || ppIdCompleteFormat == ""){
        document.getElementById("popupPPID").disabled = false;
        document.getElementById("egAutogeneratedString").style.display = "none";
        document.getElementById("popupPPID").value = "";
        
    }else{
        var ppidFormat = ppIdCompleteFormat.substring(1,(ppIdCompleteFormat.length-"\",PPID".length));//
        document.getElementById("popupPPID").disabled = true;
        document.getElementById("egAutogeneratedString").style.display = "block";
        
        var patt=/%\d+d/;
        patt.compile(patt); 
        var res = ppidFormat.split(patt);
        var digitOfPPI =  patt.exec(ppidFormat);
        var samplePPID = "";
        if(res.length == 2){
            samplePPID = res[0]+getSampleDigit(digitOfPPI)+res[1];
        }else if(res.length == 1){
            if(str.indexOf(ppidFormat)==0){
                samplePPID = res[0]+getSampleDigit(digitOfPPI);
            }else{
                samplePPID = getSampleDigit(digitOfPPI)+res[0];
            }
        }else{
            samplePPID = getSampleDigit(digitOfPPI);
        }
        document.getElementById("popupPPID").value = samplePPID;
        
    }
}


function showErrorMessage(msg)
{
            
    var errMsgDiv = document.getElementById('errorMsg');
    errMsgDiv.style.display='block';
    errMsgDiv.className='alert alert-error';
    errMsgDiv.innerHTML=msg;
    return;
}

function getSampleDigit(digit){
    var digitOfPPI=/\d+/;
    digitOfPPI.compile(digitOfPPI); 
    var digitOfPPI = digitOfPPI.exec(digit);
        var retVal = "";
        for(var cnt =0; cnt<digitOfPPI-1; cnt++){
            retVal += "0"
        }
        retVal += "7";
        return retVal;
}
function registerParticipant(){
    //var cprDate = new Date(document.getElementById("popupCPRDate").value);
    //var cprDateStr = cprDate.format("Y-m-d");
	if((!document.getElementById("popupPPID").disabled&&document.getElementById("popupPPID").value.trim()=="")||popupCPCombo.getSelectedValue()==""){
		showErrorMessage("All items marked with asterisk(*) are required");
		return;
	}
    var jsonDetail = {};
    var participantDetail = {};
    participantDetail.id = '${participantDto.participantId}';
    jsonDetail.participantDetail= participantDetail
    jsonDetail.cpId = popupCPCombo.getSelectedValue();
    jsonDetail.ppid = document.getElementById("popupPPID").disabled ?"":document.getElementById("popupPPID").value;
    jsonDetail.registrationDate = cprDateCal.getDate();
    var req = createRequest(); // defined above
    // Create the callback:
    req.onreadystatechange = function() {
        if (req.readyState != 4) return; // Not there yet
        if (req.status != 200) {
            var errorMsg=req.getResponseHeader("errorMsg");
    showErrorMessage(errorMsg);
    return;
        }else{
            document.getElementById("cpTitleList").innerHTML = document.getElementById("cpTitleList").innerHTML + ","+popupCPCombo.getSelectedText();
            closeMultipleCprWindow();
            
        }
    }
    req.open("POST", "rest/ng/collection-protocols/"+jsonDetail.cpId+"/registrations", false);
    req.setRequestHeader("Content-Type",
                     "application/json");
    req.send(JSON.stringify(jsonDetail));
    
}
function createRequest() {
  var result = null;
  if (window.XMLHttpRequest) {
    // FireFox, Safari, etc.
    result = new XMLHttpRequest();
   
  }
  else if (window.ActiveXObject) {
    // MSIE
    result = new ActiveXObject("Microsoft.XMLHTTP");
  } 
  else {
    // No known mechanism -- consider aborting the application
  }
  return result;
}


function loadCpList(popupCPCombo)
{
    var req = createRequest(); // defined above
    // Create the callback:
    req.onreadystatechange = function() {
        if (req.readyState != 4) return; // Not there yet
        var resp = req.responseText;
        partView.cpList = eval('('+resp+')');
        for(var cnt = 0;cnt < partView.cpList.length;cnt++){
            popupCPCombo.addOption(partView.cpList[cnt].id,partView.cpList[cnt].shortTitle)
        }
        
    }
    req.open("GET", "rest/ng/collection-protocols", false);
    req.setRequestHeader("Content-Type",
                     "application/json");
    req.send();
}

function getPPIDFormat(cpId){
    for(var cnt = 0;cnt < partView.cpList.length;cnt++){
            if(partView.cpList[cnt].id==cpId){
                return partView.cpList[cnt].ppidFormat;
            }
        }
    return null;
}

function closeMultipleCprWindow(){
    dhxWins.window("registerCPRPopUP").close();
}

</script>
