
angular.module('os.administrative.models.user', ['os.common.models'])
  .factory('User', function(osModel, $http, $q, $translate, ApiUtil) {
    var User =
      osModel(
        'users',
        function(user) {
          user.roleModel =  osModel('users/' + user.$id() + '/roles');
        }
      );

    User.prototype.getRoles = function() {
      return {};
    }

    User.prototype.newRole = function(role) {
      return new this.roleModel(role);
    }

    var id = 1;

    User.prototype.saveOrUpdateRole = function(role) {
      //TODO: REST Call to save or update
      // Temporary Code will be deleted after backend implementation done.
      // Here Assumes rest call will return role object with values null for 'All'
      // Also send null instead of all.
       var all = $translate.instant('user.role.all');
      var userRole = angular.copy(role);
      if (userRole.site == all) {
        delete userRole.site;
      }
      if (userRole.cp == all) {
        delete userRole.cp;
      }

      if (!role.id) {
        userRole.id = id++; // Temporary id given to saved roles
      }

      var d = $q.defer();
      d.resolve(userRole);
      return d.promise;
    }

    User.sendPasswordResetLink = function(user) {
      return $http.post(User.url() + 'forgot-password', user).then(ApiUtil.processResp);
    }

    User.resetPassword = function(passwordDetail) {
      return $http.post(User.url() + "reset-password", passwordDetail).then(ApiUtil.processResp);
    }

    User.getCurrentUser = function() {
      return $http.get(User.url() + 'current-user').then(User.modelRespTransform);
    }

    User.signup = function(user) {
      return $http.post(User.url() + 'sign-up', user).then(ApiUtil.processResp);
    }

    return User;
  });

