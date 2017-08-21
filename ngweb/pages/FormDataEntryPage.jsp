
<!DOCTYPE html>
<html ng-app="dataentry-app">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="ngweb/css/app.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/select2/css/select2.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/select2/css/select2-bootstrap.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/eternicode/css/datepicker.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/de/css/de.css" rel="stylesheet" type="text/css"></link>
    <link href="ngweb/external/jquery/css/chosen.min.css" rel="stylesheet" type="text/css">
    <link href="ngweb/external/jquery/css/chosen-sprite.png" rel="stylesheet" type="text/css">
    <link href="ngweb/external/bootstrap3-timepicker/css/bootstrap-timepicker.min.css" rel="stylesheet" type="text/css">

    <script src="ngweb/external/jquery/jquery.min.js" type="text/javascript"></script>
    <script src="ngweb/external/jquery/chosen.jquery.min.js" type="text/javascript"></script>
    <script src="ngweb/external/jquery/jquery.ui.widget.js" type="text/javascript"></script>
    <script src="ngweb/external/jquery/jquery.iframe-transport.js" type="text/javascript"></script>
    <script src="ngweb/external/jquery/jquery.fileupload.js" type="text/javascript"></script>
    <script src="ngweb/external/select2/select2.min.js" type="text/javascript"></script>
    <script src="ngweb/external/angularjs/angular.min.js" type="text/javascript"></script>
    <script src="ngweb/external/angularjs/angular-sanitize.min.js" type="text/javascript"></script>
    <script src="ngweb/external/angularjs/angular-resource.min.js" type="text/javascript"></script>
    <script src="ngweb/external/angularjs/ui-bootstrap-tpls-0.11.0.min.js" type="text/javascript"></script>
    <script src="ngweb/external/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="ngweb/external/eternicode/js/bootstrap-datepicker.js" type="text/javascript"></script>
    <script src="ngweb/external/bootstrap3-timepicker/bootstrap-timepicker.min.js" type="text/javascript"></script>

  </head>
  <body ng-controller="FormsDataController" style="height: 100%;" class="clearfix">
    <div id="notifications" class="cp-notifs hidden"></div>
    <div id="forms-list" class="panel panel-default" style="margin-left: 1%; margin-right: 2%; margin-top: 1%;" ng-if="currentView == 'forms-list'"> <!-- xs-10 -->
      <div class="panel-heading"> <!-- row -->
        <div class="panel-title"> <!-- col-xs-7 -->
          Forms
        </div>
      </div>

      <div class="panel-body">
        <div ng-if="formsList.length == 0">
          No forms to show
        </div>
        <table class="table" overflow="auto" ng-if="formsList.length > 0">
          <thead>
            <tr>
              <th class="col-xs-5">Caption</th>
              <th class="col-xs-2">Count</th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="form in formsList">
              <td>
                <a style="cursor:pointer;" 
                   tooltip-placement="bottom" 
                   tooltip="Click to view form records" 
                   ng-click="displayRecords(form)">{{form.formCaption}}
                </a>
              </td>
              <td>{{form.noOfRecords}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div id="records-list" class="panel panel-default" style="height: 100%; margin-top: 1%; margin-left: 1%; margin-right: 2%;" ng-if="currentView == 'records-list'">

      <div class="panel-heading"> 
        <div class="panel-title clearfix">
          <div class="pull-left" style="padding-top:5px;">{{form.formCaption}} Records</div>
          <div class="btn-group pull-right">
            <button type="button" class="btn btn-default" ng-show="showDeleteButton()" ng-click="deleteRecords()">
              <span class="glyphicon glyphicon-trash"></span> 
              Delete
            </button>
            <button class="btn btn-default" ng-click="showForms(false)">
              <span class="glyphicon glyphicon-th-list"></span>
              List Forms
            </button>
            <button class="btn btn-primary" ng-click="addRecord()">
              <span class="glyphicon glyphicon-plus"></span>
              Add Record
            </button>
          </div>
         
        </div>
      </div>

      <div class="panel-body" style="height: 50%; overflow: auto;">
        <div ng-if="records.length == 0">
          No records to show
        </div>
        <table class="table" style="max-height: 75%;" ng-if="records.length > 0"> 
          <thead>
            <tr>
              <th class="col-xs-3">Record Id</th>
              <th class="col-xs-3">Modified By </th>
              <th class="col-xs-3">Updated</th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="record in records | orderBy:'updateTime':true" id="{{record.id}}">
              <td>
                <span style="vertical-align: middle;">
                  <input type="checkbox" ng-model="record.mfd"/>
                </span>
                <a style="cursor: pointer; margin-left: 5px;" 
                   tooltip-placement="right"
                   tooltip="Click to view/edit form record"
                   ng-click="editFormData(record)">#{{record.recordId}}
                </a>
              </td>
              <td>{{record.user.lastName}}, {{record.user.firstName}}</td>
              <td>{{formatDate(record.updateTime)}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>     
    <div id="form-view" style="margin-left: 1%; margin-top: 1%; margin-right: 2%;" ng-show="currentView == 'form'">
    </div>
    <div id="print-view" style="margin-left: 1%; margin-top: 1%; margin-right: 2%;" ng-show="currentView == 'print'">
      <label class="print-form-caption" style = "font-size: 20px; text-align: center; width: 100%"> 
        {{printCaption}}
      </label>
      
      <table class = "table" style="margin-top: 20px">
        <tbody>
          <tr>
            <td class="cp-print-label"> Printed On : </td>
            <td class="cp-print-value"> {{headerInfo.user.printedOn}}</td>
            <td class="cp-print-label"> Printed By : </td>
            <td class="cp-print-value"> {{headerInfo.user.printedBy}}</td>
         </tr>
         <tr>
            <td class="cp-print-label"> Participant protocol ID : </td>
            <td class="cp-print-value"> {{headerInfo.participant.ppId}}</td>
            <td class="cp-print-label"> CP Title By : </td>
            <td class="cp-print-value"> {{headerInfo.participant.cpTitle}}</td>
         </tr>
         <tr ng-if="headerInfo.scg.label != 'null'">
            <td class="cp-print-label"> SCG Label : </td>
            <td class="cp-print-value"> {{headerInfo.scg.label}}</td>
            <td class="cp-print-label"> CP Event Label : </td>
            <td class="cp-print-value"> {{headerInfo.scg.cpEventLabel}}</td>
         </tr>
          <tr ng-if="headerInfo.specimen.label != 'null'">
            <td class="cp-print-label"> Specimen Label: </td>
            <td class="cp-print-value"> {{headerInfo.specimen.label}}</td>
            <td class="cp-print-label"></td>
            <td class="cp-print-value"></td>
         </tr>
        </tbody>
      </table> 
      
      <div class="header-content-hr" 
        style="border-top: 2px solid #ccc;border-bottom: 2px solid #000; margin-bottom: 25px">
      </div>
     
      <div id="de-form" ng-bind-html="printableFormHtml">
      </div>
    </div>  
    
    <script>
      var entity = "<%= request.getAttribute("entityType") %>"
      var entityObjId   = <%= request.getAttribute("entityRecId") %>
      var ppId = "<%= request.getAttribute("ppId") %>"
      var cpTitle = "<%= request.getAttribute("cpTitle") %>"
      var scgLabel = "<%= request.getAttribute("scgLabel") %>"
      var cpEventLabel = "<%= request.getAttribute("cpEventLabel") %>"
      var specimenLabel = "<%= request.getAttribute("specimenLabel") %>"
      var userName =  "<%= request.getAttribute("userName") %>"
      var dateFormat = <%= "'" + edu.wustl.common.util.global.CommonServiceLocator.getInstance().getDatePattern().toLowerCase()  + "'"%>

    </script>
    <script src="ngweb/js/utility.js" type="text/javascript"></script>
    <script src="ngweb/js/wrapper.js" type="text/javascript"></script>
    <script src="ngweb/js/directives.js" type="text/javascript"></script>
    <script src="ngweb/js/forms-service.js" type="text/javascript"></script>
    <script src="ngweb/js/dataentry-controller.js" type="text/javascript"></script>
    <script src="ngweb/js/dataentry-app.js" type="text/javascript"></script>
    <script src="ngweb/external/de/js/de-field-manager.js" type="text/javascript"></script>
    <script src="ngweb/external/de/js/de-form.js" type="text/javascript"></script>

    <script src="ngweb/js/de-users.js" type="text/javascript"></script>
  </body>
</html>
