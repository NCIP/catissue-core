
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel) {
    var User = osModel('users');
    
    User.prototype.newPermission = function() {
      return {site:'', cp:'', roleName:''};
    }
    
    User.prototype.addPermission = function (userCPRole) {
      if(!this.userCPRoles) {
        this.userCPRoles = [];
      }
      
      this.userCPRoles.push(userCPRole);
    }
    
    User.prototype.removePermission = function(userCPRole) {
      var idx = this.userCPRoles ? this.userCPRoles.indexOf(userCPRole) : -1;
      if(idx != -1) {
        this.userCPRoles.splice(idx, 1);
      }

      return idx;
    };
    
    return User;
  });

