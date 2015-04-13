angular.module('openspecimen')
  .factory('AuthorizationService', function($rootScope, $http, ApiUtil, ApiUrls) {
     var userPermissions = [];

     var getSubjectRoleUrl = function(userId) {
       var url = 'rbac/subjects/' + userId + '/roles';
       return ApiUrls.getBaseUrl() + url;
     }

     return {
       initUserPermissions: function(userId) {
         return $http.get(getSubjectRoleUrl(userId)).then(function(result) {
           userPermissions = [];
           angular.forEach(result.data, function(userRole) {
             var site = userRole.site ? userRole.site.name : null;
             var cp = userRole.collectionProtocol ? userRole.collectionProtocol.shortTitle : null;
             angular.forEach(userRole.role.acl, function(ac) {
               var userPermission = {};
               userPermission.site = site;
               userPermission.cp = cp;
               userPermission.resource = ac.resourceName;
               var operations = ac.operations.map(function(op) {
                 return op.operationName;
               });
               userPermission.operations = operations;
               userPermissions.push(userPermission);
             });
           });
         });
       },

       hasPermission: function(resource, permission, site, cp) {
         if ($rootScope.currentUser.admin) {
           return true;
         }

         for (var i = 0; i < userPermissions.length; i++) {
           if ((!userPermissions[i].site || userPermissions[i].site == site)
             && (!userPermissions[i].cp || userPermissions[i].cp == cp)
             && userPermissions[i].resource == resource) {
             return userPermissions[i].operations.indexOf(permission) != -1;
           }
         }

         return false;
       }
     }
  });