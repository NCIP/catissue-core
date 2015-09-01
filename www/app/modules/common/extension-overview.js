angular.module("openspecimen")
  .directive("osExtensionOverview", function() {
     return {
       restrict: 'A',

       templateUrl: 'modules/common/extension-overview.html',

       scope: {
         extObject: "="
       },

       link: function(scope, element, attrs) {
         scope.getValue = function(val) {
           if (val instanceof Array) {
             return val.join(", ");
           }
           return val;
         } 
       }
     }
  });
