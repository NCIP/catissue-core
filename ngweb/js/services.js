
angular.module('plus.services', [])
  .factory('CollectionProtocolService', function($http) {
    var baseUrl = '/catissuecore/rest/collection-protocols/';

    return {
      getCpList: function() {
        return $http.get(baseUrl).then(function(result) { return result.data; });
      },

      getCpForms: function(cpId) {
        return $http.get(baseUrl + cpId + '/forms').then(function(result) { return result.data; });
      },

      getQueryForms: function(cpId) {
        return $http.get(baseUrl + cpId + '/query-forms').then(function(result) { return result.data; });
      }
    };
  })

  .factory('FormService', function($http) {
    var baseUrl = '/catissuecore/rest/forms/';

    return {
      getFormFields: function(formId) {
        return $http.get(baseUrl + formId + '/fields').then(function(result) { return result.data; });
      }
    };
  })

  .factory('QueryService', function($http) {
    var baseUrl = '/catissuecore/rest/query/';
    return {
      executeQuery: function(cpId, drivingForm, aql) {
        var req = {cpId: cpId, drivingForm: drivingForm, aql: aql};
        return $http.post(baseUrl, req).then(function(result) { return result.data; });
      }
    };
  });
