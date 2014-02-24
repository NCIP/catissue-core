<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
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
<html ng-app="demoApp">
    <link rel="stylesheet" type="text/css" href="ngweb/external/select2/css/select2.css"></link>
    <link rel="stylesheet" type="text/css" href="jss/app/css/tree.css"></link> 
    <link href="ngweb/external/font-awesome-4.0.3/css/font-awesome.css" rel="stylesheet"/>
    <link href="ngweb/external/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    
    <script type="text/javascript" src="ngweb/external/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="ngweb/external/angularjs/angular.min.js"></script>
    <script type="text/javascript" src="ngweb/external/select2/select2.min.js"></script>
    <script type="text/javascript" src="ngweb/external/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="ngweb/external/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="ngweb/external/angularjs/angular-ui.js"></script>
    <script type="text/javascript" src="ngweb/external/bootstrap/js/bootstrap.min.js"></script>
    
    <script type="text/javascript" src="ngweb/external/angularjs/ui-bootstrap-tpls-0.10.0.min.js"></script>
    
    
    <script type="text/javascript" src="jss/app/js/cpBasedViewController.js"></script>
    <script type="text/javascript" src="jss/app/js/select2Combo.js"></script>
    <script type="text/javascript" src="jss/app/js/select2SearchCombo.js"></script>
    

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
    .left-box {
        padding: 10px;
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
    
    .select-box div{
        margin: 5px 0;
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
<!-- Mandar : 25Nov08  -->
    <TABLE border="1" width="100%" height="100%" cellpadding="0" cellspacing="0">
        <TR>
            <TD valign="top" height="100%" id='sideMenuTd' width="25%">
                <div id="SearchLeftPart" style="overflow:auto">
                    <div ng-controller="CpController" class="left-box">
                        <div class="search-box" >                             
                            <div class="select-box" >
                                <div class="bold_ar_b">Collection Protocol: 
                                    </div>
                                <ka-select option-id="id" option-value="name" options="cps" ng-model="selectedCp.id" on-select="onCpSelect" style="width:100%;" ></ka-select>
                            </div>
                            <div class="pull-right" style="display:none">
                                <input type="button" class="btn btn-default" ng-class="{true: 'btn-enabled', false: 'btn-disabled'}[selectedCp.id != -1]" value="Register New" ng-click="registerParticipant()" ng-disabled="selectedCp.id == -1"/>
                            </div>
                            <div class="clear"></div>
                            <div class="select-box">
                                <div style="padding-top:10px">
                                <div class="bold_ar_b">Participant: 
                                    <div class="btn-group">
                                    <span class="btn btn-default" ng-click="registerParticipant()" ng-disabled="selectedCp.id == -1"><span class="glyphicon glyphicon-plus"></span> Add</span><span class="btn btn-default" ng-click="viewParticipant()"><span class="glyphicon glyphicon-eye-open"></span> View</span>
                                    </div>
                                </div>
                                <ka-search ng-model="SelectedParticipant" on-initselectionfn="initSel" on-query="participantSearch" on-select="participantSelect(selected)" style="width:100%;"  ></ka-search>
                            </div>
                            <div class="pull-right" style="display:none">
                                
                                <input type="button" class="btn btn-default" ng-class="{true: 'btn-enabled', false: 'btn-disabled'}[SelectedParticipant.id != -1]" value="View" ng-click="viewParticipant()" ng-disabled="SelectedParticipant.id == -1"/>
                            </div>
                            <div class="clear"></div>
                            </div>
                        </div>
                        <div class="clearer">  
                            <div class="bold_ar_b" style="margin: 10px 0;">Specimen Details:</div>
                            <tree-view tree-data="tree" ></tree-view>
                        </div>
                    </div>
                </div>
            </td>
            <td width="75%" valign="top" height="100%" id="contentTd">
                <div id="SearchLeftPart2" style=" width:100%;height:100%; overflow:none;">
                    <iframe id="cpFrameNew" name="<%=Constants.DATA_DETAILS_VIEW%>" src="CPDashboardAction.do?isSystemDashboard=true" scrolling="none" frameborder="0" width="100%" height="100%">
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
    var selectionCp = "";
    var selParticipant = "";
</script>

</body>
</html>