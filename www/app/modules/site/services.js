
angular.module('openspecimen')
  .factory('SiteService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('sites');
    };

    return {
      getSites: function() {
        return $http.get(url()).then(ApiUtil.processResp);
      }
    };
  });
