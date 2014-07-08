<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
   
<link rel="STYLESHEET" type="text/css"  href="newDhtmlx/version3/dhtmlxGrid/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="newDhtmlx/version3/dhtmlxGrid/skins/dhtmlxgrid_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css"  href="newDhtmlx/version3/dhtmlxCombo/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css"/>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css"/>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />

<link rel="stylesheet" type="text/css" href="css/tag-popup.css" />

<script src="newDhtmlx/version3/dhtmlxGrid/dhtmlxcommon.js"></script>
<script type="text/javascript" src="newDhtmlx/version3/dhtmlxGrid/dhtmlxgrid.js"></script>
<script type="text/javascript" src="newDhtmlx/version3/dhtmlxGrid/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="newDhtmlx/version3/dhtmlxCombo/dhtmlxcombo.js"></script>
<script type="text/javascript" src="newDhtmlx/version3/dhtmlxGrid/excells/dhtmlxgrid_excell_combo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>

<script src="jss/json2.js" type="text/javascript"></script>
<script src="jss/ajax.js" type="text/javascript"></script>

<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script src="jss/calendarComponent.js"></script>
<script language="JavaScript" type="text/javascript"    src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"    src="jss/script.js"></script>
<script language="JavaScript" type="text/javascript"    src="jss/common.js"></script>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<script type="text/javascript" src="jss/specimen.js"></script>


<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxtree.css">

