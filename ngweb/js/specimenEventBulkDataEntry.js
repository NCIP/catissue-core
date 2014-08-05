specimenEvent = angular.module('specimenEvent-app',['plus.formsServices', 'plus.directives','ui.bootstrap']);

specimenEvent.controller('SpecimenEventController', ['$scope', 'SpecimenEventService','FormsService', function($scope, SpecimenEventService, FormsService){
  $scope.selectedEvent = undefined;

  $scope.specimenLabels = undefined;

  SpecimenEventService.getAllEvents().then( function(events) {
    console.log(events);
    $scope.specimenEvents = events;
  });

  var getSpecimenEventDataUrl = function(formId, specimenLabels) {
    return '/openspecimen/rest/ng/specimen-events/' + formId + '/data/' + specimenLabels;
  }

  $scope.onEventSelect = function(selectedEvent) {
    var specimenLabels = $scope.specimenLabels.split(',');
    FormsService.getFormDef(selectedEvent.formId).then(function (data){
    var that = this;
    var _reqTime = new Date().getTime();
    var tableDataUrl = getSpecimenEventDataUrl(selectedEvent.formId, specimenLabels);
    dataTable = new edu.common.de.DataTable({
      formId           : selectedEvent.formId,
      formContextId    : data.formContextId,
      formDef          : data,
      formDiv          : 'bulk-data-entry',
      tableDataUrl     : tableDataUrl,
      dataTableSaveUrl : '/openspecimen/rest/ng/forms/:formId/data',
      onLoadError      : function() {
        Utility.notify($("#notifications"), "Form Data Loading Failed", "error", true);
      },

      onValidationError: function() {
        Utility.notify($("#notifications"), "There are some errors on form. Please rectify them before saving", "error", true);
      },

      onSaveSuccess: function() {
        Utility.notify($("#notifications"), "Form Data Saved", "success", true);
      },

      onSaveError: function() {
        Utility.notify($("#notifications"), "Form Data Save Failed", "error", true);
      },

    });

      dataTable.render();
    });
  }
}]);

specimenEvent.factory('SpecimenEventService',function($http){

    var apiUrl = '/openspecimen/rest/ng/';
    var baseUrl = apiUrl + 'specimen-events/';

    var getSpecimenEventDataUrl = function(formId, specimenLabels) {
      return baseUrl + formId + '/data/' + specimenLabels;
    }

    var getFormsUrl = function() {
      return apiUrl + '/forms';
    }

    var successfn = function(result){
      console.log(result);
      console.log(result.data);
      return result.data;
    }

    return{
      getAllEvents : function(){
        var params = { formType : 'specimenEvent'};
        return Utility.get($http, getFormsUrl(), successfn, params);
      },

      getEventData :  function(formId, specimenLabels) {
        var url = getSpecimenEventDataUrl(formId, specimenLabels);
        return Utility.get($http, url,successfn);
      }
    }
});
