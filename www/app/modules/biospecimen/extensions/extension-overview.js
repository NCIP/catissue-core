angular.module("os.biospecimen.extensions", [])
  .directive("osExtensionOverview", function() {
     return {
       restrict: 'A',

       templateUrl: 'modules/biospecimen/extensions/extension-overview.html',

       scope: {
         extObject: "=",
         showColumn: "="
       },

       link: function(scope, element, attrs) {
         for (var i = 0; i < scope.extObject.attrs.length; i++) {
           var attr = scope.extObject.attrs[i];
           if (attr.value instanceof Array) {
             attr.value = attr.value.join(", ");
           }
         }
       }
     }
  });
