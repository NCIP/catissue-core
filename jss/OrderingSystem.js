var request,url;var pageDisplayed=1;function gotoCreateArrayPage(arrayRowCounter,rowCounter){var id="defineArrayName_"+arrayRowCounter;var arrayName=document.getElementById(id).value;var queryString="?array="+arrayName+"&operation=add&id=&arrayRowCounter="+arrayRowCounter+"&rowCounter="+rowCounter;var action="InitCreateDefineArray.do"+queryString;document.forms[0].action=action;document.forms[0].submit()}
function submitPage()
{
setGridValuesToForm();

var action = document.forms[0].action;
action = action+"&arrTabId="+arrTabId;
document.forms[0].action=action;
document.forms[0].submit()
}

function submitAdvPage()
{

var action = document.forms[0].action;
action = action+"&arrTabId="+arrTabId;
document.forms[0].action=action;
document.forms[0].submit()
}

function updateAllStatus(){
var selectNext=document.getElementById("nextStatusId");
if(selectNext.value == ("Distributed And Close(Special)"))
selectedNextValue="Distributed And Close";
else
selectedNextValue=selectNext.value;


if(selectNext.selectedIndex!=0){
var queryString="updateValue="+selectedNextValue;
url="UpdateStatus.do?"+queryString;
sendRequestForUpdateStatus()}}

