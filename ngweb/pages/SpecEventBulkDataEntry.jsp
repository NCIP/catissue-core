<!DOCTYPE html>
<html ng-app="specimen-event-app">
<head lang="en">
  <link href="../external/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="../external/select2/css/select2.css" rel="stylesheet" type="text/css">
  <link href="../external/select2/css/select2-bootstrap.css" rel="stylesheet" type="text/css">
  <link href="../external/de/css/de.css" rel="stylesheet" type="text/css">
  <link href="../external/jquery/css/chosen.min.css" rel="stylesheet" type="text/css">
  <link href="../external/jquery/css/chosen-sprite.png" rel="stylesheet" type="text/css">

  <script src="../external/jquery/jquery.min.js" type="text/javascript"></script>
  <script src="../external/jquery/chosen.jquery.min.js" type="text/javascript"></script>
  <script src="../external/jquery/jquery.ui.widget.js" type="text/javascript"></script>
  <script src="../external/select2/select2.min.js" type="text/javascript"></script>
  <script src="../external/angularjs/angular.min.js"type="text/javascript"></script>
  <script src="../external/angularjs/angular-resource.min.js" type="text/javascript"></script>
  <script src="../external/angularjs/ui-bootstrap-tpls-0.11.0.min.js" type="text/javascript"></script>
  <script src="../external/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
  <script src="../external/eternicode/js/bootstrap-datepicker.js" type="text/javascript"></script>
  <style type="text/css">
    th {
      background: rgba(170, 170, 170, 0.24);
      color:  #333;
    }

    .right {
      float: right;
    }

    .data-table {
      margin-top:10px;
      height:350px;
      overflow:auto;
    }
  </style>

    </head>
<body ng-controller="SpecimenEventController">
<div class="container">
    <div class="row">
        <div class="col-xs-7"><h3>Specimen Event Bulk Data Entry </h3></div>
    </div>
    <div id="notifications" class="cp-notifs hidden"></div>
    <form class="form-horizontal">
      <div class="form-group">
        <label for="specimenLabels" class="control-label col-xs-2">Specimen Label(s)</label>
        <div class="col-xs-7">
          <textarea class="form-control" ng-model="specimenLabels" rows="2"></textarea>
        </div>
      </div>
      <div class="form-group">
        <label for="Event" class="control-label col-xs-2">Event</label>
        <div class="col-xs-7">
          <ka-select style="width: 100%" data-placeholder="Select Event"
            options="specimenEvents" option-id="formId" option-value="caption" selected="selectedEvent" on-select="onEventSelect"></ka-select>
        </div>
      </div>
    </form>
    <div id="bulk-data-entry">
      <div>
        <button class="btn btn-primary" id="add" ng-click="addRecord()" ng-show="dataEntryMode != true">Add Record</button>
        <button class="btn btn-primary" id="edit" ng-click="editDataTable()" ng-show="dataEntryMode != true">Edit Existing Records</button>
        <button class="btn btn-primary" id="save" ng-click="saveDataTable()" ng-show="dataEntryMode">Save</button>
        <button class="btn btn-default" id="cancel" ng-click="cancelDataTable()" ng-show="dataEntryMode">Cancel</button>
        <button class="btn btn-primary right" id="Apply First To All" ng-click="applyFirstToAll()">Apply First To All</button>
      <div>
        <div id="data-table" class="data-table"></div>
      </div>
    <script>
       var dateFormat = <%= "'" + edu.wustl.common.util.global.CommonServiceLocator.getInstance().getDatePattern().toLowerCase()  + "'"%>
    </script>
    <script src="../js/utility.js" type="text/javascript"></script>
    <script src="../js/directives.js" type="text/javascript"></script>
    <script src="../js/wrapper.js" type="text/javascript"></script>
    <script src="../js/specimenEventBulkDataEntry.js" type="text/javascript"></script>
    <script src="../js/forms-service.js" type="text/javascript"></script>
    <script src="../external/de/js/de-form.js" type="text/javascript"></script>
</body>
</html>
