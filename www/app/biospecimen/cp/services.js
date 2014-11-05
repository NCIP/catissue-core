
angular.module('openspecimen')
  .factory('CollectionProtocolService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('collection-protocols');
    };

    return {   
      getCpList: function(detailed) {
        var params = {};
        if (detailed) {
          params = {detailedList: detailed};
        }

        return $http.get(url(), {params: params}).then(ApiUtil.processResp);
      }
    }
  });
