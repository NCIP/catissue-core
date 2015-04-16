angular.module('openspecimen')
  .factory('AuthorizationService', function($rootScope, $http, User, ApiUtil, ApiUrls) {
    var userRights = [];

    return {
      initializeUserRights: function() {
        return User.getCurrentUserRoles().then(
          function(userRoles) {
            userRights = [];
            angular.forEach(userRoles, function(userRole) {
              var site = userRole.site ? userRole.site.name : null;
              var cp = userRole.collectionProtocol ? userRole.collectionProtocol.shortTitle : null;
              angular.forEach(userRole.role.acl, function(ac) {
                userRights.push({
                  site: site,
                  cp: cp,
                  resource: ac.resourceName,
                  operations: ac.operations.map(function(op) { return op.operationName; } )
                });
              });
            });
            return userRights;
          }
        );
      },

      isAllowed: function(opts) {
        if ($rootScope.currentUser.admin) {
          return true;
        }

        var allowed = false;
        for (var i = 0; i < userRights.length; i++) {
          if (!opts.sites && !opts.cp && userRights[i].resource == opts.resource) {
            //
            // For resources whose rights are independent of CP and/or Site
            //
            allowed = userRights[i].operations.indexOf(opts.operation) != -1;

          } else if ((!userRights[i].site || (opts.sites && opts.sites.indexOf(userRights[i].site) != -1)) &&
                    (!userRights[i].cp || userRights[i].cp == opts.cp) &&
                    (userRights[i].resource == opts.resource)) {
            //
            // For resources whose rights are specified based on CP and/or Site
            //
            allowed = userRights[i].operations.indexOf(opts.operation) != -1;
          }

          if (allowed) {
            break;
          }
        }

        return allowed;
      }
    }
  });