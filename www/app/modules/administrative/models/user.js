
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel) {
    var User = osModel('users');

    /* use this in permission
    User.getPermissions = function(userId) {
      return [
        {site: 'In Transit', cp: 'CP1', roleName: 'Admin', id:1},
      ];
    }*/
    
    return User;
  });

