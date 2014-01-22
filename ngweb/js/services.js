
angular.module('plus.services', [])
  .factory('CollectionProtocolService', function($http) {
    var baseUrl = '/catissuecore/rest/collection-protocols/';

    return {
      getCpList: function() {
        return $http.get(baseUrl).then(function(result) { return result.data; });
      },

      getCpForms: function(cpId) {
        return $http.get(baseUrl + cpId + '/forms').then(function(result) { return result.data; });
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
  });
