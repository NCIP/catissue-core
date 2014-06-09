<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
      String access = (String)session.getAttribute("Access");
      boolean mac = false;
      Object os = request.getHeader("user-agent");
      if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
      {
          mac = true;
      }

    String URLCollectionProtocolId  = request.getParameter("URLCollectionProtocolId");
    String URLParticipantId = request.getParameter("URLParticipantId");
    //String URLScgId = request.getParameter("URLId");
    String URLnodeId = request.getParameter("URLId");
    String URLCollectionEventId = request.getParameter("URLCollectionEventId");
    String frame1Ysize = "99.9%";
    String frame2Ysize = "99.9%";
    String frame3Ysize = "99.9%";

    String cpAndParticipantViewFrameHeight="50%";
    if(access != null && access.equals("Denied"))
    {
        cpAndParticipantViewFrameHeight="15%";
    }

    if(mac)
    {
        frame1Ysize = "180";
        frame2Ysize = "180";
        frame3Ysize = "520";


        if(access != null && access.equals("Denied"))
        {
            frame1Ysize = "80";
            frame2Ysize = "320";
        }
    }
    else
    {
        frame3Ysize = "650";
    }

%>
<html>
    <link rel="stylesheet" type="text/css" href="ngweb/external/select2/css/select2.css"></link>
    <link href="ngweb/external/select2/css/select2-bootstrap.css" rel="stylesheet" type="text/css"></link>
    <link rel="stylesheet" type="text/css" href="jss/app/css/tree.css"></link> 
    <link href="ngweb/external/font-awesome-4.1.0/css/font-awesome.css" rel="stylesheet"/>
    <link href="ngweb/external/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    
    <script type="text/javascript" src="ngweb/external/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="ngweb/external/angularjs/angular.min.js"></script>
    <script type="text/javascript" src="ngweb/external/select2/select2.min.js"></script>
   
    <script type="text/javascript" src="ngweb/external/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="ngweb/external/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="ngweb/external/angularjs/angular-ui.js"></script>
    <script type="text/javascript" src="ngweb/external/bootstrap/js/bootstrap.min.js"></script>
    
    <script type="text/javascript" src="ngweb/external/angularjs/ui-bootstrap-tpls-0.11.0.min.js"></script>
    
    <script>
      document.createElement('ka-select');
    </script>
<script type="text/javascript">
var isParticipantUpdated = true;
</script>
<style>
.badge {
  padding: 1px 9px 2px;
  font-size: 12.025px;
  font-weight: bold;
  white-space: nowrap;
  color: #999999;
  background-color: #F70404;
  -webkit-border-radius: 9px;
  -moz-border-radius: 9px;
  border-radius: 9px;
}

    .cp-left-panel {
        padding: 10px;
        margin: 2px;
    }
    
    .btn-enabled {
        cursor:pointer;
    }
        
    .btn-disabled {
        cursor:not-allowed;
    }
        
    .search-box {
        width: 100%;
    }
    
    .clear{
        clear: both;
    }
    
    a:hover{
        text-decoration: none;
        color: black;
    }
    .left-panel{
        width:30%;
        float:left;
    }
    .right-panel{
        width:70%;
        float:right;
    }
    
    .bold_ar_b {
        font-family: verdana;
        font-size: 12px;
        font-weight: bold;
    }
    
    .select2-chosen,
    .select2-results li{
        font-family: verdana;
        font-size: 9pt;
    }
    
    .dropdown {
      padding: 0px;
      margin: 5px 0px 5px -15px;
    }    

    .cp-dropdown-label {
      font-size: 14px;
      font-weight: bold;
      font-family: verdana;
      margin: 0px 0px 4px 0px;
    }
    
    .img-circle {
      margin: 5px 4px 3px -2px;
      border: solid;
      border-radius: 50%;
      height: 8px;
      width: 8px;
    }
    
    .complete, .collected{
      color: green;
    }
    
     .pending {
      color: grey;
    }
    
    .not-collected {
      color: red;
    }
    
    .distributed {
      color: blue;
    }
   
    hr {
      margin-top: 10px;
      margin-bottom: 10px;
    }    
    
    .black_ar {
      text-overflow: ellipsis;
    }   
    
    .tooltip-inner {
      background-color: #ffffcc;
      border: 1px solid #f0c36d;
      color: #000000;
    }

    .tooltip-arrow:after {
      border: 1px solid #f0c36d;
    }

    .tooltip.top .tooltip-arrow {
      border-top-color: #ffffcc;
    }

    .tooltip.bottom .tooltip-arrow {
      border-bottom-color: #ffffcc;
    }

    .tooltip.left .tooltip-arrow {
      border-left-color: #ffffcc;
    }

    .tooltip.right .tooltip-arrow {
      border-right-color: #ffffcc;
    }
    </style>