function sendRequestForUpdateStatus(){request=newXMLHTTPReq();if(request){if(pageDisplayed==1){request.onreadystatechange=handleResponse}else{request.onreadystatechange=handleResponseForArray}try{request.open("GET",url,true);request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");request.send("")}catch(e){}}}

function handleResponseForArray(){if(request.readyState==4){if(request.status==200){responseStringArray=parseResponseText(",");var selectId="";if(document.getElementById("definedArrayRows")!=null){var num_rows_definedArray=document.getElementById("definedArrayRows").value}if(document.getElementById("numOfExistingArrays")!=null){var num_rows_existingArray=document.getElementById("numOfExistingArrays").value}definedArrayResponseStringArray=responseStringArray[0].split(";");setNewStatusValuesInOptionsArray("array_",num_rows_definedArray,definedArrayResponseStringArray);if(responseStringArray.length==2){existingArrayResponseStringArray=responseStringArray[1].split(";");setNewStatusValuesInOptionsArray("existingArray_",num_rows_existingArray,existingArrayResponseStringArray)}}}}
function handleResponse(){if(request.readyState==4){if(request.status==200){responseStringArray=parseResponseText(";");var tbodyId=document.getElementById("tbody");var num_rows=tbodyId.rows.length;var selectId="";setNewStatusValuesInOptionsArray("select_",num_rows,responseStringArray)}}}

var id;function handleResponseForQty(){if(request.readyState==4){if(request.status==200){var responseString=request.responseText;var divId="avaiQty"+id;document.getElementById(divId).innerHTML=responseString}}}function updateQuantity(requestForId){var sendRequestFor="updateQty";id=requestForId.substring(10);var selectNext=document.getElementById(requestForId);selectedNextValue=selectNext.value;var queryString="selectedSpecimen="+selectedNextValue+"&finalSpecimenListId="+id;url="UpdateQuantity.do?"+queryString;sendRequestForQuantity()}function sendRequestForQuantity(){request=newXMLHTTPReq();if(request){request.onreadystatechange=handleResponseForQty;try{request.open("GET",url,true);request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");request.send("")}catch(e){}}}function expandArrayBlock(){switchObj=document.getElementById("switchExistingArray");tbodyObj=document.getElementById("existingArrayId");switchBlock(switchObj,tbodyObj)}function expandOrderItemsInArray(rowid,arrayId){var switchObj=document.getElementById("switchdefineArray"+rowid+"_array"+arrayId);var dataObj=document.getElementById("dataDefinedArray"+rowid+"_array"+arrayId);switchBlock(switchObj,dataObj)}function switchOrderList(rowid){switchObj=document.getElementById("switch"+rowid);dataObj=document.getElementById("data"+rowid);switchBlock(switchObj,dataObj)}function switchDefinedArrayBlock(rowid){switchObj=document.getElementById("switchArray"+rowid);dataObj=document.getElementById("dataArray"+rowid);arrayHeaderObj=document.getElementById("headerArray"+rowid);btnCreateArrayObj=document.getElementById("btnCreateArray"+rowid);if(dataObj.style.display=="none"||dataObj.style.display==" "){if(navigator.appName=="Microsoft Internet Explorer"){dataObj.style.display="block";arrayHeaderObj.style.display="block";btnCreateArrayObj.style.display="block"}else{dataObj.style.display="table-row";arrayHeaderObj.style.display="table-row";btnCreateArrayObj.style.display="table-row"}switchObj.innerHTML="<img src=images/uIEnhancementImages/minimize.png alt=Collapse height=16 width=16 border=0 title='Hide Array Detail' />"}else{dataObj.style.display="none";arrayHeaderObj.style.display="none";btnCreateArrayObj.style.display="none";switchObj.innerHTML="<img src=images/uIEnhancementImages/maximize.png alt=Expand height=16 width=16 border=0 title='Show Array Detail'/>"}}

function gotoArrayRequestTab(){document.getElementById("tabIndexId").value=2;pageDisplayed=2;document.getElementById("specimenDataTab").style.display="none";document.getElementById("arrayDataTable").style.display="block";document.getElementById("gridView").style.display="none";var selectedTab=document.getElementById("arrayRequestTab");var unSelectedTab=document.getElementById("specimenRequestTab");selectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_arrayR2_user.png alt=Array Requests width=101 height=20 border=0 />";unSelectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_specimentR2_user1.png alt=Specimen Requests  width=124 height=20 border=0 />";var unSelectedTab1=document.getElementById("gridRequestTab");unSelectedTab1.innerHTML="<img src=images/uIEnhancementImages/tab_speciment_GridR2_user1.png alt=Specimen Requests  width=124 height=20 border=0 />";}

function gotoSpecimenRequestTab(){document.getElementById("tabIndexId").value=1;pageDisplayed=1;document.getElementById("arrayDataTable").style.display="none";document.getElementById("specimenDataTab").style.display="block";document.getElementById("gridView").style.display="none";var selectedTab=document.getElementById("specimenRequestTab");var unSelectedTab=document.getElementById("arrayRequestTab");selectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_specimentR2_user.png alt=Specimen Requests  width=124 height=20 border=0 />";unSelectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_arrayR2_user1.png alt=Array Requests width=101 height=20 border=0 />";var selectedTab=document.getElementById("specimenRequestTab");var unSelectedTab1=document.getElementById("gridRequestTab");unSelectedTab1.innerHTML="<img src=images/uIEnhancementImages/tab_speciment_GridR2_user1.png alt=Specimen Requests  width=124 height=20 border=0 />";}function expandTableForIE(){document.getElementById("table4_pageFooter").style.display="block";document.getElementById("table2_TabPage").style.display="block";document.getElementById("table1_OrderRequestHeader").style.display="block"}function expandTableForOtherBrowsers(){document.getElementById("table4_pageFooter").style.display="table-row";document.getElementById("table2_TabPage").style.display="table-row";document.getElementById("table1_OrderRequestHeader").style.display="table-row"}function changeCssForTab(strSelectedTab,strUnSelectedTab){var selectedTab=document.getElementById(strSelectedTab);var unSelectedTab=document.getElementById(strUnSelectedTab);selectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_specimentR2_user1.gif alt=Specimen Requests  width=124 height=20 border=0 />";unSelectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_arrayR2_user.gif alt=Array Requests width=101 height=20 border=0 />";selectedTab.onmouseover=null;selectedTab.onmouseout=null;selectedTab.className="tabMenuItemSelected";unSelectedTab.onmouseover=function(){changeMenuStyle(this,"tabMenuItemOver"),showCursor()};unSelectedTab.onmouseout=function(){changeMenuStyle(this,"tabMenuItem"),hideCursor()};unSelectedTab.className="tabMenuItem"}function parseResponseText(delimiter){var responseString=request.responseText;var responseStringArray=new Array();responseStringArray=responseString.split(delimiter);return responseStringArray}
function setNewStatusValuesInOptionsArray(strElement,rowCount,responseStringArray)
{
for(var counter=0;counter<rowCount;counter++)
{
strSelectElementId=strElement+counter;
selectElementId=document.getElementById(strSelectElementId);
//alert(selectElementId);
//alert(selectElementId.value);
if(selectElementId!=null){
optionStringArray=responseStringArray[counter].split("||");
for(var x=0;x<optionStringArray.length-1;x++){optionStrings=optionStringArray[x].split("|");
selectElementId.options[x]=new Option(optionStrings[0],optionStrings[1])}}}}

function switchBlock(switchObj,dataObj){if(dataObj.style.display!="none"){dataObj.style.display="none";switchObj.innerHTML='<img src="images/nolines_plus.gif" border="0"/>'}else{if(navigator.appName=="Microsoft Internet Explorer"){dataObj.style.display="block"}else{dataObj.style.display="table-row"}switchObj.innerHTML='<img src="images/nolines_minus.gif" border="0"/>'}};





function gotoGridViewTab()
{document.getElementById("tabIndexId").value=1;pageDisplayed=1;document.getElementById("arrayDataTable").style.display="none";document.getElementById("specimenDataTab").style.display="none";document.getElementById("gridView").style.display="block";var selectedTab=document.getElementById("specimenRequestTab");var unSelectedTab=document.getElementById("arrayRequestTab");selectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_specimentR2_user1.png alt=Specimen Requests  width=124 height=20 border=0 />";unSelectedTab.innerHTML="<img src=images/uIEnhancementImages/tab_arrayR2_user1.png alt=Array Requests width=101 height=20 border=0 />";var selectedTab=document.getElementById("specimenRequestTab");var unSelectedTab1=document.getElementById("gridRequestTab");unSelectedTab1.innerHTML="<img src=images/uIEnhancementImages/tab_speciment_GridR2_user.png alt=Grid View  width=124 height=20 border=0 />";}




