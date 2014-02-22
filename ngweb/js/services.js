
angular.module('plus.services', [])
  .factory('QueryService', function($http) {
    var baseUrl = '/catissuecore/rest/ng/query/';
    return {
      executeQuery: function(drivingForm, aql) {
        var req = {drivingForm: drivingForm, aql: aql};
        return $http.post(baseUrl, req).then(function(result) { return result.data; });
      }
    };
  });