<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>
<STYLE type="text/css">
<!--
.aliquot_details{padding-top: 3px; padding-bottom: 3px;}
.aliquot_details_spacing{padding-left: 6px;  padding-bottom: 3px;}
.aliquot_form_label{margin-top: 4px;}
ul li ul li a span {
    text-decoration:none;
    display: inline-block;
}
-->
</STYLE> 
<script>
        var isLabelGenerationOn = '${isLabelGenerationOn}';
        var isBarGenerationOn = '${isBarGenerationOn}';
        var aliquotDateErr = false;
        var aliquotGrid;
        var aliquotPopUpParam = {};
        var aliquotNameSpace = {};
		var parentSpeCildCount = 0;
		var parentLabel = "";
        function showConflictSpecimenWindow(conflictingSpecimens){
            if(aliquotNameSpace.dhxWins == undefined){
                aliquotNameSpace.dhxWins = new dhtmlXWindows();
                aliquotNameSpace.dhxWins.setSkin("dhx_skyblue");
                aliquotNameSpace.dhxWins.enableAutoViewport(true);
            }
            aliquotNameSpace.dhxWins.setImagePath("");
            if(aliquotNameSpace.dhxWins.window("containerPositionPopUp")==null){
                var w =300;
                var h =190;
                var x = (screen.width / 3) - (w / 2);
                var y = 150;
                aliquotNameSpace.dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
                //aliquotNameSpace.dhxWins.setPosition(x, y);
                //aliquotNameSpace.dhxWins.window("containerPositionPopUp").center();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").allowResize();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setModal(true);
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setText("Conflicting Specimens");
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("minmax1").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("park").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").button("close").hide();
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").setIcon("images/terms-conditions.png", "images/terms-conditions.png");
                 //dhxWins.window("containerPositionPopUp").setModal(false);
                var div = document.createElement("div");
            
                div.id="aliquotPopupDiv";
                div.innerHTML = "<div style='padding:10px;padding-bottom:0px;' class='black_ar'>"
                +"<span>Select the right specimen and click OK</span></br>"
                +"</div>"
                +"<div width='100%'  class='black_ar'  style='margin-left:10px;margin-right:10px; margin-top: 4px;'>"
            +"<div  style='float:left;width:25%;margin-top:14px;' >"
                +"<div  class='aliquot_details aliquot_form_label'><input type='radio' name='aliquotRadio' value='"+conflictingSpecimens[0].label+"' style='margin-left:25px;' checked></div>"
                +"<div  class='aliquot_details aliquot_form_label'><input type='radio' name='aliquotRadio' value='"+conflictingSpecimens[1].label+"' style='margin-left:25px;'></div>"
            +"</div>"
            +"<div  style='float:left;width:25%;'><div class='black_ar_b'><spna>Label</span></div>"
                +"<div  class='aliquot_details aliquot_form_label'><span>"+conflictingSpecimens[0].label+"</span>&nbsp</div>"
                +"<div  class='aliquot_details aliquot_form_label'><span>"+conflictingSpecimens[1].label+"</span></div>"
            +"</div>"
            +"<div  style='float:left;width:25%;'><div class='black_ar_b'><spna>Barcode</span></div>"
                +"<div  class='aliquot_details aliquot_form_label'><span>"+conflictingSpecimens[0].barcode+"</span></div>"
                +"<div  class='aliquot_details aliquot_form_label'><span>"+conflictingSpecimens[1].barcode+"</span></div>"
            +"</div>"
            +"<div style='clear: both'></div>"
                +"</div>"
                +"<div style='padding:10px;padding-left: 41px;' class='black_ar'>"+
                "<input type='button' name='Ok' onClick='loadAliquotDetail()' value='Ok' style='margin-left:45px'><input type='button'  value='Cancel' name='Cancel' onClick='closeTermWindow()'style='margin-left:6px'></div>";
                document.body.appendChild(div);
                aliquotNameSpace.dhxWins.window("containerPositionPopUp").attachObject("aliquotPopupDiv");
            }
        }
        function loadAliquotDetail(){
            var selectedLabel = getCheckedRadioId("aliquotRadio");
            onResubmit('true',selectedLabel);
            document.getElementById("parentSpecimentLabel").value = selectedLabel;
            closeTermWindow();
        }
        
        function getCheckedRadioId(name) {
            var elements = document.getElementsByName(name);
            for (var i=0, len=elements.length; i<len; ++i)
                if (elements[i].checked) return elements[i].value;
        }
        function closeTermWindow(){
            aliquotNameSpace.dhxWins.window("containerPositionPopUp").close();
        }
        function init_grid(){       
            aliquotGrid = new dhtmlXGridObject('gridbox');
            aliquotGrid.setImagePath("dhtmlx_suite/imgs/");
            aliquotGrid.setHeader("Label,Barcode,Quantity,Storage Container <a style='margin-left:62px;' href='#' onclick='applyFirstToALL()'>Apply first to all</a>,Position 1,Position 2,Container Map,");
            aliquotGrid.setEditable("true");
            aliquotGrid.enableAutoHeigth(false);
            aliquotGrid.enableRowsHover(true,'grid_hover')
            aliquotGrid.setInitWidths("100,100,70,250,70,70,100");
            aliquotGrid.setColTypes("ed,ed,ed,ed,ed,ed,ro,ro");
            aliquotGrid.setColumnHidden(7,true);
            
            if(isLabelGenerationOn=="true"){
                aliquotGrid.setColumnExcellType(0,"ro");
            }
            if(isBarGenerationOn=="true"){
                aliquotGrid.setColumnExcellType(1,"ro");
            }
            
            aliquotGrid.setColumnExcellType(3,"combo");
            aliquotGrid.setSkin("dhx_skyblue");
        //  aliquotGrid.setColSorting("str,str,str,str,str,str,str");
            aliquotGrid.enableMultiselect(true);
            aliquotGrid.setColAlign("left,left,center,left,center,center,center")
             aliquotGrid.setSerializableColumns("true,true,true,true,true,true,false")
            aliquotGrid.init();
            //var xmlString = "<rows>   <row>       <cell><![CDATA[${gridData.quantity}]]></cell>       <cell><![CDATA[${gridData.aliqoutLabel}]]></cell>       <cell><![CDATA[ATCC_Box_Fluid_Specimens_15]]></cell>            <cell><![CDATA[f]]></cell>          <cell><![CDATA[8]]></cell>      <cell><![CDATA[${gridData.barCode}]]></cell>         </row>     </rows>";
            aliquotGrid.enableEditEvents(true);
            aliquotGrid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){
                if(stage==2 ){
                    if(cInd == 3 && nValue != oValue){
                    aliquotGrid.cellByIndex(rId,4).setValue("");
                    aliquotGrid.cellByIndex(rId,5).setValue("");
                    }
                }
                return true;
                
            });
            onGridLoad(aliquotGrid);
            onGridLoadSetAutoGenerated(aliquotGrid);
            
        }
        function setComboValues(aliquotGrid,containerList)
        {
            var combo = aliquotGrid.getColumnCombo(3);
            combo.clearAll();
            combo.enableFilteringMode(true);
            var status = containerList.split(',');
            for(var row=0;row<status.length;row++)
            {
                combo.addOption(status[row],status[row]);
            }
        }
        function onGridLoadSetAutoGenerated(grid_obj,rowCount){
            if(isLabelGenerationOn=="true" || isLabelGenerationOn){
            aliquotGrid.setColumnExcellType(0,"ro");
                for(var cnt=1;cnt<grid_obj.getRowsNum();cnt++)
                {
                    grid_obj.cellByIndex(cnt,0).setValue("Auto Generated");
                }
            }
            else
            {
                aliquotGrid.setColumnExcellType(0,"ed");
				for(var cnt=1;cnt<grid_obj.getRowsNum();cnt++)
                {
					parentSpeCildCount = parentSpeCildCount+1;
                    grid_obj.cellByIndex(cnt,0).setValue(parentLabel+"_"+parentSpeCildCount);
                }
            }
            if(isBarGenerationOn=="true" || isBarGenerationOn){
            aliquotGrid.setColumnExcellType(1,"ro");
                for(var cnt=1;cnt<grid_obj.getRowsNum();cnt++)
                {
                    grid_obj.cellByIndex(cnt,1).setValue("Auto Generated");
                }
            }
            else
            {
                aliquotGrid.setColumnExcellType(1,"ed");
            }
        }
        function onGridLoad(grid_obj,rowCount){
            for(var cnt=1;cnt<grid_obj.getRowsNum();cnt++){
                grid_obj.cellByIndex(cnt,6).setValue("<a href='#' onclick='javascript:openMapPopUp(\""+cnt+"\");return false'><img src='images/uIEnhancementImages/grid_icon.png' alt='Displays the positions for the selected container'  width='16' height='16' border='0' style='vertical-align: middle' title='Displays the positions for the selected container'></a>&nbsp;<a href='#' onclick='javascript:openViewMap(\""+cnt+"\");return false'><img src='images/uIEnhancementImages/Tree.gif' border='0' width='16' height='16' style='vertical-align: bottom' title='select positions from hierarchical view'/></a>");
                
            }
        }
        function openMapPopUp(rowNum){
            aliquotGrid.selectRow(rowNum);
            var containerName = aliquotGrid.cellByIndex(rowNum,3).getValue();
            var pos1 = aliquotGrid.cellByIndex(rowNum,4).getValue();
            var pos2 = aliquotGrid.cellByIndex(rowNum,5).getValue();
                String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g, '');};
            
                loadDHTMLXWindowForTransferEvent(containerName,pos1,pos2,rowNum);
            
        }
        
        function openViewMap(rowNum)
        {
            aliquotGrid.selectRow(rowNum);
            var containerName = aliquotGrid.cellByIndex(rowNum,3).getValue();
            var pos1 = aliquotGrid.cellByIndex(rowNum,4).getValue();
            var pos2 = aliquotGrid.cellByIndex(rowNum,5).getValue();
                String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g, '');};
            
                var frameUrl = "ShowFramedPage.do?pageOf=pageOfNewAliquot&xDimStyleId=" + pos1 + "&yDimStyleId=" + pos2 +  "&holdCollectionProtocol="+aliquotPopUpParam.cpID+"&holdSpecimenClass="+aliquotPopUpParam.specimenClass+"&holdSpecimenType="+aliquotPopUpParam.type;
                frameUrl+="&storageContainerName="+containerName+"&containerStyle="+rowNum;
                openPopupWindow(frameUrl,'transferEvents');
            
        }
        
        function loadDHTMLXWindowForTransferEvent(containerName,pos1,pos2,rowNum)
        {
            var w =700;
            var h =450;
            var x = (screen.width / 3) - (w / 2);
            var y = 0;
            dhxWins = new dhtmlXWindows(); 
            dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
            var className = '${aliquotDetailsDTO.specimenClass}';
            var type = '${aliquotDetailsDTO.type}';
            var isVirtual = containerName=="virtual"?true:false;

            var url = "ShowStoragePositionGridView.do?pageOf=pageOfNewAliquot&forwardTo=gridView&pos1="+pos1+"&pos2="+pos2+"&holdSpecimenClass="+className+"&holdSpecimenType="+type+"&containerName="+containerName+"&collectionProtocolId=${requestScope.cpId}&collStatus=collected&isVirtual="+isVirtual+"&controlName="+rowNum;

            dhxWins.window("containerPositionPopUp").attachURL(url);                     
            //url : either an action class or you can specify jsp page path directly here
            dhxWins.window("containerPositionPopUp").button("park").hide();
            dhxWins.window("containerPositionPopUp").allowResize();
            dhxWins.window("containerPositionPopUp").setModal(true);
            dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
        }
        function setNewStoragePositionForAliquot(rowNum,containerName,pos1,pos2){
            aliquotGrid.cellByIndex(rowNum,3).setValue(containerName);
            aliquotGrid.cellByIndex(rowNum,4).setValue(pos1);
            aliquotGrid.cellByIndex(rowNum,5).setValue(pos2);
        }
        function getValueStoragePositionForAliquot(rowNum,containerName,pos1,pos2){
            return aliquotGrid.cellByIndex(rowNum,3).getValue()+","+
            aliquotGrid.cellByIndex(rowNum,4).getValue()+","+
            aliquotGrid.cellByIndex(rowNum,5).getValue();
            
        }
        function submitAliquot(){
            var tabDataJSON = {};
            var aliquotsXml = aliquotGrid.serialize();
            //tabDataJSON["aliquotsXml"] = aliquotsXml;
            var aliquotDetailDto = {};
            tabDataJSON["disposeParentSpecimen"] = document.getElementById("disposeParentSpecimen").checked;
            tabDataJSON["printLabel"]=  document.getElementById("printCheckbox").checked;
            tabDataJSON["creationDate"]= document.getElementById("creationDate").value;
            tabDataJSON["specimenClass"]= document.getElementById("ali_specimenClass").innerHTML;
            tabDataJSON["type"]=document.getElementById("ali_type").innerHTML;
            tabDataJSON["parentLabel"]= document.getElementById("parentSpecimentLabel").value;
            tabDataJSON["perAliquotDetailsCollection"]=getJsonFromGrid();
            //tabDataJSON["aliquotDetailDto"] = aliquotDetailDto;
            
            
            var request = newXMLHTTPReq();
            request.onreadystatechange=function(){
                if(request.readyState == 4)
                {  
                    //Response is ready
                    if(request.status == 201)
                    {
                        var responseString = request.responseText;
                        var response = eval('('+responseString+')');
                        if(response.success)
                        {
                            document.getElementById('error').style.display='none';
                            document.getElementById('success').style.display='block'
                            document.getElementById('successMsg').innerHTML=response.msg;
                            document.getElementById('submitButtonId').style.display='none'
                            document.getElementById('successButtonDiv').style.display='block';
                            var aliquotDetailsDTO = eval('('+response.aliquotDetailsDTO+')')
                            nodeId= "Specimen_"+aliquotDetailsDTO.parentId;
                            parent.handleCpView(null, aliquotDetailsDTO.scgId, aliquotDetailsDTO.parentId);
                            
                            if(aliquotGrid.getRowsNum()!=0){
                                deleteAllRow();
                            }
                            loadAliquotGridWithNewData(aliquotGrid,aliquotDetailsDTO);
                            //aliquotGrid.loadXMLString(response.aliquotGridXml);
                            onGridLoad(aliquotGrid);
                            document.getElementById("ali_currentAvailableQuantity").innerHTML = aliquotDetailsDTO.currentAvailableQuantity+" "+ response.unit;;
                            var idList = getCommaseparatedIdList();
                            document.getElementById('assignTargetCall').value = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+idList+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+idList+"')";
                        
                            //refreshTree(null,null,null,null,nodeId);
                            
                        }else{
                            document.getElementById('success').style.display='none'
                            document.getElementById('error').style.display='block';
                            document.getElementById('errorMsg').innerHTML=request.getResponseHeader("errorMsg");
                            
                            document.getElementById('submitButtonId').style.display='block'
                            document.getElementById('successButtonDiv').style.display='none';
                        }
                        
                    }else if(request.status != 201){
                        document.getElementById('success').style.display='none'
                        document.getElementById('error').style.display='block';
                        document.getElementById('errorMsg').innerHTML=request.getResponseHeader("errorMsg");
                        
                        document.getElementById('submitButtonId').style.display='block'
                        document.getElementById('successButtonDiv').style.display='none';
                    }
                    
                    
                }   
            };
            request.open("POST","rest/specimens/1/aliquots",true);
            request.setRequestHeader("Content-Type","application/json");
            request.send(JSON.stringify(tabDataJSON));
            
        //  request.send(tabDataJSON);
            
        }
