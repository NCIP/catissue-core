specimenEvent = angular.module('specimenEvent-app',['plus.formsServices', 'plus.directives','ui.bootstrap']);

specimenEvent.controller('SpecimenEventController', ['$scope', 'SpecimenEventService','FormsService', function($scope, SpecimenEventService, FormsService){
  $scope.selectedEvent = undefined;
  $scope.specimenLabels = undefined;
  $scope.isApplyFirstToAll = false;

  var dataTable = undefined;

  FormsService.getAllCPForms('specimenEvent').then( function(events) {
    $scope.specimenEvents = events;
  });

  $scope.onEventSelect = function(selectedEvent) {
    $('#data-table').empty();
    $scope.dataEntryMode = false;
    $scope.isApplyFirstToAll = false;
    var specimenLabels = $scope.specimenLabels.split(',');
    FormsService.getFormDef(selectedEvent.formId).then(function (data){
      var formDef = data;
      var appData =
      dataTable = new edu.common.de.DataTable({
        formId           : selectedEvent.formId,
        appData          : {formCtxtId : $scope.selectedEvent.formCtxtId},
        idColumnLabel    : 'Specimen Label',
        formDef          : formDef,
        formDiv          : 'data-table',
        onValidationError: function() {
          Utility.notify($("#notifications"), "There are some errors on form. Please rectify them before saving", "error", true);
        }
      });
    });
  }

  $scope.addRecord = function() {
    var specimenLabels = $scope.specimenLabels.split(',');
    var tableData =[];
    SpecimenEventService.getSpecimensSummary(specimenLabels).then( function(data) {
      for(var i =0 ;i < data.length; i++) {
        var tableRowJson = {label:data[i].label, objectId: data[i].id, formRecords:[] };
        tableData.push(tableRowJson);
      }
        $scope.dataEntryMode = true;
        $scope.isApplyFirstToAll = false;
        renderDataTable('add', tableData);
    });
  };

  $scope.saveDataTable = function() {
    var formData = JSON.stringify(dataTable.getData());
    FormsService.saveFormData($scope.selectedEvent.formId,null,JSON.stringify(formData)).then(function(data){
      Utility.notify($("#notifications"), "Form Data Saved", "success", true);
      showAllRecords('view');
    },
    function(data) {
     Utility.notify($("#notifications"), "Form Data Save Failed.Please Check whether form is multi record or not.", "error", true);
    }
    );
  }

  $scope.editDataTable = function() {
    showAllRecords('edit');
  }

  $scope.cancelDataTable = function() {
    showAllRecords('view');
  }

  $scope.applyFirstToAll = function() {
       dataTable.copyFirstToAll();
  }

  var showAllRecords = function(mode) {
    $scope.dataEntryMode = (mode == 'view') ? false : true;
    $scope.isApplyFirstToAll = false;
    var specimenLabels = $scope.specimenLabels.split(',');
    SpecimenEventService.getEventData($scope.selectedEvent.formId, specimenLabels).then( function(data) {
      renderDataTable(mode, data.specimenEventFormDataList);
    });
  }

  var renderDataTable = function(mode, tableData) {
    dataTable.setMode(mode);
    dataTable.setData(tableData);
  }

}]);


specimenEvent.factory('SpecimenEventService',function($http){

    var apiUrl = '/openspecimen/rest/ng/';
    var baseUrl = apiUrl + 'specimen-events/';

    var getSpecimenEventDataUrl = function(formId, specimenLabels) {
      return baseUrl + formId + '/data/' + specimenLabels;
    }

    var successfn = function(result){
      return result.data;
    }

    return{
      getEventData :  function(formId, specimenLabels) {
        var url = getSpecimenEventDataUrl(formId, specimenLabels);
        return Utility.get($http, url,successfn);
      },

      getSpecimensSummary: function(specimenLabelsArray) {
        var params = { specimenLabels : specimenLabelsArray};
        var url = apiUrl + 'specimens'
        return Utility.get($http, url,successfn,params);
      }
    }
});
