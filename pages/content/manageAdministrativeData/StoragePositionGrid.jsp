<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<head>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css"/>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css"/>
<link rel="stylesheet" type="text/css"  href="dhtmlx_suite/css/dhtmlxcombo.css"/>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxdataview.css">  
<style>
body { overflow:auto; }
td {
 width: 40px;
 height: 20px;
}
</style>

<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="dhtmlx_suite/js/dhtmlxdataview.js" type="text/javascript"></script>
</head>

<%
    String collectionProtocolId="";
        if (request.getAttribute(Constants.COLLECTION_PROTOCOL_ID)==null)
            collectionProtocolId="";
        else
         collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);

    String storageContainerName =(String) request.getAttribute(Constants.CONTAINER_NAME);

    String controlName =(String) request.getAttribute("controlName");
    System.out.println("Control name :: "+controlName);
%>

<script>

var globalPageOf="";
var globalContName="";
var globalPos1="";
var globalPos2="";
function getActionToDoURL()
{//alert('invalid.container.name.message');
    var className="${requestScope.holdSpecimenClass}";
    var sptype="${requestScope.holdSpecimenType}";
    var collectionProtocolId="<%=collectionProtocolId%>";
    var containerName="${requestScope.containerName}";
    //alert(containerName);
    if(dhtmlxCombo!=null)
    {
        containerName=dhtmlxCombo.getComboText();
    }
    var url="CatissueCommonAjaxAction.do?type=getStorageContainerListForDHTMLXcombo&holdSpecimenClass="+className+"&specimenType="+sptype+ "&holdCollectionProtocol=" + collectionProtocolId+"&selectedContainerName=${requestScope.containerName}";
    if(reloadGrid!=null && reloadGrid)
    {
        url=url+"&containerName="+containerName;
    }
    if(populateValueInCombo!=null)
    {
        url=url+"&populateValueInCombo="+populateValueInCombo;
    }
    //alert(url);
    return url;
}
</script>
<body onLoad='checkSpecimenStatus();'>
<%@ include file="/pages/content/common/ActionErrors.jsp" %>
    <table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%" >

        <tr>
            <td class="black_ar" align="left">
            <div id="error" style="height:50%;width:100%;"></div>
            </td>
        </tr>
        <tr>
            <td class="bottomtd"></td>
        </tr>
        <tr>
            <td>
                <table valign="center" cellpadding="0" cellspacing="0" border="0" width="660px" >
                    <tr>
                        <td class="black_ar" align="left" style="padding:5px 5px 0 0;"  Width="70px">&nbsp;&nbsp;&nbsp;<span style="white-space:nowrap;"><b>Container Name:</b></span>
                        </td>
                        <td class="black_ar" Width="330px">
                            <div id="comboDiv" style="width:200px;display:block;"></div>
                        </td>           
                        <td class="black_ar"  style="padding-left:15px;"   Width="30px">
                            <input type="text" class="black_ar_md_new"  size="1" id="pos11" name="pos11" value="${sessionScope.pos1Val}"disabled= "false"/>
                        </td>
                        <td class="black_ar" align="left" style="padding-left:5px;"  Width="30px">
                            <input type="text" class="black_ar_md_new"  size="1" id="pos22" name="pos22" value="${sessionScope.pos2Val}" disabled= "false"/>
                            <input type="hidden" id="pos1" name="pos1" />
                            <input type="hidden" id="pos2" name="pos2" />
                        </td>
                        <td class="black_ar" align="right" style="padding-top:2px"   Width="30px">
                            <input type="radio" id ="virtualCont" name="virtualCont" onClick="setVirtual()"/>&nbsp;
                        </td>
                        <td class="black_ar" align="left" style="padding-top:5px"  Width="100px"> <b>Virtual</b>
                        </td>
                    </tr>
                </table>
                <div id="storageContainerNameDiv" style="display:block"></div>
            </td>
        </tr>
        <tr>
            <td class="bottomtd"></td>
        </tr>
        <tr>
            <td>
                <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td style="width:2px;"> </td>
                    <td style="padding:10px 10px 10px 10px;" class="black_ar">
                        <b><span id="dimensionTwoLabel"  style="white-space: nowrap;"></span></b>
                    </td>
                </tr>
                
                <tr>
                    <td style="width:2px; padding:10px 10px 10px 10px;" class="black_ar_vlabel">
                        <b><span id="dimensionOneLabel"></span></b>
                    </td>
                    <td width="100%" height="100%" valign="top">
                        <div id="data_container" style="overflow:hidden;"></div>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
    </table>

