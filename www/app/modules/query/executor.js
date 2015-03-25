
angular.module('os.query.executor', [])
  .factory('QueryExecutor', function($http, ApiUrls) {
    var queryUrl = ApiUrls.getBaseUrl() + 'query';

    return {
      getCount: function(queryId, cpId, aql) {
        var req = {
          savedQueryId: queryId, 
          cpId: cpId, 
          drivingForm: 'Participant',
          runType: 'Count',
          aql: aql
        };

        return $http.post(queryUrl, req).then(
          function(resp) {
            var data = resp.data;

            var result = {cprCnt: 0, specimenCnt: 0};
            result.cprCnt  = data.rows[0][0];
            for (var i = 1; i < data.rows[0].length; ++i) {
              result.specimenCnt += parseInt(data.rows[0][i]);
            }
            return result;
          }
        );
      }
    };
  });