<script>

    <!-- Download Scheduled Report -->
    var pageOf="${param.pageOf}";
    var file = "${param.file}";
    var  downloadUrl= "";
    if(pageOf!=null && pageOf!='null'  && pageOf == "pageOfDownload")
    {
     
    if(file==null || file== "null") 
    {
        file = '<%=request.getAttribute("file")%>'; 
    }
    downloadUrl="DownloadAction.do?file="+file;
    }

     if(downloadUrl!="")
     {
       dhtmlxAjax.get(downloadUrl+"&message=true",downloadHandler);
     }

     function downloadHandler(loader)
     {
           var message =loader.xmlDoc.responseText;
           if(message=="You are not authorized to download this file."|| message=="The file has already been deleted.")
          {
                            alert(message);
                    }
                    else
                    {
                          window.frames["downloadframe"].document.location.href = downloadUrl ;
                    }
           }

     
    <!-- End Of Report Download -->

    //Set the alope for the IFrame
    if ( document.getElementById && !(document.all) )
    {
        var slope=10;
    }
    else
    {
        var slope=-10;
    }

    var ua = navigator.userAgent;
    if(navigator.userAgent.indexOf('Mac')<0)
    {
    window.onload = function() { adjFrmHt('cpFrameNew', .9,slope);adjFrmHt('<%=Constants.CP_AND_PARTICIPANT_VIEW%>', .9,slope); }
    window.onresize = function() { adjFrmHt('cpFrameNew', .9,slope); adjFrmHt('<%=Constants.CP_AND_PARTICIPANT_VIEW%>', .9,slope); }
    }
    else
    {
        window.onload = function() {macRelated(); }
        window.onresize = function() { macRelated();}
    }

</script>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<body>
<script>
function changeMenuStyle(obj, new_style) {
if (objclick != obj)
  obj.className = new_style;
}

function showCursor(){
    document.body.style.cursor='hand';
}

function hideCursor(){
    document.body.style.cursor='default';
}
   
