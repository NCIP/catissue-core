
angular.module('openspecimen')
  .factory('UserService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('users');
    };

    return {
      getUsers: function() {
        return $http.get(url()).then(ApiUtil.processResp, ApiUtil.processResp);
      }
    };
  });
