
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="ngweb/css/app.css">
    <link rel="stylesheet" href="ngweb/external/jquery/css/chosen.min.css">
    <link rel="stylesheet" href="ngweb/external/jquery/css/chosen-sprite.png">
    <link rel="stylesheet" href="ngweb/external/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="ngweb/external/bootstrap/css/bootstrap-responsive.min.css">
    <link rel="stylesheet" href="ngweb/external/eternicode/css/datepicker.css">
    <link rel="stylesheet" href="ngweb/external/de/css/de.css">

    <script type="text/javascript" src="ngweb/external/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="ngweb/external/jquery/chosen.jquery.min.js"></script>
    <script type="text/javascript" src="ngweb/external/underscorejs/underscore-min.js"></script>
    <script type="text/javascript" src="ngweb/external/backbonejs/backbone-min.js"></script>
    <script type="text/javascript" src="ngweb/external/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="ngweb/external/bootstrap/js/bootstrap-theme.min.css"></script>
    <script type="text/javascript" src="ngweb/external/bootstrap/js/bootbox.min.js"></script>
    <script type="text/javascript" src="ngweb/external/eternicode/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="ngweb/external/de/js/de-form.js"></script>

    <script>
      _.templateSettings = {
        interpolate: /\{\{(.+?)\}\}/gim,
        evaluate: /\{\{(.+?)\}\}/gim,
        escape: /\{\{\-(.+?)\}\}/gim
      };
    </script>
  </head>
  <body>
    <div id="notifications" class="cp-notifs hidden"></div>
    <div id="forms-list" class="panel panel-default">
      <div class="panel-heading">
        <span>Forms</span>
      </div>
      <div class="panel-body">
        <div class="row" style="border:none;">
          <div class="col-xs-1">ID</div>
          <div class="col-xs-5">Caption</div>
          <div class="col-xs-2">Record Count</div>
          <div class="col-xs-2">Action</div>
        </div>
        <hr class="cp-padded-hr"></hr>
        <div id="forms-summary">
        </div>
      </div>
    </div>

    <div id="form-data-summary" class="panel panel-default hidden">
      <div class="panel-heading">
      </div>
      <div class="panel-body">
      
      </div>
    </div>

    <div id="form-view" class="cp-de-form hidden">
    </div>

    <script type="text/template" class="template_form_summary">
      <div id="form-{{- id }}" class="form-summary">
        <div class="row" style="border:none;">
          <div class="col-xs-1">{{ id }}</div> 
          <div class="col-xs-5">{{ caption }}</div>
          <div class="col-xs-2">{{ noOfRecords }}</div>
          <div class="col-xs-2">
            <button type="button" class="btn btn-default btn-xs add-record">
              <span class="glyphicon glyphicon-plus"></span>
            </button>
            <button type="button" class="btn btn-default btn-xs edit-record">
              <span class="glyphicon glyphicon-pencil"></span>
            </button>
          </div>
        </div>
        <hr class="cp-padded-hr"></hr>
      </div>
    </script>

    <script type="text/template" class="template_data_summary_panel_heading">
      <span>{{ caption }} Records</span>
      <button id="addFormRecord" class="btn btn-default btn-sm cp-round-button-icon"> 
        <span class="glyphicon glyphicon-plus"></span>
      </button>
    </script>

    <script type="text/template" class="template_data_summary_table_heading">
      <div class="row" style="border:none;">
        <div class="col-xs-2">Record ID</div>
        <div class="col-xs-3">Updated By</div>
        <div class="col-xs-3">Update Time</div>
      </div>
      <hr class="cp-padded-hr"></hr>
    </script>

    <script type="text/template" class="template_data_summary_table_row">
      <div class="row" style="border:none;">
        <div class="col-xs-2">{{ id }}</div>
        <div class="col-xs-3">{{ updatedBy }}</div>
        <!-- div class="col-xs-3">{{ print(Utility.formatDate(updateTime)) }}</div -->
        <div class="col-xs-3">{{ updateTime }}</div>
      </div>
      <hr class="cp-padded-hr"></hr>
    </script>


    <script type="text/javascript" src="ngweb/js/utility.js"></script>
    <script type="text/javascript" src="ngweb/js/views.js"></script>
    <script type="text/javascript" src="ngweb/js/models.js"></script>

    <script>
      var entity = "<%= request.getAttribute("entityType") %>"
      var entityObjId   = <%= request.getAttribute("entityRecId") %>
      $(document).ready(function() {
        formViewMgr = new FormViewManager({entity: entity, entityObjId: entityObjId});
        router = new FormRouter();
        Backbone.history.start();
      });
    </script>
  </body>
</html>
