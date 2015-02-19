
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http, ApiUtil) {
    var User = osModel('users');

    User.sendPasswordResetLink = function(user) {
      return $http.post(User.url() + 'forgot-password', user).then(ApiUtil.processResp);
    }

    User.resetPassword = function(passwordDetail) {
      return $http.post(User.url() + "reset-password", passwordDetail).then(ApiUtil.processResp);
    }

    User.getCurrentUser = function() {
      return $http.get(User.url() + 'current-user').then(function(result) {return result.data;});
    }

    return User;
  });