function loadaliquotDetail(){
    
    if('${pageOf}'!='fromMenu'){
        //onResubmit();
        onResubmit(true,document.getElementById("parentSpecimentLabel").value)
    }else{
        document.getElementById("resubmitButton").value = "Submit";
    }
	init_grid();
}
function getJsonFromGrid(){
//"Label,Barcode,Quantity,Storage Container,Position 1,Position 2,Container Map,"
    
    var aliquotArray = new Array();
    var aliLabel = "";
    var aliBarcode = "";
        for(var cnt=1;cnt<aliquotGrid.getRowsNum();cnt++){
        if(isLabelGenerationOn == "false" || !isLabelGenerationOn)
        {
            aliLabel = aliquotGrid.cellByIndex(cnt,0).getValue();
        }
        if(isBarGenerationOn == "false" || !isLabelGenerationOn)
        {
            aliBarcode = aliquotGrid.cellByIndex(cnt,1).getValue();
        }
            var aliquotObj = {
                quantity:aliquotGrid.cellByIndex(cnt,2).getValue(),
                aliqoutLabel:aliLabel,
                storagecontainer:aliquotGrid.cellByIndex(cnt,3).getValue(),
                pos1:aliquotGrid.cellByIndex(cnt,4).getValue(),
                barCode:aliBarcode,
                pos2:aliquotGrid.cellByIndex(cnt,5).getValue()
                
            };
            aliquotArray[cnt-1] = aliquotObj;
        }
    
    return aliquotArray;
}
function onResubmit(validated,label){
    if(aliquotDateErr)
    {
        var msg="Unable to submit. Please resolve higlighted issue(s).";
        document.getElementById('errorMsg').innerHTML = msg;
        document.getElementById('error').style.display="block";
        return;
    }
    
        document.getElementById('error').style.display="none";
    var aliquotCount = document.getElementById("noOfAliquots").value;
    var quantityPerAliquot = document.getElementById("quantityPerAliquot").value;
    var specimenLabel = document.getElementById("parentSpecimentLabel").value;
    var searchBasedOn = "label";
    if(validated==null || validated == undefined){
        validated = "false";
    }else{
         specimenLabel = label
    }
    var paramStr = {validated:validated,
label:specimenLabel,
count:aliquotCount,
quantity:quantityPerAliquot};

var request = newXMLHTTPReq();
            request.onreadystatechange=function(){
                    if(request.readyState == 4)
                    {  
                        //Response is ready
                        if(request.status == 201)
                        {
                        var responseString = request.responseText;
                            var response = eval('('+responseString+')');
                            if(response.success=="true"){
                        document.getElementById("aliquotDetailsDiv").style.display="block";
                        document.getElementById('error').style.display='none';
                        document.getElementById('errorMsg').innerHTML="";
                        document.getElementById('success').style.display='none';
                        var aliquotDetailsDTO = eval('('+response.aliquotDetailsDTO+')')
                        document.getElementById("ali_currentAvailableQuantity").innerHTML = aliquotDetailsDTO.currentAvailableQuantity+" "+ response.unit;
                        if(aliquotDetailsDTO.concentration!=undefined)
                        document.getElementById("ali_concentration").innerHTML = aliquotDetailsDTO.concentration + ' <bean:message key="specimen.concentrationUnit"/>';
                        else
                        document.getElementById("ali_concentration").innerHTML = "N/A";
                        isLabelGenerationOn = response.isLabelGenerationOn;
                        isBarGenerationOn = response.isBarGenerationOn;
                        //init_grid();
                        document.getElementById("ali_tissueSide").innerHTML = aliquotDetailsDTO.tissueSide;
                        document.getElementById("ali_type").innerHTML = aliquotDetailsDTO.type;
                        
                        document.getElementById("ali_specimenClass").innerHTML = aliquotDetailsDTO.specimenClass;
                        document.getElementById("ali_tissueSite").innerHTML = aliquotDetailsDTO.tissueSite;
                        document.getElementById("ali_pathologicalStatus").innerHTML = aliquotDetailsDTO.pathologicalStatus;
                        document.getElementById("ali_initialAvailableQuantity").innerHTML = aliquotDetailsDTO.initialAvailableQuantity+" "+ response.unit;
                        parentSpeCildCount = response.specimenChildCount;
						parentLabel= response.parentLabel
						if(aliquotDetailsDTO.creationDate!=undefined && aliquotDetailsDTO.creationDate!=""){
                //          document.getElementById("creationDate").value = aliquotDetailsDTO.creationDate;
                        }
                        if(aliquotGrid.getRowsNum()!=0){
                            deleteAllRow();
                        }
                        loadAliquotGridWithNewData(aliquotGrid,aliquotDetailsDTO);
                        onGridLoad(aliquotGrid);
                        onGridLoadSetAutoGenerated(aliquotGrid);
                        setComboValues(aliquotGrid,response.availabelContainerName);
                        aliquotPopUpParam.cpID = response.cpID;
                        aliquotPopUpParam.specimenClass = aliquotDetailsDTO.specimenClass
                        aliquotPopUpParam.type = aliquotDetailsDTO.type
                        document.getElementById('submitButtonId').style.display='block'
                        document.getElementById('successButtonDiv').style.display='none';
                        
                    }else if(response.conflictingSpecimens!=null && response.conflictingSpecimens!= undefined){
                        showConflictSpecimenWindow(response.conflictingSpecimens);
                    }else{
                    document.getElementById("aliquotDetailsDiv").style.display="none";
                    document.getElementById('success').style.display='none';
                    document.getElementById('error').style.display='block';
                    document.getElementById('errorMsg').innerHTML=request.getResponseHeader("errorMsg");
                    document.getElementById("aliquotDetailsDiv").style.display="none";
                    if(aliquotGrid.getRowsNum()!=0){
                            deleteAllRow();
                    }
                    }
                }else if(request.status != 201){
                        document.getElementById("aliquotDetailsDiv").style.display="none";
                    document.getElementById('success').style.display='none';
                    document.getElementById('error').style.display='block';
                    document.getElementById('errorMsg').innerHTML=request.getResponseHeader("errorMsg");
                    document.getElementById("aliquotDetailsDiv").style.display="none";
                    if(aliquotGrid.getRowsNum()!=0){
                            deleteAllRow();
                        }
                }
                }
            };
            request.open("POST","rest/specimens/1/fetchAliquots",true);
            request.setRequestHeader("Content-Type","application/json");
            request.send(JSON.stringify(paramStr));
            //request.send();

    /*var loader=dhtmlxAjax.getSync("rest/specimens/"+specimenLabel+"/aliquots/"+JSON.stringify(paramStr));
        
            if(loader.xmlDoc.responseText != null)
            {
                var response = eval('('+loader.xmlDoc.responseText+')');
                
            }
        */      
}
function deleteAllRow(){
    var ids=aliquotGrid.getAllRowIds().split(",");
    for(var count = 0;count < ids.length;count++){
        aliquotGrid.deleteRow(ids[count]);
    }
    
}

