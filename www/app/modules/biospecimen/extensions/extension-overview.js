angular.module("os.biospecimen.extensions")
  .directive("osExtensionOverview", function(ExtensionsUtil) {

     function processAttrs(formId, objectId, attrs) {
       angular.forEach(attrs, function(attr) {
         if (attr.type == 'subForm') {
           if (attr.value instanceof Array) {
             angular.forEach(attr.value, function(sfAttrs) {
               processAttrs(formId, objectId, sfAttrs);
             })
           }
         } else {
           if (attr.value instanceof Array) {
             attr.displayValue = attr.value.join(", ");
           } else {
             attr.displayValue = attr.value;
           }

           if (attr.type == 'fileUpload') {
             attr.fileDownloadUrl = ExtensionsUtil.getFileDownloadUrl(formId, objectId, attr.name);
           }
         }
       });
     }

     return {
       restrict: 'A',

       templateUrl: 'modules/biospecimen/extensions/extension-overview.html',

       scope: {
         extObject : "=",
         showColumn: "="
       },

       link: function(scope, element, attrs) {
         processAttrs(scope.extObject.formId, scope.extObject.objectId, scope.extObject.attrs);
       }
     }
  });
