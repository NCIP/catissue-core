specimenEvent = angular.module('specimen-event-app',['plus.formsServices', 'plus.directives','ui.bootstrap']);

specimenEvent.controller('SpecimenEventController', ['$scope', 'SpecimensEventService', 'SpecimensService', 'FormsService', function($scope, SpecimensEventService, SpecimensService, FormsService){

  function getSpecimenLabels() {
    var query = parent.location.search.substr(1);
    var params = {};
    query.split("&").forEach(function(part) {
      var item = part.split("=");
      params[item[0]] = decodeURIComponent(item[1]);
    });
    return params.specimenLabels;
  }

  $scope.selectedEvent = undefined;
  $scope.specimenLabels = getSpecimenLabels();
  $scope.specimensSummary = {};

  var dataTable = undefined;

  FormsService.getAllSpecimenEventForms().then( function(events) {
    $scope.specimenEvents = events;
  });

  $scope.onEventSelect = function(selectedEvent) {
    $scope.dataEntryMode = $scope.editRecords  = $scope.deleteRows = false;
    FormsService.getFormDef(selectedEvent.formId).then(function (data){
      var formDef = data;
      var that = this;
      dataTable = new edu.common.de.DataTable({
        formId           : selectedEvent.formId,
        idColumnLabel    : 'Specimen Label',
        appColumns       : [{id: 'cpName',label: 'Collection Protocol'}, {id: 'specimenType', label:'Specimen Type'}],
        formDef          : formDef,
        formDiv          : 'data-table',
        onValidationError: function() {
          Utility.notify($("#notifications"), "There are some errors on form. Please rectify them before saving", "error", true);
        },

        onRowSelect: function(isRowSelected) {
          $scope.deleteRows = isRowSelected;
          $scope.$apply();
        }
      });
      dataTable.clear();
    });
  }

  $scope.getSpecimenSummary = function(specimenLabel) {
    for(var i = 0; i< $scope.specimensSummary.length; i++ ) {
      if($scope.specimensSummary[i].label == specimenLabel) {
        return $scope.specimensSummary[i];
      }
    }
  }

  $scope.addRecord = function() {
    $("#notifications").hide();
    $scope.loading = true;
    var re = /\s*[\s,]\s*/;
    var specimenLabels = $scope.specimenLabels.trim().split(re);
    var tableData =[];

    SpecimensService.getSpecimens(specimenLabels).then( function(data) {
      $scope.loading = false;
      $scope.specimensSummary = data.specimens;
      if(validate(specimenLabels, data.specimens)) {
        for(var i=0; i < specimenLabels.length; i++) {
          var specimenSummary = $scope.getSpecimenSummary(specimenLabels[i]);
          var tableRec = {
            key            : {id : specimenSummary.label , label : specimenSummary.label},
            appColumnsData : { cpName: specimenSummary.cpShortTitle, specimenType: specimenSummary.specimenType },
            records        : []
          };
          tableData.push(tableRec);
        }

        $scope.dataEntryMode = true;
        $scope.editRecords = false;
        dataTable.setMode($scope.dataEntryMode == true ? 'add' : 'view');
        renderDataTable(tableData);
      }
    });
  };

  $scope.deleteSelectedRows = function() {
     dataTable.deleteRows();
     $scope.deleteRows = false;
  };

  var validate = function(specimenLabels, data) {
    var validSpecimens = [];
    for(var i = 0; i< data.length; i++ ) {
      if($.inArray(data[i].label, specimenLabels) != -1) {
        validSpecimens.push(data[i].label);
      }
    }
    var invalidSpecimens = $.grep(specimenLabels, function(el){return $.inArray(el, validSpecimens) == -1});

    if(invalidSpecimens.length > 0) {
      var errorMsg = "Specimen labels " + invalidSpecimens  + " does not exist or don't have access.";
      Utility.notify($("#notifications"), errorMsg, "error", false);
      return false;
    }
    return true;
  }

  $scope.saveDataTable = function() {
    $scope.loading = true;
    var formData = JSON.stringify(dataTable.getData());
    if(formData == undefined || formData == null) {
       $scope.loading = false;
       return;
    }
    SpecimensEventService.saveFormData($scope.selectedEvent.formId, JSON.stringify(formData)).then(function(data){
      $scope.loading = false;
      Utility.notify($("#notifications"), "Form Data Saved", "success", true);

      var obj = jQuery.parseJSON(data);
      var savedFormData = eval(obj);
      var tableData = populateTableData(savedFormData);

      $scope.dataEntryMode = false;
      $scope.editRecords = true;
      dataTable.setMode('view');
      renderDataTable(tableData);
    },
    function(data) {
      $scope.loading = false;
      Utility.notify($("#notifications"), "Form Data Save Failed.", "error", true);
    }
    );
  }

  $scope.editDataTable = function() {
    $scope.loading = true;
    var tableData = populateTableData(dataTable.getData());
    $scope.dataEntryMode = true;
    $scope.editRecords = false;
    dataTable.setMode('edit');
    renderDataTable(tableData);
    $scope.loading = false;
  }

  $scope.cancelDataTable = function() {
    dataTable.clear();
    $scope.dataEntryMode = false;
  }

  $scope.applyFirstToAll = function() {
    dataTable.copyFirstToAll();
  }

  var populateTableData = function(tableData) {
    var tblData = [];
    for(var i = 0; i< tableData.length; i++) {
      var specimenLabel = tableData[i].appData.id;
      var specimenSummary = $scope.getSpecimenSummary(specimenLabel);
      var records = [];
      records.push(tableData[i]);
      var tableRec = {
        key            : {id : specimenLabel , label : specimenLabel},
        appColumnsData : { cpName: specimenSummary.cpShortTitle, specimenType: specimenSummary.specimenType },
        records        : records
      };
      tblData.push(tableRec);
    }
    return tblData;
  }

  var renderDataTable = function(tableData) {
    dataTable.setData(tableData);
  }
}]);


specimenEvent.factory('SpecimensEventService',function($http){

    var apiUrl = '/openspecimen/rest/ng/';
    var baseUrl = apiUrl + 'specimen-events/';

    var successfn = function(result){
      return result.data;
    }

    return{
      saveFormData : function(formId, formDataJson) {
        var url = baseUrl + "/" + formId + "/data/";
        ret = $http.post(url, formDataJson).then(successfn);
        return ret;
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
    getSpecimens : function(labels) {
      var params = {
        label : labels,
    	'_reqTime' : new Date().getTime()
      };
      return Utility.get($http, baseUrl, successfn, params);
    }
  }
});