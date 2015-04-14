angular.module('openspecimen')
  .factory('AuthorizationService', function($rootScope, $http, User, ApiUtil, ApiUrls) {
     var userRights = [];

     return {
       init: function() {
         return User.getCurrentUserRoles().then(function(result) {
           userRights = [];
           angular.forEach(result, function(userRole) {
             var site = userRole.site ? userRole.site.name : null;
             var cp = userRole.collectionProtocol ? userRole.collectionProtocol.shortTitle : null;
             angular.forEach(userRole.role.acl, function(ac) {
               var userRight = {};
               userRight.site = site;
               userRight.cp = cp;
               userRight.resource = ac.resourceName;
               var operations = ac.operations.map(function(op) {
                 return op.operationName;
               });
               userRight.operations = operations;
               userRights.push(userRight);
             });
           });
         });
       },

       hasRights: function(opts) {
         if ($rootScope.currentUser.admin) {
           return true;
         }

         for (var i = 0; i < userRights.length; i++) {
           if ((!userRights[i].site || userRights[i].site == opts.site || !opts.site)
             && (!userRights[i].cp || userRights[i].cp == opts.cp || !opts.cp)
             && userRights[i].resource == opts.resource) {
             if (userRights[i].operations.indexOf(opts.operation) != -1) {
               return true;
             }
           }
         }

         return false;
       }
     }
  });