</body>
<script>
        
/*var fromStoragePosition1=parent.window.document.getElementById('storageContainerPosition').value;
var fromContainerName1=fromStoragePosition1.substring(0,fromStoragePosition1.lastIndexOf('(')-1);
var storagePositions1=fromStoragePosition1.substring(fromStoragePosition1.lastIndexOf('(')+1,fromStoragePosition1.lastIndexOf(')')).split(",");
document.getElementById('pos1').value=storagePositions1[0];
document.getElementById('pos2').value=storagePositions1[1];*/


function setTextBoxValue(elementId1,elementValue1,elementId2,elementValue2,pageOf)
{

    //alert("elementId1:  "+elementId1+"  elementValue1:  "+elementValue1+"  elementId2:   "+elementId2+"elementValue2:   "+elementValue2);
    if(pageOf == 'pageOfSpecimen')
    {
        var id1 = document.getElementById("pos1");  
        id1.value = elementValue1;
        var id2 = document.getElementById("pos2");  
        id2.value = elementValue2;
    }
    else if(pageOf == 'pageOfderivative')
    {//alert('${requestScope.collStatus}');
    var id1 = document.getElementById("pos1");  
        id1.value = elementValue1;
        var id2 = document.getElementById("pos2");  
        id2.value = elementValue2;
        updatePosition();
    }
    else
    {
        var id1 = document.getElementById(elementId1);  
        id1.value = elementValue1;
        var id2 = document.getElementById(elementId2);  
        id2.value = elementValue2;
    }
    var speCollStat = '${requestScope.collStatus}';
    if(speCollStat != null && speCollStat != "" )
    {
        if(speCollStat == 'Collected')
        {
            transferSpecimen();
        }
        else
        {
            updatePosition();
        }
    }
	
}

function updatePosition()
{
    var pos1=document.getElementById('pos1').value;
    var pos2=document.getElementById('pos2').value;
    var containerName=dhtmlxCombo.getComboText();
    var containerId = dhtmlxCombo.getSelectedValue();
    var storagePosition=parent.window.document.getElementById('storageContainerPosition');
        var newStoragePosition= containerName+" ("+pos1+","+pos2+")";
        storagePosition.value=newStoragePosition;
        storagePosition.title=newStoragePosition;
        //var isVirtual=parent.window.document.getElementById('isVirtual');
        //isVirtual.value=false;
        var container = parent.window.document.getElementById('containerName');
        var position1= parent.window.document.getElementById('pos1');
        var position2= parent.window.document.getElementById('pos2');
        parent.window.document.getElementById('containerId').value=containerId;
        container.value = containerName;
        position1.value = pos1;
        position2.value= pos2;
        var isVirtual = parent.window.document.getElementById('isVirtual');
        
        if(isVirtual != null)
        {
            isVirtual.value="false";
            
        }
        parent.window.dhxWins.window("containerPositionPopUp").close();
}