function loadAliquotGridWithNewData(aliquotGrid,aliquotDetailsDTO){
    var gridData = aliquotDetailsDTO.perAliquotDetailsCollection;
    var newId = "rownewId";
    //"Label,Barcode,Quantity,Storage Container,Position 1,Position 2,Container Map,"
    var barcode = "";
    var containerName = "";
    var pos1 = "";
    var pos2 = "";
    for(var cnt = 0; cnt < gridData.length; cnt++){
    if(gridData[cnt].barCode)
        {
            barcode=gridData[cnt].barCode;
        }
    if(gridData[cnt].storagecontainer){containerName = gridData[cnt].storagecontainer;}
    else{containerName="Virtual";}
    if(gridData[cnt].pos1){pos1 = gridData[cnt].pos1;}else{pos1="";}
    if(gridData[cnt].pos2){pos2 = gridData[cnt].pos2;}else{pos2="";}
        
        aliquotGrid.addRow(cnt,gridData[cnt].aliqoutLabel+","+barcode+","+gridData[cnt].quantity+","+containerName+","+pos1+","+pos2+",,"+gridData[cnt].aliquotId,cnt+1);
    }
}
function applyFirstToALL(){
    var contName = aliquotGrid.cellByIndex(1,3).getValue();
    for(var cnt=2;cnt<aliquotGrid.getRowsNum();cnt++){
        aliquotGrid.cellByIndex(cnt,3).setValue(contName);
        aliquotGrid.cellByIndex(cnt,4).setValue("");
        aliquotGrid.cellByIndex(cnt,5).setValue("");
    }
}
function getCommaseparatedList(){
    label = "";
    for(var cnt=1;cnt<aliquotGrid.getRowsNum();cnt++){
        if(cnt!=1){
            label = label+","
        }
        label = label+aliquotGrid.cellByIndex(cnt,0).getValue();
    }
    return label;
}
function getCommaseparatedIdList(){
    label = "";
    for(var cnt=1;cnt<aliquotGrid.getRowsNum();cnt++){
        if(cnt!=1){
            label = label+","
        }
        label = label+aliquotGrid.cellByIndex(cnt,7).getValue();
    }
    return label;
}
function openEventPage()
{
    var action = 'QuickEvents.do?specimenLabel='+getCommaseparatedList()+'&pageOf=CPQuery';
    document.location=action;
    
}
function giveCall(url,msg,msg1,id)
{
    document.getElementsByName('objCheckbox').value=id;
    document.getElementsByName('objCheckbox').checked = true;
    ajaxAssignTagFunctionCall(url,msg,msg1);
}
    </script>

