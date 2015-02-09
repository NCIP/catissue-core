
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http, ApiUtil) {
    var User = osModel('users');

     User.sendPasswordResetLink = function(user) {
        return $http.post(User.url() + 'forgot-password-token', user).then(ApiUtil.processResp);
      }

     User.resetPassword = function(passowrdDetail) {
        return $http.post(User.url() + "reset-password", passowrdDetail).then(ApiUtil.processResp);
      }

    return User;
  });

