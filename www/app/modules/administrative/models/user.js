
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel) {
    var User = osModel('users');

    return User;
  });

