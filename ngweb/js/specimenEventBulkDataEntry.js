var specimenEvent = angular.module('specimen-event-app', ['plus.formsServices', 'plus.directives', 'ui.bootstrap']);

specimenEvent.controller('SpecimenEventController', 
  function($scope, SpecimensEventService, SpecimensService, FormsService){

    $scope.selectedEvent = undefined;
    $scope.specimenLabels = globalSpecimenLabels || [];
    $scope.specimensSummary = {};

    $scope.dataTable = undefined;

    $scope.specimenEvents = [];
    FormsService.getAllSpecimenEventForms().then( 
      function(events) {
        if (!events) {
          return;
        }

        for (var i = 0; i < events.length; ++i) {
          if (events[i].sysForm) {
            events.splice(i, 1);
          }
        }

        $scope.specimenEvents = events;
      }
    );

    var renderDataTable = function(formId, formDef) {
      var dataTable = new edu.common.de.DataTable({
        formId           : formId,
        idColumnLabel    : 'Specimen Label',
        appColumns       : [{id: 'cpName', label: 'Collection Protocol'}, 
                            {id: 'specimenType', label:'Specimen Type'}],
        formDef          : formDef,
        formDiv          : 'data-table',
        onValidationError: function() {
          Utility.notify($("#notifications"), "There are some errors in below data table. Please rectify them before saving", "error", true);
        },

        onRowSelect: function(isRowSelected) {
          $scope.deleteRows = isRowSelected;
          if (!$scope.$$phase) {
            $scope.$apply();
          }
        }
      });

      dataTable.clear();
      return dataTable;
    };

    $scope.onEventSelect = function(selectedEvent) {
      $scope.dataEntryMode = $scope.editRecords  = $scope.deleteRows = false;
      $scope.dataTable = undefined;
      FormsService.getFormDef(selectedEvent.formId).then(function (formDef) {
        $scope.dataTable = renderDataTable(selectedEvent.formId, formDef);
      });
    };

    $scope.addRecord = function() {
      $("#notifications").hide();
      $scope.loading = true;

      var specimenLabels = [];
      var re = /\n*\t*\v*[\n\t\v,]\n*\t*\v*/;
      $.each($scope.specimenLabels.trim().split(re), function(){
        if(this != "") {
           specimenLabels.push($.trim(this));
        }
      });

      SpecimensService.getSpecimens(specimenLabels).then( function(data) {
        $scope.loading = false;
        $scope.specimensSummary = data.specimens;
        if (!validate(specimenLabels, data.specimens)) {
          return;
        }

        $scope.specimenMap = {};
        for (var i = 0; i < data.specimens.length; ++i) {
          var specimen = data.specimens[i];
          $scope.specimenMap[specimen.label] = specimen;
        }

        var tableData =[];
        for (var i = 0; i < specimenLabels.length; i++) {
          var specimen = $scope.specimenMap[specimenLabels[i]];
          var tableRec = {
            key            : {id : specimen.label, label : specimen.label},
            appColumnsData : {cpName: specimen.cpShortTitle, specimenType: specimen.specimenType },
            records        : []
          };

          tableData.push(tableRec);
        }

        $scope.dataEntryMode = true;
        $scope.editRecords = false; // TODO: why so many ctrl variables?
        $scope.dataTable.setMode('add');
        $scope.dataTable.setData(tableData);
      });
    };

    $scope.deleteSelectedRows = function() {
      $scope.dataTable.deleteRows();
      $scope.deleteRows = false;
    };

    var validate = function(specimenLabels, data) {
      if (specimenLabels.length == data.length) {
        return true;
      }

      var validLabels = [];
      for (var i = 0; i < data.length; i++) {
        validLabels.push(data[i].label);
      }

      var invalidSpecimens = $.grep(
        specimenLabels, 
        function(el) {
          return $.inArray(el, validLabels) == -1
        }
      );

      if (invalidSpecimens.length > 0) {
        var errorMsg = "Specimen labels " + invalidSpecimens  + " either do not exist or you don't have access.";
        Utility.notify($("#notifications"), errorMsg, "error", false);
        return false;
      }

      return true;
    };

    $scope.saveDataTable = function() {
      $scope.loading = true;
      var formData = JSON.stringify($scope.dataTable.getData()); // TODO: Doesn't look correct
      if (formData == undefined || formData == null) {
        $scope.loading = false;
        return;
      }

      SpecimensEventService.saveFormData($scope.selectedEvent.formId, JSON.stringify(formData)).then(
        function(data) {
          $scope.loading = false;
          Utility.notify($("#notifications"), "Form Data Saved", "success", true);

          var obj = jQuery.parseJSON(data);
          var savedFormData = eval(obj);
          var tableData = populateTableData(savedFormData);

          $scope.dataEntryMode = false;
          $scope.editRecords = true;
          $scope.dataTable.setMode('view');
          $scope.dataTable.setData(tableData); // TODO: Why should i call this again?
        },

        function(data) {
          $scope.loading = false;
          Utility.notify($("#notifications"), "Form Data Save Failed.", "error", true);
        }
      );
    }

    $scope.editDataTable = function() {
      var tableData = populateTableData($scope.dataTable.getData());
      $scope.dataEntryMode = true;
      $scope.editRecords = false;
      $scope.dataTable.setMode('edit');
      $scope.dataTable.setData(tableData);
    };

    $scope.cancelDataTable = function() {
      $scope.dataTable.clear();
      $scope.dataEntryMode = false;
    };

    $scope.applyFirstToAll = function() {
      $scope.dataTable.copyFirstToAll();
    };

    var populateTableData = function(tableData) {
      var tblData = [];
      for (var i = 0; i < tableData.length; i++) {
        var specimenLabel = tableData[i].appData.id;
        var specimen = $scope.specimenMap[specimenLabel];
        var tableRec = {
          key            : {id : specimenLabel , label : specimenLabel},
          appColumnsData : { cpName: specimen.cpShortTitle, specimenType: specimen.specimenType},
          records        : [tableData[i]]
        };
        tblData.push(tableRec);
      }

      return tblData;
    };
  }
);


specimenEvent.factory('SpecimensEventService',function($http) {
  var apiUrl = '/openspecimen/rest/ng/';
  var baseUrl = apiUrl + 'specimen-events/';
  var successfn = function(result){
    return result.data;
  };

  return{
    saveFormData: function(formId, formDataJson) {
      var url = baseUrl + "/" + formId + "/data/";
      return $http.post(url, formDataJson).then(successfn);
    }
  }
});

specimenEvent.factory('SpecimensService', function($http) {
  var apiUrl = '/openspecimen/rest/ng/';
  var baseUrl = apiUrl + 'specimens/';
  var successfn = function(result){
    return result.data;
  }

  return {
    getSpecimens: function(labels) {
      return Utility.get($http, baseUrl, successfn, {label : labels});
    }
  }
});
