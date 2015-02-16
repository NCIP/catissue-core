
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http, ApiUtil) {
    var User = osModel('users');

    User.sendPasswordResetLink = function(user) {
      return $http.post(User.url() + 'forgot-password-token', user).then(ApiUtil.processResp);
    }

    User.resetPassword = function(passwordDetail) {
      return $http.post(User.url() + "reset-password", passwordDetail).then(ApiUtil.processResp);
    }

    return User;
  });