</script>
<table border="1" width="100%" height="100%" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" height="100%" id='sideMenuTd' width="25%" style="max-width:1em;">
      <div id="SearchLeftPart" style="overflow:auto">
        <div id="CpViewController" ng-controller="CpViewController" class="cp-left-panel">
          <div>
            <div>
              <div class="cp-dropdown-label">
                <span>Collection Protocol</span>
                <span class="watch-tutorial pull-right" style="margin-right: 10px;">
                  <a target="_blank" href="https://catissueplus.atlassian.net/wiki/display/CAT/Data+Entry+Workflow+Tutorial">
                    Watch Tutorial
                  </a>
                </span> 
              </div>
              <ka-select  data-placeholder="Select Collection Protocol" 
                          option-id="id" option-value="shortTitle" 
                          options="cps" selected="selectedCp.id"  on-select="onCpSelect" style="width:100%;" >
              </ka-select>
            </div>
            <div style="margin-top: 20px">
              <div class="cp-dropdown-label"> Participant </div>
              <ka-select data-placeholder="Select Participant" 
                         option-id="id" option-value="name" 
                         options="participantList"  selected="selectedParticipant.id" on-select="onParticipantSelect" style="width:100%;">
              </ka-select>
            </div>
            <div class="btn-group" style="margin-top: 10px; width: 100%;">
              <button class="btn btn-default btn-sm" ng-click="registerParticipant()" ng-disabled="selectedCp.id == -1" style="width: 50%">
                <span class="glyphicon glyphicon-plus"></span> Register Participant
              </button>
              <button class="btn btn-default btn-sm" ng-click="viewParticipant()" style="width: 50%">
                <span class="glyphicon glyphicon-eye-open"></span> View Participant
              </button>
            </div>
          </div>
          <div ng-if="tree.length>0">  
            <hr>
            <div class="cp-dropdown-label" style="margin: 7px 0;"> Specimen Tree </div>
            <tree-view tree-data="tree" ></tree-view>
          </div>
        </div>
      </div>
    </td>
  
    <td width="75%" valign="top" height="100%" id="contentTd">
      <div id="SearchLeftPart2" style=" width:100%;height:100%; overflow:none;">
        <iframe id="cpFrameNew" name="<%=Constants.DATA_DETAILS_VIEW%>" src="CPDashboardAction.do?isSystemDashboard=true" 
        scrolling="none" frameborder="0" width="100%" height="100%">
          Your Browser doesn't support IFrames.
        </iframe>
      </div>
      <iframe name="downloadframe" src="" style="display:none"></iframe>  
    </TD>
  </TR>
</TABLE>
    
<script>
    SearchResult=document.getElementById('SearchLeftPart');
    SearchResult.style.height = (document.body.clientHeight - 60) + 'px';
    SearchResult2=document.getElementById('SearchLeftPart2');
    SearchResult2.style.height = (document.body.clientHeight - 60) + 'px';
    var control=null;
    
    var cpId = "<%= request.getParameter("cpId") %>";
    var participantId = "<%= request.getParameter("participantId") %>";
    var scgId = "<%= request.getParameter("scgId") %>";
    var specimenId = "<%= request.getParameter("specimenId") %>";
    
    var selectionCp = {id: cpId, shortTitle:""};

    var selParticipant = {id: participantId != "null" ? participantId : -1, name: ""}; 

    var selectedScg = undefined;
    if (scgId == "null") {
      scgId = "${scgId}";
    }
    if (scgId != "null" && scgId != "") {
      selectedScg = {id: scgId, type: 'scg'};
    }

    var selectedSpecimen = undefined;
    if (specimenId != "null") {
      selectedSpecimen = {id: specimenId, type: 'specimen'};
    }
    
    function handleCpView(participant, scgId, specimenId) {
      var scope = angular.element($("#CpViewController")).scope();

      selParticipant = participant != null ? participant : scope.selectedParticipant;
      selectedScg = scgId != null ? {id: scgId, type: 'scg'} : undefined;
      selectedSpecimen = specimenId != null ? {id: specimenId, type: 'specimen'} : undefined;
      
      scope.onCpSelect({id: scope.selectedCp.id, text: scope.selectedCp.shortTitle}, false);
      var scgTreeQ = scope.onParticipantSelect(selParticipant, selectedScg == undefined ? true : selectedScg);
      scgTreeQ.then(function() { scope.handleDirectObjectLoad(false); });
    }
</script>

<script type="text/javascript" src="ngweb/js/wrapper.js"></script>
<script type="text/javascript" src="ngweb/js/directives.js"></script>
<script type="text/javascript" src="ngweb/js/cpview-service.js"></script>
<script type="text/javascript" src="ngweb/js/cpview-controller.js"></script>
<script type="text/javascript" src="ngweb/js/cpview-app.js"></script>
    
</body>
</html>
