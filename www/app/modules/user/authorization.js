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

        for (var i = 0; i < userRights.length; i++) {
          /**
           * If condition - Entities whose rights are not depends on site and cp for those,
           * check only resource and operation name is matching.
           * Else condition - Entities whose rights are depends on site and cp for those,
           * check site, cp and resource match. site and cp null means rights on all sites and cps.
           */
          if (!opts.site && !opts.cp && userRights[i].resource == opts.resource) {
            if (userRights[i].operations.indexOf(opts.operation) != -1) {
              return true;
            }
          } else if ((!userRights[i].site || userRights[i].site == opts.site)
            && (!userRights[i].cp || userRights[i].cp == opts.cp)
            && userRights[i].resource == opts.resource) {
            return userRights[i].operations.indexOf(opts.operation) != -1;
          }
        }

        return false;
      }
    }
  });