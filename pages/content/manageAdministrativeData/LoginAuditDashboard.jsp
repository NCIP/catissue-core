  
<%-- TagLibs --%>  
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%-- Imports --%>
 <head>
<!-- dhtmlx Grid/tree Grid -->
<link rel="stylesheet" type="text/css"
    href="css/advQuery/styleSheet.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css">
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<!--link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxgrid_dhx_skyblue.css" /-->
<script src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<link rel="STYLESHEET" type="text/css"
    href=" dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css"/>
<script src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="jss/advQuery/json2.js"></script>
<link rel="stylesheet" type="text/css" href="css/advQuery/tag-popup.css" />
<style>
    .dhx_toolbar_base_dhx_skyblue { 
        background-image: url("dhtmlx_suite/imgs/dhxtoolbar_dhx_skyblue/sky_blue_grid1.gif");
        background-repeat: repeat-x;
        border-radius: 0.3em 0.3em 0.3em 0.3em; 
    }
    
    div.gridbox_dhx_skyblue table.obj.row20px tr.rowselected td {
        background-image: url("dhtmlx_suite/imgs/sky_blue_sel_1.png");
    }
    div.dhx_toolbar_poly_dhx_skyblue div.btn_item span {
        margin-left: 5px;
    }
</style>
<script>
    var recordPerPage = '100';
    var gridDataJson = <%=(String)request.getAttribute("gridDataJson")%>;
</script>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
  <tr valign="center" class="bgImage">
    <td width="100%"> &nbsp; <img src="images/advQuery/ic_saved_queries.gif" id="QueryAuditMenu"
                    alt="Query Audit Page" width="38" height="48" hspace="5"
                    align="absmiddle" /> <span class="savedQueryHeading">
        Login Audit Log</span>
    </td>
    <td width="1" valign="middle" class="bgImage" align="right" style="font-size: 0.9em; font-family: verdana;">
        
    </td>
  </tr> 
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"> 
  <tr>
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0"> 
        <tr>
            <td>
                <div id='messageDiv' class="alertbox" width="95%" height="10px" style="display:none;" ></div>
                <div id='auditGridbox' height="31em" width="99%" style='background-color:#d9d7d7; margin:5px;'></div>
                <div id="pagingArea" width="99%" style="border: 1px solid #A4BED4; margin:5px;"></div>
            </td>
        </tr>   
    </table>
   </table>
</body>
<script>
 var pageNum = 1;
var auditGridBox = null;
var params = null;
var filterVal = null;
var startIndex = 0;
window.onload = function() {   
    auditGridBox = new dhtmlXGridObject('auditGridbox');
    auditGridBox.setImagePath("dhtmlx_suite/imgs/");
    auditGridBox.setHeader("<b>User Name,<b>IP Address,<b>TimeStamp,<b>Login Attempt");
    
    auditGridBox.attachHeader("#text_filter,#text_filter,#rspan,#select_filter"); 
    auditGridBox.setInitWidthsP("35,25,23,17");
    auditGridBox.setColAlign("center, left, center, left");
    auditGridBox.setColSorting("str,str,str,str");
    auditGridBox.setSkin("dhx_skyblue"); // (xp, mt, gray, light, clear, modern)
    auditGridBox.setEditable(false);
    auditGridBox.enableTooltips("false,true,true,false");
    auditGridBox.clearAll(true);
    //auditGridBox.attachEvent("onBeforePageChanged", onBeforePageChanged);
    auditGridBox.attachEvent("onFilterStart", onFilter);    
    auditGridBox.init();
    auditGridBox.enablePaging(true,200,15,"pagingArea",true);
    auditGridBox.setPagingSkin("bricks");   
    auditGridBox.parse(gridDataJson, "json");

    if(mygrid.getRowsNum() == 10000){
        document.getElementById("messageDiv").style.display = "block";
        document.getElementById("messageDiv").style.color = "blue";
        document.getElementById("messageDiv").textContent = "Showing the recent 10000 logs. Use filter for more log info.";
        document.getElementById("messageDiv").innerText = "Showing the recent 10000 logs. Use filter for more log info.";
    }
}  

