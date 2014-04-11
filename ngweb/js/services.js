
angular.module('plus.services', [])
  .factory('QueryService', function($http) {
    var baseUrl = '/catissuecore/rest/ng/query/';

    var savedQueriesUrl = '/catissuecore/rest/ng/saved-queries/';

    var successfn = function(result) { return result.data; };

    return {
      executeQuery: function(drivingForm, aql, wideRows) {
        var req = {drivingForm: drivingForm, aql: aql, wideRows: wideRows};
        return $http.post(baseUrl, req).then(successfn);
      },

      saveOrUpdateQuery: function(queryDef) {
        if (queryDef.id) {
          return $http.put(savedQueriesUrl + queryDef.id, queryDef).then(successfn);
        } else {
          return $http.post(savedQueriesUrl, queryDef).then(successfn);
        }
      },

      getQueries: function(startAt, maxRecs) {
        return $http.get(savedQueriesUrl + '?start=' + startAt + '&max=' + maxRecs).then(successfn);
      },

      getQuery: function(queryId) {
        return $http.get(savedQueriesUrl + queryId).then(successfn);
      }
    };
  });
