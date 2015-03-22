
angular.module('os.query.models.queryfolder', ['os.common.models'])
  .factory('QueryFolder', function($http, osModel, SavedQuery) {
    var QueryFolder = osModel('query-folders');

    QueryFolder.prototype.getQueries = function(countReq) {
      var queryList = {count: 0, queries: []};
      var params = {countReq: countReq};

      $http.get(QueryFolder.url() + this.$id() + '/saved-queries', {params: params}).then(
        function(resp) {
          queryList.count = resp.data.count;
          queryList.queries = resp.data.queries.map(
            function(query) {
              return new SavedQuery(query);
            }
          );
        }
      );
       
      return queryList;
    };

    return QueryFolder;
  });
