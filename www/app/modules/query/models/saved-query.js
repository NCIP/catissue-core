
angular.module('os.query.models.savedquery', ['os.common.models'])
  .factory('SavedQuery', function($http, osModel) {
    var SavedQuery = osModel('saved-queries');

    SavedQuery.list = function() {
      var result = {count: 0, queries: []};
      
      $http.get(SavedQuery.url(), {countReq: true}).then(
        function(resp) {
          result.count = resp.data.count;
          result.queries = resp.data.queries.map(
            function(query) {
              return new SavedQuery(query);
            }
          );         
        }
      );

      return result;
    }

    SavedQuery.prototype.getQueryDefUrl = function() {
      return SavedQuery.url() + this.$id() + '/definition-file';
    }

    return SavedQuery;
  });
