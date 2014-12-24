
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http) {
    var User = osModel('users');

    return User;
  });

