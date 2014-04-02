
angular.module('plus.services', [])
  .factory('QueryService', function($http) {
    var baseUrl = '/catissuecore/rest/ng/query/';
    return {
      executeQuery: function(drivingForm, aql, wideRows) {
        var req = {drivingForm: drivingForm, aql: aql, wideRows: wideRows};
        return $http.post(baseUrl, req).then(function(result) { return result.data; });
      }
    };
  });