<body onload="loadaliquotDetail()">
<table  width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>

<div  width="100%" id="mainDiv" style="padding:5px;">
    <div id="error" class="alert alert-error" style="display:none">
        <strong>Error!</strong>&nbsp;<span id="errorMsg"></span>
    </div>
    <div class="alert alert-success" id="success" style="display:none">
        <span id="successMsg"></span>
    </div>
    
    <div width="100%"  class="tr_bg_blue1"  style="margin-top:6px;">
            <div style="padding-top:4px">
            <span class="blue_ar_b">&nbsp;
                <bean:message key="aliquots.createTitle"/>
            </span>
            </div>
        </div>
        
        
        <div width="100%"  class="black_ar"  style="margin-top:5px;">
            <div  style="float:left;text-align:right;width:25%;">
                <div  class="aliquot_details aliquot_form_label">
                    <span><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
                    <span  class="black_ar_b"><bean:message key="createSpecimen.parentLabel"/>/Barcode</span>
                </div>
                <div class="aliquot_details aliquot_form_label">
                    <span><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
                    <span  class="black_ar_b"><bean:message key="aliquots.noOfAliquots"/></span>
                </div>
            </div>
            <div  style="float:left;width:25%;">
                <div  class="aliquot_details aliquot_details_spacing">
                    <input type="text" value="${parentSpecimentLabel}" name="specimenLabel" id="parentSpecimentLabel" class="black_ar" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                </div>
                <div  class="aliquot_details aliquot_details_spacing">
                    <input type="text" name="noOfAliquots" id="noOfAliquots" value="${aliquotCount}" class="black_ar"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                </div>
            </div>
            <div  style="float:left;text-align:right;width:25%;">
                <div  class="aliquot_details aliquot_form_label">
                    <span  class="black_ar_b"><bean:message key="specimen.createdDate"/></span>
                </div>
                <div  class="aliquot_details aliquot_form_label">
                    <span  class="black_ar_b"><bean:message key="aliquots.qtyPerAliquot"/></span>
                </div>
            </div>
            <div  style="float:left;width:23%;">
                <div  class="aliquot_details aliquot_details_spacing">
                    <div>
                    <input type="text" size="10" id="creationDate" name = "creationDate" class="black_ar"  onblur="validateAliqDate(this)" value='<fmt:formatDate value="${creationDate}" pattern="${datePattern}" />' onclick="doInitCalendar('creationDate',false,'${uiDatePattern}');" />
                    <span id="dateId" class="capitalized  grey_ar_s" > [<bean:message key="date.pattern" />]</span>&nbsp;
                    </div>
                </div>
                <div  class="aliquot_details aliquot_details_spacing">
                    <input type="text" name="quantityPerAliquot" id="quantityPerAliquot" value="${quantityPerAliquot}" class="black_ar"  onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                </div>

            </div>
            <div style="clear: both" ></div>
        </div>
        
        <div width="100%"   class="black_ar"  style="margin-top:5px;">
            <input type="button"  class="blue_ar_b" id="resubmitButton" value="Resubmit"
                            onclick="onResubmit()">
            
                            
        </div>

    <div width="100%" id="aliquotDetailsDiv" style="display:none">
        <div width="100%"  class="tr_bg_blue1"  style="margin-top:10px;">
            <div style="padding-top:4px">
            <span class="blue_ar_b">&nbsp;
                Aliquot Details
            </span><br/>
            </div>
            
        </div>
        <div width="100%"  class="black_ar"  style="margin-top:5px;">
            <div  style="float:left;text-align:right;width:25%;">
                <div class="aliquot_details">
                <span  class="black_ar_b"><bean:message key="specimen.type"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="specimen.tissueSite"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="specimen.pathologicalStatus"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="aliquots.initialAvailableQuantity"/></span>
                </div>
            </div>
            <div class="black_ar" style="float:left;width:25%;">
                <div class="aliquot_details aliquot_details_spacing">
                <span class="black_ar"" id="ali_specimenClass">
                ${aliquotDetailsDTO.specimenClass}
                </span>
                </div>
                <div  class="aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_tissueSite">
                ${aliquotDetailsDTO.tissueSite}
                </span>
                </div>
                <div  class="aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_pathologicalStatus">
                ${aliquotDetailsDTO.pathologicalStatus}
                </span>
                </div>
                <div  class="aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_initialAvailableQuantity">
                ${aliquotDetailsDTO.initialAvailableQuantity}
                </span>
                </div>
            </div>
            <div style="float:left;text-align:right;width:25%;">
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="specimen.subType"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="specimen.tissueSide"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="specimen.concentration"/></span>
                </div>
                <div  class="aliquot_details">
                    <span  class="black_ar_b"><bean:message key="aliquots.currentAvailableQuantity"/></span>
                </div>
                
                
            </div>
            <div style="float:left;width:23%;">
                <div  class="black_ar aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_type">
                ${aliquotDetailsDTO.type}
                </span>
                </div>
                <div  class="black_ar aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_tissueSide">
                ${aliquotDetailsDTO.tissueSide}
                </span>
                </div>
                <div  class="black_ar aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_concentration" >
                ${aliquotDetailsDTO.concentration}
                </span>
                </div>
                <div  class="black_ar aliquot_details aliquot_details_spacing">
                <span class="black_ar" id="ali_currentAvailableQuantity">
                ${aliquotDetailsDTO.currentAvailableQuantity}
                </span>
                </div>
                
            </div>
            <div style="clear: both" ></div>
        </div>
        
        <div width="100%" id="gridbox" height="250"  style="margin-top:5px;">
        </div>
        <div  width="100%"  class="black_ar"  style="margin-top:5px;">
        <!--input type="checkbox" id="aliqoutInSameContainer" name="aliqoutInSameContainer"  onclick="onCheckboxClicked()"><span class="black_ar align_radio_buttons">  <bean:message key="aliquots.storeAllAliquotes" /></span-->
        <input type="checkbox" id= "disposeParentSpecimen" name="disposeParentSpecimen" ><span style="margin-left:4px;" class="black_ar align_radio_buttons">   <bean:message key="aliquots.disposeParentSpecimen" /></span>
        
        <input type="checkbox" id= "printCheckbox" name="printCheckbox" style="margin-left:20px;" ><span  style="margin-left:4px;" class="black_ar align_radio_buttons">    <bean:message key="print.checkboxLabel"/></span>
        </div>
        <div width="100%"  id="submitButtonId" class="black_ar"  style="margin-top:5px;"  style="display:block">
            <html:button
            styleClass="blue_ar_b" property="Add Events"
            title="Submit"
            value="Submit"
            onclick="submitAliquot()">
            </html:button>
            
        </div>
        <div width="100%"  id="successButtonDiv" class="black_ar"  style="margin-top:5px;"  style="display:none">
            <html:button
            styleClass="blue_ar_b" property="Add Events"
            title="Add To Specimen List"
            value="Add To Specimen List"
            onclick="ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')">&nbsp;|&nbsp;
            </html:button>
            <html:button
            styleClass="blue_ar_b" property="Add Events"
            title="Add Events"
            value="Add Events"
            onclick="openEventPage()">
            </html:button>

        </div>
    </div>
    
    
</div>
<input type="hidden" id="assignTargetCall" name="assignTargetCall" value="giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString=${popUpSpecList}','Select at least one existing list or create a new list.','No specimen has been selected to assign.','${popUpSpecList}')"/>
<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>

</td>
</tr>
</table>
<%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
</body>