function transferVrtualSpecimen()
{
    var toPos1="";
    var toPos2="";
    var toContainerName="";
    var fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;
    var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
    var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
    var fromPos1=storagePositions[0];
    var fromPos2=storagePositions[1];
    var specimenId=parent.window.document.getElementsByName('id')[0].value;
    
    var speCollStat = '${requestScope.collStatus}';
    
    if(speCollStat != null && speCollStat != "" )
    {
        if(speCollStat == 'Collected')
        {
    
            var request = newXMLHTTPReq();
            if(request == null)
            {
                alert ("Your browser does not support AJAX!");
                return;
            }
            var handlerFunction = getReadyStateHandler(request,updateSpecimenValues,true);
            
            if((fromContainerName == '' || fromContainerName=='undefined'))
            {
                updateSpecimenValues('virtual');
                return false;
            }
            
            request.onreadystatechange = handlerFunction;
            var param="fromContainerName="+fromContainerName+"&fromPos1="+fromPos1+"&fromPos2="+fromPos2+"&toPos1="+toPos1+"&toPos2="+toPos2+"&toContainerName="+toContainerName+"&isVirtual=${requestScope.isVirtual}&specimenId="+specimenId;

            var url = "TransferEventAction.do";
            request.open("POST",url,true);
            
            request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");   
            request.send(param);
        }
        else
        {
            updateSpecimenValues('virtual');
            return false;
        }
    }
    
    return false;
}
function transferSpecimen()
{

    var toPos1=document.getElementById('pos1').value;
    var toPos2=document.getElementById('pos2').value;
    var toContainerName=dhtmlxCombo.getComboText();
    var fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;
    var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
    var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
    var fromPos1=storagePositions[0];
    var fromPos2=storagePositions[1];
    var specimenId=parent.window.document.getElementsByName('id')[0].value;
    
    var request = newXMLHTTPReq();
    if(request == null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var handlerFunction = getReadyStateHandler(request,updateSpecimenValues,true);
    
    request.onreadystatechange = handlerFunction;
    var param="fromContainerName="+fromContainerName+"&fromPos1="+fromPos1+"&fromPos2="+fromPos2+"&toPos1="+toPos1+"&toPos2="+toPos2+"&toContainerName="+toContainerName+"&isVirtual=${requestScope.isVirtual}&specimenId="+specimenId;

    var url = "TransferEventAction.do";
    request.open("POST",url,true);
    
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");   
    request.send(param);
    return false;
}

function updateSpecimenValues(responseText)
{
    var msg =responseText;
    
    if(msg.indexOf('#') != -1)
    {
        var tag=(msg).split("#");
        var containerName=tag[0];
        var pos1=tag[1];
        var pos2=tag[2];
        var storagePosition=parent.window.document.getElementById('storageContainerPosition');
        var newStoragePosition= containerName+" ("+pos1+","+pos2+")";
        storagePosition.value=newStoragePosition;
        storagePosition.title=newStoragePosition;
        var isVirtual=parent.window.document.getElementById('isVirtual');
        isVirtual.value=false;
        var container = parent.window.document.getElementById('containerName');
        var position1= parent.window.document.getElementById('pos1');
        var position2= parent.window.document.getElementById('pos2');
        container.value = containerName;
        position1.value = pos1;
        position2.value= pos2;
        parent.window.dhxWins.window("containerPositionPopUp").close();
    }
    else if(msg == 'virtual')
    {
        var storagePosition=parent.window.document.getElementById('storageContainerPosition');
        storagePosition.value='Virtually Located';
        storagePosition.title='Virtually Located';
        var isVirtual=parent.window.document.getElementById('isVirtual');
        isVirtual.value=true;
        parent.window.dhxWins.window("containerPositionPopUp").close();
    }
    else
    {
        document.getElementById('error').innerHTML="<font color='red'>"+ msg+"</font>";
    }
}
 function is_numeric (mixed_var) {
  return (typeof(mixed_var) === 'number' || typeof(mixed_var) === 'string') && mixed_var !== '' && !isNaN(mixed_var);
}
function onContainerDetailDisplay(controlName)
{//alert('<%=storageContainerName%>');
    var name;
    var specimenPosition=parent.window.document.getElementById("storageContainerPosition")
        //alert(dhtmlxCombo.getSelectedValue());
    if(dhtmlxCombo!=null && is_numeric(dhtmlxCombo.getSelectedValue()))
    {//alert('f');
        name=dhtmlxCombo.getComboText();
    }
    /*else
    {
        name='<%=storageContainerName%>';
    }*/
    var request = newXMLHTTPReq();  
    if(request == null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var handlerFunction =  getReadyStateHandler(request,createPositionGrid,true);
    request.onreadystatechange = handlerFunction;
    var param = "containerName="+name;
    var url = "NewStorageContainerAjaxAction.do?type=getStorageContainerForDataView";
    request.open("POST",url,true);
    
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");   
    request.send(param);
    return false;
}
var view;

function createPositionGrid(responseString)
{
    var obj1 = eval('( '+ responseString +')');
    var pos1ControlName = obj1.pos1ControlName;
    var pos2ControlName = obj1.pos2ControlName;
    var pageOf = obj1.pageOf;
    var controlName = obj1.controlName;
    globalPageOf=pageOf;
    globalContName=controlName;
    globalPos1 = pos1ControlName;
    globalPos2 = pos2ControlName;
    var dimensionOneLabels = eval('( '+ obj1.dimensionOneLabels +')');
    var dimensionTwoLabels = eval('( '+ obj1.dimensionTwoLabels +')');
    
    var obj = eval('( '+ obj1.containerDTO +')');
    var temp=parent.window.document.getElementById('storageContainerPosition');
    var functionName="setTextBoxValueInParent"
    if(temp!=null)
    {
        functionName="setTextBoxValue";
    }
    
    
    if(obj1.result == 'failure')
    {
        document.getElementById('error').innerHTML="<font color='red'>"+ obj1.message+"</font>";
        view = new dhtmlXDataView({
        container:"data_container",
        type:{
                padding:0,
                template:function(obj1){
                                        
                                    },
                width:25,
                height:25
            },
        x_count:0,
        y_count:0
    });
        return;
    }
    document.getElementById('error').innerHTML="";
document.getElementById('dimensionOneLabel').innerHTML=getDimensionLabel(obj.oneDimensionLabel)+' &darr;';
    document.getElementById('dimensionTwoLabel').innerHTML=getDimensionLabel(obj.twoDimensionLabel)+' &rarr;';
    view = new dhtmlXDataView({
        container:"data_container",
        type:{
                padding:0,
                template:function(obj){
                                        if(obj.text!=null && obj.text!=undefined)
                                        {
                                            return "<div style='height:100%;white-space:nowrap;cursor:default; text-align:center;overflow:hidden; background-color:#919191;padding:5px 5px 5px 5px;'>"+obj.text+"</div>";
                                        }
                                        else{
                                        
                                            if(obj.status=='Empty'){return "<div title='"+obj.label+"' style='height:100%;white-space:nowrap; overflow:hidden;background-position:center; background-image:url(images/greenCircle.png);background-repeat:no-repeat;'></div>";}
                                            else{return "<div title='"+obj.label+"' style='height:100%;white-space:nowrap;cursor:default; overflow:hidden;background-position:center; background-image:url(images/redCircle.png); background-repeat:no-repeat;'></div>";}
                                        }
                                    },
                width:25,
                height:25
            },
        x_count:obj.twoDimensionCapacity+1,
        y_count:obj.oneDimensionCapacity+1
    });

    view.attachEvent("onAfterSelect", function (id){
        var obj = view.get(id);
        if(obj.status=='Empty'){
            var position = obj.label.split(",");
            eval(functionName+"('"+pos1ControlName+"','"+position[0]+"','"+pos2ControlName+"','"+position[1]+"','"+pageOf+"','"+controlName+"')");
        }
    });
      
    var collectionObj = obj.storagePositionDTOCollection;
    
    for(var i=0; i<=obj.twoDimensionCapacity;i++)
    {
        if(i==0)
        {
            view.add({
                    text:""
                });
        }
        else
        {
            view.add({
                    text: dimensionTwoLabels[i]
                });
        }
    }
    for(var i=1; i<=obj.oneDimensionCapacity;i++)
    {
        for(var j=0; j<=obj.twoDimensionCapacity;j++)
        {
            if(j==0)
            {
                view.add({
                    text: dimensionOneLabels[i]
                });
            }
            else if(collectionObj[i][j]!=null)
            {
                view.add({
                    label: dimensionOneLabels[i]+","+dimensionTwoLabels[j],
                    status: "Occupied"
                });
            }
            else
            {
                view.add({
                    label: dimensionOneLabels[i]+","+dimensionTwoLabels[j],
                    status: "Empty"
                });
            }
        }
    }
}

function showContainerGrid()
{
    if(reloadGrid)
    {
        var controlName='<%=controlName%>';
        var contName='<%=storageContainerName%>';
        var temp=parent.window.document.getElementById('storageContainerPosition');
        if(null!=temp)
        {
            controlName="storageContainerDropDown";
        }
        onContainerDetailDisplay(controlName);
    }
}
function setVirtual()
{
    if(globalPageOf == 'pageOfSpecimen')
    {
        transferVrtualSpecimen();
    }
    else
    {
        var id1 = parent.window.document.getElementById(globalPos1);    
        if(id1 != null || id1 != undefined){
            id1.value = "";
        }
        
        var id2 = parent.window.document.getElementById(globalPos2);    
        if(id2 != null || id2 != undefined){
            id2.value = "";
        }
        
        if(globalPageOf == 'pageOfAntispec')
        {
            var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
            //alert(rowIndex);
            var contId = parent.window.document.getElementById("storageContainerDropDown_"+rowIndex);
            contId.value='-1';
            var contName = parent.window.document.getElementById(globalContName);
            contName.value='Virtual';
        }
        else if(globalPageOf == 'pageOfAliquot')
        {
            var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
            var contId = parent.window.document.getElementById("value(Specimen:"+rowIndex+"_StorageContainer_id_fromMap)");
            contId.value="-1";
            var contName = parent.window.document.getElementById(globalContName);
            contName.value='Virtual';
        }
        else if(globalPageOf == 'pageOfNewAliquot'){
            var contName = dhtmlxCombo.getSelectedText();
            parent.setNewStoragePositionForAliquot(globalContName,"Virtual","","");
        }
        else if(globalPageOf == 'pageOfShipping')
        {
            var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
            var contId = parent.window.document.getElementById("specimenDetails(selectedContainerName_"+rowIndex+")");
            contId.value="-1";
            var contName = parent.window.document.getElementById(globalContName);
            contName.value='Virtual';
        }
        else if(globalPageOf == 'pageOfNewSpecimen')
        {
            var contId = parent.window.document.getElementById('containerId');
            contId.value='-1';
            var contName = parent.window.document.getElementById('storageContainerDropDown');
            contName.value='Virtual';
        }
        else if(globalPageOf == 'pageOfderivative')
        {
            var contId = parent.window.document.getElementById('containerId');
            contId.value='-1';
            var contName = parent.window.document.getElementById('containerName');
            contName.value='Virtual';
            parent.window.document.getElementById('pos1').value='';
            parent.window.document.getElementById('pos2').value='';
            parent.window.document.getElementById('storageContainerPosition').value='Virtually Located';
            parent.window.document.getElementById('storageContainerPosition').title='Virtually Located';
        }
        else if(globalPageOf == 'pageOfBulkEvent')
        {
            var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
            var contId = parent.window.document.getElementById(globalContName);
            contId.value='-1';
            var contName = parent.window.document.getElementById(globalContName);
            contName.value='Virtual';
        }
        else if(globalPageOf == 'pageOfTransfer')
        {
            
            var contId = parent.window.document.getElementById('containerId');
            contId.value='-1';
            var contName = parent.window.document.getElementById(globalContName);
            contName.value='Virtual';
        }
        parent.window.dhxWins.window("containerPositionPopUp").close();
    }
    
    
}
function setTextBoxValueInParent(elementId1,elementValue1,elementId2,elementValue2,pageOf,controlName)
{
    //alert("elementId1:  "+elementId1+"  elementValue1:  "+elementValue1+"  elementId2:   "+elementId2+"elementValue2:   "+elementValue2);
    var id1 = parent.window.document.getElementById(elementId1);    
    if(id1 != null || id1 != undefined){
        id1.value = elementValue1;
    }
    var id2 = parent.window.document.getElementById(elementId2);    
    if(id2 != null || id2 != undefined){
        id2.value = elementValue2;
    }
    if(pageOf == 'pageOfAntispec')
    {
        var rowIndex = controlName.substring(controlName.indexOf('_')+1,controlName.length);
        var contId = parent.window.document.getElementById("storageContainerDropDown_"+rowIndex);
        contId.value=dhtmlxCombo.getSelectedValue();
        var contName = parent.window.document.getElementById(controlName);
        contName.value=dhtmlxCombo.getSelectedText();
    }
    else if(pageOf == 'pageOfNewAliquot'){
        var contName = dhtmlxCombo.getSelectedText();
        parent.setNewStoragePositionForAliquot(controlName,contName,elementValue1,elementValue2);
    }
    else if(pageOf == 'pageOfAliquot')
    {
        var rowIndex = controlName.substring(controlName.indexOf('_')+1,controlName.length);
        var contId = parent.window.document.getElementById("value(Specimen:"+rowIndex+"_StorageContainer_id_fromMap)");
        contId.value=dhtmlxCombo.getSelectedValue();
        var contName = parent.window.document.getElementById(controlName);
        contName.value=dhtmlxCombo.getSelectedText();
    }
    else if(globalPageOf == 'pageOfShipping')
    {
        var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
        var contId = parent.window.document.getElementById("specimenDetails(selectedContainerName_"+rowIndex+")");
        contId.value=dhtmlxCombo.getSelectedValue();
        var contName = parent.window.document.getElementById(globalContName);
        contName.value=dhtmlxCombo.getSelectedText();
    }
    else if(pageOf == 'pageOfNewSpecimen')
    {
        var contId = parent.window.document.getElementById('containerId');
        contId.value=dhtmlxCombo.getSelectedValue();
        var contName = parent.window.document.getElementById('storageContainerDropDown');
        contName.value=dhtmlxCombo.getSelectedText();
    }
    else if(pageOf == 'pageOfderivative')
    {
        updatePosition();
        /*var contId = parent.window.document.getElementById('containerId');
        contId.value=dhtmlxCombo.getSelectedValue();
        var contName = parent.window.document.getElementById(controlName);
        contName.value=dhtmlxCombo.getSelectedText();*/
    }
    else if(globalPageOf == 'pageOfBulkEvent')
        {
        //alert(globalContName);
            var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
            var contId = parent.window.document.getElementById(globalContName);
            contId.value=dhtmlxCombo.getSelectedValue();
            var contName = parent.window.document.getElementById(globalContName);
            contName.value=dhtmlxCombo.getSelectedText();
        }
    else if(globalPageOf == 'pageOfTransfer')
        {
        //alert(globalContName);
            
            var contId = parent.window.document.getElementById('containerId');
            contId.value=dhtmlxCombo.getSelectedValue();
            var contName = parent.window.document.getElementById(globalContName);
            contName.value=dhtmlxCombo.getSelectedText();
        }
    
    parent.window.dhxWins.window("containerPositionPopUp").close();
}

function checkSpecimenStatus()
{
    var temp=parent.window.document.getElementById('storageContainerPosition');
    var fromStoragePosition;
    if(temp!=null)
    {
        document.getElementById("storageContainerNameDiv").style.display="none";
        fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;
        if(fromStoragePosition != 'Virtually Located')
        {
            var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
            var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
            var fromPos1=storagePositions[0];
            var fromPos2=storagePositions[1];

            document.getElementById("pos11").value=fromPos1;
            document.getElementById("pos22").value=fromPos2;
        }
        else
        {
            document.getElementById("pos11").value="";
			document.getElementById("pos22").value="";
			document.getElementById("pos11").style.display="none";
            document.getElementById("pos22").style.display="none";
		 }
        }
		
}

var reloadGrid=true,populateValueInCombo=true;
var dhtmlxCombo=new dhtmlXCombo("comboDiv","storageContainerDropDown",250);
var url=getActionToDoURL();
var containerName="${requestScope.containerName}";
if(containerName)
{
    dhtmlxCombo.loadXML(url);
}
else
{
    document.getElementById('virtualCont').checked=true;
}
 dhtmlxCombo.attachEvent("onSelectionChange",function(){
 dhtmlxCombo.DOMelem_input.title=dhtmlxCombo.getSelectedText();
 });
//dhtmlxCombo.enableFilteringMode(true);
dhtmlxCombo.attachEvent("onOpen", 
function(){
    reloadGrid=false;
    populateValueInCombo=false;
    dhtmlxCombo.loadXML(getActionToDoURL());
});  
dhtmlxCombo.attachEvent("onChange", 
function()
{
    reloadGrid=true;
    document.getElementById("pos1").value="";
    document.getElementById("pos2").value="";
    showContainerGrid();
});
dhtmlxCombo.attachEvent("onKeyPressed", 
function(keyCode){
    if(keyCode != 8 || keyCode != 46)
    {
        document.getElementById('error').innerHTML="";
        reloadGrid=true;
        populateValueInCombo=false;

    }
    if(keyCode != 13 && keyCode != 39 && keyCode != 37 )
    {   
                populateValueInCombo=false;
                dhtmlxCombo.loadXML(getActionToDoURL());
    }
});

function getDimensionLabel(dimLabel)
{
    var temp ="";
    var tempLabel ="";
    var label ="";
    var filler = " ";
    for( var j=0; j<dimLabel.length;j++)
    {
        temp =dimLabel.substring(j,j+1);
        if(temp==" ")
        {
            tempLabel=temp+"&nbsp;&nbsp;\n"; 
        }
        else
        {
            tempLabel=temp+filler;
        }
        label=label+tempLabel;
    } 
    return label;
}
</script>
