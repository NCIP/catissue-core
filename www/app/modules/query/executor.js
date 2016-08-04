
angular.module('os.query.executor', [])
  .factory('QueryExecutor', function($http, $document, ApiUrls) {
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
      },

      getRecords: function(queryId, cpId, aql, wideRowMode) {
        var req = {
          savedQueryId: queryId, 
          cpId: cpId,
          drivingForm: 'Participant',
          runType: 'Data', 
          aql: aql, 
          wideRowMode: wideRowMode || "OFF"
        };
        return $http.post(queryUrl, req).then(
          function(resp) {
            return resp.data;
          }
        );
      },

      exportQueryResultsData: function(queryId, cpId, aql, wideRowMode) {
        var req = {
          savedQueryId: queryId,
          cpId: cpId,
          drivingForm: 'Participant',
          runType: 'Export',
          aql: aql,
          indexOf: 'Specimen.label',
          wideRowMode: wideRowMode || "OFF"
        };

        return $http.post(queryUrl + '/export', req).then(
          function(resp) {
            return resp.data;
          }
        );
      },

      downloadDataFile: function(fileId, filename) {
        filename = !!filename ? filename : 'QueryResults.csv';
        var link = angular.element('<a/>').attr(
          {
            href: queryUrl + '/export?fileId=' + fileId + '&filename=' + filename,
            target: '_blank'
          }
        );

        angular.element($document[0].body).append(link);

        if (typeof link[0].click == "function") {
          link[0].click();
        } else { // Safari fix
          var dispatch = document.createEvent("HTMLEvents");
          dispatch.initEvent("click", true, true);
          link[0].dispatchEvent(dispatch);
        }

        link.remove();
      },

      getFacetValues: function(cpId, facetExprs, searchTerm) {
        var params = {cpId: cpId, facet: facetExprs, searchTerm: searchTerm};
        return $http.get(queryUrl + '/facet-values', {params: params}).then(
          function(result) {
            return result.data;
          }
        );
      }
    };
  });
