<!DOCTYPE html>
<html ng-app="specimen-event-app">
<head lang="en">
  <link href="../external/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="../external/select2/css/select2.css" rel="stylesheet" type="text/css">
  <link href="../external/select2/css/select2-bootstrap.css" rel="stylesheet" type="text/css">
  <link href="../external/eternicode/css/datepicker.css" rel="stylesheet" type="text/css">
  <link href="../external/bootstrap3-timepicker/css/bootstrap-timepicker.min.css" rel="stylesheet" type="text/css">
  <link href="../external/de/css/de.css" rel="stylesheet" type="text/css">
  <link href="../css/app.css" rel="stylesheet" type="text/css">
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
  <script src="../external/bootstrap3-timepicker/bootstrap-timepicker.min.js" type="text/javascript"></script>
  <style type="text/css">
    .right {
      float: right;
    }

    .data-table-container {
      margin-top:10px;
      height:350px;
      overflow:auto;
    }

    .spin {
      -webkit-animation: spin 2s infinite linear;
      -moz-animation: spin 2s infinite linear;
      -o-animation: spin 2s infinite linear;
      animation: spin 2s infinite linear;
      -webkit-transform-origin: 50% 58%;
      transform-origin:50% 58%;
      -ms-transform-origin:50% 58%; /* IE 9 */
    }

    @-moz-keyframes spin {
      from {
        -moz-transform: rotate(0deg);
      }
      to {
        -moz-transform: rotate(360deg);
      }
    }

    @-webkit-keyframes spin {
      from {
        -webkit-transform: rotate(0deg);
      }
      to {
        -webkit-transform: rotate(360deg);
      }
    }

    @keyframes spin {
      from {
        transform: rotate(0deg);
      }
      to {
        transform: rotate(360deg);
      }
    }
  </style>
</head>
<body ng-controller="SpecimenEventController">
  <div id="notifications" class="cp-notifs hidden"></div>
  <div class="container">
    <div class="row">
    <div class="col-xs-7"><h3>Specimen Event Bulk Data Entry </h3></div>
  </div>
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
        <button class="btn btn-primary" id="add" ng-click="addRecord()" ng-show="dataEntryMode != true">
          <span ng-show="loading" class="glyphicon glyphicon-refresh spin"></span>
          <span>Add Record</span>
        </button>
        <button class="btn btn-primary" id="edit" ng-click="editDataTable()" ng-show="editRecords == true">
          <span ng-show="loading" class="glyphicon glyphicon-refresh spin"></span>
          <span>Edit</span>
        </button>
        <button class="btn btn-primary" id="save" ng-click="saveDataTable()" ng-show="dataEntryMode">
          <span ng-show="loading" class="glyphicon glyphicon-refresh spin"></span>
          <span> Save </span>
        </button>
        <button class="btn btn-default" id="cancel" ng-click="cancelDataTable()" ng-show="dataEntryMode">
           Cancel
        </button>
        <button class="btn btn-default" id="delete" ng-click="deleteSelectedRows()" ng-show="deleteRows">
          Delete
        </button>
        <button class="btn btn-primary right" id="Apply First To All" ng-click="applyFirstToAll()" ng-show="dataEntryMode">
          Apply First To All
        </button>
      </div>
      <div id="data-table" class="data-table-container"></div>
    </div>

  <script>
    var dateFormat = <%= "'" + edu.wustl.common.util.global.CommonServiceLocator.getInstance().getDatePattern().toLowerCase()  + "'"%>
    
    function getSpecimenLabels() {
      var query = parent.location.search.substr(1);
      var params = {};
      query.split("&").forEach(function(part) {
        var item = part.split("=");
        params[item[0]] = decodeURIComponent(item[1]);
      });
      return params.specimenLabels;
    }
    
    var globalSpecimenLabels = getSpecimenLabels();
  </script>
  <script src="../js/utility.js" type="text/javascript"></script>
  <script src="../js/directives.js" type="text/javascript"></script>
  <script src="../js/wrapper.js" type="text/javascript"></script>
  <script src="../js/specimenEventBulkDataEntry.js" type="text/javascript"></script>
  <script src="../js/forms-service.js" type="text/javascript"></script>
  <script src="../external/de/js/de-field-manager.js" type="text/javascript"></script>
  <script src="../external/de/js/de-form.js" type="text/javascript"></script>
  <script src="../external/de/js/de-data-table.js" type="text/javascript"></script>
  <script src="../js/de-users.js" type="text/javascript"></script>
</body>
</html>