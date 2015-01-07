
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel) {
    var User = osModel('users');
    
    User.prototype.newPermission = function() {
      return {site:'', cp:'', role:''};
    }
    
    User.prototype.addPermission = function (userCPRole) {
      if(!this.userCPRoles) {
        this.userCPRoles = [];
      }
      
      this.userCPRoles.push(userCPRole);
    }
    
    User.prototype.removePermission = function(index) {
      this.userCPRoles.splice(index, 1);
    };
    
    return User;
  });

