
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http, ApiUtil) {
    var User = osModel('users');

    User.signup = function(user) {
      return $http.post(User.url() + 'sign-up', user).then(ApiUtil.processResp);
    }

    return User;
  });

