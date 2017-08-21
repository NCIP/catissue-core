<!DOCTYPE html>

<html ng-app="plus">
  <head>
    <link href="../external/select2/css/select2.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/select2/css/select2-bootstrap.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/eternicode/css/datepicker.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/jquery/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/angularjs/css/ng-grid.min.css" rel="stylesheet" type="text/css"></link>
    <link href="../external/font-awesome-4.1.0/css/font-awesome.css" rel="stylesheet" type="text/css"></link>

    <script src="../external/jquery/jquery.min.js" type="text/javascript"></script>
    <script src="../external/jquery/jquery-ui.min.js" type="text/javascript"></script>
    <script src="../external/jquery/jquery.ui.widget.js" type="text/javascript"></script>
    <script src="../external/jquery/jquery.iframe-transport.js" type="text/javascript"></script>
    <script src="../external/jquery/jquery.fileupload.js" type="text/javascript"></script>
    <script src="../external/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="../external/select2/select2.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/angular.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/angular-resource.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/angular-sanitize.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/ng-grid-2.0.11.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/sortable.js" type="text/javascript"></script>
    <script src="../external/angularjs/ui-bootstrap-tpls-0.11.0.min.js" type="text/javascript"></script>
    <script src="../external/angularjs/checklist-model.js" type="text/javascript"></script>
    <script src="../external/angularjs/autocomplete.js"></script>
    <script src="../external/eternicode/js/bootstrap-datepicker.js" type="text/javascript"></script>

    <link href="../css/app.css" rel="stylesheet" type="text/css"></link>
    <link href="../css/pivottable.css" rel="stylesheet" type="text/css"></link>

    <style type="text/css">
       .plus-panel {
          background-color: #FAFAFA;
          border-color: #E5E5E5 #EEEEEE #EEEEEE;
          border-style: solid;
          border-width: 1px 0;
          box-shadow: 0 3px 6px rgba(0, 0, 0, 0.05) inset;
          margin: 0 -15px 15px;
          padding-top: 5px;
          padding-left: 15px;
          padding-right: 15px;
          padding-bottom: 5px;
          position: relative;
       }

       .plus-panel-title {
          color: #428BCA;/*#563D7C;/*#3B73AF;/*#dd4b39; /*#666666;/*#00AEEF;*/
          font-size: 16px; /*12px;*/
          font-weight: bold;/*700;*/
          left: 15px;
          /*letter-spacing: 1px;
          text-transform: uppercase;*/
          padding-bottom: 16px;
       }

       .plus-panel-title-addendum {
          font-size: 16px; /*12px;*/
          left: 15px;
          /*letter-spacing: 1px;*/
          padding-bottom: 16px;
       }

       @media (min-width: 768px) {
          .plus-panel {
            background-color: #FFFFFF;
            border-color: #DDDDDD;
            border-radius: 4px 4px 0 0;
            border-width: 1px;
            box-shadow: none;
            margin-left: 0;
            margin-right: 0;
          }
        }

       .plus-panel .container {
          width: auto;
        }

       .sortablePlaceholder {
          float: left;
          width: 2.5em;
          height: 2.5em;
       }

       .focus {
          border-color: #66AFE9;
          box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);
          outline: 0 none;
       }

       .op-btn {
          background-color: #f5f5f5;
          margin-bottom: 2%;
       }  

       .droppable {
          background-color: #ffffff;
          border: 1px dashed #cccccc;
       }

       .dropped {
          background-color: #ffffff;
          border: 1px solid #cccccc;
       }

       .tool-box-panel {
          height: 17%;
       }

       .z-up {
          z-index: 100;
       }

       .filter-item {
          padding: 10px 15px;
          display: block;
          border: 1px solid #DDDDDD;
          border-radius: 4px;
          margin-bottom: 1%;
       }

       .filter-item-error {
          border: 1px solid red;
       }

       .filter-item-valign {
          height: 2.5em;
          list-style-type: none;
          margin-right: 0.7em;
          padding-top: 0.5em;
          vertical-align: middle;
       }

       .filter-node {
          background-color: #cccccc; 
          width:2.5em; 
          border-radius: 50%; 
          border: 1px solid #cccccc;
          text-align: center;
       }

       .op-node {
          background-color: #f5f5f5; 
          width:2.5em; 
          border: 1px solid #cccccc;
          text-align: center;
          cursor: pointer;
       }

       .clickable-node {
          cursor: pointer;
       }

       .paren-node {
          background-color: #ffffff;
          width:1.5em;
          border: 1px solid #cccccc;
          text-align: center;
       }

       .paren-node span.glyphicon-remove, .op-node span.glyphicon-remove {
          font-size: 0.5em;
          color: #cccccc;
       }

       .data-grid {
          border: 1px solid #ddd;
          width: 100%;
       }

       .data-grid-header {
          width: 100%;
          margin: 0;
          padding: 3px 0;
          text-align:center;
          background: #428bca;
          color:white;
       }

       .tooltip-inner, .loading-notif {
          background-color: #ffffcc;
          border: 1px solid #f0c36d;
          color: #000000;
       }

       .loading-notif { 
          padding: 2px 5px;
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

       .popover {
          max-width: 350px;
       }

       .nav-pills > li {
          background-color: #f5f5f5;
          border-radius: 4px;
       }

       .ngRow.odd {
          background-color: #ffffff;
       }

       .ngRow.even {
          background-color: #f9f9f9;
       }

       .ngRow.selected {
          background-color: #BFDCF3;
       }

       .ngTopPanel {
          background-color: #f1f2f3; 
          color: #000000;
       }

       .ngFooterPanel {
          background-color: #f1f2f3; 
          color: #000000;
       }

       .ngSortButtonDown, .ngSortButtonUp {
          border-color: #ffffff rgba(0, 0, 0, 0);
       }

       html, body {
          height: 100%;
       }

       .header h3 {
          margin-bottom: 20px;
       }
 
       .header .btn {
          margin-top: 20px;
       }

       .container {
          height: 96%;
       }

       .content-left, .content-right {
          height: 100%;
       }

       .filter-box {
          height: 85%; 
          margin-top: 5px;
          overflow:auto;
       }

       .folders .item {
          padding: 2px 8px;
        } 

       .folders .item div:first-child {
          overflow: hidden;
          text-overflow: ellipsis;
          width: 85px;
          white-space: nowrap;
       }

       .folders .item:hover {
          background-color: #eee;
          cursor: pointer;
       }

       .list tr:hover td:first-child {
          background: url(../images/grippy_large.png) no-repeat 1px 50%;
       }

       .list tr th:first-child, .list tr td:first-child {
          padding-left: 12px;  
       }
 
       .query-select-cb {
          margin-right: 10px;
          margin-left: 5px;
       }

       .folders .item .btn {
          visibility: hidden;
          padding: 0px 4px;
       }

       .folders .item:hover .btn {
          visibility: visible;
       }

       .selected {
          background-color: #ffc; 
       }

       .watch-tutorial {
          margin-top: 27px;
       }

       .table.borderless > tbody > tr > td, .table.borderless > thead > tr > th {
          border: none;
       }

       .selected-folder {
          color: #DD4B39;
          border-left: 3px solid #DD4B39;
       }

       .dropdown-menu-subgroup {
          list-style: none;
          padding: 0px;
       }

       .dropdown-menu-subgroup > li > a {
          clear: both;
          color: #333333;
          display: block;
          font-weight: normal;
          line-height: 1.42857;
          padding: 3px 20px;
          white-space: nowrap;
       }

       .dropdown-menu-subgroup > li > a:hover,  .dropdown-menu-subgroup > li > a:focus {
          background: #f5f5f5;
          color: #262626;
          text-decoration: none;
       }

       .ui-autocomplete {
          max-height: 100px;
          overflow-y: auto;
          overflow-x: hidden;
       }

       .ui-front {
          z-index: 10000;
       }
 
       input[type='file'] {
         color: transparent;
       }

       .plus-heading {
         display: block;
         width: 100%;
         font-weight: bold;
       }

       .plus-note {
         display:block;
         font-style: italic;
       }

       .plus-margin-bottom {
         margin-bottom: 5px;
       }

       .parameterized-filters-dialog > div.modal-dialog {
         width: 1000px;
       }

       .ellipsis {
         overflow: hidden;
         text-overflow: ellipsis;
         white-space: nowrap;
       }

       .query-title {
         padding: 5px; 
         border-bottom: 1px solid #ddd; 
         border-radius: 4px
       }

       .pq {
         position: absolute;
         right: 0px;
         top: 0px;
         border-radius: 50%;
         background: #428bca;
         height: 12px;
         width: 12px;
         font-size: 10px;
         color: white;
       } 

       .define-view-dialog > div.modal-dialog {
         width: 700px;
       }
    </style>
  </head>

  <body ng-controller="QueryController">
    <div id="notifications" class="cp-notifs hidden"></div>
    <div class="cp-notifs" ng-if="queryData.notifs.loading">
      <span class="loading-notif"> Loading ...  </span>
    </div>

    <div class="container" ng-if="queryData.view == 'dashboard'">
      <div class="row">
        <div class="col-xs-7">
          <h3 style="float:left; margin-bottom: 20px;">Queries Dashboard</h3>
          <div class="watch-tutorial">
            <a target="_blank" href="https://catissueplus.atlassian.net/wiki/x/O4BLAQ">Watch Tutorial</a>
          </div>
        </div>
      </div>
      <div class="row" style="margin-bottom: 10px;">
        <div class="col-xs-2">
          <div class="btn-group" dropdown>
            <button class="btn btn-primary" ng-click="createQuery()">New Query</button>
            <button class="btn btn-primary dropdown-toggle" ng-disabled="queryData.cpList.length == 0">
              <span class="caret"></span>
              <span class="sr-only"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
              <li><a href="#" ng-click="createQuery()">Create</a></li>
              <li><a href="#" ng-click="importQuery()">Import</a></li>
            </ul>
          </div>
        </div>
        <div class="col-xs-3" style="padding-top: 8px; font-size: 14px;">
          <div ng-if="queryData.queries.length > 0">
            Queries ({{queryData.startAt + 1}} - {{queryData.startAt + queryData.queries.length}} of {{queryData.totalQueries}})
          </div>
        </div>
        <div class="col-xs-4">
          <div ng-class="{'btn-group': queryData.selectedFolderId != -1}"  ng-if="queryData.selectedQueries.length > 0">
            <button ng-if="queryData.selectedFolderId != -1" class="btn btn-default" ng-click="removeQueriesFromFolder()"
              tooltip-placement="bottom" tooltip="Remove queries from folder" tooltip-append-to-body="true">
              <span class="glyphicon glyphicon-trash"></span>
            </button>
            <div class="btn-group">
              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" ng-click="searchQueryFolder = ''"
                tooltip-placement="bottom" tooltip="Assign queries to folder" tooltip-append-to-body="true">
                <span class="glyphicon glyphicon-folder-close"></span>&nbsp;
                <span class="caret"></span>
              </button>
              <ul class="dropdown-menu" style="width: 300px;">
                <li>
                  <input ng-model="searchQueryFolder" 
                    type="text" class="form-control" 
                    style="width: 260px; margin: 3px 20px" 
                    ng-click="$event.stopPropagation()">
                </li>
                <li>
                  <ul class="dropdown-menu-subgroup" style="max-height: 150px; overflow: auto;">
                    <li ng-repeat="folder in queryData.myFolders | filter: searchQueryFolder" ng-click="addQueriesToFolder(folder)">
                      <a href="#">{{ folder.name }}</a>
                    </li>
                  </ul>
                </li>
                <li class="divider"></li>
                <li><a ng-click="createFolder()">Create New Folder</a></li>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-xs-3">
          <div class="plus-addon plus-addon-input-right">
            <span class="glyphicon glyphicon-search"></span>
            <input type="text" class="form-control" placeholder="Search Query" 
              ng-model="queryData.tempSearchString" ng-model-options="{debounce: 1000}" ng-change="searchQueries()">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-2 folders">
          <table class="table" style="margin-bottom: 0px;">
            <thead>
              <tr><th>Folders</th></tr>
            </thead>
            <tbody>
              <tr ng-class="{'selected-folder': queryData.selectedFolderId == -1}">
                <td class="item clearfix">
                  <div ng-click="selectFolder(-1)">All</div>
                </td>
              </tr>
            </tbody>
          </table>
          <table class="table" style="margin-top: 5px; margin-bottom: 0px;">
            <thead>
              <tr><th>My Folders</th></tr>
            </thead>
          </table>
          <div style="max-height: 150px; overflow: auto">
            <table class="table table-condensed borderless" ng-if="queryData.myFolders.length != 0">
              <tbody>
                <tr ng-repeat="folder in queryData.myFolders" ng-class="{'selected-folder': queryData.selectedFolderId == folder.id}">
                  <td class="item clearfix"> 
                    <div class="pull-left" ng-click="selectFolder(folder.id)" 
                      tooltip-placement="bottom" tooltip="{{folder.name}}" tooltip-append-to-body="true">
                        {{folder.name}}
                    </div>

                    <div class="btn-group pull-right">
                      <button type="button" class="btn btn-default" ng-click="editFolder(folder)"
                        tooltip-placement="bottom" tooltip="Edit folder settings" tooltip-append-to-body="true">
                        <span class="fa fa-cog"></span>
                      </button>
                      <button type="button" class="btn btn-default" ng-click="deleteFolder(folder)"
                        tooltip-placement="bottom" tooltip="Delete folder" tooltip-append-to-body="true">
                        <span class="fa fa-trash-o"></span>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <table class="table table-condensed borderless" ng-if="queryData.myFolders.length == 0">
              <tbody>
                <tr><td>None</td></tr>
              </tbody>
            </table>
          </div>
          <table class="table" style="margin-top: 5px; margin-bottom: 0px;">
            <thead>
              <tr><th>Shared Folders</th></tr>
            </thead>
          </table>
          <div style="max-height: 150px; overflow: auto">
            <table class="table table-condensed borderless" ng-if="queryData.sharedFolders.length != 0">
              <tbody>
                <tr ng-repeat="folder in queryData.sharedFolders" ng-class="{'selected-folder': queryData.selectedFolderId == folder.id}">
                  <td class="item clearfix">
                    <div class="pull-left" ng-click="selectFolder(folder.id)" 
                      tooltip-placement="bottom" tooltip="{{folder.name}}" tooltip-append-to-body="true">
                        {{folder.name}}
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <table class="table table-condensed borderless" ng-if="queryData.sharedFolders.length == 0">
              <tbody>
                <tr><td>None</td></tr>
              </tbody>
            </table>
          </div>
          <table class="table" style="margin-top: 5px; margin-bottom: 0px;" ng-if="queryData.isAdmin">
            <thead>
              <tr><th>Audit Logs</th></tr>
            </thead>
          </table>
          <div style="max-height: 150px; overflow: auto" ng-if="queryData.isAdmin">
            <table class="table table-condensed borderless">
              <tbody>
                <tr>
                  <td class="item clearfix">
                    <span ng-click="viewAllAuditLogs('LAST_24')">Last 24 Hrs</span>
                  </td>
                </tr>
                <tr>
                  <td class="item clearfix">
                    <span ng-click="viewAllAuditLogs('ALL')">ALL</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="col-xs-10">
          <table class="table list" overflow="auto" ng-if="queryData.queries.length > 0">
            <thead>
              <tr>
               <th class="col-xs-4" style="padding-left: 35px;">Title</th>
               <th class="col-xs-3">Created By</th>
               <th class="col-xs-2">Last Updated</th>
              </tr>
            </thead>
            <tbody>
              <tr ng-repeat="query in queryData.queries | filter:searchQueryTitle"
                  ng-mouseenter="query.highlight=true"
                  ng-mouseleave="query.highlight=false" 
                  ng-class="{selected: queryData.selectedQueries.indexOf(query) > -1}"> 
                <td class="clearfix">
                  <div class="pull-left query-select-cb">
                    <input type="checkbox" checklist-model="queryData.selectedQueries" checklist-value="query">
                  </div>
                  <div class="pull-left" style="width: 90%;">
                    <a style="cursor:pointer;" ng-click="runQuery(query)" 
                      tooltip-append-to-body="true" tooltip-placement="bottom" tooltip="Click to view records">
                      #{{query.id}} {{query.title}}
                    </a>
                  </div>
                </td>
                <td>{{query.createdBy | formatUsername}}</td>
                <td>
                  {{query.lastModifiedOn | formatDate}}
                  <div class="pull-right btn-group btn-group-xs" ng-if="query.highlight">
                    <button type="button" class="btn btn-default" ng-click="editQuery(query)"
                      tooltip-placement="bottom" tooltip="Edit Query" tooltip-append-to-body="true">
                      <span class="glyphicon glyphicon-pencil"></span>
                    </button>
                    <a type="button" class="btn btn-default" 
                      tooltip-placement="bottom" tooltip="Download Query Definition" tooltip-append-to-body="true"
                      href="/openspecimen/rest/ng/saved-queries/{{query.id}}/definition-file" target="_blank">
                      <span class="glyphicon glyphicon-download-alt"></span>
                    </a>
                    <button type="button" class="btn btn-default" ng-click="viewAuditLog(query)" 
                      tooltip-placement="bottom" tooltip="View Query Run Log" tooltip-append-to-body="true">
                      <i class="fa fa-bars"></i> 
                    </button>
                    <button type="button" class="btn btn-default" ng-click="deleteQuery(query)"
                      tooltip-placement="bottom" tooltip="Delete Query" tooltip-append-to-body="true"
                      ng-if="queryData.userId == query.createdBy.id || queryData.isAdmin">
                      <span class="fa fa-trash-o"></span>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <table class="table" ng-if="!queryData.queries || queryData.queries.length == 0">
            <thead><tr><th>Information</th></tr></thead>
            <tbody><tr><td>There are no queries to show in selected folder</td></tr></tbody>
          </table>
          <div ng-if="queryData.totalQueries > queryData.pageSize">
            <pagination class="pagination-sm"
              total-items="queryData.totalQueries" 
              items-per-page="queryData.pageSize"  
              max-size="5" 
              ng-model="queryData.currentPage" 
              ng-change="changeQueriesPage(false)">
            </pagination>
          </div>
        </div>
      </div>
    </div>

    <div class="container" ng-if="queryData.view == 'log'">
      <div class="row header">
        <div class="col-xs-7">
          <h3 class="pull-left">
            Audit Log ({{auditData.startAt + 1}} - {{auditData.startAt + auditData.auditLogs.length}} of {{auditData.logCount}})
          </h3>
          <div class="pull-left" style="margin-top: 20px; margin-left: 10px; font-size: 20px;" ng-click="queryData.view = 'dashboard'">
            <a style="cursor: pointer;" 
              tooltip="Go back to previous page" tooltip-placement="bottom" tooltip-append-to-body="true">
              &#8630;
            </a>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12">
          <div ng-if="auditData.auditLogs.length == 0">No audit logs to show</div>
          <table class="table list" overflow="auto" ng-if="auditData.auditLogs.length > 0">
            <thead>
              <tr>
                <th class="col-xs-4">Title</th>
                <th class="col-xs-2">Executed By</th>
                <th class="col-xs-2">Execution Time</th>
                <th class="col-xs-2">Run Time</th>
                <th class="col-xs-2">Type</th>
              </tr>
            </thead>
            <tbody>
              <tr ng-repeat="auditLog in auditData.auditLogs"
                  ng-mouseenter="auditLog.highlight=true"
                  ng-mouseleave="auditLog.highlight=false" style="height:41px;">
                <td ng-if="auditLog.queryId">
                  <a style="cursor:pointer;" ng-click="editQuery({id: auditLog.queryId})" 
                    tooltip-append-to-body="true" tooltip-placement="bottom" tooltip="Click to edit query">
                    #{{auditLog.queryId}} {{auditLog.queryTitle}}
                  </a>
                </td>
                <td ng-if="!auditLog.queryId">Unsaved Query</td>
                <td>{{auditLog.runBy | formatUsername}}</td>
                <td>{{auditLog.timeOfExecution | formatDate}}</td>
                <td>{{auditLog.timeToFinish | formatDuration}}</td>
                <td class="clearfix">
                  {{auditLog.runType}}
                  <div class="pull-right">
                    <button type="button" class="btn btn-xs btn-default" ng-click="viewQuerySql(auditLog)"
                      tooltip-placement="bottom" tooltip="View Generated SQL" tooltip-append-to-body="true"
                      ng-if="auditLog.highlight">
                      <i class="fa fa-code"></i> 
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div ng-if="auditData.logCount > auditData.pageSize">
            <pagination class="pagination-sm"
              total-items="auditData.logCount" 
              items-per-page="auditData.pageSize"  
              max-size="5" 
              ng-model="auditData.currentPage" 
              ng-change="changeAuditLogPage(false)">
            </pagination>
          </div>
        </div>
      </div>
    </div>
 
    <div class="container" ng-if="queryData.view == 'records'">
      <div class="row" style="margin-bottom: 1%;">
        <div class="col-xs-12" style="padding-left: 15px;">
          <div class="btn-group">
            <div class="btn btn-default" ng-click="showQueries()"> 
              <span class="glyphicon glyphicon-th-list"></span>
              <span>Dashboard</span> 
            </div> 
            <div class="btn btn-default" ng-click="redefineQuery()"> 
              <span class="glyphicon glyphicon-pencil"></span> 
              <span>Edit Query </span>
            </div> 
            <div class="btn btn-default" ng-click="showRecords()">
              <span class="glyphicon glyphicon-repeat"></span> 
              <span>Rerun Query</span>
            </div>
            <div class="btn btn-default" ng-click="defineView()"> 
              <span class="glyphicon glyphicon-eye-open"></span> 
              <span>Define View </span>
            </div> 
            <div class="btn btn-default" ng-click="exportRecords()"> 
              <span class="glyphicon glyphicon-export"></span>
              <span>Export CSV</span> 
            </div>
          </div>
          <div class="btn-group" ng-if="showAddToSpecimenList">
            <button type="button" class="btn btn-default" ng-if="!queryData.selectAll" ng-click="selectAllRows()"
              tooltip-placement="bottom" tooltip="Select all rows in below table to assign specimens to list" tooltip-append-to-body="true">
              <span class="glyphicon glyphicon-ok"></span>
              <span>Select All</span>
            </button>
            <button type="button" class="btn btn-default" ng-if="queryData.selectAll" ng-click="unSelectAllRows()">
              <span class="glyphicon glyphicon-remove"></span>
              <span>Unselect All</span>
            </button>
            <button type="button" class="btn btn-default disabled" 
              ng-if="queryData.resultGridOpts.selectedItems.length == 0">
              <span class="glyphicon glyphicon-folder-close"></span>&nbsp;
              <span class="caret"></span>
            </button>
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" ng-click="searchSpecimenList = ''"
              tooltip-placement="bottom" tooltip="Assign specimens to list" tooltip-append-to-body="true"
              ng-if="queryData.resultGridOpts.selectedItems.length > 0">
              <span class="glyphicon glyphicon-folder-close"></span>&nbsp;
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" style="width: 300px;">
              <li>
                <input ng-model="searchSpecimenList" 
                  type="text" class="form-control" style="width: 260px; margin: 3px 20px" 
                  ng-click="$event.stopPropagation()">
              </li>
              <li>
                <ul class="dropdown-menu-subgroup" style="max-height: 150px; overflow: auto;">
                  <li ng-repeat="list in queryData.myLists | filter: searchSpecimenList" ng-click="addSpecimensToList(list)">
                    <a href="#">{{ list.name }}</a>
                  </li>
                </ul>
              </li>
              <li class="divider"></li>
              <li><a ng-click="createNewSpecimenList()">Create New Specimen List</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div ng-if="queryData.notifs.waitRecs" class="alert alert-info">
        <span>Loading records, please wait for a moment ...</span>
      </div>
      <div ng-if="!queryData.notifs.waitRecs && queryData.notifs.error" class="alert alert-danger">
        <span ng-if="queryData.notifs.error == 'BAD_REQUEST'">Malformed Query. Please go back and edit query. <a href="https://catissueplus.atlassian.net/wiki/x/O4BLAQ" target="_blank"><b>Click here</b></a> to watch tutorial</span>
        <span ng-if="queryData.notifs.error == 'INTERNAL_SERVER_ERROR'">Internal Server Error. Please retry after some time or contact system admin</span>
      </div>

      <div ng-show="!queryData.notifs.waitRecs && !queryData.notifs.error" class="row" style="height: 90%; padding-left: 15px;">
        <div ng-if="queryData.moreData" style="color:#b94a48">
          Export to view all records. 
          <a style="color:#b94a48" href="https://catissueplus.atlassian.net/wiki/x/LgDGAQ" target="_blank">
            <u>Click here</u>
          </a> to know why exported data file have more records.
        </div>

        <div ng-if="queryData.reporting.type == 'crosstab'" style="height: 100%;">
          <div style="height: 95%;" ka-pivot-table="queryData.pivotTableOpts">
          </div>
        </div>

        <div ng-if="queryData.reporting.type != 'crosstab'" style="height: 100%;">
          <p class="data-grid-header">
            <strong ng-if="queryData.id">{{queryData.title}}</strong>
            <strong ng-if="!queryData.id">Unsaved Query</strong>
          </p>
          <div class="data-grid" style="height: 95%;" ng-grid="queryData.resultGridOpts">
          </div>
        </div>
      </div>
    </div>

    <div class="container" ng-if="queryData.view == 'query'">
      <div class="row" style="height: 7%;">
        <div class="col-xs-offset-3 col-xs-9">
          <h4 ng-if="queryData.id" class="ellipsis query-title"
            tooltip="{{queryData.title}}" tooltip-placement="bottom" tooltip-append-to-body="true">
            {{queryData.title}}
          </h4>
          <h4 ng-if="!queryData.id" class="query-title">
            Unsaved Query
          </h4>
        </div>
      </div>
      <div class="row" style="height: 93%">
        <div class="col-xs-3 content-left">
          <accordion ka-fix-height="100%" close-others="true"> 
            <div class="panel panel-primary">
              <div class="panel-heading">
                <h3 class="panel-title">Select a Collection Protocol</h3>
              </div>
            </div>
            <div style="margin:7px 0px;">
              <ka-select id="cps" style="width: 100%;"
                data-placeholder="Select Collection Protocol"
                options="queryData.cpList" option-id="id" option-value="shortTitle"
                on-select="onCpSelect"
                selected="queryData.selectedCp.id"
                disabled="queryData.disableCpSelection">
              </ka-select>
            </div>
            <div class="panel panel-primary">
              <div class="panel-heading clearfix">
                <h3 class="panel-title pull-left">Add Filter</h3>
                <div class="pull-right" tooltip-placement="bottom" tooltip="Click to add temporal filter" tooltip-append-to-body="true">
                  <button type="button" class="btn btn-default btn-xs"
                          ng-click="onTemporalFilterSelect()"
                          info-link="https://catissueplus.atlassian.net/wiki/x/O4BLAQ"
                          popover-title="Add Temporal Filter"
                          popover-placement="bottom"
                          popover-append-to-body="true"
                          ka-popover-template="temporal-filter-popover-tmpl.html">
                    <span class="glyphicon glyphicon-time"></span>
                  </button>
                </div>
              </div>
            </div>
            <div class="filter-box">
              <accordion-group ng-repeat="form in queryData.selectedCp.forms | filter: filterCpForm">
                <accordion-heading>
                  <div ng-click="onFormSelect(form)">{{form.caption}}</div>
                </accordion-heading>
                <div ng-show="!form.staticFields">
                  Loading form fields. Please wait for a moment ...
                </div>
                <div ng-show="form.staticFields">
                  <div class="plus-addon plus-addon-input-right" style="margin: 0 -5px 5px;">
                    <span class="glyphicon glyphicon-search"></span>
                    <input type="text" class="form-control" placeholder="Search Field" ng-model="form.searchField" 
                      ng-keyup="form.showExtnFields = (form.searchField != '' )" >
                  </div>
                  <div style="margin-bottom: 3px;" 
                       class="field" 
                       id="{{form.name}}.{{field.name}}" 
                       data-arg="{{form.name}}.{{field.name}}"
                       ng-repeat="field in form.staticFields | filter: {caption: form.searchField}">
                    <span style="cursor: pointer" 
                          ng-click="onFieldSelect(field)"
                          popover-title="Add Filter"
                          popover-placement="right" ka-popover-template="filter-popover-tmpl.html">
                      {{field.caption}}
                    </span>
                  </div>
                  <div style="margin-bottom: 3px;"
                       ng-repeat="extnForm in form.extnForms">
                    <span style="cursor:pointer; color: #666666; font-size: 14px; font-weight: bold;" 
                          ng-click="extnForm.showExtnFields = !extnForm.showExtnFields" 
                          ng-show="filteredExtnFields.length > 0">
                      {{extnForm.caption}}
                      <i ng-if="form.showExtnFields || extnForm.showExtnFields" class="fa fa-caret-down"></i>
                      <i ng-if="!form.showExtnFields && !extnForm.showExtnFields" class="fa fa-caret-right"></i>
                    </span>

                    <div ng-show="(form.showExtnFields || extnForm.showExtnFields) && filteredExtnFields.length > 0" 
                         style="border-bottom: 1px dashed #ddd;">
                      <div style="margin-bottom: 3px;" 
                           class="field" 
                           id="{{form.name}}.{{field.name}}" 
                           data-arg="{{form.name}}.{{field.name}}"
                           ng-repeat="field in filteredExtnFields  = (extnForm.fields | filter: {caption: form.searchField})">
                        <span style="cursor: pointer" 
                              ng-click="onFieldSelect(field)"
                              popover-title="Add Filter"
                              popover-placement="right" ka-popover-template="filter-popover-tmpl.html">
                          {{field.caption}}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </accordion-group>
            </div>
          </accordion>
        </div>

        <div class="col-xs-9 content-right">
          <div class="panel panel-primary" style="height: 28%;">
            <div class="panel-heading">
              <h3 class="panel-title">Query Expression</h3>
            </div>
            <div class="panel-body">
              <div class="btn-toolbar"> 
                <div class="btn-group">
                  <div class="btn btn-sm btn-default" 
                       ng-click="addOp('and')"
                       ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable: '#expr'}" data-arg="and">AND</div>
                  <div class="btn btn-sm btn-default" 
                       ng-click="addOp('or')"
                       ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable: '#expr'}" data-arg="or">OR</div>
                  <div tooltip="Intersection" tooltip-placement="bottom"
                       ng-click="addOp('intersect')"
                       class="btn btn-sm btn-default" ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable: '#expr'}" data-arg="intersect">&#8745;</div>
                  <div class="btn btn-sm btn-default" 
                       ng-click="addOp('not')"
                       ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable: '#expr'}" data-arg="not">NOT</div>
                  <div class="btn btn-sm btn-default"
                       ng-click="addOp('nthchild')"
                       ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable: '#expr'}" data-arg="nthchild">
                       <span class="fa fa-share-alt"></span>
                  </div>
                </div>

                <div class="btn-group">
                  <div class="btn btn-sm btn-default" 
                       ka-draggable="{helper: opDrag, zIndex: 100, connectToSortable:'#expr'}" 
                       data-arg="()"
                       ng-click="addParen()">
                    (&nbsp;...&nbsp;)                     
                  </div>
                </div>
              </div>
              <div id="expr" class="filter-item clearfix" ng-class="{'filter-item-error': !queryData.isValid}" 
                   style="margin-top: 1%; min-height: 4em; width: 100%;" ng-model="queryData.exprNodes" ui-sortable="exprSortOpts">
                <div ng-switch="node.type" ng-repeat="node in queryData.exprNodes" class="pull-left">
                  <div ng-switch-when="filter" class="filter-item-valign filter-node" style="position: relative;"
                    tooltip-html-unsafe="{{getFilterDesc(node.value)}}" tooltip-placement="bottom">
                    <b>{{node.value}}</b>
                    <div class="pq" ng-if="isFilterParamterized(node.value)"
                      tooltip="Parameterized Filter" tooltip-placement="bottom" tooltip-append-to-body="true">
                      P
                    </div>
                  </div>
                  <div ng-switch-when="op" ng-click="toggleOp($index)" 
                    class="filter-item-valign op-node" data-node-pos="{{$index}}" style="position: relative;"
                    tooltip="{{node.value != 'not' && node.value != 'nthchild' ? 'Click to toggle operator' : ''}}" tooltip-placement="bottom">
                    <span class="fa" ng-bind-html="getOpCode(node.value)"></span>
                    <span style="position: absolute; top: 0; right: 0; cursor: pointer;" 
                      class="glyphicon glyphicon-remove" ng-click="removeNode($index)"></span>
                  </div>
                  <div ng-switch-when="paren" class="filter-item-valign paren-node" style="position: relative;">
                    {{node.value}}
                    <span style="position: absolute; top: 0; right: 0; cursor: pointer;" 
                      class="glyphicon glyphicon-remove" ng-click="removeNode($index)"></span>
                  </div>
                </div>
              </div>
            </div>
          </div> 
          <div class="panel panel-primary" style="height: 60%;">
            <div class="panel-heading">
              <h3 class="panel-title">Current Filters</h3>
            </div>
            <div class="panel-body" style="height: 87%; overflow: auto;">
              <div class="filter-item" style="display: table; width: 100%;" ng-repeat="filter in queryData.filters">
                <div style="display: table-cell; vertical-align: middle;">
                  <div class="filter-item-valign pull-left" style="background-color: #cccccc; position: relative; width:2.5em; border-radius: 50%; border: 1px solid #cccccc;text-align: center;margin-right: 1%;">
                    <div><b>{{filter.id}}</b></div>
                    <div class="pq" ng-if="filter.parameterized"
                      tooltip="Parameterized Filter" tooltip-placement="bottom" tooltip-append-to-body="true">
                      P 
                    </div>
                  </div>
                </div>
                <div style="display: table-cell; vertical-align: middle; width: 83.33%; padding-left: 15px; padding-right: 15px;" ng-if="!filter.expr"> 
                  <i>{{filter.form.caption}} &gt;&gt; </i>
                  <i ng-if="filter.field.extensionForm">{{filter.field.extensionForm}} &gt;&gt; </i>
                  <i>{{filter.field.caption}} </i>
                  <b> {{filter.op.desc}} </b> 

                  <i ng-if="filter.op.name == 'between'">{{filter.value[0]}} and {{filter.value[1]}}</i>
                  <i ng-if="filter.op.name != 'between'">{{filter.value}}</i>
                </div>
                <div style="display: table-cell; vertical-align: middle; width: 83.33%; padding-left: 15px; padding-right: 15px;" ng-if="filter.expr"> 
                  <i>{{filter.desc}}</i>
                </div>
                <div style="display: table-cell; vertical-align: middle;">
                  <div class="btn-group pull-right">
                    <button class="btn btn-default" 
                            ng-if="!filter.expr"
                            ng-click="displayFilter(filter)"
                            popover-placement="left" 
                            popover-append-to-body="true"
                            popover-title="Edit Filter"
                            ka-popover-template="filter-popover-tmpl.html">
                      <span class="glyphicon glyphicon-pencil"></span>
                    </button>
                    <button class="btn btn-default" 
                            ng-if="filter.expr"
                            ng-click="displayFilter(filter)"
                            info-link="https://catissueplus.atlassian.net/wiki/x/O4BLAQ"
                            popover-placement="left" 
                            popover-append-to-body="true"
                            popover-title="Edit Filter"
                            ka-popover-template="temporal-filter-popover-tmpl.html">
                      <span class="glyphicon glyphicon-pencil"></span>
                    </button>
                    <button class="btn btn-default" ng-click="deleteFilter(filter)"><span class="glyphicon glyphicon-trash"></span></button>
                  </div>
                </div>
              </div>
            </div>
          </div> 
          <div class="clearfix" style="height: 6%;">
            <div class="pull-left">
              <div class="btn btn-primary" tooltip="Get Count" tooltip-placement="top" ng-click="getCount()" ng-disabled="queryData.filters.length == 0 || !queryData.isValid">
                <span class="glyphicon glyphicon-dashboard"></span>
                <span>Get Count</span>
              </div>
              <div class="btn btn-primary" tooltip="Run Query" tooltip-placement="top" ng-click="getRecords()" ng-disabled="queryData.filters.length == 0 || !queryData.isValid">
                <span class="glyphicon glyphicon-play"></span>
                <span>View Records</span>
              </div>
              <div class="btn btn-primary" tooltip="Save Query" tooltip-placement="top" ng-click="saveQuery()" ng-disabled="queryData.filters.length == 0 || !queryData.isValid">
                <span class="glyphicon glyphicon-save"></span>
                <span>Save Query</span>
              </div>
              <a href=#" tooltip="Cancel and go back to dashboard" tooltip-placement="top" ng-click="showQueries()" style="margin-left: 10px;">Cancel</a>
            </div>
            <div ng-include="'count-tmpl.html'" class="pull-right" style="width: 45%; margin-left: 1%;">
            </div>
          </div>
        </div>
      </div>
    </div>

    <script type="text/ng-template" id="parameterized-filters-modal.html">
      <div class="ka-modal-header">
        <span>{{queryData.title}}</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <table class="table" style="margin-bottom: 0px;">
          <thead>
            <th class="col-xs-5">Field</th>
            <th class="col-xs-3">Operator</th>
            <th class="col-xs-4">Condition Value</th>
          </thead>
        </table>
        <div style="max-height: 380px; overflow: auto;">
          <table class="table">
            <tbody>
              <tr ng-repeat="filter in queryData.filters | filter: {'parameterized': true}">
                <td class="col-xs-5">
                  <span ng-if="!filter.expr">
                    <i>{{filter.form.caption}} &gt;&gt; </i>
                    <i ng-if="filter.field.extensionForm">{{filter.field.extensionForm}} &gt;&gt; </i> 
                    <i>{{filter.field.caption}}</i>
                  </span>
                  <i ng-if="filter.expr">{{filter.desc}}</i>
                </td>
                <td class="col-xs-3" ng-if="!filter.expr">
                  <ka-select id="operator" style="width: 100%;"
                    data-placeholder="Select Operator"
                    options="filter.field.ops" option-id="name" option-value="desc"
                    on-select="onOpSelect(filter)"
                    selected="filter.op">
                  </ka-select>
                </td>
                <td class="col-xs-3" ng-if="filter.expr">
                  <ka-select id="operator" style="width: 100%;"
                    data-placeholder="Select Operator"
                    options="filter.tObj.ops" option-id="name" option-value="desc"
                    on-select="onOpSelect(filter)"
                    selected="filter.op">
                  </ka-select>
                </td>
                <td class="col-xs-4">
                  <div ng-if="!isUnaryOpSelected(filter)">
                    <div id="value" ng-switch="getValueType(filter)">
                      <div ng-switch-when="select">
                        <ka-select data-placeholder="Select Condition Value"
                          options="filter.field.pvs"
                          selected="filter.value" style="width: 100%">
                        </ka-select>
                      </div>
                      <div ng-switch-when="multiSelect">
                        <ka-select multiple
                          data-placeholder="Select Condition Values"
                          options="filter.field.pvs"
                          selected="filter.value" style="width: 100%">
                        </ka-select>
                      </div>
                      <div ng-switch-when="tagsSelect">
                        <ka-tags tags="filter.value" 
                          placeholder="Specify Condition Value & then hit enter key"/>
                      </div>
                      <div ng-switch-when="betweenDate" class="clearfix">
                        <input class="pull-left form-control" placeholder="Range Min" type="text" 
                          ka-date-picker="{{queryData.datePickerOpts}}"
                          ng-model="filter.value[0]"
                          style="width:42%;">
                        <span class="pull-left" style="width:15%;text-align:center;padding: 4px;">and</span>
                        <input class="pull-left form-control" placeholder="Range Max" type="text" 
                          ka-date-picker="{{queryData.datePickerOpts}}"
                          ng-model="filter.value[1]"
                          style="width:42%;">
                      </div>
                      <div ng-switch-when="betweenNumeric" class="clearfix">
                        <input class="pull-left form-control" placeholder="Range Min" type="text" 
                          ng-model="filter.value[0]"
                          style="width:42%;">
                        <span class="pull-left" style="width:15%;text-align:center;padding: 4px;">and</span>
                        <input class="pull-left form-control" placeholder="Range Max" type="text" 
                          ng-model="filter.value[1]"
                          style="width:42%;">
                      </div>
                      <div ng-switch-when="lookupSingle">
                        <input type="hidden" style="width: 100%;"
                          ka-lookup opts="filter.field.lookupProps" 
                          placeholder="Select Condition Value"
                          selected-opt="filter.value"></input>
                      </div>
                      <div ng-switch-when="lookupMultiple">
                        <input type="hidden" style="width: 100%;"
                          ka-lookup opts="filter.field.lookupProps" multiple
                          placeholder="Select Condition Values"
                          selected-opt="filter.value"></input>
                      </div>
                      <input ng-switch-when="datePicker" class="form-control"
                        data-placeholder="Select Date"
                        type="text" ka-date-picker="{{queryData.datePickerOpts}}"
                        ng-model="filter.value"></input>
                      <input ng-switch-default class="form-control"
                        placeholder="Specify Condition Value"
                        type="text" ng-model="filter.value"></input>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="ka-modal-footer">
          <div ng-include="'count-tmpl.html'" class="pull-left" style="text-align: left; width: 45%; margin-left: 1%;">
          </div>

          <button class="btn ka-btn-text" ng-click="cancel()">Cancel</button>
          <button class="btn btn-primary" ng-click="getCount()"
            tooltip="Get Count" tooltip-placement="top" tooltip-append-to-body="true">
            <span class="glyphicon glyphicon-dashboard"></span>
            <span>Get Count</span>
          </button>
          <button class="btn btn-primary" ng-click="ok()"
            tooltip="Run Query" tooltip-placement="top" tooltip-append-to-body="true">
            <span class="glyphicon glyphicon-play"></span>
            <span>View Records</span>
          </button>
        </div>
      </div>
    </script>
        
    <script type="text/ng-template" id="filter-popover-tmpl.html">
      <div style="width: 300px;">
        <div class="form-group">
          <label for="operator">Operator</label>
          <ka-select id="operator" style="width: 100%;"
            data-placeholder="Select Operator"
            options="queryData.currFilter.ops" option-id="name" option-value="desc"
            on-select="onOpSelect"
            selected="queryData.currFilter.op">
          </ka-select>
        </div>

        <div class="form-group" ng-hide="isUnaryOpSelected()">
          <label for="value">Condition Value</label>
          <div id="value" ng-switch="getValueType()">
            <div ng-switch-when="select">
              <ka-select data-placeholder="Select Condition Value"
                options="queryData.currFilter.field.pvs"
                selected="queryData.currFilter.value" style="width: 100%">
              </ka-select>
            </div>
            <div ng-switch-when="multiSelect">
              <ka-select multiple
                data-placeholder="Select Condition Values"
                options="queryData.currFilter.field.pvs"
                selected="queryData.currFilter.value" style="width: 100%">
              </ka-select>
            </div>
            <div ng-switch-when="tagsSelect">
              <ka-tags tags="queryData.currFilter.value" 
                placeholder="Specify Condition Value & then hit enter key"/>
            </div>
            <div ng-switch-when="betweenDate" class="clearfix">
              <input class="pull-left form-control" placeholder="Range Min" type="text" 
                ka-date-picker="{{queryData.datePickerOpts}}"
                ng-model="queryData.currFilter.value[0]"
                style="width:42%;">
              <span class="pull-left" style="width:15%;text-align:center;padding: 4px;">and</span>
              <input class="pull-left form-control" placeholder="Range Max" type="text" 
                ka-date-picker="{{queryData.datePickerOpts}}"
                ng-model="queryData.currFilter.value[1]"
                style="width:42%;">
            </div>
            <div ng-switch-when="betweenNumeric" class="clearfix">
              <input class="pull-left form-control" placeholder="Range Min" type="text" 
                ng-model="queryData.currFilter.value[0]"
                style="width:42%;">
              <span class="pull-left" style="width:15%;text-align:center;padding: 4px;">and</span>
              <input class="pull-left form-control" placeholder="Range Max" type="text" 
                ng-model="queryData.currFilter.value[1]"
                style="width:42%;">
            </div>
            <div ng-switch-when="lookupSingle">
              <input type="hidden" style="width: 100%;"
                ka-lookup opts="queryData.currFilter.field.lookupProps" 
                placeholder="Select Condition Value"
                selected-opt="queryData.currFilter.value"></input>
            </div>
            <div ng-switch-when="lookupMultiple">
              <input type="hidden" style="width: 100%;"
                ka-lookup opts="queryData.currFilter.field.lookupProps" multiple
                placeholder="Select Condition Values"
                selected-opt="queryData.currFilter.value"></input>
            </div>
            <input ng-switch-when="datePicker" class="form-control"
              data-placeholder="Select Date"
              type="text" ka-date-picker="{{queryData.datePickerOpts}}"
              ng-model="queryData.currFilter.value"></input>
            <input ng-switch-default class="form-control"
              placeholder="Specify Condition Value"
              type="text" ng-model="queryData.currFilter.value"></input>
          </div>
        </div>

        <div class="form-group">
          <label class="checkbox-inline">
            <input type="checkbox" ng-model="queryData.currFilter.parameterized" 
              ng-checked="queryData.currFilter.parameterized">
            Parameterized Filter
          </label>
        </div>

        <div class="form-group"> 
          <button class="btn btn-success" ng-disabled="disableAddFilterBtn()" ng-click="addFilter()" ng-show="!queryData.currFilter.id">Add</button>
          <button class="btn btn-success" ng-disabled="disableAddFilterBtn()" ng-click="editFilter()" ng-show="queryData.currFilter.id">Edit</button>
          <button class="btn btn-default" ng-click="cancelFilter()">Cancel</button>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="temporal-filter-popover-tmpl.html">
      <div style="width: 300px;">
        <div class="form-group">
          <label>Temporal Expression</label>
          <textarea class="form-control" 
                    rows="5"
                    ng-model="queryData.currFilter.expr" 
                    ui-event="{keydown: 'handleAutocompleteKeyDown($event)'}" 
                    ui-autocomplete="temporalFilterOpts">
          </textarea>
        </div>
        <div class="form-group">
          <label>Description</label>
          <input type="text" ng-model="queryData.currFilter.desc" class="form-control">
        </div>
        <div class="form-group"> 
          <label class="checkbox-inline">
            <input type="checkbox" ng-model="queryData.currFilter.parameterized" 
              ng-checked="queryData.currFilter.parameterized">
            Parameterized Filter
          </label>
        </div>
        <div class="form-group"> 
          <button class="btn btn-success" 
            ng-disabled="disableAddFilterBtn()" ng-click="addFilter()" 
            ng-show="!queryData.currFilter.id">
            Add
          </button>
          <button class="btn btn-success" 
            ng-disabled="disableAddFilterBtn()" ng-click="editFilter()" 
            ng-show="queryData.currFilter.id">
            Edit
          </button>
          <button class="btn btn-default" ng-click="cancelFilter()">
            Cancel
          </button>
        </div>
      </div>
    </script>
          
    <script type="text/ng-template" id="define-view.html">
      <div class="ka-modal-header">
        <span>Define View</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>

      <div id="define-view-notif" class="define-view-notifs hidden"></div>

      <div class="ka-modal-body">
        <div ka-wizard="defineViewWizard">
          <ka-wizard-step on-finish="prepareAggregateOpts" title="Select Fields">
            <div style="margin-top: 10px; margin-right: -20px; height: 380px; overflow: auto">
              <span class="plus-heading">Please select the fields that you wish to view in results table</span>
              <span class="plus-note plus-margin-bottom">Drag and drop fields to reorder the view</span>
              <ka-tree opts='treeOpts'></ka-tree>
            </div>
          </ka-wizard-step>

          <ka-wizard-step on-finish="preparePivotTableOpts" title="Aggregates">
            <div ng-include="'aggregate-functions.html'"></div>
          </ka-wizard-step>

          <ka-wizard-step title="Pivot Table">
            <div ng-include="'pivot-table.html'"></div>
          </ka-wizard-step>

          <div class="ka-modal-footer">
            <button class="btn ka-btn-text" ng-click="cancel()">
              Cancel
            </button>
            <button class="btn ka-btn-secondary" ng-if="!defineViewWizard.isFirstStep()" 
              ng-click="defineViewWizard.previous(false)">
              Previous
            </button>
            <button class="btn btn-primary" ng-if="!defineViewWizard.isLastStep()" 
              ng-click="defineViewWizard.next(false)">
              Next
            </button>
            <button class="btn btn-primary" ng-click="ok()">
              Done
            </button>
          </div>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="import-query.html">
      <div class="ka-modal-header">
        <span>Import Query</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <form action="/openspecimen/rest/ng/saved-queries/definition-file">
          <div class="form-group">
            <label>Select Query Definition File</label>
            <div style="position:relative;">
              <input id="uploadQueryDef" class="form-control" name="file" type="file" style="height: auto!important;">
              <span id="queryDefFilename" style="position:absolute; top:10px; left: 110px; min-width: 150px; background: white;">
                No File Selected
              </span>
            </div>
          </div>
        </form>
        <div class="ka-modal-footer">
          <button class="btn ka-btn-text" ng-click="cancel()">Cancel</button>
          <button class="btn btn-primary" id="importQuery">Import</button>
        </div>
      </div>
    </script>
      
    <script type="text/ng-template" id="save-query.html">
      <div class="ka-modal-header">
        <span>Save Query</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <div class="form-group">
          <label>Title</label>
          <input class="form-control" ng-model="modalData.title" type="text">
        </div>       
        <div class="ka-modal-footer">
          <button class="btn ka-btn-text" ng-click="cancel()">Cancel</button>
          <button class="btn ka-btn-secondary" ng-click="save(true)" ng-if="modalData.id">Save a Copy</button>
          <button class="btn btn-primary" ng-click="save(false)">Save</button>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="addedit-query-folder.html">
      <div class="ka-modal-header">
        <span ng-if="!modalData.folderId">New Query Folder</span>
        <span ng-if="modalData.folderId">Update Query Folder</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>

      <div class="ka-modal-body">
        <div class="form-group">
          <label>Folder Name</label>
          <input type="text" ng-model="modalData.folderName" class="form-control" placeholder="Query Folder Name">
        </div>

        <div> <label>Queries</label> </div>
        <div style="height: 180px; margin-bottom: 20px; width: 100%; overflow:auto;">
          <table class="table" ng-if="modalData.queries.length > 0">
            <tbody>
              <tr ng-repeat="query in modalData.queries">
                <td class="col-xs-11">{{query.title}}</td>
                <td class="col-xs-1">
                  <span ng-click="removeQuery(query, $index)" class="glyphicon glyphicon-trash" style="cursor: pointer;"
                    tooltip-placement="bottom" tooltip-append-to-body="true" tooltip="Remove query from folder"></span>
                </td>
              </tr>
            </tbody>
          </table>
          <span ng-if="modalData.queries.length == 0">
            No queries selected. Please select at least one query.
          </span>
        </div>

        <div class="checkbox">
          <label>
            <input type="checkbox" ng-checked="modalData.sharedWithAll" ng-model="modalData.sharedWithAll"> 
            Share folder with all present and future users   
          </label>
        </div>

        <div style="height: 100px;" ng-if="modalData.sharedWithAll != 1 && modalData.sharedWithAll != true">
          <label>Share folder with following users</label>
          <div style="height: 80px; overflow: auto;">
            <ka-search ng-model="modalData.sharedWith" data-placeholder="Users"
              on-initselectionfn="initSelectedUsers" on-query="searchUser" multiple
              on-select="onUserSelect(selected)" style="width:100%;">
            </ka-search>
          </div>
        </div>

        <div class="ka-modal-footer">
           <button class="btn ka-btn-text" ng-click="cancel()">Cancel</button>
           <button class="btn btn-primary" 
             ng-disabled="modalData.queries.length == 0 || !modalData.folderName" 
             ng-if="!modalData.folderId" 
             ng-click="saveOrUpdateFolder()">Create</button>
           <button class="btn btn-primary" 
             ng-disabled="modalData.queries.length == 0 || !modalData.folderName" 
             ng-if="modalData.folderId" 
             ng-click="saveOrUpdateFolder()">Update</button>
        </div> 
      </div>
    </script>

    <script type="text/ng-template" id="view-query-sql.html">
      <div class="ka-modal-header">
        <span>Generated SQL</span> 
        <div class="ka-close" ng-click="close()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <div style="height: 360px; overflow: auto; text-align: justify;">
          <code style="white-space: normal;">{{auditLog.sql}}</code>
        </div>
        <div class="ka-modal-footer">
          <button class="btn btn-primary" ng-click="close()">Close</button>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="view-audit-logs.html">
      <div class="ka-modal-header">
        <span>Audit Log of #{{query.id}}</span> 
        <div class="ka-close" ng-click="close()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <table class="table" style="margin-bottom: 0px;">
          <thead>
            <tr>
              <th class="col-xs-3">Execution Time</th>
              <th class="col-xs-3">Time to Finish</th>
              <th class="col-xs-3">Run Type</th>
            </tr>
          </thead>
        </table>
        <div style="height:300px; width: 100%; overflow:auto;">
          <table class="table list">
            <tbody>
              <tr ng-repeat="auditLog in auditLogs">
                <td class="col-xs-3">{{auditLog.timeOfExecution | formatDate}}</td>
                <td class="col-xs-3">{{auditLog.timeToFinish | formatDuration}}</td>
                <td class="col-xs-3">{{auditLog.runType}}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="ka-modal-footer">
          <button class="btn btn-primary" ng-click="close()">Close</button>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="delete-query-confirm.html">
      <div class="ka-modal-header">
        <span>Delete Query</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <p> Are you sure you want to delete following query? </p>
        <p> <i> {{query.title}} </i> </p>

        <div class="ka-modal-footer">
          <button class="btn ka-btn-secondary" ng-click="cancel()">No</button>
          <button class="btn btn-primary" ng-click="ok()">Yes</button>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="create-specimen-list.html">
      <div class="ka-modal-header">
        <span>New Specimen List</span>
        <div class="ka-close" ng-click="cancel()">&times;</div>
      </div>
      <div class="ka-modal-body">
        <div>
          <p>Please enter a new specimen list name:</p>
          <input type="text" ng-model="specimenList.name" class="form-control" placeholder="Specimen List Name">
        </div>
        <div class="ka-modal-footer">
          <button class="btn ka-btn-text" ng-click="cancel()">Cancel</button>
          <button class="btn btn-primary" ng-disabled="!specimenList.name" ng-click="saveSpecimenList()">Create</button>
        </div> 
      </div>
    </script>

    <script type="text/ng-template" id="field-tmpl.html">
      <div> {{node.val}} </div>
    </script>

    <script type="text/ng-template" id="count-tmpl.html">
      <div ng-if="queryData.notifs.showCount">
        <div ng-if="queryData.notifs.waitCount" class="alert alert-info" style="padding: 5px 12px;">
          <span>Counting ...</span>
        </div>

        <div ng-if="!queryData.notifs.waitCount">
          <div ng-if="!queryData.notifs.error">
            <div ng-if="queryData.cprCnt != 0 || queryData.specimenCnt != 0" 
              class="alert alert-info" style="padding: 5px 12px;">
              <span style="margin-right: 2.5em"><b>Participant Count:</b> {{queryData.cprCnt}} </span>
              <span><b>Specimen Count:</b> {{queryData.specimenCnt}} </span>
              <span style="font-size: 1.2em; cursor: pointer;" class="pull-right" ng-click="closeNotif('showCount')">&times;</span>
            </div>
            <div ng-if="queryData.cprCnt == 0 && queryData.specimenCnt == 0" 
              class="alert alert-warning" style="padding: 5px 12px;">
              <span>
                Zaarooo records found! 
                <a href="https://catissueplus.atlassian.net/wiki/x/O4BLAQ" target="_blank">
                  <b>Click here</b>
                </a> to watch tutorial
              </span>
              <span style="font-size: 1.2em; cursor: pointer;" class="pull-right" ng-click="closeNotif('showCount')">&times;</span>
            </div>
          </div>
          <div ng-if="queryData.notifs.error">
            <div class="alert alert-danger" style="padding: 5px 12px;" ng-switch on="queryData.notifs.error">
              <span ng-switch-when="BAD_REQUEST">
                Query is malformed! 
                <a href="https://catissueplus.atlassian.net/wiki/x/O4BLAQ" target="_blank">
                  <b>Click here</b>
                </a> to watch tutorial
              </span>
              <span ng-switch-when="INTERNAL_SERVER_ERROR">
                Internal Server Error. Contact system admin.
              </span>
              <span style="font-size: 1.2em; cursor: pointer;" 
                class="pull-right" ng-click="closeNotif('showCount')">
                &times;
              </span>
            </div>
          </div>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="pivot-table.html">
      <div style="margin-top: 10px; height: 380px; overflow: auto">
        <div class="form-group">
          <label class="checkbox-inline">
            <input type="checkbox" ng-model="pivotTable" 
              ng-checked="reporting.type == 'crosstab'" ng-change="createPivotTable(pivotTable)"> 
            Create Pivot Table
          </label>
        </div>

        <div ng-if="reporting.type == 'crosstab'">
          <div class="form-group">
            <label for="group-rows-by">Row Fields</label>
            <ka-select id="group-rows-by" style="width: 100%;"
              data-placeholder="Select fields to use for grouping rows"
              options="groupRowsBy"
              on-select="onGroupRowsByChange"
              multiple selected="reporting.params.groupRowsBy">
            </ka-select>
          </div>

          <div class="form-group">
            <label for="group-col-by">Column Field</label>
            <ka-select id="group-col-by" style="width: 100%;"
              data-placeholder="Select field to use for grouping columns"
              options="groupColBy"
              on-select="onGroupColByChange"
              selected="reporting.params.groupColBy">
            </ka-select>
          </div>

          <div class="form-group">
            <label for="summary-value">Value Field</label>
            <ka-select id="summary-value" style="width: 100%;"
              data-placeholder="Select summary field"
              options="summaryFields"
              on-select="onSummaryFieldChange"
              multiple selected="reporting.params.summaryFields">
            </ka-select>
          </div>

          <div class="form-group">
            <label class="checkbox-inline">
              <input type="checkbox" ng-model="reporting.params.includeSubTotals" 
                ng-checked="reporting.params.includeSubTotals">
              Include sub-totals
            </label>
          </div>
        </div>
      </div>
    </script>

    <script type="text/ng-template" id="aggregate-functions.html">
      <div style="margin-top: 20px;">
        <div class="row">
          <div class="col-xs-6">
            <label>Selected Fields</label>
          </div>

          <div class="col-xs-6">
            <label>Functions</label>
          </div> 
        </div>
               
        <div class="row">
          <div class="col-xs-6" style="height: 340px;">
            <div class="list-group ka-list-box">
              <a class="list-group-item ellipsis" 
                ng-class="{'active': currField.name == selectedField.name}"
                ng-repeat="selectedField in selectedFields"
                ng-click="showCurrField(selectedField)"
                tooltip="{{selectedField.form}}: {{selectedField.label}}" 
                tooltip-placement="bottom" tooltip-append-to-body="true">
                <span class="badge">
                  {{(selectedField.aggFns | filter:{opted: true}).length}}
                </span>
                {{selectedField.form}}: {{selectedField.label}}
              </a>
            </div>
          </div>

          <div class="col-xs-6" ng-if="!currField.name">
            Select field in list on the left side panel
          </div>

          <div class="col-xs-6" ng-if="currField.name">
            <div class="row form-group" ng-repeat="fn in currField.aggFns">
              <div class="col-xs-3">
                <label class="checkbox-inline" style="margin-top: 7px">
                  <input type="checkbox" 
                    ng-model="fn.opted" 
                    ng-checked="fn.opted"
                    ng-change="toggleAggFn(currField, fn)"> 
                  {{fn.label}}
                </label>
              </div>
              <div class="col-xs-9" ng-if="fn.opted">
                <input type="text" class="form-control" ng-model="fn.desc">
              </div>
            </div>
          </div>
        </div>
      </div>
    </script>

    <script>
      var query = query || {};
      query.global = query.global || {};
      query.global.userId = <%= ((edu.wustl.common.beans.SessionDataBean)session.getAttribute("sessionData")).getUserId() %>
      query.global.isAdmin = <%= ((edu.wustl.common.beans.SessionDataBean)session.getAttribute("sessionData")).isAdmin() %>
      query.global.dateFormat = <%= "'" + edu.wustl.common.util.global.CommonServiceLocator.getInstance().getDatePattern().toLowerCase()  + "'"%>
    </script>

    <script src="../js/utility.js" type="text/javascript"></script>
    <script src="../js/wrapper.js" type="text/javascript"></script>
    <script src="../js/pivottable.js" type="text/javascript"></script>
    <script src="../js/filters.js" type="text/javascript"></script>
    <script src="../js/services.js" type="text/javascript"></script>
    <script src="../js/forms-service.js" type="text/javascript"></script>
    <script src="../js/ncontrollers.js" type="text/javascript"></script>
    <script src="../js/directives.js" type="text/javascript"></script>
    <script src="../js/query-directives.js" type="text/javascript"></script>
    <script src="../js/app.js" type="text/javascript"></script>
  </body>
</html>