var xmlHttpobj = null;
function initAjaxPostCall(params){
    var url = "LoginAuditDashboard.do"; 
    if (window.XMLHttpRequest){
        xmlHttpobj=new XMLHttpRequest();
    }else {
        xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlHttpobj.onreadystatechange = populateGrid;
    xmlHttpobj.open("POST", url ,false);
    xmlHttpobj.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded");
    xmlHttpobj.send(params);
}

function populateGrid(){
    if (xmlHttpobj.readyState == 4) 
    {
         var jsonObj = eval('('+ xmlHttpobj.responseText +')');
         auditGridBox.parse(jsonObj, "json");
    }
}

function showGeneratedSQL(auditId){
    var url = "LoginAuditDashboard.do?operation=showSQL&auditId="+auditId;
    window.open(url);
}

var addElement = false; 
function addRecordPerPageOption() {     
    toolbar = auditGridBox.aToolBar;
    toolbar.setWidth('perpagenum', 130);
    
    var  opt = [10, 50, 100 , 200, 300, 500];
    
    for(i = 5; i < 35; i += 5) {
        toolbar.removeListOption('perpagenum', 'perpagenum_'+i);
    }

    for(i = 0; i < opt.length; i++) {
        toolbar.addListOption('perpagenum', 'perpagenum_'+ opt[i], NaN, "button", opt[i]+" "+ auditGridBox.i18n.paging.perpage);
    }
     
    toolbar.setListOptionSelected('perpagenum', 'perpagenum_' + recordPerPage);
    setPageDivStyle();
}

function setPageDivStyle() {
    if (document.getElementsByClassName) {      
        pageDiv = document.getElementsByClassName('dhx_toolbar_poly_dhx_skyblue')[0]; 
        pageDiv.style.maxHeight = "210px"
        pageDiv.style.overflowY = "auto"
    } else {        
        var elements = document.getElementsByTagName('div');
        for(i = 0; i < elements.length; i++) {
            if(elements[i].className == 'dhx_toolbar_poly_dhx_skyblue') {
                pageDiv = elements[i];              
                if(pageDiv.childNodes.length < 10) {
                    pageDiv.style.height = 21 * pageDiv.childNodes.length + "px"                    
                } else {
                    pageDiv.style.height = 21 * 10 + "px"
                    pageDiv.style.width = "96px";
                    pageDiv.style.overflowY = "auto"
                }               
                break;
            }
        }
    }
} 

function getJsonForFilter() {
    var columns = []
    var values = []
    var j = 0;
    for(i = 0; i < 4; i++) {
        if(auditGridBox.getFilterElement(i)){
            var value = auditGridBox.getFilterElement(i).value;         
            if(value != "") {
                columns[j] = auditGridBox.getColumnLabel(i, 0);
                values[j++] = value;                
            }
        }
    }
    //var sortColumn = sortIndex ? auditGridBox.getColumnLabel(sortIndex, 0): "";
    //sortDirection = sortDirection == "asc" ? sortDirection: "desc";
    
    return JSON.stringify({"columns": columns, "values": values/*, "sortColumn": sortColumn ,"sortDir": sortDirection*/});
}

function onFilter(indexes,values){
    auditGridBox.clearAll();
    //var option = auditGridBox.aToolBar.getListOptionSelected('perpagenum').split("_");
    //pageNum = 1;
    //recordPerPage = option[1];  
    //startIndex = (pageNum - 1) * recordPerPage;
    params = "&operation=gridData&filterValues="+getJsonForFilter()+"&startIndex=0&recordPerPage=10000"
    initAjaxPostCall(params);
    document.getElementById("messageDiv").style.display = "none";
    return true;
}

function onBeforePageChanged(ind, count){
    var option = auditGridBox.aToolBar.getListOptionSelected('perpagenum').split("_");
    auditGridBox.clearAll();
    pageNum = count;
    recordPerPage = option[1];  
    startIndex = (pageNum - 1) * recordPerPage  
    return true;
}

</script>